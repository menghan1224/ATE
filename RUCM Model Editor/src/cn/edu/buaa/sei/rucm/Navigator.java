package cn.edu.buaa.sei.rucm;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import ca.carleton.sce.squall.ucmeta.UCDiagram;
import ca.carleton.sce.squall.ucmeta.UCMetaTemplateFactory;
import ca.carleton.sce.squall.ucmeta.UseCase;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.lmf.Attribute;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Primitives;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.ContainmentNode;
import cn.edu.buaa.sei.lmf.edit.Node;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.lmf.navigator.DoubleClickActionDelegate;

public class Navigator extends cn.edu.buaa.sei.lmf.navigator.Navigator {
	
	private ManagedObject copiedObject;
	
	@Override
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		IHandlerService serv = (IHandlerService) getSite().getService(IHandlerService.class);
		
		// copy
		IHandler copy = new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws ExecutionException {
				ManagedObject obj = getSelectedObject();
				copiedObject = obj;
				return null;
			}
		};
		serv.activateHandler(org.eclipse.ui.IWorkbenchCommandConstants.EDIT_COPY, copy);
		
		// paste
		IHandler paste = new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws ExecutionException {
				if (copiedObject != null) {
					Node node = getSelectedNode();
					if (node instanceof ContainmentNode) {
						ContainmentNode cn = (ContainmentNode) node;
						if (cn.getObject().resource() == copiedObject.resource()) {
							Attribute attr = cn.getAttribute();
							if (attr.getValueType() == Primitives.LIST) {
								Type targetType = attr.getValueTypeParameter();
								if (targetType == null || copiedObject.isKindOf(targetType)) {
									ManagedObject newObj = LMFUtility.deepCopyObject(copiedObject);
									cn.getObject().get(attr.getName()).listContent().add(newObj);
								}
							} else {
								if (copiedObject.isKindOf(attr.getValueType())) {
									ManagedObject newObj = LMFUtility.deepCopyObject(copiedObject);
									cn.getObject().set(attr.getName(), newObj);
								}
							}
						}
					}
				}
				return null;
			}
		};
		serv.activateHandler(org.eclipse.ui.IWorkbenchCommandConstants.EDIT_PASTE, paste);
	}
	
	private Node getSelectedNode() {
		Object selectedObject = null;
		ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
			selectedObject = ((IStructuredSelection) sel).getFirstElement();
			if (selectedObject instanceof Node) {
				return (Node) selectedObject;
			}
		}
		return null;
	}
	
	private ManagedObject getSelectedObject() {
		Node selectedNode = getSelectedNode();
		if (selectedNode != null && selectedNode instanceof ObjectNode) {
			ManagedObject obj = ((ObjectNode) selectedNode).getObject();
			return obj;
		}
		return null;
	}
	
	public static class StandardDoubleClickActionDelegate implements DoubleClickActionDelegate {

		@Override
		public boolean shouldHandleDoubleClickAction(ManagedObject obj) {
			return obj instanceof UCDiagram ||
					obj instanceof UseCase ||
					obj instanceof UseCaseSpecification; 
		}

		@Override
		public void handleDoubleClickAction(ManagedObject obj) {
			if (obj instanceof UCDiagram) {
				try {
					DiagramEditorInput input = new DiagramEditorInput((UCDiagram) obj);
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, "cn.edu.buaa.sei.rucm.diagramEditor", true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
				} catch (PartInitException ex) {
					RUCMPlugin.logError(ex, true);
				} catch (LMFResourceException ex) {
					RUCMPlugin.logError(ex, true);
				}
			} else if (obj instanceof UseCase) {
				UseCaseSpecification specification = ((UseCase) obj).getSpecification();
				if (specification == null) {
					// create a default specification
					specification = UCMetaTemplateFactory.createUseCaseSpecification();
					((UseCase) obj).setSpecification(specification);
				}
				try {
					SpecificationEditorInput input = new SpecificationEditorInput(specification);
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, "cn.edu.buaa.sei.rucm.specEditor", true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
				} catch (PartInitException ex) {
					RUCMPlugin.logError(ex, true);
				} catch (LMFResourceException ex) {
					RUCMPlugin.logError(ex, true);
				}
			} else if (obj instanceof UseCaseSpecification) {
				try {
					SpecificationEditorInput input = new SpecificationEditorInput((UseCaseSpecification) obj);
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, "cn.edu.buaa.sei.rucm.specEditor", true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
				} catch (PartInitException ex) {
					RUCMPlugin.logError(ex, true);
				} catch (LMFResourceException ex) {
					RUCMPlugin.logError(ex, true);
				}
			}
		}
		
	}

}
