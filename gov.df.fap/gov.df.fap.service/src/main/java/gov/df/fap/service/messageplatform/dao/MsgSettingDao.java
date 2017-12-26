package gov.df.fap.service.messageplatform.dao;

import gov.df.fap.bean.messageplatform.MsgContentFieldsDTO;
import gov.df.fap.bean.messageplatform.MsgRuleDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 
 * Title:消息平台 规则设置后台Dao
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * 
 * @author yanyga
 * @see 
 * @CreateDate 2017-8-2
 * @version 1.0
 */
@Service
public class MsgSettingDao {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  private static final String QUERY_SYS_USER_MAIN = " SELECT U.USER_ID AS ID,U.USER_CODE AS CODE,U.USER_NAME AS NAME FROM SYS_USERMANAGE U WHERE 1=1 ";

  /**
   * 通过code查找用户
   */
  private static final String QUERY_SYS_USER_BY_CODE = " AND U.USER_CODE LIKE ";

  /**
   * 通过name查找用户
   */
  private static final String QUERY_SYS_USER_BY_NAME = " AND U.USER_NAME LIKE ";

  /**
   * 从序列产生消息规则ID、消息接收人ID、模版字段ID
   */
  private static final String GET_MSG_RULE_ID = "SELECT "
    + (TypeOfDB.isOracle() ? "MSG_SEQ_RULE_ID.nextval FROM DUAL" : "nextval('MSG_SEQ_RULE_ID') as nextval");

  /**
   * 从序列产生消息用户群ID、用户与用户群关系ID
   */
  private static final String GET_MSG_USER_GROUP_ID = "SELECT "
    + (TypeOfDB.isOracle() ? "MSG_SEQ_RULE_ID.nextval FROM DUAL" : "nextval('MSG_SEQ_USER_GROUP_ID') as nextval");

  /**
   * 保存内容模板新添加的按钮
   */
  private static final String SAVE_MSG_CONTENT_FIELDS = "INSERT INTO MSG_RULE_CONTENT_FIELDS (ID, MSG_RULE_ID, IS_VALID, RG_CODE, SET_YEAR, KEY_NAME, VALUE_NAME, UPID) VALUES (?, ?, '1', ?, ?, ?, ?, 0) ";

  /**
   * 保存内容模板新添加的按钮
   */
  private static final String GET_MSG_CONTENT_FIELDS = "SELECT * FROM MSG_RULE_CONTENT_FIELDS WHERE MSG_RULE_ID = ? AND RG_CODE = ? AND SET_YEAR = ? ";

  /**
   * 删除内容模板单个按钮
   */
  private static final String DELETE_MSG_CONTENT_FIELD = "DELETE FROM MSG_RULE_CONTENT_FIELDS WHERE MSG_RULE_ID = ? AND KEY_NAME = ? AND RG_CODE = ? AND SET_YEAR = ? ";

  /**
   * 删除内容模板所有按钮
   */
  private static final String DELETE_ALL_MSG_CONTENT_FIELDS = "DELETE FROM MSG_RULE_CONTENT_FIELDS WHERE MSG_RULE_ID = ? AND RG_CODE = ? AND SET_YEAR = ? ";

  /**
   * 获取消息规则
   */
  private static final String GET_MSG_RULE_DATA = "SELECT * FROM MSG_RULES WHERE MSG_RULE_CODE = ? AND RG_CODE = ? AND SET_YEAR = ? ";

  /**
   * 获取消息规则
   */
  private static final String GET_MSG_RULE_DATA_BY_ID = "SELECT * FROM MSG_RULES WHERE ID = ? AND RG_CODE = ? AND SET_YEAR = ? ";

  /**
   * 获取消息规则(启用状态的规则)
   */
  private static final String GET_MSG_RULE_DATA_ENABLED = "SELECT * FROM MSG_RULES WHERE MSG_RULE_CODE = ? AND RG_CODE = ? AND SET_YEAR = ? AND ENABLED = 1 AND IS_VALID = 1 ";

  /**
   * 查询所有工作流
   */
  private static final String QUERY_FLOW_NAME = " SELECT WF_ID CHR_ID, WF_CODE CHR_CODE, WF_NAME CHR_NAME,"
    + SQLUtil.replaceLinkChar("WF_CODE||' '||WF_NAME")
    + " AS SHOW_NAME FROM SYS_WF_FLOWS F WHERE F.RG_CODE = ? AND F.SET_YEAR = ? AND EXISTS (SELECT 1 FROM SYS_WF_NODES N WHERE NODE_TYPE = '002' AND F.RG_CODE = N.RG_CODE AND F.SET_YEAR = N.SET_YEAR AND N.WF_ID = F.WF_ID) ORDER BY F.WF_CODE ";

