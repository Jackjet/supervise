package gov.df.fap.api.workflow;

import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.workflow.FWFLogDTO;
import gov.df.fap.bean.workflow.FWFSqlDTO;
import gov.df.fap.util.exception.FAppException;
import gov.df.fap.util.xml.XMLData;

import java.sql.Statement;
import java.util.List;

/**
 * <p>
 * Title: 工作流组件接口
 * </p>
 * <p>
 * Description:工作流组件接口
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015-2017 北京用友政务软件有限公司
 * </p>
 * <p>
 * Company:北京用友政务软件有限公司
 * </p>
 * 
 * @version 1.0
 * @author 
 * @author 
 * @since java 1.6
 */
public interface IWorkFlowNew {

  /**
   * 得到数据的操作日志信息。
   * 
   * @param TableName
   *            表名称
   * @param EntityId
   *            数据ID
   * @return List对象（内部含FWFLogDTO)
   * @throws FAppException
   *             错误信息
   */
  public List getWfLogDTO(String TableName, String EntityId) throws FAppException;

  /**
   * 得到数据的操作日志信息。
   * 
   * @param TableName
   *            表名称
   * @param EntityId
   *            数据ID
   * @return List对象（内部含FWFLogDTO)
   * @throws FAppException
   *             错误信息
   */
  public byte[] getCompressWfLog(String TableName, String EntityId) throws FAppException;

  /**
   * 查询某功能的事务提醒。
   * 
   * @param UserId
   *            用户ID
   * @param RoleId
   *            角色ID
   * @param ModuleId
   *            功能ID
   * @return List(内部含FWFLogDTO)
   * @throws FAppException
   *             错误信息
   */
  public List getModuleAllTasksStatInfo(String UserId, String RoleId, String ModuleId) throws FAppException;

  /**
   * 任务查询。当用户进行事务提醒时，从工作流组件得到所能操作的任务（数据）列表。
   * 
   * @param UserId
   *            用户ID
   * @param RoleId
   *            角色ID
   * @return List对象（内部含FWFLogDTO)
   * @throws FAppException
   *             错误信息
   */
  public List getAllTasksStatInfo(String UserId, String RoleId) throws FAppException;

  /**
   * 得到传入数据所处的节点及状态信息。
   * 
   * @param TableName
   *            表名称
   * @param EntityId
   *            数据ID
   * @return List对象（内部含FWFLogDTO)
   * @throws FAppException
   *             错误信息
   */
  public List getCurrNodeInfoDTO(String TableName, String EntityId) throws FAppException;

  /**
   * 得到传入数据是否结束。
   * 
   * @param TableName
   *            表名称
   * @param EntityId
   *            数据ID
   * @return List对象（内部含FWFLogDTO)
   * @throws FAppException
   *             错误信息
   */
  public boolean isEnd(String TableName, String EntityId) throws FAppException;

  /**
   * 任务查询语句。当用户启动某一个功能或进行事务提醒时，工作流组件返回用于提取任务的SQL语句。
   * 
   * @param TableName
   *            表名称
   * @param TableId
   *            表主键ID
   * @param RoleId
   *            角色ID
   * @param ModuleId
   *            功能ID
   * @param OperateType
   *            操作类型
   * @param TableAlias
   *            主表的别名
   * @return SQL语句
   * @throws Exception
   *             错误信息
   */
  public String getTasksBySql(String TableName, String TableId, String RoleId, String ModuleId, String OperateType,
    String TableAlias) throws Exception;

  /**
   * 带有权限的任务查询语句。当用户启动某一个功能或进行事务提醒时，工作流组件返回用于提取任务并带有权限的SQL语句。
   * 
   * @param UserId
   *            用户ID
   * @param RoleId
   *            角色ID
   * @param TableName
   *            表名称
   * @param TableId
   *            表主键ID
   * @param ModuleId
   *            功能ID
   * @param OperateType
   *            操作类型
   * @param TableAlias
   *            主表的别名
   * @return SQL语句
   * @throws FAppException
   *             错误信息
   */
  public String getTasksWithRightBySql(String UserId, String RoleId, String TableName, String TableId, String ModuleId,
    String OperateType, String TableAlias) throws FAppException;

