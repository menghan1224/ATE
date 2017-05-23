package cn.edu.buaa.sei.ate.metamodel;

import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import cn.edu.buaa.sei.lmf.LMFContext;

public class AteSentenceImpl extends ManagedObjectImpl implements AteSentence, ModelElement {
	
	private static final long serialVersionUID = 1L;
	
	
	
	public AteSentenceImpl() {
		super(LMFContext.typeForName(AteSentence.TYPE_NAME));
	}
	
	@Override
	public String getName() {
		return get(AteSentence.KEY_NAME).stringValue();
	}
	
	@Override
	public void setName(String value) {
		set(AteSentence.KEY_NAME, value);
	}
	
	@Override
	public String getDescription() {
		return get(AteSentence.KEY_DESCRIPTION).stringValue();
	}
	
	@Override
	public void setDescription(String value) {
		set(AteSentence.KEY_DESCRIPTION, value);
	}
	
	@Override
	public String getPara1() {
		return get(AteSentence.KEY_PARA1).stringValue();
	}
	
	@Override
	public void setPara1(String value) {
		set(AteSentence.KEY_PARA1, value);
	}
	
	@Override
	public String getPara2() {
		return get(AteSentence.KEY_PARA2).stringValue();
	}
	
	@Override
	public void setPara2(String value) {
		set(AteSentence.KEY_PARA2, value);
	}
	
	@Override
	public String getPara3() {
		return get(AteSentence.KEY_PARA3).stringValue();
	}
	
	@Override
	public void setPara3(String value) {
		set(AteSentence.KEY_PARA3, value);
	}
	
	@Override
	public String getPara4() {
		return get(AteSentence.KEY_PARA4).stringValue();
	}
	
	@Override
	public void setPara4(String value) {
		set(AteSentence.KEY_PARA4, value);
	}
	
	@Override
	public String getPara5() {
		return get(AteSentence.KEY_PARA5).stringValue();
	}
	
	@Override
	public void setPara5(String value) {
		set(AteSentence.KEY_PARA5, value);
	}
	
	@Override
	public String getPara6() {
		return get(AteSentence.KEY_PARA6).stringValue();
	}
	
	@Override
	public void setPara6(String value) {
		set(AteSentence.KEY_PARA6, value);
	}
	
}
