package cn.edu.buaa.sei.rucm.spec.rows;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.FlowOfEvents;
import ca.carleton.sce.squall.ucmeta.PostCondition;
import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.UCMetaSentenceFactory;
import ca.carleton.sce.squall.ucmeta.UCMetaTemplateFactory;
import ca.carleton.sce.squall.ucmeta.util.SentenceUtility;
import ca.carleton.sce.squall.ucmeta.util.TextRange;
import cn.edu.buaa.sei.lmf.AttributeSetter;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import cn.edu.buaa.sei.rucm.spec.widgets.EmbeddedRow;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

public class PostconditionRow extends EmbeddedRow {
	
	public PostconditionRow(FlowOfEvents flow) {
		super(new PropertyTableViewRow<FlowOfEvents>("Postcondition", flow, FlowOfEvents.KEY_POSTCONDITION) {
			
			{ getValueColumnView().setHintText("None"); }
			
			@Override
			protected String sycnPropertyToText(FlowOfEvents model) {
				PostCondition condition = model.getPostCondition();
				if (condition == null) return "";
				else return SentenceUtility.concatSentences(condition.getSentences());
			}

			@Override
			protected void sycnTextToProperty(final String text) {
				PostCondition condition = getModel().getPostCondition();
				if (condition == null) {
					condition = UCMetaTemplateFactory.createPostCondition();
					getModel().setPostCondition(condition);
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
				initDecorator(ElementType.POSTCONDITION);
			}
			
		});
	}

}
