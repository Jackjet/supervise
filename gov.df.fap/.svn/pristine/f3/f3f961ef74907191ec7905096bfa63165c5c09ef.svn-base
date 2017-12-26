package gov.df.fap.service.workflow;

import gov.df.fap.api.workflow.IBillTypeServices;
import gov.df.fap.api.workflow.IDoWorkFlowForRecall;
import gov.df.fap.api.workflow.IWfUti;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.workflow.FBillTypeDTO;
import gov.df.fap.service.util.UtilCache;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.exception.FAppException;
import gov.df.fap.util.xml.XMLData;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 工作流组件撤消操作服务端实现
 * 
 * @version 1.0
 * @author ymj
 * @since java 1.4.1
 * 
 */
public class DoWorkFlowForRecallBO implements IDoWorkFlowForRecall {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao = null;

  @Autowired
  private IWfUti wfUtil = null;

  @Autowired
  private IBillTypeServices billtype;

  private XMLData rowData;

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 撤销专用方法
   * 
   * @param moduleid
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
  public List doSingleProcessSimplyRecall(String moduleid, String roleid, String operationname, String operationdate,
    String operationremark, List inputFVoucherDtos, boolean auto_tolly_flag, boolean isBillDetail, XMLData tmpCanGoData)
    throws FAppException {
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
          tolly_flag = wfUtil.getTollyFlag(moduleid, roleid, "RECALL", bill_table_name, detail_table_name,
            inputFVoucherDto, tmpCanGoData);
          // 如果是龙图平台
          if (SessionUtil.getParaMap().get("switch01").toString().equals("1")) {
            // 如果是龙图平台，撤销时需要传入2
            inputFVoucherDto.setIs_end(2);
          } else {
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
        return_list.add(wfUtil.doBusVouUpdateBatch(onload));// 调用批量更新执行在途的撤销
        return_list.add(wfUtil.doBusVouCancelAuditBatch(over));// 调用批量更新执行终审核的撤销
      } else// 不记帐，直接赋值
      {
        return_list = inputFVoucherDtos;
      }

      return_list = new ArrayList();

      //变量 itr 必须重新赋值，因为上一个 while (itr.hasNext()) 已经执行完毕，
      //如果 itr 不重新赋值，那么itr.hasNext()==false，下一个while (itr.hasNext())永远不会执行。
      itr = inputFVoucherDtos.iterator();
      //
      while (itr.hasNext()) {
        inputFVoucherDto = (FVoucherDTO) itr.next();// 取数据

        rowData = new XMLData();
        rowData = inputFVoucherDto.toXMLData();

        bill_table_name = ((FBillTypeDTO) billtype.getBillTypeByCode(inputFVoucherDto.getBilltype_code()))
          .getTable_name();

        //如果是单＋明细形式，则要设置bill_table_name
        if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0
          || ((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code() == null
          || ((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code().equals("")) {
          detail_table_name = bill_table_name;
        } else {

          String billType_code = ((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code().toString();
          detail_table_name = UtilCache.getTableNameByBillTypeCode(billType_code);

        }
        wfUtil.doInspect("", moduleid, roleid, "RECALL", operationname, operationdate, operationremark,
          bill_table_name, detail_table_name, inputFVoucherDto, false, isBillDetail, tmpCanGoData);
        //如果没有明细数据，则直接调工作流
        if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
          if (detail_table_name == null || detail_table_name.equals("")) {
            throw new Exception("需要传入detail_table_name!");
          }
          return_list.add(undoSingleProcessSimplyReturnObj(moduleid, roleid, detail_table_name, inputFVoucherDto));
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
            undoSingleProcessSimplyReturnObj(moduleid, roleid, detail_table_name, detailDto);
          }
          return_list.add(inputFVoucherDto);
        }
      }
      return return_list;
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      e.printStackTrace();
      throw new FAppException(e/* "WF-000001", e.getMessage() */);

    }
  }

  /**
   * 通用撤消处理操作（对单条数据）。
   * 
   * @param moduleid-----------功能ID
   * @param roleid-------------角色ID
   * @param table_name---------表名
   * @param inputFVoucherDto---业务数据DTO（单条）
   * @return 撤消后的当前节点的DTO
   * @throws Exception---------错误信息
   */
  private synchronized Object undoSingleProcessSimplyReturnObj(String moduleid, String roleid, String tablename,
    FVoucherDTO inputFVoucherDto) throws Exception {
    //增加对多财政多年度的支持
    String rg_code = getRgCode();
    String setYear = getSetYear();
    // 是否有历史数据
    boolean has_old_dto = true;
    String billId = "";
    Session tmp_session = null;
    try {
      Object return_dto = new Object();
      //显示代理用户 start
      String create_user = "";
      String authorieduser_name = (String) SessionUtil.getUserInfoContext().getAttribute("authorieduser_name");

      if (SessionUtil.getUserInfoContext().getAttribute("user_name") != null
        && !SessionUtil.getUserInfoContext().getAttribute("user_name").toString().equals("")) {
        create_user = SessionUtil.getUserInfoContext().getAttribute("user_name").toString();
      }

      if (authorieduser_name != null && authorieduser_name.length() > 0 && !authorieduser_name.equals(create_user)) {
        create_user = authorieduser_name + "(代理：" + create_user + ")";
      }
      //显示代理用户 end

      StringBuffer select_sql = new StringBuffer();
      StringBuffer insert_sql = new StringBuffer();
      StringBuffer delete_sql = new StringBuffer();
      StringBuffer update_sql = new StringBuffer();

      int num = 0;
      String entityId = "";

      List rs = null;
      String wf_id = "";
      String current_node_id = "0";
      if (moduleid == null || moduleid.equals("")) {
        throw new Exception("需要传入moduleid!");
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
      // 判断撤消操作是否为撤消确认结点的操作
      // 增加vw_sys_wf_node_relation的module_id做判断,用于支持一条单据走两个不同的工作流
      StringBuffer judge_sql = new StringBuffer();
      judge_sql.append("select distinct t.wf_id, t.current_node_id, t.next_node_id, t.remark ")
        .append("  from sys_wf_end_tasks t, sys_wf_nodes n, vw_sys_wf_node_relation d ")
        .append(" where t.wf_id = n.wf_id ").append("   and t.wf_id = d.wf_id ")
        .append("   and t.current_node_id = d.node_id ").append("   and d.module_id = ? ")
        .append("   and n.node_type = '003' ").append("   and t.next_node_id = n.node_id ")
        .append("   and t.entity_id = ? and t.rg_code=? and t.set_year=?");
      rs = dao.findBySql(judge_sql.toString(), new Object[] { moduleid, entityId, rg_code, setYear });

      // 如果不是撤销确认结点的操作，按照原来的走，如果是撤销确认结点的操作，则按改动的操作走
      if (rs.size() == 0) {
        // 不是撤销确认节点的操作，所以按照开始的走下去
        // 查询是否可以进行撤销操作
        select_sql.append("select distinct b.wf_id, ").append("                b.current_node_id, ")
          .append("                b.next_node_id, ").append("                b.remark, ")
          .append("                b.action_type_code, ").append("                b.operation_date ")
          .append("  from sys_wf_current_tasks b, vw_sys_wf_node_relation d ").append(" where d.module_id = ? ")
          .append("   and d.role_id = ? ").append("   and b.current_node_id = d.node_id ")
          .append("   and b.entity_id = ? ").append("   and not exists ").append(" (select 1 ")
          .append("          from sys_wf_current_tasks ct ").append("         where ct.current_node_id in ")
          .append("               (select distinct mt.next_node_id ")
          .append("                  from sys_wf_complete_tasks mt ")
          .append("                 where mt.current_node_id = b.current_node_id ")
          .append("                   and mt.next_node_id <> b.next_node_id ")
          .append("                   and mt.action_type_code in ('INPUT', 'NEXT', 'BACK') ")
          .append("				      and not exists (select 1 from sys_wf_node_conditions ncs  ")
          .append("           						   where ncs.wf_id = ct.wf_id ")
          .append("           							 and ncs.routing_type = '002' ")
          .append("           							 and ncs.node_id = ct.current_node_id ")
          .append("           							 and ncs.next_node_id = b.current_node_id) ")
          .append("                   and mt.entity_id = b.entity_id) ")
          .append("           and ct.entity_id = b.entity_id ) ").append("	  and not exists ").append(" (select 1 ")
          .append("          from sys_wf_complete_tasks comt, sys_wf_node_conditions ncs ")
          .append("         where ncs.wf_id = b.wf_id ")
          .append("           and ncs.routing_type = decode(b.action_type_code, 'NEXT', '001', 'BACK', '002', '0') ")
          .append("           and ncs.node_id = b.current_node_id ")
          .append("           and ncs.next_node_id <> b.next_node_id ")
          .append("           and comt.wf_id = ncs.wf_id ")
          .append("           and comt.next_node_id = ncs.next_node_id ")
          .append("           and comt.action_type_code = b.action_type_code ")
          .append("           and comt.entity_id = b.entity_id ").append("			  and not exists ")
          .append("         (select 1 ").append("                  from sys_wf_current_tasks curt ")
          .append("                 where curt.next_node_id = comt.next_node_id ")
          .append("                   and curt.wf_id = comt.wf_id ")
          .append("                   and curt.entity_id = comt.entity_id)) ");

        rs = dao.findBySql(select_sql.toString(), new Object[] { moduleid, roleid, entityId });

        // 如果查找不到时，判断当前做撤销的数据是否是在开始节点上做的撤销
        if (rs.size() == 0) {
          StringBuffer selectStartNodeSql = new StringBuffer();
          selectStartNodeSql.append("select distinct t.wf_id, t.current_node_id, t.next_node_id, t.remark ")
            .append("  from sys_wf_current_tasks t, sys_wf_nodes n ").append(" where t.wf_id = n.wf_id ")
            .append("   and n.node_type = '001' ").append("   and t.current_node_id = n.node_id ")
            .append("   and t.entity_id = ? and t.rg_code=? and t.set_year=?");
          rs = dao.findBySql(selectStartNodeSql.toString(), new Object[] { entityId, rg_code, setYear });
          has_old_dto = false;
          if (rs.size() > 0)
            throw new Exception("不能在开始节点做撤销操作!");
        }
        //放入需要撤销的节点
        List nodes = new ArrayList();
        if (rs.size() == 0) {
          throw new Exception("不能走入下一流程节点，原因有：未找到该数据；或者相邻节点数据已送审；或者该角色没有此权限!");
        } else {
          // 得到当前需要撤销的数据的流程号和节点号
          wf_id = ((Map) rs.get(0)).get("wf_id").toString();
          current_node_id = ((Map) rs.get(0)).get("current_node_id").toString();
          nodes.add(current_node_id);
          //ymj 根据operation_date来判断 撤销回退到的节点为同步节点时 需要撤销回退其他的相邻节点 用时间来判断不是很准确 因为只精确到秒 暂时想不出好的办法代替
          for (int i = 0; i < rs.size(); i++) {
            String next_node_id = ((Map) rs.get(i)).get("next_node_id").toString();
            String action_type_code = ((Map) rs.get(i)).get("action_type_code").toString();
            String operation_date = ((Map) rs.get(i)).get("operation_date").toString();
            if (action_type_code.equalsIgnoreCase("back") && wfUtil.isGatherNode(next_node_id).equalsIgnoreCase("0")) {
              StringBuffer sqlSb = new StringBuffer();
              sqlSb.append("select current_node_id ").append("  from sys_wf_current_tasks ")
                .append(" where entity_id = ? ").append("   and next_node_id = ? ")
                .append("   and current_node_id <> ? ").append("   and operation_date = ? ")
                .append("   and action_type_code = 'BACK' and rg_code=? and set_year=?");
              List rs_temp = dao.findBySql(sqlSb.toString(), new Object[] { entityId, next_node_id, current_node_id,
                operation_date, rg_code, setYear });
              Iterator it = rs_temp.iterator();
              while (it.hasNext()) {
                XMLData data = (XMLData) it.next();
                Object obj = data.get("current_node_id");
                if (obj != null) {
                  nodes.add((String) obj);
                }
              }
            }
          }
        }
        return_dto = (Object) inputFVoucherDto;
        Iterator it_nodes = nodes.iterator();
        while (it_nodes.hasNext()) {
          Object obj = it_nodes.next();
          if (obj != null) {
            current_node_id = (String) obj;
            //将当前任务列表的当前数据置上UNDO标志
            update_sql = new StringBuffer();
            update_sql.append("update  sys_wf_current_tasks b set b.is_undo=1 where b.entity_id =? ").append(
              " and b.wf_id=? and b.current_node_id=? and b.rg_code=? and b.set_year=?");
            num = dao.executeBySql(update_sql.toString(), new Object[] { entityId, wf_id, current_node_id, rg_code,
              setYear });
            if (num == 0) {
              throw new Exception("不能走入下一流程节点，系统内部错误!");
            }

            // 如果能找到上一节点数据
            if (has_old_dto) {
              // 将完成任务列表中的上一节点的数据复制到当前任务列表
              StringBuffer insert_old_sql = new StringBuffer();
              StringBuffer delete_old_sql = new StringBuffer();
              insert_old_sql
                .append("insert into sys_wf_current_tasks")
                .append(" select * from sys_wf_complete_tasks c ")
                .append("  where c.task_id in (")
                .append("      select b.task_id from sys_wf_current_tasks a, sys_wf_task_routing b ")
                .append("       where a.entity_id =? ")
                .append("         and a.wf_id= ? ")
                .append(
                  "         and a.current_node_id=? and a.task_id=b.next_task_id and a.rg_code=? and a.set_year=?) and c.rg_code=? and c.set_year=?");
              num = dao.executeBySql(insert_old_sql.toString(), new Object[] { entityId, wf_id, current_node_id,
                rg_code, setYear, rg_code, setYear });
              if (num < 1)
                throw new Exception("不能走入下一流程节点，系统内部错误!");

              // 删除完成任务列表中的上一节点的数据
              delete_old_sql
                .append("delete from sys_wf_complete_tasks c ")
                .append("   where c.task_id in (")
                .append("  		select b.task_id from sys_wf_current_tasks a,sys_wf_task_routing b ")
                .append("        where a.entity_id =? and a.wf_id=? ")
                .append(
                  "          and a.current_node_id=? and a.task_id=b.next_task_id and a.is_undo = 1 and a.rg_code=? and a.set_year=?) and c.rg_code=? and c.set_year=?");
              num = dao.executeBySql(delete_old_sql.toString(), new Object[] { entityId, wf_id, current_node_id,
                rg_code, setYear, rg_code, setYear });
              if (num < 1)
                throw new Exception("不能走入下一流程节点，系统内部错误!");
            }

            // 将当前任务列表中的UNDO的数据复制到完成列表中
            insert_sql = new StringBuffer();
            insert_sql
              .append("insert into sys_wf_complete_tasks")
              .append(" (TASK_ID, WF_ID, WF_TABLE_NAME, ENTITY_ID,")
              .append(" CURRENT_NODE_ID, NEXT_NODE_ID, ACTION_TYPE_CODE, IS_UNDO, CREATE_USER, CREATE_DATE, UNDO_USER,")
              .append(" UNDO_DATE, OPERATION_NAME, OPERATION_DATE, OPERATION_REMARK, INIT_MONEY, RESULT_MONEY, REMARK,")
              .append(" TOLLY_FLAG, BILL_TYPE_CODE, BILL_ID, RCID, CCID, CREATE_USER_ID,RG_CODE,SET_YEAR) ")
              .append(" select TASK_ID, WF_ID, WF_TABLE_NAME,	ENTITY_ID, CURRENT_NODE_ID,	NEXT_NODE_ID,")
              .append(" ACTION_TYPE_CODE,	1, CREATE_USER,	CREATE_DATE, '")
              .append(create_user)
              .append("',	'")
              .append(Tools.getCurrDate())
              .append("',")
              .append(
                " OPERATION_NAME, OPERATION_DATE, OPERATION_REMARK, INIT_MONEY, RESULT_MONEY, REMARK, TOLLY_FLAG, BILL_TYPE_CODE, bill_id, rcid, ccid, CREATE_USER_ID,'")
              .append(rg_code).append("',").append(setYear).append("    from sys_wf_current_tasks ")
              .append("   where entity_id = '").append(entityId).append("'    and wf_id= '").append(wf_id)
              .append("'    and current_node_id = '").append(current_node_id)
              .append("'    and is_undo=1 and rg_code='").append(rg_code).append("' and set_year=").append(setYear);
            num = dao.executeBySql(insert_sql.toString());
            if (num == 0) {
              throw new Exception("不能走入下一流程节点，系统内部错误!");
            }

            // 删除当前任务列表中的UNDO的数据
            delete_sql = new StringBuffer();
            delete_sql.append("delete from  sys_wf_current_tasks b ").append(
              " where b.entity_id =? and b.wf_id=? and b.current_node_id=? and is_undo=1 and rg_code=? and set_year=?");
            num = dao.executeBySql(delete_sql.toString(), new Object[] { entityId, wf_id, current_node_id, rg_code,
              setYear });
            if (num == 0) {
              throw new Exception("不能走入下一流程节点，系统内部错误!");
            }

            /**
             * add by bing-zeng 2008-02-04 对工作流ITEM表进行操作 begin
             */
            String sql4OperateItemTable = "select task_id,CURRENT_NODE_ID, NEXT_NODE_ID,BILL_ID from sys_wf_current_tasks "
              + " where entity_id =? and wf_id=? and next_node_id=? and rg_code=? and set_year=?";

            List list = this.dao.findBySql(sql4OperateItemTable, new Object[] { entityId, wf_id, current_node_id,
              rg_code, setYear });

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
                + " where entity_id =? and wf_id=? and current_node_id=? and rg_code=? and set_year=? ";

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
          .append(" from sys_wf_end_tasks b, vw_sys_wf_node_relation d ")
          .append(" where d.module_id = ? and d.role_id = ? and b.current_node_id=d.node_id ")
          .append(" and b.entity_id =? and b.rg_code=? and b.set_year=?");
        rs = dao.findBySql(select_sql.toString(), new Object[] { moduleid, roleid, entityId, rg_code, setYear });

        if (rs.size() == 0) {
          throw new Exception("不能走入下一流程节点，原因有：未找到该数据；或者该角色没有此权限!");
        } else {
          // 得到当前需要撤销的数据的流程号和节点号
          wf_id = ((Map) rs.get(0)).get("wf_id").toString();
          current_node_id = ((Map) rs.get(0)).get("current_node_id").toString();
        }

        return_dto = (Object) inputFVoucherDto;

        // 将以确认任务列表的当前数据置上UNDO标志
        update_sql.append("update  sys_wf_end_tasks b set b.is_undo=1 where b.entity_id =? ").append(
          " and b.wf_id=? and b.current_node_id=? and b.rg_code=? and b.set_year=?");
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
            .append(" 	select * from sys_wf_complete_tasks c ")
            .append("    where c.task_id in ( ")
            .append(" 		select b.task_id from sys_wf_end_tasks a,sys_wf_task_routing b ")
            .append("        where a.entity_id = ? ")
            .append("          and a.wf_id= ? ")
            .append(
              "          and a.current_node_id=? and a.task_id=b.next_task_id and a.rg_code=? and a.set_year=?) and c.rg_code=? and c.set_year=?");
          num = dao.executeBySql(insert_old_sql.toString(), new Object[] { entityId, wf_id, current_node_id, rg_code,
            setYear, rg_code, setYear });
          if (num < 1)
            throw new Exception("不能走入下一流程节点，系统内部错误!");

          // 删除完成任务列表中的上一节点的数据
          delete_old_sql
            .append(" delete from sys_wf_complete_tasks c ")
            .append("  where c.task_id in (")
            .append(" 		select b.task_id from sys_wf_end_tasks a,sys_wf_task_routing b ")
            .append("        where a.entity_id =? and a.wf_id=? and a.current_node_id= ? ")
            .append(
              "          and a.task_id = b.next_task_id and a.is_undo = 1 and a.rg_code=? and a.set_year=? ) and c.rg_code=? and c.set_year=?");
          num = dao.executeBySql(delete_old_sql.toString(), new Object[] { entityId, wf_id, current_node_id, rg_code,
            setYear, rg_code, setYear });
          if (num < 1)
            throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 将以确认任务列表中的UNDO的数据复制到完成列表中
        insert_sql
          .append("insert into sys_wf_complete_tasks")
          .append(" (TASK_ID, WF_ID, WF_TABLE_NAME, ENTITY_ID,")
          .append(" CURRENT_NODE_ID, NEXT_NODE_ID, ACTION_TYPE_CODE, IS_UNDO,CREATE_USER, CREATE_DATE, UNDO_USER,")
          .append(" UNDO_DATE, OPERATION_NAME, OPERATION_DATE, OPERATION_REMARK, INIT_MONEY, RESULT_MONEY, REMARK,")
          .append(" TOLLY_FLAG, BILL_TYPE_CODE, BILL_ID, RCID, CCID, CREATE_USER_ID,SET_YEAR,RG_CODE) ")
          .append(" select TASK_ID, WF_ID, WF_TABLE_NAME,	ENTITY_ID, CURRENT_NODE_ID,	NEXT_NODE_ID,")
          .append(" ACTION_TYPE_CODE,	1, CREATE_USER,	CREATE_DATE, '")
          .append(create_user)
          .append("',	'")
          .append(Tools.getCurrDate())
          .append("',")
          .append(
            " OPERATION_NAME, OPERATION_DATE, OPERATION_REMARK,	INIT_MONEY,	RESULT_MONEY, REMARK,TOLLY_FLAG, BILL_TYPE_CODE, bill_id, rcid, ccid, CREATE_USER_ID, ")
          .append(setYear).append(", '").append(rg_code).append("'").append(" from sys_wf_end_tasks ")
          .append(" where  entity_id = '").append(entityId).append("'  and wf_id = '").append(wf_id)
          .append("'  and current_node_id = '").append(current_node_id).append("'  and is_undo=1 and rg_code='")
          .append(rg_code).append("' and set_year=").append(setYear);
        num = dao.executeBySql(insert_sql.toString());
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 删除以确认任务列表中的UNDO的数据
        delete_sql.append("delete from sys_wf_end_tasks b ").append(
          " where b.entity_id =? and b.wf_id=? and b.current_node_id=? and is_undo=1 and b.rg_code=? and b.set_year=?");
        num = dao.executeBySql(delete_sql.toString(),
          new Object[] { entityId, wf_id, current_node_id, rg_code, setYear });
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }
        /**
         * add by bing-zeng 2008-02-04 对工作流ITEM表进行操作 begin
         */
        String sql4OperateItemTable = "select task_id, CURRENT_NODE_ID, NEXT_NODE_ID,BILL_ID from sys_wf_current_tasks "
          + " where entity_id =? and wf_id=? and next_node_id=? and rg_code=? and set_year=? ";

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
            + " where entity_id =? and wf_id=? and current_node_id=?  and rg_code=? and set_year=?";

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
        /**
         * add by bing-zeng 2008-02-04 对工作流ITEM表进行操作 end
         */
        return return_dto;
      }
      /*
       * update daiwei 20070413 end
       */
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    } finally {
      if (tmp_session != null) {
        dao.closeSession(tmp_session);
      }

    }

  }

  /**
   * 
   * 功能：处理挂起流程
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
   * @author ymj
   * @throws Exception
   * @since 1.0
   * 
   */
  public void saveOptionCurrentAndComleteTable(Statement setRoutingPsmt, String taskId, String wfId, String entityId,
    String currentNodeId, String nextNodeId, String actionType, String billId, String rcid, String ccid)
    throws Exception {

    // 同步操作时查询是否满足走入下一个节点.
    StringBuffer sqlBuffer = new StringBuffer();

    StringBuffer gatherBuffer = new StringBuffer();

    String flag = wfUtil.getValidGatherNode(sqlBuffer, gatherBuffer, nextNodeId, currentNodeId, entityId, billId, wfId,
      rcid, ccid, rowData);

    // 操作类型以第一个字母取代
    char var = actionType.charAt(0);

    switch (var) {
    //RECALL
    case 'R':
      // 同步操作
      boolean isEdit = false;
      if (nextNodeId.equals("0")) {
        nextNodeId = currentNodeId;
        isEdit = true;
      }
      wfUtil.delete4CurrentOrCompleteItems4Recall(setRoutingPsmt, taskId, currentNodeId, nextNodeId, entityId);

      // 同步操作 如果查询信息返回的集合小于1 不能进入下一个节点

      if (flag.equals("0")) {
        //				撤销退回的判断
        String actionSql = "select * from sys_wf_current_tasks where task_id = ? ";
        List tempList = this.dao.findBySql(actionSql, new Object[] { taskId });

        // 当前任务的操作类型
        String actionTypeCode = "0";
        if (tempList.size() > 0) {
          XMLData x = (XMLData) tempList.get(0);
          actionTypeCode = x.get("action_type_code").toString();
        }

        if (isCanInsertCurrentItem4GatherNodeRecall(wfId, entityId, currentNodeId, nextNodeId, rowData)) {
          String nodeId = "next_node_id";
          String statusCode = "001";

          if (actionTypeCode.equals("BACK"))
            statusCode = "004";
          if (isEdit)
            nodeId = "current_node_id";

          String sql = "insert into sys_wf_current_item " + "  select entity_id, bill_id, " + nodeId + ", '"
            + statusCode + "', rcid, ccid " + "    from sys_wf_current_tasks " + "   where entity_id = ? "
            + "     and task_id = ?";

          this.dao.executeBySql(sql, new Object[] { entityId, taskId });
        }
        //ymj 传入actiontype
        wfUtil.add4CurrentOrCompleteItems4Recall(setRoutingPsmt, actionTypeCode, entityId, billId, currentNodeId,
          nextNodeId, "001", rcid, ccid);
      } else {
        //撤销退回的判断
        String actionSql = "select * from sys_wf_current_tasks where task_id = ?";
        List tempList = this.dao.findBySql(actionSql, new Object[] { taskId });

        // 当前任务的操作类型
        String actionTypeCode = "0";
        if (tempList.size() > 0) {
          XMLData x = (XMLData) tempList.get(0);
          actionTypeCode = x.get("action_type_code").toString();
        }
        if (actionTypeCode.equals("BACK"))
          wfUtil.add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, nextNodeId, "004", rcid, ccid);
        else
          wfUtil.add4CurrentOrCompleteItems(setRoutingPsmt, entityId, billId, nextNodeId, "001", rcid, ccid);

        wfUtil.add4CurrentOrCompleteItems4Recall(setRoutingPsmt, actionTypeCode, entityId, billId, currentNodeId,
          nextNodeId, "001", rcid, ccid);
      }
      break;

    }
  }

  /**
   * 从多分支汇入一个同步节点，同步节点做退回再撤消，判断满足条件的分支数据是否已经全部走入此同步节点。
   * 如果是，才向sys_wf_current_item表里写入相应记录，否则，不写。
   * @param wfId
   * @param entityId
   * @param currentNodeId
   * @param nextNodeId
   * @param rowData
   * @return
   * @throws FAppException
   */
  private boolean isCanInsertCurrentItem4GatherNodeRecall(String wfId, String entityId, String currentNodeId,
    String nextNodeId, XMLData rowData) throws FAppException {
    //modify by liuzw 20120418 增加对多财政多年度的支持
    String rg_code = getRgCode();
    String setYear = getSetYear();
    StringBuffer sb_1 = new StringBuffer();
    sb_1
      .append("select ncs.* ")
      .append("  from sys_wf_node_conditions ncs, sys_wf_nodes nodes ")
      .append(" where ncs.routing_type = '001' ")
      .append("   and ncs.node_id <> ? ")
      .append("   and ncs.next_node_id = ? ")
      .append("   and ncs.next_node_id = nodes.node_id ")
      .append("   and nodes.gather_flag = '0' ")
      .append("   and ncs.wf_id = nodes.wf_id ")
      .append("   and ncs.wf_id = ? ")

      //以下用于判断数据是否存在于源节点或者目标节点
      .append("	and exists ").append(" (select 1 ").append("          from sys_wf_current_tasks curt ")
      .append("         where curt.wf_id = ncs.wf_id ")
      .append("           and curt.entity_id = ? and curt.rg_code=? and curt.set_year=?")
      .append("           and ((((curt.next_node_id = ncs.node_id and ")
      .append("               curt.action_type_code in ('INPUT', 'NEXT', 'BACK')) or ")
      .append("               (curt.current_node_id = ncs.node_id and ")
      .append("               curt.action_type_code in ('EDIT')))) or ")
      .append("               (curt.current_node_id = ncs.node_id and ")
      .append("               curt.next_node_id = ncs.next_node_id and ")
      .append("               curt.action_type_code in ('INPUT', 'NEXT')))) ");
    List list1 = dao.findBySql(sb_1.toString(), new Object[] { currentNodeId, nextNodeId, wfId, entityId, rg_code,
      setYear });
    //
    StringBuffer sb_2 = new StringBuffer();
    sb_2.append("select * ").append("  from sys_wf_current_tasks curt ")
      .append(" where curt.wf_id = ? and  curt.rg_code=? and curt.set_year=?").append("   and curt.entity_id = ? ")
      .append("   and ((curt.next_node_id = ? and ")
      .append("       curt.action_type_code in ('INPUT', 'NEXT', 'BACK')) or ")
      .append("       (curt.current_node_id = ? and curt.action_type_code = 'EDIT')) ");
    //
    for (int i = 0; list1 != null && i < list1.size(); i++) {
      Map tmpMap = (Map) list1.get(i);
      String tmpCondId = (String) tmpMap.get("condition_id");
      if (tmpCondId.equals("#") || wfUtil.getValidWfNode(tmpCondId, rowData)) {
        List tmpList = dao.findBySql(sb_2.toString(), new Object[] { wfId, entityId, nextNodeId, nextNodeId, rg_code,
          setYear });
        if (tmpList != null && tmpList.size() > 0) {
          continue;
        } else {
          return false;
        }
      }
    }

    return true;
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
    String set_year = (String) SessionUtil.getUserInfoContext().getSetYear();
    if (set_year == null || set_year.equalsIgnoreCase("")) {
      set_year = String.valueOf(DateHandler.getCurrentYear());
    }
    return set_year;
  }

  /**
   * 获得区划
   * 
   * @return
   * 
   * add by liuzw 20120418
   */
  private String getRgCode() {

    String rg_code = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");
    return rg_code;
  }
}
