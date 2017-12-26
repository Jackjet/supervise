package gov.df.fap.controller.businessbill;

import gov.df.fap.api.workflow.IBusinessBillNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 业务单据类型节点控制层
 * @author liuwj3
 *
 */
@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/df/businessbillcode")
public class BusinessBillNodeContrller {

  private static final Logger logger = LoggerFactory.getLogger(BusinessBillController.class);

  @Autowired
  private IBusinessBillNode businessBillCode;

  /**
   * 查询业务单据类型节点
   * @param request
   * @return 全部业务单据类型节点
   */
  @RequestMapping("/getBusinessBillNode.do")
  @ResponseBody
  public Map<String, Object> getBusinessBillNode(HttpServletRequest request) {
    Map<String, Object> result = new HashMap<String, Object>();
    List<Map<String, Object>> data;
    try {
      data = businessBillCode.getBusinessBillCode();
      result.put("data", data);
      result.put("flag", "success");
    } catch (Exception e) {
      result.put("flag", "failure");
      result.put("msg", "查询失败");
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 保存
   * @param request
   * @return
   */
  @RequestMapping(value = "/saveBusinessBillNode.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> saveBusinessBillNode(HttpServletRequest request) {
    Map<String, Object> result = new HashMap<String, Object>();
    String jsonStr = request.getParameter("data");
    JSONObject jsonObject = JSONObject.fromObject(jsonStr);
    Map<String, Object> businessBillNodeInfo = (Map<String, Object>) jsonObject;
    try {
      businessBillCode.saveBusinessBillNode(businessBillNodeInfo);
      result.put("flag", "success");
    } catch (Exception e) {
      result.put("flag", "failure");
      result.put("msg", "保存业务单据类型节点失败");
      logger.error("保存业务单据类型节点失败：", e.getMessage());
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 修改
   * @param request
   * @return
   */
  @RequestMapping(value = "/updateBusinessBillNode.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> updateBusinessBillNode(HttpServletRequest request) {
    Map<String, Object> result = new HashMap<String, Object>();
    String jsonStr = request.getParameter("data");
    JSONObject jsonObject = JSONObject.fromObject(jsonStr);
    Map<String, Object> businessBillNodeInfo = (Map<String, Object>) jsonObject;
    try {
      businessBillCode.updateBusinessBillNode(businessBillNodeInfo);
      result.put("flag", "success");
    } catch (Exception e) {
      result.put("flag", "failure");
      result.put("msg", "更新业务单据类型节点失败");
      logger.error("更新业务单据类型节点失败：", e.getMessage());
      e.printStackTrace();
    }
    return result;
  }

  @RequestMapping(value = "/deleteBusinessBillNode.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> deleteBusinessBillNode(@RequestParam("id")
  String id) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      businessBillCode.deleteBusinessBillNode(id);
      result.put("flag", "success");
    } catch (Exception e) {
      result.put("flag", "failure");
      result.put("msg", "删除业务单据类型节点失败");
      logger.error("删除业务单据类型节点失败：", e.getMessage());
      e.printStackTrace();
    }
    return result;
  }

}
