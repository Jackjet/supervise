package gov.df.fap.service.webservice;

import gov.df.fap.api.workflow.IPendingWorkService;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;

import java.util.Map;

/**
 * <p>Title: 待办事宜WebService</p>
 *
 * <p>Description: 提供给第三方的待办事宜WebService接口</p>
 *
 * <p>Copyright: Copyright (c) 2017</p>
 *
 * <p>Company: 北京用友政务软件有限公司</p>
 *
 * <p>Author: justin</p>
 * 
 * <p>Version 8.0</p> 
 */
public class PendingWorkWebService {

  private IPendingWorkService pendingWorkBO = null;

  private String ipAndPort = null;

  public String getIpAndPort() {
    return ipAndPort;
  }

  public void setIpAndPort(String ipAndPort) {
    this.ipAndPort = ipAndPort;
  }

  public IPendingWorkService getPendingWorkBO() {

    if (pendingWorkBO == null) {

      System.out.println("取得本地pendingWorkBO对象");
      pendingWorkBO = (IPendingWorkService) SessionUtil.getServerBean("sys.pendingWorkBO");

    }
    return pendingWorkBO;
  }

  public void setPendingWorkBO(IPendingWorkService pendingWorkBO) {
    this.pendingWorkBO = pendingWorkBO;
  }

  public void init() {

  }

  /**
   * 根据userid、sessionid、count、区划、年度查询待办事项列表
   * @param userId
   * @param sessionId
   * @param count
   * @param region
   * @return
   * @throws Exception
   * @author  20170622
   */
  public Object[] findAllTaskByRegionAndYear(String userId, String sessionId, String count, String region, String year)
    throws Exception {
    init();
    return getPendingWorkBO().findAllTaskByRegionAndYear(userId, sessionId, count, region, year);
  }

  /**
   * 根据userid、sessionid、count、区划查询待办事项列表
   * @param userId
   * @param sessionId
   * @param count
   * @param region
   * @return
   * @throws Exception
   * @author  20170622
   * userCode, sessionId, year，country, region 
   */
  public String findAllTaskByRegion(String userId, String sessionId, String year, String count, String region)
    throws Exception {
    if (StringUtil.isNull(year) || StringUtil.isNull(region))
      System.out.println("区划或年度为空！");
    Object[] objs = this.findAllTaskByRegionAndYear(userId, sessionId, count, region, year);
    return this.objsToString(objs);
  }

  /**
   * 把objs按照报文规范转换为String
   * @param objs
   * @return
   * @throws Exception
   * @author zwh 20120517
   */
  private String objsToString(Object[] objs) throws Exception {
    String xmlStr = "";
    if (objs != null) {
      xmlStr = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
      xmlStr += "<ROOT>";
      xmlStr += "  <FUNCTION NAME=\"财政一体化\" DESCRIPTION=\"\">";
      for (int i = 0; i < objs.length; i++) {
        Object obj = objs[i];
        if (obj != null) {
          if (obj instanceof Object[]) {
            Object[] objTask = (Object[]) obj;
            xmlStr += "    <MENU>";
            xmlStr += "      <URL>" + objTask[2] + "</URL>";
            xmlStr += "      <NAME>" + objTask[0] + "</NAME>";
            xmlStr += "      <NUMBER></NUMBER>";
            xmlStr += "    </MENU>";
          }
        }
      }
      xmlStr += "  </FUNCTION>";
      xmlStr += "</ROOT>";
    }
    return xmlStr;
  }

  public String getTestString(String uId, String sId, String year, String region) {
    String testString = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
    testString += "<ROOT>";
    testString += "  <FUNCTION NAME=\"财政一体化待办\" DESCRIPTION=\"财政一体化测试\">";
    testString += "    <MENU>";
    testString += "      <URL>/login?sysapp=800&amp;uid=7886&amp;roleid=7885&amp;menuid=800001030001&amp;sid=" + sId
      + "&amp;moduleid=111020001&amp;setyear=2012&amp;rgcode=" + region + "</URL>";
    testString += "      <NAME>年初控制数录入</NAME>";
    testString += "      <NUMBER>1</NUMBER>";
    testString += "    </MENU>";
    testString += "    <MENU>";
    testString += "      <URL>/login?sysapp=800&amp;uid=7886&amp;roleid=7885&amp;menuid=800002001101&amp;sid=" + sId
      + "&amp;moduleid=112020016&amp;setyear=2012&amp;rgcode=" + region + "</URL>";
    testString += "      <NAME>单位用款计划录入</NAME>";
    testString += "      <NUMBER>1</NUMBER>";
    testString += "    </MENU>";
    testString += "  </FUNCTION>";
    testString += "</ROOT>";
    return testString;
  }

  /**
   * 把objs按照报文规范转换为String（3.0门户与6.0服务待办接口专用）
   * @param objs
   * @return
   * @throws Exception
   * @author lwq 20120517
   */
  private String tranceToString(Object[] objs, Map tmpMap) throws Exception {
    String xmlStr = "";
    if (objs != null) {
      xmlStr = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
      xmlStr += "<Root>";
      xmlStr += "<Head>";
      xmlStr += "  <src>" + tmpMap.get("src") + "</src>";
      xmlStr += "  <des>" + tmpMap.get("des") + "</des>";
      xmlStr += "  <dataType>" + tmpMap.get("dataType") + "</dataType>";
      xmlStr += "</Head>";
      xmlStr += "<Body>";
      xmlStr += "<Object>";
      for (int i = 0; i < objs.length; i++) {
        Object obj = objs[i];
        if (obj != null) {
          if (obj instanceof Object[]) {
            Object[] objTask = (Object[]) obj;
            xmlStr += " <Record>";
            xmlStr += "  <title>" + objTask[0] + "</title>";
            xmlStr += "  <link>" + objTask[2] + "</link>";
            //xmlStr += "      <number></number>";
            xmlStr += " </Record>";
          }
        }
      }
      xmlStr += "</Object>";
      xmlStr += "</Body>";
      xmlStr += "<Signs>";
      xmlStr += "<Sign>";
      xmlStr += "</Sign>";
      xmlStr += "<Stamp>";
      xmlStr += "</Stamp>";
      xmlStr += "</Signs>";
      xmlStr += "</Root>";
    }
    return xmlStr;
  }

}
