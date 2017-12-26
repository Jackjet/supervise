package gov.df.fap.service.util;

import gov.df.fap.service.util.memcache.MemCacheMap;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.data.TableData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存机制
 */
public class UtilCache {

  public static Map uiMap = null;

  public static Map billUiMap = null;

  public static Map nextNodeMap = null;

  public static Map backNodeMap = null;

  public static List rightEleList = null;

  public static Map rightEleMap = null;

  public static Map tableNameByBillTypeCodeMap = null;

  public static Map coaIDByBillTypeCodeMap = null;

  public static Map accreditInfoMap = null;

  //节点同步标志MAP
  public static Map gatherNodeSignMap = null;

  //规则表达式缓存
  public static Map ruleConditionExpressionMap = null;

  //工作流状态名称缓存
  public static Map statusCode4NameMap = null;

  //关联关系id缓存
  public static Map eleRelationIDMap = null;

  //角色菜单缓存
  public static Map roleMenuCache = null;

  //功能、角色节点缓存
  public static Map moduleRoleNodeIDCache = null;

  static {
    //在客户端不能初始化
    //if (SessionUtil.isServerLog()) {
    uiMap = MemCacheMap.getCacheMap(UtilCache.class, "UI");

    billUiMap = MemCacheMap.getCacheMap(UtilCache.class, "billUi");

    nextNodeMap = MemCacheMap.getCacheMap(UtilCache.class, "nextNode");

    backNodeMap = MemCacheMap.getCacheMap(UtilCache.class, "backNode");

    rightEleMap = MemCacheMap.getCacheMap(UtilCache.class, "rightEle");

    tableNameByBillTypeCodeMap = MemCacheMap.getCacheMap(UtilCache.class, "tableName");

    coaIDByBillTypeCodeMap = MemCacheMap.getCacheMap(UtilCache.class, "coaIDByBillType");

    accreditInfoMap = MemCacheMap.getCacheMap(UtilCache.class, "accreditInfo");

    gatherNodeSignMap = MemCacheMap.getCacheMap(UtilCache.class, "gatherNode");

    ruleConditionExpressionMap = MemCacheMap.getCacheMap(UtilCache.class, "ruleCondition");

    statusCode4NameMap = MemCacheMap.getCacheMap(UtilCache.class, "statusCode4Name");

    eleRelationIDMap = MemCacheMap.getCacheMap(UtilCache.class, "eleRelation");

    roleMenuCache = MemCacheMap.getCacheMap(UtilCache.class, "roleMenu");

    moduleRoleNodeIDCache = MemCacheMap.getCacheMap(UtilCache.class, "moduleRoleNode");
    // }
  }

  public static TableData getRoleMenu(String roleId) {
    roleId = getTempKey(roleId);
    if (roleMenuCache == null) {
      roleMenuCache = new HashMap();
      return null;
    } else {
      TableData result = (TableData) roleMenuCache.get(roleId);
      return result;
    }
  }

  public static boolean putRoleMenu(String roleId, TableData result) {
    roleId = getTempKey(roleId);
    if (roleMenuCache == null) {
      roleMenuCache = new HashMap();
    }
    if (roleMenuCache.containsKey(roleId)) {
      roleMenuCache.remove(roleId);
      roleMenuCache.put(roleId, result);
    } else {
      roleMenuCache.put(roleId, result);
    }
    return true;
  }

  public static boolean removeRoleMenu(String roleId) {
    roleId = getTempKey(roleId);
    if (roleMenuCache == null) {
      roleMenuCache = new HashMap();
    }
    if (roleMenuCache.containsKey(roleId)) {
      roleMenuCache.remove(roleId);
    }
    return true;
  }

  public static String getEleRelationID(String eleCode) {
    eleCode = getTempKey(eleCode);
    if (eleRelationIDMap == null) {
      return null;
    } else {
      String return_str = "";
      if (eleRelationIDMap.containsKey(eleCode))
        return_str = eleRelationIDMap.get(eleCode) == null ? "" : (String) eleRelationIDMap.get(eleCode);
      return return_str;
    }
  }

  public static void putEleRelationID(String eleCode, String relation_id) {
    eleCode = getTempKey(eleCode);
    if (eleRelationIDMap == null) {
      eleRelationIDMap = new HashMap();
    }

    if (eleRelationIDMap.containsKey(eleCode)) {
      eleRelationIDMap.remove(eleCode);
      eleRelationIDMap.put(eleCode, relation_id);
    } else {
      eleRelationIDMap.put(eleCode, relation_id);
    }
  }

