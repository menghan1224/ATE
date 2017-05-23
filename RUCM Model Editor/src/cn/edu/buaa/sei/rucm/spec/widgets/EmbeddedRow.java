package cn.edu.buaa.sei.rucm.spec.widgets;

import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

/**
 * The <code>EmbeddedRow</code> is a single-column row, however, its value-column is embedded with 2-column row.
 * <p>
 * (Strange class... I don't know how to explain it more clearly <strong>:p</strong> perhaps bad design)
 * @author Gong Zhang
 */
public class EmbeddedRow extends SingleColumnRow {
	
	private TableView owner = null;
	private final TableViewRow row;
	private final TableView embeddedTable;
	
	/**
	 * 
	 * @param row the nested 2-column row
	 */
	public EmbeddedRow(final TableViewRow row) {
		this.row = row;
		this.embeddedTable = new TableView() {
			{
				setOuterGridColor(null);
				addRow(row);
			}
		};
		this.embeddedTable.addEventHandler(View.LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				if (owner != null) owner.layout();
			}
		});
	}
	
	public final TableViewRow getEmbeddedRow() {
		return row;
	}

	@Override
	public void tableViewRowAdded(TableView table) {
		owner = table;
	}

	@Override
	public void tableViewRowRemoved(TableView table) {
		embeddedTable.removeAllRows();
		owner = null;
	}

	@Override
	public final View getValueColumnView() {
		return embeddedTable;
	}

}
