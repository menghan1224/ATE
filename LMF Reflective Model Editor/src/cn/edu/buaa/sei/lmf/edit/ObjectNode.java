package cn.edu.buaa.sei.lmf.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Observer;
import cn.edu.buaa.sei.lmf.Primitives;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;
import cn.edu.buaa.sei.lmf.editor.property.IAttributeDescriptor;
import cn.edu.buaa.sei.lmf.editor.property.MultiRefAttributeDescriptor;
import cn.edu.buaa.sei.lmf.editor.property.PrimitiveAttributeDescriptor;
import cn.edu.buaa.sei.lmf.editor.property.PrimitiveListAttributeDescriptor;
import cn.edu.buaa.sei.lmf.editor.property.ReferenceAttributeDescriptor;
import cn.edu.buaa.sei.lmf.runtime.FileResource;

public class ObjectNode extends Node implements IPropertySource, IAdaptable {
	
	private static final Map<Type, NodeDecorator> DESCRIPTORS_MAP;
	
	static {
		DESCRIPTORS_MAP = new HashMap<Type, NodeDecorator>();
	}
	
	public static void initNodeDecoratorMap() {
		Map<Type, NodeDecorator> map = new HashMap<Type, NodeDecorator>();
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(LMFEditorPlugin.PLUGIN_ID + ".nodeDecorator");
		IConfigurationElement[] points = extensionPoint.getConfigurationElements();
		for (IConfigurationElement point : points) {
			try {
				String typeName = point.getAttribute("typeName");
				Type type = LMFContext.typeForName(typeName);
				NodeDecorator desc = (NodeDecorator) point.createExecutableExtension("class");
				map.put(type, desc);
			} catch (Exception ex) {
				LMFEditorPlugin.logError(ex, true);
			}
		}
		
		List<Type> sorted = LMFUtility.sortTypesByInheritance(map.keySet());
		List<Type> allTypes = LMFContext.listTypes(null);
		for (Type key : sorted) {
			NodeDecorator value = map.get(key);
			for (Type type : allTypes) {
				if (type.isOrIsSubtypeOf(key)) {
					DESCRIPTORS_MAP.put(type, value);
				}
			}
		}
	}
	
	public static NodeDecorator getObjectNodeDescriptor(Type type) {
		NodeDecorator rst = DESCRIPTORS_MAP.get(type);
		return rst != null ? rst : DEFAULT_DESCRIPTOR;
	}
	
	private static final NodeDecorator DEFAULT_DESCRIPTOR = new NodeDecorator() {
		
		@Override
		public String[] getLabelRelatedKeys() {
			return new String[0];
		}
		
		@Override
		public String getLabel(ManagedObject object) {
			if (object == null) return "(null)";
			return object.type().getName();
		}
		
		@Override
		public Image getImage(ManagedObject object) {
			return LMFEditorPlugin.getBundleImage("icons/generic_element.gif");
		}
		
		@Override
		public Image getImage(Type type) {
			return LMFEditorPlugin.getBundleImage("icons/generic_element.gif");
		}
		
	};
	
	//// Instance Features ////
	
	private final ManagedObject object;
	private final NodeDecorator descriptor;
	private final Observer labelAttrObserver = new Observer() {
		@Override
		public void notifyChanged(ManagedObject target, String key, ManagedObject value) {
			notifyLabelChanged();
		}
	};
	private final Map<String, IAttributeDescriptor> attrDescriptor;
	
	public ObjectNode(ManagedObject object) {
		this(null, null, object);
	}
	
	/**
	 * This constructor is not intended to be used by clients.
	 * @param tree
	 * @param parent
	 * @param object
	 */
	ObjectNode(TreeModel tree, Node parent, ManagedObject object) {
		super(tree, parent);
		this.object = object;
		this.descriptor = getObjectNodeDescriptor(object.type());
		this.attrDescriptor = new HashMap<String, IAttributeDescriptor>();
		initNode();
	}
	
