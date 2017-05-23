package cn.edu.buaa.sei.rucm;

import java.awt.Color;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

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

import ca.carleton.sce.squall.ucmeta.UCDNode;
import ca.carleton.sce.squall.ucmeta.util.Keyword;
import ca.carleton.sce.squall.ucmeta.util.SentencePattern;
import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.runtime.LMFInitializer;
import cn.edu.buaa.sei.rucm.diagram.LinkLayer;
import cn.edu.buaa.sei.rucm.diagram.NodeView;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.LinkMapping;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.NodeMapping;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry;
import cn.edu.buaa.sei.rucm.spec.keywords.KeywordColoring;
import cn.edu.buaa.sei.rucm.spec.keywords.KeywordContributor;

/**
 * The activator class controls the plug-in life cycle
 */
public class RUCMPlugin extends AbstractUIPlugin {
	
	// Preference Key
	public static final String PK_SPEC_EDITOR_FONT_NAME = "specEditor.fontName"; // string
	public static final String PK_SPEC_EDITOR_FONT_SIZE = "specEditor.fontSize"; // int
	public static final String PK_SPEC_EDITOR_TEXTAA = "specEditor.textAA"; // string

	// The plug-in ID
	public static final String PLUGIN_ID = "cn.edu.buaa.sei.rucm"; //$NON-NLS-1$

	// The shared instance
	private static RUCMPlugin plugin;
	
	/**
	 * The constructor
	 */
	public RUCMPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		
		// load keyword contributors
		IExtensionPoint extensionPoint = registry.getExtensionPoint(PLUGIN_ID + ".textCellDecorator");
		IConfigurationElement[] points = extensionPoint.getConfigurationElements();
		
		for (IConfigurationElement point : points) {
			TextCellDecoratorRegistry.registerDecorator(point);
		}
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
	public static RUCMPlugin getDefault() {
		return plugin;
	}
	
	public static void logError(String msg, boolean show) {
		Status status = new Status(IStatus.ERROR, PLUGIN_ID, msg);
		StatusManager.getManager().handle(status, show ? (StatusManager.SHOW | StatusManager.LOG) : (StatusManager.LOG));
	}
	
