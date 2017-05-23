package cn.edu.buaa.sei.rucm.decorators;

import org.eclipse.swt.graphics.Image;

import ca.carleton.sce.squall.ucmeta.UCModel;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.NodeDecorator;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class UCModelDescriptor implements NodeDecorator {

	@Override
	public String[] getLabelRelatedKeys() {
		return new String[] { UCModel.KEY_NAME };
	}

	@Override
	public String getLabel(ManagedObject object) {
		String name = object.get(UCModel.KEY_NAME).stringValue();
		if (name.isEmpty()) {
			return object.type().getName();
		} else {
			return String.format("%s (%s)", object.type().getName(), name);
		}
	}

	@Override
	public Image getImage(ManagedObject object) {
		return RUCMPlugin.getBundleImage("icons/ucmodel.gif");
	}
	
	@Override
	public Image getImage(Type type) {
		return RUCMPlugin.getBundleImage("icons/ucmodel.gif");
	}

}
