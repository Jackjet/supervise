package gov.df.fap.service.util.datasource;

import java.sql.SQLException;

public class SQLUtil {

  /**
   * || 替换成 concat
   * t.menu_code||' '||t.menu_name 替换成  concat(t.menu_code,' ',t.menu_name)
   * @param s
   * @return s
   */
  public static String replaceLinkChar(String s) {
    if (TypeOfDB.isMySQL()) {
      s = "concat(" + s.replaceAll("\\|\\|", ",") + ")";
    }
    return s;
  }

  /**
   * 当前时间的语法区别
   * @return
   */
  public static String getSysdateToCharSql() {
    if (TypeOfDB.isMySQL()) {
      return "date_format(sysdate(),'%Y-%m-%d %H:%i:%s')";
    }
    return "to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')";
  }

  /**
   * 一些列名是mysql的关键字，需要处理，如key字段，是mysql的关键字
   * @param s
   * @return
   */
  public static String replaceKeyChar(String s) {
    if (TypeOfDB.isMySQL()) {
      return "`" + s + "`";
    }
    return s;
  }

  /**
   * 替换nvl函数
   * @param s
   * @return
   */
  public static String replaceNvl(String s) {
    if (TypeOfDB.isMySQL()) {
      return s.replaceAll("nvl", "ifnull");
    }
    return s;
  }

  /**
   * 返回不同的序列
   * @param s
   * @return
   */
  public static String getSeqExpr(String s) {
    if (TypeOfDB.isMySQL()) {
      return "NEXTVAL('" + s + "')";
    }
    return s + ".NEXTVAL";
  }

  /**生成mysql数据库的分页语句
   * 参数 sSQL为原始SQL语句，currPageIndex为当前第几页数,pageRowsCount为每页记录数据
   * 
   * @return SQL语句
   * @throws SQLException
   */
  public static String getMySQLPageRow(String sSQL, int currPageIndex, int pageRowsCount) throws SQLException {

		return  sSQL + " limit " + (currPageIndex - 1)* pageRowsCount + ", " + pageRowsCount;

  }

}
