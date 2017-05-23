package cn.edu.buaa.sei.rucm.spec.widgets;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;

import co.gongzh.snail.Image;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;

public class FoldingButton extends View {
	
	private boolean folded;
	private boolean pressed;
	
	public FoldingButton() {
		setBackgroundColor(null);
		folded = false;
		pressed = false;
	}
	
	public boolean isFolded() {
		return folded;
	}
	
	public void setFolded(boolean folded) {
		if (this.folded && !folded) {
			// expand
			this.folded = folded;
			foldingButtonExpanded();
			setNeedsRepaint();
		} else if (!this.folded && folded) {
			// collapse
			this.folded = folded;
			foldingButtonCollapsed();
			setNeedsRepaint();
		}
	}
	
	@Override
	protected void mousePressed(MouseEvent e) {
		requestKeyboardFocus();
		setFolded(!folded);
		pressed = true;
		e.handle();
	}
	
	@Override
	protected void mouseReleased(MouseEvent e) {
		pressed = false;
		setNeedsRepaint();
		e.handle();
	}
	
	@Override
	protected void mouseDragged(MouseEvent e) {
		e.handle();
	}
	
	@Override
	protected void mouseClicked(MouseEvent e) {
		e.handle();
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		Image image = RUCMPluginResource.getImage(folded ? "folding_expand.png" : "folding_collapse.png");
		int x = (getWidth() - image.width) / 2;
		int y = (getHeight() - image.height) / 2;
		g.drawImage(image, x, y);
		if (pressed) {
			g.setColor(Button.OVERLAY_COLOR);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	@Override
	public int getPreferredHeight() {
		return 11;
	}
	
	@Override
	public int getPreferredWidth() {
		return 11;
	}
	
	protected void foldingButtonExpanded() {}
	protected void foldingButtonCollapsed() {}

}
