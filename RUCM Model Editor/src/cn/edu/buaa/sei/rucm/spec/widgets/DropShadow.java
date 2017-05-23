package cn.edu.buaa.sei.rucm.spec.widgets;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;

import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public class DropShadow {
	
	private DropShadow() {
	}
	
	public static void createDropShadowOn(final View view) {
		view.setClipped(false);
		
		final View shadow_h = new View() {
			@Override
			protected void repaintView(co.gongzh.snail.ViewGraphics g) {
				g.drawImage(RUCMPluginResource.getImage("dropshadow_h.png"), 0, 0, getWidth(), getHeight());
			}
		};
		shadow_h.setBackgroundColor(null);
		view.addSubview(shadow_h);
		
		final View shadow_v = new View() {
			@Override
			protected void repaintView(co.gongzh.snail.ViewGraphics g) {
				g.drawImage(RUCMPluginResource.getImage("dropshadow_v.png"), 0, 0, getWidth(), getHeight());
			}
		};
		shadow_v.setBackgroundColor(null);
		view.addSubview(shadow_v);
		
		view.addEventHandler(View.LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				shadow_h.setPosition(0, view.getHeight());
				shadow_h.setSize(view.getWidth() + 4, 4);
				shadow_v.setPosition(view.getWidth(), 0);
				shadow_v.setSize(4, view.getHeight());
			}
		});
		
		view.layout();
	}

}
