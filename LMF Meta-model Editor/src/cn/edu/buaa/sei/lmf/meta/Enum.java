/**
 */
package cn.edu.buaa.sei.lmf.meta;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.Enum#getValues <em>Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getEnum()
 * @model
 * @generated
 */
public interface Enum extends Type {
	/**
	 * Returns the value of the '<em><b>Values</b></em>' containment reference list.
	 * The list contents are of type {@link cn.edu.buaa.sei.lmf.meta.EnumValue}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Values</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Values</em>' containment reference list.
	 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getEnum_Values()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<EnumValue> getValues();

} // Enum
