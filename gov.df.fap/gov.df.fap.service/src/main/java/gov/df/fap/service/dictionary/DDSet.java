package gov.df.fap.service.dictionary;

import gov.df.fap.api.dictionary.interfaces.IDDSet;
import gov.df.fap.bean.dictionary.dto.EleRelationSQLDTO;
import gov.df.fap.service.util.UtilCache;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * IDD_Set接口实现类，数据字典服务端组件接口（数据元、要素关联、权限、同步）
 * @version 1.0
 * @author Zhang Peng
 * @since java 1.6
 */
@Component("sys.ddSetService")
public class DDSet implements IDDSet {
	@Autowired
  MetaDataOperation mdOperation = null;
	@Autowired
  RelationOperation relationOperation = null;

  /**
   * 数据元表查询
   * @param inObjXml 客户端组件构造的xml
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   */
  public String doMetaDataQuery(String inObjXml, boolean isNeedCount) {
    String result = null;
    try {
      // 用MetaDataOperation类的getMDByXml()实现
      result = mdOperation.getMDByXml(inObjXml, isNeedCount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 数据元表查询
   * @param inObjXml 客户端组件构造的xml
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   * @throws Exception 
   */
  public XMLData doMetaDataQueryAsXMLData(String code) {
    XMLData data = new XMLData();
    data.put("table_name", "sys_metadata" + Tools.addDbLink());
    XMLData par = new XMLData();
    par.put("par_code", "field_code");
    par.put("par_value", code != null ? code.toUpperCase() : code);
    data.put("par", par);
    try {
      return mdOperation.getMD(data.asXML("data"), false);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * 数据元表数据插入
   * @param inObjXml 客户端组件构造的xml
   * @return 插入对象
   * @throws Exception 操作失败原因
   */
  public XMLData insertMetaData(String inObjXml) throws Exception {
    // 用MetaDataOperation类的insertMDByXml()实现
    return mdOperation.insertMetaDataByXml(inObjXml);
  }

  /**
   * 数据元表数据插入
   * @param inObjXml 客户端组件构造的xml
   * @return 插入对象
   * @throws Exception 操作失败原因
   */
  public XMLData insertMetaData(Map fieldInfo) throws Exception {
    if (fieldInfo == null || fieldInfo.size() == 0) {
      throw new Exception("传入数据对象错误,无法执行插入操作!");
    }
    XMLData data = new XMLData();
    data.put("table_name", "sys_metadata" + Tools.addDbLink());
    int mapsize = fieldInfo.size();
    Iterator keyValuePairs = fieldInfo.entrySet().iterator();
    for (int i = 0; i < mapsize; i++) { // 递归Map，构造xml中插入数据的"字段名-值"
      Map.Entry entry = (Map.Entry) keyValuePairs.next();
      XMLData field = new XMLData();
      data.put("field", field);
      field.put("field_code", entry.getKey());
      field.put("field_value", entry.getValue());
    } // end for	   
    return mdOperation.insertMetaDataByXml(data.asXML("data"));
  }

  /**
   * 数据元表数据修改
   * @param inObjXml 客户端组件构造的xml
   * @return 修改后的对象
   * @throws Exception 操作失败原因
   */
  public XMLData modifyMetaData(String inObjXml) throws Exception {
    // 用MetaDataOperation类的modifyMDByXml()实现
    return mdOperation.modifyMetaDataByXml(inObjXml);
  }

  /**
   * 数据元表数据删除
   * @param id 数据元唯一ID
   * @return 操作是否成功
   * @throws Exception 操作失败原因
   */
  public boolean deleteMetaData(String id) throws Exception {
    // 用MetaDataOperation类的deleteMDByXml()实现
    return mdOperation.deleteMetaData(id);
  }

  /**
   * 根据传入的字段名获得本字段枚举值
   * @param field_code 字段名
   * @return List 枚举值列表,List中的Map已模拟成要素模式chr_id,chr_code,chr_name
   */
  public List getFieldEnumValueList(String field_code) {
    return mdOperation.getFieldEnumValueList(field_code);
  }

  /**
   * 根据传入的字段名获得本字段枚举值
   * @param field_code 字段名
   * @return String 枚举值字符串
   */
  public String getFieldEnumValueString(String field_code) {
    return mdOperation.getFieldEnumValueString(field_code);
  }

  /**
   * 插入枚举信息项（累加的方式）
   * @param fieldCode 名称，对应sys_enumerate中的field_code字段
   * @param value  由Map组成，键名chr_code,chr_name分别对应表sys_enumerate中的enu_code和enu_name字段
   * @return 操作是否成功
   * @throws Exception 异常
   */
  public boolean insertFieldEnumValue(String fieldCode, List value) throws Exception {
    return mdOperation.insertFieldEnumValue(fieldCode, value);
  }

  /**
   * 插入枚举信息项（全部替换）
   * @param fieldCode 名称，对应sys_enumerate中的field_code字段
   * @param value  由Map组成，键名chr_code,chr_name分别对应表sys_enumerate中的enu_code和enu_name字段
   * @return 操作是否成功
   * @throws Exception 异常
   */
  public boolean replaceFieldEnumValue(String fieldCode, List value) throws Exception {
    return mdOperation.replaceFieldEnumValue(fieldCode, value);
  }

  /**
  * 删除枚举信息项
  * @param fieldCode 名称，对应sys_enumerate中的field_code字段
  * @param value  对应表sys_enumerate中的enu_code字段
  * @return 操作是否成功
  * @throws Exception 异常
  */
  public boolean deleteFieldEnumValue(String fieldCode, String value) throws Exception {
    return mdOperation.deleteFieldEnumValue(fieldCode, value);
  }

  /**
   * 删除多项选中的枚举信息
   * @param fieldCode 名称，对应sys_enumerate中的field_code字段
   * @param value  对应表sys_enumerate中的enu_code字段
   * @return 操作是否成功
   * @author lwq 2008-03-28增加
   */
  public boolean deleteFieldListValue(String fieldCode, List value, String fieldValueSet) throws Exception {
    return mdOperation.deleteFieldListValue(fieldCode, value, fieldValueSet);
  }

  /**
   * 关联关系数据查询
   * @param inObjXml 客户端组件构造的xml
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   */
  public String doRelationQuery(String inObjXml, boolean isNeedCount) {

    String result = null;
    try {
      // 用RelationOperation类的getRelationByXml()实现
      result = relationOperation.getRelationByXml(inObjXml, isNeedCount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
  
  public String doRelationQuery(String inObjXml) {

	    String result = null;
	    try {
	      // 用RelationOperation类的getRelationByXml()实现
	      result = relationOperation.getRelationByXml(inObjXml);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return result;
	  }
  /**
   * 关联关系数据查询
   * @return 查询结果xml
   */
  public List doPrimaryRelationQuery(String slaveElementCode) {
    XMLData data = new XMLData();
    data.put("table_name", "SYS_RELATION_MANAGER" + Tools.addDbLink());
    XMLData par = new XMLData();
    data.put("par", par);
    par.put("par_code", "SEC_ELE_CODE");
    par.put("par_value", slaveElementCode != null ? slaveElementCode.toUpperCase() : slaveElementCode);
    try {
      return relationOperation.getRelation(data.asXML("data"), false).getRecordList();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * 关联关系数据查询
   * @return 查询结果xml
   */
  public List doSlaveRelationQuery(String primaryElementCode) {
    XMLData data = new XMLData();
    data.put("table_name", "SYS_RELATION_MANAGER" + Tools.addDbLink());
    XMLData par = new XMLData();
    data.put("par", par);
    par.put("par_code", "PRI_ELE_CODE");
    par.put("par_value", primaryElementCode != null ? primaryElementCode.toUpperCase() : primaryElementCode);
    try {
      return relationOperation.getRelation(data.asXML("data"), false).getRecordList();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * 关联关系明细表数据插入。
   *                                                     /关联关系主表信息
   * @param relationData 关联关系数据结构,结构为:relationData                         
   *                                                     \row 明细表配置信息detail --> (主控要素编码 - 被控要素列表)值对 
   * @return 关联关系唯一id 
   * @throws Exception 数据插入操作失败原因
   */
  public String insertRelation(XMLData relationData) throws Exception {
    return relationOperation.insertRelation(relationData);
  }

  /**
   * 关联关系明细表数据修改。
   *                                                     /关联关系主表信息
   * @param relationData 关联关系数据结构,结构为:relationData                         
   *                                                     \row 明细表配置信息XMLData --> (主控要素编码 - 被控要素列表)值对 
   * @return boolean 修改是否成功 
   * @throws Exception 数据修改操作失败原因
   */
  public boolean modifyRelation(XMLData relationData) throws Exception {
    //清空关联关系缓存
    UtilCache.eleRelationIDMap = null;
    return relationOperation.modifyRelation(relationData);
  }

  /**
   * 通过传入的关联关系id删除关联关系配置数据
   * @param relation_id 关联关系id
   * @return 操作是否成功的结果
  * @throws Exception 数据删除操作失败原因
   */
  public boolean deleteRelation(String relation_id) throws Exception {
    //清空缓存
    UtilCache.eleRelationIDMap = null;
    
    return relationOperation.deleteRelation(relation_id);
  }

  /**
   * 根据条件查询字符串查询数据
   * @param inXMLObj 传入查询配置xml
   * @param condition 附加过滤条件、
   * @param orderStr 排序字符
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   */
  public String getDataByCondition(String inXmlObj, String condition, String orderStr, boolean isNeedCount) {
    String result = null;
    try {
      // 用MetaDataOperation类的getDataByCondition()实现
      result = mdOperation.getDataByCondition(inXmlObj, condition, orderStr, isNeedCount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public RelationOperation getRelationOperation() {
    return relationOperation;
  }

  public void setRelationOperation(RelationOperation relationOperation) {
    this.relationOperation = relationOperation;
  }

  public MetaDataOperation getMdOperation() {
    return mdOperation;
  }

  public void setMdOperation(MetaDataOperation mdOperation) {
    this.mdOperation = mdOperation;
  }

  /** add by sungy 20080409
   * 根据条件查询对应信息,取得指定关联关系的某个主控要素的被控要素结果集
   * @param relation_code 关联关系编码
   * @param priEleValue 主控要素编码值
   * @param set_year：业务年度
   */
  public List getRelEleValuesByPriEle(String relation_code, String priEleValue, int set_year) throws Exception {
    return relationOperation.getRelationByPriEleValue(relation_code, priEleValue, set_year);
  }

  public EleRelationSQLDTO getEleRelationSQLDTO(String element, Map relation, String tableAlias) {
    return relationOperation.getEleRelationSQLDTO(element, relation, tableAlias);
  }

}