  public UtilCache() {
  }

  public static String getStatusCode4Name(String statusCode) {
    statusCode = getTempKey(statusCode);
    if (statusCode4NameMap == null) {
      return null;
    } else {
      String return_str = null;
      if (statusCode4NameMap.containsKey(statusCode))
        return_str = (String) statusCode4NameMap.get(statusCode);
      return return_str;
    }
  }

  public static void putStatusCode4Name(String statusCode, String statusName) {
    statusCode = getTempKey(statusCode);
    if (statusCode4NameMap == null) {
      statusCode4NameMap = new HashMap();
    }

    if (statusCode4NameMap.containsKey(statusCode)) {
      statusCode4NameMap.remove(statusCode);
      statusCode4NameMap.put(statusCode, statusName);
    } else {
      statusCode4NameMap.put(statusCode, statusName);
    }
  }

  public static String getRuleConditionExpression(String condition_id) {

    condition_id = getTempKey(condition_id);
    if (ruleConditionExpressionMap == null) {
      return null;
    } else {
      String return_str = null;
      if (ruleConditionExpressionMap.containsKey(condition_id))
        return_str = (String) ruleConditionExpressionMap.get(condition_id);
      return return_str;
    }
  }

  public static void putRuleConditionExpression(String condition_id, String expression) {
    if (ruleConditionExpressionMap == null) {
      ruleConditionExpressionMap = new HashMap();
    }
    condition_id = getTempKey(condition_id);
    if (ruleConditionExpressionMap.containsKey(condition_id)) {
      ruleConditionExpressionMap.remove(condition_id);
      ruleConditionExpressionMap.put(condition_id, expression);
    } else {
      ruleConditionExpressionMap.put(condition_id, expression);
    }
  }

  public static String getGatherNodeSign(String node_id) {
    node_id = getTempKey(node_id);
    if (gatherNodeSignMap == null) {
      return null;
    } else {

      String return_str = null;
      if (gatherNodeSignMap.containsKey(node_id))
        return_str = (String) gatherNodeSignMap.get(node_id);
      return return_str;
    }
  }

  public static void putGatherNodeSign(String node_id, String gatherFlag) {
    node_id = getTempKey(node_id);
    if (gatherNodeSignMap == null) {
      gatherNodeSignMap = new HashMap();
    }

    if (gatherNodeSignMap.containsKey(node_id)) {
      gatherNodeSignMap.remove(node_id);
      gatherNodeSignMap.put(node_id, gatherFlag);
    } else {
      gatherNodeSignMap.put(node_id, gatherFlag);
    }
  }

  /**
   * 去掉同步，视图在获取时，己经加过了
   * @param uiid
   * @return
   */
  public static String getUIXML(String uiid) {
    uiid = getTempKey(uiid);
    if (uiMap == null) {
      uiMap = new HashMap();
      return null;
    } else {
      return (String) uiMap.get(uiid);
    }
  }

  public static boolean putUIXML(String uiid, String xml) {
    uiid = getTempKey(uiid);
    if (uiMap == null) {
      uiMap = new HashMap();

    }
    if (uiMap.containsKey(uiid)) {
      uiMap.remove(uiid);
      uiMap.put(uiid, xml);
    } else {
      uiMap.put(uiid, xml);
    }
    return true;
  }

  public static boolean removeUIXML(String uiid) {
    uiid = getTempKey(uiid);
    if (uiMap == null) {
      uiMap = new HashMap();
    }
    if (uiMap.containsKey(uiid)) {
      uiMap.remove(uiid);
    }
    return true;
  }

  /*
   * update by lgc 20070309 start
   */
  public static String getBilltypeIdUIXML(String uiid, String billTypeid) {
    uiid = getTempKey(uiid);
    if (billUiMap != null && !billUiMap.isEmpty() && billUiMap.containsKey(uiid)) {

      Map return_Map = (Map) billUiMap.get(uiid);
      String return_str = null;
      if (return_Map.containsKey(billTypeid)) {
        return_str = (String) return_Map.get(billTypeid);
      }
      return return_str;

    } else {
      return null;
    }

  }

  public static boolean putBilltypeIdUIXML(String uiid, String billTypeid, String xml) {
    uiid = getTempKey(uiid);
    Map bill = new HashMap();
    bill.put(billTypeid, xml);
    if (billUiMap == null) {
      billUiMap = new HashMap();

    }
    if (billUiMap.containsKey(uiid)) {
      billUiMap.remove(uiid);
      billUiMap.put(uiid, bill);
    } else {
      billUiMap.put(uiid, bill);
    }
    return true;

  }

