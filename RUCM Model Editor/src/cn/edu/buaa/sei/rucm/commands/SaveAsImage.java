package cn.edu.buaa.sei.rucm.commands;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.SnailLMFEditorBase;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;

public class SaveAsImage extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (part instanceof SnailLMFEditorBase) {
			final View content = ((SnailLMFEditorBase) part).getSnailView().getPrintableContent();
			// open SWT file chooser dialog
			FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.SAVE);
			dialog.setFilterNames (new String [] {"PNG Image", "All Files (*.*)"});
			dialog.setFilterExtensions (new String [] {"*.png", "*.*"});
			final String filepath = dialog.open();
			if (filepath != null) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// generate PNG image
						final int width = content.getWidth();
						final int height = content.getHeight();
						BufferedImage bufferedImage = content.getViewContext().getGraphicsConfiguration().createCompatibleImage(width, height);
						Graphics2D g = bufferedImage.createGraphics();
						ViewGraphics vg = new ViewGraphics((Graphics2D) g);
						content.paintOnTarget(vg);
						g.dispose();
						try {
							ImageIO.write(bufferedImage, "png", new File(filepath));
						} catch (final IOException ex) {
							PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
								@Override
								public void run() {
									RUCMPlugin.logError(ex, true);
								}
							});
						}
					}
				});
			}
		}
		return null;
	}

}
