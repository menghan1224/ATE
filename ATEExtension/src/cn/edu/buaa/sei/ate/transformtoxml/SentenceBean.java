package cn.edu.buaa.sei.ate.transformtoxml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ca.carleton.sce.squall.ucmeta.Sentence;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Sentence")
@XmlType(propOrder={
		"sentenceType",
		"isNormalSentence",
		"step",
		"parameters",
		"content"
})
public class SentenceBean {
	public static HashMap<String, Integer> SENTENCEMAP=new HashMap<String, Integer>();
	{
		SENTENCEMAP.put("WaitSentence", 1);
		SENTENCEMAP.put("VoltSentence", 3);
		SENTENCEMAP.put("ValidateTimeSentence", 2);
		SENTENCEMAP.put("ValidateSentence", 2);
		SENTENCEMAP.put("ResistenceSentence", 3);
		SENTENCEMAP.put("PulseSignalSentence", 3);
		SENTENCEMAP.put("PauseSentence", 1);
		SENTENCEMAP.put("MonitorSentence", 3);
		SENTENCEMAP.put("LoopTimeSentence", 2);
		SENTENCEMAP.put("LoopLineSentence", 5);
		SENTENCEMAP.put("LoopConditionSentence", 2);
		SENTENCEMAP.put("FrequentSignalSentence", 1);
		SENTENCEMAP.put("CurrentSentence", 3);
		SENTENCEMAP.put("BoolSignalSentence", 3);
		SENTENCEMAP.put("AngleSignalSentence", 3);
	}
	
	
	public String getSentenceType() {
		return sentenceType;
	}
	public void setSentenceType(String sentenceType) {
		this.sentenceType = sentenceType;
	}
	public List<String> getParameters() {
		return parameters;
	}
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isNormalSentence() {
		return isNormalSentence;
	}
	public void setNormalSentence(boolean isNormalSentence) {
		this.isNormalSentence = isNormalSentence;
	}
	private String sentenceType;
	private List<String> parameters;
	private String content;
	private boolean isNormalSentence=false;
	private Integer step;
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	public SentenceBean(){
		
	}
	public SentenceBean(Sentence sentence){
		if(sentence.get("containate")!=null){
			String key=sentence.get("containate").get("description").stringValue();
			if(SENTENCEMAP.get(key)!=null){
				this.sentenceType=key;
				this.parameters=new ArrayList<String>();
				int numOfParas=SENTENCEMAP.get(key);
				for(int i=1;i<=numOfParas;i++){
					parameters.add(sentence.get("containate").get("para"+i).stringValue());
				}
			}
			else{
				isNormalSentence=true;
				this.content=sentence.getContent();
				this.sentenceType="NormalSentence";
			}
		}else{
			isNormalSentence=true;
			this.content=sentence.getContent();
			this.sentenceType="NormalSentence";
		}
		
	}
	public SentenceBean(Sentence sentence,Integer step){
		this.step=step;
		if(sentence.get("containate")!=null){
			String key=sentence.get("containate").get("description").stringValue();
			if(SENTENCEMAP.get(key)!=null){
				this.sentenceType=key;
				this.parameters=new ArrayList<String>();
				int numOfParas=SENTENCEMAP.get(key);
				for(int i=1;i<=numOfParas;i++){
					parameters.add(sentence.get("containate").get("para"+i).stringValue());
				}
			}
			else{
				isNormalSentence=true;
				this.content=sentence.getContent();
				this.sentenceType="NormalSentence";
			}
		}else{
			isNormalSentence=true;
			this.content=sentence.getContent();
			this.sentenceType="NormalSentence";
		}
		
	}
	public String toString(){
		if(isNormalSentence){
			return (step==null?" ":" step:"+step)+sentenceType+":"+content+"\n";
		}
		else{
			return (step==null?"":" step:"+step)+" "+sentenceType+":"+" parameters:"+parameters+"\n";
		}
	}

}
