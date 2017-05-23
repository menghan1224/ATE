package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Color;
import java.awt.Font;
import java.text.BreakIterator;
import java.util.Collections;
import java.util.List;

import ca.carleton.sce.squall.ucmeta.util.SentenceUtility;
import ca.carleton.sce.squall.ucmeta.util.TextRange;
import ca.carleton.sce.squall.ucmeta.util.Token;
import ca.carleton.sce.squall.ucmeta.util.Token.KeywordToken;
import ca.carleton.sce.squall.ucmeta.util.Token.TokenType;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource.SpecFontChangeListener;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource.SpecTextAAModeChangeListener;
import cn.edu.buaa.sei.rucm.spec.keywords.KeywordColoring;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.text.AttributedString;
import co.gongzh.snail.text.CaretIndex;
import co.gongzh.snail.text.EditableTextView;
import co.gongzh.snail.text.TextView;
import co.gongzh.snail.util.Insets;
import co.gongzh.snail.util.Range;
import co.gongzh.snail.util.Vector2D;

public class TextCell extends EditableTextView {
	
	public final static Color TEXT_COLOR = new Color(0x2c2c2c);
	public final static Color COMPOSED_TEXT_COLOR = Color.GRAY;
	
//	private final static Insets NORMAL_INSETS = Insets.make(3, 6, 3, 6);
//	private final static Insets WARNING_INSETS = Insets.make(3, 6, 3, 3 + 16 + 3);
	
	private final TextCellDelegate ownerRow;
	private final boolean multiline;
	
	private final TextView hintTextView;
	private final WarningButton warningButton; // TODO: support warning button handler
	private Object warningContext; // TODO: replace with concrete class
	private int indentation;
	private int indentationWidth;
	
	public static final int DEFAULT_INDENTATION_WIDTH = 24;
	
	private List<Token> tokens;
	
