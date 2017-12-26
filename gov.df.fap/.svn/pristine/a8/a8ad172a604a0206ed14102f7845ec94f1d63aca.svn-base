
package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.api.dictionary.ElementInfo;
import gov.df.fap.bean.gl.coa.FCoaDTO;
import gov.df.fap.bean.gl.coa.FCoaDetail;
/**
 * 要素读取方式,根据指定的要素级次读取要素。
 * @author 
 * 
 */
public interface CoaElementAccessor{
	
	/**
	 * 读取要素,用于生成CCID
	 * @param targetCoaDetail
	 * @param sourceEleChrId
	 * @return
	 */
	public ElementInfo elementAccess(FCoaDetail targetCoaDetail, String sourceEleChrId);
	
	/**
	 * 查询数据库中存在的CCID,库中存在才返回, 主要用于要素匹配,一般CCID生成不用实现该接口.
	 * @param coaDto
	 * @param elementsConstainer 要素容器,里面存放<code>ElementInfo</code>对象
	 * @return
	 */
	public Object queryCcidObjectExists(FCoaDTO coaDto, Object elementsConstainer, boolean misMatch);
	
	/**
	 * 是否校验要素,便于处理一些特殊要素
	 * @param coaDetail
	 * @return
	 */
	public boolean isCheckElement(FCoaDetail coaDetail);
}