package cn.edu.buaa.sei.lmf;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.buaa.sei.lmf.LMFUtility.ObjectFilter;

public class ManagedObjectImpl implements ManagedObject, Serializable {
	
	private static final long serialVersionUID = -7685795541026579135L;
	
	private final String typeName;
	private final Map<String, ManagedObject> store; // only valid for non-primitive types
	final Object primitive; // only valid for primitive types
	ManagedObject owner = null;
	
	private transient Type cachedType;
	private transient ObserverBundle observerBundle;
	
	/**
	 * Constructor for primitive object.
	 * @param type
	 * @param primitiveValue
	 */
	ManagedObjectImpl(Type type, Object primitiveValue) {
		this.typeName = type.getName();
		this.store = null;
		this.primitive = primitiveValue;
		this.observerBundle = null;
		this.cachedType = type;
	}
	
	/**
	 * Constructor for non-primitive object.
	 * @param type
	 */
	protected ManagedObjectImpl(Type type) {
		if (type.isPrimitiveType()) throw new IllegalArgumentException("can not create primitive instance by this constructor.");
		if (type.isAbstract()) throw new IllegalArgumentException("can not create instance from abstract type");
		this.typeName = type.getName();
		this.cachedType = type;
		this.store = new HashMap<String, ManagedObject>(type.getAttributes().size());
		this.primitive = null;
		for (Attribute attr : type.getAttributes()) {
			Type attrType = attr.getValueType();
			if (!attrType.isPrimitiveType()) {
				this.store.put(attr.getName(), null);
			} else {
				if (attrType == Primitives.BOOL) {
					ManagedObjectImpl attrValue = (ManagedObjectImpl) Primitives.newInstance(false);
					if (attr.isContainment()) attrValue.owner = this;
					this.store.put(attr.getName(), attrValue);
				} else if (attrType == Primitives.INT) {
					ManagedObjectImpl attrValue = (ManagedObjectImpl) Primitives.newInstance(0);
					if (attr.isContainment()) attrValue.owner = this;
					this.store.put(attr.getName(), attrValue);
				} else if (attrType == Primitives.LONG) {
					ManagedObjectImpl attrValue = (ManagedObjectImpl) Primitives.newInstance(0L);
					if (attr.isContainment()) attrValue.owner = this;
					this.store.put(attr.getName(), attrValue);
				} else if (attrType == Primitives.FLOAT) {
					ManagedObjectImpl attrValue = (ManagedObjectImpl) Primitives.newInstance(0.0f);
					if (attr.isContainment()) attrValue.owner = this;
					this.store.put(attr.getName(), attrValue);
				} else if (attrType == Primitives.DOUBLE) {
					ManagedObjectImpl attrValue = (ManagedObjectImpl) Primitives.newInstance(0.0);
					if (attr.isContainment()) attrValue.owner = this;
					this.store.put(attr.getName(), attrValue);
				} else if (attrType == Primitives.STRING) {
					ManagedObjectImpl attrValue = (ManagedObjectImpl) Primitives.newInstance("");
					if (attr.isContainment()) attrValue.owner = this;
					this.store.put(attr.getName(), attrValue);
				} else if (attrType == Primitives.LIST) {
					ManagedObjectImpl list = (ManagedObjectImpl) Primitives.newListInstance(Primitives.EMPTY_OBJECT_COLLECTION);
					list.owner = this;
					ManagedList.ListPrimitive pr = (ManagedList.ListPrimitive) list.primitive;
					pr.target = this;
					pr.key = attr.getName();
					pr.containment = attr.isContainment();
					pr.contentType = attr.getValueTypeParameter();
					this.store.put(attr.getName(), list);
				} else if (attrType == Primitives.ENUM) {
					ManagedObjectImpl attrValue = (ManagedObjectImpl) Primitives.newEnumInstance(0);
					if (attr.isContainment()) attrValue.owner = this;
					this.store.put(attr.getName(), attrValue);
				} else {
					throw new IllegalArgumentException(String.format("unknown primitive type: \"%s\"", attrType.getName()));
				}
			}
		}
	}
	
