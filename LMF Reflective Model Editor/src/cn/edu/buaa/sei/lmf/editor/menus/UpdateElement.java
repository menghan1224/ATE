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

import cn.edu.buaa.sei.lmf.edit.Node;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;

public class UpdateElement extends ContributionItem {

	public UpdateElement() {
	}

	public UpdateElement(String id) {
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
		menuItem.setText("Update Element");
		menuItem.setImage(LMFEditorPlugin.getBundleImage("icons/refresh_tab.gif"));
		if (selectedNode instanceof ObjectNode) {
			final ObjectNode onode = (ObjectNode) selectedNode;
			// build menu
			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					onode.forceUpdateLabel();
				}
			});
		} else {
			menuItem.setEnabled(false);
		}
	}

}
