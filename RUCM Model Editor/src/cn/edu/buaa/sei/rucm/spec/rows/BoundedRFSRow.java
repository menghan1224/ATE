package cn.edu.buaa.sei.rucm.spec.rows;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.BoundedAlternative;
import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.SentenceNature;
import ca.carleton.sce.squall.ucmeta.UCMetaSentenceFactory;
import ca.carleton.sce.squall.ucmeta.util.SentencePattern;
import ca.carleton.sce.squall.ucmeta.util.SentencePatternDismatchException;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import cn.edu.buaa.sei.rucm.spec.widgets.EmbeddedRow;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.RowLabel;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

public class BoundedRFSRow extends EmbeddedRow {
	
	public BoundedRFSRow(BoundedAlternative flow) {
		super(new PropertyTableViewRow<BoundedAlternative>("", flow, BoundedAlternative.KEY_RFSSENTENCE) {
			
			{ getValueColumnView().setHintText("RFS"); }
			
			@Override public RowLabel getKeyColumnView() { return null; }
			@Override public int getKeyColumnPreferredWidth(TableView table) { return -1; }
			
			@Override
			protected String sycnPropertyToText(BoundedAlternative model) {
				Sentence rfs = model.getRfsSentence();
				if (rfs == null) return "";
				else return rfs.getContent();
			}

			@Override
			protected void sycnTextToProperty(final String text) throws PropertyRowException {
				Sentence rfs = getModel().getRfsSentence();
				if (rfs == null) {
					rfs = UCMetaSentenceFactory.createSentence();
					
					getModel().setRfsSentence(rfs);
				}
				rfs.setContent(text);
				
				// parse the RFS sentence
				try {
					List<SentenceNature> natures = SentencePattern.RFS_PATTERN.match(getValueColumnView().getTokens());
					List<SentenceNature> target = rfs.getNatures();
					target.clear();
					target.addAll(natures);
				} catch (SentencePatternDismatchException ex) {
					// TODO: add error information
					throw new PropertyRowException();
				}
			}
			
			@Override
			public void tableViewRowAdded(TableView table) {
				super.tableViewRowAdded(table);
				initDecorator(ElementType.RFS);
			}
			
		});
	}

}
