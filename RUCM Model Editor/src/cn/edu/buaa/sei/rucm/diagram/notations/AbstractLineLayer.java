package cn.edu.buaa.sei.rucm.diagram.notations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import ca.carleton.sce.squall.ucmeta.UCDLink;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.LinkLayer;
import cn.edu.buaa.sei.rucm.diagram.NodeView;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;

import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.util.Vector2D;

public abstract class AbstractLineLayer<E extends UCDLink> extends LinkLayer<E> {
	
	private Rectangle lineBox;
	private AffineTransform lineBoxTransform;

	public AbstractLineLayer(ManagedObject model) {
		super(model);
	}

	@Override
	protected void selectionStateChanged(boolean selected) {
		setNeedsRepaint();
	}

	@Override
	protected void repaintLayer(ViewGraphics g) {
		final NodeView<?> node1 = getNodeView1();
		final NodeView<?> node2 = getNodeView2();
		if (node1 == null || node2 == null) return;
		final View rootView = node1.getDiagramView();
		
		Vector2D c1 = Vector2D.add(node1.getPositionInDiagramView(), Vector2D.multiplied(node1.getSize(), 0.5));
		Vector2D c2 = Vector2D.add(node2.getPositionInDiagramView(), Vector2D.multiplied(node2.getSize(), 0.5));
		
		Vector2D end1 = node1.getLinkPoint(View.transformPoint(c2, rootView, node1));
		Vector2D end2 = node2.getLinkPoint(View.transformPoint(c1, rootView, node2));
		end1 = View.transformPoint(end1, node1, rootView);
		end2 = View.transformPoint(end2, node2, rootView);
		
		g.translate(end2.x, end2.y);
		end1.decrease(end2);
		final double theta = Math.atan2(end1.y, end1.x);
		final int length = (int) Math.sqrt(end1.x * end1.x + end1.y * end1.y);
		final int lineBoxRadius = 3;
		g.rotate(theta);
		if (isSelected()) {
			g.setStroke(RUCMPluginResource.THICK_STROKE);
			g.setColor(RUCMPluginResource.SELECTION_BORDER_COLOR);
			g.drawLine(0, 0, length, 0);
		}
		repaintLine(g, length);
		this.lineBox = new Rectangle(0, -lineBoxRadius, length, lineBoxRadius * 2);
		this.lineBoxTransform = AffineTransform.getRotateInstance(-theta);
		this.lineBoxTransform.translate(-end2.x, -end2.y);
		g.rotate(-theta);
		repaintLabelAtPoint(g, Vector2D.make(end1.x / 2, end1.y / 2), theta);
		if (isSelected()) {
			g.setStroke(RUCMPluginResource.BASIC_STROKE);
			Dimension size = new Dimension(6, 6);
			drawCornerRect(g, new Rectangle(new Point(-3, -3), size));
			drawCornerRect(g, new Rectangle(new Point(end1.x - 3, end1.y - 3), size));
		}
		g.translate(-end2.x, -end2.y);
	}
	
	private void drawCornerRect(ViewGraphics g, Rectangle r) {
		g.setColor(Color.WHITE);
		g.fill(r);
		g.setColor(RUCMPluginResource.SELECTION_BORDER_COLOR);
		g.draw(r);
	}
	
	protected abstract void repaintLine(ViewGraphics g, int length);
	
	protected void repaintLabelAtPoint(ViewGraphics g, Vector2D point, double theta) {
	}

	@Override
	public boolean isInside(Vector2D point) {
		if (lineBox != null) {
			return lineBox.contains(lineBoxTransform.transform(point.toPoint2D(), null));
		} else {
			return false;
		}
	}

}
