package cn.edu.buaa.sei.lmf;

final class DynamicAttribute implements Attribute {

	private final String extensionID;
	private final String name;
	
	private final Type ownerType;
	private final Type valueType;
	
	private final boolean isContainment;
	private final Type valueTypeParameter;
	
	DynamicAttribute(String ext, String name, Type owner, Type valueType, boolean isContainment, Type valueTypeParameter) {
		this.extensionID = ext;
		this.name = name;
		this.ownerType = owner;
		this.valueType = valueType;
		this.isContainment = isContainment;
		this.valueTypeParameter = valueTypeParameter;
	}
	
	@Override
	public String getExtensionID() {
		return extensionID;
	}

	@Override
	public Type getOwnerType() {
		return ownerType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Type getValueType() {
		return valueType;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public String description() {
		return String.format("%s : %s", this.getName(), getValueType().getName());
	}

	@Override
	public boolean isContainment() {
		return isContainment;
	}

	@Override
	public Type getValueTypeParameter() {
		return valueTypeParameter;
	}
	
}
