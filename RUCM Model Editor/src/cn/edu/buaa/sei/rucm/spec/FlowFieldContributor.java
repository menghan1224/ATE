package cn.edu.buaa.sei.rucm.spec;

import cn.edu.buaa.sei.rucm.spec.widgets.EmbeddedRow;

/**
 * <code>FlowFieldContributor</code> is an interface used to contribute new fields to flow of events table.
 * Clients should extend "cn.edu.buaa.sei.rucm.fields" extension point and implement this interface for "flowHeaderFields" 
 * or "flowFooterFields" element.
 * 
 * @author Gong Zhang
 *
 */
public interface FlowFieldContributor {
	
	/**
	 * This method will be called before any other methods in this interface get called.
	 * Clients should retain all the needed information from <code>flowTable</code> parameter. For example, retain the 
	 * flow by calling {@link FlowTable#getFlow()}.
	 * @param flowTable the flow table in use case template
	 */
	public void init(FlowTable flowTable);
	
	/**
	 * Creates extended fields. If {@link #isVisible()} method returns <code>false</code>, this method won't be called.
	 * @return fields as an array
	 */
	public EmbeddedRow[] createFields();
	
	/**
	 * If the contributed fields are static, always return <code>true</code> from this method.
	 * <p>
	 * Otherwise, return a bool value to indicate the current visibility of the contributed fields. This method will be
	 * called once just after {@link #init(FlowTable)} method. If the return value changed, clients should manually
	 * call {@link FlowTable#notifyFieldVisibilityChange(FlowFieldContributor)} to notify the owner to update the field
	 * visibility.
	 * @return current visibility of the contributed fields
	 */
	public boolean isVisible();
	
	/**
	 * Dispose any resources related to this contributor.
	 */
	public void dispose();

}
