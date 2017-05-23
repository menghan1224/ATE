package cn.edu.buaa.sei.rucm.spec;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import ca.carleton.sce.squall.ucmeta.AlternativeFlow;
import ca.carleton.sce.squall.ucmeta.BasicFlow;
import ca.carleton.sce.squall.ucmeta.BoundedAlternative;
import ca.carleton.sce.squall.ucmeta.FlowOfEvents;
import ca.carleton.sce.squall.ucmeta.GlobalAlternative;
import ca.carleton.sce.squall.ucmeta.SpecificAlternative;
import ca.carleton.sce.squall.ucmeta.UCMetaTemplateFactory;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.lmf.ListObserver;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.spec.widgets.DropShadow;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionView;
import co.gongzh.snail.Animation;
import co.gongzh.snail.View;
import co.gongzh.snail.event.EventHandler;
import co.gongzh.snail.event.Key;

public class FlowSection extends SectionView {
	
	private final UseCaseSpecification specification;
	
	private final FlowTable basicFlowTable;
	private final Map<AlternativeFlow, FlowTable> alterFlowTables;
	
	public FlowSection (UseCaseSpecification specification) {
		this.specification = specification;
		this.setSpacing(20);
		
		// basic flow table
		if (specification.getBasicFlow() == null) {
			BasicFlow basicFlow = UCMetaTemplateFactory.createBasicFlow();
			specification.setBasicFlow(basicFlow);
		}
		basicFlowTable = new FlowTable("主测试流", specification.getBasicFlow(), specification);
		DropShadow.createDropShadowOn(basicFlowTable);
		addSubview(basicFlowTable);
		basicFlowTable.addEventHandler(LAYOUT, new EventHandler() {
			@Override
			public void handle(View sender, Key key, Object arg) {
				layoutSection();	
			}
		});
		
		// alternative flows
		alterFlowTables = new HashMap<AlternativeFlow, FlowTable>();
		buildAlterFlowTables();
		specification.addListObserver(UseCaseSpecification.KEY_ALTERNATIVEFLOWS, altFlowListObserver);
	}
	
	private final ListObserver altFlowListObserver = new ListObserver() {
		@Override
		public void listChanged(ManagedObject target, String key, ManagedObject[] added, ManagedObject[] removed) {
			final List<AlternativeFlow> flows = new ArrayList<AlternativeFlow>(specification.getAlternativeFlows());
			final AlternativeFlow[] addedFlows = Arrays.asList(added).toArray(new AlternativeFlow[added.length]);
			final AlternativeFlow[] removedFlows = Arrays.asList(removed).toArray(new AlternativeFlow[removed.length]);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					alternativeFlowsChanged(flows, addedFlows, removedFlows);
				}
			});
		}
	};
	
	private void buildAlterFlowTables() {
		for (AlternativeFlow altFlow : specification.getAlternativeFlows()) {
			FlowTable table = createTableForFlow(altFlow);
			DropShadow.createDropShadowOn(table);
			addSubview(table);
			table.addEventHandler(LAYOUT, new EventHandler() {
				@Override
				public void handle(View sender, Key key, Object arg) {
					layoutSection();	
				}
			});
			alterFlowTables.put(altFlow, table);
		}
		layoutSection();
	}
	
	private void alternativeFlowsChanged(List<AlternativeFlow> flows, AlternativeFlow[] added, AlternativeFlow[] removed) {
		// create new added flows
		for (AlternativeFlow flow : added) {
			FlowTable table = createTableForFlow(flow);
			DropShadow.createDropShadowOn(table);
			addSubview(table);
			table.addEventHandler(LAYOUT, new EventHandler() {
				@Override
				public void handle(View sender, Key key, Object arg) {
					layoutSection();	
				}
			});
			alterFlowTables.put(flow, table);
			createFadeInAnimation(table).commit();
		}
		
		// dispose removed flows
		for (AlternativeFlow flow : removed) {
			FlowTable table = alterFlowTables.get(flow);
			if (table != null) {
				table.dispose();
				table.removeFromSuperView();
				alterFlowTables.remove(flow);
			}
		}
		
		// re-sort
		for (int i = 0; i < flows.size(); i++) {
			AlternativeFlow flow = flows.get(i);
			if (alterFlowTables.containsKey(flow)) {
				alterFlowTables.get(flow).setIndex(i + 1); // the first table is basic flow table
			}
		}
		
		layoutSection();
	}
	
	public static class StandardFlowTableContributor implements FlowTableContributor {

		@Override
		public FlowTable createTableForFlow(FlowOfEvents flow, UseCaseSpecification specification) {
			String flowType = flow.type().getName();
			if (flowType.equals(SpecificAlternative.TYPE_NAME)) {
				return new SpecificAltFlowTable("特定测试分支流", (SpecificAlternative) flow, specification);
			} else if (flowType.equals(BoundedAlternative.TYPE_NAME)) {
				return new BoundedAltFlowTable("界限测试分支流", (BoundedAlternative) flow, specification);
			} else if (flowType.equals(GlobalAlternative.TYPE_NAME)) {
				return new GlobalAltFlowTable("全局测试分支流", (GlobalAlternative) flow, specification);
			} else {
				throw new IllegalStateException();
			}
		}
		
	}
	
	private FlowTable createTableForFlow(AlternativeFlow flow) {
		String flowType = flow.type().getName();
		// Extension
		for (IConfigurationElement point : Platform.getExtensionRegistry().getExtensionPoint(RUCMPlugin.PLUGIN_ID + ".flowTable").getConfigurationElements()) {
			try {
				if (flowType.equals(point.getAttribute("flowType"))) {
					FlowTableContributor ftc = (FlowTableContributor) point.createExecutableExtension("class");
					return ftc.createTableForFlow(flow, specification);
				}
			} catch (CoreException ex) {
				RUCMPlugin.logError(ex, true);
			}
		}
		// create default flow table
		return new FlowTable("Unknown Alternative Flow", flow, specification);
	}
	
	private Animation createFadeInAnimation(final FlowTable table) {
		table.setAlpha(0.0f);
		return new Animation(0.3f, Animation.EaseOut) {

			@Override
			protected void animate(float progress) {
				AffineTransform tr = AffineTransform.getScaleInstance(progress / 10 + 0.9, progress / 10 + 0.9);
				table.setAlpha(progress);
				table.setTransform(tr);
			}
			
			@Override
			protected void completed(boolean canceled) {
				table.setAlpha(1.0f);
				table.setTransform(null);
			}
			
		};
	}
	
	public UseCaseSpecification getSpecification() {
		return specification;
	}
	
	@Override
	protected void positionInScrollViewChanged(int top) {
		basicFlowTable.positionInScrollViewChanged(top + basicFlowTable.getTop());
		for (FlowTable altTable : alterFlowTables.values()) {
			altTable.positionInScrollViewChanged(top + altTable.getTop());
		}
	}
	
	@Override
	protected void dispose() {
		specification.removeListObserver(UseCaseSpecification.KEY_ALTERNATIVEFLOWS, altFlowListObserver);
		basicFlowTable.dispose();
		for (FlowTable altTable : alterFlowTables.values()) {
			altTable.dispose();
		}
		alterFlowTables.clear();
		super.dispose();
	}
	
	public FlowTable[] getTables() {
		List<FlowTable> tables = new ArrayList<FlowTable>();
		tables.add(basicFlowTable);
		for (AlternativeFlow flow : specification.getAlternativeFlows()) {
			FlowTable table = alterFlowTables.get(flow);
			if (table != null) tables.add(table);
		}
		return tables.toArray(new FlowTable[0]);
	}
	
}
