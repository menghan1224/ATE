package cn.edu.buaa.sei.ate.metamodel;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;
import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import cn.edu.buaa.sei.lmf.LMFContext;

public class ATEIODefinitionViewImpl extends ManagedObjectImpl implements ATEIODefinitionView, ModelElement {
	
	private static final long serialVersionUID = 1L;
	
	
	
	public ATEIODefinitionViewImpl() {
		super(LMFContext.typeForName(ATEIODefinitionView.TYPE_NAME));
	}
	
	@Override
	public String getName() {
		return get(ATEIODefinitionView.KEY_NAME).stringValue();
	}
	
	@Override
	public void setName(String value) {
		set(ATEIODefinitionView.KEY_NAME, value);
	}
	
	@Override
	public String getDescription() {
		return get(ATEIODefinitionView.KEY_DESCRIPTION).stringValue();
	}
	
	@Override
	public void setDescription(String value) {
		set(ATEIODefinitionView.KEY_DESCRIPTION, value);
	}
	
	@Override
	public List<IODefinition> getIodefinitions() {
		return get(ATEIODefinitionView.KEY_IODEFINITIONS).listContent().toGenericList(IODefinition.class);
	}
	
}
