package cn.edu.buaa.sei.rucm.diagram.widgets;

import java.awt.Color;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.spec.widgets.Cursor;

import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.text.AttributedString;
import co.gongzh.snail.text.CaretIndex;
import co.gongzh.snail.text.EditableTextView;
import co.gongzh.snail.util.Alignment;
import co.gongzh.snail.util.Insets;
import co.gongzh.snail.util.Range;

public class TextBox extends EditableTextView {
	
	public final static Color COMPOSED_TEXT_COLOR = Color.GRAY;
	
	public TextBox() {
		setBackgroundColor(null);
		setInsets(Insets.make(4, 8, 8, 8));
		setDefaultFont(RUCMPluginResource.FONT_DIALOG);
		setDefaultTextColor(RUCMPluginResource.TEXT_BOX_TEXT_COLOR);
		setTextAlignment(Alignment.CENTER_CENTER);
		Cursor.setCursorOnView(this, java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		g.drawImage(RUCMPluginResource.getImage("textbox.png"), 0, 0, getWidth(), getHeight());
		super.repaintView(g);
	}
	
	@Override
	protected void lostKeyboardFocus() {
		super.lostKeyboardFocus();
		setSelectionRange(Range.make(0, 0));
	}
	
	@Override
	protected void applyFontAndColorOnComposedText(AttributedString text) {
		Range r = Range.make(0, text.length());
		text.setColor(r, COMPOSED_TEXT_COLOR);
		text.setFont(r, RUCMPluginResource.getSpecFont(false));
	}
	
	@Override
	protected void doEditableTextViewKeyCommand(int keyCode, boolean ctrl, boolean shift, CaretIndex caret, Range sel) {
		if (keyCode == java.awt.event.KeyEvent.VK_ENTER) {
			resignKeyboardFocus();
			
		} else if (keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
			resignKeyboardFocus();
			
		} else {
			super.doEditableTextViewKeyCommand(keyCode, ctrl, shift, caret, sel);
		}
	}
	
	@Override
	public void setText(String str) {
		// NOTE: this override method is to filter the special characters in the model  
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (!(ch == '\t' || ch == '\n' || ch == '\r')) {
				buf.append(ch);
			}
		}
		super.setText(buf.toString());
	}
	
	@Override
	protected void filterUserInputText(AttributedString text) {
		// filter tab and new line
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == '\t' || ch == '\n' || ch == '\r') {
				text.delete(Range.make(i, 1));
				i--;
			}
		}
		super.filterUserInputText(text);
	}

}
