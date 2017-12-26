package gov.df.fap.service.workflow;

import gov.df.fap.api.framework.IPreLogin;
import gov.df.fap.api.gl.coa.ICoaService;
import gov.df.fap.api.gl.interfaces.IVoucherService;
import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.api.workflow.IBillTypeServices;
import gov.df.fap.api.workflow.IInspectService;
import gov.df.fap.api.workflow.IMessageClient;
import gov.df.fap.api.workflow.IWorkFlowNew;
import gov.df.fap.api.workflow.message.IMessage;
import gov.df.fap.api.workflow.sysregulation.IWorkFlowRuleFactory;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.rule.dto.FRightSqlDTO;
import gov.df.fap.bean.workflow.FBillTypeDTO;
import gov.df.fap.bean.workflow.FMessageDTO;
import gov.df.fap.bean.workflow.FTaskItemDTO;
import gov.df.fap.bean.workflow.FWFLogDTO;
import gov.df.fap.bean.workflow.FWFSqlDTO;
import gov.df.fap.service.util.UtilCache;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.exception.FAppException;
import gov.df.fap.util.xml.ParseXML;
import gov.df.fap.util.xml.XMLData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 工作流组件接口实现 
 * 
 * @version 1.0
 * @author 
 * @since java 1.6
 * 
 */
@Service
public class WorkFlowNewBO implements IWorkFlowNew, IMessageClient {

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao = null;

  @Autowired
  private ICoaService icoaService;

  @Autowired
  private IDataRight idataright;

  @Autowired
  private IBillTypeServices billtype;

  @Autowired
  private IPreLogin preLoginBean;

  @Autowired
  private IVoucherService ivoucher;

  @Autowired
  private IWorkFlowRuleFactory ruleFactory;

  @Autowired
  private IMessage msgService;

  private boolean isBillDetail = false;

  // 同步标志
  private static final String SYNCHRONIZATIONFLAG = "0";

  private XMLData tmpCanGoData = new XMLData();;

  //节点对角色、菜单缓存
  private Map roleModuleNodeCache = new HashMap();

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 撤销专用方法（在途和终审）
   * 
   * @param menuid
   * @param roleid
   * @param bill_table_name
   * @param detail_table_name
   * @param operationname
   * @param operationdate
   * @param operationremark
   * @param inputFVoucherDto
   * @param auto_tolly_flag
   * @return
   * @throws Exception
   * 
   */
  private Object undoSingleProcessReturnObj(String menuid, String roleid, String bill_table_name,
    String detail_table_name, String operationname, String operationdate, String operationremark,
    FVoucherDTO inputFVoucherDto, boolean auto_tolly_flag) throws Exception {
    String tolly_flag = "-1";
    // 自动记帐
    if (auto_tolly_flag) {
      // 根据传入参数取得记帐类型
      tolly_flag = this.getTollyFlag(null, menuid, roleid, "RECALL", bill_table_name, detail_table_name,
        inputFVoucherDto);

      if (tolly_flag.equals("0"))// 记帐类型为在途记帐
      {
        inputFVoucherDto.setIs_end(Integer.parseInt(tolly_flag));
        // 如果是龙图平台
        if (SessionUtil.getParaMap().get("switch01").toString().equals("1")) {
          // 如果是龙图平台，撤销时需要传入2
          inputFVoucherDto.setIs_end(2);
          inputFVoucherDto = this.doBusVouUpdate(inputFVoucherDto);
        } else {
          inputFVoucherDto = this.doBusVouUpdate(inputFVoucherDto);
        }
      } else if (tolly_flag.equals("1")) {// 记帐类型为终审记帐
        inputFVoucherDto.setIs_end(Integer.parseInt(tolly_flag));
        // 如果是龙图平台
        if (SessionUtil.getParaMap().get("switch01").toString().equals("1")) {
          // 如果是龙图平台，撤销时需要传入2
          inputFVoucherDto.setIs_end(2);
          inputFVoucherDto = this.doBusVouCancelAudit(inputFVoucherDto);
        } else {
          inputFVoucherDto = this.doBusVouCancelAudit(inputFVoucherDto);
        }
      }
    }
    // 资金监控
    IInspectService inspectService = null;
    try {
      inspectService = (IInspectService) SessionUtil.getServerBean("sys.inspectService");
    } catch (Exception e) {
      // 访问不到监控包
    }
    // 如果访问不到监控类包，则不能调用其接口。
    if (inspectService != null) {
      List nodeInspectRules = null;
      if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
        nodeInspectRules = this.getInspectRules(null, menuid, roleid, "RECALL", bill_table_name, detail_table_name,
          inputFVoucherDto);
      } else {
        nodeInspectRules = this.getInspectRules(null, menuid, roleid, "RECALL", bill_table_name, detail_table_name,
          (FVoucherDTO) inputFVoucherDto.getDetails().get(0));
      }
      // 无论能否找到规则列表，都调用监控接口
      String inspectNodeId = null;
      if (nodeInspectRules != null && nodeInspectRules.size() > 0) {
        inspectNodeId = (String) ((XMLData) nodeInspectRules.get(0)).get("node_id");
      } else {
        if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
          inspectNodeId = getNextNodeId(null, menuid, roleid, "RECALL", inputFVoucherDto, false);
        } else {
          inspectNodeId = getNextNodeId(null, menuid, roleid, "RECALL", (FVoucherDTO) inputFVoucherDto.getDetails()
            .get(0), false);
        }
      }
      String rtnMsg = inspectService.inspectInstance(getWfIdByNodeId(inspectNodeId), detail_table_name, inspectNodeId,
        null, "RECALL", operationname, operationdate, operationremark, nodeInspectRules, inputFVoucherDto, menuid,
        roleid);
      inputFVoucherDto.setWarning(rtnMsg);
    }
    // 如果没有明细数据，则直接调工作流
    if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
      if (detail_table_name == null || detail_table_name.equals("")) {
        throw new Exception("需要传入detail_table_name!");
      }
      return undoSingleProcessSimplyReturnObj(menuid, roleid, detail_table_name, inputFVoucherDto);

    } else {
      // 如果是单＋明细，则先将明细走流程，后走单的流程
      if (detail_table_name == null || detail_table_name.equals("")) {
        throw new Exception("需要传入detail_table_name!");
      }
      if (bill_table_name == null || bill_table_name.equals("")) {
        throw new Exception("需要传入bill_table_name!");
      }
      for (int i = 0; i < inputFVoucherDto.getDetails().size(); i++) {
        FVoucherDTO detailDto = new FVoucherDTO();
        detailDto = (FVoucherDTO) inputFVoucherDto.getDetails().get(i);
        undoSingleProcessSimplyReturnObj(menuid, roleid, detail_table_name, detailDto);
      }
      return inputFVoucherDto;
    }
  }

  /**
   * 通用撤消处理操作（对单条数据）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param table_name---------表名
   * @param inputFVoucherDto---业务数据DTO（单条）
   * @return 撤消后的当前节点的DTO
   * @throws Exception---------错误信息
   */
  private synchronized Object undoSingleProcessSimplyReturnObj(String menuid, String roleid, String tablename,
    FVoucherDTO inputFVoucherDto) throws Exception {
    // 是否有历史数据
    boolean has_old_dto = true;
    String billId = "";
    Session tmp_session = null;
    try {
      Object return_dto = new Object();

      String create_user = "";
      String user_name = "";
      String authorieduser_name = "";

      String rg_code = getRgCode();
      String setYear = getSetYear();

      if (SessionUtil.getUserInfoContext().getAttribute("user_name") != null
        && !SessionUtil.getUserInfoContext().getAttribute("user_name").toString().equals("")) {
        user_name = (String) SessionUtil.getUserInfoContext().getAttribute("user_name").toString();
      }

      if (SessionUtil.getUserInfoContext().getAttribute("authorieduser_name") != null
        && !SessionUtil.getUserInfoContext().getAttribute("authorieduser_name").toString().equals("")) {
        authorieduser_name = (String) SessionUtil.getUserInfoContext().getAttribute("authorieduser_name");
      }

      if (!"".equals(user_name) && !"".equals(authorieduser_name) && (!authorieduser_name.equals(user_name))) {
        create_user = authorieduser_name + "(代理：" + user_name + ")";
      } else {
        create_user = user_name;
      }

      StringBuffer select_sql = new StringBuffer();
      StringBuffer insert_sql = new StringBuffer();
      StringBuffer delete_sql = new StringBuffer();
      StringBuffer update_sql = new StringBuffer();

      int num = 0;
      String entityId = "";

      List rs = null;
      String wf_id = "";
      String current_node_id = "0";
      if (menuid == null || menuid.equals("")) {
        throw new Exception("需要传入menuid!");
      }
      if (roleid == null || roleid.equals("")) {
        throw new Exception("需要传入roleid!");
      }

      if (tablename == null || tablename.equals("")) {
        throw new Exception("需要传入tablename!");
      }
      if (inputFVoucherDto.getVou_id() == null || inputFVoucherDto.getVou_id().equals("")) {
        throw new Exception("需要传入vou_id或者vou_detail_id!");
      } else {
        entityId = inputFVoucherDto.getVou_id();
      }

      StringBuffer judge_sql = new StringBuffer();
      judge_sql.append("select distinct t.wf_id,t.current_node_id,t.next_node_id,t.remark ")
        .append(" from sys_wf_end_tasks t,sys_wf_nodes n,vw_sys_wf_node_relation d").append(" where t.wf_id = n.wf_id")
        .append(" and t.wf_id = d.wf_id").append(" and t.current_node_id = d.node_id").append(" and d.module_id = ?")
        .append(" and n.node_type = '003'").append(" and t.next_node_id = n.node_id")
        .append("  and t.entity_id = ? and t.rg_code = ? and t.set_year = ?");
      rs = dao.findBySql(judge_sql.toString(), new Object[] { menuid, entityId, rg_code, setYear });
      // 如果不是撤销确认结点的操作，按照原来的走，如果是撤销确认结点的操作，则按改动的操作走
      if (rs.size() == 0) {
        // 不是撤销确认节点的操作，所以按照开始的走下去
        // 查询是否可以进行撤销操作
        select_sql.append("select distinct b.wf_id, b.current_node_id, b.next_node_id, b.remark ")
          .append(" from sys_wf_current_tasks").append(Tools.addDbLink()).append(" b, vw_sys_wf_node_relation")
          .append(Tools.addDbLink()).append(" d ")
          .append(" where d.module_id = ? and d.role_id = ? and b.current_node_id=d.node_id ")
          .append(" and b.entity_id =? and b.rg_code = ? and b.set_year = ?");
        rs = dao.findBySql(select_sql.toString(), new Object[] { menuid, roleid, entityId, rg_code, setYear });

        // 如果查找不到时，判断当前做撤销的数据是否是在开始节点上做的撤销
        if (rs.size() == 0) {
          StringBuffer selectStartNodeSql = new StringBuffer();
          selectStartNodeSql.append("select distinct t.wf_id, t.current_node_id, t.next_node_id, t.remark ")
            .append(" from sys_wf_current_tasks").append(Tools.addDbLink()).append(" t, sys_wf_nodes")
            .append(Tools.addDbLink()).append(" n ").append(" where t.wf_id = n.wf_id ")
            .append(" and n.node_type = '001' ").append(" and t.current_node_id = n.node_id ")
            .append(" and t.entity_id = ? and t.rg_code = ? and t.set_year = ?");
          rs = dao.findBySql(selectStartNodeSql.toString(), new Object[] { entityId, rg_code, setYear });
          has_old_dto = false;
          if (rs.size() > 0)
            throw new Exception("不能在开始节点做撤销操作!");
        }

        if (rs.size() == 0) {
          throw new Exception("不能走入下一流程节点，原因有：未找到该数据；或者该角色没有此权限!");
        } else {
          // 得到当前需要撤销的数据的流程号和节点号
          wf_id = ((Map) rs.get(0)).get("wf_id").toString();
          current_node_id = ((Map) rs.get(0)).get("current_node_id").toString();
        }

        return_dto = (Object) inputFVoucherDto;

        // 将当前任务列表的当前数据置上UNDO标志
        update_sql.append("update  sys_wf_current_tasks").append(Tools.addDbLink())
          .append("  b set b.is_undo=1 where b.entity_id =? ")
          .append(" and b.wf_id=? and b.current_node_id=? and b.rg_code = ? and b.set_year = ?");
        num = dao.executeBySql(update_sql.toString(),
          new Object[] { entityId, wf_id, current_node_id, rg_code, setYear });
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 如果能找到上一节点数据
        if (has_old_dto) {
          // 将完成任务列表中的上一节点的数据复制到当前任务列表
          StringBuffer insert_old_sql = new StringBuffer();
          StringBuffer delete_old_sql = new StringBuffer();
          insert_old_sql.append(" insert into sys_wf_current_tasks").append(Tools.addDbLink())
            .append("   select * from sys_wf_complete_tasks").append(Tools.addDbLink())
            .append(" c  where c.task_id in (").append(" select b.task_id from sys_wf_current_tasks")
            .append(Tools.addDbLink()).append(" a,sys_wf_task_routing").append(Tools.addDbLink()).append(" b")
            .append(" where a.entity_id =?").append(" and a.wf_id=?")
            .append(" and a.current_node_id=? and a.task_id=b.next_task_id and a.rg_code=? and a.set_year=?)")
            .append(" and c.rg_code=? and c.set_year=?");
          num = dao.executeBySql(insert_old_sql.toString(), new Object[] { entityId, wf_id, current_node_id, rg_code,
            setYear, rg_code, setYear });
          if (num < 1)
            throw new Exception("不能走入下一流程节点，系统内部错误!");

          // 删除完成任务列表中的上一节点的数据
          delete_old_sql
            .append(" delete from sys_wf_complete_tasks")
            .append(Tools.addDbLink())
            .append(" c  where c.task_id in (")
            .append(" select b.task_id from sys_wf_current_tasks")
            .append(Tools.addDbLink())
            .append(" a,sys_wf_task_routing")
            .append(Tools.addDbLink())
            .append(" b")
            .append(
              " where a.entity_id =? and a.wf_id=? and a.current_node_id=? and a.task_id=b.next_task_id and a.is_undo = 1 and a.rg_code=? and a.set_year=?)")
            .append(" and c.rg_code=? and c.set_year=?");
          num = dao.executeBySql(delete_old_sql.toString(), new Object[] { entityId, wf_id, current_node_id, rg_code,
            setYear, rg_code, setYear });
          if (num < 1)
            throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 将当前任务列表中的UNDO的数据复制到完成列表中
        insert_sql
          .append("insert into sys_wf_complete_tasks")
          .append(Tools.addDbLink())
          .append(" (TASK_ID,WF_ID,WF_TABLE_NAME,ENTITY_ID,")
          .append("CURRENT_NODE_ID,NEXT_NODE_ID,ACTION_TYPE_CODE,IS_UNDO,CREATE_USER,CREATE_DATE,UNDO_USER,")
          .append("UNDO_DATE,OPERATION_NAME,OPERATION_DATE,OPERATION_REMARK,INIT_MONEY,RESULT_MONEY,REMARK,")
          .append("TOLLY_FLAG,BILL_TYPE_CODE,BILL_ID,RCID,CCID,CREATE_USER_ID,SET_YEAR,RG_CODE) ")
          .append(" select TASK_ID, WF_ID,  WF_TABLE_NAME,  ENTITY_ID,  CURRENT_NODE_ID,  NEXT_NODE_ID,")
          .append(" ACTION_TYPE_CODE, 1,  CREATE_USER,  CREATE_DATE,  '")
          .append(create_user)
          .append("', '")
          .append(Tools.getCurrDate())
          .append("',")
          .append(
            " OPERATION_NAME, OPERATION_DATE, OPERATION_REMARK, INIT_MONEY, RESULT_MONEY, REMARK ,TOLLY_FLAG , BILL_TYPE_CODE,bill_id,rcid,ccid, CREATE_USER_ID, ")
          .append(setYear).append(", '").append(rg_code).append("'").append(" from   sys_wf_current_tasks")
          .append(Tools.addDbLink()).append("   where  entity_id ='").append(entityId).append("'  and wf_id='")
          .append(wf_id).append("'  and current_node_id='").append(current_node_id)
          .append("' and is_undo=1 and rg_code=? and set_year=?");
        num = dao.executeBySql(insert_sql.toString(), new Object[] { rg_code, setYear });
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 删除当前任务列表中的UNDO的数据
        delete_sql
          .append("delete from  sys_wf_current_tasks")
          .append(Tools.addDbLink())
          .append(
            "  b where b.entity_id =? and b.wf_id=? and b.current_node_id=? and is_undo=1 and b.rg_code=? and b.set_year=?");
        num = dao.executeBySql(delete_sql.toString(),
          new Object[] { entityId, wf_id, current_node_id, rg_code, setYear });
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        String sql4OperateItemTable = "select task_id,CURRENT_NODE_ID, NEXT_NODE_ID,BILL_ID from sys_wf_current_tasks "
          + " where   entity_id =? and wf_id=? and next_node_id=?  and rg_code=? and set_year=?";

        List list = this.dao.findBySql(sql4OperateItemTable, new Object[] { entityId, wf_id, current_node_id, rg_code,
          setYear });

        Map map = null;
        if (list.size() > 0) {
          Iterator it = list.iterator();
          while (it.hasNext()) {
            map = (Map) it.next();
            if (null != map.get("bill_id")) {
              billId = map.get("bill_id").toString();
            }
            saveOptionCurrentAndComleteTable(null, map.get("task_id").toString(), wf_id, entityId,
              map.get("current_node_id").toString(), map.get("next_node_id").toString(), "RECALL", billId,
              inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());
          }
        } else {
          sql4OperateItemTable = "select task_id,CURRENT_NODE_ID, NEXT_NODE_ID,BILL_ID from sys_wf_current_tasks "
            + " where   entity_id =? and wf_id=? and current_node_id=?  and rg_code=? and set_year=?";

          list = this.dao.findBySql(sql4OperateItemTable, new Object[] { entityId, wf_id, current_node_id, rg_code,
            setYear });
          Iterator it = list.iterator();
          while (it.hasNext()) {
            map = (Map) it.next();
            if (null != map.get("bill_id")) {
              billId = map.get("bill_id").toString();
            }
            saveOptionCurrentAndComleteTable(null, map.get("task_id").toString(), wf_id, entityId,
              map.get("current_node_id").toString(), map.get("next_node_id").toString(), "RECALL", billId,
              inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());
          }
        }
        /**
         * add by bing-zeng 2008-02-04 对工作流ITEM表进行操作 end
         */
        return return_dto;
      } else {
        // 撤销的是以确认节点的操作，则将原来的sys_wf_current_tasks修改为sys_wf_end_tasks
        // 查询是否可以进行撤销操作
        select_sql.append("select distinct b.wf_id, b.current_node_id, b.next_node_id, b.remark ")
          .append(" from sys_wf_end_tasks").append(Tools.addDbLink()).append(" b, vw_sys_wf_node_relation")
          .append(Tools.addDbLink()).append(" d ")
          .append(" where d.module_id = ? and d.role_id = ? and b.current_node_id=d.node_id ")
          .append(" and b.entity_id =? and b.rg_code=? and b.set_year=?");
        rs = dao.findBySql(select_sql.toString(), new Object[] { menuid, roleid, entityId, rg_code, setYear });

        if (rs.size() == 0) {
          throw new Exception("不能走入下一流程节点，原因有：未找到该数据；或者该角色没有此权限!");
        } else {
          // 得到当前需要撤销的数据的流程号和节点号
          wf_id = ((Map) rs.get(0)).get("wf_id").toString();
          current_node_id = ((Map) rs.get(0)).get("current_node_id").toString();
        }

        return_dto = (Object) inputFVoucherDto;

        // 将以确认任务列表的当前数据置上UNDO标志
        update_sql.append("update  sys_wf_end_tasks").append(Tools.addDbLink())
          .append("  b set b.is_undo=1 where b.entity_id =? ")
          .append(" and b.wf_id=? and b.current_node_id=? and b.rg_code=? and b.set_year=?");
        num = dao.executeBySql(update_sql.toString(),
          new Object[] { entityId, wf_id, current_node_id, rg_code, setYear });
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 如果能找到上一节点数据
        if (has_old_dto) {
          // 将完成任务列表中的上一节点的数据复制到当前任务列表
          StringBuffer insert_old_sql = new StringBuffer();
          StringBuffer delete_old_sql = new StringBuffer();
          // 即使撤销的是以确认的数据，但是撤销后它的next_node_id不是结束结点，所以把数据复制到当前任务列表
          insert_old_sql
            .append(" insert into sys_wf_current_tasks")
            .append(Tools.addDbLink())
            .append("   select * from sys_wf_complete_tasks")
            .append(Tools.addDbLink())
            .append(" c  where c.task_id in (")
            .append(" select b.task_id from sys_wf_end_tasks")
            .append(Tools.addDbLink())
            .append(" a,sys_wf_task_routing")
            .append(Tools.addDbLink())
            .append(" b")
            .append(" where a.entity_id =?")
            .append(" and a.wf_id=?")
            .append(
              " and a.current_node_id=? and a.task_id=b.next_task_id and a.rg_code=? and a.set_year=?) and c.rg_code=? and c.set_year=?");
          num = dao.executeBySql(insert_old_sql.toString(), new Object[] { entityId, wf_id, current_node_id, rg_code,
            setYear, rg_code, setYear });
          if (num < 1)
            throw new Exception("不能走入下一流程节点，系统内部错误!");

          // 删除完成任务列表中的上一节点的数据
          delete_old_sql
            .append(" delete from sys_wf_complete_tasks")
            .append(Tools.addDbLink())
            .append(" c  where c.task_id in (")
            .append(" select b.task_id from sys_wf_end_tasks")
            .append(Tools.addDbLink())
            .append(" a,sys_wf_task_routing")
            .append(Tools.addDbLink())
            .append(" b")
            .append(
              " where a.entity_id =? and a.wf_id=? and a.current_node_id=? and a.task_id=b.next_task_id and a.is_undo = 1 and a.rg_code=? and a.set_year=?) and c.rg_code=? and c.set_year=?");
          num = dao.executeBySql(delete_old_sql.toString(), new Object[] { entityId, wf_id, current_node_id, rg_code,
            setYear, rg_code, setYear });
          if (num < 1)
            throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 将以确认任务列表中的UNDO的数据复制到完成列表中
        insert_sql
          .append("insert into sys_wf_complete_tasks")
          .append(Tools.addDbLink())
          .append(" (TASK_ID,WF_ID,WF_TABLE_NAME,ENTITY_ID,")
          .append("CURRENT_NODE_ID,NEXT_NODE_ID,ACTION_TYPE_CODE,IS_UNDO,CREATE_USER,CREATE_DATE,UNDO_USER,")
          .append("UNDO_DATE,OPERATION_NAME,OPERATION_DATE,OPERATION_REMARK,INIT_MONEY,RESULT_MONEY,REMARK,")
          .append("TOLLY_FLAG,BILL_TYPE_CODE,BILL_ID,RCID,CCID,CREATE_USER_ID, SET_YEAR, RG_CODE) ")
          .append(" select TASK_ID, WF_ID,  WF_TABLE_NAME,  ENTITY_ID,  CURRENT_NODE_ID,  NEXT_NODE_ID,")
          .append(" ACTION_TYPE_CODE, 1,  CREATE_USER,  CREATE_DATE,  '")
          .append(create_user)
          .append("', '")
          .append(Tools.getCurrDate())
          .append("',")
          .append(
            " OPERATION_NAME, OPERATION_DATE, OPERATION_REMARK, INIT_MONEY, RESULT_MONEY, REMARK ,TOLLY_FLAG , BILL_TYPE_CODE,bill_id,rcid,ccid,CREATE_USER_ID, ")
          .append(setYear).append(", '").append(rg_code).append("'").append(" from   sys_wf_end_tasks")
          .append(Tools.addDbLink()).append("   where  entity_id ='").append(entityId).append("'  and wf_id='")
          .append(wf_id).append("'  and current_node_id='").append(current_node_id)
          .append("' and is_undo=1 and rg_code=? and set_year=?");
        num = dao.executeBySql(insert_sql.toString(), new Object[] { rg_code, setYear });
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 删除以确认任务列表中的UNDO的数据
        delete_sql
          .append("delete from  sys_wf_end_tasks")
          .append(Tools.addDbLink())
          .append(
            "  b where b.entity_id =? and b.wf_id=? and b.current_node_id=? and is_undo=1 and rg_code=? and set_year=?");
        num = dao.executeBySql(delete_sql.toString(),
          new Object[] { entityId, wf_id, current_node_id, rg_code, setYear });
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        String sql4OperateItemTable = "select task_id,CURRENT_NODE_ID, NEXT_NODE_ID,BILL_ID from sys_wf_current_tasks "
          + " where   entity_id =? and wf_id=? and next_node_id=? and  rg_code=? and set_year=?";

        List list = this.dao.findBySql(sql4OperateItemTable, new Object[] { entityId, wf_id, current_node_id, rg_code,
          setYear });
        Map map = null;

        if (list.size() > 0) {
          Iterator it = list.iterator();
          while (it.hasNext()) {
            map = (Map) it.next();
            if (null != map.get("bill_id")) {
              billId = map.get("bill_id").toString();
            }
            saveOptionCurrentAndComleteTable(null, map.get("task_id").toString(), wf_id, entityId,
              map.get("current_node_id").toString(), map.get("next_node_id").toString(), "RECALL", billId,
              inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());
          }
        } else {
          sql4OperateItemTable = "select task_id,CURRENT_NODE_ID, NEXT_NODE_ID,BILL_ID from sys_wf_current_tasks "
            + " where   entity_id =? and wf_id=? and current_node_id=?  and rg_code=? and set_year=?";

          list = this.dao.findBySql(sql4OperateItemTable, new Object[] { entityId, wf_id, current_node_id, rg_code,
            setYear });
          if (list.size() > 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
              map = (Map) it.next();
              if (null != map.get("bill_id")) {
                billId = map.get("bill_id").toString();
              }
              saveOptionCurrentAndComleteTable(null, map.get("task_id").toString(), wf_id, entityId,
                map.get("current_node_id").toString(), map.get("next_node_id").toString(), "RECALL", billId,
                inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());
            }
          }
        }
        return return_dto;
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    } finally {
      if (tmp_session != null) {
        dao.closeSession(tmp_session);
      }

    }

  }

  /**
   * 得到数据的操作日志信息。不用传表名
   * 
   * @param entityid-----------数据ID
   * @return List对象（内部含FWFLogDTO)
   * @throws Exception---------错误信息
   */
  public List getWfLogDTOSimply(String EntityId) throws FAppException {
    return getWfLogDTO(null, EntityId);
  }

  /**
   * 得到数据的操作日志信息。
   * 
   * @param tablename----------表名称
   * @param entityid-----------数据ID
   * @return List对象（内部含FWFLogDTO)
   * @throws Exception---------错误信息
   */
  public List getWfLogDTO(String TableName, String EntityId) throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    List return_list = new ArrayList();
    try {
      StringBuffer select_sql = new StringBuffer();
      String userId = SessionUtil.getUserInfoContext().getUserID().toString();
      select_sql
        .append("select distinct a.*,u.user_code,u.user_name,u.telephone,u.mobile")
        .append(
          "        from (select '' task_id,t.wf_id,t.wf_table_name,t.entity_id,t.current_node_id as node_id ,t.action_type_code,")
        .append(
          "               0 as is_undo,t.create_user,t.create_date,t.operation_name,t.operation_date,t.operation_remark,")
        .append("               t.result_money, t.bill_type_code,t.bill_id,")
        .append("               (select a.wf_name  from sys_wf_flows")
        .append(Tools.addDbLink())
        .append(" a  where a.wf_id = t.wf_id and a.rg_code=t.rg_code and a.set_year=t.set_year) as wf_name,")
        .append("               (select b.node_name  from sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(
          " b  where b.node_id = t.current_node_id and b.rg_code=t.rg_code and b.set_year=t.set_year) as node_name,")
        //begin_20170716
        .append("               (select b.node_code  from sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(
          " b  where b.node_id = t.current_node_id and b.rg_code=t.rg_code and b.set_year=t.set_year) as node_code,")
        //end_20170716
        .append("               (select c.enu_name from sys_enumerate")
        .append(Tools.addDbLink())
        .append(" c where c.field_code='ACTION_TYPE'  AND c.enu_code= t.action_type_code) as action_type_name")
        .append("          from vw_sys_wf_current_end_tasks")
        .append(Tools.addDbLink())
        .append(" t where t.rg_code=? and t.set_year=?")
        .append("        UNION")
        .append(
          "        select  '' task_id,t.wf_id, t.wf_table_name, t.entity_id,t.current_node_id as node_id, t.action_type_code,")
        .append(
          "               t.is_undo,t.undo_user as create_user,t.undo_date as create_date,'撤销' as operation_name,")
        .append(
          "               '' as operation_date,'撤销' as operation_remark, t.result_money, t.bill_type_code,t.bill_id,")
        .append("               (select a.wf_name  from sys_wf_flows")
        .append(Tools.addDbLink())
        .append(" a  where a.wf_id = t.wf_id and a.rg_code=t.rg_code and a.set_year=t.set_year) as wf_name,")
        .append("               (select b.node_name  from sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(
          " b  where b.node_id = t.current_node_id and b.rg_code=t.rg_code and b.set_year=t.set_year) as node_name,")
        //begin_20170716
        .append("               (select b.node_code  from sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(
          " b  where b.node_id = t.current_node_id and b.rg_code=t.rg_code and b.set_year=t.set_year) as node_code,")
        //end_20170716
        .append("               (select c.enu_name from sys_enumerate")
        .append(Tools.addDbLink())
        .append(" c where c.field_code='ACTION_TYPE'  AND c.enu_code= t.action_type_code) as action_type_name")
        .append("          from vw_sys_wf_current_end_tasks")
        .append(Tools.addDbLink())
        .append(" t")
        .append("         where t.is_undo = 1 and t.rg_code=? and t.set_year=?")
        .append("         union")
        .append(
          "        select '' task_id,t.wf_id,t.wf_table_name,t.entity_id,t.current_node_id as node_id,t.action_type_code,")
        .append(
          "               0 as is_undo,t.create_user,t.create_date,t.operation_name,t.operation_date,t.operation_remark,")
        .append("               t.result_money, t.bill_type_code,t.bill_id,")
        .append("               (select a.wf_name  from sys_wf_flows")
        .append(Tools.addDbLink())
        .append(" a  where a.wf_id = t.wf_id  and a.rg_code=t.rg_code and a.set_year=t.set_year) as wf_name,")
        .append("               (select b.node_name  from sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(
          " b  where b.node_id = t.current_node_id  and b.rg_code=t.rg_code and b.set_year=t.set_year) as node_name,")
        //begin_20170716
        .append("               (select b.node_code  from sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(
          " b  where b.node_id = t.current_node_id and b.rg_code=t.rg_code and b.set_year=t.set_year) as node_code,")
        //end_20170716
        .append("               (select c.enu_name from sys_enumerate")
        .append(Tools.addDbLink())
        .append(" c where c.field_code='ACTION_TYPE'  AND c.enu_code= t.action_type_code) as action_type_name")
        .append("          from sys_wf_complete_tasks")
        .append(Tools.addDbLink())
        .append(" t  where t.rg_code=? and t.set_year=?")
        .append("        UNION")
        .append(
          "        select  '' task_id,t.wf_id, t.wf_table_name, t.entity_id,t.current_node_id as node_id, t.action_type_code,")
        .append(
          "               t.is_undo,t.undo_user as create_user,t.undo_date as create_date,'撤销' as operation_name,")
        .append(
          "               t.undo_date as operation_date,'撤销' as operation_remark, t.result_money, t.bill_type_code,t.bill_id,")
        //取操作日期
        .append("               (select a.wf_name  from sys_wf_flows")
        .append(Tools.addDbLink())
        .append(" a  where a.wf_id = t.wf_id  and a.rg_code=t.rg_code and a.set_year=t.set_year) as wf_name,")
        .append("               (select b.node_name  from sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(
          " b  where b.node_id = t.current_node_id and b.rg_code=t.rg_code and b.set_year=t.set_year) as node_name,")
        //begin_20170716
        .append("               (select b.node_code  from sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(
          " b  where b.node_id = t.current_node_id and b.rg_code=t.rg_code and b.set_year=t.set_year) as node_code,")
        //end_20170716
        .append("               (select c.enu_name from sys_enumerate").append(Tools.addDbLink())
        .append(" c where c.field_code='ACTION_TYPE'  AND c.enu_code= t.action_type_code) as action_type_name")
        .append("          from sys_wf_complete_tasks").append(Tools.addDbLink()).append(" t")
        .append("         where t.is_undo = 1  and t.rg_code=? and t.set_year=?  ) a,sys_usermanage u")
        //update by xuzx1 拼sql时 user_id是字符串，变量userId没有加入单引号
        .append(Tools.addDbLink()).append(" where 1=1  and u.user_id= ").append("'" + userId + "'")
        .append(" and entity_id=? ");

      // add by wanghongtao 查询日志时加表名过滤
      List list = null;
      if (TableName != null && !"".equals(TableName)) {
        select_sql.append(" and upper(wf_table_name) = upper(?) ").append(
          " order by operation_date, task_id, is_undo,action_type_code "); //根据操作日期排序
        list = dao.findBySql(select_sql.toString(), new Object[] { rg_code, setYear, rg_code, setYear, rg_code,
          setYear, rg_code, setYear, EntityId, TableName });
      } else {
        select_sql.append(" order by create_date, task_id, is_undo,action_type_code ");
        list = dao.findBySql(select_sql.toString(), new Object[] { rg_code, setYear, rg_code, setYear, rg_code,
          setYear, rg_code, setYear, EntityId });
      }

      if (list.size() == 0)
        throw new Exception("没有找到数据信息!");
      else {
        for (int i = 0; i < list.size(); i++) {
          FWFLogDTO logdto = new FWFLogDTO();
          logdto.copy((Map) list.get(i));
          return_list.add(logdto);
        }

        return return_list;
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 得到传入数据所处的节点及状态信息。
   * 
   * @param tablename----------表名称
   * @param entityid-----------数据ID
   * @return List对象（内部含FWFLogDTO)
   * @throws Exception---------错误信息
   */
  public List getCurrNodeInfoDTO(String TableName, String EntityId) throws FAppException {
    List tmp_return_list = new ArrayList();
    List return_list = new ArrayList();
    String rg_code = getRgCode();
    String setYear = getSetYear();

    // 得到当前操作节点的状态信息－－已确认，被退回，已修改，已挂起，已删除，已作废
    StringBuffer select_sql = new StringBuffer();
    select_sql
      .append(
        "select t.wf_table_name as table_name,t.action_type_code,t.entity_id,a.wf_id,a.wf_name,b.node_id,b.node_name,b.node_type,b.gather_flag")
      .append("  from sys_wf_current_tasks").append(Tools.addDbLink()).append(" t ,sys_wf_flows")
      .append(Tools.addDbLink()).append(" a,sys_wf_nodes").append(Tools.addDbLink()).append(" b")
      .append(" where t.wf_id=a.wf_id and t.wf_id=b.wf_id and t.current_node_id=b.node_id")
      .append(" and t.entity_id=?  and t.rg_code=? and t.set_year=? and a.rg_code=? and a.set_year=?");
    try {
      List list = dao.findBySql(select_sql.toString(), new Object[] { EntityId, rg_code, setYear, rg_code, setYear });
      for (int i = 0; i < list.size(); i++) {
        XMLData rowData = new XMLData();
        rowData = (XMLData) list.get(i);
        String table_name = rowData.get("table_name").toString();
        String wf_id = rowData.get("wf_id").toString();
        String node_id = rowData.get("node_id").toString();
        String action_type_code = rowData.get("action_type_code").toString();
        String gather_flag = rowData.get("gather_flag").toString();
        List list_tmp = null;

        if (action_type_code.equals("INPUT") || action_type_code.equals("NEXT")) {
          list_tmp = dao.findBySql(
            "select * from sys_wf_current_tasks" + Tools.addDbLink() + " own where own.entity_id='" + EntityId
              + "'  and ( "
              + this.getTasksBySqlByNode(wf_id, node_id, table_name, "entity_id", "002", "own", gather_flag)
              + ") and rg_code=? and set_year=?", new Object[] { rg_code, setYear });
          if (list_tmp.size() > 0) {
            rowData.put("status_code", "002");
            rowData.put("status_name", "已确认");
            tmp_return_list.add(rowData);
            continue;
          }
        }

        if (action_type_code.equals("BACK")) {
          list_tmp = dao.findBySql(
            "select * from sys_wf_current_tasks" + Tools.addDbLink() + " own where own.entity_id='" + EntityId
              + "'  and ("
              + this.getTasksBySqlByNode(wf_id, node_id, table_name, "entity_id", "004", "own", gather_flag)
              + ") and rg_code=? and set_year=?", new Object[] { rg_code, setYear });
          if (list_tmp.size() > 0) {
            rowData.put("status_code", "004");
            rowData.put("status_name", "被退回");
            tmp_return_list.add(rowData);
            continue;
          }
        }

        if (action_type_code.equals("EDIT")) {
          rowData.put("status_code", "005");
          rowData.put("status_name", "已修改");
          tmp_return_list.add(rowData);
          continue;
        }

        if (action_type_code.equals("HANG")) {
          rowData.put("status_code", "101");
          rowData.put("status_name", "已挂起");
          tmp_return_list.add(rowData);
          continue;
        }

        if (action_type_code.equals("DELETE")) {
          rowData.put("status_code", "102");
          rowData.put("status_name", "已删除");
          tmp_return_list.add(rowData);
          continue;
        }

        if (action_type_code.equals("DISCARD")) {
          rowData.put("status_code", "103");
          rowData.put("status_name", "已作废");
          tmp_return_list.add(rowData);
          continue;
        }
      }

      // 得到下一节点的状态信息－－未确认，已退回
      StringBuffer select_sql1 = new StringBuffer();
      select_sql1
        .append(
          "select t.wf_table_name as table_name,t.action_type_code,t.entity_id,a.wf_id,a.wf_name,b.node_id,b.node_name,b.node_type,b.gather_flag")
        .append("  from sys_wf_current_tasks")
        .append(Tools.addDbLink())
        .append(" t ,sys_wf_flows")
        .append(Tools.addDbLink())
        .append(" a,sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(" b")
        .append(" where t.wf_id=a.wf_id and t.wf_id=b.wf_id and t.next_node_id<>'0' and t.next_node_id=b.node_id  ")
        .append(" and t.entity_id='" + EntityId + "' and t.rg_code=? and t.set_year=? and a.rg_code=? and a.set_year=?");

      list = dao.findBySql(select_sql1.toString(), new Object[] { rg_code, setYear, rg_code, setYear });
      for (int i = 0; i < list.size(); i++) {
        XMLData rowData = new XMLData();
        rowData = (XMLData) list.get(i);
        String table_name = rowData.get("table_name").toString();
        String wf_id = rowData.get("wf_id").toString();
        String node_id = rowData.get("node_id").toString();
        String action_type_code = rowData.get("action_type_code").toString();
        String gather_flag = rowData.get("gather_flag").toString();
        List list_tmp = null;

        if (action_type_code.equals("INPUT") || action_type_code.equals("NEXT")) {
          list_tmp = dao.findBySql(
            "select * from sys_wf_current_tasks" + Tools.addDbLink() + " own where own.entity_id='" + EntityId
              + "'  and ( "
              + this.getTasksBySqlByNode(wf_id, node_id, table_name, "entity_id", "001", "own", gather_flag)
              + ") and rg_code=? and set_year=?", new Object[] { rg_code, setYear });
          if (list_tmp.size() > 0) {
            rowData.put("status_code", "001");
            rowData.put("status_name", "未确认");
            tmp_return_list.add(rowData);
            continue;
          }
        }

        if (action_type_code.equals("BACK")) {
          list_tmp = dao.findBySql(
            "select * from sys_wf_current_tasks" + Tools.addDbLink() + " own where own.entity_id='" + EntityId
              + "'  and ( "
              + this.getTasksBySqlByNode(wf_id, node_id, table_name, "entity_id", "003", "own", gather_flag)
              + ") and rg_code=? and set_year=?", new Object[] { rg_code, setYear });
          if (list_tmp.size() > 0) {
            rowData.put("status_code", "003");
            rowData.put("status_name", "已退回");
            tmp_return_list.add(rowData);
            continue;
          }
        }
      }

      // 如果找不到数据，给出提示
      if (tmp_return_list.size() == 0) {
        throw new Exception("该数据不存在！");
      }

      // 转成DTO
      for (int i = 0; i < tmp_return_list.size(); i++) {
        FWFLogDTO logdto = new FWFLogDTO();
        logdto.copy((Map) tmp_return_list.get(i));
        return_list.add(logdto);
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
    return return_list;
  }

  /**
   * 通用正向处理操作（对批量数据）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDtos--业务数据DTO列表
   * @param isForced2Execute
   *            是否强制执行（不考虑菜单，角色）
   * @return list
   * @throws Exception---------错误信息
   */
  private List doBatchProcess(String old_current_node_id, String menuid, String roleid, String actiontypeid,
    String operationname, String operationdate, String operationremark, String bill_table_name,
    String detail_table_name, List inputFVoucherDtos, boolean auto_tolly_flag, boolean auto_create_ccid,
    boolean auto_create_rcid, boolean isForced2Execute) throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    Session session = dao.getSession();
    Connection conn = session.connection();
    try {

      List return_list = new ArrayList();
      if (inputFVoucherDtos == null || inputFVoucherDtos.size() == 0) {
        throw new Exception("需要传入inputFVoucherDtos!");
      }

      // 批处理SQL
      StringBuffer insertCurrentTaskSql = new StringBuffer();
      insertCurrentTaskSql
        .append("insert into sys_wf_current_tasks")
        .append(Tools.addDbLink())
        .append(" (TASK_ID,WF_ID,WF_TABLE_NAME,ENTITY_ID,")
        .append("CURRENT_NODE_ID,NEXT_NODE_ID,ACTION_TYPE_CODE,IS_UNDO,CREATE_USER,CREATE_DATE,UNDO_USER,")
        .append("UNDO_DATE,OPERATION_NAME,OPERATION_DATE,OPERATION_REMARK,INIT_MONEY,RESULT_MONEY,REMARK,")
        .append(
          "TOLLY_FLAG,BILL_TYPE_CODE,BILL_ID,RCID,CCID,SEND_MSG_DATE,AUTO_AUDIT_DATE,SET_MONTH,UPDATE_FLAG,CREATE_USER_ID,RG_CODE,SET_YEAR) ")
        .append("values( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )");
      PreparedStatement insertCurrentTaskPsmt = conn.prepareStatement(insertCurrentTaskSql.toString());
      Statement setRoutingPsmt = conn.createStatement();

      if (auto_create_ccid) {
        List tmp_dto = new ArrayList();
        Iterator itr = inputFVoucherDtos.iterator();
        Iterator itr_detail;
        // 整理单和明细所有dto
        while (itr.hasNext()) {
          FVoucherDTO inputFVoucherDto = (FVoucherDTO) itr.next();

          // 设置coa_id
          inputFVoucherDto.setCoa_id(this.getCOAIDByBillTypeCode(inputFVoucherDto.getBilltype_code()));

          tmp_dto.add(inputFVoucherDto);
          if (inputFVoucherDto.getDetails() != null && inputFVoucherDto.getDetails().size() > 0) {
            itr_detail = inputFVoucherDto.getDetails().iterator();
            while (itr_detail.hasNext()) {
              tmp_dto.add((FVoucherDTO) itr_detail.next());
            }
          }
        }
        // 批量产生ccid;
        icoaService.getCcidBatch(tmp_dto);
      }
      //批量产生ccid end

      // 自动生成RCID(注:可应用于录入及要素变更等情况)
      if (auto_create_rcid) {
        for (int p = 0; p < inputFVoucherDtos.size(); p++) {
          FVoucherDTO inputFVoucherDto1 = (FVoucherDTO) inputFVoucherDtos.get(p);
          String rcid1 = idataright.getRcid(inputFVoucherDto1.toXMLData());
          if (rcid1 != null && !rcid1.equals("")) {
            inputFVoucherDto1.setRcid(rcid1);
          }

          if (inputFVoucherDto1.getDetails() != null && inputFVoucherDto1.getDetails().size() > 0) {
            for (int q = 0; q < inputFVoucherDto1.getDetails().size(); q++) {
              FVoucherDTO inputFVoucherDto2 = (FVoucherDTO) inputFVoucherDto1.getDetails().get(q);
              String rcid2 = idataright.getRcid(inputFVoucherDto2.toXMLData());
              if (rcid2 != null && !rcid2.equals("")) {
                inputFVoucherDto2.setRcid(rcid2);
              }
            }
          }
        }

      }

      String billId = null;
      if (auto_tolly_flag) {
        // 根据记账类型不同对其处理方式不同
        inputFVoucherDtos = this.doTolly(menuid, roleid, actiontypeid, bill_table_name, detail_table_name,
          inputFVoucherDtos);// lgc add
      }
      // 循环调用
      for (int i = 0; i < inputFVoucherDtos.size(); i++) {
        FVoucherDTO inputFVoucherDto = new FVoucherDTO();
        inputFVoucherDto = (FVoucherDTO) inputFVoucherDtos.get(i);
        FVoucherDTO returnFVoucherDto = doSingleProcess(insertCurrentTaskPsmt, setRoutingPsmt, old_current_node_id,
          menuid, roleid, actiontypeid, operationname, operationdate, operationremark, bill_table_name,
          detail_table_name, inputFVoucherDto, auto_tolly_flag, auto_create_ccid, auto_create_rcid, null,
          isForced2Execute, null, String.valueOf(inputFVoucherDto.getIs_end()), billId);
        if (returnFVoucherDto != null) {
          return_list.add(returnFVoucherDto);
        }
      }

      // 执行批处理SQL
      insertCurrentTaskPsmt.executeBatch();
      if (!actiontypeid.equals("INPUT"))
        setRoutingPsmt.executeBatch();
      /*
       * 先将这个流程的数据插入到sys_wf_current_tasks中，最后再转到sys_wf_end_tasks
       */
      if ("NEXT".equals(actiontypeid)) {
        StringBuffer insertEndTaskSql = new StringBuffer();
        StringBuffer deleteCurrentSql = new StringBuffer();
        // 如果current里面有以确认的信息，则把它们移到end表中，同事把current里面的纪录删除
        insertEndTaskSql.append("insert into sys_wf_end_tasks select t.* from sys_wf_current_tasks t")
          .append(" where exists (select 1 from  sys_wf_nodes w")
          .append(" where t.next_node_id = w.node_id and w.node_type = '003') and t.rg_code=? and t.set_year=?");
        // .append(" and t.entity_id = ?)");
        deleteCurrentSql.append("delete sys_wf_current_tasks t where exists").append(" (select 1 from  sys_wf_nodes w")
          .append(" where t.next_node_id = w.node_id and w.node_type = '003') and t.rg_code=? and t.set_year=?");
        dao.executeBySql(insertEndTaskSql.toString(), new Object[] { rg_code, setYear });
        dao.executeBySql(deleteCurrentSql.toString(), new Object[] { rg_code, setYear });
      }
      return return_list;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      e.printStackTrace();
      throw new FAppException(e);
    } finally {
      dao.closeSession(session);
    }
  }

  /**
   * 得到记帐类型 -1 不记帐 0 在途记帐 1 终审记帐
   * 
   * @param menuid
   * @param roleid
   * @param actiontypeid
   * @param bill_table_name
   * @param detail_table_name
   * @param inputFVoucherDto
   * @return
   * @throws Exception
   */
  public String getTollyType(String menuid, String roleid, String actiontypeid, String bill_table_name,
    String detail_table_name, FVoucherDTO inputFVoucherDto) throws Exception {
    // // 自动记帐
    String tolly_flag = "-1";
    // FVoucherDTO inputFVoucherDto = (FVoucherDTO)inputFVoucherDtos.get(0);
    // 根据传入参数取得记帐类型
    if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
      tolly_flag = this.getTollyFlag("", menuid, roleid, actiontypeid, bill_table_name, detail_table_name,
        inputFVoucherDto);
    } else {
      tolly_flag = this.getTollyFlag("", menuid, roleid, actiontypeid, bill_table_name, detail_table_name,
        (FVoucherDTO) inputFVoucherDto.getDetails().get(0));
    }

    return tolly_flag;
  }

  /**
   * 通用正向处理操作（对批量数据,不用传TABLE_NAME）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param inputFVoucherDtos--业务数据DTO列表
   * @param auto_tolly_flag----是否通过工作流自动记账
   * @param isForced2Execute
   *            是否强制执行（不考虑菜单，角色）
   * @return list
   * @throws Exception---------错误信息
   */
  private List doBatchProcessWithNoTableName(String old_current_node_id, String menuid, String roleid,
    String actiontypeid, String operationname, String operationdate, String operationremark, List inputFVoucherDtos,
    boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid, List control_wf_info,
    boolean isForced2Execute) throws FAppException {
    Session session = dao.getSession();
    Connection conn = session.connection();
    String rg_code = getRgCode();
    String setYear = getSetYear();
    try {
      List return_list = new ArrayList();
      if (inputFVoucherDtos == null || inputFVoucherDtos.size() == 0) {
        throw new Exception("需要传入inputFVoucherDtos!");
      }
      // 批处理SQL
      StringBuffer insertCurrentTaskSql = new StringBuffer();
      insertCurrentTaskSql
        .append("insert into sys_wf_current_tasks")
        .append(Tools.addDbLink())
        .append(" (TASK_ID,WF_ID,WF_TABLE_NAME,ENTITY_ID,")
        .append("CURRENT_NODE_ID,NEXT_NODE_ID,ACTION_TYPE_CODE,IS_UNDO,CREATE_USER,CREATE_DATE,UNDO_USER,")
        .append("UNDO_DATE,OPERATION_NAME,OPERATION_DATE,OPERATION_REMARK,INIT_MONEY,RESULT_MONEY,REMARK,")
        .append(
          "TOLLY_FLAG,BILL_TYPE_CODE,BILL_ID,RCID,CCID,SEND_MSG_DATE,AUTO_AUDIT_DATE,SET_MONTH,UPDATE_FLAG,CREATE_USER_ID,RG_CODE,SET_YEAR) ")
        .append("values( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )");
      PreparedStatement insertCurrentTaskPsmt = conn.prepareStatement(insertCurrentTaskSql.toString());
      Statement setRoutingPsmt = conn.createStatement();

      String detail_table_name = "";
      String bill_table_name = "";
      FVoucherDTO inputFVoucherDto = ((FVoucherDTO) inputFVoucherDtos.get(0));

      if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
        String billType_code = (String) (inputFVoucherDto.getBilltype_code());
        if (billType_code == null || billType_code.equals("")) {
          throw new Exception("没有传入billtype_code!");
        }
        String table_name = UtilCache.getTableNameByBillTypeCode(billType_code);
        if (table_name != null && !table_name.equals("")) {
          detail_table_name = table_name;
        } else {
          detail_table_name = ((FBillTypeDTO) billtype.getBillTypeByCode(billType_code)).getTable_name();
          UtilCache.putTableNameByBillTypeCode(billType_code, detail_table_name);
        }
      } else {

        String billType_code = (String) (((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code());
        if (billType_code == null || billType_code.equals("")) {
          throw new Exception("没有传入billtype_code!");
        }
        String table_name = UtilCache.getTableNameByBillTypeCode(billType_code);
        if (table_name != null && !table_name.equals("")) {
          detail_table_name = table_name;
        } else {
          detail_table_name = ((FBillTypeDTO) billtype.getBillTypeByCode(billType_code)).getTable_name();
          UtilCache.putTableNameByBillTypeCode(billType_code, detail_table_name);
        }

      }
      bill_table_name = detail_table_name;

      // justin 2008年4月28日修改批量产生ccid start
      if (auto_create_ccid) {
        List tmp_dto = new ArrayList();
        Iterator itr = inputFVoucherDtos.iterator();
        Iterator itr_detail;
        while (itr.hasNext()) {
          FVoucherDTO inputFVoucherDtoDto = (FVoucherDTO) itr.next();
          // 设置coa_id
          inputFVoucherDtoDto.setCoa_id(this.getCOAIDByBillTypeCode(inputFVoucherDtoDto.getBilltype_code()));

          tmp_dto.add(inputFVoucherDtoDto);
          if (inputFVoucherDtoDto.getDetails() != null && inputFVoucherDtoDto.getDetails().size() > 0) {
            itr_detail = inputFVoucherDtoDto.getDetails().iterator();
            while (itr_detail.hasNext()) {
              FVoucherDTO vou = (FVoucherDTO) itr_detail.next();
              // 给明细设置coa_id
              vou.setCoa_id(this.getCOAIDByBillTypeCode(vou.getBilltype_code()));
              tmp_dto.add(vou);
            }
          }
        }
        // 批量产生ccid
        icoaService.getCcidBatch(tmp_dto);
      }
      // 批量产生ccid end
      // 自动生成RCID(注:可应用于录入及要素变更等情况)
      if (auto_create_rcid) {
        for (int p = 0; p < inputFVoucherDtos.size(); p++) {
          FVoucherDTO inputFVoucherDto1 = (FVoucherDTO) inputFVoucherDtos.get(p);
          String rcid1 = idataright.getRcid(inputFVoucherDto1.toXMLData());
          if (rcid1 != null && !rcid1.equals("")) {
            inputFVoucherDto1.setRcid(rcid1);
          }

          if (inputFVoucherDto1.getDetails() != null && inputFVoucherDto1.getDetails().size() > 0) {
            for (int q = 0; q < inputFVoucherDto1.getDetails().size(); q++) {
              FVoucherDTO inputFVoucherDto2 = (FVoucherDTO) inputFVoucherDto1.getDetails().get(q);
              String rcid2 = idataright.getRcid(inputFVoucherDto2.toXMLData());
              if (rcid2 != null && !rcid2.equals("")) {
                inputFVoucherDto2.setRcid(rcid2);
              }
            }
          }
        }

      }
      // String billId = UUIDRandom.generate();
      String billId = null;
      // String tolly_flag= "-1";
      if (auto_tolly_flag) {
        // 根据记账类型不同对其处理方式不同
        inputFVoucherDtos = this.doTolly(menuid, roleid, actiontypeid, bill_table_name, detail_table_name,
          inputFVoucherDtos);// lgc add
      }

      // 循环调用
      for (int i = 0; i < inputFVoucherDtos.size(); i++) {
        inputFVoucherDto = new FVoucherDTO();
        inputFVoucherDto = (FVoucherDTO) inputFVoucherDtos.get(i);
        //        Log.error("循环送审"+i+" entityId="+inputFVoucherDto.getVou_id());
        //        System.out.println("循环送审"+i+" entityId="+inputFVoucherDto.getVou_id());
        FVoucherDTO returnFVoucherDto = doSingleProcess(insertCurrentTaskPsmt, setRoutingPsmt, old_current_node_id,
          menuid, roleid, actiontypeid, operationname, operationdate, operationremark, bill_table_name,
          detail_table_name, inputFVoucherDto, auto_tolly_flag, auto_create_ccid, auto_create_rcid, null,
          isForced2Execute, null, String.valueOf(inputFVoucherDto.getIs_end()), billId);
        if (returnFVoucherDto != null) {
          return_list.add(returnFVoucherDto);
        }
      }
      // 执行批处理SQL
      insertCurrentTaskPsmt.executeBatch();

      if (!actiontypeid.equals("INPUT"))
        setRoutingPsmt.executeBatch();
      /*
       * 先将这个流程的数据插入到sys_wf_current_tasks中，最后再转到sys_wf_end_tasks
       */
      if ("NEXT".equals(actiontypeid)) {
        List listTemp = new ArrayList();
        List listNextNodeId = dao.findBySql("select w.node_id from sys_wf_nodes w where w.node_type = '003'");
        if (listNextNodeId != null && listNextNodeId.size() > 0) {
          for (int i = 0; i < listNextNodeId.size(); i++) {
            XMLData data = (XMLData) listNextNodeId.get(i);
            Object obj = data.get("node_id");
            String next_node_id = "";
            if (obj != null) {
              next_node_id = (String) obj;
              List listNextNodeIdTemp = dao
                .findBySql(
                  "select t.next_node_id,t.entity_id from sys_wf_current_tasks t where t.rg_code=? and t.set_year=? and t.next_node_id = "
                    + next_node_id, new Object[] { rg_code, setYear });
              if (listNextNodeIdTemp != null && listNextNodeIdTemp.size() > 0) {
                for (int j = 0; j < listNextNodeIdTemp.size(); j++) {
                  listTemp.add(listNextNodeIdTemp.get(j));
                }
              }
            }
          }
        }
        if (listTemp != null && listTemp.size() > 0) {
          for (int i = 0; i < listTemp.size(); i++) {
            XMLData data = (XMLData) listTemp.get(i);
            if (data != null) {
              Object obj = data.get("next_node_id");
              String next_node_id = "";
              if (obj != null)
                next_node_id = (String) obj;
              if (next_node_id != null && !next_node_id.equals("")) {
                StringBuffer insertEndTaskSql = new StringBuffer();
                StringBuffer deleteCurrentSql = new StringBuffer();
                insertEndTaskSql
                  .append("insert into sys_wf_end_tasks select t.* from sys_wf_current_tasks t where t.rg_code = '"
                    + rg_code + "' and t.set_year = " + setYear + " and t.next_node_id = " + next_node_id);
                dao.executeBySql(insertEndTaskSql.toString());
                deleteCurrentSql.append("delete sys_wf_current_tasks t where t.rg_code = '" + rg_code
                  + "' and t.set_year = " + setYear + " and t.next_node_id = " + next_node_id);
                dao.executeBySql(deleteCurrentSql.toString());

              }
            }
          }

        }
      }
      return return_list;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    } finally {
      dao.closeSession(session);
    }

  }

  /**
   * 通用正向处理操作（对单条数据）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDto---业务数据DTO（单条）
   * @param isForced2Execute
   *            是否强制执行（不考虑菜单，角色）
   * @return FVoucherDTO
   * @throws Exception---------错误信息
   */
  private FVoucherDTO doSingleProcess(PreparedStatement insertCurrentTaskPsmt, Statement setRoutingPsmt,
    String old_current_node_id, String menuid, String roleid, String actiontypeid, String operationname,
    String operationdate, String operationremark, String bill_table_name, String detail_table_name,
    FVoucherDTO inputFVoucherDto, boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid,
    List control_wf_info, boolean isForced2Execute, List currentList, String tolly_flag, String billId)
    throws FAppException {
    try {
      if (actiontypeid == null || actiontypeid.equals("")) {
        throw new Exception("需要传入actiontypeid!");
      }

      for (int i = 0; inputFVoucherDto.getDetails() != null && i < inputFVoucherDto.getDetails().size(); i++) {

      }
      // 资金监控
      IInspectService inspectService = null;
      try {
        inspectService = (IInspectService) SessionUtil.getServerBean("sys.inspectService");
      } catch (Exception e) {
        // 访问不到监控包
      }
      // 如果访问不到监控类包，则不能调用其接口。
      if (inspectService != null) {
        List nodeInspectRules = null;
        if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
          nodeInspectRules = this.getInspectRules(old_current_node_id, menuid, roleid, actiontypeid, bill_table_name,
            detail_table_name, inputFVoucherDto);
        } else {
          nodeInspectRules = this.getInspectRules(old_current_node_id, menuid, roleid, actiontypeid, bill_table_name,
            detail_table_name, (FVoucherDTO) inputFVoucherDto.getDetails().get(0));
        }
        // 无论能否找到规则列表，都调用监控接口
        String inspectNodeId = null;
        if (nodeInspectRules != null && nodeInspectRules.size() > 0) {
          inspectNodeId = (String) ((XMLData) nodeInspectRules.get(0)).get("node_id");
        } else {
          if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
            inspectNodeId = getNextNodeId(old_current_node_id, menuid, roleid, actiontypeid, inputFVoucherDto,
              isForced2Execute);
          } else {
            inspectNodeId = getNextNodeId(old_current_node_id, menuid, roleid, actiontypeid,
              (FVoucherDTO) inputFVoucherDto.getDetails().get(0), isForced2Execute);
          }
        }
        String rtnMsg = inspectService.inspectInstance(getWfIdByNodeId(inspectNodeId), detail_table_name,
          inspectNodeId, null, actiontypeid, operationname, operationdate, operationremark, nodeInspectRules,
          inputFVoucherDto, menuid, roleid);
        inputFVoucherDto.setWarning(rtnMsg);
      }

      if (detail_table_name == null || detail_table_name.equals("")) {
        throw new Exception("需要传入detail_table_name!");
      }

      // 如果没有明细数据，则直接调工作流
      if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {

        billId = inputFVoucherDto.getVou_bill_id();
        doSingleProcessSimply(insertCurrentTaskPsmt, setRoutingPsmt, old_current_node_id, menuid, roleid, actiontypeid,
          operationname, operationdate, operationremark, detail_table_name, inputFVoucherDto, "detail", null,
          control_wf_info, tolly_flag, billId, isForced2Execute, currentList);

        return inputFVoucherDto;
      } else {
        // 先将明细走流程
        billId = inputFVoucherDto.getVou_id();
        for (int i = 0; i < inputFVoucherDto.getDetails().size(); i++) {
          FVoucherDTO detailDto = new FVoucherDTO();
          detailDto = (FVoucherDTO) inputFVoucherDto.getDetails().get(i);
          doSingleProcessSimply(insertCurrentTaskPsmt, setRoutingPsmt, old_current_node_id, menuid, roleid,
            actiontypeid, operationname, operationdate, operationremark, detail_table_name, detailDto, "detail", null,
            control_wf_info, tolly_flag, billId, isForced2Execute, currentList);
        }
        return inputFVoucherDto;
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }

  }

  /**
   * 通用正向处理操作（对单条数据录入操作）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDto---业务数据DTO（单条）
   * @param voutype------------单据类型
   * @param detail_wf_info-----明细数据的流程节点信息
   * @return true或false
   * @throws Exception---------错误信息
   */
  private void doSingleProcessSimplyInput(PreparedStatement insertCurrentTaskPsmt, String menuid, String roleid,
    String actiontypeid, String operationName, String operationDate, String operationRemark, String tableName,
    String vou_type, String entityId, String billId, String create_user, String create_date, String initMoney,
    String resultMoney, String remark, XMLData rowData, String tolly_flag, String bill_type_code, List detail_wf_info,
    List control_wf_info, List return_list, FVoucherDTO inputFVoucherDto, String createUserId) throws FAppException {

    // 传入的DTO中没有明细数据
    try {
      if (vou_type.equals("detail")) {
        this.setBillDetail(false);
        // 取前进列表开始
        List tmp_can_go_list = doSingleProcessSimplyInputDetailSelect(menuid, roleid, tableName, rowData);
        // 取前进列表结束
        if (tmp_can_go_list == null || tmp_can_go_list.size() == 0) {
          throw new Exception("数据ID为：" + entityId + "的数据根据设置无法找到对应的节点!");
        } else {
          Map tmp_more_wf = new HashMap();
          for (int i = 0; i < tmp_can_go_list.size(); i++) {
            if (!tmp_more_wf.containsKey(((Map) tmp_can_go_list.get(i)).get("wf_id").toString())) {
              tmp_more_wf.put(((Map) tmp_can_go_list.get(i)).get("wf_id").toString(), "");
            }
          }
          if (tmp_more_wf.keySet().size() > 1) {
            throw new Exception("数据ID为：" + entityId + "的数据根据设置信息能找到多个流程!");
          } else {
            // 写工作流开始
            for (int i = 0; i < tmp_can_go_list.size(); i++) {
              Map tmp_map = (Map) tmp_can_go_list.get(i);
              // 只使用机构权限的子系统能够使用事务提醒
              if (tmp_map.get("right_ccid") != null) {
                // 根据coa_code来取得coa_id 如果 coa_id为空 则产生异常
                String right_ccid = tmp_map.get("right_ccid").toString();
                String right_rcid = tmp_map.get("right_rcid").toString();
                String ccidSql = "select coa_id from gl_coa where coa_code='" + tmp_map.get("coa_id").toString() + "'";// lgc
                List dataList = dao.findBySql(ccidSql);// lgc
                String coa_id = ((XMLData) dataList.get(0)).get("coa_id").toString();// lgc
                if (right_ccid != null && !right_ccid.equals("")) {
                  if (right_ccid.equals("1")) {

                    inputFVoucherDto.setCoa_id(coa_id);
                    inputFVoucherDto.setCcid(ivoucher.getCCID(inputFVoucherDto));
                  }
                }
                if (right_rcid != null && !right_rcid.equals("")) {
                  if (right_rcid.equals("1")) {
                    inputFVoucherDto.setRcid(idataright.getRcid(inputFVoucherDto.toXMLData()));
                  }
                }
              }

              String task_id = getNextTaskIdBySequence();
              // lgc add end 070830
              // 批处理语句参数赋值
              setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, task_id, tmp_map.get("wf_id").toString(),
                tableName, entityId, tmp_map.get("node_id").toString(), tmp_map.get("next_node_id").toString(),
                actiontypeid, 0, create_user, "", "", operationName, operationDate, operationRemark, initMoney,
                resultMoney, null, tolly_flag, bill_type_code, billId, inputFVoucherDto.getRcid(),
                inputFVoucherDto.getCcid(), inputFVoucherDto.getSet_month(), 1, createUserId);

              XMLData return_xmldata = new XMLData();
              return_xmldata.put("wf_id", tmp_map.get("wf_id").toString());
              return_xmldata.put("current_node_id", tmp_map.get("node_id").toString());
              return_xmldata.put("next_node_id", tmp_map.get("next_node_id").toString());
              return_list.add(return_xmldata);

              saveOptionCurrentAndComleteTable(insertCurrentTaskPsmt, task_id, tmp_map.get("wf_id").toString(),
                entityId, tmp_map.get("node_id").toString(), tmp_map.get("next_node_id").toString().toString(),
                actiontypeid, billId, inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());

            }
          }
        }
      }

      // 传入的DTO中是单＋明细形式
      if (vou_type.equals("bill")) {
        this.setBillDetail(true);// lgc
        if (detail_wf_info == null || detail_wf_info.size() == 0) {
          throw new Exception("数据ID为：" + entityId + "的数据根据设置信息无法找到对应流程!");
        } else {
          for (int i = 0; i < detail_wf_info.size(); i++) {
            // 批处理语句参数赋值
            Map tmp_map = (Map) detail_wf_info.get(i);
            // lgc add start 070830
            if (tmp_map.get("right_ccid") != null) {
              String right_ccid = tmp_map.get("right_ccid").toString();
              String right_rcid = tmp_map.get("right_rcid").toString();
              String ccidSql = "select coa_id from gl_coa where coa_code='" + tmp_map.get("coa_id").toString() + "'";// lgc
              List dataList = dao.findBySql(ccidSql);// lgc
              String coa_id = ((XMLData) dataList.get(0)).get("coa_id").toString();// lgc
              if (right_ccid != null && !right_ccid.equals("")) {
                if (right_ccid.equals("1")) {
                  inputFVoucherDto.setCoa_id(coa_id);
                  icoaService.getCCIDByBaseDTO(inputFVoucherDto);// 更换产生ccid的方法
                }
              }
              if (right_rcid != null && !right_rcid.equals("")) {
                if (right_rcid.equals("1")) {
                  inputFVoucherDto.setRcid(idataright.getRcid(inputFVoucherDto.toXMLData()));
                }
              }
            }

            String taskId = getNextTaskIdBySequence();
            setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, getNextTaskIdBySequence(), tmp_map.get("wf_id")
              .toString(), tableName, entityId, tmp_map.get("current_node_id").toString(), tmp_map.get("next_node_id")
              .toString(), actiontypeid, 0, create_user, "", "", operationName, operationDate, operationRemark,
              initMoney, resultMoney, null, tolly_flag, bill_type_code, billId, inputFVoucherDto.getRcid(),
              inputFVoucherDto.getCcid(), inputFVoucherDto.getSet_month(), 1, createUserId);

            saveOptionCurrentAndComleteTable(insertCurrentTaskPsmt, taskId, tmp_map.get("wf_id").toString(), entityId,
              tmp_map.get("node_id").toString(), tmp_map.get("next_node_id").toString().toString(), actiontypeid,
              billId, inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());
          }
        }
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }

  }

  /**
   * 录入时根据权限找流程和节点。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param table_name---------单表名
   * @return
   * @throws Exception---------错误信息
   */
  private List doSingleProcessSimplyInputDetailSelect(String menuid, String roleid, String tableName, XMLData rowData)
    throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    List tmp_can_go_list = new ArrayList();
    try {
      StringBuffer select_sql = new StringBuffer();
      select_sql
        .append(
          "select distinct b.wf_id,b.condition_id as wf_condition,d.node_id,d.next_node_id,d.condition_id as node_condition")
        .append(" from ").append(" sys_wf_flows").append(Tools.addDbLink()).append(" b").append(",sys_wf_nodes")
        .append(Tools.addDbLink()).append(" c,sys_wf_node_conditions").append(Tools.addDbLink()).append(" d")
        .append(" where   b.wf_id=c.wf_id and c.node_type='001' and c.node_id=d.node_id and d.routing_type='001' ")
        .append(" and b.rg_code = '").append(rg_code).append("' and b.set_year = ").append(setYear)
        .append(" and b.wf_table_name ='" + tableName + "' ");
      // 如果传入了menu_id和role_id，则根据这两个参数进行过滤
      if (menuid != null && !menuid.equals("") && roleid != null && !roleid.equals("")) {
        select_sql.append(" and d.next_node_id in (select m.node_id from sys_wf_module_node").append(Tools.addDbLink())
          .append(" m,sys_wf_role_node n where m.node_id=n.node_id").append(" and m.module_id=").append(menuid)
          .append(" and n.role_id=").append(roleid).append(")");
      }
      List rs = null;

      // 查询出可以到达的流程节点信息
      rs = dao.findBySql(select_sql.toString());
      if (rs.size() == 0)
        throw new Exception("数据根据设置信息无法找到对应流程!");

      // 将菜单、角色作为过滤条件
      rowData.put("module_id", menuid);
      rowData.put("role_id", roleid);

      Iterator it = rs.iterator();
      while (it.hasNext()) {
        Map map = (Map) it.next();
        boolean can_go_next = false;
        // 流程为无条件流转
        if (map.get("wf_condition").equals("#")) {
          // 节点为无条件流转时，可以走入下一节点
          if (map.get("node_condition").equals("#")) {
            can_go_next = true;
          } else if (getValidWfNode((String) map.get("node_condition"), rowData)) {
            // 判断节点是否能走入下一节点
            can_go_next = true;
          }
        } else if (getValidWfNode((String) map.get("wf_condition"), rowData)) {
          // 节点为无条件流转时，可以走入下一节点
          if (map.get("node_condition").equals("#")) {
            can_go_next = true;
          } else if (getValidWfNode((String) map.get("node_condition"), rowData)) {
            // 判断节点是否能走入下一节点
            can_go_next = true;
          }
        }
        if (can_go_next) {
          tmp_can_go_list.add(map);
        }
      }
      // lgc start
      tmpCanGoData.put("bill_id", rowData.get("bill_id"));
      tmpCanGoData.put("isCan", tmp_can_go_list);
      // lgc end
      return tmp_can_go_list;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 通用正向处理操作（对单条数据除录入以外）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDto---业务数据DTO（单条）
   * @param voutype------------单据类型
   * @param detail_wf_info-----明细数据的流程节点信息
   * @param isForced2Execute
   *            是否强制执行（不考虑菜单，角色）
   * @return true或false
   * @throws Exception---------错误信息
   */
  private synchronized boolean doSingleProcessSimplyNext(PreparedStatement insertCurrentTaskPsmt,
    Statement setRoutingPsmt, String menuid, String roleid, String actiontypeid, String operationName,
    String operationDate, String operationRemark, String tableName, String vou_type, String entityId, String billId,
    String create_user, String create_date, String initMoney, String resultMoney, String remark, XMLData rowData,
    String tolly_flag, String bill_type_code, List detail_wf_info, List control_wf_info, List return_list,
    FVoucherDTO inputFVoucherDto, boolean isForced2Execute, List current_list, String createUserId)
    throws FAppException {

    // 循环处理表 CUREENT_TEMP , COMPLETE_TEMP 表使用集合
    List tempList4TempTable = new ArrayList();

    //为了防止并发前提下 取得相同rs 导致map为同一对象 写入taskId的时候 多条线程有可能会覆盖 造成不同流程取得相同的taskId
    //一节点分多流程 声明变量会有问题 还用以前map方式 用synchronized控制并发

    String rg_code = getRgCode();
    String setYear = getSetYear();

    StringBuffer select_sql = new StringBuffer();
    try {
      if (!isForced2Execute) {
        if (menuid == null || menuid.equals("")) {
          throw new Exception("需要传入menuid!");
        }
        if (roleid == null || roleid.equals("")) {
          throw new Exception("需要传入roleid!");
        }
      }

      List rs = null;
      String wf_id = "";
      String current_node_id = "0";
      // String back_node_id = "";

      // 如果是强制走流程，且是第一次走流程，则直接将数据送到某流程
      if (control_wf_info != null && control_wf_info.size() > 0) {

        // 增加到任务临时集合中 以便保存到两个新增表中
        tempList4TempTable = control_wf_info;

        for (int i = 0; i < control_wf_info.size(); i++) {
          // 批处理语句参数赋值
          Map tmp_map = (Map) control_wf_info.get(i);
          //为了防止并发前提下 取得相同rs 导致map为同一对象 写入taskId的时候 多条线程有可能会覆盖 造成不同流程取得相同的taskId
          //一节点分多流程 声明变量会有问题 还用以前map方式 用synchronized控制并发
          String tempTaskId = getNextTaskIdBySequence();
          tmp_map.put("taskId", tempTaskId);

          setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, tmp_map.get("taskId").toString(), tmp_map.get("wf_id")
            .toString(), tableName, entityId, tmp_map.get("current_node_id").toString(), tmp_map.get("next_node_id")
            .toString(), actiontypeid, 0, create_user, "", "", operationName, operationDate, operationRemark,
            initMoney, resultMoney, remark, tolly_flag, bill_type_code, billId, inputFVoucherDto.getRcid(),
            inputFVoucherDto.getCcid(), inputFVoucherDto.getSet_month(), 0, createUserId);

        }
        return true;
      }
      if (current_list != null && current_list.size() == 1) {
        Map map = (Map) rs.get(0);
        wf_id = (String) map.get("wf_id");
        current_node_id = map.get("current_node_id").toString();

      } else {
        if (!isForced2Execute) {
          select_sql
            .append(
              "select  b.wf_id,"
                + (TypeOfDB.isOracle() ? " decode(b.action_type_code,'NEXT',b.next_node_id,'INPUT',b.next_node_id,'BACK',b.next_node_id,b.current_node_id)"
                  : " case b.action_type_code when 'NEXT' then b.next_node_id when 'INPUT' then b.next_node_id when 'BACK' then b.next_node_id else b.current_node_id end ")
                + " as current_node_id,current_node_id  as back_node_id ,b.action_type_code  from sys_wf_current_tasks")
            .append(Tools.addDbLink())
            .append(" b")
            .append(" ,vw_sys_wf_node_relation")
            .append(Tools.addDbLink())
            .append(
              " d where d.module_id=? and d.role_id=?  and ((b.next_node_id=d.node_id  and b.action_type_code in ('INPUT','NEXT','BACK'))")
            .append(" or (b.current_node_id=d.node_id and b.action_type_code in ('EDIT')))")
            .append(
              " and b.entity_id = ? and b.rg_code = ? and b.set_year = ? and b.rg_code=d.rg_code and b.set_year=d.set_year ");

          // 判断数据是否存在
          rs = dao.findBySql(select_sql.toString(), new Object[] { menuid, roleid, entityId, rg_code, setYear });
        } else {
          select_sql
            .append(
              "select  b.wf_id,"
                + (TypeOfDB.isOracle() ? " decode(b.action_type_code,'NEXT',b.next_node_id,'INPUT',b.next_node_id,'BACK',b.next_node_id,b.current_node_id)"
                  : " case b.action_type_code when 'NEXT' then b.next_node_id when 'INPUT' then b.next_node_id when 'BACK' then b.next_node_id else b.current_node_id end ")
                + " as current_node_id   from sys_wf_current_tasks")
            .append(Tools.addDbLink())
            .append(" b")
            .append(" ,vw_sys_wf_node_relation")
            .append(Tools.addDbLink())
            .append(" d where ((b.next_node_id=d.node_id  and b.action_type_code in ('INPUT','NEXT','BACK'))")
            .append(" or (b.current_node_id=d.node_id and b.action_type_code in ('EDIT')))")
            .append(
              " and b.entity_id = ? and b.rg_code = ? and b.set_year = ? and b.rg_code=d.rg_code and b.set_year=d.set_year ");

          // 判断数据是否存在
          rs = dao.findBySql(select_sql.toString(), new Object[] { entityId, rg_code, setYear });
        }

        // 查找不到下一节点数据
        if (rs.size() == 0) {
          // 如果是明细，则报错
          throw new Exception("该数据不存在，或者该角色没有此权限!");
        } else {
          // 查找到数据后，保存下一节点信息
          Map map = (Map) rs.get(0);
          wf_id = (String) map.get("wf_id");
          current_node_id = map.get("current_node_id").toString();
        }
      }

      select_sql = new StringBuffer();
      select_sql.append("select distinct t.wf_id,t.node_id,t.next_node_id,t.condition_id ")
        .append(" from sys_wf_node_conditions").append(Tools.addDbLink()).append(" t ")
        .append(" where  t.routing_type=? and t.node_id=? ");

      List checkList = null;
      if (actiontypeid.equals("BACK")) {// 退回操作时
        StringBuffer checkSql = new StringBuffer();
        checkSql.append(" select * from sys_Wf_nodes wn where  exists");
        checkSql.append(" (select 1 from sys_wf_node_conditions wc where ");
        checkSql.append(" wc.node_id = ? AND wc.ROUTING_TYPE = '002' and");
        checkSql.append(" wn.node_id=wc.next_node_id)");
        checkList = dao.findBySql(checkSql.toString(), new Object[] { current_node_id });

        // 退回时不能做缓存，当多分支退回时，如果有缓存，他会记录第一次的next_node_id，当第二次从退回到另外一个节点时，由于缓存的存在，导致退到了第一次缓存的节点
        rs = dao.findBySql(select_sql.toString(), new Object[] { "002", current_node_id });

        // 如果是跨节点退回，则不找原来的路径；否则，则找原来的路径
        String tmp_sql = "select * from sys_wf_node_conditions a,sys_wf_node_conditions b where a.routing_type='002' and"
          + " a.node_id= "
          + current_node_id
          + " and b.routing_type='001' and b.next_node_id="
          + current_node_id
          + " and a.next_node_id=b.node_id";
        List tmp_list = dao.findBySql(tmp_sql);
        if (tmp_list != null && tmp_list.size() == 0) {

        } else {
          if (rs.size() > 1) {
            select_sql.append(" and exists (select 1 from (select * ").append(" from sys_wf_complete_tasks s")
              .append(" union select * from sys_wf_current_tasks w) c").append(" where c.next_node_id = t.node_id")
              .append(" and c.action_type_code IN ('NEXT', 'INPUT')").append(" and c.current_node_id = t.next_node_id")
              .append(" and c.entity_id = ? and c.rg_code = ? and c.set_year = ?)");
            rs = dao.findBySql(select_sql.toString(),
              new Object[] { "002", current_node_id, entityId, rg_code, setYear });
          }
        }

      } else {// 前进操作时
        rs = UtilCache.getNextNode(current_node_id);
        if (rs == null || rs.size() == 0) {
          rs = dao.findBySql(select_sql.toString(), new Object[] { "001", current_node_id });
          UtilCache.putNextNode(current_node_id, rs);
        }

      }

      // 判断是否能走入下一节点
      if (rs.size() == 0) {
        throw new Exception("该数据没有对应下一流程节点!");
      } else {
        // 将菜单、角色作为过滤条件
        rowData.put("module_id", menuid);
        rowData.put("role_id", roleid);
        /*
         * 
         * 设置一个boolean参数，存放流程节点是否满足表达式，只要有一个满足条件，
         * 就设置为true，则继续走流程，如果没有一次满足，则报错（流程节点不满足表达式）
         */
        if (!tableName.substring(0, 4).equalsIgnoreCase("SAL")) {
          boolean flag = false;
          Iterator it = rs.iterator();
          while (it.hasNext()) {
            Map map = (Map) it.next();
            boolean can_go_next = false;

            // NEXT时要判断表达式
            if (map.get("condition_id").equals("#")) {
              can_go_next = true;
              flag = true;
            } else if (getValidWfNode((String) map.get("condition_id"), rowData)) {
              can_go_next = true;
              flag = true;
            }
            if (can_go_next) {
              String tempTaskId = getNextTaskIdBySequence();
              map.put("taskId", tempTaskId);
              // 批处理语句参数赋值
              setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, tempTaskId, map.get("wf_id").toString(),
                tableName, entityId, current_node_id, map.get("next_node_id").toString(), actiontypeid, 0, create_user,
                "", "", operationName, operationDate, operationRemark, initMoney, resultMoney, remark, tolly_flag,
                bill_type_code, billId, inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid(),
                inputFVoucherDto.getSet_month(), 0, createUserId);

              XMLData return_xmldata = new XMLData();
              return_xmldata.put("wf_id", wf_id);
              return_xmldata.put("current_node_id", current_node_id);
              return_xmldata.put("next_node_id", map.get("next_node_id").toString());
              return_list.add(return_xmldata);
              tempList4TempTable.add(map);
            }
          }
          if (!flag) {
            throw new Exception("流程节点不满足表达式，请检查！");
          }
        } else {
          Iterator it = rs.iterator();
          StringBuffer nodes = new StringBuffer(" (");
          List can_go_list = new ArrayList();
          while (it.hasNext()) {
            Map map = (Map) it.next();
            // NEXT时要判断表达式
            if (map.get("condition_id").equals("#")) {
              can_go_list.add(map);
              nodes.append(map.get("next_node_id").toString() + ",");
            } else if (getValidWfNode((String) map.get("condition_id"), rowData)) {
              can_go_list.add(map);
              nodes.append(map.get("next_node_id").toString() + ",");
            }
          }
          nodes.deleteCharAt(nodes.length() - 1).append(")");
          if (actiontypeid.equals("BACK")) {
            if (can_go_list.size() > 0 && checkList != null) {
              for (int i = 0; i < can_go_list.size(); ++i) {
                XMLData xd = (XMLData) can_go_list.get(i);
                String next_node_id = xd.get("next_node_id").toString();
                for (int j = 0; j < checkList.size(); ++j) {
                  XMLData xd2 = (XMLData) checkList.get(j);
                  if (next_node_id.equals(xd2.get("node_id")) && "001".equals(xd2.get("node_type"))) {
                    throw new Exception("不能在开始节点进行退回操作!");
                  }
                }
              }
            }
          }
          // NEXT时要判断表达式，BACK时不需要判断表达式
          Iterator can_go_it = can_go_list.iterator();

          while (can_go_it.hasNext()) {
            Map map = (Map) can_go_it.next();
            //两条流程都满足的情况，判断是否是已经走过的流程 
            if (can_go_list.size() > 1) {
              StringBuffer otherConditonSql = new StringBuffer();
              otherConditonSql.append(" select t.action_type_code,count(1) as num ");
              otherConditonSql.append(" from (select entity_id,action_type_code,current_node_id ,next_node_id,is_undo");
              otherConditonSql.append(" from Sys_Wf_Complete_Tasks union all ");
              otherConditonSql.append("");
              otherConditonSql.append(" select entity_id,action_type_code,current_node_id ,next_node_id,is_undo");
              otherConditonSql
                .append(" from Sys_Wf_Current_Tasks union all select entity_id,action_type_code,current_node_id ,next_node_id,is_undo");
              otherConditonSql.append(" from Sys_Wf_End_Tasks )t");
              otherConditonSql.append("  where t.entity_id =?");
              otherConditonSql.append(" and ((t.current_node_id = ? and t.next_node_id = ?) or");
              otherConditonSql
                .append(" (t.current_node_id = ? and t.next_node_id = ?)) and t.is_undo=0  and t.rg_code=? and t.set_year=? group by t.action_type_code");
              List actionTypeList = dao.findBySql(otherConditonSql.toString(), new Object[] { entityId,
                current_node_id, map.get("next_node_id"), map.get("next_node_id"), current_node_id, rg_code, setYear });
              if (null != actionTypeList && actionTypeList.size() != 0) {
                if (actionTypeList.size() == 1) {
                  continue;
                } else {
                  Map actionTypeNumMap1 = (Map) actionTypeList.get(0);
                  Map actionTypeNumMap2 = (Map) actionTypeList.get(1);
                  String num1 = (String) actionTypeNumMap1.get("num");
                  String num2 = (String) actionTypeNumMap2.get("num");
                  if (!num1.equals(num2))
                    continue;
                }
              }
            }
            //为了防止并发前提下 取得相同rs 导致map为同一对象 写入taskId的时候 多条线程有可能会覆盖 造成不同流程取得相同的taskId
            //注掉以前的map.put 将下面方法中直接传入currentTaskId 以前是通过map.get("taskId")
            //一节点分多流程 声明变量会有问题 还用以前map方式 用synchronized控制并发
            String tempTaskId = getNextTaskIdBySequence();
            map.put("taskId", tempTaskId);
            // 批处理语句参数赋值
            setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, map.get("taskId").toString(), map.get("wf_id")
              .toString(), tableName, entityId, current_node_id, map.get("next_node_id").toString(), actiontypeid, 0,
              create_user, "", "", operationName, operationDate, operationRemark, initMoney, resultMoney, remark,
              tolly_flag, bill_type_code, billId, inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid(),
              inputFVoucherDto.getSet_month(), 0, createUserId);

            XMLData return_xmldata = new XMLData();
            return_xmldata.put("wf_id", wf_id);
            return_xmldata.put("current_node_id", current_node_id);
            return_xmldata.put("next_node_id", map.get("next_node_id").toString());
            return_list.add(return_xmldata);
            tempList4TempTable.add(map);
          }

          if (null == can_go_list || can_go_list.size() == 0) {
            throw new Exception("流程节点不满足表达式，请检查！");
          }
        }
      }

      setRoutingStmt(setRoutingPsmt, wf_id, entityId, current_node_id, tableName);

      try {
        Iterator it = tempList4TempTable.iterator();
        while (it.hasNext()) {

          Map map = (Map) it.next();
          //将下面方法中直接传入currentTaskId 以前是通过map.get("taskId")
          //一节点分多流程 声明变量会有问题 还用以前map方式 用synchronized控制并发
          saveOptionCurrentAndComleteTable(setRoutingPsmt, map.get("taskId").toString(), map.get("wf_id").toString(),
            entityId, current_node_id, map.get("next_node_id").toString(), actiontypeid, billId,
            inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());

        }
      } catch (Exception e) {
        throw e;
      }
      return true;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }

  }

  /**
   * 通用正向处理操作（对单条数据除录入以外）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDto---业务数据DTO（单条）
   * @param voutype------------单据类型
   * @param detail_wf_info-----明细数据的流程节点信息
   * @return true或false
   * @throws Exception---------错误信息
   */
  private boolean doSingleProcessSimplyDiscard(PreparedStatement insertCurrentTaskPsmt, Statement setRoutingPsmt,
    String menuid, String roleid, String actiontypeid, String operationName, String operationDate,
    String operationRemark, String tableName, String vou_type, String entityId, String billId, String create_user,
    String create_date, String initMoney, String resultMoney, String remark, XMLData rowData, String tolly_flag,
    String bill_type_code, List detail_wf_info, List return_list, FVoucherDTO inputFVoucherDto,
    boolean isForced2Execute, String createUserId) throws FAppException {
    StringBuffer select_sql = new StringBuffer();
    String rg_code = getRgCode();
    String setYear = getSetYear();

    try {
      if (!isForced2Execute) {
        if (menuid == null || menuid.equals("")) {
          throw new Exception("需要传入menuid!");
        }
        if (roleid == null || roleid.equals("")) {
          throw new Exception("需要传入roleid!");
        }
      }

      List rs = null;
      String wf_id = "";
      String current_node_id = "0";

      // 如果是强制走流程，且是第一次走流程，则直接将数据送到某流程
      if (detail_wf_info != null && detail_wf_info.size() > 0) {
        for (int i = 0; i < detail_wf_info.size(); i++) {
          // 批处理语句参数赋值
          String temp_task_id = getNextTaskIdBySequence();
          Map tmp_map = (Map) detail_wf_info.get(i);
          tmp_map.put("taskId", temp_task_id);
          setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, temp_task_id, tmp_map.get("wf_id").toString(),
            tableName, entityId, tmp_map.get("current_node_id").toString(), tmp_map.get("next_node_id").toString(),
            actiontypeid, 0, create_user, "", "", operationName, operationDate, operationRemark, initMoney,
            resultMoney, remark, tolly_flag, bill_type_code, billId, inputFVoucherDto.getRcid(),
            inputFVoucherDto.getCcid(), inputFVoucherDto.getSet_month(), 1, createUserId);

          saveOptionCurrentAndComleteTable(setRoutingPsmt, temp_task_id.toString(), wf_id, entityId, current_node_id,
            current_node_id, actiontypeid, billId, inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());

        }
        return true;
      } else {

        if (!isForced2Execute) {
          select_sql
            .append(
              "select distinct b.wf_id,b.current_node_id,b.next_node_id,b.action_type_code  from vw_sys_wf_current_end_tasks")
            .append(Tools.addDbLink())
            .append(" b")
            .append(" ,vw_sys_wf_node_relation")
            .append(Tools.addDbLink())
            .append(
              " d where b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=? and b.set_year=? and d.module_id=? and d.role_id=?  and ((b.next_node_id=d.node_id  and b.action_type_code in ('INPUT','NEXT','BACK'))")
            .append(" or (b.current_node_id=d.node_id and b.action_type_code in ('DISCARD','DELETE','HANG','EDIT')))")
            .append(" and b.entity_id = ?");

          // 判断数据是否存在
          rs = dao.findBySql(select_sql.toString(), new Object[] { rg_code, setYear, menuid, roleid, entityId });
        } else {
          select_sql
            .append(
              "select distinct b.wf_id,b.current_node_id,b.next_node_id,b.action_type_code  from vw_sys_wf_current_end_tasks")
            .append(Tools.addDbLink())
            .append(" b")
            .append(" ,vw_sys_wf_node_relation")
            .append(Tools.addDbLink())
            .append(
              " d where b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=? and d.set_year=? and ((b.next_node_id=d.node_id  and b.action_type_code in ('INPUT','NEXT','BACK'))")
            .append(" or (b.current_node_id=d.node_id and b.action_type_code in ('DISCARD','DELETE','HANG','EDIT')))")
            .append(" and b.entity_id = ?");

          // 判断数据是否存在
          rs = dao.findBySql(select_sql.toString(), new Object[] { rg_code, setYear, entityId });
        }

        // 查找不到下一节点数据
        if (rs.size() == 0) {
          // 如果是单时，根据明细的流程和节点走工作流
          // 如果是明细，则报错
          throw new Exception("不能走入下一流程节点，原因有：未找到该数据；或者该角色没有此权限!");
        } else {
          // 查找到数据后，保存下一节点信息
          Map map = (Map) rs.get(0);
          wf_id = (String) map.get("wf_id");
          String current_action_type_code = (String) map.get("action_type_code");
          if (current_action_type_code.equals("NEXT") || current_action_type_code.equals("BACK")
            || current_action_type_code.equals("INPUT")) {
            current_node_id = map.get("next_node_id").toString();
          } else {
            current_node_id = map.get("current_node_id").toString();
          }
        }
      }

      String temp_task_id = getNextTaskIdBySequence();

      // 批处理语句参数赋值
      setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, temp_task_id, wf_id, tableName, entityId, current_node_id,
        "0", actiontypeid, 0, create_user, "", "", operationName, operationDate, operationRemark, initMoney,
        resultMoney, remark, tolly_flag, bill_type_code, billId, inputFVoucherDto.getRcid(),
        inputFVoucherDto.getCcid(), inputFVoucherDto.getSet_month(), 0, createUserId);

      XMLData return_xmldata = new XMLData();
      return_xmldata.put("wf_id", wf_id);
      return_xmldata.put("current_node_id", current_node_id);
      return_xmldata.put("next_node_id", "0");
      return_list.add(return_xmldata);
      //为了修改setroutingstmt里面的update语句正确性
      setRoutingStmt(setRoutingPsmt, wf_id, entityId, current_node_id, tableName);

      try {
        saveOptionCurrentAndComleteTable(setRoutingPsmt, temp_task_id.toString(), wf_id, entityId, current_node_id,
          current_node_id, actiontypeid, billId, inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());

      } catch (Exception e) {
        throw e;
      }

      return true;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 处理跨节点作废等。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDto---业务数据DTO（单条）
   * @param voutype------------单据类型
   * @param detail_wf_info-----明细数据的流程节点信息
   * @return true或false
   * @throws Exception---------错误信息
   */
  private boolean doSingleProcessSimplyStrideDiscard(PreparedStatement insertCurrentTaskPsmt, Statement setRoutingPsmt,
    String old_current_node_id, String actiontypeid, String operationName, String operationDate,
    String operationRemark, String tableName, String vou_type, String entityId, String billId, String create_user,
    String create_date, String initMoney, String resultMoney, String remark, XMLData rowData, String tolly_flag,
    String bill_type_code, List detail_wf_info, List return_list, FVoucherDTO inputFVoucherDto, String createUserId)
    throws FAppException {
    //add by liuzw 20120327 
    String rg_code = getRgCode();
    String setYear = getSetYear();

    try {
      StringBuffer select_sql = new StringBuffer();
      select_sql.append("select distinct b.wf_id,b.node_id ").append(" from vw_sys_wf_stride_001_and_004")
        .append(Tools.addDbLink()).append(" b")
        .append("  where  b.entity_id = ? and b.node_id=? and b.rg_code=? and b.set_year=?");
      List list = dao
        .findBySql(select_sql.toString(), new Object[] { entityId, old_current_node_id, rg_code, setYear });
      for (int i = 0; i < list.size(); i++) {
        String wf_id = ((Map) list.get(i)).get("wf_id").toString();
        String current_node_id = ((Map) list.get(i)).get("node_id").toString();

        /**
         * modify start by bing-zeng 2008-02-25 添加自动对任务提醒ITEM表操作
         */
        String temp_task_id = getNextTaskIdBySequence();
        // 批处理语句参数赋值
        setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, temp_task_id, wf_id, tableName, entityId,
          current_node_id, "0", actiontypeid, 0, create_user, "", "", operationName, operationDate, operationRemark,
          initMoney, resultMoney, remark, tolly_flag, bill_type_code, billId, inputFVoucherDto.getRcid(),
          inputFVoucherDto.getCcid(), inputFVoucherDto.getSet_month(), 0, createUserId);

        // doSingleProcessSimplyRouting(wf_id, entityId, tableName,
        // current_node_id);
        //加上tablename 为了修改setroutingstmt里面的update语句正确性
        setRoutingStmt(setRoutingPsmt, wf_id, entityId, current_node_id, tableName);
        saveOptionCurrentAndComleteTable(setRoutingPsmt, temp_task_id, wf_id, entityId, current_node_id,
          current_node_id, actiontypeid, billId, inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());
      }

      return true;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 处理跨节点提取等。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDto---业务数据DTO（单条）
   * @param voutype------------单据类型
   * @param detail_wf_info-----明细数据的流程节点信息
   * @return true或false
   * @throws Exception---------错误信息
   */
  private boolean doSingleProcessSimplyStrideDistill(PreparedStatement insertCurrentTaskPsmt, Statement setRoutingPsmt,
    String old_current_node_id, String menuid, String roleid, String actiontypeid, String operationName,
    String operationDate, String operationRemark, String tableName, String vou_type, String entityId, String billId,
    String create_user, String create_date, String initMoney, String resultMoney, String remark, XMLData rowData,
    String tolly_flag, String bill_type_code, List detail_wf_info, List return_list, FVoucherDTO inputFVoucherDto,
    String createUserId) throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    try {

      if (menuid == null || menuid.equals("")) {
        throw new Exception("需要传入menuid!");
      }
      if (roleid == null || roleid.equals("")) {
        throw new Exception("需要传入roleid!");
      }

      List rs = null;
      String wf_id = "";
      String current_node_id = "0";

      StringBuffer select_sql = new StringBuffer();
      select_sql
        .append("select distinct d.wf_id,d.node_id  from sys_wf_current_tasks")
        .append(Tools.addDbLink())
        .append(" b")
        .append(" ,vw_sys_wf_node_relation")
        .append(Tools.addDbLink())
        .append(" d where d.wf_id=b.wf_id and d.module_id=? and d.role_id=?")
        .append(
          " and b.entity_id = ? and b.rg_code = ? and b.set_year = ? and b.rg_code=d.rg_code and b.set_year=d.set_year");

      // 判断数据是否存在
      rs = dao.findBySql(select_sql.toString(), new Object[] { menuid, roleid, entityId, rg_code, setYear });

      // 查找不到下一节点数据
      if (rs.size() == 0) {
        // 如果是单时，根据明细的流程和节点走工作流
        // 如果是明细，则报错
        throw new Exception("不能走入下一流程节点，原因有：未找到该数据；或者该角色没有此权限!");
      } else {
        // 查找到数据后，保存下一节点信息
        Map map = (Map) rs.get(0);
        wf_id = (String) map.get("wf_id");
        current_node_id = map.get("node_id").toString();
      }

      String temp_task_id = getNextTaskIdBySequence();
      // 批处理语句参数赋值
      setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, temp_task_id, wf_id, tableName, entityId, current_node_id,
        "0", actiontypeid, 0, create_user, "", "", operationName, operationDate, operationRemark, initMoney,
        resultMoney, remark, tolly_flag, bill_type_code, billId, inputFVoucherDto.getRcid(),
        inputFVoucherDto.getCcid(), inputFVoucherDto.getSet_month(), 0, createUserId);

      XMLData return_xmldata = new XMLData();
      return_xmldata.put("wf_id", wf_id);
      return_xmldata.put("current_node_id", current_node_id);
      return_xmldata.put("next_node_id", "0");
      return_list.add(return_xmldata);
      //加上tablename 为了修改setroutingstmt里面的update语句正确性
      setRoutingStmt(setRoutingPsmt, wf_id, entityId, current_node_id, tableName);

      saveOptionCurrentAndComleteTable(setRoutingPsmt, temp_task_id, wf_id, entityId, current_node_id, current_node_id,
        actiontypeid, billId, inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid());

      return true;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * modify start by bing-zeng 插入sys_wf_task_routing时 删除条数限制,
   * 增加了UPDATE_FLAGE限制 0:表示新增加的任务, 1:表示原有的任务 插入sys_wf_complete_tasks 时 删除条数限制,
   * 增加了UPDATE_FLAGE限制 0:表示新增加的任务, 1:表示原有的任务 删除sys_wf_current_tasks 时 删除条数限制,
   * 增加了UPDATE_FLAGE限制 0:表示新增加的任务, 1:表示原有的任务 增加了最后更新sys_wf_current_tasks ,
   * UPDATE_FLAGE = 1
   */
  private void setRoutingStmt(Statement setRoutingPsmt, String wf_id, String entityId, String current_node_id,
    String tableName) throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    try {

      StringBuffer insertRoutingSql = new StringBuffer();
      insertRoutingSql.append(" insert into sys_wf_task_routing").append("(TASK_ID,NEXT_TASK_ID,rg_code,set_year) ")
        .append(" select task_id,next_task_id,").append(rg_code).append(" as rg_code,").append(setYear)
        .append(" as set_year ").append(" from ( select  e.task_id from sys_wf_current_tasks e ")
        .append(" where e.entity_id='" + entityId + "' ")
        .append(" and e.rg_code='" + rg_code + "' and e.set_year='" + setYear + "'")
        .append(" and ((e.next_node_id ='" + current_node_id + "'")
        .append(" and e.action_type_code in ('INPUT','NEXT','BACK'))")
        .append(" or (e.current_node_id='" + current_node_id + "'")
        .append(" and e.action_type_code in ('DISCARD','HANG','DELETE','EDIT'))) and e.update_flag = 1  ) ")
        .append(" ,(").append(" select f.task_id next_task_id from sys_wf_current_tasks").append(" f")
        .append(" where f.entity_id='" + entityId + "' ")
        .append(" and f.rg_code='" + rg_code + "' and f.set_year='" + setYear + "'")
        .append(" and f.update_flag = 0   )");

      // 将当前任务列表中的数据复制到完成任务列表中
      StringBuffer insertCompTaskSql = new StringBuffer();
      insertCompTaskSql
        .append("insert into sys_wf_complete_tasks")
        .append(" select b.*  from sys_wf_current_tasks b ")
        .append(" where  b.entity_id ='" + entityId + "' and b.wf_id='" + wf_id + "' ")
        .append(" and ((b.next_node_id='" + current_node_id + "'  and b.action_type_code in ('INPUT','NEXT','BACK'))")
        .append(
          " or (b.current_node_id='" + current_node_id
            + "' and b.action_type_code in ('DISCARD','HANG','DELETE','EDIT'))) and b.update_flag = 1 and b.rg_code='"
            + rg_code + "' and b.set_year=" + setYear);

      // 将当前任务列表中的数据删除
      StringBuffer deleteCurTaskSql = new StringBuffer();
      deleteCurTaskSql
        .append("delete sys_wf_current_tasks b")
        .append(" where  b.entity_id ='" + entityId + "'  and b.wf_id='" + wf_id + "' ")
        .append(" and ((b.next_node_id='" + current_node_id + "' and b.action_type_code in ('INPUT','NEXT','BACK'))")
        .append(
          " or (b.current_node_id='" + current_node_id
            + "' and b.action_type_code in ('DISCARD','HANG','DELETE','EDIT'))) and b.update_flag = 1 and b.rg_code='"
            + rg_code + "' and b.set_year=" + setYear);

      //增加tableName参数 保证正确性
      String updateSql = "update sys_wf_current_tasks t set t.update_flag = 1 where t.entity_id ='" + entityId
        + "'  and t.wf_id=" + wf_id + " and t.wf_table_name='" + tableName + "' and t.rg_code='" + rg_code
        + "' and t.set_year=" + setYear;

      setRoutingPsmt.addBatch(insertRoutingSql.toString());
      setRoutingPsmt.addBatch(insertCompTaskSql.toString());
      setRoutingPsmt.addBatch(deleteCurTaskSql.toString());
      setRoutingPsmt.addBatch(updateSql);

    } catch (Exception e) {
      throw new FAppException(e);
    }

  }

  /**
   * 通用正向处理操作（对单条数据）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDto---业务数据DTO（单条）
   * @param voutype------------单据类型
   * @param detail_wf_info-----明细数据的流程节点信息
   * @param isForced2Execute
   *            是否强制执行（不考虑菜单，角色）
   * @return true或false
   * @throws Exception---------错误信息
   */
  private synchronized List doSingleProcessSimply(PreparedStatement insertCurrentTaskPsmt, Statement setRoutingPsmt,
    String old_current_node_id, String menuid, String roleid, String actiontypeid, String operationname,
    String operationdate, String operationremark, String tableName, FVoucherDTO inputFVoucherDto, String vou_type,
    List detail_wf_info, List control_wf_info, String tolly_flag, String billId, boolean isForced2Execute,
    List currentList) throws FAppException {
    List return_list = new ArrayList();
    String create_user = "";
    String create_user_id = "";
    try {
      String user_name = "";
      String authorieduser_name = "";
      String user_id = "";
      String authorieduser_id = "";

      if (SessionUtil.getUserInfoContext().getAttribute("user_name") != null
        && !SessionUtil.getUserInfoContext().getAttribute("user_name").toString().equals("")) {
        user_name = (String) SessionUtil.getUserInfoContext().getAttribute("user_name").toString();
      }

      if (SessionUtil.getUserInfoContext().getAttribute("authorieduser_name") != null
        && !SessionUtil.getUserInfoContext().getAttribute("authorieduser_name").toString().equals("")) {
        authorieduser_name = (String) SessionUtil.getUserInfoContext().getAttribute("authorieduser_name");
      }

      if (SessionUtil.getUserInfoContext().getAttribute("user_id") != null
        && !SessionUtil.getUserInfoContext().getAttribute("user_id").toString().equals("")) {
        user_id = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");
      }

      if (SessionUtil.getUserInfoContext().getAttribute("authorieduser_id") != null
        && !SessionUtil.getUserInfoContext().getAttribute("authorieduser_id").toString().equals("")) {
        authorieduser_id = (String) SessionUtil.getUserInfoContext().getAttribute("authorieduser_id");
      }

      if (!"".equals(user_name) && !"".equals(authorieduser_name) && (!authorieduser_name.equals(user_name))) {
        create_user = authorieduser_name + "(代理：" + user_name + ")";
        create_user_id = authorieduser_id;
      } else {
        create_user_id = user_id;
        create_user = user_name;
      }

      String entityId = inputFVoucherDto.getVou_id();
      if (entityId == null || entityId.equals("")) {
        throw new Exception("需要传入vou_id!");
      }

      String bill_type_code = "";
      String operationName = "";
      String operationDate = "";
      String operationRemark = "";
      String initMoney = null;
      String resultMoney = "null";
      String remark = "";
      XMLData rowData = new XMLData();
      rowData = inputFVoucherDto.toXMLData();

      if (inputFVoucherDto.getBilltype_code() != null) {
        bill_type_code = inputFVoucherDto.getBilltype_code();
      }
      if (operationname != null && !operationname.equals("")) {
        operationName = operationname;
      }
      if (operationremark != null && !operationremark.equals("")) {
        operationRemark = operationremark;
      }
      if (operationdate != null && !operationdate.equals("")) {
        operationDate = operationdate;
      }
      if (inputFVoucherDto.getVou_money() != null && !inputFVoucherDto.getVou_money().equals("")) {
        resultMoney = inputFVoucherDto.getVou_money();
      }

      remark = ParseXML.convertObjToXml(inputFVoucherDto.toXMLData(), "voucherdto");

      // INPUT操作时
      if (actiontypeid.equals("INPUT")) {
        doSingleProcessSimplyInput(insertCurrentTaskPsmt, menuid, roleid, actiontypeid, operationName, operationDate,
          operationRemark, tableName, vou_type, entityId, billId, create_user, null, initMoney, resultMoney, remark,
          rowData, tolly_flag, bill_type_code, detail_wf_info, control_wf_info, return_list, inputFVoucherDto,
          create_user_id);
      } else if (actiontypeid.equals("NEXT") || actiontypeid.equals("BACK")) {
        doSingleProcessSimplyNext(insertCurrentTaskPsmt, setRoutingPsmt, menuid, roleid, actiontypeid, operationName,
          operationDate, operationRemark, tableName, vou_type, entityId, billId, create_user, null, initMoney,
          resultMoney, remark, rowData, tolly_flag, bill_type_code, detail_wf_info, control_wf_info, return_list,
          inputFVoucherDto, isForced2Execute, currentList, create_user_id);
      } else if (actiontypeid.equals("DISTILL")) {
        doSingleProcessSimplyStrideDistill(insertCurrentTaskPsmt, setRoutingPsmt, old_current_node_id, menuid, roleid,
          actiontypeid, operationName, operationDate, operationRemark, tableName, vou_type, entityId, billId,
          create_user, null, initMoney, resultMoney, remark, rowData, tolly_flag, bill_type_code, detail_wf_info,
          return_list, inputFVoucherDto, create_user_id);
      } else {
        // 其他操作时
        // 不传入menuid和roleid，则表示是跨节点作废等操作
        if (menuid == null && roleid == null) {
          doSingleProcessSimplyStrideDiscard(insertCurrentTaskPsmt, setRoutingPsmt, old_current_node_id, actiontypeid,
            operationName, operationDate, operationRemark, tableName, vou_type, entityId, billId, create_user, null,
            initMoney, resultMoney, remark, rowData, tolly_flag, bill_type_code, detail_wf_info, return_list,
            inputFVoucherDto, create_user_id);
        } else {
          preDoSingleProcessSimplyDiscard(menuid, roleid, entityId, tableName, inputFVoucherDto, isForced2Execute);
          doSingleProcessSimplyDiscard(insertCurrentTaskPsmt, setRoutingPsmt, menuid, roleid, actiontypeid,
            operationName, operationDate, operationRemark, tableName, vou_type, entityId, billId, create_user, null,
            initMoney, resultMoney, remark, rowData, tolly_flag, bill_type_code, detail_wf_info, return_list,
            inputFVoucherDto, isForced2Execute, create_user_id);
        }

      }

      return return_list;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  private void preDoSingleProcessSimplyDiscard(String menuid, String roleid, String entityId, String tablename,
    FVoucherDTO inputFVoucherDto, boolean isForced2Execute) throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer select_sql = new StringBuffer();
    List rs = null;
    try {
      if (!isForced2Execute) {
        select_sql
          .append("select distinct b.wf_id, b.current_node_id, b.next_node_id, b.action_type_code ")
          .append(" from vw_sys_wf_current_end_tasks")
          .append(Tools.addDbLink())
          .append(" b, vw_sys_wf_node_relation")
          .append(Tools.addDbLink())
          .append(" d ")
          .append(" where d.module_id=? and d.role_id=? ")
          .append(" and (b.current_node_id=d.node_id and b.action_type_code in ('INPUT','NEXT','BACK')) ")
          .append(
            " and b.entity_id = ? and b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=? and d.set_year=?");
        // 判断数据是否存在
        rs = dao.findBySql(select_sql.toString(), new Object[] { menuid, roleid, entityId, rg_code, setYear });
      } else {
        select_sql
          .append("select distinct b.wf_id, b.current_node_id, b.next_node_id, b.action_type_code ")
          .append(" from vw_sys_wf_current_end_tasks")
          .append(Tools.addDbLink())
          .append(" b, vw_sys_wf_node_relation")
          .append(Tools.addDbLink())
          .append(" d ")
          .append(" where (b.current_node_id=d.node_id and b.action_type_code in ('INPUT','NEXT','BACK')) ")
          .append(
            " and b.entity_id = ? and b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=? and d.set_year=?");
        // 判断数据是否存在
        rs = dao.findBySql(select_sql.toString(), new Object[] { entityId, rg_code, setYear });
      }

      if (rs != null && rs.size() > 0) {
        undoSingleProcessSimplyReturnObj(menuid, roleid, tablename, inputFVoucherDto);
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 批处理SQL赋值
   * 
   * @param insertCurrentTaskPsmt
   * @param nextTaskId
   * @param wfId
   * @param wfTableName
   * @param entityId
   * @param currentNodeId
   * @param nextNodeId
   * @param actionType
   * @param isUndo
   * @param createUser
   * @param createDate
   * @param undoUser
   * @param undoDate
   * @param operationName
   * @param operationDate
   * @param operationRemark
   * @param initMoney
   * @param resultMoney
   * @param remark
   * @param tollyFlag
   * @param billTypeCode
   * @param billId
   * @param rcid
   * @param ccid
   * @param sendMsgDate
   * @param autoAuditDate
   * @param setMonth
   * @throws Exception
   */
  private void setValues4InsertCurrentTaskPsmt(PreparedStatement insertCurrentTaskPsmt, String nextTaskId, String wfId,
    String wfTableName, String entityId, String currentNodeId, String nextNodeId, String actionType, int isUndo,
    String createUser, String undoUser, String undoDate, String operationName, String operationDate,
    String operationRemark, String initMoney, String resultMoney, String remark, String tollyFlag, String billTypeCode,
    String billId, String rcid, String ccid, int setMonth, int updateFlag, String createUserId) throws FAppException {
    try {
      //发送消息和自动审核菜单自今没有实际应用，暂时屏蔽掉其相关处理逻辑，以提高性能。      
      insertCurrentTaskPsmt.setString(1, nextTaskId);
      insertCurrentTaskPsmt.setString(2, wfId);
      insertCurrentTaskPsmt.setString(3, wfTableName);
      insertCurrentTaskPsmt.setString(4, entityId);
      insertCurrentTaskPsmt.setString(5, currentNodeId);
      insertCurrentTaskPsmt.setString(6, nextNodeId);
      insertCurrentTaskPsmt.setString(7, actionType);
      insertCurrentTaskPsmt.setInt(8, isUndo);
      insertCurrentTaskPsmt.setString(9, createUser);
      insertCurrentTaskPsmt.setString(10, Tools.getCurrDate()/*(String) noticeDateMap.get("currDate")*/);
      insertCurrentTaskPsmt.setString(11, undoUser);
      insertCurrentTaskPsmt.setString(12, undoDate);
      insertCurrentTaskPsmt.setString(13, operationName);
      insertCurrentTaskPsmt.setString(14, operationDate);
      insertCurrentTaskPsmt.setString(15, operationRemark);
      insertCurrentTaskPsmt.setString(16, initMoney);
      insertCurrentTaskPsmt.setString(17, resultMoney);
      insertCurrentTaskPsmt.setString(18, null);
      insertCurrentTaskPsmt.setString(19, tollyFlag);
      insertCurrentTaskPsmt.setString(20, billTypeCode);
      insertCurrentTaskPsmt.setString(21, billId);
      insertCurrentTaskPsmt.setString(22, rcid);
      insertCurrentTaskPsmt.setString(23, ccid);
      insertCurrentTaskPsmt.setString(24, "");
      insertCurrentTaskPsmt.setString(25, "");
      insertCurrentTaskPsmt.setInt(26, setMonth);
      insertCurrentTaskPsmt.setInt(27, updateFlag);
      insertCurrentTaskPsmt.setString(28, createUserId);
      insertCurrentTaskPsmt.setString(29, SessionUtil.getRgCode());
      insertCurrentTaskPsmt.setInt(30, Integer.valueOf(SessionUtil.getLoginYear()).intValue());
      insertCurrentTaskPsmt.addBatch();
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 从sequence SEQ_SYS_WF_TASK_ID 取得下一个TaskId
   * 
   * @return
   */
  private String getNextTaskIdBySequence() {
    List rs = dao.findBySql("select "
      + (TypeOfDB.isOracle() ? "SEQ_SYS_WF_TASK_ID.Nextval" : " Nextval('SEQ_SYS_WF_TASK_ID') as nextval ")
      + " from dual");
    if (rs != null && rs.size() > 0) {
      String next_val = (String) ((Map) rs.get(0)).get("nextval");
      if (next_val != null && !next_val.equals("")) {
        return next_val;
      }
    }
    return "1";
  }

  /**
   * 任务查询。当用户进行事务提醒时，从工作流组件得到所能操作的任务（数据）列表。
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @return SQL语句
   * @throws Exception---------错误信息
   */
  public String getAllTasksWithRightBySql(String UserId, String RoleId) throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    try {
      String rightSql = idataright.getSqlBusiRight(UserId, RoleId, "d");
      StringBuffer sql = new StringBuffer();
      sql
        .append("select distinct d.wf_table_name,d.id_column_name ")
        .append(" from sys_role_module")
        .append(Tools.addDbLink())
        .append(" a,sys_wf_current_tasks")
        .append(Tools.addDbLink())
        .append(" b ,sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(" c ,sys_wf_flows")
        .append(Tools.addDbLink())
        .append(" d")
        .append(" where a.role_id = ? and  a.module_id=c.module_id and c.NODE_ID=b.current_node_id")
        .append(
          " and c.wf_id=b.wf_id and c.wf_id=d.wf_id and b.task_status='001' and c.rg_code=d.rg_code and c.set_year=d.set_year and c.rg_code=? and d.set_year=?");
      List result = null;
      StringBuffer returnSql = new StringBuffer();
      returnSql
        .append("select c.node_code, c.node_name, count(*) as count_num from sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(" c")
        .append(
          " where 1=0 and rg_code='" + rg_code + "' and set_year=" + setYear + " group by c.node_code, c.node_name");
      result = dao.findBySql(sql.toString(), new Object[] { RoleId, rg_code, setYear });
      if (result.size() == 0)
        return "";
      Iterator it = result.iterator();
      while (it.hasNext()) {
        Map rs = (Map) it.next();
        returnSql
          .append(" union select c.node_code,c.node_name,count(*) as count_num")
          .append(" from sys_role_module")
          .append(Tools.addDbLink())
          .append(" a,sys_wf_current_tasks")
          .append(Tools.addDbLink())
          .append(" b ,sys_wf_nodes")
          .append(Tools.addDbLink())
          .append(" c ,")
          .append(rs.get("wf_table_name"))
          .append(" d where a.role_id = ")
          .append(RoleId)
          .append(" and  b.task_status='001'and a.module_id=c.module_id and c.NODE_ID=b.current_node_id")
          .append(" and c.wf_id=b.wf_id and b.wf_table_name='")
          .append(rs.get("wf_table_name") + "' and b.entity_id=d.")
          .append(rs.get("id_column_name") + " " + rightSql)
          .append(
            " and b.rg_code=c.rg_code and b.set_year=c.set_year and b.rg_code='" + rg_code + "' and c.set_year="
              + setYear + " group by c.node_code,c.node_name");
      }
      return returnSql.toString();
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 任务查询语句。当用户启动某一个菜单或进行事务提醒时，工作流组件返回用于提取任务的SQL语句。
   * 
   * @param TableName------------表名称
   * @param TableId--------------表主键ID
   * @param RoleId---------------角色ID
   * @param menuId-------------菜单ID
   * @param OperateType----------操作类型
   * @param TableAlias-----------主表的别名
   * @return SQL语句
   * @throws Exception-----------错误信息
   */
  public String getTasksBySql(String TableName, String TableId, String RoleId, String menuId, String OperateType,
    String TableAlias) throws FAppException {

    String c = OperateType;
    String b = "";
    String return_sql = "";

    boolean is1stExistClauseFlag = true;// 标识是否是第一个"exist(.....)" 语句.
    // 如果是,则前面不加"or".
    // 循环调用多种状态的数据
    try {
      while (c.length() > 0) {
        if (c.indexOf("|") >= 0) {
          b = c.substring(0, c.indexOf("|"));
          c = c.substring(c.indexOf("|") + 1, c.length());
        } else {
          b = c;
          if (b.trim().length() > 0) {
            return_sql = return_sql
              + getTasksBySqlBySingleType(TableName, TableId, RoleId, menuId, b.trim(), TableAlias,
                is1stExistClauseFlag);
          }
          break;
        }
        if (b.trim().length() > 0) {
          return_sql = return_sql
            + getTasksBySqlBySingleType(TableName, TableId, RoleId, menuId, b.trim(), TableAlias, is1stExistClauseFlag);
        }
        is1stExistClauseFlag = false;
      }

      // 屏蔽 " and () " 情况
      if (return_sql.trim().equals("")) {
        return " and 1 = 0 ";
      }

      return " and (" + return_sql + ")";
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 任务查询语句。当用户启动某一个菜单或进行事务提醒时，工作流组件返回用于提取任务的SQL语句。
   * 
   * @param TableName----------表名称
   * @param TableId------------表主键ID
   * @param RoleId-------------角色ID
   * @param menuId-----------菜单ID
   * @param OperateType--------操作类型
   * @param TableAlias---------主表的别名
   * @return SQL语句
   * @throws Exception---------错误信息
   */

  private String getTasksBySqlBySingleType(String TableName, String TableId, String RoleId, String menuId,
    String OperateType, String TableAlias, boolean is1stExistClauseFlag) throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    // OperateType:001未确认、002已确认、003已退回、004被退回、101已挂起、102已删除、103已作废
    StringBuffer sql = new StringBuffer();
    String wf_id = "";
    String node_id = "0";
    String gather_flag = "";
    List result = null;
    try {
      sql
        .append("select d.wf_id,d.node_id,d.gather_flag,d.wf_table_name,e.id_column_name ")
        .append("from sys_wf_nodes")
        .append(Tools.addDbLink())
        .append(" d,sys_wf_module_node")
        .append(Tools.addDbLink())
        .append(" b,sys_wf_role_node")
        .append(Tools.addDbLink())
        .append(" c,sys_tablemanager")
        .append(Tools.addDbLink())
        .append(" e")
        .append(
          " where  d.wf_table_name=e.table_code and d.node_id=b.node_id and d.node_id=c.node_id and b.module_id=? ")
        .append(
          " and c.role_id=? and d.rg_code=b.rg_code and d.rg_code=c.rg_code and d.set_year=b.set_year and d.set_year=b.set_year and b.rg_code=? and b.set_year=?");
      result = dao.findBySql(sql.toString(), new Object[] { menuId, RoleId, rg_code, setYear });

      if (result.size() == 0)
        return "1=0";

      String return_sql = "";
      String return_return_sql = "";

      for (int i = 0; i < result.size(); i++) {
        if (TableName == null || TableName.equals("")) {
          TableName = ((Map) result.get(i)).get("wf_table_name").toString();
        }
        Map rs = (Map) result.get(i);
        wf_id = (String) rs.get("wf_id");
        node_id = (String) rs.get("node_id");
        gather_flag = (String) rs.get("gather_flag");
        return_sql = (i == 0 && is1stExistClauseFlag ? " " : " or ")
          + getTasksBySqlByNode(wf_id, node_id, TableName, TableId, OperateType, TableAlias, gather_flag);
        return_return_sql = return_return_sql + return_sql;
      }

      return return_return_sql;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 任务查询语句（通过节点）。
   * 
   * @param wf_id--------------流程ID
   * @param node_id------------节点ID
   * @param TableName----------表名称
   * @param TableId------------表主键ID
   * @param OperateType--------操作类型
   * @param TableAlias---------主表的别名
   * @param gather_flag--------是否同步节点
   * @return SQL语句
   * @throws Exception---------错误信息
   */
  private String getTasksBySqlByNode(String wf_id, String node_id, String TableName, String TableId,
    String OperateType, String TableAlias, String gather_flag) throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer return_sql = new StringBuffer();
    // 未确认
    try {
      if (OperateType.equals("001") || OperateType.equals("004")) {
        return_sql
          .append("  exists (select 1 ")
          .append(" from sys_wf_current_item")
          .append(Tools.addDbLink())
          .append(" b")
          .append(" where b.status_code= '" + OperateType + "' and b.entity_id=" + TableAlias + "." + TableId)
          .append("  and b.node_id = '" + node_id + "' and b.rg_code='" + rg_code + "' and b.set_year=" + setYear + ")");
      } else {
        return_sql
          .append("  exists (select 1 ")
          .append(" from SYS_WF_COMPLETE_ITEM ")
          .append(Tools.addDbLink())
          .append(" b")
          .append(" where b.status_code= '" + OperateType + "' and b.entity_id=" + TableAlias + "." + TableId)
          .append("  and b.node_id = '" + node_id + "' and b.rg_code='" + rg_code + "' and b.set_year=" + setYear + ")");
      }
      return return_sql.toString();
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 带有权限的任务查询语句。当用户启动某一个菜单或进行事务提醒时，工作流组件返回用于提取任务并带有权限的SQL语句。
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param TableName----------表名称
   * @param TableId------------表主键ID
   * @param menuID-----------菜单ID
   * @param OperateType--------操作类型
   * @param TableAlias---------主表的别名
   * @return SQL语句
   * @throws Exception---------错误信息
   */
  public String getTasksWithRightBySql(String UserId, String RoleId, String TableName, String TableId, String menuId,
    String OperateType, String TableAlias) throws FAppException {
    String return_sql = "";
    try {
      return_sql = idataright.getSqlBusiRight(UserId, RoleId, TableAlias)
        + getTasksBySql(TableName, TableId, RoleId, menuId, OperateType, TableAlias);
      return return_sql;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 带有权限的任务查询语句。不用传表名和主键
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param menuID-----------菜单ID
   * @param OperateType--------操作类型
   * @param TableAlias---------主表的别名
   * @return SQL语句
   * @throws Exception---------错误信息
   */
  public String getTasksWithRightBySqlWithSimply(String UserId, String RoleId, String menuId, String OperateType,
    String TableAlias) throws FAppException {
    return this.getTasksWithRightBySqlWithSimplyNew(UserId, RoleId, menuId, OperateType, TableAlias, true);
  }

  /**
   * 带有权限的任务查询语句。不用传表名和主键
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param menuID-----------菜单ID
   * @param OperateType--------操作类型
   * @param TableAlias---------主表的别名
   * @return SQL语句
   * @throws Exception---------错误信息
   */
  public String getTasksWithRightBySqlWithSimplyNew(String UserId, String RoleId, String menuId, String OperateType,
    String TableAlias, boolean isOuterTable) throws FAppException {

    String c = OperateType;
    String b = "";
    String return_sql = "";

    try {
      /*
       * 增了一个状态是查询真正全部的数据
       * 如果传进来操作类型为"999",则只返回数据权限的语句 如果不是则按原来的走
       */
      if ("999".equals(OperateType)) {
        return idataright.getSqlBusiRight(UserId, RoleId, TableAlias);
      }
      String data_right_sql = idataright.getSqlBusiRight(UserId, RoleId, "task");

      boolean is1stExistClauseFlag = true;// 标识是否是第一个"exist(.....)" 语句.
      // 如果是,则前面不加"or".
      // 循环调用多种状态的数据
      while (c.length() > 0) {
        if (c.indexOf("|") >= 0) {
          b = c.substring(0, c.indexOf("|"));
          c = c.substring(c.indexOf("|") + 1, c.length());
        } else {
          b = c;
          if (b.trim().length() > 0) {
            return_sql = return_sql
              + getTasksBySqlBySingleTypeSimply(data_right_sql, RoleId, menuId, b.trim(), TableAlias,
                is1stExistClauseFlag, isOuterTable);
          }
          break;
        }
        if (b.trim().length() > 0) {
          return_sql = return_sql
            + getTasksBySqlBySingleTypeSimply(data_right_sql, RoleId, menuId, b.trim(), TableAlias,
              is1stExistClauseFlag, isOuterTable);
        }
        is1stExistClauseFlag = false;
      }

      if (return_sql.trim().equals("")) {
        return " and 1 = 0 ";
      }

      return " and (" + return_sql + ")";
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 带有权限的任务查询语句。传明细表表名
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param menuID-----------菜单ID
   * @param OperateType--------操作类型
   * @param TableAlias---------主表的别名
   * @return SQL语句
   * @throws Exception---------错误信息
   */
  public String getTasksWithRightBySqlWithDetailTable(String UserId, String RoleId, String menuId, String OperateType,
    String detailTable) throws FAppException {

    String c = OperateType;
    String b = "";
    String return_sql = "";

    try {
      /*
       * 增了一个状态是查询真正全部的数据
       * 如果传进来操作类型为"999",则只返回数据权限的语句 如果不是则按原来的走
       */
      if ("999".equals(OperateType)) {
        return idataright.getSqlBusiRight(UserId, RoleId, detailTable);
      }

      String data_right_sql = idataright.getSqlBusiRight(UserId, RoleId, "task");

      boolean is1stExistClauseFlag = true;// 标识是否是第一个"exist(.....)" 语句.
      // 如果是,则前面不加"or".
      // 循环调用多种状态的数据
      while (c.length() > 0) {
        if (c.indexOf("|") >= 0) {
          b = c.substring(0, c.indexOf("|"));
          c = c.substring(c.indexOf("|") + 1, c.length());
        } else {
          b = c;
          if (b.trim().length() > 0) {
            return_sql = return_sql
              + getTasksBySqlBySingleTypeSimply(data_right_sql, RoleId, menuId, b.trim(), detailTable,
                is1stExistClauseFlag, false);
          }
          break;
        }
        if (b.trim().length() > 0) {
          return_sql = return_sql
            + getTasksBySqlBySingleTypeSimply(data_right_sql, RoleId, menuId, b.trim(), detailTable,
              is1stExistClauseFlag, false);
        }
        is1stExistClauseFlag = false;
      }

      if (return_sql.trim().equals("")) {
        return " and 1 = 0 ";
      }

      return " and (" + return_sql + ") ";
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 获取工作流权限，可设置是否关心外部关联表
   * @param data_right_sql
   * @param RoleId
   * @param menuId
   * @param OperateType
   * @param TableAlias
   * @param is1stExistClauseFlag
   * @param isOuterTable
   * @param orgDataRight  //机构权限李文全2013-06-04增加 
   * @author ydz
   * @return
   */
  private String getTasksBySqlBySingleTypeSimply(String data_right_sql, String RoleId, String menuId,
    String OperateType, String TableAlias, boolean is1stExistClauseFlag, boolean isOuterTable) {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer return_sql = new StringBuffer();

    StringBuffer sql = new StringBuffer();
    sql
      .append("select distinct 'a' as wf_table_name, c.id_column_name,")
      .append(" c.outer_table_name,c.outer_column_name,c.relation_column_name,c.gather_flag ")
      .append(" from sys_wf_module_node")
      .append(Tools.addDbLink())
      .append(" a,sys_wf_role_node")
      .append(Tools.addDbLink())
      .append(" b ,sys_wf_nodes")
      .append(Tools.addDbLink())
      .append(" c")
      .append(" where a.node_id=b.node_id and b.node_id=c.node_id  ")
      .append(
        " and a.set_year = b.set_year and a.set_year = c.set_year and a.rg_code = b.rg_code  and a.rg_code = c.rg_code ")
      .append(" and b.role_id=? and a.module_id=? and a.rg_code=? and a.set_year=? and c.outer_table_name is null ")
      .append(" union ")
      .append(" select distinct c.wf_table_name,c.id_column_name,")
      .append(" c.outer_table_name,c.outer_column_name,c.relation_column_name,c.gather_flag ")
      .append(" from sys_wf_module_node")
      .append(Tools.addDbLink())
      .append(" a,sys_wf_role_node")
      .append(Tools.addDbLink())
      .append(" b ,sys_wf_nodes")
      .append(Tools.addDbLink())
      .append(" c")
      .append(" where a.node_id=b.node_id and b.node_id=c.node_id  ")
      .append(
        " and b.role_id=? and a.module_id=? and c.outer_table_name is not null and a.rg_code=b.rg_code and a.rg_code=c.rg_code and a.set_year=b.set_year and a.set_year=c.set_year and a.rg_code=? and a.set_year=?");
    List result = new ArrayList();
    result = dao.findBySql(sql.toString(), new Object[] { RoleId, menuId, rg_code, setYear, RoleId, menuId, rg_code,
      setYear });
    //取缓存的节点信息 李文全2013-06-13
    if (!UtilCache.moduleRoleNodeIDCache.containsKey(UtilCache.getRgYearKey())
      || UtilCache.getNodeIdByModuleRole(menuId + RoleId) == null
      || "".equals(UtilCache.getNodeIdByModuleRole(menuId + RoleId))) {
      String nodeIds = this.getNodeIdByModuleRole(menuId, RoleId);
      UtilCache.putNodeIdByModuleRole(menuId + RoleId, nodeIds);
    }
    String nodeId = UtilCache.getNodeIdByModuleRole(menuId + RoleId);

    for (int i = 0; i < result.size(); i++) {
      String tmp_inner_table_id = ((Map) result.get(i)).get("id_column_name") == null ? "" : (String) ((Map) result
        .get(i)).get("id_column_name");
      String tmp_inner_table_name = ((Map) result.get(i)).get("wf_table_name") == null ? "" : (String) ((Map) result
        .get(i)).get("wf_table_name");

      String tmp_outer_table_name = "";
      String tmp_outer_table_id = "";
      String tmp_relation_table_id = "";
      //ydz 如果不关心外部表，则为空，不拼接外部表
      if (isOuterTable) {
        tmp_outer_table_name = ((Map) result.get(i)).get("outer_table_name") == null ? "" : (String) ((Map) result
          .get(i)).get("outer_table_name");
        tmp_outer_table_id = ((Map) result.get(i)).get("outer_column_name") == null ? "" : (String) ((Map) result
          .get(i)).get("outer_column_name");
        tmp_relation_table_id = ((Map) result.get(i)).get("relation_column_name") == null ? "" : (String) ((Map) result
          .get(i)).get("relation_column_name");
      }

      if (OperateType.equals("001") || OperateType.equals("004")) {
        // 如果工作流节点的外部事务提醒表不为空，则用外部事务提醒表和内部事务提醒表
        // 相关联，然后用内部事务提醒表的id跟entity_id相关联 begin
        if ("".equals(tmp_outer_table_name)) {
          return_sql.append((i == 0 && is1stExistClauseFlag ? "" : "or"))
            .append(" exists (select 1 from SYS_WF_CURRENT_ITEM task where ").append(" task.node_id in (")
            .append(nodeId).append(") ").append(" and task.status_code= '" + OperateType + "'").append(" ")
            .append(data_right_sql).append(" and task.entity_id=").append(TableAlias).append(".")
            .append(tmp_inner_table_id)
            .append(" and task.rg_code='" + rg_code + "' and task.set_year=" + setYear + ")");
        } else {
          return_sql
            .append((i == 0 && is1stExistClauseFlag ? "" : "or"))
            .append(
              " exists (select 1 from SYS_WF_CURRENT_ITEM task, sys_wf_module_node module, sys_wf_role_node role, ")
            .append(tmp_inner_table_name).append(" b ").append(" where ")
            .append(" task.node_id = module.node_id and task.node_id = role.node_id ")
            .append(" and module.node_id = role.node_id ").append(" and module.module_id = ").append(menuId)
            .append(" and task.status_code= '" + OperateType + "'").append(" and role.role_id = ").append(RoleId)
            .append(" ").append(data_right_sql).append(" and ").append(TableAlias).append(".")
            .append(tmp_outer_table_id).append(" = b").append(".").append(tmp_relation_table_id)
            .append(" and task.entity_id= b").append(".").append(tmp_inner_table_id)
            .append(" and task.rg_code='" + rg_code + "' and task.set_year=" + setYear + ")");
        }
      } else {
        if ("".equals(tmp_outer_table_name)) {
          return_sql.append((i == 0 && is1stExistClauseFlag ? "" : "or"))
            .append(" exists (select 1 from SYS_WF_COMPLETE_ITEM task where ").append(" task.node_id in (")
            .append(nodeId).append(") ").append(" and task.status_code= '" + OperateType + "' ").append(data_right_sql)
            .append(" and task.entity_id=").append(TableAlias).append(".").append(tmp_inner_table_id)
            .append(" and task.rg_code='" + rg_code + "' and task.set_year=" + setYear + ")");
        } else {
          return_sql
            .append((i == 0 && is1stExistClauseFlag ? "" : "or"))
            .append(
              " exists (select 1 from SYS_WF_COMPLETE_ITEM task, sys_wf_module_node module, sys_wf_role_node role, ")
            .append(tmp_inner_table_name)
            .append(" b ")
            .append(" where ")
            .append(" task.node_id = module.node_id and task.node_id = role.node_id ")
            .append(" and module.node_id = role.node_id ")
            .append(" and module.module_id = ")
            .append(menuId)
            .append(" and task.status_code= '" + OperateType + "'")
            .append(" and role.role_id = ")
            .append(RoleId)
            .append(" ")
            .append(data_right_sql)
            .append(" and ")
            .append(TableAlias)
            .append(".")
            .append(tmp_outer_table_id)
            .append(" = b")
            .append(".")
            .append(tmp_relation_table_id)
            .append(" and task.entity_id= b")
            .append(".")
            .append(tmp_inner_table_id)
            .append(
              " and task.rg_code=module.rg_code and task.rg_code=role.rg_code and task.set_year=module.set_year and task.set_year=role.set_year and task.rg_code='"
                + rg_code + "' and task.set_year=" + setYear + ")");
        }
      }
    }
    return return_sql.toString();

  }

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
  private boolean getValidWfNode(String condition_id, XMLData rowData) throws FAppException {
    try {
      /* 调用规则BeanShell验证 */
      return ruleFactory.isMatchByBSH(condition_id, rowData);
    } catch (FAppException fae) {
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 得到传入数据是否需要记帐标志。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDtos--业务数据DTO列表
   * @return String
   * @throws Exception---------错误信息
   */
  private String getTollyFlag(String old_current_node_id, String menuid, String roleid, String actiontype,
    String bill_table_name, String detail_table_name, FVoucherDTO inputFVoucherDto) throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer select_sql = new StringBuffer();
    String tolly_flag = "";
    String entityId = "";
    XMLData rowData = new XMLData();
    rowData = inputFVoucherDto.toXMLData();
    List rs = null;

    try {
      if (inputFVoucherDto.getDetails() != null && inputFVoucherDto.getDetails().size() > 0) {
        inputFVoucherDto = (FVoucherDTO) inputFVoucherDto.getDetails().get(0);
      }
      if (inputFVoucherDto.getVou_id() == null || inputFVoucherDto.getVou_id().equals("")) {
        throw new Exception("需要传入vou_id!");
      } else {
        entityId = inputFVoucherDto.getVou_id();
      }
      // 录入操作时
      if (actiontype.equals("INPUT")) {
        // 调用通用方法取得根据设置能找到的流程和节点
        List tmp_can_go_list = doSingleProcessSimplyInputDetailSelect(menuid, roleid, detail_table_name, rowData);
        if (tmp_can_go_list == null || tmp_can_go_list.size() == 0) {
          throw new Exception("数据ID为：" + entityId + "的数据根据设置无法找到对应的节点!");
        } else {
          Map tmp_more_wf = new HashMap();
          // 判断是否找到了多个节点
          for (int i = 0; i < tmp_can_go_list.size(); i++) {
            if (!tmp_more_wf.containsKey(((Map) tmp_can_go_list.get(i)).get("wf_id").toString())) {
              tmp_more_wf.put(((Map) tmp_can_go_list.get(i)).get("wf_id").toString(), "");
            }
          }
          if (tmp_more_wf.keySet().size() > 1) {
            throw new Exception("数据ID为：" + entityId + "的数据根据设置信息能找到多个流程!");
          } else {
            // 根据节点找到对应的记帐类型
            select_sql.append("select * from sys_wf_node_tolly_action_type").append(Tools.addDbLink())
              .append(" where node_id=? and rg_code=? and set_year=?");
            rs = dao.findBySql(select_sql.toString(), new Object[] {
              ((Map) tmp_can_go_list.get(0)).get("node_id").toString(), rg_code, setYear });
            // 如果找不到记帐类型，默认为不记帐
            if (rs.size() == 0) {
              tolly_flag = "-1";
            } else {
              tolly_flag = ((Map) rs.get(0)).get("tolly_flag").toString();
            }
          }
        }
      } else if (menuid == null && roleid == null) {
        select_sql.append("select distinct tolly_flag  from SYS_WF_NODE_TOLLY_ACTION_TYPE").append(Tools.addDbLink())
          .append(" where node_id = ? and action_type_code=? and rg_code=? and set_year=?");
        rs = dao.findBySql(select_sql.toString(), new Object[] { old_current_node_id, actiontype, rg_code, setYear });
        // 如果找不到记帐类型，默认为不记帐
        if (rs.size() == 0) {
          tolly_flag = "-1";
        } else {
          Map map = (Map) rs.get(0);
          tolly_flag = map.get("tolly_flag").toString();
        }
      } else {
        // 除录入操作以外的操作
        if (menuid == null || menuid.equals("")) {
          throw new Exception("需要传入menuid!");
        }
        if (roleid == null || roleid.equals("")) {
          throw new Exception("需要传入roleid!");
        }
        // 找到下一节点的记帐类型
        if (actiontype.equals("RECALL")) {
          // modified by bigdog 080911在判断撤消操作的记账类型时，
          // 不是像以前那样判断该节点上撤消操作配置的记账类型，而是
          // 根据要被撤消的那条操作的记账类型来决定撤消操作的记账类型
          select_sql
            .append("select distinct b.wf_id, b.current_node_id, b.next_node_id, b.tolly_flag ")
            .append("from vw_sys_wf_current_end_tasks b, sys_wf_module_node a, sys_wf_role_node c ")
            .append(" where a.node_id = c.node_id ")
            .append("   and a.module_id = ? ")
            .append("   and c.role_id = ? ")
            .append("   and ((b.current_node_id = a.node_id)) ")
            .append(
              "   and b.entity_id = ? and b.wf_table_name = '"
                + detail_table_name
                + "' and b.rg_code=a.rg_code and b.rg_code=c.rg_code and b.set_year=a.set_year and b.set_year=c.set_year and b.rg_code=? and b.set_year=?");

          rs = dao.findBySql(select_sql.toString(), new Object[] { menuid, roleid, entityId, rg_code, setYear });
        } else {
          select_sql
            .append(
              "select distinct b.wf_id,b.current_node_id,b.next_node_id,d.tolly_flag  from vw_sys_wf_current_end_tasks")
            .append(Tools.addDbLink())
            .append(" b")
            .append(" ,vw_sys_wf_node_relation")
            .append(Tools.addDbLink())
            .append(" d where d.module_id=? and d.role_id=?  ")
            .append(" and ((b.next_node_id =d.node_id and b.action_type_code in ('INPUT','NEXT','BACK'))")
            .append(" or (b.current_node_id =d.node_id and b.action_type_code in ('INPUT','NEXT','BACK') ")
            .append("   and d.action_type_code in ('DISCARD', 'HANG', 'DELETE'))")
            // 增加b.current_node_id =d.node_id 在修改送审的时候添加关联条件
            // 解决修改后送审时导致进行终审记账
            .append(
              " or (b.current_node_id = d.node_id and b.next_node_id='0' and b.action_type_code in ('DISCARD','HANG','DELETE','EDIT'))) ")
            .append(
              " and b.entity_id = ? and d.action_type_code=? and b.wf_table_name = '" + detail_table_name
                + "' and b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=? and b.set_year=?");

          // b.current_node_id =d.node_id 放到OR里
          rs = dao.findBySql(select_sql.toString(), new Object[] { menuid, roleid, entityId, actiontype, rg_code,
            setYear });
        }

        // 如果找不到记帐类型，默认为不记帐
        if (rs.size() == 0) {
          tolly_flag = "-1";
        } else {
          Map map = (Map) rs.get(0);
          tolly_flag = map.get("tolly_flag").toString();
          // System.out.println("action = " + actiontype + " TollyFlag
          // = " + tolly_flag);
        }

      }
      return tolly_flag;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 
   * @param old_current_node_id
   * @param menuid
   * @param roleid
   * @param actiontype
   * @param inputFVoucherDto
   * @param isForced2Execute
   * @return
   * @throws Exception
   */
  private String getNextNodeId(String old_current_node_id, String menuid, String roleid, String actiontype,
    FVoucherDTO inputFVoucherDto, boolean isForced2Execute) throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    try {
      String entityId = inputFVoucherDto.getVou_id();
      if (entityId == null || entityId.equals("")) {
        throw new Exception("需要传入vou_id!");
      }

      StringBuffer select_sql = new StringBuffer();

      if (!actiontype.equals("INPUT") && !isForced2Execute) {
        if (menuid == null || menuid.equals("")) {
          throw new Exception("需要传入menuid!");
        }
        if (roleid == null || roleid.equals("")) {
          throw new Exception("需要传入roleid!");
        }
      }

      List rs = null;

      if (!actiontype.equals("INPUT") && !isForced2Execute) {
        select_sql
          .append("select distinct b.wf_id,b.current_node_id,b.next_node_id,b.action_type_code ")
          .append("from sys_wf_current_tasks")
          .append(Tools.addDbLink())
          .append(" b, vw_sys_wf_node_relation")
          .append(Tools.addDbLink())
          .append(" d ")
          .append("where d.module_id=? and d.role_id=? ")
          .append(
            "and b.entity_id = ? and b.action_type_code = ? and b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=? and b.set_year=?");

        // 判断数据是否存在
        rs = dao.findBySql(select_sql.toString(),
          new Object[] { menuid, roleid, entityId, actiontype, rg_code, setYear });
      } else {
        select_sql
          .append("select distinct b.wf_id, b.current_node_id, b.next_node_id, b.action_type_code ")
          .append("from sys_wf_current_tasks")
          .append(Tools.addDbLink())
          .append(" b ,vw_sys_wf_node_relation")
          .append(Tools.addDbLink())
          .append(" d ")
          .append(
            "where b.entity_id = ? and b.action_type_code = ? and b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=? and d.set_year=?");

        // 判断数据是否存在
        rs = dao.findBySql(select_sql.toString(), new Object[] { entityId, actiontype, rg_code, setYear });
      }

      if (rs != null && rs.size() > 0) {
        Map map = (Map) rs.get(0);
        if (actiontype.equals("NEXT") || actiontype.equals("BACK") || actiontype.equals("INPUT")) {
          return map.get("next_node_id").toString();
        } else {
          return map.get("current_node_id").toString();
        }
      }

      return old_current_node_id;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 得到监控规则表
   * 
   * @param old_current_node_id
   * @param menuid
   * @param roleid
   * @param actiontype
   * @param bill_table_name
   * @param detail_table_name
   * @param inputFVoucherDto
   * @return
   * @throws Exception
   */
  private List getInspectRules(String old_current_node_id, String menuid, String roleid, String actiontype,
    String bill_table_name, String detail_table_name, FVoucherDTO inputFVoucherDto) throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    try {
      String entityId = inputFVoucherDto.getVou_id();
      if (entityId == null || entityId.equals("")) {
        throw new Exception("需要传入vou_id!");
      }

      StringBuffer select_sql = new StringBuffer();

      List rs = null;
      // 录入操作时
      if (actiontype.equals("INPUT")) {
        // 调用通用方法取得根据设置能找到的流程和节点
        List tmp_can_go_list = isDoSingleProcessSimplyInputDetailSelect(menuid, roleid, detail_table_name,
          inputFVoucherDto.toXMLData());
        if (tmp_can_go_list == null || tmp_can_go_list.size() == 0) {
          throw new Exception("数据ID为：" + entityId + "的数据根据设置无法找到对应的节点!");
        } else {
          Map tmp_more_wf = new HashMap();
          // 判断是否找到了多个节点
          for (int i = 0; i < tmp_can_go_list.size(); i++) {
            if (!tmp_more_wf.containsKey(((Map) tmp_can_go_list.get(i)).get("wf_id").toString())) {
              tmp_more_wf.put(((Map) tmp_can_go_list.get(i)).get("wf_id").toString(), "");
            }
          }
          if (tmp_more_wf.keySet().size() > 1) {
            throw new Exception("数据ID为：" + entityId + "的数据根据设置信息能找到多个流程!");
          } else {
            // 根据节点找到对应的监控规则
            select_sql.append("select * from sys_wf_node_action_inspect" + Tools.addDbLink()).append(
              " where node_id=? and action_type_code = ? and rg_code=? and set_year=?");
            rs = dao.findBySql(select_sql.toString(), new Object[] {
              ((Map) tmp_can_go_list.get(0)).get("next_node_id").toString(), actiontype, rg_code, setYear });
          }
        }
      } else if (menuid == null && roleid == null) {
        select_sql.append("select * from sys_wf_node_action_inspect").append(Tools.addDbLink())
          .append(" where node_id = ? and action_type_code = ? and rg_code=? and set_year=?");
        rs = dao.findBySql(select_sql.toString(), new Object[] { old_current_node_id, actiontype, rg_code, setYear });

      } else {
        // 除录入操作以外的操作
        if (menuid == null || menuid.equals("")) {
          throw new Exception("需要传入menuid!");
        }
        if (roleid == null || roleid.equals("")) {
          throw new Exception("需要传入roleid!");
        }
        // 找到下一节点的监控规则
        if (actiontype.equals("RECALL")) {
          select_sql
            .append("select distinct b.wf_id, d.node_id, d.action_type_code, d.inspect_rule_id ")
            .append(" from sys_wf_current_tasks")
            .append(Tools.addDbLink())
            .append(" b, vw_sys_wf_node_inspect")
            .append(Tools.addDbLink())
            .append(" d ")
            .append(" where d.module_id=? and d.role_id=? and b.current_node_id = d.node_id ")
            .append(
              " and b.entity_id = ? and d.action_type_code=? and b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=? and d.set_year=?");
        } else {
          select_sql
            .append("select distinct b.wf_id, d.node_id, d.action_type_code, d.inspect_rule_id ")
            .append(" from sys_wf_current_tasks")
            .append(Tools.addDbLink())
            .append(" b, vw_sys_wf_node_inspect")
            .append(Tools.addDbLink())
            .append(" d ")
            .append(
              " where d.module_id=? and d.role_id=? and ((b.next_node_id =d.node_id and b.action_type_code in ('INPUT','NEXT','BACK'))")
            .append(" or (b.next_node_id='0' and b.action_type_code in ('DISCARD','HANG','DELETE','EDIT'))) ")
            .append(
              " and b.entity_id = ? and d.action_type_code=? and b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=? and d.set_year=?");
        }

        rs = dao.findBySql(select_sql.toString(),
          new Object[] { menuid, roleid, entityId, actiontype, rg_code, setYear });

      }
      return rs;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 单条调用交易令的更新接口。
   * 
   * @param newFVoucherDto
   *            单据newDTO
   * @return FVoucherDTO
   * @throws Exception
   *             错误信息
   * @author justin 2008年4月29日新增
   */
  private FVoucherDTO doBusVouUpdate(FVoucherDTO newFVoucherDto) throws FAppException {
    try {
      return ivoucher.update(newFVoucherDto);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 批量调用交易令的更新接口。
   * 
   * @param newFVoucherDtos
   *            单据newDTO
   * @return FVoucherDTO
   * @throws Exception
   *             错误信息
   * @author 
   */
  private List doBusVouUpdateBatch(List newFVoucherDtos) throws FAppException {
    try {
      return ivoucher.updateBatch(newFVoucherDtos);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 批量调用交易令的录入接口。
   * 
   * @param saveFVoucherDtos
   *            单据newDTO
   * @return FVoucherDTO
   * @throws Exception
   *             错误信息
   * @author justin 2008年4月29日修改
   */
  private List doBusVouSaveBatch(List saveFVoucherDtos) throws FAppException {

    try {
      return ivoucher.saveBatch(saveFVoucherDtos);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 批量调用交易令的终审接口。
   * 
   * @param auditFVoucherDtos
   *            单据newDTO
   * @throws Exception
   *             错误信息
   * @author justin 2008年4月29日修改
   */
  private List doBusVouAuditBatch(List auditFVoucherDtos) throws FAppException {

    try {
      return ivoucher.auditBatch(auditFVoucherDtos);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 单条调用交易令的撤销终审接口
   * 
   * @param cancelAuditFVoucherDto
   *            单据newDTO
   * @throws Exception
   *             错误信息
   * @author 
   */
  private FVoucherDTO doBusVouCancelAudit(FVoucherDTO cancelAuditFVoucherDto) throws FAppException {
    try {
      return ivoucher.cancelAudit(cancelAuditFVoucherDto);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 批量调用交易令的撤销终审接口
   * 
   * @param cancelAuditFVoucherDtos
   *            单据newDTO
   * @throws Exception
   *             错误信息
   * @author 
   */
  private List doBusVouCancelAuditBatch(List cancelAuditFVoucherDtos) throws FAppException {

    try {
      return ivoucher.cancelAuditBatch(cancelAuditFVoucherDtos);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 批量调用交易令的删除接口。
   * 
   * @param deleteFVoucherDtos
   *            单据DTO
   * @throws Exception
   *             错误信息
   * @author  
   */
  private List doBusVouDeleteBatch(List deleteFVoucherDtos) throws FAppException {
    try {
      return ivoucher.deleteBatch(deleteFVoucherDtos);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 批量调用交易令的作废接口。
   * 
   * @param invalidaFVoucherDtos
   *            单据DTO
   * @throws Exception
   *             错误信息
   * @author
   */
  private List doBusVouInvalidBatch(List invalidateFVoucherDtos) throws FAppException {
    try {
      return ivoucher.invalidBatch(invalidateFVoucherDtos);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 通用撤消处理操作（对批量数据）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDtos--业务数据DTO列表
   * @param auto_tolly_flag----是否通过工作流自动记账
   * @return List（内部含FVoucherDTO)
   * @throws Exception---------错误信息
   * @author justin2008年5月13日修改批量调用总帐接口处理撤销
   */
  private List undoBatchProcessReturnObj(String menuid, String roleid, String bill_table_name,
    String detail_table_name, String operationname, String operationdate, String operationremark,
    List inputFVoucherDtos, boolean auto_tolly_flag) throws FAppException {
    List return_list = new ArrayList();
    try {
      Iterator itr = inputFVoucherDtos.iterator();
      FVoucherDTO inputFVoucherDto;
      List onload = new ArrayList();
      List over = new ArrayList();
      String tolly_flag = "-1";
      IInspectService inspectService = null;
      try {
        inspectService = (IInspectService) SessionUtil.getServerBean("sys.inspectService");
      } catch (Exception e) {
        // 访问不到监控包
      }
      while (itr.hasNext()) {// 循环按照在途或者终审处理数据
        inputFVoucherDto = (FVoucherDTO) itr.next();
        // 根据单表名和明细表名，获取记帐类型
        if (auto_tolly_flag)// 自动记帐
        {
          // 根据传入参数取得记帐类型
          tolly_flag = this.getTollyFlag(null, menuid, roleid, "RECALL", bill_table_name, detail_table_name,
            inputFVoucherDto);
          // 如果是龙图平台
          if (SessionUtil.getParaMap().get("switch01").toString().equals("1")) {
            // 如果是龙图平台，撤销时需要传入2
            inputFVoucherDto.setIs_end(2);
          } else {
            inputFVoucherDto.setIs_end(Integer.parseInt(tolly_flag));
          }
          if (tolly_flag.equals("0"))// 记帐类型为在途记帐
          {
            onload.add(inputFVoucherDto);
          } else if (tolly_flag.equals("1")) {// 记帐类型为终审记帐
            over.add(inputFVoucherDto);
          }
        }
      }
      if (auto_tolly_flag)// 自动记帐
      {
        return_list.add(this.doBusVouUpdateBatch(onload));// 调用批量更新执行在途的撤销
        return_list.add(this.doBusVouCancelAuditBatch(over));// 调用批量更新执行终审核的撤销
      } else// 不记帐，直接赋值
      {
        return_list = inputFVoucherDtos;
      }
      List return_data = new ArrayList();
      int ifv = inputFVoucherDtos.size();
      for (int vv = 0; vv < ifv; vv++) {// 循环调用资金监控检查
        inputFVoucherDto = (FVoucherDTO) inputFVoucherDtos.get(vv);
        // itr = return_list.iterator();
        // 资金监控
        // 如果访问不到监控类包，则不能调用其接口。
        if (inspectService != null) {
          List nodeInspectRules = null;
          if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
            nodeInspectRules = this.getInspectRules(null, menuid, roleid, "RECALL", bill_table_name, detail_table_name,
              inputFVoucherDto);
          } else {
            nodeInspectRules = this.getInspectRules(null, menuid, roleid, "RECALL", bill_table_name, detail_table_name,
              (FVoucherDTO) inputFVoucherDto.getDetails().get(0));
          }
          // 无论能否找到规则列表，都调用监控接口
          String inspectNodeId = null;
          if (nodeInspectRules != null && nodeInspectRules.size() > 0) {
            inspectNodeId = (String) ((XMLData) nodeInspectRules.get(0)).get("node_id");
          } else {
            if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
              inspectNodeId = getNextNodeId(null, menuid, roleid, "RECALL", inputFVoucherDto, false);
            } else {
              inspectNodeId = getNextNodeId(null, menuid, roleid, "RECALL", (FVoucherDTO) inputFVoucherDto.getDetails()
                .get(0), false);
            }
          }
          String rtnMsg = inspectService.inspectInstance(getWfIdByNodeId(inspectNodeId), detail_table_name,
            inspectNodeId, null, "RECALL", operationname, operationdate, operationremark, nodeInspectRules,
            inputFVoucherDto, menuid, roleid);
          inputFVoucherDto.setWarning(rtnMsg);
        }
        // 如果没有明细数据，则直接调工作流
        if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
          if (detail_table_name == null || detail_table_name.equals("")) {
            throw new Exception("需要传入detail_table_name!");
          }
          return_data.add(undoSingleProcessSimplyReturnObj(menuid, roleid, detail_table_name, inputFVoucherDto));
        } else {
          // 如果是单＋明细，则先将明细走流程，后走单的流程
          if (detail_table_name == null || detail_table_name.equals("")) {
            throw new Exception("需要传入detail_table_name!");
          }
          if (bill_table_name == null || bill_table_name.equals("")) {
            throw new Exception("需要传入bill_table_name!");
          }
          for (int i = 0; i < inputFVoucherDto.getDetails().size(); i++) {
            FVoucherDTO detailDto = new FVoucherDTO();
            detailDto = (FVoucherDTO) inputFVoucherDto.getDetails().get(i);
            undoSingleProcessSimplyReturnObj(menuid, roleid, detail_table_name, detailDto);
          }
          return_data.add(inputFVoucherDto);
        }
      }
      return return_data;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 通用撤消处理操作（对批量数据,不用传TABLE_NAME）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param inputFVoucherDtos--业务数据DTO列表
   * @param auto_tolly_flag----是否通过工作流自动记账
   * @return List（内部含FVoucherDTO)
   * @throws Exception---------错误信息
   * @author justin2008年5月13日修改批量调用总帐接口处理撤销
   * @throws Exception
   */
  private List undoBatchProcessReturnObjWithNoTableName(String menuid, String roleid, String operationname,
    String operationdate, String operationremark, List inputFVoucherDtos, boolean auto_tolly_flag) throws Exception {
    List return_list = new ArrayList();
    try {
      Iterator itr = inputFVoucherDtos.iterator();
      FVoucherDTO inputFVoucherDto;
      String bill_table_name = "";
      String detail_table_name = "";
      List onload = new ArrayList();
      List over = new ArrayList();
      String tolly_flag = "-1";
      while (itr.hasNext()) {// 循环按照在途或者终审处理数据
        inputFVoucherDto = (FVoucherDTO) itr.next();// 取数据
        // 获取明细和单的表
        if (inputFVoucherDto.getBilltype_code() == null || inputFVoucherDto.getBilltype_code().equals("")) {
          throw new Exception("没有传入billtype_code!");
        } else {
          bill_table_name = ((FBillTypeDTO) billtype.getBillTypeByCode(inputFVoucherDto.getBilltype_code()))
            .getTable_name();

          // 如果是单＋明细形式，则要设置bill_table_name
          if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0
            || ((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code() == null
            || ((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code().equals("")) {
            detail_table_name = bill_table_name;
          } else {
            /*
             * update daiwei 20070307 start 设置缓存
             */
            String billType_code = ((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code().toString();
            String table_name = UtilCache.getTableNameByBillTypeCode(billType_code);
            if (table_name != null && !table_name.equals("")) {
              detail_table_name = table_name;
            } else {
              detail_table_name = ((FBillTypeDTO) billtype.getBillTypeByCode(billType_code)).getTable_name();
              UtilCache.putTableNameByBillTypeCode(billType_code, detail_table_name);
            }
          }
        }
        // 根据单表名和明细表名，获取记帐类型
        if (auto_tolly_flag)// 自动记帐
        {
          // 根据传入参数取得记帐类型
          tolly_flag = this.getTollyFlag(null, menuid, roleid, "RECALL", bill_table_name, detail_table_name,
            inputFVoucherDto);
          // 如果是龙图平台
          if (SessionUtil.getParaMap().get("switch01").toString().equals("1")) {
            // 如果是龙图平台，撤销时需要传入2
            inputFVoucherDto.setIs_end(2);
          } else {
            // inputFVoucherDto
            // .setIs_end(Integer.parseInt(tolly_flag));
          }
          if (tolly_flag.equals("0"))// 记帐类型为在途记帐
          {
            onload.add(inputFVoucherDto);
          } else if (tolly_flag.equals("1")) {// 记帐类型为终审记帐
            over.add(inputFVoucherDto);
          }
        }
      }
      if (auto_tolly_flag)// 自动记帐
      {
        return_list.add(this.doBusVouUpdateBatch(onload));// 调用批量更新执行在途的撤销
        return_list.add(this.doBusVouCancelAuditBatch(over));// 调用批量更新执行终审核的撤销
      } else// 不记帐，直接赋值
      {
        return_list = inputFVoucherDtos;
      }
      return_list = this.doInspect4UndoBatchProcessReturn(menuid, roleid, operationname, operationdate,
        operationremark, inputFVoucherDtos, auto_tolly_flag);
      return return_list;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      e.printStackTrace();
      throw new FAppException(e);

    }
  }

  /**
   * 撤销时需要调用的资金监控处理
   * 
   * @param inputFVoucherDtos
   *            数据
   * 
   * @return 处理后的数据
   * @throws Exception
   * @author justin2008年5月13日新增批量调用总帐接口处理撤销
   */
  private List doInspect4UndoBatchProcessReturn(String menuid, String roleid, String operationname,
    String operationdate, String operationremark, List inputFVoucherDtos, boolean auto_tolly_flag) throws Exception {
    List return_list = new ArrayList();
    Iterator itr = inputFVoucherDtos.iterator();
    FVoucherDTO inputFVoucherDto;
    String bill_table_name = "";
    String detail_table_name = "";
    // 资金监控
    IInspectService inspectService = null;
    try {
      inspectService = (IInspectService) SessionUtil.getServerBean("sys.inspectService");
    } catch (Exception e) {
      // 访问不到监控包
    }
    while (itr.hasNext()) {// 循环调用资金监控检查，
      inputFVoucherDto = (FVoucherDTO) itr.next();// 取数据
      // 获取明细和单的表
      if (inputFVoucherDto.getBilltype_code() == null || inputFVoucherDto.getBilltype_code().equals("")) {
        throw new Exception("没有传入billtype_code!");
      } else {
        bill_table_name = ((FBillTypeDTO) billtype.getBillTypeByCode(inputFVoucherDto.getBilltype_code()))
          .getTable_name();

        // 如果是单＋明细形式，则要设置bill_table_name
        if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0
          || ((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code() == null
          || ((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code().equals("")) {
          detail_table_name = bill_table_name;
        } else {
          /*
           * update daiwei 20070307 start 设置缓存
           */
          String billType_code = ((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code().toString();
          String table_name = UtilCache.getTableNameByBillTypeCode(billType_code);
          if (table_name != null && !table_name.equals("")) {
            detail_table_name = table_name;
          } else {
            detail_table_name = ((FBillTypeDTO) billtype.getBillTypeByCode(billType_code)).getTable_name();
            UtilCache.putTableNameByBillTypeCode(billType_code, detail_table_name);
          }
        }
      }

      // 如果访问不到监控类包，则不能调用其接口。
      if (inspectService != null) {
        List nodeInspectRules = null;
        if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
          nodeInspectRules = this.getInspectRules(null, menuid, roleid, "RECALL", bill_table_name, detail_table_name,
            inputFVoucherDto);
        } else {
          nodeInspectRules = this.getInspectRules(null, menuid, roleid, "RECALL", bill_table_name, detail_table_name,
            (FVoucherDTO) inputFVoucherDto.getDetails().get(0));
        }
        // 无论能否找到规则列表，都调用监控接口
        String inspectNodeId = null;
        if (nodeInspectRules != null && nodeInspectRules.size() > 0) {
          inspectNodeId = (String) ((XMLData) nodeInspectRules.get(0)).get("node_id");
        } else {
          if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
            inspectNodeId = getNextNodeId(null, menuid, roleid, "RECALL", inputFVoucherDto, false);
          } else {
            inspectNodeId = getNextNodeId(null, menuid, roleid, "RECALL", (FVoucherDTO) inputFVoucherDto.getDetails()
              .get(0), false);
          }
        }
        String rtnMsg = inspectService.inspectInstance(getWfIdByNodeId(inspectNodeId), detail_table_name,
          inspectNodeId, null, "RECALL", operationname, operationdate, operationremark, nodeInspectRules,
          inputFVoucherDto, menuid, roleid);
        inputFVoucherDto.setWarning(rtnMsg);
      }
      // 如果没有明细数据，则直接调工作流
      if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
        if (detail_table_name == null || detail_table_name.equals("")) {
          throw new Exception("需要传入detail_table_name!");
        }
        return_list.add(undoSingleProcessSimplyReturnObj(menuid, roleid, detail_table_name, inputFVoucherDto));
      } else {
        // 如果是单＋明细，则先将明细走流程，后走单的流程
        if (detail_table_name == null || detail_table_name.equals("")) {
          throw new Exception("需要传入detail_table_name!");
        }
        if (bill_table_name == null || bill_table_name.equals("")) {
          throw new Exception("需要传入bill_table_name!");
        }
        for (int i = 0; i < inputFVoucherDto.getDetails().size(); i++) {
          FVoucherDTO detailDto = new FVoucherDTO();
          detailDto = (FVoucherDTO) inputFVoucherDto.getDetails().get(i);
          undoSingleProcessSimplyReturnObj(menuid, roleid, detail_table_name, detailDto);
        }
        return_list.add(inputFVoucherDto);
      }
    }
    return return_list;
  }

  /**
   * 查询某菜单的事务提醒。
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param menuID-----------菜单ID
   * @return List(内部含FWFLogDTO)
   * @throws Exception---------错误信息
   */
  private List getModuleTasksStatInfo(String UserId, String RoleId, String menuId, String OperateType)
    throws FAppException {
    List return_list = new ArrayList();
    FRightSqlDTO data_right_sql;
    try {
      data_right_sql = idataright.getSqlBusiRightByJoin(UserId, RoleId, "task");
    } catch (Exception e) {
      throw new FAppException(e);
    }

    List result = null;
    //add by liuzw 20120410
    String rg_code = getRgCode();
    String setYear = getSetYear();

    //优化查询,取缓存节点信息 李文全2013-06-20
    if (!UtilCache.moduleRoleNodeIDCache.containsKey(UtilCache.getRgYearKey())
      || UtilCache.getNodeIdByModuleRole(menuId + RoleId) == null
      || "".equals(UtilCache.getNodeIdByModuleRole(menuId + RoleId))) {
      String nodeIds = this.getNodeIdByModuleRole(menuId, RoleId);
      UtilCache.putNodeIdByModuleRole(menuId + RoleId, nodeIds);
    }
    String nodeId = UtilCache.getNodeIdByModuleRole(menuId + RoleId);
    if (nodeId == null || nodeId.equals(""))
      return new ArrayList();

    StringBuffer return_sql = new StringBuffer();
    return_sql
      .append(" select count(" + (TypeOfDB.isOracle() ? "nvl(" : "ifnull(") + "entity_id,1)) as num,status_code")
      .append(
        " from (select "
          + (TypeOfDB.isOracle() ? "decode(task.bill_id, null, task.entity_id, task.bill_id)"
            : " case when task.bill_id is null then task.entity_id else task.bill_id end ")
          + " entity_id,task.status_code ")
      .append(" from vw_sys_wf_current_item task ")
      .append(data_right_sql.getTable_clause())
      .append(" where task.node_id in (")
      .append(nodeId)
      .append(") ")
      .append(" and task.rg_code=? and task.set_year=?  ")
      .append(data_right_sql.getWhere_clause())
      .append(
        " group by task.status_code, "
          + (TypeOfDB.isOracle() ? "decode(task.bill_id, null, task.entity_id, task.bill_id)"
            : " case when task.bill_id is null then task.entity_id else task.bill_id end ") + ") as t ")
      .append(" group by status_code");

    List result_handled = null;
    StringBuffer return_sql_handled = new StringBuffer();
    return_sql_handled
      .append("select count(" + (TypeOfDB.isOracle() ? "nvl(" : "ifnull(") + "entity_id,1)) as num,status_code")
      .append(
        " from (select "
          + (TypeOfDB.isOracle() ? "decode(task.bill_id, null, task.entity_id, task.bill_id)"
            : " case when task.bill_id is null then task.entity_id else task.bill_id end ")
          + " entity_id,task.status_code ")
      .append(" from sys_wf_complete_item task ")
      .append(data_right_sql.getTable_clause())
      .append(" where task.node_id in (")
      .append(nodeId)
      .append(") ")
      .append(" and task.rg_code=? and task.set_year=?  ")
      .append(data_right_sql.getWhere_clause())
      .append(
        " group by task.status_code, "
          + (TypeOfDB.isOracle() ? "decode(task.bill_id, null, task.entity_id, task.bill_id)"
            : " case when task.bill_id is null then task.entity_id else task.bill_id end ") + ") as t ")
      .append(" group by status_code");

    if (data_right_sql.isOrg_right_flag() && data_right_sql.isData_right_flag()) {
      result = dao.findBySql(return_sql.toString(), new Object[] { rg_code, setYear, UserId, UserId, RoleId });
      result_handled = dao.findBySql(return_sql_handled.toString(), new Object[] { rg_code, setYear, UserId, UserId,
        RoleId });
    }
    if (!data_right_sql.isOrg_right_flag() && data_right_sql.isData_right_flag()) {
      result = dao.findBySql(return_sql.toString(), new Object[] { rg_code, setYear, UserId, RoleId });
      result_handled = dao.findBySql(return_sql_handled.toString(), new Object[] { rg_code, setYear, UserId, RoleId });
    }
    if (data_right_sql.isOrg_right_flag() && !data_right_sql.isData_right_flag()) {
      result = dao.findBySql(return_sql.toString(), new Object[] { rg_code, setYear, UserId });
      result_handled = dao.findBySql(return_sql_handled.toString(), new Object[] { rg_code, setYear, UserId });
    }
    if (!data_right_sql.isOrg_right_flag() && !data_right_sql.isData_right_flag()) {
      result = dao.findBySql(return_sql.toString(), new Object[] { rg_code, setYear });
      result_handled = dao.findBySql(return_sql_handled.toString(), new Object[] { rg_code, setYear });
    }

    result.addAll(result_handled);

    String status_code = "";
    StringBuffer sql4Code = new StringBuffer();
    sql4Code
      .append(
        "select ss.status_name, ss.status_code, sms.display_title from sys_status ss, sys_module sm, sys_module_status sms")
      .append(
        " where ss.status_id = sms.status_id  and sms.module_id = sm.module_id and ss.status_code = ? and sm.module_id = ?");
    List list = null;
    for (int i = 0; i < result.size(); i++) {
      FWFLogDTO log = new FWFLogDTO();
      status_code = ((Map) result.get(i)).get("status_code").toString();
      log.setStatus_code(status_code);

      String statusCode = UtilCache.getStatusCode4Name(menuId + status_code);
      if (null == statusCode || "".equals(statusCode)) {
        list = dao.findBySql(sql4Code.toString(), new Object[] { status_code, menuId });
        if (null != list && list.size() > 0) {
          XMLData tempData = (XMLData) list.get(0);
          if (tempData.get("display_title") != null && !"".equals(tempData.get("display_title"))) {
            UtilCache.putStatusCode4Name(menuId + status_code, tempData.get("display_title").toString());
          } else {
            UtilCache.putStatusCode4Name(menuId + status_code, tempData.get("status_name").toString());
          }
          statusCode = UtilCache.getStatusCode4Name(menuId + status_code);
        }
      }
      log.setStatus_name(statusCode);
      log.setNum(((Map) result.get(i)).get("num").toString());
      return_list.add(log);
    }
    return return_list;
  }

  /**
   * 
   * @param UserId
   * @param RoleId
   * @param Region
   * @return
   * @throws FAppException
   */
  private List getAllModuleTasksStatInfo(String UserId, String RoleId, String Region) throws FAppException {
    //传入的区划不为空，则取传入的区划否则从session中取当前区划 
    if (StringUtil.isNull(Region))
      throw new FAppException("待办区划为空!");

    String rg_code = Region;

    String setYear = getSetYear();
    List return_list = new ArrayList();
    //取平台参数
    String app = (String) SessionUtil.getParaMap().get("schedule_app");
    String strapp = Tools.strToSqlString(app);
    //取未启用的子系统
    FRightSqlDTO data_right_sql;
    try {
      data_right_sql = idataright.getSqlBusiRightByJoin(UserId, RoleId, "task");
    } catch (Exception e) {
      throw new FAppException(e);
    }
    //代办事项优化 加上按单按明细
    StringBuffer return_sql = new StringBuffer();

    String sys_grant_level = null;
    int sys_grant_level_num = 0;
    Map map = SessionUtil.getParaMap();
    if (map != null) {
      Object obj = map.get("sys_grant_level");
      if (obj == null) {
        obj = map.get("SYS_GRANT_LEVEL");
      }
      if (obj != null)
        sys_grant_level = obj.toString();

    }
    if (sys_grant_level != null && !sys_grant_level.equals("")) {
      //20130307李文全修改(优化待办事项)  --开始
      return_sql
        .append(
          " select count(distinct bill_id) as num,count(entity_id) as detailnum,status_code, menu_id,menu_name,menu_url ")
        .append(" from (select task.bill_id bill_id,task.entity_id entity_id,task.status_code,")
        .append(
          " module.module_id menu_id,(select menu_name from sys_menu sm where sm.menu_id=module.module_id) menu_name,(select url from sys_menu sm where sm.menu_id=module.module_id) menu_url ");
      return_sql
        .append(" from sys_wf_current_item task,sys_wf_module_node module,sys_wf_role_node role ")
        .append(data_right_sql.getTable_clause())
        .append(" where task.node_id = role.node_id and role.role_id = ? and module.node_id = task.node_id ")
        .append(
          " and task.rg_code = ? and task.set_year=? and task.rg_code=module.rg_code and task.set_year=module.set_year ")
        .append(" and task.rg_code=role.rg_code and task.set_year=role.set_year ")
        .append(data_right_sql.getWhere_clause()).append(" ) swmr  group by  status_code, menu_id,menu_name,menu_url ");
      //20130307李文全修改   ---结束
    }
    if (return_sql.length() < 1) {
      return return_list;
    }

    List result = null;

    if (sys_grant_level_num == 0) {
      if (data_right_sql.isOrg_right_flag() && data_right_sql.isData_right_flag()) {
        result = dao
          .findBySql(return_sql.toString(), new Object[] { RoleId, rg_code, setYear, UserId, UserId, RoleId });
      }
      //单位用户
      if (!data_right_sql.isOrg_right_flag() && data_right_sql.isData_right_flag()) {
        result = dao.findBySql(return_sql.toString(), new Object[] { RoleId, rg_code, setYear, UserId, RoleId });
      }
      if (data_right_sql.isOrg_right_flag() && !data_right_sql.isData_right_flag()) {
        result = dao.findBySql(return_sql.toString(), new Object[] { RoleId, rg_code, setYear, UserId });
      }
      if (!data_right_sql.isOrg_right_flag() && !data_right_sql.isData_right_flag()) {
        result = dao.findBySql(return_sql.toString(), new Object[] { RoleId, rg_code, setYear });
      }
    } else if (sys_grant_level_num > 0) {
      //优化待办事项
      if (data_right_sql.isOrg_right_flag() && data_right_sql.isData_right_flag()) {
        result = dao.findBySql(return_sql.toString(), new Object[] { RoleId, rg_code, setYear });
      }
      if (!data_right_sql.isOrg_right_flag() && data_right_sql.isData_right_flag()) {
        result = dao.findBySql(return_sql.toString(), new Object[] { RoleId, rg_code, setYear, UserId, RoleId });
      }
      if (data_right_sql.isOrg_right_flag() && !data_right_sql.isData_right_flag()) {
        result = dao.findBySql(return_sql.toString(), new Object[] { RoleId, rg_code, setYear });
      }
      if (!data_right_sql.isOrg_right_flag() && !data_right_sql.isData_right_flag()) {
        result = dao.findBySql(return_sql.toString(), new Object[] { RoleId, rg_code, setYear });
      }
    }

    String status_code = "";
    String sql4Code = "select ss.status_name, ss.status_code, sms.display_title from sys_status ss, sys_module sm, sys_module_status sms"
      + " where ss.status_id = sms.status_id"
      + " and sms.module_id = sm.module_id"
      + " and ss.status_code = ?"
      + " and sm.module_id = ?";
    List list = null;
    //记忆是否有相同的menu_id
    Map map_menu_id = new HashMap();

    for (int i = 0; i < result.size(); i++) {
      FWFLogDTO log = new FWFLogDTO();
      status_code = (String) ((Map) result.get(i)).get("status_code");
      String menu_flag = (String) ((Map) result.get(i)).get("menu_id");
      if ((null == menu_flag || "".equals(menu_flag))) {
        continue;
      }
      log.setStatus_code(status_code);
      //String statusCode = UtilCache.getStatusCode4Name(module_id + status_code);
      //      if (null == statusCode || "".equals(statusCode)) {
      //        list = dao.findBySql(sql4Code, new Object[] { status_code, module_id });
      //
      //        // 因为sys_wf_current_item表中没有001|004状态，如果菜单上配置了001|004，会造成待办事项的状态显示名为null
      //        if ((list == null || list.size() < 1) && "001|004".indexOf(status_code) != -1) {
      //          list = dao.findBySql(sql4Code, new Object[] { "001|004", module_id });
      //        }
      //
      //        if (null != list && list.size() > 0) {
      //          XMLData tempData = (XMLData) list.get(0);
      //          if (tempData.get("display_title") != null && !"".equals(tempData.get("display_title"))) {
      //            UtilCache.putStatusCode4Name(module_id + status_code, tempData.get("display_title").toString());
      //          } else {
      //            UtilCache.putStatusCode4Name(module_id + status_code, tempData.get("status_name").toString());
      //          }
      //          statusCode = UtilCache.getStatusCode4Name(module_id + status_code);
      //        } else {
      //          //菜单没有状态时,待办事项状态为null
      //          statusCode = "未处理";
      //        }
      //      }
      status_code = status_code.equals("001") ? "未处理" : "己处理";
      log.setStatus_name(status_code);
      log.setNum(((Map) result.get(i)).get("num").toString());
      //增加明细数
      log.setDetailNum(((Map) result.get(i)).get("detailnum").toString());
      //      log.setModule_id(((Map) result.get(i)).get("module_id").toString());
      //如果是一菜单对多菜单 则显示菜单名字
      String menu_id = (String) ((Map) result.get(i)).get("menu_id");
      log.setMenu_id(menu_id);
      String menu_name = (String) ((Map) result.get(i)).get("menu_name");
      log.setMenu_name(menu_name);
      String menu_url = (String) ((Map) result.get(i)).get("menu_url");
      log.setURL(menu_url);
      return_list.add(log);
    }

    return return_list;
  }

  /**
   * 查询某菜单某状态的事务提醒。
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param menuid-----------菜单ID
   * @return List(内部含FWFLogDTO)
   * @throws Exception---------错误信息
   */
  public List getModuleAllTasksStatInfo(String UserId, String RoleId, String menuid) throws FAppException {
    // return getModuleTasksStatInfo(UserId, RoleId, menuid, null);
    /*
     * add daiwei 20070604 start 用开关控制事务提醒，如果不用事务提醒的话只需要在修改库中的参数即可
     * 0代表关（即不用事务提醒）
     */
    String switch_module_task = (String) SessionUtil.getParaMap().get("switch_module_task");
    List list = new ArrayList();
    if ("0".equals(switch_module_task)) {
      return list;
    } else {
      return getModuleTasksStatInfo(UserId, RoleId, menuid, null);
    }
  }

  /**
   * 全局的事务提醒。
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @return List(内部含FWFLogDTO)
   * @throws Exception---------错误信息
   */
  public List getAllTasksStatInfo(String UserId, String RoleId) throws FAppException {
    // return this.getAllModuleTasksStatInfo(UserId, RoleId);
    /*
     * add daiwei 20070604 start 用开关控制事务提醒，如果不用事务提醒的话只需要在修改库中的参数即可
     * 0代表关（即不用事务提醒）
     */
    String switch_all_task = (String) SessionUtil.getParaMap().get("switch_all_task");
    List list = new ArrayList();
    if ("0".equals(switch_all_task)) {
      return list;
    } else {
      return this.getAllModuleTasksStatInfo(UserId, RoleId, "");
    }
  }

  /**
   * 全局的事务提醒。
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @return List(内部含FWFLogDTO)
   * @throws Exception---------错误信息
   */
  public List getAllTasksStatInfo(String UserId, String RoleId, String Region) throws FAppException {
    String switch_all_task = (String) SessionUtil.getParaMap().get("switch_all_task");
    List list = new ArrayList();
    if ("0".equals(switch_all_task)) {
      return list;
    } else {
      return this.getAllModuleTasksStatInfo(UserId, RoleId, Region);
    }
  }

  /**
   * 通用处理操作（对批量数据）。
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param bill_table_name----单表名
   * @param detail_table_name--明细表名
   * @param inputFVoucherDtos--业务数据DTO列表
   * @param auto_tolly_flag----是否通过工作流自动记账
   * @param auto_create_ccid---是否通过工作流生成CCID
   * @param auto_create_rcid---是否通过工作流生成RCID
   * 
   * @return List（内部含Object)
   * @throws Exception---------错误信息
   */
  public List doBatchAllProcessReturnObj(String menuid, String roleid, String actiontype, String operationname,
    String operationdate, String operationremark, String bill_table_name, String detail_table_name,
    List inputFVoucherDtos, boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid)
    throws FAppException {
    List result;
    // long begin = Calendar.getInstance().getTimeInMillis();
    try {
      result = doBatchAllProcessReturnObj(menuid, roleid, actiontype, operationname, operationdate, operationremark,
        bill_table_name, detail_table_name, inputFVoucherDtos, auto_tolly_flag, auto_create_ccid, auto_create_rcid,
        false);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
    return result;
  }

  private List doBatchAllProcessReturnObj(String menuid, String roleid, String actiontype, String operationname,
    String operationdate, String operationremark, String bill_table_name, String detail_table_name,
    List inputFVoucherDtos, boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid,
    boolean isForced2Execute) throws FAppException {
    try {
      if (actiontype.equals("RECALL")) {
        return undoBatchProcessReturnObj(menuid, roleid, bill_table_name, detail_table_name, operationname,
          operationdate, operationremark, inputFVoucherDtos, auto_tolly_flag);
      } else {
        return doBatchProcess(null, menuid, roleid, actiontype, operationname, operationdate, operationremark,
          bill_table_name, detail_table_name, inputFVoucherDtos, auto_tolly_flag, auto_create_ccid, auto_create_rcid,
          isForced2Execute);
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 得到传入数据是否结束。 如果存在多个分支，则所有分支都结束时，才返回true，否则返回false
   * 
   * @param tablename----------表名称
   * @param entityid-----------数据ID
   * @return List对象（内部含FWFLogDTO)
   * @throws Exception---------错误信息
   */
  public boolean isEnd(String TableName, String EntityId) throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    // 得到下一节点的状态信息－－未确认，已退回
    StringBuffer select_sql = new StringBuffer();
    // modified by bigdog
    // 所有的分支都结束时，才返回true
    select_sql
      .append("select 1 from sys_wf_current_tasks t ")
      .append("where t.entity_id = ? and upper(t.wf_table_name)=? ")
      .append(
        " and not exists(select 1 from  sys_wf_nodes s where s.node_id=t.next_node_id and s.node_type='003' and s.rg_code=t.rg_code and s.set_year=t.set_year) ")
      .append("and t.ACTION_TYPE_CODE in ('NEXT', 'BACK', 'INPUT', 'EDIT', 'HANG') and t.rg_code=? and t.set_year=? ");

    List list = dao.findBySql(select_sql.toString(),
      new Object[] { EntityId, TableName.toUpperCase(), rg_code, setYear });
    // 如果查不到下一个节点是结束节点的情况，则表示流程还没结束
    if (list == null || list.size() == 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 
   */
  public XMLData doBatchAllProcessCrossTableWithNoTableName(String menuid, String roleid, String actiontype,
    String operationname, String operationdate, String operationremark, List oldFVoucherDtos, List newFVoucherDtos,
    boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid) throws FAppException {
    try {
      return doBatchAllProcessCrossTableWithNoTableName(menuid, roleid, actiontype, operationname, operationdate,
        operationremark, oldFVoucherDtos, newFVoucherDtos, auto_tolly_flag, auto_create_ccid, auto_create_rcid, false);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }

  }

  private XMLData doBatchAllProcessCrossTableWithNoTableName(String menuid, String roleid, String actiontype,
    String operationname, String operationdate, String operationremark, List oldFVoucherDtos, List newFVoucherDtos,
    boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid, boolean isForced2Execute)
    throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    try {
      if (actiontype.equals("RECALL") || actiontype.equals("NEXT") || actiontype.equals("BACK")) {

      } else {
        throw new Exception("此方法只支持RECALL、NEXT、BACK动作");
      }
      XMLData return_data = new XMLData();

      if (actiontype.equals("RECALL")) {
        return_data.put("oldDto", this.undoBatchProcessReturnObjWithNoTableName(menuid, roleid, operationname,
          operationdate, operationremark, oldFVoucherDtos, auto_tolly_flag));
        return_data.put("newDto", this.undoBatchProcessReturnObjWithNoTableName(menuid, roleid, operationname,
          operationdate, operationremark, newFVoucherDtos, auto_tolly_flag));
      } else {
        //
        return_data.put("oldDto", this.doBatchProcessWithNoTableName(null, menuid, roleid, actiontype, operationname,
          operationdate, operationremark, oldFVoucherDtos, auto_tolly_flag, auto_create_ccid, auto_create_rcid, null,
          isForced2Execute));

        List control_wf_info = new ArrayList();

        String routing_type = "";
        String entityId = "";
        XMLData rowData = new XMLData();
        if (((FVoucherDTO) oldFVoucherDtos.get(0)).getDetails() == null
          || ((FVoucherDTO) oldFVoucherDtos.get(0)).getDetails().size() == 0) {
          FVoucherDTO dto = (FVoucherDTO) oldFVoucherDtos.get(0);
          rowData = dto.toXMLData();

        } else {
          FVoucherDTO dto = (FVoucherDTO) (((FVoucherDTO) oldFVoucherDtos.get(0)).getDetails().get(0));
          rowData = dto.toXMLData();
        }
        if (actiontype.equals("BACK")) {
          routing_type = "002";
        } else {
          // 前进操作时
          routing_type = "001";
        }

        StringBuffer select_sql = new StringBuffer();
        List rs = null;
        if (!isForced2Execute) {
          select_sql
            .append("select distinct t.wf_id,t.node_id,t.next_node_id,t.condition_id  from sys_wf_current_tasks")
            .append(Tools.addDbLink())
            .append(" b")
            .append(" ,vw_sys_wf_node_relation")
            .append(Tools.addDbLink())
            .append(" d,sys_wf_node_conditions")
            .append(Tools.addDbLink())
            .append(" t where d.module_id=? and d.role_id=?")
            .append(" and ((b.next_node_id=d.node_id  and b.action_type_code in ('INPUT','NEXT','BACK'))")
            .append(" or (b.current_node_id=d.node_id and b.action_type_code in ('DISCARD','DELETE','HANG','EDIT')))")
            .append(
              " and b.entity_id = ? and t.wf_id=b.wf_id and t.routing_type=? and t.node_id=b.next_node_id and b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=t.rg_code and b.set_year=t.set_year and b.rg_code=? and b.set_year=?");

          rs = dao.findBySql(select_sql.toString(), new Object[] { menuid, roleid, entityId, routing_type, rg_code,
            setYear });
        } else {
          select_sql
            .append("select distinct t.wf_id,t.node_id,t.next_node_id,t.condition_id  from sys_wf_current_tasks")
            .append(Tools.addDbLink())
            .append(" b")
            .append(" ,vw_sys_wf_node_relation")
            .append(Tools.addDbLink())
            .append(" d,sys_wf_node_conditions")
            .append(Tools.addDbLink())
            .append(" t ")
            .append(" where ((b.next_node_id=d.node_id  and b.action_type_code in ('INPUT','NEXT','BACK'))")
            .append(" or (b.current_node_id=d.node_id and b.action_type_code in ('DISCARD','DELETE','HANG','EDIT')))")
            .append(
              " and b.entity_id = ? and t.wf_id=b.wf_id and t.routing_type=? and t.node_id=b.next_node_id and b.rg_code=d.rg_code and b.set_year=d.set_year and b.rg_code=t.rg_code and b.set_year=t.set_year and b.rg_code=? and b.set_year=?");

          rs = dao.findBySql(select_sql.toString(), new Object[] { entityId, routing_type, rg_code, setYear });
        }

        if (rs.size() == 0) {
          throw new Exception("不能走入下一流程节点!");
        } else {
          // 将菜单、角色作为过滤条件
          rowData.put("module_id", menuid);
          rowData.put("role_id", roleid);
          //
          Iterator it = rs.iterator();
          while (it.hasNext()) {
            Map map = (Map) it.next();
            String wf_id = (String) map.get("wf_id");
            String current_node_id = (String) map.get("node_id");
            boolean can_go_next = false;
            if (map.get("condition_id").equals("#")) {
              can_go_next = true;
            } else if (getValidWfNode((String) map.get("condition_id"), rowData)) {
              can_go_next = true;
            }
            if (can_go_next) {
              XMLData return_xmldata = new XMLData();
              return_xmldata.put("wf_id", wf_id);
              return_xmldata.put("current_node_id", current_node_id);
              return_xmldata.put("next_node_id", map.get("next_node_id").toString());
              control_wf_info.add(return_xmldata);
            }
          }
        }

        return_data.put("newDto", this.doBatchProcessWithNoTableName(null, menuid, roleid, actiontype, operationname,
          operationdate, operationremark, newFVoucherDtos, auto_tolly_flag, auto_create_ccid, auto_create_rcid,
          control_wf_info, isForced2Execute));

      }
      return null;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 得到所有结束节点对应的菜单ID
   * 
   * @param
   * @return List对象（内部含XMLData)
   * @throws Exception---------错误信息
   */
  public List getIsEndNode() throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    try {
      List return_list = dao
        .findBySql("select c.module_id from sys_wf_nodes"
          + Tools.addDbLink()
          + " a,sys_wf_node_conditions"
          + Tools.addDbLink()
          + " b,sys_wf_module_node"
          + Tools.addDbLink()
          + " c where a.node_type='003'"
          + "and b.routing_type='001' and b.next_node_id=a.node_id and b.node_id=c.node_id and a.rg_code=b.rg_code and a.set_year=b.set_year and a.rg_code=c.rg_code and a.set_year=c.set_year and a.rg_code='"
          + rg_code + "' and a.set_year=" + setYear);
      return return_list;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 通用提取操作（对批量数据,不用传TABLE_NAME）。
   * 
   * @param old_node_id--------被提取的数据当前所处的节点号
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param inputFVoucherDtos--业务数据DTO列表
   * @param auto_tolly_flag----是否通过工作流自动记账
   * @param auto_create_ccid---是否通过工作流生成CCID
   * @param auto_create_rcid---是否通过工作流生成RCID
   * @return List（内部含Object)
   * @throws Exception---------错误信息
   */
  public List doBatchDistillWithNoTableName(String old_node_id, String menuid, String roleid, String operationname,
    String operationdate, String operationremark, List inputFVoucherDtos, boolean auto_tolly_flag,
    boolean auto_create_ccid, boolean auto_create_rcid) throws FAppException {
    try {
      return doBatchDistillWithNoTableName(old_node_id, menuid, roleid, operationname, operationdate, operationremark,
        inputFVoucherDtos, auto_tolly_flag, auto_create_ccid, auto_create_rcid, false);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }

  }

  private List doBatchDistillWithNoTableName(String old_current_node_id, String menuid, String roleid,
    String operationname, String operationdate, String operationremark, List inputFVoucherDtos,
    boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid, boolean isForced2Execute)
    throws FAppException {
    return doBatchProcessWithNoTableName(old_current_node_id, menuid, roleid, "DISTILL", operationname, operationdate,
      operationremark, inputFVoucherDtos, auto_tolly_flag, auto_create_ccid, auto_create_rcid, null, isForced2Execute);
  }

  /**
   * 通用跨节点操作（对批量数据,不用传TABLE_NAME）。
   * 
   * @param old_node_id--------被提取的数据当前所处的节点号
   * @param actiontype---------操作类型CODE
   * @param operationname------操作名称
   * @param operationdate------操作时间
   * @param operationremark----操作意见
   * @param inputFVoucherDtos--业务数据DTO列表
   * @param auto_tolly_flag----是否通过工作流自动记账
   * @param auto_create_ccid---是否通过工作流生成CCID
   * @param auto_create_rcid---是否通过工作流生成RCID
   * @return List（内部含Object)
   * @throws Exception---------错误信息
   */
  public List doBatchStrideProcessWithNoTableName(String old_node_id, String actiontype, String operationname,
    String operationdate, String operationremark, List inputFVoucherDtos, boolean auto_tolly_flag,
    boolean auto_create_ccid, boolean auto_create_rcid) throws FAppException {
    try {

      return doBatchStrideProcessWithNoTableName(old_node_id, actiontype, operationname, operationdate,
        operationremark, inputFVoucherDtos, auto_tolly_flag, auto_create_ccid, auto_create_rcid, false);
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  private List doBatchStrideProcessWithNoTableName(String old_current_node_id, String actiontype, String operationname,
    String operationdate, String operationremark, List inputFVoucherDtos, boolean auto_tolly_flag,
    boolean auto_create_ccid, boolean auto_create_rcid, boolean isForced2Execute) throws FAppException {
    try {
      if (!actiontype.equals("STRIDE_DISCARD") && !actiontype.equals("STRIDE_HANG")
        && !actiontype.equals("STRIDE_DELETE")) {
        throw new Exception("本方法只支持如下动作STRIDE_DISCARD、STRIDE_HANG、STRIDE_DELETE");
      } else {
        return doBatchProcessWithNoTableName(old_current_node_id, null, null, actiontype.replaceAll("STRIDE_", ""),
          operationname, operationdate, operationremark, inputFVoucherDtos, auto_tolly_flag, auto_create_ccid,
          auto_create_rcid, null, isForced2Execute);
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }

  }

  /**
   * 得到传入数据所处的节点状态下的某条数据最近第二条数据的DTO
   * 
   * @param RoleId-------------角色ID
   * @param menuID-----------菜单ID
   * @param OperateType--------操作类型
   * @param entityid-----------数据ID
   * @return Object
   * @throws Exception---------错误信息
   */
  public synchronized Object obtainOldDTO(String RoleId, String menuId, String OperateType, String EntityId)
    throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer select_sql = new StringBuffer();
    String task_id = "";
    // 未送审状态
    try {
      if (OperateType.equals("001")) {
        select_sql.append("select  distinct d.task_id   from sys_wf_nodes").append(Tools.addDbLink())
          .append("  c, sys_wf_complete_tasks").append(Tools.addDbLink()).append(" d, sys_wf_role_node")
          .append(Tools.addDbLink()).append(" e,sys_wf_module_node").append(Tools.addDbLink()).append(" f")
          .append(" where  c.gather_flag = 1 ").append(" and c.node_id=e.node_id ")
          .append(" and c.node_id =  f.node_id and d.entity_id=? and e.role_id=? and f.module_id=? ")
          .append(" and ((c.node_id = d.next_node_id and d.action_type_code in ('INPUT','NEXT')) or")
          .append(" (c.node_id = d.current_node_id and d.action_type_code  ='EDIT')) ")
          .append(" and c.rg_code=d.rg_code and c.rg_code=e.rg_code and c.rg_code=f.rg_code")
          .append(" and c.set_year=d.set_year and c.set_year=e.set_year and c.set_year=f.set_year")
          .append(" and c.rg_code=? and c.set_year=?").append(" order by d.task_id desc");

      }

      List list = dao.findBySql(select_sql.toString(), new Object[] { EntityId, RoleId, menuId, rg_code, setYear });
      if (list == null || list.size() == 0) {
        throw new Exception("查询不到数据！");
      } else {
        task_id = ((Map) list.get(0)).get("task_id").toString();
        return obtainDTOByTaskId(task_id);
      }
    } catch (FAppException e) {
      throw e;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 根据task_id得到DTO
   * 
   * @param task_id------------任务ID
   * @return Object
   * @throws Exception---------错误信息
   */
  public synchronized Object obtainDTOByTaskId(String task_id) throws FAppException {
    Session tmp_session = null;
    try {
      Object return_dto = new Object();
      // 取出上一DTO
      StringBuffer insert_sql = new StringBuffer();
      insert_sql.append("insert into SYS_WF_TASK_DTO_CACHE(task_id,dto) ")
        .append(" select b.task_id,b.dto from sys_wf_task_dto").append(Tools.addDbLink())
        .append(" b where b.task_id =").append(task_id);
      String select_sql = "select * from SYS_WF_TASK_DTO_CACHE  where task_id =" + task_id;
      tmp_session = dao.getSession();
      Connection conn = tmp_session.connection();
      PreparedStatement pstmt = conn.prepareStatement(insert_sql.toString());
      pstmt.executeUpdate();
      pstmt.close();

      ResultSet select_rs = conn.createStatement().executeQuery(select_sql);
      Blob blob = null;

      if (select_rs.next()) {
        blob = (Blob) select_rs.getBlob("dto");
        if (null != blob) {
          int iLength = (int) blob.length();
          byte[] result = new byte[iLength];
          InputStream input = blob.getBinaryStream();
          input.read(result);
          ByteArrayInputStream Input = new ByteArrayInputStream(result);
          ObjectInputStream q = new ObjectInputStream(Input);
          return_dto = q.readObject();
        }
      } else {
        throw new Exception("查询数据错误!");
      }
      // tmp_session.close();

      // 临时表的事务处理有问题，换成固定表，用完再删
      dao.executeBySql("delete from SYS_WF_TASK_DTO_CACHE");
      return return_dto;
    } catch (Exception e) {
      throw new FAppException(e);
    } finally {
      if (tmp_session != null) {
        dao.closeSession(tmp_session);
      }

    }
  }

  /**
   * 查询各子系统事务提醒。需要各子系统实现
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @return List(内部含FTaskDTO)
   * @throws Exception---------错误信息
   */
  public List findTasks(String UserId, String RoleId, String Region, String set_year) throws FAppException {
    List return_list = new ArrayList();

    try {
      String role_name = null;
      String sql = "select role_name from sys_role where role_id=?";
      List list = dao.findBySql(sql, new Object[] { RoleId });
      if (list != null && list.size() > 0) {
        Object obj = list.get(0);
        if (obj instanceof XMLData) {
          XMLData data = (XMLData) obj;
          Object role_name_obj = data.get("role_name");
          if (role_name_obj != null) {
            role_name = role_name_obj.toString();
          }
        }
      }
      // 从工作流中取出事务提醒
      List workflow_list = this.getAllTasksStatInfo(UserId, RoleId, Region);

      for (int i = 0; workflow_list != null && i < workflow_list.size(); i++) {
        FWFLogDTO log = (FWFLogDTO) workflow_list.get(i);

        boolean is_have = false;
        for (int j = 0; j < return_list.size(); j++) {
          FTaskItemDTO tmp_task = (FTaskItemDTO) return_list.get(j);
          if (log.getModule_id().equals(tmp_task.getModule_id()) && log.getMenu_id().equals(tmp_task.getMenu_id())) {
            is_have = true;
            tmp_task.setTask_content(tmp_task.getTask_content() + log.getStatus_name() + "：按单" + log.getNum() + "条,按明细"
              + log.getDetailNum() + "条； ");
            break;
          }
        }
        if (!is_have) {
          FTaskItemDTO task = new FTaskItemDTO();
          task.setRole_id(RoleId);
          task.setModule_id(log.getModule_id());
          task.setMenu_id(log.getMenu_id());
          task.setMenu_name(log.getMenu_name());
          task.setSysapp(log.getSys_id());
          task.setMsg_type_code("1");
          task.setMsg_type_name("日常事务");
          task.setMsg_type_name_local(log.getModule_name());
          task.setMenu_url(log.getURL());
          //李文全增加按明细
          if (log.getNum() != null && log.getNum().equals("0")) {
            task.setTask_content(log.getStatus_name() + " ：" + log.getDetailNum() + " 条； ");
          } else {
            task.setTask_content(log.getStatus_name() + " ：" + log.getNum() + " 条； ");
          }
          //增加操作时间和角色
          task.setOperation_date(log.getOperation_date());

          if (role_name != null) {
            task.setRole_name(role_name);
          }
          return_list.add(task);
        }

      }

      return return_list;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 根据节点找到流程
   * 
   * @param nodeId
   * @return
   * @throws Exception
   */
  public String getWfIdByNodeId(String nodeId) throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    if (nodeId == null)
      return null;

    String sql = "select wf_id from sys_wf_nodes" + Tools.addDbLink()
      + " where node_id = ? and rg_code=? and set_year=?";
    List wfs = dao.findBySql(sql, new Object[] { nodeId, rg_code, setYear });
    return (String) ((XMLData) wfs.get(0)).get("wf_id");
  }

  /**
   * 自动发送消息
   * 
   * @throws Exception
   */
  public void autoSendMsg() throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer sql = new StringBuffer();
    sql
      .append("select a.*, b.node_name, b.send_msg_flag, b.send_msg_time, b.send_msg_time_unit, c.wf_name ")
      .append(" from vw_sys_wf_stride_001_and_004 a, sys_wf_nodes b, sys_wf_flows c ")
      .append(" where a.node_id = b.node_id ")
      .append(" and b.wf_id = c.wf_id ")
      .append(" and b.send_msg_flag = 1 ")
      .append(
        " and (b.send_msg_time = 0 or "
          + (TypeOfDB.isOracle() ? " sysdate > to_date(a.send_msg_date, 'yyyy-mm-dd hh24:mi:ss') "
            : " sysdate() > STR_TO_DATE(a.send_msg_date, '%Y-%m-%d %H:%i:%s') ") + ") ")
      .append(" and a.rg_code=b.rg_code and a.rg_code=c.rg_code and a.set_year=b.set_year and a.set_year=c.set_year ")
      .append(" and a.rg_code=? and a.set_year=?");
    List list = dao.findBySql(sql.toString(), new Object[] { rg_code, setYear });
    //
    try {

      for (int i = 0; list != null && i < list.size(); i++) {
        XMLData data = (XMLData) list.get(i);
        String taskId = (String) data.get("task_id");
        FVoucherDTO taskDto = (FVoucherDTO) this.getTaskDtoByTaskId(taskId);
        String nodeId = (String) data.get("node_id");
        //
        String timeUnitName = "天";
        int timeUnit = Integer.parseInt((String) data.get("send_msg_time_unit"));
        switch (timeUnit) {
        case 0:
          timeUnitName = "天";
          break;
        case 1:
          timeUnitName = "小时";
          break;
        case 2:
          timeUnitName = "分钟";
          break;
        }
        //
        List roleNodes = this.getWfRoleNodesByNodeId(nodeId);
        for (int j = 0; roleNodes != null && j < roleNodes.size(); j++) {
          XMLData rn = (XMLData) roleNodes.get(j);
          FMessageDTO msgDto = new FMessageDTO();
          msgDto.setRole_id((String) rn.get("role_id"));
          msgDto.setMsg_content("预算单位为:" + taskDto.getAgency_name() + " 支出科目为:" + taskDto.getExpfunc_name() + " 金额为:"
            + taskDto.getVou_money() + "的数据" + " 在" + data.get("wf_name") + "流程 " + data.get("node_name") + "节点,"
            + " 已停留超过" + data.get("send_msg_time") + timeUnitName + ",请尽快处理！");

          msgService.sendMessage(msgDto);
        }
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 根据taskId查找sys_wf_task_dto
   * 
   * @param taskId
   * @return
   * @throws Exception
   */
  private synchronized Object getTaskDtoByTaskId(String taskId) throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    // Session tmp_session = null;
    try {
      Object taskDto = new Object();
      String sql = "select * from sys_wf_current_tasks where task_id ='" + taskId + "' and rg_code=? and set_year=?";
      List list = this.dao.findBySql(sql, new Object[] { rg_code, setYear });
      taskDto = list.get(0);
      return taskDto;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 查找指定节点的角色信息
   * 
   * @param nodeId
   * @return
   * @throws Exception
   */
  public List getWfRoleNodesByNodeId(String nodeId) throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    String sql = "select * from sys_wf_role_node" + Tools.addDbLink()
      + " where node_id = ? and rg_code=? and set_year=? ";
    return dao.findBySql(sql, new Object[] { nodeId, rg_code, setYear });
  }

  /** 工作流监控管理 */

  /**
   * 获得符合条件的数据
   * 
   * @param sql
   * @param countSql
   * @return
   */
  public XMLData findDatas(String sql, String countSql) throws FAppException {
    XMLData data = new XMLData();
    try {
      Integer totalCount = new Integer(0);
      // 查询并返回数据
      List rows = dao.findBySql(sql);
      // 获得总数据条数
      if (countSql != null && !countSql.equals("")) {
        List count = dao.findBySql(countSql);
        totalCount = new Integer((String) ((XMLData) count.get(0)).get("count(*)"));
      }
      data.put("total_count", totalCount);
      data.put("row", rows);
    } catch (Exception e) {
      throw new FAppException(e);
    }
    return data;
  }

  /**
   * 得到所有流程数据
   * 
   * @return List
   */
  public List getAllWorkFlows() {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    String sql = "select * from sys_wf_flows" + Tools.addDbLink() + " where rg_code=? and set_year=? order by wf_code";
    List list = dao.findBySql(sql, new Object[] { rg_code, setYear });
    return list;
  }

  /**
   * 根据流程ID获得节点
   * 
   * @param wfId
   * @return
   */
  public List getSysWfNodesByWfId(String wfId) {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    String sql = "select * from sys_wf_nodes" + Tools.addDbLink() + " where wf_id = '" + wfId
      + "' and rg_code=? and set_year=? order by node_code";
    List list = dao.findBySql(sql, new Object[] { rg_code, setYear });
    return list;
  }

  /**
   * 获得所有状态
   * 
   * @return
   */
  public List getAllSysStatus() {
    String sql = "select * from sys_status" + Tools.addDbLink() + " order by status_code";
    List list = dao.findBySql(sql);
    return list;
  }

  /**
   * 带有权限的任务查询子句。不用传表名和主键 (内部处理对于主单明细表模式进行了处理,避免因为SQL语句不合理造成执行效率低的问题)
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param menuID-----------菜单ID
   * @param OperateType--------操作类型
   * @param TableAlias---------主表的别名
   * @return SQL语句
   * @throws Exception---------错误信息
   */
  public String getTaskSqlClause(String UserId, String RoleId, String menuId, String OperateType, String TableAlias)
    throws FAppException {
    String c = OperateType;
    String b = "";
    StringBuffer return_sql = new StringBuffer();
    try {
      String data_right_sql = idataright.getSqlBusiRight(UserId, RoleId, "task");
      boolean is1stExistClauseFlag = true;// 标识是否是第一个"exist(.....)" 语句.
      // 如果是,则前面不加"or".
      // 循环调用多种状态的数据
      while (c.length() > 0) {
        if (c.indexOf("|") >= 0) {
          b = c.substring(0, c.indexOf("|"));
          c = c.substring(c.indexOf("|") + 1, c.length());
        } else {
          b = c;
          if (b.trim().length() > 0) {
            return_sql.append(getSingleTypeSqlClause(data_right_sql, RoleId, menuId, b.trim(), TableAlias,
              is1stExistClauseFlag));
          }
          break;
        }
        if (b.trim().length() > 0) {
          return_sql.append(getSingleTypeSqlClause(data_right_sql, RoleId, menuId, b.trim(), TableAlias,
            is1stExistClauseFlag));
        }
        is1stExistClauseFlag = false;
      }

      // 屏蔽 " and () " 情况
      if (return_sql.toString().trim().equals("")) {
        return " and 1 = 0 ";
      }

      return " and (" + return_sql.toString() + ")";
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 对于单一类型的处理,内部优化主单明细单的模式(所有模式都从明细表进行数据过滤)
   * 
   * @param data_right_sql
   * @param RoleId
   * @param menuId
   * @param OperateType
   * @param TableAlias
   * @param is1stExistClauseFlag
   * @return
   */
  public String getSingleTypeSqlClause(String data_right_sql, String RoleId, String menuId, String OperateType,
    String TableAlias, boolean is1stExistClauseFlag) {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer return_sql = new StringBuffer();

    StringBuffer sql = new StringBuffer();
    sql
      .append("select distinct 'a' as wf_table_name, c.id_column_name,")
      .append(" c.outer_table_name,c.outer_column_name,c.relation_column_name,c.gather_flag ")
      .append(" from sys_wf_module_node")
      .append(Tools.addDbLink())
      .append(" a,sys_wf_role_node")
      .append(Tools.addDbLink())
      .append(" b ,sys_wf_nodes")
      .append(Tools.addDbLink())
      .append(" c")
      .append(" where a.node_id=b.node_id and b.node_id=c.node_id  ")
      .append(" and b.role_id=? and a.module_id=? and c.outer_table_name is null ")
      .append(" union ")
      .append(" select distinct c.wf_table_name,c.id_column_name,")
      .append(" c.outer_table_name,c.outer_column_name,c.relation_column_name,c.gather_flag ")
      .append(" from sys_wf_module_node")
      .append(Tools.addDbLink())
      .append(" a,sys_wf_role_node")
      .append(Tools.addDbLink())
      .append(" b ,sys_wf_nodes")
      .append(Tools.addDbLink())
      .append(" c")
      .append(" where a.node_id=b.node_id and b.node_id=c.node_id  ")
      .append(" and b.role_id=? and a.module_id=? and c.outer_table_name is not null ")
      .append(
        " and a.rg_code=b.rg_code and a.rg_code=c.rg_code and a.set_year=b.set_year and a.set_year=c.set_year and a.rg_code=? and a.set_year=?");
    List result = new ArrayList();

    result = dao.findBySql(sql.toString(), new Object[] { RoleId, menuId, RoleId, menuId, rg_code, setYear });

    for (int i = 0; i < result.size(); i++) {
      String tmp_inner_table_id = (String) ((Map) result.get(i)).get("id_column_name");
      /**
       * add start by bing-zeng 2008-02-26 修改工作流事务提醒
       */
      if (OperateType.equals("001") || OperateType.equals("004")) {
        return_sql
          .append(i == 0 && is1stExistClauseFlag ? "" : "or")
          .append(
            " exists (select 1 from SYS_WF_CURRENT_ITEM task, sys_wf_module_node module, sys_wf_role_node role where ")
          .append(" task.status_code= '" + OperateType + "'")
          .append(" and task.node_id = module.node_id and task.node_id = role.node_id ")
          .append(" and module.module_id = '")
          .append(menuId)
          .append("' and role.role_id = '")
          .append(RoleId)
          .append("' ")
          .append(data_right_sql)
          .append(" and  task.entity_id=")
          .append(TableAlias)
          .append(".")
          .append(tmp_inner_table_id)
          .append(
            " and task.rg_code=module.rg_code and task.rg_code=role.rg_code and task.set_year=module.set_year and task.set_year=role.set_year and task.rg_code='"
              + rg_code + "' and task.set_year=" + setYear + ")");
      } else {
        return_sql
          .append(i == 0 && is1stExistClauseFlag ? "" : "or")
          .append(
            " exists (select 1 from " + ("008".equals(OperateType) ? "VW_SYS_WF_STRIDE_008" : "SYS_WF_COMPLETE_ITEM")
              + " task, sys_wf_module_node module, sys_wf_role_node role where ")
          .append(" task.status_code= '" + OperateType + "'")
          .append(" and task.node_id = module.node_id and task.node_id = role.node_id ")
          .append(" and module.module_id = '")
          .append(menuId)
          .append("' and role.role_id = '")
          .append(RoleId)
          .append("' ")
          .append(data_right_sql)
          .append(" and  task.entity_id=")
          .append(TableAlias)
          .append(".")
          .append(tmp_inner_table_id)
          .append(
            " and task.rg_code=module.rg_code and task.rg_code=role.rg_code and task.set_year=module.set_year and task.set_year=role.set_year and task.rg_code='"
              + rg_code + "' and task.set_year=" + setYear + ")");
      }
    }
    return return_sql.toString();

  }

  /*
   * add daiwei 20070407 start
   */

  /**
   * 查询某一业务所有结束的数据
   * 
   * @param wf_id
   *            --------------工作流id
   * @param TableAlias
   *            -----------主表的别名
   */
  public String getWFEndData(String wf_id, String TableAlias) {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    // 通过wf_id取得主键id字段的名称
    String sql = "select c.id_column_name from sys_wf_flows c where c.wf_id = ? and c.rg_code=? and c.set_year=?";
    List list = dao.findBySql(sql, new Object[] { wf_id, rg_code, setYear });
    if (list == null || list.size() == 0) {
      return "";
    }
    String idColumnName = list.get(0).toString();
    // 拼写返回的sql
    StringBuffer return_sql = new StringBuffer();
    return_sql
      .append(" and exists ( select 1 from vw_sys_wf_stride_001_and_004 a , sys_wf_nodes b")
      .append(" where a.node_id = b.node_id and b.node_type = '003' and")
      .append(" b.wf_id = '")
      .append(wf_id)
      .append("' and ")
      .append(TableAlias)
      .append(".")
      .append(idColumnName)
      .append(
        " = a.entity_id and a.rg_code=b.rg_code and a.set_year=b.set_year and a.rg_code='" + rg_code
          + "' and a.set_year=" + setYear);
    return return_sql.toString();
  }

  /*
   * add daiwei 20070407 end
   */

  public FWFSqlDTO getTasksWithRightBySqlByJoin(String UserId, String RoleId, String menuId, String OperateType,
    String TableAlias) throws Exception {

    String rg_code = getRgCode();
    String setYear = getSetYear();
    // 返回业务表的数据权限sql语句，并返回带?

    FRightSqlDTO data_right_sql = idataright.getSqlBusiRightByJoinWithQry(UserId, RoleId, "task");

    FWFSqlDTO return_sql = new FWFSqlDTO();

    StringBuffer table_sql = new StringBuffer();
    StringBuffer where_sql = new StringBuffer();

    /**
     * add by bing-zeng 2008-02-25 事物提醒新SQL start
     */
    if (OperateType.equals("001") || OperateType.equals("004")) {
      table_sql.append(" sys_wf_current_item task,sys_wf_module_node task1,sys_wf_role_node task2");
    } else if (!OperateType.equals("999") && OperateType.length() < 4 && !OperateType.equals("008")) {

      table_sql.append(" sys_wf_complete_item task,sys_wf_module_node task1,sys_wf_role_node task2");
    } else if (OperateType.equals("008")) {
      table_sql.append(" vw_sys_wf_complete_tasks task,sys_wf_module_node task1,sys_wf_role_node task2");
    } else {
      table_sql.append(" sys_wf_current_item task,sys_wf_module_node task1,sys_wf_role_node task2");
    }
    //李文全修改2013-06-14
    String tmp_sql = "select c.id_column_name from sys_wf_module_node a,sys_wf_role_node b,sys_wf_nodes c where a.node_id=b.node_id and b.node_id=c.node_id and a.module_id=? and b.role_id=? "
      + "and a.rg_code=b.rg_code and a.rg_code=c.rg_code and a.set_year=b.set_year and a.set_year=c.set_year and a.rg_code=? and a.set_year=?";
    List result = dao.findBySql(tmp_sql.toString(), new Object[] { menuId, RoleId, rg_code, setYear });

    if (result.size() < 1) {
      throw new Exception(" 找不到节点！");
    }

    if (!OperateType.equals("999") && OperateType.length() < 4) {
      where_sql.append(" and task.status_code='").append(OperateType).append("'");
    } else {
      where_sql.append(idataright.getSqlBusiRight(UserId, RoleId, "task"));
    }
    where_sql
      .append(
        " and task.rg_code=task2.rg_code and task.set_year=task2.set_year and task.rg_code=task1.rg_code and task.set_year=task1.set_year ")
      .append(" and task.rg_code = '").append(rg_code).append("' ").append(" and task.set_year = ").append(setYear)
      .append(" and task.node_id=task1.node_id and task.node_id=task2.node_id and task2.role_id = '").append(RoleId)
      .append("' and task1.module_id='").append(menuId).append("' ").append(" and task.entity_id=").append(TableAlias)
      .append(".").append(((XMLData) result.get(0)).get("id_column_name"));

    return_sql.setTable_clause(table_sql.append(data_right_sql.getTable_clause()).toString());
    return_sql.setWhere_clause(where_sql.append(data_right_sql.getWhere_clause()).toString());
    return return_sql;
  }

  /**
   * 录入时根据权限找流程和节点,如果批量返回则不用对数据库进行处理
   * 
   * @param menuid-----------菜单ID
   * @param roleid-------------角色ID
   * @param table_name---------单表名
   * @return
   * @throws Exception---------错误信息
   */
  private List isDoSingleProcessSimplyInputDetailSelect(String menuid, String roleid, String tableName, XMLData rowData)
    throws FAppException {
    if (this.isBillDetail) {
      String billId = rowData.get("bill_id").toString();
      if (billId != null && !billId.equals("") && tmpCanGoData.get("bill_id").equals(billId)) {
        return (List) tmpCanGoData.get("isCan");
      } else {
        return doSingleProcessSimplyInputDetailSelect(menuid, roleid, tableName, rowData);
      }
    }
    return doSingleProcessSimplyInputDetailSelect(menuid, roleid, tableName, rowData);

  }

  // 标识是否为单加明细
  public boolean isBillDetail() {
    return this.isBillDetail;
  }

  public void setBillDetail(boolean isBillDetail) {
    this.isBillDetail = isBillDetail;
  }

  /**
   * 根据记账类型不同对其处理方式不同
   * 
   * @param menuid
   * @param roleid
   * @param actiontypeid
   * @param bill_table_name
   * @param detail_table_name
   * @param inputFVoucherDtos
   * @return
   * @author lgc
   * @throws FAppException
   */
  private List doTolly(String menuid, String roleid, String actiontypeid, String bill_table_name,
    String detail_table_name, List inputFVoucherDtos) throws FAppException {
    // 记帐类型为在途记帐或终审记帐，则需要记帐
    try {
      String tolly_flag = "-1";
      List input = new ArrayList();
      // if (tolly_flag.equals("0") || tolly_flag.equals("1")) {//lgc
      int dtoSize = inputFVoucherDtos.size();
      // 如果是录入，则只要判断一条凭证的记账类型即可
      if ("INPUT".equals(actiontypeid)) {
        if (dtoSize > 0) {
          tolly_flag = getTollyType(menuid, roleid, actiontypeid, bill_table_name, detail_table_name,
            (FVoucherDTO) inputFVoucherDtos.get(0));
          for (int i = 0; i < dtoSize; ++i) {
            FVoucherDTO inputFVoucherDto = (FVoucherDTO) inputFVoucherDtos.get(i);
            inputFVoucherDto.setIs_end(Integer.parseInt(tolly_flag));
            // 如果是单+明细的形式，则明细的is_end也要赋值
            if (inputFVoucherDto.getDetails() != null && inputFVoucherDto.getDetails().size() > 0) {
              // 得到明细单数
              int detailsSize = inputFVoucherDto.getDetails().size();
              for (int k = 0; k < detailsSize; k++) {
                FVoucherDTO inputFVoucherDetailDto = (FVoucherDTO) inputFVoucherDto.getDetails().get(k);
                inputFVoucherDetailDto.setIs_end(Integer.parseInt(tolly_flag));
              }
            }
            // 不记帐的时候 将inputFVoucherDtos的单清除 在下步记账时可持正确性
            if ("-1".equals(tolly_flag)) {
              input.add(inputFVoucherDtos.get(i));
              inputFVoucherDtos.remove(i);
            }
          }
        }
      } else {
        for (int i = dtoSize - 1; i >= 0; i--) {
          FVoucherDTO inputFVoucherDto = (FVoucherDTO) inputFVoucherDtos.get(i);
          // lgc add start
          // 得到记帐类型或是否记帐
          tolly_flag = getTollyType(menuid, roleid, actiontypeid, bill_table_name, detail_table_name,
            (FVoucherDTO) inputFVoucherDtos.get(i));

          // lgc add end
          inputFVoucherDto.setIs_end(Integer.parseInt(tolly_flag));
          // 当有明细时 对明细也进行记帐设置
          if (inputFVoucherDto.getDetails() != null && inputFVoucherDto.getDetails().size() > 0) {
            // 得到明细单数
            int detailsSize = inputFVoucherDto.getDetails().size();
            for (int k = 0; k < detailsSize; k++) {
              FVoucherDTO inputFVoucherDetailDto = (FVoucherDTO) inputFVoucherDto.getDetails().get(k);
              inputFVoucherDetailDto.setIs_end(Integer.parseInt(tolly_flag));
            }
          }
          // 不记帐的时候 将inputFVoucherDtos的单清除 在下步记账时可持正确性
          if ("-1".equals(tolly_flag)) {
            input.add(inputFVoucherDtos.get(i));
            inputFVoucherDtos.remove(i);
          }

        }
      }

      if (inputFVoucherDtos.size() > 0) {
        if (actiontypeid.equals("INPUT")) {// 录入
          inputFVoucherDtos = this.doBusVouSaveBatch(inputFVoucherDtos);
        } else if (actiontypeid.equals("NEXT")) {// 送审，将在途和终审分开处理再合并
          Iterator itr = inputFVoucherDtos.iterator();
          List end = new ArrayList();
          List on_land = new ArrayList();
          FVoucherDTO tmp;
          while (itr.hasNext()) {
            tmp = (FVoucherDTO) itr.next();
            if (tmp.getIs_end() == 0) {// 在途
              on_land.add(tmp);
            } else// 终审
            {
              end.add(tmp);
            }
          }
          // 分开调用总帐接口再合并
          inputFVoucherDtos = new ArrayList();
          inputFVoucherDtos.addAll(this.doBusVouUpdateBatch(on_land));// 在途
          inputFVoucherDtos.addAll(this.doBusVouAuditBatch(end));// 终审
        } else if (actiontypeid.equals("EDIT") || actiontypeid.equals("BACK")) {// 修改、退回
          inputFVoucherDtos = this.doBusVouUpdateBatch(inputFVoucherDtos);
        } else if (actiontypeid.equals("DELETE")) {// 删除
          inputFVoucherDtos = this.doBusVouDeleteBatch(inputFVoucherDtos);
        } else if (actiontypeid.equals("DISCARD")) {// 作废
          inputFVoucherDtos = this.doBusVouInvalidBatch(inputFVoucherDtos);
        }
      }
      // 记帐完后，将不记帐的单加回，保持数据完整性
      for (int ip = 0; ip < input.size(); ip++) {
        inputFVoucherDtos.add(input.get(ip));
      }
      // }//lgc
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
    return inputFVoucherDtos;
  }

  /**
   * 大批量数据的导入
   * 
   * @param tableName
   *            数据来源的表
   * @param whereSql
   *            sql语句中的条件
   * @param menuId
   *            菜单ID
   * @param roleId
   *            角色ID
   * @param ENTITY_ID
   *            ENTITY_ID
   * @param moneyField
   *            RESULT_MONEY
   * @throws FAppException
   */
  public void doBatchInput(String tableName, String whereSql, String menuId, String roleId, String entityId,
    String billId, long moneyField) throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();

    try {
      // 获得当前的User
      // String createUser = SessionUtil.getOnlineUser().getUser_code();
      String createUser = "createUser";
      // 储存结果集的表名
      String wfTableName = null;

      // 储存结果集的 nodeId
      int nodeId = 0;

      // 储存结果集的 wfId
      int wfId = 0;

      StringBuffer selectSql = new StringBuffer();
      StringBuffer insertCurrentTaskSql = new StringBuffer();

      /**
       * 根据menu_id和role_id从sys_wf_module_node,sys_wf_role_node表中查找wf_id,wf_table_name,node_id
       */
      selectSql
        .append("select c.wf_id,d.wf_table_name,c.node_id from ")
        .append(" sys_wf_module_node a,sys_wf_role_node b,")
        .append(" sys_wf_nodes c ,sys_wf_flows d ")
        .append(" where a.node_id=b.node_id and a.node_id=c.node_id ")
        .append(" and c.wf_id=d.wf_id  ")
        .append(
          " and a.module_id=? and b.role_id=? and a.rg_code=b.rg_code and a.rg_code=c.rg_code and a.rg_code=d.rg_code ")
        .append(
          " and a.set_year=b.set_year and a.set_year=c.set_year and a.set_year=d.set_year and a.rg_code=? and a.set_year=?");

      // 结果放在一个list中
      List list = dao.findBySql(selectSql.toString(), new Object[] { menuId, roleId, rg_code, setYear });

      // 排除结果集不为"1"
      if (list == null || list.size() != 1) {

      } else {// 返回的结果集为"1" ,存储里面的变量
        String tempnodeid = "";
        String tempwfid = "";
        Iterator it = list.iterator();
        Map map = null;
        while (it.hasNext()) {
          map = new HashMap();
          map = (Map) it.next();
          wfTableName = (String) map.get("wf_table_name");
          tempnodeid = (String) map.get("node_id");
          nodeId = Integer.parseInt(tempnodeid);
          tempwfid = (String) map.get("wf_id");
          wfId = Integer.parseInt(tempwfid);

          // 从tableName表中 选出相应的字段插入到sys_wf_current_tasks表中
          insertCurrentTaskSql
            .append("insert into sys_wf_current_tasks")
            .append(" (TASK_ID,WF_ID,WF_TABLE_NAME,ENTITY_ID,")
            .append(" CURRENT_NODE_ID,NEXT_NODE_ID,ACTION_TYPE_CODE,")
            .append(" IS_UNDO,CREATE_USER,CREATE_DATE,UNDO_USER,")
            .append(" UNDO_DATE,OPERATION_NAME,OPERATION_DATE,")
            .append(" OPERATION_REMARK,INIT_MONEY,RESULT_MONEY,REMARK,")
            .append(" TOLLY_FLAG,BILL_TYPE_CODE,BILL_ID,RCID,CCID,")
            .append(" LAST_VER,SEND_MSG_DATE,AUTO_AUDIT_DATE,SET_MONTH,update_flag,CREATE_USER_ID,rg_code,set_year) ")
            .append(
              " select " + (TypeOfDB.isOracle() ? "Seq_Sys_Wf_Task_Id.Nextval" : " Nextval('SEQ_SYS_WF_TASK_ID') ")
                + " as TASK_ID,").append(wfId + " as WF_ID,'").append(wfTableName + "' as WF_TABLE_NAME,'")
            .append(entityId + "' as ENTITY_ID,").append(" 0 as CURRENT_NODE_ID,'")
            .append(nodeId + "' as NEXT_NODE_ID,").append(" 'INPUT' as ACTION_TYPE_CODE,").append(" 0 as IS_UNDO,'")
            .append(createUser + "' as CREATE_USER,")
            .append((TypeOfDB.isOracle() ? " sysdate" : " sysdate()") + " as create_date,")
            .append(" null as UNDO_USER,").append(" null as UNDO_DATE,").append(" null as OPERATION_NAME,")
            .append(" null as OPERATION_DATE,").append(" null as OPERATION_REMARK,").append(" null,")
            .append(moneyField + " as RESULT_MONEY,").append(" null as REMARK,").append(" 0 as TOLLY_FLAG,")
            .append(" null as BILL_TYPE_CODE,'").append(billId).append("' , rcid as RCID,").append(" ccid as CCID,")
            .append(" null as LAST_VER ,").append(" null as SEND_MSG_DATE ,").append(" null as AUTO_AUDIT_DATE ,")
            .append(" 0 as SET_MONTH,1, ").append(" null as CREATE_USER_ID ,'").append(rg_code).append("' ,")
            .append(setYear).append(" from ").append(tableName).append(" where 1=1 ");

          // 判断wheresql，不为空，拼到insertCurrentTaskSql语句中
          if (null != whereSql) {
            insertCurrentTaskSql.append(" and ").append(whereSql);
          }
          // 调用平台dao.executeBySql执行sql语句
          dao.executeBySql(insertCurrentTaskSql.toString());

          // 清空SQL
          insertCurrentTaskSql.delete(0, insertCurrentTaskSql.length());

          insertCurrentTaskSql.append("insert into sys_wf_current_item select  ").append("'").append(entityId)
            .append("','").append(billId).append("',").append(nodeId).append(",").append("'001'").append(",")
            .append("rcid").append(",").append("ccid").append(",'").append(rg_code).append("',").append(setYear)
            .append(" from ").append(tableName).append(" where 1=1 ");

          // 判断wheresql，不为空，拼到insertCurrentTaskSql语句中
          if (null != whereSql) {
            insertCurrentTaskSql.append(" and ").append(whereSql);
          }

          // 调用平台dao.executeBySql执行sql语句
          dao.executeBySql(insertCurrentTaskSql.toString());

          // 清空SQL
          insertCurrentTaskSql.delete(0, insertCurrentTaskSql.length());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new FAppException(e);
    }
  }

  /**
   * 
   * 菜单： 删除两个临时表内上次任务。
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
  private void delete4CurrentOrCompleteItems(Statement setRoutingPsmt, String gatherSql, String taskId,
    String currentNodeId, String nextNodeId, String entityId, String action_code) throws FAppException, SQLException {
    String sql = null;
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();

    if (setRoutingPsmt != null) {

      sql = "delete from sys_wf_current_item w where w.rg_code='" + rg_code + "' and w.set_year=" + setYear
        + " and w.entity_id = '" + entityId + "' and node_id = " + currentNodeId;

      // 如果是NEXT操作
      if (action_code.equals("EDIT")) {
        sql = "delete from sys_wf_current_item w where exists (select 1 "
          + " from sys_wf_node_conditions  t where  t.routing_type= '001" + "' and t.node_id = " + currentNodeId
          + " and w.node_id = t.node_id and w.rg_code=t.rg_code and w.set_year=t.set_year) and w.entity_id = '"
          + entityId + "' and w.rg_code='" + rg_code + "' and w.set_year=" + setYear;
      }

      setRoutingPsmt.addBatch(sql);

      sql = "delete from sys_wf_complete_item c where entity_id = '" + entityId
        + "' and exists (select 1 from  sys_wf_complete_tasks a ,sys_wf_task_routing b where a.task_id = b.task_id  "
        + " and b.next_task_id = " + taskId
        + " and a.current_node_id = c.node_id and a.rg_code=b.rg_code and a.rg_code=c.rg_code "
        + "and a.set_year=b.set_year and a.set_year=c.set_year) and c.rg_code='" + rg_code + "' and c.set_year="
        + setYear;

      // 同步时不能直接删除完成任务的ITEM 需要条件同时满足时才能删除
      if (gatherSql.length() > 0) {

        sql = sql + " and exists( " + gatherSql + ")";
      }
      //      Log.error("---加入删除语句到batch中---");

      setRoutingPsmt.addBatch(sql);
      //      Log.error("---加入删除语句到batch中结束---");
    } else {

      sql = "delete from sys_wf_current_item w where  exists "
        + "(select 1 from sys_wf_complete_tasks t where "
        + " w.rg_code=t.rg_code and w.set_year=t.set_year and w.node_id = t.next_node_id and entity_id = ? and task_id = ?  and current_node_id = ?) and entity_id= ? and rg_code=? and set_year=?";

      this.dao.executeBySql(sql, new Object[] { entityId, taskId, nextNodeId, entityId, rg_code, setYear });

      // 同步时不能直接删除完成任务的ITEM 需要条件同时满足时才能删除
      sql = "delete from sys_wf_complete_item where node_id = ? and  entity_id = ? and rg_code=? and set_year=?";
      if (gatherSql.length() > 0) {
      }
      //      System.out.println("---执行删除语句开始---");
      this.dao.executeBySql(sql, new Object[] { currentNodeId, entityId, rg_code, setYear });
      //      System.out.println("---执行删除语句结束---");
    }

  }

  /**
   * 
   * 菜单： 添加新任务到临时表内(方法作废)
   * 
   * <br>
   * Date ：2008-1-31
   * 
   * @param entityId 实体ID
   * @param bill_id
   * @param nodeId 当前节点ID
   * @param statusCode 操作状态
   * @param rcid
   * @param ccid
   * 
   * @author bing-zeng
   * @throws SQLException
   * @since 1.0
   * 
   */
  private void add4CurrentOrCompleteItems(Statement setRoutingPsmt, String entityId, String bill_id, String nodeId,
    String statusCode, String rcid, String ccid) throws FAppException, SQLException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();

    String sql = null;
    if (setRoutingPsmt != null) {
      if ("001".equals(statusCode) || "004".equals(statusCode)) {
        // modified by bigdog 080917
        // 如果node为结束节点，这不需要在sys_wf_current_item表
        // 中插入数据
        sql = "insert into sys_wf_current_item select '" + entityId + "', '" + bill_id + "'," + nodeId + ",'"
          + statusCode + "','" + rcid + "','" + ccid + "','" + setYear + "'," + rg_code
          + " from dual where  not exists " + "( select 1 from sys_wf_current_item where " + " entity_id = '"
          + entityId + "' and node_id = " + nodeId + " and rg_code='" + rg_code + "' and set_year=" + setYear + ")"
          + " and not exists(select 1 from sys_wf_nodes wn  " + " where wn.node_id = " + nodeId
          + " and node_type = '003' and rg_code='" + rg_code + "' and set_year=" + setYear + ")";
        /*
         * sql = "insert into sys_wf_current_item select '" + entityId +
         * "', '" + bill_id + "'," + nodeId + ",'" + statusCode + "','" +
         * rcid + "','" + ccid + "' from dual where not exists " + "(
         * select 1 from sys_wf_current_item where " + " entity_id = '" +
         * entityId + "' and node_id = " + nodeId + ")";
         */
      } else {
        sql = "insert into sys_wf_complete_item select '" + entityId + "', '" + bill_id + "'," + nodeId + ",'"
          + statusCode + "','" + rcid + "','" + ccid + "','" + setYear + "'," + rg_code
          + " from dual where  not exists" + " ( select 1 from sys_wf_complete_item where " + " entity_id = '"
          + entityId + "' and node_id = " + nodeId + " and rg_code='" + rg_code + "' and set_year=" + setYear + ")";
      }
      //      Log.error("----增加的sql="+sql);

      setRoutingPsmt.addBatch(sql);
    } else {
      if ("001".equals(statusCode) || "004".equals(statusCode)) {
        sql = "insert into sys_wf_current_item select '" + entityId + "', '" + bill_id + "'," + nodeId + ",'"
          + statusCode + "','" + rcid + "','" + ccid + "','" + rg_code + "'," + setYear
          + " from dual where  not exists " + "( select 1 from sys_wf_current_item where "
          + " entity_id = ? and node_id = ? and rg_code='" + rg_code + "' and set_year=" + setYear + ")";
      } else {
        sql = "insert into sys_wf_complete_item select '" + entityId + "', '" + bill_id + "'," + nodeId + ",'"
          + statusCode + "','" + rcid + "','" + ccid + "','" + rg_code + "'," + setYear
          + " from dual where  not exists" + " ( select 1 from sys_wf_complete_item where "
          + " entity_id = ? and node_id = ? and rg_code='" + rg_code + "' and set_year=" + setYear + ")";
      }
      //      Log.error("----(setRoutingPsmt==null)增加的sql="+sql);
      this.dao.executeBySql(sql, new Object[] { entityId, nodeId });
    }
  }

  /**
   * 
   * 菜单： 添加新任务到临时表内
   * (新增加方法,sys_wf_current_item表结构增加了字段,MB_ID,EN_ID,PB_ID,CB_ID,IB_ID,AGENCY_ID)
   * 在add4CurrentOrCompleteItems方法的基础上修改,方法add4CurrentOrCompleteItems应该要作废 <br>
   * Date ：2015-03-11
   * 
   * @param entityId 实体ID
   * @param bill_id
   * @param nodeId 当前节点ID
   * @param statusCode 操作状态
   * @param rcid
   * @param ccid
   * 
   * @author bing-zeng
   * @throws SQLException
   * @since 1.0
   * 
   */
  private void add4CurrentOrCompleteItemsNew(Statement setRoutingPsmt, String entityId, String bill_id, String nodeId,
    String statusCode, String rcid, String ccid, String MB_ID, String EN_ID, String PB_ID, String CB_ID, String IB_ID,
    String AGENCY_ID) throws FAppException, SQLException {
    // add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();

    String sql = null;
    if (setRoutingPsmt != null) {
      if ("001".equals(statusCode) || "004".equals(statusCode)) {
        // modified by bigdog 080917
        // 如果node为结束节点，这不需要在sys_wf_current_item表
        // 中插入数据
        sql = "insert into sys_wf_current_item select '" + entityId + "', '" + bill_id + "'," + nodeId + ",'"
          + statusCode + "','" + rcid + "','" + ccid + "','" + setYear + "'," + rg_code + ",'" + MB_ID + "','" + EN_ID
          + "','" + PB_ID + "','" + CB_ID + "','" + IB_ID + "','" + AGENCY_ID + "'" + " from dual where  not exists "
          + "( select 1 from sys_wf_current_item where " + " entity_id = '" + entityId + "' and node_id = " + nodeId
          + " and rg_code='" + rg_code + "' and set_year=" + setYear + ")"
          + " and not exists(select 1 from sys_wf_nodes wn  " + " where wn.node_id = " + nodeId
          + " and node_type = '003' and rg_code='" + rg_code + "' and set_year=" + setYear + ")";
        /*
         * sql = "insert into sys_wf_current_item select '" + entityId + "', '"
         * + bill_id + "'," + nodeId + ",'" + statusCode + "','" + rcid + "','"
         * + ccid + "' from dual where not exists " + "( select 1 from
         * sys_wf_current_item where " + " entity_id = '" + entityId +
         * "' and node_id = " + nodeId + ")";
         */
      } else {
        sql = "insert into sys_wf_complete_item select '" + entityId + "', '" + bill_id + "'," + nodeId + ",'"
          + statusCode + "','" + rcid + "','" + ccid + "','" + setYear + "'," + rg_code + ",'" + MB_ID + "','" + EN_ID
          + "','" + PB_ID + "','" + CB_ID + "','" + IB_ID + "','" + AGENCY_ID + "'" + " from dual where  not exists"
          + " ( select 1 from sys_wf_complete_item where " + " entity_id = '" + entityId + "' and node_id = " + nodeId
          + " and rg_code='" + rg_code + "' and set_year=" + setYear + ")";
      }
      // Log.error("----增加的sql="+sql);

      setRoutingPsmt.addBatch(sql);
    } else {
      if ("001".equals(statusCode) || "004".equals(statusCode)) {
        sql = "insert into sys_wf_current_item select '" + entityId + "', '" + bill_id + "'," + nodeId + ",'"
          + statusCode + "','" + rcid + "','" + ccid + "'," + setYear + "','" + rg_code + ",'" + MB_ID + "','" + EN_ID
          + "','" + PB_ID + "','" + CB_ID + "','" + IB_ID + "','" + AGENCY_ID + "'" + " from dual where  not exists "
          + "( select 1 from sys_wf_current_item where " + " entity_id = ? and node_id = ? and rg_code='" + rg_code
          + "' and set_year=" + setYear + ")";
      } else {
        sql = "insert into sys_wf_complete_item select '" + entityId + "', '" + bill_id + "'," + nodeId + ",'"
          + statusCode + "','" + rcid + "','" + ccid + "'," + setYear + "','" + rg_code + ",'" + MB_ID + "','" + EN_ID
          + "','" + PB_ID + "','" + CB_ID + "','" + IB_ID + "','" + AGENCY_ID + "'" + " from dual where  not exists"
          + " ( select 1 from sys_wf_complete_item where " + " entity_id = ? and node_id = ? and rg_code='" + rg_code
          + "' and set_year=" + setYear + ")";
      }
      // Log.error("----(setRoutingPsmt==null)增加的sql="+sql);
      this.dao.executeBySql(sql, new Object[] { entityId, nodeId });
    }
  }

  /**
   * 
   * 菜单：
   * 
   * <br>
   * Date ：2008-1-31
   * 
   * @param taskId 当前的任务ID
   * @param wfId 工作流ID
   * @param entityId 工作流实体ID
   * @param currentNodeId 当前节点
   * @param nextNodeId 下一个节点
   * @param actionType 操作类型
   * @param billId
   * @param rcid
   * @param ccid
   * 
   * @author bing-zeng
   * @throws Exception
   * @since 1.0
   * 
   */
  public void saveOptionCurrentAndComleteTable(Statement setRoutingPsmt, String taskId, String wfId, String entityId,
    String currentNodeId, String nextNodeId, String actionType, String billId, String rcid, String ccid)
    throws Exception {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    String sql = null;
    List list = null;
    // 同步操作时查询是否满足走入下一个节点.
    StringBuffer sqlBuffer = new StringBuffer();

    StringBuffer gatherBuffer = new StringBuffer();

    // 如果不存在节点同步标志信息
    // if (UtilCache.gatherNodeSignMap == null) {
    if (null == UtilCache.getGatherNodeSign(nextNodeId) || "".equals(UtilCache.getGatherNodeSign(nextNodeId))) {
      // 判断当前节点是同步, 还是异步
      sql = "select node_id, gather_flag  from sys_wf_nodes where rg_code=? and set_year=? ";
      list = this.dao.findBySql(sql, new Object[] { rg_code, setYear });
      Iterator returnList = list.iterator();
      while (returnList.hasNext()) {
        XMLData temp = (XMLData) returnList.next();
        UtilCache.putGatherNodeSign(temp.get("node_id").toString(), temp.get("gather_flag").toString());
      }
    }

    String flag = "1";
    if ("0".equals(nextNodeId)) {
      nextNodeId = currentNodeId;
      // 从缓存中取得当前节点的同步，异步标志 flag 0 ：同步 ， 1 ：异步
      flag = UtilCache.getGatherNodeSign(nextNodeId);
    } else {// 不为0时证明不是修改节点，直接next_node_id取成同步异步即可 --beaf
      flag = UtilCache.getGatherNodeSign(nextNodeId);
    }
    //之前是"0".equals(SYNCHRONIZATIONFLAG) SYNCHRONIZATIONFLAG常量一直没赋值 不明白为什么这么比 改成flag判断是否同步
    if (flag.equals(SYNCHRONIZATIONFLAG)) {
      gatherBuffer
        .append(" select 1 from sys_wf_role_node a, sys_wf_module_node   b, sys_wf_nodes c, ")
        .append(" sys_wf_current_tasks d where a.node_id = b.node_id and b.node_id = c.node_id ")
        .append(" and c.gather_flag = 0 and d.wf_table_name = c.wf_table_name ")
        .append(" and ((c.node_id = d.current_node_id and d.action_type_code = 'EDIT') or ")
        .append(
          " (c.node_id = d.next_node_id and d.action_type_code in ('INPUT', 'NEXT') and "
            + (TypeOfDB.isOracle() ? "not" : "") + " exists ")
        .append(
          " (select e.node_id from sys_wf_node_conditions e  where e.rg_code=a.rg_code and e.set_year=a.set_year and e.wf_id = c.wf_id ")
        .append(
          "  and e.next_node_id = c.node_id and e.routing_type = '001' "
            + (TypeOfDB.isOracle() ? " minus " : " ) and not exists( "))
        .append(
          " select f.current_node_id from sys_wf_current_tasks f where f.rg_code=a.rg_code and f.set_year=a.set_year and f.wf_id = c.wf_id ")
        .append(" and f.wf_table_name = c.wf_table_name  and f.next_node_id = c.node_id")
        .append(" and f.entity_id = d.entity_id))) and d.entity_id = '").append(entityId).append("' and d.wf_id =")
        .append(wfId).append(" and d.current_node_id =").append(currentNodeId)
        .append(" and a.rg_code=b.rg_code and a.rg_code=c.rg_code and a.rg_code=d.rg_code")
        .append(" and a.set_year=b.set_year and a.set_year=c.set_year and a.set_year=d.set_year")
        .append(" and a.rg_code='").append(rg_code).append("' and a.set_year=").append(setYear);

      sqlBuffer.append(" insert into sys_wf_current_item  select  ").append("  '").append(entityId).append("' , '")
        .append(billId).append("'  , '").append(nextNodeId).append("' , ").append("'001'").append(" , ").append(rcid)
        .append(" , ").append(ccid).append(" , '").append(rg_code).append("' , ").append(setYear)
        .append(" from dual where   exists(  ").append(gatherBuffer.toString()).append(")");
    }

    // 操作类型以第一个字母取代
    char var = actionType.charAt(0);

    switch (var) {

    // INPUT
    case 'I':
      add4CurrentOrCompleteItems(null, entityId, billId, nextNodeId, "001", rcid, ccid);
      break;

    // BACK
    case 'B':
      delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
        entityId, actionType);

      // 同步操作 如果查询信息返回的集合小于1 不能进入下一个节点
      if (flag.equals(SYNCHRONIZATIONFLAG)) {
        sqlBuffer = new StringBuffer();
        sqlBuffer.append(" insert into sys_wf_current_item  select  ").append("  '").append(entityId).append("' , '")
          .append(billId).append("'  , '").append(nextNodeId).append("' , ").append("'004'").append(" , ").append(rcid)
          .append(" , ").append(ccid).append(" , '").append(rg_code).append("' , ").append(setYear)
          .append(" from dual where   exists(  ").append(gatherBuffer.toString()).append(")");
        setRoutingPsmt.addBatch(sqlBuffer.toString());
      } else {
        add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, nextNodeId, "004", rcid, ccid);
      }

      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "003", rcid, ccid);

      break;

    // EDIT
    case 'E':
      delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
        entityId, actionType);
      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "001", rcid, ccid);
      break;

    // DELETE | DISCARD
    case 'D':

      if ("DISCARD".equals(actionType)) {
        delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
          entityId, actionType);
        add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "103", rcid, ccid);
      } else {
        delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
          entityId, "001");
        add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "102", rcid, ccid);
      }
      break;

    // RECALL
    case 'R':
      // 同步操作

      delete4CurrentOrCompleteItems4Recall(setRoutingPsmt, taskId, currentNodeId, nextNodeId, entityId);

      // 同步操作 如果查询信息返回的集合小于1 不能进入下一个节点

      if (flag.equals(SYNCHRONIZATIONFLAG)) {

        sql = "insert into sys_wf_current_item  select entity_id, bill_id, next_node_id, '001', rcid, ccid,rg_code,set_year "
          + " from sys_wf_current_tasks where  entity_id = ?  and task_id = ? and rg_code=? and set_year=?";
        this.dao.executeBySql(sql, new Object[] { entityId, taskId, rg_code, setYear });
        add4CurrentOrCompleteItems4Recall(setRoutingPsmt, actionType, entityId, billId, currentNodeId, nextNodeId,
          "001", rcid, ccid);
      } else {
        //         撤销退回的判断
        String actionSql = "select * from sys_wf_current_tasks where task_id = ? and rg_code=? and set_year=?";
        List tempList = this.dao.findBySql(actionSql, new Object[] { taskId, rg_code, setYear });

        // 当前任务的操作类型
        String actionTypeCode = "0";
        if (tempList.size() > 0) {
          XMLData x = (XMLData) tempList.get(0);
          actionTypeCode = x.get("action_type_code").toString();
        }
        if (actionTypeCode.equals("BACK"))
          add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, nextNodeId, "004", rcid, ccid);
        else
          add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, nextNodeId, "001", rcid, ccid);
        add4CurrentOrCompleteItems4Recall(setRoutingPsmt, actionTypeCode, entityId, billId, currentNodeId, nextNodeId,
          "001", rcid, ccid);
      }
      break;

    // NEXT
    case 'N':
      delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
        entityId, actionType);
      // 同步操作 如果查询信息返回的集合小于1 不能进入下一个节点
      if (flag.equals(SYNCHRONIZATIONFLAG)) {

        setRoutingPsmt.addBatch(sqlBuffer.toString());

      } else {
        add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, nextNodeId, "001", rcid, ccid);
      }
      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "002", rcid, ccid);
      break;

    // HANG
    case 'H':
      delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
        entityId, actionType);

      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "101", rcid, ccid);
      break;
    }

  }

  /**
   * 
   * 菜单：
   * 
   * <br>
   * Date ：2008-1-31
   * 
   * @param taskId 当前的任务ID
   * @param wfId 工作流ID
   * @param entityId 工作流实体ID
   * @param currentNodeId 当前节点
   * @param nextNodeId 下一个节点
   * @param actionType 操作类型
   * @param billId
   * @param rcid
   * @param ccid
   * 
   * @author bing-zeng
   * @throws Exception
   * @since 1.0 FVoucherDTO
   */
  public void saveOptionCurrentAndComleteTableNew(Statement setRoutingPsmt, String taskId, String wfId,
    String entityId, String currentNodeId, String nextNodeId, String actionType, String billId, String rcid,
    String ccid, FVoucherDTO inputFVoucherDto) throws Exception {
    // add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    String sql = null;
    List list = null;
    // 同步操作时查询是否满足走入下一个节点.
    StringBuffer sqlBuffer = new StringBuffer();

    StringBuffer gatherBuffer = new StringBuffer();

    // 如果不存在节点同步标志信息
    // if (UtilCache.gatherNodeSignMap == null) {
    if (null == UtilCache.getGatherNodeSign(nextNodeId) || "".equals(UtilCache.getGatherNodeSign(nextNodeId))) {
      // 判断当前节点是同步, 还是异步
      sql = "select node_id, gather_flag  from sys_wf_nodes where rg_code=? and set_year=? ";
      list = this.dao.findBySql(sql, new Object[] { rg_code, setYear });
      Iterator returnList = list.iterator();
      while (returnList.hasNext()) {
        XMLData temp = (XMLData) returnList.next();
        UtilCache.putGatherNodeSign(temp.get("node_id").toString(), temp.get("gather_flag").toString());
      }
    }

    String flag = "1";
    if ("0".equals(nextNodeId)) {
      nextNodeId = currentNodeId;
      // 从缓存中取得当前节点的同步，异步标志 flag 0 ：同步 ， 1 ：异步
      flag = UtilCache.getGatherNodeSign(nextNodeId);
    } else {// 不为0时证明不是修改节点，直接next_node_id取成同步异步即可 --beaf
      flag = UtilCache.getGatherNodeSign(nextNodeId);
    }
    //之前是"0".equals(SYNCHRONIZATIONFLAG) SYNCHRONIZATIONFLAG常量一直没赋值
    // 不明白为什么这么比 改成flag判断是否同步
    if (flag.equals(SYNCHRONIZATIONFLAG)) {
      gatherBuffer
        .append(" select 1 from sys_wf_role_node a, sys_wf_module_node   b, sys_wf_nodes c, ")
        .append(" sys_wf_current_tasks d where a.node_id = b.node_id and b.node_id = c.node_id ")
        .append(" and c.gather_flag = 0 and d.wf_table_name = c.wf_table_name ")
        .append(" and ((c.node_id = d.current_node_id and d.action_type_code = 'EDIT') or ")
        .append(
          " (c.node_id = d.next_node_id and d.action_type_code in ('INPUT', 'NEXT') and "
            + (TypeOfDB.isOracle() ? "not" : "") + " exists ")
        .append(
          " (select e.node_id from sys_wf_node_conditions e  where e.rg_code=a.rg_code and e.set_year=a.set_year and e.wf_id = c.wf_id ")
        .append(
          "  and e.next_node_id = c.node_id and e.routing_type = '001' "
            + (TypeOfDB.isOracle() ? "minus " : " ) and not exists( "))
        .append(
          " select f.current_node_id from sys_wf_current_tasks f where f.rg_code=a.rg_code and f.set_year=a.set_year and f.wf_id = c.wf_id ")
        .append(" and f.wf_table_name = c.wf_table_name  and f.next_node_id = c.node_id")
        .append(" and f.entity_id = d.entity_id))) and d.entity_id = '").append(entityId).append("' and d.wf_id =")
        .append(wfId).append(" and d.current_node_id =").append(currentNodeId)
        .append(" and a.rg_code=b.rg_code and a.rg_code=c.rg_code and a.rg_code=d.rg_code")
        .append(" and a.set_year=b.set_year and a.set_year=c.set_year and a.set_year=d.set_year")
        .append(" and a.rg_code='").append(rg_code).append("' and a.set_year=").append(setYear);

      sqlBuffer.append(" insert into sys_wf_current_item  select  ").append("  '").append(entityId).append("' , '")
        .append(billId).append("'  , '").append(nextNodeId).append("' , ").append("'001'").append(" , ").append(rcid)
        .append(" , ").append(ccid).append(" , '").append(rg_code).append("' , ").append(setYear)
        .append(" from dual where   exists(  ").append(gatherBuffer.toString()).append(")");
    }

    // 操作类型以第一个字母取代
    char var = actionType.charAt(0);

    switch (var) {

    // INPUT
    case 'I':
      add4CurrentOrCompleteItems(null, entityId, billId, nextNodeId, "001", rcid, ccid);
      break;

    // BACK
    case 'B':
      delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
        entityId, actionType);

      // 同步操作 如果查询信息返回的集合小于1 不能进入下一个节点
      if (flag.equals(SYNCHRONIZATIONFLAG)) {
        sqlBuffer = new StringBuffer();
        sqlBuffer.append(" insert into sys_wf_current_item  select  ").append("  '").append(entityId).append("' , '")
          .append(billId).append("'  , '").append(nextNodeId).append("' , ").append("'004'").append(" , ").append(rcid)
          .append(" , ").append(ccid).append(" , '").append(rg_code).append("' , ").append(setYear)
          .append(" from dual where   exists(  ").append(gatherBuffer.toString()).append(")");
        setRoutingPsmt.addBatch(sqlBuffer.toString());
      } else {
        // add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId,
        // nextNodeId, "004", rcid, ccid);
        add4CurrentOrCompleteItemsNew(setRoutingPsmt, entityId, billId, nextNodeId, "004", rcid, ccid,
          inputFVoucherDto.getMb_id(), inputFVoucherDto.getAgency_id(), inputFVoucherDto.getPb_id(),
          inputFVoucherDto.getCb_id(), inputFVoucherDto.getIb_id(), inputFVoucherDto.getAgency_id());

      }

      // add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId,
      // currentNodeId, "003", rcid, ccid);
      add4CurrentOrCompleteItemsNew(setRoutingPsmt, entityId, billId, nextNodeId, "003", rcid, ccid,
        inputFVoucherDto.getMb_id(), inputFVoucherDto.getAgency_id(), inputFVoucherDto.getPb_id(),
        inputFVoucherDto.getCb_id(), inputFVoucherDto.getIb_id(), inputFVoucherDto.getAgency_id());

      break;

    // EDIT
    case 'E':
      delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
        entityId, actionType);
      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "001", rcid, ccid);
      break;

    // DELETE | DISCARD
    case 'D':

      if ("DISCARD".equals(actionType)) {
        delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
          entityId, actionType);
        add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "103", rcid, ccid);
      } else {
        delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
          entityId, "001");
        add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "102", rcid, ccid);
      }
      break;

    // RECALL
    case 'R':
      // 同步操作

      delete4CurrentOrCompleteItems4Recall(setRoutingPsmt, taskId, currentNodeId, nextNodeId, entityId);

      // 同步操作 如果查询信息返回的集合小于1 不能进入下一个节点

      if (flag.equals(SYNCHRONIZATIONFLAG)) {

        sql = "insert into sys_wf_current_item  select entity_id, bill_id, next_node_id, '001', rcid, ccid,rg_code,set_year "
          + " from sys_wf_current_tasks where  entity_id = ?  and task_id = ? and rg_code=? and set_year=?";
        this.dao.executeBySql(sql, new Object[] { entityId, taskId, rg_code, setYear });
        // add4CurrentOrCompleteItems4Recall(setRoutingPsmt, taskId,
        // entityId, billId, currentNodeId, nextNodeId, "001",
        // rcid, ccid);
        add4CurrentOrCompleteItems4Recall(setRoutingPsmt, actionType, entityId, billId, currentNodeId, nextNodeId,
          "001", rcid, ccid);
      } else {
        // 撤销退回的判断
        String actionSql = "select * from sys_wf_current_tasks where task_id = ? and rg_code=? and set_year=?";
        List tempList = this.dao.findBySql(actionSql, new Object[] { taskId, rg_code, setYear });

        // 当前任务的操作类型
        String actionTypeCode = "0";
        if (tempList.size() > 0) {
          XMLData x = (XMLData) tempList.get(0);
          actionTypeCode = x.get("action_type_code").toString();
        }
        if (actionTypeCode.equals("BACK"))
          add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, nextNodeId, "004", rcid, ccid);
        // add4CurrentOrCompleteItemsNew(setRoutingPsmt, entityId, billId,
        // nextNodeId, "004", rcid, ccid);
        else
          add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, nextNodeId, "001", rcid, ccid);
        add4CurrentOrCompleteItems4Recall(setRoutingPsmt, actionTypeCode, entityId, billId, currentNodeId, nextNodeId,
          "001", rcid, ccid);
      }
      break;

    // NEXT
    case 'N':
      // Log.error("----删除开始 entityId="+entityId+",taskId="+taskId+"----");
      delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
        entityId, actionType);
      // Log.error("----删除结束 entityId="+entityId+",taskId="+taskId+"----");
      // 同步操作 如果查询信息返回的集合小于1 不能进入下一个节点
      if (flag.equals(SYNCHRONIZATIONFLAG)) {

        setRoutingPsmt.addBatch(sqlBuffer.toString());

      } else {
        // Log.error("----开始增加001 entityId="+entityId+",taskId="+taskId+"----");
        add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, nextNodeId, "001", rcid, ccid);
      }
      // Log.error("----开始增加002 entityId="+entityId+",taskId="+taskId+"----");
      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "002", rcid, ccid);
      break;

    // HANG
    case 'H':
      delete4CurrentOrCompleteItems(setRoutingPsmt, gatherBuffer.toString(), taskId, currentNodeId, nextNodeId,
        entityId, actionType);

      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, currentNodeId, "101", rcid, ccid);
      break;
    }

  }

  /**
   * 
   * 菜单：RECALL 专用删除任务ITEM方法
   * 
   * <br>
   * Date ：2008-2-19
   * 
   * @param setRoutingPsmt Statement
   * @param taskId 工作流ID
   * @param currentNodeId 当前节点ID
   * @param nextNodeId 下一个节点ID
   * @param entityId 实体ID
   * @throws FAppException
   * @throws SQLException
   * 
   * @author bing-zeng
   * @since 1.0
   * 
   */
  private void delete4CurrentOrCompleteItems4Recall(Statement setRoutingPsmt, String taskId, String currentNodeId,
    String nextNodeId, String entityId) throws FAppException, SQLException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    String sql = null;

    // 删除CURRENT ITEM表当前任务
    sql = "delete from sys_wf_current_item w where exists (select 1 from sys_wf_complete_tasks t, sys_wf_task_routing r "
      + " where t.entity_id = ? and w.node_id = t.next_node_id and t.task_id = r.next_task_id  and t.current_node_id =? and r.task_id = ? "
      + "and w.rg_code=t.rg_code and w.rg_code=r.rg_code and w.set_year=t.set_year and w.set_year=r.set_year) "
      + " and w.entity_id = ? and w.rg_code=? and w.set_year=?";

    int tempDelete = this.dao.executeBySql(sql,
      new Object[] { entityId, nextNodeId, taskId, entityId, rg_code, setYear });

    // 如果是编辑 则删除当前节点为标志。
    if (tempDelete < 1) {
      sql = "delete from sys_wf_current_item w where node_id = ?"
        + " and w.entity_id = ? and w.rg_code=? and w.set_year=?";
      this.dao.executeBySql(sql, new Object[] { nextNodeId, entityId, rg_code, setYear });
    }

    // 删除COMPLETE ITEM表上一个完成的任务
    sql = "delete from sys_wf_complete_item  w where   exists (select 1 from sys_wf_complete_tasks t, sys_wf_task_routing r "
      + " where t.entity_id = ? and w.node_id  = t.current_node_id and t.task_id = r.next_task_id  and t.current_node_id =? and r.task_id = ? "
      + "and w.rg_code=t.rg_code and w.rg_code=r.rg_code and w.set_year=t.set_year and w.set_year=r.set_year) "
      + " and w.entity_id = ?  and w.rg_code=? and w.set_year=?";
    this.dao.executeBySql(sql, new Object[] { entityId, nextNodeId, taskId, entityId, rg_code, setYear });

  }

  /**
   * 
   * 菜单： RECALL 专用增加任务ITEM表所使用方法
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
  private void add4CurrentOrCompleteItems4Recall(Statement setRoutingPsmt, String actionType, String entityId,
    String bill_id, String currentNodeId, String nextNodeId, String statusCode, String rcid, String ccid)
    throws FAppException, SQLException {
    // 操作类型以第一个字母取代
    char var = actionType.charAt(0);
    switch (var) {

    // NEXT
    case 'N':
      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, bill_id, currentNodeId, "002", rcid, ccid);
      break;

    // INPUT
    case 'I':
      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, bill_id, currentNodeId, "002", rcid, ccid);
      break;

    // EDIT
    case 'E':
      break;

    // BACK
    case 'B':
      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, bill_id, currentNodeId, "003", rcid, ccid);
      break;

    // DELETE, DISCARD
    case 'D':

      break;

    // RECALL
    case 'R':
      //修改recall时候不写completeItem表bug
      add4CurrentOrCompleteItems(setRoutingPsmt, entityId, bill_id, currentNodeId, "002", rcid, ccid);
      break;

    }

  }

  private String getCOAIDByBillTypeCode(String billType_code) {
    String coa = UtilCache.getCoaIDByBillTypeCode(billType_code);
    String coa_id = "";
    if (coa != null && !coa.equals("")) {
      coa_id = coa;
    } else {
      coa_id = (String) billtype.getBillTypeByCode(billType_code).getCoa_id();
      UtilCache.putCoaIDByBillTypeCode(billType_code, coa_id);
    }
    return coa_id;
  }

  /**
   * 工作流任意节点作废删除和撤消操作接口
   * 
   * @param actionType
   *            动作类型（DISCARD，DELETE和RECALL）
   * @param operationName
   *            操作名称
   * @param operationDate
   *            操作日期
   * @param operationRemark
   *            操作备注
   * @param tableName
   *            业务表名称
   * @param inputFVoucherDTOs
   *            凭证列表
   * @param auto_tolly_flag
   *            是否需要记账
   * @return
   * @throws FAppException
   */
  public List doStrideProcess(String actionType, String operationName, String operationDate, String operationRemark,
    List inputFVoucherDTOs, boolean auto_tolly_flag, String table_name) throws FAppException {

    if (inputFVoucherDTOs.size() == 0)
      throw new FAppException("传入的凭证列表为空！");

    if (auto_tolly_flag == true) {
      doStrideProcessTolly(actionType, inputFVoucherDTOs);
    }
    Statement st = null;
    Connection con = null;
    Session session = dao.getSession();
    try {
      con = session.connection();
      // st = dao.getSession().connection().createStatement();
      st = con.createStatement();

      for (int i = 0; i < inputFVoucherDTOs.size(); ++i) {
        FVoucherDTO vou = (FVoucherDTO) inputFVoucherDTOs.get(i);
        if (vou.getDetails() == null || vou.getDetails().size() == 0) {
          doStrideProcessSimply(actionType, operationName, operationDate, operationRemark, vou, st, table_name);
        } else {
          for (int j = 0; j < vou.getDetails().size(); ++i) {
            FVoucherDTO vou1 = (FVoucherDTO) vou.getDetails().get(j);
            doStrideProcessSimply(actionType, operationName, operationDate, operationRemark, vou1, st, table_name);
          }
        }
      }

      st.executeBatch();
      // con.commit();
    } catch (Exception e) {
      e.printStackTrace();
      throw new FAppException(e);
    } finally {
      dao.closeSession(session);
    }

    return inputFVoucherDTOs;
  }

  private List doStrideProcessTolly(String actionType, List inputFVoucherDTOs) throws FAppException {
    String tolly_flag = "-1";
    List noAuditVouchers = new ArrayList();
    List overAudit = new ArrayList();
    List onloadAudit = new ArrayList();
    for (int i = inputFVoucherDTOs.size() - 1; i >= 0; --i) {
      FVoucherDTO vou = (FVoucherDTO) inputFVoucherDTOs.get(i);
      tolly_flag = getStrideProcessTollyType(actionType, vou);
      vou.setIs_end(Integer.parseInt(tolly_flag));
      if (vou.getDetails() != null && vou.getDetails().size() > 0) {
        for (int j = 0; i < vou.getDetails().size(); ++j)
          ((FVoucherDTO) vou.getDetails().get(j)).setIs_end(Integer.parseInt(tolly_flag));
      }
      if ("-1".equals(tolly_flag)) {
        noAuditVouchers.add(inputFVoucherDTOs.get(i));
        inputFVoucherDTOs.remove(i);
      }

      if ("RECALL".equals(actionType)) {
        if ("0".equals(tolly_flag))
          onloadAudit.add(vou);
        else if ("1".equals(tolly_flag))
          overAudit.add(vou);
      }
    }

    if ("DISCARD".equals(actionType)) {
      this.doBusVouInvalidBatch(inputFVoucherDTOs);
    } else if ("DELETE".equals(actionType)) {
      this.doBusVouDeleteBatch(inputFVoucherDTOs);
    } else if ("RECALL".equals(actionType)) {
      this.doBusVouUpdateBatch(onloadAudit);// 调用批量更新执行在途的撤销
      this.doBusVouCancelAuditBatch(overAudit);// 调用批量更新执行终审核的撤销
    }

    for (int i = 0; i < noAuditVouchers.size(); ++i) {
      inputFVoucherDTOs.add(noAuditVouchers.get(i));
    }
    return inputFVoucherDTOs;
  }

  private String getStrideProcessTollyType(String actionType, FVoucherDTO inputFVoucherDto) throws FAppException {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();

    FVoucherDTO vou = null;
    if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0)
      vou = inputFVoucherDto;
    else
      vou = (FVoucherDTO) inputFVoucherDto.getDetails().get(0);
    StringBuffer sql = new StringBuffer();
    if ("DISCARD".equals(actionType) || "DELETE".equals(actionType)) {
      sql
        .append("select t1.tolly_flag from sys_wf_node_tolly_action_type t1, ")
        .append(
          " (select a.entity_id, case when a.action_type_code in ('INPUT', 'NEXT', 'BACK') then next_node_id when a.action_type_code in ('EDIT', 'HANG') then current_node_id end next_node_id,a.rg_code,a.set_year from vw_sys_wf_current_end_tasks a) t2 ")
        .append(" where t1.node_id = t2.NEXT_NODE_ID and t2.entity_id = '")
        .append(vou.getVou_id())
        .append("'")
        .append(" and t1.action_type_code = '")
        .append(actionType)
        .append(
          "' and t1.rg_code=t2.rg_code and t1.set_year=t2.set_year and t1.rg_code='" + rg_code + "' and t1.set_year="
            + setYear);
    } else if ("RECALL".equals(actionType)) {
      sql.append("select t.TOLLY_FLAG ").append("from vw_sys_wf_current_end_tasks t ").append("where t.ENTITY_ID = '")
        .append(vou.getVou_id()).append("' and t.rg_code='" + rg_code + "' and t.set_year=" + setYear);
    }
    List ls = dao.findBySql(sql.toString());
    if (!ls.isEmpty()) {
      return ((Map) ls.get(0)).get("tolly_flag") == null ? "-1" : ((Map) ls.get(0)).get("tolly_flag").toString();
    }
    return "-1";
  }

  private FVoucherDTO doStrideProcessSimply(String actionType, String operationName, String operationDate,
    String operationRemark, FVoucherDTO vou, Statement st, String table_name) throws Exception {
    //add by liuzw 20120327
    String rg_code = getRgCode();
    String setYear = getSetYear();
    String create_date = "";

    int tf = vou.getIs_end();
    String tolly_flag;
    if (tf > 0)
      tolly_flag = "1";
    else if (tf < 0)
      tolly_flag = "-1";
    else
      tolly_flag = "0";

    String entity_id = vou.getVou_id();
    String status_code = "";

    if ("DISCARD".equals(actionType))
      status_code = "103";
    else if ("DELETE".equals(actionType))
      status_code = "102";

    // String remark = ParseXML.convertObjToXml(vou.toXMLData(),
    // "voucherdto");
    String remark = "";

    String create_user = "";
    String user_name = "";
    String authorieduser_name = "";

    if (SessionUtil.getUserInfoContext().getAttribute("user_name") != null
      && !SessionUtil.getUserInfoContext().getAttribute("user_name").toString().equals("")) {
      user_name = (String) SessionUtil.getUserInfoContext().getAttribute("user_name").toString();
    }

    if (SessionUtil.getUserInfoContext().getAttribute("authorieduser_name") != null
      && !SessionUtil.getUserInfoContext().getAttribute("authorieduser_name").toString().equals("")) {
      authorieduser_name = (String) SessionUtil.getUserInfoContext().getAttribute("authorieduser_name");
    }

    if (!"".equals(user_name) && !"".equals(authorieduser_name) && (!authorieduser_name.equals(user_name))) {
      create_user = authorieduser_name + "(代理：" + user_name + ")";
    } else {
      create_user = user_name;
    }

    create_date = Tools.getCurrDate();

    String create_user_id = "";
    if (SessionUtil.getUserInfoContext().getAttribute("user_id") != null
      && !SessionUtil.getUserInfoContext().getAttribute("user_id").toString().equals("")) {
      create_user_id = SessionUtil.getUserInfoContext().getAttribute("user_id").toString();
    }

    StringBuffer sql = new StringBuffer();
    // 处理作废和删除
    if ("DISCARD".equals(actionType) || "DELETE".equals(actionType)) {

      //       在current表中插入 discard 和 delete 的数据
      sql
        .append("insert into sys_wf_current_tasks (TASK_ID,WF_ID,WF_TABLE_NAME,ENTITY_ID, ")
        .append(" CURRENT_NODE_ID,NEXT_NODE_ID,ACTION_TYPE_CODE,IS_UNDO,CREATE_USER,CREATE_DATE,UNDO_USER, ")
        .append(" UNDO_DATE,OPERATION_NAME,OPERATION_DATE,OPERATION_REMARK,INIT_MONEY,RESULT_MONEY,REMARK, ")
        .append(
          " TOLLY_FLAG,BILL_TYPE_CODE,BILL_ID,RCID,CCID,SEND_MSG_DATE,AUTO_AUDIT_DATE,SET_MONTH,UPDATE_FLAG,CREATE_USER_ID,rg_code,set_year)  ")

        .append(
          " select " + (TypeOfDB.isOracle() ? "SEQ_SYS_WF_TASK_ID.Nextval" : " Nextval('SEQ_SYS_WF_TASK_ID') ")
            + ", wf_id, wf_table_name, entity_id,  ")
        .append(
          "  case when action_type_code in ('NEXT', 'BACK', 'INPUT') then next_node_id when action_type_code in ('EDIT', 'HANG') then current_node_id end  next_node_id, 0, '")
        .append(actionType).append("', 0, '").append(create_user).append("', '").append(create_date)
        .append("', null,  ").append("  null, '").append(operationName).append("', '").append(operationDate)
        .append("', '").append(operationRemark).append("', init_money, result_money, '").append(remark).append("', ")
        .append(" '").append(tolly_flag)
        .append("', bill_type_code, bill_id, rcid, ccid, send_msg_date, auto_audit_date, set_month, 0, '")
        .append(create_user_id).append("' ,'").append(rg_code).append("',").append(setYear)
        .append("  from vw_sys_wf_current_end_tasks where entity_id = '").append(entity_id)
        .append("' and wf_table_name='" + table_name + "' and rg_code='" + rg_code + "' and set_year=" + setYear);

      st.addBatch(sql.toString());

      // 将current或end tasks表中的数据挪到complete tasks表中
      sql = new StringBuffer();
      sql
        .append("insert into sys_wf_complete_tasks select b.* from sys_wf_current_tasks b ")
        .append(" where b.entity_id = '")
        .append(entity_id)
        .append(
          "' and update_flag = '1' and wf_table_name='" + table_name + "' and rg_code='" + rg_code + "' and set_year="
            + setYear);
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql
        .append("insert into sys_wf_complete_tasks select b.* from sys_wf_end_tasks b ")
        .append(" where b.entity_id = '")
        .append(entity_id)
        .append(
          "' and update_flag = '1' and wf_table_name='" + table_name + "' and rg_code='" + rg_code + "' and set_year="
            + setYear);
      st.addBatch(sql.toString());

      // 处理sys_wf_task_routing
      sql = new StringBuffer();
      sql
        .append("insert into sys_wf_task_routing ")
        .append("select v1.task_id, v2.task_id, null,")
        .append(setYear)
        .append(" as set_year,")
        .append(rg_code)
        .append(" as rg_code from ")
        .append(
          "( select t1.task_id,  case when action_type_code in ('NEXT', 'BACK', 'INPUT') then next_node_id when action_type_code in ('EDIT', 'HANG') then current_node_id end  next_node_id ")
        .append("from sys_wf_current_tasks t1 ").append("where t1.entity_id = '").append(entity_id)
        .append("' and t1.update_flag = '1' and t1.wf_table_name='" + table_name + "') v1, ")
        .append(" (select t2.task_id, t2.current_node_id ").append(" from sys_wf_current_tasks t2 ")
        .append("  where t2.entity_id = '").append(entity_id)
        .append("' and t2.update_flag = '0' and t2.wf_table_name='" + table_name + "') v2 ")
        .append("  where v1.next_node_id = v2.current_node_id ");
      st.addBatch(sql.toString());

      // 删除current和end tasks表中的当前数据
      sql = new StringBuffer();
      sql.append("delete from sys_wf_current_tasks b where b.update_flag = '1' and b.entity_id = '").append(entity_id)
        .append("' and b.wf_table_name='" + table_name + "' and b.rg_code='" + rg_code + "' and b.set_year=" + setYear);
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql.append("delete from sys_wf_end_tasks b where b.update_flag = '1' and b.entity_id = '").append(entity_id)
        .append("' and b.wf_table_name='" + table_name + "' and b.rg_code='" + rg_code + "' and b.set_year=" + setYear);
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql.append("update  sys_wf_current_tasks set update_flag = '1' ").append(" where entity_id = '")
        .append(entity_id)
        .append("' and wf_table_name='" + table_name + "' and rg_code='" + rg_code + "' and set_year=" + setYear);
      st.addBatch(sql.toString());

      // 在complete item 表中删除当前的数据
      sql = new StringBuffer();
      sql
        .append("delete from sys_wf_complete_item c where c.entity_id = '")
        .append(entity_id)
        .append("'")
        .append(" and c.set_year = ")
        .append(setYear)
        .append(" and c.rg_code = '")
        .append(rg_code)
        .append("' ")
        .append(" and exists (")
        .append(
          " select 1 from sys_wf_current_tasks ct where ct.entity_id='" + entity_id
            + "' and (c.node_id=ct.current_node_id or c.node_id=ct.next_node_id) and ct.wf_table_name='" + table_name
            + "'")
        .append(" union ")
        .append(
          " select 1 from sys_wf_complete_tasks mt where mt.entity_id='" + entity_id
            + "' and (c.node_id=mt.current_node_id or c.node_id=mt.next_node_id) and mt.wf_table_name='" + table_name
            + "'")
        .append(" union ")
        .append(
          " select 1 from sys_wf_end_tasks et where et.entity_id='" + entity_id
            + "' and (c.node_id=et.current_node_id or c.node_id=et.next_node_id) and et.wf_table_name='" + table_name
            + "'").append(")");

      st.addBatch(sql.toString());
      // 在complete item 表中插入 已作废 或 已删除 的数据
      sql = new StringBuffer();
      sql
        .append("insert into sys_wf_complete_item ")
        .append("select c.entity_id, c.bill_id, c.node_id, '")
        .append(status_code)
        .append("', c.rcid, c.ccid, ")
        .append(setYear)
        .append(" as set_year,")
        .append(rg_code)
        .append(" as rg_code ")
        .append(" from sys_wf_current_item c where c.entity_id = '")
        .append(entity_id)
        .append("'")
        .append(" and exists (")
        .append(
          " select 1 from sys_wf_current_tasks ct where ct.entity_id='" + entity_id
            + "' and (c.node_id=ct.current_node_id or c.node_id=ct.next_node_id) and ct.wf_table_name='" + table_name
            + "'")
        .append(" union ")
        .append(
          " select 1 from sys_wf_complete_tasks mt where mt.entity_id='" + entity_id
            + "' and (c.node_id=mt.current_node_id or c.node_id=mt.next_node_id) and mt.wf_table_name='" + table_name
            + "'")
        .append(" union ")
        .append(
          " select 1 from sys_wf_end_tasks et where et.entity_id='" + entity_id
            + "' and (c.node_id=et.current_node_id or c.node_id=et.next_node_id) and et.wf_table_name='" + table_name
            + "'").append(")");

      st.addBatch(sql.toString());
      // 在current item 表中删除当前的数据
      sql = new StringBuffer();
      sql
        .append("delete from sys_wf_current_item c where c.entity_id = '")
        .append(entity_id)
        .append("'")
        .append(" and c.set_year = ")
        .append(setYear)
        .append(" and c.rg_code = '")
        .append(rg_code)
        .append("' ")
        .append(" and exists (")
        .append(
          " select 1 from sys_wf_current_tasks ct where ct.entity_id='" + entity_id
            + "' and (c.node_id=ct.current_node_id or c.node_id=ct.next_node_id) and ct.wf_table_name='" + table_name
            + "'")
        .append(" union ")
        .append(
          " select 1 from sys_wf_complete_tasks mt where mt.entity_id='" + entity_id
            + "' and (c.node_id=mt.current_node_id or c.node_id=mt.next_node_id) and mt.wf_table_name='" + table_name
            + "'")
        .append(" union ")
        .append(
          " select 1 from sys_wf_end_tasks et where et.entity_id='" + entity_id
            + "' and (c.node_id=et.current_node_id or c.node_id=et.next_node_id) and et.wf_table_name='" + table_name
            + "'").append(")");

      st.addBatch(sql.toString());
    }
    // 处理撤消
    else if ("RECALL".equals(actionType)) {
      // 从complete task表上将上一步的任务挪到current task表中
      sql = new StringBuffer();
      sql.append("update sys_wf_current_tasks set is_undo = 1, undo_user = '").append(create_user)
        .append("', undo_date = '").append(create_date).append("' where entity_id = '").append(entity_id).append("'");
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql.append("update sys_wf_end_tasks set is_undo = 1 where entity_id = '").append(entity_id).append("'");
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql.append("insert into sys_wf_current_tasks ").append("select * from sys_wf_complete_tasks t ")
        .append("where t.entity_id = '").append(entity_id)
        .append("' and exists(select 1 from sys_wf_current_tasks a, sys_wf_task_routing b ")
        .append("where a.task_id = b.next_task_id and t.task_id = b.task_id and a.entity_id = '").append(entity_id)
        .append("')");
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql.append("insert into sys_wf_current_tasks ").append("select * from sys_wf_complete_tasks t ")
        .append("where t.entity_id = '").append(entity_id)
        .append("' and exists(select 1 from sys_wf_end_tasks a, sys_wf_task_routing b ")
        .append("where a.task_id = b.next_task_id and t.task_id = b.task_id and a.entity_id = '").append(entity_id)
        .append("')");
      st.addBatch(sql.toString());

      // 将current 或 end task 的任务挪到complete表中
      sql = new StringBuffer();
      sql.append("insert into sys_wf_complete_tasks ").append("select * from sys_wf_current_tasks  ")
        .append("where entity_id = '").append(entity_id).append("' and is_undo = 1");
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql.append("insert into sys_wf_complete_tasks ").append("select * from sys_wf_end_tasks  ")
        .append("where entity_id = '").append(entity_id).append("' and is_undo = 1");
      st.addBatch(sql.toString());
      // 删除complete表中的上一步任务
      sql = new StringBuffer();
      sql.append("delete from sys_wf_complete_tasks t where entity_id = '").append(entity_id).append("' and exists( ")
        .append("select 1 from sys_wf_current_tasks a, sys_wf_task_routing b ")
        .append("where a.task_id = b.next_task_id and t.task_id = b.task_id and a.entity_id = '").append(entity_id)
        .append("' and is_undo = 1)");
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql.append("delete from sys_wf_complete_tasks t where entity_id = '").append(entity_id).append("' and exists( ")
        .append("select 1 from sys_wf_end_tasks a, sys_wf_task_routing b ")
        .append("where a.task_id = b.next_task_id and t.task_id = b.task_id and a.entity_id = '").append(entity_id)
        .append("' and is_undo = 1)");
      st.addBatch(sql.toString());
      // 删除current 或 end表中的当前任务
      sql = new StringBuffer();
      sql.append("delete from sys_wf_current_tasks where entity_id = '").append(entity_id).append("' and is_undo = 1");
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql.append("delete from sys_wf_end_tasks where entity_id = '").append(entity_id).append("' and is_undo = 1");
      st.addBatch(sql.toString());
      // 在current item表中插入对应的任务
      sql = new StringBuffer();
      sql.append("delete from sys_wf_current_item where entity_id = '").append(entity_id).append("'");
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql
        .append(
          "insert into sys_wf_current_item select entity_id, bill_id,  case when action_type_code in ('NEXT', 'BACK', 'INPUT') then next_node_id when action_type_code in ('EDIT', 'HANG') then current_node_id end  next_node_id, '001', rcid, ccid ")
        .append("from sys_wf_current_tasks where entity_id = '").append(entity_id).append("'");
      st.addBatch(sql.toString());
      // 在complete item表中插入对应的任务
      sql = new StringBuffer();
      sql.append("delete from sys_wf_complete_item where entity_id = '").append(entity_id).append("'");
      st.addBatch(sql.toString());

      sql = new StringBuffer();
      sql
        .append(
          "insert into sys_wf_complete_item select entity_id, bill_id, case when action_type_code in ('NEXT', 'BACK', 'INPUT') then next_node_id when action_type_code in ('EDIT', 'HANG') then current_node_id end  next_node_id, '002', rcid, ccid ")
        .append(
          "from sys_wf_complete_tasks e where exists(select 1 from sys_wf_current_tasks w, sys_wf_task_routing q ")
        .append("where w.task_id = q.next_task_id and q.task_id = e.task_id and w.entity_id = '").append(entity_id)
        .append("' ) and entity_id = '").append(entity_id).append("'");
      st.addBatch(sql.toString());
    }

    return vou;
  }

  /**
   * 刷新工作流5个表中的rcid,ccid
   * 
   * @param tableName 业务数据表名
   * @param entityId 业务数据唯一标识
   * @param newRcid 新的rcid
   * @param newCcid 新的ccid
   * @throws Exception
   */
  public void refreshRcidAndCcid(String tableName, String entityId, String newRcid, String newCcid) throws Exception {
    String rg_code = getRgCode();
    String setYear = getSetYear();
    //通过 tableName,entityId 找到此3个表的相关工作流数据,并将其rcid/ccid 进行刷新. 
    StringBuffer sql_cur_task = new StringBuffer();
    StringBuffer sql_com_task = new StringBuffer();
    StringBuffer sql_end_task = new StringBuffer();

    //sys_wf_current_tasks
    sql_cur_task.append("update sys_wf_current_tasks c set c.rcid=");
    sql_cur_task.append(newRcid + ",c.ccid=" + newCcid);
    sql_cur_task.append(" where c.entity_id='" + entityId + "' and c.wf_table_name='" + tableName + "' and c.rg_code='"
      + rg_code + "' and c.set_year=" + setYear);

    //sys_wf_complete_tasks
    sql_com_task.append("update sys_wf_complete_tasks c set c.rcid=");
    sql_com_task.append(newRcid + ",c.ccid=" + newCcid);
    sql_com_task.append(" where c.entity_id='" + entityId + "' and c.wf_table_name='" + tableName + "' and c.rg_code='"
      + rg_code + "' and c.set_year=" + setYear);

    //sys_wf_end_tasks      
    sql_end_task.append("update sys_wf_end_tasks c set c.rcid=");
    sql_end_task.append(newRcid + ",c.ccid=" + newCcid);
    sql_end_task.append(" where c.entity_id='" + entityId + "' and c.wf_table_name='" + tableName + "' and c.rg_code='"
      + rg_code + "' and c.set_year=" + setYear);

    //2. 刷新如下2个工作流数据库表:

    // 通过 tableName,entityId  在 sys_wf_current_item 查找到相应的数据,并且关联sys_wf_nodes找到对应的节点 表名;
    StringBuffer sql_cur_item = new StringBuffer();
    StringBuffer sql_com_item = new StringBuffer();
    //sys_wf_current_item
    sql_cur_item.append("update sys_wf_current_item i set i.rcid=");
    sql_cur_item.append(newRcid + ",i.ccid=" + newCcid);
    sql_cur_item.append(" where i.entity_id='" + entityId
      + "' and exists(select 1 from sys_wf_nodes n where n.node_id=i.node_id and n.wf_table_name='" + tableName + "')");

    //sys_wf_complete_item
    sql_com_item.append("update sys_wf_complete_item i set i.rcid=");
    sql_com_item.append(newRcid + ",i.ccid=" + newCcid);
    sql_com_item.append(" where i.entity_id='" + entityId
      + "' and exists(select 1 from sys_wf_nodes n where n.node_id=i.node_id and n.wf_table_name='" + tableName + "')");

    //dao执行
    dao.executeBySql(sql_cur_task.toString());
    dao.executeBySql(sql_com_task.toString());
    dao.executeBySql(sql_end_task.toString());
    dao.executeBySql(sql_cur_item.toString());
    dao.executeBySql(sql_com_item.toString());
  }

  /**
   * 通用处理操作（对批量数据,不用传TABLE_NAME）。
   * 
   * @param menuid      菜单ID
   * @param roleid      角色ID
   * @param actiontype    操作类型CODE
   * @param operationname   操作名称
   * @param operationdate   操作时间
   * @param operationremark   操作意见
   * @param inputFVoucherDtos 业务数据DTO列表
   * @param auto_tolly_flag   是否通过工作流自动记账
   * @param auto_create_ccid  是否通过工作流生成CCID
   * @param auto_create_rcid  是否通过工作流生成RCID
   * @return List（内部含Object)
   * @throws FAppException 错误信息
   */
  public List doBatchBackToInputProcessWithNoTableName(String menuid, String roleid, String actiontypeid,
    String operationname, String operationdate, String operationremark, List inputFVoucherDtos,
    boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid) throws FAppException {

    Session session = dao.getSession();
    Connection conn = session.connection();
    try {
      List return_list = new ArrayList();
      if (inputFVoucherDtos == null || inputFVoucherDtos.size() == 0) {
        throw new Exception("需要传入inputFVoucherDtos!");
      }
      // 批处理SQL
      StringBuffer insertCurrentTaskSql = new StringBuffer();
      insertCurrentTaskSql
        .append("insert into sys_wf_current_tasks")
        .append(" (TASK_ID,WF_ID,WF_TABLE_NAME,ENTITY_ID,")
        .append("CURRENT_NODE_ID,NEXT_NODE_ID,ACTION_TYPE_CODE,IS_UNDO,CREATE_USER,CREATE_DATE,UNDO_USER,")
        .append("UNDO_DATE,OPERATION_NAME,OPERATION_DATE,OPERATION_REMARK,INIT_MONEY,RESULT_MONEY,REMARK,")
        .append(
          "TOLLY_FLAG,BILL_TYPE_CODE,BILL_ID,RCID,CCID,SEND_MSG_DATE,AUTO_AUDIT_DATE,SET_MONTH,UPDATE_FLAG,CREATE_USER_ID,RG_CODE,SET_YEAR) ")
        .append("values( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      PreparedStatement insertCurrentTaskPsmt = conn.prepareStatement(insertCurrentTaskSql.toString());
      Statement setRoutingPsmt = conn.createStatement();

      String detail_table_name = "";
      String bill_table_name = "";
      FVoucherDTO inputFVoucherDto = ((FVoucherDTO) inputFVoucherDtos.get(0));

      if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
        String billType_code = (String) (inputFVoucherDto.getBilltype_code());
        if (billType_code == null || billType_code.equals("")) {
          throw new Exception("没有传入billtype_code!");
        }
        String table_name = UtilCache.getTableNameByBillTypeCode(billType_code);
        if (table_name != null && !table_name.equals("")) {
          detail_table_name = table_name;
        } else {
          detail_table_name = ((FBillTypeDTO) billtype.getBillTypeByCode(billType_code)).getTable_name();
          UtilCache.putTableNameByBillTypeCode(billType_code, detail_table_name);
        }
      } else {

        String billType_code = (String) (((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code());
        if (billType_code == null || billType_code.equals("")) {
          throw new Exception("没有传入billtype_code!");
        }
        String table_name = UtilCache.getTableNameByBillTypeCode(billType_code);
        if (table_name != null && !table_name.equals("")) {
          detail_table_name = table_name;
        } else {
          detail_table_name = ((FBillTypeDTO) billtype.getBillTypeByCode(billType_code)).getTable_name();
          UtilCache.putTableNameByBillTypeCode(billType_code, detail_table_name);
        }

      }
      bill_table_name = detail_table_name;

      //修改批量产生ccid start
      if (auto_create_ccid) {
        List tmp_dto = new ArrayList();
        Iterator itr = inputFVoucherDtos.iterator();
        Iterator itr_detail;
        while (itr.hasNext()) {
          FVoucherDTO inputFVoucherDtoDto = (FVoucherDTO) itr.next();
          // 设置coa_id
          inputFVoucherDtoDto.setCoa_id(this.getCOAIDByBillTypeCode(inputFVoucherDtoDto.getBilltype_code()));

          tmp_dto.add(inputFVoucherDtoDto);
          if (inputFVoucherDtoDto.getDetails() != null && inputFVoucherDtoDto.getDetails().size() > 0) {
            itr_detail = inputFVoucherDtoDto.getDetails().iterator();
            while (itr_detail.hasNext()) {
              FVoucherDTO vou = (FVoucherDTO) itr_detail.next();
              // 给明细设置coa_id
              vou.setCoa_id(this.getCOAIDByBillTypeCode(vou.getBilltype_code()));
              tmp_dto.add(vou);
            }
          }
        }
        // 批量产生ccid
        icoaService.getCcidBatch(tmp_dto);
      }
      // 自动生成RCID(注:可应用于录入及要素变更等情况)
      if (auto_create_rcid) {
        for (int p = 0; p < inputFVoucherDtos.size(); p++) {
          FVoucherDTO inputFVoucherDto1 = (FVoucherDTO) inputFVoucherDtos.get(p);
          String rcid1 = idataright.getRcid(inputFVoucherDto1.toXMLData());
          if (rcid1 != null && !rcid1.equals("")) {
            inputFVoucherDto1.setRcid(rcid1);
          }

          if (inputFVoucherDto1.getDetails() != null && inputFVoucherDto1.getDetails().size() > 0) {
            for (int q = 0; q < inputFVoucherDto1.getDetails().size(); q++) {
              FVoucherDTO inputFVoucherDto2 = (FVoucherDTO) inputFVoucherDto1.getDetails().get(q);
              String rcid2 = idataright.getRcid(inputFVoucherDto2.toXMLData());
              if (rcid2 != null && !rcid2.equals("")) {
                inputFVoucherDto2.setRcid(rcid2);
              }
            }
          }
        }

      }
      String billId = null;
      if (auto_tolly_flag) {
        // 根据记账类型不同对其处理方式不同
        inputFVoucherDtos = this.doTolly(menuid, roleid, actiontypeid, bill_table_name, detail_table_name,
          inputFVoucherDtos);
      }

      // 循环调用
      for (int i = 0; i < inputFVoucherDtos.size(); i++) {
        inputFVoucherDto = new FVoucherDTO();
        inputFVoucherDto = (FVoucherDTO) inputFVoucherDtos.get(i);

        FVoucherDTO returnFVoucherDto = doBackToInputProcess(insertCurrentTaskPsmt, setRoutingPsmt, null, menuid,
          roleid, actiontypeid, operationname, operationdate, operationremark, bill_table_name, detail_table_name,
          inputFVoucherDto, auto_tolly_flag, auto_create_ccid, auto_create_rcid, false, null,
          String.valueOf(inputFVoucherDto.getIs_end()), billId);
        if (returnFVoucherDto != null) {
          return_list.add(returnFVoucherDto);
        }
      }

      insertCurrentTaskPsmt.executeBatch();
      setRoutingPsmt.executeBatch();

      return return_list;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    } finally {
      dao.closeSession(session);
    }

  }

  private FVoucherDTO doBackToInputProcess(PreparedStatement insertCurrentTaskPsmt, Statement setRoutingPsmt,
    String old_current_node_id, String menuid, String roleid, String actiontypeid, String operationname,
    String operationdate, String operationremark, String bill_table_name, String detail_table_name,
    FVoucherDTO inputFVoucherDto, boolean auto_tolly_flag, boolean auto_create_ccid, boolean auto_create_rcid,
    boolean isForced2Execute, List currentList, String tolly_flag, String billId) throws FAppException {
    try {
      if (actiontypeid == null || actiontypeid.equals("")) {
        throw new Exception("需要传入actiontypeid!");
      }
      // 资金监控
      IInspectService inspectService = null;
      try {
        inspectService = (IInspectService) SessionUtil.getServerBean("sys.inspectService");
      } catch (Exception e) {
      }

      // 如果访问不到监控类包，则不能调用其接口。
      if (inspectService != null) {
        List nodeInspectRules = null;
        if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
          nodeInspectRules = this.getInspectRules(old_current_node_id, menuid, roleid, actiontypeid, bill_table_name,
            detail_table_name, inputFVoucherDto);
        } else {
          nodeInspectRules = this.getInspectRules(old_current_node_id, menuid, roleid, actiontypeid, bill_table_name,
            detail_table_name, (FVoucherDTO) inputFVoucherDto.getDetails().get(0));
        }
        // 无论能否找到规则列表，都调用监控接口
        String inspectNodeId = null;
        if (nodeInspectRules != null && nodeInspectRules.size() > 0) {
          inspectNodeId = (String) ((XMLData) nodeInspectRules.get(0)).get("node_id");
        } else {
          if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
            inspectNodeId = getNextNodeId(old_current_node_id, menuid, roleid, actiontypeid, inputFVoucherDto,
              isForced2Execute);
          } else {
            inspectNodeId = getNextNodeId(old_current_node_id, menuid, roleid, actiontypeid,
              (FVoucherDTO) inputFVoucherDto.getDetails().get(0), isForced2Execute);
          }
        }
        String rtnMsg = inspectService.inspectInstance(getWfIdByNodeId(inspectNodeId), detail_table_name,
          inspectNodeId, null, actiontypeid, operationname, operationdate, operationremark, nodeInspectRules,
          inputFVoucherDto, menuid, roleid);
        inputFVoucherDto.setWarning(rtnMsg);
      }

      if (detail_table_name == null || detail_table_name.equals("")) {
        throw new Exception("需要传入detail_table_name!");
      }

      // 如果没有明细数据，则直接调工作流
      if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {

        billId = inputFVoucherDto.getVou_bill_id();
        doBackToInputProcessSimply(insertCurrentTaskPsmt, setRoutingPsmt, old_current_node_id, menuid, roleid,
          actiontypeid, operationname, operationdate, operationremark, detail_table_name, inputFVoucherDto, "detail",
          null, tolly_flag, billId, isForced2Execute, currentList);

        return inputFVoucherDto;
      } else {

        billId = inputFVoucherDto.getVou_id();
        for (int i = 0; i < inputFVoucherDto.getDetails().size(); i++) {
          FVoucherDTO detailDto = new FVoucherDTO();
          detailDto = (FVoucherDTO) inputFVoucherDto.getDetails().get(i);
          doBackToInputProcessSimply(insertCurrentTaskPsmt, setRoutingPsmt, old_current_node_id, menuid, roleid,
            actiontypeid, operationname, operationdate, operationremark, detail_table_name, detailDto, "detail", null,
            tolly_flag, billId, isForced2Execute, currentList);
        }

        return inputFVoucherDto;
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }

  }

  private synchronized List doBackToInputProcessSimply(PreparedStatement insertCurrentTaskPsmt,
    Statement setRoutingPsmt, String old_current_node_id, String menuid, String roleid, String actiontypeid,
    String operationname, String operationdate, String operationremark, String tableName, FVoucherDTO inputFVoucherDto,
    String vou_type, List detail_wf_info, String tolly_flag, String billId, boolean isForced2Execute, List current_list)
    throws FAppException {
    String rg_code = getRgCode();
    String setYear = getSetYear();

    List return_list = new ArrayList();
    String create_user = "";
    String create_user_id = "";
    try {
      String user_name = "";
      String authorieduser_name = "";
      String user_id = "";
      String authorieduser_id = "";

      if (SessionUtil.getUserInfoContext().getAttribute("user_name") != null
        && !SessionUtil.getUserInfoContext().getAttribute("user_name").toString().equals("")) {
        user_name = (String) SessionUtil.getUserInfoContext().getAttribute("user_name").toString();
      }

      if (SessionUtil.getUserInfoContext().getAttribute("authorieduser_name") != null
        && !SessionUtil.getUserInfoContext().getAttribute("authorieduser_name").toString().equals("")) {
        authorieduser_name = (String) SessionUtil.getUserInfoContext().getAttribute("authorieduser_name");
      }

      if (SessionUtil.getUserInfoContext().getAttribute("user_id") != null
        && !SessionUtil.getUserInfoContext().getAttribute("user_id").toString().equals("")) {
        user_id = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");
      }

      if (SessionUtil.getUserInfoContext().getAttribute("authorieduser_id") != null
        && !SessionUtil.getUserInfoContext().getAttribute("authorieduser_id").toString().equals("")) {
        authorieduser_id = (String) SessionUtil.getUserInfoContext().getAttribute("authorieduser_id");
      }

      if (!"".equals(user_name) && !"".equals(authorieduser_name) && (!authorieduser_name.equals(user_name))) {
        create_user = authorieduser_name + "(代理：" + user_name + ")";
        create_user_id = authorieduser_id;
      } else {
        create_user_id = user_id;
        create_user = user_name;
      }

      String entityId = inputFVoucherDto.getVou_id();
      if (entityId == null || entityId.equals("")) {
        throw new Exception("需要传入vou_id!");
      }

      String bill_type_code = "";
      String operationName = "";
      String operationDate = "";
      String operationRemark = "";
      String initMoney = null;
      String resultMoney = "null";
      String remark = "";
      XMLData rowData = new XMLData();
      rowData = inputFVoucherDto.toXMLData();

      if (inputFVoucherDto.getBilltype_code() != null) {
        bill_type_code = inputFVoucherDto.getBilltype_code();
      }
      if (operationname != null && !operationname.equals("")) {
        operationName = operationname;
      }
      if (operationremark != null && !operationremark.equals("")) {
        operationRemark = operationremark;
      }
      if (operationdate != null && !operationdate.equals("")) {
        operationDate = operationdate;
      }
      if (inputFVoucherDto.getVou_money() != null && !inputFVoucherDto.getVou_money().equals("")) {
        resultMoney = inputFVoucherDto.getVou_money();
      }

      remark = ParseXML.convertObjToXml(inputFVoucherDto.toXMLData(), "voucherdto");

      if (actiontypeid.equals("BACK")) {
        List tempList4TempTable = new ArrayList();
        //为了防止并发前提下 取得相同rs 导致map为同一对象 写入taskId的时候 多条线程有可能会覆盖 造成不同流程取得相同的taskId
        //一节点分多流程 声明变量会有问题 还用以前map方式 用synchronized控制并发
        StringBuffer select_sql = new StringBuffer();
        try {
          if (!isForced2Execute) {
            if (menuid == null || menuid.equals("")) {
              throw new Exception("需要传入menuid!");
            }
            if (roleid == null || roleid.equals("")) {
              throw new Exception("需要传入roleid!");
            }
          }

          List rs = null;
          String wf_id = "";
          String current_node_id = "0";

          if (current_list != null && current_list.size() == 1) {
            Map map = (Map) rs.get(0);
            wf_id = (String) map.get("wf_id");
            current_node_id = map.get("current_node_id").toString();

          }

          select_sql = new StringBuffer();
          select_sql
            .append(
              " select b.wf_id, "
                + (TypeOfDB.isOracle() ? " decode(b.action_type_code,'NEXT',b.next_node_id, 'INPUT',b.next_node_id,'BACK',b.next_node_id, b.current_node_id)"
                  : " case b.action_type_code when 'NEXT' then b.next_node_id when 'INPUT' then b.next_node_id when 'BACK' then b.next_node_id else b.current_node_id end ")
                + " as current_node_id,d.next_node_id,b.action_type_code,")
            .append(" '#' as condition_id from sys_wf_current_tasks b,sys_wf_complete_tasks d")
            .append(
              " where b.rg_code=d.rg_code and b.set_year = d.set_year and b.rg_code = ? and b.set_year = ? and b.entity_id=d.entity_id and b.wf_table_name=d.wf_table_name")
            .append(" and d.action_type_code='INPUT' and b.entity_id=? and b.wf_table_name=?");
          rs = dao.findBySql(select_sql.toString(), new Object[] { rg_code, setYear, entityId, tableName });
          //查找不到下一节点数据
          if (rs.size() == 0) {
            // 如果是明细，则报错
            throw new Exception("该数据不存在，或者该角色没有此权限!");
          } else if (rs.size() > 1) {
            throw new Exception("该数据存在于多个分支流程，不能直接退回到录入人!");
          } else {
            // 查找到数据后，保存下一节点信息
            Map map = (Map) rs.get(0);
            wf_id = (String) map.get("wf_id");
            current_node_id = map.get("current_node_id").toString();
          }

          List checkList = null;
          if (actiontypeid.equals("BACK")) {// 退回操作时
            StringBuffer checkSql = new StringBuffer();
            checkSql.append(" select * from sys_Wf_nodes wn where  exists");
            checkSql.append(" (select 1 from sys_wf_node_conditions wc where ");
            checkSql.append(" wc.node_id = ? AND wc.ROUTING_TYPE = '002' and");
            checkSql.append(" wn.node_id=wc.next_node_id)");
            checkList = dao.findBySql(checkSql.toString(), new Object[] { current_node_id });
          }

          // 判断是否能走入下一节点
          if (rs.size() == 0) {
            throw new Exception("该数据没有对应下一流程节点!");
          } else {
            // 将菜单、角色作为过滤条件
            rowData.put("module_id", menuid);
            rowData.put("role_id", roleid);
            if (!tableName.substring(0, 4).equalsIgnoreCase("SAL")) {
              boolean flag = false;
              Iterator it = rs.iterator();
              while (it.hasNext()) {
                Map map = (Map) it.next();
                boolean can_go_next = false;

                // NEXT时要判断表达式
                if (map.get("condition_id").equals("#")) {
                  can_go_next = true;
                  flag = true;
                } else if (getValidWfNode((String) map.get("condition_id"), rowData)) {
                  can_go_next = true;
                  flag = true;
                }
                if (can_go_next) {
                  String tempTaskId = getNextTaskIdBySequence();
                  map.put("taskId", tempTaskId);
                  // 批处理语句参数赋值
                  setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, tempTaskId, map.get("wf_id").toString(),
                    tableName, entityId, current_node_id, map.get("next_node_id").toString(), actiontypeid, 0,
                    create_user, "", "", operationName, operationDate, operationRemark, initMoney, resultMoney, remark,
                    tolly_flag, bill_type_code, billId, inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid(),
                    inputFVoucherDto.getSet_month(), 0, create_user_id);

                  XMLData return_xmldata = new XMLData();
                  return_xmldata.put("wf_id", wf_id);
                  return_xmldata.put("current_node_id", current_node_id);
                  return_xmldata.put("next_node_id", map.get("next_node_id").toString());
                  return_list.add(return_xmldata);
                  tempList4TempTable.add(map);
                }
              }
              if (!flag) {
                throw new Exception("流程节点不满足表达式，请检查！");
              }
            } else {
              Iterator it = rs.iterator();
              StringBuffer nodes = new StringBuffer(" (");
              List can_go_list = new ArrayList();
              while (it.hasNext()) {
                Map map = (Map) it.next();

                // NEXT时要判断表达式
                if (map.get("condition_id").equals("#")) {
                  // flag = true;
                  can_go_list.add(map);
                  nodes.append(map.get("next_node_id").toString() + ",");
                } else if (getValidWfNode((String) map.get("condition_id"), rowData)) {
                  // flag = true;
                  can_go_list.add(map);
                  nodes.append(map.get("next_node_id").toString() + ",");
                }
              }
              nodes.deleteCharAt(nodes.length() - 1).append(")");
              if (actiontypeid.equals("BACK")) {
                if (can_go_list.size() > 0 && checkList != null) {
                  for (int i = 0; i < can_go_list.size(); ++i) {
                    XMLData xd = (XMLData) can_go_list.get(i);
                    String next_node_id = xd.get("next_node_id").toString();
                    for (int j = 0; j < checkList.size(); ++j) {
                      XMLData xd2 = (XMLData) checkList.get(j);
                      if (next_node_id.equals(xd2.get("node_id")) && "001".equals(xd2.get("node_type"))) {
                        throw new Exception("不能在开始节点进行退回操作!");
                      }
                    }
                  }
                }
              }

              // NEXT时要判断表达式，BACK时不需要判断表达式
              Iterator can_go_it = can_go_list.iterator();

              while (can_go_it.hasNext()) {
                Map map = (Map) can_go_it.next();
                //两条流程都满足的情况，判断是否是已经走过的流程 add by beaf
                if (can_go_list.size() > 1) {
                  StringBuffer otherConditonSql = new StringBuffer();
                  otherConditonSql.append(" select t.action_type_code,count(1) as num ");
                  otherConditonSql
                    .append(" from (select entity_id,action_type_code,current_node_id ,next_node_id,is_undo");
                  otherConditonSql.append(" from Sys_Wf_Complete_Tasks union all ");
                  otherConditonSql.append("");
                  otherConditonSql.append(" select entity_id,action_type_code,current_node_id ,next_node_id,is_undo");
                  otherConditonSql
                    .append(" from Sys_Wf_Current_Tasks union all select entity_id,action_type_code,current_node_id ,next_node_id,is_undo");
                  otherConditonSql.append(" from Sys_Wf_End_Tasks )t");
                  otherConditonSql.append("  where t.entity_id =?");
                  otherConditonSql.append(" and ((t.current_node_id = ? and t.next_node_id = ?) or");
                  otherConditonSql
                    .append(" (t.current_node_id = ? and t.next_node_id = ?)) and t.is_undo=0 and t.rg_code=? and t.set_year=? group by t.action_type_code");
                  List actionTypeList = dao.findBySql(otherConditonSql.toString(), new Object[] { entityId,
                    current_node_id, map.get("next_node_id"), map.get("next_node_id"), current_node_id, rg_code,
                    setYear });
                  if (null != actionTypeList && actionTypeList.size() != 0) {
                    if (actionTypeList.size() == 1) {
                      continue;
                    } else {
                      Map actionTypeNumMap1 = (Map) actionTypeList.get(0);
                      Map actionTypeNumMap2 = (Map) actionTypeList.get(1);
                      String num1 = (String) actionTypeNumMap1.get("num");
                      String num2 = (String) actionTypeNumMap2.get("num");
                      if (!num1.equals(num2))
                        continue;
                    }
                  }
                }

                //为了防止并发前提下 取得相同rs 导致map为同一对象 写入taskId的时候 多条线程有可能会覆盖 造成不同流程取得相同的taskId
                //注掉以前的map.put 将下面方法中直接传入currentTaskId 以前是通过map.get("taskId")
                //一节点分多流程 声明变量会有问题 还用以前map方式 用synchronized控制并发
                String tempTaskId = getNextTaskIdBySequence();
                map.put("taskId", tempTaskId);
                // 批处理语句参数赋值
                setValues4InsertCurrentTaskPsmt(insertCurrentTaskPsmt, map.get("taskId").toString(), map.get("wf_id")
                  .toString(), tableName, entityId, current_node_id, map.get("next_node_id").toString(), actiontypeid,
                  0, create_user, "", "", operationName, operationDate, operationRemark, initMoney, resultMoney,
                  remark, tolly_flag, bill_type_code, billId, inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid(),
                  inputFVoucherDto.getSet_month(), 0, create_user_id);

                XMLData return_xmldata = new XMLData();
                return_xmldata.put("wf_id", wf_id);
                return_xmldata.put("current_node_id", current_node_id);
                return_xmldata.put("next_node_id", map.get("next_node_id").toString());
                return_list.add(return_xmldata);
                tempList4TempTable.add(map);
              }

              if (null == can_go_list || can_go_list.size() == 0) {
                throw new Exception("流程节点不满足表达式，请检查！");
              }

            }
          }

          setRoutingStmt(setRoutingPsmt, wf_id, entityId, current_node_id, tableName);

          try {
            Iterator it = tempList4TempTable.iterator();

            while (it.hasNext()) {

              Map map = (Map) it.next();
              saveOptionCurrentAndComleteTableNew(setRoutingPsmt, map.get("taskId").toString(), map.get("wf_id")
                .toString(), entityId, current_node_id, map.get("next_node_id").toString(), actiontypeid, billId,
                inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid(), inputFVoucherDto);

            }
          } catch (Exception e) {
            throw e;
          }
        } catch (FAppException fae) {
          throw fae;
        } catch (Exception e) {
          throw new FAppException(e);
        }

      }

      return return_list;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  public byte[] getCompressWfLog(String TableName, String EntityId) throws FAppException {
    //     建立字节数组输出流
    ByteArrayOutputStream byteOS = null;
    // 建立gzip压缩输出流
    GZIPOutputStream gzout = null;
    // 建立对象序列化输出流
    ObjectOutputStream out = null;
    // 建立字节数组输出流
    byteOS = new ByteArrayOutputStream();
    // 建立gzip压缩输出流
    try {
      gzout = new GZIPOutputStream(byteOS);
      out = new ObjectOutputStream(gzout);
      out.writeObject(this.getWfLogDTO(TableName, EntityId));
      out.flush();
      out.close();
      gzout.close();
      byteOS.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return byteOS.toByteArray();
  }

  /**
   * 查询某菜单的曾经办事务提醒。
   * 
   * @param UserId
   *            用户ID
   * @param RoleId
   *            角色ID
   * @param menuId
   *            菜单ID
   * @return List(内部含FWFLogDTO)
   * @throws FAppException
   *             错误信息
   */
  public FWFLogDTO getModule008StatInfo(String UserId, String RoleId, String menuId) throws FAppException {
    String switch_module_task = (String) SessionUtil.getParaMap().get("switch_module_task");
    FWFLogDTO log = new FWFLogDTO();
    if ("0".equals(switch_module_task)) {
      return log;
    } else {
      return getModule008StatInfo(UserId, RoleId, menuId, null);
    }
  }

  private FWFLogDTO getModule008StatInfo(String UserId, String RoleId, String menuId, String OperateType)
    throws FAppException {
    final String STATUS_CODE = "008";
    FRightSqlDTO data_right_sql;
    try {
      data_right_sql = idataright.getSqlBusiRightByJoin(UserId, RoleId, "task");
    } catch (Exception e) {
      throw new FAppException(e);
    }

    List result_008 = null;
    StringBuffer return_sql_008 = new StringBuffer();
    return_sql_008
      .append(
        "select count("
          + (TypeOfDB.isOracle() ? " nvl(" : " ifnull(")
          + "entity_id,1)) as num,status_code from (select "
          + (TypeOfDB.isOracle() ? "decode(task.bill_id, null, task.entity_id, task.bill_id)"
            : " case when task.bill_id is null then task.entity_id else task.bill_id end ")
          + " entity_id,task.status_code")
      .append(
        " from vw_sys_wf_stride_008 task , sys_wf_role_node role , sys_wf_module_node module"
          + data_right_sql.getTable_clause())
      .append(
        " where task.node_id = role.node_id and task.node_id = module.node_id and role.role_id = ?  and module.module_id = ? ")
      .append(
        data_right_sql.getWhere_clause()
          + "  group by task.status_code, "
          + (TypeOfDB.isOracle() ? "decode(task.bill_id, null, task.entity_id, task.bill_id)"
            : "case when task.bill_id is null then task.entity_id else task.bill_id end ") + ") group by status_code");

    if (data_right_sql.isOrg_right_flag() && data_right_sql.isData_right_flag()) {
      result_008 = dao.findBySql(return_sql_008.toString(), new Object[] { RoleId, menuId, UserId, UserId, RoleId });
    }
    if (!data_right_sql.isOrg_right_flag() && data_right_sql.isData_right_flag()) {
      result_008 = dao.findBySql(return_sql_008.toString(), new Object[] { RoleId, menuId, UserId, RoleId });
    }
    if (data_right_sql.isOrg_right_flag() && !data_right_sql.isData_right_flag()) {
      result_008 = dao.findBySql(return_sql_008.toString(), new Object[] { RoleId, menuId, UserId });
    }
    if (!data_right_sql.isOrg_right_flag() && !data_right_sql.isData_right_flag()) {
      result_008 = dao.findBySql(return_sql_008.toString(), new Object[] { RoleId, menuId });
    }

    String statusName = UtilCache.getStatusCode4Name(menuId + STATUS_CODE);
    if (null == statusName || "".equals(statusName)) {
      List list = null;
      String sql4Code = "select ss.status_name, ss.status_code, sms.display_title from sys_status ss, sys_module sm, sys_module_status sms"
        + " where ss.status_id = sms.status_id"
        + " and sms.module_id = sm.module_id"
        + " and ss.status_code = ?"
        + " and sm.module_id = ?";
      list = dao.findBySql(sql4Code, new Object[] { STATUS_CODE, menuId });
      if (null != list && list.size() > 0) {
        XMLData tempData = (XMLData) list.get(0);
        if (tempData.get("display_title") != null && !"".equals(tempData.get("display_title"))) {
          UtilCache.putStatusCode4Name(menuId + STATUS_CODE, tempData.get("display_title").toString());
        } else {
          UtilCache.putStatusCode4Name(menuId + STATUS_CODE, tempData.get("status_name").toString());
        }
        statusName = UtilCache.getStatusCode4Name(menuId + STATUS_CODE);
      }
    }
    FWFLogDTO log = new FWFLogDTO();
    log.setStatus_code(STATUS_CODE);
    log.setStatus_name(statusName);
    if (result_008.size() > 0) {
      log.setNum(((Map) result_008.get(0)).get("num").toString());
    } else {
      log.setNum("0");
    }
    return log;
  }

  /**
   * 带有权限的任务查询语句。传明细表表名
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param menuID-----------菜单ID
   * @param TableAlias---------主表的别名
   * @return SQL语句
   * @throws Exception---------错误信息
   */
  public String getTask008WithRightBySqlWithDetailTable(String UserId, String RoleId, String menuId, String detailTable)
    throws FAppException {
    String return_sql = "";
    try {
      // String data_right_sql = idataright.getSqlBusiRight(UserId, RoleId, "task");
      String data_right_sql = "";
      return_sql = return_sql
        + getTasks008BySqlBySingleTypeSimply(data_right_sql, UserId, RoleId, menuId, detailTable, true);

      if (return_sql.trim().equals("")) {
        return " and 1 = 0 ";
      }
      return " and (" + return_sql + ") ";
      /*} catch (FAppException fae) {
        throw fae;*/
    } catch (Exception e) {
      throw new FAppException(e);
    }
  }

  /**
   * 获取工作流权限，可设置是否关心外部关联表
   * @param data_right_sql
   * @param RoleId
   * @param menuId
   * @param OperateType
   * @param TableAlias
   * @param is1stExistClauseFlag
   * @author ydz
   * @return
   */
  private String getTasks008BySqlBySingleTypeSimply(String data_right_sql, String userId, String RoleId, String menuId,
    String TableAlias, boolean is1stExistClauseFlag) {

    StringBuffer return_sql = new StringBuffer();
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();

    StringBuffer sql = new StringBuffer();
    sql.append("select distinct 'a' as wf_table_name, c.id_column_name,")
      .append(" c.outer_table_name,c.outer_column_name,c.relation_column_name,c.gather_flag ")
      .append(" from sys_wf_module_node").append(Tools.addDbLink()).append(" a,sys_wf_role_node")
      .append(Tools.addDbLink()).append(" b ,sys_wf_nodes").append(Tools.addDbLink()).append(" c")
      .append(" where a.node_id=b.node_id and b.node_id=c.node_id  ")
      .append(" and b.role_id=? and a.module_id=? and c.outer_table_name is null ").append(" union ")
      .append(" select distinct c.wf_table_name,c.id_column_name,")
      .append(" c.outer_table_name,c.outer_column_name,c.relation_column_name,c.gather_flag ")
      .append(" from sys_wf_module_node").append(Tools.addDbLink()).append(" a,sys_wf_role_node")
      .append(Tools.addDbLink()).append(" b ,sys_wf_nodes").append(Tools.addDbLink()).append(" c")
      .append(" where a.node_id=b.node_id and b.node_id=c.node_id  ")
      .append(" and b.role_id=? and a.module_id=? and c.outer_table_name is not null ");
    List result = new ArrayList();

    result = dao.findBySql(sql.toString(), new Object[] { RoleId, menuId, RoleId, menuId });
    String nodeId = UtilCache.getNodeIdByModuleRole(menuId + RoleId);
    if (StringUtil.isNull(nodeId)) {
      return " 1=0 ";
    }
    return_sql
      .append(" alias.id not in (select "
        + (TypeOfDB.isOracle() ? "nvl(" : "ifnull(")
        + "bill_id,entity_id) from sys_wf_current_item b where b.status_code='001' and b.set_year=alias.set_year and b.rg_code=alias.rg_code and b.node_id in ("
        + nodeId + ") ) ");//全部剔除待办数据
    if (result != null && !result.isEmpty()) {
      return_sql.append(" and ");
      for (int i = 0; i < result.size(); i++) {
        String tmp_inner_table_id = ((Map) result.get(i)).get("id_column_name") == null ? "" : (String) ((Map) result
          .get(i)).get("id_column_name");
        return_sql
          .append((i == 0 && is1stExistClauseFlag ? "" : "or"))
          .append(" exists (select 1 from sys_wf_complete_tasks task where task.rg_code='")
          .append(rg_code)
          .append("' and task.set_year = ")
          .append(set_year)
          .append(" and task.create_user_id = '")
          .append(userId)
          .append("' ")
          .append(data_right_sql)
          .append(
            " and action_type_code != 'DELETE' AND exists (SELECT 1 FROM sys_wf_module_node t WHERE t.module_id = '"
              + menuId + "' and t.node_id in (task.current_node_id,task.next_node_id))  and task.entity_id=")
          .append(TableAlias).append(".").append(tmp_inner_table_id).append(")");
      }
    } else {
      return_sql.append(" and  1=0 ");
    }
    return return_sql.toString();

  }

  /**
  * 获得业务年度
  * 
  * @return
  * 
  * add by liuzw 20120418
  * 
  */
  public String getSetYear() {
    return SessionUtil.getLoginYear();
  }

  /**
   * 获得区划
   * 
   * @return
   * 
   * add by liuzw 20120418
   */
  private String getRgCode() {
    return SessionUtil.getRgCode();
  }

  /**
   * 按区划存储工作流节点(菜单节点、角色节点)
   * @param menuId 菜单ID
   * @param roleId 角色ID
   * @param obj 存储对象
   * @return Map
   * @author Administrator 李文全  2013-06-14
   */
  private String getNodeIdByModuleRole(String menuid, String roleId) {
    String rgCode = SessionUtil.getRgCode();
    Integer setYear = new Integer(SessionUtil.getLoginYear());
    StringBuffer sb = new StringBuffer();
    List nodeList = dao.findBySql(QUERY_NODEID_SQL, new Object[] { rgCode, setYear, menuid, roleId });
    if (!nodeList.isEmpty()) {
      int m = nodeList.size();
      Map tmpMap = null;
      for (int k = 0; k < m; k++) {
        tmpMap = (Map) nodeList.get(k);
        sb.append(tmpMap.get("node_id")).append(",");
      }
      if (sb.length() > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
    }
    return sb.toString();
  }

  private final static String QUERY_NODEID_SQL = "select distinct a.node_id node_id from sys_wf_module_node a, sys_wf_role_node b"
    + " where a.rg_code = ?  and a.set_year = ? and a.module_id = ? and b.role_id=? and a.node_id = b.node_id and a.set_year = b.set_year and a.rg_code = b.rg_code ";

  @Override
  public void autoAudit() throws FAppException {
    // TODO Auto-generated method stub

  }

}
