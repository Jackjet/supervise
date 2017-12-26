package gov.df.fap.service.util;

import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 数据操作辅助类,获取相关的sql信息
 * 
 * @version 1.0
 * @author 
 * @since java 1.6
 */
public class DatabaseAccess {

  /**
   * 构造函数
   */
  public DatabaseAccess() {
  }

  /**
   * 自动生成修改数据库的语句 set field = value格式
   * 
   * @param tableName
   *            要修改的表名
   * @param fieldMap
   *            要修改的字段值对Map
   * @param primaryMap
   *            主键的值对Map
   * @return 返回修改操作的SQL语句
   * @throws java.lang.Exception
   */
  public static String getUpdateSql(String tableName, Map fieldMap, Map primaryMap) throws Exception {
    Map tableMap = getFieldMap(tableName);
    StringBuffer updateSql = new StringBuffer();
    updateSql.append("update ");
    updateSql.append(tableName);
    updateSql.append(" set ");
    String fieldName = null;
    for (Iterator iter = fieldMap.keySet().iterator(); iter.hasNext();) {
      fieldName = null;
      fieldName = iter.next().toString().toLowerCase(); // 转换为小写
      // if(fieldMap.get(fieldName) == null
      // ||fieldMap.get(fieldName).toString().equalsIgnoreCase("")
      // ||!tableMap.containsKey(fieldName))continue;
      if (!tableMap.containsKey(fieldName))
        continue; // 传递进来的字段在表结构里面没有的时候才不拼凑sql,如果传递进来的值为空,更新为''
      updateSql.append(fieldName);
      updateSql.append("='");
      if (fieldMap.get(fieldName) != null)//有值的情况下才需要拼装值
        updateSql.append(fieldMap.get(fieldName).toString());
      updateSql.append("',");
    }

    updateSql.deleteCharAt(updateSql.length() - 1);
    updateSql.append(" where ");
    String fieldPrimary = null;
    for (Iterator iter = primaryMap.keySet().iterator(); iter.hasNext();) {
      fieldPrimary = null;
      fieldPrimary = (String) iter.next();
      if (!tableMap.containsKey(fieldPrimary))
        continue;
      updateSql.append(fieldPrimary);
      updateSql.append("='");
      if (primaryMap.get(fieldPrimary) != null)//有值的情况下才需要拼装值
        updateSql.append(primaryMap.get(fieldPrimary).toString());
      updateSql.append("'");
      updateSql.append(" and ");
    }
    updateSql.delete(updateSql.length() - 4, updateSql.length() - 1);
    return updateSql.toString();
  }

  /**
   * 自动生成修改数据库的语句 set field = value格式
   * 
   * @param tableName
   *            要修改的表名
   * @param fieldMap
   *            要修改的字段值对Map
   * @param primaryMap
   *            主键的值对Map
   * @param isModifyNull
   *            是否更新为空的字段
   * @return 返回修改操作的SQL语句
   * @throws java.lang.Exception
   */
  public static String getUpdateSql(String tableName, Map fieldMap, Map primaryMap, boolean isModifyNull)
    throws Exception {
    if (!isModifyNull) {
      return getUpdateSql(tableName, fieldMap, primaryMap);
    } else {
      StringBuffer updateSql = new StringBuffer();
      updateSql.append("update ");
      updateSql.append(tableName);
      updateSql.append(" set ");
      String fieldName = null;
      for (Iterator iter = fieldMap.keySet().iterator(); iter.hasNext();) {
        fieldName = (String) iter.next();
        if (fieldName == null || fieldName.equals(""))
          continue;
        String fieldValue = fieldMap.get(fieldName) == null ? null : fieldMap.get(fieldName).toString();
        if (fieldValue != null && !fieldValue.equals("")) {
          updateSql.append(fieldName).append("='").append(fieldValue).append("',");
        } else {
          updateSql.append(fieldName).append("=null,");
        }
      }

      updateSql.deleteCharAt(updateSql.length() - 1);
      updateSql.append(" where ");
      String fieldPrimary = null;
      for (Iterator iter = primaryMap.keySet().iterator(); iter.hasNext();) {
        fieldPrimary = (String) iter.next();
        if (fieldPrimary == null || fieldPrimary.equals(""))
          continue;
        String primaryValue = primaryMap.get(fieldPrimary) != null ? primaryMap.get(fieldPrimary).toString()
          : null;
        if (primaryValue != null && !primaryValue.equals("")) {
          updateSql.append(fieldPrimary).append("='").append(primaryValue).append("' and ");
        } else {
          updateSql.append(fieldPrimary).append("=null and ");
        }
      }
      updateSql.delete(updateSql.length() - 4, updateSql.length() - 1);
      return updateSql.toString();
    }
  }

