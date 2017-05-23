/**
 */
package cn.edu.buaa.sei.lmf.meta.impl;

import cn.edu.buaa.sei.lmf.meta.Extension;
import cn.edu.buaa.sei.lmf.meta.LMFPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extension</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.impl.ExtensionImpl#getExtensionID <em>Extension ID</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ExtensionImpl extends EObjectImpl implements Extension {
	/**
	 * The default value of the '{@link #getExtensionID() <em>Extension ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtensionID()
	 * @generated
	 * @ordered
	 */
	protected static final String EXTENSION_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExtensionID() <em>Extension ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtensionID()
	 * @generated
	 * @ordered
	 */
	protected String extensionID = EXTENSION_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExtensionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LMFPackage.Literals.EXTENSION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExtensionID() {
		return extensionID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtensionID(String newExtensionID) {
		String oldExtensionID = extensionID;
		extensionID = newExtensionID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LMFPackage.EXTENSION__EXTENSION_ID, oldExtensionID, extensionID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case LMFPackage.EXTENSION__EXTENSION_ID:
				return getExtensionID();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case LMFPackage.EXTENSION__EXTENSION_ID:
				setExtensionID((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case LMFPackage.EXTENSION__EXTENSION_ID:
				setExtensionID(EXTENSION_ID_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case LMFPackage.EXTENSION__EXTENSION_ID:
				return EXTENSION_ID_EDEFAULT == null ? extensionID != null : !EXTENSION_ID_EDEFAULT.equals(extensionID);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (extensionID: ");
		result.append(extensionID);
		result.append(')');
		return result.toString();
	}

} //ExtensionImpl
