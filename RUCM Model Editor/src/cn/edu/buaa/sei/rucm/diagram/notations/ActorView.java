package cn.edu.buaa.sei.rucm.diagram.notations;

import ca.carleton.sce.squall.ucmeta.Actor;
import ca.carleton.sce.squall.ucmeta.UCDActorNode;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.NodeView;
import cn.edu.buaa.sei.rucm.diagram.widgets.DataBinding;
import cn.edu.buaa.sei.rucm.diagram.widgets.LabeledTextBox;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.Image;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.util.Vector2D;

public class ActorView extends NodeView<UCDActorNode> {

	private final LabeledTextBox label;
	private final DataBinding nameBinding;
	
	public ActorView(ManagedObject model) {
		super(model);
		setBackgroundColor(null);
		label = new LabeledTextBox(LabeledTextBox.DOUBLE_CLICK_TO_EDIT);
		label.setBackgroundColor(null);
		label.setPaintMode(PaintMode.DIRECTLY);
		label.setDefaultTextColor(RUCMPluginResource.ACTOR_LABEL_TEXT_COLOR);
		addSubview(label);
		layout();
		
		nameBinding = new DataBinding(label, LabeledTextBox.TEXT_CHANGED, getModel(), UCDActorNode.KEY_ACTOR, Actor.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				String name = ActorView.this.label.getPlainText();
				if (name.isEmpty()) {
					ActorView.this.label.setText("(Untitled)");
				}
				Actor actor = ActorView.this.getModel().getActor();
				if (actor != null) {
					actor.setName(name);
				}
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				if (value == null || value.stringValue().isEmpty()) {
					ActorView.this.label.setText("(Untitled)");
				} else {
					ActorView.this.label.setText(value.stringValue());
				}
			}
			
		};
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		Image human = RUCMPluginResource.getImage("human_actor.png");
		g.drawImage(human, (getWidth() - human.width) / 2, 5);
	}
	
	@Override
	protected Vector2D normalizeSize(Vector2D size) {
		if (size.x < 60) size.x = 60;
		size.y = 70 + label.getPreferredHeight();
		return super.normalizeSize(size);
	}
	
	@Override
	public int getPreferredHeight() {
		return 70 + label.getPreferredHeight();
	}
	
	@Override
	public int getPreferredWidth() {
		return label.getPreferredWidth();
	}
	
	@Override
	public Vector2D getLinkPoint(Vector2D dst) {
		final int human_height = 63;
		final int human_width = 36;
		Vector2D p = super.getLinkPoint(dst);
		if (p.y >= human_height + 10) {
			return super.getLinkPoint(dst);
		} else {
			Vector2D src = Vector2D.make();
			dst = dst.clone();
			dst.x -= getWidth() / 2;
			dst.y -= human_height / 2;
			
			if (dst.x == 0) {
				if (0 >= dst.y) src.y -= 5 + human_height / 2;
				else src.y += human_height / 2;
			} else {
				final float k = dst.y / (float) dst.x;
				final float k0 = (human_height - 10) / (float) human_width;

				if (Math.abs(k) < k0) {
					if (0 >= dst.x) src.x -= 10 + human_width / 2;
					else src.x += 10 + human_width / 2;
					src.y = (int) (k * src.x);
				} else {
					if (0 >= dst.y) src.y -= 5 + human_height / 2;
					else src.y += human_height / 2;
					src.x = (int) (src.y / k);
				}
			}
			
			src.x += getWidth() / 2;
			src.y += 5 + human_height / 2;
			return src;
		}
	}
	
	@Override
	protected void layoutView() {
		if (label != null) {
			View.scaleViewWithLeftAndRight(label, 0, 0);
			label.setHeight(label.getPreferredHeight());
			View.putViewWithBottom(label, 0);
		}
	}
	
	@Override
	public void dispose() {
		nameBinding.dispose();
		super.dispose();
	}

}
