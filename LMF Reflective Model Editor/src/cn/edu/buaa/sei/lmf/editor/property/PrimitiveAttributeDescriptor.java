package cn.edu.buaa.sei.lmf.editor.property;

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Primitives;
import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;

public class PrimitiveAttributeDescriptor extends TextPropertyDescriptor implements IAttributeDescriptor {

	public PrimitiveAttributeDescriptor(String key) {
		super(key, key);
	}

	@Override
	public String getAttributeValue(ManagedObject object, String key) {
		ManagedObject value = object.get(key);
		Attribute attr = object.type().getAttribute(key);
		if (attr.getValueType() == Primitives.BOOL) {
			return Boolean.toString(value.boolValue());
		} else if (attr.getValueType() == Primitives.INT) {
			return Integer.toString(value.intValue());
		} else if (attr.getValueType() == Primitives.LONG) {
			return Long.toString(value.longValue());
		} else if (attr.getValueType() == Primitives.DOUBLE) {
			return Double.toString(value.doubleValue());
		} else if (attr.getValueType() == Primitives.FLOAT) {
			return Float.toString(value.floatValue());
		} else if (attr.getValueType() == Primitives.STRING) {
			return value.stringValue();
		} else if (attr.getValueType() == Primitives.ENUM) {
			int ev = value.intValue();
			return attr.getValueTypeParameter().getEnumValues()[ev];
		} else {
			LMFEditorPlugin.logError(getClass().getName(), true);
			return "";
		}
	}

	@Override
	public void setAttributeValue(ManagedObject object, String key, Object value) {
		Attribute attr = object.type().getAttribute(key);
		if (attr.getValueType() == Primitives.BOOL) {
			boolean v = Boolean.parseBoolean(value.toString());
			object.set(attr.getName(), v);
		} else if (attr.getValueType() == Primitives.INT) {
			int v = Integer.parseInt(value.toString());
			object.set(attr.getName(), v);
		} else if (attr.getValueType() == Primitives.LONG) {
			long v = Long.parseLong(value.toString());
			object.set(attr.getName(), v);
		} else if (attr.getValueType() == Primitives.DOUBLE) {
			double v = Double.parseDouble(value.toString());
			object.set(attr.getName(), v);
		} else if (attr.getValueType() == Primitives.FLOAT) {
			float v = Float.parseFloat(value.toString());
			object.set(attr.getName(), v);
		} else if (attr.getValueType() == Primitives.STRING) {
			String v = value.toString();
			object.set(attr.getName(), v);
		} else if (attr.getValueType() == Primitives.ENUM) {
			String v = value.toString();
			String[] values = attr.getValueTypeParameter().getEnumValues();
			for (int i = 0; i < values.length; i++) {
				if (values[i].equals(v)) {
					object.set(attr.getName(), i);
					return;
				}
			}
			object.set(attr.getName(), 0);
		} else {
			LMFEditorPlugin.logError(getClass().getName(), true);
		}
	}

	@Override
	public boolean canRestoreDefaultValue(ManagedObject object, String key) {
		return false;
	}

	@Override
	public void restoreDefaultValue(ManagedObject object, String key) {
	}

}
