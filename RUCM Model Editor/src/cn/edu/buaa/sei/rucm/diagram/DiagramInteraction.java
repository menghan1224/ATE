package cn.edu.buaa.sei.rucm.diagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import ca.carleton.sce.squall.ucmeta.UCDContainer;
import ca.carleton.sce.squall.ucmeta.UCDNode;
import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.ManagedList;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Primitives;
import cn.edu.buaa.sei.rucm.diagram.DiagramSelection.SelectionType;
import cn.edu.buaa.sei.rucm.diagram.NotationRegistry.NodeMapping;
import cn.edu.buaa.sei.rucm.diagram.SelectionLayers.ContainerContentBorder;
import cn.edu.buaa.sei.rucm.diagram.SelectionLayers.DashLineLinkLayer;
import cn.edu.buaa.sei.rucm.diagram.SelectionLayers.SelectionArea;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.layer.Layer;
import co.gongzh.snail.util.Rectangle;
import co.gongzh.snail.util.Vector2D;

// XXX: really really bad code :p (but it works...)
public class DiagramInteraction {
	
	private final DiagramView diagramView;
	private final ContextMenu contextMenu;
	
	public DiagramInteraction(DiagramView diagramView) {
		this.diagramView = diagramView;
		this.contextMenu = new ContextMenu(diagramView);
		initHandlers();
	}
	
