package cn.edu.buaa.sei.rucm.diagram;

import java.util.Collection;

import ca.carleton.sce.squall.ucmeta.UCDLink;
import ca.carleton.sce.squall.ucmeta.UCDNode;

public final class DiagramSelection {
	
	public static final DiagramSelection EMPTY_SELECTION = new DiagramSelection();
	
	public static enum SelectionType {
		EMPTY, NODE, LINK, MULTIPLE_NODES
	}
	
	private final SelectionType type;
	private final UCDNode[] nodes;
	private final UCDLink link;
	
	public DiagramSelection() {
		this.type = SelectionType.EMPTY;
		this.nodes = new UCDNode[] { null };
		this.link = null;
	}
	
	public DiagramSelection(UCDNode node) {
		if (node == null) {
			this.type = SelectionType.EMPTY;
			this.nodes = new UCDNode[] { null };
			this.link = null;
		} else {
			this.type = SelectionType.NODE;
			this.nodes = new UCDNode[] { node };
			this.link = null;
		}
	}
	
	public DiagramSelection(Collection<? extends UCDNode> nodes) {
		if (nodes == null || nodes.size() == 0) {
			this.type = SelectionType.EMPTY;
			this.nodes = new UCDNode[] { null };
			this.link = null;
		} else if (nodes.size() == 1) {
			this.type = SelectionType.NODE;
			this.nodes = nodes.toArray(new UCDNode[1]);
			this.link = null;
		} else {
			this.type = SelectionType.MULTIPLE_NODES;
			this.nodes = nodes.toArray(new UCDNode[0]);
			this.link = null;
		}
	}
	
	public DiagramSelection(UCDLink link) {
		if (link == null) {
			this.type = SelectionType.EMPTY;
			this.nodes = new UCDNode[] { null };
			this.link = null;
		} else {
			this.type = SelectionType.LINK;
			this.nodes = new UCDNode[] { null };
			this.link = link;
		}
	}
	
	public UCDLink getLink() {
		return link;
	}
	
	public UCDNode getNode() {
		return nodes[0];
	}
	
	public UCDNode[] getNodes() {
		return nodes.clone();
	}
	
	public SelectionType getType() {
		return type;
	}

}
