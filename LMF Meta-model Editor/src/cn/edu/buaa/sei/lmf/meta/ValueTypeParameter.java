/**
 */
package cn.edu.buaa.sei.lmf.meta;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Type Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.ValueTypeParameter#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getValueTypeParameter()
 * @model
 * @generated
 */
public interface ValueTypeParameter extends EObject {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(Type)
	 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getValueTypeParameter_Type()
	 * @model required="true"
	 * @generated
	 */
	Type getType();

	/**
	 * Sets the value of the '{@link cn.edu.buaa.sei.lmf.meta.ValueTypeParameter#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(Type value);

} // ValueTypeParameter
