package gov.df.fap.service.dictionary;

import gov.df.fap.service.util.DatabaseAccess;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.ParseXML;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.stereotype.Component;

/**
 * MetaData_Operation 内部实现类
 * @version 1.0
 * @author Zhang Peng
 * @since java 1.4.1
 */
@Component
public class MetaDataOperation extends DBOperation {

  /**
   * 默认构造函数
   */
  public MetaDataOperation() {
  }

  /**
   * 通过传入的xml查询数据元表数据并返回结果
   * @param inXmlObj 传入的xml
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   * @throws Exception 查询失败原因
   */
  public String getMDByXml(String inXmlObj, boolean isNeedCount) throws Exception {
    String total_count = "-1";
    String sql = ParseXML.convertXmlToQuerySQL(inXmlObj);
    XMLData condition = ParseXML.convertXmlToObj(inXmlObj);
    if (isNeedCount) {
      total_count = this.getTotalCount(sql);
      String index = (String) condition.getSubObject("page_index");
      index = (index == null || index.equals("")) ? "0" : index;
      String count = (String) condition.getSubObject("page_count");
      count = (count == null || count.equals("")) ? "0" : count;
      setPageInfo(Integer.parseInt(index), Integer.parseInt(count));// 设置分页信息    	
    } else {
      this.setPageIndex(0);
    }
    List ret = queryBySql(sql);
    XMLData data = new XMLData();
    data.put("total_count", total_count);
    data.put("row", ret);
    return ParseXML.convertObjToXml(data, "data");
  }

  /**
   * 通过传入的xml查询数据元表数据并返回结果
   * @param inXmlObj 传入的xml
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   * @throws Exception 查询失败原因
   */
  public XMLData getMD(String inXmlObj, boolean isNeedCount) throws Exception {
    String total_count = "-1";
    String sql = ParseXML.convertXmlToQuerySQL(inXmlObj);
    XMLData condition = ParseXML.convertXmlToObj(inXmlObj);
    if (isNeedCount) {
      total_count = this.getTotalCount(sql);
      String index = (String) condition.getSubObject("page_index");
      index = (index == null || index.equals("")) ? "0" : index;
      String count = (String) condition.getSubObject("page_count");
      count = (count == null || count.equals("")) ? "0" : count;
      setPageInfo(Integer.parseInt(index), Integer.parseInt(count));// 设置分页信息    	
    } else {
      this.setPageIndex(0);
    }
    List ret = queryBySql(sql);
    XMLData data = new XMLData();
    data.put("total_count", total_count);
    data.put("row", ret);
    return data;
  }

  /**
   * 根据条件查询字符串查询数据
   * @param inXmlObj 传入xml
   * @param condition 查询条件字符串
   * @param orderStr 排序条件
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   * @throws Exception 
   */
  public String getDataByCondition(String inXmlObj, String condition, String orderStr, boolean isNeedCount)
    throws Exception {
    String total_count = "-1";
    StringBuffer strSQL = new StringBuffer(ParseXML.convertXmlToQuerySQL(inXmlObj));
    if (condition != null && !condition.trim().equals("")) {
      strSQL.append(" ").append(condition).append(" ");
    }
    if (!StringUtil.isNull(orderStr)) {
      strSQL.append(" ").append(orderStr);
    }
    try {
      XMLData conData = ParseXML.convertXmlToObj(inXmlObj);
      if (isNeedCount) {
        total_count = this.getTotalCount(strSQL.toString());
        String index = (String) conData.getSubObject("page_index");
        index = (index == null || index.equals("")) ? "0" : index;
        String count = (String) conData.getSubObject("page_count");
        count = (count == null || count.equals("")) ? "0" : count;
        setPageInfo(Integer.parseInt(index), Integer.parseInt(count));// 设置分页信息   	
      } else {
        this.setPageIndex(0);
      }
    } catch (Exception e) {
      throw e;
    }
    //查询结果
    List ret = queryBySql(strSQL.toString());
    XMLData data = new XMLData();
    data.put("total_count", total_count);
    data.put("row", ret);
    return ParseXML.convertObjToXml(data, "data");
  }

