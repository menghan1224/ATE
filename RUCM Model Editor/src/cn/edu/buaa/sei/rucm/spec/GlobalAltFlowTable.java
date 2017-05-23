package cn.edu.buaa.sei.rucm.spec;

import cn.edu.buaa.sei.rucm.spec.rows.GuardConditionRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;
import ca.carleton.sce.squall.ucmeta.GlobalAlternative;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;

public class GlobalAltFlowTable extends FlowTable {

	public GlobalAltFlowTable(String caption, GlobalAlternative flow, UseCaseSpecification specification) {
		super(caption, flow, specification);
	}
	
	@Override
	public GlobalAlternative getFlow() {
		return (GlobalAlternative) super.getFlow();
	}
	
	@Override
	protected void createHeaderRows(TableView container) {
		container.addRow(new GuardConditionRow(getFlow()));
	}

}
