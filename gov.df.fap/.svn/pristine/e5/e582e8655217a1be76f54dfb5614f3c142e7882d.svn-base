package gov.df.fap.bean.portal;

import java.io.Serializable;

/**
 * 常用操作关系非持久bean
 * <p>AP_LINK_PORTLET</p>
 */
public class ApLinkPortletBean implements Serializable  {

  private static final long serialVersionUID = 5238567474038446322L;

  /**
   * 频道ID<br>
   * <b>暂定为 "CZB2.0"</b>
   */
  private String pg_plet_id = "CZB2.0";
  
  /**
   * {@link gov.df.fap.bean.portal.ApLinkBean} ApLinkBean.guid
   */
  private String link_guid;
  
  /**
   * 菜单ID menu.guid
   */
  private String menu_id;
  
  /**
   * 接受授权的用户ID user.guid<br>
   * <b>默认*，即授权给任何人</b>
   */
  private String emp_code;
  
  /**
   * 接受授权的用户角色ID role.guid<br>
   * <b>默认*，即授权给任何人</b>
   */
  private String role_id;
  
  /**
   * 接受授权的单位ID co.guid<br>
   * <b>默认*，即授权给任何人</b>
   */
  private String co_code;
  
  /**
   * 发布授权确认的用户ID user.guid
   */
  private String pub_user;
  
  /**
   * 发布授权确认时间 YYYY-MM-DD HH24:MI:SS
   */
  private String pub_time;

  public ApLinkPortletBean() {
    super();
  }

  public ApLinkPortletBean(String pg_plet_id, String link_guid, String menu_id,
    String emp_code, String role_id, String co_code, String pub_user,
    String pub_time) {
    super();
    this.pg_plet_id = pg_plet_id;
    this.link_guid = link_guid;
    this.menu_id = menu_id;
    this.emp_code = emp_code;
    this.role_id = role_id;
    this.co_code = co_code;
    this.pub_user = pub_user;
    this.pub_time = pub_time;
  }

  public String getPg_plet_id() {
    return pg_plet_id;
  }

  public void setPg_plet_id(String pg_plet_id) {
    this.pg_plet_id = pg_plet_id;
  }

  public String getLink_guid() {
    return link_guid;
  }

  public void setLink_guid(String link_guid) {
    this.link_guid = link_guid;
  }

  public String getMenu_id() {
    return menu_id;
  }

  public void setMenu_id(String menu_id) {
    this.menu_id = menu_id;
  }

  public String getEmp_code() {
    return emp_code;
  }

  public void setEmp_code(String emp_code) {
    this.emp_code = emp_code;
  }

  public String getRole_id() {
    return role_id;
  }

  public void setRole_id(String role_id) {
    this.role_id = role_id;
  }

  public String getCo_code() {
    return co_code;
  }

  public void setCo_code(String co_code) {
    this.co_code = co_code;
  }

  public String getPub_user() {
    return pub_user;
  }

  public void setPub_user(String pub_user) {
    this.pub_user = pub_user;
  }

  public String getPub_time() {
    return pub_time;
  }

  public void setPub_time(String pub_time) {
    this.pub_time = pub_time;
  }
  
}
