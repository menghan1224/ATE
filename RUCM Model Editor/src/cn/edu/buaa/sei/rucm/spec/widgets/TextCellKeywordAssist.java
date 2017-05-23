package cn.edu.buaa.sei.rucm.spec.widgets;

import java.util.ArrayList;
import java.util.List;

import ca.carleton.sce.squall.ucmeta.util.Keyword;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.text.CaretIndex;
import co.gongzh.snail.util.Range;
import co.gongzh.snail.util.Vector2D;

public class TextCellKeywordAssist {
	
	private final TextCell target;
	private final DropDownList list;
	
	private int startIndex;
	
	public TextCellKeywordAssist(TextCell target) {
		this.target = target;
		this.list = new DropDownList();
		this.target.addEventHandler(View.LOST_KEYBOARD_FOCUS, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				// dismiss list
				if (list.isShown()) {
					dismissList();
				}
			}
		});
		this.target.addEventHandler(TextCell.PRE_KEY_TYPED, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				KeyEvent e = (KeyEvent) arg;
				char ch = e.getKeyChar();
				if (ch != java.awt.event.KeyEvent.CHAR_UNDEFINED) {
					if (!list.isShown()) {
						// not shown -> trigger
						triggerList(ch);
					} else {
						// update
						updateList(ch);
					}
				} else {
					// undefined char -> dismiss list
					if (list.isShown()) {
						dismissList();
					}
				}
			}
		});
		this.target.addEventHandler(TextCell.PRE_KEY_PRESSED, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				KeyEvent e = (KeyEvent) arg;
				if (list.isShown()) {
					if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
						list.selectPrevious();
						e.handle();
					} else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
						list.selectNext();
						e.handle();
					} else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
						dismissList();
						e.handle();
					} else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
						// commit text
						DropDownListItem item = list.getSelectedItem();
						if (item != null) {
							commitKeyword(item.getTextView().getPlainText());
						}
						dismissList();
						e.handle();
					}
				}
			}
		});
	}
	
	private void commitKeyword(String keyword) {
		CaretIndex index = target.getCaretPosition().toBefore();
		target.deleteText(Range.make(startIndex, index.charIndex - startIndex));
		target.insertText(startIndex, keyword);
		target.setCaretPosition(CaretIndex.before(startIndex + keyword.length()));
	}
	
	private boolean triggerList(char ch) {
		if (Character.isLetter(ch)) {
			CaretIndex index = target.getCaretPosition().toBefore();
			if (index.charIndex == 0 ||
				(index.charIndex <= target.textLength() && Character.isWhitespace(target.charAt(index.charIndex - 1)))) {
				List<Keyword> keywords = new ArrayList<Keyword>();
				for (Keyword keyword : Keyword.allKeywords()) {
					if (keyword.getName().charAt(0) == ch&&!keyword.getName().equals("ABORT")) {
						keywords.add(keyword);
					}
				}
				if (keywords.size() > 0) {
					startIndex = index.charIndex;
					resetListItem(keywords);
					Vector2D loc = target.basePointAtCaretIndex(index);
					loc.y += target.descentAtCaretIndex(index);
					loc = target.transformPointToRootView(loc);
					list.popup(target.getViewContext(), loc);
					return true;
				}
			}
		}
		return false;
	}
	
	private void updateList(char ch) {
		CaretIndex index = target.getCaretPosition().toBefore();
		if (index.charIndex > startIndex && index.charIndex <= target.textLength()) {
			String prefix = target.getPlainText(Range.make(startIndex, index.charIndex - startIndex)).toUpperCase();
			prefix = prefix.concat(String.valueOf(Character.toUpperCase(ch)));
			List<Keyword> keywords = new ArrayList<Keyword>();
//			for (Keyword keyword : Keyword.allKeywords()) {
//				if (keyword.getName().startsWith(prefix)) {
//					keywords.add(keyword);
//				}
//			}
			if (keywords.size() > 0) {
				resetListItem(keywords);
			} else {
				dismissList();
			}
		} else {
			dismissList();
		}
	}
	
	private void resetListItem(List<Keyword> keywords) {
		list.removeAllSubviews();
		for (Keyword keyword : keywords) {
			DropDownListItem item = new DropDownListItem(keyword.getName());
			list.addSubview(item);
		}
		list.setSize(list.getPreferredSize());
		list.layout();
		list.selectFirst();
	}
	
	private void dismissList() {
		list.dismiss();
		list.removeAllSubviews();
	}

}
