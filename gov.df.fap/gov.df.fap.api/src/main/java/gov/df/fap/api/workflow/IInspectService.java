package gov.df.fap.api.workflow;

import gov.df.fap.bean.rule.FVoucherDTO;

import java.util.List;

public interface IInspectService {
  public void testserver();

  public void testclient();

  public String inspectInstance(String wf_id, String detail_table_name, String current_node_id, String next_node_id,
    String action_type, String operation_name, String operation_date, String operation_remark, List inspect_ruleMaps,
    FVoucherDTO voucher, String menuid, String role_id) throws Exception;

  public String inspectBatchInstance(String wf_id, String detail_table_name, String current_node_id,
    String next_node_id, String action_type, String operation_name, String operation_date, String operation_remark,
    List inspect_ruleMaps, List voucherList, String menuid, String role_id) throws Exception;

  public void inspectDealResult(String wf_id, String detail_table_name, List listFVoucherDTO) throws Exception;

  public List getInspectRule() throws Exception;

  public List test() throws Exception;
}
