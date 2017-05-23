package cn.edu.buaa.sei.rucm.diagram.widgets;


import java.awt.geom.AffineTransform;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.Animation;
import co.gongzh.snail.Image;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Vector2D;

public class PopupMenu extends View {
	
	public PopupMenu() {
		setBackgroundColor(null);
	}
	
	private final int margin = 8;
	
	@Override
	public int getPreferredWidth() {
		int width = 0, max_width = 0;
		for (View view : this) {
			width = view.getPreferredWidth();
			if (width > max_width) max_width = width;
		}
		return Math.max(max_width + margin * 2, 200);
	}
	
	@Override
	public int getPreferredHeight() {
		int top = margin;
		for (View view : this) {
			top += view.getPreferredHeight();
		}
		return top + margin;
	}
	
	@Override
	protected void layoutView() {
		int top = margin;
		for (View view : this) {
			View.scaleViewWithLeftAndRight(view, margin, margin);
			view.setTop(top);
			view.setHeight(view.getPreferredHeight());
			top += view.getHeight();
		}
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		Image background = RUCMPluginResource.getImage("menu.png");
		g.drawImage(background, 0, 0, getWidth(), getHeight());
	}
	
	//// Pop-up and dismiss ////
	
	private boolean shown = false;
	private View maskView = null;
	private EventHandler maskViewLayout = new EventHandler() {
		@Override
		public void handle(View sender, Key key, Object arg) {
			if (maskView != null) View.scaleViewWithMarginToSuperView(maskView, 0);
		}
	};
	
	protected void menuAboutToShow(Vector2D location) {
	}
	
	protected void menuDismissed() {
	}
	
	public void popup(ViewContext target, Vector2D location) {
		if (shown) throw new IllegalStateException("Menu has already been shown.");
		if (target == null || location == null) throw new IllegalArgumentException();
		shown = true;
		
		// mask view
		maskView = new View() {
			@Override
			protected void mousePressed(MouseEvent e) {
				dismiss();
				e.handle();
			}
			@Override
			protected void mouseReleased(MouseEvent e) {
				e.handle();
			}
		};
		maskView.setBackgroundColor(null);
		maskView.setPaintMode(PaintMode.DISABLED);
		target.getRootView().addSubview(maskView);
		target.getRootView().addEventHandler(LAYOUT, maskViewLayout);
		target.getRootView().layout();
		
		// menu
		menuAboutToShow(location.clone());
		layout();
		target.getRootView().addSubview(this);
		setSize(getPreferredSize());
		setPosition(location.x - 6, location.y - 6);
		if (getRight() < 0) {
			View.putViewWithRight(PopupMenu.this, 0);
		}
		if (getBottom() < 0) {
			View.putViewWithBottom(PopupMenu.this, 0);
		}
		fadeInAnimation().commit();
	}
	
	public void dismiss() {
		if (!shown) throw new IllegalStateException("Menu has not been shown.");
		removeFromSuperView();
		maskView.getViewContext().getRootView().removeEventHandler(LAYOUT, maskViewLayout);
		maskView.removeFromSuperView();
		maskView = null;
		shown = false;
		menuDismissed();
	}
	
	private Animation fadeInAnimation() {
		return new Animation(0.1f, Animation.EaseOut, this) {
			@Override
			protected void animate(float progress) {
				AffineTransform tr = new AffineTransform();
				tr.setToScale(0.2f + progress * 0.8f, 0.2f + progress * 0.8f);
				setTransform(tr);
				setAlpha(progress);
			}
			@Override
			protected void completed(boolean canceled) {
				setAlpha(1.0f);
				setTransform(null);
			}
		};
	}
	
}
