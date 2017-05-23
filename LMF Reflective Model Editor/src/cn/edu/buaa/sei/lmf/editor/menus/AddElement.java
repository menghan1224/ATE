package cn.edu.buaa.sei.lmf.editor.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.ManagedList;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.Primitives;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.TypeFilter;
import cn.edu.buaa.sei.lmf.edit.ContainmentNode;
import cn.edu.buaa.sei.lmf.edit.Node;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;

public class AddElement extends ContributionItem {

	public AddElement() {
	}

	public AddElement(String id) {
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
		
		// build add menu
		MenuItem menuItem = new MenuItem(menu, SWT.CASCADE, index);
		menuItem.setImage(LMFEditorPlugin.getBundleImage("icons/add_obj.gif"));
		if (selectedNode instanceof ContainmentNode) {
			ContainmentNode cnode = (ContainmentNode) selectedNode;
			if (cnode.getAttribute().getValueType() == Primitives.LIST) {
				menuItem.setText("Add Element");
			} else {
				menuItem.setText("Set Element");
				ObjectNode ownerNode = (ObjectNode) cnode.getParent();
				if (ownerNode.getObject().get(cnode.getKey()) != null) {
					menuItem.setEnabled(false);
					return;
				}
			}
			
			// build sub-menu
			Menu submenu = new Menu(menu.getShell(), SWT.DROP_DOWN);
			menuItem.setMenu(submenu);
			List<Type> allElements=getAllInstantiatableType(getValueType(cnode));
			
			//Set<Type> preferred = ContainmentNode.getPreferredChildTypes(cnode.getAttribute());
			List<Type> preferred=new ArrayList<Type>();
			for(Type type:allElements){
				if(type.getName().contains("View")||type.getName().equals("UseCase")){
					preferred.add(type);				}
			}
			if (preferred == null) {
				// no preferred type specified
				for (Type type : getAllInstantiatableType(getValueType(cnode))) {
					buildSubmenuItem(submenu, type, cnode);
				}
			} else {
				// preferred types specified
				for (Type type : preferred) {
					buildSubmenuItem(submenu, type, cnode);
				}
//				new MenuItem(submenu, SWT.SEPARATOR);
//				MenuItem other = new MenuItem(submenu, SWT.CASCADE);
//				other.setText("Other");
//				submenu = new Menu(menu.getShell(), SWT.DROP_DOWN);
//				other.setMenu(submenu);
//				List<Type> nonpref = getAllInstantiatableType(getValueType(cnode));
//				nonpref.removeAll(preferred);
//				for (Type type : nonpref) {
//					buildSubmenuItem(submenu, type, cnode);
//				}
			}
			
		} else {
			menuItem.setText("Add/Set Element");
			menuItem.setEnabled(false);
		}
	}
	
	private void buildSubmenuItem(Menu menu, final Type type, final ContainmentNode node) {
		MenuItem menuItem1 = new MenuItem(menu, SWT.NONE);
		menuItem1.setText(type.getName());
		menuItem1.setImage(ObjectNode.getObjectNodeDescriptor(type).getImage(type));
		menuItem1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// create element
				ManagedObject obj = LMFContext.newInstance(type);
				
				if (node.getAttribute().getValueType() == Primitives.LIST) {
					// add element
					ManagedList list = node.getObject().get(node.getKey()).listContent();
					list.add(obj);
				} else {
					// reset element
					node.getObject().set(node.getKey(), obj);
				}
				// TODO: select the object after creation
			}
		});
	}
	
	private Type getValueType(ContainmentNode cnode) {
		Attribute attr = cnode.getAttribute();
		if (attr.getValueType() == Primitives.LIST) {
			return attr.getValueTypeParameter();
		} else {
			return attr.getValueType();
		}
	}
	
	private List<Type> getAllInstantiatableType(final Type type) {
		return LMFContext.listTypes(new TypeFilter() {
			@Override
			public boolean accept(Type t) {
				if (t == type && !t.isAbstract()) {
					return true;
				} else if (t != type && t.isSubtypeOf(type) && !t.isAbstract()) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

}
