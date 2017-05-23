package cn.edu.buaa.sei.rucm.spec.keywords;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ca.carleton.sce.squall.ucmeta.util.Keyword;
import ca.carleton.sce.squall.ucmeta.util.SentencePattern;

public class StandardKeywordContributor implements KeywordContributor {
	
	private static final Map<Keyword, Color> colorMap;
	
	static {
		colorMap = new HashMap<Keyword, Color>();
		
		Color bule = new Color(0x003cff);
//		colorMap.put(Keyword.RFS, bule);
//		colorMap.put(Keyword.IF, bule);
//		colorMap.put(Keyword.THEN, bule);
//		colorMap.put(Keyword.ELSE, bule);
//		colorMap.put(Keyword.ELSEIF, bule);
//		colorMap.put(Keyword.ENDIF, bule);
//		colorMap.put(Keyword.DO, bule);
//		colorMap.put(Keyword.UNTIL, bule);
//		
//		Color maroon = new Color(0xc80038);
//		colorMap.put(Keyword.ABORT, maroon);
//		colorMap.put(Keyword.RESUME_STEP, maroon);
//		
//		Color orange = new Color(0xff7800);
//		colorMap.put(Keyword.MEANWHILE, orange);
//		
//		Color grass = new Color(0x037400);
//		colorMap.put(Keyword.INCLUDE_USE_CASE, grass);
//		colorMap.put(Keyword.EXTENDED_BY_USE_CASE, grass);
//		
//		Color grape = new Color(0xae00d9);
//		colorMap.put(Keyword.VALIDATE_THAT, grape);
	}

	public StandardKeywordContributor() {
	}

	@Override
	public Keyword[] getExtentedKeywords() {
		return null;
	}

	@Override
	public Map<Keyword, Color> getKeywordSyntaxColors() {
		return Collections.unmodifiableMap(colorMap);
	}

	@Override
	public SentencePattern[] getExtendedPatterns() {
		return null;
	}

}
