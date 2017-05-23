package cn.edu.buaa.sei.ate.transformtoxml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Test Cases")
@XmlType(propOrder={
		"list"
})
public class TestCasesBean {
	private List<TeseCaseBean> list;
	public TestCasesBean(List<TeseCaseBean> list){
		this.list=list;
	}
	public TestCasesBean(){
		
	}

}
