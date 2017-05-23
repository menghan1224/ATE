package cn.edu.buaa.sei.rucm.menus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;

import co.gongzh.snail.util.Vector2D;

import ca.carleton.sce.squall.ucmeta.Actor;
import ca.carleton.sce.squall.ucmeta.Extend;
import ca.carleton.sce.squall.ucmeta.Generalization;
import ca.carleton.sce.squall.ucmeta.Include;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import ca.carleton.sce.squall.ucmeta.Package;
import ca.carleton.sce.squall.ucmeta.Relationship;
import ca.carleton.sce.squall.ucmeta.UCDActorNode;
import ca.carleton.sce.squall.ucmeta.UCDContainer;
import ca.carleton.sce.squall.ucmeta.UCDExtendLink;
import ca.carleton.sce.squall.ucmeta.UCDFactory;
import ca.carleton.sce.squall.ucmeta.UCDGeneralizationLink;
import ca.carleton.sce.squall.ucmeta.UCDIncludeLink;
import ca.carleton.sce.squall.ucmeta.UCDLink;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import ca.carleton.sce.squall.ucmeta.UCDPackage;
import ca.carleton.sce.squall.ucmeta.UCDRelationshipLink;
import ca.carleton.sce.squall.ucmeta.UCDUseCaseNode;
import ca.carleton.sce.squall.ucmeta.UCDiagram;
import ca.carleton.sce.squall.ucmeta.UCModel;
import ca.carleton.sce.squall.ucmeta.UseCase;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.edit.MenuContributor;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class GenerateDiagram extends ContributionItem implements MenuContributor {

	private UCModel ucModel;
	private Map<ModelElement, UCDNode> nodeTrace = new HashMap<ModelElement, UCDNode>();
	
	public GenerateDiagram() {
		ucModel = null;
	}
	
	@Override
	public void fill(Menu menu, int index) {
		// build delete menu
		MenuItem menuItem = new MenuItem(menu, SWT.NONE, index);
		menuItem.setText("Generate Diagram");
		menuItem.setImage(RUCMPlugin.getBundleImage("icons/elements_obj.gif"));
		if (ucModel != null) {
			// build menu
			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					generateDiagram(ucModel);
				}
			});
		} else {
			menuItem.setEnabled(false);
		}
	}
	
	@Override
	public IContributionItem[] createMenuItems() {
		ObjectNode selectedNode = null;
		ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) sel).getFirstElement();
			if (obj instanceof ObjectNode) {
				selectedNode = (ObjectNode) obj;
				ManagedObject object = selectedNode.getObject();
				if (object instanceof UCModel) {
					ucModel = (UCModel) object;
					return new IContributionItem[] { this };
				}
			}
		}
		return new IContributionItem[0];
	}
	
	private UCDUseCaseNode generateUseCase(UseCase usecase) {
		UCDUseCaseNode node = UCDFactory.createUCDUseCaseNode();
		node.setUseCase(usecase);
		nodeTrace.put(usecase, node);
		return node;
	}
	
	private UCDActorNode generateActor(Actor actor) {
		UCDActorNode node = UCDFactory.createUCDActorNode();
		node.setActor(actor);
		nodeTrace.put(actor, node);
		return node;
	}
	
	private UCDPackage generatePackage(Package model) {
		UCDPackage node = UCDFactory.createUCDPackage();
		node.setPackage(model);
		nodeTrace.put(model, node);
		generateSubNodes(node, model.getModelElements());
		return node;
	}

	private void generateSubNodes(UCDContainer container, List<ModelElement> elements) {
		List<UCDNode> nodes = container.getNodes();
		// add sub nodes
		for (ModelElement el : elements) {
			UCDNode n = null;
			if (el instanceof UseCase) {
				n = generateUseCase((UseCase) el);
			} else if (el instanceof Actor) {
				n = generateActor((Actor) el);
			} else if (el instanceof Package) {
				n = generatePackage((Package) el);
			}
			if (n != null) nodes.add(n);
		}
	}
	
	private synchronized void generateDiagram(UCModel ucModel) {
		nodeTrace.clear();
		UCDiagram diagram = UCDFactory.createUCDiagram();
		diagram.setUcModel(ucModel);
		nodeTrace.put(ucModel, diagram);
		// generate sub-nodes
		generateSubNodes(diagram, ucModel.getModelElements());
		// generate links
		generateLinks(diagram.getLinks(), ucModel.getModelElements());
		nodeTrace.clear();
		// layout
		layoutNodes(diagram.getNodes());
		ucModel.getDiagrams().add(diagram);
	}
	
	private void generateLinks(List<UCDLink> links, List<ModelElement> elements) {
		for (ModelElement el : elements) {
			if (el instanceof Relationship) {
				Relationship rel = (Relationship) el;
				UCDNode node1 = nodeTrace.get(rel.getActor());
				UCDNode node2 = nodeTrace.get(rel.getUseCase());
				if (node1 != null && node2 != null) {
					UCDRelationshipLink link = UCDFactory.createUCDRelationshipLink();
					link.setRelationship(rel);
					link.setNode1(node1);
					link.setNode2(node2);
					links.add(link);
				}
			} else if (el instanceof Generalization) {
				Generalization gen = (Generalization) el;
				UCDNode node1 = nodeTrace.get(gen.getSpecific());
				UCDNode node2 = nodeTrace.get(gen.getGeneral());
				if (node1 != null && node2 != null) {
					UCDGeneralizationLink link = UCDFactory.createUCDGeneralizationLink();
					link.setGeneralization(gen);
					link.setNode1(node1);
					link.setNode2(node2);
					links.add(link);
				}
			} else if (el instanceof UseCase) {
				UseCase useCase = (UseCase) el;
				for (Include include : useCase.getInclude()) {
					UCDNode node1 = nodeTrace.get(include.getIncludingCase());
					UCDNode node2 = nodeTrace.get(include.getAddition());
					if (node1 != null && node2 != null) {
						UCDIncludeLink link = UCDFactory.createUCDIncludeLink();
						link.setInclude(include);
						link.setNode1(node1);
						link.setNode2(node2);
						links.add(link);
					}
				}
				for (Extend extend : useCase.getExtend()) {
					UCDNode node1 = nodeTrace.get(extend.getExtension());
					UCDNode node2 = nodeTrace.get(extend.getExtendedCase());
					if (node1 != null && node2 != null) {
						UCDExtendLink link = UCDFactory.createUCDExtendLink();
						link.setExtend(extend);
						link.setNode1(node1);
						link.setNode2(node2);
						links.add(link);
					}
				}
			} else if (el instanceof Package) {
				generateLinks(links, ((Package) el).getModelElements());
			}
		}
	}
	
	private Vector2D layoutNodes(List<UCDNode> nodes) {
		// resize
		for (UCDNode node : nodes) {
			if (node instanceof UCDUseCaseNode) {
				node.setWidth(140);
				node.setHeight(60);
			} else if (node instanceof UCDActorNode) {
				node.setWidth(100);
				node.setHeight(100);
			} else if (node instanceof UCDPackage) {
				Vector2D size = layoutNodes(((UCDPackage) node).getNodes());
				node.setWidth(size.x);
				node.setHeight(size.y);
			}
		}
		// relocate
		final int width_limit = 800;
		final int margin = 30;
		int cur_top = margin, cur_left = margin, max_height = 0, max_width = 0;
		int count = 0;
		for (UCDNode node : nodes) {
			if (count > 0 && cur_left + node.getWidth() > width_limit) {
				// new line
				cur_left = margin;
				cur_top += max_height + margin;
				max_height = 0;
				count = 0;
			}
			node.setLeft(cur_left);
			cur_left += node.getWidth() + margin;
			if (cur_left > max_width) max_width = cur_left;
			node.setTop(cur_top);
			if (node.getHeight() > max_height) max_height = node.getHeight();
			count++;
		}
		return Vector2D.make(max_width, cur_top + max_height + margin);
	}
	
}
