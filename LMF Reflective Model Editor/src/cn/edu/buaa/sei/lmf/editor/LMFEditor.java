package cn.edu.buaa.sei.lmf.editor;

import java.util.List;
import java.util.Vector;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import cn.edu.buaa.sei.lmf.edit.TreeModel;

public class LMFEditor extends LMFEditorBase implements ISelectionProvider {
	
	// model
	private final TreeModel treeModel;
	
	// ui
	private TreeViewer treeViewer;
	
	// other
	private final List<ISelectionChangedListener> selectionListeners;

	public LMFEditor() {
		selectionListeners = new Vector<ISelectionChangedListener>();
		treeModel = new TreeModel(true);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		getSite().setSelectionProvider(this);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		treeViewer = new TreeViewer(parent, SWT.NONE);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				for (ISelectionChangedListener listener : selectionListeners) {
					listener.selectionChanged(event);
				}
			}
		});
		
		// init tree model
		treeViewer.setContentProvider(treeModel.getContentProvider());
		treeViewer.setLabelProvider(treeModel.getLabelProvider());
		treeViewer.setInput(getRootObject());
		
		// context menu
		treeModel.createContextMenu(treeViewer.getControl());
	}
	
	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	//// ISelectionProvider Methods ////

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		return treeViewer.getSelection();
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.remove(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		treeViewer.setSelection(selection);
	}
	
	@Override
	public void setUnload() {
		getSite().getPage().closeEditor(this, false);
	}

}
