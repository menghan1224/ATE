package cn.edu.buaa.sei.lmf.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.runtime.LMFPlugin;

public class LMFEditorInputFactory implements IElementFactory {
	
	public static final String KEY_RESOURCE = "resource";
	
	public static IFile getFile(IMemento memento) {
		String path = memento.getString(LMFEditorInputFactory.KEY_RESOURCE);
		if (path != null) {
			return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
		} else {
			return null;
		}
	}

	@Override
	public IAdaptable createElement(IMemento memento) {
		IFile file = LMFEditorInputFactory.getFile(memento);
		if (file != null) {
			LMFEditorInput input = null;
			try {
				input = new LMFEditorInput(file);
			} catch (LMFResourceException ex) {
				LMFPlugin.logError(ex, true);
				return null;
			}
			return input;
		} else {
			return null;
		}
	}

}
