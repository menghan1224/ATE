package cn.edu.buaa.sei.lmf.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.runtime.FileResource;

public class LMFEditorInput implements IEditorInput, IPersistableElement {
	
	private final IFile file;
	private final FileResource resource;
	
	public LMFEditorInput(IFile file) throws LMFResourceException {
		if (file == null) throw new IllegalArgumentException();
		this.file = file;
		LMFResource raw = LMFResource.getResource(file.getFullPath().toString());
		if (raw != null) {
			resource = (FileResource) raw;
		} else {
			resource = new FileResource(file, null);
			try {
				resource.load();
			} catch (LMFResourceException ex) {
				resource.unload();
				throw ex;
			}
		}
	}
	
	@Override
	public void saveState(IMemento memento) {
		memento.putString(LMFEditorInputFactory.KEY_RESOURCE, file.getFullPath().toString());
	}
	
	@Override
	public String getFactoryId() {
		return "cn.edu.buaa.sei.lmf.editor.inputFactory";
	}
	
	public final IFile getFile() {
		return file;
	}
	
	public final FileResource getResource() {
		return resource;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IFile.class) {
			return file;
		} else if (adapter == LMFResource.class) {
			return resource;
		} else {
			return null;
		}
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromImage(LMFEditorPlugin.getBundleImage("icons/elements_obj.gif"));
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public final IPersistableElement getPersistable() {
		return this;
	}

	@Override
	public String getToolTipText() {
		return file.getFullPath().toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LMFEditorInput) {
			return file.equals(((LMFEditorInput) obj).getFile());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}

}
