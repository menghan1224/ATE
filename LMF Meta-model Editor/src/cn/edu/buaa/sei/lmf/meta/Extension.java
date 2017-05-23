/**
 */
package cn.edu.buaa.sei.lmf.meta;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extension</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.Extension#getExtensionID <em>Extension ID</em>}</li>
 * </ul>
 * </p>
 *
 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getExtension()
 * @model abstract="true"
 * @generated
 */
public interface Extension extends EObject {
	/**
	 * Returns the value of the '<em><b>Extension ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extension ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extension ID</em>' attribute.
	 * @see #setExtensionID(String)
	 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getExtension_ExtensionID()
	 * @model
	 * @generated
	 */
	String getExtensionID();

	/**
	 * Sets the value of the '{@link cn.edu.buaa.sei.lmf.meta.Extension#getExtensionID <em>Extension ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extension ID</em>' attribute.
	 * @see #getExtensionID()
	 * @generated
	 */
	void setExtensionID(String value);

} // Extension
