package cn.edu.buaa.sei.rucm.diagram.widgets;

import java.awt.Color;

import cn.edu.buaa.sei.rucm.spec.widgets.Cursor;

import co.gongzh.snail.Animation;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Vector2D;

public abstract class Scrollbar extends View {
	
	private int contentSize;
	private int windowSize;
	private int windowOffset;
	private boolean mouseOn;
	
	protected final View slider;
	private static final float SLIDER_ALPHA_NORMAL = 0.15f;
	private static final float SLIDER_ALPHA_HIGHLIGHT = 0.35f;
	
	public static final Key SLIDER_DRAGGED = new Key("sliderDragged", Scrollbar.class, null);
	
	public Scrollbar() {
		setBackgroundColor(null);
		setPaintMode(PaintMode.DIRECTLY);
		contentSize = 0;
		windowSize = 0;
		windowOffset = 0;
		slider = new View() {
			@Override
			protected void repaintView(ViewGraphics g) {
				g.setColor(Color.BLACK);
				g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
			}
			// mouse event
			Vector2D p0;
			@Override
			protected void mousePressed(MouseEvent e) {
				p0 = e.getPosition(Scrollbar.this);
				sliderPressed();
				e.handle();
			}
			@Override
			protected void mouseDragged(MouseEvent e) {
				Vector2D p = e.getPosition(Scrollbar.this);
				sliderDragged(Vector2D.subtract(p, p0));
				Scrollbar.this.fireEvent(SLIDER_DRAGGED, null);
				e.handle();
			}
			@Override
			protected void mouseReleased(MouseEvent e) {
				e.handle();
			}
		};
		slider.setBackgroundColor(null);
		slider.setPaintMode(PaintMode.DIRECTLY);
		slider.setAlpha(SLIDER_ALPHA_NORMAL);
		Cursor.setCursorOnView(slider, java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
		mouseOn = false;
		addSubview(slider);
	}
	
	public int getContentSize() {
		return contentSize;
	}
	
	public int getWindowSize() {
		return windowSize;
	}
	
	public int getWindowOffset() {
		return windowOffset;
	}
	
	public void setContentSize(int contentSize) {
		this.contentSize = contentSize;
		layout();
	}
	
	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
		layout();
	}
	
	public void setWindowOffset(int windowOffset) {
		this.windowOffset = windowOffset;
		layout();
	}
	
	@Override
	protected void mouseEntered() {
		mouseOn = true;
		new Animation(0.2f) {
			@Override
			protected void animate(float progress) {
				slider.setAlpha(progress * (SLIDER_ALPHA_HIGHLIGHT - SLIDER_ALPHA_NORMAL) + SLIDER_ALPHA_NORMAL);
			}
		}.commit();
	}
	
	@Override
	protected void mouseExited() {
		mouseOn = false;
		new Animation(0.2f) {
			@Override
			protected void animate(float progress) {
				slider.setAlpha(progress * (SLIDER_ALPHA_NORMAL - SLIDER_ALPHA_HIGHLIGHT) + SLIDER_ALPHA_HIGHLIGHT);
			}
		}.commit();
	}
	
	@Override
	public boolean isInside(Vector2D point) {
		if (windowSize < contentSize) {
			return super.isInside(point);
		} else {
			return false;
		}
	}
	
	@Override
	protected void layoutView() {
		if (windowSize > contentSize) {
			windowSize = contentSize;
			windowOffset = 0;
		}
		if (windowOffset + windowSize > contentSize) {
			windowOffset = contentSize - windowSize;
		}
		if (windowOffset < 0) {
			windowOffset = 0;
		}
		layourSlider(mouseOn);
	}
	
	abstract protected void layourSlider(boolean mouseOn);
	abstract protected void sliderPressed();
	abstract protected void sliderDragged(Vector2D delta);
	
	public static class Horizontal extends Scrollbar {

		@Override
		protected void mouseEntered() {
			super.mouseEntered();
			new Animation(0.2f, slider) {
				final int h0 = 6;
				final int ht = getHeight() - 4;
				@Override
				protected void animate(float progress) {
					slider.setHeight((int) (progress * (ht - h0) + h0));
					View.putViewWithBottom(slider, 2);
				}
			}.commit();
		}
		
		@Override
		protected void mouseExited() {
			super.mouseExited();
			new Animation(0.2f, slider) {
				final int h0 = slider.getHeight();
				final int ht = 6;
				@Override
				protected void animate(float progress) {
					slider.setHeight((int) (progress * (ht - h0) + h0));
					View.putViewWithBottom(slider, 2);
				}
			}.commit();
		}

		@Override
		protected void layourSlider(boolean mouseOn) {
			if (getContentSize() == 0 || getContentSize() == getWindowSize()) {
				slider.setWidth(0);
				slider.setLeft(0);
			} else {
				slider.setWidth((int) (getWidth() * (getWindowSize() / (double) getContentSize())));
				slider.setLeft((int) (getWidth() * (getWindowOffset() / (double) getContentSize())));
			}
			if (mouseOn) {
				View.scaleViewWithTopAndBottom(slider, 2, 2);
			} else {
				slider.setHeight(6);
				View.putViewWithBottom(slider, 2);
			}
		}
		
		private int offset0;

		@Override
		protected void sliderPressed() {
			offset0 = getWindowOffset();
		}

		@Override
		protected void sliderDragged(Vector2D delta) {
			int offsetDelta = getContentSize() * delta.x / getWidth();
			setWindowOffset(offset0 + offsetDelta);
		}
		
	}
	
	public static class Vertical extends Scrollbar {

		@Override
		protected void mouseEntered() {
			super.mouseEntered();
			new Animation(0.2f, slider) {
				final int w0 = 6;
				final int wt = getWidth() - 4;
				@Override
				protected void animate(float progress) {
					slider.setWidth((int) (progress * (wt - w0) + w0));
					View.putViewWithRight(slider, 2);
				}
			}.commit();
		}
		
		@Override
		protected void mouseExited() {
			super.mouseExited();
			new Animation(0.2f, slider) {
				final int w0 = slider.getWidth();
				final int wt = 6;
				@Override
				protected void animate(float progress) {
					slider.setWidth((int) (progress * (wt - w0) + w0));
					View.putViewWithRight(slider, 2);
				}
			}.commit();
		}

		@Override
		protected void layourSlider(boolean mouseOn) {
			if (getContentSize() == 0 || getContentSize() == getWindowSize()) {
				slider.setHeight(0);
				slider.setTop(0);
			} else {
				slider.setHeight((int) (getHeight() * (getWindowSize() / (double) getContentSize())));
				slider.setTop((int) (getHeight() * (getWindowOffset() / (double) getContentSize())));
			}
			if (mouseOn) {
				View.scaleViewWithLeftAndRight(slider, 2, 2);
			} else {
				slider.setWidth(6);
				View.putViewWithRight(slider, 2);
			}
		}
		
		private int offset0;

		@Override
		protected void sliderPressed() {
			offset0 = getWindowOffset();
		}

		@Override
		protected void sliderDragged(Vector2D delta) {
			int offsetDelta = getContentSize() * delta.y / getHeight();
			setWindowOffset(offset0 + offsetDelta);
		}
		
	}

}