  /**
   * 查询工作流节点
   */
  private static final String QUERY_NODE_NAME = "SELECT NODE_ID CHR_ID, NODE_CODE CHR_CODE, NODE_NAME CHR_NAME FROM SYS_WF_NODES WHERE WF_ID = ? AND NODE_TYPE = '002' AND RG_CODE = ? AND SET_YEAR = ? ORDER BY NODE_ID";

  /**
   * 获取用户Tree(可选接收人)
   */
  //  private static final String  GET_USER_TREE = "SELECT A.*, B.USER_ID,B.USER_CODE,B.USER_NAME FROM  SYS_USERTREE A, SYS_USERMANAGE B WHERE NOT EXISTS (SELECT R.USER_ID "
  //            + " FROM MSG_RULE_RECEIVER R WHERE R.USER_ID = A.CHR_ID AND R.MSG_RULE_ID = ? AND R.RG_CODE = ? AND R.SET_YEAR = ? AND R.IS_VALID = '1' AND R.IS_USER = '1') "
  //            + " AND A.CHR_ID = B.USER_ID(+) AND (A.RG_CODE IS NULL OR A.RG_CODE = ? ) AND (A.SET_YEAR IS NULL OR A.SET_YEAR = ? ) ORDER BY CHR_CODE";
  private static final String GET_CHOOSING_USER_TREE_BY_RULE_ID = " SELECT U.USER_ID AS ID, "
    + SQLUtil.replaceLinkChar("U.USER_CODE || ' ' || U.USER_NAME")
    + " AS NAME,'用户' AS TYPE FROM SYS_USERMANAGE U "
    + "WHERE NOT EXISTS (SELECT 1 FROM MSG_RULE_RECEIVER R WHERE R.USER_ID = U.USER_ID AND R.MSG_RULE_ID = ? AND R.RG_CODE = ? AND R.SET_YEAR = ? AND R.IS_VALID = '1' "
    + " AND R.IS_USER = '1') AND (U.SET_YEAR IS NULL OR U.SET_YEAR = ?) ORDER BY NAME";

  /**
   * 获取用户群Tree(可选接收人)
   */
  private static final String GET_CHOOSING_USER_GROUP_TREE_BY_RULE_ID = "SELECT G.ID, G.NAME, '用户群' AS TYPE FROM SYS_USER_GROUP G WHERE NOT EXISTS ( SELECT R.USER_GROUP_ID FROM MSG_RULE_RECEIVER R WHERE  R.USER_GROUP_ID = G.ID AND R.MSG_RULE_ID = ? AND R.RG_CODE = ? "
    + " AND R.SET_YEAR = ? AND R.IS_VALID = '1' AND R.IS_USER = '0' ) AND G.IS_VALID = '1' AND G.RG_CODE = ? AND G.SET_YEAR = ? ORDER BY NAME ";

  /**
   * 获取用户和用户群Tree(已选接收人)
   */
  private static final String GET_CHOOSED_USER_AND_GROUP_TREE_BY_RULE_ID = "SELECT TO_CHAR(G.ID) AS ID, G.NAME, '用户群' TYPE FROM SYS_USER_GROUP G WHERE EXISTS (SELECT R.USER_GROUP_ID "
    + " FROM MSG_RULE_RECEIVER R WHERE R.USER_GROUP_ID = G.ID AND R.MSG_RULE_ID = ? AND R.RG_CODE = ? AND R.SET_YEAR = ? AND R.IS_VALID = '1' AND R.IS_USER = '0') "
    + " UNION ALL SELECT U.USER_ID ID, "
    + SQLUtil.replaceLinkChar("U.USER_CODE || ' ' || U.USER_NAME")
    + " NAME, '用户' TYPE FROM SYS_USERMANAGE U WHERE EXISTS (SELECT R.USER_ID FROM MSG_RULE_RECEIVER R "
    + " WHERE R.USER_ID = U.USER_ID AND R.MSG_RULE_ID = ? AND R.RG_CODE = ? AND R.SET_YEAR = ? AND R.IS_VALID = '1' AND R.IS_USER = '1') ORDER BY TYPE, NAME ";

  //获得已选的接收人
  private static final String GET_CHOOSED_GROUP_BY_RULE_ID = "SELECT TO_CHAR(G.ID) AS ID, G.NAME, '用户群' TYPE FROM SYS_USER_GROUP G WHERE EXISTS (SELECT R.USER_GROUP_ID "
    + " FROM MSG_RULE_RECEIVER R WHERE R.USER_GROUP_ID = G.ID AND R.MSG_RULE_ID = ? AND R.RG_CODE = ? AND R.SET_YEAR = ? AND R.IS_VALID = '1' AND R.IS_USER = '0') ";

