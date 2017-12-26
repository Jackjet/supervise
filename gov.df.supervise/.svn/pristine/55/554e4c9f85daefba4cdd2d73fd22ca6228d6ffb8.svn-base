package gov.df.supervise.service.examine;

import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.supervise.api.examine.ExamineService;
import gov.df.supervise.bean.examine.CsofSupBillEntity;
import gov.df.supervise.service.common.SessionUtilEx;
import gov.df.supervise.service.workflow.workFlowDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ufgov.ip.apiUtils.UUIDTools;

/**
 * 监管审核
 * @author tongya
 *
 */
@Service
public class ExamineServiceimp implements ExamineService {
  @Autowired
  private ExamineDao csDao;

  @Autowired
  private workFlowDao workFlowdao;

  public String getYear() {
    return SessionUtilEx.getLoginYear();
  }

  public String getRgCode() {
    return SessionUtilEx.getRgCode();
  }

  // 列表数据的展示
  @SuppressWarnings("null")
  public List selectAll(String chr_code, String chr_id, String type, String oid, String status, String billtype_code,
    String menu_id, String task_id) throws Exception {
    Map<String, Object> billdata = workFlowdao.selectTableName(billtype_code);
    String table_name = billdata.get("TABLE_NAME").toString();
    List data = null;
    String user_id = (String) SessionUtil.getUserInfoContext().getUserID();
    String org_code = (String) SessionUtil.getUserInfoContext().getOrgCode();//获取org_code作为专员办id(oid)
    Map<String, Object> param = new HashMap<String, Object>();
    param.put("menu_id", menu_id);
    param.put("billtype_code", billtype_code);
    Map<String, Object> res = csDao.SelectMenuNode(param);
    String from_node_id = res.get("FROM_NODE_ID").toString();
    String cur_node_id = res.get("CUR_NODE_ID").toString();
    Map<String, Object> map = workFlowdao.SelectNode(cur_node_id);
    String node_type = map.get("NODE_TYPE").toString();
    if (type == null || type.equals("")) {
      if (status != null && status.equals("9")) {
        if (node_type != null && node_type.equals("2")) {
          param.put("table_name", table_name);
          param.put("cur_node_id", cur_node_id);
          param.put("from_node_id", from_node_id);
          param.put("oid", org_code);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.getwfendAllBill(param);
        } else {
          param.put("table_name", table_name);
          param.put("cur_node_id", cur_node_id);
          param.put("from_node_id", from_node_id);
          param.put("oid", org_code);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.getwfAllBill(param);
        }
      } else if (status != null && status.equals("0")) {
        param.put("table_name", table_name);
        param.put("cur_node_id", cur_node_id);
        param.put("from_node_id", from_node_id);
        param.put("oid", org_code);
        param.put("user_id", user_id);
        param.put("task_id", task_id);
        data = csDao.getwfBill(param);
      } else if (status != null && status.equals("1")) {
        if (node_type != null && node_type.equals("2")) {
          param.put("table_name", table_name);
          param.put("cur_node_id", cur_node_id);
          param.put("oid", org_code);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.getwfendBillEnd(param);
        } else {
          param.put("table_name", table_name);
          param.put("cur_node_id", cur_node_id);
          param.put("oid", org_code);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.getwfBillEnd(param);
        }
      }
    } else if (type != null && type.equals("AGENCY")) {
      if (status != null && status.equals("9")) {
        if (node_type != null && node_type.equals("2")) {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("from_node_id", from_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectendAgencyAll(param);
        } else {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("from_node_id", from_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectAgencyAll(param);
        }
      } else if (status != null && status.equals("0")) {
        param.put("table_name", table_name);
        param.put("chr_code", chr_code);
        param.put("oid", oid);
        param.put("cur_node_id", cur_node_id);
        param.put("from_node_id", from_node_id);
        param.put("user_id", user_id);
        param.put("task_id", task_id);
        data = csDao.selectwfAgency(param);
      } else if (status != null && status.equals("1")) {
        if (node_type != null && node_type.equals("2")) {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectendAgencyEnd(param);
        } else {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectAgencyEnd(param);
        }
      }

    } else if (type != null && type.equals("MOFDEP")) {
      if (status != null && status.equals("9")) {
        if (node_type != null && node_type.equals("2")) {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("from_node_id", from_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectendMofdepAll(param);
        } else {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("from_node_id", from_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectMofdepAll(param);
        }
      } else if (status != null && status.equals("0")) {
        param.put("table_name", table_name);
        param.put("chr_code", chr_code);
        param.put("oid", oid);
        param.put("cur_node_id", cur_node_id);
        param.put("from_node_id", from_node_id);
        param.put("user_id", user_id);
        param.put("task_id", task_id);
        data = csDao.selectwfMofdep(param);
      } else if (status != null && status.equals("1")) {
        if (node_type != null && node_type.equals("2")) {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectendMofdepEnd(param);
        } else {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectMofdepEnd(param);
        }
      }
    } else if (type != null && type.equals("DEP")) {
      if (status != null && status.equals("9")) {
        if (node_type != null && node_type.equals("2")) {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("from_node_id", from_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectendDepAll(param);
        } else {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("from_node_id", from_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectDepAll(param);
        }
      } else if (status != null && status.equals("0")) {
        param.put("table_name", table_name);
        param.put("chr_code", chr_code);
        param.put("oid", oid);
        param.put("cur_node_id", cur_node_id);
        param.put("from_node_id", from_node_id);
        param.put("user_id", user_id);
        param.put("task_id", task_id);
        data = csDao.selectwfDep(param);
      } else if (status != null && status.equals("1")) {
        if (node_type != null && node_type.equals("2")) {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectendDepEnd(param);
        } else {
          param.put("table_name", table_name);
          param.put("chr_code", chr_code);
          param.put("oid", oid);
          param.put("cur_node_id", cur_node_id);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectDepEnd(param);
        }
      }
    } else if (type != null && type.equals("OFFICE")) {
      if (status != null && status.equals("9")) {
        if (node_type != null && node_type.equals("2")) {
          param.put("table_name", table_name);
          param.put("chr_id", chr_id);
          param.put("cur_node_id", cur_node_id);
          param.put("from_node_id", from_node_id);
          param.put("oid", oid);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectendOfficeAll(param);
        } else {
          param.put("table_name", table_name);
          param.put("chr_id", chr_id);
          param.put("cur_node_id", cur_node_id);
          param.put("from_node_id", from_node_id);
          param.put("oid", oid);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectOfficeAll(param);
        }
      } else if (status != null && status.equals("0")) {
        param.put("table_name", table_name);
        param.put("chr_id", chr_id);
        param.put("cur_node_id", cur_node_id);
        param.put("from_node_id", from_node_id);
        param.put("oid", oid);
        param.put("user_id", user_id);
        param.put("task_id", task_id);
        data = csDao.selectwfOffice(param);
      } else if (status != null && status.equals("1")) {
        if (node_type != null && node_type.equals("2")) {
          param.put("table_name", table_name);
          param.put("chr_id", chr_id);
          param.put("cur_node_id", cur_node_id);
          param.put("oid", oid);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectendOfficeEnd(param);
        } else {
          param.put("table_name", table_name);
          param.put("chr_id", chr_id);
          param.put("cur_node_id", cur_node_id);
          param.put("oid", oid);
          param.put("user_id", user_id);
          param.put("task_id", task_id);
          data = csDao.selectOfficeEnd(param);
        }
      }
    }
    return data;
  }

  //监管内容录入界面的删除操作
  public void update(String id, String is_valid) {
    Map<String, String> param = new HashMap<String, String>();
    param.put("id", id);
    param.put("is_valid", is_valid);
    csDao.update(param);
  }

  //处汇总操作
  public void doCollect(String billtype_code, String data, int total, String bill_no) throws Exception {
    Map<String, Object> param = new HashMap<String, Object>();
    CsofSupBillEntity Data = JSONObject.parseObject(data, CsofSupBillEntity.class);
    String billtype_name = csDao.selectBillName(billtype_code);
    int num;
    if (Data.getSup_cycle() == 0) {
      param.put("id", Data.getId());
      param.put("task_id", Data.getTask_id());
      num = csDao.getEndMofDepCount(param);
    } else {
      param.put("id", Data.getId());
      param.put("task_id", Data.getTask_id());
      num = csDao.getMofDepCount(param);
    }

    String date = Tools.getCurrDate();
    String user = (String) SessionUtil.getUserInfoContext().getUserID();
    if (num == 0) {
      int sup_money = csDao.countSupMoney(Data.getMofdep_id());
      int sup_num = csDao.countSupNum(Data.getMofdep_id());
      String id = UUIDTools.uuidRandom(); // 自动生成id
      param.put("id", id);
      param.put("bill_no", bill_no);
      param.put("dep_id", Data.getDep_id());
      param.put("dep_code", Data.getDep_code());
      param.put("dep_name", Data.getDep_name());
      param.put("mofdep_id", Data.getMofdep_id());
      param.put("mofdep_code", Data.getMofdep_code());
      param.put("mofdep_name", Data.getMofdep_name());
      param.put("task_id", Data.getTask_id());
      param.put("task_name", Data.getTask_name());
      param.put("task_no", Data.getTask_no());
      param.put("sum_type", 1);
      param.put("oid", Data.getOid());
      param.put("sid", Data.getSid());
      param.put("sup_no", Data.getSup_no());
      param.put("sup_name", Data.getSup_name());
      param.put("billtype_code", billtype_code);
      param.put("billtype_name", billtype_name);
      param.put("status", 0);
      param.put("is_valid", Data.getIs_valid());
      param.put("is_end", 0);
      param.put("set_year", getYear());
      param.put("rg_code", getRgCode());
      param.put("sup_num", sup_num);
      param.put("sup_money", sup_money);
      param.put("remark", "");
      param.put("create_user", user);
      param.put("create_date", date);
      param.put("latest_op_user", user);
      param.put("latest_op_date", date);
      csDao.insertBillMofDep(param);
      param.put("entity_id", Data.getId());
      param.put("sum_type", 1);
      param.put("mofdep_bill_id", id);
      csDao.updateSupBillBydep(param);
    } else {
      String id = csDao.getId(Data.getMofdep_id());
      param.put("entity_id", Data.getId());
      param.put("sum_type", 1);
      param.put("mofdep_bill_id", id);
      csDao.updateSupBillBydep(param);
    }
  }

  //处撤销汇总
  public void undoSummary(String chr_code, String id, int total) throws Exception {
    Map<String, Object> param = new HashMap<String, Object>();
    String mofdep_bill_id = csDao.getId(chr_code);
    int sum = csDao.getMofDepCount1(mofdep_bill_id);
    if (sum == 1) {
      param.put("entity_id", id);
      param.put("sum_type", 0);
      param.put("mofdep_bill_id", "");
      csDao.updateSupBillBydep(param);
      csDao.deleteBillMofDep(mofdep_bill_id);
    } else {
      param.put("entity_id", id);
      param.put("sum_type", 0);
      param.put("mofdep_bill_id", "");
      csDao.updateSupBillBydep(param);
    }
  }

  //办汇总操作
  public void doOfficeCollect(String billtype_code, String data, int total, String bill_no) throws Exception {
    Map<String, Object> param = new HashMap<String, Object>();
    CsofSupBillEntity Data = JSONObject.parseObject(data, CsofSupBillEntity.class);
    String billtype_name = csDao.selectBillName(billtype_code);
    int num;
    if (Data.getSup_cycle() == 0) {
      param.put("id", Data.getId());
      param.put("task_id", Data.getTask_id());
      num = csDao.getEndOfficeCount(param);
    } else {
      param.put("id", Data.getId());
      param.put("task_id", Data.getTask_id());
      num = csDao.getOfficeCount(param);
    }
    String date = Tools.getCurrDate();
    String user = (String) SessionUtil.getUserInfoContext().getUserID();
    if (num == 0) {
      int sup_money = csDao.countOfficeSupMoney(Data.getOid());
      int sup_num = csDao.countOfficeSupNum(Data.getOid());
      String id = UUIDTools.uuidRandom(); // 自动生成id
      param.put("id", id);
      param.put("bill_no", bill_no);
      param.put("sum_type", 2);
      param.put("oid", Data.getOid());
      param.put("sid", Data.getSid());
      param.put("sup_no", Data.getSup_no());
      param.put("sup_name", Data.getSup_name());
      param.put("billtype_code", billtype_code);
      param.put("billtype_name", billtype_name);
      param.put("task_id", Data.getTask_id());
      param.put("task_name", Data.getTask_name());
      param.put("task_no", Data.getTask_no());
      param.put("status", 0);
      param.put("is_valid", Data.getIs_valid());
      param.put("is_end", 0);
      param.put("set_year", getYear());
      param.put("rg_code", getRgCode());
      param.put("sup_num", sup_num);
      param.put("sup_money", sup_money);
      param.put("remark", "");
      param.put("create_user", user);
      param.put("create_date", date);
      param.put("latest_op_user", user);
      param.put("latest_op_date", date);
      csDao.insertBillOffice(param);
      param.put("entity_id", Data.getId());
      param.put("sum_type", 2);
      param.put("office_bill_id", id);
      csDao.updateSupBillByoffice(param);
    } else {
      String id = csDao.getOfficeId(Data.getOid());
      param.put("entity_id", Data.getId());
      param.put("sum_type", 2);
      param.put("office_bill_id", id);
      csDao.updateSupBillByoffice(param);
    }
  }

  //办撤销汇总
  public void undoOfficeSummary(String chr_id, String id, int total) throws Exception {
    Map<String, Object> param = new HashMap<String, Object>();
    String office_bill_id = csDao.getOfficeId(chr_id);
    int sum = csDao.getOfficeCount1(office_bill_id);
    if (sum == 1) {
      param.put("entity_id", id);
      param.put("sum_type", 1);
      param.put("office_bill_id", "");
      csDao.updateSupBillByoffice(param);
      csDao.deleteBillOffice(office_bill_id);
    } else {
      param.put("entity_id", id);
      param.put("sum_type", 1);
      param.put("office_bill_id", "");
      csDao.updateSupBillByoffice(param);
    }
  }

  //动态按钮获取
  public List getActionButton(String menu_id) {
    return csDao.getActionButton(menu_id);
  }

  //获取汇总数据
  public List selectBill(String billtype_code, String chr_code, String chr_id) {
    Map<String, Object> param = new HashMap<String, Object>();
    List data = null;
    Map<String, Object> billdata = workFlowdao.selectTableName(billtype_code);
    String table_name = billdata.get("TABLE_NAME").toString();
    if (billtype_code != null && billtype_code.equals("102")) {
      param.put("table_name", table_name);
      param.put("chr_code", chr_code);
      data = csDao.selectMofdepBill(param);
    } else {
      param.put("table_name", table_name);
      param.put("chr_id", chr_id);
      data = csDao.selectOfficeBill(param);
    }
    return data;
  }

  //录入数据到csof_sup_bill表里
  public boolean inputSupBill(String sid, String bill_no, String id, String billtype_code, String chr_id,
    String chr_code, String chr_name, String task_id, String sup_date, String dep_id, String mofdep_id) {
    Map<String, Object> param = new HashMap<String, Object>();
    int num;
    param.put("task_id", task_id);
    Map<String, Object> data = csDao.getTaskSup(param);
    // sup_cycle 0: 不定期  允许重复录入监管事项     其它：不允许
    param.put("chr_id", chr_id);
    param.put("task_id", task_id);
    String sup_cycle = data.get("SUP_CYCLE").toString();
    if (sup_cycle.equals("0")) {
      num = csDao.getEndCount(param);
    } else {
      num = csDao.getCount(param);
    }
    if (num == 0) {
      Map<String, Object> billdata = workFlowdao.selectTableName(billtype_code);
      String billtype_name = billdata.get("BILLTYPE_NAME").toString();
      Map<String, Object> res = csDao.getDep(dep_id);
      Map<String, Object> map = csDao.getMofdep(mofdep_id);
      String date = Tools.getCurrDate();
      String user = (String) SessionUtil.getUserInfoContext().getUserID();
      param.put("sid", data.get("SID").toString());
      param.put("sup_no", data.get("SUP_NO").toString());
      param.put("sup_name", data.get("SUP_NAME").toString());
      param.put("mofdep_id", mofdep_id);
      param.put("mofdep_code", map.get("CHR_CODE").toString());
      param.put("mofdep_name", map.get("CHR_NAME").toString());
      param.put("oid", data.get("OID").toString());
      param.put("dep_id", dep_id);
      param.put("dep_code", res.get("CHR_CODE").toString());
      param.put("dep_name", res.get("CHR_NAME").toString());
      param.put("sup_type_id", data.get("SUP_TYPE_ID").toString());
      param.put("sup_type_code", data.get("SUP_TYPE_CODE").toString());
      param.put("sup_type_name", data.get("SUP_TYPE_NAME").toString());
      param.put("obj_type_id", data.get("OBJ_TYPE_ID").toString());
      param.put("obj_type_code", data.get("OBJ_TYPE_CODE").toString());
      param.put("obj_type_name", data.get("OBJ_TYPE_NAME").toString());
      param.put("task_no", data.get("TASK_NO").toString());
      param.put("task_name", data.get("TASK_NAME").toString());
      param.put("sup_cycle", sup_cycle);
      param.put("is_end", "0");
      param.put("sum_type", "0");
      param.put("is_valid", "0");
      param.put("billtype_code", billtype_code);
      param.put("billtype_name", billtype_name);
      param.put("bill_no", bill_no);
      param.put("id", id);
      param.put("sup_num", 1);
      param.put("status", 0);
      param.put("task_id", task_id);
      param.put("chr_id", chr_id);
      param.put("chr_code", chr_code);
      param.put("chr_name", chr_name);
      param.put("record_user", user);
      param.put("record_date", date);
      param.put("latest_op_user", user);
      param.put("latest_op_date", date);
      param.put("create_user", user);
      param.put("create_date", date);
      param.put("sup_date", sup_date);
      param.put("set_year", getYear());
      param.put("rg_code", getRgCode());
      csDao.saveSupBill(param);
      return true;
    } else {
      return false;
    }
  }

  //录入数据到csof_sup_bill表里
  public boolean ifExistBill(String billtype_code, String chr_id, String task_id) {
    Map<String, Object> param = new HashMap<String, Object>();
    int num;
    param.put("task_id", task_id);
    Map<String, Object> data = csDao.getTaskSup(param);
    // sup_cycle 0: 不定期  允许重复录入监管事项     其它：不允许
    param.put("chr_id", chr_id);
    param.put("task_id", task_id);
    String sup_cycle = data.get("SUP_CYCLE").toString();
    if (sup_cycle.equals("0")) {
      num = csDao.getEndCount(param);
    } else {
      num = csDao.getCount(param);
    }
    if (num == 0) {
      return true;
    } else {
      return false;
    }
  }

  //录入数据到csof_sup_bill表里
  public boolean saveSupBill(String sid, String bill_no, String bill_id, String billtype_code, String chr_id,
    String chr_code, String chr_name, String task_id, String sup_date, String dep_id, String mofdep_id) {
    Map<String, Object> param = new HashMap<String, Object>();
    int num;
    param.put("task_id", task_id);
    Map<String, Object> data = csDao.getTaskSup(param);
    // sup_cycle 0: 不定期  允许重复录入监管事项     其它：不允许
    param.put("chr_id", chr_id);
    param.put("task_id", task_id);
    String sup_cycle = data.get("SUP_CYCLE").toString();
    if (sup_cycle.equals("0")) {
      num = csDao.getEndCount(param);
    } else {
      num = csDao.getCount(param);
    }
    if (num == 0) {
      Map<String, Object> billdata = workFlowdao.selectTableName(billtype_code);
      String billtype_name = billdata.get("BILLTYPE_NAME").toString();
      Map<String, Object> res = csDao.getDep(dep_id);
      Map<String, Object> map = csDao.getMofdep(mofdep_id);
      String date = Tools.getCurrDate();
      String user = (String) SessionUtil.getUserInfoContext().getUserID();
      param.put("sid", data.get("SID").toString());
      param.put("sup_no", data.get("SUP_NO").toString());
      param.put("sup_name", data.get("SUP_NAME").toString());
      param.put("mofdep_id", mofdep_id);
      param.put("mofdep_code", map.get("CHR_CODE").toString());
      param.put("mofdep_name", map.get("CHR_NAME").toString());
      param.put("oid", data.get("OID").toString());
      param.put("dep_id", dep_id);
      param.put("dep_code", res.get("CHR_CODE").toString());
      param.put("dep_name", res.get("CHR_NAME").toString());
      param.put("sup_type_id", data.get("SUP_TYPE_ID").toString());
      param.put("sup_type_code", data.get("SUP_TYPE_CODE").toString());
      param.put("sup_type_name", data.get("SUP_TYPE_NAME").toString());
      param.put("obj_type_id", data.get("OBJ_TYPE_ID").toString());
      param.put("obj_type_code", data.get("OBJ_TYPE_CODE").toString());
      param.put("obj_type_name", data.get("OBJ_TYPE_NAME").toString());
      param.put("task_no", data.get("TASK_NO").toString());
      param.put("task_name", data.get("TASK_NAME").toString());
      param.put("sup_cycle", sup_cycle);
      param.put("is_end", "0");
      param.put("sum_type", "0");
      param.put("is_valid", "1");
      param.put("billtype_code", billtype_code);
      param.put("billtype_name", billtype_name);
      param.put("bill_no", bill_no);
      param.put("id", bill_id);
      param.put("sup_num", 1);
      param.put("status", 0);
      param.put("task_id", task_id);
      param.put("chr_id", chr_id);
      param.put("chr_code", chr_code);
      param.put("chr_name", chr_name);
      param.put("record_user", user);
      param.put("record_date", date);
      param.put("latest_op_user", user);
      param.put("latest_op_date", date);
      param.put("create_user", user);
      param.put("create_date", date);
      param.put("sup_date", sup_date);
      param.put("set_year", getYear());
      param.put("rg_code", getRgCode());
      csDao.saveSupBill(param);
      return true;
    } else {
      return false;
    }
  }
}
