package cn.edu.buaa.sei.ate.metamodel;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;
import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import cn.edu.buaa.sei.lmf.LMFContext;

public class ATECapbilityViewImpl extends ManagedObjectImpl implements ATECapbilityView, ModelElement {
	
	private static final long serialVersionUID = 1L;
	
	
	
	public ATECapbilityViewImpl() {
		super(LMFContext.typeForName(ATECapbilityView.TYPE_NAME));
	}
	
	@Override
	public String getName() {
		return get(ATECapbilityView.KEY_NAME).stringValue();
	}
	
	@Override
	public void setName(String value) {
		set(ATECapbilityView.KEY_NAME, value);
	}
	
	@Override
	public List<ATECapbility> getCapbilities() {
		return get(ATECapbilityView.KEY_CAPBILITIES).listContent().toGenericList(ATECapbility.class);
	}
	
	@Override
	public String getDescription() {
		return get(ATECapbilityView.KEY_DESCRIPTION).stringValue();
	}
	
	@Override
	public void setDescription(String value) {
		set(ATECapbilityView.KEY_DESCRIPTION, value);
	}
	
}
