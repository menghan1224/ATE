package cn.edu.buaa.sei.rucm;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;

import ca.carleton.sce.squall.ucmeta.UseCase;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.editor.LMFEditorInput;
import cn.edu.buaa.sei.lmf.runtime.FileResource;

public class SpecificationEditorInput extends LMFEditorInput {
	
	private final UseCaseSpecification specification;

	public SpecificationEditorInput(UseCaseSpecification specification) throws LMFResourceException {
		super(((FileResource) specification.resource()).getFile());
		this.specification = specification;
	}
	
	public final UseCaseSpecification getSpecification() {
		return specification;
	}
	
	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
		memento.putString(EditorInputFactory.KEY_ELEMENT, LMFResource.encodeReference(specification));
	}
	
	@Override
	public String getFactoryId() {
		return "cn.edu.buaa.sei.rucm.editorInputFactory";
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ManagedObject.class) {
			return specification;
		} else {
			return super.getAdapter(adapter);
		}
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromImage(RUCMPlugin.getBundleImage("icons/properties.gif"));
		
	}
	
	@Override
	public String getName() {
		if (specification.owner() != null &&
			specification.owner().isKindOf(UseCase.TYPE_NAME)) {
			String name = specification.owner().get(UseCase.KEY_NAME).stringValue();
			return name.isEmpty() ? "Untitled" : name;
		} else {
			return "Untitled";
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SpecificationEditorInput) {
			return ((SpecificationEditorInput) obj).specification == this.specification;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return specification.hashCode();
	}

}
