package cn.edu.buaa.sei.rucm.spec.widgets;

import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Insets;

/**
 * <code>SectionView</code> provides a generic section area for use case template. Clients can add customized view into
 * the section. See the implementation of {@link BasicSection} to understand the behavior of <code>SectionView</code>.
 * 
 * @author Gong Zhang
 *
 */
public class SectionView extends View {
	
	private SectionScrollView owner;
	private int preferredHeight;
	private final Insets insets;
	private int spacing;

	public SectionView() {
		this.owner = null;
		this.preferredHeight = 0;
		this.insets = Insets.make();
		this.spacing = 0;
		setBackgroundColor(null);
		setPaintMode(PaintMode.DISABLED);
		addEventHandler(SIZE_CHANGED, new EventHandler() {
			int cachedWidth = 0;
			boolean mutex = false;
			@Override
			public void handle(View sender, Key key, Object arg) {
				if (mutex) return;
				mutex = true;
				if (sender.getWidth() != cachedWidth) {
					cachedWidth = sender.getWidth();
					layoutSection();
				}
				mutex = false;
			}
		});
	}
	
	public Insets getInsets() {
		return insets;
	}
	
	public void setInsets(Insets insets) {
		this.insets.set(insets);
		layoutSection();
	}
	
	public int getSpacing() {
		return spacing;
	}
	
	public void setSpacing(int spacing) {
		this.spacing = spacing;
		layoutSection();
	}
	
	public void layoutSection() {
		// layout subviews
		int top = insets.top;
		final int width = getWidth() - insets.left - insets.right;
		for (View view : this) {
			view.setPosition(insets.left, top);
			view.setWidth(width);
			view.setHeight(view.getPreferredHeight());
			top += view.getHeight() + spacing;
		}
		if (count() > 0) top -= spacing;
		top += insets.bottom;
		
		// determine preferred height
		if (preferredHeight != top) {
			preferredHeight = top;
			// notify owner to layout
			if (owner != null) owner.layout();
		}
	}
	
	public SectionScrollView getOwnerScrollView() {
		return owner;
	}
	
	@Override
	public int getPreferredHeight() {
		return preferredHeight;
	}
	
	protected void init(SectionScrollView owner) {
		this.owner = owner;
	}
	
	protected void dispose() {
		this.owner = null;
	}
	
	protected void positionInScrollViewChanged(int top) {
	}
	
}
