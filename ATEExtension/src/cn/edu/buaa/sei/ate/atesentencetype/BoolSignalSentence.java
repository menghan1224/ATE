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

public class BoolSignalSentence implements TableAteViewRow{
	private TextCell pin1Cell;
	private TextCell pin2Cell;
	private TextCell signalValueCell;
	private AteSentence sentence;
	
	private TextCellDelegate defaultDelegate;
	List<View> list=new ArrayList<View>();
	private DataBinding pin1Binding;
	private DataBinding pin2Binding;
	private DataBinding signalValueBinding;
	private RowLabel label1;
	private RowLabel label2;
	private RowLabel label3;
	private RowLabel label4;
	List<TextCell> cellList=new ArrayList<TextCell>();
	{
		label1=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(3);
			setIcon(RUCMPluginResource.getImage("bool.png"));
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("在");
			}
		};
		 label2=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("和");
			}
		};
		 label3=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("之间添加");
			}
		};
		 label4=new RowLabel(){
				{
				setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
				setInsets(Insets.make(3, 6, 3, 6));
				setIconSpacing(0);
//				getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
				getTextView().setDefaultTextColor(Color.black);
				getTextView().setText("布尔信号");
				}
			};
	}
	public int getNumOfColumns(){
		return 7;
	}

	public BoolSignalSentence(final TableAteView table,AteSentence sentence) {
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
		
			pin1Cell=new TextCell(defaultDelegate, false, false);
			pin1Cell.setText("");
			pin1Cell.setHintText("PIN1");
			pin1Cell.setBreakIterator(null);
			pin2Cell=new TextCell(defaultDelegate, false, false);
			pin2Cell.setText("");
			pin2Cell.setHintText("PIN2");
			pin2Cell.setBreakIterator(null);
			signalValueCell=new TextCell(defaultDelegate, false, false);
			signalValueCell.setText("");
			signalValueCell.setHintText("SIGNAL_PARAM");
			signalValueCell.setBreakIterator(null);
			list.add(label1);
			list.add(pin1Cell);
			list.add(label2);
			list.add(pin2Cell);
			list.add(label3);
			list.add(signalValueCell);
			list.add(label4);
			cellList.add(pin1Cell);
			cellList.add(pin2Cell);
			cellList.add(signalValueCell);
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
	
pin1Binding=new DataBinding(pin1Cell,TextCell.TEXT_CHANGED,sentence,AteSentence.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				sentence.setPara1(pin1Cell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				pin1Cell.setText(sentence.getPara1());
			}
		};
		pin2Binding=new DataBinding(pin2Cell,TextCell.TEXT_CHANGED,sentence,AteSentence.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				sentence.setPara2(pin2Cell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				pin2Cell.setText(sentence.getPara2());
			}
		};
		signalValueBinding=new DataBinding(signalValueCell,TextCell.TEXT_CHANGED,sentence,AteSentence.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				sentence.setPara3(signalValueCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				signalValueCell.setText(sentence.getPara3());
			}
		};
		
		
	}

	@Override
	public void tableViewRowRemoved(TableAteView table) {
		// TODO Auto-generated method stub
		
		pin1Binding.dispose();
		pin2Binding.dispose();
		signalValueBinding.dispose();
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
