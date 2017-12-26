package gov.df.fap.controller.autotask;

import gov.df.fap.api.systemmanager.autotask.ibs.ISysAutoTask;
import gov.df.fap.api.util.ISysAppUtil;
import gov.df.fap.api.util.paramanage.IParaManage;
import gov.df.fap.bean.form.SysAutoTaskForm;
import gov.df.fap.util.xml.XMLData;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/sysAutoTask")
public class SysAutoTaskController {
  private static final Logger logger = LoggerFactory.getLogger(SysAutoTaskController.class);

  @Autowired
  public ISysAutoTask sysAutoTaskService;

  @Autowired
  public ISysAppUtil sysAppUtilService;

  @Autowired
  @Qualifier("sys.paraManBO")
  public IParaManage paraManageService;

  /**
   * 查询出数据作为界面左侧菜单
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/initTree.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> initTree() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      List dataAutoTaskInfo = new ArrayList();
      List dataSysAppInfo = new ArrayList();
      XMLData xmlData = new XMLData();
      xmlData.put("id", 0);
      xmlData.put("cid", 0);
      xmlData.put("sys_id", null);
      xmlData.put("name", "自动任务");
      dataAutoTaskInfo = sysAutoTaskService.findTreeSysAutoTaskInfo();
      dataSysAppInfo = sysAutoTaskService.findTreeSysAppInfo();
      dataAutoTaskInfo.add(xmlData);
      dataAutoTaskInfo.addAll(dataSysAppInfo);
      result.put("data", dataAutoTaskInfo);
      result.put("errorCode", 0);
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "系统出现异常，请稍后重试");
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 查询新增/修改所属模块下拉框数据
   * @return
   */
  @RequestMapping(value = "/initModule.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> initModule() {
    Map<String, Object> result = new HashMap<String, Object>();
    List AllSysAppforFComboBoxList = new ArrayList();
    try {
      List sysAppsList = sysAppUtilService.findAllSysApps();
      int size = sysAppsList.size();
      if (size > 0) {
        Iterator itr = sysAppsList.iterator();
        XMLData tmp_data;
        String sys_id, sys_name;
        String showSysName = null;
        while (itr.hasNext()) {
          XMLData map = new XMLData();
          tmp_data = (XMLData) itr.next();
          sys_id = (String) tmp_data.get("sys_id");
          sys_name = (String) tmp_data.get("sys_name");
          showSysName = sys_id + " " + sys_name;
          map.put("sys_id", sys_id);
          map.put("sys_name", showSysName);
          AllSysAppforFComboBoxList.add(map);
        }
      }
      result.put("data", AllSysAppforFComboBoxList);
      result.put("errorCode", 0); // 0代表操作成功
    } catch (SQLException e) {
      result.put("errorCode", -1); // -1代表自动任务获取失败
      result.put("errorMsg", "系统出现异常，请稍后重试");
      logger.error("获取所有子系统模块信息失败", e);
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 根据autotaskId获取一条自动任务信息
   * @param autotask_id 任务id
   * @return
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/findOneById.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> findOneById(@RequestParam("autotask_id")
  String autotask_id) {
    Map<String, Object> result = new HashMap<String, Object>();
    List tasksList = new ArrayList();
    try {
      tasksList = sysAutoTaskService.getSysAutoTaskWithStatusByTaskId(autotask_id);
      if (tasksList.size() == 0) {
        tasksList = sysAppUtilService.findSysAppsById(autotask_id);
      }
      result.put("data", tasksList);
      result.put("errorCode", 0); // 0代表操作成功
    } catch (Exception e) {
      result.put("errorCode", -1); // -1代表自动任务获取失败
      result.put("errorMsg", "系统出现异常，请稍后重试");
      logger.error("自动任务获取失败", e);
      e.printStackTrace();

    }
    return result;
  }

  /**
   * 保存(修改)自动任务
   * @param form 自动任务表单
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/save.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> saveorUpdateSysAutoTask(@RequestBody
  SysAutoTaskForm sysAutoTaskForm) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      sysAutoTaskForm.setEnabled("1");
      if (sysAutoTaskForm.getAutotask_id() == null || sysAutoTaskForm.getAutotask_id().isEmpty()
        || sysAutoTaskForm.getAutotask_id().trim().length() == 0) {
        String autotask_id = sysAutoTaskService.generateNumberBySeq("SEQ_SYS_FRAME_ID");
        sysAutoTaskForm.setAutotask_id(autotask_id);
        sysAutoTaskForm.setCreate_date(paraManageService.getServerTime());
        sysAutoTaskService.saveTask(getXMLData(sysAutoTaskForm));
        result.put("errorCode", 0); // 0代表操作成功
        result.put("errorMsg", "新增成功");
      } else {
        sysAutoTaskForm.setLast_ver(paraManageService.getServerTime());
        sysAutoTaskService.saveTask(getXMLData(sysAutoTaskForm));
        result.put("errorCode", 0); // 0代表操作成功
        result.put("errorMsg", "修改成功");
      }
    } catch (Exception e) {
      result.put("errorCode", -1); // -1代表操作失败
      result.put("errorMsg", "系统出现异常，请稍后重试");
      e.printStackTrace();
    }
    return result;
  }

  /**
   *删除
   * @param autotask_id  任务id
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/delete.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> deleteSysAutoTask(@RequestParam("autotask_id")
  String autotask_id) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      sysAutoTaskService.deleteTask(Long.parseLong(autotask_id));
      result.put("errorCode", 0); // 0代表操作成功
      result.put("errorMsg", "删除成功");

    } catch (Exception e) {
      result.put("errorCode", -1); // -1代表操作失败
      result.put("errorMsg", "系统出现异常，请稍后重试");
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 
   * @param autotask_id 任务id
   * @param status  1：启用； 2：挂起 ；3：恢复；4：停用
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/changeStatus.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> excuteTask(String autotask_id, int status) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      sysAutoTaskService.excuteTask(Long.parseLong(autotask_id), status);
      result.put("errorCode", 0); // 0代表操作成功
      if (status == 1) {
        result.put("errorMsg", "启动成功");
      }
      if (status == 4) {
        result.put("errorMsg", "停用成功");
      }
    } catch (Exception e) {
      result.put("errorCode", -1); // -1代表操作失败
      result.put("errorMsg", "系统出现异常，请稍后重试");
      e.printStackTrace();
    }
    return result;

  }

  /**
     * 将form中数据取出放入XMLData中
     * @param form
     * @return value
     */
  private XMLData getXMLData(SysAutoTaskForm form) {
    XMLData value = new XMLData();
    Class<?> classForm = form.getClass();
    Field[] fields = classForm.getDeclaredFields(); // 得到SysAutoTaskForm全部属性
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), classForm);
        Method getMethod = pd.getReadMethod(); // 获得get方法
        Object obj = getMethod.invoke(form); // 执行get方法返回一个Object对象
        if (obj != null) {
          value.put(field.getName(), obj);
        }
      } catch (Exception e) {
        logger.error("form表单转XMLData出错：", e);
        e.printStackTrace();
      }
    }
    return value;
  }

  /**
   * 查找所有启动的任务
   * @return
   */
  @RequestMapping(value = "/findAllEnableTask.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getAllTaskMonitor() {
    List<Object> list = new ArrayList<Object>();
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      list = sysAutoTaskService.getAllAutoTaskMonitor();
      result.put("errorCode", 0);
      result.put("data", list);
    } catch (Exception e) {
      e.printStackTrace();
      result.put("errorCode", -1);
      result.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return result;
  }

  /**
   * 挂起
   * @param autotask_id 任务id
   * @return
   */
  @RequestMapping(value = "/pause.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> pause(String autotask_id) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      sysAutoTaskService.excuteTask(Long.parseLong(autotask_id), 2);
      result.put("errorCode", 0);
      result.put("errorMsg", "挂起成功");
    } catch (NumberFormatException e) {
      e.printStackTrace();
      result.put("errorCode", -2);
      result.put("errorMsg", "系统出现异常，请稍后重试");
    } catch (Exception e) {
      e.printStackTrace();
      result.put("errorCode", -1);
      result.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return result;
  }

  /**
   * 恢复
   * @param autotask_id 任务id
   * @return
   */
  @RequestMapping(value = "/resume.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> resume(String autotask_id) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      sysAutoTaskService.excuteTask(Long.parseLong(autotask_id), 3);
      result.put("errorCode", 0);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      result.put("errorCode", -2);
      result.put("errorMsg", "系统出现异常，请稍后重试");
    } catch (Exception e) {
      e.printStackTrace();
      result.put("errorCode", -1);
      result.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return result;
  }

  /**
   * 执行
   * @param autotask_id 任务id
   * @return
   */
  @RequestMapping(value = "/execute.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> execute(String autotask_id) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      int flag = sysAutoTaskService.excuteTaskByHand(Long.parseLong(autotask_id));
      result.put("errorCode", 0);
      switch (flag) {
      case 1:
        result.put("data", flag); //自动任务设有参数，不能手动执行！
        break;
      case 2:
        result.put("data", flag);//自动任务没有找到，手动执行失败！
        break;
      default:
        result.put("data", 0); //自动任务手动执行成功！
        break;
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
      result.put("errorCode", -1);
      result.put("errorMsg", "系统出现异常，请稍后重试");
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      result.put("errorCode", -2);
      result.put("errorMsg", "手动执行失败");
    } catch (Exception e) {
      e.printStackTrace();
      result.put("errorCode", -1);
      result.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return result;
  }

}
