package cn.edu.buaa.sei.rucm.spec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import cn.edu.buaa.sei.rucm.RUCMPlugin;

public final class TextCellDecoratorRegistry {
	
	public static enum ElementType {
		
		BRIEF_DESCRIPTION("briefDescription"),
		PRECONDITION("precondition"),
		PRIMARY_ACTOR("primaryActor"),
		SECONDARY_ACTORS("secondaryActor"),
		DEPENDENCY("dependency"),
		GENERALIZATION("dependency"),
		RFS("rfs"),
		GUARD_CONDITION("guardCondition"),
		STEP("step"),
		POSTCONDITION("postcondition"),
		;
		
		private final String elementName;
		private ElementType(String elementName) {
			this.elementName = elementName;
		}
		public String elementName() {
			return elementName;
		}
	}

	private TextCellDecoratorRegistry() {
	}
	
	private static final Map<String, List<IConfigurationElement>> ELEMENTS_MAP = new HashMap<String, List<IConfigurationElement>>();
	
	public static void registerDecorator(IConfigurationElement element) {
		String key = element.getName();
		List<IConfigurationElement> list = ELEMENTS_MAP.get(key);
		if (list == null) {
			list = new ArrayList<IConfigurationElement>();
			ELEMENTS_MAP.put(key, list);
		}
		list.add(element);
	}
	
	public static List<TextCellDecorator> createDecorators(ElementType type) {
		List<IConfigurationElement> list = ELEMENTS_MAP.get(type.elementName());
		if (list == null) return null;
		List<TextCellDecorator> decorators = new ArrayList<TextCellDecorator>(list.size());
		for (IConfigurationElement element : list) {
			try {
				TextCellDecorator decorator = (TextCellDecorator) element.createExecutableExtension("class");
				decorators.add(decorator);
			} catch (CoreException ex) {
				RUCMPlugin.logError(ex, true);
			}
		}
		return decorators;
	}
	
}
