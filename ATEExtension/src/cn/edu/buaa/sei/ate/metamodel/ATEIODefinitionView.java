package cn.edu.buaa.sei.ate.metamodel;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;

public interface ATEIODefinitionView extends ModelElement {
	
	public static final String KEY_IODEFINITIONS = "iodefinitions";
	public static final String TYPE_NAME = "ATEIODefinitionView";
	
	
	public List<IODefinition> getIodefinitions();
	
}