	public synchronized final Type type() {
		if (cachedType == null) {
			cachedType = LMFContext.typeForName(typeName);
		}
		return cachedType;
	}
	
	public synchronized final boolean isKindOf(Type type) {
		return type() == type || type().isSubtypeOf(type);
	}
	
	public synchronized final boolean isKindOf(String typeName) {
		return isKindOf(LMFContext.typeForName(typeName));
	}
	
	public synchronized final ManagedObject get(String key) {
		if (store == null) throw new UnsupportedOperationException("primitive object has no attributes.");
		validateKey(key);
		return store.get(key);
	}
	
	public synchronized final void set(String key, ManagedObject value) {
		LMFResource resource = resource();
		if (resource != null && !resource.isEditable()) {
			throw new UnsupportedOperationException("resource is not editable.");
		}
		if (store == null) throw new UnsupportedOperationException("primitive object has no attributes.");
		validateKey(key);
		Attribute attr = type().getAttribute(key);
		Type valueType = attr.getValueType();
		if (valueType == Primitives.LIST) {
			throw new UnsupportedOperationException("can not set value for list attribute.");
		}
		if (value == null && valueType.isPrimitiveType()) {
			throw new IllegalArgumentException("can not set null value for primitive attribute.");
		}
		if (value != null && valueType == Primitives.ENUM) {
			if (value.isKindOf(Primitives.INT)) {
				int enumValue = value.intValue();
				Type para = attr.getValueTypeParameter();
				if (para != null && para.getEnumValues() != null) {
					if (enumValue < 0 || enumValue >= para.getEnumValues().length) {
						throw new IllegalArgumentException("unknown enum value for " + para.getName());
					}
				} else {
					throw new IllegalArgumentException("enum value type is not specified: " + attr.getName());
				}
				value = Primitives.newEnumInstance(enumValue);
			} else if (value.isKindOf(Primitives.ENUM)) {
				int enumValue = value.intValue();
				Type para = attr.getValueTypeParameter();
				if (para != null && para.getEnumValues() != null) {
					if (enumValue < 0 || enumValue >= para.getEnumValues().length) {
						throw new IllegalArgumentException("unknown enum value for " + para.getName());
					}
				} else {
					throw new IllegalArgumentException("enum value type is not specified: " + attr.getName());
				}
			} else {
				throw new IllegalArgumentException("enum attribute only accept <int> primitive");
			}
		}
		if (value != null && !value.isKindOf(attr.getValueType())) {
			throw new IllegalArgumentException(String.format("attribute value type \"%s\" is mismatch with \"%s\"", attr.getValueType().getName(), value.type().getName()));
		}
		if (attr.isContainment()) {
			ManagedObjectImpl oldValue = (ManagedObjectImpl) store.get(key);
			if (oldValue != null) {
				oldValue.owner = null;
				oldValue.notifyOwnerChanged();
			}
		}
		store.put(key, value);
		if (attr.isContainment() && value != null) {
			if (value.owner() != null) {
				throw new IllegalArgumentException("duplicate containment relationship");
			}
			((ManagedObjectImpl) value).owner = this;
			((ManagedObjectImpl) value).notifyOwnerChanged();
		}
		notifyAttributeValueChanged(key, value);
	}
	
	public synchronized final void set(String key, boolean value) {
		set(key, Primitives.newInstance(value));
	}
	
	public synchronized final void set(String key, int value) {
		set(key, Primitives.newInstance(value));
	}
	
	public synchronized final void set(String key, long value) {
		set(key, Primitives.newInstance(value));
	}
	
	public synchronized final void set(String key, float value) {
		set(key, Primitives.newInstance(value));
	}
	
	public synchronized final void set(String key, double value) {
		set(key, Primitives.newInstance(value));
	}
	
	public synchronized final void set(String key, String value) {
		set(key, Primitives.newInstance(value));
	}
	
	public synchronized final void set(String key, final Collection<? extends ManagedObject> objs) {
		final List<ManagedObject> list = get(key).listContent();
		set(new AttributeSetter() {
			@Override
			public void apply(ManagedObject target) {
				list.clear();
				list.addAll(objs);
			}
		});
	}
	
