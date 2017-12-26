package gov.df.fap.api.workflow;

import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.util.exception.FAppException;
import gov.df.fap.util.xml.XMLData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: 工作流帮助类接口
 * </p>
 * <p>
 * Description:工作流组件接口
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005-2008 
 * </p>
 * <p>
 * Company:用友政务软件有限公司
 * </p>
 * 
 * @version 1.0
 * @author justin
 * @since java 1.6.34
 */
public interface IWorkFlowHelper {
  /**
   * 得到传入数据是否需要记帐标志。
   * 
   * @param moduleid-----------功能ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDtos--业务数据DTO列表
   * @return String
   * @throws Exception---------错误信息
   */
  public String getTollyFlag(String old_current_node_id, String moduleid, String roleid, String actiontype,
    String bill_table_name, String detail_table_name, FVoucherDTO inputFVoucherDto, XMLData tmpCanGoData)
    throws FAppException;

  /**
   * 批量调用交易令的录入接口。
   * 
   * @param saveFVoucherDtos
   *            单据newDTO
   * @return FVoucherDTO
   * @throws Exception
   *             错误信息
   * @author ymj 
   */
  public List doBusVouSaveBatch(List saveFVoucherDtos) throws FAppException;

  /**
   * 批量调用交易令的更新接口。
   * 
   * @param newFVoucherDtos
   *            单据newDTO
   * @return FVoucherDTO
   * @throws Exception
   *             错误信息
   * @author ymj
   */
  public List doBusVouUpdateBatch(List newFVoucherDtos) throws FAppException;

  /**
   * 批量调用交易令的终审接口。
   * 
   * @param auditFVoucherDtos
   *            单据newDTO
   * @throws Exception
   *             错误信息
   * @author ymj
   */
  public List doBusVouAuditBatch(List auditFVoucherDtos) throws FAppException;

  /**
   * 批量调用交易令的删除接口。
   * 
   * @param deleteFVoucherDtos
   *            单据DTO
   * @throws Exception
   *             错误信息
   * @author ymj
   */
  public List doBusVouDeleteBatch(List deleteFVoucherDtos) throws FAppException;

  /**
   * 批量调用交易令的作废接口。
   * 
   * @param invalidaFVoucherDtos
   *            单据DTO
   * @throws Exception
   *             错误信息
   * @author ymj
   */
  public List doBusVouInvalidBatch(List invalidateFVoucherDtos) throws FAppException;

  /**
   * 批量调用交易令的撤销终审接口
   * 
   * @param cancelAuditFVoucherDtos
   *            单据newDTO
   * @throws Exception
   *             错误信息
   * @author 黄节 2008年4月29日修改
   */
  public List doBusVouCancelAuditBatch(List cancelAuditFVoucherDtos) throws FAppException;

  /**
   * 得到监控规则表
   * 
   * @param old_current_node_id
   * @param moduleid
   * @param roleid
   * @param actiontype
   * @param bill_table_name
   * @param detail_table_name
   * @param inputFVoucherDto
   * @param isBillDetail 是否为单加明细
   * @return
   * @throws Exception
   */
  public List getInspectRules(String old_current_node_id, String moduleid, String roleid, String actiontype,
    String bill_table_name, String detail_table_name, FVoucherDTO inputFVoucherDto, boolean isBillDetail,
    XMLData tmpCanGoData) throws FAppException;

  /**
   * 
   * @param old_current_node_id
   * @param moduleid
   * @param roleid
   * @param actiontype
   * @param inputFVoucherDto
   * @param isForced2Execute
   * @return
   * @throws Exception
   */
  public String getNextNodeId(String old_current_node_id, String moduleid, String roleid, String actiontype,
    FVoucherDTO inputFVoucherDto, boolean isForced2Execute) throws FAppException;

  /**
   * 根据节点找到流程
   * 
   * @param nodeId
   * @return
   * @throws Exception
   */
  public String getWfIdByNodeId(String nodeId) throws FAppException;

