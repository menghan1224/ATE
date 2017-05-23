package cn.edu.buaa.sei.rucm.diagram.notations;

import java.awt.font.TextLayout;

import ca.carleton.sce.squall.ucmeta.Include;
import ca.carleton.sce.squall.ucmeta.UCDIncludeLink;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import ca.carleton.sce.squall.ucmeta.UseCase;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.ContainerView;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.NodeMapping;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.util.Vector2D;

public class IncludeLayer extends AbstractLineLayer<UCDIncludeLink> {

	private TextLayout textLayout;
	
	public IncludeLayer(ManagedObject model) {
		super(model);
		textLayout = null;
	}

	@Override
	protected void repaintLine(ViewGraphics g, int length) {
		g.setColor(RUCMPluginResource.ARROW_STROKE_COLOR);
		g.setStroke(RUCMPluginResource.DASH_STROKE);
		g.drawLine(0, 0, length, 0);
		g.setStroke(RUCMPluginResource.BASIC_STROKE);
		g.drawLine(0, 0, 12, 4);
		g.drawLine(0, 0, 12, -4);
	}
	
	@Override
	protected void repaintLabelAtPoint(ViewGraphics g, Vector2D point, double theta) {
		if (theta < 0.0) theta += Math.PI;
		if (theta >= Math.PI) theta = 0;
		g.setColor(RUCMPluginResource.ARROW_LABEL_COLOR);
		g.setFont(RUCMPluginResource.FONT_DIALOG);
		
		final String label = "include";
		
		if (textLayout == null) {
			textLayout = new TextLayout(label, g.getFont(), g.getFontRenderContext());
		}
		
		int y = (int) (point.y - textLayout.getDescent()) - 2;
		if (theta < Math.PI / 10 || theta > Math.PI * 9 / 10) {
			int tx = (int) (point.x - textLayout.getAdvance() / 2 - 2);
			int ty = (int) (y - textLayout.getDescent());
			g.translate(tx, ty);
			g.drawString(label, 0, 0);
			g.drawImage(RUCMPluginResource.getImage("brackets.png"), -7, 3 - (int) textLayout.getAscent(), (int) textLayout.getAdvance() + 14, 9);
			g.translate(-tx, -ty);
		} else if (theta > Math.PI / 2.0) {
			int tx = (int) (point.x - textLayout.getAdvance() - 2);
			g.translate(tx, y);
			g.drawString(label, 0, 0);
			g.drawImage(RUCMPluginResource.getImage("brackets.png"), -7, 3 - (int) textLayout.getAscent(), (int) textLayout.getAdvance() + 14, 9);
			g.translate(-tx, -y);
		} else {
			g.drawString(label, point.x + 2, y);
			g.drawImage(RUCMPluginResource.getImage("brackets.png"), point.x + 2 - 7, y + 3 - (int) textLayout.getAscent(), (int) textLayout.getAdvance() + 14, 9);
		}
	}
	
	@Override
	public void addElementToContainer(ContainerView<?> container, UCDNode node1, UCDNode node2, ManagedObject element) {
		Include include = (Include) element;
		NodeMapping nm1 = NotationRegistry.getNodeMapping(node1.type());
		ManagedObject value1 = node1.get(nm1.getTargetAttribute().getName());
		if (value1 instanceof UseCase) {
			((UseCase) value1).getInclude().add(include);
		}
	}

}