  /**
   * 根据传入的字段名获得本字段枚举值
   * @param field_code 字段名
   * @return List 枚举值列表,List中的Map已模拟成要素模式chr_id,chr_code,chr_name
   */
  public List getFieldEnumValueList(String field_code) {
    List valueList = new ArrayList();
    XMLData data = this.getMetaDataByCode(field_code);
    if (data != null) {
      String fieldValueSet = (String) data.get("field_valueset");
      if (fieldValueSet != null && !fieldValueSet.equals("")) {
        StringTokenizer st = new StringTokenizer(fieldValueSet, "+");
        while (st.hasMoreTokens()) {
          String valueSet = st.nextToken();
          if (valueSet.indexOf("#") == -1) {
            continue;
          }
          Map map = new XMLData();
          String code = valueSet.substring(0, valueSet.indexOf("#"));
          String name = valueSet.substring(valueSet.indexOf("#") + 1);
          //为了模拟成要素方便前台显示
          map.put("chr_id", code);
          map.put("chr_code", code);
          map.put("chr_name", name);
          valueList.add(map);
        }
      }
    }
    return valueList;
  }

  /**
   * 根据传入的字段名获得本字段枚举值
   * @param field_code 字段名
   * @return String 枚举值字符串
   */
  public String getFieldEnumValueString(String field_code) {
    String fieldValueSet = "";
    XMLData data = this.getMetaDataByCode(field_code);
    if (data != null) {
      fieldValueSet = (String) data.get("field_valueset");
    }
    return fieldValueSet == null ? "" : fieldValueSet;
  }

  /**
   * 插入枚举信息项（累加的方式）
   * @param fieldCode 名称，对应sys_enumerate中的field_code字段
   * @param value  由Map组成，键名chr_code,chr_name分别对应表sys_enumerate中的enu_code和enu_name字段
   * @return 操作是否成功
   * @throws Exception 异常
   */
  public boolean insertFieldEnumValue(String fieldCode, List value) throws Exception {
    boolean result = true;
    if (fieldCode == null || fieldCode.equals("")) {
      throw new Exception("参数传入错误,无法删除");
    }
    fieldCode = fieldCode.toUpperCase();
    String fieldValueSet = getFieldEnumValueString(fieldCode);
    for (int i = 0; value != null && i < value.size(); i++) {
      Map tmp = (Map) value.get(i);
      if (fieldValueSet != null && !fieldValueSet.equals("")) {
        fieldValueSet += "+";
      }
      fieldValueSet += tmp.get("chr_code") + "#" + tmp.get("chr_name");
    }
    StringBuffer strSQL = new StringBuffer();
    //删除sys_enumerate已有值
    strSQL.append("delete from sys_enumerate").append(Tools.addDbLink()).append(" where field_code = '")
      .append(fieldCode).append("'");
    deleteBySql(strSQL.toString());
    //修改sys_metadata中的valueSet
    strSQL.delete(0, strSQL.length());
    strSQL.append("update sys_metadata").append(Tools.addDbLink()).append(" set field_valueset = '")
      .append(fieldValueSet).append("' where field_code = '").append(fieldCode).append("'");
    modifyBySql(strSQL.toString());
    //往sys_enumerate中插入枚举值
    insertFieldEnumValue(fieldCode, fieldValueSet);
    return result;
  }

