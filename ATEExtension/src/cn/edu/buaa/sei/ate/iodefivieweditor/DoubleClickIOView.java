package cn.edu.buaa.sei.ate.iodefivieweditor;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.ate.metamodel.ATEIODefinitionView;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.navigator.DoubleClickActionDelegate;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class DoubleClickIOView implements DoubleClickActionDelegate {

	@Override
	public boolean shouldHandleDoubleClickAction(ManagedObject obj) {
		
		return obj instanceof ATEIODefinitionView;
	}

	@Override
	public void handleDoubleClickAction(ManagedObject obj) {
		try {
			// 创建编辑器的输入数据
			IOViewEditorInput input = new IOViewEditorInput((ATEIODefinitionView) obj);
			
			// 打开编辑器（注意第二个参数是plugin.xml里面editors扩展点里面Fault Editor的id）
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, "ateioview.editor", true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
		} catch (PartInitException ex) {
			RUCMPlugin.logError(ex, true);
		} catch (LMFResourceException ex) {
			RUCMPlugin.logError(ex, true);
		}
	}

	}

