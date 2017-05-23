package cn.edu.buaa.sei.ate.transformtoxml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ca.carleton.sce.squall.ucmeta.GlobalAlternative;
import ca.carleton.sce.squall.ucmeta.Sentence;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="GlobalValidationFlow")
@XmlType(propOrder={
		"flowName",
		"guardCondition",
		"postcondition",
		"list"
})
public class GlobalTestFlowBean {
	private String guardCondition="";
	private String flowName="Global Validation Flow";
	private String postcondition="";
	private List<SentenceBean> list;
	public GlobalTestFlowBean(GlobalAlternative flow){
		this.guardCondition=flow.getConditionSentence().getContent();
		for(Sentence sentence:flow.getPostCondition().getSentences()){
			this.postcondition+=sentence.getContent();
		}
		list=new ArrayList<SentenceBean>();
		for(int i=1;i<=flow.getSteps().size();i++){
			SentenceBean bean=new SentenceBean(flow.getSteps().get(i-1),i);
			list.add(bean);
		}
	}
	public GlobalTestFlowBean(){
		
	}
	public String getGuardCondition() {
		return guardCondition;
	}
	public void setGuardCondition(String guardCondition) {
		this.guardCondition = guardCondition;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getPostcondition() {
		return postcondition;
	}
	public void setPostcondition(String postcondition) {
		this.postcondition = postcondition;
	}
	public List<SentenceBean> getList() {
		return list;
	}
	public void setList(List<SentenceBean> list) {
		this.list = list;
	}
	public String toString(){
		return flowName+":"+"\nGuard Condition:"+guardCondition+"\nSteps:"+list+"\nPostcondition:"+postcondition;
	}
	
}
