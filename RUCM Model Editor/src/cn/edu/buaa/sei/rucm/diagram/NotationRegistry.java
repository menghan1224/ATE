package cn.edu.buaa.sei.rucm.diagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.Type;

public final class NotationRegistry {

	private NotationRegistry() {
	}
	
	//// Class Fields & Methods ////
	
	private static Map<Type, NodeMapping> nodeMappings;
	private static Map<Type, LinkMapping> linkMappings;
	
	public static void init() {
		nodeMappings = new HashMap<Type, NotationRegistry.NodeMapping>();
		linkMappings = new HashMap<Type, NotationRegistry.LinkMapping>();
	}
	
	public static Collection<NodeMapping> getAllNodeMappings() {
		return Collections.unmodifiableCollection(nodeMappings.values());
	}
	
	public static Collection<LinkMapping> getAllLinkMappings() {
		return Collections.unmodifiableCollection(linkMappings.values());
	}
	
	public static Collection<LinkMapping> getLinkMappings(Type nodeType1, Type nodeType2) {
		NodeMapping nm1 = getNodeMapping(nodeType1);
		NodeMapping nm2 = getNodeMapping(nodeType2);
		List<LinkMapping> rst = new ArrayList<LinkMapping>();
		for (Entry<Type, LinkMapping> entry : linkMappings.entrySet()) {
			LinkMapping mapping = entry.getValue();
			Attribute elAttr1 = mapping.getElement1Attribute();
			Attribute elAttr2 = mapping.getElement2Attribute();
			if (mapping.isDirected()) {
				// directed
				if (nodeType1.isOrIsSubtypeOf(mapping.getNode1Type()) && nodeType2.isOrIsSubtypeOf(mapping.getNode2Type())) {
					if (elAttr1 != null && elAttr2 != null) {
						if (nm1.getTargetAttribute().getValueType().isOrIsSubtypeOf(elAttr1.getValueType()) &&
							nm2.getTargetAttribute().getValueType().isOrIsSubtypeOf(elAttr2.getValueType())) {
							rst.add(mapping);
						}
					} else {
						rst.add(mapping);
					}
				}
			} else {
				// not directed
				if (nodeType1.isOrIsSubtypeOf(mapping.getNode1Type()) && nodeType2.isOrIsSubtypeOf(mapping.getNode2Type())) {
					if (elAttr1 != null && elAttr2 != null) {
						if (nm1.getTargetAttribute().getValueType().isOrIsSubtypeOf(elAttr1.getValueType()) &&
							nm2.getTargetAttribute().getValueType().isOrIsSubtypeOf(elAttr2.getValueType())) {
							rst.add(mapping);
						}
					} else {
						rst.add(mapping);
					}
				} else if (nodeType2.isOrIsSubtypeOf(mapping.getNode1Type()) && nodeType1.isOrIsSubtypeOf(mapping.getNode2Type())) {
					if (elAttr1 != null && elAttr2 != null) {
						if (nm2.getTargetAttribute().getValueType().isOrIsSubtypeOf(elAttr1.getValueType()) &&
							nm1.getTargetAttribute().getValueType().isOrIsSubtypeOf(elAttr2.getValueType())) {
							rst.add(mapping);
						}
					} else {
						rst.add(mapping);
					}
				}
			}
		}
		return Collections.unmodifiableCollection(rst);
	}
	
	public static void register(NodeMapping mapping) {
		nodeMappings.put(mapping.getNodeType(), mapping);
	}
	
	public static void register(LinkMapping mapping) {
		linkMappings.put(mapping.getLinkType(), mapping);
	}
	
	public static NodeMapping getNodeMapping(Type type) {
		return nodeMappings.get(type);
	}
	
	public static LinkMapping getLinkMapping(Type type) {
		return linkMappings.get(type);
	}
	
	public static Class<? extends NodeView<?>> getNodeViewClass(Type type) {
		return getNodeMapping(type).getNodeViewClass();
	}
	
	public static Class<? extends LinkLayer<?>> getLinkLayerClass(Type type) {
		return getLinkMapping(type).getLinkLayerClass();
	}
	
	//// Mapping Object ////
	
	public final static class NodeMapping {
		private final String displayName;
		private final Type nodeType;
		private final Class<? extends NodeView<?>> nodeViewClass;
		private final Attribute targetAttribute;
		private final Attribute containerAttribute;
		private final boolean visible;
		public NodeMapping(String displayName, boolean visible, Type nodeType, Class<? extends NodeView<?>> nodeViewClass, Attribute targetAttribute, Attribute contianerAttribute) {
			if (displayName == null || displayName.isEmpty()) {
				displayName = nodeType.getName();
			}
			this.displayName = displayName;
			this.nodeType = nodeType;
			this.nodeViewClass = nodeViewClass;
			this.targetAttribute = targetAttribute;
			this.containerAttribute = contianerAttribute;
			this.visible = visible;
		}
		public String getDisplayName() {
			return displayName;
		}
		public Type getNodeType() {
			return nodeType;
		}
		public Class<? extends NodeView<?>> getNodeViewClass() {
			return nodeViewClass;
		}
		public Attribute getTargetAttribute() {
			return targetAttribute;
		}
		public Attribute getContainerAttribute() {
			return containerAttribute;
		}
		public boolean isVisible() {
			return visible;
		}
	}
	
	public final static class LinkMapping {
		private final String displayName;
		private final Type linkType;
		private final Type node1Type;
		private final Type node2Type;
		private final Class<? extends LinkLayer<?>> linkLayerClass;
		private final Attribute targetAttribute;
		private final Attribute element1Attribute;
		private final Attribute element2Attribute;
		private final boolean directed;
		private final boolean visible;
		public LinkMapping(String displayName, Type node1Type, Type node2Type, boolean visible, Type linkType, Class<? extends LinkLayer<?>> linkLayerClass, Attribute targetAttribute, Attribute element1Attribute, Attribute element2Attribute, boolean directed) {
			if (displayName == null || displayName.isEmpty()) {
				displayName = linkType.getName();
			}
			this.displayName = displayName;
			this.linkType = linkType;
			this.node1Type = node1Type;
			this.node2Type = node2Type;
			this.linkLayerClass = linkLayerClass;
			this.targetAttribute = targetAttribute;
			this.element1Attribute = element1Attribute;
			this.element2Attribute = element2Attribute;
			this.directed = directed;
			this.visible = visible;
		}
		public boolean isDirected() {
			return directed;
		}
		public String getDisplayName() {
			return displayName;
		}
		public Type getLinkType() {
			return linkType;
		}
		public Type getNode1Type() {
			return node1Type;
		}
		public Type getNode2Type() {
			return node2Type;
		}
		public Class<? extends LinkLayer<?>> getLinkLayerClass() {
			return linkLayerClass;
		}
		public Attribute getTargetAttribute() {
			return targetAttribute;
		}
		public Attribute getElement1Attribute() {
			return element1Attribute;
		}
		public Attribute getElement2Attribute() {
			return element2Attribute;
		}
		public boolean isVisible() {
			return visible;
		}
	}
	
}
