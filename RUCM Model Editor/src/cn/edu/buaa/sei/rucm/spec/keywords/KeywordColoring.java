package cn.edu.buaa.sei.rucm.spec.keywords;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import ca.carleton.sce.squall.ucmeta.util.Keyword;

public class KeywordColoring {
	
	private KeywordColoring() {
	}
	
	private static final Map<Keyword, Color> COLORING_MAP;
	
	static {
		COLORING_MAP = new HashMap<Keyword, Color>();
	}
	
	public static Color getColor(Keyword keyword) {
		if (COLORING_MAP.containsKey(keyword)) {
			return COLORING_MAP.get(keyword);
		} else {
			return Color.BLUE; // default color
		}
	}
	
	public static void setColor(Keyword keyword, Color color) {
		COLORING_MAP.put(keyword, color);
	}

}
