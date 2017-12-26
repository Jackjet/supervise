package gov.df.fap.util.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CurrentUser implements Serializable, Cloneable {

  private static final long serialVersionUID = 3251481021811287036L;

  /**
   * 用户的主键字符串
   */
  private String userId;

  private String userCode;

  private String userName;

  /**
   * 用户选择的当前角色字符串
   */
  private String roleId;

  /**
   * 当前选择的菜单
   */
  private String menuId;

  private String menuName;

  private String wfMenuId;

  /**
   * 当前流程状态(未送审，已送审，被退回，已退回，已挂起，全部)
   */
  private String currentState;

  /**
   * 年度
   */
  private int setYear;

  /**
   * 交易凭证类型
   */
  private String billtype_code;

  /**
   * 单据类型
   */
  private String busbilltype_code;

  /**
   * 区划编码
   */
  private String rg_code;

  /**
   * 操作类型
   */
  private String actionType;

  /**
   * 操作id
   */
  private String operationId;

  /**
   * 操作名称
   */
  private String operationName;

  private String transDate;

  private boolean isBill = false;//单和明细  true 表示单  false 表示明细

  private String isBank;

  private String smallType;

  private String payCardType;

  private int doType = 1;//合单标识

  public String getIsBank() {
    return isBank;
  }

  public void setIsBank(String isBank) {
    this.isBank = isBank;
  }

  /**
   * 自定义往后台传输的对象
   */
  private Object object;

  //凭证是否已经存在于凭证库中  
  private boolean isExist;

  // 是否允许调用凭证库接口（用于测试环境只走流程）
  private boolean isSign;

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }

  public String getUserCode() {
    return userCode;
  }

  public void setUserCode(String userCode) {
    this.userCode = userCode;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setCurrentState(String currentState) {
    this.currentState = currentState;
  }

  public String getCurrentState() {
    return currentState;
  }

  public void setObject(Object object) {
    this.object = object;
  }

  public Object getObject() {
    return object;
  }

  public String getMenuId() {
    return menuId;
  }

  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }

  public int getSetYear() {
    return setYear;
  }

  public void setSetYear(int setYear) {
    this.setYear = setYear;
  }

  public String getRg_code() {
    return rg_code;
  }

  public void setRg_code(String rg_code) {
    this.rg_code = rg_code;
  }

  public String getBilltype_code() {
    return billtype_code;
  }

  public void setBilltype_code(String billtype_code) {
    this.billtype_code = billtype_code;
  }

  public String getActionType() {
    return actionType;
  }

  public void setActionType(String actionType) {
    this.actionType = actionType;
  }

  public String getOperationName() {
    return operationName;
  }

  public void setOperationName(String operationName) {
    this.operationName = operationName;
  }

  public String getBusbilltype_code() {
    return busbilltype_code;
  }

  public void setBusbilltype_code(String busbilltype_code) {
    this.busbilltype_code = busbilltype_code;
  }

  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public String getTransDate() {
    return transDate;
  }

  public void setTransDate(String transDate) {
    this.transDate = transDate;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getMenuName() {
    return menuName;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }

  public String getSmallType() {
    return smallType;
  }

  public void setSmallType(String smallType) {
    this.smallType = smallType;
  }

  public String getPayCardType() {
    return payCardType;
  }

  public void setPayCardType(String payCardType) {
    this.payCardType = payCardType;
  }

  public int getDoType() {
    return doType;
  }

  public void setDoType(int doType) {
    this.doType = doType;
  }

  public boolean isBill() {
    return isBill;
  }

  public void setBill(boolean isBill) {
    this.isBill = isBill;
  }

  public String getWfMenuId() {
    return wfMenuId;
  }

  public void setWfMenuId(String wfMenuId) {
    this.wfMenuId = wfMenuId;
  }

  public boolean isExist() {
    return isExist;
  }

  public void setExist(boolean isExist) {
    this.isExist = isExist;
  }

  public boolean isSign() {
    return isSign;
  }

  public void setSign(boolean isSign) {
    this.isSign = isSign;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Map toMap() {
    Map userMap = new HashMap();
    userMap.put("userId", getUserId());
    userMap.put("userCode", getUserCode());
    userMap.put("userName", getUserCode());
    userMap.put("roleId", getRoleId());
    userMap.put("moduleId", getMenuId());
    userMap.put("menuId", getMenuId());
    userMap.put("menuName", getMenuName());
    userMap.put("wfMenuId", getWfMenuId());
    userMap.put("setYear", String.valueOf(getSetYear()));
    userMap.put("rg_code", getRg_code());
    userMap.put("billtype_code", getBilltype_code());
    userMap.put("busbilltype_code", getBusbilltype_code());
    userMap.put("actionType", getActionType());
    userMap.put("operationId", getOperationId());
    userMap.put("operationName", getOperationName());
    userMap.put("transDate", getTransDate());
    userMap.put("smallType", getSmallType());
    userMap.put("isBank", getIsBank());
    userMap.put("payCardType", getPayCardType());
    userMap.put("doType", getDoType());
    userMap.put("isBill", isBill());
    userMap.put("isExist", isExist());
    userMap.put("isSign", isSign());
    return userMap;
  }

  @Override
  public Object clone() {
    CurrentUser user = null;
    try {
      user = (CurrentUser) super.clone();
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
    }

    return user;
  }

}
