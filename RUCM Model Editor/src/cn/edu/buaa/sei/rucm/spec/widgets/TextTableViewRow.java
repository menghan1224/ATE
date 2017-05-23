package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Color;
import java.util.List;

import co.gongzh.snail.Image;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.text.CaretIndex;
import co.gongzh.snail.text.TextView;
import co.gongzh.snail.util.Insets;
import co.gongzh.snail.util.Vector2D;

/**
 * <code>TextTableViewRow</code> is an useful implementation for {@link TableViewRow}. A <code>TextTableViewRow</code>
 * consists of a label for the key column, and an editable text cell for value column. <code>TextTableViewRow</code> also implements
 * the up, down and tab key behaviors. Clients can override following methods to support more keyboard behaviors:
 * <li>{@link #cellEnterPressed()}</li>
 * <li>{@link #cellBackspacePressed()}</li>
 * <li>{@link #cellGotFocus()}</li>
 * <li>{@link #cellLostFocus()}</li>
 * <p>
 * To customize the label, override {@link #createLabel()}. To customize the text cell, override {@link #createTextView(boolean)}.
 * 
 * @author Gong Zhang
 *
 */
public class TextTableViewRow implements TableViewRow, TextCellDelegate {
	
	public final static Color LABEL_BG_COLOR = new Color(0xf4f7fd);
	public final static Color LABEL_TEXT_COLOR = new Color(0x617896);
	
	
	private final RowLabel label;
	private final TextCell textCell;
	
	private TableView owner;
	
	/**
	 * Create a new <code>TextTableViewRow</code> with an empty single-line text cell.
	 * @param caption the caption of this row
	 */
	@Deprecated
	public TextTableViewRow(String caption) {
		this(caption, "");
	}
	
	/**
	 * Create a new <code>TextTableViewRow</code> with a single-line text cell.
	 * @param caption the caption of this row
	 * @param text the initial text of the text cell
	 */
	@Deprecated
	public TextTableViewRow(String caption, String text) {
		this(caption, text, false, true);
	}
	
	/**
	 * Create a new <code>TextTableViewRow</code>.
	 * @param caption the caption of this row
	 * @param text the initial text of the text cell
	 * @param multiline indicates whether the text cell supports multiple lines or not
	 * @param parseKeywords
	 */
	public TextTableViewRow(String caption, String text, boolean multiline, boolean parseKeywords) {
		this(null, caption, text, multiline, parseKeywords);
	}
	
	/**
	 * Create a new <code>TextTableViewRow</code>.
	 * @param icon the icon will be display before the label
	 * @param caption the caption of this row
	 * @param text the initial text of the text cell
	 * @param multiline indicates whether the text cell supports multiple lines or not
	 * @param parseKeywords
	 */
	public TextTableViewRow(Image icon, String caption, String text, boolean multiline, boolean parseKeywords) {
		owner = null;
		label = createLabel();
		label.setIcon(icon);
		label.getTextView().setText(caption);
		textCell = createTextView(multiline, parseKeywords);
		textCell.setText(text);
	}
	
	protected RowLabel createLabel() {
		RowLabel label = new RowLabel();
		label.setBackgroundColor(LABEL_BG_COLOR);
		label.setInsets(Insets.make(3, 6, 3, 6));
		label.setIconSpacing(3);
//		label.getTextView().setDefaultFont(SpecViewResource.FONT_DIALOG_BOLD);
		label.getTextView().setDefaultTextColor(LABEL_TEXT_COLOR);
//		label.getTextView().setTextAlignment(Alignment.LEFT_TOP);
		return label;
	}
	
	protected TextCell createTextView(boolean multiline, boolean parseKeywords) {
		TextCell textCell = new TextCell(this, multiline, parseKeywords);
		return textCell;
	}
	
	private final EventHandler textChangeHandler = new EventHandler() {
		@Override
		public void handle(View sender, Key key, Object arg) {
			owner.layout();
		}
	};
	