  //获得已选的接收人群
  private static final String GET_CHOOSED_USER_BY_RULE_ID = "SELECT U.USER_ID ID, "
    + SQLUtil.replaceLinkChar("U.USER_CODE || ' ' || U.USER_NAME")
    + " NAME, '用户' TYPE FROM SYS_USERMANAGE U WHERE EXISTS (SELECT R.USER_ID FROM MSG_RULE_RECEIVER R "
    + " WHERE R.USER_ID = U.USER_ID AND R.MSG_RULE_ID = ? AND R.RG_CODE = ? AND R.SET_YEAR = ? AND R.IS_VALID = '1' AND R.IS_USER = '1') ORDER BY TYPE, NAME ";

  /**
   * 获取用户Tree(可选接收人)
   */
  //  private static final String  GET_USER_TREE = "SELECT A.*, B.USER_ID,B.USER_CODE,B.USER_NAME FROM  SYS_USERTREE A, SYS_USERMANAGE B WHERE NOT EXISTS (SELECT R.USER_ID "
  //            + " FROM MSG_RULE_RECEIVER R WHERE R.USER_ID = A.CHR_ID AND R.MSG_RULE_ID = ? AND R.RG_CODE = ? AND R.SET_YEAR = ? AND R.IS_VALID = '1' AND R.IS_USER = '1') "
  //            + " AND A.CHR_ID = B.USER_ID(+) AND (A.RG_CODE IS NULL OR A.RG_CODE = ? ) AND (A.SET_YEAR IS NULL OR A.SET_YEAR = ? ) ORDER BY CHR_CODE";
  private static final StringBuffer GET_CHOOSING_USER_TREE_BY_CHOOSED_ID_A = new StringBuffer("SELECT U.USER_ID AS ID,"
    + SQLUtil.replaceLinkChar("U.USER_CODE||' '||U.USER_NAME")
    + " AS NAME, '用户' AS TYPE FROM SYS_USERMANAGE U,SYS_USERTREE A WHERE (1=1");

  private static final StringBuffer GET_CHOOSING_USER_TREE_BY_CHOOSED_ID_B = new StringBuffer(
    ") AND U.USER_ID NOT IN ( ");

  private static final StringBuffer GET_CHOOSING_USER_TREE_BY_CHOOSED_ID_C = new StringBuffer(
  //    ") AND U.USER_ID = A.CHR_ID AND U.IS_LEAF = '1' AND (A.RG_CODE IS NULL OR A.RG_CODE = ? ) AND (U.SET_YEAR IS NULL OR U.SET_YEAR = ? ) ORDER BY NAME");
    ") AND U.USER_ID = A.CHR_ID) AND (U.SET_YEAR IS NULL OR U.SET_YEAR = ? ) ORDER BY NAME");

  /**
   * 获取用户群Tree(可选接收人)
   */
  private static final StringBuffer GET_CHOOSING_USER_GROUP_TREE_BY_CHOOSED_ID_A = new StringBuffer(
    "SELECT G.ID, G.NAME, '用户群' AS TYPE FROM SYS_USER_GROUP G WHERE ( 1=1 ");

  private static final StringBuffer GET_CHOOSING_USER_GROUP_TREE_BY_CHOOSED_ID_B = new StringBuffer(
    " ) AND G.ID NOT IN ( ");

  private static final StringBuffer GET_CHOOSING_USER_GROUP_TREE_BY_CHOOSED_ID_C = new StringBuffer(
    " ) AND G.IS_VALID = '1' AND G.RG_CODE = ? AND G.SET_YEAR = ? ORDER BY NAME ");

  /**
   * 获取用户和用户群Tree(已选接收人)
   */
  private static final StringBuffer GET_CHOOSED_USER_AND_GROUP_TREE_BY_CHOOSED_ID_A = new StringBuffer(
    "SELECT U.USER_ID ID, " + SQLUtil.replaceLinkChar("U.USER_CODE || ' ' || U.USER_NAME")
      + " NAME, '用户' TYPE FROM SYS_USERMANAGE U WHERE U.USER_ID IN ( ");

  private static final StringBuffer GET_CHOOSED_USER_AND_GROUP_TREE_BY_CHOOSED_ID_B = new StringBuffer(
    " ) UNION ALL SELECT " + (TypeOfDB.isOracle() ? "TO_CHAR(G.ID)" : "CAST(G.ID AS CHAR)")
      + " AS ID, G.NAME, '用户群' TYPE FROM SYS_USER_GROUP G WHERE  G.ID IN ( ");

  private static final StringBuffer GET_CHOOSED_USER_AND_GROUP_TREE_BY_CHOOSED_ID_C = new StringBuffer(
    " ) ORDER BY TYPE, NAME");

  /**
   * 获取用户群Tree(所有)
   */
  private static final String GET_USER_GROUP_TREE = "SELECT G.* FROM SYS_USER_GROUP G WHERE G.IS_VALID = '1' AND G.RG_CODE = ? AND G.SET_YEAR = ? ";

