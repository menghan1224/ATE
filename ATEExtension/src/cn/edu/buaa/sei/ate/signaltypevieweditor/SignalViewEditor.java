package cn.edu.buaa.sei.ate.signaltypevieweditor;

import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;

import org.eclipse.ui.internal.layout.Row;

import ca.carleton.sce.squall.ucmeta.AlternativeFlow;
import ca.carleton.sce.squall.ucmeta.FlowOfEvents;
import ca.carleton.sce.squall.ucmeta.PostCondition;
import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.UCMetaFactory;
import ca.carleton.sce.squall.ucmeta.UCMetaTemplateFactory;
import cn.edu.buaa.sei.ate.metamodel.ATESignalTypeView;
import cn.edu.buaa.sei.ate.metamodel.Signal;
import cn.edu.buaa.sei.ate.metamodel.nullFactory;
import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.AttributeSetter;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ListObserver;
import cn.edu.buaa.sei.lmf.ManagedList;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Observer;
import cn.edu.buaa.sei.lmf.OwnerObserver;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.SnailLMFEditorBase;
import cn.edu.buaa.sei.rucm.SnailLMFEditorBase.SnailEditorView;
import cn.edu.buaa.sei.rucm.spec.FlowTable;
import cn.edu.buaa.sei.rucm.spec.SpecificationView;
import cn.edu.buaa.sei.rucm.spec.StepTable;
import cn.edu.buaa.sei.rucm.spec.widgets.DropShadow;
import cn.edu.buaa.sei.rucm.spec.widgets.EmbeddedRow;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.RowLabel;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionBar;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionScrollView;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionView;
import cn.edu.buaa.sei.rucm.spec.widgets.SingleColumnRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import cn.edu.buaa.sei.rucm.spec.widgets.TextTableViewRow;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Insets;

public class SignalViewEditor extends SnailLMFEditorBase{
	private ViewContext viewContext;
	private FaultEditorView view;
	@Override
	protected ViewContext createPartControl(Container container) {
		// TODO Auto-generated method stub
		ATESignalTypeView ateSignalTypeView =((SignalViewEditorInput) getEditorInput()).getFault();
		view = new FaultEditorView(ateSignalTypeView); // 创建GUI
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
	
	private final ATESignalTypeView model;
	
	private final SectionScrollView scrollView;
	
	FaultEditorView(final ATESignalTypeView model) { 
		this.model = model;
		setBackgroundColor(Color.WHITE);
		setPaintMode(PaintMode.DIRECTLY);
		scrollView = new SectionScrollView();
		addSubview(scrollView);

		// title section
		SectionView titleSection = new SectionView();
		titleSection.setInsets(Insets.make(5, 8, 0, 14));
		SectionBar titleBar = new SectionBar();
		titleBar.setText("信号类型视图");
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

class TitleRow implements TableAteViewRow {
	private  RowLabel label1;
	private  RowLabel label2;
	private RowLabel label3;
	private RowLabel label4;
	{
		label1=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("信号类型 ");
			}
		};
		 label2=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("信号名称");
			}
		};
		 label3=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("参数");
			}
		};
		 label4=new RowLabel(){
				{
				setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
				setInsets(Insets.make(3, 6, 3, 6));
				setIconSpacing(0);
//				getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
				getTextView().setDefaultTextColor(Color.black);
				getTextView().setText("通道数");
				}
			};
	}
	@Override
	public void tableViewRowAdded(TableAteView table) {
		// TODO Auto-generated method stub
		System.out.println("added");
	}

	@Override
	public void tableViewRowRemoved(TableAteView table) {
		// TODO Auto-generated method stub
		System.out.println("removed");
	}

	@Override
	public List<View> getView() {
		// TODO Auto-generated method stub
		List<View> list=new ArrayList<View>();
		list.add(label1);
		list.add(label2);
		list.add(label3);
		list.add(label4);
		return list;
	}

	@Override
	public List<TextCell> getTextCellList() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}




class NameAndDescriptionSection extends SectionView {
	
	private final ATESignalTypeView model;
	private final TableAteView table;
	
	NameAndDescriptionSection(ATESignalTypeView model) { // 把当前Actor传进来
		this.model = model;
		
		// 创建一个Table
//		table = new TableView() {
//			@Override
//			protected int getMinimumKeyColumnWidth() {
//				return SpecificationView.KEY_COL_PREFERRED_WIDTH;
//			}
//		};
		table=new TableAteView(4);
//		table=new StepTable(UCMetaTemplateFactory.createBasicFlow());
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
		table.addRow(new TitleRow());
		if(model.getSignallist()==null||model.getSignallist().size()==0){
			Signal sig=nullFactory.createSignal();
			model.getSignallist().add(sig);
			table.addRow(new SignalRow(model, sig, table));
		}
		else{
			for(Signal signal:model.getSignallist()){
				table.addRow(new SignalRow(model, signal, table));
			}
		}
//		List<Signal> list=model.getSignallist();
//		if(list.size()==0)
//			table.addRow(new SignalDescriptionRow(nullFactory.createSignal(),model,table));
//		for(Signal signal:list){
//			table.addRow(new SignalDescriptionRow(signal, model, table));
//		}
		

//		

						
			
	}
	
	@Override
	protected void dispose() {
		table.removeAllRows();
		super.dispose();
	}
	
}