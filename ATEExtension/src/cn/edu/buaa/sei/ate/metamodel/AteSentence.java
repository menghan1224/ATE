package cn.edu.buaa.sei.ate.metamodel;

import ca.carleton.sce.squall.ucmeta.ModelElement;

public interface AteSentence extends ModelElement {
	
	public static final String KEY_PARA2 = "para2";
	public static final String KEY_PARA3 = "para3";
	public static final String KEY_PARA1 = "para1";
	public static final String KEY_PARA6 = "para6";
	public static final String TYPE_NAME = "AteSentence";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_PARA4 = "para4";
	public static final String KEY_PARA5 = "para5";
	
	
	public String getDescription();
	
	public void setDescription(String value);
	
	public String getPara1();
	
	public void setPara1(String value);
	
	public String getPara2();
	
	public void setPara2(String value);
	
	public String getPara3();
	
	public void setPara3(String value);
	
	public String getPara4();
	
	public void setPara4(String value);
	
	public String getPara5();
	
	public void setPara5(String value);
	
	public String getPara6();
	
	public void setPara6(String value);
	
}