	public static void logError(Throwable ex, boolean show) {
		Status status = new Status(IStatus.ERROR, PLUGIN_ID, ex.getLocalizedMessage(), ex);
		StatusManager.getManager().handle(status, show ? (StatusManager.SHOW | StatusManager.LOG) : (StatusManager.LOG));
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

	public static class RUCMPluginInitializer implements LMFInitializer {

		@SuppressWarnings("unchecked")
		@Override
		public void initialize() {
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			
			// load keyword contributors
			IExtensionPoint extensionPoint = registry.getExtensionPoint(PLUGIN_ID + ".keywords");
			IConfigurationElement[] points = extensionPoint.getConfigurationElements();
			
			for (IConfigurationElement point : points) {
				try {
					KeywordContributor contributor = (KeywordContributor) point.createExecutableExtension("class");
					Keyword[] keywords = contributor.getExtentedKeywords();
					if (keywords != null) {
						for (Keyword keyword : keywords) {
							Keyword.registerKeyword(keyword);
						}
					}
					Map<Keyword, Color> colorMap = contributor.getKeywordSyntaxColors();
					if (colorMap != null) {
						for (Entry<Keyword, Color> entry : colorMap.entrySet()) {
							KeywordColoring.setColor(entry.getKey(), entry.getValue());
						}
					}
					SentencePattern[] patterns = contributor.getExtendedPatterns();
					if (patterns != null) {
						for (SentencePattern pattern : patterns) {
							SentencePattern.registerPattern(pattern);
						}
					}
				} catch (Exception ex) {
					logError(ex, true);
				}
			}
			
			// initialize diagram
			NotationRegistry.init();
			extensionPoint = registry.getExtensionPoint(PLUGIN_ID + ".notations");
			points = extensionPoint.getConfigurationElements();
			for (IConfigurationElement point : points) {
				try {
					Type type = LMFContext.typeForName(point.getAttribute("typeName"));
					String className = point.getAttribute("class");
					Class<?> clazz = Platform.getBundle(point.getDeclaringExtension().getContributor().getName()).loadClass(className);
					
					if (point.getName().equals("node")) {
						NodeMapping mapping = createNodeMapping(point, type, (Class<? extends NodeView<?>>) clazz);
						if (mapping != null) NotationRegistry.register(mapping);
					} else if (point.getName().equals("link")) {
						LinkMapping mapping = createLinkMapping(point, type, (Class<? extends LinkLayer<?>>) clazz);
						if (mapping != null) NotationRegistry.register(mapping);
					} else {
						throw new IllegalArgumentException("Unknown notation contribution type: " + point.getName());
					}
				} catch (Exception ex) {
					logError(ex, true);
				}
			}
		}
		
		private NodeMapping createNodeMapping(IConfigurationElement point, Type type, Class<? extends NodeView<?>> clazz) {
			try {
				Attribute attr = null;
				Attribute cattr = null;
				IConfigurationElement[] children = point.getChildren("nodeMapping");
				if (children.length == 1) {
					String attrName = children[0].getAttribute("targetAttribute");
					attr = type.getAttribute(attrName);
					if (attr == null) {
						throw new IllegalArgumentException("there is no attribute \"" + attrName + "\" in type \"" + type.getName() + "\"");
					}
					attrName = children[0].getAttribute("containerAttribute");
					if (attrName != null) {
						cattr = attr.getValueType().getAttribute(attrName);
						if (cattr == null) {
							throw new IllegalArgumentException("there is no attribute \"" + attrName + "\" in type \"" + type.getName() + "\"");
						}
					}
				}
				String visibleStr = point.getAttribute("isVisible");
				boolean visible = visibleStr == null ? true : Boolean.parseBoolean(visibleStr);
				return new NodeMapping(point.getAttribute("displayName"), visible, type, clazz, attr, cattr);
			} catch (Exception ex) {
				logError(ex, true);
				return null;
			}
		}
		
		private LinkMapping createLinkMapping(IConfigurationElement point, Type type, Class<? extends LinkLayer<?>> clazz) {
			try {
				Attribute targetAttr = null, attr1 = null, attr2 = null;
				IConfigurationElement[] children = point.getChildren("linkMapping");
				if (children.length == 1) {
					String attrName = children[0].getAttribute("targetAttribute");
					targetAttr = type.getAttribute(attrName);
					if (targetAttr == null) {
						throw new IllegalArgumentException("there is no attribute \"" + attrName + "\" in type \"" + type.getName() + "\"");
					}
					Type valueType = targetAttr.getValueType();
					String attr1Name = children[0].getAttribute("element1Attribute");
					String attr2Name = children[0].getAttribute("element2Attribute");
					attr1 = valueType.getAttribute(attr1Name);
					attr2 = valueType.getAttribute(attr2Name);
					if (attr1 == null) {
						throw new IllegalArgumentException("there is no attribute \"" + attr1Name + "\" in type \"" + valueType.getName() + "\"");
					}
					if (attr2 == null) {
						throw new IllegalArgumentException("there is no attribute \"" + attr2Name + "\" in type \"" + valueType.getName() + "\"");
					}
				}
				Type node1Type = null, node2Type = null;
				String node1TypeName = point.getAttribute("node1Type");
				if (node1TypeName != null && !node1TypeName.isEmpty()) {
					node1Type = LMFContext.typeForName(node1TypeName);
				} else {
					node1Type = LMFContext.typeForName(UCDNode.TYPE_NAME);
				}
				String node2TypeName = point.getAttribute("node2Type");
				if (node2TypeName != null && !node2TypeName.isEmpty()) {
					node2Type = LMFContext.typeForName(node2TypeName);
				} else {
					node2Type = LMFContext.typeForName(UCDNode.TYPE_NAME);
				}
				String directedStr = point.getAttribute("isDirected");
				boolean directed = directedStr == null ? true : Boolean.parseBoolean(directedStr);
				String visibleStr = point.getAttribute("isVisible");
				boolean visible = visibleStr == null ? true : Boolean.parseBoolean(visibleStr);
				return new LinkMapping(point.getAttribute("displayName"), node1Type, node2Type, visible, type, clazz, targetAttr, attr1, attr2, directed);
			} catch (Exception ex) {
				logError(ex, true);
				return null;
			}
		}
		
	}

}
