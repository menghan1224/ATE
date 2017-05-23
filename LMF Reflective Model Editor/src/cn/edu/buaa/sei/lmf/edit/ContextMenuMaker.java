package cn.edu.buaa.sei.lmf.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;
import cn.edu.buaa.sei.lmf.editor.menus.AddElement;
import cn.edu.buaa.sei.lmf.editor.menus.DeleteElement;
import cn.edu.buaa.sei.lmf.editor.menus.MoveDown;
import cn.edu.buaa.sei.lmf.editor.menus.MoveUp;
import cn.edu.buaa.sei.lmf.editor.menus.OpenPropertyView;
import cn.edu.buaa.sei.lmf.editor.menus.UpdateElement;

public class ContextMenuMaker {
	
	private static final String GROUP_EDITING = "editing";
	private static final String GROUP_ACTION = "action";
	private static final String GROUP_UTILITY = "utility";
	
	private final Map<String, List<MenuContributor>> contributors;
	private final List<String> keys;
	
	public ContextMenuMaker() {
		contributors = new HashMap<String, List<MenuContributor>>();
		keys = new ArrayList<String>();
		// add standard menu
		addStandardMenus();
		// add extended menu
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(LMFEditorPlugin.PLUGIN_ID + ".contextMenu");
		IConfigurationElement[] points = extensionPoint.getConfigurationElements();
		for (IConfigurationElement point : points) {
			try {
				String group = point.getAttribute("group");
				MenuContributor contributor = (MenuContributor) point.createExecutableExtension("class");
				addExtendedMenus(group, contributor);
			} catch (Exception ex) {
				LMFEditorPlugin.logError(ex, true);
			}
		}
	}
	
	private void addExtendedMenus(String group, MenuContributor contributor) {
		List<MenuContributor> list = contributors.get(group);
		if (list == null) {
			list = new ArrayList<MenuContributor>();
			contributors.put(group, list);
			keys.add(group);
		}
		list.add(contributor);
	}
	
	private void addStandardMenus() {
		List<MenuContributor> list = new ArrayList<MenuContributor>();
		contributors.put(GROUP_EDITING, list);
		keys.add(GROUP_EDITING);
		list.add(new MenuContributor() {
			@Override
			public IContributionItem[] createMenuItems() {
				return new IContributionItem[] {
					new AddElement(),
					new UpdateElement(),
					new DeleteElement(),
					new Separator(),
					new MoveUp(),
					new MoveDown()
				};
			}
		});
		list = new ArrayList<MenuContributor>();
		contributors.put(GROUP_ACTION, list);
		keys.add(GROUP_ACTION);
		list = new ArrayList<MenuContributor>();
		contributors.put(GROUP_UTILITY, list);
		keys.add(GROUP_UTILITY);
		list.add(new MenuContributor() {
			@Override
			public IContributionItem[] createMenuItems() {
				return new IContributionItem[] {
					new OpenPropertyView()
				};
			}
		});
	}
	
	void createContextMenu(Control control) {
		final MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				// fill up the menu:
				createContextMenu(manager);
			}
		});
		Menu menu = menuManager.createContextMenu(control);
		control.setMenu(menu);

		// NOTE: we don't want to extend the menu by eclipse
		// getSite().registerContextMenu(menuManager, treeViewer);
	}
	
	public void createContextMenu(IMenuManager menu) {
		for (int i = 0; i < keys.size(); i++) {
			List<MenuContributor> cs = contributors.get(keys.get(i));
			if (i > 0) menu.add(new Separator());
			for (MenuContributor c : cs) {
				for (IContributionItem item : c.createMenuItems()) {
					menu.add(item);
				}
			}
		}
	}
	
}

