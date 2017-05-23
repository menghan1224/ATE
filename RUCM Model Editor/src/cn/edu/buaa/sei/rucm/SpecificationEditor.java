package cn.edu.buaa.sei.rucm;

import java.awt.Container;

import javax.swing.SwingUtilities;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import ca.carleton.sce.squall.ucmeta.UseCase;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Observer;
import cn.edu.buaa.sei.rucm.spec.SpecificationView;
import co.gongzh.snail.ViewContext;

public class SpecificationEditor extends SnailLMFEditorBase {
	
	private ViewContext viewContext;
	private SpecificationView specView;
	private UseCase ownerUseCase;
	
	private final Observer nameObserver = new Observer() {
		@Override
		public void notifyChanged(ManagedObject target, String key, ManagedObject value) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					setPartName(getEditorInput().getName());
				}
			});
		}
	};
	
	public SpecificationEditor() {
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		if (input instanceof SpecificationEditorInput) {
			UseCaseSpecification spec = getEditorInput().getSpecification();
			setPartName(getEditorInput().getName());
			if (spec.owner() != null &&
				spec.owner().isKindOf(UseCase.TYPE_NAME)) {
				ownerUseCase = (UseCase) spec.owner();
				ownerUseCase.addObserver(UseCase.KEY_NAME, nameObserver);
			}
			setTitleImage(RUCMPlugin.getBundleImage("icons/properties.gif"));
		} else {
			throw new PartInitException("Specification editor only accepts SpecificationEditorInput.");
		}
	}
	
	@Override
	public SpecificationEditorInput getEditorInput() {
		return (SpecificationEditorInput) super.getEditorInput();
	}
	
	@Override
	protected ViewContext createPartControl(Container container) {
		specView = new SpecificationView(this);
		viewContext = new ViewContext(container, specView);
		specView.setSpecification(getEditorInput().getSpecification());
		return viewContext;
	}
	
	@Override
	public void dispose() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				specView.setSpecification(null, true);
				viewContext.dispose();
				viewContext = null;
			}
		});
		if (ownerUseCase != null) {
			ownerUseCase.removeObserver(UseCase.KEY_NAME, nameObserver);
			ownerUseCase = null;
		}
		super.dispose();
	}

	public SpecificationView getSnailView() {
		return specView;
	}
	
}
