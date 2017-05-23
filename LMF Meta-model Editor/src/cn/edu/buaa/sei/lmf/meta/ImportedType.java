/**
 */
package cn.edu.buaa.sei.lmf.meta;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Imported Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.ImportedType#getImport <em>Import</em>}</li>
 * </ul>
 * </p>
 *
 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getImportedType()
 * @model
 * @generated
 */
public interface ImportedType extends Type {
	/**
	 * Returns the value of the '<em><b>Import</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Import</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Import</em>' reference.
	 * @see #setImport(Type)
	 * @see cn.edu.buaa.sei.lmf.meta.LMFPackage#getImportedType_Import()
	 * @model required="true"
	 * @generated
	 */
	Type getImport();

	/**
	 * Sets the value of the '{@link cn.edu.buaa.sei.lmf.meta.ImportedType#getImport <em>Import</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Import</em>' reference.
	 * @see #getImport()
	 * @generated
	 */
	void setImport(Type value);

} // ImportedType
