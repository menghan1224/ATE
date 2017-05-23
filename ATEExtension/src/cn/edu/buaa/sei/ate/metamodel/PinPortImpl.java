package cn.edu.buaa.sei.ate.metamodel;

import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import cn.edu.buaa.sei.lmf.LMFContext;

public class PinPortImpl extends ManagedObjectImpl implements PinPort, ModelElement {
	
	private static final long serialVersionUID = 1L;
	
	
	
	public PinPortImpl() {
		super(LMFContext.typeForName(PinPort.TYPE_NAME));
	}
	
	@Override
	public String getNumber() {
		return get(PinPort.KEY_NUMBER).stringValue();
	}
	
	@Override
	public void setNumber(String value) {
		set(PinPort.KEY_NUMBER, value);
	}
	
	@Override
	public String getName() {
		return get(PinPort.KEY_NAME).stringValue();
	}
	
	@Override
	public void setName(String value) {
		set(PinPort.KEY_NAME, value);
	}
	
	@Override
	public String getDescription() {
		return get(PinPort.KEY_DESCRIPTION).stringValue();
	}
	
	@Override
	public void setDescription(String value) {
		set(PinPort.KEY_DESCRIPTION, value);
	}
	
}