	private void initHandlers() {
		// determine selection
		EventHandler preMouseHandler = new EventHandler() {
			
			boolean shouldHandle = false;
			Vector2D p0;
			Vector2D node_p0;
			SelectionArea selectionArea = null;
			
			// drag to container...
			ContainerView<?> containerView;
			ContainerView<?> targetContainer;
			ContainerContentBorder border;
			
			@Override
			public void handle(View sender, Key key, Object arg) {
				MouseEvent e = (MouseEvent) arg;
				if (e.getButton() != java.awt.event.MouseEvent.BUTTON1 ||
					e.getAWTEvent().isAltDown()) {
					shouldHandle = false;
					return;
				}
				
				Vector2D point = e.getPosition(diagramView);
				if (key == View.PRE_MOUSE_PRESSED) {
					
					if (diagramView.hudContainer.getLayerAtPoint(point) == null) {
						
						p0 = point.clone();
						node_p0 = null;

						LinkLayer<?> linkLayer = getLinkLayerAtPoint(point);
						NodeView<?> nodeView = getNodeViewAtPoint(point);
						if (linkLayer != null) {
							// select a link
							diagramView.setSelectionInternal(new DiagramSelection(linkLayer.getModel()));
							shouldHandle = true;
							e.handle();
						} else if (nodeView != null) {
							
							if (nodeView != null) {
								containerView = nodeView.getContainerView();
								targetContainer = nodeView.getContainerView();
								border = null;
							}
							
							// select a node
							List<UCDNode> selected = new ArrayList<UCDNode>();
							if (diagramView.getSelection().getType() == SelectionType.NODE ||
								diagramView.getSelection().getType() == SelectionType.MULTIPLE_NODES) {
								selected.addAll(Arrays.asList(diagramView.getSelection().getNodes()));
							}
							
							if (e.getAWTEvent().isShiftDown() || e.getAWTEvent().isControlDown() || e.getAWTEvent().isMetaDown()) {
								if (!selected.remove(nodeView.getModel())) {
									selected.add(nodeView.getModel());
								}
								diagramView.setSelectionInternal(new DiagramSelection(selected));
								diagramView.requestKeyboardFocus();
								shouldHandle = true;
								e.handle();
							} else if (!selected.contains(nodeView.getModel())) {
								diagramView.setSelectionInternal(new DiagramSelection(nodeView.getModel()));
								diagramView.requestKeyboardFocus();
								node_p0 = Vector2D.make(nodeView.getModel().getLeft(), nodeView.getModel().getTop());
								shouldHandle = true;
								e.handle();
							} else {
								shouldHandle = false;
							}
						} else {
							// clear selection
							diagramView.setSelectionInternal(null);
							diagramView.requestKeyboardFocus();
							shouldHandle = true;
							e.handle();
						}
					} else {
						shouldHandle = false;
					}
					
				} else if (key == View.PRE_MOUSE_DRAGGED) {
					
					if (shouldHandle) {
						DiagramSelection sel = diagramView.getSelection();
						if (sel.getType() == SelectionType.EMPTY) {
							// start to draw selection area
							if (selectionArea == null) {
								selectionArea = new SelectionArea();
								diagramView.hudContainer.addLayer(selectionArea);
							}
							selectionArea.setArea(p0, point);
							diagramView.setSelectionInternal(new DiagramSelection(getNodeViewsInArea(selectionArea.getRectangle())));
						} else if (selectionArea != null) {
							// selecting nodes...
							selectionArea.setArea(p0, point);
							diagramView.setSelectionInternal(new DiagramSelection(getNodeViewsInArea(selectionArea.getRectangle())));
						} else if (node_p0 != null && sel.getType() == SelectionType.NODE) {
							UCDNode node = sel.getNode();
							Vector2D delta = Vector2D.subtract(point, p0);
							Vector2D pos = Vector2D.add(node_p0, delta);
							node.setLeft(pos.x);
							node.setTop(pos.y);
							
							// highlight target container
							NodeView<?> nodeView = diagramView.getNodeView(node);
							ContainerView<?> newTargetContainer = getContainerViewAtPoint(point, nodeView);
							if (newTargetContainer != containerView && newTargetContainer != targetContainer) {
								// drag to new container
								targetContainer = newTargetContainer;
								if (border != null) diagramView.hudContainer.removeLayer(border);
								border = new ContainerContentBorder(targetContainer);
								diagramView.hudContainer.addLayer(border);
							} else if (newTargetContainer == containerView && targetContainer != containerView) {
								// drag back to old container
								targetContainer = containerView;
								diagramView.hudContainer.removeLayer(border);
								border = null;
							}
						}
						e.handle();
					}
					
				} else if (key == View.PRE_MOUSE_RELEASED) {
					
					if (shouldHandle) {
						
						if (selectionArea != null) {
							// remove selection area
							diagramView.hudContainer.removeLayer(selectionArea);
							selectionArea = null;
						}
						
						DiagramSelection sel = diagramView.getSelection();
						if (sel.getType() == SelectionType.NODE) {
							if (targetContainer != containerView) {
								// drag to new container
								changeNodeContainer(diagramView.getNodeView(sel.getNode()), containerView, targetContainer);
								if (border != null) diagramView.hudContainer.removeLayer(border);
								border = null;
							}
						}
						
						e.handle();
					}
					
				}
			}
		};
		diagramView.addEventHandler(View.PRE_MOUSE_PRESSED, preMouseHandler);
		diagramView.addEventHandler(View.PRE_MOUSE_RELEASED, preMouseHandler);
		diagramView.addEventHandler(View.PRE_MOUSE_DRAGGED, preMouseHandler);
		
		// drag to move
		EventHandler postMouseHandler = new EventHandler() {
			
			boolean shouldHandle = false;
			Vector2D p0;
			Vector2D[] node_p0;
			
			// drag to container...
			ContainerView<?> containerView;
			ContainerView<?> targetContainer;
			ContainerContentBorder border;
			
			@Override
			public void handle(View sender, Key key, Object arg) {
				MouseEvent e = (MouseEvent) arg;
				if (e.getButton() != java.awt.event.MouseEvent.BUTTON1 ||
					e.getAWTEvent().isAltDown()) {
					shouldHandle = false;
					return;
				}
				
				Vector2D point = e.getPosition(diagramView);
				if (key == View.MOUSE_PRESSED) {
					
						p0 = point.clone();
						NodeView<?> nodeView = getNodeViewAtPoint(point);
						
						if (nodeView != null) {
							containerView = nodeView.getContainerView();
							targetContainer = nodeView.getContainerView();
							border = null;
						}
						
						if (nodeView != null) {
							
							// select a node
							List<UCDNode> selected = new ArrayList<UCDNode>();
							if (diagramView.getSelection().getType() == SelectionType.NODE ||
								diagramView.getSelection().getType() == SelectionType.MULTIPLE_NODES) {
								selected.addAll(Arrays.asList(diagramView.getSelection().getNodes()));
							}
							if (selected.contains(nodeView.getModel())) {
								node_p0 = new Vector2D[selected.size()];
								for (int i = 0; i < node_p0.length; i++) {
									UCDNode node = selected.get(i);
									node_p0[i] = Vector2D.make(node.getLeft(), node.getTop());
								}
								e.handle();
								shouldHandle = true;
								return;
							}
						}
						shouldHandle = false;
					
				} else if (key == View.MOUSE_DRAGGED) {
					
					if (shouldHandle) {
						DiagramSelection sel = diagramView.getSelection();
						if (sel.getType() == SelectionType.MULTIPLE_NODES) {
							// drag multiple nodes
							UCDNode[] nodes = sel.getNodes();
							Vector2D delta = Vector2D.subtract(point, p0);
							for (int i = 0; i < nodes.length; i++) {
								Vector2D pos = Vector2D.add(node_p0[i], delta);
								nodes[i].setLeft(pos.x);
								nodes[i].setTop(pos.y);
							}
						} else if (sel.getType() == SelectionType.NODE) {
							// drag a single node
							UCDNode node = sel.getNode();
							Vector2D delta = Vector2D.subtract(point, p0);
							Vector2D pos = Vector2D.add(node_p0[0], delta);
							node.setLeft(pos.x);
							node.setTop(pos.y);
							
							// highlight target container
							NodeView<?> nodeView = diagramView.getNodeView(node);
							ContainerView<?> newTargetContainer = getContainerViewAtPoint(point, nodeView);
							if (newTargetContainer != containerView && newTargetContainer != targetContainer) {
								// drag to new container
								targetContainer = newTargetContainer;
								if (border != null) diagramView.hudContainer.removeLayer(border);
								border = new ContainerContentBorder(targetContainer);
								diagramView.hudContainer.addLayer(border);
							} else if (newTargetContainer == containerView && targetContainer != containerView) {
								// drag back to old container
								targetContainer = containerView;
								diagramView.hudContainer.removeLayer(border);
								border = null;
							}
						}
						e.handle();
					}
					
				} else if (key == View.MOUSE_RELEASED) {
					
					if (shouldHandle) {
						DiagramSelection sel = diagramView.getSelection();
						if (sel.getType() == SelectionType.NODE) {
							if (targetContainer != containerView) {
								// drag to new container
								changeNodeContainer(diagramView.getNodeView(sel.getNode()), containerView, targetContainer);
								if (border != null) diagramView.hudContainer.removeLayer(border);
								border = null;
							}
						}
						e.handle();
					}
					
				}
			}
		};
		diagramView.addEventHandler(View.MOUSE_PRESSED, postMouseHandler);
		diagramView.addEventHandler(View.MOUSE_RELEASED, postMouseHandler);
		diagramView.addEventHandler(View.MOUSE_DRAGGED, postMouseHandler);
		
		// draw a line
		EventHandler rightClickHander = new EventHandler() {
			boolean shouldHandle = false;
			Vector2D p0;
			boolean click;
			DashLineLinkLayer dashLine = null;
			
			@Override
			public void handle(View sender, Key key, Object arg) {
				MouseEvent e = (MouseEvent) arg;
				if (key == View.MOUSE_PRESSED) {
					if ((e.getButton() != java.awt.event.MouseEvent.BUTTON3 && !e.getAWTEvent().isAltDown())) {
						shouldHandle = false;
						return;
					} else {
						shouldHandle = true;
					}
				}
				if (!shouldHandle) {
					shouldHandle = false;
					return;
				}
				
				Vector2D point = e.getPosition(diagramView);
				if (key == View.MOUSE_PRESSED) {
					p0 = point.clone();
					shouldHandle = true;
					click = true;
					dashLine = null;
					
					final LinkLayer<?> linkLayer = getLinkLayerAtPoint(point);
					final NodeView<?> nodeView = getNodeViewAtPoint(point);
					
					if (linkLayer != null) {
						diagramView.setSelectionInternal(new DiagramSelection(linkLayer.getModel()));
					} else if (nodeView == null) {
						diagramView.setSelectionInternal(new DiagramSelection());
					} else {
						DiagramSelection sel = diagramView.getSelection();
						if (sel.getType() == SelectionType.NODE || sel.getType() == SelectionType.MULTIPLE_NODES) {
							if (!Arrays.asList(sel.getNodes()).contains(nodeView.getModel())) {
								diagramView.setSelectionInternal(new DiagramSelection(nodeView.getModel()));
							}
						} else {
							diagramView.setSelectionInternal(new DiagramSelection(nodeView.getModel()));
						}
					}
					
					e.handle();
				} else if (key == View.MOUSE_DRAGGED) {
					if (shouldHandle) {
						Vector2D delta = Vector2D.subtract(point, p0);
						if (dashLine == null && delta.x * delta.x + delta.y * delta.y > 64 &&
							diagramView.getSelection().getType() == SelectionType.NODE) {
							// dragging
							dashLine = new DashLineLinkLayer(p0);
							dashLine.setP2(point);
							diagramView.hudContainer.addLayer(dashLine);
							click = false;
						} else if (dashLine != null) {
							dashLine.setP2(point);
						}
						e.handle();
					}
				} else if (key == View.MOUSE_RELEASED) {
					if (shouldHandle) {
						if (click) {
							// context menu
							contextMenu.popup(sender.getViewContext(), diagramView.transformPointToRootView(point));
						} else {
							// finish dragging
							final NodeView<?> nodeView2 = getNodeViewAtPoint(point);
							if (nodeView2 != null) {
								final UCDNode node1 = diagramView.getSelection().getNode();
								final UCDNode node2 = nodeView2.getModel();
								diagramView.setSelectionInternal(new DiagramSelection(Arrays.asList(node1, node2)));
								final Layer layer1 = dashLine;
								EventHandler cleanup = new EventHandler() {
									@Override
									public void handle(View sender, Key key, Object arg) {
										diagramView.hudContainer.removeLayer(layer1);
										diagramView.setSelectionInternal(null);
										contextMenu.removeEventHandler(ContextMenu.CONTEXT_MENU_DISMISSED, this);
									}
								};
								contextMenu.addEventHandler(ContextMenu.CONTEXT_MENU_DISMISSED, cleanup);
								contextMenu.popup(sender.getViewContext(), diagramView.transformPointToRootView(point));
								dashLine = null;
							}  else {
								// clean up
								diagramView.hudContainer.removeLayer(dashLine);
								dashLine = null;
								diagramView.setSelectionInternal(null);
							}
						}
						e.handle();
						shouldHandle = false;
					}
				}
			}
		};
		diagramView.addEventHandler(View.MOUSE_PRESSED, rightClickHander);
		diagramView.addEventHandler(View.MOUSE_RELEASED, rightClickHander);
		diagramView.addEventHandler(View.MOUSE_DRAGGED, rightClickHander);
	}
	
