package gov.df.fap.api.workflow;

import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.util.web.CurrentUser;

import java.util.List;

public interface IBusinessBill {
  /**
   * 查询选中的的业务节点的数据总数
   * @param user
   * @param page
   * @param bsi_bill_type_code 
   * @param condition 查询条件
   * @return
   */
  public int getBillNodeCount(CurrentUser user, FPaginationDTO page, String bsi_bill_type_code, String condition)
    throws Exception;

  /**
   * 查询全部需要统计的业务节点的数据
   * @param user
  * @param bsi_bill_type_code
  * @param bsinodecode TODO
   * @return
   */
  public List getBillNodeAllCount(CurrentUser user, String bsi_bill_type_code, String bsinodecode) throws Exception;

  /**
   * 查询选中的的业务节点的数据（分页模式、含总条数）
   * @param user
   * @param page
   * @param bsi_bill_type_code 
   * @param condition 查询条件
   * @return
   */
  public List getBillNodeData(CurrentUser user, FPaginationDTO page, String bsi_bill_type_code, String condition,
    String bsi_bill_node_code) throws Exception;

  /**
   * 查询默认的业务节点数据
   * @param user
  * @param bsibill TODO
   * @return
   */
  public String getDefaultMyBillNode(CurrentUser user, String bsibill) throws Exception;

  /**
   * 流程跟踪（查看选中单据的流程详信息）
   * @param bsi_bill_type_code 业务节点类型
   * @param billNodeCode 业务节点编码
   * @param isBill 是否单据
   * @param billNo 是明细ID或单号
   * @return
   */
  public List getMyBillLog(String bsi_bill_type_code, String billNodeCode, String billNo) throws Exception;

}
