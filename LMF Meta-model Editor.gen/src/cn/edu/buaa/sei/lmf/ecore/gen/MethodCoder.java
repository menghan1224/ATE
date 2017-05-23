package cn.edu.buaa.sei.lmf.ecore.gen;


public class MethodCoder {
	
	public boolean override = false;
	public boolean hasBody = true;
	public String methodName;
	public String returnType;
	public String parameters;
	public boolean staticMethod = false;

	public MethodCoder() {
	}
	
	protected void genMethodBody(Coder coder) {
	}
	
	public void genCode(Coder coder) {
		if (override) coder.appendLine("@Override");
		final String modifiers = staticMethod ? "public static" : "public";
		if (returnType != null) {
			coder.appendLine(String.format("%s %s %s(%s)%s", modifiers, returnType, methodName, parameters, hasBody ? " {" : ";"));
		} else {
			coder.appendLine(String.format("%s %s(%s)%s", modifiers, methodName, parameters, hasBody ? " {" : ";"));
		}
		if (hasBody) {
			coder.increaseIndentation();
			genMethodBody(coder);
			coder.decreaseIndentation();
			coder.appendLine("}");
		}
	}

}
