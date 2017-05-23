package cn.edu.buaa.sei.ate.iodefivieweditor;

import java.awt.Color;
import java.awt.Container;

import org.eclipse.ui.IMemento;

import cn.edu.buaa.sei.ate.metamodel.ATEIODefinitionView;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.editor.LMFEditorInput;
import cn.edu.buaa.sei.lmf.runtime.FileResource;
import cn.edu.buaa.sei.rucm.EditorInputFactory;
import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.SnailLMFEditorBase;
import cn.edu.buaa.sei.rucm.SnailLMFEditorBase.SnailEditorView;
import cn.edu.buaa.sei.rucm.spec.SpecificationView;
import cn.edu.buaa.sei.rucm.spec.widgets.DropShadow;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionBar;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionScrollView;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Insets;

public class IOViewEditorInput extends LMFEditorInput{
	private final ATEIODefinitionView aTEIODefinitionView;
	

	public IOViewEditorInput(ATEIODefinitionView ATEIODefinitionView) throws LMFResourceException {
		
		// TODO Auto-generated constructor stub
		super(((FileResource) ATEIODefinitionView.resource()).getFile());
		this.aTEIODefinitionView=ATEIODefinitionView;
	}
	public ATEIODefinitionView getFault() {
		return aTEIODefinitionView;
	}
	
	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
		memento.putString(EditorInputFactory.KEY_ELEMENT, LMFResource.encodeReference(aTEIODefinitionView));
	}
	
	@Override
	public String getFactoryId() {
		// 注意！这个地方返回的字符串必须和plugin.xml里面配置的elementFactory扩展点的id一致。
		return "iodefinitionview.editorinputfactory";
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ManagedObject.class) {
			return aTEIODefinitionView;
		} else {
			return super.getAdapter(adapter);
		}
	}
	
	@Override
	public String getName() {
		return aTEIODefinitionView.getName().isEmpty() ? "Untitled" : aTEIODefinitionView.getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IOViewEditorInput) {
			return ((IOViewEditorInput) obj).aTEIODefinitionView == this.aTEIODefinitionView;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return aTEIODefinitionView.hashCode();
	}


}

	

