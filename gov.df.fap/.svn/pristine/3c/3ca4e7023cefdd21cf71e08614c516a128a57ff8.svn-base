package gov.df.fap.service.util.beanfactory;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.UrlResource;

public class CustomBeanFactory extends XmlBeanFactory{
	private static Map beanMap = new HashMap();
	public CustomBeanFactory(UrlResource rs){
		super(rs);
	}
	public Object getBean(String name){
		if(beanMap.containsKey(name))
			return beanMap.get(name);
		else{
			return super.getBean(name);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根

	}

}
