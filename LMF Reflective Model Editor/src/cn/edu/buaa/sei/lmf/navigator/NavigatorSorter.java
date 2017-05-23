package cn.edu.buaa.sei.lmf.navigator;

import java.text.Collator;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import cn.edu.buaa.sei.lmf.edit.ContainmentNode;
import cn.edu.buaa.sei.lmf.edit.Node;

public class NavigatorSorter extends ViewerSorter {

	public NavigatorSorter() {
	}

	public NavigatorSorter(Collator collator) {
		super(collator);
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1 instanceof Node && e2 instanceof Node) {
			Node node1 = (Node) e1;
			Node node2 = (Node) e2;
			if (node1.getParent() instanceof ContainmentNode) {
				ContainmentNode parent = (ContainmentNode) node1.getParent();
				List<Node> list = parent.getChildren();
				int index1 = list.indexOf(node1);
				int index2 = list.indexOf(node2);
				return index1 - index2;
			} else {
				return 0;
			}
		} else {
			return super.compare(viewer, e1, e2);
		}
	}

}
