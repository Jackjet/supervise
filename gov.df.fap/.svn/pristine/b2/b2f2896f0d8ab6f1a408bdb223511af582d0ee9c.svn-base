package gov.df.fap.service.relation;

import gov.df.fap.api.dictionary.interfaces.IDDElement;
import gov.df.fap.api.dictionary.interfaces.IDDSet;
import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.ParseXML;
import gov.df.fap.util.xml.XMLData;

import java.util.Iterator;
import java.util.Map;

/**
 * 客户端接口常规操作 1、针对单字段的条件查询 2、简单的插入、修改、删除数据 3、远程接口调用
 * 
 * @version 1.0
 * @author Zhang Peng
 * @since java 1.4.2
 */
public class CommonOperation {
  static IDDElement ddElementBean = null;

  static IDDSet ddSetBean = null;

  static IDictionary dicBean = null;

  /**
   * 错误信息
   */
  private StringBuffer errorStr = new StringBuffer();

  /**
   * 是否需要返回查询记录总数
   */
  private boolean isNeedCount = true;

  /**
   * 设置是否需要返回查询记录总数
   */
  public void setTotalCount(boolean isNeedCount) {
    this.isNeedCount = isNeedCount;
  }

  /**
   * 返回是否需要总记录数
   */
  public boolean getTotalCount() {
    return isNeedCount;
  }

