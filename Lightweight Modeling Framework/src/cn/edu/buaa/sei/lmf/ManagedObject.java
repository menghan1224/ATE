package cn.edu.buaa.sei.lmf;

import java.util.Collection;
import java.util.List;

/**
 * <code>ManagedObject</code> is the model instance presentation in LMF. Each model element is a single <code>ManagedObject</code>
 * object, and it can be compared with other obejct by "==" operator.
 * Basically, each <code>ManagedObject</code> instance is a key-value store that maps the attribute name (key) to its value.
 * <p>
 * To determine the <code>Type</code> of the object, use {@link #type()} and {@link #isKindOf(Type)}. To get or set the attribute value,
 * use {@link #get(String)} and {@link #set(String, ManagedObject)}. To observe the attribute change, use {@link #addObserver(String, Observer)}.
 * <p>
 * Note: This interface does not intend to be implemented by clients. Clients should create new instance by {@link LMFContext#newInstance(Type)}.
 * @author Gong Zhang
 */
public interface ManagedObject {

	/**
	 * Returns the {@link Type} (in other words, meta-class) of this object.
	 * @return
	 */
	public Type type();
	/**
	 * Determines this object whether conforms to the given {@link Type} or not.
	 * The implementation is:
	 * <p>
	 * <code>type() == type || type().isSubtypeOf(type)</code>
	 * @param type
	 * @return
	 */
	public boolean isKindOf(Type type);
	public boolean isKindOf(String typeName);
	
	/**
	 * Retrieves the attribute value by its name.
	 * @param key the attribute name
	 * @return the attribute value
	 * @see {@link #boolValue()}
	 * @see {@link #intValue()}
	 * @see {@link #longValue()}
	 * @see {@link #floatValue()}
	 * @see {@link #doubleValue()}
	 * @see {@link #stringValue()}
	 * @see {@link #listContent()}
	 */
	public ManagedObject get(String key);
	
	/**
	 * Sets the value for specified attribute.
	 * @param key the attribute name
	 * @param value the new attribute value
	 * @see {@link #set(String, boolean)} setter for the attribute with {@link Primitives#BOOL} value type
	 * @see {@link #set(String, int)} setter for the attribute with {@link Primitives#INT} and {@link Primitives#ENUM} value type
	 * @see {@link #set(String, long)} setter for the attribute with {@link Primitives#LONG} value type
	 * @see {@link #set(String, float)} setter for the attribute with {@link Primitives#FLOAT} value type
	 * @see {@link #set(String, double)} setter for the attribute with {@link Primitives#DOUBLE} value type
	 * @see {@link #set(String, String)} setter for the attribute with {@link Primitives#STRING} value type
	 * @see {@link #set(String, Collection) setter for the attribute with {@link Primitives#LIST} value type
	 */
	public void set(String key, ManagedObject value);
	public void set(String key, boolean value);
	public void set(String key, int value);
	public void set(String key, long value);
	public void set(String key, float value);
	public void set(String key, double value);
	public void set(String key, String value);
	public void set(String key, Collection<? extends ManagedObject> objs);
	
	/**
	 * Retrieves the primitive value for {@link Primitives#BOOL} instance.
	 * @return
	 */
	public boolean boolValue();
	/**
	 * Retrieves the primitive value for {@link Primitives#INT} or {@link Primitives#ENUM}} instance.
	 * @return
	 */
	public int intValue();
	/**
	 * Retrieves the primitive value for {@link Primitives#LONG} instance.
	 * @return
	 */
	public long longValue();
	/**
	 * Retrieves the primitive value for {@link Primitives#FLOAT} instance.
	 * @return
	 */
	public float floatValue();
	/**
	 * Retrieves the primitive value for {@link Primitives#DOUBLE} instance.
	 * @return
	 */
	public double doubleValue();
	/**
	 * Retrieves the primitive value for {@link Primitives#STRING} instance.
	 * @return
	 */
	public String stringValue();
	/**
	 * Retrieves the Java list presentation for {@link Primitives#LIST} instance.
	 * <p>
	 * The change made on the Java list will be reflected to LMF object.
	 * @return
	 */
	public ManagedList listContent();
	public <E extends ManagedObject> List<E> listContent(Class<E> clazz);
	
	public ManagedObject[] oppositeObjects(Attribute attribute);
	public ManagedObject[] oppositeObjects(String typeName, String key);
	public ManagedObject oppositeObject(Attribute attribute);
	public ManagedObject oppositeObject(String typeName, String key);
	
	/**
	 * Add an {@link Observer} for an attribute. When the attribute value changed, the {@link Observer} will get notification
	 * on the same thread.
	 * <p>
	 * The {@link Observer} should be manually remove from the object when not needed. See {@link #removeObserver(String, Observer)}.
	 * @param key the attribute name
	 * @param observer observer object
	 */
	public void addObserver(String key, Observer observer);
	public void removeObserver(String key, Observer observer);
	public void addObserver(String[] keys, Observer observer);
	public void removeObserver(String[] keys, Observer observer);
	public void set(AttributeSetter setter);
	
	public void addOwnerObserver(OwnerObserver observer);
	public void removeOwnerObserver(OwnerObserver observer);
	public void addListObserver(String key, ListObserver observer);
	public void removeListObserver(String key, ListObserver observer);
	
	/**
	 * Debug description for this object.
	 * @return
	 */
	public String description();
	
	public ManagedObject owner();
	public LMFResource resource();
	
}
