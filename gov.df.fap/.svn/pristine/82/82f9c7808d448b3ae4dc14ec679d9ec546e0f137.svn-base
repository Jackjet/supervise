package gov.df.fap.service.util.beanfactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.UrlResource;

/**
 * 远程服务接口beanfactory实现类
 * 
 * @author swj
 * @version 1.0
 */
public class BeanFactoryUtil {

	private static Map factoryMap = new HashMap();

	public static BeanFactory getSpringBeanFactory(String xmlPath) {
		// 由于在swing事件线程中随意使用Thread多线程处理，导致线程同步错误，线程类加载器丢失，此处作为临时解决办法，做一个判断，如果类加载器不正确，设置正确类加载器
		if (factoryMap.containsKey(xmlPath)) {
			return (BeanFactory) factoryMap.get(xmlPath);
		} else {
			URL url = null;
			url = Thread.currentThread().getContextClassLoader().getResource(xmlPath);
			UrlResource cresource = new UrlResource(url);
			BeanFactory cfactory = new CustomBeanFactory(cresource);
			factoryMap.put(xmlPath, cfactory);
			return cfactory;
		}
	}

	public static BeanFactory getBeanFactory(String xmlPath) {
		return getSpringBeanFactory(xmlPath);
	}

	public static void main(String[] args) throws Exception {
	}
}