	private final TextCellUndoRedo undoRedo;
	public TextCell(TextCellDelegate delegate, boolean multiline, final boolean parseKeywords) {
		this.ownerRow = delegate;
		this.multiline = multiline;
		this.indentation = 0;
		indentationWidth = DEFAULT_INDENTATION_WIDTH;
		setDefaultFont(RUCMPluginResource.getSpecFont(false));
		setDefaultTextColor(TEXT_COLOR);
		setBreakIterator(BreakIterator.getLineInstance());
		setTextAntialiasing(RUCMPluginResource.getSpecTextAntialiasing());
		
		// hint text
		hintTextView = new TextView();
		hintTextView.setDefaultFont(getDefaultFont());
		hintTextView.setDefaultTextColor(Color.GRAY);
		hintTextView.setInsets(getInsets());
		hintTextView.setBreakIterator(BreakIterator.getLineInstance());
		addSubview(hintTextView);
		addEventHandler(TEXT_CHANGED, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				if (!isKeyboardFocus()) {
					if (textLength() == 0 && hintTextView.getSuperView() == null) {
						addSubview(hintTextView, 0);
						layout();
					} else if (textLength() > 0 && hintTextView.getSuperView() != null) {
						hintTextView.removeFromSuperView();
					}
				}
			}
		});
		
		// warning button
		warningButton = new WarningButton();
		
		// keyword coloring
		addEventHandler(TEXT_CHANGED, new EventHandler() {
			private boolean mutex = false;
			@Override
			public void handle(View sender, Key key, Object arg) {
				if (!mutex && parseKeywords) {
					mutex = true;
					String text = getPlainText();
					setText(text);
					tokens = Collections.unmodifiableList(SentenceUtility.tokenizeSentence(text));
					for (Token token : tokens) {
						if (token.getType() == TokenType.KEYWORD) {
							TextRange tr = token.getRange();
							Range range = Range.make(tr.getOffset(), tr.getLength());
							setFont(range, RUCMPluginResource.getSpecFont(true));
							setTextColor(range, KeywordColoring.getColor(((KeywordToken) token).getKeyword()));
						}
					}
					mutex = false;
				}
			}
		});
		
		// undo/redo
		undoRedo = new TextCellUndoRedo(this);
		
		RUCMPluginResource.addSpecFontChangeListener(fontChangeListener);
		RUCMPluginResource.addSpecTextAAModeChangeListener(aaChangeListener);
		Cursor.setCursorOnView(this, Cursor.TEXT_CURSOR);
		
		// keyword assist
		if (parseKeywords) {
			new TextCellKeywordAssist(this);
		}
		
		updateInsets();
	}
	
	public int getIndentation() {
		return indentation;
	}
	
	public void setIndentation(int indentation) {
		this.indentation = indentation;
		updateInsets();
	}
	
	public int getIndentationWidth() {
		return indentationWidth;
	}
	
	public void setIndentationWidth(int indentationWidth) {
		this.indentationWidth = indentationWidth;
		updateInsets();
	}
	
	@Override
	public void removeFromSuperView() {
		RUCMPluginResource.removeSpecTextAAModeChangeListener(aaChangeListener);
		RUCMPluginResource.removeSpecFontChangeListener(fontChangeListener);
		super.removeFromSuperView();
	}
	
	private final SpecTextAAModeChangeListener aaChangeListener = new SpecTextAAModeChangeListener() {
		@Override
		public void textAAModeChanged(co.gongzh.snail.text.TextAntialiasing mode) {
			setTextAntialiasing(mode);
		}
	};
	
	private final SpecFontChangeListener fontChangeListener = new SpecFontChangeListener() {
		@Override
		public void fontChanged(Font plainFont, Font boldFont) {
			setDefaultFont(plainFont);
			setText(getPlainText());
			hintTextView.setDefaultFont(plainFont);
			hintTextView.setText(hintTextView.getPlainText());
		}
	};
	
	public List<Token> getTokens() {
		return tokens; // the list is unmodifiable
	}
	
	public boolean isMultiline() {
		return multiline;
	}
	
	public TextCellDelegate getOwnerRow() {
		return ownerRow;
	}
	
	public String getHintText() {
		return hintTextView.getPlainText();
	}
	
	public void setHintText(String text) {
		hintTextView.setText(text);
		layout();
	}
	
	public void setWarningButtonVisible(final boolean visible, Object warningContext) {
		if (visible) {
			if (warningButton.getSuperView() == null) {
				addSubview(warningButton);
				updateInsets();
				layout();
				fireEvent(TEXT_LAYOUT_CHANGED, null); // XXX: why?
			}
		} else {
			if (warningButton.getSuperView() != null) {
				warningButton.removeFromSuperView();
				updateInsets();
				fireEvent(TEXT_LAYOUT_CHANGED, null); // XXX: why?
			}
		}
		this.warningContext = warningContext;
	}
	
	public Object getWarningContext() { // TODO: replace with concrete class
		return warningContext;
	}
	
	@Override
	public boolean isInside(Vector2D point) {
		if (super.isInside(point)) return true;
		else {
			Vector2D p;
			for (View subView : this) {
				p = subView.transformPointFromSuperView(point);
				if (subView.isInside(p)) return true;
			}
		}
		return false;
	}
	
	@Override
	protected void layoutView() {
		super.layoutView();
		if (hintTextView.getSuperView() != null) {
			View.scaleViewWithMarginToSuperView(hintTextView, 0);
		}
		if (warningButton.getSuperView() != null) {
			warningButton.setSize(warningButton.getPreferredWidth(), getHeight());
			View.putViewWithRightAndTop(warningButton, 3, 0);
		}
	}
	
	@Override
	protected void gotKeyboardFocus() {
		super.gotKeyboardFocus();
		if (hintTextView.getSuperView() != null) {
			hintTextView.removeFromSuperView();
		}
		setWarningButtonVisible(false, null);
	}
	
	@Override
	protected void lostKeyboardFocus() {
		super.lostKeyboardFocus();
		setSelectionRange(Range.make(0, 0));
		if (textLength() == 0 && hintTextView.getSuperView() == null) {
			addSubview(hintTextView, 0);
			layout();
		}
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
			if (shift && multiline) {
				super.doEditableTextViewKeyCommand(keyCode, ctrl, false, caret, sel);
			} else {
				resignKeyboardFocus();
				getViewContext().getRootView().requestKeyboardFocus();
				// notify enter to owner row
				ownerRow.cellEnterPressed(this);
			}
			
		} else if (keyCode == java.awt.event.KeyEvent.VK_TAB) {
			// notify tab pressed
			ownerRow.cellTabPressed(this, shift);
			
		} else if (keyCode == java.awt.event.KeyEvent.VK_BACK_SPACE) {
			if (caret.toBefore().charIndex == 0 && sel.length == 0) {
				resignKeyboardFocus();
				getViewContext().getRootView().requestKeyboardFocus();
				// notify backspace pressed
				ownerRow.cellBackspacePressed(this);
			} else {
				super.doEditableTextViewKeyCommand(keyCode, ctrl, shift, caret, sel);
			}
		
		} else if (keyCode == java.awt.event.KeyEvent.VK_UP) {
			int line = lineIndexOfCaretIndex(caret);
			if (line == 0) {
				// notify up pressed
				ownerRow.cellUpPressed(this, basePointAtCaretIndex(caret).x);
			} else {
				super.doEditableTextViewKeyCommand(keyCode, ctrl, shift, caret, sel);
			}
			
		} else if (keyCode == java.awt.event.KeyEvent.VK_DOWN) {
			int line = lineIndexOfCaretIndex(caret);
			if (line == lineIndexOfCaretIndex(CaretIndex.before(textLength()))) {
				// notify down pressed
				ownerRow.cellDownPressed(this, basePointAtCaretIndex(caret).x);
			} else {
				super.doEditableTextViewKeyCommand(keyCode, ctrl, shift, caret, sel);
			}
			
		} else if (keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
			resignKeyboardFocus();
			getViewContext().getRootView().requestKeyboardFocus();
			
		} else if (keyCode == java.awt.event.KeyEvent.VK_Z) {
			if (ctrl && !shift) {
				undoRedo.undo();
			} else if (ctrl && shift) {
				undoRedo.redo();
			}
			
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
			if (!(ch == '\t' || (!multiline && (ch == '\n' || ch == '\r')))) {
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
			if (ch == '\t' || (!multiline && (ch == '\n' || ch == '\r'))) {
				text.delete(Range.make(i, 1));
				i--;
			}
		}
		super.filterUserInputText(text);
	}
	
	private void updateInsets() {
		setInsets(Insets.make(
				3,
				6 + indentation * indentationWidth,
				3,
				warningButton.getSuperView() == null ? 6 : 3 + 16 + 3));
		hintTextView.setInsets(getInsets());
	}
	
}