  /**
   * 插入枚举信息项（全部替换）
   * @param fieldCode 名称，对应sys_enumerate中的field_code字段
   * @param value  由Map组成，键名chr_code,chr_name分别对应表sys_enumerate中的enu_code和enu_name字段
   * @return 操作是否成功
   * @throws Exception 异常
   */
  public boolean replaceFieldEnumValue(String fieldCode, List value) throws Exception {
    boolean result = true;
    if (fieldCode == null || fieldCode.equals("")) {
      throw new Exception("参数传入错误,无法删除");
    }
    String fieldValueSet = "";
    fieldCode = fieldCode.toUpperCase();
    for (int i = 0; value != null && i < value.size(); i++) {
      Map tmp = (Map) value.get(i);
      if (fieldValueSet != null && !fieldValueSet.equals("")) {
        fieldValueSet += "+";
      }
      fieldValueSet += tmp.get("chr_code") + "#" + tmp.get("chr_name");
    }
    StringBuffer strSQL = new StringBuffer();
    //删除sys_enumerate已有值
    strSQL.append("delete from sys_enumerate").append(Tools.addDbLink()).append(" where field_code = '")
      .append(fieldCode).append("'");
    deleteBySql(strSQL.toString());
    //修改sys_metadata中的valueSet
    strSQL.delete(0, strSQL.length());
    strSQL.append("update sys_metadata").append(Tools.addDbLink()).append(" set field_valueset = '")
      .append(fieldValueSet).append("' where field_code = '").append(fieldCode).append("'");
    modifyBySql(strSQL.toString());
    //往sys_enumerate中插入枚举值
    insertFieldEnumValue(fieldCode, fieldValueSet);
    return result;
  }

  /**
  * 删除枚举信息项
  * @param fieldCode 名称，对应sys_enumerate中的field_code字段
  * @param value  对应表sys_enumerate中的enu_code字段
  * @return 操作是否成功
  * @throws Exception 异常
  */
  public boolean deleteFieldEnumValue(String fieldCode, String value) throws Exception {
    boolean result = true;
    if (fieldCode == null || fieldCode.equals("") || value == null) {
      throw new Exception("参数传入错误,无法删除");
    }
    fieldCode = fieldCode.toUpperCase();
    StringBuffer strSQL = new StringBuffer();
    strSQL.append("delete from sys_enumerate").append(Tools.addDbLink()).append(" where field_code = '")
      .append(fieldCode).append("' and enu_code = '").append(value).append("'");
    deleteBySql(strSQL.toString());
    List list = getFieldEnumValueList(fieldCode);
    String fieldValueSet = "";
    for (int i = 0; list != null && i < list.size(); i++) {
      Map tmp = (Map) list.get(i);
      if (value.equals((String) tmp.get("chr_code"))) {
        //李文全屏蔽20080328
        //break;
      } else {
        fieldValueSet += tmp.get("chr_code") + "#" + tmp.get("chr_name");
        if (i < list.size() - 1) {
          fieldValueSet += "+";
        }
      }
    }
    //修改sys_metadata中的valueSet
    strSQL.delete(0, strSQL.length());
    strSQL.append("update sys_metadata").append(Tools.addDbLink()).append(" set field_valueset = '")
      .append(fieldValueSet).append("' where field_code = '").append(fieldCode).append("'");
    modifyBySql(strSQL.toString());
    return result;
  }

  /**
   * 删除枚举信息项
   * @param fieldCode 名称，对应sys_enumerate中的field_code字段
   * @param value  对应表sys_enumerate中的enu_code字段
   * @return 操作是否成功
   * @throws Exception 异常
   * @author lwq 2008-03-28增加
   */
  public boolean deleteFieldListValue(String fieldCode, List value, String fieldValueSet) throws Exception {
    boolean result = true;
    if (fieldCode == null || fieldCode.equals("") || value == null) {
      throw new Exception("参数传入错误,无法删除");
    }
    fieldCode = fieldCode.toUpperCase();
    StringBuffer strSQL = new StringBuffer();
    XMLData temData = null;
    Iterator temIt = value.iterator();
    if (value != null) {
      while (temIt.hasNext()) {
        temData = new XMLData();
        temData = (XMLData) temIt.next();
        String chrcode = (String) temData.get("chr_code");
        String sql = "delete from sys_enumerate where field_code = '" + fieldCode + "' and enu_code = '" + chrcode
          + "'";
        deleteBySql(sql.toString());
      }

    }

    strSQL.append("update sys_metadata").append(Tools.addDbLink()).append(" set field_valueset = '")
      .append(fieldValueSet).append("' where field_code = '").append(fieldCode).append("'");
    modifyBySql(strSQL.toString());
    return result;
  }

