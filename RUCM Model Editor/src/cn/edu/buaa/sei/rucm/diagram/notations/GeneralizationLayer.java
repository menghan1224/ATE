package cn.edu.buaa.sei.rucm.diagram.notations;

import java.awt.Color;
import java.awt.Polygon;

import ca.carleton.sce.squall.ucmeta.UCDGeneralizationLink;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.util.Vector2D;

public class GeneralizationLayer extends AbstractLineLayer<UCDGeneralizationLink> {

	private static final Polygon ARROW_SHAPE;
	
	static {
		int xs[] = {0, 13, 13};
		int ys[] = {0, 6, -6};
		ARROW_SHAPE = new Polygon(xs, ys, 3);
	}
	
	public GeneralizationLayer(ManagedObject model) {
		super(model);
	}

	protected void repaintLine(ViewGraphics g, int length) {
		g.setColor(RUCMPluginResource.ARROW_STROKE_COLOR);
		g.setStroke(RUCMPluginResource.BASIC_STROKE);
		g.drawLine(0, 0, length, 0);
		g.setColor(Color.WHITE);
		g.fill(ARROW_SHAPE);
		g.setColor(RUCMPluginResource.ARROW_STROKE_COLOR);
		g.draw(ARROW_SHAPE);
	}
	
	protected void repaintLabelAtPoint(ViewGraphics g, Vector2D point, double theta) {
	}
	
}
