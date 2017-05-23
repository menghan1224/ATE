package cn.edu.buaa.sei.ate.atesentencetype;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import cn.edu.buaa.sei.ate.metamodel.AteSentence;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.widgets.DataBinding;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.spec.StepTable;
import cn.edu.buaa.sei.rucm.spec.rows.StepRow;
import cn.edu.buaa.sei.rucm.spec.widgets.RowLabel;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCellDelegate;
import cn.edu.buaa.sei.rucm.spec.widgets.TextTableViewRow;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Insets;

public class LoopLineSentence implements TableAteViewRow{
	
	private TextCell startCell;
	private TextCell timeCell;
	private TextCell increCell;
	private TextCell endCell;
	private TextCell signalCell;
	private AteSentence sentence;
	private TextCellDelegate defaultDelegate;
	List<View> list=new ArrayList<View>();
	private DataBinding startBinding;
	private DataBinding timeBinding;
	private DataBinding increBinding;
	private DataBinding endBinding;
	private DataBinding signalBinding;
	
	private RowLabel label1;
	private RowLabel label2;
	private RowLabel label3;
	private RowLabel label4;
	private RowLabel label5;
	List<TextCell> cellList=new ArrayList<TextCell>();
	{
		label1=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(3);
			setIcon(RUCMPluginResource.getImage("cycle.png"));
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.RED);
			getTextView().setText("以");
			}
		};
		label2=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("为起点,每");
			}
		};
		
		label3=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("增加");
			}
		};
		label4=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("到");
			}
		};
		label5=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("结束");
			}
		};
		
	}
	public int getNumOfColumns(){
		return 10;
	}

	public LoopLineSentence(final TableAteView table,AteSentence sentence) {
			this.sentence=sentence;
			 defaultDelegate = new TextCellDelegate() {
				@Override public void cellTabPressed(TextCell cell, boolean shift) {}
				@Override public void cellUpPressed(TextCell cell, int offset) {
					StepRow row=(StepRow)(((TextCell)(table.getSuperView())).getOwnerRow());
					if(row.getStepNumber()>1){
						StepTable stepTable=(StepTable)(table.getSuperView().getSuperView());
						int rowIndex = stepTable.indexOfRow(row);
						View[] views=stepTable.getRow(rowIndex-1).getValueColumnView().getSubviews();
						boolean isNormal=true;
						for(View view:views){
							if(view instanceof TableAteView){
								((TableAteView)view).getRow(0).getTextCellList().get(0).requestKeyboardFocus();
								isNormal=false;
								break;
							}
						}
						if(isNormal) stepTable.getRow(rowIndex-1).getValueColumnView().requestKeyboardFocus();
					}
				}
				@Override public void cellDownPressed(TextCell cell, int offset) {
					StepRow row=(StepRow)(((TextCell)(table.getSuperView())).getOwnerRow());
						StepTable stepTable=(StepTable)(table.getSuperView().getSuperView());
						if(row.getStepNumber()<stepTable.getRowCount()){
						int rowIndex = stepTable.indexOfRow(row);
						View[] views=stepTable.getRow(rowIndex+1).getValueColumnView().getSubviews();
						boolean isNormal=true;
						for(View view:views){
							if(view instanceof TableAteView){
								((TableAteView)view).getRow(0).getTextCellList().get(0).requestKeyboardFocus();
								isNormal=false;
								break;
							}
						}
						if(isNormal) stepTable.getRow(rowIndex-1).getValueColumnView().requestKeyboardFocus();
						}
				}
				@Override public void cellEnterPressed(TextCell cell) {
					StepRow row=(StepRow)(((TextCell)(table.getSuperView())).getOwnerRow());

//					stepTable.addRow(stepTable.indexOfRow(row)+1, "", true);
					row.cellEnterPressed(cell);
				}
				@Override public void cellBackspacePressed(TextCell cell) {
					StepRow row=(StepRow)(((TextCell)(table.getSuperView())).getOwnerRow());
					StepTable stepTable=(StepTable)(table.getSuperView().getSuperView());
					int rowIndex = stepTable.indexOfRow(row);
					if (rowIndex > 0) {
//						String text = getValueColumnView().getPlainText();
						stepTable.deleteRow(stepTable.indexOfRow(row), true);
//						TextCell cell = stepTable.getRow(rowIndex - 1).getValueColumnView();
//						cell.setText(cell.getPlainText() + text);
//						cell.setCaretPosition(CaretIndex.before(cell.textLength() - text.length()));
					}
				
				}
			};
			signalCell=new TextCell(defaultDelegate, false, false);
			signalCell.setHintText("SIGNAL");
			signalCell.setBreakIterator(null);
			signalCell.setText("");
			startCell=new TextCell(defaultDelegate, false, false);
			startCell.setHintText("START");
			startCell.setBreakIterator(null);
			startCell.setText("");
			timeCell=new TextCell(defaultDelegate, false, false);
			timeCell.setHintText("TIME");
			timeCell.setBreakIterator(null);
			timeCell.setText("");
			increCell=new TextCell(defaultDelegate, false, false);
			increCell.setHintText("FOOTSTEP");
			increCell.setBreakIterator(null);
			increCell.setText("");
			endCell=new TextCell(defaultDelegate, false, false);
			endCell.setHintText("END");
			endCell.setBreakIterator(null);
			endCell.setText("");
			list.add(signalCell);
			list.add(label1);
			list.add(startCell);
			list.add(label2);
			list.add(timeCell);
			list.add(label3);
			list.add(increCell);
			list.add(label4);
			list.add(endCell);
			list.add(label5);
			cellList.add(signalCell);
			cellList.add(startCell);
			cellList.add(timeCell);
			cellList.add(increCell);
			cellList.add(endCell);
			for(final TextCell cell:cellList){
				cell.addEventHandler(View.KEY_PRESSED, new EventHandler() {
					
					@Override
					public void handle(View arg0, Key arg1, Object arg2) {
						// TODO Auto-generated method stub
						int index=cellList.indexOf(cell);
						if(((KeyEvent)arg2).getKeyCode()==37){
							if(index>0){
								cellList.get(index-1).requestKeyboardFocus();
							}
						}else if(((KeyEvent)arg2).getKeyCode()==39){
							if(index<cellList.size()-1){
								cellList.get(index+1).requestKeyboardFocus();
							}
						}
					}
				});
			}
	}
	@Override
	public void tableViewRowAdded(TableAteView table) {
		// TODO Auto-generated method stub
		signalBinding=new DataBinding(signalCell,TextCell.TEXT_CHANGED,sentence,AteSentence.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				sentence.setPara1(signalCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				signalCell.setText(sentence.getPara1());
			}
		};
		startBinding=new DataBinding(startCell,TextCell.TEXT_CHANGED,sentence,AteSentence.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				sentence.setPara2(startCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				startCell.setText(sentence.getPara2());
			}
		};
		timeBinding=new DataBinding(timeCell,TextCell.TEXT_CHANGED,sentence,AteSentence.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				sentence.setPara3(timeCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				timeCell.setText(sentence.getPara3());
			}
		};
		increBinding=new DataBinding(increCell,TextCell.TEXT_CHANGED,sentence,AteSentence.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				sentence.setPara4(increCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				increCell.setText(sentence.getPara4());
			}
		};
		endBinding=new DataBinding(endCell,TextCell.TEXT_CHANGED,sentence,AteSentence.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				sentence.setPara5(endCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				endCell.setText(sentence.getPara5());
			}
		};
		
	}

	@Override
	public void tableViewRowRemoved(TableAteView table) {
		// TODO Auto-generated method stub
		signalBinding.dispose();
		startBinding.dispose();
		timeBinding.dispose();
		increBinding.dispose();
		endBinding.dispose();
		
	}

	@Override
	public List<View> getView() {
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public List<TextCell> getTextCellList() {
		// TODO Auto-generated method stub
		return cellList;
	}

}
