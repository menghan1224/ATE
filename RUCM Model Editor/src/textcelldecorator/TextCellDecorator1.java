package textcelldecorator;

import javax.swing.CellEditor;
import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;

import ca.carleton.sce.squall.ucmeta.Sentence;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.spec.TextCellDecorator;
import cn.edu.buaa.sei.rucm.spec.widgets.Button;
import cn.edu.buaa.sei.rucm.spec.widgets.TableAteView;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Insets;

public class TextCellDecorator1 implements TextCellDecorator {
	private TableAteView table;
	public TextCellDecorator1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(final TextCell cell, ManagedObject model) {
		// TODO Auto-generated method stub
		final Button button = new Button();
		//System.out.println("ÊÇ·ñÊÇ¾ä×Ó:"+(Sentence)(model).get("") instanceof );
		cell.addSubview(button);
		cell.addEventHandler(View.LAYOUT, new EventHandler() {
			@Override
			public void handle(View arg0, Key arg1, Object arg2) {
				// TODO Auto-generated method stub
				button.setSize(20, 20);
				button.setPosition(1000, 0);
				button.layout();
			}
		});
//		cell.addEventHandler(View.MOUSE_RELEASED, new EventHandler() {
//			
//			@Override
//			public void handle(View arg0, Key arg1, Object arg2) {
//				}
//				
//			}
//		});
		button.addEventHandler(Button.MOUSE_CLICKED, new EventHandler() {
			
			@Override
			public void handle(View arg0, Key arg1, Object arg2) {
				// TODO Auto-generated method stub
				if(table!=null){
				table.removeAllRows();
				}
				table=new TableAteView(7);
			
				cell.addSubview(table);
				table.setHeight(20);
			     table.setWidth(500);
				table.layout();
				
//				cell.addEventHandler(Button.MOUSE_CLICKED, new EventHandler() {
//					
//					@Override
//					public void handle(View arg0, Key arg1, Object arg2) {
//						// TODO Auto-generated method stub
//						table.setHeight(cell.getHeight()-1);
//						table.setWidth(cell.getWidth()-1);
//						table.layout();
//						
//					}
//				});
	//cell.setInsets(Insets.make(0,0,0,0));
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
