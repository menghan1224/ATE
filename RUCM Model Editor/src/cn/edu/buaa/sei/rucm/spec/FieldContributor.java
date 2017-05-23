package cn.edu.buaa.sei.rucm.spec;

import cn.edu.buaa.sei.rucm.spec.widgets.TableViewRow;

/**
 * <code>FieldContributor</code> is an interface used to contribute new fields to use case template.
 * Clients should extend "cn.edu.buaa.sei.rucm.fields" extension point and implement this interface for "basicFields" element.
 * 
 * @author Gong Zhang
 *
 */
public interface FieldContributor {
	
	/**
	 * This method will be called before any other methods in this interface get called.
	 * Clients should retain all the needed information from <code>basicSection</code> parameter. For example, retain the 
	 * specification by calling {@link BasicSection#getSpecification()}.
	 * @param basicSection the basic section in use case template
	 */
	public void init(BasicSection basicSection);
	
	/**
	 * Creates extended fields. If {@link #isVisible()} method returns <code>false</code>, this method won't be called.
	 * @return fields as an array
	 */
	public TableViewRow[] createFields();
	
	/**
	 * If the contributed fields are static, always return <code>true</code> from this method.
	 * <p>
	 * Otherwise, return a bool value to indicate the current visibility of the contributed fields. This method will be
	 * called once just after {@link #init(BasicSection)} method. If the return value changed, clients should manually
	 * call {@link BasicSection#notifyFieldVisibilityChange(FieldContributor)} to notify the owner to update the field
	 * visibility.
	 * @return current visibility of the contributed fields
	 */
	public boolean isVisible();
	
	/**
	 * Dispose any resources related to this contributor.
	 */
	public void dispose();

}
