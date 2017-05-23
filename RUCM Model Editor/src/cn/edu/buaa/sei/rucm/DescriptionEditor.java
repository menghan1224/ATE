package cn.edu.buaa.sei.rucm;

import java.awt.Color;
import java.awt.Container;

import javax.swing.SwingUtilities;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import ca.carleton.sce.squall.ucmeta.Actor;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import ca.carleton.sce.squall.ucmeta.UCModel;
import ca.carleton.sce.squall.ucmeta.UseCase;
import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Observer;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.rucm.SnailLMFEditorBase.SnailEditorView;
import cn.edu.buaa.sei.rucm.diagram.widgets.DataBinding;
import cn.edu.buaa.sei.rucm.diagram.widgets.LabeledTextBox;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.spec.SpecificationView;
import cn.edu.buaa.sei.rucm.spec.widgets.Cursor;
import cn.edu.buaa.sei.rucm.spec.widgets.DropShadow;
import cn.edu.buaa.sei.rucm.spec.widgets.RowLabel;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionScrollView;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCellDelegate;
import cn.edu.buaa.sei.rucm.spec.widgets.TextTableViewRow;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.text.TextView;
import co.gongzh.snail.util.Alignment;
import co.gongzh.snail.util.Insets;

public class DescriptionEditor extends SnailLMFEditorBase {
	
	private ViewContext viewContext;
	private DescriptionView descriptionView;
	
	public DescriptionEditor() {
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
	}

	@Override
	protected ViewContext createPartControl(Container container) {
		descriptionView = new DescriptionView((UCModel) getEditorInput().getResource().getRootObject());
		viewContext = new ViewContext(container, descriptionView);
		return viewContext;
	}
	
	@Override
	public void dispose() {
		if (getEditorInput().getResource().isLoaded()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					descriptionView.dispose();
					viewContext.dispose();
					viewContext = null;
				}
			});
		}
		super.dispose();
	}

	@Override
	public SnailEditorView getSnailView() {
		return descriptionView;
	}

}

class DescriptionView extends SnailEditorView {
	
	private final UCModel model;
	
	private final SectionScrollView scrollView;
	private final TitleSection titleSection;
	private final ElementSection useCaseSection;
	private final ElementSection actorSection;
	
	DescriptionView(final UCModel model) {
		this.model = model;
		setBackgroundColor(Color.WHITE);
		setPaintMode(PaintMode.DIRECTLY);
		scrollView = new SectionScrollView();
		addSubview(scrollView);
		
		titleSection = new TitleSection(model);
		titleSection.setInsets(Insets.make(20, 5, 10, 5));
		scrollView.addSection(titleSection);
		
		Insets insets = Insets.make(10, 12, 20, 18);
		
		useCaseSection = new ElementSection(model, LMFContext.typeForName(UseCase.TYPE_NAME), "Use Case");
		useCaseSection.setInsets(insets);
		scrollView.addSection(useCaseSection);
		
		actorSection = new ElementSection(model, LMFContext.typeForName(Actor.TYPE_NAME), "Actor");
		actorSection.setInsets(insets);
		scrollView.addSection(actorSection);
	}
	
	@Override
	protected void mouseReleased(MouseEvent e) {
		requestKeyboardFocus();
	}
	
	@Override
	protected void keyTyped(KeyEvent e) {
		// XXX: debug mode
		if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
			if (getViewContext().isDebugModeEnable()) {
				getViewContext().setDebugModeEnable(false);
			} else {
				getViewContext().setDebugModeEnable(true);
			}
		}
	}
	
	@Override
	protected void preKeyTyped(KeyEvent e) {
		// save resource
		if (e.isMetaOrCtrlDown() && (e.getKeyChar() == 's' || e.getKeyChar() == 'S')) {
			try {
				model.resource().save();
			} catch (LMFResourceException ex) {
				RUCMPlugin.logError(ex, true);
			}
			e.handle();
		}
	}
	
	@Override
	protected void layoutView() {
		View.scaleViewWithMarginToSuperView(scrollView, 0);
	}

	void dispose() {
		scrollView.clearSections();
	}

	@Override
	public View getPrintableContent() {
		return scrollView.getContentView();
	}
	
}

