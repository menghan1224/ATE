package cn.edu.buaa.sei.rucm.decorators;

import org.eclipse.swt.graphics.Image;

import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.NodeDecorator;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class SpecDescriptor implements NodeDecorator {

	@Override
	public String[] getLabelRelatedKeys() {
		return new String[0];
	}

	@Override
	public String getLabel(ManagedObject object) {
		return "Specification";
	}

	@Override
	public Image getImage(ManagedObject object) {
		return RUCMPlugin.getBundleImage("icons/properties.gif");
	}
	
	@Override
	public Image getImage(Type type) {
		return RUCMPlugin.getBundleImage("icons/properties.gif");
	}

}
