package cn.edu.buaa.sei.rucm.diagram.menus;

import java.awt.geom.AffineTransform;

import javax.swing.SwingUtilities;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import ca.carleton.sce.squall.ucmeta.UCDNode;
import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.editor.property.ReferenceDialog;
import cn.edu.buaa.sei.rucm.diagram.ContainerView;
import cn.edu.buaa.sei.rucm.diagram.ContextMenu;
import cn.edu.buaa.sei.rucm.diagram.DiagramView;
import cn.edu.buaa.sei.rucm.diagram.NodeView;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.NodeMapping;
import cn.edu.buaa.sei.rucm.diagram.widgets.MenuItems.LabelItem;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.Animation;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.util.Vector2D;

public class CreateNode extends LabelItem {

	private final ContainerView<?> container;
	private final NodeMapping mapping;
	private final Vector2D location;
	
	public CreateNode(ContainerView<?> container, NodeMapping mapping, Vector2D location) {
		super(RUCMPluginResource.getImage("add_obj.gif"), String.format("Create %s", mapping.getDisplayName()));
		this.container = container;
		this.mapping = mapping;
		this.location = location.clone();
	}
	
	@Override
	protected void mouseClicked(MouseEvent e) {
		final boolean option = e.getAWTEvent().isAltDown();
		final Type nodeType = mapping.getNodeType();
		final Attribute targetAttr = mapping.getTargetAttribute();
		
		ContextMenu menu = getSuperViewInHierarchy(ContextMenu.class);
		final DiagramView diagramView = menu.getDiagramView();
		
		// create notation
		final UCDNode node = (UCDNode) LMFContext.newInstance(nodeType);
		container.getModel().getNodes().add(node); // add notation into container
		
		// retrieve the node view
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// create element
				final NodeView<?> nodeView = diagramView.getNodeView(node);
				nodeView.nodeViewCreated(container);
				
				if (targetAttr != null) {
					if (!option) {
						// create a new element
						ManagedObject obj = LMFContext.newInstance(targetAttr.getValueType());
						
						// add new element to model
						nodeView.addElementToContainer(container, obj); // this method should store the object to an owner
						
						// set to node
						if (obj.owner() != null) {
							node.set(targetAttr.getName(), obj);
						} else {
							throw new IllegalStateException();
						}
					} else {
						// select an existing element
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								ReferenceDialog dialog = new ReferenceDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
								dialog.init(targetAttr.getValueType(), diagramView.getUCModel());
								dialog.open();
								ManagedObject obj = dialog.getValue();
								if (obj != null) {
									node.set(targetAttr.getName(), obj);
								}
							}
						});
					}
				}
				
				final Vector2D size = nodeView.getPreferredSize();
				node.setWidth(size.x);
				node.setHeight(size.y);
				Vector2D loc = Vector2D.subtract(location, Vector2D.multiplied(size, 0.5));
				node.setLeft(loc.x);
				node.setTop(loc.y);
				
				new Animation(0.2f, Animation.EaseOut) {
					@Override
					protected void animate(float progress) {
						AffineTransform tr = AffineTransform.getTranslateInstance(size.x / 2, size.y / 2);
						float scale = (1 - 0.2f) * progress + 0.2f;
						tr.scale(scale, scale);
						tr.translate(-size.x / 2, -size.y / 2);
						nodeView.setTransform(tr);
						nodeView.setAlpha(progress);
					}
					@Override
					protected void completed(boolean canceled) {
						nodeView.setTransform(null);
						nodeView.setAlpha(1.0f);
					}
				}.commit();
			}
		});
		
		e.handle();
		menu.dismiss();
	}
	
}
