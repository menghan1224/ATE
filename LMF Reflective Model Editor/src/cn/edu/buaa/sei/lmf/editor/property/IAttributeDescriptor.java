package cn.edu.buaa.sei.lmf.editor.property;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import cn.edu.buaa.sei.lmf.ManagedObject;

public interface IAttributeDescriptor extends IPropertyDescriptor {

	public Object getAttributeValue(ManagedObject object, String key);
	public void setAttributeValue(ManagedObject object, String key, Object value);
	
	public boolean canRestoreDefaultValue(ManagedObject object, String key);
	public void restoreDefaultValue(ManagedObject object, String key);
	
}
