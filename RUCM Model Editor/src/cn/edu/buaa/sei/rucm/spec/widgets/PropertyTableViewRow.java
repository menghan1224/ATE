package cn.edu.buaa.sei.rucm.spec.widgets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import cn.edu.buaa.sei.lmf.CascadeObserver;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.spec.TextCellDecorator;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import co.gongzh.snail.View;
import co.gongzh.snail.util.Insets;

/**
 * <code>PropertyTableViewRow</code> is a concrete {@link TextTableViewRow} that is specifically used to edit model
 * property with a text cell. <code>PropertyTableViewRow</code> provides a M-V-C mechanism to synchronize the text and
 * property value. Clients should implement {@link #sycnPropertyToText(ManagedObject)} and {@link #sycnTextToProperty(String)}
 * to complete a <code>PropertyTableViewRow</code>.
 * <p>
 * Clients should specify the property change trigger in constructor so that method {@link #sycnPropertyToText(ManagedObject)} will be
 * called when the property changed. When the user changes the text and presses enter key, the method {@link #sycnTextToProperty(String)}
 * will be called.
 * <p>
 * Here are some default rows implemented with <code>PropertyTableViewRow</code> in RUCM template:
 * <li>{@link cn.edu.buaa.sei.rucm.spec.rows.BriefDescriptionRow BriefDescriptionRow}</li>
 * <li>{@link cn.edu.buaa.sei.rucm.spec.rows.DependencyRow DependencyRow}</li>
 * <li>{@link cn.edu.buaa.sei.rucm.spec.rows.GeneralizationRow GeneralizationRow}</li>
 * <li>{@link cn.edu.buaa.sei.rucm.spec.rows.PreconditionRow PreconditionRow}</li>
 * <li>{@link cn.edu.buaa.sei.rucm.spec.rows.PrimaryActorRow PrimaryActorRow}</li>
 * <li>{@link cn.edu.buaa.sei.rucm.spec.rows.SecondaryActorsRow SecondaryActorsRow}</li>
 * <p>
 * 
 * @author Gong Zhang
 *
 * @param <E> target model type
 * 
 * @see #PropertyTableViewRow(String, ManagedObject, String)
 * @see #PropertyTableViewRow(String, ManagedObject, PropertyChangeTrigger...)
 */
public abstract class PropertyTableViewRow<E extends ManagedObject> extends TextTableViewRow {
	
	public final static class PropertyChangeTrigger {
		private CascadeObserver observer;
		private final String[] keys;
		public PropertyChangeTrigger(String... keys) {
			this.keys = keys.clone();
			this.observer = null;
		}
		public String[] getKeys() {
			return keys.clone();
		}
		void createObserver(final PropertyTableViewRow<?> row, ManagedObject target) {
			if (observer == null) {
				observer = new CascadeObserver(target, keys) {
					@Override
					protected void notifyChanged(ManagedObject value) {
						row.updateTextCell();
					}
				};
			} else {
				throw new IllegalStateException();
			}
		}
		void dispose() {
			observer.removeFromTarget();
		}
	}
	
	/**
	 * This class is unfinished. In the future, this exception class will contain information about the error details
	 * and even support auto-fix feature.
	 * 
	 * @author Gong Zhang
	 *
	 */
	public static class PropertyRowException extends Exception {
		
		private static final long serialVersionUID = 1L;

		public PropertyRowException() {
			super();
		}
		
		// TODO: add other information
		
	}

	private  E model;
	private final PropertyChangeTrigger[] triggers;
	
	private int uiMutex = 0;
	private String cachedText;
	private Object cachedWarningContext;
	
	private final List<TextCellDecorator> decorators;
	
	/**
	 * Create a new <code>PropertyTableViewRow</code> that observes the target <code>model</code> with specified
	 * <code>key</code>.
	 * <p>
	 * For example, observe the name property of a use case:
	 * <p>
	 * <code>
	 * new PropertyTableViewRow("Use Case Name", useCase, UseCase.KEY_NAME) { ... };
	 * </code>
	 * @param caption the caption of this row
	 * @param model the target model
	 * @param key the key(name) of property of target model
	 */
	public PropertyTableViewRow(String caption, E model, String key) {
		this(caption, model, new PropertyChangeTrigger(key));
	}
	
	public PropertyTableViewRow(String caption, boolean multiline, E model, String key) {
		this(caption, multiline, model, new PropertyChangeTrigger(key));
	}
	
