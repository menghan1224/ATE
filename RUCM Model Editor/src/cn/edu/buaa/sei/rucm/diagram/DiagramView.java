package cn.edu.buaa.sei.rucm.diagram;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import ca.carleton.sce.squall.ucmeta.UCDLink;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import ca.carleton.sce.squall.ucmeta.UCDiagram;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ListObserver;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.OwnerObserver;
import cn.edu.buaa.sei.rucm.DiagramEditor;
import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.diagram.DiagramSelection.SelectionType;
import cn.edu.buaa.sei.rucm.diagram.SelectionLayers.SimpleBorder;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.layer.LayeredView;
import co.gongzh.snail.util.Vector2D;

public class DiagramView extends ContainerView<UCDiagram> {
	
	//// Instance Fields & Methods ////
	
	private final DiagramEditor editorPart;
	
	// Model
	private final Map<UCDLink, LinkLayer<?>> linkMap;
	
	// View Structure
	final LayeredView hudContainer;
	final LayeredView linkContainer;
	private final View nodeContainer;
	
	private final List<SimpleBorder> selectionBorders;
	
	// Selection
	private DiagramSelection selection;
	
	public DiagramView(DiagramEditor editorPart) {
		super(editorPart.getEditorInput().getDiagram(), new View());
		this.editorPart = editorPart;
		setBackgroundColor(null);
		setPaintMode(PaintMode.DIRECTLY);
		linkMap = new HashMap<UCDLink, LinkLayer<?>>();
		selection = DiagramSelection.EMPTY_SELECTION;
		
		// UI
		nodeContainer = getContentView();
		nodeContainer.setPaintMode(PaintMode.DISABLED);
		addSubview(nodeContainer);
		linkContainer = new LayeredView();
		addSubview(linkContainer);
		hudContainer = new LayeredView();
		addSubview(hudContainer);
		
		selectionBorders = new ArrayList<SimpleBorder>();
		
		new DiagramInteraction(this);
		
		// create links
		for (UCDLink link : getModel().getLinks()) {
			addLinkLayer(link);
		}
		// observer the links change
		getModel().addListObserver(UCDiagram.KEY_LINKS, linkListObserver);
	}
	
	@Override
	protected void layoutView() {
		View.scaleViewWithMarginToSuperView(hudContainer, 0);
		View.scaleViewWithMarginToSuperView(linkContainer, 0);
		View.scaleViewWithMarginToSuperView(nodeContainer, 0);
	}
	
	@Override
	public void dispose() {
		// dispose links
		setSelectionInternal(null); // clear selection
		for (LinkLayer<?> link : linkMap.values()) {
			linkContainer.removeLayer(link);
			link.dispose();
		}
		linkMap.clear();
		getModel().removeListObserver(UCDiagram.KEY_LINKS, linkListObserver);
		super.dispose();
	}
	
