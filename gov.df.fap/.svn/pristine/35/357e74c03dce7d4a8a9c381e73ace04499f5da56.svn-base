package gov.df.fap.service.workflow;

import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.api.workflow.IBusinessBill;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.web.CurrentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unchecked")
public class BusinessBillBO implements IBusinessBill {
  ////TODO 优化缓存先查库来做
  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  @Autowired
  IDataRight dataRight;

  @Override
  public int getBillNodeCount(CurrentUser user, FPaginationDTO page, String bill_node_code, String condition)
    throws Exception {
    List<Map> list = ((List<Map>) dao
      .findBySql(
        "select bill_node_code,wf_node_ids,from_where_sql,right_table_alias from sys_business_bill_node where BILL_NODE_CODE	=? ",
        new Object[] { bill_node_code }));

    if (list.size() == 0) {
      return 0;
    }
    Map<String, String> node = list.get(0);
    String right = dataRight.getSqlBusiRightByUserAndRoleNoRcid(user.getUserId(), user.getRoleId(),
      node.get("right_table_alias"));
    String sql = "select count(*) as total "
      + node.get("from_where_sql").replaceAll("#node#", node.get("wf_node_ids")).replaceAll("#right#", right)
        .replaceAll("#condition#", condition);
    return Integer.parseInt(((List<Map>) dao.findBySql(sql)).get(0).get("total").toString());
  }

  @Override
  public List getBillNodeAllCount(CurrentUser user, String bsi_bill_type_code, String bsinodecode) throws Exception {
    List count = new ArrayList();

    List<Map> nodes = dao
      .findBySql(
        "select bill_node_code,wf_node_ids,from_where_sql,right_table_alias from sys_business_bill_node where BUSINESS_BILL_CODE=? and is_count=1",
        new Object[] { bsi_bill_type_code });
    for (Map<String, String> node : nodes) {
      
//      if (bsinodecode.equals(node.get("bill_node_code"))) {
//        continue;
//      }
      String right = dataRight.getSqlBusiRightByUserAndRoleNoRcid(user.getUserId(), user.getRoleId(),
        node.get("right_table_alias"));
      String sql = "select '"
        + node.get("bill_node_code")
        + "' as bill_node_code,count(*) total "
        + node.get("from_where_sql").replaceAll("#node#", node.get("wf_node_ids")).replaceAll("#right#", right)
          .replaceAll("#condition#", "");
      Map map = (Map) dao.findBySql(sql).get(0);
      if (!"0".equals(map.get("total").toString())) {
        count.add(map);
      }

    }

    return count;
  }

  @Override
  public List getBillNodeData(CurrentUser user, FPaginationDTO page, String bsi_bill_type_code, String condition,
    String bsi_bill_node_code) throws Exception {
    List list = ((List<Map>) dao
      .findBySql(
        "select bill_node_code,wf_node_ids,from_where_sql,right_table_alias,select_sql,order_sql from sys_business_bill_node where BILL_NODE_CODE	=?  ",
        new Object[] { bsi_bill_node_code }));
    if (list.size() == 0) {
      return list;
    }
    Map<String, String> node = (Map<String, String>) list.get(0);
    String right = dataRight.getSqlBusiRightByUserAndRoleNoRcid(user.getUserId(), user.getRoleId(),
      node.get("right_table_alias"));
    String sql = (TypeOfDB.isOracle() ? "select rownum rw_, "
      : "SELECT @rn:=@rn+1 AS rw_, t.* FROM (SELECT @rn:=0) r, ( select ")
      + node.get("select_sql")
      + node.get("from_where_sql").replaceAll("#node#", node.get("wf_node_ids")).replaceAll("#right#", right)
        .replaceAll("#condition#", condition) + node.get("order_sql") + (TypeOfDB.isOracle() ? "" : " ) as t");
    sql = "select * from (" + sql + (TypeOfDB.isOracle() ? ") where rw_> " : ") as t2 where rw_> ")
      + page.getPagecount() * (page.getCurrpage() - 1) + " and rw_<=" + page.getPagecount() * (page.getCurrpage());
    return dao.findBySql(sql);
  }

