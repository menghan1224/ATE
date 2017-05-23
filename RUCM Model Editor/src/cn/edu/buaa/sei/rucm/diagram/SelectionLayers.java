package cn.edu.buaa.sei.rucm.diagram;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;

import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.layer.Layer;
import co.gongzh.snail.util.Vector2D;

class SelectionLayers {
	
	static class ResizableBorder extends SimpleBorder {
		
		private final int radius = 3;
		private final int logical_radius = 5;
		
		private static enum Corner {
			NONE, LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
		}

		ResizableBorder(NodeView<?> nodeView) {
			super(nodeView);
		}

		@Override
		protected void repaintLayer(ViewGraphics g) {
			super.repaintLayer(g);
			Vector2D pos = nodeView.getPositionInDiagramView();
			Vector2D size = nodeView.getSize();
			
			g.translate(pos.x, pos.y);
			
			Rectangle r = new Rectangle();
			r.x = -radius; r.y = -radius;
			r.width = 2 * radius - 1; r.height = 2 * radius - 1;
			drawCornerRect(g, r);
			r.x = size.x - radius;
			drawCornerRect(g, r);
			r.y = size.y - radius;
			drawCornerRect(g, r);
			r.x = -radius;
			drawCornerRect(g, r);
			
			g.translate(-pos.x, -pos.y);
		}
		
		private void drawCornerRect(ViewGraphics g, Rectangle r) {
			g.setColor(Color.WHITE);
			g.fill(r);
			g.setColor(RUCMPluginResource.SELECTION_BORDER_COLOR);
			g.draw(r);
		}
		
		private Corner getCornerAtPoint(Vector2D point) {
			point = point.clone();
			Vector2D size = nodeView.getSize();
			point.decrease(nodeView.getPositionInDiagramView());
			Rectangle r = new Rectangle();
			r.x = -logical_radius; r.y = -logical_radius;
			r.width = 2 * logical_radius - 1; r.height = 2 * logical_radius - 1;
			if (r.contains(point.toPoint())) return Corner.LEFT_TOP;
			r.x = size.x - logical_radius;
			if (r.contains(point.toPoint())) return Corner.RIGHT_TOP;
			r.y = size.y - logical_radius;
			if (r.contains(point.toPoint())) return Corner.RIGHT_BOTTOM;
			r.x = -logical_radius;
			if (r.contains(point.toPoint())) return Corner.LEFT_BOTTOM;
			return Corner.NONE;
		}

		@Override
		public boolean isInside(Vector2D point) {
			return getCornerAtPoint(point) != Corner.NONE;
		}
		
		@Override
		protected void mouseMoved(MouseEvent e, Vector2D location) {
			updateCursor(location);
		}
		
		private Vector2D p0;
		private Corner corner;
		private Vector2D node_pos0, node_size0;
		
		@Override
		protected void mousePressed(MouseEvent e, Vector2D location) {
			p0 = location.clone();
			corner = getCornerAtPoint(location);
			node_pos0 = nodeView.getPosition();
			node_size0 = nodeView.getSize();
		}
		
		// FIXME: the resizing behavior is not correct when NodeView.normalizeSize is applied
		@Override
		protected void mouseDragged(MouseEvent e, Vector2D location) {
			Vector2D delta = Vector2D.subtract(location, p0);
			switch (corner) {
			case LEFT_TOP:
				{
					Vector2D pos = Vector2D.add(node_pos0, delta);
					Vector2D size = nodeView.normalizeSize(Vector2D.subtract(node_size0, delta));
					nodeView.getModel().setLeft(pos.x);
					nodeView.getModel().setTop(pos.y);
					nodeView.getModel().setWidth(size.x);
					nodeView.getModel().setHeight(size.y);
				}
				break;
			case LEFT_BOTTOM:
				{
					Vector2D pos = Vector2D.make(node_pos0.x + delta.x, node_pos0.y);
					Vector2D size = nodeView.normalizeSize(Vector2D.make(node_size0.x - delta.x, node_size0.y + delta.y));
					nodeView.getModel().setLeft(pos.x);
					nodeView.getModel().setTop(pos.y);
					nodeView.getModel().setWidth(size.x);
					nodeView.getModel().setHeight(size.y);
				}
				break;
			case RIGHT_TOP:
				{
					Vector2D pos = Vector2D.make(node_pos0.x, node_pos0.y + delta.y);
					Vector2D size = nodeView.normalizeSize(Vector2D.make(node_size0.x + delta.x, node_size0.y - delta.y));
					nodeView.getModel().setLeft(pos.x);
					nodeView.getModel().setTop(pos.y);
					nodeView.getModel().setWidth(size.x);
					nodeView.getModel().setHeight(size.y);
				}
				break;
			case RIGHT_BOTTOM:
				{
					Vector2D size = nodeView.normalizeSize(Vector2D.add(node_size0, delta));
					nodeView.getModel().setWidth(size.x);
					nodeView.getModel().setHeight(size.y);
				}
				break;
			default:
				break;
			}
		}
		
