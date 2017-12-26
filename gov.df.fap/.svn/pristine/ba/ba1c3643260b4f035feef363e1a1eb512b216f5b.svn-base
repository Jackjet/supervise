package gov.df.fap.service.rule;

import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.api.rule.IRuleConfigure;
import gov.df.fap.bean.rule.dto.RightGroupDTO;
import gov.df.fap.bean.rule.dto.RightGroupDetailDTO;
import gov.df.fap.bean.rule.dto.RightGroupTypeDTO;
import gov.df.fap.bean.rule.dto.RuleDTO;
import gov.df.fap.bean.rule.entity.RightGroupDetailEntity;
import gov.df.fap.bean.rule.entity.RightGroupEntity;
import gov.df.fap.bean.rule.entity.RightGroupTypeEntity;
import gov.df.fap.bean.rule.entity.RuleEntity;
import gov.df.fap.bean.rule.entity.SysRightGroupDetailEntity;
import gov.df.fap.bean.rule.entity.SysRightGroupEntity;
import gov.df.fap.bean.rule.entity.SysRightGroupTypeEntity;
import gov.df.fap.bean.rule.entity.SysRuleEntity;
import gov.df.fap.bean.sysdb.SysRightGroup;
import gov.df.fap.bean.sysdb.SysRightGroupDetail;
import gov.df.fap.bean.sysdb.SysRightGroupType;
import gov.df.fap.bean.sysdb.SysRoleRule;
import gov.df.fap.bean.sysdb.SysRule;
import gov.df.fap.service.util.DatabaseAccess;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.log.Log;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.XMLData;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 规则组件配置时接口实现类
 * 
 * @author 
 * 
 */
@Service
public class RuleImplBO implements IRuleConfigure {

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao = null;

  /**
   * 删除规则
   * 
   * @param
   */
  public void deleteRule(String ruleid) throws Exception {
    String delete_sql = "";

    try {
      // 删除权限组类型表
      delete_sql = "delete from  sys_right_group_type" + Tools.addDbLink()
        + " b where b.right_group_id in (select right_group_id from sys_right_group" + Tools.addDbLink()
        + " where rule_id=?) ";

      /*** Add by fengdz TM:2012-03-23 ***/
      String rg_code = SessionUtil.getRgCode();
      String set_year = SessionUtil.getLoginYear();

      delete_sql += " and b.set_year= " + set_year + " and b.rg_code='" + rg_code + "'";

      /*** Add by fengdz TM:2012-03-23 ***/
      dao.executeBySql(delete_sql, new Object[] { ruleid });

      // 删除权限明细表
      delete_sql = "delete from  sys_right_group_detail" + Tools.addDbLink()
        + " b where b.right_group_id in (select right_group_id from sys_right_group" + Tools.addDbLink()
        + " where rule_id=?) ";
      /*** Add by fengdz TM:2012-03-23 ***/

      delete_sql += " and b.set_year= " + set_year + " and b.rg_code='" + rg_code + "'";

      /*** Add by fengdz TM:2012-03-23 ***/
      dao.executeBySql(delete_sql, new Object[] { ruleid });
      /*
       * add daiwei 20070516 start 删除权限组的同时要把相关的范围也删除
       */
      // 删除主范围和追加范围
      delete_sql = "delete from sys_rule_cross_join_add" + Tools.addDbLink() + " b where b.rule_id = ?";
      dao.executeBySql(delete_sql, new Object[] { ruleid });

      // 删除追减范围
      delete_sql = "delete from sys_rule_cross_join_del" + Tools.addDbLink() + " b where b.rule_id = ?";
      dao.executeBySql(delete_sql, new Object[] { ruleid });
      /*
       * add daiwei 20070516 end
       */

      // 删除权限组主表
      delete_sql = "delete from  sys_right_group" + Tools.addDbLink() + " b where rule_id=?";
      /*** Add by fengdz TM:2012-03-23 ***/

      delete_sql += " and b.set_year= " + set_year + " and b.rg_code='" + rg_code + "'";

      /*** Add by fengdz TM:2012-03-23 ***/
      dao.executeBySql(delete_sql, new Object[] { ruleid });

      // 删除规则主表
      delete_sql = "delete from  sys_rule" + Tools.addDbLink() + " b where b.rule_id=? ";
      dao.executeBySql(delete_sql, new Object[] { ruleid });
    } catch (Exception e) {
      e.printStackTrace();
      Log.error("执行sql出错：\n" + e.getMessage() + "\nSQL开始\n" + delete_sql + "\nSQL结束");
      throw e;
    }

  }

  /**
   * 删除角色与规则的关联
   * 
   * @param
   */
  public void deleteRoleRule(String roleid, String userid) throws Exception {
    // 将用户角色的组合后细化的数据权限恢复成对应的用户权限
    String sql = "update sys_user_role_rule a set a.rule_id=(select rule_id from sys_user_rule where user_id=?) "
      + "where a.user_id=?";
    dao.executeBySql(sql, new Object[] { userid, userid });
    // 将对应角色置为没有权限
    String update_sql = "update sys_role" + Tools.addDbLink() + " set right_type=2 where role_id='" + roleid + "' ";
    dao.executeBySql(update_sql);
  }

  /**
   * 得到所有角色启用的基础要素
   * 
   * @return
   */
  public ArrayList getRoleEnabledEle(String setYear) {
    String sql = "select t.ele_source,t.ele_code as ele_code,t.ele_name from sys_element" + Tools.addDbLink()
      + " t where t.is_rightfilter=1 and t.set_year=? and t.rg_code=?";
    String[] params = new String[] { setYear, SessionUtil.getUserInfoContext().getRgCode() };
    ArrayList listData = (ArrayList) dao.findBySql(sql, params);
    return listData;
  }

  /**
   * 得到所有启用的基础要素
   * 
   * @return
   */
  public ArrayList getAllEnabledEle(String setYear) {
    IDictionary iDic = (IDictionary) SessionUtil.getServerBean("sys.dictionaryService");
    List listData = iDic.getElementSet(" and is_local=0 ");
    ArrayList retList = new ArrayList();

    if (listData != null) {
      Iterator it = listData.iterator();
      while (it.hasNext()) {
        retList.add(it.next());
      }

    }

    return retList;
  }

  /**
   * 得到所有子系统
   * 
   * @return
   */
  public List getAllSysApp() {
    String sql = "SELECT * from sys_app" + Tools.addDbLink() + "";
    List list = null;
    list = dao.findBySql(sql);
    return list;
  }

  /**
   * 保存角色与规则的关联数据
   * 
   * @param
   * @throws Exception
   */
  public void saveOrUpdateRoleRule(SysRoleRule ruleData, String role_new_right_type) throws Exception {
    updateRole(ruleData.getROLE_ID(), ruleData.getRULE_ID(), role_new_right_type);
  }