  /**
   * 自动生成修改数据库的语句 set field = field +(-) value格式
   * 
   * @param tableName
   *            要修改的表名
   * @param fieldMap
   *            要修改的字段值对Map
   * @param pirmaryMap
   *            主键的值对Map
   * @param operation
   *            操作类型（＋，-)
   * @return 返回修改操作的SQL语句
   * @throws java.lang.Exception
   */
  public static String getUpdateSql(String tableName, Map fieldMap, Map pirmaryMap, String operation)
    throws Exception {
    Map tableMap = getFieldMap(tableName);
    StringBuffer updateSql = new StringBuffer();
    updateSql.append("update ");
    updateSql.append(tableName);
    updateSql.append(" set ");
    String fieldName = null;
    for (Iterator iter = fieldMap.keySet().iterator(); iter.hasNext();) {
      fieldName = null;
      fieldName = (String) iter.next();
      if (!tableMap.containsKey(fieldName))
        continue;
      updateSql.append(fieldName);
      updateSql.append("=");
      updateSql.append(fieldName);
      updateSql.append(operation);
      updateSql.append("'");
      if (fieldMap.get(fieldName) != null)//有值的情况下才需要拼装值
        updateSql.append(fieldMap.get(fieldName).toString());
      updateSql.append("',");
    }

    updateSql.deleteCharAt(updateSql.length() - 1);
    updateSql.append(" where ");
    String fieldPrimary = null;
    for (Iterator iter = pirmaryMap.keySet().iterator(); iter.hasNext();) {
      fieldPrimary = null;
      fieldPrimary = (String) iter.next();
      if (!tableMap.containsKey(fieldPrimary))
        continue;
      updateSql.append(fieldPrimary);
      updateSql.append("='");
      if (pirmaryMap.get(fieldPrimary) != null)//有值的情况下才需要拼装值
        updateSql.append(pirmaryMap.get(fieldPrimary).toString());
      updateSql.append("'");
      updateSql.append(" and ");
    }
    updateSql.delete(updateSql.length() - 4, updateSql.length() - 1);
    return updateSql.toString();
  }

  /**
   * 自动生成修改数据库的语句 set field = field +(-) value(除了主键条件还有其他条件）格式
   * 
   * @param tableName
   *            要修改的表名
   * @param fieldMap
   *            要修改的字段值对Map
   * @param pirmaryMap
   *            主键的值对Map
   * @param operation
   *            操作类型（＋，-)
   * @param strWhere
   *            其他条件
   * @return 返回修改操作的SQL语句
   * @throws java.lang.Exception
   */
  public static String getUpdateSql(String tableName, Map fieldMap, Map pirmaryMap, String operation,
    String strWhere) throws Exception {
    Map tableMap = getFieldMap(tableName);
    StringBuffer updateSql = new StringBuffer();
    updateSql.append("update ");
    updateSql.append(tableName);
    updateSql.append(" set ");
    String fieldName = null;
    for (Iterator iter = fieldMap.keySet().iterator(); iter.hasNext();) {
      fieldName = null;
      fieldName = (String) iter.next();
      if (!tableMap.containsKey(fieldName))
        continue;
      updateSql.append(fieldName);
      updateSql.append("=");
      updateSql.append(fieldName);
      updateSql.append(operation);
      updateSql.append("'");
      if (fieldMap.get(fieldName) != null)//有值的情况下才需要拼装值
        updateSql.append(fieldMap.get(fieldName).toString());
      updateSql.append("',");
    }

    updateSql.deleteCharAt(updateSql.length() - 1);
    updateSql.append(" where ");
    String fieldPrimary = null;
    for (Iterator iter = pirmaryMap.keySet().iterator(); iter.hasNext();) {
      fieldPrimary = null;
      fieldPrimary = (String) iter.next();
      if (!tableMap.containsKey(fieldPrimary))
        continue;
      updateSql.append(fieldPrimary);
      updateSql.append("='");
      if (pirmaryMap.get(fieldPrimary) != null)//有值的情况下才需要拼装值
        updateSql.append(pirmaryMap.get(fieldPrimary).toString());
      updateSql.append("'");
      updateSql.append(" and ");
    }
    updateSql.append(strWhere);
    return updateSql.toString();
  }

  /**
   * 自动生成插入数据库的语句
   * 
   * @param tableName
   *            要插入的表名
   * @param formMap
   *            通过form形成的Map
   * @param conn
   *            数据库的连接
   * @return 返回插入操作的SQL语句
   * @throws java.lang.Exception
   */
  public static String getInsertSql(String tableName, Map formMap) throws Exception {
    return getInsetSql(tableName, getFieldMap(tableName), formMap);
  }