class TitleSection extends SectionView {
	
	private final LabeledTextBox txtTitle;
	private final LabeledTextBox txtDescription;
	
	private final DataBinding txtTitleBinding;
	private final DataBinding txtDescriptionBinding;
	
	public TitleSection(final UCModel model) {
		// model title
		txtTitle = new LabeledTextBox();
		txtTitle.setDefaultFont(RUCMPluginResource.FONT_DIALOG_TITLE);
		txtTitle.setTextAlignment(Alignment.LEFT_CENTER);
//		txtTitle.setInsets(Insets.make(20, 10, 10, 10));
		Cursor.setCursorOnView(txtTitle, java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		txtTitleBinding = new DataBinding(txtTitle, LabeledTextBox.TEXT_CHANGED, model, UCModel.KEY_NAME) {
			@Override
			protected void viewPropertyChanged(Object arg) {
				model.setName(txtTitle.getPlainText());
				if (model.getName().isEmpty()) {
					txtTitle.setText("Untitled");
				}
			}
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				String name = model.getName();
				if (!name.isEmpty()) {
					txtTitle.setText(name);
				} else {
					txtTitle.setText("Untitled");
				}
			}
		};
		addSubview(txtTitle);
		
		// model description
		txtDescription = new LabeledTextBox();
		txtDescription.setDefaultTextColor(Color.GRAY);
		txtDescription.setDefaultFont(RUCMPluginResource.FONT_DIALOG_SUBTITLE);
		txtDescription.setTextAlignment(Alignment.LEFT_CENTER);
//		txtDescription.setInsets(Insets.make(10, 10, 10, 10));
		Cursor.setCursorOnView(txtDescription, java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		txtDescriptionBinding = new DataBinding(txtDescription, LabeledTextBox.TEXT_CHANGED, model, UCModel.KEY_DESCRIPTION) {
			@Override
			protected void viewPropertyChanged(Object arg) {
				model.setDescription(txtDescription.getPlainText());
				if (model.getDescription().isEmpty()) {
					txtDescription.setText("Input description for this model here.");
				}
			}
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				String desc = model.getDescription();
				if (!desc.isEmpty()) {
					txtDescription.setText(desc);
				} else {
					txtDescription.setText("Input description for this model here.");
				}
			}
		};
		addSubview(txtDescription);
		
		layoutSection();
	}
	
	@Override
	protected void dispose() {
		txtTitleBinding.dispose();
		txtDescriptionBinding.dispose();
		super.dispose();
	}
	
}

class ElementSection extends SectionView {
	
	private final UCModel model;
	private final TableView table;
	private final Type targetType;
	private final String targetTypeTitle;
	
	ElementSection(UCModel model, Type targetType, String targetTypeTitle) {
		this.targetType = targetType;
		this.targetTypeTitle = targetTypeTitle;
		this.model = model;
		// initialize table
		table = new TableView() {
			@Override
			protected int getMinimumKeyColumnWidth() {
				return SpecificationView.KEY_COL_PREFERRED_WIDTH;
			}
		};
		DropShadow.createDropShadowOn(table);
		rebuildRows();
		addSubview(table);
		table.addEventHandler(LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				layoutSection();	
			}
		});
		layoutSection();
		
		model.addObserver(UCModel.KEY_MODELELEMENTS, observer);
	}
	
	private final Observer observer = new Observer() {
		@Override
		public void notifyChanged(ManagedObject target, String key, ManagedObject value) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					rebuildRows();
				}
			});
		}
	};
	
	private void rebuildRows() {
		table.removeAllRows();
		table.addRow(new TitleRow(targetTypeTitle, "Description"));
		for (ModelElement element : model.getModelElements()) {
			if (element.isKindOf(targetType)) {
				table.addRow(new ElementRow(element));
			}
		}
	}
	
	@Override
	protected void dispose() {
		model.removeObserver(UCModel.KEY_MODELELEMENTS, observer);
		table.removeAllRows();
		super.dispose();
	}
	
}

