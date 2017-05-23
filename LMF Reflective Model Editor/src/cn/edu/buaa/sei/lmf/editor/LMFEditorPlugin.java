package cn.edu.buaa.sei.lmf.editor;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.BundleContext;

import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.TypeFilter;
import cn.edu.buaa.sei.lmf.edit.ContainmentNode;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.lmf.runtime.LMFInitializer;

/**
 * The activator class controls the plug-in life cycle
 */
public class LMFEditorPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "cn.edu.buaa.sei.lmf.editor"; //$NON-NLS-1$

	// The shared instance
	private static LMFEditorPlugin plugin;
	
	/**
	 * The constructor
	 */
	public LMFEditorPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static LMFEditorPlugin getDefault() {
		return plugin;
	}
	
	public static void logError(String msg, boolean show) {
		Status status = new Status(IStatus.ERROR, PLUGIN_ID, msg);
		StatusManager.getManager().handle(status, show ? (StatusManager.LOG | StatusManager.SHOW) : (StatusManager.LOG));
	}
	
	public static void logError(Throwable ex, boolean show) {
		Status status = new Status(IStatus.ERROR, PLUGIN_ID, ex.getLocalizedMessage(), ex);
		StatusManager.getManager().handle(status, show ? (StatusManager.LOG | StatusManager.SHOW) : (StatusManager.LOG));
	}
	
	public static Image getBundleImage(String relativeURL) {
		ImageRegistry reg = getDefault().getImageRegistry();
		Image image = reg.get(relativeURL); 
		if (image == null) { 
			URL imageURL = getDefault().getBundle().getEntry(relativeURL); 
			ImageDescriptor descriptor = ImageDescriptor.createFromURL(imageURL); 
			image = descriptor.createImage(); 
			reg.put(relativeURL, image); 
		}
		return image;
	}
	
	public static class LMFEditorPluginInitializer implements LMFInitializer {

		@Override
		public void initialize() {
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			
			// load object node descriptor
			ObjectNode.initNodeDecoratorMap();
			
			IExtensionPoint extensionPoint = null;
			IConfigurationElement[] points = null;
			
			// load preferred child type
			extensionPoint = registry.getExtensionPoint(PLUGIN_ID + ".preferredSubNode");
			points = extensionPoint.getConfigurationElements();
			for (IConfigurationElement point : points) {
				try {
					String typeName = point.getAttribute("typeName");
					String attrName = point.getAttribute("attributeName");
					final Type type = LMFContext.typeForName(typeName);
					Set<Type> preferred = new HashSet<Type>();
					
					for (IConfigurationElement subpoint : point.getChildren()) {
						String childTypeName = subpoint.getAttribute("typeName");
						final Type target = LMFContext.typeForName(childTypeName);
						preferred.addAll(LMFContext.listTypes(new TypeFilter() {
							@Override
							public boolean accept(Type type) {
								if (type == target) return true;
								else if (type.isSubtypeOf(target)) return true;
								else return false;
							}
						}));
					}
					
					List<Type> targetTypes = LMFContext.listTypes(new TypeFilter() {
						@Override
						public boolean accept(Type t) {
							if (t == type) return true;
							else if (t.isSubtypeOf(type)) return true;
							else return false;
						}
					});
					for (Type t : targetTypes) {
						ContainmentNode.registerPreferredChildTypes(t.getAttribute(attrName), preferred);
					}
					
				} catch (Exception ex) {
					logError(ex, true);
				}
			}
		}
		
	}

}
