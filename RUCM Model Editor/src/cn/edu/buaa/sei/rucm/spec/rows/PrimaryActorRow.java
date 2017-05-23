package cn.edu.buaa.sei.rucm.spec.rows;

import ca.carleton.sce.squall.ucmeta.Actor;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.lmf.LMFResource;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Observer;
import cn.edu.buaa.sei.rucm.spec.SpecificationUtility;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

public class PrimaryActorRow extends PropertyTableViewRow<UseCaseSpecification> {
	
	private Actor actor = null; // current observed PreCondition
	
	private final Observer nameObserver = new Observer() {
		@Override
		public void notifyChanged(ManagedObject target, String key, ManagedObject value) {
			updateTextCell();
		}
	};

	public PrimaryActorRow(String displayName, UseCaseSpecification model) {
		super(displayName, model, UseCaseSpecification.KEY_PRIMARYACTOR);
		getValueColumnView().setHintText("None");
	}
	
	private void observeActor(Actor actor) {
		if (this.actor != actor) {
			if (this.actor != null) {
				this.actor.removeObserver(Actor.KEY_NAME, nameObserver);
			}
			this.actor = actor;
			if (this.actor != null) {
				this.actor.addObserver(Actor.KEY_NAME, nameObserver);
			}
		}
	}
	
	@Override
	public void tableViewRowRemoved(TableView table) {
		observeActor(null);
		super.tableViewRowRemoved(table);
	}

	@Override
	protected String sycnPropertyToText(UseCaseSpecification model) {
		Actor actor = model.getPrimaryActor();
		observeActor(actor);
		return actor != null ? SpecificationUtility.toNameExpr(actor) : "";
	}

	@Override
	protected void sycnTextToProperty(String text) throws PropertyRowException {
		final String name = text.trim();
		if (name.isEmpty()) {
			getModel().setPrimaryActor(null);
			observeActor(null);
		} else {
			LMFResource res = getModel().resource();
			ManagedObject root = res != null ? res.getRootObject() : null;
			if (root == null) {
				// TODO: add other information
				throw new PropertyRowException();
			}
			ManagedObject actor = SpecificationUtility.findObjectWithNameExpr(name, root, Actor.TYPE_NAME);
			if (actor != null) {
				getModel().setPrimaryActor((Actor) actor);
				observeActor((Actor) actor);
			} else {
				// TODO: add other information
				throw new PropertyRowException();
			}
		}
	}
	
	@Override
	public void tableViewRowAdded(TableView table) {
		super.tableViewRowAdded(table);
		initDecorator(ElementType.PRIMARY_ACTOR);
	}

}
