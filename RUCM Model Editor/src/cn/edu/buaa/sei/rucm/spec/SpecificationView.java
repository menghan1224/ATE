package cn.edu.buaa.sei.rucm.spec;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import ca.carleton.sce.squall.ucmeta.UCModel;
import ca.carleton.sce.squall.ucmeta.UseCase;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;
import cn.edu.buaa.sei.lmf.LMFResourceException;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.lmf.OwnerObserver;
import cn.edu.buaa.sei.rucm.RUCMPlugin;
import cn.edu.buaa.sei.rucm.SnailLMFEditorBase.SnailEditorView;
import cn.edu.buaa.sei.rucm.SpecificationEditor;
import cn.edu.buaa.sei.rucm.res.RUCMPluginResource;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionBar;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionScrollView;
import cn.edu.buaa.sei.rucm.spec.widgets.SectionView;

import co.gongzh.snail.Image;
import co.gongzh.snail.KeyEvent;
import co.gongzh.snail.MouseEvent;
import co.gongzh.snail.PaintMode;
import co.gongzh.snail.View;
import co.gongzh.snail.ViewGraphics;
import co.gongzh.snail.text.TextView;
import co.gongzh.snail.util.Alignment;
import co.gongzh.snail.util.Insets;

public class SpecificationView extends SnailEditorView {
	
	public static final int KEY_COL_PREFERRED_WIDTH = 120;
	
	private UseCaseSpecification specification;
	private UseCase ownerUseCase;
	private UCModel ownerUcModel;
	private SpecificationEditor editorPart;
	
	private final OwnerObserver ownerObserver = new OwnerObserver() {
		@Override
		public void ownerChanged(ManagedObject target, ManagedObject owner) {
			// XXX: when re-order the owner use case, this method will be called. That's not correct.
		setSpecification(null, true);
		}
	};
	
	private final SectionScrollView scrollView;
	
	private final Map<SectionContributor, SectionView> sectionContributors;
	
	public SpecificationView(SpecificationEditor editorPart) {
		this.editorPart = editorPart;
		sectionContributors = new HashMap<SectionContributor, SectionView>();
		setBackgroundColor(Color.WHITE);
		setPaintMode(PaintMode.DIRECTLY);
		addSubview(scrollView = new SectionScrollView());
		buildEmptySection();
	}
	
	public void setSpecification(UseCaseSpecification spec) {
		setSpecification(spec, false);
	}
	
	public void setSpecification(UseCaseSpecification spec, boolean force) {
		if (specification != spec) {
			// remove old listener
			if (specification != null) specification.removeOwnerObserver(ownerObserver);
			if (ownerUseCase != null) ownerUseCase.removeOwnerObserver(ownerObserver);
			if (ownerUcModel != null) ownerUcModel.removeOwnerObserver(ownerObserver);
			
			// remove old sections
			scrollView.clearSections();
			
			// dispose section contributors
			for (SectionContributor contributor : sectionContributors.keySet()) {
				contributor.dispose();
			}
			sectionContributors.clear();
			
			// rebuild new sections
			specification = spec;
			ManagedObject owner = specification != null ? specification.owner() : null;
			if (owner != null && owner.isKindOf(UseCase.TYPE_NAME)) {
				ownerUseCase = (UseCase) owner;
			} else {
				ownerUseCase = null;
			}
			if (editorPart.getResource() != null &&
				editorPart.getResource().getRootObject().isKindOf(UCModel.TYPE_NAME)) {
				ownerUcModel = (UCModel) editorPart.getResource().getRootObject();
			} else {
				ownerUcModel = null;
			}
			if (specification != null) {
				buildSections(specification, ownerUseCase, ownerUcModel);
				specification.addOwnerObserver(ownerObserver);
				if (ownerUseCase != null) ownerUseCase.addOwnerObserver(ownerObserver);
				if (ownerUcModel != null) ownerUcModel.addOwnerObserver(ownerObserver);
			} else {
				buildEmptySection();
			}
			requestKeyboardFocus();
		}
	}
	
