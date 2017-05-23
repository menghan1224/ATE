package cn.edu.buaa.sei.rucm.spec;

import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.spec.widgets.TextCell;

public interface TextCellDecorator {
	
	public void init(TextCell cell, ManagedObject model);
	public void textChanged(String text);
	public void dispose();

}
