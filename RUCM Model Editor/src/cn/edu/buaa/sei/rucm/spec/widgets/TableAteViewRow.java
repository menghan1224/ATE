package cn.edu.buaa.sei.rucm.spec.widgets;

import java.util.List;

import co.gongzh.snail.View;

public interface TableAteViewRow {
	/**
	 * This method will be called when this row is added to a {@link TableView}. Clients should initialize the row in
	 * this method.
	 * @param table the owner table
	 */
	public void tableViewRowAdded(TableAteView table);
	//public void tableViewRowAdded(TableAteView table);
	/**
	 * This method will be called when this row is removed from a {@link TableView}. Clients should dispose any resource
	 * allocated by this row.
	 * @param table the owner table
	 */
	public void tableViewRowRemoved(TableAteView table);
	//public void tableViewRowRemoved(TableAteView table);
	/**
	 * Returns the view for key column. The {@link View#getPreferredWidth()} and {@link View#getPreferredHeight()} methods
	 * should be implemented by the returning view so <code>TableView</code> can layout the rows correctly.
	 * <p>
	 * <strong>Note that</strong> this method should always return the same instance all the time, instead of creating
	 * a new view at each call.
	 * <p>
	 * Plus, clients can implement a table with a single column by return <code>null</code> from this method, which means
	 * the row doesn't has a key column.
	 * @return the view for key column or {@code null} if there is no key column
	 * @see #getKeyColumnPreferredWidth(TableView)
	 */
	//public View getKeyColumnView();
	
	/**
	 * Returns the view for value column. The {@link View#getPreferredHeight()} methods
	 * should be implemented by the returning view so <code>TableView</code> can layout the rows correctly.
	 * <p>
	 * <strong>Note that</strong> this method should always return the same instance all the time, instead of creating
	 * a new view at each call.
	 * <p>
	 * To implement a multiple columns table, clients should 
	 * @return the view for value column
	 */
	//public View getValueColumnView();
	
	/**
	 * Retrieves the preferred width of the key column in this row. The owner {@link TableView} will check
	 * all the values returned by this method for each rows in the table, and find the maximum value.  
	 * <p>
	 * If the row doesn't have a key column, clients should return <code>-1</code> from this column.
	 * @param table the owner table
	 * @return the preferred with of key column or {@code -1} if there is no key column
	 * @see #getKeyColumnView()
	 */
	//public int getKeyColumnPreferredWidth(TableView table);
	//public int getKeyColumnPreferredWidth(TableAteView table);
	public List<View> getView();
	public List<TextCell> getTextCellList();
}
