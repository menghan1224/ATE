package cn.edu.buaa.sei.ate.metamodel;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;

public interface ATECapbilityView extends ModelElement {
	
	public static final String KEY_CAPBILITIES = "capbilities";
	public static final String TYPE_NAME = "ATECapbilityView";
	
	
	public List<ATECapbility> getCapbilities();
	
}