		private void updateCursor(Vector2D location) {
			Cursor cursor;
			switch (getCornerAtPoint(location)) {
			case LEFT_TOP:
				cursor = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);
				break;
			case LEFT_BOTTOM:
				cursor = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);
				break;
			case RIGHT_TOP:
				cursor = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
				break;
			case RIGHT_BOTTOM:
				cursor = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
				break;
			default:
				cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
				break;
			}
			getSuperView().getViewContext().getSwingContainer().setCursor(cursor);
		}
		
		@Override
		protected void mouseExited() {
			Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
			getSuperView().getViewContext().getSwingContainer().setCursor(cursor);
		}
		
	}

	static class SimpleBorder extends Layer {
		
		final NodeView<?> nodeView;
		
		SimpleBorder(NodeView<?> nodeView) {
			this.nodeView = nodeView;
		}

		@Override
		protected void repaintLayer(ViewGraphics g) {
			g.setStroke(RUCMPluginResource.BASIC_STROKE);
			g.setColor(RUCMPluginResource.SELECTION_BORDER_COLOR);
			Vector2D pos = nodeView.getPositionInDiagramView();
			Vector2D size = nodeView.getSize();
			g.drawRect(pos.x, pos.y, size.x - 1, size.y - 1);
		}

		@Override
		public boolean isInside(Vector2D point) {
			return false;
		}
		
	}
	
	static class ContainerContentBorder extends Layer {
		
		final ContainerView<?> view;
		
		ContainerContentBorder(ContainerView<?> view) {
			this.view = view;
		}

		@Override
		protected void repaintLayer(ViewGraphics g) {
			g.setStroke(RUCMPluginResource.THICK_DASH_STROKE);
			g.setColor(RUCMPluginResource.SELECTION_BORDER_COLOR);
			View contentView = view.getContentView();
			Vector2D pos = View.transformPoint(Vector2D.make(), contentView, view.getDiagramView());
			Vector2D size = contentView.getSize();
			g.drawRect(pos.x + 1, pos.y + 1, size.x - 3, size.y - 3);
		}

		@Override
		public boolean isInside(Vector2D point) {
			return false;
		}
		
	}
	
	static class SelectionArea extends Layer {
		
		private final Vector2D p1;
		private final Vector2D p2;
		
		SelectionArea() {
			p1 = Vector2D.make();
			p2 = Vector2D.make();
		}
		
		void setArea(Vector2D p1, Vector2D p2) {
			this.p1.set(p1);
			this.p2.set(p2);
			if (this.p2.x < this.p1.x) {
				int temp = this.p2.x;
				this.p2.x = this.p1.x;
				this.p1.x = temp;
			}
			if (this.p2.y < this.p1.y) {
				int temp = this.p2.y;
				this.p2.y = this.p1.y;
				this.p1.y = temp;
			}
			setNeedsRepaint();
		}
		
		co.gongzh.snail.util.Rectangle getRectangle() {
			return co.gongzh.snail.util.Rectangle.make(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
		}

		@Override
		protected void repaintLayer(ViewGraphics g) {
			g.setStroke(RUCMPluginResource.DASH_STROKE);
			g.setColor(RUCMPluginResource.SELECTION_BORDER_COLOR);
			g.drawRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
		}

		@Override
		public boolean isInside(Vector2D point) {
			return false;
		}
		
	}
	
	static class DashLineLinkLayer extends Layer {
		
		private final Vector2D p1;
		private final Vector2D p2;
		
		DashLineLinkLayer(Vector2D p1) {
			this.p1 = p1.clone();
			p2 = Vector2D.make();
		}
		
		void setP2(Vector2D p2) {
			this.p2.set(p2);
			setNeedsRepaint();
		}

		@Override
		protected void repaintLayer(ViewGraphics g) {
			g.setColor(RUCMPluginResource.SELECTION_BORDER_COLOR);
			g.setStroke(RUCMPluginResource.THICK_DASH_STROKE);
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
			g.fillOval(p1.x - 3, p1.y - 3, 7, 7);
			g.fillOval(p2.x - 3, p2.y - 3, 7, 7);
		}

		@Override
		public boolean isInside(Vector2D point) {
			return false;
		}
		
	}

}
