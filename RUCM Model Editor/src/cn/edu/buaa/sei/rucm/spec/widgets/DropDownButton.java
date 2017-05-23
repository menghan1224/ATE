package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Color;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;

import co.gongzh.snail.Image;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.util.Insets;

public class DropDownButton extends View {
	
	private final Label label;
	
	private static final float NORMAL_ALPHA = 0.5f;
	private static final float HIGHLIGHT_ALPHA = 0.8f;
	
	public DropDownButton() {
		setBackgroundColor(null);
		setPaintMode(PaintMode.DIRECTLY);
		setAlpha(NORMAL_ALPHA);
		label = new Label();
		label.setBackgroundColor(null);
		label.setPaintMode(PaintMode.DIRECTLY);
		label.setIconSpacing(3);
		label.setInsets(Insets.make(3, 3, 3, 3));
		label.getTextView().setDefaultFont(RUCMPluginResource.FONT_DIALOG);
		label.getTextView().setDefaultTextColor(Color.BLACK);
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
		final int width = label.getWidth() + 9 + 3;
		final int x = (getWidth() - width) / 2;
		final int y = (getHeight() - label.getHeight()) / 2;
		g.translate(x, y);
		label.paintOnTarget(g);
		g.translate(-x, -y);
		g.drawImage(RUCMPluginResource.getImage("triangle.png"), x + label.getWidth(), (getHeight() - 8) / 2 + 1);
	}
	
	@Override
	protected void layoutView() {
		label.setSize(label.getPreferredSize());
		setNeedsRepaint();
	}
	
	@Override
	public int getPreferredWidth() {
		return label.getPreferredWidth() + 9 + 3;
	}
	
	@Override
	public int getPreferredHeight() {
		return label.getPreferredHeight();
	}
	
	@Override
	protected void mousePressed(MouseEvent e) {
		requestKeyboardFocus();
		setAlpha(HIGHLIGHT_ALPHA);
		e.handle();
	}
	
	@Override
	protected void mouseReleased(MouseEvent e) {
		setAlpha(NORMAL_ALPHA);
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
