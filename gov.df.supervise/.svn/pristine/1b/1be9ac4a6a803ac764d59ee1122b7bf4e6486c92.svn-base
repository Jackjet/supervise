package gov.df.supervise.controller.workfolw;

import gov.df.supervise.api.workflow.WorkFlow;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/workflow")
public class workFlowController {
  @Autowired
  private WorkFlow workflow;

  @RequestMapping(value = "/work.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> doWorkFlow(HttpServletRequest request) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    String menu_id = request.getParameter("menu_id");
    String entity_id = request.getParameter("entity_id");
    String billtype_code = request.getParameter("billtype_code");
    String op_type = request.getParameter("op_type");
    String op_name = request.getParameter("op_name");
    String opinion = request.getParameter("opinion") != null ? request.getParameter("opinion") : "";
    try {
      boolean data = workflow.doWorkFlow(menu_id, entity_id, billtype_code, op_type, op_name, opinion);
      result.put("data", data);
      result.put("errorCode", 0);
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "工作流执行失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
    }
    return result;

  }
}
