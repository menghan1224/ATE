package cn.edu.buaa.sei.ate.metamodel;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;

public interface ATESignalTypeView extends ModelElement {
	
	public static final String KEY_SIGNALLIST = "signallist";
	public static final String TYPE_NAME = "ATESignalTypeView";
	
	
	public List<Signal> getSignallist();
	
}
