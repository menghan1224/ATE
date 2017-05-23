package cn.edu.buaa.sei.rucm.commands;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.SwingUtilities;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.rucm.SnailLMFEditorBase;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;

public class Print extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (part instanceof SnailLMFEditorBase) {
			final View content = ((SnailLMFEditorBase) part).getSnailView().getPrintableContent();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					PrinterJob pj = PrinterJob.getPrinterJob();
					PageFormat pf = new PageFormat(); //pj.pageDialog(pj.defaultPage());
					Paper paper = new Paper();
					final int margin = 50;
					final int width = content.getWidth() + 2 * margin;
					final int height = content.getHeight() + 2 * margin;
					if (width <= height) {
						paper.setSize(width, height);
						paper.setImageableArea(margin, margin, content.getWidth(), content.getHeight());
						pf.setPaper(paper);
					} else {
						paper.setSize(height, width);
						paper.setImageableArea(margin, margin, content.getHeight(), content.getWidth());
						pf.setPaper(paper);
						pf.setOrientation(PageFormat.LANDSCAPE);
					}
					
					pj.setPrintable(new Printable() {

						@Override
						public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
							
							if (page > 0) {
								return NO_SUCH_PAGE;
							}

							/*
							 * User (0,0) is typically outside the imageable
							 * area, so we must translate by the X and Y
							 * values in the PageFormat to avoid clipping
							 */
							Graphics2D g2d = (Graphics2D) g;
							g2d.translate(pf.getImageableX(), pf.getImageableY());
							ViewGraphics vg = new ViewGraphics((Graphics2D) g);
							content.paintOnTarget(vg);
							
							
							return PAGE_EXISTS;
						}
						
					}, pf);
					if (pj.printDialog()) {
						try {
							pj.print();
						} catch (PrinterException exc) {
							System.out.println(exc);
						}
					}
				}
			});
		}
		return null;
	}

}
