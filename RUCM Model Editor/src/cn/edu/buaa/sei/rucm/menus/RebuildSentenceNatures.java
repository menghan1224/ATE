package cn.edu.buaa.sei.rucm.menus;

import java.util.List;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.lmf.LMFUtility.ObjectFilter;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.edit.MenuContributor;
import cn.edu.buaa.sei.lmf.edit.Node;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

import ca.carleton.sce.squall.ucmeta.FlowOfEvents;
import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.SentenceNature;
import ca.carleton.sce.squall.ucmeta.util.CompositeSentenceMismatchException;
import ca.carleton.sce.squall.ucmeta.util.FlowUtility;
import ca.carleton.sce.squall.ucmeta.util.IncorrectKeywordsUsageException;
import ca.carleton.sce.squall.ucmeta.util.SentencePatternDismatchException;
import ca.carleton.sce.squall.ucmeta.util.SentenceUtility;

public class RebuildSentenceNatures extends ContributionItem {
	
	public static class Contributor implements MenuContributor {
		
//		private final RebuildSentenceNatures item = new RebuildSentenceNatures();

		@Override
		public IContributionItem[] createMenuItems() {
//			ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
//			if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
//				Object obj = ((IStructuredSelection) sel).getFirstElement();
//				if (obj instanceof ObjectNode) {
//					if (((ObjectNode) obj).getObject() instanceof UCModel) {
//						return new IContributionItem[] { item };
//					}
//				}
//			}
			return new IContributionItem[0];
		}
		
	}

	public RebuildSentenceNatures() {
	}

	public RebuildSentenceNatures(String id) {
		super(id);
	}
	
	@Override
	public void fill(Menu menu, int index) {
		// get current selection
		Node selectedNode = null;
		ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) sel).getFirstElement();
			if (obj instanceof Node) {
				selectedNode = (Node) obj;
			}
		}
		
		// build delete menu
		MenuItem menuItem = new MenuItem(menu, SWT.NONE, index);
		menuItem.setText("Rebuild Sentence Natures");
		menuItem.setImage(RUCMPlugin.getBundleImage("icons/genericvariable_obj.gif"));
		if (selectedNode instanceof ObjectNode) {
			final ObjectNode onode = (ObjectNode) selectedNode;
			// build menu
			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					updateNatures(onode.getObject());
				}
			});
		} else {
			menuItem.setEnabled(false);
		}
	}
	
	private void updateNatures(ManagedObject obj) {
		ManagedObject[] sentences = LMFUtility.findObjects(obj, new ObjectFilter() {
			@Override
			public boolean accept(ManagedObject obj) {
				return obj.isKindOf(Sentence.TYPE_NAME);
			}
		});
		for (ManagedObject sentence : sentences) {
			updateSentenceNatures((Sentence) sentence);
		}
		ManagedObject[] flows = LMFUtility.findObjects(obj, new ObjectFilter() {
			@Override
			public boolean accept(ManagedObject obj) {
				return obj.isKindOf(FlowOfEvents.TYPE_NAME);
			}
		});
		for (ManagedObject flow : flows) {
			updateFlowNatures((FlowOfEvents) flow);
		}
	}
	
	private void updateSentenceNatures(Sentence sentence) {
		List<SentenceNature> natures = null;
		try {
			natures = SentenceUtility.parseSentence(sentence.getContent());
		} catch (SentencePatternDismatchException ex) {
			RUCMPlugin.logError(ex, true);
		} catch (IncorrectKeywordsUsageException ex) {
			RUCMPlugin.logError(ex, true);
		}
		List<SentenceNature> target = sentence.getNatures();
		target.clear();
		if (natures != null) target.addAll(natures);
	}
	
	private void updateFlowNatures(FlowOfEvents flow) {
		try {
			FlowUtility.rebuildCompositeNatures(flow);
		} catch (CompositeSentenceMismatchException ex) {
			RUCMPlugin.logError(ex, true);
		}
	}
	
}
