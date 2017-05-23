package cn.edu.buaa.sei.ate.action;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import ca.carleton.sce.squall.ucmeta.util.Keyword;
import ca.carleton.sce.squall.ucmeta.util.SentencePattern;

public class DataContributor implements cn.edu.buaa.sei.rucm.spec.keywords.KeywordContributor{
	public static final Keyword VOLT=new Keyword("电压");
	public static final Keyword PIN=new Keyword("引脚");
	public static final Keyword ELEC=new Keyword("电流");
	public static final Keyword PITCH=new Keyword("俯仰");
	public static final Keyword FREQUENCY=new Keyword("频率");
	public static final Keyword SIGNAL=new Keyword("信号");
	public static final Keyword RESIST=new Keyword("电阻");
	public static final Keyword STEPDATA=new Keyword("步骤");
	
	@Override
	public Keyword[] getExtentedKeywords() {
		// TODO Auto-generated method stub
		return new Keyword[]{VOLT,PIN,ELEC,PITCH,FREQUENCY,SIGNAL,RESIST,STEPDATA};
	}

	@Override
	public Map<Keyword, Color> getKeywordSyntaxColors() {
		// TODO Auto-generated method stub
		Map<Keyword,Color> map=new HashMap();
		map.put(VOLT, Color.RED);
		map.put(PIN, Color.RED);
		map.put(ELEC, Color.RED);
		map.put(PITCH, Color.RED);
		map.put(FREQUENCY, Color.RED);
		map.put(SIGNAL, Color.RED);
		map.put(RESIST, Color.RED);
		map.put(STEPDATA, Color.RED);
		return map;
		
	}

	@Override
	public SentencePattern[] getExtendedPatterns() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