  /**
   * 更新对应的角色数据
   * 
   * @param
   * @throws Exception
   */
  private void updateRole(String role_id, String rule_id, String new_right_type) throws Exception {
    String delete_sql = "";
    // 如果修改后的权限类型为部分权限，
    // 则需要根据目前的授权情况重新刷新一下角色与RCID的对应关系
    if (new_right_type.toString().equals("1")) {
      delete_sql = "delete from sys_rule_rcid" + Tools.addDbLink() + " a where a.rule_id='" + rule_id + "'";
      dao.executeBySql(delete_sql);

      String insert_sql = "insert into sys_rule_rcid" + Tools.addDbLink() + "(set_year,rule_id,rcid,rg_code)"//add by kongcy
        + " select a.set_year, '" + rule_id + "', a.rcid,a.rg_code " + " from sys_right_combination"//add by kongcy 
        + Tools.addDbLink() + " a" + " where 1=1 and (1 = 0 ";
      // 主权限和追加权限的插入语句
      String insert_union_sql = "";

      // 排除权限的插入语句
      String insert_minus_sql = "";
      String rg_code = getRgCode();//add by kongcy  at 2012-03-22 15:54:52
      String set_year = getSetYear();//add by kongcy  at 2012-03-22 15:54:52
      // 查询该规则对应的权限组
      List rightGroupList = dao.findBySql("select * from sys_right_group" + Tools.addDbLink() + " where rule_id='"
        + rule_id + "'" + " and set_year= " + set_year + " and rg_code='"// modify
        + rg_code + "'");
      // 取得角色的启用要素
      List elelist = (List) getRoleEnabledEle(SessionUtil.getUserInfoContext().getSetYear());
      Iterator tadaIt = null;
      Iterator listParasIt = null;

      if (rightGroupList != null) {
        tadaIt = rightGroupList.iterator();
      }
      if (elelist != null) {
        listParasIt = elelist.iterator();
      }

      // 遍历所有权限组
      while (tadaIt.hasNext()) {
        HashMap tadaRow = (HashMap) tadaIt.next();
        // 如果为排除权限时
        if (tadaRow.get("right_group_describe").toString().equals("003")) {
          insert_minus_sql = insert_minus_sql + " and not (1 = 1 ";
          listParasIt = elelist.iterator();
          while (listParasIt.hasNext()) {
            HashMap ParasRow = (HashMap) listParasIt.next();
            insert_minus_sql = insert_minus_sql + "and  exists" + " (select 1" + " from sys_right_group_detail"
              + Tools.addDbLink() + " b" + "  where b.right_group_id = '" + tadaRow.get("right_group_id").toString()
              + "'" + " and b.set_year = a.set_year"

              /*** Add by fengdz TM:2012-03-23 ***/
              + " and b.rg_code = a.rg_code "
              /*** Add by fengdz TM:2012-03-23 ***/
              + " and b.ele_code = '" + ParasRow.get("ele_code")
              + "'and (b.ele_value = '#' or b.ele_value =(select c.chr_code from " + ParasRow.get("ele_source") + ""
              + Tools.addDbLink() + " c where c.chr_id=a." + ParasRow.get("ele_code") + "_id)))";
          }
          // 拼成排除权限的SQL
          insert_minus_sql = insert_minus_sql + ")";
        } else {
          // 如果为主权限或者追加权限时
          insert_union_sql = insert_union_sql + " or (1 = 1 ";
          listParasIt = elelist.iterator();
          while (listParasIt.hasNext()) {
            HashMap ParasRow = (HashMap) listParasIt.next();
            insert_union_sql = insert_union_sql
              + "and (a."
              + ParasRow.get("ele_code")
              // + "_id is null or exists"
              + "_id = '#' or exists" + " (select 1" + " from sys_right_group_detail" + Tools.addDbLink() + " b"
              + " where b.right_group_id =" + "  '" + tadaRow.get("right_group_id").toString() + "'"
              + " and b.set_year = a.set_year"

              /*** Add by fengdz TM:2012-03-23 ***/
              + " and b.rg_code = a.rg_code "
              /*** Add by fengdz TM:2012-03-23 ***/

              + " and b.ele_code = '" + ParasRow.get("ele_code") + "'"
              + " and (b.ele_value = '#' or b.ele_value =(select c.chr_code from " + ParasRow.get("ele_source") + ""
              + Tools.addDbLink() + " c where c.chr_id=a." + ParasRow.get("ele_code") + "_id)))) ";
          }
          insert_union_sql = insert_union_sql + " ) ";
        }
      }

      // 拼成一个总的SQL
      insert_sql = insert_sql + insert_union_sql + " ) " + insert_minus_sql;
      dao.executeBySql(insert_sql);
    } else {
      delete_sql = "delete from sys_rule_rcid" + Tools.addDbLink() + " a where a.rule_id='" + rule_id + "'";
      dao.executeBySql(delete_sql);
    }
  }

  /**
   * 保存规则数据
   * 
   * @param
   * @throws Exception
   */
  public void saveOrUpdateRule(RuleDTO ruleData) throws Exception {
    // 删除原有规则数据
    deleteRule(ruleData.getRULE_ID().toString());

    // 保存规则主表数据
    SysRule sysRule = new SysRule();
    //清空权限组缓存
    DataRightBO.removeRoleMenu();
    Map tmp_map = new HashMap();
    // 将界面DTO转为数据库DTO

    BeanUtils.copyProperties(sysRule, ruleData);

    // dao.save(sysRule);
    tmp_map = BeanUtils.describe(sysRule);
    tmp_map.remove("class");
    dao.executeBySql(DatabaseAccess.getInsetSql("sys_rule" + Tools.addDbLink() + "", tmp_map, tmp_map));

    // 循环保存规则中的权限组
    for (int i = 0; i < ruleData.right_group_list.size(); i++) {
      // 保存权限组的基本信息
      SysRightGroup sysRightGroup = new SysRightGroup();
      RightGroupDTO rightGroupDto = (RightGroupDTO) ruleData.right_group_list.get(i);
      BeanUtils.copyProperties(sysRightGroup, rightGroupDto);
      // dao.save(sysRightGroup);
      tmp_map = BeanUtils.describe(sysRightGroup);
      tmp_map.remove("class");
      dao.executeBySql(DatabaseAccess.getInsetSql("sys_right_group" + Tools.addDbLink() + "", tmp_map, tmp_map));

      // 循环保存权限组类型数据
      for (int j = 0; j < rightGroupDto.type_list.size(); j++) {
        SysRightGroupType sysRightGroupType = new SysRightGroupType();
        BeanUtils.copyProperties(sysRightGroupType, (RightGroupTypeDTO) rightGroupDto.type_list.get(j));
        // dao.save(sysRightGroupType);
        tmp_map = BeanUtils.describe(sysRightGroupType);
        tmp_map.remove("class");
        dao.executeBySql(DatabaseAccess.getInsetSql("sys_right_group_type" + Tools.addDbLink() + "", tmp_map, tmp_map));

      }

      // 循环保存权限组明细数据
      for (int k = 0; k < rightGroupDto.detail_list.size(); k++) {
        SysRightGroupDetail sysRightGroupDetail = new SysRightGroupDetail();
        BeanUtils.copyProperties(sysRightGroupDetail, (RightGroupDetailDTO) rightGroupDto.detail_list.get(k));
        tmp_map = BeanUtils.describe(sysRightGroupDetail);
        tmp_map.remove("class");
        dao
          .executeBySql(DatabaseAccess.getInsetSql("sys_right_group_detail" + Tools.addDbLink() + "", tmp_map, tmp_map));

      }

    }
    String sql = "select b.rule_classify from sys_rule b where b.rule_id = " + ruleData.getRULE_ID().toString();
    List list = dao.findBySql(sql);

    if (list != null && list.size() > 0) {
      String ruleClassify = ((XMLData) list.get(0)).get("rule_classify").toString();
      if ("006".equals(ruleClassify) || "005".equals(ruleClassify)) {
        this.saveRuleCross(ruleData.getRULE_ID().toString());
      }
    }
  }

