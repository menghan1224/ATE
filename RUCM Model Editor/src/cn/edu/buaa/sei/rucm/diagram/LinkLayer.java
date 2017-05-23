package cn.edu.buaa.sei.rucm.diagram;

import javax.swing.SwingUtilities;

import ca.carleton.sce.squall.ucmeta.UCDLink;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import ca.carleton.sce.squall.ucmeta.UCModel;
import cn.edu.buaa.sei.lmf.ManagedList;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Observer;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.NodeMapping;

import co.gongzh.snail.layer.Layer;

public abstract class LinkLayer<E extends UCDLink> extends Layer {

	private final E model;
	private DiagramView ownerDiagramView;
	private boolean selected;
	
	@SuppressWarnings("unchecked")
	public LinkLayer(ManagedObject model) {
		if (model == null) throw new IllegalArgumentException();
		this.model = (E) model; // ...
		this.model.addObserver(new String[] { UCDLink.KEY_NODE1, UCDLink.KEY_NODE2 }, nodeObserver);
		this.selected = false;
	}
	
	public final DiagramView getDiagramView() {
		return ownerDiagramView;
	}
	
	public final UCModel getUCModel() {
		return getDiagramView().getUCModel();
	}
	
	void setOwnerDiagramView(DiagramView ownerDiagramView) {
		this.ownerDiagramView = ownerDiagramView;
	}
	
	public final NodeView<?> getNodeView1() {
		return model.getNode1() == null ? null : ownerDiagramView.getNodeView(model.getNode1());
	}
	
	public final NodeView<?> getNodeView2() {
		return model.getNode2() == null ? null : ownerDiagramView.getNodeView(model.getNode2());
	}
	
	private final Observer nodeObserver = new Observer() {
		@Override
		public void notifyChanged(ManagedObject target, String key, ManagedObject value) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					setNeedsRepaint();
				}
			});
		}
	};
	
	public final E getModel() {
		return model;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	void setSelected(boolean selected) {
		if (this.selected != selected) {
			this.selected = selected;
			selectionStateChanged(selected);
		}
	}
	
	protected void selectionStateChanged(boolean selected) {
	}
	
	/**
	 * Client should not call this method.
	 * @param container
	 * @param element
	 */
	public void addElementToContainer(ContainerView<?> container, UCDNode node1, UCDNode node2, ManagedObject element) {
		// add new element to model
		NodeMapping cmap = NotationRegistry.getNodeMapping(container.getModel().type());
		ManagedObject cobj = container.getModel().get(cmap.getTargetAttribute().getName());
		ManagedList list = cobj.get(cmap.getContainerAttribute().getName()).listContent();
		list.add(element);
	}
	
	/**
	 * Client should not call this method.
	 * @param container
	 * @param node1
	 * @param node2
	 */
	public void linkLayerCreated(ContainerView<?> container, UCDNode node1, UCDNode node2) {
	}
	
	/**
	 * Client should not call this method.
	 * @param container
	 */
	public void linkLayerDeleted(ContainerView<?> container) {
	}
	
	public void dispose() {
		this.model.removeObserver(new String[] { UCDLink.KEY_NODE1, UCDLink.KEY_NODE2 }, nodeObserver);
	}
	
}
