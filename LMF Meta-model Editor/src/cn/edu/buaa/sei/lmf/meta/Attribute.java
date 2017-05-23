/**
 */
package cn.edu.buaa.sei.lmf.meta;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.Attribute#getName <em>Name</em>}</li>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.Attribute#getValueType <em>Value Type</em>}</li>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.Attribute#isContainment <em>Containment</em>}</li>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.Attribute#getValueTypeParameter <em>Value Type Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getAttribute()
 * @model
 * @generated
 */
public interface Attribute extends Extension {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getAttribute_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link cn.edu.buaa.sei.lmf.meta.Attribute#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Value Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Type</em>' reference.
	 * @see #setValueType(Type)
	 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getAttribute_ValueType()
	 * @model required="true"
	 * @generated
	 */
	Type getValueType();

	/**
	 * Sets the value of the '{@link cn.edu.buaa.sei.lmf.meta.Attribute#getValueType <em>Value Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Type</em>' reference.
	 * @see #getValueType()
	 * @generated
	 */
	void setValueType(Type value);

	/**
	 * Returns the value of the '<em><b>Containment</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Containment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Containment</em>' attribute.
	 * @see #setContainment(boolean)
	 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getAttribute_Containment()
	 * @model default="false"
	 * @generated
	 */
	boolean isContainment();

	/**
	 * Sets the value of the '{@link cn.edu.buaa.sei.lmf.meta.Attribute#isContainment <em>Containment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Containment</em>' attribute.
	 * @see #isContainment()
	 * @generated
	 */
	void setContainment(boolean value);

	/**
	 * Returns the value of the '<em><b>Value Type Parameter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value Type Parameter</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Type Parameter</em>' containment reference.
	 * @see #setValueTypeParameter(ValueTypeParameter)
	 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getAttribute_ValueTypeParameter()
	 * @model containment="true"
	 * @generated
	 */
	ValueTypeParameter getValueTypeParameter();

	/**
	 * Sets the value of the '{@link cn.edu.buaa.sei.lmf.meta.Attribute#getValueTypeParameter <em>Value Type Parameter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Type Parameter</em>' containment reference.
	 * @see #getValueTypeParameter()
	 * @generated
	 */
	void setValueTypeParameter(ValueTypeParameter value);

} // Attribute
