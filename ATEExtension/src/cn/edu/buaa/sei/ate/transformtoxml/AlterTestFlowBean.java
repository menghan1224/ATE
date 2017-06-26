package cn.edu.buaa.sei.ate.transformtoxml;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.SpecificAlternative;
import co.gongzh.snail.Image;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="SpecificValidationFlow")
@XmlType(propOrder={
		"flowName",
		"rfsSentence",
		"postcondition",
		"list"
})
public class AlterTestFlowBean {
	private String flowName="Specific Validation Flow";
	private String rfsSentence="";
	private String postcondition="";
	private List<SentenceBean> list;
	public AlterTestFlowBean(SpecificAlternative flow){
		if(flow.getRfsSentence()!=null){
		this.rfsSentence=flow.getRfsSentence().getContent();
		}
		for(Sentence sentence:flow.getSteps()){
			this.postcondition+=sentence.getContent();
		}
		list=new ArrayList<SentenceBean>();
		for(int i=1;i<=flow.getSteps().size();i++){
			SentenceBean bean=new SentenceBean(flow.getSteps().get(i-1),i);
			list.add(bean);
		}
	}
	public AlterTestFlowBean(){
		
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getRfsSentence() {
		return rfsSentence;
	}
	public void setRfsSentence(String rfsSentence) {
		this.rfsSentence = rfsSentence;
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
		return flowName+":"+"\nrfs:"+rfsSentence+"\nSteps:"+list+"\nPostcondition:"+postcondition;
	}
	

}
