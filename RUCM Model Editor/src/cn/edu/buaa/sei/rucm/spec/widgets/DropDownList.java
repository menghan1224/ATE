package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.geom.AffineTransform;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.Animation;
import co.gongzh.snail.Image;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.util.Vector2D;

public class DropDownList extends View {
	
	public DropDownList() {
		setBackgroundColor(null);
	}
	
	private final int margin = 6;
	
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
		Image background = RUCMPluginResource.getImage("dropdownlist.png");
		g.drawImage(background, 0, 0, getWidth(), getHeight());
	}
	
	//// Pop-up and dismiss ////
	
	private boolean shown = false;
	
	protected void menuAboutToShow(Vector2D location) {
	}
	
	protected void menuDismissed() {
	}
	
	public void popup(ViewContext target, Vector2D location) {
		if (shown) throw new IllegalStateException("Menu has already been shown.");
		if (target == null || location == null) throw new IllegalArgumentException();
		shown = true;
		
		// menu
		menuAboutToShow(location.clone());
		layout();
		target.getRootView().addSubview(this);
		setSize(getPreferredSize());
		setPosition(location.x - 6, location.y);
		if (getRight() < 0) {
			View.putViewWithRight(DropDownList.this, 0);
		}
		if (getBottom() < 0) {
			View.putViewWithBottom(DropDownList.this, 0);
		}
		fadeInAnimation().commit();
	}
	
	public void dismiss() {
		if (!shown) throw new IllegalStateException("Menu has not been shown.");
		removeFromSuperView();
		shown = false;
		menuDismissed();
	}
	
	public boolean isShown() {
		return shown;
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
	
	public void selectNext() {
		boolean nextFlag = false;
		View[] views = getSubviews();
		for (int i = 0; i < views.length; i++) {
			if (views[i] instanceof DropDownListItem) {
				if (nextFlag) {
					((DropDownListItem) views[i]).setSelected(true);
					return;
				} else if (((DropDownListItem) views[i]).isSelected()) {
					((DropDownListItem) views[i]).setSelected(false);
					nextFlag = true;
				}
			}
		}
		selectFirst();
	}
	
	public void selectPrevious() {
		boolean nextFlag = false;
		View[] views = getSubviews();
		for (int i = views.length - 1; i >= 0; i--) {
			if (views[i] instanceof DropDownListItem) {
				if (nextFlag) {
					((DropDownListItem) views[i]).setSelected(true);
					return;
				} else if (((DropDownListItem) views[i]).isSelected()) {
					((DropDownListItem) views[i]).setSelected(false);
					nextFlag = true;
				}
			}
		}
		selectLast();
	}
	
	public void selectFirst() {
		View[] views = getSubviews();
		for (int i = 0; i < views.length; i++) {
			if (views[i] instanceof DropDownListItem) {
				if (i == 0) ((DropDownListItem) views[i]).setSelected(true);
				else ((DropDownListItem) views[i]).setSelected(false);
			}
		}
	}
	
	public void selectLast() {
		View[] views = getSubviews();
		for (int i = views.length - 1; i >= 0; i--) {
			if (views[i] instanceof DropDownListItem) {
				if (i == views.length - 1) ((DropDownListItem) views[i]).setSelected(true);
				else ((DropDownListItem) views[i]).setSelected(false);
			}
		}
	}
	
	public DropDownListItem getSelectedItem() {
		View[] views = getSubviews();
		for (int i = 0; i < views.length; i++) {
			if (views[i] instanceof DropDownListItem) {
				if (((DropDownListItem) views[i]).isSelected()) {
					return (DropDownListItem) views[i];
				}
			}
		}
		return null;
	}
	
}
