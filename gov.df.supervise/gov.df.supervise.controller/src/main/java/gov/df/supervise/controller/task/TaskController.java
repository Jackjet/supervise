package gov.df.supervise.controller.task;

import gov.df.fap.api.workflow.IBillTypeServices;
import gov.df.supervise.api.task.TaskService;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ufgov.ip.apiUtils.UUIDTools;

@Controller
@RequestMapping(value = "/df/task")
public class TaskController {

  @Autowired
  private TaskService taskService;

  @Autowired
  private IBillTypeServices billTypeService;

  /**
   * 获取处室任务列表
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(method = RequestMethod.GET, value = "/getTaskDep.do")
  public @ResponseBody
  Map<String, Object> getTaskDep(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List resultList = new ArrayList();
      String condition = "";
      String chr_id = null != request.getParameter("CHR_ID") ? request.getParameter("CHR_ID") : "";
      String type_code = null != request.getParameter("TYPE_CODE") ? request.getParameter("TYPE_CODE") : "";
      String status = null != request.getParameter("STATUS") ? request.getParameter("STATUS") : "";
      if (status.equals("-1")) {
        map.put("errorCode", "0");
        map.put("flag", true);
        map.put("dataDetail", resultList);
        map.put("totalElements", resultList.size());
        return map;
      }
      if (!chr_id.equals("") && (!type_code.equals(""))) {
        condition = "AND " + type_code + "= '" + chr_id + "'";
      }
      if (!status.equals("")) {
        if (status.equals("0")) {
          condition += " AND IS_PUB = 0 AND ASSIGN_DATE IS NULL and is_valid=1 ";
        } else if (status.equals("1")) {
          condition += " AND IS_PUB = 0 AND ASSIGN_DATE IS NOT NULL and is_valid=1 ";
        } else {
          condition += " AND IS_PUB = 1 and is_valid=1 ";
        }
      }

      resultList = taskService.getPorData("VW_CSOF_TASK_DEP", condition);
      map.put("errorCode", "0");
      map.put("flag", true);
      map.put("dataDetail", resultList);
      map.put("totalElements", resultList.size());
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  /**
   * 获取处室任务列表
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(method = RequestMethod.GET, value = "/getTaskDepDetail.do")
  public @ResponseBody
  Map<String, Object> getTaskDepDetail(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List resultList = new ArrayList();
      String condition = "";
      String chr_id = null != request.getParameter("CHR_ID") ? request.getParameter("CHR_ID") : "";
      String type_code = null != request.getParameter("TYPE_CODE") ? request.getParameter("TYPE_CODE") : "";
      String status = null != request.getParameter("STATUS") ? request.getParameter("STATUS") : "";
      if (status.equals("-1")) {
        map.put("errorCode", "0");
        map.put("flag", true);
        map.put("dataDetail", resultList);
        map.put("totalElements", resultList.size());
        return map;
      }
      if (!chr_id.equals("") && (!type_code.equals(""))) {
        condition = "AND " + type_code + "= '" + chr_id + "'";
      }
      if (!status.equals("")) {
        condition += status;
      }

      resultList = taskService.getData("VW_CSOF_TASK_DEP", condition + " and is_valid=1 ");
      map.put("errorCode", "0");
      map.put("flag", true);
      map.put("dataDetail", resultList);
      map.put("totalElements", resultList.size());
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  @RequestMapping(method = RequestMethod.GET, value = "/getTaskSup.do")
  public @ResponseBody
  Map<String, Object> getTaskSup(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List resultList = new ArrayList();
      String condition = "";
      String chr_id = null != request.getParameter("CHR_ID") ? request.getParameter("CHR_ID") : "";
      String type_code = null != request.getParameter("TYPE_CODE") ? request.getParameter("TYPE_CODE") : "";
      String status = null != request.getParameter("STATUS") ? request.getParameter("STATUS") : "";

      if (!chr_id.equals("") && (!type_code.equals(""))) {
        condition = "AND " + type_code + "= '" + chr_id + "'";
      }
      if (!status.equals("")) {
        if (status.equals("1")) {
          condition += " AND APPROVE_DATE IS NOT NULL";
        } else if (status.equals("0")) {
          condition += " AND APPROVE_DATE IS NULL";
        } else if (status.equals("-1")) {
          return map;
        }
      }
      resultList = taskService.getPorData("CSOF_TASK_SUP", condition + " and is_valid=1");

      map.put("errorCode", "0");
      map.put("flag", true);
      map.put("dataDetail", resultList);
      map.put("totalElements", resultList.size());
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  /**
   * 保存监管事项、处室任务
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/saveTask.do")
  public @ResponseBody
  Map<String, Object> saveTask(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

      Map<String, String> supMap = new HashMap<String, String>();
      Enumeration em = request.getParameterNames();
      List objList = new ArrayList();
      List reportList = new ArrayList();
      List agencyList = new ArrayList();
      String bill_type_code = "";
      while (em.hasMoreElements()) {
        String name = (String) em.nextElement();
        String value = request.getParameter(name);

        if (name.equals("billtype_code")) {
          bill_type_code = request.getParameter("billtype_code");
        } else if (name.equals("Objectlist")) {
          objList = JSONObject.parseArray(request.getParameter("Objectlist"), Map.class);
        } else if (name.equals("EReportlist")) {
          reportList = JSONObject.parseArray(request.getParameter("EReportlist"), Map.class);
        } else if (name.equals("agencyList")) {
          agencyList = JSONObject.parseArray(request.getParameter("agencyList"), Map.class);
        } else if (!name.equals("tokenid") && !name.equals("ajax")) {
          if (value.contains("@")) {
            supMap.put(name.substring(0, name.lastIndexOf("_")) + "_ID", value.split("@")[0]);
            supMap.put(name.substring(0, name.lastIndexOf("_")) + "_NAME",
              URLDecoder.decode(value.split("@")[1], "UTF-8"));
            supMap.put(name.substring(0, name.lastIndexOf("_")) + "_CODE", value.split("@")[2]);
          } else {
            supMap.put(name, value);
          }
        }

      }

      if (supMap.get("IS_ALLOBJ").equals("1")) {
        objList = taskService.getObjList();
      }

      String sup_no = billTypeService.getBillNo(bill_type_code, supMap);
      supMap.put("SUP_NO", sup_no);
      supMap.put("BILLTYPE_CODE", bill_type_code);

      int snum = taskService.saveTask(supMap, objList, agencyList, reportList);

      map.put("errorCode", "0");
      map.put("totalElements", snum);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  /**
   * 获取事项监管单位
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/getTaskAgency.do")
  public @ResponseBody
  Map<String, Object> getAgecny(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List resultList = new ArrayList();
      String chr_id = null != request.getParameter("CHR_ID") ? request.getParameter("CHR_ID") : "";
      String type_code = null != request.getParameter("TYPE_CODE") ? request.getParameter("TYPE_CODE") : "";
      resultList = taskService.getData("CSOF_TASK_OBJECT", "AND " + type_code + "='" + chr_id + "'");

      map.put("flag", true);
      map.put("dataDetail", resultList);
      map.put("errorCode", "0");
      map.put("totalElements", resultList.size());
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  /**
   * 获取监管事项报表
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/getSupReport.do")
  public @ResponseBody
  Map<String, Object> getSupReport(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List resultList = new ArrayList();
      String chr_id = null != request.getParameter("CHR_ID") ? request.getParameter("CHR_ID") : "";
      String type_code = null != request.getParameter("TYPE_CODE") ? request.getParameter("TYPE_CODE") : "";
      resultList = taskService.getData("CSOF_SUP_REPORT", "AND " + type_code + "='" + chr_id + "'");

      map.put("flag", true);
      map.put("dataDetail", resultList);
      map.put("errorCode", "0");
      map.put("totalElements", resultList.size());
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  /**
   * 监管事项分解
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/newTaskUser.do")
  public @ResponseBody
  Map<String, Object> newTaskUser(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

      String data = request.getParameter("postData");
      //	String bill_type_code=request.getParameter("billtype_code");

      int userNum = taskService.saveTaskUser(data);

      map.put("flag", true);
      map.put("errorCode", "0");
      map.put("totalElements", userNum);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  @RequestMapping(method = RequestMethod.GET, value = "/getTaskUser.do")
  public @ResponseBody
  Map<String, Object> getTaskUser(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List resultList = new ArrayList();
      String chr_id = null != request.getParameter("CHR_ID") ? request.getParameter("CHR_ID") : "";
      String type_code = null != request.getParameter("TYPE_CODE") ? request.getParameter("TYPE_CODE") : "";
      resultList = taskService.getPorData("VW_CSOF_TASK_BILL", "AND " + type_code + "='" + chr_id
        + "'  and is_valid=1 ");

      map.put("errorCode", "0");
      map.put("flag", true);
      map.put("dataDetail", resultList);
      map.put("totalElements", resultList.size());
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  /**
   * 撤销分解
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/deleteTaskUser.do")
  public @ResponseBody
  Map<String, Object> deleteTaskUser(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String chr_id = null != request.getParameter("CHR_ID") ? request.getParameter("CHR_ID") : "";
      String type_code = null != request.getParameter("TYPE_CODE") ? request.getParameter("TYPE_CODE") : "";
      int delNum = taskService.clearAssign(chr_id, type_code);

      map.put("flag", true);
      map.put("errorCode", "0");
      map.put("totalElements", delNum);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  @RequestMapping(method = RequestMethod.POST, value = "/updateDepTask.do")
  public @ResponseBody
  Map<String, Object> updateDepTask(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

      Map<String, String> supMap = new HashMap<String, String>();
      String data = request.getParameter("updateData");
      int resNum = taskService.updateTaskDep(data);
      map.put("errorCode", "0");
      map.put("totalElements", resNum);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  /**
   * 任务下达  修改监管事项 处室任务的 下达时间
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/approveTask.do")
  public @ResponseBody
  Map<String, Object> approveTask(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

      String sids = request.getParameter("SIDS");
      int resNum = taskService.approveTask(sids);
      map.put("errorCode", "0");
      map.put("totalElements", resNum);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

  /**
   * 任务发布
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/publishTask.do")
  public @ResponseBody
  Map<String, Object> publishTask(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String depids = request.getParameter("IDS");
      int resNum = taskService.publishTask(depids);
      map.put("errorCode", "0");
      map.put("totalElements", resNum);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }
  


  /**
   * 获取UUID
   * @param request
   * @param response
   * @return UUID
   */
  @RequestMapping(method = RequestMethod.POST, value = "/getUUID.do")
  public @ResponseBody
  String getUUID(HttpServletRequest request, HttpServletResponse response) {
    return UUIDTools.uuidRandom();
  }

  /**
   * 
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(method = RequestMethod.GET, value = "/getTaskTree.do")
  public @ResponseBody
  Map<String, Object> getTaskTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List resultList = new ArrayList();
      resultList = taskService.getSupTree();

      map.put("errorCode", "0");
      map.put("flag", true);
      map.put("dataDetail", resultList);
      map.put("totalElements", resultList.size());
    } catch (Exception e) {
      e.printStackTrace();
      map.put("errorCode", "1");
      map.put("errorMsg", "获取数据出现异常");
    }

    return map;

  }

}
