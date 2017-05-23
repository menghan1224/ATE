package cn.edu.buaa.sei.ate.metamodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.edu.buaa.sei.lmf.AttributeBuilder;
import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import cn.edu.buaa.sei.lmf.TypeBuilder;
import cn.edu.buaa.sei.lmf.TypeLoader;

public class SignalTypeLoader implements TypeLoader {


	@Override
	public Set<TypeBuilder> loadTypes(Map<String, TypeBuilder> existingTypes) {
		Set<TypeBuilder> types = new HashSet<TypeBuilder>();
		
		// Type Definition: ATESignalTypeView
		TypeBuilder type_ATESignalTypeView = new TypeBuilder("ATESignalTypeView");
		type_ATESignalTypeView.extensionID = null;
		type_ATESignalTypeView.packageName = null;
		type_ATESignalTypeView.isAbstract = false;
		type_ATESignalTypeView.isFinal = false;
		type_ATESignalTypeView.superTypeNames.add("ModelElement");
		{
			// Attribute Definition: signallist
			AttributeBuilder attr_signallist = new AttributeBuilder("signallist");
			attr_signallist.extensionID = null;
			attr_signallist.categoryName = null;
			attr_signallist.valueTypeName = "<list>";
			attr_signallist.isContainment = true;
			attr_signallist.valueTypeParameter = "Signal";
			type_ATESignalTypeView.attributes.add(attr_signallist);
			
		}
		types.add(type_ATESignalTypeView);
		
		// Type Definition: Signal
		TypeBuilder type_Signal = new TypeBuilder("Signal");
		type_Signal.extensionID = null;
		type_Signal.packageName = null;
		type_Signal.isAbstract = false;
		type_Signal.isFinal = false;
		type_Signal.superTypeNames.add("ModelElement");
		{
			// Attribute Definition: signalTypeName
			AttributeBuilder attr_signalTypeName = new AttributeBuilder("signalTypeName");
			attr_signalTypeName.extensionID = null;
			attr_signalTypeName.categoryName = null;
			attr_signalTypeName.valueTypeName = "<string>";
			attr_signalTypeName.isContainment = true;
			attr_signalTypeName.valueTypeParameter = null;
			type_Signal.attributes.add(attr_signalTypeName);
			
			// Attribute Definition: signalName
			AttributeBuilder attr_signalName = new AttributeBuilder("signalName");
			attr_signalName.extensionID = null;
			attr_signalName.categoryName = null;
			attr_signalName.valueTypeName = "<string>";
			attr_signalName.isContainment = true;
			attr_signalName.valueTypeParameter = null;
			type_Signal.attributes.add(attr_signalName);
			
			// Attribute Definition: parameter
			AttributeBuilder attr_parameter = new AttributeBuilder("parameter");
			attr_parameter.extensionID = null;
			attr_parameter.categoryName = null;
			attr_parameter.valueTypeName = "<string>";
			attr_parameter.isContainment = true;
			attr_parameter.valueTypeParameter = null;
			type_Signal.attributes.add(attr_parameter);
			
			// Attribute Definition: channel
			AttributeBuilder attr_channel = new AttributeBuilder("channel");
			attr_channel.extensionID = null;
			attr_channel.categoryName = null;
			attr_channel.valueTypeName = "<string>";
			attr_channel.isContainment = true;
			attr_channel.valueTypeParameter = null;
			type_Signal.attributes.add(attr_channel);
			
		}
		types.add(type_Signal);
		
		// Type Definition: ATECapbility
		TypeBuilder type_ATECapbility = new TypeBuilder("ATECapbility");
		type_ATECapbility.extensionID = null;
		type_ATECapbility.packageName = null;
		type_ATECapbility.isAbstract = false;
		type_ATECapbility.isFinal = false;
		type_ATECapbility.superTypeNames.add("ModelElement");
		{
			// Attribute Definition: pinport
			AttributeBuilder attr_pinport = new AttributeBuilder("pinport");
			attr_pinport.extensionID = null;
			attr_pinport.categoryName = null;
			attr_pinport.valueTypeName = "<string>";
			attr_pinport.isContainment = true;
			attr_pinport.valueTypeParameter = null;
			type_ATECapbility.attributes.add(attr_pinport);
			
			// Attribute Definition: pinporttype
			AttributeBuilder attr_pinporttype = new AttributeBuilder("pinporttype");
			attr_pinporttype.extensionID = null;
			attr_pinporttype.categoryName = null;
			attr_pinporttype.valueTypeName = "<string>";
			attr_pinporttype.isContainment = true;
			attr_pinporttype.valueTypeParameter = null;
			type_ATECapbility.attributes.add(attr_pinporttype);
			
			// Attribute Definition: function
			AttributeBuilder attr_function = new AttributeBuilder("function");
			attr_function.extensionID = null;
			attr_function.categoryName = null;
			attr_function.valueTypeName = "<string>";
			attr_function.isContainment = true;
			attr_function.valueTypeParameter = null;
			type_ATECapbility.attributes.add(attr_function);
			
			// Attribute Definition: datarange
			AttributeBuilder attr_datarange = new AttributeBuilder("datarange");
			attr_datarange.extensionID = null;
			attr_datarange.categoryName = null;
			attr_datarange.valueTypeName = "<string>";
			attr_datarange.isContainment = true;
			attr_datarange.valueTypeParameter = null;
			type_ATECapbility.attributes.add(attr_datarange);
			
			// Attribute Definition: accuracy
			AttributeBuilder attr_accuracy = new AttributeBuilder("accuracy");
			attr_accuracy.extensionID = null;
			attr_accuracy.categoryName = null;
			attr_accuracy.valueTypeName = "<string>";
			attr_accuracy.isContainment = true;
			attr_accuracy.valueTypeParameter = null;
			type_ATECapbility.attributes.add(attr_accuracy);
			
			// Attribute Definition: number
			AttributeBuilder attr_number = new AttributeBuilder("number");
			attr_number.extensionID = null;
			attr_number.categoryName = null;
			attr_number.valueTypeName = "<string>";
			attr_number.isContainment = true;
			attr_number.valueTypeParameter = null;
			type_ATECapbility.attributes.add(attr_number);
			
			// Attribute Definition: code
			AttributeBuilder attr_code = new AttributeBuilder("code");
			attr_code.extensionID = null;
			attr_code.categoryName = null;
			attr_code.valueTypeName = "<string>";
			attr_code.isContainment = true;
			attr_code.valueTypeParameter = null;
			type_ATECapbility.attributes.add(attr_code);
			
		}
		types.add(type_ATECapbility);
		
		// Type Definition: ATECapbilityView
		TypeBuilder type_ATECapbilityView = new TypeBuilder("ATECapbilityView");
		type_ATECapbilityView.extensionID = null;
		type_ATECapbilityView.packageName = null;
		type_ATECapbilityView.isAbstract = false;
		type_ATECapbilityView.isFinal = false;
		type_ATECapbilityView.superTypeNames.add("ModelElement");
		{
			// Attribute Definition: capbilities
			AttributeBuilder attr_capbilities = new AttributeBuilder("capbilities");
			attr_capbilities.extensionID = null;
			attr_capbilities.categoryName = null;
			attr_capbilities.valueTypeName = "<list>";
			attr_capbilities.isContainment = true;
			attr_capbilities.valueTypeParameter = "ATECapbility";
			type_ATECapbilityView.attributes.add(attr_capbilities);
			
		}
		types.add(type_ATECapbilityView);
		
		// Type Definition: IODefinition
		TypeBuilder type_IODefinition = new TypeBuilder("IODefinition");
		type_IODefinition.extensionID = null;
		type_IODefinition.packageName = null;
		type_IODefinition.isAbstract = false;
		type_IODefinition.isFinal = false;
		type_IODefinition.superTypeNames.add("ModelElement");
		{
			// Attribute Definition: ioName
			AttributeBuilder attr_ioName = new AttributeBuilder("ioName");
			attr_ioName.extensionID = null;
			attr_ioName.categoryName = null;
			attr_ioName.valueTypeName = "<string>";
			attr_ioName.isContainment = true;
			attr_ioName.valueTypeParameter = null;
			type_IODefinition.attributes.add(attr_ioName);
			
			// Attribute Definition: pinport
			AttributeBuilder attr_pinport = new AttributeBuilder("pinport");
			attr_pinport.extensionID = null;
			attr_pinport.categoryName = null;
			attr_pinport.valueTypeName = "<string>";
			attr_pinport.isContainment = true;
			attr_pinport.valueTypeParameter = null;
			type_IODefinition.attributes.add(attr_pinport);
			
			// Attribute Definition: direction
			AttributeBuilder attr_direction = new AttributeBuilder("direction");
			attr_direction.extensionID = null;
			attr_direction.categoryName = null;
			attr_direction.valueTypeName = "<string>";
			attr_direction.isContainment = true;
			attr_direction.valueTypeParameter = null;
			type_IODefinition.attributes.add(attr_direction);
			
			// Attribute Definition: signalType
			AttributeBuilder attr_signalType = new AttributeBuilder("signalType");
			attr_signalType.extensionID = null;
			attr_signalType.categoryName = null;
			attr_signalType.valueTypeName = "<string>";
			attr_signalType.isContainment = true;
			attr_signalType.valueTypeParameter = null;
			type_IODefinition.attributes.add(attr_signalType);
			
			// Attribute Definition: ioDefinition
			AttributeBuilder attr_ioDefinition = new AttributeBuilder("ioDefinition");
			attr_ioDefinition.extensionID = null;
			attr_ioDefinition.categoryName = null;
			attr_ioDefinition.valueTypeName = "<string>";
			attr_ioDefinition.isContainment = true;
			attr_ioDefinition.valueTypeParameter = null;
			type_IODefinition.attributes.add(attr_ioDefinition);
			
			// Attribute Definition: remark
			AttributeBuilder attr_remark = new AttributeBuilder("remark");
			attr_remark.extensionID = null;
			attr_remark.categoryName = null;
			attr_remark.valueTypeName = "<string>";
			attr_remark.isContainment = true;
			attr_remark.valueTypeParameter = null;
			type_IODefinition.attributes.add(attr_remark);
			
		}
		types.add(type_IODefinition);
		
		// Type Definition: ATEIODefinitionView
		TypeBuilder type_ATEIODefinitionView = new TypeBuilder("ATEIODefinitionView");
		type_ATEIODefinitionView.extensionID = null;
		type_ATEIODefinitionView.packageName = null;
		type_ATEIODefinitionView.isAbstract = false;
		type_ATEIODefinitionView.isFinal = false;
		type_ATEIODefinitionView.superTypeNames.add("ModelElement");
		{
			// Attribute Definition: iodefinitions
			AttributeBuilder attr_iodefinitions = new AttributeBuilder("iodefinitions");
			attr_iodefinitions.extensionID = null;
			attr_iodefinitions.categoryName = null;
			attr_iodefinitions.valueTypeName = "<list>";
			attr_iodefinitions.isContainment = true;
			attr_iodefinitions.valueTypeParameter = "IODefinition";
			type_ATEIODefinitionView.attributes.add(attr_iodefinitions);
			
		}
		types.add(type_ATEIODefinitionView);
		
		// Type Definition: PinPort
		TypeBuilder type_PinPort = new TypeBuilder("PinPort");
		type_PinPort.extensionID = null;
		type_PinPort.packageName = null;
		type_PinPort.isAbstract = false;
		type_PinPort.isFinal = false;
		type_PinPort.superTypeNames.add("ModelElement");
		{
			// Attribute Definition: number
			AttributeBuilder attr_number = new AttributeBuilder("number");
			attr_number.extensionID = null;
			attr_number.categoryName = null;
			attr_number.valueTypeName = "<string>";
			attr_number.isContainment = false;
			attr_number.valueTypeParameter = null;
			type_PinPort.attributes.add(attr_number);
			
		}
		types.add(type_PinPort);
		// Type Definition: SignalType
				TypeBuilder type_SignalType = new TypeBuilder("SignalType");
				type_SignalType.extensionID = null;
				type_SignalType.packageName = null;
				type_SignalType.isAbstract = false;
				type_SignalType.isFinal = false;
				type_SignalType.superTypeNames.add("ModelElement");
				{
				}
				types.add(type_SignalType);
		
				
				// Imported Type Definition: Sentence
				TypeBuilder type_Sentence = existingTypes.get("Sentence");
				{
					// Attribute Definition: containate
					AttributeBuilder attr_containate = new AttributeBuilder("containate");
					attr_containate.extensionID = null;
					attr_containate.categoryName = null;
					attr_containate.valueTypeName = "AteSentence";
					attr_containate.isContainment = true;
					attr_containate.valueTypeParameter = null;
					type_Sentence.attributes.add(attr_containate);
					
				}
				
				TypeBuilder type_AteSentence = new TypeBuilder("AteSentence");
				type_AteSentence.extensionID = null;
				type_AteSentence.packageName = null;
				type_AteSentence.isAbstract = false;
				type_AteSentence.isFinal = false;
				type_AteSentence.superTypeNames.add("ModelElement");
				{
					// Attribute Definition: description
					AttributeBuilder attr_description = new AttributeBuilder("description");
					attr_description.extensionID = null;
					attr_description.categoryName = null;
					attr_description.valueTypeName = "<string>";
					attr_description.isContainment = true;
					attr_description.valueTypeParameter = null;
					type_AteSentence.attributes.add(attr_description);
					
					// Attribute Definition: para1
					AttributeBuilder attr_para1 = new AttributeBuilder("para1");
					attr_para1.extensionID = null;
					attr_para1.categoryName = null;
					attr_para1.valueTypeName = "<string>";
					attr_para1.isContainment = true;
					attr_para1.valueTypeParameter = null;
					type_AteSentence.attributes.add(attr_para1);
					
					// Attribute Definition: para2
					AttributeBuilder attr_para2 = new AttributeBuilder("para2");
					attr_para2.extensionID = null;
					attr_para2.categoryName = null;
					attr_para2.valueTypeName = "<string>";
					attr_para2.isContainment = true;
					attr_para2.valueTypeParameter = null;
					type_AteSentence.attributes.add(attr_para2);
					
					// Attribute Definition: para3
					AttributeBuilder attr_para3 = new AttributeBuilder("para3");
					attr_para3.extensionID = null;
					attr_para3.categoryName = null;
					attr_para3.valueTypeName = "<string>";
					attr_para3.isContainment = true;
					attr_para3.valueTypeParameter = null;
					type_AteSentence.attributes.add(attr_para3);
					
					// Attribute Definition: para4
					AttributeBuilder attr_para4 = new AttributeBuilder("para4");
					attr_para4.extensionID = null;
					attr_para4.categoryName = null;
					attr_para4.valueTypeName = "<string>";
					attr_para4.isContainment = true;
					attr_para4.valueTypeParameter = null;
					type_AteSentence.attributes.add(attr_para4);
					
					// Attribute Definition: para5
					AttributeBuilder attr_para5 = new AttributeBuilder("para5");
					attr_para5.extensionID = null;
					attr_para5.categoryName = null;
					attr_para5.valueTypeName = "<string>";
					attr_para5.isContainment = true;
					attr_para5.valueTypeParameter = null;
					type_AteSentence.attributes.add(attr_para5);
					
					// Attribute Definition: para6
					AttributeBuilder attr_para6 = new AttributeBuilder("para6");
					attr_para6.extensionID = null;
					attr_para6.categoryName = null;
					attr_para6.valueTypeName = "<string>";
					attr_para6.isContainment = true;
					attr_para6.valueTypeParameter = null;
					type_AteSentence.attributes.add(attr_para6);
					
				}
				types.add(type_AteSentence);
				
			
		
		return types;
	}
	
	@Override
	public Map<String, Class<? extends ManagedObjectImpl>> loadImplementationClasses() {
		Map<String, Class<? extends ManagedObjectImpl>> map = new HashMap<String, Class<? extends ManagedObjectImpl>>();
		map.put("ATESignalTypeView", ATESignalTypeViewImpl.class);
		map.put("Signal", SignalImpl.class);
		map.put("ATECapbility", ATECapbilityImpl.class);
		map.put("ATECapbilityView", ATECapbilityViewImpl.class);
		map.put("IODefinition", IODefinitionImpl.class);
		map.put("ATEIODefinitionView", ATEIODefinitionViewImpl.class);
		map.put("PinPort", PinPortImpl.class);
		map.put("SignalType", SignalTypeImpl.class);
		map.put("AteSentence", AteSentenceImpl.class);
		return map;
	}
	

}
