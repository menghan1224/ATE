package cn.edu.buaa.sei.rucm.spec.rows;

import java.awt.Color;
import java.util.List;

import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.SentenceNature;
import ca.carleton.sce.squall.ucmeta.util.IncorrectKeywordsUsageException;
import ca.carleton.sce.squall.ucmeta.util.SentencePatternDismatchException;
import ca.carleton.sce.squall.ucmeta.util.SentenceUtility;
import cn.edu.buaa.sei.rucm.spec.StepTable;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import cn.edu.buaa.sei.rucm.spec.widgets.DropDownButton;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.RowLabel;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import co.gongzh.snail.text.CaretIndex;
import co.gongzh.snail.util.Alignment;
import co.gongzh.snail.util.Insets;

public class StepRow extends PropertyTableViewRow<Sentence> {
	
	static final Color LABEL_BG_COLOR = new Color(0xe5ecfb);
//	static final Color LABEL_TEXT_COLOR = new Color(0x5b5b5b);

	private int stepNumber;
	private final StepTable stepTable;
	
	public StepRow(Sentence model, StepTable stepTable) {
		super("0", model, Sentence.KEY_CONTENT);
		this.stepTable = stepTable;
		this.stepNumber = 0;
	}
	
	public int getStepNumber() {
		return stepNumber;
	}
	
	public void setStepNumber(int stepNumber) {
		if (this.stepNumber != stepNumber) {
			this.stepNumber = stepNumber;
			getKeyColumnView().getTextView().setText(String.valueOf(stepNumber));
			stepTable.layout();
		}
	}
	
	@Override
	protected RowLabel createLabel() {
		RowLabel label = super.createLabel();
		label.setBackgroundColor(LABEL_BG_COLOR);
		label.getTextView().setTextAlignment(Alignment.CENTER_CENTER);
		label.getTextView().setDefaultTextColor(LABEL_TEXT_COLOR);
		label.setInsets(Insets.make(3, 4, 3, 4));
		return label;
	}

	@Override
	protected String sycnPropertyToText(Sentence model) {
		return model.getContent();
	}

	@Override
	protected void sycnTextToProperty(String text) throws PropertyRowException {
		try {
			// parse the sentence
			List<SentenceNature> natures = SentenceUtility.parseSentence(getValueColumnView().getTokens());
			List<SentenceNature> target = getModel().getNatures();
			target.clear();
			target.addAll(natures);
		} catch (SentencePatternDismatchException ex) {
			// TODO: add error information
			throw new PropertyRowException();
		} catch (IncorrectKeywordsUsageException ex) {
			// TODO: add error information
			throw new PropertyRowException();
		} finally {
			getModel().setContent(text);
			stepTable.pushToModel();
		}
	}
	
	@Override
	public void cellBackspacePressed(TextCell c) {
		super.cellBackspacePressed(c);
		int rowIndex = stepTable.indexOfRow(this);
		if (rowIndex > 0) {
			String text = getValueColumnView().getPlainText();
			stepTable.deleteRow(stepTable.indexOfRow(this), true);
			TextCell cell = stepTable.getRow(rowIndex - 1).getValueColumnView();
			cell.setText(cell.getPlainText() + text);
			cell.setCaretPosition(CaretIndex.before(cell.textLength() - text.length()));
		}
	}
	
	@Override
	public void cellEnterPressed(TextCell c) {
		super.cellEnterPressed(c);
		int index = getValueColumnView().getCaretPosition().toBefore().charIndex;
		String text = getValueColumnView().getPlainText();
		getValueColumnView().setText(text.substring(0, index));
		stepTable.addRow(stepTable.indexOfRow(this) + 1, text.substring(index), true);
	}
	
	@Override
	public void tableViewRowAdded(TableView table) {
		super.tableViewRowAdded(table);
		initDecorator(ElementType.STEP);
	}
	
}
