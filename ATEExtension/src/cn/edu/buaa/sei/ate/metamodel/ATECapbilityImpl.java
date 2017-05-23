package cn.edu.buaa.sei.ate.metamodel;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;
import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import cn.edu.buaa.sei.lmf.AttributeSetter;
import cn.edu.buaa.sei.lmf.LMFContext;

public class ATECapbilityImpl extends ManagedObjectImpl implements ATECapbility, ModelElement {
	
	private static final long serialVersionUID = 1L;
	
	
	
	public ATECapbilityImpl() {
		super(LMFContext.typeForName(ATECapbility.TYPE_NAME));
	}
	
	@Override
	public String getNumber() {
		return get(ATECapbility.KEY_NUMBER).stringValue();
	}
	
	@Override
	public void setNumber(String value) {
		set(ATECapbility.KEY_NUMBER, value);
	}
	
	@Override
	public String getCode() {
		return get(ATECapbility.KEY_CODE).stringValue();
	}
	
	@Override
	public void setCode(String value) {
		set(ATECapbility.KEY_CODE, value);
	}
	
	@Override
	public String getFunction() {
		return get(ATECapbility.KEY_FUNCTION).stringValue();
	}
	
	@Override
	public void setFunction(String value) {
		set(ATECapbility.KEY_FUNCTION, value);
	}
	
	@Override
	public String getName() {
		return get(ATECapbility.KEY_NAME).stringValue();
	}
	
	@Override
	public void setName(String value) {
		set(ATECapbility.KEY_NAME, value);
	}
	
	@Override
	public String getDatarange() {
		return get(ATECapbility.KEY_DATARANGE).stringValue();
	}
	
	@Override
	public void setDatarange(String value) {
		set(ATECapbility.KEY_DATARANGE, value);
	}
	
	@Override
	public String getAccuracy() {
		return get(ATECapbility.KEY_ACCURACY).stringValue();
	}
	
	@Override
	public void setAccuracy(String value) {
		set(ATECapbility.KEY_ACCURACY, value);
	}
	
	@Override
	public String getDescription() {
		return get(ATECapbility.KEY_DESCRIPTION).stringValue();
	}
	
	@Override
	public void setDescription(String value) {
		set(ATECapbility.KEY_DESCRIPTION, value);
	}
	
	@Override
	public String getPinporttype() {
		return get(ATECapbility.KEY_PINPORTTYPE).stringValue();
	}
	
	@Override
	public void setPinporttype(String value) {
		set(ATECapbility.KEY_PINPORTTYPE, value);
	}
	
	@Override
	public String getPinport() {
		return get(ATECapbility.KEY_PINPORT).stringValue();
	}
	
	@Override
	public void setPinport(String value){
		set(ATECapbility.KEY_PINPORT,value);
	}
	
}
