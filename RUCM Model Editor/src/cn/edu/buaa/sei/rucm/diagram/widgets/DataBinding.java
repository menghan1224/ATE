package cn.edu.buaa.sei.rucm.diagram.widgets;

import javax.swing.SwingUtilities;

import cn.edu.buaa.sei.lmf.CascadeObserver;
import cn.edu.buaa.sei.lmf.ManagedObject;

import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public abstract class DataBinding {
	
	private final View targetView;
	private final Key event;
	private final ManagedObject targetObject;
	private final CascadeObserver cascadeObserver;
	
	private int viewMutex = 0;
	private int modelMutex = 0;
	
	public DataBinding(View targetView, Key event, ManagedObject targetObject, String...keys) {
		this.targetView = targetView;
		this.event = event;
		this.targetObject = targetObject;
		
		this.targetView.addEventHandler(event, eventHandler);
		this.cascadeObserver = new CascadeObserver(targetObject, keys) {
			@Override
			protected void notifyChanged(final ManagedObject value) {
				if (viewMutex == 0 && modelMutex == 0) {
					modelMutex++;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							modelPropertyChanged(value);
							modelMutex--;
						}
					});
				}
			}
		};
		
		// init
		modelMutex++;
		modelPropertyChanged(this.cascadeObserver.getValue());
		modelMutex--;
	}
	
	private final EventHandler eventHandler = new EventHandler() {
		@Override
		public void handle(View sender, Key key, final Object arg) {
			if (modelMutex == 0 && viewMutex == 0) {
				viewMutex++;
				viewPropertyChanged(arg);
				viewMutex--;
			}
		}
	};
	
	public void dispose() {
		targetView.removeEventHandler(event, eventHandler);
		cascadeObserver.removeFromTarget();
	}
	
	public ManagedObject getTargetObject() {
		return targetObject;
	}
	
	public View getTargetView() {
		return targetView;
	}
	
	protected abstract void viewPropertyChanged(Object arg);
	protected abstract void modelPropertyChanged(ManagedObject value);

}
