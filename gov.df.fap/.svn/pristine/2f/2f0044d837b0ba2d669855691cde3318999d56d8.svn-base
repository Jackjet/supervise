package gov.df.fap.service.workflow;

import gov.df.fap.api.workflow.IMessageClient;
import gov.df.fap.api.workflow.message.IMessage;
import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.bean.workflow.FMessageDTO;
import gov.df.fap.bean.workflow.FTaskItemDTO;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.data.TableData;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 消息组件接口实现
 * 
 * @version 1.0
 * @author justin
 * @since java 1.6
 * 
 */
@Service
public class MessageBO implements IMessage {
  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao = null;

  /**
   * 查询全局事务提醒。
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @return List(内部含FTaskItemDTO)
   * @throws Exception---------错误信息
   */
  public TableData findAllTasks(String UserId, String RoleId, String Region, String set_year) throws Exception {
    List return_list = new ArrayList();

    List taskServiceList = new ArrayList();

    // 从工作流中取出事务提醒
    String strTmp = (String) SessionUtil.getParaMap().get("sys.taskServices");

    if (strTmp != null && !"".equals(strTmp.trim())) {

      StringTokenizer tk = new StringTokenizer(strTmp, "|");
      while (tk.hasMoreTokens()) {

        taskServiceList.add(tk.nextToken());
      }

    }
    //循环各行
    Iterator serviceIt = taskServiceList.iterator();
    while (serviceIt.hasNext()) {

      String service = (String) serviceIt.next();

      if (service != null && !service.equals("")) {
        List tmp_task_list = new ArrayList();
        try {
          //构建服务
          IMessageClient message = (IMessageClient) SessionUtil.getServerBean(service);
          // 业务系统实现待办接口调用开始时间
          long startTime = System.currentTimeMillis();
          System.out.println("业务系统:" + service + ",调用开始时间:" + startTime);
          tmp_task_list = message.findTasks(UserId, RoleId, Region, set_year);
          //业务系统实现待办接口调用结束时间
          long endTime = System.currentTimeMillis();
          System.out.println("业务系统:" + service + ",调用结束时间:" + endTime);
          System.out.println("业务系统:" + service + ",实现代办接口耗时:" + (endTime - startTime) + "毫秒");
        } catch (Exception e) {
        }
        for (int k = 0; k < tmp_task_list.size(); k++) {
          // 由于支付回单登记做过处理，需要在这边做下处理。当支付中有相同的功能id时，屏蔽掉工作流的全局事务提醒
          if (!"workFlowNewBO".equals(service)) {
            String taskModuleId = ((FTaskItemDTO) tmp_task_list.get(k)).getModule_id();
            for (int j = 0; j < return_list.size(); j++) {
              if (taskModuleId.equals(((FTaskItemDTO) return_list.get(j)).getModule_id())) {
                return_list.remove(j);
              }
            }
          }
          if (((FTaskItemDTO) tmp_task_list.get(k)).getTask_content() != null
            && !"".equals(((FTaskItemDTO) tmp_task_list.get(k)).getTask_content())) {
            return_list.add(tmp_task_list.get(k));
          }

        }

      }
    }
    //增加sysapp字段，解决点击门户中的待办事项无法进入系统的问题
    TableData data = new TableData(new Object[] { "msg_type_code", "msg_type_name", "module_id", "role_id",
      "menu_name", "menu_id", "task_content", "msg_type_name_local", "sysapp", "menu_url" });
    data.addDataByDataList(return_list);
    return data;
  }

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 发送消息接口。
   * 
   * @param FMessageDTO-------------FMessageDTO
   * @return true或false
   * @throws Exception---------错误信息
   */
  public boolean sendMessage(FMessageDTO msg) throws Exception {
    List user_list = new ArrayList();
    if (msg.getRole_id() == null || msg.getRole_id().toString().equals("")) {
      throw new Exception("请传入角色ID");
    }
    if (msg.getMsg_type_code() == null || msg.getMsg_type_code().toString().equals("")) {
      throw new Exception("请传入类型编码");
    }
    if (msg.getSend_type() == null || msg.getSend_type().toString().equals("")) {
      throw new Exception("请传入发送类型");
    }
    if (msg.getIs_temp() == null || msg.getIs_temp().toString().equals("")) {
      throw new Exception("请传入是否临时消息标志");
    }

    //如果没有传入用户ID，则根据角色ID查到对应的用户ID
    if (msg.getUser_id() == null || msg.getUser_id().toString().equals("")) {
      user_list = dao.findBySql("select * from sys_user_role_rule" + Tools.addDbLink() + " where role_id=?",
        new Object[] { msg.getRole_id() });
      for (int i = 0; i < user_list.size(); i++) {
        String user_id = (String) ((Map) user_list.get(i)).get("user_id");
        String role_id = (String) msg.getRole_id();
        saveSingleMessage(user_id, role_id, msg);
      }
    } else {
      String user_id = (String) msg.getUser_id();
      String role_id = (String) msg.getRole_id();
      saveSingleMessage(user_id, role_id, msg);
    }

    return true;
  }

