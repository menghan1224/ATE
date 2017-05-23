package cn.edu.buaa.sei.lmf.ecore.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JavaClassCoder extends Coder {
	
	public final List<String> imports = new ArrayList<String>();
	
	public boolean isPublic = true;
	public boolean isInterface = false;
	public String className;
	public final List<String> extendsList = new ArrayList<String>();
	public final List<String> implementsList = new ArrayList<String>();
	
	public boolean hasSerialVersionUID = true;
	public final Map<String, String> stringConstants = new HashMap<String, String>();
	public final Map<String, Integer> enumConstants = new HashMap<String, Integer>();
	public final List<MethodCoder> methodCoders = new ArrayList<MethodCoder>();
	
	public JavaClassCoder() {
		super(0);
	}
	
	@Override
	public String toString() {
		reset();
		for (String im : imports) {
			appendLine(String.format("import %s;", im));
		}
		appendLine("");
		
		StringBuilder classDeclare = new StringBuilder();
		if (isPublic) classDeclare.append("public ");
		classDeclare.append(isInterface ? "interface " : "class ");
		classDeclare.append(className);
		classDeclare.append(" ");
		if (extendsList.size() > 0) {
			classDeclare.append("extends ");
			for (int i = 0; i < extendsList.size(); i++) {
				classDeclare.append(extendsList.get(i));
				if (i == extendsList.size() - 1) {
					classDeclare.append(" ");
				} else {
					classDeclare.append(", ");
				}
			}
		}
		if (implementsList.size() > 0) {
			classDeclare.append("implements ");
			for (int i = 0; i < implementsList.size(); i++) {
				classDeclare.append(implementsList.get(i));
				if (i == implementsList.size() - 1) {
					classDeclare.append(" ");
				} else {
					classDeclare.append(", ");
				}
			}
		}
		classDeclare.append("{");
		appendLine(classDeclare.toString());
		
		increaseIndentation();
		
		appendLine("");
		
		if (hasSerialVersionUID) {
			appendLine("private static final long serialVersionUID = 1L;");
			appendLine("");
		}
		
		for (Entry<String, String> entry : stringConstants.entrySet()) {
			appendLine(String.format("public static final String %s = \"%s\";", entry.getKey(), entry.getValue()));
		}
		
		appendLine("");
		
		for (Entry<String, Integer> entry : enumConstants.entrySet()) {
			appendLine(String.format("public static final int %s = %d;", entry.getKey(), entry.getValue()));
		}
		
		appendLine("");
		
		for (MethodCoder methodCoder : methodCoders) {
			methodCoder.genCode(this);
			appendLine("");
		}
		
		decreaseIndentation();
		appendLine("}");
		return super.toString();
	}

}
