package cn.edu.buaa.sei.ate.signaltypevieweditor;

import org.eclipse.ui.IMemento;

import cn.edu.buaa.sei.ate.metamodel.ATESignalTypeView;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.editor.LMFEditorInput;
import cn.edu.buaa.sei.lmf.runtime.FileResource;
import cn.edu.buaa.sei.rucm.EditorInputFactory;

public class SignalViewEditorInput extends LMFEditorInput{
	private final ATESignalTypeView ateSignalView;
	

	public SignalViewEditorInput(ATESignalTypeView ateSignalView) throws LMFResourceException {
		
		// TODO Auto-generated constructor stub
		super(((FileResource) ateSignalView.resource()).getFile());
		this.ateSignalView=ateSignalView;
	}
	public ATESignalTypeView getFault() {
		return ateSignalView;
	}
	
	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
		memento.putString(EditorInputFactory.KEY_ELEMENT, LMFResource.encodeReference(ateSignalView));
	}
	
	@Override
	public String getFactoryId() {
		// 注意！这个地方返回的字符串必须和plugin.xml里面配置的elementFactory扩展点的id一致。
		return "signaltypeview.editorinputfactory";
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ManagedObject.class) {
			return ateSignalView;
		} else {
			return super.getAdapter(adapter);
		}
	}
	
	@Override
	public String getName() {
		return ateSignalView.getName().isEmpty() ? "Untitled" : ateSignalView.getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SignalViewEditorInput) {
			return ((SignalViewEditorInput) obj).ateSignalView == this.ateSignalView;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return ateSignalView.hashCode();
	}


}
