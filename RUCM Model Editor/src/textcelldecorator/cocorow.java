package textcelldecorator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import cn.edu.buaa.sei.rucm.spec.widgets.RowLabel;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import cn.edu.buaa.sei.rucm.spec.widgets.TextTableViewRow;
import co.gongzh.snail.View;
import co.gongzh.snail.util.Insets;

public class cocorow implements TableAteViewRow {
	private  RowLabel label1;
	private  RowLabel label2;
	private RowLabel label3;
	private RowLabel label4;
	private RowLabel label5;
	private RowLabel label6;
	{
		label1=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.RED);
			getTextView().setText("�ź�����");
			}
		};
		 label2=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.RED);
			getTextView().setText("���ź�");
			}
		};
		 label3=new RowLabel(){
			{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(0);
//			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setDefaultTextColor(Color.RED);
			getTextView().setText("�źŷ���");
			}
		};
		 label4=new RowLabel(){
				{
				setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
				setInsets(Insets.make(3, 6, 3, 6));
				setIconSpacing(0);
//				getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
				getTextView().setDefaultTextColor(Color.RED);
				getTextView().setText("�ź�����");
				}
			};
		label5=new RowLabel(){
					{
					setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
					setInsets(Insets.make(3, 6, 3, 6));
					setIconSpacing(0);
//					getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
					getTextView().setDefaultTextColor(Color.RED);
					getTextView().setText("�ź�˵��");
					}
				};
		label6=new RowLabel(){
						{
						setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
						setInsets(Insets.make(3, 6, 3, 6));
						setIconSpacing(0);
//						getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
						getTextView().setDefaultTextColor(Color.RED);
						getTextView().setText("��ע");
						}
					};
	}
	@Override
	public void tableViewRowAdded(TableAteView table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tableViewRowRemoved(TableAteView table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<View> getView() {
		// TODO Auto-generated method stub
		List<View> list=new ArrayList<View>();
		list.add(label1);
		list.add(label2);
		list.add(label3);
		list.add(label4);
		list.add(label5);
		list.add(label6);
		return list;
	}

	@Override
	public List<TextCell> getTextCellList() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
