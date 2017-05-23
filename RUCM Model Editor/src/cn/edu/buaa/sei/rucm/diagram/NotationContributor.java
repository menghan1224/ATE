package cn.edu.buaa.sei.rucm.diagram;

import java.util.Map;

import cn.edu.buaa.sei.lmf.Type;

public interface NotationContributor {
	
	public Map<Type, Class<? extends NodeView<?>>> getNodeNotations();
	public Map<Type, Class<? extends LinkLayer<?>>> getLinkNotations();

}
