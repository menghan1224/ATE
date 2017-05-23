package cn.edu.buaa.sei.lmf.navigator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.Saveable;

import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.editor.LMFEditorPlugin;
import cn.edu.buaa.sei.lmf.runtime.FileResource;

public class SaveableResource extends Saveable {
	
	private final FileResource resource;
	
	public SaveableResource(FileResource resource) {
		super();
		this.resource = resource;
	}
	
	public FileResource getResource() {
		return resource;
	}

	@Override
	public String getName() {
		return resource.getFile().getName();
	}

	@Override
	public String getToolTipText() {
		return resource.getFile().getFullPath().toString();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromImage(LMFEditorPlugin.getBundleImage("icons/elements_obj.gif"));
	}

	@Override
	public void doSave(IProgressMonitor monitor) throws CoreException {
		try {
			resource.save();
		} catch (LMFResourceException ex) {
			throw new CoreException(new Status(IStatus.ERROR, LMFEditorPlugin.PLUGIN_ID, ex.getMessage(), ex));
		}
	}

	@Override
	public boolean isDirty() {
		return resource.isDirty();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof SaveableResource) {
			return ((SaveableResource) object).getResource().equals(this.resource);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return resource.hashCode();
	}

}
