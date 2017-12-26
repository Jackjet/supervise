package gov.df.fap.service.dictionary;

import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 数据库底层操作类,专门用来针对数据库增、删、查、改操作
 * @version 1.0
 * @author justin
 * @since java 1.6
 */
@Component
public class DBOperation {
  @Autowired
  @Qualifier("generalDAO")
  protected GeneralDAO dao = null;

  private int page_index = 0;

  private int page_count = 0;

  /**
   * 默认构造函数
   */
  public DBOperation() {

  }

  public int getPageIndex() {
    return this.page_index;
  }

  public void setPageIndex(int page_index) {
    this.page_index = page_index;
  }

  public int getPageCount() {
    return this.page_count == 0 ? Integer.parseInt(SessionUtil.getUserInfoContext().getPageSize()) : page_count;
  }

  public void setPageCount(int page_count) {
    this.page_count = page_count;
  }

  public String getUserId() {
    return (String) SessionUtil.getUserInfoContext().getUserID();
  }

  public String getRoleId() {
    return (String) SessionUtil.getUserInfoContext().getRoleID();
  }

  public String getSetYear() {
    String set_year = SessionUtil.getLoginYear();
    if (set_year == null || set_year.equalsIgnoreCase("")) {
      set_year = String.valueOf(DateHandler.getCurrentYear());
    }
    return set_year;
  }

  /**
   * 查询模版,根据传入的sql执行查询并返回查询结果
   * @param sql sql语句
   * @return 查询结果xml字符串
   */
  public List queryBySql(String sql) {
    List data = new ArrayList();
    if (page_index >= 1) {
      String strTest = sql;
      boolean flag = false;
      if (strTest.indexOf("from") != -1) {
        strTest = strTest.substring(strTest.indexOf("from") + 4);
        if (strTest.indexOf("from") != -1)
          flag = true;
      }
      if (flag == true) {
        if (sql.indexOf("from") != -1)
          sql = sql.substring(0, sql.indexOf("from")) + "into #temp " + sql.substring(sql.indexOf("from"));
        sql = sql + " select data.* from #temp data";
        List res = dao.findBySql(sql);
        for (int i = (page_index - 1) * getPageCount(); i <= (page_index) * getPageCount(); i++) {
          data.add(res.get(i));
        }
      } else
        data = dao.findBySql(sql);
    } else
      data = dao.findBySql(sql);

    //将page_index,page_count还原,方便下一次调用
    //  page_index = 1;
    // page_count = Integer.parseInt(SessionUtil.getUserInfoContext().getPageSize());
    return data;
  }

  /**
   * 删除模版,根据传入的sql执行删除操作并返回操作结果
   * @param sql sql语句
   * @return 是否成功
   */
  public boolean deleteBySql(String sql) throws Exception {
    if (dao.executeBySql(sql) > 0)
      return true;
    else
      return false;
  }

  /**
   * 修改模版,根据传入的sql执行修改操作并返回操作结果
   * @param sql sql语句
   * @return 是否成功
   */
  public boolean modifyBySql(String sql) throws Exception {
    if (dao.executeBySql(sql) > 0)
      return true;
    else
      return false;
  }

  /**
   * 修改模版,根据传入的sql执行修改操作并返回操作结果
   * @author zhangsheng 添加变量绑定方法
   * @param sql sql语句
   * @return 是否成功
   */
  public boolean modifyBySql(String sql, Object[] params) throws Exception {
    if (dao.executeBySql(sql, params) > 0)
      return true;
    else
      return false;
  }

  /**
   * 插入模版,根据传入的sql执行插入操作并返回操作结果
   * @param sql sql语句
   * @return 是否成功
   */
  public boolean insertBySql(String sql) throws Exception {
    if (dao.executeBySql(sql) > 0)
      return true;
    else
      return false;
  }

  /**
  * 设置分页当前页、分页页大小信息
  * @param pageIndex 当前页
  * @param pageCount 分页大小
  */
  public void setPageInfo(int pageIndex, int pageCount) {
    this.setPageIndex(pageIndex);
    this.setPageCount(pageCount);
  }

  /**
     * 查询结果总记录数
     * @param sql 查询SQL语句
     * @return 记录总数
     */
  public String getTotalCount(String sql) {
    String s = sybaseGetTotalCount(sql);
    List list = dao.findBySql(s);
    if (list.size() > 0) {
      XMLData data = (XMLData) list.get(0);
      return (String) data.get("total") == null ? "0" : (String) data.get("total");
    } else
      return "0";

  }

