package cn.edu.buaa.sei.rucm.spec;

import cn.edu.buaa.sei.rucm.spec.widgets.SectionView;

/**
 * <code>SectionContributor</code> is an interface used to contribute new section to use case template.
 * Clients should extend "cn.edu.buaa.sei.rucm.sections" extension point and implement this interface for "section" element.
 * 
 * @author Gong Zhang
 *
 */
public interface SectionContributor {

	/**
	 * This method will be called before any other methods in this interface get called.
	 * Clients should retain all the needed information from <code>specView</code> parameter. For example, retain the 
	 * specification by calling {@link SpecificationView#getSpecification()}.
	 * @param specView
	 */
	public void init(SpecificationView specView);
	
	/**
	 * Creates extended section. If {@link #isVisible()} method returns <code>false</code>, this method won't be called.
	 * @return fields as an array
	 */
	public SectionView createSection();
	
	/**
	 * If the contributed section are static, always return <code>true</code> from this method.
	 * <p>
	 * Otherwise, return a bool value to indicate the current visibility of the contributed section. This method will be
	 * called once just after {@link #init(SpecificationView)} method. If the return value changed, clients should manually
	 * call {@link SpecificationView#notifySectionVisibilityChange(SectionContributor)} to notify the owner to update the section
	 * visibility.
	 * @return current visibility of the contributed section
	 */
	public boolean isVisible();
	
	/**
	 * Dispose any resources related to this contributor.
	 */
	public void dispose();

}
