package cn.edu.buaa.sei.rucm.spec.widgets;

import java.util.ArrayDeque;
import java.util.Deque;

import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.text.CaretIndex;
import co.gongzh.snail.text.EditableTextView;
import co.gongzh.snail.text.TextChangeEvent;
import co.gongzh.snail.util.Range;

class TextCellUndoRedo {
	
	private final TextCell target;
	private final Deque<TextChangeEvent> undoQueue;
	private final Deque<TextChangeEvent> redoQueue;
	
	TextCellUndoRedo(TextCell target) {
		this.target = target;
		this.undoQueue = new ArrayDeque<TextChangeEvent>();
		this.redoQueue = new ArrayDeque<TextChangeEvent>();
		target.addEventHandler(EditableTextView.TEXT_CHANGED_BY_UI, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				TextChangeEvent event = (TextChangeEvent) arg;
				redoQueue.clear();
				undoQueue.push(event);
			}
		});
		target.addEventHandler(View.LOST_KEYBOARD_FOCUS, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				undoQueue.clear();
				redoQueue.clear();
			}
		});
	}
	
	void undo() {
		if (!undoQueue.isEmpty()) {
			target.setSelectionRange(null);
			TextChangeEvent event = undoQueue.pop();
			switch (event.type) {
			case INSERT:
				if (delete(event.offset, event.text)) {
					redoQueue.push(event);
				} else {
					undoQueue.clear();
					redoQueue.clear();
				}
				break;
				
			case DELETE:
				if (insert(event.offset, event.text)) {
					redoQueue.push(event);
				} else {
					undoQueue.clear();
					redoQueue.clear();
				}
				break;
			}
		}
	}
	
	void redo() {
		if (!redoQueue.isEmpty()) {
			target.setSelectionRange(null);
			TextChangeEvent event = redoQueue.pop();
			switch (event.type) {
			case INSERT:
				if (insert(event.offset, event.text)) {
					undoQueue.push(event);
				} else {
					undoQueue.clear();
					redoQueue.clear();
				}
				break;
				
			case DELETE:
				if (delete(event.offset, event.text)) {
					undoQueue.push(event);
				} else {
					undoQueue.clear();
					redoQueue.clear();
				}
				break;
			}
		}
	}
	
	private boolean delete(int offset, String text) {
		if (offset + text.length() > target.textLength()) {
			return false;
		} else if (!target.getPlainText(Range.make(offset, text.length())).equals(text)) {
			return false;
		} else {
			target.deleteText(Range.make(offset, text.length()));
			target.setCaretPosition(CaretIndex.before(offset));
			return true;
		}
	}
	
	private boolean insert(int offset, String text) {
		if (offset > target.textLength()) {
			return false;
		} else {
			target.insertText(offset, text);
			target.setCaretPosition(CaretIndex.before(offset + text.length()));
			return true;
		}
	}

}
