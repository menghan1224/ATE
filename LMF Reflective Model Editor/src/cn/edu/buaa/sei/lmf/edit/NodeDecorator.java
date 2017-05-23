package cn.edu.buaa.sei.lmf.edit;

import org.eclipse.swt.graphics.Image;

import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;

/**
 * This interface should be used with "nodeDecorator" extension point to determine
 * the label and icon of a node in Model Explorer.
 * 
 * @author Gong Zhang
 */
public interface NodeDecorator {
	
	/**
	 * Return a group of property name that will affect the label and icon. So the model editor
	 * can automatically update the label and icon when one of the property changes.
	 * <p>
	 * For example, the name of use case will be displayed in the label. This method should return:
	 * <p>
	 * <code>new String[] { "name" };</code>
	 * <p>
	 * or
	 * <p>
	 * <code>new String[] { UseCase.KEY_NAME };</code>
	 * <p>
	 * 
	 * @return a group of property name that will affect the label and icon
	 */
	public String[] getLabelRelatedKeys();
	
	/**
	 * Returns the textual label of the given object.
	 * @param object
	 * @return the textual label
	 */
	public String getLabel(ManagedObject object);
	
	/**
	 * Returns the icon of given object. To create an image, see {@link org.eclipse.jface.resource.ImageDescriptor}.
	 * @param object
	 * @return the icon
	 * @see org.eclipse.jface.resource.ImageDescriptor
	 */
	public Image getImage(ManagedObject object);
	
	/**
	 * Returns the icon of given type. To create an image, see {@link org.eclipse.jface.resource.ImageDescriptor}.
	 * @param type
	 * @return the icon
	 * @see org.eclipse.jface.resource.ImageDescriptor
	 */
	public Image getImage(Type type);
	
}
