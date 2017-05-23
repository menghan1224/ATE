package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Color;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource.SpecTextAAModeChangeListener;

import co.gongzh.snail.Image;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.text.TextView;
import co.gongzh.snail.util.Alignment;
import co.gongzh.snail.util.Insets;

public class Label extends View {
	
	private final TextView textView;
	private Image icon;
	private final Insets insets;
	private int iconSpacing;
	
	public Label() {
		iconSpacing = 0;
		icon = null;
		insets = Insets.make();
		textView = new TextView();
		textView.setTextAlignment(Alignment.LEFT_CENTER);
		textView.setTextAntialiasing(RUCMPluginResource.getSpecTextAntialiasing());
		addSubview(textView);
		RUCMPluginResource.addSpecTextAAModeChangeListener(aaChangeListener);
	}
	
	@Override
	public void removeFromSuperView() {
		RUCMPluginResource.removeSpecTextAAModeChangeListener(aaChangeListener);
		super.removeFromSuperView();
	}
	
	private final SpecTextAAModeChangeListener aaChangeListener = new SpecTextAAModeChangeListener() {
		@Override
		public void textAAModeChanged(co.gongzh.snail.text.TextAntialiasing mode) {
			textView.setTextAntialiasing(mode);
			layout();
			setNeedsRepaint();
		}
	};
	
	@Override
	public void setBackgroundColor(Color backgroundColor) {
		super.setBackgroundColor(backgroundColor);
		textView.setBackgroundColor(backgroundColor);
	}
	
	public TextView getTextView() {
		return textView;
	}
	
	public Image getIcon() {
		return icon;
	}
	
	public void setIcon(Image icon) {
		this.icon = icon;
		layout();
		setNeedsRepaint();
	}

	public Insets getInsets() {
		return insets.clone();
	}
	
	public void setInsets(Insets insets) {
		this.insets.set(insets);
		layout();
		setNeedsRepaint();
	}
	
	public int getIconSpacing() {
		return iconSpacing;
	}
	
	public void setIconSpacing(int iconSpacing) {
		this.iconSpacing = iconSpacing;
		layout();
		setNeedsRepaint();
	}
	
	@Override
	public int getPreferredWidth() {
		if (icon != null) {
			return icon.width + iconSpacing + textView.getPreferredWidth() + insets.left + insets.right;
		} else {
			return textView.getPreferredWidth() + insets.left + insets.right;
		}
	}
	
	@Override
	public int getPreferredHeight() {
		if (icon != null) {
			return Math.max(icon.height, textView.getPreferredHeight()) + insets.top + insets.bottom;
		} else {
			return textView.getPreferredHeight() + insets.top + insets.bottom;
		}
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		if (icon != null) {
			final int height = getHeight() - insets.top - insets.bottom;
			g.drawImage(icon, insets.left, insets.top + (height - icon.height) / 2);
		}
	}
	
	@Override
	protected void layoutView() {
		if (icon != null) {
			View.scaleViewWithLeftAndRight(textView, insets.left + icon.width + iconSpacing, insets.right);
			View.scaleViewWithTopAndBottom(textView, insets.top, insets.bottom);
		} else {
			View.scaleViewWithLeftAndRight(textView, insets.left, insets.right);
			View.scaleViewWithTopAndBottom(textView, insets.top, insets.bottom);
		}
	}

}