	private final EventHandler gotFocusHandler = new EventHandler() {
		@Override
		public void handle(View sender, Key key, Object arg) {
			cellGotFocus();
		}
	};
	
	private final EventHandler lostFocusHandler = new EventHandler() {
		@Override
		public void handle(View sender, Key key, Object arg) {
			cellLostFocus();
		}
	};

	@Override
	public void tableViewRowAdded(TableView table) {
		owner = table;
		textCell.addEventHandler(TextView.TEXT_LAYOUT_CHANGED, textChangeHandler);
		textCell.addEventHandler(View.GOT_KEYBOARD_FOCUS, gotFocusHandler);
		textCell.addEventHandler(View.LOST_KEYBOARD_FOCUS, lostFocusHandler);
	}

	@Override
	public void tableViewRowRemoved(TableView table) {
		textCell.removeEventHandler(View.GOT_KEYBOARD_FOCUS, gotFocusHandler);
		textCell.removeEventHandler(View.LOST_KEYBOARD_FOCUS, lostFocusHandler);
		textCell.removeEventHandler(TextView.TEXT_LAYOUT_CHANGED, textChangeHandler);
		owner = null;
	}
	
	public TableView getOwnerTable() {
		return owner;
	}

	@Override
	public RowLabel getKeyColumnView() {
		return label;
	}

	@Override
	public TextCell getValueColumnView() {
		return textCell;
	}

	@Override
	public int getKeyColumnPreferredWidth(TableView table) {
		return label.getPreferredWidth();
	}
	
	@Override
	public final void cellTabPressed(TextCell c, boolean shift) {
		List<TableViewRow> rows = getOwnerTable().rows();
		int index = rows.indexOf(this);
		if (!shift) {
			// focus to next
			if (index < rows.size() - 1) {
				View view = rows.get(index + 1).getValueColumnView();
				if (view instanceof TextCell) {
					TextCell cell = (TextCell) view;
					cell.requestKeyboardFocus();
//					cell.setSelectionRange(Range.make(0, cell.textLength()));
					cell.setCaretPosition(CaretIndex.before(cell.textLength()));
				}
			}
		} else {
			// focus to previous
			if (index > 0) {
				View view = rows.get(index - 1).getValueColumnView();
				if (view instanceof TextCell) {
					TextCell cell = (TextCell) view;
					cell.requestKeyboardFocus();
//					cell.setSelectionRange(Range.make(0, cell.textLength()));
					cell.setCaretPosition(CaretIndex.before(cell.textLength()));
				}
			}
		}
	}
	
	@Override
	public final void cellUpPressed(TextCell c, int offset) {
		List<TableViewRow> rows = getOwnerTable().rows();
		int index = rows.indexOf(this);
		if (index > 0) {
			View view = rows.get(index - 1).getValueColumnView();
			if (view instanceof TextCell) {
				TextCell cell = (TextCell) view;
				cell.requestKeyboardFocus();
				Vector2D point = cell.basePointAtCaretIndex(CaretIndex.before(cell.textLength()));
				point.x = offset;
				cell.setCaretPosition(cell.caretIndexNearPoint(point));
			}
		}
	}
	
	@Override
	public final void cellDownPressed(TextCell c, int offset) {
		List<TableViewRow> rows = getOwnerTable().rows();
		int index = rows.indexOf(this);
		if (index < rows.size() - 1) {
			View view = rows.get(index + 1).getValueColumnView();
			if (view instanceof TextCell) {
				TextCell cell = (TextCell) view;
				cell.requestKeyboardFocus();
				Vector2D point = cell.basePointAtCaretIndex(CaretIndex.before(0));
				point.x = offset;
				cell.setCaretPosition(cell.caretIndexNearPoint(point));
			}
		}
	}

	@Override
	public void cellEnterPressed(TextCell cell) {
	}

	@Override
	public void cellBackspacePressed(TextCell cell) {
	}
	
	protected void cellGotFocus() {
	}
	
	protected void cellLostFocus() {
	}

}
