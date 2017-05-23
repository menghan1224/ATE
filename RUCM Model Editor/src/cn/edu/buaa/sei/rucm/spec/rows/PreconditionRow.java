package cn.edu.buaa.sei.rucm.spec.rows;

import java.util.List;

import cn.edu.buaa.sei.lmf.AttributeSetter;
import cn.edu.buaa.sei.lmf.CascadeObserver;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

import ca.carleton.sce.squall.ucmeta.PreCondition;
import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.UCMetaSentenceFactory;
import ca.carleton.sce.squall.ucmeta.UCMetaTemplateFactory;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import ca.carleton.sce.squall.ucmeta.util.SentenceUtility;
import ca.carleton.sce.squall.ucmeta.util.TextRange;

public class PreconditionRow extends PropertyTableViewRow<UseCaseSpecification> {

	private final CascadeObserver sentenceObserver;
	
	public PreconditionRow(String displayName, UseCaseSpecification model) {
		super(displayName, model, UseCaseSpecification.KEY_PRECONDITION);
		getValueColumnView().setHintText("None");
		sentenceObserver = new CascadeObserver(model, UseCaseSpecification.KEY_PRECONDITION, PreCondition.KEY_SENTENCES) {
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
		PreCondition condition = model.getPreCondition();
		if (condition == null) return "";
		else return SentenceUtility.concatSentences(condition.getSentences());
	}
	
	@Override
	protected void sycnTextToProperty(final String text) {
		PreCondition condition = getModel().getPreCondition();
		if (condition == null) {
			condition = UCMetaTemplateFactory.createPreCondition();
			getModel().setPreCondition(condition);
		}
		final List<Sentence> sentences = condition.getSentences();
		condition.set(new AttributeSetter() {
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
		initDecorator(ElementType.PRECONDITION);
	}

}
