package cn.edu.buaa.sei.lmf.ecore.gen;

import cn.edu.buaa.sei.lmf.meta.ImportedType;
import cn.edu.buaa.sei.lmf.meta.Package;
import cn.edu.buaa.sei.lmf.meta.Type;

public class FactoryCoder extends JavaClassCoder {
	
	public FactoryCoder(String className, final Package pack) {
		this.isPublic = true;
		this.isInterface = false;
		this.className = className;
		hasSerialVersionUID = false;
		for (Type type : pack.getTypes()) {
			if (type.isAbstract() || type instanceof ImportedType) continue;
			final Type closure = type; 
			MethodCoder methodCoder = new MethodCoder() {
				@Override
				protected void genMethodBody(Coder coder) {
					coder.appendLine("return new " + closure.getName() + "Impl();");
				}
			};
			methodCoder.override = false;
			methodCoder.hasBody = true;
			methodCoder.methodName = "create" + type.getName();
			methodCoder.returnType = type.getName();
			methodCoder.parameters = "";
			methodCoder.staticMethod = true;
			methodCoders.add(methodCoder);
		}
	}

}
