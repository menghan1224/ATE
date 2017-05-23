package cn.edu.buaa.sei.ate.decorators;

import org.eclipse.swt.graphics.Image;

import cn.edu.buaa.sei.ate.activators.Activator;
import cn.edu.buaa.sei.ate.metamodel.ATECapbilityView;
import cn.edu.buaa.sei.ate.metamodel.ATEIODefinitionView;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.NodeDecorator;

public class IODefinitionDecorator implements NodeDecorator {

	@Override
	public String[] getLabelRelatedKeys() {
		// TODO Auto-generated method stub
		return new String[]{ATEIODefinitionView.KEY_NAME};
	}

	@Override
	public String getLabel(ManagedObject object) {
		// TODO Auto-generated method stub
		if(object.get(ATEIODefinitionView.KEY_NAME).stringValue().isEmpty())
			return "ATEIODefinitionView";
		return String.format("\"%s\"",object.get(ATEIODefinitionView.KEY_NAME).stringValue());
	}

	@Override
	public Image getImage(ManagedObject object) {
		// TODO Auto-generated method stub
		return Activator.getBundleImage("icons/ATEview.png");
	}

	@Override
	public Image getImage(Type type) {
		// TODO Auto-generated method stub
		return Activator.getBundleImage("icons/ATEview.png");
	}

}