	private NodeView<?> getNodeViewAtPoint(Vector2D point) {
		View[] views = diagramView.getSubviewHierachyAtPoint(point);
		for (int i = views.length - 1; i >= 0; i--) {
			if (views[i] instanceof NodeView<?>) return (NodeView<?>) views[i];
		}
		return null;
	}
	
	private static final void getViewHierachyAtPoint(View view, Vector2D p, List<View> dstList, View except) {
		ListIterator<View> it = view.listIterator(view.count());
		while (it.hasPrevious()) {
			View child = it.previous();
			if (!child.isHidden()) { 
				Vector2D p1 = child.transformPointFromSuperView(p);
				if (child.isInside(p1)) {
					if (child != except) {
						dstList.add(child);
						getViewHierachyAtPoint(child, p1, dstList, except);
						return;
					}
				}
			}
		}
	}
	
	private ContainerView<?> getContainerViewAtPoint(Vector2D point, View except) {
		List<View> list = new ArrayList<View>();
		getViewHierachyAtPoint(diagramView, point, list, except);
		for (int i = list.size() - 1; i >= 0; i--) {
			if (list.get(i) instanceof ContainerView<?>) return (ContainerView<?>) list.get(i);
		}
		return diagramView;
	}
	
	private LinkLayer<?> getLinkLayerAtPoint(Vector2D point) {
		return (LinkLayer<?>) diagramView.linkContainer.getLayerAtPoint(point);
	}
	
