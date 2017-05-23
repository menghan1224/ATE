package cn.edu.buaa.sei.rucm.diagram.notations;

import ca.carleton.sce.squall.ucmeta.UCDRelationshipLink;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.util.Vector2D;

public class RelationshipLayer extends AbstractLineLayer<UCDRelationshipLink> {

	public RelationshipLayer(ManagedObject model) {
		super(model);
	}

	protected void repaintLine(ViewGraphics g, int length) {
		g.setColor(RUCMPluginResource.ARROW_STROKE_COLOR);
		g.setStroke(RUCMPluginResource.BASIC_STROKE);
		g.drawLine(0, 0, length, 0);
	}
	
	protected void repaintLabelAtPoint(ViewGraphics g, Vector2D point, double theta) {
	}

}
