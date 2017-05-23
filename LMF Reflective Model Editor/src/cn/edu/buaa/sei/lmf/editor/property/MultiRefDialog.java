package cn.edu.buaa.sei.lmf.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.lmf.edit.TreeModel;

public class MultiRefDialog extends Dialog {
	
	private class ListModel implements IStructuredContentProvider, ILabelProvider {
		
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return result.toArray();
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}

		@Override
		public Image getImage(Object element) {
			ManagedObject obj = (ManagedObject) element;
			return ObjectNode.getObjectNodeDescriptor(obj.type()).getImage(obj);
		}

		@Override
		public String getText(Object element) {
			ManagedObject obj = (ManagedObject) element;
			return ObjectNode.getObjectNodeDescriptor(obj.type()).getLabel(obj);
		}
		
	}

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MultiRefDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.RESIZE | SWT.TITLE);
	}
	
	@Override
	protected void configureShell(Shell shell) {
	     super.configureShell(shell);
	     shell.setText("Reference Elements");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(3, false));
		
		treeViewer = new TreeViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		Tree tree = treeViewer.getTree();
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tree.widthHint = 235;
		tree.setLayoutData(gd_tree);
		
		treeViewer.setContentProvider(treeModel.getContentProvider());
		treeViewer.setLabelProvider(treeModel.getLabelProvider());
		treeViewer.setInput(rootObject);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		
		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addElement();
			}
		});
		btnAdd.setText(">");
		
		Button btnRemove = new Button(composite, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeElement();
			}
		});
		btnRemove.setText("<");
		
		listViewer = new ListViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		org.eclipse.swt.widgets.List list = listViewer.getList();
		GridData gd_list = new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1);
		gd_list.widthHint = 207;
		list.setLayoutData(gd_list);
		
		listViewer.setContentProvider(listModel);
		listViewer.setLabelProvider(listModel);
		listViewer.setInput(result);

		return container;
	}
	
	private void addElement() {
		IStructuredSelection sel = (IStructuredSelection) treeViewer.getSelection();
		if (!sel.isEmpty()) {
			Object obj = sel.getFirstElement();
			if (obj instanceof ObjectNode) {
				ManagedObject mobj = ((ObjectNode) obj).getObject();
				if (mobj.isKindOf(type)) {
					result.add(mobj);
					listViewer.refresh();
				}
			}
		}
	}
	
	private void removeElement() {
		IStructuredSelection sel = (IStructuredSelection) listViewer.getSelection();
		if (!sel.isEmpty()) {
			Object obj = sel.getFirstElement();
			result.remove(obj);
			listViewer.refresh();
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(504, 448);
	}
	
	private Type type;
	private ManagedObject rootObject;
	private final List<ManagedObject> result = new ArrayList<ManagedObject>();
	private TreeModel treeModel;
	private ListModel listModel;
	private TreeViewer treeViewer;
	private ListViewer listViewer;
	
	public void init(Type type, Object[] initObjects, ManagedObject root) {
		this.type = type;
		this.rootObject = root;
		treeModel = new TreeModel(false);
		listModel = new ListModel();
		for (Object object : initObjects) {
			result.add((ManagedObject) object);
		}
	}
	
	public Object[] getValue() {
		return result.toArray();
	}
}
