package cn.edu.buaa.sei.rucm.spec;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import ca.carleton.sce.squall.ucmeta.UCModel;
import ca.carleton.sce.squall.ucmeta.UseCase;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.spec.rows.BriefDescriptionRow;
import cn.edu.buaa.sei.rucm.spec.rows.DependencyRow;
import cn.edu.buaa.sei.rucm.spec.rows.GeneralizationRow;
import cn.edu.buaa.sei.rucm.spec.rows.PreconditionRow;
import cn.edu.buaa.sei.rucm.spec.rows.PrimaryActorRow;
import cn.edu.buaa.sei.rucm.spec.rows.SecondaryActorsRow;
import cn.edu.buaa.sei.rucm.spec.widgets.DropShadow;
import cn.edu.buaa.sei.rucm.spec.widgets.EmbeddedRow;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;
import cn.edu.buaa.sei.rucm.spec.widgets.TableViewRow;

import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public class BasicSection extends SectionView {
	
	public static final String ROW_USECASE_NAME = "ROW_USECASE_NAME";
	public static final String ROW_BRIEF_DESCRIPTION = "ROW_BRIEF_DESCRIPTION";
	public static final String ROW_PRE_CONDITION = "ROW_PRE_CONDITION";
	public static final String ROW_PRIMARY_ACTOR = "ROW_PRIMARY_ACTOR";
	public static final String ROW_SECONDARY_ACTOR = "ROW_SECONDARY_ACTOR";
	public static final String ROW_DEPENDENCY = "ROW_DEPENDENCY";
	public static final String ROW_GENERALIZATION = "ROW_GENERALIZATION";
	
	private final UseCaseSpecification specification;
	private final UseCase ownerUseCase;
	private final UCModel ownerUcModel;
	
	private final TableView table;
	
	// extensibility
	private final Map<String, TableViewRow> standardRowMap;
	private final Map<FieldContributor, TableViewRow[]> fieldContributors;
	
	public BasicSection(UseCaseSpecification spec, UseCase ownerUseCase, UCModel ownerUcModel) {
		this.specification = spec;
		this.ownerUseCase = ownerUseCase;
		this.ownerUcModel = ownerUcModel;
		this.standardRowMap = new HashMap<String, TableViewRow>();
		this.fieldContributors = new HashMap<FieldContributor, TableViewRow[]>();
		
		// initialize table
		table = new TableView() {
			@Override
			protected int getMinimumKeyColumnWidth() {
				return SpecificationView.KEY_COL_PREFERRED_WIDTH;
			}
		};
		DropShadow.createDropShadowOn(table);
		initTableRows();
		addSubview(table);
		table.addEventHandler(LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				layoutSection();	
			}
		});
	}
	
	private void initTableRows() {
		
		TableViewRow r;
		
		// Use Case Name
		if (ownerUseCase != null) {
			r = new PropertyTableViewRow<UseCase>("测试用例名", ownerUseCase, UseCase.KEY_NAME) {
				{ getValueColumnView().setHintText("Untitled"); }
				@Override protected String sycnPropertyToText(UseCase model) { return model.getName(); }
				@Override protected void sycnTextToProperty(String text) { getModel().setName(text.trim()); }
//				@Override public int getKeyColumnPreferredWidth(TableView table) { return SpecificationView.KEY_COL_PREFERRED_WIDTH; }
			};
			standardRowMap.put(ROW_USECASE_NAME, r);
			table.addRow(r);
		}
		
		// Brief Description
		r = new BriefDescriptionRow("用例说明", specification);
		standardRowMap.put(ROW_BRIEF_DESCRIPTION, r);
		table.addRow(r);
		
		// Precondition
		r = new PreconditionRow("前置条件", specification);
		standardRowMap.put(ROW_PRE_CONDITION, r);
		table.addRow(r);
		
//		// Primary Actor
//		r = new PrimaryActorRow("Primary Actor", specification);
//		standardRowMap.put(ROW_PRIMARY_ACTOR, r);
//		table.addRow(r);
//		
//		// Secondary Actors
//		r = new SecondaryActorsRow("Secondary Actors", specification);
//		standardRowMap.put(ROW_SECONDARY_ACTOR, r);
//		table.addRow(r);
//		
		// Dependency
		if (ownerUcModel != null && ownerUseCase != null) {
			r = new DependencyRow("依赖", ownerUseCase, ownerUcModel);
			standardRowMap.put(ROW_DEPENDENCY, r);
			table.addRow(r);
		}
		
		// Generalization
//		if (ownerUcModel != null && ownerUseCase != null) {
//			r = new GeneralizationRow("Generalization", ownerUcModel, ownerUseCase);
//			standardRowMap.put(ROW_GENERALIZATION, r);
//			table.addRow(r);
//		}
		
		// Extension
		for (IConfigurationElement point : Platform.getExtensionRegistry().getExtensionPoint(RUCMPlugin.PLUGIN_ID + ".fields").getConfigurationElements()) {
			if ("basicFields".equals(point.getName())) {
				try {
					FieldContributor fc = (FieldContributor) point.createExecutableExtension("class");
					fc.init(this);
					if (fc.isVisible()) {
						TableViewRow[] rows = fc.createFields().clone();
						for (TableViewRow row : rows) table.addRow(row);
						fieldContributors.put(fc, rows);
					} else {
						fieldContributors.put(fc, new TableViewRow[0]);
					}
				} catch (CoreException ex) {
					RUCMPlugin.logError(ex, true);
				}
			}
		}
	}
	
	public void removeStandardRow(String rowName) {
		if (standardRowMap.containsKey(rowName)) {
			table.removeRow(standardRowMap.get(rowName));
			standardRowMap.remove(rowName);
		}
	}
	
	public UseCaseSpecification getSpecification() {
		return specification;
	}
	
	public UseCase getOwnerUseCase() {
		return ownerUseCase;
	}
	
	public void notifyFieldVisibilityChange(FieldContributor contributor) {
		if (fieldContributors.containsKey(contributor)) {
			if (contributor.isVisible()) {
				// hide -> show
				TableViewRow[] rows = contributor.createFields().clone();
				for (TableViewRow row : rows) table.addRow(row);
				fieldContributors.put(contributor, rows);
			} else {
				// show -> hide
				for (TableViewRow row : fieldContributors.get(contributor)) {
					table.removeRow(row);
				}
				fieldContributors.put(contributor, new EmbeddedRow[0]);
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	protected void dispose() {
		standardRowMap.clear();
		for (FieldContributor contributor : fieldContributors.keySet()) {
			contributor.dispose();
		}
		table.removeAllRows();
		super.dispose();
	}
	
	public TableView getTable() {
		return table;
	}

}
