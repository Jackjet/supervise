package gov.df.supervise.controller.Tree;

import gov.df.supervise.api.Tree.elementTreeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/tree")
public class elementTreeController {
  @Autowired
  private elementTreeService elementTreeservice;

  @RequestMapping(value = "/initTree.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> initTree(HttpServletRequest request) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    String ele_code = request.getParameter("ele_code");
    String task_bill_id = request.getParameter("task_id");
    String is_all = request.getParameter("is_allobj");
    int is_allobj = Integer.parseInt(request.getParameter("is_allobj"));
    String obj_type_id = request.getParameter("obj_type_id");
    try {
      List data = elementTreeservice.initTree(ele_code, task_bill_id, is_allobj, obj_type_id);
      result.put("data", data);
      result.put("errorCode", 0);
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "查询要素树失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
    }
    return result;
  }

  @RequestMapping(value = "/getElementData.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getElementData(HttpServletRequest request) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    String ele_code = request.getParameter("ele_code");
    try {
      List data = elementTreeservice.getElementData(ele_code);
      result.put("data", data);
      result.put("errorCode", 0);
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "查询要素详细信息失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
    }
    return result;
  }

}