  /*
   * update by lgc 20070309 end
   */

  /*
   * update by daiwei 20070306 start
   */
  public static List getAccreditInfo(String tableName) {
    tableName = getTempKey(tableName);
    if (accreditInfoMap == null) {
      accreditInfoMap = new HashMap();
      return null;
    } else {
      List return_accreditInfo = (List) accreditInfoMap.get(tableName);
      return return_accreditInfo;
    }
  }

  public static boolean putAccreditInfo(String tableName, List resultList) {
    tableName = getTempKey(tableName);
    if (accreditInfoMap == null) {
      accreditInfoMap = new HashMap();

    }
    if (accreditInfoMap.containsKey(tableName)) {
      accreditInfoMap.remove(tableName);
      accreditInfoMap.put(tableName, resultList);
    } else {
      accreditInfoMap.put(tableName, resultList);
    }
    return true;
  }

  public static String getTableNameByBillTypeCode(String billType_code) {
    String setYear = (String) SessionUtil.getUserInfoContext().getSetYear();
    String rgCode = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");
    billType_code += setYear;
    billType_code += rgCode;
    if (tableNameByBillTypeCodeMap == null) {
      tableNameByBillTypeCodeMap = new HashMap();
      return null;
    } else {
      String return_str = (String) tableNameByBillTypeCodeMap.get(billType_code);
      return return_str;
    }
  }

  public static boolean putTableNameByBillTypeCode(String billType_code, String tableName) {
    String setYear = (String) SessionUtil.getUserInfoContext().getSetYear();
    String rgCode = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");
    billType_code += setYear;
    billType_code += rgCode;

    if (tableNameByBillTypeCodeMap == null) {
      tableNameByBillTypeCodeMap = new HashMap();

    }
    if (tableNameByBillTypeCodeMap.containsKey(billType_code)) {
      tableNameByBillTypeCodeMap.remove(billType_code);
      tableNameByBillTypeCodeMap.put(billType_code, tableName);
    } else {
      tableNameByBillTypeCodeMap.put(billType_code, tableName);
    }
    return true;
  }

  public static String getCoaIDByBillTypeCode(String billType_code) {

    billType_code = getTempKey(billType_code);
    if (coaIDByBillTypeCodeMap == null) {
      coaIDByBillTypeCodeMap = new HashMap();
      return null;
    } else {
      String return_str = (String) coaIDByBillTypeCodeMap.get(billType_code);
      return return_str;
    }
  }

  public static boolean putCoaIDByBillTypeCode(String billType_code, String coa_id) {
    billType_code = getTempKey(billType_code);
    if (coaIDByBillTypeCodeMap == null) {
      coaIDByBillTypeCodeMap = new HashMap();
    }
    if (coaIDByBillTypeCodeMap.containsKey(billType_code)) {
      coaIDByBillTypeCodeMap.remove(billType_code);
      coaIDByBillTypeCodeMap.put(billType_code, coa_id);
    } else {
      coaIDByBillTypeCodeMap.put(billType_code, coa_id);
    }
    return true;
  }

  /*
   * update by daiwei 20070306 end
   */
  public static void putRightEleList(List inpuList) {
    if (rightEleList == null) {
      rightEleList = new ArrayList();

    }
    rightEleList = inpuList;
  }

  public static List getRightEleList() {
    if (rightEleList == null) {
      return null;
    } else {
      return rightEleList;
    }
  }

  /*
   * add by wanghongtao 带年度信息  2009-01-04 
   */
  public static List getRightEleList(String year_rg) {
    if (rightEleMap == null) {
      rightEleMap = new HashMap();
      return null;
    } else {
      List return_rightEleList = (List) rightEleMap.get(year_rg);
      return return_rightEleList;
    }
  }

  /*
   * add by wanghongtao  带年度信息 2009-01-04
   */
  public static boolean putRightEleList(String year_rg, List inpuList) {
    if (rightEleMap == null) {
      rightEleMap = new HashMap();

    }
    if (rightEleMap.containsKey(year_rg)) {
      rightEleMap.remove(year_rg);
      rightEleMap.put(year_rg, inpuList);
    } else {
      rightEleMap.put(year_rg, inpuList);
    }
    return true;
  }

