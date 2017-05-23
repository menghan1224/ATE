package cn.edu.buaa.sei.ate.metamodel;

import ca.carleton.sce.squall.ucmeta.ModelElement;

public interface Signal extends ModelElement {
	
	public static final String KEY_SIGNALTYPENAME = "signalTypeName";
	public static final String KEY_SIGNALNAME = "signalName";
	public static final String KEY_PARAMETER = "parameter";
	public static final String TYPE_NAME = "Signal";
	public static final String KEY_CHANNEL = "channel";
	
	
	public String getSignalName();
	
	public void setSignalName(String value);
	
	public String getParameter();
	
	public void setParameter(String value);
	
	public String getChannel();
	
	public void setChannel(String value);
	
	public String getSignalTypeName();
	
	public void setSignalTypeName(String value);
	
}
