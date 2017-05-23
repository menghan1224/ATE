package cn.edu.buaa.sei.ate.metamodel;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;

public interface ATECapbility extends ModelElement {
	
	public static final String KEY_CODE = "code";
	public static final String KEY_NUMBER = "number";
	public static final String KEY_DATARANGE = "datarange";
	public static final String KEY_FUNCTION = "function";
	public static final String KEY_ACCURACY = "accuracy";
	public static final String KEY_PINPORTTYPE = "pinporttype";
	public static final String TYPE_NAME = "ATECapbility";
	public static final String KEY_PINPORT = "pinport";
	
	
	public String getNumber();
	
	public void setNumber(String value);
	
	public String getCode();
	
	public void setCode(String value);
	
	public String getFunction();
	
	public void setFunction(String value);
	
	public String getDatarange();
	
	public void setDatarange(String value);
	
	public String getAccuracy();
	
	public void setAccuracy(String value);
	
	public String getPinporttype();
	
	public void setPinporttype(String value);
	
	public String getPinport();
	public void setPinport(String value);
	
}