  /**
   * 根据表名获取表的字段
   * @param tableName
   *            表名
   * @return Map 字段Map集合
   * @throws Exception
   */
  public static Map getFieldMap(String tableName) throws Exception {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    org.hibernate.Session session = null;
    Map fieldsMap = null;
    GeneralDAO dao = (GeneralDAO) SessionUtil.getServerBean("generalDAO");
    session = dao.getSession();
    StringBuffer insertSql = new StringBuffer();
    insertSql.append("select * from ").append(tableName).append(" where 1<>1");
    try {
      conn = session.connection();
      ps = conn.prepareStatement(insertSql.toString());
      rs = ps.executeQuery();
      fieldsMap = getFieldName(rs);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("定位" + tableName + "表字段出错！");
    } finally {
      if (rs != null) {
        rs.close();
        rs = null;
      }
      if (ps != null) {
        ps.close();
        ps = null;
      }
      if (session != null) {
        dao.closeSession(session);
      }
    }
    return fieldsMap;
  }

  /**
   * 根据rs得到对应表的字段集
   * 
   * @param rs
   *            查询数据库得到的结果集
   * @return 根据rs得到对应表的字段集
   * @throws SQLException
   */
  private static Map getFieldName(ResultSet rs) throws SQLException {
    ResultSetMetaData metaData = rs.getMetaData();
    HashMap fieldName = new HashMap();
    for (int i = 1, n = metaData.getColumnCount(); i <= n; i++) {
      fieldName.put(java2sqlName(metaData.getColumnName(i)), "");
    }
    return fieldName;
  }

  /**
   * 转换字段名字为小写
   * 
   * @param name
   *            字段名字
   * @return 小写字段名
   */
  private static String java2sqlName(String name) {
    return name == null ? "" : name.toLowerCase(); // 提高性能
  }

  /**
   * 得到插入的sql语句
   * 
   * @param tableName
   *            表名
   * @param fieldMap
   *            表字段的集合
   * @param formMap
   *            由form得到的集合
   * @return 插入的sql语句
   */
  public static String getInsetSql(String tableName, Map fieldMap, Map formMap) {
    StringBuffer strResult = new StringBuffer();
    strResult.append("insert into ");
    strResult.append(tableName);
    strResult.append(" (");
    for (Iterator iter = fieldMap.keySet().iterator(); iter.hasNext();) {
      String temp = (String) iter.next();
      if (formMap.containsKey(temp)) {
        strResult.append(temp);
        strResult.append(",");
      }
    }
    strResult.deleteCharAt(strResult.length() - 1);
    strResult.append(")");

    Object[] field = fieldMap.keySet().toArray();
    strResult.append(" values(");
    for (int i = 0, n = field.length; i < n; i++) {
      if (formMap.containsKey(field[i])) {
        if (formMap.get(field[i]) != null && !(formMap.get(field[i]).toString().trim().equals(""))) {
          strResult.append("'");
          strResult.append(formMap.get(field[i]).toString());
          strResult.append("',");
        } else {
          strResult.append("null,");
        }
      }
    }
    strResult.deleteCharAt(strResult.length() - 1);
    strResult.append(")");

    return strResult.toString();
  }

  /**
   * 根据Map存贮的信息组合成用逗号分隔的字段名
   * 
   * @param table_name
   *            表名
   * @return
   */
  public static String getFieldString(String table_name) {
    String fieldName = "";
    try {
      Map map = DatabaseAccess.getFieldMap(table_name);
      Object[] obj = map.keySet().toArray();
      Arrays.sort(obj);
      for (int i = 0; i < obj.length; i++) {
        fieldName += obj[i].toString();
        if (i != obj.length - 1) {
          fieldName += ",";
        }
      }
    } catch (Exception e) {

    }
    return fieldName;
  }

  /**
   * 通过结果集得到属性Map对象
   * 
   * @param rs
   *            结果集
   * @return 属性XMLData对象
   * @throws SQLException
   */
  public static XMLData getProperties(ResultSet rs) throws Exception {
    ResultSetMetaData metaData = rs.getMetaData();
    int ncolumns = metaData.getColumnCount();
    XMLData properties = new XMLData();
    for (int i = 1; i <= ncolumns; i++) {
      properties.put(metaData.getColumnName(i).toLowerCase(),
        (rs.getString(i) == null) ? "" : rs.getString(i));
    }
    return properties;
  }

  /**
   * 把数据集转换成对象
   * 
   * @param bean
   *            object对象
   * @param rs
   *            结果集
   * @throws SQLException
   */
  public static void populate(Object bean, Map properties) throws Exception {
    try {
      BeanUtils.populate(bean, properties);
    } catch (Exception e) {
      throw new Exception("转换Map对象到bean对象过程中发生错误");
    }
  }
}