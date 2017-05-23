package cn.edu.buaa.sei.rucm.decorators;

import org.eclipse.swt.graphics.Image;

import ca.carleton.sce.squall.ucmeta.Sentence;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.NodeDecorator;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class SentenceDescriptor implements NodeDecorator {

	@Override
	public String[] getLabelRelatedKeys() {
		return new String[] { Sentence.KEY_CONTENT };
	}

	@Override
	public String getLabel(ManagedObject object) {
		String content = object.get(Sentence.KEY_CONTENT).stringValue();
		if (content.isEmpty()) return "(Empty Sentence)";
		else return String.format("\"%s\"", content);
	}

	@Override
	public Image getImage(ManagedObject object) {
		return RUCMPlugin.getBundleImage("icons/field_public_obj.gif");
	}
	
	@Override
	public Image getImage(Type type) {
		return RUCMPlugin.getBundleImage("icons/field_public_obj.gif");
	}

}
