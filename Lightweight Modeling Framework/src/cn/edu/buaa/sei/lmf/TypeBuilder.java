package cn.edu.buaa.sei.lmf;

import java.util.HashSet;
import java.util.Set;

public final class TypeBuilder {
	
	public String extensionID;
	public String packageName;
	public final String name;
	public boolean isAbstract;
	public boolean isFinal;
	public String[] enumValues;
	
	public TypeBuilder(String name) {
		this.name = name;
	}

	public final Set<String> superTypeNames = new HashSet<String>();
	public final Set<AttributeBuilder> attributes = new HashSet<AttributeBuilder>();

}
