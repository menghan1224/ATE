package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Color;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;

import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.text.TextView;

public class SectionBar extends View {
	
	public static final Color TEXT_COLOR = new Color(0x435061);
	
	private final RowLabel label;
	
	public SectionBar() {
		label = new RowLabel();
		label.setBackgroundColor(null);
		label.setPaintMode(PaintMode.DIRECTLY);
		label.getTextView().setDefaultTextColor(TEXT_COLOR);
		label.getTextView().addEventHandler(TextView.TEXT_LAYOUT_CHANGED, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				SectionBar.this.layout();
				SectionBar.this.setNeedsRepaint();
			}
		});
//		label.getTextView().setDefaultFont(SpecViewResource.FONT_DIALOG_BOLD);
	}
	
	public String getText() {
		return label.getTextView().getText().toString();
	}
	
	public void setText(String text) {
		label.getTextView().setText(text);
	}
	
	@Override
	protected void layoutView() {
		label.setSize(label.getPreferredSize());
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		g.drawImage(RUCMPluginResource.getImage("sectionbar.png"), 0, 0, getWidth(), getHeight());
		final int x = (getWidth() - label.getWidth()) / 2;
		final int y = (getHeight() - label.getHeight()) / 2;
		g.translate(x, y);
		label.paintOnTarget(g);
		g.translate(-x, -y);
	}
	
	@Override
	public int getPreferredHeight() {
		return 28;
	}
	
}