  /**
   * 保存单条消息。
   * 
   * @param FMessageDTO-------------FMessageDTO
   * @return true或false
   * @throws Exception---------错误信息
   */
  private void saveSingleMessage(String user_id, String role_id, FMessageDTO msg) {

    FMessageDTO msg1 = null;
    try {
      msg1 = (FMessageDTO) msg.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    msg1.setMsg_id(UUIDRandom.generate());

    msg1.setIs_send(Integer.valueOf("0"));
    msg1.setIs_receive(Integer.valueOf("0"));
    msg1.setSend_num(Integer.valueOf("0"));

    dao.deleteDataBySql("sys_message", msg1, new String[] { "msg_id" });
    dao.saveDataBySql("sys_message", msg1);
  }

  /**
   * 检索消息接口。
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param plussql------------附加SQL
   * @param FPageDTO-----------分页
   * @return List(内部含FMessageDTO)
   * @throws Exception---------错误信息
   */
  public List findMessage(String user_id, String role_id, String plussql, FPaginationDTO page) throws Exception {
    List return_list = new ArrayList();
    String sql = "select * from sys_message" + Tools.addDbLink() + " where IS_RECEIVE=0 ";
    if (user_id != null && !user_id.equals("")) {
      sql = sql + " and user_id ='" + user_id + "'";
    }
    if (role_id != null && !role_id.equals("")) {
      sql = sql + " and role_id ='" + role_id + "'";
    }
    if (plussql != null && !plussql.equals("")) {
      sql = sql + plussql;
    }
    //传入PAGE不为空时，取PAGE里的数据
    if (page != null) {
      int current_page = page.getCurrpage();
      int page_count = page.getPagecount();
      // 当前页>1时，进行分页处理
      if (current_page > 1) {
        sql = "select subdata.* from (select "
          + (TypeOfDB.isOracle() ? "rownum r,data.* from(" : " @rn:=@rn+1 AS r, data.* FROM (SELECT @rn:=0) r, (")
          + sql + (TypeOfDB.isOracle() ? ") data where rownum <= " : ") data limit ") + (current_page * page_count)
          + ")subdata where subdata.r>" + (current_page - 1) * page_count;
        page.setCurrpage(current_page);

        //取总的行数
        List tmp_list = dao.findBySql("select count(1) as num from(" + sql + ")");
        if (tmp_list != null && tmp_list.size() > 0) {
          page.setRows(Integer.valueOf((String) ((Map) tmp_list.get(0)).get("num")).intValue());
        }
      }

    }

    return_list = dao.findBySql(sql);
    return return_list;
  }

  /**
   * 写消息接收成功标志。
   * 
   * @param msg_id-------------消息ID
   * @return true或false
   * @throws Exception---------错误信息
   */
  public boolean setReceiveflag(String msg_id) throws Exception {
    int num = 0;
    num = dao.executeBySql("update sys_message" + Tools.addDbLink() + " set IS_RECEIVE=1 where msg_id=?",
      new Object[] { msg_id });
    if (num > 0) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * findAllTask的扩展，根据rgcode、用户ID得到所有待办事宜
   * @param userId
   * @param sessionId
   * @param count
   * @param rgcode
   * @return
   * @throws Exception
  * @author zwh 20120517
   */
  public List findAllTaskByRegion(String userId, String sessionId, int count, String rgcode, String set_year)
    throws Exception {
    List taskList = null;
    //根据用户ID得到用户的ROLE_ID列表
    String getRoleIdSql = "select r.role_id from sys_role " + Tools.addDbLink() + " r , sys_user_role_rule "
      + Tools.addDbLink()
      + " ur where ur.user_id = ? and ur.role_id = r.role_id and r.enabled = 1 and ur.rg_code = ? and ur.set_year = ?";
    List roleIdList = dao.findBySql(getRoleIdSql, new Object[] { userId, rgcode, SessionUtil.getLoginYear() });
    if (roleIdList != null && roleIdList.size() != 0) {
      /*
       * 循环用户角色列表
       * 调用findAllTask(String UserId,String RoleId)
       * 根据参数count数目的待办事宜
       */
      int roleIdListSize = roleIdList.size();
      //循环角色,执行findAllTask方法
      taskList = new ArrayList();
      for (int i = 0; i < roleIdListSize; i++) {
        if (taskList.size() >= count) {
          break;
        }
        XMLData xmldata = (XMLData) roleIdList.get(i);
        String roleId = (String) xmldata.get("role_id");
        try {
          List list = findAllTasks(userId, roleId, rgcode, set_year).toDataList();
          if (list.size() != 0 && list != null) {
            for (Iterator it = list.iterator(); it.hasNext();) {
              if (taskList.size() >= count) {
                break;
              }
              taskList.add(it.next());
            }
          }
        } catch (Exception e) {
          throw e;
        }
      }
    }
    return taskList;
  }

  /**
   * 从ele_region中获取is_valid=1的记录，返回list中装载map,select e.chr_code,e.chr_name,e.is_top  from ele_region e  where  e.is_valid=1
   * @return
   * @throws Exception
   * @author zwh 20120517
   */
  public List getValidRegion() throws Exception {
    String sql = "select e.chr_code,e.chr_name,e.is_top  from ele_region e  where  e.is_valid=1";
    List rowList = this.getDao().findBySql(sql);
    return rowList;
  }

}
