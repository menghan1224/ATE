package cn.edu.buaa.sei.lmf;

/**
 * This interface does not intend to be implemented by clients. To retrieve an <code>Attribute</code>, use {@link Type#getAttribute(String)} or {@link Type#getAttributes()}.
 * @author Gong Zhang
 * @see {@link #getName()}
 * @see {@link #getValueType()}
 * @see {@link #getValueTypeParameter()}
 */
public interface Attribute {
	
	public Type getOwnerType();
	public String getExtensionID();
	
	/**
	 * Retrieves the name of this <code>Attribute</code>.
	 * <p>
	 * The attribute name also called a "key" when use {@link ManagedObject}. 
	 * @return
	 * @see {@link ManagedObject#get(String)} the attribute getter for <code>ManagedObject</code>
	 * @see {@link ManagedObject#set(String, ManagedObject)} the attribute setter for <code>ManagedObject</code>
	 * @see {@link #getValueTypeParameter()} for value type parameter details
	 */
	public String getName();
	/**
	 * Retrieves the return value type of this attribute.
	 * @return
	 */
	public Type getValueType();
	public boolean isContainment();
	/**
	 * If the value type of this attribute is {@link Primitives#LIST}, this method will return the element type in that list;
	 * <p>
	 * If the value type of this attribute is {@link Primitives#ENUM}, this method will return the concrete enumeration type;
	 * <p>
	 * if the value type is other types, this method will always return <code>null</code>.
	 * @return
	 */
	public Type getValueTypeParameter();
	
	public String description();
	
}
