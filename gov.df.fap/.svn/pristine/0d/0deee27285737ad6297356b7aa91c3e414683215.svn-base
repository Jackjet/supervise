package gov.df.fap.service.rule;

import gov.df.fap.api.rule.ISysRight;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 权限管理服务端接口实现类
 * 
 * @author zhadaopeng
 * 
 */
@Service
public class SysRightImplBO implements ISysRight {

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  /**
   * 得到所有权限组数据
   * 
   * @return
   */
  public List getAllSysRightGroup() {
    try {
      //      String sql = "select sr.*,sr.rule_code as chr_code," + "sr.rule_name as chr_name,sr.rule_id as chr_id "
      //        + " from sys_rule sr where sr.set_year=? and sr.rg_code=? and  sr.rule_name "
      //        + "is not null and (sr.rule_classify = '006' or sr.rule_classify = "
      //        + "'001' or sr.rule_classify is null) order by sr.rule_code";
      //      List rs_list = dao.findBySql(sql, new String[] { getSetYear(), getRgCode() });
      //4-7修改 yanyga
      String sql = null;
      if (TypeOfDB.isOracle()) {
        String tempSql = "with sys_rule_tmp as (select * from sys_rule where set_year = ? "
          + "  and rg_code = ?) select sr.rule_id,sr.rule_code ||' '|| sr.rule_name as name,( case"
          + " when length(rule_code)=3 then  ''  when length(rule_code)>3 and length(rule_code)<=6 "
          + " then  (select distinct to_char(rule_id) from sys_rule_tmp where rule_code=substr(sr.rule_code,0,3) )"
          + "  when length(rule_code)>6 and length(rule_code)<=9 "
          + "  then  (select distinct to_char(rule_id) from sys_rule_tmp where rule_code=substr(sr.rule_code,0,6) )"
          + " ELSE NULL END)as pid from (";
        sql = tempSql + "select sr.*,sr.rule_code as chr_code, sr.rule_name as chr_name,sr.rule_id as chr_id "
          + " from sys_rule_tmp sr where  sr.rule_name "
          + "is not null and (sr.rule_classify = '006' or sr.rule_classify = "
          + "'001' or sr.rule_classify is null))sr order by sr.rule_code";
      } else {
        sql = " select sr.rule_id,concat(sr.rule_code , ' ' , sr.rule_name) as name,"
          + " (select min((case when length(sr.rule_code) between 4 and 9 then  cast(rule_id as char) else '' end)) "
          + " from sys_rule  where set_year = sr.set_year  and rg_code = sr.rg_code and  "
          + " rule_code = (case when length(sr.rule_code) between 4 and 6 then substr(sr.rule_code, 1, 3) "
          + " when length(sr.rule_code) between 7 and 9 then substr(sr.rule_code, 1, 6)  else concat(rule_code , '*#') "
          + " end) ) as pid from (select sr.*,sr.rule_code as chr_code, sr.rule_name as chr_name,sr.rule_id as chr_id "
          + " from sys_rule sr where set_year = ? and rg_code = ? and sr.rule_name "
          + "is not null and (sr.rule_classify = '006' or sr.rule_classify = "
          + "'001' or sr.rule_classify is null))sr order by sr.rule_code";
      }
      List rs_list = dao.findBySql(sql, new String[] { getSetYear(), getRgCode() });

      return rs_list;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  public String getRgCode() {
    String rg_code = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");
    return rg_code;
  }

  public String getSetYear() {
    String set_year = (String) SessionUtil.getUserInfoContext().getAttribute("set_year");

    return set_year;
  }

  /**
   * 删除权限组数据
   * 
   * @param
   */
  public String deleteSysRight(String roleId, String rightGroupId, String right_type) {
    String delete_sql = "";
    String update_sql = "";
    update_sql = "update sys_role  b set b.right_type=? where b.role_id =? ";
    dao.executeBySql(update_sql, new String[] { right_type, roleId });

    delete_sql = "delete from  sys_role_right_group  b where b.role_id =? and b.right_group_id=? ";
    dao.executeBySql(delete_sql, new String[] { roleId, rightGroupId });

    delete_sql = "delete from  sys_right_group  b where b.right_group_id=? and b.set_year= ? and b.rg_code=? ";

    dao.executeBySql(delete_sql, new String[] { rightGroupId, getSetYear(), getRgCode() });

    delete_sql = "delete from  sys_right_group_type  b where b.right_group_id=? and b.set_year= ? and b.rg_code=? ";

    dao.executeBySql(delete_sql, new String[] { rightGroupId, getSetYear(), getRgCode() });

    delete_sql = "delete from  sys_right_group_detail  b where b.right_group_id=? and b.set_year= ? and b.rg_code=? ";

    dao.executeBySql(delete_sql, new String[] { rightGroupId, getSetYear(), getRgCode() });
    return "OK";
  }

  /**
   * 得到所有启用的基础要素
   * 
   * @return
   */
  public List getEnabledEle(String setYear) {
    String sql = "select t.ele_source,t.ele_code as ele_code,t.ele_name from sys_element t where t.is_rightfilter=1 and t.set_year=? ";
    List listData = dao.findBySql(sql, new String[] { setYear });
    return listData;
  }

  /**
   * 得到基础数据列表
   * 
   * @return
   * @deprecated
   */
  //	public List getEleByRight(String eleTable) {
  ////		BUSI_Element busi = new BUSI_Element();
  ////		CDD_Element busi = new CDD_Element();
  //		XMLData mapData = new XMLData();
  //		ArrayList listData = new ArrayList();
  //		try {
  //			String returnData = ParseXML.convertObjToXml(
  //					busi.getEle(eleTable, -1, false), "data").toString();
  //			mapData = ParseXML.convertXmlToObj(returnData);
  //			listData = (ArrayList) mapData.getRecordList();
  //		} catch (Exception e1) {
  //			e1.printStackTrace();
  //		}
  //		return listData;
  //	}

  /**
   * 保存权限组数据
   * 
   * @param xmlData
   * @throws Exception 
   */
  public String saveOrUpdateSysRight(XMLData xmlData) throws Exception {
    String delete_sql = "";
    String new_right_type = "";
    try {
      XMLData base_info = (XMLData) xmlData.getSubObject("base_info");
      List detail_data = (List) xmlData.getSubObject("detail_data");
      List type_data = (List) xmlData.getSubObject("type_data");
      XMLData relation_info_data = (XMLData) xmlData.getSubObject("relation_info");
      XMLData role_info_data = (XMLData) xmlData.getSubObject("role_info");

      new_right_type = role_info_data.get("new_right_type").toString();
      // 新增权限组为主权限/追加权限/排除权限
      XMLData rowData1;
      XMLData rowData2;

      delete_sql = "delete from  sys_role_right_group  b where b.right_group_id=? ";
      dao.executeBySql(delete_sql, new String[] { (String) base_info.get("right_group_id") });
      delete_sql = "delete from  sys_right_group  b where b.right_group_id=? and b.set_year= ? and b.rg_code=? ";
      dao
        .executeBySql(delete_sql, new String[] { (String) base_info.get("right_group_id"), getSetYear(), getRgCode() });

      delete_sql = "delete from  sys_right_group_type  b where b.right_group_id=? and b.set_year= ? and b.rg_code=? ";

      dao
        .executeBySql(delete_sql, new String[] { (String) base_info.get("right_group_id"), getSetYear(), getRgCode() });

      delete_sql = "delete from  sys_right_group_detail  b where b.right_group_id=? and b.set_year= ? and b.rg_code=? ";
      dao
        .executeBySql(delete_sql, new String[] { (String) base_info.get("right_group_id"), getSetYear(), getRgCode() });
      base_info.put("table_name", "mappingfiles.sysdb.SysRightGroup");
      try {
        dao.saveDataBySql("sys_right_group", base_info);
      } catch (Exception e) {
        // TODO 自动生成 catch 块
        e.printStackTrace();
      }

      relation_info_data.put("table_name", "mappingfiles.sysdb.SysRoleRightGroup");
      try {
        //				dao.saveorUpdate(relation_info_data);
        dao.saveDataBySql("sys_role_right_group", relation_info_data);
      } catch (Exception e) {
        // TODO 自动生成 catch 块
        e.printStackTrace();
      }

      Iterator iterator_detail_data = detail_data.iterator();
      while (iterator_detail_data.hasNext()) {
        rowData1 = (XMLData) iterator_detail_data.next();
        rowData1.put("table_name", "mappingfiles.sysdb.SysRightGroupDetail");
        System.out.println(rowData1);
        try {
          //					dao.saveorUpdate(rowData1);
          dao.saveDataBySql("sys_right_group_detail", rowData1);
        } catch (Exception e) {
          // TODO 自动生成 catch 块
          e.printStackTrace();
        }
      }
      Iterator iterator_type_data = type_data.iterator();
      while (iterator_type_data.hasNext()) {
        rowData2 = (XMLData) iterator_type_data.next();
        rowData2.put("table_name", "mappingfiles.sysdb.SysRightGroupType");
        System.out.println(rowData2);
        try {
          //					dao.saveorUpdate(rowData2);
          dao.saveDataBySql("sys_right_group_type", rowData2);
        } catch (Exception e) {
          // TODO 自动生成 catch 块
          e.printStackTrace();
        }
      }

      String update_sql = "update sys_role  b set b.right_type=? where b.role_id =? ";
      dao.executeBySql(update_sql, new String[] { new_right_type, (String) role_info_data.get("role_id") });

      if (new_right_type.toString().equals("1")) {
        delete_sql = "delete from sys_role_rcid  a where a.role_id=? ";
        dao.executeBySql(delete_sql, new String[] { (String) role_info_data.get("role_id") });

        String insert_sql = "insert into sys_role_rcid (set_year,role_id,rcid) " + " select a.set_year, '"
          + role_info_data.get("role_id").toString() + "', a.rcid" + " from sys_right_combination" + Tools.addDbLink()
          + " a" + " where a.set_year=" + getSetYear() + " and a.rg_code='" + getRgCode() + "'"
          + " and 1=1 and (1 = 0 ";
        String insert_and_sql = "";
        String insert_and_all_sql = "";
        List tadaList = (List) xmlData.get("tadaList");
        List listParas = (List) xmlData.get("listParas");
        Iterator tadaIt = null;
        Iterator listParasIt = null;
        if (tadaList != null && !tadaList.toString().equals("")) {
          tadaIt = tadaList.iterator();
        }
        if (listParas != null && !listParas.toString().equals("")) {
          listParasIt = listParas.iterator();
        }
        while (tadaIt.hasNext()) {
          HashMap tadaRow = (HashMap) tadaIt.next();
          if (tadaRow.get("right_group_describe").toString().equals("003")) {
            insert_and_sql = " and (1 = 1";
            listParas = (List) xmlData.get("listParas");
            listParasIt = listParas.iterator();
            while (listParasIt.hasNext()) {
              HashMap ParasRow = (HashMap) listParasIt.next();
              insert_and_sql = insert_and_sql + "and not exists" + " (select 1" + " from sys_right_group_detail  b"
                + "  where b.right_group_id = '" + tadaRow.get("right_group_id").toString() + "'"
                + " and b.rg_code = a.rg_code " + " and b.set_year = a.set_year" + " and b.ele_code = '"
                + ParasRow.get("ele_code") + "'and (b.ele_value = '#' or b.ele_value = a." + ParasRow.get("ele_code")
                + "_id))";
            }
            insert_and_all_sql = insert_and_all_sql + insert_and_sql + ")";
          } else {
            insert_sql = insert_sql + " or (1 = 1 ";
            listParas = (List) xmlData.get("listParas");
            listParasIt = listParas.iterator();
            while (listParasIt.hasNext()) {
              HashMap ParasRow = (HashMap) listParasIt.next();
              insert_sql = insert_sql + "and exists" + " (select 1" + " from sys_right_group_detail"
                + Tools.addDbLink() + " b" + " where b.right_group_id =" + "  '"
                + tadaRow.get("right_group_id").toString() + "'" + " and b.rg_code = a.rg_code "
                + " and b.set_year = a.set_year" + " and b.ele_code = '" + ParasRow.get("ele_code") + "'"
                + " and (b.ele_value = '#' or b.ele_value = a." + ParasRow.get("ele_code") + "_id)) ";
            }
            insert_sql = insert_sql + " ) ";
          }
        }
        insert_sql = insert_sql + " ) " + insert_and_all_sql;
        dao.executeBySql(insert_sql);
      } else {
        delete_sql = "delete from sys_role_rcid  a where a.role_id=? ";
        dao.executeBySql(delete_sql, new String[] { (String) role_info_data.get("role_id") });
      }
    } catch (Exception e) {
      throw e;
    }
    return "OK";
  }

  /**
   * 得到所有角色数据
   * 
   * @return
   */
  public List getRoles() {
    String sql = " select * from Sys_Role ";
    List list = null;
    list = dao.findBySql(sql);
    return list;
  }

  public List getAllSysApp() {
    String sql = "SELECT * from sys_app ";
    List list = null;
    list = dao.findBySql(sql);
    return list;
  }

  /**
   * 通过条件得到所有权限组信息数据
   * 
   * @return
   */
  public List getRightGroupByCondition(String condition) {
    String sql = "SELECT right1.RIGHT_GROUP_ID,right1.RIGHT_GROUP_CODE,right1.RIGHT_GROUP_NAME,right1.REMARK,right1.SET_YEAR,right1.ENABLED,"
      + "right1.RIGHT_GROUP_CLASSIFY,right1.RIGHT_GROUP_DESCRIBE,"
      + "right1.SYS_REMARK,"
      + "role1.ROLE_ID,role1.ROLE_CODE,role1.ROLE_NAME,right1.RIGHT_TYPE  "
      + "from Sys_Role"
      + " role1,Sys_Role_Right_Group"
      + " rolegroup1,Sys_Right_Group"
      + " right1"
      + " where role1.ROLE_ID = rolegroup1.ROLE_ID "
      + " and right1.RIGHT_GROUP_ID = rolegroup1.RIGHT_GROUP_ID "
      + condition;
    sql += " and right1.set_year= ? and right1.rg_code=? ";
    List list = null;
    list = dao.findBySql(sql, new Object[] { getSetYear(), getRgCode() });
    return list;
  }

  /**
   * 根据权限组ID得到权限组数据
   * 
   * @return
   */
  public List getSysRightGroup(String right_group_id) {
    String hql = "select * from SysRightGroup srg1 where srg1.RIGHT_GROUP_ID=?";
    List list = null;
    try {
      list = dao.findBySql(hql, new Object[] { right_group_id });
    } catch (Exception e) {
      e.printStackTrace();
    }

    return list;
  }

  /**
   * 根据权限组ID得到权限组TYPE数据
   * 
   * @return
   */
  public List getSysRightGroupType(String right_group_id) {
    String hql = " from Sys_Right_Group_Type   srg1 where srg1.RIGHT_GROUP_ID=?";
    List list = null;
    try {
      list = dao.findBySql(hql, new Object[] { right_group_id });
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  /**
   * 根据权限组ID得到权限组明细数据
   * 
   * @return
   */
  public List getSysRightGroupDetail(String right_group_id) {
    String hql = "select * from Sys_Right_Group_Detail srg1 where srg1.RIGHT_GROUP_ID=?";
    List list = null;
    try {
      list = dao.findBySql(hql, new Object[] { right_group_id });
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  public List getEleByRight(String eleTable) {
    return null;
  }

  /**
   * 得到权限组信息的sql语句
   * @return
   */
  public String getRightGroupInfoSql() {
    StringBuffer sql = new StringBuffer();
    if (TypeOfDB.isOracle()) {
      sql.append("select * from ").append(" (select d.ele_code,").append(" d.ele_value,").append(" case")
        .append(" when length(d.ele_value) = 3 then").append(" '  ' || d.ele_value || ' ' || d.ele_name")
        .append(" when (length(d.ele_value) = 6 or length(d.ele_value) = 5) then")
        .append(" '    ' || d.ele_value || ' ' || d.ele_name")
        .append(" when (length(d.ele_value) = 9 or length(d.ele_value) = 7) then")
        .append(" '       ' || d.ele_value || ' ' || d.ele_name").append(" when (length(d.ele_value) = 12) then")
        .append(" '        ' || d.ele_value || ' ' || d.ele_name").append(" when (length(d.ele_value) = 15) then")
        .append(" '            ' || d.ele_value || ' ' || d.ele_name").append(" when (length(d.ele_value) < 3) then")
        .append(" '' || d.ele_value || ' ' || d.ele_name").append(" end codename")
        .append(" from sys_right_group c, sys_right_group_detail d")
        .append(" where c.right_group_id = d.right_group_id").append("  and  c.set_year = d.set_year ")
        .append("  and  c.rg_code = d.rg_code ").append(" and d.ele_value <> '#'")
        .append(" and c.right_group_describe = ?").append("    and c.rule_id = ?").append(" union all")
        .append(" select distinct d.ele_code,'0' ele_value, e.ele_name ||'：' ele_name ")
        .append(" from sys_right_group c, sys_right_group_detail d, sys_element e")
        .append(" where c.right_group_id = d.right_group_id").append("  and  c.set_year = d.set_year ")
        .append("  and  c.rg_code = d.rg_code ").append(" and d.ele_value <> '#'")
        .append(" and c.right_group_describe = ?").append(" and c.rule_id = ?")
        .append(" and d.ele_code = e.ele_code) v").append("  order by v.ele_code, ele_value");
    } else {
      sql.append("select * from ").append(" (select d.ele_code,").append(" d.ele_value, ")
        .append(" concat((case length(d.ele_value) ").append(" when 3 then  '  ' ")
        .append(" when 6 then  '    '     when 5 then  '    ' ")
        .append(" when 9 then  '       '  when 7 then  '       ' ")
        .append(" when 12 then '        ' when 15 then '            ' ")
        .append(" when 0 then  '' when 1 then  '' when 2 then  '' ").append(" end),  ")
        .append(" (case when length(d.ele_value) in (0, 1, 2, 3, 5, 6, 7, 9, 12, 15) then ")
        .append(" concat(d.ele_value , ' ' , d.ele_name) ").append(" end)) codename ")
        .append(" from sys_right_group c, sys_right_group_detail d")
        .append(" where c.right_group_id = d.right_group_id").append("  and  c.set_year = d.set_year ")
        .append("  and  c.rg_code = d.rg_code ").append(" and d.ele_value <> '#'")
        .append(" and c.right_group_describe = ?").append("    and c.rule_id = ?").append(" union all")
        .append(" select distinct d.ele_code,'0' ele_value, e.ele_name ||'：' ele_name ")
        .append(" from sys_right_group c, sys_right_group_detail d, sys_element e")
        .append(" where c.right_group_id = d.right_group_id").append("  and  c.set_year = d.set_year ")
        .append("  and  c.rg_code = d.rg_code ").append(" and d.ele_value <> '#'")
        .append(" and c.right_group_describe = ?").append(" and c.rule_id = ?")
        .append(" and d.ele_code = e.ele_code) v").append("  order by v.ele_code, ele_value");

    }
    return sql.toString();
  }

  /**
   * 得到主范围的信息
   */
  public List getMainRightInfoByRuleId(String ruleid) {
    return dao.findBySql(getRightGroupInfoSql(), new Object[] { "001", ruleid, "001", ruleid });
  }

  /**
   * 得到追加范围的信息
   */
  public List getSuperAdditionRightInfoByRuleId(String ruleid) {
    return dao.findBySql(getRightGroupInfoSql(), new Object[] { "002", ruleid, "002", ruleid });
  }

  /**
   * 得到排除范围的信息
   */
  public List getMinusRightInfoByRuleId(String ruleid) {
    return dao.findBySql(getRightGroupInfoSql(), new Object[] { "003", ruleid, "003", ruleid });
  }

  /**
   * 获取右边展示列表
   * yanyga
   */
  public List getMainInfoTreeByRuleIdNew(String ruleid) {

    String selectDistinctEleSql = "select distinct t.right_group_id,t.ele_code,"
      + (TypeOfDB.isOracle() ? " t.ele_code||'001' " : " concat(t.ele_code, '001') ")
      + " as chr_id,e.ele_name as chr_name,'' as parent_id  from sys_right_group_detail t,sys_element e where t.ele_code=e.ele_code and t.right_group_id in "
      + "(select srp.right_group_id from sys_right_group srp where srp.rule_id =?)";

    List listPanent = dao.findBySql(selectDistinctEleSql, new Object[] { ruleid });
    List resultList = new ArrayList();
    for (int i = 0; i < listPanent.size(); i++) {
      Map temp = (Map) listPanent.get(i);
      String eleCode = (String) temp.get("ele_code");
      String rightGroupId = (String) temp.get("right_group_id");

      String sql = "select '"
        + eleCode
        + "001' as parent_id, "
        + (TypeOfDB.isOracle() ? "decode(t.ele_value||' '||t.ele_name,'# ','全部权限',t.ele_value||' '||t.ele_name)"
          : "if(concat(t.ele_value,' ',t.ele_name)='# ','全部权限',concat(t.ele_value,' ',t.ele_name))") + " as chr_name,"
        + (TypeOfDB.isOracle() ? "t.ele_code||t.ele_name" : " concat(t.ele_code,t.ele_name)")
        + " as chr_id from sys_right_group_detail t where t.ele_code=? and t.right_group_id=?";
      //modify 显示编码和名称  yanyga 2017-07-27
      //        + "001' as parent_id,nvl(t.ele_name,'全部权限')as chr_name,t.ele_code| |t.ele_name as chr_id from sys_right_group_detail t where t.ele_code=? and t.right_group_id=?";

      List tempList = dao.findBySql(sql, new Object[] { eleCode, rightGroupId });
      resultList.addAll(tempList);
    }
    resultList.addAll(listPanent);
    return resultList;

  }

  public List getMainRightInfoTreeByRuleId(String ruleid) {
    String sql = "SELECT *                                                      "
      + "  FROM (SELECT *                                                       "
      + "           FROM sys_right_group_info i                                 "
      + "          WHERE (rule_id = ? OR (rule_id = 0 AND parent_id=''))        "
      + "            AND rg_code = ?                                            "
      + "            AND set_year = ?                                           "
      + "         UNION ALL                                                     "
      + "         SELECT i.*                                                    "
      + "           FROM sys_right_group_info i                                 "
      + "          WHERE i.rule_id = 0                                          "
      + "            AND i.rg_code = ?                                          "
      + "            AND i.set_year = ?                                         "
      + "            AND EXISTS (SELECT 1                                       "
      + "                   FROM sys_right_group_info g                         "
      + "                  WHERE i.chr_id = g.parent_id                         "
      + "                    AND g.rule_id = ?)                                 "
      + "   )                                                                   "
      + " ORDER BY chr_code                                                     ";

    String rgCode = SessionUtil.getRgCode();
    String setYear = SessionUtil.getLoginYear();
    Object[] params = new Object[] { ruleid, rgCode, setYear, rgCode, setYear, ruleid };
    return dao.findBySql(sql, params);//dingyy20121218 多年度下数据重复
  }

  /**
   * 判断是否是全部权限
   */
  public boolean isAllRight(String ruleid) {
    String sql = "select right_type from sys_right_group where rule_id = ? and set_year= ? and rg_code=? ";
    Object[] params = new Object[] { ruleid, getSetYear(), getRgCode() };
    List list = dao.findBySql(sql, params);
    if (list == null || list.size() == 0) {
      return false;
    }
    String righttype = ((XMLData) list.get(0)).get("right_type").toString();
    if (righttype == "0") {
      return true;
    }
    return false;
  }

  /**
   * 2017-05-04 by yanyga
   * 得到权限要素配置数据
   * 
   * @return List
   */
  public List getElementByCondition(String condition) {
    String sql = "select * from sys_element where rg_code =? and set_year=? " + condition;
    List list = dao.findBySql(sql, new Object[] { getRgCode(), getSetYear() });
    return list;
  }

}
