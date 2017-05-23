package cn.edu.buaa.sei.ate.transformtoxml;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.internal.win32.SYSTEMTIME;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;

import ca.carleton.sce.squall.ucmeta.Actor;
import ca.carleton.sce.squall.ucmeta.Generalization;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import ca.carleton.sce.squall.ucmeta.Package;
import ca.carleton.sce.squall.ucmeta.Relationship;
import ca.carleton.sce.squall.ucmeta.UseCase;
import ca.carleton.sce.squall.ucmeta.UseCaseImpl;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.lmf.ManagedList;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.edit.MenuContributor;
import cn.edu.buaa.sei.rucm.RUCMPlugin;


public class TransformMenu extends ContributionItem implements MenuContributor {
	private Package pack = null;
	List<UseCaseImpl> toTransformList=new ArrayList<UseCaseImpl>();
	@Override
	public void fill(Menu menu, int index) {
		MenuItem menuItem = new MenuItem(menu, SWT.NONE, index);
		menuItem.setText("Transform to Xml File");
		menuItem.setImage(RUCMPlugin.getBundleImage("icons/alphab_sort_co.gif"));
		if (pack != null) {
			// build menu
			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if(pack!=null){
					ManagedList list=pack.get(Package.KEY_MODELELEMENTS).listContent();
					for(ManagedObject obj:list){
						if(obj.getClass().getName().contains("UseCaseImpl")){
							toTransformList.add((UseCaseImpl)(obj));
						}
					}
					List<TeseCaseBean> beanlist=transformList(toTransformList);
					TestCasesBean testCases=new TestCasesBean(beanlist);
					SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddhhmmss");
					String time=simpleDateFormat.format(System.currentTimeMillis());
					String filename="TestCases-"+time+".xml";
					XmlUtil.convertToXml(testCases, ".\\"+filename);
					System.out.println("Create Path in:"+new File(".").getAbsolutePath());
					}
				}
			});
		} else {
			menuItem.setEnabled(false);
		}
	}
	public List<TeseCaseBean> transformList(List<UseCaseImpl> list){
		List<TeseCaseBean> beanlist=new ArrayList<TeseCaseBean>();
		for(UseCaseImpl usecase:list){
			UseCaseSpecification specification=usecase.getSpecification();
			TeseCaseBean bean=new TeseCaseBean(specification,usecase);
			beanlist.add(bean);
		}
		return beanlist;
	}
	
	
	@Override
	public IContributionItem[] createMenuItems() {
		ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) sel).getFirstElement();
			if (obj instanceof IAdaptable) {
				ManagedObject o = (ManagedObject) ((IAdaptable) obj).getAdapter(ManagedObject.class);
				if (o instanceof Package) {
					pack = (Package) o;
					return new IContributionItem[] { this };
				}
			}
		}
		return new IContributionItem[0];
	}
	

	

}