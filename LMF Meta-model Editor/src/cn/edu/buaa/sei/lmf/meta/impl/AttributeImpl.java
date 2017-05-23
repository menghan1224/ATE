/**
 */
package cn.edu.buaa.sei.lmf.meta.impl;

import cn.edu.buaa.sei.lmf.meta.Attribute;
import cn.edu.buaa.sei.lmf.meta.LMFPackage;
import cn.edu.buaa.sei.lmf.meta.Type;
import cn.edu.buaa.sei.lmf.meta.ValueTypeParameter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.impl.AttributeImpl#getName <em>Name</em>}</li>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.impl.AttributeImpl#getValueType <em>Value Type</em>}</li>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.impl.AttributeImpl#isContainment <em>Containment</em>}</li>
 *   <li>{@link cn.edu.buaa.sei.lmf.meta.impl.AttributeImpl#getValueTypeParameter <em>Value Type Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeImpl extends ExtensionImpl implements Attribute {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getValueType() <em>Value Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueType()
	 * @generated
	 * @ordered
	 */
	protected Type valueType;

	/**
	 * The default value of the '{@link #isContainment() <em>Containment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isContainment()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CONTAINMENT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isContainment() <em>Containment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isContainment()
	 * @generated
	 * @ordered
	 */
	protected boolean containment = CONTAINMENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getValueTypeParameter() <em>Value Type Parameter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueTypeParameter()
	 * @generated
	 * @ordered
	 */
	protected ValueTypeParameter valueTypeParameter;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AttributeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LMFPackage.Literals.ATTRIBUTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LMFPackage.ATTRIBUTE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Type getValueType() {
		if (valueType != null && valueType.eIsProxy()) {
			InternalEObject oldValueType = (InternalEObject)valueType;
			valueType = (Type)eResolveProxy(oldValueType);
			if (valueType != oldValueType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LMFPackage.ATTRIBUTE__VALUE_TYPE, oldValueType, valueType));
			}
		}
		return valueType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Type basicGetValueType() {
		return valueType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValueType(Type newValueType) {
		Type oldValueType = valueType;
		valueType = newValueType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LMFPackage.ATTRIBUTE__VALUE_TYPE, oldValueType, valueType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isContainment() {
		return containment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContainment(boolean newContainment) {
		boolean oldContainment = containment;
		containment = newContainment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LMFPackage.ATTRIBUTE__CONTAINMENT, oldContainment, containment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValueTypeParameter getValueTypeParameter() {
		return valueTypeParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetValueTypeParameter(ValueTypeParameter newValueTypeParameter, NotificationChain msgs) {
		ValueTypeParameter oldValueTypeParameter = valueTypeParameter;
		valueTypeParameter = newValueTypeParameter;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LMFPackage.ATTRIBUTE__VALUE_TYPE_PARAMETER, oldValueTypeParameter, newValueTypeParameter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValueTypeParameter(ValueTypeParameter newValueTypeParameter) {
		if (newValueTypeParameter != valueTypeParameter) {
			NotificationChain msgs = null;
			if (valueTypeParameter != null)
				msgs = ((InternalEObject)valueTypeParameter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LMFPackage.ATTRIBUTE__VALUE_TYPE_PARAMETER, null, msgs);
			if (newValueTypeParameter != null)
				msgs = ((InternalEObject)newValueTypeParameter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LMFPackage.ATTRIBUTE__VALUE_TYPE_PARAMETER, null, msgs);
			msgs = basicSetValueTypeParameter(newValueTypeParameter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LMFPackage.ATTRIBUTE__VALUE_TYPE_PARAMETER, newValueTypeParameter, newValueTypeParameter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LMFPackage.ATTRIBUTE__VALUE_TYPE_PARAMETER:
				return basicSetValueTypeParameter(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case LMFPackage.ATTRIBUTE__NAME:
				return getName();
			case LMFPackage.ATTRIBUTE__VALUE_TYPE:
				if (resolve) return getValueType();
				return basicGetValueType();
			case LMFPackage.ATTRIBUTE__CONTAINMENT:
				return isContainment();
			case LMFPackage.ATTRIBUTE__VALUE_TYPE_PARAMETER:
				return getValueTypeParameter();
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
			case LMFPackage.ATTRIBUTE__NAME:
				setName((String)newValue);
				return;
			case LMFPackage.ATTRIBUTE__VALUE_TYPE:
				setValueType((Type)newValue);
				return;
			case LMFPackage.ATTRIBUTE__CONTAINMENT:
				setContainment((Boolean)newValue);
				return;
			case LMFPackage.ATTRIBUTE__VALUE_TYPE_PARAMETER:
				setValueTypeParameter((ValueTypeParameter)newValue);
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
			case LMFPackage.ATTRIBUTE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case LMFPackage.ATTRIBUTE__VALUE_TYPE:
				setValueType((Type)null);
				return;
			case LMFPackage.ATTRIBUTE__CONTAINMENT:
				setContainment(CONTAINMENT_EDEFAULT);
				return;
			case LMFPackage.ATTRIBUTE__VALUE_TYPE_PARAMETER:
				setValueTypeParameter((ValueTypeParameter)null);
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
			case LMFPackage.ATTRIBUTE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case LMFPackage.ATTRIBUTE__VALUE_TYPE:
				return valueType != null;
			case LMFPackage.ATTRIBUTE__CONTAINMENT:
				return containment != CONTAINMENT_EDEFAULT;
			case LMFPackage.ATTRIBUTE__VALUE_TYPE_PARAMETER:
				return valueTypeParameter != null;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", containment: ");
		result.append(containment);
		result.append(')');
		return result.toString();
	}

} //AttributeImpl
