package cn.edu.buaa.sei.lmf.navigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.INavigatorSaveablesService;

import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.Node;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;

public class Navigator extends CommonNavigator {
	
	public static final String ID = "cn.edu.buaa.sei.rucm.commonNavigator";
	
	private static Map<Type, List<DoubleClickActionDelegate>> DOUBLE_CLICK_ACTION_MAP = null;
	
	@Override
	public Saveable[] getSaveables() {
		return this.getActiveSaveables();
	}
	
	@Override
	public Saveable[] getActiveSaveables() {
		INavigatorContentService contentService = getNavigatorContentService();
		INavigatorSaveablesService saveablesService = contentService.getSaveablesService();
		try {
			Saveable[] saveables = saveablesService.getActiveSaveables();
			return saveables;
		} catch (NullPointerException ex) {
			// is this a bug of Eclipse?
			return new Saveable[0];
		}
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	public void fireSaveabelsChanged(){
        this.firePropertyChange(CommonNavigator.PROP_DIRTY);
    }
	
	@Override
	protected void handleDoubleClick(DoubleClickEvent anEvent) {
		Node selectedNode = null;
		Object selectedObject = null;
		ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
			selectedObject = ((IStructuredSelection) sel).getFirstElement();
			if (selectedObject instanceof Node) {
				selectedNode = (Node) selectedObject;
			}
		}
		if (selectedNode != null && selectedNode instanceof ObjectNode) {
			ManagedObject obj = ((ObjectNode) selectedNode).getObject();
			// initialize double click action map
			if (DOUBLE_CLICK_ACTION_MAP == null) {
				initDoubleClickActionMap();
			}
			List<DoubleClickActionDelegate> delegates = DOUBLE_CLICK_ACTION_MAP.get(obj.type());
			if (delegates != null) {
				for (int i = delegates.size() - 1; i >= 0; i--) {
					DoubleClickActionDelegate delegate = delegates.get(i);
					if (delegate.shouldHandleDoubleClickAction(obj)) {
						delegate.handleDoubleClickAction(obj);
						break;
					}
				}
			}
		} else {
			super.handleDoubleClick(anEvent);
			
			// NOTE: if user double clicked the model file, it will be updated.
			if (selectedObject instanceof IFile) {
				getCommonViewer().refresh(selectedObject);
			}
		}
	}
	
	private static void initDoubleClickActionMap() {
		DOUBLE_CLICK_ACTION_MAP = new HashMap<Type, List<DoubleClickActionDelegate>>();
		Map<Type, DoubleClickActionDelegate> map = new HashMap<Type, DoubleClickActionDelegate>();
		for (IConfigurationElement point : Platform.getExtensionRegistry().getExtensionPoint(LMFEditorPlugin.PLUGIN_ID + ".doubleClickAction").getConfigurationElements()) {
			try {
				final Type targetType = LMFContext.typeForName(point.getAttribute("typeName"));
				DoubleClickActionDelegate delegate = (DoubleClickActionDelegate) point.createExecutableExtension("class");
				map.put(targetType, delegate);
			} catch (IllegalArgumentException ex) {
				LMFEditorPlugin.logError(ex, true);
			} catch (CoreException ex) {
				LMFEditorPlugin.logError(ex, true);
			}
		}
		
		List<Type> sorted = LMFUtility.sortTypesByInheritance(map.keySet());
		List<Type> allTypes = LMFContext.listTypes(null);
		for (Type key : sorted) {
			DoubleClickActionDelegate value = map.get(key);
			for (Type type : allTypes) {
				if (type.isOrIsSubtypeOf(key)) {
					List<DoubleClickActionDelegate> list = DOUBLE_CLICK_ACTION_MAP.get(type);
					if (list == null) {
						list = new ArrayList<DoubleClickActionDelegate>();
						DOUBLE_CLICK_ACTION_MAP.put(type, list);
					}
					list.add(value);
				}
			}
		}
	}

}
