package cn.edu.buaa.sei.rucm.spec.commands;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import ca.carleton.sce.squall.ucmeta.Actor;
import ca.carleton.sce.squall.ucmeta.AlternativeFlow;
import ca.carleton.sce.squall.ucmeta.BoundedAlternative;
import ca.carleton.sce.squall.ucmeta.Extend;
import ca.carleton.sce.squall.ucmeta.FlowOfEvents;
import ca.carleton.sce.squall.ucmeta.Generalization;
import ca.carleton.sce.squall.ucmeta.GlobalAlternative;
import ca.carleton.sce.squall.ucmeta.Include;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.SpecificAlternative;
import ca.carleton.sce.squall.ucmeta.UseCase;
import ca.carleton.sce.squall.ucmeta.UseCaseClassifier;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import ca.carleton.sce.squall.ucmeta.util.Keyword;
import ca.carleton.sce.squall.ucmeta.util.SentenceUtility;
import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.LMFUtility.ObjectFilter;
import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.SpecificationEditor;

public class SaveSpecAsText extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (part instanceof SpecificationEditor) {
			final UseCaseSpecification specification = ((SpecificationEditor) part).getSnailView().getSpecification();
			final UseCase ownerUseCase = ((SpecificationEditor) part).getSnailView().getOwnerUseCase();
			// open SWT file chooser dialog
			FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.SAVE);
			dialog.setFilterNames (new String [] {"Text File", "All Files (*.*)"});
			dialog.setFilterExtensions (new String [] {"*.txt", "*.*"});
			final String filepath = dialog.open();
			if (filepath != null) {
				FileWriter writer = null;
				try {
					writer = new FileWriter(filepath);
					writeTextContent(writer, specification, ownerUseCase);
				} catch (IOException ex) {
					RUCMPlugin.logError(ex, true);
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException ex) {
						}
					}
				}
			}
		}
		return null;
	}
	
	private void writeTextContent(FileWriter writer, UseCaseSpecification spec, UseCase useCase) throws IOException {
		// TODO
		writer.append(String.format("Use Case Name: %s\n", useCase.getName()));
		writer.append(String.format("Brief Description: %s\n", SentenceUtility.concatSentences(spec.getBriefDescription().getSentences())));
		writer.append(String.format("Precondition: %s\n", SentenceUtility.concatSentences(spec.getPreCondition().getSentences())));
		writer.append(String.format("Primary Actor: %s\n", spec.getPrimaryActor() != null ? spec.getPrimaryActor().getName() : "None"));
		writer.append(String.format("Secondary Actors: %s\n", spec.getSecondaryActors().size() > 0 ? getSecondaryActorsString(spec.getSecondaryActors()) : "None"));
		writer.append(String.format("Dependency: %s\n", getDependencyString(useCase)));
		writer.append(String.format("Generalization: %s\n", getGeneralizationString(useCase)));
		
		writer.append("\nBasic Flow\n");
		writeSteps(writer, spec.getBasicFlow());
		writer.append(String.format("Postcondition: %s\n", SentenceUtility.concatSentences(spec.getBasicFlow().getPostCondition().getSentences())));
		
		for (AlternativeFlow flow : spec.getAlternativeFlows()) {
			String flowNameString = flow.getName().isEmpty() ? "" : String.format(" \"%s\"", flow.getName());
			if (flow instanceof SpecificAlternative) {
				writer.append(String.format("\nSpecific Alternative Flow%s\n", flowNameString));
				writer.append(((SpecificAlternative) flow).getRfsSentence().getContent());
				writer.append('\n');
			} else if (flow instanceof BoundedAlternative) {
				writer.append(String.format("\nBounded Alternative Flow%s\n", flowNameString));
				writer.append(((BoundedAlternative) flow).getRfsSentence().getContent());
				writer.append('\n');
			} else if (flow instanceof GlobalAlternative) {
				writer.append(String.format("\nGlobal Alternative Flow%s\n", flowNameString));
				writer.append(((GlobalAlternative) flow).getConditionSentence().getContent());
				writer.append('\n');
			} else {
				writer.append(String.format("\nAlternative Flow%s\n", flowNameString));
			}
			writeSteps(writer, spec.getBasicFlow());
			writer.append(String.format("Postcondition: %s\n", SentenceUtility.concatSentences(spec.getBasicFlow().getPostCondition().getSentences())));
		}
	}
	
	private void writeSteps(FileWriter writer, FlowOfEvents flow) throws IOException {
		List<Sentence> steps = flow.getSteps();
		for (int i = 0; i < steps.size(); i++) {
			writer.append(String.format("%d. %s\n", i + 1, steps.get(i).getContent()));
		}
	}
	
	private String getSecondaryActorsString(List<Actor> actors) {
		StringBuilder sb = new StringBuilder(actors.get(0).getName());
		for (int i = 1; i < actors.size(); i++) {
			sb.append(", ");
			sb.append(actors.get(i).getName());
		}
		return sb.toString();
	}
	
	private String getDependencyString(UseCase useCase) {
		List<ModelElement> dependencies = new ArrayList<ModelElement>();
		dependencies.addAll(useCase.getInclude());
		dependencies.addAll(useCase.getExtend());
		if (dependencies.isEmpty()) {
			return "None";
		} else {
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < dependencies.size(); i++) {
				if (i > 0) buf.append(", ");
				ModelElement dep = dependencies.get(i);
				if (dep.isKindOf(Include.TYPE_NAME)) {
					buf.append(Keyword.INCLUDE_USE_CASE.getName());
					buf.append(" ");
					buf.append(dep.get(Include.KEY_ADDITION).get(UseCase.KEY_NAME).stringValue());
				} else if (dep.isKindOf(Extend.TYPE_NAME)) {
					buf.append(Keyword.EXTENDED_BY_USE_CASE.getName());
					buf.append(" ");
					buf.append(dep.get(Extend.KEY_EXTENSION).get(UseCase.KEY_NAME).stringValue());
				}
			}
			return buf.toString();
		}
	}
	
	private String getGeneralizationString(UseCase useCase) {
		ManagedObject[] objects = getAllRelatedGeneralizations(useCase);
		if (objects.length == 0) {
			return "None";
		} else {
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < objects.length; i++) {
				if (i > 0) buf.append(", ");
				buf.append(objects[i].get(Generalization.KEY_GENERAL).get(UseCaseClassifier.KEY_NAME).stringValue());
			}
			return buf.toString();
		}
	}
	
	private ManagedObject[] getAllRelatedGeneralizations(final UseCase useCase) {
		return LMFUtility.findObjects(useCase.resource().getRootObject(), new ObjectFilter() {
			@Override
			public boolean accept(ManagedObject obj) {
				return obj.isKindOf(Generalization.TYPE_NAME) &&
						obj.get(Generalization.KEY_SPECIFIC) == useCase &&
						obj.get(Generalization.KEY_GENERAL) != null;
			}
		});
	}

}
