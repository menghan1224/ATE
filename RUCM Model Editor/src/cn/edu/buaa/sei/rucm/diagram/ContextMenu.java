package cn.edu.buaa.sei.rucm.diagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.carleton.sce.squall.ucmeta.UCDContainer;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import ca.carleton.sce.squall.ucmeta.UCDUseCaseNode;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.LinkMapping;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.NodeMapping;
import cn.edu.buaa.sei.rucm.diagram.menus.CreateLink;
import cn.edu.buaa.sei.rucm.diagram.menus.CreateNode;
import cn.edu.buaa.sei.rucm.diagram.menus.DeleteFromDiagram;
import cn.edu.buaa.sei.rucm.diagram.menus.DeleteFromModel;
import cn.edu.buaa.sei.rucm.diagram.menus.ShowSpecification;
import cn.edu.buaa.sei.rucm.diagram.widgets.MenuItems;
import cn.edu.buaa.sei.rucm.diagram.widgets.PopupMenu;
import co.gongzh.snail.View;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Vector2D;


public class ContextMenu extends PopupMenu {
	
	private final DiagramView diagramView;
	public static final Key CONTEXT_MENU_DISMISSED = new Key("contextMenuDismissed", ContextMenu.class, null);
	
	public ContextMenu(DiagramView diagramView) {
		this.diagramView = diagramView;
	}
	
	public DiagramView getDiagramView() {
		return diagramView;
	}
	
	@Override
	protected void menuAboutToShow(Vector2D location) {
		removeAllSubviews();
		DiagramSelection sel = diagramView.getSelection();
		Vector2D diagramLocation = diagramView.transformPointFromRootView(location);
		switch (sel.getType()) {
		case NODE:
			if (sel.getNode() instanceof UCDUseCaseNode) {
				addSubview(new ShowSpecification());
				addSubview(new MenuItems.Separator());
			}
			if (checkContainerView(diagramView.getNodeView(sel.getNode()), diagramLocation)) {
				makeCreateNodeItems(diagramLocation);
				addSubview(new MenuItems.Separator());
			}
			addSubview(new DeleteFromDiagram());
			addSubview(new DeleteFromModel());
			break;
			
		case MULTIPLE_NODES:
			if (sel.getNodes().length == 2) {
				if (makeCreateLinkItems(sel.getNodes()) == 0) {
					addSubview(new MenuItems.DisabledLabelItem("No link can be created here."));
				}
			} else {
				addSubview(new DeleteFromDiagram());
				addSubview(new DeleteFromModel());
			}
			break;
			
		case LINK:
			addSubview(new DeleteFromDiagram());
			addSubview(new DeleteFromModel());
			break;
			
		case EMPTY:
			makeCreateNodeItems(diagramLocation);
			break;
		}
	}
	
	@Override
	protected void menuDismissed() {
		fireEvent(CONTEXT_MENU_DISMISSED, null);
	}
	
	private int makeCreateLinkItems(final UCDNode[] nodes) {
		Collection<LinkMapping> mappings = NotationRegistry.getLinkMappings(nodes[0].type(), nodes[1].type());
		NodeView<?> view1 = diagramView.getNodeView(nodes[0]);
		NodeView<?> view2 = diagramView.getNodeView(nodes[1]);
		ContainerView<?> container = getCommonContainerView(view1, view2);
		for (LinkMapping mapping : mappings) {
			if (mapping.isVisible()) {
				addSubview(new CreateLink(container, mapping, nodes[0], nodes[1]));
			}
		}
		return mappings.size();
	}
	
	private ContainerView<?> getCommonContainerView(NodeView<?> view1, NodeView<?> view2) {
		List<ContainerView<?>> cvs1 = new ArrayList<ContainerView<?>>();
		List<ContainerView<?>> cvs2 = new ArrayList<ContainerView<?>>();
		for (View v : view1.getViewHierarchy()) if (v instanceof ContainerView<?>) cvs1.add((ContainerView<?>) v);
		for (View v : view2.getViewHierarchy()) if (v instanceof ContainerView<?>) cvs2.add((ContainerView<?>) v);
		ContainerView<?> result = diagramView;
		for (int i = 1; i < Math.min(cvs1.size(), cvs2.size()); i++) {
			if (cvs1.get(i) == cvs2.get(i)) result = cvs1.get(i);
			else break;
		}
		return result;
	}
	
	/**
	 * The location is relative to diagram view.
	 * @param location
	 */
	private void makeCreateNodeItems(final Vector2D location) {
		ContainerView<?> container = getContainerViewAtPoint(location);
		Vector2D containerLocation = View.transformPoint(location, diagramView, container.getContentView());
		UCDContainer containerNode = container.getModel();
		Type conatinerChildNodeType = containerNode.get(UCDContainer.KEY_NODES).listContent().getContentType();
		Type targetModelType = null;
		NodeMapping cmap = NotationRegistry.getNodeMapping(containerNode.type());
		if (cmap != null &&
			cmap.getTargetAttribute() != null &&
			cmap.getContainerAttribute() != null) {
			ManagedObject cobj = container.getModel().get(cmap.getTargetAttribute().getName());
			targetModelType = cobj.get(cmap.getContainerAttribute().getName()).listContent().getContentType();
		}
		
		Collection<NodeMapping> mappings = NotationRegistry.getAllNodeMappings();
		for (NodeMapping mapping : mappings) {
			if (mapping.isVisible()) {
				// check if the node can be put into UCDContainer.node property
				if (!mapping.getNodeType().isOrIsSubtypeOf(conatinerChildNodeType)) continue;
				// check if the target element can be into target model
				if (targetModelType != null && mapping.getTargetAttribute() != null) {
					if (!mapping.getTargetAttribute().getValueType().isOrIsSubtypeOf(targetModelType)) continue;
				}
				addSubview(new CreateNode(container, mapping, containerLocation));
			}
		}
	}
	
	/**
	 * The location is relative to diagram view.
	 * @param location
	 */
	private boolean checkContainerView(NodeView<?> view, Vector2D location) {
		ContainerView<?> container = getContainerViewAtPoint(location);
		return container == view;
	}
	
	/**
	 * The location is relative to diagram view.
	 * @param location
	 */
	private ContainerView<?> getContainerViewAtPoint(Vector2D location) {
		ContainerView<?> container = null;
		View[] views = diagramView.getSubviewHierachyAtPoint(location);
		for (int i = views.length - 1; i >= 0; i--) {
			if (views[i] instanceof ContainerView<?>) {
				container = (ContainerView<?>) views[i];
				break;
			}
		}
		if (container == null) container = diagramView;
		return container;
	}
	
}