	/**
	 * Create a new <code>PropertyTableViewRow</code> that observes the target model with a group
	 * of <code>triggers</code>. Each trigger defines a chain of keys that will be observed.
	 * <p>
	 * For example, observe the name of primary actor of a specification of a use case:
	 * <p>
	 * <code>
	 * new PropertyTableViewRow("Primary Actor Name", useCase, new PropertyChangeTrigger(UseCase.KEY_SPECIFICATION, UseCaseSpecification.KEY_PRIMARYACTOR, Actor.KEY_NAME)) { ... };
	 * </code>
	 * @param caption the caption of this row
	 * @param model the target model
	 * @param triggers a group of property triggers
	 */
	public PropertyTableViewRow(String caption, E model, PropertyChangeTrigger... triggers) {
		this(caption, false, model, triggers);
	}
	
	public PropertyTableViewRow(String caption, boolean multiline, E model, PropertyChangeTrigger... triggers) {
		super(caption, "", multiline, true);
		if (triggers.length == 0) throw new IllegalArgumentException();
		this.model = model;
		this.triggers = triggers.clone();
		this.decorators = new ArrayList<TextCellDecorator>();
	}
	
	public E getModel() {
		return model;
	}
	public void  setModel(E model) {
		this.model=(E)model;
	}
	/**
	 * Force to update the text displayed in text cell.
	 */
	public void updateTextCell() {
		if (uiMutex == 0) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					String text = sycnPropertyToText(model);
					getValueColumnView().setText(text);
					for (TextCellDecorator decorator : decorators) {
						decorator.textChanged(text);
					}
				}
			});
		}
	}
	
	/**
	 * This method will be called when the specified property value changed. Clients should present current property value
	 * in a <code>String</code> and return it. The row will display that <code>String</code> in the text cell automatically.
	 * <p>
	 * Client should not call this method directly. Instead, call {@link #updateTextCell()} to manually update the displayed text.
	 * @param model
	 * @param key
	 * @return the displayable text
	 */
	protected abstract String sycnPropertyToText(E model);
	
	/**
	 * This method will be called when the user finishes editing the text. In this method, clients should parse the <code>text</code> into a property
	 * value, then assign that value to the model. If the <code>text</code> can not be recognized, throw a {@link PropertyRowException}
	 * instead.
	 * @param text the text that user inputs in the text cell
	 */
	protected abstract void sycnTextToProperty(String text) throws PropertyRowException;

	@Override
	protected void cellGotFocus() {
		cachedText = getValueColumnView().getPlainText();
	}
	
	@Override
	protected void cellLostFocus() {
		String text = getValueColumnView().getPlainText();
		if (text.equals(cachedText)) {
			if (cachedWarningContext != null) {
				getValueColumnView().setWarningButtonVisible(true, cachedWarningContext);
			}
			return;
		}
		uiMutex++;
		try {
			sycnTextToProperty(text);
			for (TextCellDecorator decorator : decorators) {
				decorator.textChanged(text);
			}
			cachedWarningContext = null;
			getValueColumnView().setWarningButtonVisible(false, null);
		} catch (PropertyRowException ex) {
			// TODO: handle text exception
			cachedWarningContext = new Object(); // TODO: get warning context from exception
			getValueColumnView().setWarningButtonVisible(true, cachedWarningContext);
		}
		uiMutex--;
	}
	
	@Override
	public void tableViewRowAdded(TableView table) {
		super.tableViewRowAdded(table);
		getValueColumnView().setText(sycnPropertyToText(model));
		for (PropertyChangeTrigger trigger : triggers) {
			trigger.createObserver(this, model);
		}
	}
	
	@Override
	public void tableViewRowRemoved(TableView table) {
		for (TextCellDecorator decorator : decorators) {
			decorator.dispose();
		}
		for (PropertyChangeTrigger trigger : triggers) {
			trigger.dispose();
		}
		super.tableViewRowRemoved(table);
	}
	
	@Override
	public void cellEnterPressed(TextCell cell) {
		getValueColumnView().resignKeyboardFocus();
	}
	
	/**
	 * This method is not intended to be called by clients.
	 * @param element the element name defined in "textCellDecorator" extension point
	 */
	protected void initDecorator(ElementType type) {
		List<TextCellDecorator> decorators = TextCellDecoratorRegistry.createDecorators(type);
		if (decorators == null) return;
		for (TextCellDecorator decorator : decorators) {
			decorator.init(getValueColumnView(), getModel());
		}
		this.decorators.addAll(decorators);
	}
	class TitleRow extends SingleColumnRow {

		@Override public void tableViewRowAdded(TableView table) {}
		@Override public void tableViewRowRemoved(TableView table) {}
		@Override public View getValueColumnView() { return label; }

		private final RowLabel label = new RowLabel() {
			{
				setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
				setInsets(Insets.make(3, 6, 3, 6));
				setIconSpacing(3);
//				getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
				getTextView().setDefaultTextColor(Color.RED);
				getTextView().setText("Real-Time Constraints"+"\t"+"haha");
				
			}
		};
		
	}
	
}
