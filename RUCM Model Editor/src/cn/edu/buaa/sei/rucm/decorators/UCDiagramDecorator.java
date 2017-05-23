package cn.edu.buaa.sei.rucm.decorators;

import org.eclipse.swt.graphics.Image;

import ca.carleton.sce.squall.ucmeta.UCDiagram;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.NodeDecorator;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class UCDiagramDecorator implements NodeDecorator {

	public UCDiagramDecorator() {
	}

	@Override
	public String[] getLabelRelatedKeys() {
		return new String[] { UCDiagram.KEY_NAME };
	}

	@Override
	public String getLabel(ManagedObject object) {
		ManagedObject nameObject = object.get(UCDiagram.KEY_NAME);
		if (nameObject != null && !nameObject.stringValue().isEmpty()) {
			return nameObject.stringValue();
		} else {
			return "Use Case Diagram (Untitled)";
		}
	}

	@Override
	public Image getImage(ManagedObject object) {
		return RUCMPlugin.getBundleImage("icons/elements_obj.gif");
	}

	@Override
	public Image getImage(Type type) {
		return RUCMPlugin.getBundleImage("icons/elements_obj.gif");
	}

}
