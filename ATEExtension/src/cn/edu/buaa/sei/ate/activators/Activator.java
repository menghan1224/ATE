package cn.edu.buaa.sei.ate.activators;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


public class Activator extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "ATEExtension"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin=new Activator();
	
	/**
	 * The constructor
	 */
	public Activator() {
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
	public static Activator getDefault() {
		return plugin;
	}

	public static Image getBundleImage(String relativeURL) {
		ImageRegistry reg = getDefault().getImageRegistry();
		Image image = reg.get(relativeURL); 
		if (image == null) { 
			URL imageURL = Activator.getDefault().getBundle().getEntry(relativeURL); 
			ImageDescriptor descriptor = ImageDescriptor.createFromURL(imageURL); 
			image = descriptor.createImage(); 
			reg.put(relativeURL, image); 
		}
		return image;
	}

}
