package gov.df.fap.controller.businessbill;

import gov.df.fap.api.workflow.IBusinessBill;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.util.web.CurrentUser;
import gov.df.fap.util.web.WfUserContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author 胡立田
 *  20170030
 *  我的业务单据控制层
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Controller
@RequestMapping(value = "/df/businessbill/")
public class BusinessBillController {

  @Autowired
  private IBusinessBill businessBill;

  /**
   * 
   */
  @RequestMapping(value = "/getMyBillData.do")
  public @ResponseBody
  Map<String, Object> getMyBillData(HttpServletRequest request, HttpServletResponse response) {
    WfUserContext.setRequest(request);
    CurrentUser user = WfUserContext.getCurrentUser();
    //单据类型
    String bsibill = request.getParameter("bsibilltypecode");
    //单据节点
    String bsinodecode = request.getParameter("bsinodecode");
    //查询条件
    String condition = request.getParameter("condition");
    if (condition == null) {
      condition = "";
    }

    String pageInfo = request.getParameter("pageInfo");
    FPaginationDTO pageSet = new FPaginationDTO();
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (pageInfo != null) {
        String[] pageArray = pageInfo.split("-");
        pageSet.setPagecount(Integer.parseInt(pageArray[0]));
        pageSet.setCurrpage(Integer.parseInt(pageArray[1]) + 1);
        List dataList = businessBill.getBillNodeData(user, pageSet, bsibill, condition, bsinodecode);
        result.put("totalElements", businessBill.getBillNodeCount(user, pageSet, bsinodecode, condition));
        result.put("dataDetail", dataList);
        result.put("flag", true);
        result.put("result", "");
      }
    } catch (Exception e) {
      e.printStackTrace();
      result.put("flag", false);
      result.put("result", "查询我的单据失败，请联系管理员！" + e.getMessage());
    }
    return result;
  }

  /**
   * 
   */
  @RequestMapping(value = "/getMyBillCount.do")
  public @ResponseBody
  Map<String, Object> getMyBillCount(HttpServletRequest request, HttpServletResponse response) {
    WfUserContext.setRequest(request);
    CurrentUser user = WfUserContext.getCurrentUser();
    //单据类型
    String bsibill = request.getParameter("bsibilltypecode");

    //单据节点
    String bsinodecode = request.getParameter("bsinodecode");
    if (bsinodecode == null) {
      bsinodecode = "";
    }

    Map<String, Object> result = new HashMap<String, Object>();
    try {

      List dataList = businessBill.getBillNodeAllCount(user, bsibill, bsinodecode);

      result.put("count", dataList);
      result.put("flag", true);
      result.put("result", "");

    } catch (Exception e) {
      e.printStackTrace();
      result.put("flag", false);
      result.put("result", "查询我的单据失败，请联系管理员！" + e.getMessage());
    }
    return result;
  }

  /**
   * 获取我的单据默认状态
   */
  @RequestMapping(value = "/getMyBillNode.do")
  public @ResponseBody
  Map<String, Object> getMybillNode(HttpServletRequest request, HttpServletResponse response) {
    WfUserContext.setRequest(request);
    CurrentUser user = WfUserContext.getCurrentUser();
    String bsibill = request.getParameter("bsibilltypecode");

    FPaginationDTO pageSet = new FPaginationDTO();
    Map<String, Object> result = new HashMap<String, Object>();
    try {

      String node_code = businessBill.getDefaultMyBillNode(user, bsibill);
      result.put("bsinodecode", node_code);
      result.put("flag", true);
      result.put("result", "");
    } catch (Exception e) {
      e.printStackTrace();
      result.put("flag", false);
      result.put("result", "查询我的单据失败，请联系管理员！");
    }
    return result;
  }

  /**
   * 流程跟踪日志
   */
  @RequestMapping(value = "/wfLogQuery.do")
  public @ResponseBody
  List getMyBillLog(HttpServletRequest request, HttpServletResponse response) {
    //WfUserContext.setRequest(request);
    //CurrentUser user = WfUserContext.getCurrentUser();
    List wfLogList = null;
    String bsi_bill_type_code = request.getParameter("bsibilltypecode");
    String billNodeCode = request.getParameter("bsinodecode");
    String billNo = request.getParameter("id");
    try {
      wfLogList = businessBill.getMyBillLog(bsi_bill_type_code, billNodeCode, billNo);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return wfLogList;
  }
}
