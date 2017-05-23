package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Color;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;

import co.gongzh.snail.Image;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;

public class Button extends View {
	
	public static final Color TEXT_COLOR = new Color(0x24252b);
	public static final Color OVERLAY_COLOR = new Color(0, 0, 0, 0.1f);
	
	private final Label label;
	private boolean pressed;
	
	public Button() {
		setBackgroundColor(null);
		pressed = false;
		label = new Label();
		label.setBackgroundColor(null);
		label.setPaintMode(PaintMode.DIRECTLY);
		label.setIconSpacing(3);
		label.getTextView().setDefaultFont(RUCMPluginResource.FONT_DIALOG);
		label.getTextView().setDefaultTextColor(TEXT_COLOR);
		DropShadow.createDropShadowOn(this);
	}
	
	public Image getIcon() {
		return label.getIcon();
	}
	
	public void setIcon(Image icon) {
		label.setIcon(icon);
		layout();
		setNeedsRepaint();
	}
	
	public String getText() {
		return label.getTextView().getText().toString();
	}
	
	public void setText(String text) {
		label.getTextView().setText(text);
		layout();
		setNeedsRepaint();
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		g.drawImage(RUCMPluginResource.getImage("button.png"), 0, 0, getWidth(), getHeight());
		final int x = (getWidth() - label.getWidth()) / 2;
		final int y = (getHeight() - label.getHeight()) / 2;
		g.translate(x, y);
		label.paintOnTarget(g);
		g.translate(-x, -y);
		if (pressed) {
			g.setColor(OVERLAY_COLOR);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	@Override
	protected void layoutView() {
		label.setSize(label.getPreferredSize());
		setNeedsRepaint();
	}
	
	@Override
	public int getPreferredWidth() {
		return label.getPreferredWidth() + 20;
	}
	
	@Override
	public int getPreferredHeight() {
		return label.getPreferredHeight() + 6;
	}
	
	@Override
	protected void mousePressed(MouseEvent e) {
		requestKeyboardFocus();
		pressed = true;
		setNeedsRepaint();
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

}
