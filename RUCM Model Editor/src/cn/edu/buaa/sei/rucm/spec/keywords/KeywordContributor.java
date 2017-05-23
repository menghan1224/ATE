package cn.edu.buaa.sei.rucm.spec.keywords;

import java.awt.Color;
import java.util.Map;

import ca.carleton.sce.squall.ucmeta.util.Keyword;
import ca.carleton.sce.squall.ucmeta.util.SentencePattern;

public interface KeywordContributor {

	public Keyword[] getExtentedKeywords();
	public Map<Keyword, Color> getKeywordSyntaxColors();
	public SentencePattern[] getExtendedPatterns();
	
}
