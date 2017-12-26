package gov.df.fap.service.util.sessionmanager;

import gov.df.fap.bean.user.UserDTO;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.memcache.MemCacheMap;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpSession;


public class OnlineUsers {
  private Map usersOnline = MemCacheMap.getCacheMap(OnlineUsers.class, "1");

  private Map users = new Hashtable();

  private static OnlineUsers onlineUsers = new OnlineUsers();

  private OnlineUsers() {
  }

  public static OnlineUsers getInstance() {
    return onlineUsers;
  }

  public String getKey(UserDTO user) {
    return user.getUser_id() + user.getSet_year() + user.getRg_code();
  }

  /**
   * 检查用户是否存在
   * @param key
   * @return
   */
  public boolean examineIsOnline(String key) {
	  HttpSession session = (HttpSession) usersOnline.get(key);
	    boolean isnew = false;
	    try {
	      isnew = session.isNew();
	    } catch (Exception e) {
	      usersOnline.remove(key);
	      return false;
	    }
	    return !isnew && usersOnline.containsKey(key);
  }

  /**
   * 删除在线用户
   * @param key
   */
  public void removeUser(String key) {
	  if (usersOnline.get(key) != null) {
	      try {
	        HttpSession session = (HttpSession) usersOnline.get(key);
	        session.invalidate();
	      } catch (Exception e) {
	        System.out.println("Session is already invalid!");
	      } finally {
	        usersOnline.remove(key);
	      }
	    }
  }

  /**
   * 添加在线用户
   * @param user
   */
  public void addUser(UserDTO user) {
    String isValid = (String) SessionUtil.getParaMap().get("is_single_user");
    if (isValid != null && "1".equals(isValid))
      usersOnline.put(getKey(user), SessionUtil.getHttpSession());
    users.put(user.getSession_id(), user);
    Map userLogin = new HashMap();
    userLogin.put("user_id", user.getUser_id());
    userLogin.put("user_name", user.getUser_name());
    userLogin.put("user_ip", user.getUser_ip());
    userLogin.put("sys_id", user.getSys_app());
    userLogin.put("log_type", "0");
    userLogin.put("log_level", new Integer(0));
    userLogin.put("oper_time", formatDateText());
    userLogin.put("remark", user.getUser_name() + "登录系统");
    userLogin.put("log_id", UUIDRandom.generate());
    userLogin.put("rg_code", user.getRg_code());
  }

  /**
   * 取服务器本地日期时间.
   * 
   * @return 格式化后的日期时间字符
   */
  private String formatDateText() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  /**
   * 根据用户key获取用户的sessionid值
   * @param userKey
   * @return
   * @author songfp 20150512
   */
  public String getSessionIdByUserKey(String userKey) {
    if (usersOnline.get(userKey) == null) {
      return null;
    }
    HttpSession session = (HttpSession) usersOnline.get(userKey);
    String sessionid = session.getId();
    return sessionid;
  }

  public void removeUser(String sessionid, int type) {
    if (users.containsKey(sessionid)) {
      UserDTO user = (UserDTO) users.get(sessionid);
      Map userLogin = new HashMap();
      userLogin.put("user_id", user.getUser_id());
      userLogin.put("user_name", user.getUser_name());
      userLogin.put("user_ip", user.getUser_ip());
      userLogin.put("log_type", "0");
      userLogin.put("log_level", new Integer(0));
      userLogin.put("oper_time", formatDateText());
      String typeMsg = "登录失效，退出系统";
      if (type == 1)
        typeMsg = "离开了系统";
      userLogin.put("remark", user.getUser_name() + typeMsg);
      userLogin.put("log_id", UUIDRandom.generate());
      userLogin.put("rg_code", user.getRg_code());
      String userName = user.getUser_name();
      // 在用推出系统时，去除用于验证用户是否从web登录的cookie
      SessionUtil.removeCookie(user.getUser_ip());
      users.remove(sessionid);
      OnlineSessionInvalid.getInstance().removeInvalidSessionListerner(sessionid);
    }
  }

  public Collection getUsers() {
    return users.values();
  }

  public UserDTO getCurrUser(String sessionid) {
    if (users.containsKey(sessionid)) {
      return (UserDTO) users.get(sessionid);
    }
    return null;
  }

}
