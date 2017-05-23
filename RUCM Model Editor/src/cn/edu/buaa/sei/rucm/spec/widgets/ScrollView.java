package cn.edu.buaa.sei.rucm.spec.widgets;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;

import co.gongzh.snail.Animation;
import co.gongzh.snail.MouseWheelEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Insets;

/**
 * A view with scroll bar. Subviews should be added to content view of this scroll view.
 * <p>
 * Scroll view <code>layout</code> behavior is:
 * <li>layout subviews one by one;
 * <li>each subview's width will be reset to same width with scroll content;
 * <li>each subview's height will be reset to its preferred height;
 * <li>each subview will be put after the previous subview.
 */
public class ScrollView extends View {
    
    private final View clipView;
    private final View contentView;
    private final View indicator;
    private Insets insets;
    
    private int indicatorState;
    private Animation indicatorAnimation;
    private boolean mouseIsOnIndicator;
    private static final int INDICATOR_HIDDEN = 1;
    private static final int INDICATOR_FADING_IN = 2;
    private static final int INDICATOR_SHOWN = 3;
    private static final int INDICATOR_COUNTING = 4;
    private static final int INDICATOR_FADING_OUT = 5;
    
	private class IndicatorFadeInAnimation extends Animation {

		public IndicatorFadeInAnimation() {
			super(0.1f);
		}

		@Override
		protected void completed(boolean canceled) {
			if (!canceled) {
				synchronized (indicator) {
					indicatorAnimation = null;
					if (mouseIsOnIndicator) {
						indicatorState = INDICATOR_SHOWN;
					} else {
						indicatorState = INDICATOR_COUNTING;
						indicatorAnimation = new IndicatorCountingAnimation();
						indicatorAnimation.commit();
					}
				}
			}
		}

		@Override
		protected void animate(float progress) {
			indicator.setAlpha(progress);
		}

	}
    
	private class IndicatorFadeOutAnimation extends Animation {

		public IndicatorFadeOutAnimation() {
			super(0.3f);
		}

		@Override
		protected void animate(float progress) {
			indicator.setAlpha(1.0f - progress);
		}

		@Override
		protected void completed(boolean canceled) {
			if (!canceled) {
				synchronized (indicator) {
					indicatorAnimation = null;
					indicatorState = INDICATOR_HIDDEN;
				}
			}
		}

	}
    
	private class IndicatorCountingAnimation extends Animation {

		public IndicatorCountingAnimation() {
			super(0.8f);
		}

		@Override
		protected void animate(float progress) {
		}

		@Override
		protected void completed(boolean canceled) {
			if (!canceled) {
				synchronized (indicator) {
					indicatorState = INDICATOR_FADING_OUT;
					indicatorAnimation = new IndicatorFadeOutAnimation();
					indicatorAnimation.commit();
				}
			}
		}

	}

