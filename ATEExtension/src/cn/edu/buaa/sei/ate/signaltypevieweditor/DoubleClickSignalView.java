package cn.edu.buaa.sei.ate.signaltypevieweditor;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.ate.metamodel.ATESignalTypeView;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.navigator.DoubleClickActionDelegate;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class DoubleClickSignalView implements DoubleClickActionDelegate {

	@Override
	public boolean shouldHandleDoubleClickAction(ManagedObject obj) {
		
		return obj instanceof ATESignalTypeView;
	}

	@Override
	public void handleDoubleClickAction(ManagedObject obj) {
		try {
			// �����༭������������
			SignalViewEditorInput input = new SignalViewEditorInput((ATESignalTypeView) obj);
			
			// �򿪱༭����ע��ڶ���������plugin.xml����editors��չ������Fault Editor��id��
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, "atesignaltypeview.editor", true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
		} catch (PartInitException ex) {
			RUCMPlugin.logError(ex, true);
		} catch (LMFResourceException ex) {
			RUCMPlugin.logError(ex, true);
		}
	}

}