	private List<UCDNode> getNodeViewsInArea(Rectangle rect) {
		List<UCDNode> result = new ArrayList<UCDNode>();
		for (UCDNode node : diagramView.getModel().getNodes()) {
			NodeView<?> view = diagramView.getNodeView(node);
			Vector2D pos = view.getPositionInDiagramView();
			Vector2D size = view.getSize();
			if (pos.x + size.x > rect.origin.x && pos.x < rect.origin.x + rect.size.x &&
				pos.y + size.y > rect.origin.y && pos.y < rect.origin.y + rect.size.y) {
				result.add(node);
			}
		}
		return result;
	}
	
	private void changeNodeContainer(NodeView<?> nodeView, ContainerView<?> oldContainerView, ContainerView<?> newContainerView) {
		final UCDContainer oldContainer = oldContainerView.getModel();
		final UCDContainer newContainer = newContainerView.getModel();
		final UCDNode node = nodeView.getModel();
		
		Vector2D loc = View.transformPoint(Vector2D.make(), nodeView, diagramView);
		loc = View.transformPoint(loc, diagramView, newContainerView.getContentView());
		
		NodeMapping mapping = NotationRegistry.getNodeMapping(node.type());
		if (mapping.getTargetAttribute() != null) {
			ManagedObject obj = node.get(mapping.getTargetAttribute().getName());
			if (obj != null) {
				// check target
				mapping = NotationRegistry.getNodeMapping(newContainer.type());
				if (mapping.getTargetAttribute() != null) {
					ManagedObject target = newContainer.get(mapping.getTargetAttribute().getName());
					if (target != null && mapping.getContainerAttribute() != null) {
						ManagedList targetList = target.get(mapping.getContainerAttribute().getName()).listContent();
						if (obj.type().isOrIsSubtypeOf(targetList.getContentType())) {
							// perform the owner change
							detachObject(obj);
							targetList.add(obj);
						} else {
							return; // incompatible container
						}
					} else {
						return; // missing target
					}
				} else {
					return; // target is not a container
				}
			} else {
				return; // missing object
			}
		}
		
		oldContainer.getNodes().remove(node);
		newContainer.getNodes().add(node);
		node.setLeft(loc.x);
		node.setTop(loc.y);
	}
	
	private static void detachObject(ManagedObject obj) {
		ManagedObject owner = obj.owner();
		if (owner != null) {
			for (Attribute attr : owner.type().getAttributes()) {
				if (!attr.isContainment()) continue;
				if (attr.getValueType() == Primitives.LIST && obj.type().isOrIsSubtypeOf(attr.getValueTypeParameter())) {
					ManagedList list = owner.get(attr.getName()).listContent();
					if (list.contains(obj)) {
						list.remove(obj);
						return;
					}
				} else if (obj.type().isOrIsSubtypeOf(attr.getValueType())) {
					if (owner.get(attr.getName()) == obj) {
						owner.set(attr.getName(), (ManagedObject) null);
					}
				}
			}
		}
	}

}
