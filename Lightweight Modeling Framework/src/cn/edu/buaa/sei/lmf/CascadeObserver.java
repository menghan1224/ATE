package cn.edu.buaa.sei.lmf;

public abstract class CascadeObserver {
	
	private final ManagedObject[] targets;
	private final String[] keys;
	private final ObserverElement[] observers;
	
	public CascadeObserver(ManagedObject target, String... keys) {
		// check target and keys
		if (target == null) throw new IllegalArgumentException("target object can not be null");
		if (keys.length == 0) throw new IllegalArgumentException("missing observed key");
		Type type = target.type();
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			Attribute attribute = type.getAttribute(key);
			if (attribute == null) throw new IllegalArgumentException(String.format("unknown key \"%s\" for type %s", key, type.getName()));
			type = attribute.getValueType();
			if (i != keys.length - 1) {
				if (type.isPrimitiveType()) throw new IllegalArgumentException("can not observe primitive attribute: " + attribute.getName());
			}
		}
		// initialize
		this.targets = new ManagedObject[keys.length];
		this.targets[0] = target;
		this.keys = keys.clone();
		this.observers = new ObserverElement[keys.length];
		initObserver();
	}
	
	public ManagedObject getValue() {
		ManagedObject object = targets[keys.length - 1];
		ManagedObject value = null;
		if (object != null) {
			value = object.get(keys[keys.length - 1]);
		}
		return value;
	}
	
	private void initObserver() {
		ManagedObject target = targets[0];
		for (int i = 0; i < keys.length; i++) {
			targets[i] = target;
			ManagedObject value = null;
			if (i != keys.length - 1) {
				value = target.get(keys[i]);
			}
			observers[i] = new ObserverElement(i);
			target.addObserver(keys[i], observers[i]);
			if (value != null) {
				target = value;
			} else {
				break;
			}
		}
	}
	
	private void notifyChangedAtIndex(final int index, ManagedObject valueAtIndex) {
		if (index != keys.length - 1) {
			// remove current observers
			for (int i = index + 1; i < keys.length; i++) {
				ManagedObject target = targets[i];
				if (target != null) {
					target.removeObserver(keys[i], observers[i]);
					observers[i] = null;
					targets[i] = null;
				} else {
					break;
				}
			}
			// add new observer
			ManagedObject target = valueAtIndex;
			if (target != null) {
				for (int i = index + 1; i < keys.length; i++) {
					targets[i] = target;
					ManagedObject value = null;
					if (i != keys.length - 1) {
						value = target.get(keys[i]);
					}
					observers[i] = new ObserverElement(i);
					target.addObserver(keys[i], observers[i]);
					if (value != null) {
						target = value;
					} else {
						break;
					}
				}
			}
		}
		// notify client
		ManagedObject object = targets[keys.length - 1];
		ManagedObject newValue = null;
		if (object != null) {
			newValue = object.get(keys[keys.length - 1]);
		}
		notifyChanged(newValue);
	}
	
	public void removeFromTarget() {
		for (int i = 0; i < keys.length; i++) {
			ManagedObject target = targets[i];
			if (target != null) {
				target.removeObserver(keys[i], observers[i]);
				observers[i] = null;
				targets[i] = null;
			} else {
				break;
			}
		}
	}
	
	private final class ObserverElement implements Observer {

		private final int index;
		
		ObserverElement(int index) {
			this.index = index;
		}
		
		@Override
		public void notifyChanged(ManagedObject target, String key, ManagedObject value) {
			notifyChangedAtIndex(index, value);
		}
		
	}
	
	protected abstract void notifyChanged(ManagedObject value);
	
}