  /**
   * 通过传入的xml插入数据元表数据
   * @param inXmlObj 传入的xml(来自Client)
   * @return 插入对象
   * @exception 操作失败原因
   */
  public synchronized XMLData insertMetaDataByXml(String inXmlObj) throws Exception {
    XMLData data = null;
    try {
      String chr_id = UUIDRandom.generate();
      //String thisYear = this.getSetYear();
      String fieldCode = (String) ParseXML.getSubObjectOfXml(inXmlObj, "field_code");
      // 针对数据元的默认值设置
      Map defaultField = new XMLData();
      //defaultField.put("SET_YEAR", thisYear);
      defaultField.put("CHR_ID", chr_id);
      defaultField.put("CREATE_DATE", DateHandler.getLastVerTime());
      defaultField.put("CREATE_USER", getUserId());
      defaultField.put("LATEST_OP_DATE", DateHandler.getLastVerTime());
      defaultField.put("LATEST_OP_USER", getUserId());
      defaultField.put("LAST_VER", DateHandler.getLastVerTime());

      // 数据查重
      checkRepeat("sys_metadata" + Tools.addDbLink(), "field_code", fieldCode, "");
      insertBySql(ParseXML.covertXmlToInsertSQL(inXmlObj, defaultField));
      data = getMetaDataByID(chr_id);
      //将枚举值保存在
      if ("2".equals(data.get("field_disptype"))) {
        insertFieldEnumValue((String) data.get("field_code"), (String) data.get("field_valueset"));
      }
    } catch (Exception e) {
      throw e;
    }
    return data;
  }

