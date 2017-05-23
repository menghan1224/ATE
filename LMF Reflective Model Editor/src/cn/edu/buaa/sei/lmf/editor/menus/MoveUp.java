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

import cn.edu.buaa.sei.lmf.AttributeSetter;
import cn.edu.buaa.sei.lmf.ManagedList;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Primitives;
import cn.edu.buaa.sei.lmf.edit.ContainmentNode;
import cn.edu.buaa.sei.lmf.edit.Node;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;

public class MoveUp extends ContributionItem {

	public MoveUp() {
	}

	public MoveUp(String id) {
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
		
		// build move up menu
		MenuItem menuItem = new MenuItem(menu, SWT.NONE, index);
		menuItem.setText("Move Up");
//		menuItem.setAccelerator(SWT.ALT | 'u');
		if (selectedNode instanceof ObjectNode && selectedNode.getParent() != null) {
			final ObjectNode onode = (ObjectNode) selectedNode;
			if (onode.getParent() instanceof ContainmentNode) {
				final ContainmentNode cnode = (ContainmentNode) onode.getParent();
				if (cnode.getAttribute().getValueType() == Primitives.LIST) {
					final ManagedList list = cnode.getObject().get(cnode.getKey()).listContent();
					final int i = list.indexOf(onode.getObject());
					if (i > 0) {
						// build menu
						menuItem.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e) {
								// move up
								cnode.getObject().set(new AttributeSetter() {
									@Override
									public void apply(ManagedObject target) {
										list.exchangeElements(i - 1, i);
									}
								});
							}
						});
					} else {
						menuItem.setEnabled(false);
					}
				} else {
					menuItem.setEnabled(false);
				}
			} else {
				menuItem.setEnabled(false);
			}
		} else {
			menuItem.setEnabled(false);
		}
	}
	
}
