package cn.edu.buaa.sei.lmf;

public final class AttributeBuilder {

	public String extensionID;
	@Deprecated public String categoryName;
	public final String name;
	public String valueTypeName;
	
	public boolean isContainment;
	public String valueTypeParameter;
	
	public AttributeBuilder(String name) {
		this.name = name;
	}
	
}
