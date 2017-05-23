package cn.edu.buaa.sei.ate.iodefivieweditor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicSplitPaneUI.KeyboardDownRightHandler;
import javax.xml.crypto.Data;

import cn.edu.buaa.sei.ate.capbilityvieweditor.CapbilityRow;
import cn.edu.buaa.sei.ate.metamodel.ATECapbility;
import cn.edu.buaa.sei.ate.metamodel.ATECapbilityView;
import cn.edu.buaa.sei.ate.metamodel.ATEIODefinitionView;
import cn.edu.buaa.sei.ate.metamodel.IODefinition;
import cn.edu.buaa.sei.ate.metamodel.nullFactory;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.widgets.DataBinding;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCellDelegate;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.text.CaretIndex;

public class IORow implements TableAteViewRow{
	private TextCell ioNameCell;
	private TextCell pinportCell;
	private TextCell ioDefinitionCell;
	private TextCell directionCell;
	private TextCell signalTypeCell;
	private TextCell remarkCell;
	private IODefinition definition;
	private TextCellDelegate defaultDelegate;
	List<View> list=new ArrayList<View>();
	List<TextCell> cellList=new ArrayList<TextCell>();
	private DataBinding ioNameBinding;
	private DataBinding pinportBinding;
	private DataBinding ioDefinitionBinding;
	private DataBinding directionBinding;
	private DataBinding signalTypeBinding;
	private DataBinding remarkBinding;
	private int index;
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public IORow(final ATEIODefinitionView model,final IODefinition definition,final TableAteView table) {
			 this.index=model.getIodefinitions().indexOf(definition);
			 this.definition=definition;
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
					IODefinition defi=nullFactory.createIODefinition();
					int index=getIndex();
					model.getIodefinitions().add(index+1,defi);
					IORow ioRow=new IORow(model, defi, table);
					TextCell newcell=(TextCell)(ioRow.getView().get(0));
					table.addRow(index+2,ioRow);
					newcell.requestKeyboardFocus();
					//newcell.setCaretPosition(CaretIndex.before(0));
				}
				@Override public void cellBackspacePressed(TextCell cell) {
					int rowIndex=model.getIodefinitions().indexOf(definition);
					if(rowIndex>0){
						model.getIodefinitions().remove(rowIndex);
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
			ioNameCell=new TextCell(defaultDelegate, false, false);
			ioNameCell.setText("");
			ioNameCell.setBreakIterator(null);
			signalTypeCell=new TextCell(defaultDelegate, false, false);
			signalTypeCell.setText("");
			signalTypeCell.setBreakIterator(null);
			remarkCell=new TextCell(defaultDelegate, false, false);
			remarkCell.setText("");
			remarkCell.setBreakIterator(null);
			ioDefinitionCell=new TextCell(defaultDelegate, false, false);
			ioDefinitionCell.setText("");
			ioDefinitionCell.setBreakIterator(null);
			directionCell=new TextCell(defaultDelegate, false, false);
			directionCell.setText("");
			directionCell.setBreakIterator(null);
			
			list.add(ioNameCell);
			list.add(pinportCell);
			list.add(directionCell);
			list.add(signalTypeCell);
			list.add(ioDefinitionCell);
			list.add(remarkCell);
			cellList.add(ioNameCell);
			cellList.add(pinportCell);
			cellList.add(directionCell);
			cellList.add(signalTypeCell);
			cellList.add(ioDefinitionCell);
			cellList.add(remarkCell);
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
		ioNameBinding = new DataBinding(ioNameCell, TextCell.TEXT_CHANGED, definition, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				definition.setIoName(ioNameCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				ioNameCell.setText(definition.getIoName());
			}
		};
		pinportBinding = new DataBinding(pinportCell, TextCell.TEXT_CHANGED, definition, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				definition.setPinport(pinportCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				pinportCell.setText(definition.getPinport());
			}
		};
		directionBinding = new DataBinding(directionCell, TextCell.TEXT_CHANGED, definition, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				definition.setDirection(directionCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				directionCell.setText(definition.getDirection());
			}
		};
		signalTypeBinding = new DataBinding(signalTypeCell, TextCell.TEXT_CHANGED, definition, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				definition.setSignalType(signalTypeCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				signalTypeCell.setText(definition.getSignalType());
			}
		};
		ioDefinitionBinding = new DataBinding(ioDefinitionCell, TextCell.TEXT_CHANGED, definition, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				definition.setIoDefinition(ioDefinitionCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				ioDefinitionCell.setText(definition.getIoDefinition());
			}
		};
		remarkBinding = new DataBinding(remarkCell, TextCell.TEXT_CHANGED, definition, ATECapbility.KEY_NAME) {		
			@Override
			protected void viewPropertyChanged(Object arg) {
				definition.setRemark(remarkCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				remarkCell.setText(definition.getRemark());
			}
		};
		
		
	}

	@Override
	public void tableViewRowRemoved(TableAteView table) {
		// TODO Auto-generated method stub
		pinportBinding.dispose();
		remarkBinding.dispose();
		ioDefinitionBinding.dispose();
		signalTypeBinding.dispose();
		ioNameBinding.dispose();
		directionBinding.dispose();
		
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
