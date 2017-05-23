package cn.edu.buaa.sei.rucm.spec;

import java.util.ArrayList;
import java.util.List;

import ca.carleton.sce.squall.ucmeta.ModelElement;
import ca.carleton.sce.squall.ucmeta.Package;
import cn.edu.buaa.sei.lmf.LMFUtility;
import cn.edu.buaa.sei.lmf.LMFUtility.ObjectFilter;
import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;

public final class SpecificationUtility {

	private SpecificationUtility() {
	}
	
	private static ModelElement findObjectWithName(final String name, ManagedObject root, final Type type) {
		return (ModelElement) LMFUtility.findObject(root, new ObjectFilter() {
			@Override
			public boolean accept(ManagedObject obj) {
				if (!obj.isKindOf(ModelElement.TYPE_NAME)) return false;
				return (obj.get(ModelElement.KEY_NAME).stringValue().equals(name) && obj.isKindOf(type));
			}
		});
	}
	
	public static String toNameExpr(ModelElement obj) {
		List<ModelElement> elements = new ArrayList<ModelElement>();
		ManagedObject o = obj;
		while ((o = o.owner()) != null) {
			if (o.isKindOf(Package.TYPE_NAME)) {
				elements.add((Package) o);
			}
		}
		if (elements.size() > 0) elements.remove(elements.size() - 1);
		StringBuilder sb = new StringBuilder();
		for (ModelElement el : elements) {
			sb.insert(0, "::");
			sb.insert(0, el.getName());
		}
		sb.append(obj.getName());
		return sb.toString();
	}
	
	public static ModelElement findObjectWithNameExpr(String nameExpression, ManagedObject root, String typeName) {
		return findObjectWithNameExpr(nameExpression, root, LMFContext.typeForName(typeName));
	}
	
	public static ModelElement findObjectWithNameExpr(String nameExpression, ManagedObject root, Type type) {
		ManagedObject rst = null;
		String[] segs = nameExpression.split("::");
		for (int i = 0; i < segs[i].length(); i++) {
			if (i != segs.length - 1) {
				// not last one
				rst = findObjectWithName(segs[i], root, LMFContext.typeForName(ModelElement.TYPE_NAME));
				if (rst == null) return null;
				root = rst;
			} else {
				return findObjectWithName(segs[i], root, type);
			}
		}
		return null;
	}
	
}