  /**
   * 获取某个用户群下的用户Tree(可选用户)
   */
  private static final String GET_CHOOSING_USER_TREE_BY_GROUP_ID = "SELECT U.USER_ID AS ID, "
    + SQLUtil.replaceLinkChar("U.USER_CODE || ' ' || U.USER_NAME")
    + " AS NAME FROM SYS_USERMANAGE U WHERE NOT EXISTS (SELECT R.USER_ID "
    + " FROM SYS_USER_GROUP_RELATION R WHERE R.USER_ID = U.USER_ID AND R.USER_GROUP_ID = ? AND R.RG_CODE = ? AND R.SET_YEAR = ? AND R.IS_VALID = '1') "
    //    + " AND U.USER_ID = A.CHR_ID AND U.IS_LEAF = '1' AND (A.RG_CODE IS NULL OR A.RG_CODE = ?) AND (U.SET_YEAR IS NULL OR U.SET_YEAR = ?) ORDER BY NAME ";
    + " AND (U.SET_YEAR IS NULL OR U.SET_YEAR = ?) ORDER BY NAME ";

  /**
   * 获取用户
   */
  //  private static final String GET_USER_TREE = "SELECT U.USER_ID AS ID, U.USER_CODE || ' ' || U.USER_NAME AS NAME FROM SYS_USERMANAGE U, SYS_USERTREE A WHERE U.USER_ID = A.CHR_ID AND U.IS_LEAF = '1' AND (A.RG_CODE IS NULL OR A.RG_CODE = ?) AND (U.SET_YEAR IS NULL OR U.SET_YEAR = ?) ORDER BY NAME";
  private static final String GET_USER_TREE = "SELECT U.USER_ID AS ID, "
    + SQLUtil.replaceLinkChar("U.USER_CODE || ' ' || U.USER_NAME")
    + " AS NAME FROM SYS_USERMANAGE U, SYS_USERTREE A WHERE U.USER_ID = A.CHR_ID ) AND (U.SET_YEAR IS NULL OR U.SET_YEAR = ?) ORDER BY NAME";

  /**
   * 获取某个用户群下的用户Tree(已选用户)
   */
  private static final String GET_CHOOSED_USER_TREE_BY_GROUP_ID = "SELECT U.USER_ID ID, "
    + SQLUtil.replaceLinkChar("U.USER_CODE || ' ' || U.USER_NAME")
    + " NAME FROM SYS_USERMANAGE U, SYS_USER_GROUP_RELATION R WHERE R.USER_GROUP_ID = ? "
    + " AND U.USER_ID = R.USER_ID AND R.IS_VALID = '1' AND R.RG_CODE = ? AND R.SET_YEAR = ? ORDER BY NAME ";

  /**
   * 保存用户群所选用户
   */
  //  private static final String SAVE_USER_GROUP_RELATION = "INSERT INTO SYS_USER_GROUP_RELATION (ID, IS_VALID, RG_CODE, SET_YEAR, USER_ID, USER_GROUP_ID, LAST_VER, UPID) VALUES (?, '1', ?, ?, ?, ?, SYSDATE, '0') ";

  /**
   * 查询用户群用户
   */
  private static final String GET_USER_ID_FROM_GROUP_REALTION = "SELECT USER_ID FROM SYS_USER_GROUP_RELATION WHERE USER_GROUP_ID = ? AND RG_CODE = ? AND SET_YEAR = ? ";

  /**
   * 保存用户群
   */
  private static final String SAVE_USER_GROUP = "INSERT INTO SYS_USER_GROUP (ID, IS_VALID, RG_CODE, SET_YEAR, NAME, LAST_VER, UPID) VALUES (?, '1', ?, ?, ?, "
    + (TypeOfDB.isOracle() ? "SYSDATE" : "SYSDATE()") + ", '0')";

  /**
   * 更新用户群
   */
  private static final String UPDATE_USER_GROUP = "UPDATE SYS_USER_GROUP SET NAME = ?, UPID = UPID+1, LAST_VER = SYSDATE WHERE ID = ? ";

  private static final String DELETE_USER_GROUP = "DELETE SYS_USER_GROUP WHERE ID = ? AND RG_CODE = ? AND SET_YEAR = ?";

  private static final String DELETE_USER_GROUP_RELATION = "DELETE SYS_USER_GROUP_RELATION WHERE USER_GROUP_ID = ? AND RG_CODE = ? AND SET_YEAR = ?";

  private static final String DELETE_MSG_RULE_RECEIVER = "DELETE MSG_RULE_RECEIVER WHERE MSG_RULE_ID = ? AND RG_CODE = ? AND SET_YEAR = ?";