  /**
   * 要素定值规则：保存规则数据
   * 
   * @param
   * @throws Exception
   */
  public void saveAndUpdateRule(String ele_value, String ele_code, String ele_name, String rule_id, String ele_rule_id,
    RuleEntity ruleData, String is_relation_ele) throws Exception {
    PreparedStatement ps = null;
    Session session = null;
    if ("1".equals(is_relation_ele)) {
      try {
        /** 黄节 2007年12月13日修改要素定值规则出现重复的错误 start**/
        // 删除重复数据
        StringBuffer strSQL = new StringBuffer();
        strSQL.append("select (1) from sys_ele_rule_detail");
        strSQL.append(" where ele_value='");
        strSQL.append(ele_value);
        strSQL.append("' and ele_rule_id='");
        strSQL.append(ele_rule_id);
        strSQL.append("' ");
        List detailList = dao.findBySql(strSQL.toString());
        if (detailList.size() > 0) {
          strSQL.delete(0, strSQL.length());
          strSQL.append("delete from sys_ele_rule_detail");
          strSQL.append(" where ele_value='");
          strSQL.append(ele_value);
          strSQL.append("' and ele_rule_id='");
          strSQL.append(ele_rule_id);
          strSQL.append("' ");
          dao.executeBySql(strSQL.toString());
        }
        /** 黄节 2007年12月13日修改要素定值规则出现重复的错误 end**/
        // 插入新数据
        strSQL.delete(0, strSQL.length());
        strSQL.append("insert into sys_ele_rule_detail").append(Tools.addDbLink())
          .append(" (ELE_RULE_DETAIL_ID,ELE_RULE_ID,ELE_VALUE,RULE_ID,create_user,create_date,LATEST_OP_USER,")
          .append("LATEST_OP_DATE,LAST_VER,SET_YEAR,ele_code,ele_name,rg_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
        Connection conn = null;
        session = dao.getSession();
        conn = session.connection();
        ps = conn.prepareStatement(strSQL.toString());
        ps.setString(1, UUIDRandom.generate());
        ps.setString(2, ele_rule_id);
        ps.setString(3, ele_value);
        ps.setString(4, rule_id);
        ps.setString(5, (String) SessionUtil.getUserInfoContext().getUserID());
        ps.setString(6, DateHandler.getLastVerTime());
        ps.setString(7, (String) SessionUtil.getUserInfoContext().getUserID());
        ps.setString(8, DateHandler.getLastVerTime());
        ps.setString(9, Tools.getCurrDate());
        ps.setString(10, SessionUtil.getLoginYear());
        ps.setString(11, ele_code);
        ps.setString(12, ele_name);
        ps.setString(13, SessionUtil.getRgCode());

        ps.execute();
        strSQL = null;
      } catch (Exception e) {
        throw e;
      } finally {
        try {
          if (ps != null) {
            ps.close();
          }
          if (session != null) {
            dao.closeSession(session);
          }
        } catch (Exception ex) {
        }
      }
    }
    // 删除原有规则数据
    deleteRule(ruleData.getRule_id().toString());

    // 保存规则主表数据
    SysRuleEntity sysRule = new SysRuleEntity();
    //清空权限组缓存
    DataRightBO.removeRoleMenu();
    Map tmp_map = new HashMap();
    // 将界面DTO转为数据库DTO

    BeanUtils.copyProperties(sysRule, ruleData);

    // dao.save(sysRule);
    tmp_map = BeanUtils.describe(sysRule);
    tmp_map.remove("class");
    dao.executeBySql(DatabaseAccess.getInsetSql("sys_rule" + Tools.addDbLink() + "", tmp_map, tmp_map));

    // 循环保存规则中的权限组
    for (int i = 0; i < ruleData.right_group_list.size(); i++) {
      // 保存权限组的基本信息
      SysRightGroupEntity sysRightGroup = new SysRightGroupEntity();
      RightGroupEntity rightGroupDto = (RightGroupEntity) ruleData.right_group_list.get(i);
      BeanUtils.copyProperties(sysRightGroup, rightGroupDto);
      // dao.save(sysRightGroup);
      tmp_map = BeanUtils.describe(sysRightGroup);
      tmp_map.remove("class");
      dao.executeBySql(DatabaseAccess.getInsetSql("sys_right_group" + Tools.addDbLink() + "", tmp_map, tmp_map));

      // 循环保存权限组类型数据
      for (int j = 0; j < rightGroupDto.type_list.size(); j++) {
        SysRightGroupTypeEntity sysRightGroupType = new SysRightGroupTypeEntity();
        BeanUtils.copyProperties(sysRightGroupType, (RightGroupTypeEntity) rightGroupDto.type_list.get(j));
        // dao.save(sysRightGroupType);
        tmp_map = BeanUtils.describe(sysRightGroupType);
        tmp_map.remove("class");
        dao.executeBySql(DatabaseAccess.getInsetSql("sys_right_group_type" + Tools.addDbLink() + "", tmp_map, tmp_map));

      }

      // 循环保存权限组明细数据
      for (int k = 0; k < rightGroupDto.detail_list.size(); k++) {
        SysRightGroupDetailEntity sysRightGroupDetail = new SysRightGroupDetailEntity();
        BeanUtils.copyProperties(sysRightGroupDetail, (RightGroupDetailEntity) rightGroupDto.detail_list.get(k));
        tmp_map = BeanUtils.describe(sysRightGroupDetail);
        tmp_map.remove("class");
        dao
          .executeBySql(DatabaseAccess.getInsetSql("sys_right_group_detail" + Tools.addDbLink() + "", tmp_map, tmp_map));

      }

    }
    String sql = "select b.rule_classify from sys_rule b where b.rule_id = " + ruleData.getRule_id().toString();
    List list = dao.findBySql(sql);

    if (list != null && list.size() > 0) {
      String ruleClassify = ((XMLData) list.get(0)).get("rule_classify").toString();
      if ("006".equals(ruleClassify) || "005".equals(ruleClassify)) {
        this.saveRuleCross(ruleData.getRule_id().toString());
      }
    }
  }

  /**
   * 保存规则数据到临时表中，为了在没保存前预览用
   * 
   * @param
   * @throws Exception
   * 
   *    update by kongcy at 2012-03-22 15:24:15 sys_rule_cache
   *             增加rg_code字段
   */
  public void saveRuleToTmp(RuleDTO ruleData) throws Exception {
    String insert_sql = "";
    insert_sql = "insert into sys_rule_cache" + Tools.addDbLink()
      + " (rule_id,rule_code,set_year,right_type,rg_code) values('" + ruleData.getRULE_ID().toString() + "','"
      + ruleData.getRULE_ID().toString() + "'," + ruleData.getSET_YEAR().toString() + "," + ruleData.getRIGHT_TYPE()
      + "," + ruleData.getRG_CODE() + ")";//modify by kongcy
    dao.executeBySql(insert_sql);
    String rg_code = getRgCode();
    String set_year = getSetYear();
    for (int i = 0; i < ruleData.right_group_list.size(); i++) {
      // 保存权限组基本信息
      RightGroupDTO rightGroupDto = (RightGroupDTO) ruleData.right_group_list.get(i);
      insert_sql = "insert into sys_right_group_cache"
        + Tools.addDbLink()
        /*** Modify by fengdz TM:2012-03-22 ***/
        // +
        // "(right_group_id,right_group_describe,right_type,rule_id) values('"
        + "(right_group_id,right_group_describe,right_type,rule_id,rg_code,set_year) values('"
        /*** Modify by fengdz TM:2012-03-22 ***/

        + rightGroupDto.getRIGHT_GROUP_ID().toString() + "','" + rightGroupDto.getRIGHT_GROUP_DESCRIBE().toString()
        + "'," + rightGroupDto.getRIGHT_TYPE() + ",'" + rightGroupDto.getRULE_ID().toString() + "',"

        /*** Add by fengdz TM:2012-03-22 ***/
        + rg_code + ",'" + set_year + "')";
      /*** Add by fengdz TM:2012-03-22 ***/
      dao.executeBySql(insert_sql);

      // 循环保存权限组类型数据
      for (int j = 0; j < rightGroupDto.type_list.size(); j++) {
        RightGroupTypeDTO typeDto = (RightGroupTypeDTO) rightGroupDto.type_list.get(j);
        insert_sql = "insert into sys_right_group_type_cache" + Tools.addDbLink()
          + "(right_group_id,ele_code,right_type,set_year) values('" + typeDto.getRIGHT_GROUP_ID().toString() + "','"
          + typeDto.getELE_CODE().toString() + "'," + typeDto.getRIGHT_TYPE() + "," + typeDto.getSET_YEAR() + ")";

        dao.executeBySql(insert_sql);
      }

      // 循环保存权限组明细数据
      for (int k = 0; k < rightGroupDto.detail_list.size(); k++) {
        RightGroupDetailDTO detailDto = (RightGroupDetailDTO) rightGroupDto.detail_list.get(k);
        insert_sql = "insert into sys_right_group_detail_cache" + Tools.addDbLink()
          + "(right_group_id,ele_code,ele_value,set_year) values('" + detailDto.getRIGHT_GROUP_ID().toString() + "','"
          + detailDto.getELE_CODE().toString() + "','" + detailDto.getELE_VALUE().toString() + "',"
          + detailDto.getSET_YEAR() + ")";

        dao.executeBySql(insert_sql);
      }

    }

  }

  /**
   * 得到所有角色数据
   * 
   * @return
   */
  public List getAllRoles() {
    String sql = " select * from Sys_Role" + Tools.addDbLink() + "";
    List list = null;
    list = dao.findBySql(sql);
    return list;
  }

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 范围查重
   * 
   * @param ruleid-------------
   * @return list--------------返回范围重复笛卡儿集
   * @throws Exception---------错误信息
   */
  public List checkDuplication(List ruleids) throws Exception {

    return null;
  }

  /**
   * 范围查重
   * 
   * @param ruleid-------------
   * @return list--------------返回范围重复笛卡儿集
   * @throws Exception---------错误信息
   */
  public XMLData checkDuplication(String ruleid1, String ruleid2) throws Exception {
    List tmp_list;
    String right_type1 = "";
    String right_type2 = "";

    tmp_list = dao.findBySql("select * from sys_rule" + Tools.addDbLink()

    /*** Add by fengdz TM:2012-03-23 ***/
    // + " where rule_id='" + ruleid1 + "'");

      + " where rule_id='" + ruleid1 + "' and set_year=" + getSetYear() + " and rg_code='" + getRgCode() + "'");

    /*** Add by fengdz TM:2012-03-23 ***/
    if (tmp_list.size() > 0) {
      right_type1 = ((Map) tmp_list.get(0)).get("right_type").toString();
    } else {
      throw new Exception("找不到该规则");
    }

    tmp_list = dao.findBySql("select * from sys_rule" + Tools.addDbLink()

    /*** Add by fengdz TM:2012-03-23 ***/
    // + " where rule_id='" + ruleid2 + "'");

      + " where rule_id='" + ruleid2 + "' and set_year=" + getSetYear() + " and rg_code='" + getRgCode() + "'");

    /*** Add by fengdz TM:2012-03-23 ***/
    if (tmp_list.size() > 0) {
      right_type2 = ((Map) tmp_list.get(0)).get("right_type").toString();
    } else {
      throw new Exception("找不到该规则");
    }

    if (right_type1.equals("0") && right_type2.equals("0")) {
      return this.getAllRightTypeData();
    }
    if (right_type1.equals("0") && right_type2.equals("1")) {
      return this.getDisplayData(ruleid2);
    }
    if (right_type1.equals("1") && right_type2.equals("0")) {
      return this.getDisplayData(ruleid1);
    }
    String rg_code = getRgCode();
    String set_year = getSetYear();
    if (right_type1.equals("1") && right_type2.equals("1")) {
      // 返回数据
      XMLData return_data = new XMLData();

      // 返回数据中预览数据列表
      List return_list = new ArrayList();

      // 返回数据中要素信息
      Map return_map = new HashMap();

      String select_sql = "";
      // 参与统计的笛卡儿乘积中的要素项
      List allEleList = new ArrayList();
      allEleList = dao.findBySql("select distinct a.ele_code,0 as right_type,b.ele_name,b.ele_source "
        + " from sys_right_group_type" + Tools.addDbLink() + " a,sys_element" + Tools.addDbLink()
        + " b,sys_right_group" + Tools.addDbLink() + " c where a.right_type=1 and "
        + " a.set_year=c.set_year  and a.rg_code=c.rg_code and a.rg_code = b.rg_code  and b.rg_code = c.rg_code and  "
        + " a.right_group_id =c.right_group_id and c.rule_id in ('" + ruleid1 + "','" + ruleid2
        + "') and a.ele_code=b.ele_code" + " and a.set_year= " + set_year + " and a.rg_code='" + rg_code + "'");

      // 构建返回数据中的要素信息
      for (int i = 0; i < allEleList.size(); i++) {
        String ele_code = ((Map) allEleList.get(i)).get("ele_code").toString();
        String ele_name = ((Map) allEleList.get(i)).get("ele_name").toString();
        return_map.put(ele_code.toLowerCase() + "_id", ele_name);

        select_sql = select_sql + ele_code.toLowerCase() + "_id,";
      }

      // 构建预览数据
      create_tmp_rule(ruleid1, (ArrayList) allEleList, "");
      create_tmp_rule(ruleid2, (ArrayList) allEleList, "");

      return_list = dao.findBySql("Select " + select_sql.substring(0, select_sql.length() - 1)
        + " from Sys_RULE_CROSS_JOIN_cache" + Tools.addDbLink() + " group "
        + select_sql.substring(0, select_sql.length() - 1) + " having count(*)>1");

      return_data.put("return_map", return_map);
      return_data.put("return_list", return_list);
      return return_data;
    }

    return null;
  }

  /**
   * 范围查漏
   * 
   * @param ruleids------------
   * @return list--------------返回范围遗漏笛卡儿集
   * @throws Exception---------错误信息
   */
  public XMLData checkMiss(List ruleids) throws Exception {
    String select_sql = "";
    String select_sql1 = "";
    String select_sql2 = "select * from (";
    String select_sql3 = "";
    for (int i = 0; i < ruleids.size(); i++) {
      XMLData rowData = (XMLData) ruleids.get(i);
      String ruleid = rowData.get("rule_id").toString();
      String right_type = "";
      List tmp_list = dao.findBySql("select * from sys_rule"
        /*** Add by fengdz TM:2012-03-23 ***/
        // + Tools.addDbLink() + " where rule_id='" + ruleid + "'");
        + Tools.addDbLink() + " where rule_id='" + ruleid + "' and set_year=" + getSetYear() + " and rg_code='"
        + getRgCode() + "'");
      /*** Add by fengdz TM:2012-03-23 ***/
      if (tmp_list.size() > 0) {
        right_type = ((Map) tmp_list.get(0)).get("right_type").toString();
      } else {
        throw new Exception("找不到该规则");
      }
      if (right_type.equals("0")) {
        return null;
      }
      select_sql = select_sql + "'" + ruleid + "',";
    }

    // 返回数据
    XMLData return_data = new XMLData();

    // 返回数据中预览数据列表
    List return_list = new ArrayList();

    // 返回数据中要素信息
    Map return_map = new HashMap();
    String rg_code = getRgCode();
    String set_year = getSetYear();
    // 参与统计的笛卡儿乘积中的要素项
    List allEleList = new ArrayList();
    allEleList = dao.findBySql("select distinct a.ele_code,0 as right_type,b.ele_name,b.ele_source "
      + " from sys_right_group_type" + Tools.addDbLink() + " a,sys_element" + Tools.addDbLink() + " b,sys_right_group"
      + Tools.addDbLink() + " c where a.right_type=1 and "
      + "  a.set_year=c.set_year  and a.rg_code=c.rg_code and a.rg_code = b.rg_code  and b.rg_code = c.rg_code and  "
      + " a.right_group_id =c.right_group_id and c.rule_id in (" + select_sql.substring(0, select_sql.length() - 1)
      + ") and a.ele_code=b.ele_code" + " and a.set_year= " + set_year + " and a.rg_code='" + rg_code + "'");

    // 构建返回数据中的要素信息
    for (int i = 0; i < allEleList.size(); i++) {
      String ele_code = ((Map) allEleList.get(i)).get("ele_code").toString();
      String ele_name = ((Map) allEleList.get(i)).get("ele_name").toString();
      String ele_source = ((Map) allEleList.get(i)).get("ele_source").toString();
      return_map.put(ele_code.toLowerCase() + "_id", ele_name);

      select_sql1 = select_sql1 + ele_code.toLowerCase() + "_id,";
      if (TypeOfDB.isOracle()) {
        select_sql2 = select_sql2 + "(select chr_code as " + ele_code.toLowerCase() + "_id" + " from " + ele_source
          + "" + Tools.addDbLink() + "),";
      } else {
        select_sql2 = select_sql2 + "(select chr_code as " + ele_code.toLowerCase() + "_id" + " from " + ele_source
          + "" + Tools.addDbLink() + ") as tab_" + i + " ,";
      }
    }

    // 构建预览数据
    for (int i = 0; i < ruleids.size(); i++) {
      XMLData rowData = (XMLData) ruleids.get(i);
      String ruleid = rowData.get("rule_id").toString();
      create_tmp_rule(ruleid, (ArrayList) allEleList, "");
    }

    if (TypeOfDB.isOracle()) {
      select_sql3 = select_sql2.substring(0, select_sql2.length() - 1) + ") minus " + "Select "
        + select_sql1.substring(0, select_sql1.length() - 1) + " from sys_RULE_CROSS_JOIN_cache" + Tools.addDbLink()
        + "";
    } else {
      select_sql3 = select_sql2.substring(0, select_sql2.length() - 1) + ") where ("
        + select_sql1.substring(0, select_sql1.length() - 1) + ") not in ( " + "Select "
        + select_sql1.substring(0, select_sql1.length() - 1) + " from sys_RULE_CROSS_JOIN_cache" + Tools.addDbLink()
        + ")";
    }
    return_list = dao.findBySql(select_sql3);

    return_data.put("return_map", return_map);
    return_data.put("return_list", return_list);
    return return_data;
  }

  /**
   * 得到规则DTO（界面DTO）
   * 
   * @return
   */
  public RuleDTO getRuleDto(String ruleid) throws Exception {
    /*** Add by fengdz TM:2012-03-23 ***/
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();
    /*** Add by fengdz TM:2012-03-23 ***/
    RuleDTO return_dto = new RuleDTO();
    // 查询规则
    List rule_list = dao.findBySqlByUpper("select * from sys_rule"
    /*** Add by fengdz TM:2012-03-23 ***/
    // + Tools.addDbLink() + " where rule_id='" + ruleid + "'");
      + Tools.addDbLink() + " where rule_id='" + ruleid + "' and rg_code='" + rg_code + "' and set_year=" + set_year);
    /*** Add by fengdz TM:2012-03-23 ***/
    if (rule_list.size() > 0) {
      BeanUtils.populate(return_dto, (Map) rule_list.get(0));

    }
    // 查询该规则对应的权限组
    List right_group_list = dao.findBySqlByUpper("select * from sys_right_group" + Tools.addDbLink()
      + " where rule_id='" + ruleid + "' " +

      /*** Add by fengdz TM:2012-03-23 ***/
      " and set_year=" + set_year + " and rg_code=" + "'" + rg_code + "'" +
      /*** Add by fengdz TM:2012-03-23 ***/

      "order by right_group_describe");
    for (int i = 0; i < right_group_list.size(); i++) {
      RightGroupDTO right_group_dto = new RightGroupDTO();
      BeanUtils.populate(right_group_dto, (Map) right_group_list.get(i));

      // 查询该权限组对应的TYPE
      String rightGroupId = ((Map) right_group_list.get(i)).get("RIGHT_GROUP_ID").toString();
      List type_list = dao.findBySqlByUpper("select a.*,b.ele_name from sys_right_group_type" + Tools.addDbLink()
        + " a,sys_element" + Tools.addDbLink() + " b where a.right_group_id='" + rightGroupId
        + "' and a.ele_code=b.ele_code" + " and a.set_year= " + set_year + " and a.rg_code='" + rg_code + "'"
        + " and b.set_year= " + set_year + " and b.rg_code='" + rg_code + "'");
      for (int j = 0; j < type_list.size(); j++) {
        RightGroupTypeDTO type_dto = new RightGroupTypeDTO();
        BeanUtils.populate(type_dto, (Map) type_list.get(j));
        right_group_dto.type_list.add(type_dto);
      }

      // 查询该权限组对应的DETAIL
      List detail_list = dao.findBySqlByUpper("select * from sys_right_group_detail" + Tools.addDbLink()
        + " where right_group_id='" + rightGroupId + "'" + " and set_year= " + set_year + " and rg_code='" + rg_code
        + "'"

      );
      for (int k = 0; k < detail_list.size(); k++) {
        RightGroupDetailDTO detail_dto = new RightGroupDetailDTO();
        BeanUtils.populate(detail_dto, (Map) detail_list.get(k));
        right_group_dto.detail_list.add(detail_dto);
      }

      return_dto.right_group_list.add(right_group_dto);
    }
    return return_dto;
  }

  /**
   * 通过角色ID得到规则ID
   * 
   * @return
   */
  public String getRuleIdByRoleId(String roleId) throws Exception {
    String return_str = "";
    /*
     * update daiwei 20070331 start 将原来通过角色ID从sys_role_rule表中查询规则ID的处理，
     * 改为通过用户ID和角色ID从SYS_USER_ROLE_RULE表中查询规则ID（rule_id）
     */
    // List rule_list = dao
    // .findBySql("select * from sys_role_rule"+Tools.addDbLink()+" where
    // role_id='"
    // + roleId + "'");
    List rule_list = dao.findBySql("select * from sys_user_role_rule" + Tools.addDbLink() + " where role_id='" + roleId
      + "' and user_id = '" + SessionUtil.getUserInfoContext().getUserID()
      /*** Add by fengdz TM:2012-03-23 ***/
      // .toString() + "'");
        .toString() + "' and  set_year=" + getSetYear() + " and rg_code='" + getRgCode() + "'");
    /*** Add by fengdz TM:2012-03-23 ***/
    /*
     * update daiwei 20070331 end
     */
    if (rule_list.size() > 0) {
      return_str = ((Map) rule_list.get(0)).get("rule_id").toString();
    }
    return return_str;
  }

  /**
   * 通过临时表构建规则的笛卡儿乘积 allEleList--参与计算的笛卡儿乘积项
   * 
   * @return
   */
  public void create_tmp_rule(String ruleid, ArrayList allEleList, String table_type) throws Exception {
    /*** Add by fengdz TM:2012-03-23 ***/
    String rg_code = getRgCode();
    String set_year = getSetYear();
    /*** Add by fengdz TM:2012-03-23 ***/
    // 得到该规则的所有权限组的信息
    List right_group_list = dao.findBySql("select * from " + "sys_right_group" + table_type + "" + Tools.addDbLink()
      + " where rule_id='" + ruleid + "'"
      + " and right_group_id = (select right_group_id from sys_right_group where rule_id='" + ruleid + "') "

      /*** Add by fengdz TM:2012-03-23 ***/
      + " and set_year= " + set_year + " and rg_code='" + rg_code + "'"

    );

    // 循环调用各权限组
    for (int i = 0; i < right_group_list.size(); i++) {
      String right_group_id = ((Map) right_group_list.get(i)).get("right_group_id").toString();
      String right_group_describe = ((Map) right_group_list.get(i)).get("right_group_describe").toString();

      // 得到该权限的TYPE信息以及ELE_SOURCE等信息
      List sub_type_list = dao.findBySql("select a.ele_code,a.right_type,b.ele_name,b.ele_source " + " from "
        + "sys_right_group_type" + table_type + "" + Tools.addDbLink() + " a,sys_element" + Tools.addDbLink()
        + " b where a.right_type=1 and " + " a.right_group_id ='" + right_group_id
        + "' and a.ele_code=b.ele_code and a.rg_code=b.rg_code and a.set_year= " + set_year + " and a.rg_code='"
        + rg_code + "'");
      // 该权限组的要素信息
      ArrayList tmpAllEleList = new ArrayList();

      // 将传入的要素项复制到临时要素项中
      for (int m = 0; m < allEleList.size(); m++) {
        XMLData tmp_xmldata = new XMLData();
        tmp_xmldata = (XMLData) ((XMLData) allEleList.get(m)).clone();
        tmpAllEleList.add(tmp_xmldata);
      }

      // 构建临时要素项列表，如果是本权限组有的要素，以该要素的权限类型为准，
      // 如果是本权限组没有的要素，则设为全部权限
      for (int k = 0; k < sub_type_list.size(); k++) {
        String subEleCode = ((Map) sub_type_list.get(k)).get("ele_code").toString();
        for (int l = 0; l < tmpAllEleList.size(); l++) {
          String parentEleCode = ((Map) tmpAllEleList.get(l)).get("ele_code").toString();
          if (parentEleCode.equals(subEleCode)) {
            XMLData tmp_map = (XMLData) tmpAllEleList.get(l);
            tmp_map.remove("right_type");
            tmp_map.put("right_type", "1");
            break;
          }
        }

      }

      // 如果是主权限或者是追加权限，最后要拼成类似如下语句
      /*
       * insert into tmp_CCID(en_id,bs_id) SELECT a1.chr_code,a2.ele_value
       * from (select * from ele_enterprise) a1, (select * from
       * sys_right_group_detail where right_group_id='' and ele_code='BS')
       * a2
       * 
       */
      if (right_group_describe.equals("001") || right_group_describe.equals("002")) {
        String str = " ";
        String str1 = " ";
        String str2 = " ";
        String str3 = " ";

        // 删除临时表内容
        dao.executeBySql("delete from sys_RULE_CROSS_JOIN_cache");

        // 根据该要素的启用要素循环调用
        for (int j = 0; j < tmpAllEleList.size(); j++) {
          String ele_code = ((Map) tmpAllEleList.get(j)).get("ele_code").toString();
          String right_type = ((Map) tmpAllEleList.get(j)).get("right_type").toString();
          String ele_source = ((Map) tmpAllEleList.get(j)).get("ele_source").toString();

          str1 = str1 + ele_code.toLowerCase() + "_id,";
          // 如果是全部权限，则需要从基础表中取数据
          if (right_type.equals("0")) {
            str2 = str2 + "a" + String.valueOf(j) + ".chr_code,";
            str3 = str3 + "(select * from " + ele_source + "" + Tools.addDbLink() + ") a" + String.valueOf(j) + ",";
          } else {
            // 如果是部分权限，则直接从权限明细表中取数据
            str2 = str2 + "a" + String.valueOf(j) + ".ele_value,";
            str3 = str3 + "(select * from "

            + "sys_right_group_detail" + table_type + "" + Tools.addDbLink() + "  where right_group_id='"
              + right_group_id

              + "' " +

              /*** Add by fengdz TM:2012-03-23 ***/
              " and set_year= " + set_year + " and rg_code='" + rg_code + "'" +

              /*** Add by fengdz TM:2012-03-23 ***/

              " and ele_code='" + ele_code + "') a" + String.valueOf(j) + ",";
          }

        }
        if (tmpAllEleList.size() > 0) {

          // 拼成往临时表中插入的语句
          str = "insert into sys_RULE_CROSS_JOIN_cache" + Tools.addDbLink() + "("
            + str1.substring(0, str1.length() - 1) + ") SELECT " + str2.substring(0, str2.length() - 1) + " from "
            + str3.substring(0, str3.length() - 1);
          dao.executeBySql(str);
        }
      }

      // 如果是主权限或者是追加权限，最后要拼成类似如下语句
      /*
       * delete from tmp_ccid b where 1=1 and exists(select 1 from
       * ele_enterprise a0 where a0.chr_code=b.en_id) and exists(select 1
       * from sys_right_group_detail a1 where a1.right_group_id='' and
       * a1.ele_code='BS' and a1.ele_value=b.bs_id) *
       */
      if (right_group_describe.equals("003")) {
        String str = "";
        String str2 = "";
        // 根据该要素的启用要素循环调用
        for (int j = 0; j < tmpAllEleList.size(); j++) {
          String ele_code = ((Map) tmpAllEleList.get(j)).get("ele_code").toString();
          String right_type = ((Map) tmpAllEleList.get(j)).get("right_type").toString();
          String ele_source = ((Map) tmpAllEleList.get(j)).get("ele_source").toString();
          // 如果是全部权限，则需要从基础表中取数据
          if (right_type.equals("0")) {
            str2 = str2 + "and exists(select 1 from " + ele_source + "" + Tools.addDbLink() + " a" + String.valueOf(j)
              + " where a" + String.valueOf(j) + ".chr_code=b." + ele_code.toLowerCase() + "_id)";
          } else {
            // 如果是部分权限，则直接从权限明细表中取数据
            str2 = str2 + "and exists(select 1 from " + "sys_right_group_detail" + table_type + "" + Tools.addDbLink()
              + " a" + String.valueOf(j) + " where a" + String.valueOf(j) + ".right_group_id='" + right_group_id

              + "'" +

              /*** Add by fengdz TM:2012-03-23 ***/
              " and set_year= " + set_year + " and rg_code='" + rg_code + "'" +

              /*** Add by fengdz TM:2012-03-23 ***/
              " and a" + String.valueOf(j) + ".ele_code='" + ele_code + "' and a" + String.valueOf(j) + ".ele_value=b."
              + ele_code.toLowerCase() + "_id)";
          }

        }
        // 拼成往临时表中删除的语句
        str = "delete from sys_RULE_CROSS_JOIN_cache" + Tools.addDbLink() + " b where 1=1" + str2;
        dao.executeBySql(str);
      }

    }
  }

  /**
   * 通过RuleDTO来得到预览数据
   * 
   * @return
   */
  public XMLData getDisplayDataByRuleData(RuleDTO ruleData) throws Exception {
    if (ruleData == null) {
      return getNullRightTypeData();
    }
    // 如果是全部权限
    if (ruleData.getRIGHT_TYPE() == 0) {
      return getAllRightTypeData();
    } else {
      // 如果是部分权限
      // 通过临时表构建预览数据
      saveRuleToTmp(ruleData);

      // 通过规则ID得到预览数据
      return getDisplayData(ruleData.getRULE_ID().toString(), "_cache");
    }

  }

  /**
   * 通过RuleId来得到预览数据
   * 
   * @return
   */
  public XMLData getDisplayData(String ruleid) throws Exception {
    return getDisplayData(ruleid, "");
  }

  /**
   * 得到全部权限的描述
   * 
   * @return
   */
  private XMLData getAllRightTypeData() {
    // 返回数据
    XMLData return_data = new XMLData();
    // 返回数据中预览数据列表
    List return_list = new ArrayList();
    // 返回数据中要素信息
    Map return_map = new HashMap();

    return_map.put("all", "权限描述");

    XMLData rowData = new XMLData();
    rowData.put("all", "全部权限");
    return_list.add(rowData);

    return_data.put("return_map", return_map);
    return_data.put("return_list", return_list);
    return return_data;
  }

  /**
   * 得到部分权限的描述
   * 
   * @return
   */
  private XMLData getPartRightTypeData() {
    // 返回数据
    XMLData return_data = new XMLData();
    // 返回数据中预览数据列表
    List return_list = new ArrayList();
    // 返回数据中要素信息
    Map return_map = new HashMap();

    return_map.put("all", "权限描述");

    XMLData rowData = new XMLData();
    rowData.put("all", "部分权限");
    return_list.add(rowData);

    return_data.put("return_map", return_map);
    return_data.put("return_list", return_list);
    return return_data;
  }

  /**
   * 得到没有权限的描述
   * 
   * @return
   */
  private XMLData getNullRightTypeData() {
    // 返回数据
    XMLData return_data = new XMLData();
    // 返回数据中预览数据列表
    List return_list = new ArrayList();
    // 返回数据中要素信息
    Map return_map = new HashMap();

    return_map.put("all", "权限描述");

    XMLData rowData = new XMLData();
    rowData.put("all", "没有权限");
    return_list.add(rowData);

    return_data.put("return_map", return_map);
    return_data.put("return_list", return_list);
    return return_data;
  }

  /**
   * 通过RoleId来得到预览数据
   * 
   * @return
   */
  public XMLData getDisplayDataByRoleId(String roleid) throws Exception {
    List tmp_list = dao.findBySql("select * from sys_role" + Tools.addDbLink() + " where role_id='" + roleid + "'");
    if (tmp_list.size() > 0) {
      String right_type = ((Map) tmp_list.get(0)).get("right_type").toString();
      // 角色未全部权限时
      if (right_type.equals("0")) {
        return getAllRightTypeData();
      }

      // 角色为部分权限时，通过规则查找
      if (right_type.equals("1")) {
        // 部分权限时，显示笛卡儿
        // String ruleid= this.getRuleIdByRoleId(roleid);
        // return getDisplayData(ruleid,"");

        // 部分权限时，显示简单描述
        return getPartRightTypeData();
      }

      // 角色为没有权限时
      if (right_type.equals("2")) {
        return getNullRightTypeData();
      }
    } else {
      throw new Exception("找不到该角色！");
    }

    return null;
  }

  /**
   * 通过RuleId来得到预览数据
   * 
   * @return
   */
  public XMLData getDisplayData(String ruleid, String table_type) throws Exception {
    List tmp_list = dao.findBySql("select *  " + " from " + "sys_rule" + table_type + "" + Tools.addDbLink()
      + " a where a.rule_id='"
      /*** Add by fengdz TM:2012-03-23 ***/
      // + ruleid + "'");
      + ruleid + "' and set_year=" + getSetYear() + " and rg_code='" + getRgCode() + "'");
    /*** Add by fengdz TM:2012-03-23 ***/
    if (tmp_list != null && tmp_list.size() > 0) {
      String right_type = ((Map) tmp_list.get(0)).get("right_type").toString();
      if (right_type.equals("0")) {
        return getAllRightTypeData();
      }
      if (right_type.equals("2")) {
        return getNullRightTypeData();
      }
    } else {
      return getNullRightTypeData();
    }

    // 返回数据
    XMLData return_data = new XMLData();

    // 返回数据中预览数据列表
    List return_list = new ArrayList();

    // 返回数据中要素信息
    Map return_map = new HashMap();
    /*** Add by fengdz TM:2012-03-23 ***/
    String rg_code = getRgCode();
    String set_year = getSetYear();
    /*** Add by fengdz TM:2012-03-23 ***/
    // 参与统计的笛卡儿乘积中的要素项
    List allEleList = new ArrayList();
    allEleList = dao.findBySql("select distinct a.ele_code,0 as right_type,b.ele_name,b.ele_source " + " from "
      + "sys_right_group_type" + table_type + "" + Tools.addDbLink() + " a,sys_element" + Tools.addDbLink() + " b,"
      + "sys_right_group" + table_type + "" + Tools.addDbLink() + " c where a.right_type=1 and "
      + " a.right_group_id =c.right_group_id and c.rule_id='" + ruleid + "' and a.ele_code=b.ele_code "

      /*** Add by fengdz TM:2012-03-23 ***/
      + " and a.set_year=c.set_year and a.rg_code=c.rg_code and a.set_year= " + set_year + " and  a.rg_code='"
      + rg_code + "'"

    /*** Add by fengdz TM:2012-03-23 ***/

    );

    // 构建返回数据中的要素信息
    for (int i = 0; i < allEleList.size(); i++) {
      String ele_code = ((Map) allEleList.get(i)).get("ele_code").toString();
      String ele_name = ((Map) allEleList.get(i)).get("ele_name").toString();
      return_map.put(ele_code.toLowerCase() + "_id", ele_name);
    }

    // 构建预览数据
    create_tmp_rule(ruleid, (ArrayList) allEleList, table_type);

    // 返回数据中预览数据列表
    // return_list = dao
    // .findBySql("select count(*) as num from
    // sys_RULE_CROSS_JOIN_cache"+Tools.addDbLink()+" ");
    // if
    // (Integer.valueOf((String)((Map)return_list.get(0)).get("num")).intValue()>500){
    //      
    // }

    String num = "100";
    num = (String) SessionUtil.getParaMap().get("SYS_RULE_DISP_NUM");
    if (num == null || num.equals("")) {
      num = "100";
    }
    if (TypeOfDB.isOracle()) {
      return_list = dao.findBySql("select distinct a.* from sys_RULE_CROSS_JOIN_cache" + Tools.addDbLink()
        + " a where rownum<=" + num + " order by a.mk_id");
    } else {
      return_list = dao.findBySql("select * from (select distinct a.* from sys_RULE_CROSS_JOIN_cache"
        + Tools.addDbLink() + " a where limit " + num + ") as t order by t.mk_id");
    }

    if (return_list.size() == 0) {
      return getAllRightTypeData();
    }
    return_data.put("return_map", return_map);
    return_data.put("return_list", return_list);
    return return_data;

  }

  private void saveRuleCross(String ruleId) throws Exception {

    Connection conn = null;
    CallableStatement call = null;
    Session session = null;
    try {
      session = dao.getSession();
      if (session == null) {
        throw new Exception("数据库连接已关闭,无法使用");
      }
      conn = session.connection();

      call = conn.prepareCall("{? =call create_rule_cross" + Tools.addDbLink() + "(?)}");
      call.registerOutParameter(1, Types.VARCHAR);
      call.setString(2, ruleId);
      call.execute();
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (call != null) {
          call.close();
          call = null;
        }
        if (session != null) {
          dao.closeSession(session);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  /*** Add by fengdz TM:2012-03-19 start ***/
  public String getRgCode() {
    String rg_code = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");

    return rg_code;
  }

  public String getSetYear() {
    String set_year = (String) SessionUtil.getUserInfoContext().getAttribute("set_year");

    return set_year;
  }

  /*** Add by fengdz TM:2012-03-19 end***/
  /*
   * update daiwei 20070331 start
   */
  /**
   * 判断规则是否做过高级设置
   */

  public boolean isAdvanced(String ruleid) throws Exception {
    StringBuffer sql = new StringBuffer();
    /*** Add by fengdz TM:2012-03-23 ***/
    String rg_code = getRgCode();
    String set_year = getSetYear();
    /*** Add by fengdz TM:2012-03-23 ***/
    sql.append("select a.* from sys_right_group a where a.right_group_describe in ('002','003') ").append(
      " and a.rule_id = ? and a.rg_code=? and a.set_year=? ");
    List list = dao.findBySql(sql.toString(), new Object[] { ruleid, rg_code, set_year });
    if (list == null || list.size() == 0) {
      return false;
    } else {
      return true;
    }
  }

  // 找到user_role_rule表是否已启用的该规则
  public boolean isUsedInUserRoleRule(String ruleid) throws Exception {
    /*** Modify by fengdz TM:2012-03-19 ***/
    // String sql = "select * from  sys_user_role_rule where rule_id = ?";
    // List list = dao.findBySql(sql, new Object[] { ruleid });

    String sql = "select * from  sys_user_role_rule where rule_id = ?  and set_year=? and rg_code=?";
    List list = dao.findBySql(sql, new Object[] { ruleid, getSetYear(), getRgCode() });

    /*** Modify by fengdz TM:2012-03-19 ***/

    if (list == null || list.size() == 0) {
      return false;
    } else {
      return true;
    }
  }

  // 找到user_rule表是否已启用的该规则
  public boolean isUsedInUserRule(String ruleid) throws Exception {
    /*** Modify by fengdz TM:2012-03-19 ***/
    // String sql = "select * from  sys_user_rule where rule_id = ?";
    // List list = dao.findBySql(sql, new Object[] { ruleid });
    String sql = "select * from  sys_user_rule where rule_id = ? and set_year=? and rg_code=? ";
    List list = dao.findBySql(sql, new Object[] { ruleid, getSetYear(), getRgCode() });

    /*** Modify by fengdz TM:2012-03-19 ***/
    if (list == null || list.size() == 0) {
      return false;
    } else {
      return true;
    }
  }

  // 得到所有已启用权限
  public List getAllEnabledRule() throws Exception {
    /*** Modify by fengdz TM:2012-03-19 ***/
    // String sql = "select * from sys_element where is_rightfilter=1";
    String sql = "select * from sys_element where is_rightfilter=1 and rg_code='" + getRgCode() + "' and set_year="
      + getSetYear();
    /*** Modify by fengdz TM:2012-03-19 ***/

    return dao.findBySql(sql);
  }

  // 判断该权限是否已存在
  public boolean isExist(String ruleCode) throws Exception {
    /*** Modify by fengdz TM:2012-03-19 ***/
    // String sql = "select * from sys_rule where rule_code = ?";
    // List list = dao.findBySql(sql, new Object[] { ruleCode });
    String sql = "select * from sys_rule where rule_code = ? and set_year=? and rg_code=? ";
    List list = dao.findBySql(sql, new Object[] { ruleCode, getSetYear(), getRgCode() });

    /*** Modify by fengdz TM:2012-03-19 ***/
    if (list == null || list.size() == 0) {
      return false;
    } else {
      return true;
    }

  }

  /*
   * update daiwei 20070331 end
   */

  /**
   * add by wanghongtao 查询表的所有字段，用于增加要素时，判断可用要素
   */
  public List getAllFieldByTableName(String tableName) throws Exception {
    tableName = tableName.toUpperCase();
    String sql = "select t.COLUMN_NAME from "
      + (TypeOfDB.isOracle() ? "user_tab_columns" : "information_schema.`COLUMNS`") + " t where t.TABLE_NAME=?";
    List list = dao.findBySql(sql, new Object[] { tableName });
    return list;
  }

  /**
   * 判断权限编码是否存在 
   * yanyga 2017-04-14
   * @param ruleCode
   * @return
   */
  public List checkRightCodeExist(String ruleCode) {
    String sql = "select * from sys_rule where rule_code = ? and set_year=? and rg_code=? ";
    List list = dao.findBySql(sql, new Object[] { ruleCode, getSetYear(), getRgCode() });
    return list;
  }

}
