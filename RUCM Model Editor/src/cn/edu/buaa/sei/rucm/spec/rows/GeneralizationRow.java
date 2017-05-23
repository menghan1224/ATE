package cn.edu.buaa.sei.rucm.spec.rows;

import java.util.ArrayList;
import java.util.List;

import cn.edu.buaa.sei.lmf.AttributeSetter;
import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.lmf.LMFUtility.ObjectFilter;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.spec.SpecificationUtility;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

import ca.carleton.sce.squall.ucmeta.Generalization;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import ca.carleton.sce.squall.ucmeta.UCMetaFactory;
import ca.carleton.sce.squall.ucmeta.UCModel;
import ca.carleton.sce.squall.ucmeta.UseCase;
import ca.carleton.sce.squall.ucmeta.UseCaseClassifier;

public class GeneralizationRow extends PropertyTableViewRow<UCModel> {
	
	private final UseCase useCase;

	public GeneralizationRow(String displayName, UCModel model, UseCase useCase) {
		super(displayName, model, UCModel.KEY_MODELELEMENTS);
		this.useCase = useCase;
		getValueColumnView().setHintText("None");
	}

	@Override
	protected String sycnPropertyToText(UCModel model) {
		ManagedObject[] objects = getAllRelatedGeneralizations();
		if (objects.length == 0) {
			return "";
		} else {
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < objects.length; i++) {
				if (i > 0) buf.append(", ");
				buf.append(SpecificationUtility.toNameExpr((UseCaseClassifier) objects[i].get(Generalization.KEY_GENERAL)));
			}
			return buf.toString();
		}
	}

	@Override
	protected void sycnTextToProperty(String text) throws PropertyRowException {
		text = text.trim();
		if (text.isEmpty()) {
			removeAllRelatedGeneralizations();
		} else {
			String[] segments = text.split(",");
			final List<UseCase> generals = new ArrayList<UseCase>();
			for (String segment : segments) {
				final String name = segment.trim();
				ManagedObject genUseCase = SpecificationUtility.findObjectWithNameExpr(name, getModel(), UseCase.TYPE_NAME);
				if (genUseCase != null) {
					generals.add((UseCase) genUseCase);
				} else {
					throw new PropertyRowException(); // TODO
				}
			}
			removeAllRelatedGeneralizations();
			final List<ModelElement> list = getModel().getModelElements();
			getModel().set(new AttributeSetter() {
				@Override
				public void apply(ManagedObject target) {
					for (UseCase genUseCase : generals) {
						Generalization generalization = UCMetaFactory.createGeneralization();
						generalization.setSpecific(useCase);
						generalization.setGeneral(genUseCase);
						list.add(generalization);
					}
				}
			});
		}
	}
	
	private void removeAllRelatedGeneralizations() {
		for (ManagedObject obj : getAllRelatedGeneralizations()) {
			LMFUtility.removeObject(getModel(), obj);
		}
	}
	
	private ManagedObject[] getAllRelatedGeneralizations() {
		return LMFUtility.findObjects(getModel(), new ObjectFilter() {
			@Override
			public boolean accept(ManagedObject obj) {
				return obj.isKindOf(Generalization.TYPE_NAME) &&
						obj.get(Generalization.KEY_SPECIFIC) == useCase &&
						obj.get(Generalization.KEY_GENERAL) != null;
			}
		});
	}
	
	@Override
	public void tableViewRowAdded(TableView table) {
		super.tableViewRowAdded(table);
		initDecorator(ElementType.GENERALIZATION);
	}

}
