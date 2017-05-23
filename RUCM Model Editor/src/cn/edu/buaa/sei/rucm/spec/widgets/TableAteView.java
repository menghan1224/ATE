package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.gongzh.snail.Animation;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public class TableAteView extends View{
	public static final Color INNER_GRID_COLOR = new Color(0xc3d5f2);
	public static final Color OUTER_GRID_COLOR = new Color(0xa2bbe4);
	
	protected final List<TableAteViewRow> rows;
	protected int keyColWidth;
	protected boolean layoutMutex;
	protected Color innerGridColor, outerGridColor;
	protected int preferredHeight;
	private int columns;
	public TableAteView(int columns) {
		super();
		setPaintMode(PaintMode.DIRECTLY);
		setBackgroundColor(null);
		
		rows = new ArrayList<TableAteViewRow>();
		innerGridColor = INNER_GRID_COLOR;
		outerGridColor = OUTER_GRID_COLOR;
		this.columns=columns;
		layoutMutex = false;
		preferredHeight = 0;
		addEventHandler(LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				layoutTableView();
			}
		});
	}
	public TableAteView(){
		super();
		setPaintMode(PaintMode.DIRECTLY);
		setBackgroundColor(null);
		
		rows = new ArrayList<TableAteViewRow>();
		innerGridColor = INNER_GRID_COLOR;
		outerGridColor = OUTER_GRID_COLOR;
		layoutMutex = false;
		preferredHeight = 0;
		addEventHandler(LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				layoutTableView();
			}
		});
	}
	public void setColumns(int column){
		this.columns=column;
	}
	public Color getInnerGridColor() {
		return innerGridColor;
	}
	
	public void setInnerGridColor(Color innerGridColor) {
		this.innerGridColor = innerGridColor;
		setNeedsRepaint();
	}
	
	public Color getOuterGridColor() {
		return outerGridColor;
	}
	
	public void setOuterGridColor(Color outerGridColor) {
		this.outerGridColor = outerGridColor;
		layout();
		setNeedsRepaint();
	}
	
	public final void addRow(TableAteViewRow row) {
		addRow(rows.size(), row);
	}
	
	public void addRow(int index, TableAteViewRow row) {
		rows.add(index, row);
//		if (row.getKeyColumnView() != null) addSubview(row.getKeyColumnView());
//		addSubview(row.getValueColumnView());
		for(View view:row.getView()){
			addSubview(view);
		}
		layout();
		row.tableViewRowAdded(this);
	}
	
	public final void removeRow(TableViewRow row) {
		removeRow(rows.indexOf(row));
	}
	
	public void removeRow(int i) {
		TableAteViewRow row = rows.get(i);
//		if (row.getKeyColumnView() != null) row.getKeyColumnView().removeFromSuperView();
//		row.getValueColumnView().removeFromSuperView();
		if(row.getView().size()!=0){
			for(View view:row.getView()){
				view.removeFromSuperView();
			}
		}
		
		
		rows.remove(i);
		layout();
		row.tableViewRowRemoved(this);
	}
	
	public void removeAllRows() {
		for (TableAteViewRow row : rows) {
//			if (row.getKeyColumnView() != null) row.getKeyColumnView().removeFromSuperView();
//			row.getValueColumnView().removeFromSuperView();
//			row.tableViewRowRemoved(this);
			List<View> list=row.getView();
			if(list.size()!=0){
				for(View view:list){
					view.removeFromSuperView();
				}
			}
			
		}
		rows.clear();
		layout();
	}
	
	public int indexOfRow(TableAteViewRow row) {
		return rows.indexOf(row);
	}
	
	public TableAteViewRow getRow(int index) {
		return rows.get(index);
	}
	
	public int getRowCount() {
		return rows.size();
	}
	
	public List<TableAteViewRow> rows() {
		return Collections.unmodifiableList(rows);
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		g.setColor(innerGridColor);
		//System.out.println("repaint");
		final int width = getWidth() - 1;
		
		int top = outerGridColor != null ? 0 : -1;
		for (int i = 0; i < rows.size(); i++) {
			top += rows.get(i).getView().get(0).getHeight() + 1;
			if (i < rows.size() - 1) g.drawLine(0, top, width, top);
		}
		
		//final int height = top;
		final int height=getHeight()-1;
		int block=width/columns;
		int start=0;
		int step=block;
		
		for(int i=0;i<columns;i++){
			g.drawLine(start, 0, start, height);
			
			start+=step+1;
		}
		
		if (outerGridColor != null) {
			g.setColor(outerGridColor);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		}
	}
	
	private void layoutTableView() {
		if (!layoutMutex) {
			layoutMutex = true;
			
			// calculate widths
			keyColWidth = getMinimumKeyColumnWidth();
//			for (TableAteViewRow row : rows) {
//				keyColWidth = Math.max(keyColWidth, row.getKeyColumnPreferredWidth(this));
//			}
//			
			// layout
			int curTop = outerGridColor != null ? 1 : 0;
			final int keyColLeft = outerGridColor != null ? 1 : 0;
			final int valueColWidth = Math.max(getWidth() - keyColWidth - 1 - keyColLeft * 2, 0);
			for (TableAteViewRow row : rows) {
//				if (row.getKeyColumnView() != null) {
//					row.getKeyColumnView().setLeft(keyColLeft);
//					row.getKeyColumnView().setWidth(keyColWidth);
//				}
//				row.getValueColumnView().setLeft(keyColWidth + 1 + keyColLeft);
//				row.getValueColumnView().setWidth(valueColWidth);
				int width=getWidth()-1;
				int block=width/columns;
				int start=1;
				int step=block;
				int rowHeight=0;
				for(View view:row.getView()){
					view.setLeft(start);
					view.setWidth(step);
//					System.out.println("start:"+start);
//					System.out.println("step:"+step);
					start+=step+1;
					rowHeight=view.getPreferredHeight();
					if(view!=null){
						rowHeight=Math.max(view.getPreferredHeight(), rowHeight);
						//rowHeight=getHeight()-1;
						view.setTop(curTop);
						view.setHeight(rowHeight);
					}
				}
				
				curTop+=rowHeight+1;
//				int rowHeight = row.getView().get(0).getPreferredHeight();
//				if (row.getKeyColumnView() != null) {
//					rowHeight = Math.max(row.getKeyColumnView().getPreferredHeight(), rowHeight);
//					row.getKeyColumnView().setTop(curTop);
//					row.getKeyColumnView().setHeight(rowHeight);
//				}
//				row.getValueColumnView().setTop(curTop);
//				row.getValueColumnView().setHeight(rowHeight);
//				curTop += rowHeight + 1;
				
			}
			int start=1;
			int step=350;
		
			preferredHeight = curTop - (outerGridColor == null ? 1 : 0);
			setNeedsRepaint();
			
			layoutMutex = false;
		}
	}
	
	protected int getMinimumKeyColumnWidth() {
		return -1;
	}
	
	@Override
	public int getPreferredHeight() {
		return preferredHeight;
	}
	
	public Animation makeFadeInAnimation(float delay) {
		setAlpha(0.0f);
		return new Animation(0.25f, Animation.EaseOut) {
			@Override
			protected void animate(float progress) {
				AffineTransform tr = AffineTransform.getTranslateInstance(0, (progress - 1.0f) * 24.f);
				setAlpha(progress);
				setTransform(tr);
			}
			@Override
			protected void completed(boolean canceled) {
				setTransform(null);
			}
		}.makeDelay(delay);
	}
	

}
