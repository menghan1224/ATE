package cn.edu.buaa.sei.lmf.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Primitives;
import cn.edu.buaa.sei.lmf.Type;

public class PrimitiveListDialog extends Dialog {
	private Text text;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PrimitiveListDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.RESIZE | SWT.TITLE);
	}
	
	@Override
	protected void configureShell(Shell shell) {
	     super.configureShell(shell);
	     shell.setText("Primitives Editor");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		text = new Text(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < initValue.length; i++) {
			sb.append(initValue[i].toString());
			if (i != initValue.length - 1) sb.append('\n');
		}
		text.setText(sb.toString());

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected void okPressed() {
		result.clear();
		if (text.getText().isEmpty()) {
			super.okPressed();
		} else {
			String[] lines = text.getText().split("\n");
			for (int i = 0; i < lines.length; i++) {
				ManagedObject obj = null;
				try {
					if (primitiveType == Primitives.BOOL) {
						obj = Primitives.newInstance(Boolean.parseBoolean(lines[i]));
					} else if (primitiveType == Primitives.INT) {
						obj = Primitives.newInstance(Integer.parseInt(lines[i]));
					} else if (primitiveType == Primitives.LONG) {
						obj = Primitives.newInstance(Long.parseLong(lines[i]));
					} else if (primitiveType == Primitives.FLOAT) {
						obj = Primitives.newInstance(Float.parseFloat(lines[i]));
					} else if (primitiveType == Primitives.DOUBLE) {
						obj = Primitives.newInstance(Double.parseDouble(lines[i]));
					} else if (primitiveType == Primitives.STRING) {
						obj = Primitives.newInstance(lines[i]);
					}
				} catch (Exception ex) {
					obj = null;
				}
				if (obj == null) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
			        messageBox.setText("Error");
			        messageBox.setMessage(String.format("Invalid value at line %d.", i + 1));
			        messageBox.open();
			        return;
				}
				result.add(obj);
			}
			super.okPressed();
		}
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(379, 319);
	}
	
	private Type primitiveType;
	private Object[] initValue;
	private final List<ManagedObject> result = new ArrayList<ManagedObject>();
	
	public void init(Type primitiveType, Object[] values) {
		this.primitiveType = primitiveType;
		this.initValue = values;
	}
	
	public Object[] getValue() {
		return result.toArray();
	}

}
