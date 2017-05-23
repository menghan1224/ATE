package cn.edu.buaa.sei.rucm.decorators;

import org.eclipse.swt.graphics.Image;

import ca.carleton.sce.squall.ucmeta.Generalization;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.NodeDecorator;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class GeneralizationDescriptor implements NodeDecorator {

	@Override
	public String[] getLabelRelatedKeys() {
		return new String[] { Generalization.KEY_SPECIFIC, Generalization.KEY_GENERAL };
	}

	@Override
	public String getLabel(ManagedObject object) {
		ManagedObject spcific = object.get(Generalization.KEY_SPECIFIC);
		ManagedObject general = object.get(Generalization.KEY_GENERAL);
		
		String spcificName = spcific == null ? "?" : spcific.get(ModelElement.KEY_NAME).stringValue();
		if (spcificName.isEmpty()) spcificName = "?";
		String generalName = general == null ? "?" : general.get(ModelElement.KEY_NAME).stringValue();
		if (generalName.isEmpty()) generalName = "?";
		
		return String.format("%s (%s -> %s)", object.type().getName(), spcificName, generalName);
	}

	@Override
	public Image getImage(ManagedObject object) {
		return RUCMPlugin.getBundleImage("icons/generalization.gif");
	}
	
	@Override
	public Image getImage(Type type) {
		return RUCMPlugin.getBundleImage("icons/generalization.gif");
	}

}
