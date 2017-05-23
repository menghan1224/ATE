package cn.edu.buaa.sei.lmf.navigator;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.navigator.CommonActionProvider;

import cn.edu.buaa.sei.lmf.edit.ContextMenuMaker;

public class ActionProvider extends CommonActionProvider {

	private final ContextMenuMaker menuMaker; 
	
	public ActionProvider() {
		menuMaker = new ContextMenuMaker();
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		menuMaker.createContextMenu(menu);
	}

}
