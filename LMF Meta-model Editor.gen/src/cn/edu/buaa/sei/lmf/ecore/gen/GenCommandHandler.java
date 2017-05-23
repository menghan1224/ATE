package cn.edu.buaa.sei.lmf.ecore.gen;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import cn.edu.buaa.sei.lmf.meta.ImportedType;
import cn.edu.buaa.sei.lmf.meta.Package;
import cn.edu.buaa.sei.lmf.meta.Type;

public class GenCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
        Object firstElement = selection.getFirstElement();
        Package root = null;
        if (firstElement instanceof Package) {
        	root = (Package) firstElement;
        }
        
        if (root != null) {
			URI uri = root.eResource().getURI();
			IFile modelFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(uri.toPlatformString(true))); 
			IProject project = modelFile.getProject();
			IFolder outputFolder = project.getFolder("output");
			if (!outputFolder.exists()) {
				try {
					outputFolder.create(true, true, null);
				} catch (CoreException ex) {
					logError(ex);
					return null;
				}
			}
			
			final String packageName = root.getNamePrefix();
			
			TypeLoaderCoder typeLoaderCoder = new TypeLoaderCoder(packageName + "TypeLoader", root);
			writeFile(outputFolder, packageName + "TypeLoader.java", typeLoaderCoder.toString());
			
			FactoryCoder factoryCoder = new FactoryCoder(packageName + "Factory", root);
			writeFile(outputFolder, packageName + "Factory.java", factoryCoder.toString());
			
			for (Type type : root.getTypes()) {
				if (type instanceof ImportedType) continue;
				
				TypeCoder typeCoder = new TypeCoder(type);
				writeFile(outputFolder, type.getName() + ".java", typeCoder.toString());
				
				if (!type.isAbstract()) {
					TypeImplCoder typeImplCoder = new TypeImplCoder(type);
					writeFile(outputFolder, type.getName() + "Impl.java", typeImplCoder.toString());
				}
			}
			
		}
		return null;
	}
	
	private void writeFile(IFolder folder, String fileName, String content) {
		InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("utf-8")));
		
		IFile file = folder.getFile(fileName);
		try {
			if (!file.exists()) {
				file.create(inputStream, true, null);
			} else {
				file.setContents(inputStream, true, false, null);
			}
		} catch (CoreException ex) {
			logError(ex);
		}
	}
	
	private void logError(Throwable e) {
		CodeGenPlugin.getDefault().getLog().log(new Status(Status.ERROR, CodeGenPlugin.PLUGIN_ID, e.getMessage(), e));
	}

}
