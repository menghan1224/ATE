package cn.edu.buaa.sei.rucm.decorators;

import org.eclipse.swt.graphics.Image;

import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.NodeDecorator;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

import ca.carleton.sce.squall.ucmeta.FlowOfEvents;

public class FlowDescriptor implements NodeDecorator {

	@Override
	public String[] getLabelRelatedKeys() {
		return new String[] { FlowOfEvents.KEY_NAME };
	}

	@Override
	public String getLabel(ManagedObject object) {
		String name = object.get(FlowOfEvents.KEY_NAME).stringValue();
		if (name.isEmpty()) {
			return object.type().getName(); 
		} else {
			return String.format("%s (%s)", object.type().getName(), name);
		}
	}

	@Override
	public Image getImage(ManagedObject object) {
		return RUCMPlugin.getBundleImage("icons/all_instances.gif");
	}
	
	@Override
	public Image getImage(Type type) {
		return RUCMPlugin.getBundleImage("icons/all_instances.gif");
	}

}
