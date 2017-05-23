package cn.edu.buaa.sei.rucm.spec;

import ca.carleton.sce.squall.ucmeta.BoundedAlternative;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.rucm.spec.rows.BoundedRFSRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

public class BoundedAltFlowTable extends FlowTable {

	public BoundedAltFlowTable(String caption, BoundedAlternative flow, UseCaseSpecification specification) {
		super(caption, flow, specification);
	}
	
	@Override
	public BoundedAlternative getFlow() {
		return (BoundedAlternative) super.getFlow();
	}
	
	@Override
	protected void createHeaderRows(TableView container) {
		container.addRow(new BoundedRFSRow(getFlow()));
	}

}
