package cn.edu.buaa.sei.rucm.spec.rows;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.BriefDescription;
import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.UCMetaSentenceFactory;
import ca.carleton.sce.squall.ucmeta.UCMetaTemplateFactory;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import ca.carleton.sce.squall.ucmeta.util.SentenceUtility;
import ca.carleton.sce.squall.ucmeta.util.TextRange;
import cn.edu.buaa.sei.lmf.AttributeSetter;
import cn.edu.buaa.sei.lmf.CascadeObserver;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

public class BriefDescriptionRow extends PropertyTableViewRow<UseCaseSpecification> {

	private final CascadeObserver sentenceObserver;
	
	public BriefDescriptionRow(String displayName, UseCaseSpecification model) {
		super(displayName, true, model, UseCaseSpecification.KEY_BRIEFDESCRIPTION);
		getValueColumnView().setHintText("None");
		sentenceObserver = new CascadeObserver(model, UseCaseSpecification.KEY_BRIEFDESCRIPTION, BriefDescription.KEY_SENTENCES) {
			@Override
			protected void notifyChanged(ManagedObject value) {
				updateTextCell();
			}
		};
	}
	
	@Override
	public void tableViewRowRemoved(TableView table) {
		sentenceObserver.removeFromTarget();
		super.tableViewRowRemoved(table);
	}
	
	@Override
	protected String sycnPropertyToText(UseCaseSpecification model) {
		BriefDescription description = model.getBriefDescription();
		if (description == null) return "";
		else return SentenceUtility.concatSentences(description.getSentences());
	}
	
	@Override
	protected void sycnTextToProperty(final String text) {
		BriefDescription description = getModel().getBriefDescription();
		if (description == null) {
			description = UCMetaTemplateFactory.createBriefDescription();
			getModel().setBriefDescription(description);
		}
		final List<Sentence> sentences = description.getSentences();
		description.set(new AttributeSetter() {
			@Override
			public void apply(ManagedObject target) {
				sentences.clear();
				for (TextRange range : SentenceUtility.splitSentences(text)) {
					Sentence sentence = UCMetaSentenceFactory.createSentence();
					sentence.setContent(range.getText().trim());
					sentences.add(sentence);
				}
			}
		});
	}
	
	@Override
	public void tableViewRowAdded(TableView table) {
		super.tableViewRowAdded(table);
		initDecorator(ElementType.BRIEF_DESCRIPTION);
	}

}
