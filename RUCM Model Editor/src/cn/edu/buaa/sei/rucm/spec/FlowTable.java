package cn.edu.buaa.sei.rucm.spec;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import ca.carleton.sce.squall.ucmeta.AlternativeFlow;
import ca.carleton.sce.squall.ucmeta.BasicFlow;
import ca.carleton.sce.squall.ucmeta.FlowOfEvents;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.lmf.ManagedList;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Observer;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.TypeFilter;
import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.diagram.widgets.MenuItems;
import cn.edu.buaa.sei.rucm.diagram.widgets.PopupMenu;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.spec.rows.PostconditionRow;
import cn.edu.buaa.sei.rucm.spec.widgets.DropDownButton;
import cn.edu.buaa.sei.rucm.spec.widgets.EmbeddedRow;
import cn.edu.buaa.sei.rucm.spec.widgets.RowLabel;
import cn.edu.buaa.sei.rucm.spec.widgets.SingleColumnRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TextTableViewRow;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewContext;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;
import co.gongzh.snail.util.Insets;
import co.gongzh.snail.util.Vector2D;

/**
 * This is a default implementation for flow table. Clients can override {@link #createHeaderRows(TableView)} and
 * {@link #createFooterRows(TableView)} to customize the table.
 * 
 * @author Gong Zhang
 */
public class FlowTable extends TableView {
	
	// model
	private final FlowOfEvents flow;
	private final UseCaseSpecification specification;
	
	// ui
	private final FlowLabel flowLabel;
	private final TableView contentTable;
	private final StepTable stepTable;

	private final TableViewRow mainRow = new TableViewRow() {
		@Override public void tableViewRowAdded(TableView table) {}
		@Override public void tableViewRowRemoved(TableView table) {}
		@Override public View getValueColumnView() { return contentTable; }
		@Override public View getKeyColumnView() { return flowLabel; }
//		@Override public int getKeyColumnPreferredWidth(TableView table) { return flowLabel.getPreferredWidth(); }
		@Override public int getKeyColumnPreferredWidth(TableView table) {
			return Math.max(SpecificationView.KEY_COL_PREFERRED_WIDTH, flowLabel.getPreferredWidth());
		}
	};
	
	// extensibility
	private final Map<FlowFieldContributor, EmbeddedRow[]> headerFieldContributors;
	private final Map<FlowFieldContributor, EmbeddedRow[]> footerFieldContributors;
	
