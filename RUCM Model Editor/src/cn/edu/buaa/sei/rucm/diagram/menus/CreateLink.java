  package cn.edu.buaa.sei.rucm.diagram.menus;

import javax.swing.SwingUtilities;

import ca.carleton.sce.squall.ucmeta.UCDLink;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.rucm.diagram.ContainerView;
import cn.edu.buaa.sei.rucm.diagram.ContextMenu;
import cn.edu.buaa.sei.rucm.diagram.DiagramView;
import cn.edu.buaa.sei.rucm.diagram.LinkLayer;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.LinkMapping;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.NodeMapping;
import cn.edu.buaa.sei.rucm.diagram.widgets.MenuItems.LabelItem;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.MouseEvent;

public class CreateLink extends LabelItem {

	private final ContainerView<?> container;
	private final LinkMapping mapping;
	private final UCDNode node1;
	private final UCDNode node2;
	
	public CreateLink(ContainerView<?> container, LinkMapping mapping, UCDNode node1, UCDNode node2) {
		super(RUCMPluginResource.getImage("add_obj.gif"), String.format("Create %s", mapping.getDisplayName()));
		this.mapping = mapping;
		this.node1 = node1;
		this.node2 = node2;
		this.container = container;
	}
	
	@Override
	protected void mouseClicked(MouseEvent e) {
		final Type linkType = mapping.getLinkType();
		final Attribute targetAttr = mapping.getTargetAttribute();
		final Attribute el1Attr = mapping.getElement1Attribute();
		final Attribute el2Attr = mapping.getElement2Attribute();
		
		ContextMenu menu = getSuperViewInHierarchy(ContextMenu.class);
		final DiagramView diagramView = menu.getDiagramView();
		
		// create notation
		final UCDLink link = (UCDLink) LMFContext.newInstance(linkType);
		link.setNode1(node1);
		link.setNode2(node2);
		diagramView.getModel().getLinks().add(link);
		
		// retrieve the node view
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// create element
				final LinkLayer<?> linkLayer = diagramView.getLinkLayer(link);
				linkLayer.linkLayerCreated(container, node1, node2);
				
				if (targetAttr != null) {
					NodeMapping nm1 = NotationRegistry.getNodeMapping(node1.type());
					NodeMapping nm2 = NotationRegistry.getNodeMapping(node2.type());
					
					ManagedObject obj = LMFContext.newInstance(targetAttr.getValueType());
					ManagedObject value1 = node1.get(nm1.getTargetAttribute().getName());
					ManagedObject value2 = node2.get(nm2.getTargetAttribute().getName());
					if (value1.type().isOrIsSubtypeOf(el1Attr.getValueType())) {
						obj.set(el1Attr.getName(), value1);
						obj.set(el2Attr.getName(), value2);
					} else {
						// indirected link
						obj.set(el1Attr.getName(), value2);
						obj.set(el2Attr.getName(), value1);
					}
					
					linkLayer.addElementToContainer(container, node1, node2, obj);
					
					// set to link
					if (obj.owner() != null) {
						link.set(targetAttr.getName(), obj);
					} else {
						throw new IllegalStateException();
					}	
				}
				
			}
		});
		
		e.handle();
		menu.dismiss();
	}
	
}
