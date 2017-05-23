package cn.edu.buaa.sei.rucm.diagram.menus;

import ca.carleton.sce.squall.ucmeta.UCDLink;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.rucm.diagram.ContextMenu;
import cn.edu.buaa.sei.rucm.diagram.DiagramSelection;
import cn.edu.buaa.sei.rucm.diagram.DiagramView;
import cn.edu.buaa.sei.rucm.diagram.NodeView;
import cn.edu.buaa.sei.rucm.diagram.widgets.MenuItems.LabelItem;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.MouseEvent;

public class DeleteFromDiagram extends LabelItem {
	
	public DeleteFromDiagram() {
		super(RUCMPluginResource.getImage("delete_obj.gif"), "Delete from Diagram");
	}
	
	@Override
	protected void mouseClicked(MouseEvent e) {
		ContextMenu menu = getSuperViewInHierarchy(ContextMenu.class);
		DiagramView diagramView = menu.getDiagramView();
		DiagramSelection sel = diagramView.getSelection();
		LMFResource resource = diagramView.getModel().resource();
		switch (sel.getType()) {
		case LINK:
			// remove link
			diagramView.getLinkLayer(sel.getLink()).linkLayerDeleted(diagramView);
			LMFUtility.removeObject(resource.getRootObject(), sel.getLink());
			break;
			
		case NODE:
		case MULTIPLE_NODES:
			for (UCDNode node : sel.getNodes()) {
				// remove links
				for (UCDLink link : diagramView.getModel().getLinks().toArray(new UCDLink[0])) {
					if (link.getNode1() == node || link.getNode2() == node) {
						diagramView.getLinkLayer(link).linkLayerDeleted(diagramView);
						LMFUtility.removeObject(resource.getRootObject(), link);
					}
				}
				// remove node
				NodeView<?> nodeView = diagramView.getNodeViewRecursively(node);
				nodeView.nodeViewDeleted(nodeView.getContainerView());
				LMFUtility.removeObject(resource.getRootObject(), node);
			}
			break;
		default:
			break;
		}
		e.handle();
		menu.dismiss();
	}
	
}
