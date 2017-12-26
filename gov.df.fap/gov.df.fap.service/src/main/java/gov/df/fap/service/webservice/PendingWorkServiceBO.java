package gov.df.fap.service.webservice;

import gov.df.fap.api.message.ISysMessage;
import gov.df.fap.api.user.ISysUser;
import gov.df.fap.api.workflow.IPendingWorkService;
import gov.df.fap.api.workflow.message.IMessage;
import gov.df.fap.bean.workflow.FTaskItemDTO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Title: 待办事项webservices
 * </p>
 * 
 * <p>
 * Description: 对待办事项接口实现进行封装
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * 
 * <p>
 * Author: justin
 * </p>
 * 
 * <p>
 * Version 8.0
 * </p>
 */
public class PendingWorkServiceBO implements IPendingWorkService {

  private List messages = new ArrayList();

  private String local_IpAndPort = null;

  private List someList = null;

  public String getLocal_IpAndPort() {

    //读取系统参数
    String strTmp = (String) SessionUtil.getParaMap().get("sys.local_IpAndPort");
    if (strTmp != null && !"".equals(strTmp.trim())) {
      local_IpAndPort = strTmp.trim();
    }

    return local_IpAndPort;
  }

  public void setLocal_IpAndPort(String local_IpAndPort) {
    this.local_IpAndPort = local_IpAndPort;
  }

  public List getSomeList() {
    return someList;
  }

  public void setSomeList(List someList) {
    this.someList = someList;
  }

  /**
   * PendingWorkService无参构造函数 在此构造函数中取得IMessage接口实例
   */
  public PendingWorkServiceBO() {
    init();
  }

