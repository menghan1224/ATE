package cn.edu.buaa.sei.rucm.menus;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ca.carleton.sce.squall.ucmeta.UCDiagram;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.edit.MenuContributor;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.rucm.DiagramEditorInput;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class OpenWithDiagramEditor extends ContributionItem implements MenuContributor {

	private UCDiagram diagram;
	
	public OpenWithDiagramEditor() {
		diagram = null;
	}
	
	@Override
	public void fill(Menu menu, int index) {
		// build delete menu
		MenuItem menuItem = new MenuItem(menu, SWT.NONE, index);
		menuItem.setText("Open with Diagram Editor");
		menuItem.setImage(RUCMPlugin.getBundleImage("icons/ucmodel.gif"));
		if (diagram != null) {
			// build menu
			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					try {
						DiagramEditorInput input = new DiagramEditorInput(diagram);
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, "cn.edu.buaa.sei.rucm.diagramEditor", true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
					} catch (PartInitException ex) {
						RUCMPlugin.logError(ex, true);
					} catch (LMFResourceException ex) {
						RUCMPlugin.logError(ex, true);
					}
				}
			});
		} else {
			menuItem.setEnabled(false);
		}
	}
	
	@Override
	public IContributionItem[] createMenuItems() {
		ObjectNode selectedNode = null;
		ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) sel).getFirstElement();
			if (obj instanceof ObjectNode) {
				selectedNode = (ObjectNode) obj;
				ManagedObject object = selectedNode.getObject();
				if (object instanceof UCDiagram) {
					diagram = (UCDiagram) object;
					return new IContributionItem[] { this };
				}
			}
		}
		return new IContributionItem[0];
	}

}
