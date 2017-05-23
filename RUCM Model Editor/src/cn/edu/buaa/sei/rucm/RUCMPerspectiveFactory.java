package cn.edu.buaa.sei.rucm;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class RUCMPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// Add "new wizards".
//        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
//        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
		layout.addNewWizardShortcut("cn.edu.buaa.sei.rucm.newModelWizard");

        // Add "show views".
//        layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
//        layout.addShowViewShortcut(IPageLayout.ID_BOOKMARKS);
//        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
//        layout.addShowViewShortcut("cn.edu.buaa.sei.rucm.modelNavigator");
        layout.addShowViewShortcut("cn.edu.buaa.sei.rucm.commonNavigator");
//        layout.addShowViewShortcut("org.eclipse.jdt.ui.PackageExplorer");
//        layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
        
     // Editors are placed for free.
        String editorArea = layout.getEditorArea();

        // Place navigator and outline to left of
        // editor area.
        IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.25f, editorArea);
        left.addView("cn.edu.buaa.sei.rucm.commonNavigator");
//        left.addView(IPageLayout.ID_OUTLINE);
        
        IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.70f, editorArea);
        right.addView(IPageLayout.ID_PROP_SHEET);
	}

}
