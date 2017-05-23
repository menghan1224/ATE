package cn.edu.buaa.sei.lmf.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.BundleContext;

import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.ManagedObjectImpl;
import cn.edu.buaa.sei.lmf.TypeBuilder;
import cn.edu.buaa.sei.lmf.TypeLoader;

/**
 * The activator class controls the plug-in life cycle
 */
public class LMFPlugin extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "cn.edu.buaa.sei.lmf.runtime"; //$NON-NLS-1$

	// The shared instance
	private static LMFPlugin plugin;
	
	/**
	 * The constructor
	 */
	public LMFPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		int loader_cnt = 0;
		
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		
		// load meta-model
		IExtensionPoint extensionPoint = registry.getExtensionPoint(PLUGIN_ID + ".metamodel");
		IConfigurationElement[] points = extensionPoint.getConfigurationElements();
		Map<String, DependencyTypeLoader> loaders = new HashMap<String, DependencyTypeLoader>();
		for (IConfigurationElement point : points) {
			try {
				TypeLoader loader = (TypeLoader) point.createExecutableExtension("class");
				String className = point.getAttribute("class");
				DependencyTypeLoader depLoader = new DependencyTypeLoader(loader, className);
				for (IConfigurationElement dependency : point.getChildren()) {
					depLoader.addDependency(dependency.getAttribute("class"));
				}
				loaders.put(depLoader.getClassName(), depLoader);
			} catch (Exception ex) {
				logError(ex, true);
			}
		}
		while (!loaders.isEmpty()) {
			DependencyTypeLoader loader = loaders.remove(loaders.keySet().iterator().next());
			loader_cnt += loadTypes(loader, loaders);
		}
		LMFContext.pack();
		
		// initializers
		extensionPoint = registry.getExtensionPoint(PLUGIN_ID + ".initializer");
		points = extensionPoint.getConfigurationElements();
		for (IConfigurationElement point : points) {
			try {
				LMFInitializer initializer = (LMFInitializer) point.createExecutableExtension("class");
				initializer.initialize();
			} catch (Exception ex) {
				logError(ex, true);
			}
		}
		
		Status status = new Status(IStatus.INFO, PLUGIN_ID, String.format("Totally %d type loaders have been loaded.", loader_cnt));
		StatusManager.getManager().handle(status, StatusManager.LOG);
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
	public static LMFPlugin getDefault() {
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
	
	private static int loadTypes(DependencyTypeLoader loader, Map<String, DependencyTypeLoader> loaders) {
		int cnt = 0;
		for (String dependency : loader.getDependency()) {
			if (loaders.containsKey(dependency)) {
				DependencyTypeLoader dep = loaders.remove(dependency);
				cnt += loadTypes(dep, loaders);
			}
 		}
		try {
			LMFContext.load(loader);
			cnt++;
		} catch (Exception ex) {
			logError(ex, true);
		}
		loaders.remove(loader.getClassName());
		return cnt;
	}

	@Override
	public void earlyStartup() {
	}

}

class DependencyTypeLoader implements TypeLoader {
	
	private final TypeLoader target;
	private final String className;
	private final List<String> dependency;
	
	DependencyTypeLoader(TypeLoader target, String className) {
		this.target = target;
		this.className = className;
		this.dependency = new ArrayList<String>();
	}
	
	public TypeLoader getTarget() {
		return target;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void addDependency(String className) {
		dependency.add(className);
	}
	
	public List<String> getDependency() {
		return dependency;
	}

	@Override
	public Set<TypeBuilder> loadTypes(Map<String, TypeBuilder> existingTypes) {
		return target.loadTypes(existingTypes);
	}

	@Override
	public Map<String, Class<? extends ManagedObjectImpl>> loadImplementationClasses() {
		return target.loadImplementationClasses();
	}
	
}
