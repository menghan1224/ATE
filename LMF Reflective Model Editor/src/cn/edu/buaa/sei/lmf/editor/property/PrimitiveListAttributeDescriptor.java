package cn.edu.buaa.sei.lmf.editor.property;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import cn.edu.buaa.sei.lmf.AttributeSetter;
import cn.edu.buaa.sei.lmf.ManagedList;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;

public class PrimitiveListAttributeDescriptor implements IAttributeDescriptor {

	private class Cell extends DialogCellEditor {
		
		@Override
		protected void updateContents(Object value) {
			if (value != null) {
				getDefaultLabel().setText(toDisplayString((Object[]) value));
			}
		}
		
		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			PrimitiveListDialog dialog = new PrimitiveListDialog(cellEditorWindow.getShell());
			dialog.init(type, (Object[]) getValue());
			if (IDialogConstants.OK_ID == dialog.open()) {
				return dialog.getValue();
			} else {
				return null;
			}
		}
		
	}
	
	private final ILabelProvider labelProvider = new ILabelProvider() {
		
		@Override
		public void removeListener(ILabelProviderListener listener) {
		}
		
		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}
		
		@Override
		public void dispose() {
		}
		
		@Override
		public void addListener(ILabelProviderListener listener) {
		}
		
		@Override
		public String getText(Object element) {
			return toDisplayString((Object[]) element);
		}
		
		@Override
		public Image getImage(Object element) {
			return null;
		}
	};
	
	private final String key;
	private final Type type;
	
	public PrimitiveListAttributeDescriptor(Type type, String key) {
		this.key = key;
		this.type = type;
	}
	
	public String getKey() {
		return key;
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		Cell cell = new Cell();
		cell.create(parent);
		return cell;
	}

	@Override
	public String getCategory() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return key;
	}

	@Override
	public String[] getFilterFlags() {
		return null;
	}

	@Override
	public Object getHelpContextIds() {
		return null;
	}

	@Override
	public Object getId() {
		return key;
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	@Override
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		return false;
	}

	@Override
	public Object getAttributeValue(ManagedObject object, String key) {
		return object.get(key).listContent().toArray();
	}

	@Override
	public void setAttributeValue(ManagedObject object, final String key, Object value) {
		final Object[] values = (Object[]) value;
		object.set(new AttributeSetter() {
			@Override
			public void apply(ManagedObject target) {
				ManagedList list = target.get(key).listContent();
				list.clear();
				for (Object obj : values) {
					list.add((ManagedObject) obj);
				}
			}
		});
	}
	
	private static String toDisplayString(Object[] values) {
		return String.format("(%d elements)", values.length);
	}

	@Override
	public boolean canRestoreDefaultValue(ManagedObject object, String key) {
		return !object.get(key).listContent().isEmpty();
	}

	@Override
	public void restoreDefaultValue(ManagedObject object, String key) {
		object.get(key).listContent().clear();
	}

}
