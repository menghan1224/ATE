/**
 */
package cn.edu.buaa.sei.lmf.meta.impl;

import cn.edu.buaa.sei.lmf.meta.Attribute;
import cn.edu.buaa.sei.lmf.meta.Category;
import cn.edu.buaa.sei.lmf.meta.EnumValue;
import cn.edu.buaa.sei.lmf.meta.ImportedType;
import cn.edu.buaa.sei.lmf.meta.LMFFactory;
import cn.edu.buaa.sei.lmf.meta.LMFPackage;
import cn.edu.buaa.sei.lmf.meta.Type;
import cn.edu.buaa.sei.lmf.meta.ValueTypeParameter;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LMFFactoryImpl extends EFactoryImpl implements LMFFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LMFFactory init() {
		try {
			LMFFactory theLMFFactory = (LMFFactory)EPackage.Registry.INSTANCE.getEFactory("cn.edu.buaa.sei.lmf.ecore"); 
			if (theLMFFactory != null) {
				return theLMFFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new LMFFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LMFFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case LMFPackage.PACKAGE: return createPackage();
			case LMFPackage.TYPE: return createType();
			case LMFPackage.CATEGORY: return createCategory();
			case LMFPackage.ATTRIBUTE: return createAttribute();
			case LMFPackage.VALUE_TYPE_PARAMETER: return createValueTypeParameter();
			case LMFPackage.IMPORTED_TYPE: return createImportedType();
			case LMFPackage.ENUM: return createEnum();
			case LMFPackage.ENUM_VALUE: return createEnumValue();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public cn.edu.buaa.sei.lmf.meta.Package createPackage() {
		PackageImpl package_ = new PackageImpl();
		return package_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Type createType() {
		TypeImpl type = new TypeImpl();
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Category createCategory() {
		CategoryImpl category = new CategoryImpl();
		return category;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Attribute createAttribute() {
		AttributeImpl attribute = new AttributeImpl();
		return attribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValueTypeParameter createValueTypeParameter() {
		ValueTypeParameterImpl valueTypeParameter = new ValueTypeParameterImpl();
		return valueTypeParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImportedType createImportedType() {
		ImportedTypeImpl importedType = new ImportedTypeImpl();
		return importedType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public cn.edu.buaa.sei.lmf.meta.Enum createEnum() {
		EnumImpl enum_ = new EnumImpl();
		return enum_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumValue createEnumValue() {
		EnumValueImpl enumValue = new EnumValueImpl();
		return enumValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LMFPackage getLMFPackage() {
		return (LMFPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static LMFPackage getPackage() {
		return LMFPackage.eINSTANCE;
	}

} //LMFFactoryImpl