	public FlowTable(String caption, FlowOfEvents flow, UseCaseSpecification specification) {
		this.flow = flow;
		this.specification = specification;
		this.flowLabel = new FlowLabel(caption, flow, this);
		this.headerFieldContributors = new HashMap<FlowFieldContributor, EmbeddedRow[]>();
		this.footerFieldContributors = new HashMap<FlowFieldContributor, EmbeddedRow[]>();
		this.stepTable = new StepTable(flow);
		this.contentTable = new TableView() {
			{
				setOuterGridColor(null);
				createHeaderRows(this);
				// Header Extension
				for (IConfigurationElement point : Platform.getExtensionRegistry().getExtensionPoint(RUCMPlugin.PLUGIN_ID + ".fields").getConfigurationElements()) {
					if ("flowHeaderFields".equals(point.getName())) {
						try {
							if (FlowTable.this.flow.isKindOf(point.getAttribute("flowType"))) {
								FlowFieldContributor ffc = (FlowFieldContributor) point.createExecutableExtension("class");
								ffc.init(FlowTable.this);
								if (ffc.isVisible()) {
									EmbeddedRow[] rows = ffc.createFields().clone();
									for (TableViewRow row : rows) addRow(row);
									headerFieldContributors.put(ffc, rows);
								} else {
									headerFieldContributors.put(ffc, new EmbeddedRow[0]);
								}
							}
						} catch (CoreException ex) {
							RUCMPlugin.logError(ex, true);
						}
					}
				}
				addRow(FlowTable.this.stepTable.getWrapperRow());
				createFooterRows(this);
				// Footer Extension
				for (IConfigurationElement point : Platform.getExtensionRegistry().getExtensionPoint(RUCMPlugin.PLUGIN_ID + ".fields").getConfigurationElements()) {
					if ("flowFooterFields".equals(point.getName())) {
						try {
							if (FlowTable.this.flow.isKindOf(point.getAttribute("flowType"))) {
								FlowFieldContributor ffc = (FlowFieldContributor) point.createExecutableExtension("class");
								ffc.init(FlowTable.this);
								if (ffc.isVisible()) {
									EmbeddedRow[] rows = ffc.createFields().clone();
									for (TableViewRow row : rows) addRow(row);
									footerFieldContributors.put(ffc, rows);
								} else {
									footerFieldContributors.put(ffc, new EmbeddedRow[0]);
								}
							}
						} catch (CoreException ex) {
							RUCMPlugin.logError(ex, true);
						}
					}
				}
			}
		};
		this.contentTable.addEventHandler(LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				FlowTable.this.layout();
			}
		});
		addRow(mainRow);
	}
	
	/**
	 * Clients can override this method to contribute new header rows to the table
	 * by calling {@link TableView#addRow(TableViewRow)}.
	 * @param container the container table
	 */
	protected void createHeaderRows(TableView container) {
		container.addRow(new StepPlaceholderRow());
	}
	
	/**
	 * Clients can override this method to contribute new footer rows to the table
	 * by calling {@link TableView#addRow(TableViewRow)}.
	 * @param container the container table
	 */
	protected void createFooterRows(TableView container) {
		container.addRow(new PostconditionRow(FlowTable.this.flow));
	}
	
	public FlowOfEvents getFlow() {
		return flow;
	}
	
	public UseCaseSpecification getSpecification() {
		return specification;
	}
	
	public void positionInScrollViewChanged(int top) {
		flowLabel.setLabelTop(-top + 1);
	}
	
	public void notifyFieldVisibilityChange(FlowFieldContributor contributor) {
		if (headerFieldContributors.containsKey(contributor)) {
			if (contributor.isVisible()) {
				// hide -> show
				int insertIndex = contentTable.indexOfRow(stepTable.getWrapperRow());
				EmbeddedRow[] rows = contributor.createFields().clone();
				for (TableViewRow row : rows) contentTable.addRow(insertIndex++, row);
				headerFieldContributors.put(contributor, rows);
			} else {
				// show -> hide
				for (EmbeddedRow row : headerFieldContributors.get(contributor)) {
					contentTable.removeRow(row);
				}
				headerFieldContributors.put(contributor, new EmbeddedRow[0]);
			}
		} else if (footerFieldContributors.containsKey(contributor)) {
			if (contributor.isVisible()) {
				// hide -> show
				EmbeddedRow[] rows = contributor.createFields().clone();
				for (TableViewRow row : rows) contentTable.addRow(row);
				footerFieldContributors.put(contributor, rows);
			} else {
				// show -> hide
				for (EmbeddedRow row : footerFieldContributors.get(contributor)) {
					contentTable.removeRow(row);
				}
				footerFieldContributors.put(contributor, new EmbeddedRow[0]);
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public boolean isFreeformMode() {
		return stepTable.isFreeformMode();
	}
	
	public void setFreeformMode(boolean freeformMode) {
		stepTable.setFreeformMode(freeformMode);
	}
	
	public void dispose() {
		for (FlowFieldContributor fieldContributor : this.headerFieldContributors.keySet()) {
			fieldContributor.dispose();
		}
		for (FlowFieldContributor fieldContributor : this.footerFieldContributors.keySet()) {
			fieldContributor.dispose();
		}
		flowLabel.dispose();
		contentTable.removeAllRows();
	}
	
}

class FlowLabel extends View {
	
	private final RowLabel label;
	private final DropDownButton dropDownButton;
	private final FlowTable ownerTable;
	
	private int labelTop;
	private final FlowOfEvents flow;
	
	FlowLabel(String labelText, final FlowOfEvents flow, final FlowTable ownerTable) {
		this.flow = flow;
		this.ownerTable = ownerTable;
		
		setPaintMode(PaintMode.DIRECTLY);
		setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
		labelTop = 0;
		
		// label
		label = new RowLabel();
		label.setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
		label.setInsets(Insets.make(3, 6, 3, 6));
		label.setIconSpacing(3);
//		label.getTextView().setDefaultFont(SpecViewResource.FONT_DIALOG_BOLD);
		label.getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
		label.getTextView().setText(labelText);
		label.getTextView().setBreakIterator(BreakIterator.getWordInstance());
		addSubview(label);
		
		// drop-down button and drop-down menu
		dropDownButton = new DropDownButton();
		dropDownButton.addEventHandler(MOUSE_CLICKED, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				PopupMenu menu = new FlowContextMenu(ownerTable);
				Vector2D location = dropDownButton.getPositionInRootView();
				location.y += dropDownButton.getHeight();
				menu.popup(getViewContext(), location);
			}
		});
		updateFlowName();
		flow.addObserver(FlowOfEvents.KEY_NAME, nameObserver);
		addSubview(dropDownButton);
	}
	
	public void dispose() {
		flow.removeObserver(FlowOfEvents.KEY_NAME, nameObserver);
	}
	
	private final Observer nameObserver = new Observer() {
		@Override
		public void notifyChanged(ManagedObject target, String key, ManagedObject value) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateFlowName();
				}
			});
		}
	};
	
	private void updateFlowName() {
		String name = flow.getName();
		dropDownButton.setText(name.isEmpty() ? "(Untitled)" : String.format("\"%s\"", name));
		layout();
		ownerTable.layout();
	}
	
	@Override
	public int getPreferredWidth() {
		return Math.max(SpecificationView.KEY_COL_PREFERRED_WIDTH, dropDownButton.getPreferredWidth() + 12);
//		return Math.max(label.getPreferredWidth(), dropDownButton.getPreferredWidth() + 12);
//		return Math.max(120, dropDownButton.getPreferredWidth() + 12);
	}
	
	@Override
	public int getPreferredHeight() {
		return label.getPreferredHeight() + dropDownButton.getPreferredHeight() + 3;
	}
	
	public void setLabelTop(int labelTop) {
		this.labelTop = labelTop;
		layout();
	}
	
	@Override
	protected void layoutView() {
		label.setSize(getWidth(), label.getPreferredHeight());
		dropDownButton.setSize(dropDownButton.getPreferredSize());
		
		if (labelTop < 0) labelTop = 0;
		else if (labelTop > getHeight() - (label.getHeight() + dropDownButton.getHeight() + 3)) labelTop = getHeight() - (label.getHeight() + dropDownButton.getHeight() + 3);
		label.setPosition(0, labelTop);
		View.putViewWithHorizontalCenterAndTop(dropDownButton, getWidth() / 2, labelTop + label.getHeight());
	}
	
}

