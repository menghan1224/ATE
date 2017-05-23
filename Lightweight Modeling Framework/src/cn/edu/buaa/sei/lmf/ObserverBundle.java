package cn.edu.buaa.sei.lmf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ObserverBundle {

	private final Map<String, List<Observer>> observers;
	private int retainCount;
	private final ManagedObject target;
	private final List<Notification> notifications;
	
	private final List<OwnerObserver> ownerObservers;
	private final Map<String, List<ListObserver>> listObservers;
	
	ObserverBundle(ManagedObject target) {
		this.target = target;
		this.observers = new HashMap<String, List<Observer>>(0);
		this.retainCount = 0;
		this.notifications = new ArrayList<ObserverBundle.Notification>(0);
		this.ownerObservers = new ArrayList<OwnerObserver>(0);
		this.listObservers = new HashMap<String, List<ListObserver>>(0);
	}
	
	synchronized void addObserver(String key, Observer observer) {
		List<Observer> list = observers.get(key);
		if (list == null) {
			list = new ArrayList<Observer>(1);
			observers.put(key, list);
		}
		list.add(observer);
	}
	
	synchronized void removeObserver(String key, Observer observer) {
		List<Observer> list = observers.get(key);
		if (list != null) {
			list.remove(observer);
		}
	}
	
	synchronized void notify(String key, ManagedObject value) {
		if (retainCount == 0) {
			// notify instantly
			List<Observer> list = observers.get(key);
			if (list != null) {
				for (Observer ob : list.toArray(new Observer[0])) {
					ob.notifyChanged(target, key, value);
				}
			}
		} else {
			// hold notification
			Notification candidate = null;
			for (Notification notif : notifications) {
				if (notif.key.equals(key)) {
					candidate = notif;
					break;
				}
			}
			if (candidate != null) {
				notifications.remove(candidate);
			}
			notifications.add(new Notification(key, value));
		}
	}
	
	synchronized void addListObserver(String key, ListObserver observer) {
		List<ListObserver> list = listObservers.get(key);
		if (list == null) {
			list = new ArrayList<ListObserver>(1);
			listObservers.put(key, list);
		}
		list.add(observer);
	}
	
	synchronized void removeListObserver(String key, ListObserver observer) {
		List<ListObserver> list = listObservers.get(key);
		if (list != null) {
			list.remove(observer);
		}
	}
	
	synchronized void notifyListChanged(String key, ManagedObject[] added, ManagedObject[] removed) {
		// notify instantly
		List<ListObserver> list = listObservers.get(key);
		if (list != null) {
			for (ListObserver ob : list.toArray(new ListObserver[0])) {
				ob.listChanged(target, key, added, removed);
			}
		}
	}
	
	synchronized void retainNotifications() {
		retainCount++;
	}
	
	synchronized void releaseNotifications() {
		retainCount--;
		if (retainCount == 0) {
			// drain notifications
			for (Notification notif : notifications) {
				List<Observer> list = observers.get(notif.key);
				if (list != null) {
					for (Observer ob : list.toArray(new Observer[0])) {
						ob.notifyChanged(target, notif.key, notif.value);
					}
				}
			}
			notifications.clear();
		}
	}
	
	private static final class Notification {
		
		final String key;
		final ManagedObject value;
		
		Notification(String key, ManagedObject value) {
			this.key = key;
			this.value = value;
		}
		
	}
	
	synchronized void addOwnerObserver(OwnerObserver observer) {
		ownerObservers.add(observer);
	}
	
	synchronized void removeOwnerObserver(OwnerObserver observer) {
		ownerObservers.remove(observer);
	}
	
	synchronized void notifyOwnerChanged(ManagedObject owner) {
		for (OwnerObserver observer : ownerObservers.toArray(new OwnerObserver[0])) {
			observer.ownerChanged(target, owner);
		}
	}
	
}