  //  private static final String SAVE_MSG_RULE_RECEIVER = "INSERT INTO MSG_RULE_RECEIVER (ID, MSG_RULE_ID, IS_VALID, RG_CODE, SET_YEAR, IS_USER, USER_ID, USER_GROUP_ID, UPID) VALUES (?, ?, '1', ?, ?, ?, ?, NULL, '0')";

  private static final String SAVE_MSG_RULE = "INSERT INTO MSG_RULES (ID, IS_VALID, RG_CODE, SET_YEAR, MSG_RULE_CODE, MSG_RULE_NAME, INVOKETYPE, "
    + "WF_FLOW_ID, WF_FLOW_CODE, WF_FLOW_NAME, WF_NODE_ID, WF_NODE_CODE, WF_NODE_NAME, WF_ACTION_CODE, WF_ACTION_NAME, WF_CONDITION, "
    + "SEND_TYPE, ENABLED, CONTENT_MODEL, LAST_VER, UPID, CONTENT_TITLE, WF_ACTION_ID) VALUES (?, '1', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
    + (TypeOfDB.isOracle() ? "SYSDATE" : "SYSDATE()") + ", '0', ?, ?) ";

  private static final String UPDATE_MSG_RULE = "UPDATE MSG_RULES SET MSG_RULE_CODE = ?, MSG_RULE_NAME = ?, INVOKETYPE = ?, WF_FLOW_ID = ?, "
    + "WF_FLOW_CODE = ?, WF_FLOW_NAME = ?, WF_NODE_ID = ?, WF_NODE_CODE = ?, WF_NODE_NAME = ?, WF_ACTION_CODE = ?, WF_ACTION_NAME = ?, WF_CONDITION = ?, "
    + "SEND_TYPE = ?, ENABLED = ?, CONTENT_MODEL = ?, LAST_VER = SYSDATE, UPID = UPID+1, CONTENT_TITLE = ?, WF_ACTION_ID = ? WHERE ID = ? AND RG_CODE = ? AND SET_YEAR = ? ";

  /**
   * 获取所有规则
   */
  private static final String GET_ALL_MSG_RULES = "SELECT R.MSG_RULE_CODE CODE, "
    + SQLUtil.replaceLinkChar("R.MSG_RULE_CODE||' '||R.MSG_RULE_NAME")
    + " NAME, R.ID FROM MSG_RULES R WHERE R.IS_VALID = '1' AND R.RG_CODE = ? AND R.SET_YEAR = ? ORDER BY NAME";

  /**
   * 删除消息规则
   */
  private static final String DELETE_MSG_RULE = "DELETE MSG_RULES WHERE ID = ? AND RG_CODE = ? AND SET_YEAR = ? ";

  /**
    * 根据工作流节点和操作类型查找规则
    */
  private static final String GET_MSG_RULES_BY_WORKFLOW_INFO = " select * FROM msg_rules WHERE ENABLED = 1 AND is_valid = 1 AND invoketype = 1 AND wf_node_id = ? AND wf_action_code = ? AND RG_CODE = ? AND SET_YEAR = ? ";

  /**
   * 得到规则机构映射
   */
  private static final String GET_RULE_TO_ORG = " select * FROM msg_rule_en WHERE RG_CODE = ? AND SET_YEAR = ? ";

  /**
   * 根据单据编码获取单据类型名称
   */
  private static final String GET_BILL_TYPE_NAME_BY_CODE = " select billtype_name FROM sys_billtype WHERE ENABLED = 1 AND billtype_code = ? AND rg_code = ? AND set_year = ? ";

  public List querySysUserByUsercodeOrName(String u_code, String u_name) {
    return generalDAO.findBySql(QUERY_SYS_USER_MAIN + QUERY_SYS_USER_BY_CODE + "'%" + u_code + "%'"
      + QUERY_SYS_USER_BY_NAME + "'%" + u_name + "%'");
  }

  public List querySysUserBycode(String u_code) {
    return generalDAO.findBySql(QUERY_SYS_USER_MAIN + QUERY_SYS_USER_BY_CODE + "'%" + u_code + "%'");
  }

  public List querySysUserByName(String u_name) {
    return generalDAO.findBySql(QUERY_SYS_USER_MAIN + QUERY_SYS_USER_BY_NAME + "'%" + u_name + "%'");
  }

  /**
   * 从序列产生消息规则ID、消息接收人ID、模版字段ID
   * @return
   */
  public String generateMsgRuleIdBySeq() {
    Map map = (Map) generalDAO.findBySql(GET_MSG_RULE_ID).get(0);
    return (String) map.get("nextval");
  }

  /**
   * 从序列产生消息用户群ID、用户与用户群关系ID
   * @return
   */
  public String generateMsgUserGroupIdBySeq() {
    Map map = (Map) generalDAO.findBySql(GET_MSG_USER_GROUP_ID).get(0);
    return (String) map.get("nextval");
  }

