package gov.df.supervise.service.common;

import gov.df.fap.bean.user.UserInfoContext;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

/**
 * 扩展fap平台SessionUtil工具类
 * @author hujhb
 *
 */
public class SessionUtilEx extends SessionUtil {

  /** 所属机构类型. */
  public static final String BELONG_TYPE = "belong_type";

  /** 所属机构单位编码. */
  public static final String BELONG_ORG_CODE = "belong_org_code";

  /** 所属机构单位编码. */
  public static final String BELONG_ORG_NAME = "belong_org_name";

  /** The Constant BELONG_DIV_LEVEL_NUM. */
  public static final String BELONG_DIV_LEVEL_NUM = "belong_div_level_num";

  /** The Constant BELONG_DIV_IS_LEAF. */
  public static final String BELONG_DIV_IS_LEAF = "belong_div_is_leaf";

  /** The Constant USER_DATA_TYPE. */
  public static final String USER_DATA_TYPE = "user_data_type";

  /** 用户所履属多数据类型 */
  public static final String USER_DATA_TYPE_MULTI = "user_data_type_multi";

  /** The Constant DLIB_BIZ_CODE 滚动项目库流程编码 */
  public static final String DLIB_BIZ_CODE = "dlib_biz_code";

  /** The Constant FB_BIZ_CODE 预算编审流程编码 */
  public static final String FB_BIZ_CODE = "fb_biz_code";

  /** The Constant FB_BIZ_CODE 预算编审流程编码 */
  public static final String DLIB_FINAL_DATA_TYPE = "dlib_final_data_type";

  /** The Constant KEY_USER_ID. */
  public static final String KEY_USER_ID = "##KEY_USER_ID##";

  /**
   * 根据传入的用户ID,获取对应的用户所属机构编码
   * 
   * @return 用户所属机构编码
   * @throws Exception
   */

  public static String getUserBeLongOrg() throws Exception {
    String org_id = "";
    try {
      org_id = (String) SessionUtil.getUserInfoContext().getAttribute("belong_org_code");
    } catch (Exception e) {
      throw e;
    }
    return org_id;
  }

  /**
   * 根据传入的用户ID,获取对应的用户所属机构类型
   * 
   * @return 用户所属机构类型
   * @throws Exception
   */

  public static String getUserBeLongType() throws Exception {
    String org_id = "";
    try {
      org_id = (String) SessionUtil.getUserInfoContext().getAttribute("belong_type");
    } catch (Exception e) {
      throw e;
    }
    return org_id;
  }

  /**
   * 根据传入的用户ID,获取对应的用户的user_data_type
   * 
   * @return  user_data_type
   * @throws Exception
   */

  public static String getUserDataType() throws Exception {
    String org_id = "";
    try {
      org_id = (String) SessionUtil.getUserInfoContext().getAttribute("user_data_type");
    } catch (Exception e) {
      throw e;
    }
    return org_id;
  }

  /**
   * 获取用户子系统
   * @return
   */
  public static String getFbSysID() {
    return (String) getUserInfoContext().getAttribute("FB_SYS_ID");
  }

  /**
   * 检查clientContext中的用户信息
   */
  private static synchronized void syncUserInfoContext(UserInfoContext userInfoContext) {
    String keyUserId = (String) userInfoContext.getAttribute(KEY_USER_ID);
    String keyUser = SessionUtil.getUserInfoContext().getUserID() + "##" + SessionUtil.getUserInfoContext().getRoleID();
    if (!keyUser.equals(keyUserId)) {
      userInfoContext.setAttribute(BELONG_TYPE, null);
      userInfoContext.setAttribute(BELONG_ORG_CODE, null);
      userInfoContext.setAttribute(BELONG_DIV_LEVEL_NUM, null);
      userInfoContext.setAttribute(BELONG_DIV_IS_LEAF, null);
      userInfoContext.setAttribute(USER_DATA_TYPE, null);
      userInfoContext.setAttribute(DLIB_BIZ_CODE, null);
      userInfoContext.setAttribute(FB_BIZ_CODE, null);
      userInfoContext.setAttribute(KEY_USER_ID, keyUser);
    }
  }

  /**
   * 获取当前登录用户所属区划
   */
  public static String getCurRegion() {
    syncUserInfoContext(getUserInfoContext());
    return (String) getUserInfoContext().getAttribute("cur_region");
  }

  public static void main(String[] args) {

  }

}
