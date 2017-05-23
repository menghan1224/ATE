package cn.edu.buaa.sei.lmf.navigator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;

import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.runtime.FileResource;

public class FileModifiedDecorator extends LabelProvider implements ILightweightLabelDecorator {
	
	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof IFile) {
			FileResource resource = (FileResource) LMFResource.getResource(((IFile) element).getFullPath().toString());
			if (resource != null && resource.isDirty()) {
				decoration.addSuffix(" *");
			}
		}
	}
	
}