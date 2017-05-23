package cn.edu.buaa.sei.ate.action;

import java.awt.Color;
import java.util.Map;

import java.util.HashMap;
import ca.carleton.sce.squall.ucmeta.util.Keyword;
import ca.carleton.sce.squall.ucmeta.util.SentencePattern;

public class ActionContributor implements cn.edu.buaa.sei.rucm.spec.keywords.KeywordContributor{
	public static final  Keyword MEASURE=new Keyword("����");
	public static final Keyword TERMINATE=new Keyword("��ֹ");
	public static final Keyword VALIDATE=new Keyword("�ж�");
	public static final Keyword BIGGER=new Keyword("����");
	public static final Keyword LESS=new Keyword("С��");
	public static final Keyword ANDACTION=new Keyword("��");
	public static final Keyword ADD=new Keyword("���");
	public static final Keyword WARNMESSAGE=new Keyword("������Ϣ");
	public static final Keyword WARNACTION=new Keyword("���涯��");
	public static final Keyword CONTINUEACTION=new Keyword("����");
	public static final Keyword IFACTION=new Keyword("���");
	
	@Override
	public Keyword[] getExtentedKeywords() {
		// TODO Auto-generated method stub
		return new Keyword[]{MEASURE,TERMINATE,VALIDATE,BIGGER,LESS,ANDACTION,ADD,WARNMESSAGE,WARNACTION,CONTINUEACTION,IFACTION};
	}

	@Override
	public Map<Keyword, Color> getKeywordSyntaxColors() {
		// TODO Auto-generated method stub
		Map<Keyword, Color> map = new HashMap();
		map.put(MEASURE, Color.BLUE);
		map.put(TERMINATE, Color.RED);
		map.put(VALIDATE,Color.BLUE);
		map.put(BIGGER, Color.orange);
		map.put(LESS,Color.orange);
		map.put(ANDACTION, Color.BLACK);
		map.put(ADD, Color.blue);
		map.put(WARNMESSAGE, Color.BLUE);
		map.put(WARNACTION, Color.BLUE);
		map.put(CONTINUEACTION, Color.BLUE);
		map.put(IFACTION, Color.BLUE);
		return map;
	}

	@Override
	public SentencePattern[] getExtendedPatterns() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
