package cn.edu.buaa.sei.rucm.spec;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.Sentence;
import cn.edu.buaa.sei.rucm.spec.widgets.Button;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCellDelegate;
import cn.edu.buaa.sei.rucm.spec.widgets.TextTableViewRow;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.text.CaretIndex;
import co.gongzh.snail.util.Range;

public class FreeFormStepCell extends View implements TextCellDelegate {

	private final TextCell textCell;
	private final View buttonBar;
	private final Button doneButton;
	private final Button cancelButton;
	
	public FreeFormStepCell(List<Sentence> steps) {
		setPaintMode(PaintMode.DISABLED);
		
		textCell = new TextCell(this, true, false) {
			@Override
			protected void doEditableTextViewKeyCommand(int keyCode, boolean ctrl, boolean shift, CaretIndex caret, Range sel) {
				if (keyCode == java.awt.event.KeyEvent.VK_ENTER) {
					super.doEditableTextViewKeyCommand(keyCode, ctrl, true, caret, sel);
				} else {
					super.doEditableTextViewKeyCommand(keyCode, ctrl, shift, caret, sel);
				}
			}
		};
		for (int i = 0; i < steps.size(); i++) {
			if (i > 0) textCell.insertText(textCell.textLength(), "\n");
			textCell.insertText(textCell.textLength(), steps.get(i).getContent());
		}
		addSubview(textCell);
		
		buttonBar = new View();
		buttonBar.setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
		buttonBar.setPaintMode(PaintMode.DIRECTLY);
		addSubview(buttonBar);
		
		doneButton = new Button() {
			@Override
			protected void mouseClicked(MouseEvent e) {
				buttonClicked(false);
				super.mouseClicked(e);
			}
		};
		doneButton.setText("Done");
		buttonBar.addSubview(doneButton);
		
		cancelButton = new Button() {
			@Override
			protected void mouseClicked(MouseEvent e) {
				buttonClicked(true);
				super.mouseClicked(e);
			}
		};
		cancelButton.setText("Cancel");
		buttonBar.addSubview(cancelButton);
	}
	
	public String[] getLines() {
		return textCell.getPlainText().split("\n");
	}
	
	protected void buttonClicked(boolean cancel) {
	}
	
	@Override
	protected void layoutView() {
		final int barHeight = 30;
		if (textCell != null) {
			View.scaleViewWithLeftAndRight(textCell, 0, 0);
			View.scaleViewWithTopAndBottom(textCell, 0, barHeight);
			View.scaleViewWithLeftAndRight(buttonBar, 0, 0);
			buttonBar.setHeight(barHeight);
			View.putViewWithBottom(buttonBar, 0);
			int buttonWidth = Math.max(doneButton.getPreferredWidth(), cancelButton.getPreferredWidth());
			View.scaleViewWithTopAndBottom(doneButton, 4, 4);
			View.scaleViewWithTopAndBottom(cancelButton, 4, 4);
			doneButton.setLeft(8);
			doneButton.setWidth(buttonWidth);
			View.putViewAtRightSideOfView(cancelButton, doneButton, 8);
			cancelButton.setWidth(buttonWidth);
		}
	}

	@Override
	public void cellTabPressed(TextCell cell, boolean shift) {
	}

	@Override
	public void cellUpPressed(TextCell cell, int offset) {
	}

	@Override
	public void cellDownPressed(TextCell cell, int offset) {
	}

	@Override
	public void cellEnterPressed(TextCell cell) {
	}

	@Override
	public void cellBackspacePressed(TextCell cell) {
	}
	
}
