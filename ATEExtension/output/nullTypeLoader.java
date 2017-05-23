import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import cn.edu.buaa.sei.lmf.AttributeBuilder;
import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import cn.edu.buaa.sei.lmf.TypeBuilder;
import cn.edu.buaa.sei.lmf.TypeLoader;

public class nullTypeLoader implements TypeLoader {
	
	
	
	@Override
	public Set<TypeBuilder> loadTypes(Map<String, TypeBuilder> existingTypes) {
		Set<TypeBuilder> types = new HashSet<TypeBuilder>();
		
		// Type Definition: AteSentence
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
		
		return types;
	}
	
	@Override
	public Map<String, Class<? extends ManagedObjectImpl>> loadImplementationClasses() {
		Map<String, Class<? extends ManagedObjectImpl>> map = new HashMap<String, Class<? extends ManagedObjectImpl>>();
		map.put("AteSentence", AteSentenceImpl.class);
		return map;
	}
	
}