	private void initNode() {
		if (getTree() != null) {
			String[] relatedKeys = descriptor.getLabelRelatedKeys();
			if (relatedKeys != null && relatedKeys.length > 0) {
				object.addObserver(relatedKeys, labelAttrObserver);
			}
		}
		for (Attribute attr : object.type().getAttributes()) {
			Type valueType = attr.getValueType();
			if (!attr.isContainment()) {
				addAttributeDescriptor(attr);
			} else if (valueType.isPrimitiveType() && valueType != Primitives.LIST) {
				addAttributeDescriptor(attr);
			} else if (valueType == Primitives.LIST && attr.getValueTypeParameter().isPrimitiveType()) {
				addAttributeDescriptor(attr);
			}
		}
	}
	
	public void forceUpdateLabel() {
		notifyLabelChanged();
	}
	
	@Override
	protected void disposeNode() {
		String[] relatedKeys = descriptor.getLabelRelatedKeys();
		if (relatedKeys != null && relatedKeys.length > 0) {
			object.removeObserver(relatedKeys, labelAttrObserver);
		}
	}

	public final ManagedObject getObject() {
		return object;
	}

	@Override
	public Image getImage() {
		return descriptor.getImage(object);
	}

	@Override
	public String getText() {
		return descriptor.getLabel(object);
	}

	private String[] getChildKeys() {
		List<String> keys = new ArrayList<String>();
		for (Attribute attr : object.type().getAttributes()) {
			if (attr.isContainment()) {
				Type valueType = attr.getValueType();
				if (!valueType.isPrimitiveType()) {
					keys.add(attr.getName());
				} else if (valueType == Primitives.LIST && !attr.getValueTypeParameter().isPrimitiveType()) {
					keys.add(attr.getName());
				}
			}
		}
		return keys.toArray(new String[0]);
	}
	
	@Override
	protected List<Node> createChildren() {
		List<Node> nodes = new ArrayList<Node>();
		for (String key : getChildKeys()) {
			ContainmentNode node = new ContainmentNode(getTree(), this, key);
			nodes.add(node);
		}
		return nodes;
	}
	
	//// IPropertySource Methods ////

	@Override
	public Object getEditableValue() {
		return object;
	}

	private void addAttributeDescriptor(Attribute attribute) {
		if (attribute.getValueType() == Primitives.LIST) {
			if (attribute.isContainment()) {
				// primitive-value list
				attrDescriptor.put(attribute.getName(), new PrimitiveListAttributeDescriptor(attribute.getValueTypeParameter(), attribute.getName()));
			} else {
				// reference object list property
				attrDescriptor.put(attribute.getName(), new MultiRefAttributeDescriptor(attribute.getValueTypeParameter(), attribute.getName(), (FileResource) object.resource()));
			}
		} else if (attribute.getValueType().isPrimitiveType()) {
			// bool, int, long, float, double, string
			attrDescriptor.put(attribute.getName(), new PrimitiveAttributeDescriptor(attribute.getName()));
		} else {
			// reference object property
			attrDescriptor.put(attribute.getName(), new ReferenceAttributeDescriptor(attribute.getValueType(), attribute.getName(), (FileResource) object.resource()));
		}
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return attrDescriptor.values().toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		return attrDescriptor.get(id).getAttributeValue(object, (String) id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (getTree() != null && getTree().isEditable()) {
			attrDescriptor.get(id).setAttributeValue(object, (String) id, value);
		}
	}

	@Override
	public boolean isPropertySet(Object id) {
		return attrDescriptor.get(id).canRestoreDefaultValue(object, (String) id);
	}

	@Override
	public void resetPropertyValue(Object id) {
		attrDescriptor.get(id).restoreDefaultValue(object, (String) id);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ManagedObject.class) {
			return getObject();
		} else {
			return null;
		}
	}
	
}
