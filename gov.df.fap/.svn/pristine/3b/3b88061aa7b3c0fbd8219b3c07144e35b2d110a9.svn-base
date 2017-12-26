package gov.df.fap.bean.util;

import gov.df.fap.util.xml.XMLData;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * DTO基础类,所有的DTO必须从该类继承
 * @author fangyi
 *
 */
public abstract class DTO implements Serializable 
{
	public static final long serialVersionUID = 2675368388887820112L;	
	
	private Boolean isCheck;		//是否多选
	
	
	/**
	 * 从DTO映射成Map
	 * @return 映射值对
	 */
	public Map mapping()throws Exception
	{
		Map map = null;
		try
		{
		  map = BeanUtils.describe(this);
		}catch(Exception e)
		{
			throw e;
		}
		return map;
	}
	/**
	* 从DTO映射成XMLData
	* @return XMLData 映射对象
	*/
	public XMLData toXMLData() throws Exception
	{
		XMLData xmlData = null;
		try
		{
			Map map = BeanUtils.describe(this);
			if(map != null)
			{
				xmlData = new XMLData();
				xmlData.putAll(map);
			}
		}catch(Exception e)
		{
			throw e;
		}
		return xmlData;
	}
	
	/**
	* 从Map拷贝到DTO对象
	* @param map 对象
	* @throws Exception 异常 
	*/
	public void copy(Map map)throws Exception
	{
		if(map == null)return;
		else
		{
			try
			{
				BeanUtils.copyProperties(this,map);
			}catch(Exception e)
			{
				throw e;
			}
		}
	}
	/**
	 * 克隆方法
	 * @throws CloneNotSupportedException 对象不支持克隆异常
	 */
	public Object clone()throws CloneNotSupportedException
	{
		Object obj = null;
		try
		{
			obj = BeanUtils.cloneBean(this);
		}catch(Exception e)
		{
			throw new CloneNotSupportedException(e.getMessage());
		}
		return obj;
	}
	
	public Boolean getIsCheck() {
		return isCheck;
	}
	
	
	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}	
}
