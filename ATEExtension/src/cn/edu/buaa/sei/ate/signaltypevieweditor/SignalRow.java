package cn.edu.buaa.sei.ate.signaltypevieweditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import cn.edu.buaa.sei.ate.iodefivieweditor.IORow;
import cn.edu.buaa.sei.ate.metamodel.ATECapbilityView;
import cn.edu.buaa.sei.ate.metamodel.ATESignalTypeView;
import cn.edu.buaa.sei.ate.metamodel.IODefinition;
import cn.edu.buaa.sei.ate.metamodel.Signal;
import cn.edu.buaa.sei.ate.metamodel.nullFactory;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.diagram.widgets.DataBinding;
import cn.edu.buaa.sei.rucm.spec.widgets.EmbeddedRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCellDelegate;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public class SignalRow implements TableAteViewRow{
	private TextCell signalNameCell;
	private TextCell signalTypeCell;
	private TextCell parameterCell;
	private TextCell channerCell;	
	private TextCellDelegate defaultDelegate;
	private DataBinding signalNameBinding;
	private DataBinding signalTypeBinding;
	private DataBinding parameterBinding;
	private DataBinding channelBinding;
	private Signal signal;
	private int index;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	List<TextCell> cellList=new ArrayList<TextCell>();
	List<View> list=new ArrayList<View>();
	public SignalRow(final ATESignalTypeView model,final Signal signal,final TableAteView table) {
			this.signal=signal;
			this.index=model.getSignallist().indexOf(signal);
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
					Signal sig=nullFactory.createSignal();
					int index=getIndex();
					model.getSignallist().add(index+1,sig);
					SignalRow sigRow=new SignalRow(model, sig, table);
					TextCell newcell=(TextCell)(sigRow.getView().get(0));
					table.addRow(index+2,sigRow);
					newcell.requestKeyboardFocus();
				}
				@Override public void cellBackspacePressed(TextCell cell) {
					int rowIndex=model.getSignallist().indexOf(signal);
					if(rowIndex>0){
						model.getSignallist().remove(rowIndex);
						table.removeRow(rowIndex+1);
						TextCell newcell=(TextCell)(table.getRow(rowIndex).getView().get(0));
						newcell.requestKeyboardFocus();
				}
				}
			};
		
			//model.getSignallist().add(signal);			
			signalNameCell = new TextCell(defaultDelegate, false, false);
			signalNameCell.setText("");
			signalNameCell.setHintText("");
			signalNameCell.setBreakIterator(null);
			signalTypeCell = new TextCell(defaultDelegate, false, true);
			signalTypeCell.setText("");
			signalTypeCell.setHintText("");
			signalTypeCell.setBreakIterator(null);
			parameterCell = new TextCell(defaultDelegate, false, true);
			parameterCell.setText("");
			parameterCell.setHintText("");
			parameterCell.setBreakIterator(null);
//			parameterCell=new RowLabel();
//			parameterCell.setBackgroundColor(Color.BLACK);
			channerCell = new TextCell(defaultDelegate, false, true);
			channerCell.setText("");
			channerCell.setHintText("");
			channerCell.setBreakIterator(null);
			list.add(signalNameCell);
			list.add(signalTypeCell);
			list.add(parameterCell);
			list.add(channerCell);
			cellList.add(signalNameCell);
			cellList.add(signalTypeCell);
			cellList.add(parameterCell);
			cellList.add(channerCell);
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
		signalTypeBinding = new DataBinding(signalTypeCell, TextCell.TEXT_CHANGED, signal, Signal.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				signal.setSignalTypeName(signalTypeCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				signalTypeCell.setText(signal.getSignalTypeName());
			}
		};
		signalNameBinding=new DataBinding(signalNameCell,TextCell.TEXT_CHANGED,signal,Signal.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				signal.setSignalName(signalNameCell.getPlainText());
				
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				signalNameCell.setText(signal.getSignalName());
				
			}
		};
		channelBinding=new DataBinding(channerCell,TextCell.TEXT_CHANGED,signal,Signal.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				signal.setChannel(channerCell.getPlainText());
				
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				channerCell.setText(signal.getChannel());
			}
		};
		parameterBinding=new DataBinding(parameterCell,TextCell.TEXT_CHANGED,signal,Signal.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				signal.setParameter(parameterCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				parameterCell.setText(signal.getParameter());
			}
		};
	}

	@Override
	public void tableViewRowRemoved(TableAteView table) {
		// TODO Auto-generated method stub
		signalNameBinding.dispose();
		signalTypeBinding.dispose();
		parameterBinding.dispose();
		channelBinding.dispose();
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
