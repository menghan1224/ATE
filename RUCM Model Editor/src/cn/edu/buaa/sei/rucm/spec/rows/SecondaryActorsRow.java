package cn.edu.buaa.sei.rucm.spec.rows;

import java.util.ArrayList;
import java.util.List;

import ca.carleton.sce.squall.ucmeta.Actor;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.spec.SpecificationUtility;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

public class SecondaryActorsRow extends PropertyTableViewRow<UseCaseSpecification> {

	public SecondaryActorsRow(String displayName, UseCaseSpecification model) {
		super(displayName, model, UseCaseSpecification.KEY_SECONDARYACTORS);
		getValueColumnView().setHintText("None");
	}

	@Override
	protected String sycnPropertyToText(UseCaseSpecification model) {
		List<Actor> actors = model.getSecondaryActors();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < actors.size(); i++) {
			if (i > 0) buf.append(", ");
			buf.append(SpecificationUtility.toNameExpr(actors.get(i)));
		}
		return buf.toString();
	}

	@Override
	protected void sycnTextToProperty(String text) throws PropertyRowException {
		if (text.isEmpty()) {
			getModel().getSecondaryActors().clear();
		} else {
			String[] names = text.split(",");
			List<Actor> actors = new ArrayList<Actor>();
			LMFResource res = getModel().resource();
			ManagedObject root = res != null ? res.getRootObject() : null;
			if (root == null) {
				throw new PropertyRowException(); // TODO
			}
			for (int i = 0; i < names.length; i++) {
				final String name = names[i].trim();
				if (!name.isEmpty()) {
					Actor actor = (Actor) SpecificationUtility.findObjectWithNameExpr(name, root, Actor.TYPE_NAME);
					if (actor != null) {
						actors.add(actor);
					} else {
						throw new PropertyRowException(); // TODO
					}
				} else {
					throw new PropertyRowException(); // TODO
				}
			}
			getModel().set(UseCaseSpecification.KEY_SECONDARYACTORS, actors);
		}
	}
	
	@Override
	public void tableViewRowAdded(TableView table) {
		super.tableViewRowAdded(table);
		initDecorator(ElementType.SECONDARY_ACTORS);
	}

}
