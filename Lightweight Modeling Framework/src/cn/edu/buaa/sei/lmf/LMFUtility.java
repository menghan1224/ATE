package cn.edu.buaa.sei.lmf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class LMFUtility {
	
	public static interface ObjectFilter { boolean accept(ManagedObject obj); }

	/**
	 * Retrieves the containment closure of specified object.
	 * @param obj
	 * @return all objects contained by given {@code obj} and {@code obj} itself.
	 */
	public static ManagedObject[] getContainmentClosure(ManagedObject obj) {
		List<ManagedObject> closure = new ArrayList<ManagedObject>();
		getContainmentClosure(obj, closure);
		return closure.toArray(new ManagedObject[closure.size()]);
	}
	
	/**
	 * Removes object and all references of specified object on whole model tree.
	 * @param root
	 * @param obj
	 */
	public static void removeObject(ManagedObject root, ManagedObject obj) {
		ManagedObject owner = obj.owner();
		ManagedObject[] containments = getContainmentClosure(obj);
		if (owner != null) {
			for (Attribute attr : owner.type().getAttributes()) {
				if (attr.isContainment()) {
					if (attr.getValueType() == Primitives.LIST &&
						(attr.getValueTypeParameter() == obj.type() || obj.type().isSubtypeOf(attr.getValueTypeParameter()))) {
						if (owner.get(attr.getName()).listContent().remove(obj)) {
							break;
						}
					} else if (attr.getValueType() == obj.type() || obj.type().isSubtypeOf(attr.getValueType())) {
						if (owner.get(attr.getName()) == obj) {
							owner.set(attr.getName(), (ManagedObject) null);
							break;
						}
					}
				}
			}
		}
		for (ManagedObject containment : containments) {
			removeObjectReferences(root, containment);
		}
	}
	
	/**
	 * Removes all references of specified object on whole model tree.
	 * @param root
	 * @param ref
	 */
	public static void removeObjectReferences(ManagedObject root, ManagedObject ref) {
		for (Attribute attr : root.type().getAttributes()) {
			if (!attr.isContainment()) {
				if (attr.getValueType() == Primitives.LIST) {
					ManagedList list = root.get(attr.getName()).listContent();
					list.remove(ref);
				} else {
					ManagedObject o = root.get(attr.getName());
					if (o == ref) root.set(attr.getName(), (ManagedObject) null);
				}
			} else {
				if (attr.getValueType() == Primitives.LIST) {
					ManagedList list = root.get(attr.getName()).listContent();
					for (ManagedObject o : list) {
						removeObjectReferences(o, ref);
					}
				} else {
					ManagedObject o = root.get(attr.getName());
					if (o != null) removeObjectReferences(o, ref);
				}
			}
		}
	}
	
	/**
	 * Retrieves all objects accepted by given filter.
	 * @param root
	 * @param filter
	 * @return
	 */
	public static ManagedObject[] findObjects(ManagedObject root, ObjectFilter filter) {
		List<ManagedObject> rst = new ArrayList<ManagedObject>();
		findObjects(root, filter, rst);
		return rst.toArray(new ManagedObject[rst.size()]);
	}
	
	/**
	 * Retrieves the first occurred object accepted by given filter.
	 * @param root
	 * @param filter
	 * @return object accepted by given {@code filter}, or {@code null} if none.
	 */
	public static ManagedObject findObject(ManagedObject root, ObjectFilter filter) {
		// XXX: change to standard widely search
		if (filter.accept(root)) return root;
		for (Attribute attr : root.type().getAttributes()) {
			if (attr.isContainment()) {
				if (attr.getValueType() == Primitives.LIST) {
					ManagedList list = root.get(attr.getName()).listContent();
					for (ManagedObject o : list) {
						if (filter.accept(o)) return o;
					}
				} else {
					ManagedObject o = root.get(attr.getName());
					if (o != null) {
						if (filter.accept(o)) return o;
					}
				}
			}
		}
		for (Attribute attr : root.type().getAttributes()) {
			if (attr.isContainment()) {
				if (attr.getValueType() == Primitives.LIST) {
					ManagedList list = root.get(attr.getName()).listContent();
					for (ManagedObject o : list) {
						ManagedObject rst = findObject(o, filter);
						if (rst != null) return rst;
					}
				} else {
					ManagedObject o = root.get(attr.getName());
					if (o != null) {
						ManagedObject rst = findObject(o, filter);
						if (rst != null) return rst;
					}
				}
			}
		}
		return null;
	}
	
	public static ManagedObject deepCopyObject(ManagedObject obj) {
		if (obj.type().isPrimitiveType()) {
			if (obj.type() == Primitives.BOOL) {
				return Primitives.newInstance(obj.boolValue());
			} else if (obj.type() == Primitives.INT || obj.type() == Primitives.ENUM) {
				return Primitives.newInstance(obj.intValue());
			} else if (obj.type() == Primitives.LONG) {
				return Primitives.newInstance(obj.longValue());
			} else if (obj.type() == Primitives.FLOAT) {
				return Primitives.newInstance(obj.floatValue());
			} else if (obj.type() == Primitives.DOUBLE) {
				return Primitives.newInstance(obj.doubleValue());
			} else if (obj.type() == Primitives.STRING) {
				return Primitives.newInstance(obj.stringValue());
			} else {
				throw new IllegalArgumentException(String.format("unsupported primitive type: \"%s\"", obj.type().getName()));
			}
		} else {
			ManagedObject rst = LMFContext.newInstance(obj.type());
			for (Attribute attr : obj.type().getAttributes()) {
				if (attr.isContainment()) {
					if (attr.getValueType() == Primitives.LIST) {
						ManagedList src = obj.get(attr.getName()).listContent();
						ManagedList dst = rst.get(attr.getName()).listContent();
						for (ManagedObject o : src) {
							dst.add(deepCopyObject(o));
						}
					} else {
						ManagedObject o = obj.get(attr.getName());
						if (o != null) {
							rst.set(attr.getName(), deepCopyObject(o));
						}
					}
				} else {
					if (attr.getValueType() == Primitives.LIST) {
						ManagedList src = obj.get(attr.getName()).listContent();
						ManagedList dst = rst.get(attr.getName()).listContent();
						dst.addAll(src);
					} else {
						ManagedObject o = obj.get(attr.getName());
						if (o != null) {
							rst.set(attr.getName(), o);
						}
					}
				}
			}
			return rst;
		}
	}
	
	public static List<Type> sortTypesByInheritance(Collection<Type> types) {
		List<List<Type>> lists = new ArrayList<List<Type>>(1);
		Iterator<Type> it = types.iterator();
		while (it.hasNext()) {
			Type type = it.next();
			boolean added = false;
			for (List<Type> list : lists) {
				if (list.get(0).isOrIsSubtypeOf(type) || type.isOrIsSubtypeOf(list.get(0))) {
					list.add(type);
					added = true;
				}
			}
			if (!added) {
				List<Type> list = new ArrayList<Type>(1);
				list.add(type);
				lists.add(list);
			}
		}
		// merge
		List<Type> rst = new ArrayList<Type>(types.size());
		for (List<Type> list : lists) {
			// sort list
			Collections.sort(list, INHERITANCE_COMPARATOR);
			for (Type type : list) {
				if (!rst.contains(type)) {
					rst.add(type);
				}
			}
		}
		return rst;
	}

	private LMFUtility() {
	}
	
	private static final Comparator<Type> INHERITANCE_COMPARATOR = new Comparator<Type>() {
		@Override
		public int compare(Type t1, Type t2) {
			return t2.isSubtypeOf(t1) ? -1 : 1;
		}
	};

	private static void getContainmentClosure(ManagedObject obj, List<ManagedObject> rst) {
		rst.add(obj);
		for (Attribute attr : obj.type().getAttributes()) {
			if (attr.isContainment()) {
				if (attr.getValueType() == Primitives.LIST) {
					ManagedList list = obj.get(attr.getName()).listContent();
					for (ManagedObject o : list) {
						getContainmentClosure(o, rst);
					}
				} else {
					ManagedObject o = obj.get(attr.getName());
					if (o != null) getContainmentClosure(o, rst);
				}
			}
		}
	}

	private static void findObjects(ManagedObject obj, ObjectFilter filter, List<ManagedObject> rst) {
		if (filter.accept(obj)) rst.add(obj);
		for (Attribute attr : obj.type().getAttributes()) {
			if (attr.isContainment()) {
				if (attr.getValueType() == Primitives.LIST) {
					ManagedList list = obj.get(attr.getName()).listContent();
					for (ManagedObject o : list) {
						findObjects(o, filter, rst);
					}
				} else {
					ManagedObject o = obj.get(attr.getName());
					if (o != null) findObjects(o, filter, rst);
				}
			}
		}
	}
	
}
