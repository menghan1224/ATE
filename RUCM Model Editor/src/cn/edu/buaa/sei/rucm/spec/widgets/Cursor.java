package cn.edu.buaa.sei.rucm.spec.widgets;

import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;

public class Cursor {
	
	public static final java.awt.Cursor TEXT_CURSOR = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR);
	public static final java.awt.Cursor HAND_CURSOR = java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR);
	
	public static void setCursorOnView(View view, final java.awt.Cursor cursor) {
		EventHandler handler = new EventHandler() {
			@Override
			public void handle(final View sender, co.gongzh.snail.event.Key eventName, Object arg) {
				if (sender.getSuperView() != null) {
					if (eventName.equals(View.MOUSE_ENTERED)) {
						sender.getViewContext().getSwingContainer().setCursor(cursor);
					} else if (eventName.equals(View.MOUSE_EXITED)) {
						sender.getViewContext().getSwingContainer().setCursor(java.awt.Cursor.getDefaultCursor());
					}
				}
			}
		};
		view.addEventHandler(View.MOUSE_ENTERED, handler);
		view.addEventHandler(View.MOUSE_EXITED, handler);
	}
	
}
