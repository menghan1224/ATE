package cn.edu.buaa.sei.lmf.editor.menus;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;

public class OpenPropertyView extends ContributionItem {

	public OpenPropertyView() {
	}

	public OpenPropertyView(String id) {
		super(id);
	}
	
	@Override
	public void fill(Menu menu, int index) {
		MenuItem menuItem = new MenuItem(menu, SWT.NONE, index);
		menuItem.setText("Open Property View");
		menuItem.setImage(LMFEditorPlugin.getBundleImage("icons/properties.gif"));
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.PropertySheet");
				} catch (PartInitException ex) {
					LMFEditorPlugin.logError(ex, true);
				}
			}
		});
	}

}