  /**
   * 保存内容模板新添加的按钮
   * @param MsgContentFieldsDTO
   * @return
   */
  public void saveMsgContentFields(MsgContentFieldsDTO dto) {
    generalDAO.executeBySql(
      SAVE_MSG_CONTENT_FIELDS,
      new Object[] { dto.getId(), dto.getMsgRuleId(), SessionUtil.getRgCode(), SessionUtil.getLoginYear(),
        dto.getKeyName(), dto.getValueName() });
  }

  /**
   * 删除内容模板按钮
   * @param id
   */
  public void deleteMsgContentField(String msgRuleId, String key_name) {
    generalDAO.executeBySql(DELETE_MSG_CONTENT_FIELD, new Object[] { msgRuleId, key_name, SessionUtil.getRgCode(),
      SessionUtil.getLoginYear() });
  }

  /**
   * 删除内容模板所有按钮
   * @param msgRuleId 消息规则Id
   */
  public void deleteAllMsgContentFields(String msgRuleId) {
    generalDAO.executeBySql(DELETE_ALL_MSG_CONTENT_FIELDS, new Object[] { msgRuleId, SessionUtil.getRgCode(),
      SessionUtil.getLoginYear() });
  }

  /**
   * 获取消息规则
   * @param msgRuleCode
   */
  public List getMsgRuleData(String msgRuleCode) {
    return generalDAO.findBySql(GET_MSG_RULE_DATA,
      new Object[] { msgRuleCode, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * （通过ID）获取消息规则
   * @param msgRuleCode
   */
  public List getMsgRuleDataByMsgID(String msgID) {
    return generalDAO.findBySql(GET_MSG_RULE_DATA_BY_ID,
      new Object[] { msgID, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 获取消息规则(启用状态的规则)
   * @param msgRuleCode
   */
  public List getMsgRuleDataEnabled(String msgRuleCode) {
    return generalDAO.findBySql(GET_MSG_RULE_DATA_ENABLED, new Object[] { msgRuleCode, SessionUtil.getRgCode(),
      SessionUtil.getLoginYear() });
  }

  /**
   * 获取内容模板按钮字段
   * @param msgRuleId
   * @return
   */
  public List getMsgContentFields(String msgRuleId) {
    return generalDAO.findBySql(GET_MSG_CONTENT_FIELDS,
      new Object[] { msgRuleId, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 查询所有工作流
   * @return ID,NAME,Code
   */
  public List queryFlowName() {
    return generalDAO.findBySql(QUERY_FLOW_NAME, new Object[] { SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 查询工作流节点
   * @param flowId
   * @return ID,NAME,Code
   */
  public List queryNodeName(String flowId) {
    return generalDAO.findBySql(QUERY_NODE_NAME,
      new Object[] { flowId, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 获取用户Tree(可选接收人)
   * @param msgRuleId
   */
  public List getChoosingUserTreeByRuleId(String msgRuleId) {
    return generalDAO.findBySql(GET_CHOOSING_USER_TREE_BY_RULE_ID, new Object[] { msgRuleId, SessionUtil.getRgCode(),
      SessionUtil.getLoginYear(), SessionUtil.getLoginYear() });

  }

  /**
   * 获取用户群Tree(可选接收人)
   * @param msgRuleId
   */
  public List getChoosingUserGroupTreeByRuleId(String msgRuleId) {
    return generalDAO.findBySql(GET_CHOOSING_USER_GROUP_TREE_BY_RULE_ID,
      new Object[] { msgRuleId, SessionUtil.getRgCode(), SessionUtil.getLoginYear(), SessionUtil.getRgCode(),
        SessionUtil.getLoginYear() });
  }

  /**
   * 获取用户和用户群Tree(已选接收人)
   * @param msgRuleId
   */
  public List getChoosedUserAndGroupTreeByRuleId(String msgRuleId) {
    return generalDAO.findBySql(GET_CHOOSED_USER_AND_GROUP_TREE_BY_RULE_ID,
      new Object[] { msgRuleId, SessionUtil.getRgCode(), SessionUtil.getLoginYear(), msgRuleId,
        SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 根据规则ID找出用户已选择的接收人
   * @param msgRuleId
   * @return
   */
  public List getChoosedUserByRuleId(String msgRuleId) {
    return generalDAO.findBySql(GET_CHOOSED_USER_BY_RULE_ID, new Object[] { msgRuleId, SessionUtil.getRgCode(),
      SessionUtil.getLoginYear() });
  }

  /**
   * 根据规则ID找出用户已选择的接收人群
   * @param msgRuleId
   * @return
   */
  public List getChoosedGroupTreeByRuleId(String msgRuleId) {
    return generalDAO.findBySql(GET_CHOOSED_GROUP_BY_RULE_ID, new Object[] { msgRuleId, SessionUtil.getRgCode(),
      SessionUtil.getLoginYear() });
  }

  /**
   * 获取用户Tree(可选接收人)
   * @param choosedUserId
   */
  public List getChoosingUserTreeByChoosedId(String choosedUserId) {
    StringBuffer sb = new StringBuffer("");
    if (choosedUserId == null || "".equals(choosedUserId.trim())) {
      sb.append(GET_CHOOSING_USER_TREE_BY_CHOOSED_ID_A).append(GET_CHOOSING_USER_TREE_BY_CHOOSED_ID_C);
    } else {
      sb.append(GET_CHOOSING_USER_TREE_BY_CHOOSED_ID_A).append(GET_CHOOSING_USER_TREE_BY_CHOOSED_ID_B)
        .append(choosedUserId).append(GET_CHOOSING_USER_TREE_BY_CHOOSED_ID_C);
    }
    return generalDAO.findBySql(sb.toString(), new Object[] { SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 获取用户群Tree(可选接收人)
   * @param choosedUserId
   */
  public List getChoosingUserGroupTreeByChoosedId(String choosedUserGroupId) {
    StringBuffer sb = new StringBuffer("");
    if (choosedUserGroupId == null || "".equals(choosedUserGroupId.trim())) {
      sb.append(GET_CHOOSING_USER_GROUP_TREE_BY_CHOOSED_ID_A).append(GET_CHOOSING_USER_GROUP_TREE_BY_CHOOSED_ID_C);
    } else {
      sb.append(GET_CHOOSING_USER_GROUP_TREE_BY_CHOOSED_ID_A).append(GET_CHOOSING_USER_GROUP_TREE_BY_CHOOSED_ID_B)
        .append(choosedUserGroupId).append(GET_CHOOSING_USER_GROUP_TREE_BY_CHOOSED_ID_C);
    }
    return generalDAO.findBySql(sb.toString(), new Object[] { SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 获取用户和用户群Tree(已选接收人)
   * @param choosedUserId
   * @param choosedUserGroupId
   */
  public List getChoosedUserAndGroupTreeByChoosedId(String choosedUserId, String choosedUserGroupId) {
    StringBuffer sb = new StringBuffer("");
    if (choosedUserId == null || "".equals(choosedUserId.trim())) {
      choosedUserId = "''";
    }
    if (choosedUserGroupId == null || "".equals(choosedUserGroupId.trim())) {
      choosedUserGroupId = "''";
    }
    sb.append(GET_CHOOSED_USER_AND_GROUP_TREE_BY_CHOOSED_ID_A).append(choosedUserId)
      .append(GET_CHOOSED_USER_AND_GROUP_TREE_BY_CHOOSED_ID_B).append(choosedUserGroupId)
      .append(GET_CHOOSED_USER_AND_GROUP_TREE_BY_CHOOSED_ID_C);
    return generalDAO.findBySql(sb.toString());
  }

  /**
   * 获取用户群Tree(所有)
   */
  public List getUserGroupTree() {
    return generalDAO.findBySql(GET_USER_GROUP_TREE,
      new Object[] { SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 获取某个用户群下的用户Tree(可选用户)
   * @param userGroupId
   */
  public List getChoosingUserTreeByGroupId(String userGroupId) {
    return generalDAO.findBySql(GET_CHOOSING_USER_TREE_BY_GROUP_ID, new Object[] { userGroupId,
      SessionUtil.getRgCode(), SessionUtil.getLoginYear(), SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 获取某个用户群下的用户Tree(已选用户)
   * @param userGroupId
   */
  public List getChoosedUserTreeByGroupId(String userGroupId) {
    return generalDAO.findBySql(GET_CHOOSED_USER_TREE_BY_GROUP_ID, new Object[] { userGroupId, SessionUtil.getRgCode(),
      SessionUtil.getLoginYear() });
  }

  /**
   * 获取所有用户Tree
   * @param userGroupId
   */
  public List getUserTree() {
    return generalDAO.findBySql(GET_USER_TREE, new Object[] { SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 保存用户群
   * @param userGroupId
   * @param name
   */
  public void saveUserGroup(String name, String userGroupId) {
    generalDAO.executeBySql(SAVE_USER_GROUP,
      new Object[] { userGroupId, SessionUtil.getRgCode(), SessionUtil.getLoginYear(), name });
  }

  /**
   * 更新用户群
   * @param userGroupId
   * @param name
   */
  public void updateUserGroup(String name, String userGroupId) {
    generalDAO.executeBySql(UPDATE_USER_GROUP, new Object[] { name, userGroupId });
  }

  /**
   * 批量保存用户群所选用户
   * @param tableName
   * @param fields
   * @param dtoOrMapList
   */
  public void batchSaveUserGroupRelation(String tableName, String[] fields, List dtoOrMapList) {
    //    generalDAO.executeBySql(SAVE_USER_GROUP_RELATION,
    //      new Object[] { id, SessionUtil.getRgCode(), SessionUtil.getLoginYear(), userId, userGroupId});
    generalDAO.batchSaveDataBySql(tableName, fields, dtoOrMapList);
  }

  /**
   * 获取用户群所有用户的userId
   * @param userGroupId
   */
  public List getUserIdFromUserGroupRelatioin(String userGroupId) {
    return generalDAO.findBySql(GET_USER_ID_FROM_GROUP_REALTION, new Object[] { userGroupId, SessionUtil.getRgCode(),
      SessionUtil.getLoginYear() });
  }

  /**
   * 删除用户群
   * @param userGroupId
   */
  public void deleteUserGroup(String userGroupId) {
    generalDAO.executeBySql(DELETE_USER_GROUP,
      new Object[] { userGroupId, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });

  }

  /**
   * 删除用户群表
   * @param userGroupId
   */
  public void deleteUserGroupRelation(String userGroupId) {
    generalDAO.executeBySql(DELETE_USER_GROUP_RELATION, new Object[] { userGroupId, SessionUtil.getRgCode(),
      SessionUtil.getLoginYear() });
  }

  /**
   * 保存消息规则
   * @param dto
   */
  public void saveMsgRule(MsgRuleDTO dto) {
    generalDAO.executeBySql(
      SAVE_MSG_RULE,
      new Object[] { dto.getId(), SessionUtil.getRgCode(), SessionUtil.getLoginYear(), dto.getMsg_rule_code(),
        dto.getMsg_rule_name(), dto.getInvoketype(), dto.getWf_flow_id(), dto.getWf_flow_code(), dto.getWf_flow_name(),
        dto.getWf_node_id(), dto.getWf_node_code(), dto.getWf_node_name(), dto.getWf_action_code(),
        dto.getWf_action_name(), dto.getWf_condition(), dto.getSend_type(), dto.getEnabled(), dto.getContent_model(),
        dto.getContent_title(), dto.getWf_action_id() });
  }

  /**
   * 删除消息规则
   * @param dto
   */
  public void updateMsgRule(MsgRuleDTO dto) {
    generalDAO.executeBySql(
      UPDATE_MSG_RULE,
      new Object[] { dto.getMsg_rule_code(), dto.getMsg_rule_name(), dto.getInvoketype(), dto.getWf_flow_id(),
        dto.getWf_flow_code(), dto.getWf_flow_name(), dto.getWf_node_id(), dto.getWf_node_code(),
        dto.getWf_node_name(), dto.getWf_action_code(), dto.getWf_action_name(), dto.getWf_condition(),
        dto.getSend_type(), dto.getEnabled(), dto.getContent_model(), dto.getContent_title(), dto.getWf_action_id(),
        dto.getId(), SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 删除消息接收人
   * @param msgRuleId
   */
  public void deleteMsgRuleReceiver(String msgRuleId) {
    generalDAO.executeBySql(DELETE_MSG_RULE_RECEIVER,
      new Object[] { msgRuleId, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 保存或更新消息接收人
   * @param msgRuleId
   * @param isUser
   * @param id
   */
  public void batchSaveMsgRuleReceiver(String tableName, String[] fields, List dtoOrMapList) {
    generalDAO.batchSaveDataBySql(tableName, fields, dtoOrMapList);
  }

  /**
   * 获取所有规则
   */
  public List getAllMsgRules() {
    return generalDAO
      .findBySql(GET_ALL_MSG_RULES, new Object[] { SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 删除消息规则
   * @param msgRuleId
   */
  public void deleteMsgRule(String msgRuleId) {
    generalDAO.executeBySql(DELETE_MSG_RULE,
      new Object[] { msgRuleId, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 根据工作流节点和操作类型查找规则
   * @param currentNodeId
   * @param actionType
   * @return
   */
  public List getMsgRulesByWorkFlowInfo(Long currentNodeId, String actionType) {
    return generalDAO.findBySql(GET_MSG_RULES_BY_WORKFLOW_INFO,
      new Object[] { currentNodeId, actionType, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 得到规则和机构映射关系
   * @return
   */
  public List getRuleToOrg() {
    return generalDAO.findBySql(GET_RULE_TO_ORG, new Object[] { SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 根据单据类型编码获取单据类型名称
   * @param billtypeCode
   * @return
   */
  public String getBilltypeNameByCode(String billtypeCode) {
    List billtypeList = generalDAO.findBySql(GET_BILL_TYPE_NAME_BY_CODE,
      new Object[] { billtypeCode, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });

    String rtnStr = null;

    if (billtypeList != null && billtypeList.size() > 0) {
      XMLData billtypeData = (XMLData) billtypeList.get(0);
      rtnStr = (String) billtypeData.get("billtype_name");
    }

    return rtnStr;
  }
}