  /**
   * 通过传入的xml修改数据元表数据
   * @param inXmlObj 传入的xml(来自Client)
   * @return 修改后的对象
   * @exception 操作失败原因
   */
  public synchronized XMLData modifyMetaDataByXml(String inXmlObj) throws Exception {
    XMLData data = null;
    try {
      XMLData xml = ParseXML.convertXmlToObj(inXmlObj);
      String chr_id = (String) xml.getSubObject("par_value"); // 被修改记录的唯一ID
      String fieldCode = (String) ParseXML.getSubObjectOfXml(inXmlObj, "field_code");
      // 针对数据元的默认值设置
      Map defaultField = new XMLData();
      defaultField.put("LATEST_OP_DATE", DateHandler.getLastVerTime());
      defaultField.put("LATEST_OP_USER", getUserId());
      defaultField.put("LAST_VER", DateHandler.getLastVerTime());

      // 数据查重
      checkRepeat("sys_metadata" + Tools.addDbLink(), "field_code", fieldCode, " and chr_id<>'" + chr_id + "'");
      modifyBySql(ParseXML.covertXmlToModifySQL(inXmlObj, defaultField));
      data = getMetaDataByID(chr_id);
      //将历史枚举值清空
      this.deleteBySql("delete from sys_enumerate" + Tools.addDbLink() + " where field_code = '"
        + data.get("field_code") + "'");
      //将枚举值保存在
      if ("2".equals(data.get("field_disptype"))) {
        String fieldValueSet = (String) data.get("field_valueset");
        if (fieldValueSet != null && !fieldValueSet.equals("")) {
          StringTokenizer st = new StringTokenizer(fieldValueSet, "+");
          while (st.hasMoreTokens()) {
            String valueSet = st.nextToken();
            XMLData enuData = new XMLData();
            enuData.put("chr_id", UUIDRandom.generate());
            enuData.put("field_code", data.get("field_code"));
            enuData.put("enu_code", valueSet.substring(0, valueSet.indexOf("#")));
            enuData.put("enu_name", valueSet.substring(valueSet.indexOf("#") + 1));
            enuData.put("create_date", DateHandler.getLastVerTime());
            enuData.put("create_user", this.getUserId());
            enuData.put("latest_op_date", DateHandler.getLastVerTime());
            enuData.put("latest_op_user", this.getUserId());
            enuData.put("is_deleted", "0");
            enuData.put("last_ver", DateHandler.getLastVerTime());
            this.insertBySql(DatabaseAccess.getInsertSql("sys_enumerate" + Tools.addDbLink(), enuData));
          }
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return data;
  }

  /**
   * 通过传入的id删除数据元表数据
  * @param id 数据元唯一ID
   * @return 操作是否成功的结果
   * @exception 操作失败原因
   */
  public boolean deleteMetaData(String id) throws Exception {
    boolean operationResult = false;
    try {
      if (id == null || id.equals("")) {
        throw new Exception("非法的数据元ID,无法执行删除!");
      }
      StringBuffer strSQL = new StringBuffer();
      strSQL.append("select * from SYS_METADATA").append(Tools.addDbLink()).append(" WHERE chr_id='").append(id)
        .append("'");
      List result = queryBySql(strSQL.toString());
      if (result.size() == 0) {
        throw new Exception("未查询到相应数据元,无法执行删除!");
      } else {
        strSQL.delete(0, strSQL.length());
        String is_system = ((Map) result.get(0)).get("is_system").toString();
        if (is_system.equalsIgnoreCase("0")) {
          strSQL.append("delete from SYS_METADATA").append(Tools.addDbLink()).append(" where chr_id='").append(id)
            .append("'");
          operationResult = deleteBySql(strSQL.toString());
        } else {
          throw new Exception("系统保留字段，不能删除");
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return operationResult;
  }

  /**
   * 根据chr_id获取指定数据元信息
   * @param chr_id 唯一码
   * @return XMLData对象
   */
  public XMLData getMetaDataByID(String chr_id) {
    XMLData data = null;
    StringBuffer sql = new StringBuffer();
    sql.append("select * from sys_metadata").append(Tools.addDbLink()).append(" where chr_id = '").append(chr_id)
      .append("'");
    List list = this.queryBySql(sql.toString());
    if (list.size() > 0) {
      data = (XMLData) list.get(0);
    }
    return data;
  }

  /**
   * 根据field_code获取指定数据元信息
   * @param field_code 字段编码
   * @return XMLData对象
   */
  public XMLData getMetaDataByCode(String field_code) {
    XMLData data = null;
    StringBuffer sql = new StringBuffer();
    sql.append("select * from sys_metadata").append(Tools.addDbLink()).append(" where field_code = upper('")
      .append(field_code).append("')");
    List list = this.queryBySql(sql.toString());
    if (list.size() > 0) {
      data = (XMLData) list.get(0);
    }
    return data;
  }

  /**
   * 根据值集字符串插入对应的枚举值
   * @param fieldCode 对应数据元字段
   * @param fieldValueSet 枚举值字符串 1#1+2#2 格式
   * @throws Exception 异常
   */
  protected void insertFieldEnumValue(String fieldCode, String fieldValueSet) throws Exception {
    //插入sys_enumerate中
    if (fieldValueSet != null && !fieldValueSet.equals("")) {
      StringTokenizer st = new StringTokenizer(fieldValueSet, "+");
      while (st.hasMoreTokens()) {
        String valueSet = st.nextToken();
        String enu_code = valueSet.substring(0, valueSet.indexOf("#"));
        String enu_name = valueSet.substring(valueSet.indexOf("#") + 1);
        //排除空行的情况
        if (enu_code != null && !enu_code.equals("")) {
          XMLData enuData = new XMLData();
          enuData.put("chr_id", UUIDRandom.generate());
          enuData.put("field_code", fieldCode);
          enuData.put("enu_code", enu_code);
          enuData.put("enu_name", enu_name);
          enuData.put("create_date", DateHandler.getLastVerTime());
          enuData.put("create_user", this.getUserId());
          enuData.put("latest_op_date", DateHandler.getLastVerTime());
          enuData.put("latest_op_user", this.getUserId());
          enuData.put("is_deleted", "0");
          enuData.put("last_ver", DateHandler.getLastVerTime());
          insertBySql(DatabaseAccess.getInsertSql("sys_enumerate" + Tools.addDbLink(), enuData));
        }
      }
    }
  }
}
