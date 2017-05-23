package cn.edu.buaa.sei.lmf.ecore.gen;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.edu.buaa.sei.lmf.meta.Attribute;
import cn.edu.buaa.sei.lmf.meta.Category;
import cn.edu.buaa.sei.lmf.meta.Type;
import cn.edu.buaa.sei.lmf.meta.ValueTypeParameter;

public class TypeImplCoder extends JavaClassCoder {
	
	private final static Map<String, String> PRIMITIVE_GETTER;
	
	static {
		PRIMITIVE_GETTER = new HashMap<String, String>();
		PRIMITIVE_GETTER.put("<bool>", "boolValue");
		PRIMITIVE_GETTER.put("<int>", "intValue");
		PRIMITIVE_GETTER.put("<long>", "longValue");
		PRIMITIVE_GETTER.put("<float>", "floatValue");
		PRIMITIVE_GETTER.put("<double>", "doubleValue");
		PRIMITIVE_GETTER.put("<string>", "stringValue");
		PRIMITIVE_GETTER.put("<list>", "listContent");
		PRIMITIVE_GETTER.put("<enum>", "intValue");
	}

	public TypeImplCoder(Type type) {
		Map<String, String> attrs = new HashMap<String, String>();
		Map<String, String> valueTypeParas = new HashMap<String, String>();
		addAllAttributes(attrs, valueTypeParas, type);
		if (attrs.containsValue("<list>")) {
			imports.add("java.util.List");
		}
		imports.add("cn.edu.buaa.sei.lmf.ManagedObjectImpl");
		imports.add("cn.edu.buaa.sei.lmf.LMFContext");
		isPublic = true;
		isInterface = false;
		className = type.getName() + "Impl";
		extendsList.add("ManagedObjectImpl");
		implementsList.add(type.getName());
		for (Type superType : type.getSuperTypes()) {
			implementsList.add(superType.getName());
		}
		hasSerialVersionUID = true;
		
		// constructor
		genConstructor(type);
		
		// attributes
		for (Entry<String, String> entry : attrs.entrySet()) {
			genMethod(entry.getKey(), entry.getValue(), valueTypeParas.get(entry.getKey()), type);
		}
	}
	
	private void genConstructor(final Type type) {
		MethodCoder con = new MethodCoder() {
			@Override
			protected void genMethodBody(Coder coder) {
				coder.appendLine(String.format("super(LMFContext.typeForName(%s.TYPE_NAME));", type.getName()));
			}
		};
		con.override = false;
		con.hasBody = true;
		con.methodName = className;
		con.returnType = null;
		con.parameters = "";
		methodCoders.add(con);
	}
	
	private void addAllAttributes(Map<String, String> attrs, Map<String, String> valueTypeParas, Type type) {
		for (Category category : type.getCategories()) {
			for (Attribute attr : category.getAttributes()) {
				attrs.put(attr.getName(), attr.getValueType().getName());
				ValueTypeParameter para = attr.getValueTypeParameter();
				if (para != null && para.getType() != null) {
					valueTypeParas.put(attr.getName(), para.getType().getName());
				}
			}
		}
		for (Type supertType : type.getSuperTypes()) {
			addAllAttributes(attrs, valueTypeParas, supertType);
		}
	}
	
	private void genMethod(final String key, final String valueType, final String valueTypePara, final Type type) {
		MethodCoder getter = new MethodCoder() {
			@Override
			protected void genMethodBody(Coder coder) {
				genGetterBody(coder, key, valueType, valueTypePara, type);
			}
		};
		getter.override = true;
		getter.hasBody = true;
		getter.methodName = "get" + TypeCoder.getAccessorName(key);
		getter.returnType = TypeCoder.getJavaType(valueType, valueTypePara);
		getter.parameters = "";
		methodCoders.add(getter);
		
		if (!valueType.equals("<list>")) {
			MethodCoder setter = new MethodCoder() {
				@Override
				protected void genMethodBody(Coder coder) {
					genSetterBody(coder, key, valueType, type);
				}
			};
			setter.override = true;
			setter.hasBody = true;
			setter.methodName = "set" + TypeCoder.getAccessorName(key);
			setter.returnType = "void";
			setter.parameters = TypeCoder.getJavaType(valueType, valueTypePara) + " value";
			methodCoders.add(setter);
		}
	}
	
	private void genGetterBody(Coder coder, String key, String valueType, String valueTypePara, Type type) {
		if (valueType.equals("<list>")) {
			coder.appendLine(String.format("return get(%s.%s).listContent().toGenericList(%s.class);", type.getName(), TypeCoder.getConstantVarName(key), valueTypePara));
		} else if (TypeCoder.PRIMITIVE_NAMES.containsKey(valueType)) {
			coder.appendLine(String.format("return get(%s.%s).%s();", type.getName(), TypeCoder.getConstantVarName(key), PRIMITIVE_GETTER.get(valueType)));
		} else {
			coder.appendLine(String.format("return (%s) get(%s.%s);", valueType, type.getName(), TypeCoder.getConstantVarName(key)));
		}
	}
	
	private void genSetterBody(Coder coder, String key, String valueType, Type type) {
		coder.appendLine(String.format("set(%s.%s, value);", type.getName(), TypeCoder.getConstantVarName(key)));
	}
	
}
