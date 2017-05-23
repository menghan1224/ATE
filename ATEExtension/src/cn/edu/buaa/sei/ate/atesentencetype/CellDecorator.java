package cn.edu.buaa.sei.ate.atesentencetype;

import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;

import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.SentenceImpl;
import cn.edu.buaa.sei.ate.metamodel.AteSentence;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.widgets.PopupMenu;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.spec.StepTable;
import cn.edu.buaa.sei.rucm.spec.TextCellDecorator;
import cn.edu.buaa.sei.rucm.spec.widgets.Button;
import cn.edu.buaa.sei.rucm.spec.widgets.DropDownButton;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteView;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import co.gongzh.snail.Image;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Insets;
import co.gongzh.snail.util.Vector2D;

public class CellDecorator implements TextCellDecorator {
	final DropDownButton dropDownButton=new DropDownButton();
	final Button button=new Button();
	TableAteView table;
	public CellDecorator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(final TextCell cell, final ManagedObject model) {
		AteSentence sentence=(AteSentence)(model.get("containate"));
		if(sentence!=null){
			if(sentence.getDescription().equals("CurrentSentence"))
			{
				table=new TableAteView(7);
				table.addRow(new CurrentSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("VoltSentence")){
				table=new TableAteView(7);
				table.addRow(new VoltSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("ResistenceSentence")){
				table=new TableAteView(7);
				table.addRow(new ResistenceSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("AngleSignalSentence")){
				table=new TableAteView(7);
				table.addRow(new AngleSignalSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("PulseSignalSentence")){
				table=new TableAteView(7);
				table.addRow(new PulseSignalSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("BoolSignalSentence")){
				table=new TableAteView(7);
				table.addRow(new BoolSignalSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("FrequentSignalSentence")){
				table=new TableAteView(3);
				table.addRow(new FrequentSignalSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("MonitorSentence")){
				table=new TableAteView(7);
				table.addRow(new MonitorSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("ValidateSentence")){
				table=new TableAteView(3);
				table.addRow(new ValidateSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("ValidateTimeSentence")){
				table=new TableAteView(5);
				table.addRow(new ValidateTimeSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("LoopTimeSentence")){
				table=new TableAteView(4);
				table.addRow(new LoopTImeLoopSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("LoopConditionSentence")){
				table=new TableAteView(4);
				table.addRow(new LoopConditionSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("LoopLineSentence")){
				table=new TableAteView(10);
				table.addRow(new LoopLineSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("WaitSentence")){
				table=new TableAteView(3);
				table.addRow(new WaitSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
			if(sentence.getDescription().equals("PauseSentence")){
				table=new TableAteView(2);
				table.addRow(new PauseSentence(table, sentence));
				cell.addSubview(table);
				table.setWidth(cell.getWidth()-50);
				table.setHeight(cell.getHeight());
				cell.layout();
			}
		}
			
		dropDownButton.addEventHandler(View.MOUSE_CLICKED, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				PopupMenu menu = new SentenceSelectMenu(cell,model);
				Vector2D location = dropDownButton.getPositionInRootView();
				location.y += dropDownButton.getHeight();
				menu.popup(cell.getViewContext(), location);
			}
		});
		button.addEventHandler(View.MOUSE_CLICKED, new EventHandler() {
			
			@Override
			public void handle(View arg0, Key arg1, Object arg2) {
				// TODO Auto-generated method stub
				for(View view:cell.getSubviews()){
					if(view instanceof TableAteView){
						view.removeFromSuperView();
						model.get("containate").set("description","");
						break;
					}
				}
			}
		});
		cell.addSubview(dropDownButton);
		cell.addSubview(button);
		cell.addEventHandler(View.LAYOUT, new EventHandler() {
			@Override
			public void handle(View arg0, Key arg1, Object arg2) {
				// TODO Auto-generated method stub
				dropDownButton.setIcon(RUCMPluginResource.getImage("add_obj.gif"));
				dropDownButton.setSize(30,20);
				dropDownButton.setPosition(cell.getWidth()-40,0);
				dropDownButton.layout();
				button.setSize(20,20);
				button.setPosition(cell.getWidth()-20,0);
				button.setIcon(RUCMPluginResource.getImage("delete_obj.gif"));
				button.layout();
				if(table!=null){
					table.setWidth(cell.getWidth()-50);
				    table.setHeight(cell.getHeight());
					table.layout();
				}
				
			}
		});
		
	}

	@Override
	public void textChanged(String text) {
		// TODO Auto-generated method stub
	

	}
	

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
