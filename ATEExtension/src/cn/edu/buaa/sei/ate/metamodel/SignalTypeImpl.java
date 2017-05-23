package cn.edu.buaa.sei.ate.metamodel;

import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import cn.edu.buaa.sei.lmf.LMFContext;

public class SignalTypeImpl extends ManagedObjectImpl implements SignalType, ModelElement {
	
	private static final long serialVersionUID = 1L;
	
	
	
	public SignalTypeImpl() {
		super(LMFContext.typeForName(SignalType.TYPE_NAME));
	}
	
	@Override
	public String getName() {
		return get(SignalType.KEY_NAME).stringValue();
	}
	
	@Override
	public void setName(String value) {
		set(SignalType.KEY_NAME, value);
	}
	
	@Override
	public String getDescription() {
		return get(SignalType.KEY_DESCRIPTION).stringValue();
	}
	
	@Override
	public void setDescription(String value) {
		set(SignalType.KEY_DESCRIPTION, value);
	}
	
}
