package cn.edu.buaa.sei.rucm.spec;

import java.util.List;

import ca.carleton.sce.squall.ucmeta.FlowOfEvents;
import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.UCMetaSentenceFactory;
import ca.carleton.sce.squall.ucmeta.util.CompositeSentenceMismatchException;
import ca.carleton.sce.squall.ucmeta.util.FlowUtility;
import cn.edu.buaa.sei.lmf.AttributeSetter;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.spec.rows.StepRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import co.gongzh.snail.Animation;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.text.CaretIndex;

public class StepTable extends TableView {
	
	private TableView ownerTable;
	private final FlowOfEvents flow;
	
	// free-form support
	private boolean freeformMode;
	private FreeFormStepCell freeFormCell;
	
	public StepTable(FlowOfEvents flow) {
		setOuterGridColor(null);
		this.flow = flow;
		addEventHandler(LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				if (ownerTable != null) {
					ownerTable.layout();
				}
			}
		});
		initRows();
		if (flow.getSteps().isEmpty()) {
			// create default rows
			for (int i = 0; i < 3; i++) {
				Sentence sentence = UCMetaSentenceFactory.createSentence();
				flow.getSteps().add(sentence);
				StepRow row = new StepRow(sentence, this);
				row.setStepNumber(i + 1);
				addRow(row);
			}
		}
		
		// update indentation
		int[] indentation = FlowUtility.getIndentations(flow);
		for (int i = 0; i < indentation.length; i++) {
			getRow(i).getValueColumnView().setIndentation(indentation[i]);
		}
		// XXX: support step observer in the future
		
		this.freeformMode = false;
		this.freeFormCell = null;
	}

	private void dispose() {
		StepTable.this.removeAllRows();
	}
	
	private void initRows() {
		int i = 1;
		for (Sentence sentence : flow.getSteps()) {
			StepRow row = new StepRow(sentence, this);
			row.setStepNumber(i);
			addRow(row);
			i++;
		}
	}
	
	@Override
	public StepRow getRow(int index) {
		return (StepRow) super.getRow(index);
	}
	
	public void addRow(int index, String sentence, boolean autoFocus) {
		Sentence st = UCMetaSentenceFactory.createSentence();
		st.setContent(sentence);
		StepRow row = new StepRow(st, this);
		addRow(index, row);
		for (int i = index; i < getRowCount(); i++) {
			getRow(i).setStepNumber(i + 1);
		}
		pushToModel();
		if (autoFocus) {
			TextCell cell = row.getValueColumnView();
			cell.requestKeyboardFocus();
			cell.setCaretPosition(CaretIndex.before(0));
		}
	}
	
	public void deleteRow(int index, boolean autoFocus) {
		if (getRowCount() > 1) {
			removeRow(index);
			for (int i = index; i < getRowCount(); i++) {
				getRow(i).setStepNumber(i + 1);
			}
			pushToModel();
			if (autoFocus) {
				if (index > 0) {
					TextCell cell = getRow(index - 1).getValueColumnView();
					cell.requestKeyboardFocus();
					cell.setCaretPosition(CaretIndex.before(cell.textLength()));
				}
			}
		}
	}
	
	public void resetRows(String[] lines) {
		removeAllRows();
		for (int i = 0; i < lines.length; i++) {
			Sentence st = UCMetaSentenceFactory.createSentence();
			st.setContent(lines[i]);
			StepRow row = new StepRow(st, this);
			addRow(row);
			row.setStepNumber(i + 1);
		}
		pushToModel();
	}
	
	public void pushToModel() {
		// reset flow model
		// XXX: do not reset model in the future
		final List<Sentence> sentences = flow.getSteps();
		flow.set(new AttributeSetter() {
			@Override
			public void apply(ManagedObject target) {
				sentences.clear();
				for (TableViewRow row : rows()) {
					sentences.add(((StepRow) row).getModel());
				}
			}
		});
		
		// update composite natures
		try {
			FlowUtility.rebuildCompositeNatures(flow);
		} catch (CompositeSentenceMismatchException ex) {
			// TODO: show composite error
		}
		
		// update indentation
		int[] indentation = FlowUtility.getIndentations(flow);
		for (int i = 0; i < indentation.length; i++) {
			getRow(i).getValueColumnView().setIndentation(indentation[i]);
		}
		
		// XXX: flow number changed, sync with other flows ?
	}
	
	public TableViewRow getWrapperRow() {
		return wrapperRow;
	}
	
	public FlowOfEvents getFlow() {
		return flow;
	}
	
	private final TableViewRow wrapperRow = new TableViewRow() {
		
		@Override
		public void tableViewRowAdded(TableView table) {
			ownerTable = table;
		}
		
		@Override
		public void tableViewRowRemoved(TableView table) {
			ownerTable = null;
			StepTable.this.dispose();
		}
		
		@Override public View getValueColumnView() { return StepTable.this; }
		@Override public View getKeyColumnView() { return null; }
		@Override public int getKeyColumnPreferredWidth(TableView table) { return -1; }
		
	};
	
	public boolean isFreeformMode() {
		return freeformMode;
	}
	
	public void setFreeformMode(boolean freeformMode) {
		if (freeformMode == this.freeformMode) return;
		this.freeformMode = freeformMode;
		if (freeformMode) {
			// enter free-form mode
			freeFormCell = new FreeFormStepCell(flow.getSteps()) {
				@Override
				protected void buttonClicked(boolean cancel) {
					if (!cancel) {
						// update rows
						resetRows(getLines());
					}
					setFreeformMode(false);
				}
			};
			addSubview(freeFormCell);
			new Animation(0.3f) {
				@Override
				protected void animate(float progress) {
					freeFormCell.setAlpha(progress);
				}
			}.commit();
			layout();
		} else {
			// exit free-form mode
			freeFormCell.removeFromSuperView();
			freeFormCell = null;
			layout();
		}
	}
	
	@Override
	public int getPreferredHeight() {
		if (!isFreeformMode()) {
			return super.getPreferredHeight();
		} else {
			return Math.max(super.getPreferredHeight(), 200);
		}
	}
	
	@Override
	protected void layoutView() {
		super.layoutView();
		if (freeFormCell != null) {
			View.scaleViewWithMarginToSuperView(freeFormCell, 0);
		}
	}
	
}
