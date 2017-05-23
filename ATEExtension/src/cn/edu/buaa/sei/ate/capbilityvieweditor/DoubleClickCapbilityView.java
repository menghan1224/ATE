package cn.edu.buaa.sei.ate.capbilityvieweditor;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.ate.metamodel.ATECapbilityView;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.navigator.DoubleClickActionDelegate;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class DoubleClickCapbilityView implements DoubleClickActionDelegate {

	@Override
	public boolean shouldHandleDoubleClickAction(ManagedObject obj) {
		
		return obj instanceof ATECapbilityView;
	}

	@Override
	public void handleDoubleClickAction(ManagedObject obj) {
		try {
			// �����༭������������
			CapbilityViewEditorInput input = new CapbilityViewEditorInput((ATECapbilityView) obj);
			
			// �򿪱༭����ע��ڶ���������plugin.xml����editors��չ������Fault Editor��id��
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, "atecapbilityview.editor", true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
		} catch (PartInitException ex) {
			RUCMPlugin.logError(ex, true);
		} catch (LMFResourceException ex) {
			RUCMPlugin.logError(ex, true);
		}
	}

}
