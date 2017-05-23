package cn.edu.buaa.sei.rucm.spec.rows;

import ca.carleton.sce.squall.ucmeta.GlobalAlternative;
import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.UCMetaSentenceFactory;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import cn.edu.buaa.sei.rucm.spec.widgets.EmbeddedRow;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.RowLabel;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

public class GuardConditionRow extends EmbeddedRow {
	
	public GuardConditionRow(GlobalAlternative flow) {
		super(new PropertyTableViewRow<GlobalAlternative>("", flow, GlobalAlternative.KEY_CONDITIONSENTENCE) {
			
			{ getValueColumnView().setHintText("Guard Condition"); }
			
			@Override public RowLabel getKeyColumnView() { return null; }
			@Override public int getKeyColumnPreferredWidth(TableView table) { return -1; }
			
			@Override
			protected String sycnPropertyToText(GlobalAlternative model) {
				Sentence condition = model.getConditionSentence();
				if (condition == null) return "";
				else return condition.getContent();
			}

			@Override
			protected void sycnTextToProperty(final String text) {
				Sentence condition = getModel().getConditionSentence();
				if (condition == null) {
					condition = UCMetaSentenceFactory.createSentence();
					getModel().setConditionSentence(condition);
				}
				condition.setContent(text);
			}
			
			@Override
			public void tableViewRowAdded(TableView table) {
				super.tableViewRowAdded(table);
				initDecorator(ElementType.GUARD_CONDITION);
			}
			
		});
	}

}
