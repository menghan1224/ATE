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
import co.gongzh.snail.View;
import co.gongzh.snail.util.Insets;

public class PauseSentence implements TableAteViewRow{
	private TextCell pauseCell;
	private AteSentence sentence;
	
	private TextCellDelegate defaultDelegate;
	List<View> list=new ArrayList<View>();
	private DataBinding pauseBinding;
	List<TextCell> cellList=new ArrayList<TextCell>();
	private RowLabel label1;
	{
		label1=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(3);
			setIcon(RUCMPluginResource.getImage("pause.png"));
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.black);
			getTextView().setText("ÏµÍ³µ÷ÊÔÔÝÍ£");
			}
		};
		
	}
	public int getNumOfColumns(){
		return 2;
	}

	public PauseSentence(final TableAteView table,AteSentence sentence) {
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
		
			pauseCell=new TextCell(defaultDelegate, false, false);
			pauseCell.setText("");
			pauseCell.setHintText("REMARK");
			pauseCell.setBreakIterator(null);
			list.add(label1);
			list.add(pauseCell);
			cellList.add(pauseCell);
			
			
	}
	@Override
	public void tableViewRowAdded(TableAteView table) {
		// TODO Auto-generated method stub
		pauseBinding=new DataBinding(pauseCell,TextCell.TEXT_CHANGED,sentence,AteSentence.KEY_NAME) {
			
			@Override
			protected void viewPropertyChanged(Object arg) {
				// TODO Auto-generated method stub
				sentence.setPara1(pauseCell.getPlainText());
			}
			
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				// TODO Auto-generated method stub
				pauseCell.setText(sentence.getPara1());
			}
		};
	}

	@Override
	public void tableViewRowRemoved(TableAteView table) {
		// TODO Auto-generated method stub
		pauseBinding.dispose();
		
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
