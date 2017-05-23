package cn.edu.buaa.sei.ate.capbilityvieweditor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import cn.edu.buaa.sei.ate.iodefivieweditor.IORow;
import cn.edu.buaa.sei.ate.metamodel.ATECapbility;
import cn.edu.buaa.sei.ate.metamodel.ATECapbilityView;
import cn.edu.buaa.sei.ate.metamodel.ATESignalTypeView;
import cn.edu.buaa.sei.ate.metamodel.IODefinition;
import cn.edu.buaa.sei.ate.metamodel.Signal;
import cn.edu.buaa.sei.ate.metamodel.nullFactory;
import cn.edu.buaa.sei.ate.signaltypevieweditor.SignalRow;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.widgets.DataBinding;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCellDelegate;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public class CapbilityRow implements TableAteViewRow{
	private TextCell pinportCell;
	private TextCell pinportTypeCell;
	private TextCell functionCell;
	private TextCell dataRangeCell;
	private TextCell accuracyCell;
	private TextCell numberCell;
	private TextCell codeCell;
	private ATECapbility capbility;
	private TextCellDelegate defaultDelegate;
	List<View> list=new ArrayList<View>();
	List<TextCell> cellList=new ArrayList<TextCell>();
	private DataBinding pinportBinding;
	private DataBinding pinportTypeBinding;
	private DataBinding functionBinding;
	private DataBinding dataRangeBinding;
	private DataBinding accuracyBinding;
	private DataBinding numberBinding;
	private DataBinding codeBinding;
	private int index;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public CapbilityRow(final ATECapbilityView model,final ATECapbility capbility,final TableAteView table) {
			this.capbility=capbility;
			this.index=model.getCapbilities().indexOf(capbility);
			 defaultDelegate = new TextCellDelegate() {
				@Override public void cellTabPressed(TextCell cell, boolean shift) {}
				@Override public void cellUpPressed(TextCell cell, int offset) {
					if(index>0){
						((TextCell)(table.getRow(index).getView().get(cellList.indexOf(cell)))).requestKeyboardFocus();
					}
				}
				@Override public void cellDownPressed(TextCell cell, int offset) {
					if(index<table.getRowCount()-2){
						((TextCell)(table.getRow(index+2).getView().get(cellList.indexOf(cell)))).requestKeyboardFocus();
					}
				}
				@Override public void cellEnterPressed(TextCell cell) {
					ATECapbility capbi=nullFactory.createATECapbility();
					int index=getIndex();
					model.getCapbilities().add(index+1,capbi);
					CapbilityRow capRow=new CapbilityRow(model, capbi, table);
					TextCell newcell=(TextCell)(capRow.getView().get(0));
					table.addRow(index+2,capRow);
					newcell.requestKeyboardFocus();
				}
				@Override public void cellBackspacePressed(TextCell cell) {
					int rowIndex=model.getCapbilities().indexOf(capbility);
					if(rowIndex>0){
						model.getCapbilities().remove(rowIndex);
						table.removeRow(rowIndex+1);
						TextCell newcell=(TextCell)(table.getRow(rowIndex).getView().get(0));
						newcell.requestKeyboardFocus();
				}
				}
			};
			
		
			pinportCell=new TextCell(defaultDelegate, false, false);
			pinportCell.setText("");
			pinportCell.setHintText("");
			pinportCell.setBreakIterator(null);
			pinportTypeCell=new TextCell(defaultDelegate, false, false);
			pinportTypeCell.setText("");
			pinportTypeCell.setHintText("");
			pinportTypeCell.setBreakIterator(null);
			functionCell=new TextCell(defaultDelegate, false, false);
			functionCell.setText("");
			functionCell.setHintText("");
			functionCell.setBreakIterator(null);
			dataRangeCell=new TextCell(defaultDelegate, false, false);
			dataRangeCell.setText("");
			dataRangeCell.setHintText("");
			dataRangeCell.setBreakIterator(null);
			accuracyCell=new TextCell(defaultDelegate, false, false);
			accuracyCell.setText("");
			accuracyCell.setHintText("");
			accuracyCell.setBreakIterator(null);
			numberCell=new TextCell(defaultDelegate, false, false);
			numberCell.setText("");
			numberCell.setHintText("");
			numberCell.setBreakIterator(null);
			codeCell=new TextCell(defaultDelegate, false, false);
			codeCell.setText("");
			codeCell.setHintText("");
			codeCell.setBreakIterator(null);
			list.add(pinportCell);
			list.add(pinportTypeCell);
			list.add(functionCell);
			list.add(dataRangeCell);
			list.add(accuracyCell);
			list.add(numberCell);
			list.add(codeCell);
			cellList.add(pinportCell);
			cellList.add(pinportTypeCell);
			cellList.add(functionCell);
			cellList.add(dataRangeCell);
			cellList.add(accuracyCell);
			cellList.add(numberCell);
			cellList.add(codeCell);
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
		pinportBinding = new DataBinding(pinportCell, TextCell.TEXT_CHANGED, capbility, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				capbility.setPinport(pinportCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				pinportCell.setText(capbility.getPinport());
			}
		};
		pinportTypeBinding = new DataBinding(pinportTypeCell, TextCell.TEXT_CHANGED, capbility, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				capbility.setPinporttype(pinportTypeCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				pinportTypeCell.setText(capbility.getPinporttype());
			}
		};
		functionBinding = new DataBinding(functionCell, TextCell.TEXT_CHANGED, capbility, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				capbility.setFunction(functionCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				functionCell.setText(capbility.getFunction());
			}
		};
		dataRangeBinding = new DataBinding(dataRangeCell, TextCell.TEXT_CHANGED, capbility, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				capbility.setDatarange(dataRangeCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				dataRangeCell.setText(capbility.getDatarange());
			}
		};
		accuracyBinding = new DataBinding(accuracyCell, TextCell.TEXT_CHANGED, capbility, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				capbility.setAccuracy(accuracyCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				accuracyCell.setText(capbility.getAccuracy());
			}
		};
		numberBinding = new DataBinding(numberCell, TextCell.TEXT_CHANGED, capbility, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				capbility.setNumber(numberCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				numberCell.setText(capbility.getNumber());
			}
		};
		codeBinding = new DataBinding(codeCell, TextCell.TEXT_CHANGED, capbility, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				capbility.setCode(codeCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				codeCell.setText(capbility.getCode());
			}
		};
		
	}

	@Override
	public void tableViewRowRemoved(TableAteView table) {
		// TODO Auto-generated method stub
		pinportBinding.dispose();
		pinportTypeBinding.dispose();
		codeBinding.dispose();
		numberBinding.dispose();
		dataRangeBinding.dispose();
		accuracyBinding.dispose();
		functionBinding.dispose();
		
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

