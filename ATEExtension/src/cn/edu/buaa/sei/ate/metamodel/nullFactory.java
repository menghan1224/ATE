package cn.edu.buaa.sei.ate.metamodel;


public class nullFactory {
	
	
	
	public static ATESignalTypeView createATESignalTypeView() {
		return new ATESignalTypeViewImpl();
	}
	
	public static Signal createSignal() {
		return new SignalImpl();
	}
	
	public static ATECapbility createATECapbility() {
		return new ATECapbilityImpl();
	}
	
	public static ATECapbilityView createATECapbilityView() {
		return new ATECapbilityViewImpl();
	}
	
	public static IODefinition createIODefinition() {
		return new IODefinitionImpl();
	}
	
	public static ATEIODefinitionView createATEIODefinitionView() {
		return new ATEIODefinitionViewImpl();
	}
	
	public static PinPort createPinPort() {
		return new PinPortImpl();
	}
	public static SignalType createSignalType() {
		return new SignalTypeImpl();
	}
	public static AteSentence createAteSentence() {
		return new AteSentenceImpl();
	}
	
	
}
