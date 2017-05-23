package cn.edu.buaa.sei.rucm.spec.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public class SectionScrollView extends ScrollView {
	
	private final List<SectionView> sections;
	
	public SectionScrollView() {
		sections = new ArrayList<SectionView>();
		getContentView().addEventHandler(POSITION_CHANGED, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				contentViewScrolled();
			}
		});
	}
	
	public final void addSection(SectionView section) {
		addSection(sections.size(), section);
	}
	
	public void addSection(int index, SectionView section) {
		sections.add(index, section);
		getContentView().addSubview(section, index);
		section.init(this);
		section.layoutSection();
	}
	
	public void removeSection(SectionView section) {
		if (sections.contains(section)) {
			sections.remove(section);
			section.removeFromSuperView();
			section.dispose();
			layout();
		} else {
			throw new IllegalArgumentException();
		}
	}

	public final SectionView getSection(int index) {
		return sections.get(index);
	}
	
	public final void removeSection(int index) {
		removeSection(getSection(index));
	}
	
	public final List<SectionView> sections() {
		return Collections.unmodifiableList(sections);
	}
	
	public final void clearSections() {
		while (!sections.isEmpty()) {
			removeSection(0);
		}
	}
	
	protected void contentViewScrolled() {
		int top = getContentView().getTop(); // top <= 0
		for (SectionView section : sections) {
			section.positionInScrollViewChanged(section.getTop() + top);
		}
	}
	
}
