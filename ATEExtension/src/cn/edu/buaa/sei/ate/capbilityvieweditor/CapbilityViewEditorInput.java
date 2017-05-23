package cn.edu.buaa.sei.ate.capbilityvieweditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IMemento;

import cn.edu.buaa.sei.ate.metamodel.ATECapbility;
import cn.edu.buaa.sei.ate.metamodel.ATECapbilityView;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.editor.LMFEditorInput;
import cn.edu.buaa.sei.lmf.runtime.FileResource;
import cn.edu.buaa.sei.rucm.EditorInputFactory;

public class CapbilityViewEditorInput extends LMFEditorInput{
	private final ATECapbilityView ateCapbilityView;
	

	public CapbilityViewEditorInput(ATECapbilityView ateCapbilityView) throws LMFResourceException {
		
		// TODO Auto-generated constructor stub
		super(((FileResource) ateCapbilityView.resource()).getFile());
		this.ateCapbilityView=ateCapbilityView;
	}
	public ATECapbilityView getFault() {
		return ateCapbilityView;
	}
	
	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
		memento.putString(EditorInputFactory.KEY_ELEMENT, LMFResource.encodeReference(ateCapbilityView));
	}
	
	@Override
	public String getFactoryId() {
		// 注意！这个地方返回的字符串必须和plugin.xml里面配置的elementFactory扩展点的id一致。
		return "capbilityview.editorinputfactory";
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ManagedObject.class) {
			return ateCapbilityView;
		} else {
			return super.getAdapter(adapter);
		}
	}
	
	@Override
	public String getName() {
		return ateCapbilityView.getName().isEmpty() ? "Untitled" : ateCapbilityView.getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CapbilityViewEditorInput) {
			return ((CapbilityViewEditorInput) obj).ateCapbilityView == this.ateCapbilityView;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return ateCapbilityView.hashCode();
	}


}
