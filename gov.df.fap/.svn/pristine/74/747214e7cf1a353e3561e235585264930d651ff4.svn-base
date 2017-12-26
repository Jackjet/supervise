package gov.df.fap.api.dictionary.interfaces;

import gov.df.fap.bean.dictionary.dto.EleRelationSQLDTO;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

/**
 * IDD_Set 数据字典服务端组件接口（数据元、要素关联、权限、同步）
 * @version 1.0
 * @author justin
 * @since java 1.6
 */
public interface IDDSet {
	/**
	 * 数据元表查询
	 * @param inXmlObj 客户端组件构造的xml
	 * @param isNeedCount 是否需要分页
	 * @return 查询结果xml
	 */
	public String doMetaDataQuery(String inXmlObj, boolean isNeedCount);
	
	/**
	 * 数据元表数据插入
	 * @param inXmlObj 客户端组件构造的xml
	 * @return 插入的对象
	 * @throws Exception 操作失败原因
	 */
	public XMLData insertMetaData(String inXmlObj) throws Exception;
	
	/**
	 * 数据元表数据修改
	 * @param inXmlObj 客户端组件构造的xml
	 * @return 修改后的对象
	 * @throws Exception 操作失败原因
	 */
	public XMLData modifyMetaData(String inXmlObj) throws Exception;
	
	/**
	 * 数据元表数据删除
	 * @param id 数据元唯一ID
	 * @return 操作是否成功
	 * @throws Exception 操作失败原因
	 */
	public boolean deleteMetaData(String id) throws Exception;
	
    /**
     * 根据传入的字段名获得本字段枚举值
     * @param field_code 字段名
     * @return String 枚举值字符串
     */
    public String getFieldEnumValueString(String field_code);
    
    /**
     * 根据传入的字段名获得本字段枚举值
     * @param field_code 字段名
     * @return List 枚举值列表,List中的Map已模拟成要素模式chr_id,chr_code,chr_name
     */
    public List getFieldEnumValueList(String field_code);
    /**
     * 插入枚举信息项（累加的方式）
     * @param fieldCode 名称，对应sys_enumerate中的field_code字段
     * @param value  由Map组成，键名chr_code,chr_name分别对应表sys_enumerate中的enu_code和enu_name字段
     * @return 操作是否成功
     * @throws Exception 异常
     */
     public boolean insertFieldEnumValue(String fieldCode, List value)throws Exception;
     /**
      * 插入枚举信息项（全部替换）
      * @param fieldCode 名称，对应sys_enumerate中的field_code字段
      * @param value  由Map组成，键名chr_code,chr_name分别对应表sys_enumerate中的enu_code和enu_name字段
      * @return 操作是否成功
      * @throws Exception 异常
      */
      public boolean replaceFieldEnumValue(String fieldCode, List value)throws Exception;     
     /**
     * 删除枚举信息项
     * @param fieldCode 名称，对应sys_enumerate中的field_code字段
     * @param value  对应表sys_enumerate中的enu_code字段
     * @return 操作是否成功
     * @throws Exception 异常
     */
    public boolean deleteFieldEnumValue(String fieldCode, String value)throws Exception;
    /**
	   * 删除多项选中的枚举信息
	   * @param fieldCode 名称，对应sys_enumerate中的field_code字段
	   * @param value  对应表sys_enumerate中的enu_code字段
	   * @return 操作是否成功
	   * @throws Exception 异常
	   * @author lwq 2008-03-28增加
	   */
    public boolean deleteFieldListValue(String fieldCode, List value,String fieldValueSet)throws Exception;
	/**
	 * 关联关系数据查询
	 * @param inXmlObj 客户端组件构造的xml
	 * @return 查询结果xml
	 */    
	public String doRelationQuery(String inXmlObj, boolean isNeedCount);
	public String doRelationQuery(String inXmlObj);
	/**
	 * 关联关系明细表数据插入。
	 *                                                     /关联关系主表信息
	 * @param relationData 关联关系数据结构,结构为:relationData                         
	 *                                                     \row 明细表配置信息detail --> (主控要素编码 - 被控要素列表)值对 
	 * @return 关联关系唯一id 
	 * @throws Exception 数据插入操作失败原因
	 */
	public String insertRelation(XMLData relationData) throws Exception;
	
	/**
	 * 关联关系明细表数据修改。
	 *                                                     /关联关系主表信息
	 * @param relationData 关联关系数据结构,结构为:relationData                         
	 *                                                     \row 明细表配置信息XMLData --> (主控要素编码 - 被控要素列表)值对 
	 * @return boolean 修改是否成功 
	 * @throws Exception 数据修改操作失败原因
	 */
	public boolean modifyRelation(XMLData relationData) throws Exception;
	
    /**
     * 通过传入的关联关系id删除关联关系配置数据
     * @param relation_id 关联关系id
     * @return 操作是否成功的结果
	 * @throws Exception 数据删除操作失败原因
     */
	public boolean deleteRelation(String relation_id) throws Exception;
	
	/**
	 * 根据条件查询对应信息
	 * @param inXmlObj 查询配置信息
	 * @param condition 附加过滤条件
	 * @param orderStr 排序条件
	 * @param isNeedCount 是否需要分页
	 * @return 结果集xml串
	 */
	public String getDataByCondition(String inXmlObj,String condition,String orderStr,boolean isNeedCount);
	
	/** 
	 * 根据条件查询对应信息,取得指定关联关系的某个主控要素的被控要素结果集
	 * @param relation_code 关联关系编码
	 * @param priEleValue 主控要素编码值
	 * @param set_year：业务年度
	 */
	public List getRelEleValuesByPriEle(String relation_code,String priEleValue,int set_year)throws Exception;

	/** 
	 * 根据被控要素编码，主控要素编码和值，拼凑from和where子句
	 * @param element 被控要素编码
	 * @param relation 主控要素编码和值
	 * @param tableAlias 被控要素表的别名
	 * @return EleRelationSQLDTO对象
	 */
    public EleRelationSQLDTO getEleRelationSQLDTO(String element, Map relation, String tableAlias);
    

}
