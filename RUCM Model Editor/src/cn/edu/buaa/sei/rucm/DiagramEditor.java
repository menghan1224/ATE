package cn.edu.buaa.sei.rucm;

import java.awt.Color;
import java.awt.Container;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import ca.carleton.sce.squall.ucmeta.UCDLink;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import ca.carleton.sce.squall.ucmeta.UCDiagram;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Observer;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.rucm.SnailLMFEditorBase.SnailEditorView;
import cn.edu.buaa.sei.rucm.diagram.DiagramSelection;
import cn.edu.buaa.sei.rucm.diagram.DiagramView;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.LinkMapping;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.NodeMapping;
import cn.edu.buaa.sei.rucm.diagram.widgets.Scrollbar;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.Animation;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Vector2D;

public class DiagramEditor extends SnailLMFEditorBase implements ISelectionProvider {
	
	private ViewContext viewContext;
	private DiagramView diagramView;
	private RootView rootView;
	
	private final Observer nameObserver = new Observer() {
		@Override
		public void notifyChanged(ManagedObject target, String key, ManagedObject value) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					setPartName(getEditorInput().getName());
				}
			});
		}
	};

	public DiagramEditor() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		if (input instanceof DiagramEditorInput) {
			getSite().setSelectionProvider(this);
			setPartName(getEditorInput().getName());
			getEditorInput().getDiagram().addObserver(UCDiagram.KEY_NAME, nameObserver);
			setTitleImage(RUCMPlugin.getBundleImage("icons/elements_obj.gif"));
		} else {
			throw new PartInitException("Diagram editor only accepts DiagramEditorInput.");
		}
	}
	
	@Override
	public DiagramEditorInput getEditorInput() {
		return (DiagramEditorInput) super.getEditorInput();
	}

	@Override
	protected ViewContext createPartControl(Container container) {
		diagramView = new DiagramView(this);
		viewContext = new ViewContext(container, rootView = new RootView(diagramView));
		diagramView.requestKeyboardFocus();
		return viewContext;
	}

	@Override
	public void dispose() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				diagramView.dispose();
				viewContext.dispose();
				viewContext = null;
			}
		});
		getEditorInput().getDiagram().removeObserver(UCDiagram.KEY_NAME, nameObserver);
		getSite().setSelectionProvider(null);
		super.dispose();
	}
	
	//// Selection Provider ////
	
	private final List<ISelectionChangedListener> selectionChangedListeners = new Vector<ISelectionChangedListener>();

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	@Override
	public ISelection getSelection() {
		DiagramSelection ds = diagramView.getSelection();
		ObjectNode node = null;
		switch (ds.getType()) {
		case NODE:
			node = new ObjectNode(getNotatedObject(ds.getNode()));
			return new StructuredSelection(node);
		case LINK:
			node = new ObjectNode(getNotatedObject(ds.getLink()));
			return new StructuredSelection(node);
		default:
			return new StructuredSelection();
		}
	}
	
	private ManagedObject getNotatedObject(UCDNode node) {
		NodeMapping mapping = NotationRegistry.getNodeMapping(node.type());
		if (mapping != null && mapping.getTargetAttribute() != null) {
			ManagedObject obj = node.get(mapping.getTargetAttribute().getName());
			if (obj != null) return obj;
			else return node;
		} else {
			return node;
		}
	}
	
	private ManagedObject getNotatedObject(UCDLink link) {
		LinkMapping mapping = NotationRegistry.getLinkMapping(link.type());
		if (mapping != null && mapping.getTargetAttribute() != null) {
			ManagedObject obj = link.get(mapping.getTargetAttribute().getName());
			if (obj != null) return obj;
			else return link;
		} else {
			return link;
		}
	}
	
	@Override
	public void setSelection(ISelection selection) {
		final SelectionChangedEvent event = new SelectionChangedEvent(this, selection);
		if (!getComposite().isDisposed()) {
			getComposite().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					for (ISelectionChangedListener listener : selectionChangedListeners) {
						listener.selectionChanged(event);
					}
				}
			});
		}
	}

	@Override
	public SnailEditorView getSnailView() {
		return rootView;
	}
	
}

