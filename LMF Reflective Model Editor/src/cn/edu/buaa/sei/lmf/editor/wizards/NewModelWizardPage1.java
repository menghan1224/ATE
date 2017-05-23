package cn.edu.buaa.sei.lmf.editor.wizards;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import cn.edu.buaa.sei.lmf.LMFContext;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.TypeFilter;

public class NewModelWizardPage1 extends WizardPage {
	private Combo comboRootModel;

	protected NewModelWizardPage1() {
		super("Page 1");
		setTitle("MOMF Model File");
		setDescription("This wizard creates a new file with *.json extension that can be opened by MOMF model editor.");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 4;
		layout.verticalSpacing = 9;
		Label lblrootModelElement = new Label(container, SWT.NULL);
		lblrootModelElement.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblrootModelElement.setText("&Root Model Element:");
		
		comboRootModel = new Combo(container, SWT.NONE);
		comboRootModel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboRootModel.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		init();
		dialogChanged();
		setControl(container);
	}
	
	private void init() {
		List<Type> types = LMFContext.listTypes(new TypeFilter() {
			@Override
			public boolean accept(Type type) {
				return !type.isAbstract() && !type.isPrimitiveType();
			}
		});
		String[] items = new String[types.size()];
		for (int i = 0; i < items.length; i++) {
			items[i] = types.get(i).getName();
		}
		comboRootModel.setItems(items);
	}
	
	private void dialogChanged() {
		String typeName = getRootModelElementName();
		if (typeName.length() == 0) {
			updateStatus("Root model element type must be specified");
			return;
		}
		if (!LMFContext.hasType(typeName)) {
			updateStatus("Unknown model element type");
			return;
		}
		Type type = LMFContext.typeForName(typeName);
		if (type.isAbstract()) {
			updateStatus("Selected model element type is abstract");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getRootModelElementName() {
		return comboRootModel.getText();
	}
	
}
