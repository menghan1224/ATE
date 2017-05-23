package cn.edu.buaa.sei.rucm.spec.widgets;

import co.gongzh.snail.View;

/**
 * A single column row that only has a value column.
 * 
 * @author Gong Zhang
 *
 */
public abstract class SingleColumnRow implements TableViewRow {
	
	@Override public final View getKeyColumnView() { return null; }
	@Override public final int getKeyColumnPreferredWidth(TableView table) { return -1; }

}
