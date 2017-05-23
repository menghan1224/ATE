package cn.edu.buaa.sei.rucm.diagram;

import javax.swing.SwingUtilities;

import org.eclipse.core.runtime.IAdaptable;

import ca.carleton.sce.squall.ucmeta.UCDNode;
import ca.carleton.sce.squall.ucmeta.UCModel;

import cn.edu.buaa.sei.lmf.ManagedList;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Observer;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.NodeMapping;

import co.gongzh.snail.View;
import co.gongzh.snail.util.Vector2D;

/**
 * Client should subclass this class to define a node notation in Diagram Editor.
 * <p>
 * Override {@link #repaintView(co.gongzh.snail.ViewGraphics)} method to define the appearance
 * of this node.
 * <p>
 * Override {@link #getPreferredWidth()} and {@link #getPreferredHeight()} to determine the preferred size
 * of this node.
 * 
 * @author Gong Zhang
 * 
 * @param <E> the concrete notation model type
 */
public abstract class NodeView<E extends UCDNode> extends View implements IAdaptable {

	private final E model;
	private boolean selected;
	
	private final Observer basicObserver = new Observer() {
		@Override
		public void notifyChanged(ManagedObject target, final String key, ManagedObject value) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (key.equals(UCDNode.KEY_LEFT)) {
						setLeft(model.getLeft());
					} else if (key.equals(UCDNode.KEY_TOP)) {
						setTop(model.getTop());
					} else if (key.equals(UCDNode.KEY_WIDTH)) {
						setWidth(model.getWidth());
					} else if (key.equals(UCDNode.KEY_HEIGHT)) {
						setHeight(model.getHeight());
					}
				}
			});
		}
	};
	
	@SuppressWarnings("unchecked")
	public NodeView(ManagedObject model) {
		if (model == null) throw new IllegalArgumentException();
		this.model = (E) model; // ...
		setPosition(this.model.getLeft(), this.model.getTop());
		setSize(this.model.getWidth(), this.model.getHeight());
		this.model.addObserver(new String[] {
			UCDNode.KEY_LEFT,
			UCDNode.KEY_TOP,
			UCDNode.KEY_WIDTH,
			UCDNode.KEY_HEIGHT
		}, basicObserver);
		this.selected = false;
	}
	
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
	public void addElementToContainer(ContainerView<?> container, ManagedObject element) {
		// add new element to model
		NodeMapping cmap = NotationRegistry.getNodeMapping(container.getModel().type());
		ManagedObject cobj = container.getModel().get(cmap.getTargetAttribute().getName());
		ManagedList list = cobj.get(cmap.getContainerAttribute().getName()).listContent();
		list.add(element);
	}
	
	/**
	 * Client should not call this method.
	 * @param container
	 */
	public void nodeViewCreated(ContainerView<?> container) {
	}
	
	/**
	 * Client should not call this method.
	 * @param container
	 */
	public void nodeViewDeleted(ContainerView<?> container) {
	}
	
	protected Vector2D normalizeSize(Vector2D size) {
		size = size.clone();
		if (size.x <= 0) size.x = 1;
		if (size.y <= 0) size.y = 1;
		return size;
	}
	
	public Vector2D getLinkPoint(Vector2D dst) {
		Vector2D src = Vector2D.make();
		dst = dst.clone();
		dst.x -= getWidth() / 2;
		dst.y -= getHeight() / 2;
		
		if (dst.x == 0) {
			if (0 >= dst.y) src.y -= getHeight() / 2;
			else src.y += getHeight() / 2;
		} else {
			final float k = dst.y / (float) dst.x;
			final float k0 = getHeight() / (float) getWidth();

			if (Math.abs(k) < k0) {
				if (0 >= dst.x) src.x -= getWidth() / 2;
				else src.x += getWidth() / 2;
				src.y = (int) (k * src.x);
			} else {
				if (0 >= dst.y) src.y -= getHeight() / 2;
				else src.y += getHeight() / 2;
				src.x = (int) (src.y / k);
			}
		}
		
		src.x += getWidth() / 2;
		src.y += getHeight() / 2;
		return src;
	}
	
	public final DiagramView getDiagramView() {
		return this instanceof DiagramView ? (DiagramView) this : getSuperViewInHierarchy(DiagramView.class);
	}
	
	public final ContainerView<?> getContainerView() {
		return getSuperViewInHierarchy(ContainerView.class);
	}
	
	public final UCModel getUCModel() {
		return getDiagramView().getModel().getUcModel();
	}
	
	public final Vector2D getPositionInDiagramView() {
		return View.transformPoint(Vector2D.make(), this, getDiagramView());
	}
	
	public void dispose() {
		this.model.removeObserver(new String[] {
			UCDNode.KEY_LEFT,
			UCDNode.KEY_TOP,
			UCDNode.KEY_WIDTH,
			UCDNode.KEY_HEIGHT
		}, basicObserver);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ManagedObject.class) {
			return getModel();
		} else {
			return null;
		}
	}
	
}
