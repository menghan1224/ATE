package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Color;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.Image;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Insets;

public class DropDownListItem extends Label {
	
	public static final Key DROPDOWN_LISTITEM_SELECTION_CHANGED = new Key("dropDownListItemSelectionChanged", DropDownList.class, null);
	
	boolean selected = false;

	public DropDownListItem() {
		setBackgroundColor(Color.WHITE);
		getTextView().setDefaultFont(RUCMPluginResource.FONT_DIALOG_BOLD);
		getTextView().setDefaultTextColor(Color.BLACK);
		setInsets(Insets.make(2, 8, 5, 10));
		setIconSpacing(4);
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		if (this.selected != selected) {
			this.selected = selected;
			fireEvent(DROPDOWN_LISTITEM_SELECTION_CHANGED, null);
			if (selected) {
				getTextView().setTextColor(Color.WHITE);
				setBackgroundColor(Color.BLACK);
			} else {
				getTextView().setTextColor(Color.BLACK);
				setBackgroundColor(Color.WHITE);
			}
		}
	}
	
	public DropDownListItem(Image icon, String text) {
		this();
		setIcon(icon);
		getTextView().setText(text);
	}
	
	public DropDownListItem(String text) {
		this(null, text);
	}
	
}
