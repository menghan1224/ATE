package cn.edu.buaa.sei.lmf.edit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.graphics.Image;

import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Observer;
import cn.edu.buaa.sei.lmf.Primitives;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;

public class ContainmentNode extends Node {
	
	private static final Map<Attribute, Set<Type>> PREFERRED_CHILD_TYPES;
	
	static {
		PREFERRED_CHILD_TYPES = new HashMap<Attribute, Set<Type>>();
	}
	
	public static void registerPreferredChildTypes(Attribute attr, Set<Type> types) {
		if (PREFERRED_CHILD_TYPES.containsKey(attr)) {
			Set<Type> preferred = PREFERRED_CHILD_TYPES.get(attr);
			preferred.addAll(types);
		} else {
			PREFERRED_CHILD_TYPES.put(attr, new HashSet<Type>(types));
		}
	}
	
	public static Set<Type> getPreferredChildTypes(Attribute attr) {
		if (PREFERRED_CHILD_TYPES.containsKey(attr)) {
			return Collections.unmodifiableSet(PREFERRED_CHILD_TYPES.get(attr));
		} else {
			return null;
		}
	}
	
	private final ManagedObject object;
	private final String key;
	private final Observer observer = new Observer() {
		@Override
		public void notifyChanged(ManagedObject target, String key, ManagedObject value) {
			notifyLabelChanged();
			notifyStructureChanged();
		}
	};

	public ContainmentNode(TreeModel tree, ObjectNode parent, String key) {
		super(tree, parent);
		this.object = parent.getObject();
		this.key = key;
		initNode();
	}
	
	private void initNode() {
		object.addObserver(key, observer);
	}
	
	@Override
	protected void disposeNode() {
		object.removeObserver(key, observer);
	}

	public ManagedObject getObject() {
		return object;
	}
	
	public String getKey() {
		return key;
	}

	@Override
	public Image getImage() {
		return LMFEditorPlugin.getBundleImage("icons/field_protected_obj.gif");
	}
	
	public Attribute getAttribute() {
		Type owner = object.type();
		return owner.getAttribute(key);
	}

	@Override
	public String getText() {
		Attribute attr = object.type().getAttribute(key);
		if (attr.getValueType() == Primitives.LIST) {
			return String.format("%s (%d)", key, object.get(key).listContent().size());
		} else {
			return String.format("%s%s", key, object.get(key) == null ? " (null)" : "");
		}
	}

	@Override
	protected List<Node> createChildren() {
		List<Node> nodes = new ArrayList<Node>();
		Attribute attr = object.type().getAttribute(key);
		if (attr.getValueType() == Primitives.LIST) {
			for (ManagedObject obj : object.get(key).listContent().toArray(new ManagedObject[0])) {
				ObjectNode node = new ObjectNode(getTree(), this, obj);
				nodes.add(node);
			}
		} else {
			ManagedObject obj = object.get(key);
			if (obj != null) {
				ObjectNode node = new ObjectNode(getTree(), this, obj);
				nodes.add(node);
			}
		}
		return nodes;
	}

}
