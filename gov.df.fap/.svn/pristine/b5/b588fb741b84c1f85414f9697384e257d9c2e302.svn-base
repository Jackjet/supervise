package gov.df.fap.service.dictionary;

import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 要素数据权限接口
 * 
 * @version 1.0
 * @author justin
 * @since java 1.6
 */
@Component
public class DictionaryRight {
  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  /**
   * 实现返回基础要素表的数据权限sql语句
   * 
   * @param userid-------------用户id
   * @param roleid-------------角色id
   * @param elemcode-----------要素简称
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlElemRight(String userid, String roleid, String elemcode, String tablealias) throws Exception {
    return getSqlElemRightByUser(userid, elemcode, tablealias)
      + getSqlElemRightByRole(userid, roleid, elemcode, tablealias);
  }

  /**
   * 实现返回基础要素表的数据权限sql语句（通过角色）
   * 
   * @param roleid-------------角色id
   * @param elemcode-----------要素简称
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlElemRightByRole(String userId, String roleid, String elemcode, String tablealias)
    throws Exception {
    StringBuffer strSQL = new StringBuffer();
    //modify by 张君阳 on 2011-03-08
    //增加查出rule_id字段，以便可以查出追加范围并去除排除范围
    strSQL
      .append(
        "select c.right_group_id, b.rule_id from Sys_user_Role_rule a, Sys_Right_Group b, Sys_Right_Group_Type c ")
      .append(" where a.rule_id = b.rule_id and b.right_group_id = c.right_group_id and a.ROLE_ID = ?")
      .append(" and upper(c.ELE_CODE) = ? and c.right_type = 1 and a.user_id = ? and b.right_group_describe <> '003'");
    if ((roleid == null) || (tablealias == null) || (elemcode == null)) {
      return " and 1=0";
    }
    List lstDataType = null;
    lstDataType = dao.findBySql(strSQL.toString(), new Object[] { roleid, elemcode.toUpperCase(), userId });
    if (lstDataType == null)
      return " and 1=1";
    Iterator it = lstDataType.iterator();
    if (it.hasNext()) {
      //String temSql = "";
      XMLData map = new XMLData();
      map = (XMLData) it.next();
      String right_group_id = (String) map.get("right_group_id");
      //			modify by 张君阳 on 2011-03-08
      //增加追加范围并去除排除范围
      //			temSql = new StringBuffer(
      //					" and exists (select 1 from sys_right_group_detail")
      //					.append(" e where e.right_group_id='").append(
      //							right_group_id).append("' and e.ELE_CODE='")
      //					.append(elemcode).append("' and  e.ELE_VALUE=").append(
      //							tablealias).append(".CHR_CODE)").toString();

      //主范围部分
      StringBuffer temSBSql = new StringBuffer(" and ((exists (select 1 from sys_right_group_detail")
        .append(" e where e.right_group_id='").append(right_group_id).append("' and e.ELE_CODE='").append(elemcode)
        .append("' and  e.ELE_VALUE=").append(tablealias).append(".CHR_CODE)");
      //追加范围部分
      temSBSql.append(" or exists (select 1 from sys_rule_cross_join_add cross_add where cross_add.rule_id = ")
        .append(map.get("rule_id").toString()).append(" and cross_add.").append(elemcode.toUpperCase())
        .append("_ID = ").append(tablealias).append(".chr_id ))");
      //去除排除部分
      temSBSql.append(" and not exists (select 1 from sys_rule_cross_join_del cross_del where cross_del.rule_id = ")
        .append(map.get("rule_id").toString()).append(" and cross_del.").append(elemcode.toUpperCase())
        .append("_ID = ").append(tablealias).append(".chr_id )");
      temSBSql.append(")");
      return temSBSql.toString();//temSql;

    } else {
      return " and 1=1";
    }
  }

  /**
   * 实现返回基础要素表的数据权限sql语句（通过用户）
   * 
   * @param userid-------------用户id
   * @param elemcode-----------要素简称
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlElemRightByUser(String userid, String elemcode, String tablealias) throws Exception {
    if ((userid != null) && !userid.toString().equals("") && (tablealias != null) && !tablealias.toString().equals("")) {
      // 查询该用户是否有机构类型,机构权限已经缓存
      String curUserId = SessionUtil.getUserInfoContext().getUserID();
      String orgType = SessionUtil.getUserInfoContext().getOrgType();
      if (userid.equalsIgnoreCase(curUserId)// 传入的用户等于当前用户
        && ("001".equals(orgType))) {
        return " and 1=1";
      } else {
        StringBuffer strSQL = new StringBuffer("select b.* from Sys_User").append(Tools.addDbLink())
          .append(" a,sys_orgtype").append(Tools.addDbLink()).append(" b where a.USER_ID=?")
          .append(" and a.org_type=b.orgtype_code");
        List lstDataType = dao.findBySql(strSQL.toString(), new Object[] { userid });
        String tmp_ele_code = "";
        if (lstDataType != null && lstDataType.size() > 0) {
          XMLData m = new XMLData();
          m = (XMLData) lstDataType.get(0);
          tmp_ele_code = (String) m.get("ele_code");

          // 如果该用户的机构类型要素不是传入的要素，则不受机构类型控制
          if (tmp_ele_code == null || tmp_ele_code.equals("") || !tmp_ele_code.equals(elemcode)) {
            return " and 1=1";
          } else {
            return new StringBuffer(" and exists(select 1 from sys_user_org").append(Tools.addDbLink())
              .append(" org where").append(" org.user_id = '").append(userid).append("' and org.org_id = ")
              .append(tablealias).append(".chr_id and set_year ='" + SessionUtil.getLoginYear() + "')").toString();
          }
        } else {
          // 如果找不到该用户的机构信息，则返回
          return " and 1=1";
        }
      }
    }
    return " and 1=0 ";
  }

  public GeneralDAO getDao(GeneralDAO dao) {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 返回通过用户的预算单位机构权限过滤基础数据的sql语句
   * 
   * @param userid-------------用户id
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getEnOrgRightSqlByUser(String userid, String tablealias) {
    if ((userid != null) && !userid.toString().equals("") && (tablealias != null) && !tablealias.toString().equals("")) {
      // 查询该用户是否有机构类型,机构权限已经缓存
      String curUserId = SessionUtil.getUserInfoContext().getUserID();
      String orgType = SessionUtil.getUserInfoContext().getOrgType();
      if (userid.equalsIgnoreCase(curUserId)// 传入的用户等于当前用户
        && ("001".equals(orgType))) {
        return " and 1=1";
      } else {
        StringBuffer strSQL = new StringBuffer("select b.* from Sys_User a,sys_orgtype b ").append(
          " where a.USER_ID = ?").append(" and a.org_type = b.orgtype_code");
        List lstDataType = dao.findBySql(strSQL.toString(), new Object[] { userid });
        String tmp_ele_code = "";
        if (lstDataType != null && lstDataType.size() > 0) {
          XMLData m = new XMLData();
          m = (XMLData) lstDataType.get(0);
          tmp_ele_code = (String) m.get("ele_code");

          // 如果该用户的机构类型要素不是传入的要素，则不受机构类型控制
          if (tmp_ele_code == null || tmp_ele_code.equals("") || !tmp_ele_code.equalsIgnoreCase("EN")) {
            return " and 1=1";
          } else {
            return new StringBuffer(" and exists(select 1 from sys_user_org org ").append(" 	  where org.user_id = '")
              .append(userid).append("'      and org.org_id = ").append("alias_").append(tablealias)
              .append(".en_id and set_year ='" + SessionUtil.getLoginYear() + "')").toString();
          }
        } else {
          // 如果找不到该用户的机构信息，则返回
          return " and 1=1";
        }
      }
    }
    return " and 1=0 ";
  }

}
