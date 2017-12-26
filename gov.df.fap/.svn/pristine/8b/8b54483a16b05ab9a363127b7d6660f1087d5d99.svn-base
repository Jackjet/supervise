package gov.df.fap.service.util.sessionmanager;

import gov.df.fap.api.util.paramanage.IParaManage;
import gov.df.fap.bean.sysdb.SysRegion;
import gov.df.fap.bean.sysdb.SysYear;
import gov.df.fap.bean.user.UserDTO;
import gov.df.fap.bean.user.UserInfo;
import gov.df.fap.bean.user.UserInfoContext;
import gov.df.fap.service.util.datasource.MultiDataSource;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.factory.ServiceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.acegisecurity.event.authentication.AuthenticationSuccessEvent;
import org.acegisecurity.event.authorization.PublicInvocationEvent;
import org.acegisecurity.ui.WebAuthenticationDetails;
import org.acegisecurity.ui.session.HttpSessionDestroyedEvent;
import org.acegisecurity.userdetails.UserDetails;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpSession;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SessionUtil implements ApplicationListener {
  /**
   * 在线标示
   */
  public static final int ONLINE_TYPE = 0;

  /**
   * 离线标示
   */
  public static final int OFFLINE_TYPE = 2;

  /**
   * 供业务系统使用的全局变量
   */
  private static HashMap context = new HashMap();

  /**
   * 0: 三层；1：二层; 2：离线
   */
  private static int type = ONLINE_TYPE;

  /**
   * 应用程序上下文,在Spring初始化成功后,该变量被设置成Spring的Web Context
   */
  private static BeanFactory applicationContext = null;

  // 模拟离线的服务端全局变量
  private static BeanFactory offapplicationContext = null;

  // 用户列表
  private static Hashtable userList = new Hashtable();

  // 线程上下文
  private static ThreadLocal currentSession = new ThreadLocal();

  // 保存验证用户是否从web登录的用户cookie
  private static Map loginCookies = new HashMap();

  // 应用服务启动时加载的数据库默认年度
  private static String defaultYear = null;

  private static String defaultRgCode = null;// 默认区划--暂时应对大集中改造引起的一些bug，集中处理RGCODE，优先保证单区划可用

  private static boolean hasLoadDefaultRgCode = false;// 是否已经加载了默认区划，不管是否成功，只加载一次

  /**
   * 构造函数
   */
  public SessionUtil() {
  }

  /**
   * 根据用户ID取得验证用户是否从web登录的cookie
   * 
   * @param userId
   *            用户ID
   * @return 返回验证用户是否从web登录的cookie
   */
  public static String getLoginCookies(String userId) {
    return (String) loginCookies.get(userId);
  }

  /**
   * 设置验证用户是否从web登录的cookie
   * 
   * @param userId
   *            用户ID
   * @param cookie
   *            用户cookie
   */
  public static void setLoginCookies(String userId, String cookie) {
    loginCookies.put(userId, cookie);
  }

  /**
   * 从loginCookies中移出验证用户是否从web登录的cookie
   * 
   * @param userId
   *            用户ID
   */
  public static void removeCookie(String userId) {
    loginCookies.remove(userId);
  }

  /**
   * 获取离线端的bean
   * 
   * @param beanName
   *            bean名
   * @return bean对象
   * 
   */
  public static Object getOffServerBean(String beanName) {
    BeanFactory app = getOffApplicationFactory();

    if (app != null)
      return app.getBean(beanName);
    else
      return null;
  }

  /**
   * 获取离线的服务端全局变量
   * 
   * @return 离线的服务端全局变量对象
   * 
   */
  public static BeanFactory getOffapplicationContext() {
    BeanFactory app = getApplicationFactory();
    return app;
  }

  /**
   * 设置 离线的服务端全局变量
   * 
   * @param offapplicationContext
   *            离线的服务端全局变量对象
   * 
   */
  public static void setOffapplicationContext(BeanFactory offapplicationContext) {
    SessionUtil.offapplicationContext = offapplicationContext;
  }

  /**
   * 返回登录用户信息上下文
   * 
   * @return 登录用户信息上下文
   */
  public static UserInfoContext getUserInfoContext() {
    if (getType() == 0) {
      HttpSession httpSession = getHttpSession();
      Session session = (Session) httpSession.getAttribute("usercontext");
      return session.getUserInfoContext();
    } else {
      UserInfoContext userInfoContext = new UserInfoContext();
      userInfoContext.setContext(context);
      return userInfoContext;
    }
  }

  /**
   * 获得系统参数列表
   * 
   * @return 系统参数列表
   * 
   *update by kongcy at 2012-03-22 globalMap中和loginParaMap结构改成一致
   */
  public static Map getParaMap() {
    /**
     * add by fengdz  2013.03.25 
     * 系统参数全局缓存统一管理 <BR>
     * 在weblogic启动时，根据用户年度和区划缓存所有参数表数据 <BR>  
     * 数据库交互次数1次 减少数据库服务器压力 <BR>
     * 参数中途被修改后  可立即同步用户 全局缓存可实时更新(更新缓存内容而不是查询数据库) <BR>
     * 如果中途新启用了区划（启用过程需要复制参数表数据）只查询当前区划的数据然后更新全局缓存 <BR>
     * 全局缓存的生命周期与weiblobic生命周期相同<BR>
     */
    Map loginParaMap = null;
    IParaManage paraMan = null;
    paraMan = (IParaManage) SessionUtil.getServerBean("sys.paraManService");
    loginParaMap = paraMan.getGlobalParaMap();
    return loginParaMap;
    /**
     * add by fengdz  2013.03.25 
     * 系统参数全局缓存统一管理 <BR>
     * 在weblogic启动时，根据用户年度和区划缓存所有参数表数据 <BR>  
     * 数据库交互次数1次 减少数据库服务器压力 <BR>
     * 参数中途被修改后  可立即同步用户 全局缓存可实时更新(更新缓存内容而不是查询数据库) <BR>
     * 如果中途新启用了区划（启用过程需要复制参数表数据）只查询当前区划的数据然后更新全局缓存 <BR>
     * 全局缓存的生命周期与weiblobic生命周期相同<BR>
     */
  }

  /**
   * 判断是处在服务端还是在客户端
   * 
   * @return true服务端,false客户端
   * @author zhupeidong at 2008-2-22日下午04:55:27
   */
  public static boolean isServerLog() {
    boolean isServerLog = true;
    if (null == applicationContext)// applicationContext为空,为客户端
      isServerLog = false;
    return isServerLog;
  }

  /**
   * 获取SessionUtil的类型
   * 
   * @return SessionUtil的类型
   */
  public static int getType() {
    return type;
  }

  /**
   * 设置SessionUtil的类型
   * 
   * @param type
   *            SessionUtil的类型
   * 
   */
  public static void setType(int type) {
    SessionUtil.type = type;
  }

  /**
   * 获取在线用户的列表
   * 
   * @return 在线用户的列表
   * 
   */
  public static synchronized Hashtable getOnlineUsers() {
    return userList;
  }

  /**
   * 返回当前在线操作用户的UserDTO
   * 
   * @return 当前在线操作用户的UserDTO
   * 
   */
  public static UserDTO getOnlineUser() {
    return null;
  }

  /**
   * 根据UserDTO判断此用户是否在线
   * 
   * @param user
   *            要判断的用户UserDTO
   * @return true-在线，false-不在线
   * 
   */
  public static synchronized boolean checkOnlineUser(UserDTO user) {
    for (Iterator it = userList.values().iterator(); it.hasNext();)
      if (user.getUser_code().equals(((UserDetails) it.next()).getUsername()))
        return true;

    return false;
  }

  /**
   * 从在线信息里面删除当前在线操作用户
   * 
   */
  public static synchronized void removeCurrentUser() {
    HttpSession session = (HttpSession) currentSession.get();
    if (session != null)
      userList.remove(session.getId());
  }

  /**
   * 从SessionUtil中获得当前线程对应的HttpSession.
   * 
   * @return 当前线程对应的HttpSession.
   */
  public static HttpSession getHttpSession() {
    HttpSession session = (HttpSession) currentSession.get();

    if (session == null) {
      // 对于后台线程,当前的session为空，模拟一个Session
      session = new MockHttpSession();
      Session userSession = new Session();
      // 模拟用户的Context
      session.setAttribute("usercontext", userSession);
      currentSession.set(session);
    }
    return session;
  }

  /**
   * 设置HttpSession到SessionUtil的当前线程中
   * 
   * @param httpSession
   */
  public static void setHttpSession(HttpSession httpSession) {
    //添加支持还没有登录前的操作，需要将原有ThreadLocal中的上下文清空
    if (httpSession == null) {
      currentSession.set(null);
      return;
    }
    if (httpSession.getAttribute("usercontext") == null) {
      Session session = new Session();
      httpSession.setAttribute("usercontext", session);
    }
    currentSession.set(httpSession);
  }

  /**
   * 上下文刷新的响应事件
   * 
   * @param event
   *            响应事件
   * 
   */
  public void onApplicationEvent(ApplicationEvent event) {

    // 上下文刷新事件,设置系统的上下文
    if (event instanceof ContextRefreshedEvent) {
    }
    if (event instanceof PublicInvocationEvent) {

    }
    if (event instanceof AuthenticationSuccessEvent) {
      AuthenticationSuccessEvent ase = (AuthenticationSuccessEvent) event;
      Object other = ase.getAuthentication().getDetails();
      String switch01 = (String) getParaMap().get("switch01");
      if (switch01.equals("0")) {
        UserInfo userInfo = (UserInfo) ase.getAuthentication().getPrincipal();
        String userIP = ((WebAuthenticationDetails) ase.getAuthentication().getDetails()).getRemoteAddress();
        if (other != null && (other instanceof WebAuthenticationDetails)) {
          String id = ((WebAuthenticationDetails) other).getSessionId();
          userList.put(((WebAuthenticationDetails) other).getSessionId(), ase.getAuthentication());

          UserDTO u = new UserDTO();
          u.setSession_id(id);
          u.setUser_id(userInfo.getUser().getUser_id());
          u.setUser_code(userInfo.getUser().getUser_code());
          u.setUser_name(userInfo.getUser().getUser_name());
          u.setUser_ip(userIP);
          getHttpSession().setAttribute("currentUser", u);
          getUserInfoContext().setAttribute("user_ip", userIP);
          OnlineUsers.getInstance().addUser(u);
        }

        return;
      } else {
        if (other != null && (other instanceof WebAuthenticationDetails))
          userList.put(((WebAuthenticationDetails) other).getSessionId(), ase.getAuthentication());
        return;
      }
    }
    if (event instanceof HttpSessionDestroyedEvent) {
      HttpSessionDestroyedEvent hse = (HttpSessionDestroyedEvent) event;
      userList.remove(hse.getSession().getId());
      OnlineUsers.getInstance().removeUser(hse.getSession().getId(), 0);
      return;
    } else {
      return;
    }
  }

  /**
   * 获取供业务系统使用的全局变量
   * 
   * @return 供业务系统使用的全局变量
   * 
   */
  public static synchronized HashMap getContext() {
    return context;
  }

  /**
   * 设置供业务系统使用的全局变量
   * 
   * @param context
   *            供业务系统使用的全局变量
   * 
   */
  public static synchronized void setContext(HashMap context) {
    SessionUtil.context = context;
  }

  /**
   * 获取应用程序上下文
   * 
   * @return 应用程序上下文
   * 
   */
  public static synchronized BeanFactory getApplicationContext() {
    return applicationContext;
  }

  /**
   * 设置应用程序上下文
   * 
   * @param applicationContext
   *            应用程序上下文
   * 
   */
  public static synchronized void setApplicationContext(BeanFactory applicationContext) {
    SessionUtil.applicationContext = applicationContext;
  }

  /**
   * 获取服务端bean
   * 
   * @param beanName
   *            服务端bean
   * @return 服务端bean对象
   * 
   */
  public static Object getServerBean(String beanName) {

    //    BeanFactory app = getApplicationFactory();
    //    if (app != null) {
    //      return app.getBean(beanName);
    //    } else {
    //
    //      return null;
    //    }
    //使用龙图服务 justin修改
    return ServiceFactory.getBean(beanName);

  }

  /** 
   * 获取web目录,Weblogic 默认以war包部署的时候不能用getRealPath 
   * getResource("/")获取的是当前应用所在的类路径，截取到WEB-INF 
   * 之后的路径就是当前应用的web根目录了 
   * @param request 
   * @return 
   */
  public static String getWebPath() {
    HttpSession httpSession = getHttpSession();
    String webPath;
    if (httpSession != null) {
      webPath = httpSession.getServletContext().getRealPath(java.io.File.separator);
      if (webPath.indexOf("WEB-INF") > -1) {
        webPath = webPath.substring(0, webPath.indexOf("WEB-INF") - 1);
      }
      if (webPath == null) {
        webPath = SessionUtil.class.getClassLoader().getResource("/").getPath();
        webPath = webPath.substring(0, webPath.indexOf("WEB-INF") - 1);
      }
      return webPath;
    } else
      return "";
  }

  /**
   * 获取服务端部署的cptfile位置
   * 
   * @return 服务端部署的cptfile位置
   * 
   */
  public static String getWebCptfilePath() {
    String path = SessionUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    if (path.indexOf("WEB-INF") > 0) {
      path = path.substring(1, path.indexOf("WEB-INF") + 15);
    } else {

    }
    return path;

  }

  /**
   * 获取传进来的ttp字符串
   * 
   * @return 参数字符串
   * 
   */
  public static String getServerInfo() {
    HttpSession httpSession = getHttpSession();
    if (httpSession != null) {
      return httpSession.getServletContext().getServerInfo();
    } else
      return "";
  }

  /**
   * 根据传入的用户ID,获取对应的用户所属机构
   * 
   * @deprecated call SessionUtil.getUserInfoContext().getMbid() or
   *             SessionUtil.getUserInfoContext().getBelongOrg
   * @return 用户所属机构
   * @throws Exception
   */

  public static String getUserBeLong() throws Exception {
    String org_id = "";
    try {
      org_id = (String) SessionUtil.getUserInfoContext().getAttribute("belong_org");
    } catch (Exception e) {
      throw e;
    }
    return org_id;
  }
  
  /**
   * 判断当前子系统是否采用分布式库结构
   * 
   * @deprecated call SessionUtil.getParaMap().get("is_destore");
   * 
   * @return true-是，false-否
   */
  public static boolean isDstore() {
    try {
      return Integer.parseInt((String) SessionUtil.getUserInfoContext().getAttribute("is_dstore")) == 1;
    } catch (Exception e) {
      //
    }
    return true;
  }

  /**
   * 判断当前系统是否采用龙图平台
   * 
   * @return true-是，false-否
   */
  public static boolean is4LT() {
    try {
      return SessionUtil.getUserInfoContext().getSwitch().equals("1");
    } catch (Exception e) {
      //
    }
    return false;
  }

  /**
   * 客户端获取区划
   * 
   * @return String用户区划
   * @author 黄节 2007年6月20日
   */
  public static String getRgCode() {
    // 当从用户中获取不到rg_code（比如用户未登陆）时，获取默认当前区划，至少先保证单区划OK
    // 此处不能使用getParaMap方法获取
    if (getUserInfoContext().getContext().get("rg_code") == null) {
      if (!hasLoadDefaultRgCode && defaultRgCode == null) {// 没有
        // 则去数据库里取，并且只取一次
        hasLoadDefaultRgCode = true;
        defaultRgCode = getRGbyPubliContextXML(getAllDataSources());
      }
      return defaultRgCode;
    }
    return (String) getUserInfoContext().getContext().get("rg_code");
  }

  /***
   * 
   * <strong>Description:</strong><br>
   * 返回所有的数据源<br>
   * @author 
   * @date 
   *
   */
  public static Map getAllDataSources() {
    Map dataSources = null;
    MultiDataSource mt = (MultiDataSource) SessionUtil.getServerBean("multiDataSource");
    dataSources = mt.getDataSources();
    return dataSources;
  }

  /**
   * 
   * <strong>Description:</strong><br>
   * 从所有的数据源中返回第一个区划<br>
   * @author
   * @date 
   *
   */
  private static String getRGbyPubliContextXML(Map AllDataSources) {
    String rg_code = StringUtil.EMPTY;
    if (AllDataSources != null && !AllDataSources.isEmpty() && AllDataSources.size() > 0) {
      Set keySet = AllDataSources.keySet();
      Iterator it = keySet.iterator();
      while (it.hasNext()) {
        String rgAndyear = it.next().toString();
        rg_code = String.valueOf(rgAndyear.split("#")[0]);
        if (!(rg_code == null) || !"".equals(rg_code))
          break;

      }
    }
    return rg_code;

  }

  /**
   * 从xml获取所有区划
   * @author 
   * @return key="420000" value="420000#湖北省#420000#1"
   */
  public static Map getAllRgCodes() {
    Map rgCodes = null;
    MultiDataSource mt = (MultiDataSource) SessionUtil.getServerBean("multiDataSource");
    rgCodes = mt.getRgCodes();
    return rgCodes;
  }

  /**
   * 查询public-context。xml文件，返回所有区划列表
   * @author yangzs 
   * @param AllDataSources
   * @return
   */

  public static List getAllRGbyPubliContextXML() {
    Map rgCodes = getAllRgCodes();
    List rgList = new ArrayList();
    if (rgCodes != null && !rgCodes.isEmpty() && rgCodes.size() > 0) {
      Set keySet = rgCodes.keySet();
      Iterator it = keySet.iterator();
      while (it.hasNext()) {
        String key = it.next().toString();
        String rgName = rgCodes.get(key).toString();
        if (rgName != null) {
          SysRegion sysRegion = new gov.df.fap.bean.sysdb.SysRegion(key, rgName);
          sysRegion.setIs_top("");
          rgList.add(sysRegion);
        }

      }
    }
    return rgList;

  }

  /**
   * 查询public-context。xml文件，返回所有区划列表
   * @author yangzs 
   * @param AllDataSources
   * @return
   */

  public static List getAllSetYearbyPubliContextXML() {
    Map dataSources = getAllDataSources();
    List syList = new ArrayList();
    boolean ishas = false;
    if (dataSources != null && !dataSources.isEmpty() && dataSources.size() > 0) {
      Set keySet = dataSources.keySet();
      Iterator it = keySet.iterator();
      while (it.hasNext()) {
        String year = it.next().toString();
        String[] yearearArray = year.split("#");
        if (yearearArray != null && yearearArray.length > 1) {
          int size = syList.size();
          for (int i = 0; i < size; i++) {
            SysYear y = (SysYear) syList.get(i);
            if (y.getSET_YEAR().intValue() == Integer.parseInt(yearearArray[1])) {
              ishas = true;
              break;
            }
          }
          if (!ishas) {
            SysYear sysYear = new SysYear();
            sysYear.setSET_YEAR(new Integer(yearearArray[1]));
            sysYear.setYEAR_NAME(yearearArray[1] + "年");
            syList.add(sysYear);
          }
        }
        ishas = false;
      }
    }
    return syList;

  }

  /**
   * 客户端设置用户区划
   * 
   * @param rg_code
   *            要设置的区划
   * @author 黄节 2007年6月20日
   */
  public static void setRgcode(String rg_code) {
    getHttpSession().setAttribute("rg_code", rg_code);
  }

  /**
   * 获取在线用户的列表
   * 
   * @return 在线用户的列表
   * 
   */
  public static Hashtable getUserList() {
    return userList;
  }

  private static BeanFactory getApplicationFactory() {
    // 取得服务端的SPRING上下文
    if (applicationContext == null) {
      applicationContext = new ClassPathXmlApplicationContext(new String[] { "application-context.xml" });
    }
    return applicationContext;
  }

  private static BeanFactory getOffApplicationFactory() {

    if (offapplicationContext == null) {

      offapplicationContext = new ClassPathXmlApplicationContext(new String[] { "offapplication-context.xml" });
    }

    return offapplicationContext;

  }

  /**
   * 获取用户登录年度,如果用户上下文中没有,则取服务初始化的年度
   * 
   * @return
   * @author 
   */
  public static String getLoginYear() {
    if (null == getUserInfoContext().getAttribute("set_year")) {
      return defaultYear;
    } else {// 用户已经登录,从上下文中取出用户登录年度
      return (String) getUserInfoContext().getAttribute("set_year");
    }
  }

  public static String getDefaultYear() {
    return defaultYear;
  }

  public static void setDefaultYear(String defaultYear) {
    SessionUtil.defaultYear = defaultYear;
  }

  /**
   * 大集中模式改造;SQL查询条件加入年度区划公共方法<BR>
   * conFlag为数据库表别名<BR>
   * 在需要加入年度区划的sql条件中，如果表有别名则传参<BR>
   * 无别名的传null即可<BR>
   * 
   * @param conFlag
   */
  public static String setYearAndRgCodeForCon(String conFlag) {
    StringBuffer bf = new StringBuffer();
    bf.append(" and ");
    if (conFlag != null) {
      bf.append(conFlag.trim());
      bf.append(".");
    }

    bf.append("rg_code=");
    bf.append("'");
    bf.append(SessionUtil.getRgCode());
    bf.append("'");
    bf.append(" and ");

    if (conFlag != null) {
      bf.append(conFlag.trim());
      bf.append(".");
    }
    bf.append("set_year=");
    bf.append(getLoginYear());
    return bf.toString();

  }
}
