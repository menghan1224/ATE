package cn.edu.buaa.sei.lmf.edit;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.graphics.Image;

public abstract class Node {
	
	private final TreeModel tree;
	private final Node parent;
	private List<Node> children;
	
	public Node(TreeModel tree, Node parent) {
		this.tree = tree;
		this.parent = parent;
		this.children = null;
	}
	
	public final TreeModel getTree() {
		return tree;
	}
	
	public final Node getParent() {
		return parent;
	}
	
	protected final void notifyLabelChanged() {
		if (tree != null) tree.notifyLabelChanged(this);
	}
	
	protected final void notifyStructureChanged() {
		if (children != null) {
			for (Node child : children) {
				child.dispose();
			}
			children.clear();
			children = null;
		}
		if (tree != null) tree.notifyStructureChanged(this);
	}
	
	public final List<Node> getChildren() {
		if (children == null) {
			children = createChildren();
		}
		return Collections.unmodifiableList(children);
	}
	
	public final boolean hasChildren() {
		if (children == null) {
			children = createChildren();
		}
		return !children.isEmpty();
	}
	
	protected final void dispose() {
		if (children != null) {
			for (Node child : children) {
				child.dispose();
			}
			children.clear();
			children = null;
		}
		disposeNode();
	}
	
	//// Delegates ////
	
	public abstract Image getImage();
	public abstract String getText();
	protected abstract List<Node> createChildren();
	protected abstract void disposeNode();
		
}
