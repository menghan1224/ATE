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

import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.Type;
import cn.edu.buaa.sei.lmf.edit.ObjectNode;
import cn.edu.buaa.sei.lmf.runtime.FileResource;

public class ReferenceAttributeDescriptor implements IAttributeDescriptor {

	private class Cell extends DialogCellEditor {
		
		@Override
		protected void updateContents(Object value) {
			if (value != null) {
				ManagedObject obj = (ManagedObject) value;
				getDefaultLabel().setText(ObjectNode.getObjectNodeDescriptor(obj.type()).getLabel(obj));
//				getDefaultLabel().setText(descriptor.getLabel((ManagedObject) value));
			}
		}
		
		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			ReferenceDialog dialog = new ReferenceDialog(cellEditorWindow.getShell());
			dialog.init(type, resource.getRootObject());
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
			ManagedObject obj = (ManagedObject) element;
			if (element == null) return null;
			else return ObjectNode.getObjectNodeDescriptor(obj.type()).getLabel(obj);
		}
		
		@Override
		public Image getImage(Object element) {
			ManagedObject obj = (ManagedObject) element;
			if (element == null) return null;
			else return ObjectNode.getObjectNodeDescriptor(obj.type()).getImage(obj);
		}
	};
	
	private final String key;
	private final Type type;
//	private final IObjectNodeDescriptor descriptor;
	private final FileResource resource;
	
	public ReferenceAttributeDescriptor(Type type, String key, FileResource resource) {
		this.key = key;
		this.type = type;
//		this.descriptor = ObjectNode.getObjectNodeDescriptor(type);
		this.resource = resource;
	}
	
	public String getKey() {
		return key;
	}
	
	public Type getType() {
		return type;
	}
	
	public FileResource getResource() {
		return resource;
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
		return object.get(key);
	}

	@Override
	public void setAttributeValue(ManagedObject object, final String key, Object value) {
		object.set(key, (ManagedObject) value);
	}

	@Override
	public boolean canRestoreDefaultValue(ManagedObject object, String key) {
		return object.get(key) != null;
	}

	@Override
	public void restoreDefaultValue(ManagedObject object, String key) {
		object.set(key, (ManagedObject) null);
	}
	
}
