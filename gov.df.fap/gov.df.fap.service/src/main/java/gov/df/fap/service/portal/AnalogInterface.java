package gov.df.fap.service.portal;

import java.util.ArrayList;
import java.util.List;

import gov.df.fap.bean.workflow.FTaskItemDTO;

/**
 * 模拟接口，无实际功能
 */
public class AnalogInterface {

  /**
   * 获取待办事项
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public List findTasks(String UserId, String RoleId, String Region) {
    List return_list = new ArrayList();
    
    FTaskItemDTO task = new FTaskItemDTO();
    //task.setRole_id(RoleId);
    task.setModule_id("Module_id");
    task.setMenu_id("Menu_id");
    task.setMenu_name("Menu_name");
    task.setSysapp("Sys_id");
    task.setMsg_type_code("1");
    task.setMsg_type_name("日常事务");
    task.setMsg_type_name_local("Module_name");
    //ymj 增加按明细
    task.setTask_content("Status_name " + "：按单 " + "Num" + " 条, 按明细 " + "DetailNum" + " 条； ");
    //ymj 增加操作时间和角色
    task.setOperation_date("Operation_date");
    task.setRole_name("role_name");
    
    task.setRole_id(RoleId + " -- 1");
    return_list.add(task);
    task.setRole_id(RoleId + " -- 2");
    return_list.add(task);
    task.setRole_id(RoleId + " -- 3");
    return_list.add(task);
    task.setRole_id(RoleId + " -- 4");
    return_list.add(task);
    task.setRole_id(RoleId + " -- 5");
    return_list.add(task);
    
    return return_list;
  }
  
}