  public static List getNextNode(String nodeId) {
    nodeId = getTempKey(nodeId);
    if (nextNodeMap == null) {
      nextNodeMap = new HashMap();
      return null;
    } else {
      List return_list = (List) nextNodeMap.get(nodeId);
      return return_list;
    }
  }

  public static boolean putNextNode(String nodeId, List resultList) {
    nodeId = getTempKey(nodeId);
    if (nextNodeMap == null) {
      nextNodeMap = new HashMap();

    }
    if (nextNodeMap.containsKey(nodeId)) {
      nextNodeMap.remove(nodeId);
      nextNodeMap.put(nodeId, resultList);
    } else {
      nextNodeMap.put(nodeId, resultList);
    }
    return true;
  }

  public static List getBackNode(String nodeId) {
    nodeId = getTempKey(nodeId);
    if (backNodeMap == null) {
      backNodeMap = new HashMap();
      return null;
    } else {
      List return_list = (List) backNodeMap.get(nodeId);
      return return_list;
    }
  }

  public static boolean putBackNode(String nodeId, List resultList) {
    nodeId = getTempKey(nodeId);
    if (backNodeMap == null) {
      backNodeMap = new HashMap();

    }
    if (backNodeMap.containsKey(nodeId)) {
      backNodeMap.remove(nodeId);
      backNodeMap.put(nodeId, resultList);
    } else {
      backNodeMap.put(nodeId, resultList);
    }
    return true;
  }

  /**
   * 取功能、角色节点ID
   * @param nodeId
   * @author Administrator 李文全
   * @data 2013-06-17
   * @return
   */
  public static String getNodeIdByModuleRole(String moduleroleid) {
    String moduleroleidKey = getTempKey(moduleroleid);
    String rgYearKey = getRgYearKey();
    return (String) ((Map) moduleRoleNodeIDCache.get(rgYearKey)).get(moduleroleidKey);
  }

  /**
   * 存功能、角色节点ID
   * @param moduleroleid 功能角色ID
   * @param nodeIds 功能角色对应的节点ID,如果多个以逗号","隔开
   * @author Administrator 李文全
   * @data 2013-06-17
   * @return
   */
  public static boolean putNodeIdByModuleRole(String moduleroleid, String nodeIds) {
    String moduleroleidKey = getTempKey(moduleroleid);
    String rgYearKey = getRgYearKey();
    if (moduleRoleNodeIDCache == null) {
      moduleRoleNodeIDCache = new HashMap();
    }
    if (!moduleRoleNodeIDCache.containsKey(rgYearKey)) {
      //创建节点区划
      Map nodeMap = new HashMap();
      nodeMap.put(moduleroleidKey, nodeIds);
      moduleRoleNodeIDCache.put(rgYearKey, nodeMap);
    } else {
      Map rgNodeMap = (Map) moduleRoleNodeIDCache.get(rgYearKey);
      if (!rgNodeMap.containsKey(moduleroleidKey)) {
        rgNodeMap.put(moduleroleidKey, nodeIds);
        moduleRoleNodeIDCache.remove(rgYearKey);
        moduleRoleNodeIDCache.put(rgYearKey, rgNodeMap);
      } else {
        String value = (String) rgNodeMap.get(moduleroleidKey);
        if (StringUtil.isNull(value) && !StringUtil.isNull(nodeIds)) {
          rgNodeMap.put(moduleroleidKey, nodeIds);
          moduleRoleNodeIDCache.remove(rgYearKey);
          moduleRoleNodeIDCache.put(rgYearKey, rgNodeMap);
        }
      }
    }
    return true;
  }

  /**
   * 按区划删除功能、角色节点ID
   * @author Administrator 李文全
   * @data 2013-06-17
   * @return
   */
  public static void removeNodeIdByRGYear() {
    String rgYearKey = getRgYearKey();
    moduleRoleNodeIDCache.remove(rgYearKey);
  }

  /**
   *  根据年度+区划获取缓存key
   * @param key
   * @return  带有年度+区划的缓存key
   */
  public static String getTempKey(String key) {
    //true服务端,false客户端
    boolean isServerLog = SessionUtil.isServerLog();
    key = key + SessionUtil.getLoginYear() + SessionUtil.getRgCode();
    return key;
  }

  /**
   *  根据年度+区划获取缓存key
   * @param key
   * @return  带有年度+区划的缓存key
   */
  public static String getRgYearKey() {
    //true服务端,false客户端
    boolean isServerLog = SessionUtil.isServerLog();
    String key = null;
    key = SessionUtil.getLoginYear() + SessionUtil.getRgCode();
    return key;
  }

}