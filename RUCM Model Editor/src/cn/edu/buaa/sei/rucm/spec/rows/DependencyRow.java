package cn.edu.buaa.sei.rucm.spec.rows;

import java.util.ArrayList;
import java.util.List;

import ca.carleton.sce.squall.ucmeta.Extend;
import ca.carleton.sce.squall.ucmeta.Include;
import ca.carleton.sce.squall.ucmeta.ModelElement;
import ca.carleton.sce.squall.ucmeta.UCMetaFactory;
import ca.carleton.sce.squall.ucmeta.UCModel;
import ca.carleton.sce.squall.ucmeta.UseCase;
import ca.carleton.sce.squall.ucmeta.util.Keyword;
import ca.carleton.sce.squall.ucmeta.util.SentenceUtility;
import ca.carleton.sce.squall.ucmeta.util.Token;
import ca.carleton.sce.squall.ucmeta.util.Token.KeywordToken;
import ca.carleton.sce.squall.ucmeta.util.Token.TokenType;
import cn.edu.buaa.sei.lmf.ManagedObject;
import cn.edu.buaa.sei.rucm.spec.SpecificationUtility;
import cn.edu.buaa.sei.rucm.spec.TextCellDecoratorRegistry.ElementType;
import cn.edu.buaa.sei.rucm.spec.widgets.PropertyTableViewRow;
import cn.edu.buaa.sei.rucm.spec.widgets.TableView;

public class DependencyRow extends PropertyTableViewRow<UseCase> {
	
	private final UCModel ownerUcModel;

	public DependencyRow(String displayName, UseCase model, UCModel owneruUcModel) {
		super(displayName, model, new PropertyChangeTrigger[] {
			new PropertyChangeTrigger(UseCase.KEY_EXTEND),
			new PropertyChangeTrigger(UseCase.KEY_INCLUDE)
		});
		this.ownerUcModel = owneruUcModel;
		getValueColumnView().setHintText("None");
	}

	@Override
	protected String sycnPropertyToText(UseCase model) {
		List<ModelElement> dependencies = new ArrayList<ModelElement>();
		dependencies.addAll(model.getInclude());
		dependencies.addAll(model.getExtend());
		if (dependencies.isEmpty()) {
			return "";
		} else {
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < dependencies.size(); i++) {
				if (i > 0) buf.append(", ");
				ModelElement dep = dependencies.get(i);
				if (dep.isKindOf(Include.TYPE_NAME)) {
					buf.append(Keyword.INCLUDE_USE_CASE.getName());
					buf.append(" ");
					buf.append(SpecificationUtility.toNameExpr((UseCase) dep.get(Include.KEY_ADDITION)));
				} else if (dep.isKindOf(Extend.TYPE_NAME)) {
					buf.append(Keyword.EXTENDED_BY_USE_CASE.getName());
					buf.append(" ");
					buf.append(SpecificationUtility.toNameExpr((UseCase) dep.get(Extend.KEY_EXTENSION)));
				}
			}
			return buf.toString();
		}
	}

	@Override
	protected void sycnTextToProperty(String text) throws PropertyRowException {
		text = text.trim();
		if (text.isEmpty()) {
			getModel().getInclude().clear();
			getModel().getExtend().clear();
		} else {
			String[] segments = text.split(",");
			final List<Include> includes = new ArrayList<Include>();
			final List<Extend> extendz = new ArrayList<Extend>();
			for (String segment : segments) {
				List<Token> tokens = new ArrayList<Token>();
				for (Token token : SentenceUtility.tokenizeSentence(segment)) {
					tokens.add(token);
				}
				if (tokens.size() == 2 &&
					tokens.get(0).getType() == TokenType.KEYWORD &&
					tokens.get(1).getType() == TokenType.NL) {
					final String name = tokens.get(1).getRange().getText().trim();
					ManagedObject usecase = SpecificationUtility.findObjectWithNameExpr(name, ownerUcModel, UseCase.TYPE_NAME);
					if (usecase == null) {
						throw new PropertyRowException(); // TODO
					}
					Keyword keyword = ((KeywordToken) tokens.get(0)).getKeyword();
					if (keyword == Keyword.INCLUDE_USE_CASE) {
						Include include = UCMetaFactory.createInclude();
						include.setIncludingCase(getModel());
						include.setAddition((UseCase) usecase);
						includes.add(include);
					} else if (keyword == Keyword.EXTENDED_BY_USE_CASE) {
						Extend extend = UCMetaFactory.createExtend();
						extend.setExtendedCase(getModel());
						extend.setExtension((UseCase) usecase);
						extendz.add(extend);
					} else {
						throw new PropertyRowException(); // TODO
					}
				} else {
					throw new PropertyRowException(); // TODO
				}
			}
			getModel().getInclude().clear();
			getModel().getInclude().addAll(includes);
			getModel().getExtend().clear();
			getModel().getExtend().addAll(extendz);
		}
	}
	
	@Override
	public void tableViewRowAdded(TableView table) {
		super.tableViewRowAdded(table);
		initDecorator(ElementType.DEPENDENCY);
	}

}
