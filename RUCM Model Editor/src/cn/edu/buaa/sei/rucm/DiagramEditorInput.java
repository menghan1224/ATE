package cn.edu.buaa.sei.rucm;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;

import ca.carleton.sce.squall.ucmeta.UCDiagram;
import ca.carleton.sce.squall.ucmeta.UCModel;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.editor.LMFEditorInput;
import cn.edu.buaa.sei.lmf.runtime.FileResource;

public class DiagramEditorInput extends LMFEditorInput {
	
	private final UCDiagram diagram;
	
	public DiagramEditorInput(UCDiagram diagram) throws LMFResourceException {
		this(diagram, diagram.owner() instanceof UCModel ? (UCModel) diagram.owner() : null);
	}
	
	public DiagramEditorInput(UCDiagram diagram, UCModel ucModel) throws LMFResourceException {
		super(((FileResource) diagram.resource()).getFile());
		this.diagram = diagram;
		if (this.diagram.getUcModel() != ucModel) {
			this.diagram.setUcModel(ucModel);
		}
	}
	
	public final UCDiagram getDiagram() {
		return diagram;
	}
	
	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
		memento.putString(EditorInputFactory.KEY_ELEMENT, LMFResource.encodeReference(diagram));
	}
	
	@Override
	public String getFactoryId() {
		return "cn.edu.buaa.sei.rucm.editorInputFactory";
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ManagedObject.class) {
			return diagram;
		} else {
			return super.getAdapter(adapter);
		}
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromImage(RUCMPlugin.getBundleImage("icons/elements_obj.gif"));
		
	}
	
	@Override
	public String getName() {
		return diagram.getName().isEmpty() ? "Untitled" : diagram.getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DiagramEditorInput) {
			return ((DiagramEditorInput) obj).diagram == this.diagram;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return diagram.hashCode();
	}

}