	public synchronized final boolean boolValue() {
		if (type() == Primitives.BOOL) {
			return (Boolean) this.primitive;
		} else {
			throw createPrimitiveTypeException(Primitives.BOOL);
		}
	}
	
	public synchronized final int intValue() {
		if (type() == Primitives.INT || type() == Primitives.ENUM) {
			return (Integer) this.primitive;
		} else {
			throw createPrimitiveTypeException(Primitives.INT);
		}
	}
	
	public synchronized final long longValue() {
		if (type() == Primitives.LONG) {
			return (Long) this.primitive;
		} else {
			throw createPrimitiveTypeException(Primitives.LONG);
		}
	}
	
	public synchronized final float floatValue() {
		if (type() == Primitives.FLOAT) {
			return (Float) this.primitive;
		} else {
			throw createPrimitiveTypeException(Primitives.FLOAT);
		}
	}
	
	public synchronized final double doubleValue() {
		if (type() == Primitives.DOUBLE) {
			return (Double) this.primitive;
		} else {
			throw createPrimitiveTypeException(Primitives.DOUBLE);
		}
	}
	
	public synchronized final String stringValue() {
		if (type() == Primitives.STRING) {
			return (String) this.primitive;
		} else {
			throw createPrimitiveTypeException(Primitives.STRING);
		}
	}
	
	public synchronized final ManagedList listContent() {
		if (type() == Primitives.LIST) {
			return new ManagedList(this);
		} else {
			throw createPrimitiveTypeException(Primitives.LIST);
		}
	}
	
	public synchronized final <E extends ManagedObject> List<E> listContent(Class<E> clazz) {
		return listContent().toGenericList(clazz);
	}
	
	public synchronized final void addObserver(String key, Observer observer) {
		getObserverBundle().addObserver(key, observer);
	}
	
	public synchronized final void removeObserver(String key, Observer observer) {
		getObserverBundle().removeObserver(key, observer);
	}
	
	public synchronized final void addObserver(String[] keys, Observer observer) {
		for (String key : keys) getObserverBundle().addObserver(key, observer);
	}
	
	public synchronized final void removeObserver(String[] keys, Observer observer) {
		for (String key : keys) getObserverBundle().removeObserver(key, observer);
	}
	
	public synchronized final void addListObserver(String key, ListObserver observer) {
		Attribute attr = type().getAttribute(key);
		if (attr == null) throw new IllegalArgumentException("unknown key: " + key);
		if (attr.getValueType() != Primitives.LIST) throw new IllegalArgumentException("the attribute value type is not a list for key: " + key);
		getObserverBundle().addListObserver(key, observer);
	}
	
	public synchronized final void removeListObserver(String key, ListObserver observer) {
		getObserverBundle().removeListObserver(key, observer);
	}
	
	public synchronized final void set(AttributeSetter setter) {
		getObserverBundle().retainNotifications();
		setter.apply(this);
		getObserverBundle().releaseNotifications();
	}
	
	public synchronized final void addOwnerObserver(OwnerObserver observer) {
		getObserverBundle().addOwnerObserver(observer);
	}
	
	public synchronized final void removeOwnerObserver(OwnerObserver observer) {
		getObserverBundle().removeOwnerObserver(observer);
	}
	
	private UnsupportedOperationException createPrimitiveTypeException(Type primitiveType) {
		return new UnsupportedOperationException(String.format("object with type \"%s\" is not a instance of \"%s\"", type().getName(), primitiveType.getName()));
	}
	
	private void validateKey(String key) {
		if (key == null) {
			throw new IllegalArgumentException("null key is invalid.");
		}
		if (!store.containsKey(key)) {
			throw new IllegalArgumentException(String.format("undefined key \"%s\" on type \"%s\".", key, typeName));
		}
	}
	
	private ObserverBundle getObserverBundle() {
		if (observerBundle == null) {
			observerBundle = new ObserverBundle(this);
		}
		return observerBundle;
	}
	
