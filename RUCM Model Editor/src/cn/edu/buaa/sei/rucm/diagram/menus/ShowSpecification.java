package cn.edu.buaa.sei.rucm.diagram.menus;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ca.carleton.sce.squall.ucmeta.UCDUseCaseNode;
import ca.carleton.sce.squall.ucmeta.UCMetaTemplateFactory;
import ca.carleton.sce.squall.ucmeta.UseCase;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.SpecificationEditorInput;
import cn.edu.buaa.sei.rucm.diagram.ContextMenu;
import cn.edu.buaa.sei.rucm.diagram.DiagramSelection;
import cn.edu.buaa.sei.rucm.diagram.DiagramSelection.SelectionType;
import cn.edu.buaa.sei.rucm.diagram.DiagramView;
import cn.edu.buaa.sei.rucm.diagram.widgets.MenuItems.LabelItem;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import co.gongzh.snail.MouseEvent;

public class ShowSpecification extends LabelItem {
	
	public ShowSpecification() {
		super(RUCMPluginResource.getImage("properties.gif"), "Show Specification");
	}
	
	@Override
	protected void mouseClicked(MouseEvent e) {
		ContextMenu menu = getSuperViewInHierarchy(ContextMenu.class);
		DiagramView diagramView = menu.getDiagramView();
		DiagramSelection sel = diagramView.getSelection();
		if (sel.getType() == SelectionType.NODE && sel.getNode() instanceof UCDUseCaseNode) {
			final UseCase useCase = ((UCDUseCaseNode) sel.getNode()).getUseCase();
			if (useCase != null) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						UseCaseSpecification specification = useCase.getSpecification();
						if (specification == null) {
							// create a default specification
							specification = UCMetaTemplateFactory.createUseCaseSpecification();
							useCase.setSpecification(specification);
						}
						try {
							SpecificationEditorInput input = new SpecificationEditorInput(specification);
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, "cn.edu.buaa.sei.rucm.specEditor", true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
						} catch (PartInitException ex) {
							RUCMPlugin.logError(ex, true);
						} catch (LMFResourceException ex) {
							RUCMPlugin.logError(ex, true);
						}
					}
				});
			}
		}
		e.handle();
		menu.dismiss();
	}

}
