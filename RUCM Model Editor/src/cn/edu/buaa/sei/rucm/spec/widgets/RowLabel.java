package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Font;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource.SpecFontChangeListener;

public class RowLabel extends Label {

	public RowLabel() {
		getTextView().setDefaultFont(RUCMPluginResource.getSpecFont(true));
		RUCMPluginResource.addSpecFontChangeListener(fontChangeListener);
	}
	
	@Override
	public void removeFromSuperView() {
		RUCMPluginResource.removeSpecFontChangeListener(fontChangeListener);
		super.removeFromSuperView();
	}
	
	private final SpecFontChangeListener fontChangeListener = new SpecFontChangeListener() {
		@Override
		public void fontChanged(Font plainFont, Font boldFont) {
			getTextView().setDefaultFont(boldFont);
			getTextView().setText(getTextView().getPlainText());
		}
	};
	
}
