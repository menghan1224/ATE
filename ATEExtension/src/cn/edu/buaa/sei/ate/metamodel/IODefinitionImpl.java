package cn.edu.buaa.sei.ate.metamodel;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;
import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import cn.edu.buaa.sei.lmf.LMFContext;

public class IODefinitionImpl extends ManagedObjectImpl implements IODefinition, ModelElement {
	
	private static final long serialVersionUID = 1L;
	
	
	
	public IODefinitionImpl() {
		super(LMFContext.typeForName(IODefinition.TYPE_NAME));
	}
	
	@Override
	public String getSignalType() {
		return get(IODefinition.KEY_SIGNALTYPE).stringValue();
	}
	
	@Override
	public void setSignalType(String value) {
		set(IODefinition.KEY_SIGNALTYPE, value);
	}
	
	@Override
	public String getIoDefinition() {
		return get(IODefinition.KEY_IODEFINITION).stringValue();
	}
	
	@Override
	public void setIoDefinition(String value) {
		set(IODefinition.KEY_IODEFINITION, value);
	}
	
	@Override
	public String getName() {
		return get(IODefinition.KEY_NAME).stringValue();
	}
	
	@Override
	public void setName(String value) {
		set(IODefinition.KEY_NAME, value);
	}
	
	@Override
	public String getDescription() {
		return get(IODefinition.KEY_DESCRIPTION).stringValue();
	}
	
	@Override
	public void setDescription(String value) {
		set(IODefinition.KEY_DESCRIPTION, value);
	}
	
	@Override
	public String getRemark() {
		return get(IODefinition.KEY_REMARK).stringValue();
	}
	
	@Override
	public void setRemark(String value) {
		set(IODefinition.KEY_REMARK, value);
	}
	
	@Override
	public String getIoName() {
		return get(IODefinition.KEY_IONAME).stringValue();
	}
	
	@Override
	public void setIoName(String value) {
		set(IODefinition.KEY_IONAME, value);
	}
	
	@Override
	public String getPinport() {
		return get(IODefinition.KEY_PINPORT).stringValue();
	}
	
	@Override
	public void setPinport(String value) {
		set(IODefinition.KEY_PINPORT, value);
	}
	
	@Override
	public String getDirection() {
		return get(IODefinition.KEY_DIRECTION).stringValue();
	}
	
	@Override
	public void setDirection(String value) {
		set(IODefinition.KEY_DIRECTION, value);
	}
	
}
