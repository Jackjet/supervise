package gov.df.fap.api.workflow;

import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.util.exception.FAppException;
import gov.df.fap.util.xml.XMLData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface IWfUti {

  /**
   * 通用 区别各种操作类型之前走工作流的处理
   * 
   * @param List 业务数据DTO集合
   * @return
   * @throws Exception
   * 
   */
  public void doWorkFlowSimple(PreparedStatement insertCurrentTaskPsmt, Statement setRoutingPsmt,
    String old_current_node_id, String moduleid, String roleid, String actiontypeid, String operationname,
    String operationdate, String operationremark, List inputFVoucherDtos, boolean auto_tolly_flag,
    boolean auto_create_ccid, boolean auto_create_rcid, List control_wf_info, boolean isForced2Execute,
    XMLData tmpCanGoData, StringBuffer bill_table_name, StringBuffer detail_table_name) throws Exception;

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
  public String getTollyFlag(String moduleid, String roleid, String actiontypeid, String bill_table_name,
    String detail_table_name, FVoucherDTO inputFVoucherDto, XMLData tmpCanGoData) throws Exception;

  /**
   * 调用资金监控接口
   * @return IWorkFlowHelper
   * @throws Exception
   */
  public void doInspect(String old_current_node_id, String moduleid, String roleid, String actiontypeid,
    String operationname, String operationdate, String operationremark, String bill_table_name,
    String detail_table_name, FVoucherDTO inputFVoucherDto, boolean isForced2Execute, boolean isBillDetail,
    XMLData tmpCanGoData) throws Exception;

  /**
   * 从sequence SEQ_SYS_WF_TASK_ID 取得下一个TaskId
   * 
   * @return
   */
  public String getNextTaskIdBySequence();

  /**
   * 获取当前节点的下游节点
   * 
   * @return
   */
  public List getNextNode(String nodeId, String entityId, String wfId);

  /**
   * 根据权限获取当前节点的下游节点
   * 
   * @return
   */
  //	public List getNextNodeWithDataRight(String entityId,String tableName,String nodeId,List list,List temp);
  /**
   * 根据权限和参数设置获取节点信息
   * operationLogFlag==1只显示下岗结点操作信息
   * operationLogFlag==2只显示当前岗结点操作信息
     * operationLogFlag==3同时显示当前岗和下岗操作信息
   * @return
   */
  //	public List getNodeWithDataRight(String entityId,String tableName,String nodeId,List list,List temp,String operationLogFlag);
  /**
   * 获取当前节点的所有下游节点
   * @param nodeId 当前节点
   * @param entityId 表层系统业务主键
   * @param wfId 工作流id
   * @return
   */
  public List getNextNodeForAll(String nodeId, String entityId, String wfId);

  /**
   * 批处理SQL赋值
   * 
   * 
   * @throws Exception
   */
  public void setValues4InsertCurrentTaskPsmt(PreparedStatement insertCurrentTaskPsmt, String nextTaskId, String wfId,
    String wfTableName, String entityId, String currentNodeId, String nextNodeId, String actionType, int isUndo,
    String createUser, String undoUser, String undoDate, String operationName, String operationDate,
    String operationRemark, String initMoney, String resultMoney, String remark, String tollyFlag, String billTypeCode,
    String billId, String rcid, String ccid, int setMonth, int updateFlag, String createUserId) throws FAppException;

  /**
   * modify start by bing-zeng 插入sys_wf_task_routing时 删除条数限制,
   * 增加了UPDATE_FLAGE限制 0:表示新增加的任务, 1:表示原有的任务 插入sys_wf_complete_tasks 时 删除条数限制,
   * 增加了UPDATE_FLAGE限制 0:表示新增加的任务, 1:表示原有的任务 删除sys_wf_current_tasks 时 删除条数限制,
   * 增加了UPDATE_FLAGE限制 0:表示新增加的任务, 1:表示原有的任务 增加了最后更新sys_wf_current_tasks ,
   * UPDATE_FLAGE = 1
   */
  public void setRoutingStmt(Statement setRoutingPsmt, String wf_id, String entityId, String current_node_id,
    String tableName) throws FAppException;

  /**
   * 判断某流程节点是否满足表达式
   * 
   * @param Wf_id--------------流程ID
   * @param nodeId-------------当前节点ID
   * @param nextNodeId---------下一节点ID
   * @param rowData------------行数据
   * @return OK
   * @throws Exception---------错误信息
   */
  public boolean getValidWfNode(String condition_id, XMLData rowData) throws FAppException;

  /**
   * 一个节点是否为同步节点
   */
  public String isGatherNode(String nodeId);

  /**
   * 同步操作时查询是否满足走入下一个节点.
   * 
   * 
   * @param flag--------------是否同步标志
   * @param nextNodeId-------------下一节点ID
   * @param currentNodeId---------当前节点ID
   * @param rowData------------行数据
   * @return OK
   * @throws Exception---------错误信息
   */
  public String getValidGatherNode(StringBuffer sqlBuffer, StringBuffer gatherBuffer, String nextNodeId,
    String currentNodeId, String entityId, String billId, String wfId, String rcid, String ccid, XMLData rowData);

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
   * @author bing-zeng
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
  public List doBusVouAuditBatch(List newFVoucherDtos) throws FAppException;

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
   * 对已确认状态下的作废、删除操作进行预处理，将数据撤消回来。
   * @param moduleid
   * @param roleid
   * @param entityId
   * @param tablename
   * @param inputFVoucherDto
   * @param isForced2Execute
   * @throws FAppException
   */
  public void preDoSingleProcessSimplyDiscard(String moduleid, String roleid, String entityId, String tablename,
    FVoucherDTO inputFVoucherDto, boolean isForced2Execute) throws FAppException;

  /**
   * 如果当前节点的来源节点是同步节点，判断该数据是否也存在于此同步节点的其它下游分支节点中
   * @param wfId
   * @param thisNode
   * @param entityId
   * @param bizData
   * @return
   * @throws FAppException 
   */
  public boolean isExistsOtherSyncBranchTasks(String wfId, String thisNode, String entityId, XMLData bizData)
    throws FAppException;

  /**
   * 判断数据是否可以从fromNode走到toNode。(不包含开始/结束节点)
   * @param wfId
   * @param fromNode
   * @param toNode
   * @param routingType
   * @param bizData
   * @return
   * @throws FAppException
   */
  public boolean isCanGoRoute(String wfId, String fromNode, String toNode, String routingType, XMLData bizData)
    throws FAppException;
}
