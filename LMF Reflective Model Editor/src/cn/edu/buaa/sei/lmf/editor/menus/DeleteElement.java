package cn.edu.buaa.sei.lmf.editor.menus;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.edit.ContainmentNode;
import cn.edu.buaa.sei.lmf.edit.Node;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;

public class DeleteElement extends ContributionItem {

	public DeleteElement() {
	}

	public DeleteElement(String id) {
		super(id);
	}
	
	@Override
	public void fill(Menu menu, int index) {
		// get current selection
		Node selectedNode = null;
		ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) sel).getFirstElement();
			if (obj instanceof Node) {
				selectedNode = (Node) obj;
			}
		}
		
		// build delete menu
		MenuItem menuItem = new MenuItem(menu, SWT.NONE, index);
		menuItem.setText("Delete Element");
		menuItem.setImage(LMFEditorPlugin.getBundleImage("icons/delete_obj.gif"));
		menuItem.setAccelerator(SWT.DEL);
		if (selectedNode instanceof ObjectNode && selectedNode.getParent() != null) {
			final ObjectNode onode = (ObjectNode) selectedNode;
			// build menu
			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					Node parentNode = onode.getParent();
					if (parentNode instanceof ContainmentNode) {
						// delete element from parent
						final ManagedObject target = onode.getObject();
						final LMFResource res = target.resource();
						LMFUtility.removeObject(res.getRootObject(), target);
					} else {
						LMFEditorPlugin.logError(getClass().getName(), true);
					}
				}
			});
		} else {
			menuItem.setEnabled(false);
		}
	}
	
}
