package gov.df.fap.api.dictionary.interfaces;

import gov.df.fap.bean.dictionary.dto.FElementDTO;
import gov.df.fap.util.xml.XMLData;

import java.util.Map;


/**
 * IDD_Element 数据字典服务端组件接口
 * 接收数据字典客户端组件的服务请求以及构造的xml字符串，
 * 对要素管理表（SYS_ELEMENT）及要素表（ELE_*）进行增、删、查、改操作，
 * 返回数据库操作结果。
 * @version 1.0
 * @author justin
 * @since java 1.6
 */
public interface IDDElement 
{	
	/**
	 * 是否要素判断函数
	 * @param element 要素简称
	 * @return 判断结果
	 */
	public boolean isElement(String element);	
	/**
	 * 根据要素表别名、分页当前页、分页页大小、显示字段、
	 * 是否需要权限过滤、要素COA、要素关联关系、查询条件字符串查询要素表数据，
	 * 按显示字段返回分页处理后的查询结果。
	 * @param element 要素简称
	 * @param pageIndex 当前页
	 * @param pageCount 当前页大小
	 * @param column 需要显示的字段
	 * @param isNeedRight 是否需要权限过滤
	 * @param coa_id 要素表COA
	 * @param relation 要素关联关系
	 * @param condition 查询条件字符串
	 * @param tableAlias 要素表名
	 * @return 查询结果XMLData对象
	 */
	public XMLData getEleByCondition(String element
									,int pageIndex
									,int pageCount
									,String[] column
									,boolean isNeedRight
									,String coa_id
									,Map relation
									,String condition);
	
	/**
	 * 根据传入的xml对象，执行相应的要素表数据插入
	 * @param inXmlObj 客户端组件构造的xml
	 * @return 插入对象
	 * @exception 操作失败原因
	 */
	public XMLData insertEle(String inXmlObj) throws Exception;
	
	/**
	 * 根据传入的xml对象，执行相应的要素表数据修改
	 * @param inXmlObj 客户端组件构造的xml
	 * @return 修改后的对象
	 * @exception 操作失败原因
	 */
	public XMLData modifyEle(String inXmlObj) throws Exception;
	
	/**
     * 级联删除要素表数据
	 * @param element 要素简称
	 * @param id 数据id
     * @return 操作状态
     * @throws Exception 操作失败原因
     */
	public boolean deleteEle(String element,String id) throws Exception;
	
	/**
	 * 根据传入的xml对象，执行相应的要素管理表查询
	 * @param inXmlObj 客户端组件构造的xml
	 * @param isNeedCount 是否需要分页
	 * @return 查询结果xml
	 */
	public String doEleSetQuery(String inXmlObj, boolean isNeedCount);
	
	/**
	 * 根据传入的xml对象，执行相应的要素管理表数据插入
	 * @param inXmlObj 客户端组件构造的xml
	 * @return 插入对象
	 * @exception 操作失败原因
	 */
	public XMLData insertEleSet(String inXmlObj) throws Exception;
	
	/**
	 * 根据传入的xml对象，执行相应的要素管理表数据修改
	 * @param inXmlObj 客户端组件构造的xml
	 * @return 修改后的对象
	 * @exception 操作失败原因
	 */
	public XMLData modifyEleSet(String inXmlObj) throws Exception;
	
	/**
	 * 根据传入的id，执行相应的要素管理表数据删除
	 * @param id 要素配置信息唯一ID
	 * @return 操作是否成功
	 * @exception 操作失败原因
	 */
	public boolean deleteEleSet(String id) throws Exception;
	/**
	 * 根据要素表别名、要素值查询处理后的查询结果。
	 * @param elementDtos 传入的elementDtos
	 * @param tableAlias  要素表别名
	 * @return 查询结果xml
	 */
	public String getCondition(FElementDTO elementDto, String tableAlias);
	/**
	 * 实现返回基础要素表的数据权限sql语句
	 * @param userid-------------用户id
	 * @param roleid-------------角色id
	 * @param elemcode-----------要素简称
	 * @param tablealias---------基础表别名
	 * @return sql语句
	 * @throws Exception---------错误信息
	 */
	public String getSqlElemRight(String userid, String roleid,String elemcode,String tablealias)throws Exception;	
}