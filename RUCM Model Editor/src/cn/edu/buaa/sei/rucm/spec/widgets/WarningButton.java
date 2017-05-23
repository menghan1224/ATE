package cn.edu.buaa.sei.rucm.spec.widgets;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;

import co.gongzh.snail.Image;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;

public class WarningButton extends View {

	public WarningButton() {
		setBackgroundColor(null);
	}
	
	@Override
	protected void mousePressed(MouseEvent e) {
		requestKeyboardFocus();
		e.handle();
	}
	
	@Override
	protected void mouseReleased(MouseEvent e) {
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
		Image image = RUCMPluginResource.getImage("warning.gif");
		int x = (getWidth() - image.width) / 2;
		int y = (getHeight() - image.height) / 2;
		g.drawImage(image, x, y + 1);
	}
	
	@Override
	public int getPreferredHeight() {
		return 16;
	}
	
	@Override
	public int getPreferredWidth() {
		return 16;
	}

}