  /**
   * Sybase查询结果总记录数,Sybase中不支持在传入的sql语句的最完成包上
   * select count(1) from ....的语法，所以将传入的sql的子查询的结果
   * 插入临时表中，然后在从该临时表取数的方式来去掉子查询。
   * 如 select * from (select * from a) t1, (select* from b) t2,
   * 	   c t3 where t1.chr_id = t2.en_id and t2.user_id = t3.user_id
   * 转化为如下的sybase查询语句
   * select * into #temp1 from b
   * select * into #temp2 from a
   * select * into #temp3 from b #temp2 t1, #temp1 t2, c t3 
   * where t1.chr_id = t2.en_id and t2.user_id = t3.user_id
   * select count(1) from #temp3
   * @param oSql 查询SQL语句
   * @return 转化后的sql语句
   * @author bigdog 080623
   */
  private String sybaseGetTotalCount(String oSql) {
    StringBuffer sb = new StringBuffer(oSql);
    StringBuffer resultSb = new StringBuffer();
    String sql = sb.toString();
    List sqlList = new ArrayList();
    String tempName = "", tempStr, tempStr2;
    int fromIndex, selectIndex, leftBracketIndex, rightBracketIndex, n = 1;
    int nLeftBracketIndex = 0, nTempIndex = 0;
    boolean hasLeftBracketIndex = true;
    do {
      hasLeftBracketIndex = true;
      tempName = "#temp" + String.valueOf(n++);
      fromIndex = sql.lastIndexOf("from");
      if (fromIndex < 0)
        break;
      tempStr = sql.substring(0, fromIndex);
      selectIndex = tempStr.lastIndexOf("select");
      if (selectIndex < 0)
        break;
      tempStr = tempStr.substring(0, selectIndex);
      leftBracketIndex = tempStr.lastIndexOf("(");
      if (leftBracketIndex < 0) {
        hasLeftBracketIndex = false;
        leftBracketIndex = 0;
      }

      if (hasLeftBracketIndex) {
        rightBracketIndex = sql.lastIndexOf(")");
        if (rightBracketIndex < 0)
          rightBracketIndex = sql.length();
        tempStr2 = sql.substring(fromIndex, rightBracketIndex);
        nTempIndex = 0;
        nLeftBracketIndex = 0;
        while ((nTempIndex = tempStr2.indexOf(nTempIndex, '(')) != -1) {
          nLeftBracketIndex++;
        }
        nTempIndex = 0;

        rightBracketIndex = fromIndex;
        for (int i = 0; i <= nLeftBracketIndex; ++i) {
          rightBracketIndex = sql.indexOf(")", rightBracketIndex);
          if (rightBracketIndex < 0)
            rightBracketIndex = sql.length();
        }
      } else
        rightBracketIndex = sql.length();

      String sqlStr = sql.substring(leftBracketIndex == 0 ? leftBracketIndex : leftBracketIndex + 1, rightBracketIndex);
      sql = sql.substring(0, leftBracketIndex) + " " + tempName + " "
        + sql.substring((rightBracketIndex + 1) > sql.length() ? sql.length() : rightBracketIndex + 1);
      sqlStr = sqlStr.substring(0, sqlStr.lastIndexOf("from")) + " into " + tempName + " "
        + sqlStr.substring(sqlStr.lastIndexOf("from"));
      sqlList.add(sqlStr);
    } while (leftBracketIndex != 0);
    sqlList.add("select count(1) total from " + "#temp" + String.valueOf(--n));

    for (int i = 0; i < sqlList.size(); ++i) {
      resultSb.append(sqlList.get(i) + "\n");
    }
    return resultSb.toString();
  }

  /**
  * 插入、修改时检查数据正确性
  * @param tableName 表名
  * @param field 需要检查的字段名
  * @param value 需要检查的字段值
  * @param whereStr 过滤条件字符
  * @throws Exception 违反数据唯一性异常
  */
  public void checkRepeat(String tableName, String field, String value, String whereStr) throws Exception {
    //由于特殊要求,可能在一个年度中维护不同年度的数据,而不同年度的数据编码允许相同,所以
    //年度条件均由外部传入,不再默认为当前业务年度
    StringBuffer strSQL = new StringBuffer();
    strSQL.append("select 1 from ").append(tableName).append(" where ").append(field).append("='").append(value)
      .append("' ").append(whereStr);
    List list = dao.findBySql(strSQL.toString());
    if (list.size() > 0) {
      List list_field_name = null;
      list_field_name = dao.findBySql("select isnull(field_name,'" + field + "') from sys_metadata" + Tools.addDbLink()
        + " where field_code=upper('" + field + "')");
      if (list_field_name.size() > 0) {
        String field_name = ((XMLData) list_field_name.get(0)).get("field_name").toString();
        throw new Exception("违反数据唯一性约束，请修改" + field_name + "字段值");
      }
      throw new Exception("违反数据唯一性约束，请修改" + field + "字段值");
    }
  }

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }
}
