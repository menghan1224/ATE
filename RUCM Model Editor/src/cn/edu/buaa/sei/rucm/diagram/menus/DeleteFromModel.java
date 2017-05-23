package cn.edu.buaa.sei.rucm.diagram.menus;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ca.carleton.sce.squall.ucmeta.UCDLink;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.ContextMenu;
import cn.edu.buaa.sei.rucm.diagram.DiagramSelection;
import cn.edu.buaa.sei.rucm.diagram.DiagramView;
import cn.edu.buaa.sei.rucm.diagram.NodeView;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry;
import cn.edu.buaa.sei.rucm.diagram.widgets.MenuItems.LabelItem;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.ViewContext;

public class DeleteFromModel extends LabelItem {
	
	public DeleteFromModel() {
		super(RUCMPluginResource.getImage("delete_obj.gif"), "Delete from Model");
	}
	
	@Override
	protected void mouseClicked(MouseEvent e) {
		ContextMenu menu = getSuperViewInHierarchy(ContextMenu.class);
		DiagramView diagramView = menu.getDiagramView();
		final ViewContext context = diagramView.getViewContext();
		DiagramSelection sel = diagramView.getSelection();
		LMFResource resource = diagramView.getModel().resource();
		e.handle();
		menu.dismiss();
		
		int reply = JOptionPane.showConfirmDialog(context.getSwingContainer(),
				"Delete this element from model?", "Delete", JOptionPane.OK_CANCEL_OPTION);
		if (reply == JOptionPane.OK_OPTION) {
			switch (sel.getType()) {
			case LINK:
				// remove link
				diagramView.getLinkLayer(sel.getLink()).linkLayerDeleted(diagramView);
				deleteLink(resource, sel.getLink());
				break;
				
			case NODE:
			case MULTIPLE_NODES:
				for (UCDNode node : sel.getNodes()) {
					// remove links
					for (UCDLink link : diagramView.getModel().getLinks().toArray(new UCDLink[0])) {
						if (link.getNode1() == node || link.getNode2() == node) {
							diagramView.getLinkLayer(link).linkLayerDeleted(diagramView);
							deleteLink(resource, link);
						}
					}
					// remove node
					NodeView<?> nodeView = diagramView.getNodeViewRecursively(node);
					nodeView.nodeViewDeleted(nodeView.getContainerView());
					deleteNode(resource, node);
				}
				break;
			default:
				break;
			}
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				context.getSwingContainer().requestFocus();
			}
		});
	}
	
	private void deleteLink(LMFResource resource, UCDLink link) {
		Attribute attribute = NotationRegistry.getLinkMapping(link.type()).getTargetAttribute();
		if (attribute != null) {
			ManagedObject el = link.get(attribute.getName());
			if (el != null) {
				LMFUtility.removeObject(resource.getRootObject(), el);
			}
		}
		LMFUtility.removeObject(resource.getRootObject(), link);
	}
	
	private void deleteNode(LMFResource resource, UCDNode node) {
		Attribute attribute = NotationRegistry.getNodeMapping(node.type()).getTargetAttribute();
		if (attribute != null) {
			ManagedObject el = node.get(attribute.getName());
			if (el != null) {
				LMFUtility.removeObject(resource.getRootObject(), el);
			}
		}
		LMFUtility.removeObject(resource.getRootObject(), node);
	}

}
