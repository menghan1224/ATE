package cn.edu.buaa.sei.rucm.spec;

import ca.carleton.sce.squall.ucmeta.SpecificAlternative;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.rucm.spec.rows.SpecificRFSRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

public class SpecificAltFlowTable extends FlowTable {

	public SpecificAltFlowTable(String caption, SpecificAlternative flow, UseCaseSpecification specification) {
		super(caption, flow, specification);
	}
	
	@Override
	public SpecificAlternative getFlow() {
		return (SpecificAlternative) super.getFlow();
	}
	
	@Override
	protected void createHeaderRows(TableView container) {
		container.addRow(new SpecificRFSRow(getFlow()));
	}

}