  /**
   * 通用处理操作（对批量数据）。
   * 
   * @param moduleid
   *            功能ID
   * @param roleid
   *            角色ID
   * @param actiontype
   *            操作类型CODE
   * @param operationname
   *            操作名称
   * @param operationdate
   *            操作时间
   * @param operationremark
   *            操作意见
   * @param bill_table_name
   *            单表名
   * @param detail_table_name
   *            明细表名
   * @param inputFVoucherDtos
   *            业务数据DTO列表
   * @param auto_tolly_flag
   *            是否通过工作流自动记账
   * @param auto_create_ccid
   *            是否通过工作流生成CCID
   * @param auto_create_rcid
   *            是否通过工作流生成RCID
   * @return List（内部含Object)
   * @throws FAppException
   *             错误信息
   */
  public List doBatchAllProcessReturnObj(String moduleid, String roleid, String actiontype, String operationname,
    String operationdate, String operationremark, String bill_table_name, String detail_table_name,
    List inputFVoucherDtos, boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid)
    throws FAppException;

  /**
   * 通用提取操作（对批量数据,不用传TABLE_NAME）。
   * 
   * @param old_node_id
   *            被提取的数据当前所处的节点号
   * @param moduleid
   *            功能ID
   * @param roleid
   *            角色ID
   * @param operationname
   *            操作名称
   * @param operationdate
   *            操作时间
   * @param operationremark
   *            操作意见
   * @param inputFVoucherDtos
   *            业务数据DTO列表
   * @param auto_tolly_flag
   *            是否通过工作流自动记账
   * @param auto_create_ccid
   *            是否通过工作流生成CCID
   * @param auto_create_rcid
   *            是否通过工作流生成RCID
   * @return List（内部含Object)
   * @throws FAppException
   *             错误信息
   */
  public List doBatchDistillWithNoTableName(String old_node_id, String moduleid, String roleid, String operationname,
    String operationdate, String operationremark, List inputFVoucherDtos, boolean auto_tolly_flag,
    boolean auto_create_ccid, boolean auto_create_rcid) throws FAppException;

  /**
   * 通用跨节点操作（对批量数据,不用传TABLE_NAME）。
   * 
   * @param old_node_id
   *            被提取的数据当前所处的节点号
   * @param actiontype
   *            操作类型CODE
   * @param operationname
   *            操作名称
   * @param operationdate
   *            操作时间
   * @param operationremark
   *            操作意见
   * @param inputFVoucherDtos
   *            业务数据DTO列表
   * @param auto_tolly_flag
   *            是否通过工作流自动记账
   * @param auto_create_ccid
   *            是否通过工作流生成CCID
   * @param auto_create_rcid
   *            是否通过工作流生成RCID
   * @return List（内部含Object)
   * @throws FAppException
   *             错误信息
   */
  public List doBatchStrideProcessWithNoTableName(String old_node_id, String actiontype, String operationname,
    String operationdate, String operationremark, List inputFVoucherDtos, boolean auto_tolly_flag,
    boolean auto_create_ccid, boolean auto_create_rcid) throws FAppException;

  /**
   * 通用处理操作（对批量数据,不用传TABLE_NAME，跨表操作）。
   * 
   * @param moduleid
   *            功能ID
   * @param roleid
   *            角色ID
   * @param actiontype
   *            操作类型CODE
   * @param operationname
   *            操作名称
   * @param operationdate
   *            操作时间
   * @param operationremark
   *            操作意见
   * @param oldFVoucherDtos
   *            旧业务数据DTO列表
   * @param newFVoucherDtos
   *            新业务数据DTO列表
   * @param auto_tolly_flag
   *            是否通过工作流自动记账
   * @param auto_create_ccid
   *            是否通过工作流生成CCID
   * @param auto_create_rcid
   *            是否通过工作流生成RCID
   * @return List（内部含Object)
   * @throws FAppException
   *             错误信息
   */
  public XMLData doBatchAllProcessCrossTableWithNoTableName(String moduleid, String roleid, String actiontype,
    String operationname, String operationdate, String operationremark, List oldFVoucherDtos, List newFVoucherDtos,
    boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid) throws FAppException;

  /**
   * 带有权限的任务查询语句。不用传表名和主键
   * 
   * @param UserId
   *            用户ID
   * @param RoleId
   *            角色ID
   * @param ModuleId
   *            功能ID
   * @param OperateType
   *            操作类型
   * @param TableAlias
   *            主表的别名
   * @return SQL语句
   * @throws FAppException
   *             错误信息
   */
  public String getTasksWithRightBySqlWithSimply(String UserId, String RoleId, String ModuleId, String OperateType,
    String TableAlias) throws FAppException;