  /**
   * 查询类远程接口调用。
   * 
   * @param data
   *            根据不同操作构造的XMLData
   * @param tableAlias
   *            数据库表名
   * @param isNeedRight
   *            是否需要权限过滤
   * @throws Exception
   *             服务端组件运行异常
   */
  private String getRemoteInterface(XMLData data, String tableAlias, boolean isNeedRight) throws Exception {
    String xmlStr = "";
    try {
      if (tableAlias.equalsIgnoreCase("SYS_ELEMENT" + Tools.addDbLink())) // 要素管理
        // 调用数据字典组件服务端接口，返回查询结果xml
        xmlStr = getIDDElement().doEleSetQuery(ParseXML.convertObjToXml(data, "data"), isNeedCount);
      else if (tableAlias.equalsIgnoreCase("SYS_METADATA" + Tools.addDbLink())) // 数据元
        xmlStr = getIDDSet().doMetaDataQuery(ParseXML.convertObjToXml(data, "data"), isNeedCount);
      else if (tableAlias.equalsIgnoreCase("SYS_RELATION_MANAGER" + Tools.addDbLink()) // 关联关系管理/明细
        || tableAlias.equalsIgnoreCase("SYS_RELATIONS" + Tools.addDbLink()))
        xmlStr = getIDDSet().doRelationQuery(ParseXML.convertObjToXml(data, "data"), isNeedCount);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
    return xmlStr;
  }

  private String getRemoteInterface(XMLData data, String tableAlias) throws Exception {
    String xmlStr = "";
    try {
      if (tableAlias.equalsIgnoreCase("SYS_ELEMENT" + Tools.addDbLink())) // 要素管理
        // 调用数据字典组件服务端接口，返回查询结果xml
        xmlStr = getIDDElement().doEleSetQuery(ParseXML.convertObjToXml(data, "data"), isNeedCount);
      else if (tableAlias.equalsIgnoreCase("SYS_METADATA" + Tools.addDbLink())) // 数据元
        xmlStr = getIDDSet().doMetaDataQuery(ParseXML.convertObjToXml(data, "data"), isNeedCount);
      else if (tableAlias.equalsIgnoreCase("SYS_RELATION_MANAGER" + Tools.addDbLink()) // 关联关系管理/明细
        || tableAlias.equalsIgnoreCase("SYS_RELATIONS" + Tools.addDbLink()))
        xmlStr = getIDDSet().doRelationQuery(ParseXML.convertObjToXml(data, "data"));
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
    return xmlStr;
  }

  /**
   * 根据数据库表名查询数据，返回分页当前页码的查询结果。
   * 
   * @param tableAlias
   *            数据库表名
   * @param pageIndex
   *            分页当前页
   * @param isNeedRight
   *            是否需要权限过滤
   * @return 返回查询结果对应的XMLData
   */
  protected XMLData getData(String tableAlias, String pageIndex, boolean isNeedRight) {
    // 构造<data>元素
    XMLData data = new XMLData();
    data.put("table_name", tableAlias); // table_name属性
    data.put("page_index", pageIndex); // page_index属性
    XMLData result = new XMLData();
    try {
      // 调用查询接口，返回查询结果XMLData
      result = ParseXML.convertXmlToObj(getRemoteInterface(data, tableAlias, isNeedRight));
    } catch (Exception e) {
    }
    return result;
  }

  protected XMLData getData(String ele_code, String tableAlias, String pageIndex, boolean isNeedRight) {
    // 构造<data>元素
    XMLData data = new XMLData();
    data.put("ele_code", ele_code); // ele_code属性
    data.put("table_name", tableAlias); // table_name属性
    data.put("page_index", pageIndex); // page_index属性
    XMLData result = new XMLData();
    try {
      // 调用查询接口，返回查询结果XMLData
      result = ParseXML.convertXmlToObj(getRemoteInterface(data, tableAlias, isNeedRight));
    } catch (Exception e) {
    }
    return result;
  }

  /**
   * 根据数据库表名查询数据，按显示字段返回分页处理后的查询结果。
   * @param tableAlias 数据库表名
   * @param pageIndex 分页当前页
   * @param pageCount 分页页大小
   * @param displayField 显示字段
   * @return 返回查询结果对应的XMLData
   */
  protected XMLData getDataByDispField(String tableAlias, String pageIndex, String pageCount, String displayField[]) {
    // 构造<data>元素
    XMLData data = new XMLData();
    data.put("table_name", tableAlias); // table_name属性
    data.put("page_index", pageIndex); // page_index属性
    data.put("page_count", pageCount); // page_count属性
    data.put("display_type", "2"); // display_type属性
    // 构造<display>元素
    XMLData display = new XMLData();
    data.put("display", display);
    for (int i = 0; i < displayField.length; i++) {
      XMLData col = new XMLData();
      display.put("col", col);
      col.put("col_field", displayField[i]); // col_field属性
    }
    XMLData result = new XMLData();
    try {
      // 调用查询接口，返回查询结果XMLData
      result = ParseXML.convertXmlToObj(getRemoteInterface(data, tableAlias, isNeedCount));
    } catch (Exception e) {
    }
    return result;
  }

  /**
   * 根据条件查询字符串查询数据
   * 无需分页请求
   * @param tableAlias 数据表名
   * @param displayField 显示字段
   * @return 返回查询结果对应的XMLData
   */
  protected XMLData getDataByDispField(String tableAlias, String displayField[]) {
    // 构造<data>元素
    XMLData data = new XMLData();
    data.put("table_name", tableAlias); // table_name属性
    data.put("display_type", "2"); // display_type属性
    // 构造<display>元素
    XMLData display = new XMLData();
    data.put("display", display);
    for (int i = 0; i < displayField.length; i++) {
      XMLData col = new XMLData();
      display.put("col", col);
      col.put("col_field", displayField[i]); // col_field属性
    }
    XMLData result = new XMLData();
    try {
      // 调用查询接口，返回查询结果XMLData
      result = ParseXML.convertXmlToObj(getRemoteInterface(data, tableAlias));
    } catch (Exception e) {
    }
    return result;
  }

  /**
   * 根据条件查询字符串查询数据
   * @param tableAlias 数据表名
   * @param pageIndex 分页当前页
   * @param pageCount 分页页大小
   * @param displayField 显示字段
   * @param condition 条件查询字符串
   * @param orderStr 排序条件
   * @return 返回查询结果对应的XMLData
   */
  public XMLData getDataByCondition(String tableAlias, String pageIndex, String pageCount, String displayField[],
    String condition, String orderStr) {
    // 构造<data>元素
    XMLData data = new XMLData();
    data.put("table_name", tableAlias); // table_name属性
    data.put("page_index", pageIndex); // page_index属性
    data.put("page_count", pageCount); // page_count属性
    if (displayField != null) {
      data.put("display_type", "2"); // display_type属性
      // 构造<display>元素
      XMLData display = new XMLData();
      data.put("display", display);
      for (int i = 0; i < displayField.length; i++) {
        XMLData col = new XMLData();
        display.put("col", col);
        col.put("col_field", displayField[i]); // col_field属性
      }
    } else
      data.put("display_type", "1");
    XMLData result = new XMLData();
    try {
      // 调用通用查询接口，返回查询结果XMLData
      result = ParseXML.convertXmlToObj(getIDDSet().getDataByCondition(ParseXML.convertObjToXml(data, "data"),
        condition, orderStr, isNeedCount));
    } catch (Exception e) {

    }
    return result;
  }

  /**
   * 条件查询数据库表数据
   * 
   * @param tableAlias
   *            数据库表名
   * @param fieldName
   *            字段名
   * @param fieldValue
   *            字段值
   * @param isNeedRight
   *            是否需要权限过滤
   * @return 返回查询结果对应的XMLData
   */
  protected XMLData getDataBySingleField(String tableAlias, String fieldName, String fieldValue, boolean isNeedRight) {
    XMLData data = new XMLData();
    data.put("table_name", tableAlias);
    XMLData par = new XMLData();
    data.put("par", par);
    par.put("par_code", fieldName);
    par.put("par_value", fieldValue != null ? fieldValue.toUpperCase() : fieldValue);
    XMLData result = new XMLData();
    try {
      result = ParseXML.convertXmlToObj(getRemoteInterface(data, tableAlias, isNeedRight));
    } catch (Exception e) {

    }
    return result;
  }

  protected XMLData getDataBySingleField(String tableAlias, String fieldName, String fieldValue) {
    XMLData data = new XMLData();
    data.put("table_name", tableAlias);
    XMLData par = new XMLData();
    data.put("par", par);
    par.put("par_code", fieldName);
    par.put("par_value", fieldValue != null ? fieldValue.toUpperCase() : fieldValue);
    XMLData result = new XMLData();
    try {
      result = ParseXML.convertXmlToObj(getRemoteInterface(data, tableAlias));
    } catch (Exception e) {

    }
    return result;
  }

  protected XMLData getDataBySingleField(String eleCode, String tableAlias, String fieldName, String fieldValue,
    boolean isNeedRight) {
    XMLData data = new XMLData();
    data.put("ele_code", eleCode);
    data.put("table_name", tableAlias);
    XMLData par = new XMLData();
    data.put("par", par);
    par.put("par_code", fieldName);
    par.put("par_value", fieldValue != null ? fieldValue.toUpperCase() : fieldValue);
    XMLData result = new XMLData();
    try {
      result = ParseXML.convertXmlToObj(getRemoteInterface(data, tableAlias, isNeedRight));
    } catch (Exception e) {

    }
    return result;
  }

  /**
   * 向数据表插入数据，返回插入数据的唯一ID
   * 
   * @param tableAlias
   *            要素表表名
   * @param fieldInfo
   *            值集(key - value)
   * @return 插入数据对象
   * @throws Exception
   *             操作失败原因
   */
  protected XMLData insertData(String table_name, Map fieldInfo) throws Exception {
    XMLData result = null;
    XMLData data = new XMLData();
    data.put("table_name", table_name);
    int mapsize = fieldInfo.size();
    Iterator keyValuePairs = fieldInfo.entrySet().iterator();
    for (int i = 0; i < mapsize; i++) { // 递归Map，构造xml中插入数据的"字段名-值"
      Map.Entry entry = (Map.Entry) keyValuePairs.next();
      XMLData field = new XMLData();
      data.put("field", field);
      field.put("field_code", entry.getKey());
      field.put("field_value", entry.getValue());
    } // end for
    try {
      // 根据调用远程接口的不同，如操作成功返回CHR_ID
      if (table_name.equalsIgnoreCase("SYS_ELEMENT" + Tools.addDbLink()))
        result = getIDDElement().insertEleSet(ParseXML.convertObjToXml(data, "data"));
      else if (table_name.equalsIgnoreCase("SYS_METADATA" + Tools.addDbLink()))
        result = getIDDSet().insertMetaData(ParseXML.convertObjToXml(data, "data"));
      else
        // 调用远程业务接口insertEle()
        result = getIDDElement().insertEle(ParseXML.convertObjToXml(data, "data"));
    } catch (Exception e) {
      errorStr.delete(0, errorStr.length());
      errorStr.append("向 " + table_name + " 表中插入数据时出错\n");
      errorStr.append("出错原因：" + e.getMessage());
      throw new Exception(errorStr.toString());
    }
    return result;
  }

  /**
   * 修改数据表数据
   * 
   * @param tableAlias
   *            表名
   * @param id
   *            唯一ID
   * @param fieldInfo
   *            值集
   * @return 修改后的数据对象
   * @throws Exception
   *             数据修改操作失败原因
   */
  protected XMLData modifyData(String tableAlias, String fieldCode, String id, Map fieldInfo) throws Exception {
    XMLData result = null;
    XMLData data = new XMLData();
    data.put("table_name", tableAlias);
    int mapsize = fieldInfo.size();
    Iterator keyValuePairs = fieldInfo.entrySet().iterator();
    for (int i = 0; i < mapsize; i++) { // 递归Map，构造xml中修改数据的"字段名-值"
      Map.Entry entry = (Map.Entry) keyValuePairs.next();
      XMLData field = new XMLData();
      data.put("field", field);
      field.put("field_code", entry.getKey());
      field.put("field_value", entry.getValue());
    }
    XMLData par = new XMLData();
    data.put("par", par);
    par.put("par_code", fieldCode);
    par.put("par_value", id);
    try {
      // 根据调用远程接口的不同，返回boolean形式的查询结果
      if (tableAlias.equalsIgnoreCase("SYS_ELEMENT" + Tools.addDbLink()))
        // 调用远程业务接口modifyEleSet()
        result = getIDDElement().modifyEleSet(ParseXML.convertObjToXml(data, "data"));
      else if (tableAlias.equalsIgnoreCase("SYS_METADATA" + Tools.addDbLink()))
        // 调用远程业务接口modifyDM()
        result = getIDDSet().modifyMetaData(ParseXML.convertObjToXml(data, "data"));
      else
        // 要素表
        // 调用远程业务接口modifyEle()
        result = getIDDElement().modifyEle(ParseXML.convertObjToXml(data, "data"));
    } catch (Exception e) {
      errorStr.delete(0, errorStr.length());
      errorStr.append("修改 " + tableAlias + " 表中数据时出错\n");
      errorStr.append("出错原因：" + e.getMessage());
      throw new Exception(errorStr.toString());
    }
    return result;
  }

  /**
   * 单例获得IDDSet客户端代理,减轻服务压力
   * 
   * @return IDDSet客户端代理
   */
  public static synchronized IDDSet getIDDSet() {
    try {
      ddSetBean = (IDDSet) SessionUtil.getServerBean("sys.ddSetService");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ddSetBean;
  }

  /**
   * 单例获得IDDElement客户端代理,减轻服务压力
   * 
   * @return IDDElement客户端代理
   */
  public static synchronized IDDElement getIDDElement() {
    try {

      ddElementBean = (IDDElement) SessionUtil.getServerBean("sys.ddElementService");

    } catch (Exception e) {
      e.printStackTrace();
    }
    return ddElementBean;
  }

  /**
   * 单例获得IDictionary客户端代理,减轻服务压力
   * 
   * @return IDictionary客户端代理
   */
  public static synchronized IDictionary getIDictionary() {
    try {
      dicBean = (IDictionary) SessionUtil.getServerBean("sys.dictionaryService");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dicBean;
  }
}