	synchronized void notifyListContentChanged(ManagedObject[] added, ManagedObject[] removed) {
		ManagedList.ListPrimitive pr = (ManagedList.ListPrimitive) this.primitive;
		if (pr.target != null) {
			if (pr.target.observerBundle != null) {
				pr.target.observerBundle.notifyListChanged(pr.key, added, removed);
			}
			pr.target.notifyAttributeValueChanged(pr.key, this);
		}
	}

	private synchronized void notifyAttributeValueChanged(String key, ManagedObject value) {
		if (observerBundle != null) {
			observerBundle.notify(key, value);
		}
		LMFResource resource = resource();
		if (resource != null) resource.notifyModelChanged(this, key, value);
	}
	
	synchronized void notifyOwnerChanged() {
		if (observerBundle != null) {
			observerBundle.notifyOwnerChanged(owner);
		}
	}
	
	@Override
	public synchronized String toString() {
		if (type().isPrimitiveType()) {
			if (type() == Primitives.LIST) {
				return ((ManagedList.ListPrimitive) this.primitive).content.toString();
			} else {
				return this.primitive.toString();
			}
		} else {
			return String.format("%s@%s", type().getName(), Integer.toHexString(super.hashCode()));
		}
	}
	
	public synchronized String description() {
		if (type().isPrimitiveType()) return toString();
		else {
			StringBuilder sb = new StringBuilder();
			sb.append(toString());
			sb.append(" {\n");
			for (Attribute attr : type().getAttributes()) {
				ManagedObject value = get(attr.getName());
				sb.append(String.format("\t%s %s %s;\n", attr.getName(), attr.isContainment() ? "=" : "->", value == null ? "<null>" : value.toString()));
			}
			sb.append("}");
			return sb.toString();
		}
	}

	@Override
	public synchronized final ManagedObject owner() {
		return owner;
	}

	@Override
 	public synchronized final LMFResource resource() {
		if (owner == null) return LMFResource.getResource(this);
		else return owner.resource();
	}
	
	@Override
	public final boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public final int hashCode() {
		return super.hashCode();
	}
	
	private ManagedObject[] oppositeObjects(final Attribute attribute, boolean findAll) {
		// XXX: maybe need to improve the performance
		LMFResource resource = resource();
		if (resource == null) throw new UnsupportedOperationException("resource must not be null.");
		ManagedObject root = resource.getRootObject();
		if (attribute.getValueType() == Primitives.LIST) {
			if (attribute.getValueTypeParameter() != null) {
				if (!this.type().isOrIsSubtypeOf(attribute.getValueTypeParameter())) {
					throw new IllegalArgumentException();
				}
			}
		} else if (!this.type().isOrIsSubtypeOf(attribute.getValueType())) {
			throw new IllegalArgumentException();
		}
		final Type targetType = attribute.getOwnerType();
		ObjectFilter filter = new ObjectFilter() {
			@Override
			public boolean accept(ManagedObject obj) {
				if (obj.isKindOf(targetType)) {
					if (attribute.getValueType() == Primitives.LIST) {
						return obj.get(attribute.getName()).listContent().contains(ManagedObjectImpl.this);
					} else {
						return obj.get(attribute.getName()) == ManagedObjectImpl.this;
					}
				}
				return false;
			}
		};
		return findAll ? LMFUtility.findObjects(root, filter) : new ManagedObject[] { LMFUtility.findObject(root, filter) };
	}
	
	@Override
	public ManagedObject[] oppositeObjects(final Attribute attribute) {
		return oppositeObjects(attribute, true);
	}
	
	public final ManagedObject[] oppositeObjects(String typeName, String key) {
		return oppositeObjects(LMFContext.typeForName(typeName).getAttribute(key));
	}

	@Override
	public ManagedObject oppositeObject(Attribute attribute) {
		return oppositeObjects(attribute, false)[0];
	}

	@Override
	public ManagedObject oppositeObject(String typeName, String key) {
		return oppositeObject(LMFContext.typeForName(typeName).getAttribute(key));
	}
	
}