	private final ListObserver linkListObserver = new ListObserver() {
		@Override
		public void listChanged(ManagedObject target, String key, final ManagedObject[] added, final ManagedObject[] removed) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					for (ManagedObject link : removed) {
						removeLinkLayer((UCDLink) link);
					}
					for (ManagedObject link : added) {
						addLinkLayer((UCDLink) link);
					}
				}
			});
		}
	};
	
	private void addLinkLayer(UCDLink link) {
		Class<? extends LinkLayer<?>> clazz = NotationRegistry.getLinkLayerClass(link.type());
		if (clazz == null) {
			RUCMPlugin.logError("Missing implementation class for notation: " + link.type().getName(), true);
		}
		try {
			Constructor<? extends LinkLayer<?>> constructor = clazz.getConstructor(ManagedObject.class);
			LinkLayer<?> linkLayer = constructor.newInstance(link);
			linkLayer.setOwnerDiagramView(this);
			linkMap.put(link, linkLayer);
			linkContainer.addLayer(linkLayer);
		} catch (Exception ex) {
			RUCMPlugin.logError(ex, true);
		}
	}
	
	private void removeLinkLayer(UCDLink link) {
		LinkLayer<?> layer = linkMap.get(link);
		linkContainer.removeLayer(layer);
		layer.dispose();
		linkMap.remove(link);
	}
	
	public LinkLayer<?> getLinkLayer(UCDLink link) {
		return linkMap.get(link);
	}
	
	public NodeView<?> getNodeViewRecursively(UCDNode node) {
		return getNodeViewRecursively(this, node);
	}
	
	private static NodeView<?> getNodeViewRecursively(ContainerView<?> container, UCDNode node) {
		NodeView<?> rst = container.getNodeView(node);
		if (rst != null) return rst;
		else {
			for (NodeView<?> subnode : container.getNodeMap().values()) {
				if (subnode instanceof ContainerView<?>) {
					rst = getNodeViewRecursively((ContainerView<?>) subnode, node);
					if (rst != null) return rst;
				}
			}
		}
		return null;
	}
	
	public DiagramSelection getSelection() {
		return selection;
	}
	
	// XXX: setSelection externally?
	void setSelectionInternal(DiagramSelection selection) {
		
		// clear current selection
		switch (this.selection.getType()) {
		case NODE:
			this.selection.getNode().removeOwnerObserver(selectionOwnerObserver);
			getNodeView(this.selection.getNode()).setSelected(false);
			break;
		case MULTIPLE_NODES:
			for (UCDNode node : this.selection.getNodes()) {
				node.removeOwnerObserver(selectionOwnerObserver);
			}
			break;
		case LINK:
			this.selection.getLink().removeOwnerObserver(selectionOwnerObserver);
			getLinkLayer(this.selection.getLink()).setSelected(false);
			break;
		default:
		}
		for (SimpleBorder border : selectionBorders) {
			hudContainer.removeLayer(border);
		}
		selectionBorders.clear();
		
		// new selection
		if (selection == null) selection = DiagramSelection.EMPTY_SELECTION;
		this.selection = selection;
		
		// update selected note/layer
		switch (this.selection.getType()) {
		case NODE:
			{
				NodeView<?> nodeView = getNodeView(this.selection.getNode());
				nodeView.setSelected(true);
				nodeView.setIndex(nodeView.getSuperView().count() - 1); // move to top
				SimpleBorder border = new SelectionLayers.ResizableBorder(getNodeView(this.selection.getNode()));
				selectionBorders.add(border);
				hudContainer.addLayer(border);
				this.selection.getNode().addOwnerObserver(selectionOwnerObserver);
			}
			break;
		case MULTIPLE_NODES:
			{
				for (UCDNode node : this.selection.getNodes()) {
					NodeView<?> nodeView = getNodeView(node);
					nodeView.setIndex(nodeView.getSuperView().count() - 1);  // move to top
					SimpleBorder border = new SelectionLayers.SimpleBorder(getNodeView(node));
					selectionBorders.add(border);
					hudContainer.addLayer(border);
					node.addOwnerObserver(selectionOwnerObserver);
				}
			}
			break;
		case LINK:
			getLinkLayer(this.selection.getLink()).setSelected(true);
			this.selection.getLink().addOwnerObserver(selectionOwnerObserver);
			break;
		default:
		}
		
		editorPart.setSelection(editorPart.getSelection());
	}
	
	private final OwnerObserver selectionOwnerObserver = new OwnerObserver() {
		@Override
		public void ownerChanged(final ManagedObject target, ManagedObject owner) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// clear selection
					if (target instanceof UCDNode) {
						if (selection.getType() == SelectionType.NODE && selection.getNode() == target) {
							setSelectionInternal(null);
						} else if (selection.getType() == SelectionType.MULTIPLE_NODES) {
							List<UCDNode> nodes = new ArrayList<UCDNode>(Arrays.asList(selection.getNodes()));
							nodes.remove(target);
							setSelectionInternal(new DiagramSelection(nodes));
						}
					} else if (target instanceof UCDLink) {
						if (selection.getType() == SelectionType.LINK && selection.getLink() == target) {
							setSelectionInternal(null);
						}
					}
				}
			});
		}
	};
	
	@Override
	public int getPreferredHeight() {
		int max = 0;
		for (NodeView<?> nodeView : getNodeMap().values()) {
			int value = nodeView.getTop() + nodeView.getHeight();
			if (value > max) max = value;
		}
		return max + 40;
	}
	
	@Override
	public int getPreferredWidth() {
		int max = 0;
		for (NodeView<?> nodeView : getNodeMap().values()) {
			int value = nodeView.getLeft() + nodeView.getWidth();
			if (value > max) max = value;
		}
		return max + 40;
	}
	
	private int cachedPreferredWidth = 0;
	private int cachedPreferredHeight = 0;
	
	public static final Key DIAGRAM_PREFERRED_SIZE_CHANGED = new Key("diagramPreferredSizeChanged", DiagramView.class, Vector2D.class);
	
	@Override
	protected void notifyPotentialPreferredSizeChanged() {
		int width = getPreferredWidth();
		int height = getPreferredHeight();
		if (width != cachedPreferredWidth || height != cachedPreferredHeight) {
			cachedPreferredWidth = width;
			cachedPreferredHeight = height;
			fireEvent(DIAGRAM_PREFERRED_SIZE_CHANGED, Vector2D.make(width, height));
		}
	}
	
	protected final Map<UCDLink, LinkLayer<?>> getLinkMap() {
		return Collections.unmodifiableMap(linkMap);
	}
	
	//// Other Editor-Related Methods ////
	
	@Override
	protected void mouseReleased(MouseEvent e) {
		requestKeyboardFocus();
	}
	
	@Override
	protected void keyTyped(KeyEvent e) {
		// XXX: debug mode
		if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
			if (getViewContext().isDebugModeEnable()) {
				getViewContext().setDebugModeEnable(false);
			} else {
				getViewContext().setDebugModeEnable(true);
			}
		}
	}
	
	@Override
	protected void preKeyTyped(KeyEvent e) {
		// save resource
		if (e.isMetaOrCtrlDown() && (e.getKeyChar() == 's' || e.getKeyChar() == 'S')) {
			try {
				editorPart.getResource().save();
			} catch (LMFResourceException ex) {
				RUCMPlugin.logError(ex, true);
			}
			e.handle();
		}
		
		// select all
		if (e.isMetaDown() && (e.getKeyChar() == 'a' || e.getKeyChar() == 'A')) {
			setSelectionInternal(new DiagramSelection(getNodeMap().keySet()));
			e.handle();
		}
	}

	@Override
	protected void selectionStateChanged(boolean selected) {
	}
	
}