	private void buildSections(UseCaseSpecification spec, UseCase ownerUseCase, UCModel ownerUcModel) {
		Insets insets = Insets.make(10, 8, 10, 14);
		
		// title section
		SectionView titleSection = new SectionView();
		titleSection.setInsets(Insets.make(5, 8, 0, 14));
		SectionBar titleBar = new SectionBar();
		titleBar.setText("Test Case Specification");
		titleSection.addSubview(titleBar);
		scrollView.addSection(titleSection);
		
		// basic section
		final BasicSection basicSection = new BasicSection(spec, ownerUseCase, ownerUcModel);
		basicSection.setInsets(insets);
		scrollView.addSection(basicSection);
		
		// flow section
		final FlowSection flowSection = new FlowSection(spec);
		flowSection.setInsets(insets);
		scrollView.addSection(flowSection);
		
		// Extension
		for (IConfigurationElement point : Platform.getExtensionRegistry().getExtensionPoint(RUCMPlugin.PLUGIN_ID + ".sections").getConfigurationElements()) {
			if ("section".equals(point.getName())) {
				try {
					SectionContributor sc = (SectionContributor) point.createExecutableExtension("class");
					sc.init(this);
					if (sc.isVisible()) {
						SectionView sectionView = sc.createSection();
						scrollView.addSection(sectionView);
						sectionContributors.put(sc, sectionView);
					} else {
						
						sectionContributors.put(sc, null);
					}
				} catch (CoreException ex) {
					RUCMPlugin.logError(ex, true);
				}
			}
		}
		
		scrollView.layout();
		
		// fade-in animation
		final float delay = 0.4f;
		final float delay_step = 0.07f;
		basicSection.getTable().makeFadeInAnimation(delay).commit();
		int i = 1;
		for (FlowTable table : flowSection.getTables()) {
			table.makeFadeInAnimation(delay + delay_step * (i++)).commit();
		}
	}
	
	private void buildEmptySection() {
		SectionView section = new SectionView();
		View imageView = new View() {
			{
				setBackgroundColor(Color.WHITE);
				setSize(105, 126);
			}
			@Override
			protected void repaintView(ViewGraphics g) {
				Image image = RUCMPluginResource.getImage("rucm_doc.png");
				g.drawImage(image, (getWidth() - 105) / 2 - 4, 40);
			}
			@Override
			public int getPreferredHeight() {
				return 126 + 40 + 20;
			}
		};
		section.addSubview(imageView);
		TextView textView = new TextView();
		textView.setDefaultFont(RUCMPluginResource.FONT_DIALOG);
		textView.setDefaultTextColor(Color.GRAY);
		textView.setTextAlignment(Alignment.CENTER_CENTER);
		textView.setText("Please select an use case or specification.");
		textView.setInsets(Insets.make(3, 0, 0, 0));
		section.addSubview(textView);
		scrollView.addSection(section);
	}
	
	public UseCaseSpecification getSpecification() {
		return specification;
	}
	
	public UseCase getOwnerUseCase() {
		return ownerUseCase;
	}
	
	public UCModel getOwnerUCModel() {
		return ownerUcModel;
	}
	
//	@Override
//	protected void mousePressed(MouseEvent e) {
//		requestKeyboardFocus();
//	}
	
	@Override
	protected void mouseReleased(MouseEvent e) {
		requestKeyboardFocus();
	}
	
	@Override
	protected void keyTyped(KeyEvent e) {
		// XXX: debug mode
		if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
			if (getViewContext().isDebugModeEnable()) {
				getViewContext().setDebugModeEnable(false);
			} else {
				getViewContext().setDebugModeEnable(true);
			}
		}
	}
	
	@Override
	protected void preKeyTyped(KeyEvent e) {
		// save resource
		if (e.isMetaOrCtrlDown() && (e.getKeyChar() == 's' || e.getKeyChar() == 'S')) {
			try {
				editorPart.getResource().save();
			} catch (LMFResourceException ex) {
				RUCMPlugin.logError(ex, true);
			}
			e.handle();
		}
	}
	
	@Override
	protected void layoutView() {
		View.scaleViewWithMarginToSuperView(scrollView, 0);
	}
	
	public void notifySectionVisibilityChange(SectionContributor contributor) {
		if (sectionContributors.containsKey(contributor)) {
			if (contributor.isVisible()) {
				// hide -> show
				SectionView sectionView = contributor.createSection();
				scrollView.addSection(sectionView);
				sectionContributors.put(contributor, sectionView);
			} else {
				// show -> hide
				SectionView sectionView = sectionContributors.get(contributor);
				scrollView.removeSection(sectionView);
				sectionContributors.put(contributor, null);
			}
			scrollView.layout();
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public View getPrintableContent() {
		return scrollView.getContentView();
	}

}