  /**
   * 获取工作流权限，可设置是否关心外部关联表
   * 可以传入参数 isOuterTable 来控制是否需要拼接外部事务表。
   * true 如果节点配置则拼接， false 永远不拼接
   * @param data_right_sql
   * @param RoleId
   * @param ModuleId
   * @param OperateType
   * @param TableAlias
   * @param is1stExistClauseFlag
   * @param isOuterTable
   * @author ydz
   * @return
   */

  public String getTasksWithRightBySqlWithSimplyNew(String UserId, String RoleId, String ModuleId, String OperateType,
    String TableAlias, boolean isOuterTable) throws FAppException;

  /**
   *  带有权限的任务查询语句。传明细表表名,明细表再外部查询 权限和工作流节点条件并列查询
   * @param UserId
   * @param RoleId
   * @param ModuleId
   * @param OperateType
   * @param detailTable
   * @return
   * @throws FAppException
   */
  public String getTasksWithRightBySqlWithDetailTable(String UserId, String RoleId, String ModuleId,
    String OperateType, String detailTable) throws FAppException;

  /**
   * 得到数据的操作日志信息。不用传表名
   * 
   * @param EntityId
   *            数据ID
   * @return List对象（内部含FWFLogDTO)
   * @throws FAppException
   *             错误信息
   */
  public List getWfLogDTOSimply(String EntityId) throws FAppException;

  /**
   * 得到传入数据所处的节点状态下的某条数据最近第二条数据的DTO，供比较用
   * 
   * @param RoleId
   *            角色ID
   * @param ModuleId
   *            功能ID
   * @param OperateType
   *            操作类型
   * @param EntityId
   *            数据ID
   * @return Object
   * @throws FAppException
   *             错误信息
   */
  public Object obtainOldDTO(String RoleId, String ModuleId, String OperateType, String EntityId) throws FAppException;

  /**
   * 根据task_id得到DTO
   * 
   * @param task_id
   *            任务ID
   * @return Object
   * @throws FAppException
   *             错误信息
   */
  public Object obtainDTOByTaskId(String task_id) throws FAppException;

  /**
   * 得到所有结束节点对应的功能ID
   * 
   * @return List对象（内部含XMLData，只有一列module_id)
   * @throws FAppException
   *             错误信息
   */
  public List getIsEndNode() throws FAppException;

  /** 工作流监控管理 */

  /**
   * 获得符合条件的数据
   * 
   * @param sql
   *            要查询的sql
   * @param countSql
   *            要统计的sql
   * @return 符合条件的数据
   * @throws FAppException
   *             错误信息
   */
  public XMLData findDatas(String sql, String countSql) throws FAppException;

  /**
   * 得到所有流程数据
   * 
   * @return List 所有流程数据
   */
  public List getAllWorkFlows();

  /**
   * 根据流程ID获得节点
   * 
   * @param wfId
   *            流程ID
   * @return 节点信息
   */
  public List getSysWfNodesByWfId(String wfId);

  /**
   * 获得所有状态
   * 
   * @return 所有状态
   */
  public List getAllSysStatus();

  /**
   * 带有权限的任务查询子句。不用传表名和主键 (内部处理对于主单明细表模式进行了处理,避免因为SQL语句不合理造成执行效率低的问题)
   * 
   * @param UserId
   *            用户ID
   * @param RoleId
   *            角色ID
   * @param ModuleId
   *            功能ID
   * @param OperateType
   *            操作类型
   * @param TableAlias
   *            主表的别名
   * @return SQL语句
   * @throws FAppException
   *             错误信息
   */
  public String getTaskSqlClause(String UserId, String RoleId, String ModuleId, String OperateType, String TableAlias)
    throws FAppException;

  /**
   * 查询某一业务所有结束的数据
   * 
   * @param wf_id
   *            工作流id
   * @param TableAlias
   *            主表的别名
   * 
   * @return 所有结束的数据
   */
  public String getWFEndData(String wf_id, String TableAlias);

  /**
   * 根据设置的权限获取工作流日志数据传输的DTO对象
   * 
   * @param UserId
   *            用户ID
   * @param RoleId
   *            角色ID
   * @param ModuleId
   *            功能ID
   * @param OperateType
   *            操作类型
   * @param TableAlias
   *            主表的别名
   * @return SQL语句dto
   * @throws Exception
   *             错误信息
   * 
   */
  public FWFSqlDTO getTasksWithRightBySqlByJoin(String UserId, String RoleId, String ModuleId, String OperateType,
    String TableAlias) throws Exception;

  /**
   * 自动审核
   * 
   * @throws FAppException
   */
  public void autoAudit() throws FAppException;

