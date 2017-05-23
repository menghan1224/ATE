package cn.edu.buaa.sei.rucm.diagram.notations;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import ca.carleton.sce.squall.ucmeta.UCDUseCaseNode;
import ca.carleton.sce.squall.ucmeta.UseCase;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.NodeView;
import cn.edu.buaa.sei.rucm.diagram.widgets.DataBinding;
import cn.edu.buaa.sei.rucm.diagram.widgets.LabeledTextBox;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.Animation;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.util.Vector2D;

public class UseCaseView extends NodeView<UCDUseCaseNode> {
	
	private final Ellipse2D.Float ellipse;
	private final LabeledTextBox label;
	private final DataBinding nameBinding;
	
	private float highlight; // the highlight layer alpha value

	public UseCaseView(ManagedObject model) {
		super(model);
		setBackgroundColor(null);
		highlight = 0.0f;
		
		ellipse = new Ellipse2D.Float(0, 0, getWidth() - 1, getHeight() - 1);
		
		label = new LabeledTextBox(LabeledTextBox.DOUBLE_CLICK_TO_EDIT);
		label.setPaintMode(PaintMode.DIRECTLY);
		label.setBackgroundColor(null);
		label.setDefaultTextColor(RUCMPluginResource.USECASE_LABEL_TEXT_COLOR);
		addSubview(label);
		layout();
		
		nameBinding = new DataBinding(label, LabeledTextBox.TEXT_CHANGED, getModel(), UCDUseCaseNode.KEY_USECASE, UseCase.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				String name = UseCaseView.this.label.getPlainText();
				if (name.isEmpty()) {
					UseCaseView.this.label.setText("(Untitled)");
				}
				UseCase usecase = UseCaseView.this.getModel().getUseCase();
				if (usecase != null) {
					usecase.setName(name);
				}
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				if (value == null || value.stringValue().isEmpty()) {
					UseCaseView.this.label.setText("(Untitled)");
				} else {
					UseCaseView.this.label.setText(value.stringValue());
				}
			}
			
		};
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		GradientPaint gradient = null;
		if (highlight < 1.0f) {
			gradient = new GradientPaint(new Point2D.Float(0.0f, 0.0f),
														RUCMPluginResource.USECASE_BG_COLOR1,
														new Point2D.Float(0.0f, getHeight() - 1),
														RUCMPluginResource.USECASE_BG_COLOR2);
			g.setPaint(gradient);
			g.fill(ellipse);
			g.setColor(RUCMPluginResource.USECASE_STROKE_COLOR);
			g.draw(ellipse);
		}
		if (highlight > 0.0f) {
			gradient = new GradientPaint(new Point2D.Float(0.0f, 0.0f),
										RUCMPluginResource.USECASE_BG_COLOR1_H,
										new Point2D.Float(0.0f, getHeight() - 1),
										RUCMPluginResource.USECASE_BG_COLOR2_H);
			Composite c = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, highlight));
			g.setPaint(gradient);
			g.fill(ellipse);
			g.setColor(RUCMPluginResource.USECASE_STROKE_COLOR_H);
			g.draw(ellipse);
			g.setComposite(c);
		}
	}
	
	@Override
	protected void mouseEntered() {
		new Animation(0.18f, this) {
			@Override
			protected void animate(float progress) {
				highlight = progress;
				setNeedsRepaint();
			}
		}.commit();
	}
	
	@Override
	protected void mouseExited() {
		new Animation(0.18f, this) {
			@Override
			protected void animate(float progress) {
				highlight = 1.0f - progress;
				setNeedsRepaint();
			}
		}.commit();
	}
	
	@Override
	protected Vector2D normalizeSize(Vector2D size) {
		if (size.y < 40) size.y = 40;
		if (size.x < 100) size.x = 100;
		return super.normalizeSize(size);
	}
	
	@Override
	public boolean isInside(Vector2D point) {
		return ellipse.contains(point.toPoint2D());
	}
	
	@Override
	protected void layoutView() {
		if (ellipse != null) {
			ellipse.width = getWidth() - 1;
			ellipse.height = getHeight() - 1;
		}
		if (label != null) {
			View.scaleViewWithLeftAndRight(label, 10, 10);
			label.setHeight(label.getPreferredHeight());
			View.putViewWithVerticalCenter(label, getHeight() / 2);
		}
	}
	
	@Override
	public Vector2D getLinkPoint(Vector2D dst) {
		Vector2D src = Vector2D.make();
		final double theta = Math.atan2((dst.y - getHeight() / 2) * getWidth(), (dst.x - getWidth() / 2) * getHeight());
		src.x = (int) ((Math.cos(theta) + 1) * (getWidth()) / 2);
		src.y = (int) ((Math.sin(theta) + 1) * (getHeight()) / 2);
		return src;
	}
	
	@Override
	public int getPreferredWidth() {
		return 140;
	}
	
	@Override
	public int getPreferredHeight() {
		return 70;
	}
	
	@Override
	public void dispose() {
		nameBinding.dispose();
		super.dispose();
	}

}
