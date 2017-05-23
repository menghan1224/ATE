package cn.edu.buaa.sei.ate.capbilityvieweditor;

import java.awt.Color;
import java.awt.Container;

import cn.edu.buaa.sei.ate.metamodel.ATECapbility;
import cn.edu.buaa.sei.ate.metamodel.ATECapbilityView;
import cn.edu.buaa.sei.ate.metamodel.nullFactory;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.SnailLMFEditorBase;
import cn.edu.buaa.sei.rucm.SnailLMFEditorBase.SnailEditorView;
import cn.edu.buaa.sei.rucm.spec.SpecificationView;
import cn.edu.buaa.sei.rucm.spec.widgets.DropShadow;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionBar;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionScrollView;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Insets;

public class CapbilityViewEditor extends SnailLMFEditorBase{
	private ViewContext viewContext;
	private FaultEditorView view;
	@Override
	protected ViewContext createPartControl(Container container) {
		// TODO Auto-generated method stub
		ATECapbilityView ateCapbilityView =((CapbilityViewEditorInput) getEditorInput()).getFault();
		view = new FaultEditorView(ateCapbilityView); // 创建GUI
		viewContext = new ViewContext(container, view);
		return viewContext;
	}

	@Override
	public SnailEditorView getSnailView() {
		// TODO Auto-generated method stub
		return view;
	}

}

class FaultEditorView extends SnailEditorView {
	
	private final ATECapbilityView model;
	
	private final SectionScrollView scrollView;
	
	FaultEditorView(final ATECapbilityView model) { // 把当前打开的Fault对象传进来
		this.model = model;
		setBackgroundColor(Color.WHITE);
		setPaintMode(PaintMode.DIRECTLY);
		scrollView = new SectionScrollView();
		addSubview(scrollView);

		// title section
		SectionView titleSection = new SectionView();
		titleSection.setInsets(Insets.make(5, 8, 0, 14));
		SectionBar titleBar = new SectionBar();
		titleBar.setText("测试能力视图");
		titleSection.addSubview(titleBar);
		scrollView.addSection(titleSection);
		
		
		// Basic Section
		NameAndDescriptionSection section = new NameAndDescriptionSection(model); // 这个类的定义在下面
		section.setInsets(Insets.make(10, 12, 20, 18));
		scrollView.addSection(section);
		
		
	}
	
	@Override
	protected void mouseReleased(MouseEvent e) {
		requestKeyboardFocus();
	}
	
	@Override
	protected void preKeyTyped(KeyEvent e) {
		// Ctrl+S的保存功能
		if (e.isMetaOrCtrlDown() && (e.getKeyChar() == 's' || e.getKeyChar() == 'S')) {
			try {
				model.resource().save();
			} catch (LMFResourceException ex) {
				RUCMPlugin.logError(ex, true);
			}
			e.handle();
		}
	}
	
	@Override
	protected void layoutView() {
		View.scaleViewWithMarginToSuperView(scrollView, 0);
	}

	void dispose() {
		scrollView.clearSections();
	}

	@Override
	public View getPrintableContent() {
		return scrollView.getContentView();
	}
	
}





/**
 * 一个表格，有三行：
 * 第一行编辑Actor的名字；
 * 第二行编辑Actor的描述文本；
 * 第三行是一个自由文本框，没有实际用途。
 * 
 * @author wuxue
 *
 */
class NameAndDescriptionSection extends SectionView {
	
	private final ATECapbilityView model;
	private final TableAteView table;
	
	NameAndDescriptionSection(ATECapbilityView model) { // 把当前Actor传进来
		this.model = model;
		
		// 创建一个Table
		table = new TableAteView(7);
		DropShadow.createDropShadowOn(table);
		rebuildRows();
		addSubview(table);
		table.addEventHandler(LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				layoutSection();
			}
		});
		layoutSection();
	}
	
	/**
	 * 构建表格里面的行。
	 */
	private void rebuildRows() {
		table.removeAllRows();
		table.addRow(new CapbilityTitleRow());
		if(model.getCapbilities()==null||model.getCapbilities().size()==0){
			ATECapbility capbility=nullFactory.createATECapbility();
			model.getCapbilities().add(capbility);
			table.addRow(new CapbilityRow(model, capbility, table));
		}else{
			for(ATECapbility cap:model.getCapbilities()){
				table.addRow(new CapbilityRow(model, cap, table));
			}
		}
			
	}
	
	@Override
	protected void dispose() {
		table.removeAllRows();
		super.dispose();
	}
	
}