	public ScrollView() {
		setBackgroundColor(null);
		setPaintMode(PaintMode.DISABLED);
		mouseIsOnIndicator = false;
		insets = Insets.make();

		clipView = new View() {
			@Override
			public void setSize(int width, int height) {
				super.setSize(width, height);
				if (contentView != null && contentView.getWidth() != width) {
					contentView.setWidth(width);
					ScrollView.this.layout();
				} else {
					updateContentsVisibilities();
				}
			}
		};
		clipView.setPaintMode(PaintMode.DISABLED);
		clipView.setClipped(true);
		addSubview(clipView);

		contentView = new View() {
			@Override
			public void setSize(int width, int height) {
				boolean height_changed = false;
				if (getHeight() != height)
					height_changed = true;
				super.setSize(width, height);
				if (height_changed)
					updateIndicator();
			}

			@Override
			public void setPosition(int left, int top) {
				boolean top_changed = false;
				if (getTop() != top)
					top_changed = true;
				super.setPosition(left, top);
				if (top_changed) {
					updateIndicator();
					updateContentsVisibilities();
				}
			}

			@Override
			protected void subviewAdded(View subview) {
				subview.setHidden(true);
			}
		};
		contentView.setPaintMode(PaintMode.DISABLED);
		clipView.addSubview(contentView);

		indicator = new View(0, 0, 7, 20) {
			@Override
			protected void repaintView(ViewGraphics g) {
				g.drawImage(RUCMPluginResource.getImage("scrollbar.png"), 0, 0, getWidth(), getHeight());
			}

			@Override
			protected void mouseEntered() {
				mouseIsOnIndicator = true;
				showIndicator();
			}

			@Override
			protected void mouseExited() {
				mouseIsOnIndicator = false;
				switch (indicatorState) {
				case INDICATOR_SHOWN:
					indicatorState = INDICATOR_COUNTING;
					indicatorAnimation = new IndicatorCountingAnimation();
					indicatorAnimation.commit();
					break;
				}
			}

			int mouse_y0;

			@Override
			protected void mousePressed(co.gongzh.snail.MouseEvent e) {
				e.handle();
				mouse_y0 = e.getPosition(ScrollView.this).y;
			}

			@Override
			protected void mouseReleased(co.gongzh.snail.MouseEvent e) {
				e.handle();
			}

			@Override
			protected void mouseMoved(co.gongzh.snail.MouseEvent e) {
				e.handle();
			}

			@Override
			protected void mouseDragged(co.gongzh.snail.MouseEvent e) {
				e.handle();
				int mouse_y = e.getPosition(ScrollView.this).y;
				float dy = (mouse_y - mouse_y0)
						/ (float) (ScrollView.this.getHeight() - 8);
				contentView.setTop(contentView.getTop()
						- (int) (dy * contentView.getHeight()));
				limitContentViewTop();
				mouse_y0 = mouse_y;
			}

			@Override
			protected void mouseClicked(co.gongzh.snail.MouseEvent e) {
				e.handle();
			}
		};
		indicator.setBackgroundColor(null);
		addSubview(indicator);

		// indicator state
		indicatorState = INDICATOR_FADING_OUT;
		indicatorAnimation = new IndicatorFadeOutAnimation();
		indicatorAnimation.commit();
		
		// add handlers
		addEventHandler(LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key eventName, Object arg) {
				layoutScrollView();
			}
		});
		addEventHandler(MOUSE_WHEEL_MOVED, new EventHandler() {
			@Override
			public void handle(View sender, Key eventName, Object arg) {
				MouseWheelEvent e = (MouseWheelEvent) arg;
	            contentView.setTop(contentView.getTop() - e.getRotation() * 4);
	            limitContentViewTop();
	            e.handle();
			}
		});
	}
    
	private void layoutScrollView() {
		if (clipView != null) {
			View.scaleViewWithLeftAndRight(clipView, insets.left, insets.right);
			View.scaleViewWithTopAndBottom(clipView, insets.top, insets.bottom);
		}
		if (contentView != null) {
			limitContentViewTop();
		}
		if (indicator != null) {
			indicator.setLeft(getWidth() - 10 - insets.right);
			updateIndicator();
		}
		// layout content
		int cur_top = 0;
		for (View view : contentView) {
			view.setWidth(contentView.getWidth());
			view.setHeight(view.getPreferredHeight());
			view.setPosition(0, cur_top);
			cur_top += view.getHeight();
		}
		contentView.setHeight(cur_top);
		limitContentViewTop();
		updateContentsVisibilities();
		contentViewScrolled();
	}
    
    @Override
    public int getPreferredHeight() {
    	return contentView.getHeight();
    }
    
	private void updateContentsVisibilities() {
		for (View view : contentView) {
			if (contentView.getTop() + view.getTop() + view.getHeight() > 0) {
				if (contentView.getTop() + view.getTop() > clipView.getHeight()) {
					break;
				}
				view.setHidden(false);
			}
		}
	}
    
	public View getContentView() {
		return contentView;
	}
    
	private void limitContentViewTop() {
		if (contentView.getHeight() <= clipView.getHeight()) {
			contentView.setTop(0);
		} else if (contentView.getTop() > 0) {
			contentView.setTop(0);
		} else if (contentView.getTop() + contentView.getHeight() < clipView
				.getHeight()) {
			contentView.setTop(clipView.getHeight() - contentView.getHeight());
		}
	}
    
	private void updateIndicator() {
		float total = contentView.getHeight();
		if (total <= 0.0f)
			total = 1.0f;
		float window = clipView.getHeight();
		if (window > total)
			window = total;
		else if (window <= 0.0f)
			window = 1.0f;
		float top = -contentView.getTop();
		window /= total;
		top /= total;
		total = getHeight() - 6 - insets.top - insets.bottom;
		indicator.setTop((int) (insets.top + 3 + top * total));
		indicator.setHeight((int) (window * total));
		if (contentView.getHeight() <= clipView.getHeight()) {
			indicator.setHidden(true);
		} else {
			indicator.setHidden(false);
			showIndicator();
		}
	}
    
	private void showIndicator() {
		if (contentView.getHeight() <= clipView.getHeight())
			return;
		synchronized (indicator) {
			switch (indicatorState) {
			case INDICATOR_HIDDEN:
				indicatorState = INDICATOR_FADING_IN;
				indicatorAnimation = new IndicatorFadeInAnimation();
				indicatorAnimation.commit();
				break;
			case INDICATOR_COUNTING:
			case INDICATOR_FADING_OUT:
				if (indicatorAnimation != null) {
					indicatorAnimation.cancel();
				}
				indicator.setAlpha(1.0f);
				indicatorState = INDICATOR_COUNTING;
				indicatorAnimation = new IndicatorCountingAnimation();
				indicatorAnimation.commit();
				break;
			}
		}
	}
    
	public Insets getInsets() {
		return insets.clone();
	}
	
	public void setInsets(Insets insets) {
		this.insets = insets.clone();
		layout();
	}
	
	protected void contentViewScrolled() {
	}
	
}
