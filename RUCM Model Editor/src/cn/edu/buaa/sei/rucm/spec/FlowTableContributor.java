package cn.edu.buaa.sei.rucm.spec;

import ca.carleton.sce.squall.ucmeta.FlowOfEvents;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;

/**
 * This interface is used to contribute new type of flow table for a flow of events.
 * Clients should extend "cn.edu.buaa.sei.rucm.flowTable" extension point and implement this interface for "flowTable" element.
 * 
 * @author Gong Zhang
 *
 */
public interface FlowTableContributor {
	
	/**
	 * Creates the customized table for specified flow.
	 * @param flow
	 * @param spec
	 * @return
	 */
	public FlowTable createTableForFlow(FlowOfEvents flow, UseCaseSpecification spec);

}
