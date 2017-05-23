package cn.edu.buaa.sei.rucm;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Panel;

import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;

import cn.edu.buaa.sei.lmf.editor.LMFEditorBase;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;

/**
 * This class mainly deals with the focus problem when using Swing controls in Eclipse
 * (Issue #23 and #33).
 * <b>I spent more than 10 hours to fix the focus issue.</b> The code below is very subtle, do not
 * try to modify anything if you don't familiar with it.
 * 
 * @author Gong Zhang
 *
 */
public abstract class SnailLMFEditorBase extends LMFEditorBase {
	
	public abstract static class SnailEditorView extends View {
		public abstract View getPrintableContent();
	}
	
	private final IPartListener partListener = new IPartListener() {
		@Override public void partClosed(IWorkbenchPart part) {}
		@Override public void partBroughtToTop(IWorkbenchPart part) {}
		@Override public void partActivated(IWorkbenchPart part) {
			if (part == SnailLMFEditorBase.this) {
				SnailLMFEditorBase.this.partActivated();
			}
		}
		@Override public void partOpened(IWorkbenchPart part) {}
		@Override public void partDeactivated(IWorkbenchPart part) {
			if (part == SnailLMFEditorBase.this) {
				SnailLMFEditorBase.this.partDeactivated();
			}
		}
	};
	
	private final IWindowListener windowListener = new IWindowListener() {
		@Override public void windowOpened(IWorkbenchWindow window) {}
		@Override public void windowClosed(IWorkbenchWindow window) {}
		@Override
		public void windowDeactivated(IWorkbenchWindow window) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// disable all the Swing focus in Eclipse workbench
					viewContext.getRootView().requestKeyboardFocus();
					viewContext.getSwingContainer().setFocusable(false);
				}
			});
		}
		@Override
		public void windowActivated(final IWorkbenchWindow window) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// recover the Swing focus
					viewContext.getSwingContainer().setFocusable(true);
					if (window.getActivePage().getActivePart() == SnailLMFEditorBase.this) {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								window.getActivePage().activate(SnailLMFEditorBase.this);
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										viewContext.getSwingContainer().requestFocusInWindow();
									}
								});
							}
						});
					}
				}
			});
		}
	};
	
	private Composite composite;
	private ViewContext viewContext;
	
	public SnailLMFEditorBase() {
	}
	
	protected Composite getComposite() {
		return composite;
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
	}
	
	protected abstract ViewContext createPartControl(Container container);
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE);
		
		// bridge to swing
		Frame frame = null;
		String embeddedFrameClass = "sun.lwawt.macosx.CViewEmbeddedFrame";
		try {
			Class.forName(embeddedFrameClass);
			RUCMPlugin.logError("The editor does not support JRE 1.7.", true); // FIXME
		} catch (ClassNotFoundException ex1) {
			embeddedFrameClass = "apple.awt.CEmbeddedFrame";
			try {
				Class.forName(embeddedFrameClass);
			} catch (ClassNotFoundException ex2) {
				embeddedFrameClass = null;
			}
		}
		SWT_AWT.embeddedFrameClass = embeddedFrameClass;
		frame = SWT_AWT.new_Frame(composite);
		if (frame == null) {
			RUCMPlugin.logError("Can not create SWT_AWT bridge for Snail GUI.", true);
		}
		frame.setFocusable(false);
		Panel panel = new Panel();
		panel.setFocusable(false);
		frame.add(panel);
		
		// load resource
		RUCMPluginResource.initResource(panel.getGraphicsConfiguration());
		
		viewContext = createPartControl(panel);
		viewContext.getSwingContainer().setFocusable(true);
		viewContext.getSwingContainer().setRequestFocusEnabled(true);
		getSite().getPage().addPartListener(partListener);
		PlatformUI.getWorkbench().addWindowListener(windowListener);
	}
	
	@Override
	public void dispose() {
		PlatformUI.getWorkbench().removeWindowListener(windowListener);
		getSite().getPage().removePartListener(partListener);
		super.dispose();
	}
	
	@Override
	public void setFocus() {
		composite.forceFocus();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				viewContext.getSwingContainer().requestFocusInWindow();
			}
		});
	}
	
	private void partActivated() {
		IBindingService binding = (IBindingService) PlatformUI.getWorkbench().getService(IBindingService.class);
		if (binding != null) binding.setKeyFilterEnabled(false);
	}
	
	private void partDeactivated() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				viewContext.getRootView().requestKeyboardFocus();
			}
		});
		IBindingService binding = (IBindingService) PlatformUI.getWorkbench().getService(IBindingService.class);
		if (binding != null) binding.setKeyFilterEnabled(true);
	}
	
	@Override
	public void setUnload() {
		getSite().getPage().closeEditor(this, false);
	}
	
	@Override
	public boolean isSaveOnCloseNeeded() {
		// The editor won't prompt user to save if there are more than one editor associated with same input model.
		if (getEditorInput().getResource().getEditorOccupantCount() > 1) {
			return false;
		} else {
			return super.isSaveOnCloseNeeded();
		}
	}
	
	abstract public SnailEditorView getSnailView();
	
}
