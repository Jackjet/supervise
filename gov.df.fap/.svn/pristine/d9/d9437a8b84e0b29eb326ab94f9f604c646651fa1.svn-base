package gov.df.fap.service.workflow;

import gov.df.fap.api.gl.coa.ibs.ICoa;
import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.api.workflow.IBillTypeServices;
import gov.df.fap.api.workflow.IInspectService;
import gov.df.fap.api.workflow.IWfUti;
import gov.df.fap.api.workflow.IWorkFlowHelper;
import gov.df.fap.api.workflow.sysregulation.IWorkFlowRuleFactory;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.workflow.FBillTypeDTO;
import gov.df.fap.service.util.UtilCache;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.exception.FAppException;
import gov.df.fap.util.xml.XMLData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 工作流组件工具类
 * 
 * @version 1.0
 * @author ymj
 * @since java 1.4.1
 * 
 */
public class WfUti implements IWfUti {

  @Autowired
  private IWorkFlowHelper workFlowHelper = null;

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao = null;

  @Autowired
  private ICoa icoaService;

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 批量生成ccid
   * 
   * @param List 业务数据DTO集合
   * @return
   * @throws Exception
   * 
   */
  private void createCcidForBatch(List inputFVoucherDtos) throws FAppException {

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

  /**
   * 根据billtype获取coaid
   * 
   * @param billType_code
   * @return
   * @throws Exception
   * 
   */
  private String getCOAIDByBillTypeCode(String billType_code) throws FAppException {

    IBillTypeServices billType = (IBillTypeServices) SessionUtil.getServerBean("sys.billTypeService");
    String coa = UtilCache.getCoaIDByBillTypeCode(billType_code);
    String coa_id = "";
    if (coa != null && !coa.equals("")) {
      coa_id = coa;
    } else {
      coa_id = (String) billType.getBillTypeByCode(billType_code).getCoa_id();
      UtilCache.putCoaIDByBillTypeCode(billType_code, coa_id);
    }
    return coa_id;
  }

  /**
   * 生成rcid
   * 
   * @param List 业务数据DTO集合
   * @return
   * @throws Exception
   * 
   */
  private void createRcid(List inputFVoucherDtos) throws FAppException {
    try {
      IDataRight idataright = (IDataRight) SessionUtil.getServerBean("sys.dataRightService");
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
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e/* "WF-000001", e.getMessage() */);
    }
  }

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
    XMLData tmpCanGoData, StringBuffer bill_table_name, StringBuffer detail_table_name) throws Exception {

    try {
      IBillTypeServices billtype = (IBillTypeServices) SessionUtil.getServerBean("sys.billTypeService");

      FVoucherDTO inputFVoucherDto = ((FVoucherDTO) inputFVoucherDtos.get(0));

      if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
        String billType_code = (String) (inputFVoucherDto.getBilltype_code());
        if (billType_code == null || billType_code.equals("")) {
          throw new Exception("没有传入billtype_code!");
        }
        String table_name = UtilCache.getTableNameByBillTypeCode(billType_code);
        if (table_name != null && !table_name.equals("")) {
          detail_table_name.append(table_name);
        } else {
          String detail_table_name_temp = ((FBillTypeDTO) billtype.getBillTypeByCode(billType_code)).getTable_name();
          detail_table_name.append(detail_table_name_temp);
          UtilCache.putTableNameByBillTypeCode(billType_code, detail_table_name_temp);
        }
      } else {
        String billType_code = (String) (((FVoucherDTO) inputFVoucherDto.getDetails().get(0)).getBilltype_code());
        if (billType_code == null || billType_code.equals("")) {
          throw new Exception("没有传入billtype_code!");
        }
        String table_name = UtilCache.getTableNameByBillTypeCode(billType_code);
        if (table_name != null && !table_name.equals("")) {
          detail_table_name.append(table_name);
        } else {
          String detail_table_name_temp = ((FBillTypeDTO) billtype.getBillTypeByCode(billType_code)).getTable_name();
          detail_table_name.append(detail_table_name_temp);
          UtilCache.putTableNameByBillTypeCode(billType_code, detail_table_name_temp);
        }

      }
      bill_table_name = detail_table_name;

      // ymj 调用批量生成ccid
      if (auto_create_ccid) {
        createCcidForBatch(inputFVoucherDtos);
      }

      // 自动生成RCID(注:可应用于录入及要素变更等情况)
      if (auto_create_rcid) {
        createRcid(inputFVoucherDtos);

      }

      if (auto_tolly_flag) {

        // 根据记账类型不同对其处理方式不同
        inputFVoucherDtos = this.doTolly(moduleid, roleid, actiontypeid, bill_table_name.toString(),
          detail_table_name.toString(), inputFVoucherDtos, tmpCanGoData);// lgc add
      }

    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e/* "WF-000001", e.getMessage() */);
    }
  }

  /**
   * 根据记账类型不同对其处理方式不同
   * 
   * @param moduleid
   * @param roleid
   * @param actiontypeid
   * @param bill_table_name
   * @param detail_table_name
   * @param inputFVoucherDtos
   * @return
   * @author lgc
   * @throws FAppException
   */
  private List doTolly(String moduleid, String roleid, String actiontypeid, String bill_table_name,
    String detail_table_name, List inputFVoucherDtos, XMLData tmpCanGoData) throws FAppException {
    // 记帐类型为在途记帐或终审记帐，则需要记帐
    try {
      // FVoucherDTO vd = (FVoucherDTO) inputFVoucherDtos.get(0);
      // System.out.println(vd.getToctrlid());
      // System.out.println(vd.getFromctrlid());

      String tolly_flag = "-1";
      List input = new ArrayList();
      // if (tolly_flag.equals("0") || tolly_flag.equals("1")) {//lgc
      int dtoSize = inputFVoucherDtos.size();
      // 如果是录入，则只要判断一条凭证的记账类型即可
      if ("INPUT".equals(actiontypeid)) {
        if (dtoSize > 0) {
          tolly_flag = getTollyFlag(moduleid, roleid, actiontypeid, bill_table_name, detail_table_name,
            (FVoucherDTO) inputFVoucherDtos.get(0), tmpCanGoData);
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
          tolly_flag = getTollyFlag(moduleid, roleid, actiontypeid, bill_table_name, detail_table_name,
            (FVoucherDTO) inputFVoucherDtos.get(i), tmpCanGoData);

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
          inputFVoucherDtos = getWorkFlowHelper().doBusVouSaveBatch(inputFVoucherDtos);
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
          inputFVoucherDtos.addAll(doBusVouUpdateBatch(on_land));// 在途
          inputFVoucherDtos.addAll(doBusVouAuditBatch(end));// 终审

        } else if (actiontypeid.equals("EDIT") || actiontypeid.equals("BACK")) {// 修改、退回
          inputFVoucherDtos = doBusVouUpdateBatch(inputFVoucherDtos);
        } else if (actiontypeid.equals("DELETE")) {// 删除
          inputFVoucherDtos = doBusVouDeleteBatch(inputFVoucherDtos);
        } else if (actiontypeid.equals("DISCARD")) {// 作废
          inputFVoucherDtos = doBusVouInvalidBatch(inputFVoucherDtos);
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
      throw new FAppException(e/* "WF-000001", e.getMessage() */);
    }
    return inputFVoucherDtos;
  }

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
    String detail_table_name, FVoucherDTO inputFVoucherDto, XMLData tmpCanGoData) throws Exception {

    // // 自动记帐
    String tolly_flag = "-1";
    // FVoucherDTO inputFVoucherDto = (FVoucherDTO)inputFVoucherDtos.get(0);
    // 根据传入参数取得记帐类型
    if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
      tolly_flag = getWorkFlowHelper().getTollyFlag("", moduleid, roleid, actiontypeid, bill_table_name,
        detail_table_name, inputFVoucherDto, tmpCanGoData);
    } else {
      tolly_flag = getWorkFlowHelper().getTollyFlag("", moduleid, roleid, actiontypeid, bill_table_name,
        detail_table_name, (FVoucherDTO) inputFVoucherDto.getDetails().get(0), tmpCanGoData);
    }

    return tolly_flag;
  }

  /**
   * 获取帮助类
   * @return IWorkFlowHelper
   * @throws Exception
   */
  private IWorkFlowHelper getWorkFlowHelper() {

    if (workFlowHelper == null) {
      workFlowHelper = (IWorkFlowHelper) SessionUtil.getServerBean("sys.workFlowHelperService");
    }
    return workFlowHelper;

  }

  /**
   * 调用资金监控接口
   * @return IWorkFlowHelper
   * @throws Exception
   */
  public void doInspect(String old_current_node_id, String moduleid, String roleid, String actiontypeid,
    String operationname, String operationdate, String operationremark, String bill_table_name,
    String detail_table_name, FVoucherDTO inputFVoucherDto, boolean isForced2Execute, boolean isBillDetail,
    XMLData tmpCanGoData) throws Exception {
    //资金监控
    IInspectService inspectService = null;
    try {
      try {
        inspectService = (IInspectService) SessionUtil.getServerBean("sys.inspectService");
      } catch (Exception e) {
        //没有配置资金监控 不做任何动作
      }

      // 如果访问不到监控类包，则不能调用其接口。
      if (inspectService != null) {
        List nodeInspectRules = null;
        if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
          nodeInspectRules = getWorkFlowHelper().getInspectRules(old_current_node_id, moduleid, roleid, actiontypeid,
            bill_table_name, detail_table_name, inputFVoucherDto, isBillDetail, tmpCanGoData);
        } else {
          nodeInspectRules = getWorkFlowHelper().getInspectRules(old_current_node_id, moduleid, roleid, actiontypeid,
            bill_table_name, detail_table_name, (FVoucherDTO) inputFVoucherDto.getDetails().get(0), isBillDetail,
            tmpCanGoData);
        }
        // 无论能否找到规则列表，都调用监控接口
        String inspectNodeId = null;
        if (nodeInspectRules != null && nodeInspectRules.size() > 0) {
          inspectNodeId = (String) ((XMLData) nodeInspectRules.get(0)).get("node_id");
        } else {
          if (inputFVoucherDto.getDetails() == null || inputFVoucherDto.getDetails().size() == 0) {
            inspectNodeId = getWorkFlowHelper().getNextNodeId(old_current_node_id, moduleid, roleid, actiontypeid,
              inputFVoucherDto, isForced2Execute);
          } else {
            inspectNodeId = getWorkFlowHelper().getNextNodeId(old_current_node_id, moduleid, roleid, actiontypeid,
              (FVoucherDTO) inputFVoucherDto.getDetails().get(0), isForced2Execute);
          }
        }
        String rtnMsg = inspectService.inspectInstance(getWorkFlowHelper().getWfIdByNodeId(inspectNodeId),
          detail_table_name, inspectNodeId, null, actiontypeid, operationname, operationdate, operationremark,
          nodeInspectRules, inputFVoucherDto, moduleid, roleid);
        inputFVoucherDto.setWarning(rtnMsg);
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e/* "WF-000001", e.getMessage() */);
    }
  }

  /**
   * 从sequence SEQ_SYS_WF_TASK_ID 取得下一个TaskId
   * 
   * @return
   */
  public String getNextTaskIdBySequence() {
    return this.getWorkFlowHelper().getNextTaskIdBySequence();
  }

  /**
   * 获取当前节点的下游节点
   * 
   * @return
   */
  public List getNextNode(String nodeId, String entityId, String wfId) {
    return this.getWorkFlowHelper().getNextNode(nodeId, entityId, wfId);
  }

  /**
   * 根据权限获取当前节点的下游节点
   * 
   * @return
   */
  //	public List getNextNodeWithDataRight(String entityId,String tableName,String nodeId,List list,List temp) {
  //		List return_list=new ArrayList();
  //		// 查询当前节点的下游节点，数据项中包含用户信息，和当前节点以及下一节点的信息
  //		List tempList=this.getWorkFlowHelper().getNextNodeWithDataRight(entityId,tableName,nodeId,list,temp);
  //		String sql;
  //		try {
  //			// 获取查询业务数据的sql
  //			sql = getSearchSql(entityId,tableName);
  //			// 查询业务数据
  //			List bizDataList=dao.findBySql(sql);
  //			// 如果业务数据不为空，并且有下游节点
  //			if(bizDataList!=null && bizDataList.size()>0 && tempList!=null){
  //				// 业务数据
  //				XMLData bizData=(XMLData)bizDataList.get(0);
  //				// 业务数据中增加module_id，在getValidWfNode()方法中判断节点是否满足表达式
  //				if(temp!=null && temp.size()>0){
  //					bizData.put("module_id",((Map)temp.get(0)).get("module_id"));
  //				}
  //
  //				Iterator it=tempList.iterator();
  //				// 循环判断是否能从当前节点走到tempList中的某个下游节点
  //				while(it.hasNext()){
  //					// 某个下游节点
  //					Map map=(Map)it.next();
  //					// 获取流转线条件
  //					String condition_id=(String)map.get("condition_id");
  //					try {
  //						// 查询是否可以走到下个节点
  //						if("#".equals(condition_id) || getValidWfNode(condition_id, bizData)){
  //							return_list.add(map);
  //						}
  //					} catch (FAppException e) {					
  //						e.printStackTrace();
  //					}
  //				}
  //			}
  //		} catch (Exception e1) {
  //			e1.printStackTrace();
  //		}
  //		return return_list;
  //	}
  /**
   * 根据权限和参数设置获取节点信息 
   * operationLogFlag==1只显示下岗结点操作信息
   * operationLogFlag==2只显示当前岗结点操作信息 
   * operationLogFlag==3同时显示当前岗和下岗操作信息
   * 
   * @return
   */
  //	public List getNodeWithDataRight(String entityId, String tableName,
  //			String nodeId, List list, List temp, String operationLogFlag) {
  //		List return_list = new ArrayList();
  //		// 查询当前节点的下游节点，数据项中包含用户信息，和当前节点以及下一节点的信息
  //		List tempList = new ArrayList();
  //		//operationLogFlag==1只显示下岗结点操作信息
  //		if(operationLogFlag.equals("1"))
  //			tempList = this.getWorkFlowHelper().getNextNodeWithDataRight(entityId,tableName,nodeId,list,temp);
  //		//operationLogFlag==2只显示当前岗结点操作信息 
  //		if(operationLogFlag.equals("2"))
  //			tempList = this.getWorkFlowHelper().getCurrentNodeWithDataRight(entityId,tableName,nodeId,list,temp);
  //		//operationLogFlag==3同时显示当前岗和下岗操作信息
  //		if(operationLogFlag.equals("3")){
  //			//先获取当前岗的操作信息
  //			this.getWorkFlowHelper().getCurrentNodeWithDataRight(entityId,tableName,nodeId,list,temp);
  //			//再添加下岗操作信息
  //			this.getWorkFlowHelper().getNextNodeWithDataRight(entityId,tableName,nodeId,list,temp);
  //			tempList = list; 
  //		}
  //		//operationLogFlag==4只显示当前岗信息,但当前岗只显示流程节点名称
  //		if(operationLogFlag.equals("4"))
  //			tempList = this.getWorkFlowHelper().getCurrentNodeWithDataRight(entityId,tableName,nodeId,list,temp);
  //		String sql;
  //		try {
  //			// 获取查询业务数据的sql
  //			sql = getSearchSql(entityId, tableName);
  //			// 查询业务数据
  //			List bizDataList = dao.findBySql(sql);
  //			// 如果业务数据不为空，并且有下游节点
  //			if (bizDataList != null && bizDataList.size() > 0
  //					&& tempList != null) {
  //				// 业务数据
  //				XMLData bizData = (XMLData) bizDataList.get(0);
  //				// 业务数据中增加module_id，在getValidWfNode()方法中判断节点是否满足表达式
  //				if (temp != null && temp.size() > 0) {
  //					bizData.put("module_id", ((Map) temp.get(0))
  //							.get("module_id"));
  //				}
  //
  //				Iterator it = tempList.iterator();
  //				// 循环判断是否能从当前节点走到tempList中的某个下游节点
  //				while (it.hasNext()) {
  //					// 某个下游节点
  //					Map map = (Map) it.next();
  //					// 获取流转线条件
  //					String condition_id = (String) map.get("condition_id");
  //					try {
  //						// 查询是否可以走到下个节点
  //						if ("#".equals(condition_id)
  //								|| getValidWfNode(condition_id, bizData)) {
  //							return_list.add(map);
  //						}
  //					} catch (FAppException e) {
  //						e.printStackTrace();
  //					}
  //				}
  //			}
  //		} catch (Exception e1) {
  //			e1.printStackTrace();
  //		}
  //		return return_list;
  //	}
  /**
   * 获取当前节点的所有下游节点
   * @param nodeId 当前节点
   * @param entityId 表层系统业务主键
   * @param wfId 工作流id
   * @return
   */
  public List getNextNodeForAll(String nodeId, String entityId, String wfId) {
    return this.getWorkFlowHelper().getNextNodeForAll(nodeId, entityId, wfId);
  }

  /**
   * 通过调用Oracle的function，获取查询业务数据的sql
   * @param entityId
   * @param tableName
   * @return
   * @throws Exception
   */
  //	private String getSearchSql(String entityId,String tableName) throws Exception{
  //		CallableStatement cs=null;
  //		Session session=null;
  //		try {
  //			session = dao.getSession();
  //			Connection conn = session.connection();
  //			cs = conn
  //					.prepareCall("{call ?:=get_bizdata_searchsql(?, ?)}");
  //			cs.registerOutParameter(1,Types.CHAR);
  //			cs.setString(2, entityId);
  //			cs.setString(3, tableName);
  //			cs.executeQuery();
  //			return cs.getString(1);
  //		} finally {
  //			if (cs != null)
  //				cs.close();
  //			if (session != null)
  //				dao.closeSession(session);
  //		}
  //	}

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
    String billId, String rcid, String ccid, int setMonth, int updateFlag, String createUserId) throws FAppException {
    try {
      //add by liuzw 20120410
      String rg_code = getRgCode();
      String setYear = getSetYear();

      Map noticeDateMap = this.getWorkFlowHelper().getNoticeDateMap(nextNodeId);

      insertCurrentTaskPsmt.setString(1, nextTaskId);
      insertCurrentTaskPsmt.setString(2, wfId);
      insertCurrentTaskPsmt.setString(3, wfTableName);
      insertCurrentTaskPsmt.setString(4, entityId);
      insertCurrentTaskPsmt.setString(5, currentNodeId);
      insertCurrentTaskPsmt.setString(6, nextNodeId);
      insertCurrentTaskPsmt.setString(7, actionType);
      insertCurrentTaskPsmt.setInt(8, isUndo);
      insertCurrentTaskPsmt.setString(9, createUser);
      insertCurrentTaskPsmt.setString(10, (String) noticeDateMap.get("currDate"));
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
      insertCurrentTaskPsmt.setString(24, (String) noticeDateMap.get("sendMsgDate"));
      insertCurrentTaskPsmt.setString(25, (String) noticeDateMap.get("autoAuditDate"));
      insertCurrentTaskPsmt.setInt(26, setMonth);
      insertCurrentTaskPsmt.setInt(27, updateFlag);
      insertCurrentTaskPsmt.setString(28, createUserId);
      //add by liuzw 20120410
      insertCurrentTaskPsmt.setString(29, rg_code);
      insertCurrentTaskPsmt.setInt(30, Integer.parseInt(setYear));
      insertCurrentTaskPsmt.addBatch();
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
      throw new FAppException(e/* "WF-000001", e.getMessage() */);
    }
  }

  /**
   * modify start by bing-zeng 插入sys_wf_task_routing时 删除条数限制,
   * 增加了UPDATE_FLAGE限制 0:表示新增加的任务, 1:表示原有的任务 插入sys_wf_complete_tasks 时 删除条数限制,
   * 增加了UPDATE_FLAGE限制 0:表示新增加的任务, 1:表示原有的任务 删除sys_wf_current_tasks 时 删除条数限制,
   * 增加了UPDATE_FLAGE限制 0:表示新增加的任务, 1:表示原有的任务 增加了最后更新sys_wf_current_tasks ,
   * UPDATE_FLAGE = 1
   */
  public void setRoutingStmt(Statement setRoutingPsmt, String wf_id, String entityId, String current_node_id,
    String tableName) throws FAppException {
    try {
      StringBuffer insertRoutingSql = new StringBuffer();
      insertRoutingSql
        .append(" insert into sys_wf_task_routing")
        .append(Tools.addDbLink())
        .append("(TASK_ID,NEXT_TASK_ID) ")
        .append("   select * from (")
        .append("   select  e.task_id from sys_wf_current_tasks")
        .append(Tools.addDbLink())
        .append(" e")
        .append(" where e.entity_id='" + entityId + "' ")
        .append(
          "    and ((e.next_node_id ='" + current_node_id + "' and e.action_type_code in ('INPUT','NEXT','BACK'))")
        .append(
          " or (e.current_node_id='" + current_node_id
            + "' and e.action_type_code in ('DISCARD','HANG','DELETE','EDIT'))) and e.update_flag = 1  ) ")
        .append("   ,(").append(" select  f.task_id from sys_wf_current_tasks").append(Tools.addDbLink()).append(" f")
        .append(" where f.entity_id='" + entityId + "' ").append(" and f.update_flag = 0   )");

      // 将当前任务列表中的数据复制到完成任务列表中
      StringBuffer insertCompTaskSql = new StringBuffer();
      insertCompTaskSql
        .append("insert into sys_wf_complete_tasks")
        .append(Tools.addDbLink())
        .append(" select b.* ")
        .append(" from   sys_wf_current_tasks")
        .append(Tools.addDbLink())
        .append("  b where  b.entity_id ='" + entityId + "' ")
        .append(" and b.wf_id='" + wf_id + "' ")
        .append(" and ((b.next_node_id='" + current_node_id + "'  and b.action_type_code in ('INPUT','NEXT','BACK'))")
        .append(
          " or (b.current_node_id='" + current_node_id
            + "' and b.action_type_code in ('DISCARD','HANG','DELETE','EDIT'))) and b.update_flag = 1 ");

      // 将当前任务列表中的数据删除
      StringBuffer deleteCurTaskSql = new StringBuffer();
      deleteCurTaskSql
        .append("delete sys_wf_current_tasks")
        .append(Tools.addDbLink())
        .append("  b where  b.entity_id ='" + entityId + "' ")
        .append(" and b.wf_id='" + wf_id + "' ")
        .append(" and ((b.next_node_id='" + current_node_id + "' and b.action_type_code in ('INPUT','NEXT','BACK'))")
        .append(
          " or (b.current_node_id='" + current_node_id
            + "' and b.action_type_code in ('DISCARD','HANG','DELETE','EDIT'))) and b.update_flag = 1 ");

      //ymj 增加tableName参数 保证正确性
      String updateSql = "update sys_wf_current_tasks t set t.update_flag = 1 where t.entity_id ='" + entityId
        + "'  and t.wf_id=" + wf_id + " and t.wf_table_name='" + tableName + "'";

      setRoutingPsmt.addBatch(insertRoutingSql.toString());
      setRoutingPsmt.addBatch(insertCompTaskSql.toString());
      setRoutingPsmt.addBatch(deleteCurTaskSql.toString());
      setRoutingPsmt.addBatch(updateSql);

    } catch (Exception e) {
      throw new FAppException(e/* "WF-000001", e.getMessage() */);
    }

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
  public boolean getValidWfNode(String condition_id, XMLData rowData) throws FAppException {
    IWorkFlowRuleFactory ruleFactory = (IWorkFlowRuleFactory) SessionUtil
      .getServerBean("sys.workFlowRuleFactoryService");

    // 判断流程是否满足表达式
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
   * 一个节点是否为同步节点
   */
  public String isGatherNode(String nodeId) {
    if (null == UtilCache.getGatherNodeSign(nodeId) || "".equals(UtilCache.getGatherNodeSign(nodeId))) {
      // 判断当前节点是同步, 还是异步
      String sql = "select node_id, gather_flag  from sys_wf_nodes  ";
      List list = getWorkFlowHelper().getListForSql(sql);
      Iterator returnList = list.iterator();
      while (returnList.hasNext()) {
        XMLData temp = (XMLData) returnList.next();
        UtilCache.putGatherNodeSign(temp.get("node_id").toString(), temp.get("gather_flag").toString());
      }

    }
    return UtilCache.getGatherNodeSign(nodeId);
  }

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
    String currentNodeId, String entityId, String billId, String wfId, String rcid, String ccid, XMLData rowData) {

    // 如果不存在节点同步标志信息
    isGatherNode(nextNodeId);
    String flag = "1";
    if ("0".equals(nextNodeId)) {
      nextNodeId = currentNodeId;
      // 从缓存中取得当前节点的同步，异步标志 flag 0 ：同步 ， 1 ：异步
      flag = UtilCache.getGatherNodeSign(nextNodeId);
    } else {// 不为0时证明不是修改节点，直接next_node_id取成同步异步即可 --beaf
      flag = UtilCache.getGatherNodeSign(nextNodeId);
    }
    //ymj 之前是"0".equals(SYNCHRONIZATIONFLAG) SYNCHRONIZATIONFLAG常量一直没赋值 不明白为什么这么比 改成flag判断是否同步
    if (flag != null && flag.equals("0")) {

      boolean isExist = false;

      //查找除当前节点外，目标节点的所有上一个节点
      List prevNodes = getPrevNodes(wfId, currentNodeId, nextNodeId);

      if (prevNodes != null && prevNodes.size() > 0) {
        //查找除当前节点外，同一数据还处在哪些节点
        List listTemp = getAllCurrentTasks(wfId, entityId);
        //除当前线路外，判断其它路线中的同一数据是否有未到达目标节点
        Iterator it = listTemp.iterator();
        while (it.hasNext()) {
          Object obj = it.next();
          if (obj instanceof Map) {
            Map map = (Map) obj;
            String actionTypeCode = (String) map.get("action_type_code");
            String node_id = (String) map.get("next_node_id");
            if ("EDIT".equals(actionTypeCode)) {
              node_id = (String) map.get("current_node_id");
            }
            if (isArriveNode(currentNodeId, nextNodeId, node_id, wfId, rowData)) {
              isExist = true;
              break;
            }
          }
        }
      }
      if (isExist) {
        gatherBuffer.append(" select 1 from dual where 1=2 ");
      } else {
        gatherBuffer.append(" select 1 from dual where 1=1 ");
      }

      sqlBuffer.append(" insert into sys_wf_current_item  select  ").append("  '").append(entityId).append("' , '")
        .append(billId).append("'  , '").append(nextNodeId).append("' , ").append("'001'").append(" , '").append(rcid)
        .append("' , '").append(ccid).append("' from dual where   exists(  ").append(gatherBuffer.toString())
        .append(")");
    }
    return flag;
  }

  /**
   * 获得即将要到达的节点的所有上一个节点
   * @param wfId
   * @param currentNodeId
   * @param nextNodeId
   * @return
   */
  private List getPrevNodes(String wfId, String currentNodeId, String nextNodeId) {
    StringBuffer sb = new StringBuffer();
    sb.append("select * from sys_wf_node_conditions nc ").append(" where nc.routing_type = '001' ")
      .append("   and nc.wf_id = ").append(wfId).append("   and nc.node_id <> ").append(currentNodeId)
      .append("   and nc.next_node_id = ").append(nextNodeId);

    return dao.findBySql(sb.toString());

  }

  /**
   * 获得数据所有当前任务
   * @param wfId
   * @param entityId
   * @return
   */
  private List getAllCurrentTasks(String wfId, String entityId) {
    //add by liuzw 20120410
    String rg_code = getRgCode();
    String setYear = getSetYear();
    //modify by liuzw 20120410 增加对多财政多年度的支持
    StringBuffer sb = new StringBuffer();
    sb.append("select * from sys_wf_current_tasks cur ").append(" where cur.wf_id = ").append(wfId)
      .append("   and cur.entity_id = '").append(entityId).append("' ")
      .append("   and cur.action_type_code in ('INPUT', 'NEXT', 'BACK', 'EDIT') and cur.rg_code=? and cur.set_year=?");

    return dao.findBySql(sb.toString(), new Object[] { rg_code, setYear });
  }

  /**
   * 目标节点是当前操作即将到达的节点，判断除当前路线外，其它路线中的数据是否未到达目标节点
   * currentNodeId --> arrNode,是当前操作的路线
   * 
   * @param currentNodeId
   * @param arrNode
   * @param beNode
   * @param wfId
   * @param rowData
   * @return
   */
  private boolean isArriveNode(String currentNodeId, String arrNode, String beNode, String wfId, XMLData rowData) {

    StringBuffer sb = new StringBuffer();
    sb.append("select * from sys_wf_node_conditions nc ").append(" where nc.wf_id = ").append(wfId)
      .append("   and nc.routing_type = '001' ").append("   and nc.node_id = ").append(beNode)
      .append("   and (nc.node_id <> ").append(currentNodeId).append(" 			or nc.next_node_id <> ").append(arrNode)
      .append(")");

    List list = dao.findBySql(sb.toString());
    if (list == null || list.size() == 0) {
      return false;
    }
    if (list != null) {
      Iterator it = list.iterator();
      try {
        while (it.hasNext()) {
          Map mapTemp = (Map) it.next();
          String next_node_id = (String) mapTemp.get("next_node_id");
          String condiiton_id = (String) mapTemp.get("condition_id");
          if (condiiton_id.equals("#")) {
            if (next_node_id.equalsIgnoreCase(arrNode)) {
              return true;
            } else {
              if (isArriveNode(currentNodeId, arrNode, next_node_id, wfId, rowData)) {
                return true;
              } else {
                continue;
              }
            }
          } else if (getValidWfNode(condiiton_id, rowData)) {
            if (next_node_id.equalsIgnoreCase(arrNode)) {
              return true;
            } else {
              if (isArriveNode(currentNodeId, arrNode, next_node_id, wfId, rowData)) {
                return true;
              } else {
                continue;
              }
            }
          }
        }
      } catch (FAppException e) {
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }

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
    String currentNodeId, String nextNodeId, String entityId, String action_code) throws FAppException, SQLException {

    getWorkFlowHelper().delete4CurrentOrCompleteItems(setRoutingPsmt, gatherSql, taskId, currentNodeId, nextNodeId,
      entityId, action_code);

  }

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
    String statusCode, String rcid, String ccid) throws FAppException, SQLException {
    getWorkFlowHelper().add4CurrentOrCompleteItems(setRoutingPsmt, entityId, bill_id, nodeId, statusCode, rcid, ccid);
  }

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
    String nextNodeId, String entityId) throws FAppException, SQLException {
    getWorkFlowHelper().delete4CurrentOrCompleteItems4Recall(setRoutingPsmt, taskId, currentNodeId, nextNodeId,
      entityId);
  }

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
    throws FAppException, SQLException {
    getWorkFlowHelper().add4CurrentOrCompleteItems4Recall(setRoutingPsmt, actionType, entityId, bill_id, currentNodeId,
      nextNodeId, statusCode, rcid, ccid);
  }

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
    XMLData tmpCanGoData) throws FAppException {
    return getWorkFlowHelper().doSingleProcessSimplyInputDetailSelect(moduleid, roleid, tableName, rowData,
      tmpCanGoData);
  }

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
  public List doBusVouUpdateBatch(List newFVoucherDtos) throws FAppException {
    return getWorkFlowHelper().doBusVouUpdateBatch(newFVoucherDtos);
  }

  /**
   * 批量调用交易令的终审接口。
   * 
   * @param auditFVoucherDtos
   *            单据newDTO
   * @throws Exception
   *             错误信息
   * @author ymj
   */
  public List doBusVouAuditBatch(List newFVoucherDtos) throws FAppException {
    return getWorkFlowHelper().doBusVouAuditBatch(newFVoucherDtos);
  }

  /**
   * 批量调用交易令的删除接口。
   * 
   * @param deleteFVoucherDtos
   *            单据DTO
   * @throws Exception
   *             错误信息
   * @author ymj
   */
  public List doBusVouDeleteBatch(List deleteFVoucherDtos) throws FAppException {
    return getWorkFlowHelper().doBusVouDeleteBatch(deleteFVoucherDtos);
  }

  /**
   * 批量调用交易令的作废接口。
   * 
   * @param invalidaFVoucherDtos
   *            单据DTO
   * @throws Exception
   *             错误信息
   * @author ymj
   */
  public List doBusVouInvalidBatch(List invalidateFVoucherDtos) throws FAppException {
    return getWorkFlowHelper().doBusVouInvalidBatch(invalidateFVoucherDtos);
  }

  /**
   * 批量调用交易令的撤销终审接口
   * 
   * @param cancelAuditFVoucherDtos
   *            单据newDTO
   * @throws Exception
   *             错误信息
   * @author 黄节 2008年4月29日修改
   */
  public List doBusVouCancelAuditBatch(List cancelAuditFVoucherDtos) throws FAppException {
    return getWorkFlowHelper().doBusVouCancelAuditBatch(cancelAuditFVoucherDtos);
  }

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
    FVoucherDTO inputFVoucherDto, boolean isForced2Execute) throws FAppException {
    StringBuffer select_sql = new StringBuffer();
    List rs = null;
    try {
      if (!isForced2Execute) {
        select_sql.append("select distinct b.wf_id, b.current_node_id, b.next_node_id, b.action_type_code ")
          .append(" from vw_sys_wf_current_end_tasks").append(Tools.addDbLink()).append(" b, vw_sys_wf_node_relation")
          .append(Tools.addDbLink()).append(" d ").append(" where d.module_id=? and d.role_id=? ")
          .append(" and (b.current_node_id=d.node_id and b.action_type_code in ('INPUT','NEXT','BACK')) ")
          .append(" and b.entity_id = ?");
        // 判断数据是否存在
        rs = dao.findBySql(select_sql.toString(), new Object[] { moduleid, roleid, entityId });
      } else {
        select_sql.append("select distinct b.wf_id, b.current_node_id, b.next_node_id, b.action_type_code ")
          .append(" from vw_sys_wf_current_end_tasks").append(Tools.addDbLink()).append(" b, vw_sys_wf_node_relation")
          .append(Tools.addDbLink()).append(" d ")
          .append(" where (b.current_node_id=d.node_id and b.action_type_code in ('INPUT','NEXT','BACK')) ")
          .append(" and b.entity_id = ?");
        // 判断数据是否存在
        rs = dao.findBySql(select_sql.toString(), new Object[] { entityId });
      }

      if (rs != null && rs.size() > 0) {
        undoSingleProcessSimplyReturnObj(moduleid, roleid, tablename, inputFVoucherDto);
      }
    } catch (FAppException fae) {
      throw fae;
    } catch (Exception e) {
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
    //add by liuzw 20120410
    String rg_code = getRgCode();
    String setYear = getSetYear();
    // 是否有历史数据
    boolean has_old_dto = true;
    String billId = "";
    Session tmp_session = null;
    try {
      Object return_dto = new Object();
      String create_user = "";
      if (SessionUtil.getUserInfoContext().getAttribute("user_name") != null
        && !SessionUtil.getUserInfoContext().getAttribute("user_name").toString().equals("")) {
        create_user = SessionUtil.getUserInfoContext().getAttribute("user_name").toString();
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
      /*
       * update daiwei 20070413 start
       */
      // 判断撤消操作是否为撤消确认结点的操作
      // 增加vw_sys_wf_node_relation的module_id做判断,用于支持一条单据走两个不同的工作流---zhiwen.you
      // 2008-03-03
      //modify by liuzw 20120410 增加对多财政多年度的支持
      StringBuffer judge_sql = new StringBuffer();
      judge_sql.append("select distinct t.wf_id,t.current_node_id,t.next_node_id,t.remark ")
        .append(" from sys_wf_end_tasks t,sys_wf_nodes n,vw_sys_wf_node_relation d").append(" where t.wf_id = n.wf_id")
        .append(" and t.wf_id = d.wf_id")
        .append(" and t.current_node_id = d.node_id and t.rg_code=? and t.set_year=? ").append(" and d.module_id = ?")
        .append(" and n.node_type = '003'").append(" and t.next_node_id = n.node_id").append("  and t.entity_id = ?");
      rs = dao.findBySql(judge_sql.toString(), new Object[] { rg_code, setYear, moduleid, entityId });
      // 如果不是撤销确认结点的操作，按照原来的走，如果是撤销确认结点的操作，则按改动的操作走
      if (rs.size() == 0) {
        // 不是撤销确认节点的操作，所以按照开始的走下去
        // 查询是否可以进行撤销操作
        //modify by liuzw 20120410 增加对多财政多年度的支持
        select_sql
          .append(
            "select distinct b.wf_id, b.current_node_id, b.next_node_id, b.remark ,b.action_type_code,b.operation_date")
          .append(" from sys_wf_current_tasks")
          .append(Tools.addDbLink())
          .append(" b, vw_sys_wf_node_relation")
          .append(Tools.addDbLink())
          .append(" d ")
          .append(
            " where b.rg_code=? and b.set_year=? and d.module_id = ? and d.role_id = ? and b.current_node_id=d.node_id ")
          .append(" and b.entity_id =? ")
          .append(" and not exists(select 1 from sys_wf_current_tasks ct where ct.current_node_id in(")
          .append(
            " select distinct mt.next_node_id from sys_wf_complete_tasks mt where mt.current_node_id = b.current_node_id and mt.action_type_code in('INPUT','NEXT') and mt.entity_id=b.entity_id ) and ct.entity_id=b.entity_id)");
        //						
        //						//如果数据已经被下一岗修改过(例如：计划审核-修改)，那么要在本岗(例如：计划录入)做撤消，则要增加如下查询逻辑：
        //						.append(" union all ")
        //						.append(" select distinct c.wf_id, c.current_node_id, c.next_node_id, c.remark, c.action_type_code, c.operation_date")
        //						.append("   from sys_wf_current_tasks b, sys_wf_complete_tasks c, vw_sys_wf_node_relation d, sys_wf_task_routing r")
        //						.append("  where d.module_id = ? ")
        //						.append("    and d.role_id = ? ")
        //						.append("    and c.current_node_id = d.node_id ")
        //						.append("    and c.wf_id = b.wf_id ")
        //						.append("    and c.next_node_id = b.current_node_id ")
        //						.append("    and b.action_type_code = 'EDIT' ")
        //						.append("    and b.task_id = r.next_task_id ")
        //						.append("    and c.task_id = r.task_id ")
        //						.append("    and c.entity_id = b.entity_id ")
        //						.append("    and b.entity_id = ? ")
        //						.append("    and not exists ")
        //						.append("  		(select 1 from sys_wf_current_tasks ct ")
        //						.append("                where ct.current_node_id in ")
        //						.append("                     (select distinct mt.next_node_id ")
        //						.append("                        from sys_wf_complete_tasks mt ")
        //						.append("                       where mt.current_node_id = b.current_node_id ")
        //						.append("                         and mt.action_type_code in ('INPUT', 'NEXT') ")
        //						.append("                         and mt.entity_id = b.entity_id) ")
        //						.append("                         and ct.entity_id = b.entity_id) ");		

        rs = dao.findBySql(select_sql.toString(), new Object[] { rg_code, setYear, moduleid, roleid, entityId });

        // 如果查找不到时，判断当前做撤销的数据是否是在开始节点上做的撤销
        if (rs.size() == 0) {
          //modify by liuzw 20120410 增加对多财政多年度的支持
          StringBuffer selectStartNodeSql = new StringBuffer();
          selectStartNodeSql.append("select distinct t.wf_id, t.current_node_id, t.next_node_id, t.remark ")
            .append(" from sys_wf_current_tasks").append(Tools.addDbLink()).append(" t, sys_wf_nodes")
            .append(Tools.addDbLink()).append(" n ")
            .append(" where t.rg_code=? and t.set_year=? and t.wf_id = n.wf_id ").append(" and n.node_type = '001' ")
            .append(" and t.current_node_id = n.node_id ").append(" and t.entity_id = ? ");
          rs = dao.findBySql(selectStartNodeSql.toString(), new Object[] { rg_code, setYear, entityId });
          has_old_dto = false;
          if (rs.size() > 0)
            throw new Exception("不能在开始节点做撤销操作!");
        }
        //ymj 放入需要撤销的节点
        List nodes = new ArrayList();
        if (rs.size() == 0) {
          throw new Exception("不能走入下一流程节点，原因有：未找到该数据；或者相邻节点数据已送审；或者该角色没有此权限!");
        } else {
          // 得到当前需要撤销的数据的流程号和节点号
          wf_id = ((Map) rs.get(0)).get("wf_id").toString();
          current_node_id = ((Map) rs.get(0)).get("current_node_id").toString();
          nodes.add(current_node_id);
          //ymj 根据operation_date来判断 撤销回退到的节点为同步节点时 需要撤销回退其他的相邻节点 用时间来判断不是很准确 因为只精确到秒 暂时想不出好的办法代替
          String next_node_id = ((Map) rs.get(0)).get("next_node_id").toString();
          String action_type_code = ((Map) rs.get(0)).get("action_type_code").toString();
          String operation_date = ((Map) rs.get(0)).get("operation_date").toString();
          if (action_type_code.equalsIgnoreCase("back") && isGatherNode(next_node_id).equalsIgnoreCase("0")) {
            //modify by liuzw 20120410 增加对多财政多年度的支持
            String sql = "select current_node_id from sys_wf_current_tasks where rg_code=? and set_year=? and entity_id=? and next_node_id=? and current_node_id<>? and operation_date=? and action_type_code='BACK'";
            List rs_temp = dao.findBySql(sql, new Object[] { rg_code, setYear, entityId, next_node_id, current_node_id,
              operation_date });
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
        return_dto = (Object) inputFVoucherDto;
        Iterator it_nodes = nodes.iterator();
        while (it_nodes.hasNext()) {
          Object obj = it_nodes.next();
          if (obj != null) {
            current_node_id = (String) obj;
            //将当前任务列表的当前数据置上UNDO标志
            //modify by liuzw 20120410 增加对多财政多年度的支持
            update_sql = new StringBuffer();
            update_sql.append("update  sys_wf_current_tasks").append(Tools.addDbLink())
              .append("  b set b.is_undo=1 where b.rg_code=? and b.set_year=? and b.entity_id =? ")
              .append(" and b.wf_id=? and b.current_node_id=? ");
            num = dao.executeBySql(update_sql.toString(), new Object[] { rg_code, setYear, entityId, wf_id,
              current_node_id });
            if (num == 0) {
              throw new Exception("不能走入下一流程节点，系统内部错误!");
            }

            // 如果能找到上一节点数据
            if (has_old_dto) {
              // 将完成任务列表中的上一节点的数据复制到当前任务列表
              //modify by liuzw 20120410 增加对多财政多年度的支持
              StringBuffer insert_old_sql = new StringBuffer();
              StringBuffer delete_old_sql = new StringBuffer();
              insert_old_sql.append("	insert into sys_wf_current_tasks").append(Tools.addDbLink())
                .append(" 	select * from sys_wf_complete_tasks").append(Tools.addDbLink())
                .append(" c  where c.rg_code=? and c.set_year=? and c.task_id in (")
                .append(" select b.task_id from sys_wf_current_tasks").append(Tools.addDbLink())
                .append(" a,sys_wf_task_routing").append(Tools.addDbLink()).append(" b")
                .append(" where a.entity_id =?").append(" and a.wf_id=?")
                .append(" and a.current_node_id=? and a.task_id=b.next_task_id and a.rg_code=? and a.set_year=?)");
              num = dao.executeBySql(insert_old_sql.toString(), new Object[] { rg_code, setYear, entityId, wf_id,
                current_node_id, rg_code, setYear });
              if (num < 1)
                throw new Exception("不能走入下一流程节点，系统内部错误!");

              //modify by liuzw 20120410 增加对多财政多年度的支持
              delete_old_sql
                .append("	delete from sys_wf_complete_tasks")
                .append(Tools.addDbLink())
                .append(" c  where c.rg_code=? and c.set_year=? and c.task_id in (")
                .append(" select b.task_id from sys_wf_current_tasks")
                .append(Tools.addDbLink())
                .append(" a,sys_wf_task_routing")
                .append(Tools.addDbLink())
                .append(" b")
                .append(
                  " where a.rg_code=? and a.set_year=? and a.entity_id =? and a.wf_id=? and a.current_node_id=? and a.task_id=b.next_task_id and a.is_undo = 1)");
              num = dao.executeBySql(delete_old_sql.toString(), new Object[] { rg_code, setYear, rg_code, setYear,
                entityId, wf_id, current_node_id });
              if (num < 1)
                throw new Exception("不能走入下一流程节点，系统内部错误!");
            }

            // 将当前任务列表中的UNDO的数据复制到完成列表中
            //modify by liuzw 20120410 增加对多财政多年度的支持
            insert_sql = new StringBuffer();
            insert_sql
              .append("insert into sys_wf_complete_tasks")
              .append(Tools.addDbLink())
              .append(" (TASK_ID,WF_ID,WF_TABLE_NAME,ENTITY_ID,")
              .append("CURRENT_NODE_ID,NEXT_NODE_ID,ACTION_TYPE_CODE,IS_UNDO,CREATE_USER,CREATE_DATE,UNDO_USER,")
              .append("UNDO_DATE,OPERATION_NAME,OPERATION_DATE,OPERATION_REMARK,INIT_MONEY,RESULT_MONEY,REMARK,")
              .append("TOLLY_FLAG,BILL_TYPE_CODE,BILL_ID,RCID,CCID,CREATE_USER_ID,RG_CODE,SET_YEAR) ")
              .append(" select TASK_ID,	WF_ID,	WF_TABLE_NAME,	ENTITY_ID,	CURRENT_NODE_ID,	NEXT_NODE_ID,")
              .append(" ACTION_TYPE_CODE,	1,	CREATE_USER,	CREATE_DATE,	'")
              .append(create_user)
              .append("',	'")
              .append(Tools.getCurrDate())
              .append("',")
              .append(
                "	OPERATION_NAME,	OPERATION_DATE,	OPERATION_REMARK,	INIT_MONEY,	RESULT_MONEY,	REMARK ,TOLLY_FLAG , BILL_TYPE_CODE,bill_id,rcid,ccid,CREATE_USER_ID,RG_CODE,SET_YEAR")
              .append(" from   sys_wf_current_tasks").append(Tools.addDbLink())
              .append("   where  rg_code=? and set_year=? and entity_id ='").append(entityId).append("'  and wf_id='")
              .append(wf_id).append("'  and current_node_id='").append(current_node_id).append("' and is_undo=1");
            num = dao.executeBySql(insert_sql.toString(), new Object[] { rg_code, setYear });
            if (num == 0) {
              throw new Exception("不能走入下一流程节点，系统内部错误!");
            }

            // 删除当前任务列表中的UNDO的数据
            //modify by liuzw 20120410 增加对多财政多年度的支持
            delete_sql = new StringBuffer();
            delete_sql
              .append("delete from  sys_wf_current_tasks")
              .append(Tools.addDbLink())
              .append(
                "  b where b.rg_code=? and b.set_year=? and b.entity_id =? and b.wf_id=? and b.current_node_id=? and is_undo=1");
            num = dao.executeBySql(delete_sql.toString(), new Object[] { rg_code, setYear, entityId, wf_id,
              current_node_id });
            if (num == 0) {
              throw new Exception("不能走入下一流程节点，系统内部错误!");
            }

            /**
             * add by bing-zeng 2008-02-04 对工作流ITEM表进行操作 begin
             */
            //modify by liuzw 20120410 增加对多财政多年度的支持
            String sql4OperateItemTable = "select task_id,CURRENT_NODE_ID, NEXT_NODE_ID,BILL_ID from sys_wf_current_tasks "
              + " where rg_code=? and set_year=? and entity_id =? and wf_id=? and next_node_id=?  ";

            List list = this.dao.findBySql(sql4OperateItemTable, new Object[] { rg_code, setYear, entityId, wf_id,
              current_node_id });

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
                  inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid(), inputFVoucherDto.toXMLData());
              }
            } else {
              //modify by liuzw 20120410 增加对多财政多年度的支持
              sql4OperateItemTable = "select task_id,CURRENT_NODE_ID, NEXT_NODE_ID,BILL_ID from sys_wf_current_tasks "
                + " where rg_code=? and set_year=? and  entity_id =? and wf_id=? and current_node_id=?  ";

              list = this.dao.findBySql(sql4OperateItemTable, new Object[] { rg_code, setYear, entityId, wf_id,
                current_node_id });
              Iterator it = list.iterator();
              while (it.hasNext()) {
                map = (Map) it.next();
                if (null != map.get("bill_id")) {
                  billId = map.get("bill_id").toString();
                }
                saveOptionCurrentAndComleteTable(null, map.get("task_id").toString(), wf_id, entityId,
                  map.get("current_node_id").toString(), map.get("next_node_id").toString(), "RECALL", billId,
                  inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid(), inputFVoucherDto.toXMLData());
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
        //modify by liuzw 20120410 增加对多财政多年度的支持
        select_sql
          .append("select distinct b.wf_id, b.current_node_id, b.next_node_id, b.remark ")
          .append(" from sys_wf_end_tasks")
          .append(Tools.addDbLink())
          .append(" b, vw_sys_wf_node_relation")
          .append(Tools.addDbLink())
          .append(" d ")
          .append(
            " where b.rg_code=? and b.set_year=? and d.module_id = ? and d.role_id = ? and b.current_node_id=d.node_id ")
          .append(" and b.entity_id =? ");
        rs = dao.findBySql(select_sql.toString(), new Object[] { rg_code, setYear, moduleid, roleid, entityId });

        if (rs.size() == 0) {
          throw new Exception("不能走入下一流程节点，原因有：未找到该数据；或者该角色没有此权限!");
        } else {
          // 得到当前需要撤销的数据的流程号和节点号
          wf_id = ((Map) rs.get(0)).get("wf_id").toString();
          current_node_id = ((Map) rs.get(0)).get("current_node_id").toString();
        }

        return_dto = (Object) inputFVoucherDto;

        // 将以确认任务列表的当前数据置上UNDO标志
        //modify by liuzw 20120410 增加对多财政多年度的支持
        update_sql.append("update  sys_wf_end_tasks").append(Tools.addDbLink())
          .append("  b set b.is_undo=1 where b.rg_code=? and b.set_year=? and b.entity_id =? ")
          .append(" and b.wf_id=? and b.current_node_id=? ");
        num = dao.executeBySql(update_sql.toString(),
          new Object[] { rg_code, setYear, entityId, wf_id, current_node_id });
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 如果能找到上一节点数据
        if (has_old_dto) {
          // 将完成任务列表中的上一节点的数据复制到当前任务列表
          StringBuffer insert_old_sql = new StringBuffer();
          StringBuffer delete_old_sql = new StringBuffer();
          // 即使撤销的是以确认的数据，但是撤销后它的next_node_id不是结束结点，所以把数据复制到当前任务列表
          //modify by liuzw 20120410 增加对多财政多年度的支持
          insert_old_sql.append("	insert into sys_wf_current_tasks").append(Tools.addDbLink())
            .append(" 	select * from sys_wf_complete_tasks").append(Tools.addDbLink())
            .append(" c  where c.rg_code = ? and c.set_year=? and c.task_id in (")
            .append(" select b.task_id from sys_wf_end_tasks").append(Tools.addDbLink())
            .append(" a,sys_wf_task_routing").append(Tools.addDbLink()).append(" b")
            .append(" where a.rg_code=? and a.set_year=? and a.entity_id =?").append(" and a.wf_id=?")
            .append(" and a.current_node_id=? and a.task_id=b.next_task_id)");
          num = dao.executeBySql(insert_old_sql.toString(), new Object[] { rg_code, setYear, rg_code, setYear,
            entityId, wf_id, current_node_id });
          if (num < 1)
            throw new Exception("不能走入下一流程节点，系统内部错误!");

          // 删除完成任务列表中的上一节点的数据
          //modify by liuzw 20120410 增加对多财政多年度的支持
          delete_old_sql
            .append("	delete from sys_wf_complete_tasks")
            .append(Tools.addDbLink())
            .append(" c  where c.rg_code=? and c.set_year=? and c.task_id in (")
            .append(" select b.task_id from sys_wf_end_tasks")
            .append(Tools.addDbLink())
            .append(" a,sys_wf_task_routing")
            .append(Tools.addDbLink())
            .append(" b")
            .append(
              " where a.rg_code=? and a.set_year=? and a.entity_id =? and a.wf_id=? and a.current_node_id=? and a.task_id=b.next_task_id and a.is_undo = 1 )");
          num = dao.executeBySql(delete_old_sql.toString(), new Object[] { rg_code, setYear, rg_code, setYear,
            entityId, wf_id, current_node_id });
          if (num < 1)
            throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 将以确认任务列表中的UNDO的数据复制到完成列表中
        //modify by liuzw 20120410 增加对多财政多年度的支持
        insert_sql
          .append("insert into sys_wf_complete_tasks")
          .append(Tools.addDbLink())
          .append(" (TASK_ID,WF_ID,WF_TABLE_NAME,ENTITY_ID,")
          .append("CURRENT_NODE_ID,NEXT_NODE_ID,ACTION_TYPE_CODE,IS_UNDO,CREATE_USER,CREATE_DATE,UNDO_USER,")
          .append("UNDO_DATE,OPERATION_NAME,OPERATION_DATE,OPERATION_REMARK,INIT_MONEY,RESULT_MONEY,REMARK,")
          .append("TOLLY_FLAG,BILL_TYPE_CODE,BILL_ID,RCID,CCID,CREATE_USER_ID,RG_CODE,SET_YEAR) ")
          .append(" select TASK_ID,	WF_ID,	WF_TABLE_NAME,	ENTITY_ID,	CURRENT_NODE_ID,	NEXT_NODE_ID,")
          .append(" ACTION_TYPE_CODE,	1,	CREATE_USER,	CREATE_DATE,	'")
          .append(create_user)
          .append("',	'")
          .append(Tools.getCurrDate())
          .append("',")
          .append(
            "	OPERATION_NAME,	OPERATION_DATE,	OPERATION_REMARK,	INIT_MONEY,	RESULT_MONEY,	REMARK ,TOLLY_FLAG , BILL_TYPE_CODE,bill_id,rcid,ccid,CREATE_USER_ID,RG_CODE,SET_YEAR")
          .append(" from   sys_wf_end_tasks").append(Tools.addDbLink()).append("   where  entity_id ='")
          .append(entityId).append("'  and wf_id='").append(wf_id).append("'  and current_node_id='")
          .append(current_node_id).append("' and is_undo=1 and rg_code ='" + rg_code + "' and set_year =" + setYear);
        num = dao.executeBySql(insert_sql.toString());
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }

        // 删除以确认任务列表中的UNDO的数据
        //modify by liuzw 20120410 增加对多财政多年度的支持
        delete_sql
          .append("delete from  sys_wf_end_tasks")
          .append(Tools.addDbLink())
          .append(
            "  b where b.rg_code =? and b.set_year=? and b.entity_id =? and b.wf_id=? and b.current_node_id=? and is_undo=1");
        num = dao.executeBySql(delete_sql.toString(),
          new Object[] { rg_code, setYear, entityId, wf_id, current_node_id });
        if (num == 0) {
          throw new Exception("不能走入下一流程节点，系统内部错误!");
        }
        /**
         * add by bing-zeng 2008-02-04 对工作流ITEM表进行操作 begin
         */
        //modify by liuzw 20120410 增加对多财政多年度的支持
        String sql4OperateItemTable = "select task_id,CURRENT_NODE_ID, NEXT_NODE_ID,BILL_ID from sys_wf_current_tasks "
          + " where rg_code=? and set_year =? and entity_id =? and wf_id=? and next_node_id=?  ";

        List list = this.dao.findBySql(sql4OperateItemTable, new Object[] { rg_code, setYear, entityId, wf_id,
          current_node_id });
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
              inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid(), inputFVoucherDto.toXMLData());
          }
        } else {
          //modify by liuzw 20120410 增加对多财政多年度的支持
          sql4OperateItemTable = "select task_id,CURRENT_NODE_ID, NEXT_NODE_ID,BILL_ID from sys_wf_current_tasks "
            + " where rg_code=? and set_year=? and entity_id =? and wf_id=? and current_node_id=?  ";

          list = this.dao.findBySql(sql4OperateItemTable, new Object[] { rg_code, setYear, entityId, wf_id,
            current_node_id });
          if (list.size() > 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
              map = (Map) it.next();
              if (null != map.get("bill_id")) {
                billId = map.get("bill_id").toString();
              }
              saveOptionCurrentAndComleteTable(null, map.get("task_id").toString(), wf_id, entityId,
                map.get("current_node_id").toString(), map.get("next_node_id").toString(), "RECALL", billId,
                inputFVoucherDto.getRcid(), inputFVoucherDto.getCcid(), inputFVoucherDto.toXMLData());
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
   * @param taskId
   *            当前的任务ID
   * @param wfId
   *            工作流ID
   * @param entityId
   *            工作流实体ID
   * @param currentNodeId
   *            当前节点
   * @param nextNodeId
   *            下一个节点
   * @param actionType
   *            操作类型
   * @param billId
   * @param rcid
   * @param ccid
   * 
   * @author ymj
   * @throws Exception
   * @since 1.0
   * 
   */
  private void saveOptionCurrentAndComleteTable(Statement setRoutingPsmt, String taskId, String wfId, String entityId,
    String currentNodeId, String nextNodeId, String actionType, String billId, String rcid, String ccid, XMLData rowData)
    throws Exception {

    //add by liuzw 20120410 增加对多财政多年度的支持
    String rg_code = getRgCode();
    String setYear = getSetYear();
    // 同步操作时查询是否满足走入下一个节点.
    StringBuffer sqlBuffer = new StringBuffer();

    StringBuffer gatherBuffer = new StringBuffer();

    String flag = getValidGatherNode(sqlBuffer, gatherBuffer, nextNodeId, currentNodeId, entityId, billId, wfId, rcid,
      ccid, rowData);

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
      delete4CurrentOrCompleteItems4Recall(setRoutingPsmt, taskId, currentNodeId, nextNodeId, entityId);

      // 同步操作 如果查询信息返回的集合小于1 不能进入下一个节点

      if (flag.equals("0")) {
        //				撤销退回的判断
        //modify by liuzw 20120410 增加对多财政多年度的支持
        String actionSql = "select * from sys_wf_current_tasks where task_id = ? and rg_code = ? and set_year =?";
        List tempList = this.dao.findBySql(actionSql, new Object[] { taskId, rg_code, setYear });

        // 当前任务的操作类型
        String actionTypeCode = "0";
        if (tempList.size() > 0) {
          XMLData x = (XMLData) tempList.get(0);
          actionTypeCode = x.get("action_type_code").toString();
        }
        String sql = "";
        //modify by liuzw 20120410 增加对多财政多年度的支持
        if (actionTypeCode.equals("BACK")) {
          if (isEdit) {
            sql = "insert into sys_wf_current_item  select entity_id, bill_id, current_node_id, '004', rcid, ccid,rg_code,setYear "
              + " from sys_wf_current_tasks where  entity_id = ?  and task_id = ? and rg_code = ? and set_year = ?";
          } else {
            sql = "insert into sys_wf_current_item  select entity_id, bill_id, next_node_id, '004', rcid, ccid,rg_code,setYear "
              + " from sys_wf_current_tasks where  entity_id = ?  and task_id = ? and rg_code = ? and set_year = ?";
          }
        } else {
          if (isEdit) {
            sql = "insert into sys_wf_current_item  select entity_id, bill_id, current_node_id, '001', rcid, ccid,rg_code,setYear  "
              + " from sys_wf_current_tasks where  entity_id = ?  and task_id = ? and rg_code = ? and set_year = ?";
          } else {
            sql = "insert into sys_wf_current_item  select entity_id, bill_id, next_node_id, '001', rcid, ccid,rg_code,setYear  "
              + " from sys_wf_current_tasks where  entity_id = ?  and task_id = ? and rg_code = ? and set_year = ?";
          }
        }
        this.dao.executeBySql(sql, new Object[] { entityId, taskId, rg_code, setYear });

        //ymj 传入actiontyep
        add4CurrentOrCompleteItems4Recall(setRoutingPsmt, actionTypeCode, entityId, billId, currentNodeId, nextNodeId,
          "001", rcid, ccid);
      } else {
        //撤销退回的判断
        //modify by liuzw 20120410 增加对多财政多年度的支持
        String actionSql = "select * from sys_wf_current_tasks where task_id = ? and rg_code = ? and set_year = ?";
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

    }
  }

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
    throws FAppException {
    //add by liuzw 20120410 增加对多财政多年度的支持
    String rg_code = getRgCode();
    String setYear = getSetYear();
    //查询是否存在多分支数据，并且是来源于一个同步节点。
    //modify by liuzw 20120410 增加对多财政多年度的支持
    StringBuffer sqlSb = new StringBuffer();
    sqlSb.append("select a.current_node_id ")
      .append("  from (select distinct comt.wf_id, comt.current_node_id, comt.next_node_id ")
      .append("  		from sys_wf_complete_tasks comt ").append(" 		   where comt.wf_id = ? ")
      .append("   		 and comt.entity_id = ? and comt.rg_code =? and comt.set_year=? ")
      .append("   		 and comt.action_type_code = 'NEXT' ").append("		  union all ")
      .append("		  select distinct curt.wf_id, curt.current_node_id, curt.next_node_id ")
      .append("  		from sys_wf_current_tasks curt ").append(" 		   where curt.wf_id = ? ")
      .append("   		 and curt.entity_id = ? and curt.rg_code = ? and curt.set_year = ? ")
      .append("   		 and curt.action_type_code = 'NEXT') a, ").append("       sys_wf_nodes nodes ")
      .append(" where a.wf_id = nodes.wf_id ").append("   and a.current_node_id = nodes.node_id ")
      .append("   and nodes.gather_flag = 0 ").append(" group by a.current_node_id ").append("having count(1) > 1 ")
      .append("and exists (select 1 ").append("          from (select rownum rn, b.* ")
      .append("                  from (select distinct curt1.wf_id, curt1.next_node_id ")
      .append("                          from sys_wf_current_tasks curt1 ")
      .append("                         where curt1.wf_id = ? ")
      .append("                           and curt1.entity_id = ? and curt1.rg_code = ? and curt1.set_year=?) b) c ")
      .append("         where c.rn > 1) ");
    List list = dao.findBySql(sqlSb.toString(), new Object[] { wfId, entityId, rg_code, setYear, wfId, entityId,
      rg_code, setYear, wfId, entityId, rg_code, setYear });
    //
    for (int i = 0; list != null && i < list.size(); i++) {
      String fromNode = (String) ((Map) list.get(i)).get("current_node_id");
      //判断当前节点是否来源于一个同步节点
      if (isCanGoRoute(wfId, fromNode, thisNode, "001", bizData)) {
        return true;
      }
    }

    return false;
  }

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
    throws FAppException {
    //查询fromNode的下一步节点
    StringBuffer sqlSb = new StringBuffer();
    sqlSb.append("select ncs.* ").append("  from sys_wf_node_conditions ncs, sys_wf_nodes nodes ")
      .append(" where ncs.wf_id = ? ").append("   and ncs.node_id = ? ").append("   and ncs.routing_type = ? ")
      .append("   and ncs.wf_id = nodes.wf_id ").append("   and ncs.next_node_id = nodes.node_id ");
    if (routingType.equals("001")) {//正向流转不包含结束节点
      sqlSb.append(" and nodes.node_type <> '003' ");
    }
    if (routingType.equals("002")) {//反向流转不包含开始节点
      sqlSb.append(" and nodes.node_type <> '001' ");
    }

    //判断线路是否可行
    List list = dao.findBySql(sqlSb.toString(), new Object[] { wfId, fromNode, routingType });
    for (int i = 0; list != null && i < list.size(); i++) {
      Map map = (Map) list.get(i);
      String nextNodeId = (String) map.get("next_node_id");
      //			String conditionId = (String)map.get("condition_id");
      if (nextNodeId.equals(toNode)) {
        //				if (conditionId.equals("#") || getValidWfNode(conditionId, bizData)) {
        return true;
        //				}
      } else {
        if (isCanGoRoute(wfId, nextNodeId, toNode, routingType, bizData))
          return true;
      }
    }

    return false;
  }

  /**
   * 得到系统业务年度
   * 
   * @return
   * 
   * add by liuzw 20120410
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
   * 得到行政区划代码
   * 
   * @return
   * 
   * add by liuzw 20120410
   */
  private String getRgCode() {

    String rg_code = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");
    return rg_code;
  }
}