  /**
   * 初始化messags
   */
  public void init() {

    try {
      //读取本地messageBean配置
      if (messages.size() == 0) {
        IMessage message = (IMessage) SessionUtil.getServerBean("sys.messageBO");
        ISysMessage sysMessage = (ISysMessage) SessionUtil.getServerBean("sys.sysMsgBO");
        messages = new ArrayList();
        if (message != null && sysMessage != null) {
          Object[] iMessageAndUrl = new Object[3];
          iMessageAndUrl[0] = message;
          iMessageAndUrl[1] = this.getLocal_IpAndPort();
          iMessageAndUrl[2] = sysMessage;
          this.messages.add(iMessageAndUrl);
        }

        //				for (Iterator it = this.getSomeList().iterator(); it.hasNext();) {
        //					String ipAndPort = (String) it.next();
        //					/** modify 支持门户与应用系统集成部署 @author jerry 20071016 start*/
        //					String ipAndPortServer = ipAndPort;
        //					if("compositive".equals(ipAndPortServer) && this.local_IpAndPort != null) {
        //						ipAndPortServer = this.local_IpAndPort;
        //					}
        //					HessianProxyFactory factory = new HessianProxyFactory();
        //					IMessage message = (IMessage) factory.create(
        //							IMessage.class, "http://" + ipAndPortServer + serverURL
        //									+ "messageHttpInvoker");
        //					ISysMessage sysMessage = (ISysMessage) factory.create(
        //							ISysMessage.class, "http://" + ipAndPortServer
        //									+ serverURL + "sysMsgHttpInvoker");
        //					/** modify 支持门户与应用系统集成部署 @author jerry 20071016 end*/
        //					Object[] iMessageAndUrl = new Object[3];
        //					iMessageAndUrl[0] = message;
        //					iMessageAndUrl[1] = ipAndPort;
        //					iMessageAndUrl[2] = sysMessage;
        //					this.messages.add(iMessageAndUrl);
        //				}
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * 根据userid、sessionid、count、区划、年度查询待办事项列表
   * @param userId
   * @param sessionId
   * @param count
   * @param region
   * @return
   * @throws Exception
   * @author zhaoqiang 20140623
   */
  public Object[] findAllTaskByRegionAndYear(String userId, String sessionId, String count, String region, String year)
    throws Exception {
    init();
    //取得用户角色定义年度
    String curYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    ISysUser sysUser = (ISysUser) SessionUtil.getServerBean("sys.sysUserBO");
    List userRoles = sysUser.findSysUserRoleByUserIdAndYear(userId, year);
    if (userRoles != null && userRoles.size() > 0) {
      XMLData userRole = (XMLData) userRoles.get(0);
      curYear = (String) userRole.get("set_year");
    }

    Object[] resultObs = null;
    String ipAndPort = null;
    // 判断参数count是否可以转换成int型，如不能则countInt=10
    int countInt = 0;
    try {
      countInt = Integer.parseInt(count);
    } catch (Exception e) {
      countInt = 10;
    }
    if (this.messages != null && this.messages.size() != 0) {
      List taskList = null;
      resultObs = new Object[countInt];
      int obLength = 0;
      // 循环messagesList,得到不同服务的IMessage实例
      for (Iterator it = this.messages.iterator(); it.hasNext();) {
        if (obLength >= countInt) {
          break;
        }
        Object[] iMessageAndUrl = (Object[]) it.next();
        IMessage message = (IMessage) iMessageAndUrl[0];
        ipAndPort = (String) iMessageAndUrl[1];
        taskList = message.findAllTaskByRegion(userId, sessionId, countInt, region, year);
        if (taskList != null && taskList.size() != 0) {
          for (Iterator ite = taskList.iterator(); ite.hasNext();) {
            if (obLength >= countInt) {
              break;
            }
            FTaskItemDTO taskDTO = (FTaskItemDTO) ite.next();
            if (taskDTO != null) {// 对taskDTO中的数据进行验证
              String taskContent = null;
              if (taskDTO.getTask_content() != null && (!taskDTO.getTask_content().equals(""))) {
                taskContent = taskDTO.getTask_content();
              }
              String msgTypeNameLocal = null;
              if (taskDTO.getMenu_name() != null && (!taskDTO.getMenu_name().equals(""))) {
                msgTypeNameLocal = taskDTO.getMenu_name();
              } else if (taskDTO.getMsg_type_name_local() != null && (!taskDTO.getMsg_type_name_local().equals(""))) {
                msgTypeNameLocal = taskDTO.getMsg_type_name_local();
              }
              String msgTypeName = null;
              if (taskDTO.getMsg_type_name() != null && (!taskDTO.getMsg_type_name().equals(""))) {
                msgTypeName = taskDTO.getMsg_type_name();
              }
              String sysApp = null;
              if (taskDTO.getSysapp() != null && (!taskDTO.getSysapp().equals(""))) {
                sysApp = taskDTO.getSysapp();
              }
              String menuId = null;
              if (taskDTO.getMenu_id() != null && (!taskDTO.getMenu_id().equals(""))) {
                menuId = taskDTO.getMenu_id();
              }
              String roleId = null;
              if (taskDTO.getRole_id() != null && (!taskDTO.getRole_id().equals(""))) {
                roleId = taskDTO.getRole_id();
              }
              String moduleId = null;
              if (taskDTO.getModule_id() != null && !(taskDTO.getModule_id().equals(""))) {
                moduleId = taskDTO.getModule_id();
              }
              // 组装PendingWorkDTO
              if (taskContent != null && msgTypeNameLocal != null && msgTypeName != null) {
                Object[] obs = new Object[5];
                obs[0] = msgTypeNameLocal + "————" + taskContent;
                obs[1] = msgTypeName;
                /** modify 支持门户与应用系统集成部署 @author jerry 20071016 start*/
                if ("compositive".equals(ipAndPort)) {//如果为compositive则为门户与应用系统集中部署，使用相对地址
                  obs[2] = "/login?sysapp=" + sysApp + "&amp;uid=" + userId + "&amp;roleid=" + roleId + "&amp;menuid="
                    + menuId + "&amp;sid=" + sessionId + "&amp;moduleid=" + moduleId + "&amp;setyear=" + curYear
                    + "&amp;rgcode=" + region;
                } else {
                  obs[2] = "/login?sysapp=" + sysApp + "&amp;uid=" + userId + "&amp;roleid=" + roleId + "&amp;menuid="
                    + menuId + "&amp;sid=" + sessionId + "&amp;moduleid=" + moduleId + "&amp;setyear=" + curYear
                    + "&amp;rgcode=" + region;
                }
                /** modify 支持门户与应用系统集成部署 @author jerry 20071016 end*/

                //ymj 增加操作时间 
                if (taskDTO.getOperation_date() != null && !(taskDTO.getOperation_date().equals(""))) {
                  obs[3] = taskDTO.getOperation_date();
                } else {
                  obs[3] = Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) + "-"
                    + Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
                    + Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                }
                // ymj 增加角色
                if (taskDTO.getRole_name() != null && !(taskDTO.getRole_name().equals(""))) {
                  obs[4] = taskDTO.getRole_name();
                }
                resultObs[obLength] = obs;
                obLength = obLength + 1;
              }
            }
          }
        }
      }
    }

    if (resultObs == null || resultObs[0] == null) {
      Object[] ob1 = new Object[5];
      ob1[0] = "";
      ob1[1] = "";
      ob1[2] = "";
      ob1[3] = "";
      ob1[4] = "";
      resultObs[0] = ob1;
    }
    return resultObs;
  }

  /**
   * 从ele_region中获取is_valid=1的记录，返回list中装载map,select e.chr_code,e.chr_name,e.is_top  from ele_region e  where  e.is_valid=1
   * @return
   * @throws Exception
   * @author zwh 20120517
   */
  public List getValidRegion() throws Exception {
    IMessage message = null;
    if (messages.size() == 0) {
      message = (IMessage) SessionUtil.getServerBean("sys.messageBO");
    } else {
      message = (IMessage) messages.get(0);
    }
    return message.getValidRegion();
  }

}
