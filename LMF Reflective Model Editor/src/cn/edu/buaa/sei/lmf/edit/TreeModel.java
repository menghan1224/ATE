package cn.edu.buaa.sei.lmf.edit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

import cn.edu.buaa.sei.lmf.ManagedObject;

public class TreeModel {
	
	private ObjectNode rootNode;
	private TreeViewer treeViewer;
	private final List<ILabelProviderListener> labelListeners;
	private final boolean editable;
	
	public TreeModel(boolean editable) {
		this.rootNode = null;
		this.labelListeners = new ArrayList<ILabelProviderListener>();
		this.editable = editable;
	}
	
	public ObjectNode getRootNode() {
		return rootNode;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	private final ITreeContentProvider contentProvider = new ITreeContentProvider() {
		
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			treeViewer = (TreeViewer) viewer;
			if (rootNode != null) {
				rootNode.dispose();
				rootNode = null;
			}
			if (newInput != null) {
				rootNode = new ObjectNode(TreeModel.this, null, (ManagedObject) newInput);
			}
		}
		
		@Override
		public void dispose() {
			if (rootNode != null) {
				rootNode.dispose();
				rootNode = null;
			}
			treeViewer = null;
		}
		
		@Override
		public boolean hasChildren(Object element) {
			return ((Node) element).hasChildren();
		}
		
		@Override
		public Object getParent(Object element) {
			return ((Node) element).getParent();
		}
		
		@Override
		public Object[] getElements(Object inputElement) {
			if (rootNode != null) {
				return new Object[] { rootNode };
			} else {
				return new Object[0];
			}
		}
		
		@Override
		public Object[] getChildren(Object parentElement) {
			return ((Node) parentElement).getChildren().toArray();
		}
	};
	
	private final ILabelProvider labelProvider = new ILabelProvider() {
		
		@Override
		public void removeListener(ILabelProviderListener listener) {
			labelListeners.remove(listener);
		}
		
		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}
		
		@Override
		public void dispose() {
			if (rootNode != null) {
				rootNode.dispose();
				rootNode = null;
			}
			labelListeners.clear();
		}
		
		@Override
		public void addListener(ILabelProviderListener listener) {
			labelListeners.add(listener);
		}
		
		@Override
		public String getText(Object element) {
			return ((Node) element).getText();
		}
		
		@Override
		public Image getImage(Object element) {
			return ((Node) element).getImage();
		}
	};
	
	public ITreeContentProvider getContentProvider() {
		return contentProvider;
	}
	
	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}
	
	void notifyLabelChanged(final Node node) {
		if (treeViewer != null) treeViewer.getTree().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				LabelProviderChangedEvent event = new LabelProviderChangedEvent(labelProvider, node);
				for (ILabelProviderListener listener : labelListeners) {
					listener.labelProviderChanged(event);
				}
			}
		});
	}
	
	void notifyStructureChanged(final Node node) {
		if (treeViewer != null) treeViewer.getTree().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				treeViewer.refresh(node);
			}
		});
	}
	
	public void createContextMenu(Control control) {
		new ContextMenuMaker().createContextMenu(control);
	}

}
