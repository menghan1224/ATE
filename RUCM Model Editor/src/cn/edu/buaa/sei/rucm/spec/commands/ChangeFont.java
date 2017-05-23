package cn.edu.buaa.sei.rucm.spec.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;

public class ChangeFont extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FontDialog fd = new FontDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
		fd.setText("Select Font");

		java.awt.Font font = RUCMPluginResource.getSpecFont(false);
		FontData defaultFont = new FontData(font.getFamily(), font.getSize(), SWT.NORMAL);
		fd.setFontList(new FontData[] { defaultFont });

		FontData newFont = fd.open();
		if (newFont == null)
			return null;

		IPreferenceStore store = RUCMPlugin.getDefault().getPreferenceStore();
		store.setValue(RUCMPlugin.PK_SPEC_EDITOR_FONT_NAME, newFont.getName());
		store.setValue(RUCMPlugin.PK_SPEC_EDITOR_FONT_SIZE, newFont.getHeight());
		return null;
	}

}
