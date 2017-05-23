package cn.edu.buaa.sei.rucm.diagram.notations;

import ca.carleton.sce.squall.ucmeta.Package;
import ca.carleton.sce.squall.ucmeta.UCDPackage;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.ContainerView;
import cn.edu.buaa.sei.rucm.diagram.widgets.DataBinding;
import cn.edu.buaa.sei.rucm.diagram.widgets.LabeledTextBox;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.spec.widgets.DropShadow;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Vector2D;

public class PackageView extends ContainerView<UCDPackage> {
	
	private final LabeledTextBox label;
	private final DataBinding nameBinding;
	private final View content;

	public PackageView(ManagedObject model) {
		super((UCDPackage) model, new View() {
			private final View bgview = new View();
			{
				setPaintMode(PaintMode.DISABLED);
				setBackgroundColor(null);
				setClipped(true);
				addSubview(bgview);
				bgview.setPaintMode(PaintMode.DIRECTLY);
				bgview.setBackgroundColor(RUCMPluginResource.PACKAGE_BG_COLOR);
//				bgview.setAlpha(0.5f);
				layout();
			}
			@Override
			protected void layoutView() {
				View.scaleViewWithMarginToSuperView(bgview, 0);
			}
		});
		addSubview(content = new View() {
			{
				setPaintMode(PaintMode.DISABLED);
				DropShadow.createDropShadowOn(this);
				addSubview(getContentView());
			}
			@Override
			protected void layoutView() {
				if (getContentView().getSuperView() != null) {
					View.scaleViewWithMarginToSuperView(getContentView(), 0);
				}
			}
		});
		
		setBackgroundColor(null);
		setPaintMode(PaintMode.DIRECTLY);
		
		label = new LabeledTextBox(LabeledTextBox.DOUBLE_CLICK_TO_EDIT);
		label.setBackgroundColor(RUCMPluginResource.PACKAGE_BG_COLOR);
		label.setDefaultTextColor(RUCMPluginResource.PACKAGE_TEXT_COLOR);
		label.addEventHandler(LabeledTextBox.FINISH_EDITING, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				PackageView.this.layout();
			}
		});
		addSubview(label);
		
		nameBinding = new DataBinding(label, LabeledTextBox.TEXT_CHANGED, getModel(), UCDPackage.KEY_PACKAGE, Package.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				String name = PackageView.this.label.getPlainText();
				if (name.isEmpty()) {
					PackageView.this.label.setText("(Untitled)");
				}
				Package packa9e = PackageView.this.getModel().getPackage();
				if (packa9e != null) {
					packa9e.setName(name);
				}
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				if (value == null || value.stringValue().isEmpty()) {
					PackageView.this.label.setText("(Untitled)");
				} else {
					PackageView.this.label.setText(value.stringValue());
				}
			}
			
		};
		
		layout();
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		g.setColor(RUCMPluginResource.PACKAGE_BORDER_COLOR);
		g.drawRect(0, 0, label.getWidth() + 1, label.getHeight() + 1);
		g.drawRect(0, 1 + label.getHeight(), getWidth() - 1, getHeight() - label.getHeight() - 2);
	}

	@Override
	protected void selectionStateChanged(boolean selected) {
	}
	
	@Override
	protected void layoutView() {
		if (getContentView() != null) {
			Vector2D labelSize = label.getPreferredSize();
			if (labelSize.x < 50) labelSize.x = 50;
			label.setPosition(1, 1);
			label.setSize(labelSize);
			
			View.scaleViewWithLeftAndRight(content, 1, 1);
			View.scaleViewWithTopAndBottom(content, 2 + labelSize.y, 1);
		}
		setNeedsRepaint();
	}
	
	@Override
	public int getPreferredWidth() {
		return 100;
	}
	
	@Override
	public int getPreferredHeight() {
		return 100;
	}
	
	@Override
	protected Vector2D normalizeSize(Vector2D size) {
		if (size.x < 100) size.x = 100;
		if (size.y < 100) size.y = 100;
		return size;
	}
	
	@Override
	public void dispose() {
		nameBinding.dispose();
		super.dispose();
	}
	
}
