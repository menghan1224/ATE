package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.buaa.sei.lmf.LMFUtility;

import co.gongzh.snail.Animation;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

/**
 * <code>TableView</code> is a basic widget for building 2-column table. The first columns is called "key column", while the second
 * is called "value column". <code>TableView</code> is often used to present key-value structure data. By default, a <code>TableView</code>
 * has both outer grid lines and inner grid lines. Clients can change the grid line color by method {@link #setOuterGridColor(Color)} and
 * {@link #getInnerGridColor()}. If the outer grid color is set to <code>null</code>, the table will dismiss the outer grid, which is
 * useful to build a nested table in a row.
 * <p>
 * The key column width of the table is determined by the maximum value of the all the values returned by {@link TableViewRow#getKeyColumnPreferredWidth(TableView)} of
 * each rows. Clients can also override {@link #getMinimumKeyColumnWidth()} method to provide a minimum width for key column. To update the
 * layout of the table, call {@link #layout()}.
 * <p>
 * The content of <code>TableView</code> is presented by a group of {@link TableViewRow} objects. Use {@link #addRow(TableViewRow)}
 * and {@link #removeRow(int)} to modify the content.
 * <p>
 * Although <code>TableView</code> is designed to present 2-column table, client still can provide special {@link TableViewRow} to build
 * 1-column table or multiple columns table as well. For more information, see {@link TableViewRow}.
 * @author Gong Zhang
 *
 */
public class TableView extends View {
	
	public static final Color INNER_GRID_COLOR = new Color(0xc3d5f2);
	public static final Color OUTER_GRID_COLOR = new Color(0xa2bbe4);
	
	protected final List<TableViewRow> rows;
	protected int keyColWidth;
	protected boolean layoutMutex;
	protected Color innerGridColor, outerGridColor;
	protected int preferredHeight;
	
	public TableView() {
		super();
		setPaintMode(PaintMode.DIRECTLY);
		setBackgroundColor(null);
		
		rows = new ArrayList<TableViewRow>();
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
	
	public final void addRow(TableViewRow row) {
		addRow(rows.size(), row);
	}
	
	public void addRow(int index, TableViewRow row) {
		rows.add(index, row);
		if (row.getKeyColumnView() != null) addSubview(row.getKeyColumnView());
		addSubview(row.getValueColumnView());
		layout();
		row.tableViewRowAdded(this);
	}
	
	public final void removeRow(TableViewRow row) {
		removeRow(rows.indexOf(row));
	}
	
	public void removeRow(int i) {
		TableViewRow row = rows.get(i);
		if (row.getKeyColumnView() != null) row.getKeyColumnView().removeFromSuperView();
		row.getValueColumnView().removeFromSuperView();
		rows.remove(i);
		layout();
		row.tableViewRowRemoved(this);
	}
	
	public void removeAllRows() {
		for (TableViewRow row : rows) {
			if (row.getKeyColumnView() != null) row.getKeyColumnView().removeFromSuperView();
			row.getValueColumnView().removeFromSuperView();
			row.tableViewRowRemoved(this);
		}
		rows.clear();
		layout();
	}
	
	public int indexOfRow(TableViewRow row) {
		return rows.indexOf(row);
	}
	
	public TableViewRow getRow(int index) {
		return rows.get(index);
	}
	
	public int getRowCount() {
		return rows.size();
	}
	
	public List<TableViewRow> rows() {
		return Collections.unmodifiableList(rows);
	}
	
	@Override
	protected void repaintView(ViewGraphics g) {
		g.setColor(innerGridColor);
		
		final int width = getWidth() - 1;
		int top = outerGridColor != null ? 0 : -1;
		for (int i = 0; i < rows.size(); i++) {
			top += rows.get(i).getValueColumnView().getHeight() + 1;
			if (i < rows.size() - 1) g.drawLine(0, top, width, top);
		}
		
		final int height = top;
		g.drawLine(
				keyColWidth + (outerGridColor != null ? 1 : 0),
				0,
				keyColWidth + (outerGridColor != null ? 1 : 0),
				height);
		
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
			for (TableViewRow row : rows) {
				keyColWidth = Math.max(keyColWidth, row.getKeyColumnPreferredWidth(this));
			}
			
			// layout
			int curTop = outerGridColor != null ? 1 : 0;
			final int keyColLeft = outerGridColor != null ? 1 : 0;
			final int valueColWidth = Math.max(getWidth() - keyColWidth - 1 - keyColLeft * 2, 0);
			for (TableViewRow row : rows) {
				if (row.getKeyColumnView() != null) {
					row.getKeyColumnView().setLeft(keyColLeft);
					row.getKeyColumnView().setWidth(keyColWidth);
				}
				row.getValueColumnView().setLeft(keyColWidth + 1 + keyColLeft);
				row.getValueColumnView().setWidth(valueColWidth);
				int rowHeight = row.getValueColumnView().getPreferredHeight();
				if (row.getKeyColumnView() != null) {
					rowHeight = Math.max(row.getKeyColumnView().getPreferredHeight(), rowHeight);
					row.getKeyColumnView().setTop(curTop);
					row.getKeyColumnView().setHeight(rowHeight);
				}
				row.getValueColumnView().setTop(curTop);
				row.getValueColumnView().setHeight(rowHeight);
				curTop += rowHeight + 1;
			}
			
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
