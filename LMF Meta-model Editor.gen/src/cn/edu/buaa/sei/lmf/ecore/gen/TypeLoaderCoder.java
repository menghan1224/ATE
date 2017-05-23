package cn.edu.buaa.sei.lmf.ecore.gen;

import cn.edu.buaa.sei.lmf.meta.Attribute;
import cn.edu.buaa.sei.lmf.meta.Category;
import cn.edu.buaa.sei.lmf.meta.EnumValue;
import cn.edu.buaa.sei.lmf.meta.ImportedType;
import cn.edu.buaa.sei.lmf.meta.Package;
import cn.edu.buaa.sei.lmf.meta.Type;
import cn.edu.buaa.sei.lmf.meta.ValueTypeParameter;

public class TypeLoaderCoder extends JavaClassCoder {
	
	public TypeLoaderCoder(String className, final Package pack) {
		imports.add("java.util.HashSet");
		imports.add("java.util.HashMap");
		imports.add("java.util.Map");
		imports.add("java.util.Set");
		imports.add("cn.edu.buaa.sei.lmf.AttributeBuilder");
		imports.add("cn.edu.buaa.sei.lmf.ManagedObjectImpl");
		imports.add("cn.edu.buaa.sei.lmf.TypeBuilder");
		imports.add("cn.edu.buaa.sei.lmf.TypeLoader");
		this.isPublic = true;
		this.isInterface = false;
		this.className = className;
		this.implementsList.add("TypeLoader");
		hasSerialVersionUID = false;
		
		MethodCoder method1 = new MethodCoder() {
			@Override
			protected void genMethodBody(Coder coder) {
				generateTypes(coder, pack);
			}
		};
		method1.override = true;
		method1.returnType = "Set<TypeBuilder>";
		method1.methodName = "loadTypes";
		method1.parameters = "Map<String, TypeBuilder> existingTypes";
		methodCoders.add(method1);
		
		MethodCoder method2 = new MethodCoder() {
			@Override
			protected void genMethodBody(Coder coder) {
				generateImplementationClasses(coder, pack);
			}
		};
		method2.override = true;
		method2.returnType = "Map<String, Class<? extends ManagedObjectImpl>>";
		method2.methodName = "loadImplementationClasses";
		method2.parameters = "";
		methodCoders.add(method2);
	}
	
	private void generateTypes(Coder coder, Package pack) {
		final String packageName = getStringValueOrNull(pack.getName());
		final String packageVar = "types";
		coder.appendLine(String.format("Set<TypeBuilder> %s = new HashSet<TypeBuilder>();", packageVar));
		coder.appendLine("");
		
		for (Type type : pack.getTypes()) {
			String typeVar = null;
			if (type instanceof ImportedType) {
				typeVar = String.format("type_%s", ((ImportedType) type).getImport().getName());
				coder.appendLine(String.format("// Imported Type Definition: %s", ((ImportedType) type).getImport().getName()));
			} else {
				typeVar = String.format("type_%s", type.getName());
				coder.appendLine(String.format("// Type Definition: %s", type.getName()));
			}
			
			if (type instanceof ImportedType) {
				coder.appendLine(String.format("TypeBuilder %s = existingTypes.get(\"%s\");", typeVar, ((ImportedType) type).getImport().getName()));
			} else {
				coder.appendLine(String.format("TypeBuilder %s = new TypeBuilder(\"%s\");", typeVar, type.getName()));
				coder.appendLine(String.format("%s.extensionID = %s;", typeVar, getStringValueOrNull(type.getExtensionID())));
				coder.appendLine(String.format("%s.packageName = %s;", typeVar, packageName));
				coder.appendLine(String.format("%s.isAbstract = %s;", typeVar, type.isAbstract() ? "true" : "false"));
				coder.appendLine(String.format("%s.isFinal = %s;", typeVar, type.isFinal() ? "true" : "false"));
			}
			for (Type superType : type.getSuperTypes()) {
				coder.appendLine(String.format("%s.superTypeNames.add(\"%s\");", typeVar, superType.getName()));
			}
			if (type instanceof cn.edu.buaa.sei.lmf.meta.Enum) {
				StringBuilder values = new StringBuilder();
				for (EnumValue ev : ((cn.edu.buaa.sei.lmf.meta.Enum) type).getValues()) {
					values.append('\"');
					values.append(ev.getName());
					values.append("\", ");
				}
				coder.appendLine(String.format("%s.enumValues = new String[] { %s};", typeVar, values));
			}
			coder.appendLine("{");
			coder.increaseIndentation();
			for (Category category : type.getCategories()) {
				generateAttributes(coder, category, typeVar);
			}
			coder.decreaseIndentation();
			coder.appendLine("}");
			if (!(type instanceof ImportedType)) {
				coder.appendLine(String.format("%s.add(%s);", packageVar, typeVar));
			}
			coder.appendLine("");
		}
		coder.appendLine("return " + packageVar + ";");
	}
	
	private void generateAttributes(Coder coder, Category category, String ownerTypeVar) {
		for (Attribute attr : category.getAttributes()) {
			String attrVar = String.format("attr_%s", attr.getName());
			coder.appendLine(String.format("// Attribute Definition: %s", attr.getName()));
			coder.appendLine(String.format("AttributeBuilder %s = new AttributeBuilder(\"%s\");", attrVar, attr.getName()));
			coder.appendLine(String.format("%s.extensionID = %s;", attrVar, getStringValueOrNull(attr.getExtensionID())));
			coder.appendLine(String.format("%s.valueTypeName = \"%s\";", attrVar, attr.getValueType().getName()));
			coder.appendLine(String.format("%s.isContainment = %s;", attrVar, attr.isContainment() ? "true" : "false"));
			String parameterString = null;
			ValueTypeParameter parameter = attr.getValueTypeParameter();
			if (parameter != null && parameter.getType() != null) {
				parameterString = parameter.getType().getName();
			}
			coder.appendLine(String.format("%s.valueTypeParameter = %s;", attrVar, getStringValueOrNull(parameterString)));
			coder.appendLine(String.format("%s.attributes.add(%s);", ownerTypeVar, attrVar));
			coder.appendLine("");
		}
	}
	
	private String getStringValueOrNull(String value) {
		if (value == null || value.isEmpty()) return "null";
		else return String.format("\"%s\"", value);
	}
	
	private void generateImplementationClasses(Coder coder, Package pack) {
		coder.appendLine("Map<String, Class<? extends ManagedObjectImpl>> map = new HashMap<String, Class<? extends ManagedObjectImpl>>();");
		for (Type type : pack.getTypes()) {
			if (!type.isAbstract() && !(type instanceof ImportedType)) {
				coder.appendLine(String.format("map.put(\"%s\", %sImpl.class);", type.getName(), type.getName()));
			}
		}
		coder.appendLine("return map;");
	}

}
