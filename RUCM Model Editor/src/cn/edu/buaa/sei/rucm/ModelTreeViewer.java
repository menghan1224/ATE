package cn.edu.buaa.sei.rucm;

import java.util.List;
import java.util.Vector;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ca.carleton.sce.squall.ucmeta.UCModel;
import cn.edu.buaa.sei.lmf.edit.TreeModel;
import cn.edu.buaa.sei.lmf.editor.LMFEditorBase;

public class ModelTreeViewer extends ViewPart implements ISelectionProvider {
	
	private TreeModel treeModel;
	private TreeViewer treeViewer;
	private final List<ISelectionChangedListener> selectionListeners;
	
	private LMFEditorBase currentEditor;
	
	private final IPartListener partListener = new IPartListener() {
		
		@Override public void partOpened(IWorkbenchPart part) {}
		@Override public void partDeactivated(IWorkbenchPart part) {}
		@Override public void partBroughtToTop(IWorkbenchPart part) {}
		
		@Override
		public void partClosed(IWorkbenchPart part) {
			if (part == currentEditor) {
				currentEditor = null;
				clearInputModel();
			}
		}
		
		@Override
		public void partActivated(IWorkbenchPart part) {
			if (part != currentEditor && part instanceof LMFEditorBase) {
				LMFEditorBase editor = (LMFEditorBase) part;
				if (editor.getRootObject() != null &&
					editor.getRootObject().isKindOf(UCModel.TYPE_NAME)) {
					currentEditor = editor;
					resetInputModel();
				}
			}
		}
		
	};

	public ModelTreeViewer() {
		currentEditor = null;
		treeModel = null;
		treeViewer = null;
		selectionListeners = new Vector<ISelectionChangedListener>();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(1, true);
		parent.setLayout(layout);
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("This view is deprecated. Please use new Model Explorer.");
		label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		Button button = new Button(parent, SWT.NONE);
		button.setText("Open Model Explorer");
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("cn.edu.buaa.sei.rucm.commonNavigator");
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(ModelTreeViewer.this);
				} catch (PartInitException ex) {
					RUCMPlugin.logError(ex, true);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
		});
		
		treeViewer = new TreeViewer(parent, SWT.NONE);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				for (ISelectionChangedListener listener : selectionListeners) {
					listener.selectionChanged(event);
				}
			}
		});
		
		treeModel = new TreeModel(true);
		treeViewer.setContentProvider(treeModel.getContentProvider());
		treeViewer.setLabelProvider(treeModel.getLabelProvider());
		treeModel.createContextMenu(treeViewer.getControl());
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		treeViewer.getTree().setLayoutData(data);
		
		IWorkbenchPart part = getSite().getPage().getActivePart();
		if (part != null && part instanceof LMFEditorBase) {
			LMFEditorBase editor = (LMFEditorBase) part;
			if (editor.getRootObject() != null &&
				editor.getRootObject().isKindOf(UCModel.TYPE_NAME)) {
				currentEditor = editor;
			}
		}
		resetInputModel();
		
		getSite().setSelectionProvider(this);
		getSite().getPage().addPartListener(partListener);
	}
	
	private void clearInputModel() {
		treeViewer.setInput(null);
	}
	
	private void resetInputModel() {
		if (currentEditor != null && treeViewer.getInput() != currentEditor.getRootObject()) {
			treeViewer.setInput(currentEditor.getRootObject());
		}
	}

	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}
	
	@Override
	public void dispose() {
		getSite().getPage().removePartListener(partListener);
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

}
