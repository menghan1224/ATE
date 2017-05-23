package cn.edu.buaa.sei.lmf.navigator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.navigator.SaveablesProvider;

import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.edit.Node;
import cn.edu.buaa.sei.lmf.edit.TreeModel;
import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;
import cn.edu.buaa.sei.lmf.runtime.FileResource;
import cn.edu.buaa.sei.lmf.runtime.FileResourceOccupant;
import cn.edu.buaa.sei.lmf.runtime.LMFPlugin;

public class NavigatorContentProvider extends SaveablesProvider implements ITreeContentProvider, FileResourceOccupant, ILabelProvider, IAdaptable {
	
	private TreeViewer treeViewer;
	private final Map<FileResource, TreeModel> treeModels;
	private final Map<FileResource, SaveableResource> saveables;
	
	public NavigatorContentProvider() {
		treeViewer = null;
		treeModels = new HashMap<FileResource, TreeModel>();
		saveables = new HashMap<FileResource, SaveableResource>();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.treeViewer = (TreeViewer) viewer;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IFile) {
			IFile file = (IFile) parentElement;
			FileResource resource = (FileResource) LMFResource.getResource(file.getFullPath().toString());
			if (resource == null) {
				resource = new FileResource(file, null);
				try {
					resource.load();
				} catch (LMFResourceException ex) {
					resource.unload();
					LMFPlugin.logError(ex, true);
					return new Object[0];
				}
			}
			resource.registerEditor(this);
			TreeModel tree = null;
			if (treeModels.containsKey(resource)) {
				tree = treeModels.get(resource);
			} else {
				tree = new TreeModel(true);
				tree.getContentProvider().inputChanged(treeViewer, null, resource.getRootObject());
				tree.getLabelProvider().addListener(new ILabelProviderListener() {
					@Override
					public void labelProviderChanged(LabelProviderChangedEvent event) {
						treeViewer.update(event.getElement(), null);
					}
				});
				treeModels.put(resource, tree);
				saveables.put(resource, new SaveableResource(resource));
			}
			return tree.getContentProvider().getElements(resource.getRootObject());
		} else if (parentElement instanceof Node) {
			Node node = (Node) parentElement;
			return node.getTree().getContentProvider().getChildren(parentElement);
		} else {
			return new Object[0];
		}
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Node) {
			return ((Node) element).getTree().getContentProvider().getParent(element);
		} else {
			return null;
		}
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Node) {
			return ((Node) element).getTree().getContentProvider().hasChildren(element);
		} else {
			return true;
		}
	}

	@Override
	public void dispose() {
		for (Entry<FileResource, TreeModel> entry : treeModels.entrySet()) {
			entry.getValue().getContentProvider().dispose();
			entry.getValue().getLabelProvider().dispose();
			entry.getKey().unregisterEditor(this);
		}
		treeModels.clear();
		saveables.clear();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		for (Entry<FileResource, TreeModel> entry : treeModels.entrySet()) {
			entry.getValue().getLabelProvider().addListener(listener);
		}
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		for (Entry<FileResource, TreeModel> entry : treeModels.entrySet()) {
			entry.getValue().getLabelProvider().removeListener(listener);
		}
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Node) {
			return ((Node) element).getTree().getLabelProvider().getImage(element);
		} else {
			return null;
		}
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Node) {
			return ((Node) element).getTree().getLabelProvider().getText(element);
		} else if (element instanceof IFile) {
			return ((IFile) element).getName();
		} else {
			return element.toString();
		}
	}
	
	@Override
	public Object[] getElements(Saveable saveable) {
		if (saveable instanceof SaveableResource) {
			return new Object[] { ((SaveableResource) saveable).getResource().getFile() };
		}
		return null;
	} 

	@Override
	public Saveable getSaveable(Object element) {
		if (element instanceof IFile) {
			for (FileResource resource : saveables.keySet()) {
				if (resource.getFile().equals(element)) {
					return saveables.get(resource);
				}
			}
		} else if (element instanceof Node) {
			FileResource resource = (FileResource) ((Node) element).getTree().getRootNode().getObject().resource();
			return saveables.get(resource);
		}
		return null;
	} 

	@Override
	public Saveable[] getSaveables() {
		return saveables.values().toArray(new Saveable[0]);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == SaveablesProvider.class) {
			return this;
		} else {
			return null;
		}
	}
	
	@Override
	public void setDirty(boolean dirty) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (window == null) return;
				final IViewPart view = window.getActivePage().findView(Navigator.ID);
				if (view != null) {
					((Navigator) view).fireSaveabelsChanged();
				}
				IDecoratorManager decoratorManager = LMFEditorPlugin.getDefault().getWorkbench().getDecoratorManager();
				decoratorManager.update("cn.edu.buaa.sei.lmf.navigator.modelDecorator");
			}
		});
		
	}

	@Override
	public void setUnload() {
	}
	
}