class RootView extends SnailEditorView {
	
	private final DiagramView diagramView;
	private final Scrollbar hBar = new Scrollbar.Horizontal();
	private final Scrollbar vBar = new Scrollbar.Vertical();
	
	private final Vector2D diagramPreferredSize = Vector2D.make();
	
	private DiagramTipView tipView;
	
	RootView(final DiagramView diagramView) {
		this.diagramView = diagramView;
		setBackgroundColor(Color.WHITE);
		setPaintMode(PaintMode.DIRECTLY);
		addSubview(diagramView);
		diagramView.addEventHandler(DiagramView.DIAGRAM_PREFERRED_SIZE_CHANGED, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				Vector2D size = (Vector2D) arg;
				diagramPreferredSize.set(size);
				RootView.this.layout();
			}
		});
		
		addSubview(hBar);
		addSubview(vBar);
		diagramPreferredSize.set(diagramView.getPreferredSize());
		layout();
		
		hBar.addEventHandler(Scrollbar.SLIDER_DRAGGED, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				int offset = hBar.getWindowOffset();
				diagramView.setLeft(-offset);
				RootView.this.layout();
			}
		});
		
		vBar.addEventHandler(Scrollbar.SLIDER_DRAGGED, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				int offset = vBar.getWindowOffset();
				diagramView.setTop(-offset);
				RootView.this.layout();
			}
		});
		
		// tip view
		if (diagramView.getModel().getNodes().size() == 0) {
			tipView = new DiagramTipView() {
				@Override
				void closeButtonClicked() {
					new Animation(0.2f) {
						@Override
						protected void animate(float progress) {
							setAlpha(1.0f - progress);
						}
						@Override
						protected void completed(boolean canceled) {
							tipView = null;
							removeFromSuperView();
						}
					}.commit();
				}
			};
			tipView.setSize(tipView.getPreferredSize());
			addSubview(tipView);
		} else {
			tipView = null;
		}
	}
	
	@Override
	protected void layoutView() {
		// determine diagram size
		Vector2D size = Vector2D.subtract(getSize(), diagramView.getPosition());
		if (size.x < diagramPreferredSize.x) size.x = diagramPreferredSize.x;
		if (size.y < diagramPreferredSize.y) size.y = diagramPreferredSize.y;
		diagramView.setSize(size);
		
		// update scroll bar
		hBar.setContentSize(size.x);
		vBar.setContentSize(size.y);
		hBar.setWindowSize(getWidth());
		vBar.setWindowSize(getHeight());
		hBar.setWindowOffset(-diagramView.getLeft());
		vBar.setWindowOffset(-diagramView.getTop());
		
		hBar.setSize(getWidth() - 22, 20);
		View.putViewWithLeftAndBottom(hBar, 2, 0);
		vBar.setSize(20, getHeight() - 22);
		View.putViewWithRightAndTop(vBar, 0, 2);
		
		// tip view
		if (tipView != null) {
			View.putViewWithHorizontalCenterAndBottom(tipView, getWidth() / 2, 32);
		}
	}

	@Override
	public View getPrintableContent() {
		return diagramView;
	}
	
}

class DiagramTipView extends View {
	
	DiagramTipView() {
		setBackgroundColor(null);
		setPaintMode(PaintMode.DIRECTLY);
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		g.drawImage(RUCMPluginResource.getImage("diagram_tip.png"), 0, 0);
	}
	
	@Override
	protected void mouseClicked(MouseEvent e) {
		Vector2D p = e.getPosition(this);
		if (p.x > getWidth() - 32 && p.y < 32) {
			closeButtonClicked();
		}
		e.handle();
	}
	
	void closeButtonClicked() {
	}
	
	@Override
	public int getPreferredWidth() {
		return 305;
	}
	
	@Override
	public int getPreferredHeight() {
		return 159;
	}
	
}
