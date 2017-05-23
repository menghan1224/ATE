package cn.edu.buaa.sei.rucm.diagram.widgets;

import java.awt.Color;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.text.CaretIndex;
import co.gongzh.snail.text.TextView;
import co.gongzh.snail.util.Alignment;
import co.gongzh.snail.util.Insets;
import co.gongzh.snail.util.Range;

public class LabeledTextBox extends TextView {
	
	public static final Key START_EDITING = new Key("startEditing", LabeledTextBox.class, null);
	public static final Key FINISH_EDITING = new Key("finishEditing", LabeledTextBox.class, null);
	
	public static final int CLICK_TO_EDIT = 0;
	public static final int DOUBLE_CLICK_TO_EDIT = 1;
	
	private TextBox textBox;
	private int editingMode;
	
	public LabeledTextBox() {
		this(CLICK_TO_EDIT);
	}
	
	public LabeledTextBox(int editingMode) {
		this.editingMode = editingMode;
		textBox = new TextBox();
		textBox.addEventHandler(LOST_KEYBOARD_FOCUS, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				if (sender.getSuperView() != null) {
					setText(((TextBox) sender).getPlainText());
					sender.removeFromSuperView();
					LabeledTextBox.this.fireEvent(FINISH_EDITING, null);
				}
			}
		});
		
		setBackgroundColor(Color.WHITE);
		setInsets(Insets.make(2, 4, 2, 4));
		setDefaultFont(RUCMPluginResource.FONT_DIALOG_BOLD);
		setTextAlignment(Alignment.CENTER_CENTER);
	}
	
	@Override
	protected void gotKeyboardFocus() {
		super.gotKeyboardFocus();
//		startEditing();
	}
	
	private void startEditing() {
		if (textBox.getSuperView() == null) {
			addSubview(textBox);
			layout();
			textBox.setDefaultFont(getDefaultFont());
			textBox.setTextAlignment(getTextAlignment());
			textBox.setText(getPlainText());
			textBox.requestKeyboardFocus();
			textBox.setSelectionRange(Range.make(0, textBox.textLength()));
			textBox.setCaretPosition(CaretIndex.before(textBox.textLength()));
			fireEvent(START_EDITING, null);
		}
	}
	
	@Override
	protected void layoutView() {
		super.layoutView();
		if (textBox.getSuperView() != null) {
			View.scaleViewWithLeftAndRight(textBox, -3, -3);
			View.scaleViewWithTopAndBottom(textBox, -3, -7);
		}
	}
	
	@Override
	protected void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
			if (editingMode == CLICK_TO_EDIT) {
				requestKeyboardFocus();
				startEditing();
				e.handle();
			} else if (editingMode == DOUBLE_CLICK_TO_EDIT && e.getClickCount() == 2) {
				requestKeyboardFocus();
				startEditing();
				e.handle();
			}
		}
	}
	
	@Override
	protected void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
		if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
			if (editingMode == CLICK_TO_EDIT) {
				e.handle();
			} else if (editingMode == DOUBLE_CLICK_TO_EDIT && e.getClickCount() == 2) {
				e.handle();
			}
		}
	}
	
	@Override
	protected void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
			if (editingMode == CLICK_TO_EDIT) {
				e.handle();
			} else if (editingMode == DOUBLE_CLICK_TO_EDIT && e.getClickCount() == 2) {
				e.handle();
			}
		}
	}

}
