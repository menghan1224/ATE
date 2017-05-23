package cn.edu.buaa.sei.rucm.diagram;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import ca.carleton.sce.squall.ucmeta.UCDContainer;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import cn.edu.buaa.sei.lmf.ListObserver;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public abstract class ContainerView<E extends UCDContainer> extends NodeView<E> {

	protected final View contentView;
	private final Map<UCDNode, NodeView<?>> nodeMap;
	
	public ContainerView(UCDContainer model, View contentView) {
		super(model);
		this.contentView = contentView;
		this.nodeMap = new HashMap<UCDNode, NodeView<?>>();
		
		// create nodes
		for (UCDNode node : getModel().getNodes()) {
			addNodeView(node);
		}
		// observer the nodes change
		getModel().addListObserver(UCDContainer.KEY_NODES, nodeListObserver);
	}
	
	public final View getContentView() {
		return contentView;
	}
	
	private final ListObserver nodeListObserver = new ListObserver() {
		@Override
		public void listChanged(ManagedObject target, String key, final ManagedObject[] added, final ManagedObject[] removed) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					for (ManagedObject node : removed) {
						removeNodeView((UCDNode) node);
					}
					for (ManagedObject node : added) {
						addNodeView((UCDNode) node);
					}
				}
			});
		}
	};
	
	private void addNodeView(UCDNode node) {
		Class<? extends NodeView<?>> clazz = NotationRegistry.getNodeViewClass(node.type());
		if (clazz == null) {
			RUCMPlugin.logError("Missing implementation class for notation: " + node.type().getName(), true);
		}
		try {
			Constructor<? extends NodeView<?>> constructor = clazz.getConstructor(ManagedObject.class);
			final NodeView<?> nodeView = constructor.newInstance(node);
			nodeMap.put(node, nodeView);
			getContentView().addSubview(nodeView);
			nodeView.addEventHandler(POSITION_CHANGED, nodeViewPositionAndSizeHandler);
			nodeView.addEventHandler(SIZE_CHANGED, nodeViewPositionAndSizeHandler);
			notifyPotentialPreferredSizeChanged();
		} catch (Exception ex) {
			RUCMPlugin.logError(ex, true);
		}
	}
	
	private void removeNodeView(UCDNode node) {
		final NodeView<?> nodeView = nodeMap.get(node);
		nodeView.removeEventHandler(POSITION_CHANGED, nodeViewPositionAndSizeHandler);
		nodeView.removeEventHandler(SIZE_CHANGED, nodeViewPositionAndSizeHandler);
		nodeView.removeFromSuperView();
		nodeView.dispose();
		nodeMap.remove(node);
	}
	
	private final EventHandler nodeViewPositionAndSizeHandler = new EventHandler() {
		@Override
		public void handle(View sender, Key key, Object arg) {
			notifyPotentialPreferredSizeChanged();
		}
	};
	
	protected void notifyPotentialPreferredSizeChanged() {
	}
	
	public NodeView<?> getNodeView(UCDNode node) {
		if (nodeMap.containsKey(node)) return nodeMap.get(node);
		else {
			for (NodeView<?> nodeView : nodeMap.values()) {
				if (nodeView instanceof ContainerView<?>) {
					ContainerView<?> containerView = (ContainerView<?>) nodeView;
					nodeView = containerView.getNodeView(node);
					if (nodeView != null) return nodeView;
				}
			}
			return null;
		}
	}
	
	protected final Map<UCDNode, NodeView<?>> getNodeMap() {
		return Collections.unmodifiableMap(nodeMap);
	}
	
	@Override
	public void dispose() {
		for (NodeView<?> node : nodeMap.values()) {
			node.removeFromSuperView();
			node.dispose();
		}
		nodeMap.clear();
		getModel().removeListObserver(UCDContainer.KEY_NODES, nodeListObserver);
		super.dispose();
	}

}
