package gov.df.fap.api.workflow;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:工作流组件接口
 * </p>
 * <p>
 * Description:专为支付提供的工作流组件接口
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015-2017 北京用友政务软件有限公司
 * </p>
 * <p>
 * Company:北京用友政务软件有限公司
 * </p>
 * 
 * @version 1.0
 * @author hult
 * @since java 1.6
 */
public interface IWorkFlow {

  /**
   * 批量单数据处理操作。
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
   * @param detail_table_name
   *            明细表名
   * @param billDtos
   *            业务单DTO列表，必须含有明细，通过getDetails()获取明细
   * @param auto_tolly_flag
   *            是否通过工作流自动记账	
   * @param inspect_flag 是否资金监控 
   * @return List（内部含BillObject,都有details)
   * @throws Exception
   *             错误信息
   */
  public List doBatchBillProcessNextObj(String moduleid, String roleid, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List billDtos, boolean auto_tolly_flag,
    boolean inspect_flag) throws Exception;

  /**
   * 批量明细数据处理操作。
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
   * @param detail_table_name
   *            明细表名
   * @param detailDtos
   *            明细
   * @param auto_tolly_flag
   *            是否通过工作流自动记账	
   * @param inspect_flag 是否资金监控 
   * @return List（内部含Object)
   * @throws Exception
   *             错误信息
   */
  public List doBatchDetailProcessNextObj(String moduleid, String roleid, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List detailDtos, boolean auto_tolly_flag,
    boolean inspect_flag) throws Exception;

  /**
   * 批量明细数据处理操作。
   * 
   * @param menuid
   *            菜单ID
   * @param roleid
   *            角色ID
   * @param actiontype
   *            操作类型CODE
   * @param nextNodeIds
   *            指定要走的工作流节点
   * @param operationname
   *            操作名称
   * @param operationdate
   *            操作时间
   * @param operationremark
   *            操作意见
   * @param detail_table_name
   *            明细表名
   * @param detailDtos
   *            明细
   * @param auto_tolly_flag
   *            是否通过工作流自动记账 
   * @param inspect_flag 是否资金监控 
   * @return List（内部含Object)
   * @throws Exception
   *             错误信息
   */
  public List doBatchDetailProcessNextNodes(String menuid, String roleid, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List detailDtos, boolean auto_tolly_flag,
    boolean inspect_flag, String[] nextNodeIds) throws Exception;

  /**
  * 批量单数据处理撤销审核退回等操作，按明细调用。
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
  * @param detail_table_name
  *            明细表名
  * @param billDtos
  *            业务单DTO列表，必须含有明细，通过getDetails()获取明细
  * @param inspect_flag 是否资金监控 
  * @return List（内部含Object)
  * @throws Exception
  *             错误信息
  */
  public List doBatchBillProcessRecallObj(String moduleid, String roleid, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List billDtos, boolean inspect_flag)
    throws Exception;

  /**
   * 批量单数据处理删除、作废操作。
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
   * @param detail_table_name
   *            明细表名
   * @param billDtos
   *            业务单DTO列表，必须含有明细，通过getDetails()获取明细
   * @param auto_tolly_flag 是否使用工作流记账
   * @param inspect_flag 是否资金监控 
   * @return List（内部含Object)
   * @throws Exception
   *             错误信息
   */
  public List doBatchBillProcessDelOrDiscardObj(String moduleid, String roleid, String actionType,
    String operationName, String operationDate, String operationRemark, String detail_table_name, List billDtos,
    boolean tollyFlag, boolean inspect_flag) throws Exception;

  /**
   * 批量明细数据处理撤销操作，按历史记录的工作流信息撤销记账。
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
   * @param detail_table_name
   *            明细表名
   * @param detailDtos
   *            明细DTO列表
   * @param inspect_flag 是否资金监控 
   * @return List（内部含Object)
   * @throws Exception
   *             错误信息
   */
  public List doBatchDetailProcessRecallObj(String moduleid, String roleid, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List detailDtos, boolean inspect_flag)
    throws Exception;

