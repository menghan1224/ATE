package cn.edu.buaa.sei.lmf;

import java.util.Map;
import java.util.Set;

public interface TypeLoader {
	
	public Set<TypeBuilder> loadTypes(Map<String, TypeBuilder> existingTypes);
	
	public Map<String, Class<? extends ManagedObjectImpl>> loadImplementationClasses();

}