class StepPlaceholderRow extends SingleColumnRow {

	@Override public void tableViewRowAdded(TableView table) {}
	@Override public void tableViewRowRemoved(TableView table) {}
	@Override public View getValueColumnView() { return label; }

	private final RowLabel label = new RowLabel() {
		{
			setBackgroundColor(TextTableViewRow.LABEL_BG_COLOR);
			setInsets(Insets.make(3, 6, 3, 6));
			setIconSpacing(3);
//			getTextView().setDefaultFont(SpecViewResource.FONT_DIALOG_BOLD);
			getTextView().setDefaultTextColor(TextTableViewRow.LABEL_TEXT_COLOR);
			getTextView().setText("Steps");
		}
	};
	
}

class FlowContextMenu extends PopupMenu {
	
	FlowContextMenu(final FlowTable flowTable) {
		final FlowOfEvents flow = flowTable.getFlow();
		
		// Rename
		addSubview(new MenuItems.LabelItem("Rename...") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				e.handle();
				ViewContext context = getViewContext();
				dismiss();
			    String answer = JOptionPane.showInputDialog(context.getSwingContainer(), 
			         "Please input the name:", 
			         "Rename Flow", JOptionPane.INFORMATION_MESSAGE);
			    if (answer != null) {
			    	flow.setName(answer);
			    }
			    recoverFocus(context);
			}
		});
		
		// Free-form Mode
		if (!flowTable.isFreeformMode()) {
			addSubview(new MenuItems.LabelItem("Free-form Mode") {
				@Override
				protected void mouseClicked(MouseEvent e) {
					flowTable.setFreeformMode(true);
					e.handle();
					dismiss();
				}
			});
		}
		
		if (!flow.isKindOf(BasicFlow.TYPE_NAME)) {
			addSubview(new MenuItems.Separator());
			
			// Move Up
			addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("nav_up.gif"), "Move Up") {
				@Override
				protected void mouseClicked(MouseEvent e) {
					if (flow.owner().isKindOf(UseCaseSpecification.TYPE_NAME)) {
						ManagedList flows = ((UseCaseSpecification) flow.owner()).get(UseCaseSpecification.KEY_ALTERNATIVEFLOWS).listContent();
						int index;
						if (flows.contains(flow) && (index = flows.indexOf(flow)) > 0) {
							flows.exchangeElements(index - 1, index);
						}
					}
					e.handle();
					dismiss();
				}
			});
			
			// Move Down
			addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("nav_down.gif"), "Move Down") {
				@Override
				protected void mouseClicked(MouseEvent e) {
					if (flow.owner().isKindOf(UseCaseSpecification.TYPE_NAME)) {
						ManagedList flows = ((UseCaseSpecification) flow.owner()).get(UseCaseSpecification.KEY_ALTERNATIVEFLOWS).listContent();
						int index;
						if (flows.contains(flow) && (index = flows.indexOf(flow)) < flows.size() - 1) {
							flows.exchangeElements(index, index + 1);
						}
					}
					e.handle();
					dismiss();
				}
			});
			
			addSubview(new MenuItems.Separator());
			
			// Duplicate
			addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("copy_edit.gif"), "Duplicate") {
				@Override
				protected void mouseClicked(MouseEvent e) {
					if (flow.owner().isKindOf(UseCaseSpecification.TYPE_NAME)) {
						ManagedList flows = ((UseCaseSpecification) flow.owner()).get(UseCaseSpecification.KEY_ALTERNATIVEFLOWS).listContent();
						if (flows.contains(flow)) {
							ManagedObject newFlow = LMFUtility.deepCopyObject(flow);
							flows.add(flows.indexOf(flow) + 1, newFlow);
						}
					}
					e.handle();
					dismiss();
				}
			});
			
			// Delete
			addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("delete_obj.gif"), "Delete...") {
				@Override
				protected void mouseClicked(MouseEvent e) {
					e.handle();
					ViewContext context = getViewContext();
					dismiss();
					int reply = JOptionPane.showConfirmDialog(context.getSwingContainer(),
							"Delete this flow?", "Delete", JOptionPane.OK_CANCEL_OPTION);
					if (reply == JOptionPane.OK_OPTION) {
						if (flow.owner().isKindOf(UseCaseSpecification.TYPE_NAME)) {
							ManagedList flows = ((UseCaseSpecification) flow.owner()).get(UseCaseSpecification.KEY_ALTERNATIVEFLOWS).listContent();
							if (flows.contains(flow)) {
								flows.remove(flow);
							}
						}
					}
					recoverFocus(context);
				}
			});
		}
		
		addSubview(new MenuItems.Separator());
		
		// Add New Flow
		addSubview(new MenuItems.LabelItem(RUCMPluginResource.getImage("add_obj.gif"), "Create Alternative Flow...") {
			@Override
			protected void mouseClicked(MouseEvent e) {
				e.handle();
				final ViewContext context = getViewContext();
				dismiss();
				final Type alterType = LMFContext.typeForName(AlternativeFlow.TYPE_NAME);
				List<Type> types = LMFContext.listTypes(new TypeFilter() {
					@Override
					public boolean accept(Type type) {
						return type.isOrIsSubtypeOf(alterType) && !type.isAbstract();
					}
				});
				List<String> typeNames = new ArrayList<String>();
				for (Type type : types) {
					typeNames.add(type.getName());
				}
				String[] displayNames = new String[typeNames.size()];
				for (IConfigurationElement point : Platform.getExtensionRegistry().getExtensionPoint(RUCMPlugin.PLUGIN_ID + ".flowTable").getConfigurationElements()) {
					String displayName = point.getAttribute("displayName");
					if (displayName != null && !displayName.isEmpty()) {
						int index = typeNames.indexOf(point.getAttribute("flowType"));
						if (index != -1) displayNames[index] = displayName;
					}
				}
				for (int i = 0; i < displayNames.length; i++) {
					if (displayNames[i] == null) displayNames[i] = typeNames.get(i);
				}
				displayNames[0]="特定测试分支流";
				displayNames[1]="全局测试分支流";
				displayNames[2]="界限测试分支流";
				
			    Object answer = JOptionPane.showInputDialog(context.getSwingContainer(), 
			         "Please select flow type:", 
			         "New Flow", JOptionPane.INFORMATION_MESSAGE,
			         null,
			         displayNames,
			         displayNames[0]);
			    
//			    answer=submap.get(String.valueOf(answer));
			    if (answer != null && flow.owner().isKindOf(UseCaseSpecification.TYPE_NAME)) {
					ManagedList flows = ((UseCaseSpecification) flow.owner()).get(UseCaseSpecification.KEY_ALTERNATIVEFLOWS).listContent();
					if (flows.contains(flow)) {
						int index = flows.indexOf(flow);
						ManagedObject newFlow = LMFContext.newInstance(types.get(Arrays.asList(displayNames).indexOf(answer)));
						flows.add(index + 1, newFlow);
					} else {
						ManagedObject newFlow = LMFContext.newInstance(types.get(Arrays.asList(displayNames).indexOf(answer)));
						flows.add(newFlow);
					}
			    }
			    recoverFocus(context);
			}
		});
	}
	
	private void recoverFocus(final ViewContext context) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				context.getSwingContainer().requestFocus();
			}
		});
	}
	
}
