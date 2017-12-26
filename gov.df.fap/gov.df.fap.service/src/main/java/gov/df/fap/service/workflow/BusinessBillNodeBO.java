package gov.df.fap.service.workflow;

import gov.df.fap.api.workflow.IBusinessBillNode;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 业务单据类型节点接口实现类
 * @author liuwj3
 *
 */
@SuppressWarnings("unchecked")
@Service
public class BusinessBillNodeBO implements IBusinessBillNode {
  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  @Override
  public List<Map<String, Object>> getBusinessBillCode() throws Exception {
    String findSql = "select * from sys_business_bill_node";
    List<Map<String, Object>> list = dao.findBySql(findSql);
    return list;
  }

  @Override
  public void saveBusinessBillNode(Map<String, Object> map) throws Exception {
    String insertSql = "insert into sys_business_bill_node (id, business_bill_code, bill_node_code, bill_node_name, wf_node_ids, select_sql, from_where_sql, right_table_alias, order_sql, is_count, rg_code, set_year, remak, table_name, isbill) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    dao.executeBySql(
      insertSql,
      new Object[] { UUIDRandom.generate(), map.get("business_bill_code"), map.get("bill_node_code"),
        map.get("bill_node_name"), map.get("wf_node_ids"), map.get("select_sql"), map.get("from_where_sql"),
        map.get("right_table_alias"), map.get("order_sql"), map.get("is_count"), SessionUtil.getRgCode(),
        SessionUtil.getLoginYear(), map.get("remak"), map.get("table_name"), map.get("isbill") });
  }

  @Override
  public void updateBusinessBillNode(Map<String, Object> map) throws Exception {
    String updateSql = "update sys_business_bill_node set business_bill_code = ?, bill_node_code = ?, bill_node_name = ?, wf_node_ids = ?, select_sql = ?, from_where_sql = ?, right_table_alias = ?, order_sql = ?, is_count = ?, rg_code = ?, set_year = ?, remak = ?, table_name = ?, isbill = ? where id = ?";
    dao.executeBySql(
      updateSql,
      new Object[] { map.get("business_bill_code"), map.get("bill_node_code"), map.get("bill_node_name"),
        map.get("wf_node_ids"), map.get("select_sql"), map.get("from_where_sql"), map.get("right_table_alias"),
        map.get("order_sql"), map.get("is_count"), SessionUtil.getRgCode(), SessionUtil.getLoginYear(),
        map.get("remak"), map.get("table_name"), map.get("isbill"), map.get("id") });
  }

  @Override
  public void deleteBusinessBillNode(String id) throws Exception {
    String deleteSql = "delete from sys_business_bill_node where id = ?";
    dao.executeBySql(deleteSql, new Object[] { id });
  }
}
