package cn.edu.buaa.sei.lmf.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.runtime.FileResource;
import cn.edu.buaa.sei.lmf.runtime.FileResourceOccupant;

public abstract class LMFEditorBase extends EditorPart implements FileResourceOccupant {
	
	// model
	private LMFEditorInput input;
	private Composite parentComposite;
	
	public LMFEditorBase() {
		input = null;
		parentComposite = null;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Save Model File", 1);
			input.getResource().save();
			monitor.worked(1);
		} catch (LMFResourceException ex) {
			LMFEditorPlugin.logError(ex, true);
		} finally {
			monitor.done();
		}
	}

	@Override
	public void doSaveAs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (input instanceof LMFEditorInput) {
			this.input = (LMFEditorInput) input;
		} else {
			IFile file = (IFile) input.getAdapter(IFile.class);
			if (file == null) throw new PartInitException("Can not open input: " + input);
			try {
				this.input = new LMFEditorInput(file);
			} catch (LMFResourceException ex) {
				throw new PartInitException("Can not load model: " + file, ex);
			}
		}
		
		setSite(site);
		setInput(this.input);
		FileResource resource = this.input.getResource();
		setPartName(resource.getFile().getName());
		resource.registerEditor(this);
	}
	
	@Override
	public LMFEditorInput getEditorInput() {
		return input;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		this.parentComposite = parent;
	}

	@Override
	public boolean isDirty() {
		if (input != null) return input.getResource().isDirty();
		else return false;
	}
	
	@Override
	public void setDirty(boolean dirty) {
		if (parentComposite != null) {
			parentComposite.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					firePropertyChange(PROP_DIRTY);
				}
			});
		} else {
			firePropertyChange(PROP_DIRTY);
		}
	}
	
	public FileResource getResource() {
		return input == null ? null : input.getResource();
	}
	
	public ManagedObject getRootObject() {
		if (input != null) return input.getResource().getRootObject();
		else return null;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	@Override
	public void dispose() {
		if (input != null && !input.getResource().isUnloaded()) {
			input.getResource().unregisterEditor(this);
			if (input.getResource().isDirty()) {
				// TODO: user clicks "cancel". reload the resource?
			}
		}
		parentComposite = null;
		input = null;
		super.dispose();
	}
	
}