class TitleRow implements TableViewRow {
	
	private final RowLabel keyLabel;
	private final RowLabel valueLabel;
	
	public TitleRow(String keyName, String valueName) {
		keyLabel = createLabel();
		keyLabel.getTextView().setText(keyName);
		valueLabel = createLabel();
		valueLabel.getTextView().setText(valueName);
	}

	@Override
	public void tableViewRowAdded(TableView table) {
	}

	@Override
	public void tableViewRowRemoved(TableView table) {
	}
	
	private RowLabel createLabel() {
		RowLabel label = new RowLabel();
		label.setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
		label.setInsets(Insets.make(3, 6, 3, 6));
		label.setIconSpacing(3);
		label.getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
		return label;
	}

	@Override
	public View getKeyColumnView() {
		return keyLabel;
	}

	@Override
	public View getValueColumnView() {
		return valueLabel;
	}

	@Override
	public int getKeyColumnPreferredWidth(TableView table) {
		return getKeyColumnView().getPreferredWidth();
	}
	
}

class ElementRow implements TableViewRow, TextCellDelegate {
	
	private TableView owner;
	private final TextCell keyCell;
	private final TextCell valueCell;
	
	private final DataBinding keyBinding;
	private final DataBinding valueBinding;
	
	public ElementRow(final ModelElement element) {
		keyCell = new TextCell(this, false, false);
		keyCell.setText(element.getName());
		keyCell.setHintText("(Untitled)");
		keyCell.setBreakIterator(null);
		valueCell = new TextCell(this, true, false);
		valueCell.setText(element.getDescription());
		valueCell.setHintText("Input description here.");
		keyBinding = new DataBinding(keyCell, TextCell.LOST_KEYBOARD_FOCUS, element, ModelElement.KEY_NAME) {
			@Override
			protected void viewPropertyChanged(Object arg) {
				element.setName(keyCell.getPlainText());
			}
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				keyCell.setText(element.getName());
			}
		};
		valueBinding = new DataBinding(valueCell, TextCell.LOST_KEYBOARD_FOCUS, element, ModelElement.KEY_DESCRIPTION) {
			@Override
			protected void viewPropertyChanged(Object arg) {
				element.setDescription(valueCell.getPlainText());
			}
			@Override
			protected void modelPropertyChanged(ManagedObject value) {
				valueCell.setText(element.getDescription());
			}
		};
	}

	private final EventHandler textChangeHandler = new EventHandler() {
		@Override
		public void handle(View sender, Key key, Object arg) {
			owner.layout();
		}
	};

	@Override
	public void tableViewRowAdded(TableView table) {
		owner = table;
		keyCell.addEventHandler(TextView.TEXT_LAYOUT_CHANGED, textChangeHandler);
		valueCell.addEventHandler(TextView.TEXT_LAYOUT_CHANGED, textChangeHandler);
	}

	@Override
	public void tableViewRowRemoved(TableView table) {
		keyCell.removeEventHandler(TextView.TEXT_LAYOUT_CHANGED, textChangeHandler);
		valueCell.removeEventHandler(TextView.TEXT_LAYOUT_CHANGED, textChangeHandler);
		keyBinding.dispose();
		valueBinding.dispose();
		owner = null;
	}

	@Override
	public View getKeyColumnView() {
		return keyCell;
	}

	@Override
	public View getValueColumnView() {
		return valueCell;
	}

	@Override
	public int getKeyColumnPreferredWidth(TableView table) {
		return keyCell.getPreferredWidth();
	}

	@Override
	public void cellTabPressed(TextCell cell, boolean shift) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cellUpPressed(TextCell cell, int offset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cellDownPressed(TextCell cell, int offset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cellEnterPressed(TextCell cell) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cellBackspacePressed(TextCell cell) {
		// TODO Auto-generated method stub
		
	}
	
}
