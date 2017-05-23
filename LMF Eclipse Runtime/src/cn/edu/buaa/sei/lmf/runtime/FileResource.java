package cn.edu.buaa.sei.lmf.runtime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;

import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;

public class FileResource extends LMFResource {
	
	private final IFile file;
	private final Vector<FileResourceOccupant> occupants;
	private ByteArrayOutputStream temporaryStream = null;
	private boolean dirty;

	private final IResourceChangeListener fileListener = new IResourceChangeListener() {
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta().findMember(file.getFullPath());
			if (delta != null && (delta.getKind() == IResourceDelta.REMOVED)) {
				// XXX: Maybe REMOVED_PHANTOM and REPLACED ?
				fileRemoved();
			}
		}
	};
	
	public FileResource(IFile file, ManagedObject rootObject) {
		super(file.getFullPath().toString(), rootObject);
		this.file = file;
		this.occupants = new Vector<FileResourceOccupant>();
		this.dirty = false;
	}
	
	public synchronized boolean isDirty() {
		return dirty;
	}
	
	public IFile getFile() {
		return file;
	}
	
	@Override
	protected synchronized void modelChanged(ManagedObject target, String key, ManagedObject value) {
		if (!dirty) {
			dirty = true;
			for (FileResourceOccupant editor : occupants) {
				editor.setDirty(dirty);
			}
		}
	}
	
	public synchronized void registerEditor(FileResourceOccupant editor) {
		this.occupants.add(editor);
		editor.setDirty(dirty);
	}
	
	public synchronized void unregisterEditor(FileResourceOccupant editor) {
		if (this.occupants.remove(editor)) {
			// XXX: check no dependency?
			if (this.occupants.isEmpty()) {
				unload();
			}
		}
	}
	
	public synchronized int getOccupantCount() {
		return occupants.size();
	}
	
	public synchronized int getEditorOccupantCount() {
		int cnt = 0;
		for (FileResourceOccupant occupant : occupants) {
			if (occupant instanceof IEditorPart) {
				cnt++;
			}
		}
		return cnt;
	}
	
	private synchronized void fileRemoved() {
		// forcefully unload
		unload();
		for (FileResourceOccupant editor : occupants) {
			editor.setUnload();
		}
		occupants.clear();
	}
	
	@Override
	protected void unloaded() {
		setEditable(false);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(fileListener);
	}

	@Override
	protected InputStream getInputStream() throws LMFResourceException {
		try {
			return file.getContents();
		} catch (CoreException ex) {
			throw new LMFResourceException(ex);
		}
	}

	@Override
	protected OutputStream getOutputStream() throws LMFResourceException {
		temporaryStream = new ByteArrayOutputStream();
		return temporaryStream;
	}
	
	private final byte[] pullOutputData() throws LMFResourceException {
		try {
			temporaryStream.close();
		} catch (IOException ex) {
			throw new LMFResourceException(ex);
		}
		byte[] bytes = temporaryStream.toByteArray();
		temporaryStream = null;
		return bytes;
	}
	
	@Override
	protected synchronized void saved() throws LMFResourceException {
		ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream(pullOutputData());
			if (file.exists()) {
				file.setContents(is, true, true, null);
			} else {
				file.create(is, true, null);
			}
			// set dirty false
			this.dirty = false;
			for (FileResourceOccupant editor : occupants) {
				editor.setDirty(dirty);
			}
		} catch (CoreException ex) {
			throw new LMFResourceException(ex);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	@Override
	protected FileResource deriveResource(String uri) throws LMFResourceException {
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(uri);
		if (res != null && res instanceof IFile) {
			return new FileResource((IFile) res, null);
		} else {
			throw new LMFResourceException("Missing resource: " + uri);
		}
	}

}
