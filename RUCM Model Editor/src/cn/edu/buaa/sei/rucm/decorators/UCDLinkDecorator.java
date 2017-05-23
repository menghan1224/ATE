package cn.edu.buaa.sei.rucm.decorators;

import org.eclipse.swt.graphics.Image;

import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.NodeDecorator;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class UCDLinkDecorator implements NodeDecorator {

	public UCDLinkDecorator() {
	}

	@Override
	public String[] getLabelRelatedKeys() {
		return new String[0];
	}

	@Override
	public String getLabel(ManagedObject object) {
		return object.type().getName();
	}

	@Override
	public Image getImage(ManagedObject object) {
		return RUCMPlugin.getBundleImage("icons/link_obj.gif");
	}

	@Override
	public Image getImage(Type type) {
		return RUCMPlugin.getBundleImage("icons/link_obj.gif");
	}

}
