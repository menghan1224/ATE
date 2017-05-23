package cn.edu.buaa.sei.lmf.ecore.gen;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.edu.buaa.sei.lmf.meta.Attribute;
import cn.edu.buaa.sei.lmf.meta.Category;
import cn.edu.buaa.sei.lmf.meta.Enum;
import cn.edu.buaa.sei.lmf.meta.EnumValue;
import cn.edu.buaa.sei.lmf.meta.Type;
import cn.edu.buaa.sei.lmf.meta.ValueTypeParameter;

public class TypeCoder extends JavaClassCoder {

	public static final Map<String, String> PRIMITIVE_NAMES;
	
	static {
		PRIMITIVE_NAMES = new HashMap<String, String>();
		PRIMITIVE_NAMES.put("<bool>", "boolean");
		PRIMITIVE_NAMES.put("<int>", "int");
		PRIMITIVE_NAMES.put("<long>", "long");
		PRIMITIVE_NAMES.put("<float>", "float");
		PRIMITIVE_NAMES.put("<double>", "double");
		PRIMITIVE_NAMES.put("<string>", "String");
		PRIMITIVE_NAMES.put("<list>", "ManagedList");
		PRIMITIVE_NAMES.put("<enum>", "int");
	}
	
	public TypeCoder(Type type) {
		Map<String, String> attrs = new HashMap<String, String>();
		Map<String, String> valueTypeParas = new HashMap<String, String>();
		
		for (Category category : type.getCategories()) {
			for (Attribute attr : category.getAttributes()) {
				attrs.put(attr.getName(), attr.getValueType().getName());
				ValueTypeParameter para = attr.getValueTypeParameter();
				if (para != null && para.getType() != null) {
					valueTypeParas.put(attr.getName(), para.getType().getName());
				}
			}
		}
		if (attrs.containsValue("<list>")) {
			imports.add("java.util.List");
		}
		if (type.getSuperTypes().size() == 0) {
			imports.add("cn.edu.buaa.sei.lmf.ManagedObject");
		}
		
		isPublic = true;
		isInterface = true;
		className = type.getName();
		for (Type superType : type.getSuperTypes()) {
			extendsList.add(superType.getName());
		}
		if (extendsList.size() == 0) {
			extendsList.add("ManagedObject");
		}
		hasSerialVersionUID = false;
		
		// constants
		stringConstants.put("TYPE_NAME", type.getName());
		for (Entry<String, String> entry : attrs.entrySet()) {
			stringConstants.put(getConstantVarName(entry.getKey()), entry.getKey());
		}
		
		// enum
		if (type instanceof Enum) {
			int i = 0;
			for (EnumValue ev : ((Enum) type).getValues()) {
				enumConstants.put("ENUM_" + ev.getName().toUpperCase(), i++);
			}
		}
		
		// attributes
		for (Entry<String, String> entry : attrs.entrySet()) {
			genMethod(entry.getKey(), entry.getValue(), valueTypeParas.get(entry.getKey()));
		}
	}
	
	public static String getConstantVarName(String key) {
		return "KEY_" + key.toUpperCase();
	}
	
	public static String getAccessorName(String key) {
		StringBuilder sb = new StringBuilder();
		sb.append(Character.toUpperCase(key.charAt(0)));
		if (key.length() > 1) {
			sb.append(key.substring(1));
		}
		return sb.toString();
	}
	
	public static String getJavaType(String type, String valueTypePara) {
		if (type.equals("<list>")) {
			return String.format("List<%s>", valueTypePara != null ? valueTypePara : "ManagedObject");
		} else if (PRIMITIVE_NAMES.containsKey(type)) {
			return PRIMITIVE_NAMES.get(type);
		} else {
			return type;
		}
	}
	
	private void genMethod(String key, String valueType, String valueTypePara) {
		MethodCoder getter = new MethodCoder();
		getter.override = false;
		getter.hasBody = false;
		getter.methodName = "get" + getAccessorName(key);
		getter.returnType = getJavaType(valueType, valueTypePara);
		getter.parameters = "";
		methodCoders.add(getter);
		
		if (!valueType.equals("<list>")) {
			MethodCoder setter = new MethodCoder();
			setter.override = false;
			setter.hasBody = false;
			setter.methodName = "set" + getAccessorName(key);
			setter.returnType = "void";
			setter.parameters = getJavaType(valueType, null) + " value";
			methodCoders.add(setter);
		}
	}
	
}