  /**
   * 大批量数据的导入
   * 
   * @param tableName
   *            数据来源的表
   * @param whereSql
   *            sql语句中的条件
   * @param moduleId
   *            功能ID
   * @param roleid
   *            角色ID
   * @param entityId
   *            ENTITY_ID
   * @param billId
   *            billId
   * @param moneyField
   *            RESULT_MONEY
   * @throws FAppException
   */

  public void doBatchInput(String tableName, String whereSql, String moduleId, String roleid, String entityId,
    String billId, long moneyField) throws FAppException;

  /**
   * 
   * 功能：工作流任务列表操作方法
   * 
   * <br>
   * Date ：2008-3-1
   * 
   * @param setRoutingPsmt
   *            Statement
   * @param taskId
   *            当前任务ID
   * @param wfId
   *            工作流ID
   * @param entityId
   *            实体ID
   * @param currentNodeId
   *            当前节点
   * @param nextNodeId
   *            下一个节点
   * @param actionType
   *            操作类型
   * @param billId
   * @param rcid
   * @param ccid
   * @throws Exception
   * 
   * @author bing-zeng
   * @since 1.0
   * 
   */
  public void saveOptionCurrentAndComleteTable(Statement setRoutingPsmt, String taskId, String wfId, String entityId,
    String currentNodeId, String nextNodeId, String actionType, String billId, String rcid, String ccid)
    throws Exception;

  /**
   * 工作流任意节点作废删除和撤消操作接口
   * @param actionType 动作类型（DISCARD，DELETE和RECALL）
   * @param operationName 操作名称
   * @param operationDate 操作日期
   * @param operationRemark 操作备注
   * @param tableName 业务表名称
   * @param inputFVoucherDTOs 凭证列表
   * @param auto_tolly_flag 是否需要记账
   * @return
   * @throws FAppException
   */
  public List doStrideProcess(String actionType, String operationName, String operationDate, String operationRemark,
    List inputFVoucherDTOs, boolean auto_tolly_flag, String table_name) throws FAppException;

  /**
   * 得到记帐类型 -1 不记帐 0 在途记帐 1 终审记帐
   * 
   * @param moduleid
   * @param roleid
   * @param actiontypeid
   * @param bill_table_name
   * @param detail_table_name
   * @param inputFVoucherDto
   * @return
   * @throws Exception
   */
  public String getTollyType(String moduleid, String roleid, String actiontypeid, String bill_table_name,
    String detail_table_name, FVoucherDTO inputFVoucherDto) throws Exception;

  /**
   * ymj 刷新工作流5个表中的rcid,ccid
   * 
   * @param tableName 业务数据表名
   * @param entityId 业务数据唯一标识
   * @param newRcid 新的rcid
   * @param newCcid 新的ccid
   * @throws Exception
   */
  public void refreshRcidAndCcid(String tableName, String entityId, String newRcid, String newCcid) throws Exception;

  /**
   * 通用处理操作（对批量数据,不用传TABLE_NAME）。
   * 
   * @param moduleid 			功能ID
   * @param roleid 			角色ID
   * @param actiontype 		操作类型CODE
   * @param operationname 	操作名称
   * @param operationdate 	操作时间
   * @param operationremark 	操作意见
   * @param inputFVoucherDtos 业务数据DTO列表
   * @param auto_tolly_flag 	是否通过工作流自动记账
   * @param auto_create_ccid 	是否通过工作流生成CCID
   * @param auto_create_rcid 	是否通过工作流生成RCID
   * @return List（内部含Object)
   * @throws FAppException 错误信息
   */
  public List doBatchBackToInputProcessWithNoTableName(String moduleid, String roleid, String actiontype,
    String operationname, String operationdate, String operationremark, List inputFVoucherDtos,
    boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid) throws FAppException;

  /**
   * 查询某功能的曾经办事务提醒。
   * 
   * @param UserId
   *            用户ID
   * @param RoleId
   *            角色ID
   * @param ModuleId
   *            功能ID
   * @return List(内部含FWFLogDTO)
   * @throws FAppException
   *             错误信息
   */
  public FWFLogDTO getModule008StatInfo(String UserId, String RoleId, String ModuleId) throws FAppException;

  /**
   * 带有权限的任务查询语句。传明细表表名
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param ModuleID-----------功能ID
   * @param OperateType--------操作类型
   * @param TableAlias---------主表的别名
   * @return SQL语句
   * @throws Exception---------错误信息
   */
  public String getTask008WithRightBySqlWithDetailTable(String UserId, String RoleId, String ModuleId,
    String detailTable) throws FAppException;

}
