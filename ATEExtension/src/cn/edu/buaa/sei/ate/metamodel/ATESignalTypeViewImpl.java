package cn.edu.buaa.sei.ate.metamodel;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;
import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import cn.edu.buaa.sei.lmf.LMFContext;

public class ATESignalTypeViewImpl extends ManagedObjectImpl implements ATESignalTypeView, ModelElement {
	
	private static final long serialVersionUID = 1L;
	
	
	
	public ATESignalTypeViewImpl() {
		super(LMFContext.typeForName(ATESignalTypeView.TYPE_NAME));
	}
	
	@Override
	public List<Signal> getSignallist() {
		return get(ATESignalTypeView.KEY_SIGNALLIST).listContent().toGenericList(Signal.class);
	}
	
	@Override
	public String getName() {
		return get(ATESignalTypeView.KEY_NAME).stringValue();
	}
	
	@Override
	public void setName(String value) {
		set(ATESignalTypeView.KEY_NAME, value);
	}
	
	@Override
	public String getDescription() {
		return get(ATESignalTypeView.KEY_DESCRIPTION).stringValue();
	}
	
	@Override
	public void setDescription(String value) {
		set(ATESignalTypeView.KEY_DESCRIPTION, value);
	}
	
}
