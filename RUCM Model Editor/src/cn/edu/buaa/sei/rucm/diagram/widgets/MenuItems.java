package cn.edu.buaa.sei.rucm.diagram.widgets;


import java.awt.Color;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.spec.widgets.Label;
import co.gongzh.snail.Image;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.util.Insets;

public final class MenuItems {
	
	private MenuItems() {
	}
	
	public static class LabelItem extends Label {
		
		public LabelItem() {
			setBackgroundColor(RUCMPluginResource.MENU_BG_COLOR);
			getTextView().setDefaultFont(RUCMPluginResource.FONT_DIALOG_BOLD);
			getTextView().setDefaultTextColor(RUCMPluginResource.MENU_TEXT_COLOR);
			setInsets(Insets.make(4, 4, 4, 10));
			setIconSpacing(4);
		}
		
		public LabelItem(Image icon, String text) {
			this();
			setIcon(icon);
			getTextView().setText(text);
		}
		
		public LabelItem(String text) {
			this(null, text);
		}
		
		@Override
		protected void mouseEntered() {
			super.mouseEntered();
			getTextView().setTextColor(Color.WHITE);
			setBackgroundColor(Color.BLACK);
		}
		
		@Override
		protected void mouseExited() {
			super.mouseExited();
			getTextView().setTextColor(RUCMPluginResource.MENU_TEXT_COLOR);
			setBackgroundColor(RUCMPluginResource.MENU_BG_COLOR);
		}
		
	}
	
	public static class Separator extends View {
		
		public Separator() {
			setBackgroundColor(Color.black);
			setPaintMode(PaintMode.DIRECTLY);
		}
		
		@Override
		public int getPreferredWidth() {
			return 100;
		}
		
		@Override
		public int getPreferredHeight() {
			return 6;
		}
		
		@Override
		protected void repaintView(ViewGraphics g) {
			int y = getHeight() / 2;
			g.setColor(RUCMPluginResource.SEPERATOR_LINE_COLOR);
			g.drawLine(0, y, getWidth() - 1, y);
		}
		
	}
	
	public static class DisabledLabelItem extends Label {
		
		public DisabledLabelItem() {
			setBackgroundColor(RUCMPluginResource.MENU_BG_COLOR);
			getTextView().setDefaultFont(RUCMPluginResource.FONT_DIALOG_BOLD);
			getTextView().setDefaultTextColor(Color.LIGHT_GRAY);
			setInsets(Insets.make(4, 4, 4, 10));
			setIconSpacing(4);
		}
		
		public DisabledLabelItem(Image icon, String text) {
			this();
			setIcon(icon);
			getTextView().setText(text);
		}
		
		public DisabledLabelItem(String text) {
			this(null, text);
		}
		
	}

}
