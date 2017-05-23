package cn.edu.buaa.sei.ate.transformtoxml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ca.carleton.sce.squall.ucmeta.AlternativeFlow;
import ca.carleton.sce.squall.ucmeta.GlobalAlternative;
import ca.carleton.sce.squall.ucmeta.Sentence;
import ca.carleton.sce.squall.ucmeta.SpecificAlternative;
import ca.carleton.sce.squall.ucmeta.UseCaseImpl;
import ca.carleton.sce.squall.ucmeta.UseCaseSpecification;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="TestCase")
@XmlType(propOrder={
		"name",
		"usecaseName",
		"briefDescription",
		"precondition",
		"mainTestFlow",
		"specificValidationFlows",
		"globalValidationFlows"
})

public class TeseCaseBean {
	private String name="Test Case";
	private String usecaseName="";
	private String briefDescription="";
	private String precondition="";
	private MainTestFlowBean mainTestFlow;
	private List<AlterTestFlowBean> specificValidationFlows;
	private List<GlobalTestFlowBean> globalValidationFlows;
	public TeseCaseBean(UseCaseSpecification spec,UseCaseImpl usecase){
		this.usecaseName=usecase.getName();
		if(spec.getBriefDescription()!=null){
		for(Sentence sentence:spec.getBriefDescription().getSentences()){
			this.briefDescription+=sentence.getContent();
		}
		}
		if(spec.getPreCondition()!=null){
		for(Sentence sentence:spec.getPreCondition().getSentences()){
			this.precondition+=sentence.getContent();
		}
		}
		this.mainTestFlow=new MainTestFlowBean(spec.getBasicFlow());
		globalValidationFlows=new ArrayList<GlobalTestFlowBean>();
		specificValidationFlows=new ArrayList<AlterTestFlowBean>();
		for(AlternativeFlow flow:spec.getAlternativeFlows()){
			if(flow instanceof GlobalAlternative){
				GlobalTestFlowBean bean=new GlobalTestFlowBean((GlobalAlternative)flow);
				globalValidationFlows.add(bean);
			}
			if(flow instanceof SpecificAlternative){
				AlterTestFlowBean bean=new AlterTestFlowBean((SpecificAlternative)flow);
				specificValidationFlows.add(bean);
			}
		}
		
	}
	public TeseCaseBean(){
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsecaseName() {
		return usecaseName;
	}
	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}
	public String getBriefDescription() {
		return briefDescription;
	}
	public void setBriefDescription(String briefDescription) {
		this.briefDescription = briefDescription;
	}
	public String getPrecondition() {
		return precondition;
	}
	public void setPrecondition(String precondition) {
		this.precondition = precondition;
	}
	public MainTestFlowBean getMainTestFlow() {
		return mainTestFlow;
	}
	public void setMainTestFlow(MainTestFlowBean mainTestFlow) {
		this.mainTestFlow = mainTestFlow;
	}
	public List<AlterTestFlowBean> getSpecificValidationFlows() {
		return specificValidationFlows;
	}
	public void setSpecificValidationFlows(List<AlterTestFlowBean> specificValidationFlows) {
		this.specificValidationFlows = specificValidationFlows;
	}
	public List<GlobalTestFlowBean> getGlobalValidationFlows() {
		return globalValidationFlows;
	}
	public void setGlobalValidationFlows(List<GlobalTestFlowBean> globalValidationFlows) {
		this.globalValidationFlows = globalValidationFlows;
	}
	public String toString(){
		return name+"\n"+"Test Case Name:"+this.usecaseName+"\n"+"BriefDescription:"+this.briefDescription+"\n"+"Precondition:"+this.precondition+"\n"
					+mainTestFlow+"\n"+specificValidationFlows+"\n"+globalValidationFlows;
	}
}
