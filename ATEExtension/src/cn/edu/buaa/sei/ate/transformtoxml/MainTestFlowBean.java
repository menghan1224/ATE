package cn.edu.buaa.sei.ate.transformtoxml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ca.carleton.sce.squall.ucmeta.BasicFlow;
import ca.carleton.sce.squall.ucmeta.Sentence;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="MainTestFlow")
@XmlType(propOrder={
		"flowName",
		"list",
		"postcondition"
})
public class MainTestFlowBean {
	private String flowName="Main Test Flow";
	private List<SentenceBean> list;
	private String postcondition="";
	public MainTestFlowBean(BasicFlow basicFlow){
		if(basicFlow.getPostCondition()!=null){
		for(Sentence sentence:basicFlow.getPostCondition().getSentences()){
			this.postcondition+=sentence.getContent();
		}
		}
		list=new ArrayList<SentenceBean>();
		for(int i=1;i<=basicFlow.getSteps().size();i++){
			SentenceBean bean=new SentenceBean(basicFlow.getSteps().get(i-1),i);
			list.add(bean);
		}
	}
	public MainTestFlowBean(){
		
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public List<SentenceBean> getList() {
		return list;
	}
	public void setList(List<SentenceBean> list) {
		this.list = list;
	}
	public String getPostcondition() {
		return postcondition;
	}
	public void setPostcondition(String postcondition) {
		this.postcondition = postcondition;
	}
	public String toString(){
		return flowName+":"+"\nsteps:"+list+"\nPostcondition:"+postcondition+"\n";
	}

}
