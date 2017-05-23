package cn.edu.buaa.sei.lmf;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class ManagedList implements List<ManagedObject> {
	
	final static class ListPrimitive {
		
		final List<ManagedObject> content;
		ManagedObjectImpl target;
		String key;
		boolean containment;
		Type contentType;
		
		ListPrimitive(List<ManagedObject> content) {
			this.content = content;
		}
		
	}
	
	private final ManagedObjectImpl target;
	private final List<ManagedObject> targetList;
	private final Type contentType;
	private final boolean containment;
	
	ManagedList(ManagedObjectImpl target) {
		this.target = target;
		ListPrimitive pr = (ListPrimitive) target.primitive;
		this.targetList = pr.content;
		this.contentType = pr.contentType;
		this.containment = pr.containment;
	}
	
	public Type getContentType() {
		return contentType;
	}
	
	public boolean isContainment() {
		return containment;
	}
	
	private void checkElement(ManagedObject o) {
		if (o == null) {
			throw new IllegalArgumentException("null value is not allowed in primitive list.");
		}
		// type check
		if (contentType != null && !o.isKindOf(contentType)) {
			throw new IllegalArgumentException(String.format("%s is dismatch with list content type: \"%s\"", o, contentType.getName()));
		}
	}
	
	private void setOwnerForObject(ManagedObject o) {
		synchronized (o) {
			if (o.owner() != null) {
				throw new IllegalArgumentException("duplicate containment relationship");
			}
			((ManagedObjectImpl) o).owner = target.owner();
			((ManagedObjectImpl) o).notifyOwnerChanged();
		}
	}
	
	private void checkResource() {
		LMFResource resource = target.resource();
		if (resource != null && !resource.isEditable()) {
			throw new UnsupportedOperationException("resource is not editable.");
		}
	}

	@Override
	public synchronized boolean add(ManagedObject e) {
		checkResource();
		checkElement(e);
		boolean rst = targetList.add(e);
		if (containment) setOwnerForObject(e);
		if (rst) target.notifyListContentChanged(new ManagedObject[] { e }, new ManagedObject[0]);
		return rst;
	}

	@Override
	public synchronized void add(int index, ManagedObject element) {
		checkResource();
		checkElement(element);
		targetList.add(index, element);
		if (containment) setOwnerForObject(element);
		target.notifyListContentChanged(new ManagedObject[] { element }, new ManagedObject[0]);
	}

	@Override
	public synchronized boolean addAll(Collection<? extends ManagedObject> c) {
		checkResource();
		for (ManagedObject o : c) checkElement(o);
		boolean rst = targetList.addAll(c);
		if (containment) {
			for (ManagedObject o : c) {
				setOwnerForObject(o);
			}
		}
		if (rst) target.notifyListContentChanged(c.toArray(new ManagedObject[0]), new ManagedObject[0]);
		return rst;
	}

	@Override
	public synchronized boolean addAll(int index, Collection<? extends ManagedObject> c) {
		checkResource();
		for (ManagedObject o : c) checkElement(o);
		boolean rst = targetList.addAll(index, c);
		if (containment) {
			for (ManagedObject o : c) {
				setOwnerForObject(o);
			}
		}
		if (rst) target.notifyListContentChanged(c.toArray(new ManagedObject[0]), new ManagedObject[0]);
		return rst;
	}

	@Override
	public synchronized void clear() {
		checkResource();
		if (containment) {
			for (ManagedObject o : targetList) {
				((ManagedObjectImpl) o).owner = null;
				((ManagedObjectImpl) o).notifyOwnerChanged();
			}
		}
		ManagedObject[] objects = targetList.toArray(new ManagedObject[0]);
		targetList.clear();
		target.notifyListContentChanged(new ManagedObject[0], objects);
	}

	@Override
	public synchronized boolean contains(Object o) {
		return targetList.contains(o);
	}

	@Override
	public synchronized boolean containsAll(Collection<?> c) {
		return targetList.containsAll(c);
	}

	@Override
	public synchronized ManagedObject get(int index) {
		return targetList.get(index);
	}

	@Override
	public synchronized int indexOf(Object o) {
		return targetList.indexOf(o);
	}

	@Override
	public synchronized boolean isEmpty() {
		return targetList.isEmpty();
	}

	@Override
	public synchronized Iterator<ManagedObject> iterator() {
		return targetList.iterator();
	}

	@Override
	public synchronized int lastIndexOf(Object o) {
		return targetList.lastIndexOf(o);
	}

	@Override
	public synchronized ListIterator<ManagedObject> listIterator() {
		return targetList.listIterator();
	}

	@Override
	public synchronized ListIterator<ManagedObject> listIterator(int index) {
		return targetList.listIterator(index);
	}

	@Override
	public synchronized boolean remove(Object o) {
		checkResource();
		boolean rst = targetList.remove(o);
		if (containment && rst) {
			((ManagedObjectImpl) o).owner = null;
			((ManagedObjectImpl) o).notifyOwnerChanged();
		}
		if (rst) target.notifyListContentChanged(new ManagedObject[0], new ManagedObject[] { (ManagedObject) o });
		return rst;
	}

	@Override
	public synchronized ManagedObject remove(int index) {
		checkResource();
		ManagedObject rst = targetList.remove(index);
		if (containment) {
			((ManagedObjectImpl) rst).owner = null;
			((ManagedObjectImpl) rst).notifyOwnerChanged();
		}
		target.notifyListContentChanged(new ManagedObject[0], new ManagedObject[] { rst });
		return rst;
	}

	@Override
	public synchronized boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized ManagedObject set(int index, ManagedObject element) {
		checkResource();
		checkElement(element);
		ManagedObject rst = targetList.set(index, element);
		if (containment) {
			((ManagedObjectImpl) rst).owner = null;
			((ManagedObjectImpl) rst).notifyOwnerChanged();
			setOwnerForObject(element);
		}
		target.notifyListContentChanged(new ManagedObject[] { element }, new ManagedObject[] { rst });
		return rst;
	}

	@Override
	public synchronized int size() {
		return targetList.size();
	}

	@Override
	public synchronized List<ManagedObject> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized Object[] toArray() {
		return targetList.toArray();
	}

	@Override
	public synchronized <T> T[] toArray(T[] a) {
		return targetList.toArray(a);
	}
	
	@Override
	public synchronized String toString() {
		return targetList.toString();
	}
	
	@Override
	public synchronized int hashCode() {
		return targetList.hashCode();
	}
	
	@Override
	public synchronized boolean equals(Object arg0) {
		return targetList.equals(arg0);
	}
	
	public synchronized void exchangeElements(int index1, int index2) {
		checkResource();
		ManagedObject obj2 = targetList.set(index2, targetList.get(index1));
		targetList.set(index1, obj2);
		target.notifyListContentChanged(new ManagedObject[0], new ManagedObject[0]);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <E extends ManagedObject> List<E> toGenericList(Class<E> clazz) {
		return new List<E>() {
			@Override public boolean add(E e) { return ManagedList.this.add(e); }
			@Override public void add(int index, E element) { ManagedList.this.add(index, element); }
			@Override public boolean addAll(Collection<? extends E> c) { return ManagedList.this.addAll(c); }
			@Override public boolean addAll(int index, Collection<? extends E> c) { return ManagedList.this.addAll(index, c); }
			@Override public void clear() { ManagedList.this.clear(); }
			@Override public boolean contains(Object o) { return ManagedList.this.contains(o); }
			@Override public boolean containsAll(Collection<?> c) { return ManagedList.this.containsAll(c); }
			@Override public E get(int index) { return (E) ManagedList.this.get(index); }
			@Override public int indexOf(Object o) { return ManagedList.this.indexOf(o); }
			@Override public boolean isEmpty() { return ManagedList.this.isEmpty(); }
			@Override public int lastIndexOf(Object o) { return ManagedList.this.lastIndexOf(o); }
			@Override public boolean remove(Object o) { return ManagedList.this.remove(o); }
			@Override public E remove(int index) { return (E) ManagedList.this.remove(index); }
			@Override public boolean removeAll(Collection<?> c) { return ManagedList.this.removeAll(c); }
			@Override public boolean retainAll(Collection<?> c) { return ManagedList.this.retainAll(c); }
			@Override public E set(int index, E element) { return (E) ManagedList.this.set(index, element); }
			@Override public int size() { return ManagedList.this.size(); }
			@Override public List<E> subList(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }
			@Override public Object[] toArray() { return ManagedList.this.toArray(); }
			@Override public <T> T[] toArray(T[] a) { return ManagedList.this.toArray(a); }
			@Override public ListIterator<E> listIterator() { throw new UnsupportedOperationException(); }
			@Override public ListIterator<E> listIterator(int index) { throw new UnsupportedOperationException(); }
			
			@Override
			public Iterator<E> iterator() {
				final Iterator<ManagedObject> it = ManagedList.this.iterator();
				return new Iterator<E>() {
					@Override public boolean hasNext() { return it.hasNext(); }
					@Override public E next() { return (E) it.next(); }
					@Override public void remove() { it.remove(); }
				};
			}
		};
	}

}
