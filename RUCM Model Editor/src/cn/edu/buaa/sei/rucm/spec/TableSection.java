package cn.edu.buaa.sei.rucm.spec;

import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.rucm.spec.widgets.DropShadow;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public class TableSection extends SectionView {
	
	private final UseCaseSpecification specification;
	
	private final TableView table;
	
	public TableSection(UseCaseSpecification spec) {
		this.specification = spec;
		
		// initialize table
		table = new TableView() {
			@Override
			protected int getMinimumKeyColumnWidth() {
				return SpecificationView.KEY_COL_PREFERRED_WIDTH;
			}
		};
		DropShadow.createDropShadowOn(table);
		addSubview(table);
		table.addEventHandler(LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				layoutSection();	
			}
		});
	}
	
	public final TableView getTable() {
		return table;
	}
	
	public final UseCaseSpecification getSpecification() {
		return specification;
	}
	
	@Override
	protected void dispose() {
		table.removeAllRows();
		super.dispose();
	}

}