  /**
   * 批量单数据处理退回操作。
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
   * @param detail_table_name
   *            明细表名
   * @param billDtos
   *            业务单DTO列表，必须含有明细，通过getDetails()获取明细
   * @param auto_tolly_flag 是否使用工作流记账
   * @param inspect_flag 是否资金监控 
   * @return List（内部含Object)
   * @throws Exception
   *             错误信息
   */
  public List doBatchBillProcessBackObj(String moduleid, String roleid, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List billDtos, boolean tollyFlag,
    boolean inspect_flag) throws Exception;

  /**
   * 批量明细数据退回处理操作。
   * @param moduleid
   * @param roleid
   * @param actionType
   * @param operationName
   * @param operationDate
   * @param operationRemark
   * @param detail_table_name
   * @param detailDtos
   * @param auto_tolly_flag 是否使用工作流记账
   * @return
   * @param inspect_flag 是否资金监控 
   * @throws Exception
   */
  public List doBatchDetailProcessBackObj(String moduleid, String roleid, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List detailDtos, boolean auto_tolly_flag,
    boolean inspect_flag) throws Exception;

  /**
   * 批量明细数据退回到录入处理操作,不记账（一般有问题才退回录入的，如果额度不足就能退回了，就是不够了才退的）。
   * @param moduleid
   * @param roleid
   * @param actionType
   * @param operationName
   * @param operationDate
   * @param operationRemark
   * @param detail_table_name
   * @param detailDtos
   * @param inspect_flag 是否资金监控 
   * @return
   * @throws Exception
   */
  public List doBatchDetailProcessBackToInputObj(String moduleid, String roleid, String actionType,
    String operationName, String operationDate, String operationRemark, String detail_table_name, List detailDtos,
    boolean inspect_flag) throws Exception;

  /**
   * 批量明细数据录入操作,不需要在工作流配置录入记账，根据auto_tolly_flag直接记账。
   * @param moduleid
   * @param roleid
   * @param actionType
   * @param operationName
   * @param operationDate
   * @param operationRemark
   * @param detail_table_name
   * @param detailDtos
   * @param auto_tolly_flag
   * @param inspect_flag 是否资金监控 
   * @return
   * @throws Exception
   */
  public List doBatchDetailProcessInputObj(String moduleid, String roleid, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List detailDtos, boolean auto_tolly_flag,
    boolean inspect_flag) throws Exception;

  /**
   *  批量明细数据修改处理操作,不需要在工作流配置修改记账，根据auto_tolly_flag直接记账。
   * @param moduleid
   * @param roleid
   * @param actionType
   * @param operationName
   * @param operationDate
   * @param operationRemark
   * @param detail_table_name
   * @param detailDtos
   * @param auto_tolly_flag
   * @param inspect_flag 是否资金监控 
   * @return
   * @throws Exception
   */
  public List doBatchDetailProcessEditObj(String moduleid, String roleid, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List detailDtos, boolean auto_tolly_flag,
    boolean inspect_flag) throws Exception;

  /**
   * 批量明细数据修改挂起操作，不记账。
   * @param moduleid
   * @param roleid
   * @param actionType
   * @param operationName
   * @param operationDate
   * @param operationRemark
   * @param detail_table_name
   * @param detailDtos
   * @return
   * @throws Exception
   */
  public List doBatchDetailProcessHangObj(String moduleid, String roleid, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List detailDtos) throws Exception;

  /**
   * 批量明细数据删除作废操作，不判读配置操作流的是否配置了删除作废记账根据auto_tolly_flag直接记账。
   * @param moduleid
   * @param roleid
   * @param actionType
   * @param operationName
   * @param operationDate
   * @param operationRemark
   * @param detail_table_name
   * @param detailDtos
   * @param auto_tolly_flag 是否要记账
   * @param inspect_flag 是否资金监控 
   * @return
   * @throws Exception
   */
  public List doBatchDetailProcessDelOrDiscardObj(String moduleid, String roleid, String actionType,
    String operationName, String operationDate, String operationRemark, String detail_table_name, List detailDtos,
    boolean auto_tolly_flag, boolean inspect_flag) throws Exception;

