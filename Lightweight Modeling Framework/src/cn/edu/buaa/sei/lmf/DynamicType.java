package cn.edu.buaa.sei.lmf;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

final class DynamicType implements Type {
	
	private final String extensionID;
	private final String packageName;
	private final String name;
	private final boolean isAbstract;
	private final boolean isFinal;
	private String[] enumValues;
	
	private final Set<Type> superTypes;
	private Map<String, Attribute> attributes;
	
	public DynamicType(String extensionID, String packageName, String name, boolean isAbstract, boolean isFinal) {
		this.extensionID = extensionID;
		this.packageName = packageName;
		this.name = name;
		this.superTypes = new HashSet<Type>(0);
		this.attributes = null;
		this.isAbstract = isAbstract;
		this.isFinal = isFinal;
	}
	
	@Override
	public String getExtensionID() {
		return extensionID;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Set<Type> getSuperTypes() {
		return Collections.unmodifiableSet(superTypes);
	}

	@Override
	public Collection<Attribute> getAttributes() {
		return Collections.unmodifiableCollection(attributes.values());
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	void addSuperType(Type type) {
		superTypes.add(type);
	}
	
	void setAttributes(Collection<Attribute> attrs) {
		this.attributes = new HashMap<String, Attribute>(0);
		for (Attribute attr : attrs) {
			this.attributes.put(attr.getName(), attr);
		}
	}

	@Override
	public Attribute getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public String description() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append(" -> (");
		for (Type superType : getSuperTypes()) {
			sb.append(superType.getName());
			sb.append(", ");
		}
		sb.append(") {\n");
		for (Attribute attr : attributes.values()) {
			sb.append("\t");
			sb.append(attr.description());
			sb.append(";\n");
		}
		sb.append("}");
		return sb.toString();
	}
	
	@Override
	public boolean isOrIsSubtypeOf(Type type) {
		return this == type || this.isSubtypeOf(type);
	}

	@Override
	public boolean isSubtypeOf(Type type) {
		if (type != null) {
			Queue<Type> queue = new LinkedList<Type>();
			queue.addAll(getSuperTypes());
			while (!queue.isEmpty()) {
				Type t = queue.poll();
				if (t == type) return true;
				else queue.addAll(t.getSuperTypes());
			}
		}
		return false;
	}

	@Override
	public boolean isPrimitiveType() {
		return false;
	}

	@Override
	public boolean isAbstract() {
		return isAbstract;
	}

	@Override
	public boolean isFinal() {
		return isFinal;
	}

	@Override
	public String[] getEnumValues() {
		if (enumValues == null) return null;
		else return enumValues.clone();
	}
	
	void setEnumValues(String[] values) {
		this.enumValues = values.clone();
	}
	
}
