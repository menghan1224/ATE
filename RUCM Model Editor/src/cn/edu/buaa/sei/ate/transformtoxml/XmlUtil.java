package cn.edu.buaa.sei.ate.transformtoxml;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XmlUtil {
	 public static void convertToXml(Object obj, String path) {  
	        try {  
	            // 利用jdk中自带的转换类实现  
	            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
	  
	            Marshaller marshaller = context.createMarshaller();  
	            // 格式化xml输出的格式  
	            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  
	                    Boolean.TRUE);  
	            // 将对象转换成输出流形式的xml  
	            // 创建输出流  
	            FileWriter fw = null;  
	            try {  
	                fw = new FileWriter(path);  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	            marshaller.marshal(obj, fw);  
	        } catch (JAXBException e) {  
	            e.printStackTrace();  
	        }  
	    }
		public static void main(String[] args){
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddhhmmss");
			String time=simpleDateFormat.format(System.currentTimeMillis());
		}

}