  /**
   * 批量明细数据作废处理操作,按节点作废。
   * @param nCurrentItemNodeId	
   * @param actionType
   * @param operationName
   * @param operationDate
   * @param operationRemark
   * @param detail_table_name
   * @param detailDtos
   * @param create_user
   * @param create_user_id
   * @param pcon
   * @return
   * @throws Exception
   */

  public List doDetailProcessDelOrDiscardObjByNodeId(long nCurrentItemNodeId, String actionType, String operationName,
    String operationDate, String operationRemark, String detail_table_name, List detailDtos, Connection pcon)
    throws Exception;

  /**
   * 年结批量明细数据删除作废操作，不判读配置操作流的是否配置了删除作废记账根据auto_tolly_flag直接记账,不根据功能角色直接作废删除。
   * @param actionType
   * @param operationName
   * @param operationDate
   * @param operationRemark
   * @param detail_table_name
   * @param detailDtos
   * @param auto_tolly_flag 是否要记账
   * @param inspect_flag 是否资金监控 
   * @return
   * @throws Exception
   */
  public List doYeadEndBatchDetailProcessDelOrDiscard(String actionType, String operationName, String operationDate,
    String operationRemark, String detail_table_name, List detailDtos, boolean auto_tolly_flag, boolean inspect_flag)
    throws Exception;

  /**
   * 获取批量监控需要的工作流id，节点等参数
   * @param module_id
   * @param role_id
   * @param actiontypeid
   * @param operation_name
   * @param operation_date
   * @param operation_remark
   * @param detail_table_name
   * @param listFVoucherDTO
   * @param boolean isDetail是单还是明细 true是明细，false是单
  *  @return Map取得监控需要的参数，包括：String wf_id, String current_node_id,detail_table_name
    map中包括三个key,分别为wf_id,current_node_id,detail_table_name
   * @throws Exception
   * @author zwh
   */
  public Map getBatchInspectPar(String module_id, String role_id, String action_type, String operation_name,
    String operation_date, String operation_remark, String detail_table_name, List listFVoucherDTO, boolean isDetail)
    throws Exception;

  /**
   * 批量数据调用资金监控接口进行资金监控
   * @param module_id
   * @param role_id
   * @param actiontypeid
   * @param operation_name
   * @param operation_date
   * @param operation_remark
   * @param detail_table_name
   * @param listFVoucherDTO，资金监控接口监控完后会修改list中每个FVoucherDTO的监控结果标志，如果违规有违规描述，联系人信息等
   * @param boolean isDetail是单还是明细 true是明细，false是单
  *  @return map,保存的wf_id,detail_table_name,提供给后续预警数据强制通过使用
   * @throws Exception
   * @author zwh
   */
  public Map doBatchInspect(String module_id, String role_id, String action_type, String operation_name,
    String operation_date, String operation_remark, String detail_table_name, List listFVoucherDTO, boolean isDetail)
    throws Exception;

  /**
   * 监控处理预警强制通过数据
   * @param wf_id工作流ID
   * @param detail_table_name业务数据表名
   * @param listFVoucherDTO业务凭证或业务明细
   * @throws Exception违规则抛出异常InspectException
   * 
   */
  public void inspectDealResult(String wf_id, String detail_table_name, List listFVoucherDTO) throws Exception;

  /**
   * 根据菜单，角色查询当前节点的全部下一节点
   * @param menuid
   * @param roleid
   * @param entity_id
   * @param pcon
   * @return
  * @throws Exception 
   */
  public List getNextNodes(String menuid, String roleid) throws Exception;

  /**
   * 根据菜单，角色查询当前节点
   * @param menuid
   * @param roleid
   * @param entity_id
   * @param pcon
   * @return
  * @throws Exception 
   */
  public List getCurrentNodes(String menuid, String roleid) throws Exception;
}