  @Override
  public String getDefaultMyBillNode(CurrentUser user, String bsibill) throws Exception {
    String bill_node_code = "";
    List<Map> nodes = dao
      .findBySql(
        "select bill_node_code,wf_node_ids from sys_business_bill_node where BUSINESS_BILL_CODE=? and is_count=1 and wf_node_ids is not null",
        new Object[] { bsibill });
    for (Map<String, String> node : nodes) {
      if (bill_node_code == "") {
        bill_node_code = node.get("bill_node_code");
      }
      String wf_node_ids = node.get("wf_node_ids");
      String sql = "select 1 from sys_wf_role_node where rg_code=? and set_year=? and role_id = ? and node_id in ("
        + wf_node_ids + ")";
      if (dao.findBySql(sql, new Object[] { user.getRg_code(), user.getSetYear(), user.getRoleId() }).size() > 0) {
        return node.get("bill_node_code");
      }
    }

    // TODO Auto-generated method stub
    return bill_node_code;
  }

  /**
   * 流程跟踪（查看选中单据的流程详信息）
   * @param bsi_bill_type_code 业务节点类型
   * @param billNodeCode 业务节点编码
   * @param isBill 是否单据
   * @param billNo 是明细ID或单号
   * @return
   */
  @Override
  public List getMyBillLog(String bsi_bill_type_code, String billNodeCode, String billNo) throws Exception {
    StringBuffer sb = new StringBuffer();
    String table_name = "";
    String entityIdString = "";
    int isBill = 0;
    List table_nameList = dao
      .findBySql(
        "select table_name,isbill from sys_business_bill_node t where t.business_bill_code=? and t.bill_node_code=? and t.rg_code=? and t.set_year=?",
        new String[] { bsi_bill_type_code, billNodeCode, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    if (table_nameList != null && table_nameList.size() > 0) {
      table_name = (String) ((Map) table_nameList.get(0)).get("table_name");
      table_name = table_name.toUpperCase();
      isBill = Integer.parseInt((String) ((Map) table_nameList.get(0)).get("isbill"));
    }
    //如果是单据
    if (isBill == 1) {
      if (table_name.toUpperCase().equals("PAY_VOUCHER_BILL")) {
        entityIdString = " (select p.id from pay_voucher p  where exists(select 1 from pay_voucher_bill pv where pv.id = p.voucher_bill_id and pv.id='"+ billNo +"'))";
      } else if (table_name.toUpperCase().equals("PAY_CLEAR_BILL")) {
        entityIdString = " (select p.id from pay_voucher p  where exists(select 1 from pay_voucher_bill pv where pv.id = p.clear_bill_id and pv.id='"+ billNo +"'))";
      } else if (table_name.toUpperCase().equals("PAY_AGENT_BILL")) {
        entityIdString = " (select p.id from pay_voucher p  where exists(select 1 from pay_voucher_bill pv where pv.id = p.agent_bill_id and pv.id='"+ billNo +"'))";
      }
      table_name = "PAY_VOUCHER";
    } else {
      entityIdString = "'" + billNo + "'";

    }
    //明细
    sb.append(
      " select swc.create_date,"
        + (TypeOfDB.isOracle() ? "'['||swc.create_user||']'" : "concat('[',swc.create_user,']')")
        + " create_user,(select node_name from sys_wf_nodes sf where ")
      .append(" sf.node_id=swc.current_node_id) node_name,(select sue.TELEPHONE  from sys_usermanage sue where  ")
      .append("sue.USER_ID = swc.create_user_id) TELEPHONE from sys_wf_complete_tasks swc where swc.entity_id=")
      .append(entityIdString)
      .append("and swc.wf_table_name=?")
      .append(" union all ")
      .append(
        " select distinct swc.create_date,"
          + (TypeOfDB.isOracle() ? "'['||swc.create_user||']'" : "concat('[',swc.create_user,']')")
          + " create_user,(select node_name from sys_wf_nodes sf where ")
      .append(" sf.node_id=swc.current_node_id) node_name,(select sue.TELEPHONE  from sys_usermanage sue where  ")
      .append("sue.USER_ID = swc.create_user_id) TELEPHONE from sys_wf_current_tasks ")
      .append(" swc where swc.entity_id=").append(entityIdString.toUpperCase())
      .append(" and swc.wf_table_name=? order by create_date");

    return dao.findBySql(sb.toString(), new String[] { table_name, table_name });
  }
}
