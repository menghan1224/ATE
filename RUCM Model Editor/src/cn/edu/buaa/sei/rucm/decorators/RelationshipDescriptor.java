package cn.edu.buaa.sei.rucm.decorators;

import org.eclipse.swt.graphics.Image;

import ca.carleton.sce.squall.ucmeta.Actor;
import ca.carleton.sce.squall.ucmeta.Relationship;
import ca.carleton.sce.squall.ucmeta.UseCase;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.NodeDecorator;
import cn.edu.buaa.sei.rucm.RUCMPlugin;

public class RelationshipDescriptor implements NodeDecorator {

	@Override
	public String[] getLabelRelatedKeys() {
		return new String[] { Relationship.KEY_ACTOR, Relationship.KEY_USECASE };
	}

	@Override
	public String getLabel(ManagedObject object) {
		ManagedObject usecase = object.get(Relationship.KEY_USECASE);
		ManagedObject actor = object.get(Relationship.KEY_ACTOR);
		
		String usecaseString = usecase == null ? "?" : usecase.get(UseCase.KEY_NAME).stringValue();
		if (usecaseString.isEmpty()) usecaseString = "?";
		String actorString = actor == null ? "?" : actor.get(Actor.KEY_NAME).stringValue();
		if (actorString.isEmpty()) actorString = "?";
		
		return String.format("%s (%s - %s)", object.type().getName(), actorString, usecaseString);
	}

	@Override
	public Image getImage(ManagedObject object) {
		return RUCMPlugin.getBundleImage("icons/relationship.gif");
	}
	
	@Override
	public Image getImage(Type type) {
		return RUCMPlugin.getBundleImage("icons/relationship.gif");
	}

}
