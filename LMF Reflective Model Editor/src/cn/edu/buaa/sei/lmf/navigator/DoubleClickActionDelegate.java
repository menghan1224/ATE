package cn.edu.buaa.sei.lmf.navigator;

import cn.edu.buaa.sei.lmf.ManagedObject;

public interface DoubleClickActionDelegate {

	public boolean shouldHandleDoubleClickAction(ManagedObject obj);
	public void handleDoubleClickAction(ManagedObject obj);
	
}
