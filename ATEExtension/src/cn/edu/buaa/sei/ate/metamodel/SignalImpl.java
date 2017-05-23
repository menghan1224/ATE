package cn.edu.buaa.sei.ate.metamodel;

import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import cn.edu.buaa.sei.lmf.LMFContext;

public class SignalImpl extends ManagedObjectImpl implements Signal, ModelElement {
	
	private static final long serialVersionUID = 1L;
	
	
	
	public SignalImpl() {
		super(LMFContext.typeForName(Signal.TYPE_NAME));
	}
	
	@Override
	public String getSignalName() {
		return get(Signal.KEY_SIGNALNAME).stringValue();
	}
	
	@Override
	public void setSignalName(String value) {
		set(Signal.KEY_SIGNALNAME, value);
	}
	
	@Override
	public String getParameter() {
		return get(Signal.KEY_PARAMETER).stringValue();
	}
	
	@Override
	public void setParameter(String value) {
		set(Signal.KEY_PARAMETER, value);
	}
	
	@Override
	public String getChannel() {
		return get(Signal.KEY_CHANNEL).stringValue();
	}
	
	@Override
	public void setChannel(String value) {
		set(Signal.KEY_CHANNEL, value);
	}
	
	@Override
	public String getName() {
		return get(Signal.KEY_NAME).stringValue();
	}
	
	@Override
	public void setName(String value) {
		set(Signal.KEY_NAME, value);
	}
	
	@Override
	public String getDescription() {
		return get(Signal.KEY_DESCRIPTION).stringValue();
	}
	
	@Override
	public void setDescription(String value) {
		set(Signal.KEY_DESCRIPTION, value);
	}
	
	@Override
	public String getSignalTypeName() {
		return get(Signal.KEY_SIGNALTYPENAME).stringValue();
	}
	
	@Override
	public void setSignalTypeName(String value) {
		set(Signal.KEY_SIGNALTYPENAME, value);
	}
	
}
