package cn.edu.buaa.sei.ate.metamodel;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;

public interface IODefinition extends ModelElement {
	
	public static final String KEY_DIRECTION = "direction";
	public static final String KEY_IODEFINITION = "ioDefinition";
	public static final String KEY_SIGNALTYPE = "signalType";
	public static final String KEY_REMARK = "remark";
	public static final String TYPE_NAME = "IODefinition";
	public static final String KEY_PINPORT = "pinport";
	public static final String KEY_IONAME = "ioName";
	
	
	public String getSignalType();
	
	public void setSignalType(String value);
	
	public String getIoDefinition();
	
	public void setIoDefinition(String value);
	
	public String getRemark();
	
	public void setRemark(String value);
	
	public String getIoName();
	
	public void setIoName(String value);
	
	public String getPinport();
	
	public void setPinport(String value);
	
	public String getDirection();
	
	public void setDirection(String value);
	
}
