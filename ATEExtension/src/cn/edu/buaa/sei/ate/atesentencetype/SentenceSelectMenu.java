package cn.edu.buaa.sei.ate.atesentencetype;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;

import org.eclipse.swt.internal.win32.SYSTEMTIME;
import org.eclipse.swt.widgets.Table;

import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.ate.metamodel.AteSentence;
import cn.edu.buaa.sei.ate.metamodel.nullFactory;
import cn.edu.buaa.sei.lmf.ManagedList;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.widgets.MenuItems;
import cn.edu.buaa.sei.rucm.diagram.widgets.PopupMenu;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteView;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public class SentenceSelectMenu extends PopupMenu{
	private TableAteView table;
	public SentenceSelectMenu(final TextCell cell,final ManagedObject model){
		cell.addEventHandler(View.LAYOUT, new EventHandler() {
			@Override
			public void handle(View arg0, Key arg1, Object arg2) {
				// TODO Auto-generated method stub
				if(table!=null){
					table.setWidth(cell.getWidth()-50);
				    table.setHeight(cell.getHeight());
					table.layout();
				}
				
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("current.png"),"电流类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
					AteSentence sentence=nullFactory.createAteSentence();
					sentence.setDescription("CurrentSentence");
					model.set("containate",sentence);
					table=new TableAteView(7);
					table.addRow(new CurrentSentence(table,sentence));
					cell.addSubview(table);
					table.setWidth(cell.getWidth()-50);
					table.setHeight(cell.getHeight());
					//table.layout();
					cell.layout();
					dismiss();
					
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("volt.png"),"电压类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("VoltSentence");
				model.set("containate",sentence);
				table=new TableAteView(7);
				table.addRow(new VoltSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
				
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("resis.png"),"电阻类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("ResistenceSentence");
				model.set("containate",sentence);
				table=new TableAteView(7);
				table.addRow(new ResistenceSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("angle.png"),"角度信号类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("AngleSignalSentence");
				model.set("containate",sentence);
				table=new TableAteView(7);
				table.addRow(new AngleSignalSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("frequency.png"),"频率信号类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("FrequentSignalSentence");
				model.set("containate",sentence);
				table=new TableAteView(3);
				table.addRow(new FrequentSignalSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("pulse.png"),"脉冲信号类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("PulseSignalSentence");
				model.set("containate",sentence);
				table=new TableAteView(7);
				table.addRow(new PulseSignalSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("bool.png"),"布尔信号类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("BoolSignalSentence");
				model.set("containate",sentence);
				table=new TableAteView(7);
				table.addRow(new BoolSignalSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
				
			}
		});
		addSubview(new MenuItems.Separator());
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("monitor.png"),"监控类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("MonitorSentence");
				model.set("containate",sentence);
				table=new TableAteView(7);
				table.addRow(new MonitorSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.Separator());
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("waiting.png"),"等待类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("WaitSentence");
				model.set("containate",sentence);
				table=new TableAteView(3);
				table.addRow(new WaitSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("pause.png"),"暂停类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("PauseSentence");
				model.set("containate",sentence);
				table=new TableAteView(2);
				table.addRow(new PauseSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.Separator());
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("validate.png"),"确认类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("ValidateSentence");
				model.set("containate",sentence);
				table=new TableAteView(3);
				table.addRow(new ValidateSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("validate.png"),"时间限定确认类型句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("ValidateTimeSentence");
				model.set("containate",sentence);
				table=new TableAteView(5);
				table.addRow(new ValidateTimeSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				//table.layout();
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.Separator());
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("cycle.png"),"循环执行固定次数句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("LoopTimeSentence");
				model.set("containate",sentence);
				table=new TableAteView(4);
				table.addRow(new LoopTImeLoopSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("cycle.png"),"循环执行固定条件句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("LoopConditionSentence");
				model.set("containate",sentence);
				table=new TableAteView(4);
				table.addRow(new LoopConditionSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
				dismiss();
			}
		});
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("cycle.png"),"循环执行渐变信号句式") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				AteSentence sentence=nullFactory.createAteSentence();
				sentence.setDescription("LoopLineSentence");
				model.set("containate",sentence);
				table=new TableAteView(10);
				table.addRow(new LoopLineSentence(table,sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
				dismiss();
			
			}
		});
//		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("delete_obj.gif"), "Delete Sentence") {
//			@Override
//			protected void mouseClicked(MouseEvent e) {
//				if(cell.getSubviews().length==6) {
//					for(View view:cell.getSubviews()){
//						if(view instanceof TableAteView){
//							view.removeFromSuperView();
//							model.get("containate").set("description","");
//							dismiss();
//							break;
//						}
//					}
//				}
//			}
//		});
//		addSubview(new MenuItems.LabelItem("测试分组句式") {
//			@Override
//			protected void mouseClicked(MouseEvent e) {
//				
//			}
//		});
	}
	public TableAteView getTable(){
		return table;
	}
	private void recoverFocus(final ViewContext context) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				context.getSwingContainer().requestFocus();
			}
		});
	}

}
