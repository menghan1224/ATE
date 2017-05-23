package cn.edu.buaa.sei.ate.metamodel;

import ca.carleton.sce.squall.ucmeta.ModelElement;

public interface PinPort extends ModelElement {
	
	public static final String KEY_NUMBER = "number";
	public static final String TYPE_NAME = "PinPort";
	
	
	public String getNumber();
	
	public void setNumber(String value);
	
}
