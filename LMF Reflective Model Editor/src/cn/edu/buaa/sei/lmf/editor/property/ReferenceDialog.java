package cn.edu.buaa.sei.lmf.editor.property;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.lmf.edit.TreeModel;

public class ReferenceDialog extends Dialog {

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ReferenceDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.RESIZE | SWT.TITLE);
	}
	
	@Override
	protected void configureShell(Shell shell) {
	     super.configureShell(shell);
	     shell.setText("Reference Element");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		treeViewer = new TreeViewer(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		treeViewer.setContentProvider(treeModel.getContentProvider());
		treeViewer.setLabelProvider(treeModel.getLabelProvider());
		treeViewer.setInput(rootObject);

		return container;
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
	
	@Override
	protected void okPressed() {
		result = null;
		ISelection sel = treeViewer.getSelection();
		if (sel != null && !sel.isEmpty() && sel instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) sel).getFirstElement();
			if (obj instanceof ObjectNode) {
				ManagedObject mobj = ((ObjectNode) obj).getObject();
				if (mobj.isKindOf(type)) {
					result = mobj;
					super.okPressed();
				} else {
					showErrMsgBox();
			        return;
				}
			} else {
				showErrMsgBox();
		        return;
			}
		} else {
			super.okPressed();
		}
	}
	
	private void showErrMsgBox() {
		MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
        messageBox.setText("Error");
        messageBox.setMessage(String.format("Selected element is not an instance of %s.", type.getName()));
        messageBox.open();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	private Type type;
	private TreeModel treeModel;
	private ManagedObject rootObject;
	private ManagedObject result;
	private TreeViewer treeViewer;
	
	public void init(Type type, ManagedObject root) {
		this.type = type;
		this.rootObject = root;
		this.treeModel = new TreeModel(false);
	}
	
	public ManagedObject getValue() {
		return result;
	}

}
