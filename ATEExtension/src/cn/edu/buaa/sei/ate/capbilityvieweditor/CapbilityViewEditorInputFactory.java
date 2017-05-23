package cn.edu.buaa.sei.ate.capbilityvieweditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

import cn.edu.buaa.sei.ate.metamodel.ATECapbilityView;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.editor.LMFEditorInputFactory;
import cn.edu.buaa.sei.lmf.runtime.FileResource;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class CapbilityViewEditorInputFactory implements IElementFactory {
	public static final String  KEY_ELEMENT = "element";
	@Override
	public IAdaptable createElement(IMemento memento) {
		IFile file = LMFEditorInputFactory.getFile(memento);
		LMFResource resource = LMFResource.getResource(file.getFullPath().toString());
		if (resource == null) {
			resource = new FileResource(file, null);
			try {
				resource.load();
			} catch (LMFResourceException ex) {
				resource.unload();
				RUCMPlugin.logError(ex, true);
				return null;
			}
		}
		
		String ref = memento.getString(KEY_ELEMENT);
		ManagedObject obj = LMFResource.decodeReference(ref, resource.getRootObject());
		try {
			if (obj instanceof ATECapbilityView) {
				return new CapbilityViewEditorInput((ATECapbilityView) obj); // 这个创建编辑器的输入数据。
			} else {
				return null;
			}
		} catch (LMFResourceException ex) {
			RUCMPlugin.logError(ex, true);
			return null;
		}
	}
	
	
	

}