  /**
   * 从sequence SEQ_SYS_WF_TASK_ID 取得下一个TaskId
   * 
   * @return
   */
  public String getNextTaskIdBySequence();

  /**
   * 获得发送消息和自动审核时间
   * 
   * @param nodeId
   * @return
   * @throws Exception
   */
  public Map getNoticeDateMap(String nodeId) throws FAppException;

  /**
   * 获取节点信息
   */
  public List getListForSql(String sql);

  /**
   * 
   * 功能： 删除两个临时表内上次任务。
   * 
   * <br>
   * Date ：2008-1-31
   * 
   * @param currentNodeId
   *            当前节点
   * @param entityId
   *            当前实体对象
   * @return 删除行数
   * 
   * @author bing-zeng
   * @throws SQLException
   * @since 1.0
   * 
   */
  public void delete4CurrentOrCompleteItems(Statement setRoutingPsmt, String gatherSql, String taskId,
    String currentNodeId, String nextNodeId, String entityId, String action_code) throws FAppException, SQLException;

  /**
   * 
   * 功能： 添加新任务到临时表内
   * 
   * <br>
   * Date ：2008-1-31
   * 
   * @param entityId
   *            实体ID
   * @param bill_id
   * @param nodeId
   *            当前节点ID
   * @param statusCode
   *            操作状态
   * @param rcid
   * @param ccid
   * 
   * @author bing-zeng
   * @throws SQLException
   * @since 1.0
   * 
   */
  public void add4CurrentOrCompleteItems(Statement setRoutingPsmt, String entityId, String bill_id, String nodeId,
    String statusCode, String rcid, String ccid) throws FAppException, SQLException;

  /**
   * 录入时根据权限找流程和节点。
   * 
   * @param moduleid-----------功能ID
   * @param roleid-------------角色ID
   * @param table_name---------单表名
   * @return
   * @throws Exception---------错误信息
   */
  public List doSingleProcessSimplyInputDetailSelect(String moduleid, String roleid, String tableName, XMLData rowData,
    XMLData tmpCanGoData) throws FAppException;

  /**
   * 
   * 功能：RECALL 专用删除任务ITEM方法
   * 
   * <br>
   * Date ：2008-2-19
   * 
   * @param setRoutingPsmt
   *            Statement
   * @param taskId
   *            工作流ID
   * @param currentNodeId
   *            当前节点ID
   * @param nextNodeId
   *            下一个节点ID
   * @param entityId
   *            实体ID
   * @throws FAppException
   * @throws SQLException
   * 
   * @author 
   * @since 1.0
   * 
   */
  public void delete4CurrentOrCompleteItems4Recall(Statement setRoutingPsmt, String taskId, String currentNodeId,
    String nextNodeId, String entityId) throws FAppException, SQLException;

  /**
   * 
   * 功能： RECALL 专用增加任务ITEM表所使用方法
   * 
   * <br>
   * Date ：2008-2-19
   * 
   * @param taskId
   *            工作流ID
   * @param entityId
   *            工作流实体
   * @param bill_id
   * @param currentNodeId
   *            当前节点ID
   * @param nextNodeId
   *            下一个节点ID
   * @param statusCode
   *            状态
   * @param rcid
   * @param ccid
   * @throws FAppException
   * 
   * @author bing-zeng
   * @throws SQLException
   * @since 1.0
   * 
   */
  public void add4CurrentOrCompleteItems4Recall(Statement setRoutingPsmt, String actionType, String entityId,
    String bill_id, String currentNodeId, String nextNodeId, String statusCode, String rcid, String ccid)
    throws FAppException, SQLException;

  /**
   * 获取当前节点的下游节点
   * 
   * @return
   */
  public List getNextNode(String nodeId, String entityId, String wfId);

  /**
   * 获取当前节点的所有下游节点
   * @param nodeId 当前节点
   * @param entityId 表层系统业务主键
   * @param wfId 工作流id
   * @return
   */
  public List getNextNodeForAll(String nodeId, String entityId, String wfId);
}
