package cn.edu.buaa.sei.lmf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Primitives {
	
	public static final Type BOOL = new Primitive("<bool>");
	public static final Type INT = new Primitive("<int>");
	public static final Type LONG = new Primitive("<long>");
	public static final Type FLOAT = new Primitive("<float>");
	public static final Type DOUBLE = new Primitive("<double>");
	public static final Type STRING = new Primitive("<string>");
	public static final Type LIST = new Primitive("<list>");
	public static final Type ENUM = new Primitive("<enum>");
	
	public static final Collection<ManagedObject> EMPTY_OBJECT_COLLECTION = Collections.unmodifiableCollection(new ArrayList<ManagedObject>(0));

	private Primitives() {
	}
	
	public static ManagedObject newInstance(boolean value) {
		return new ManagedObjectImpl(BOOL, value);
	}
	
	public static ManagedObject newInstance(int value) {
		return new ManagedObjectImpl(INT, value);
	}
	
	public static ManagedObject newInstance(long value) {
		return new ManagedObjectImpl(LONG, value);
	}
	
	public static ManagedObject newInstance(float value) {
		return new ManagedObjectImpl(FLOAT, value);
	}
	
	public static ManagedObject newInstance(double value) {
		return new ManagedObjectImpl(DOUBLE, value);
	}
	
	public static ManagedObject newInstance(String value) {
		if (value == null) {
			throw new IllegalArgumentException("string primitive value is must not null.");
		}
		return new ManagedObjectImpl(STRING, value);
	}
	
	static ManagedObject newListInstance(Collection<? extends ManagedObject> initValue) {
		List<ManagedObject> list = new ArrayList<ManagedObject>(initValue.size());
		if (initValue != null) {
			list.addAll(initValue);
		}
		ManagedList.ListPrimitive primitive = new ManagedList.ListPrimitive(list);
		return new ManagedObjectImpl(LIST, primitive);
	}
	
	static ManagedObject newEnumInstance(int value) {
		return new ManagedObjectImpl(ENUM, value);
	}
	
	private static final class Primitive implements Type {
		
		private static final Set<Type> EMPTY_TYPE_SET = new HashSet<Type>(0);
		private static final Set<Attribute> EMPTY_ATTRIBUTE_SET = new HashSet<Attribute>(0);
		
		private final String name;
		
		Primitive(String name) {
			this.name = name;
		}
		
		@Override
		public String getExtensionID() {
			return LMFContext.STANDARD_EXTENSION_ID;
		}

		@Override
		public String getPackageName() {
			return LMFContext.DEFAULT_PACKAGE_NAME;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Set<Type> getSuperTypes() {
			return EMPTY_TYPE_SET;
		}

		@Override
		public Collection<Attribute> getAttributes() {
			return EMPTY_ATTRIBUTE_SET;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public String description() {
			return name + " -> () {\n}";
		}

		@Override
		public Attribute getAttribute(String name) {
			return null;
		}
		
		@Override
		public boolean isOrIsSubtypeOf(Type type) {
			return type == this;
		}

		@Override
		public boolean isSubtypeOf(Type type) {
			return false;
		}

		@Override
		public boolean isPrimitiveType() {
			return true;
		}

		@Override
		public boolean isAbstract() {
			return false;
		}

		@Override
		public boolean isFinal() {
			return true;
		}
		
		@Override
		public String[] getEnumValues() {
			return null;
		}
		
	}
	
}
