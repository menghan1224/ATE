package cn.edu.buaa.sei.rucm.decorators;

import org.eclipse.swt.graphics.Image;

import ca.carleton.sce.squall.ucmeta.UseCase;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.NodeDecorator;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class UseCaseDescriptor implements NodeDecorator {

	@Override
	public String[] getLabelRelatedKeys() {
		return new String[] { UseCase.KEY_NAME, UseCase.KEY_SPECIFICATION };
	}

	@Override
	public String getLabel(ManagedObject object) {
		String name = object.get(UseCase.KEY_NAME).stringValue();
		if (name.isEmpty()) {
			return "(Untitled)";
		} else {
			return name;
		}
	}

	@Override
	public Image getImage(ManagedObject object) {
		return RUCMPlugin.getBundleImage("icons/usecase.gif");
	}
	
	@Override
	public Image getImage(Type type) {
		return RUCMPlugin.getBundleImage("icons/usecase.gif");
	}

}
