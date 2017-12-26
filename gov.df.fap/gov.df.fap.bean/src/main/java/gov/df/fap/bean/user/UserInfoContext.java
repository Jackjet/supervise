package gov.df.fap.bean.user;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 服务端用户身份信息保存类 提供setAttribute(),getAttribute()便于访问
 * @version 1.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserInfoContext implements Serializable {

  private static final long serialVersionUID = 1L;

  // 用户保存用户信息的hashmap
  private HashMap context = new HashMap();

  public void setAttribute(Object attrKey, Object attrValue) {
    if (context.containsKey(attrKey)) {
      context.remove(attrKey);
      context.put(attrKey, attrValue);
    } else {
      context.put(attrKey, attrValue);
    }
  }

  public Object getAttribute(Object attrKey) {
    if (context.containsKey(attrKey)) {
      return context.get(attrKey);
    } else
      return null;
  }

  public HashMap getContext() {
    return context;
  }

  public void setContext(HashMap context) {
    this.context = context;
  }

  public String getRgCode() {

    return (String) context.get("rg_code");

  }

  public void setRgCode(String rg_code) {
    context.put("rg_code", rg_code);
  }

  public String getUserID() {

    String userId = (String) context.get("user_id");

    return userId;
  }

  public void setUserID(String userID) {
    context.put("user_id", userID);
  }

  public String getRoleID() {
    return (String) context.get("role_id");
  }

  public void setRoleID(String roleID) {
    context.put("role_id", roleID);
  }

  public String getSysID() {
    return (String) context.get("sys_id");
  }

  public void setSysID(String sysID) {
    context.put("sys_id", sysID);
  }

  public String getSetYear() {
    return (String) context.get("set_year");
  }

  public void setSetYear(String setYear) {
    context.put("set_year", setYear);
  }

  public String getSetID() {
    return (String) context.get("set_id");
  }

  public void setSetID(String setID) {
    context.put("set_id", setID);
  }

  //  public String getModuleID() {
  //    return (String) context.get("module_id");
  //  }
  //
  //  public void setModuleID(String moduleID) {
  //    context.put("module_id", moduleID);
  //  }

  /**
   * 服务端设置用户区划
   * 
   * @return String 返回当前用户区划
   * @author 2007年6月21日修改
   */
  public String getCurrRegion() {
    return (String) context.get("rg_code");
  }

  public String getPageSize() {
    return (String) context.get("SYS_INTPAGE_SIZE");
  }

  public String getMenuID() {
    return ((String) context.get("menu_id"));
  }

  public void setMenuID(String menuID) {
    context.put("menu_id", menuID);
  }

  public String getSwitch() {
    return (String) context.get("switch01");
  }

  public void setSwitch(String switch01) {
    context.put("switch01", switch01);
  }

  public String getServerUrl() {
    return (String) context.get("server_url");
  }

  public void setServerUrl(String serverUrl) {
    context.put("server_url", serverUrl);
  }

  public String getLoginMode() {
    return (String) context.get("loginmode");
  }

  public void setLoginMode(String loginMode) {
    context.put("loginmode", loginMode);
  }

  public String getMbid() {
    return (String) context.get("belong_org");
  }

  public void setMbid(String mbid) {
    context.put("belong_org", mbid);
  }

  public String getOrgType() {
    return (String) context.get("org_type");
  }

  public void setOrgType(String orgType) {
    context.put("org_type", orgType);
  }

  public String getOrgCode() {
    return (String) context.get("org_code");
  }

  public void setOrgCode(String orgCode) {
    context.put("org_code", orgCode);
  }

  public String getBelongOrg() {
    return (String) context.get("belong_org");
  }

  public void setBelongOrg(String belongOrg) {
    context.put("belong_org", belongOrg);
  }

  public String getAuthorizedUserCode() {
    return (String) context.get("authorieduser_code");
  }

  public String getAuthorizedUserName() {
    return (String) context.get("authorieduser_name");
  }
}
