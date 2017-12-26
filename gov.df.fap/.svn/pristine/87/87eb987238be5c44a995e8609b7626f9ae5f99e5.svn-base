package gov.df.fap.service.login.filter;

import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.mof.fasp2.ca.user.dto.UserDTO;
import gov.mof.fasp2.sec.syncuserinfo.SecureUtil;
import gov.mof.fasp2.sec.syncuserinfo.UserInfo;
import gov.mof.fasp2.sec.syncuserinfo.filter.BusinessDomainProcess;
import gov.mof.fasp2.sec.syncuserinfo.filter.CommonDomainProcess;
import gov.mof.fasp2.sec.syncuserinfo.filter.DomainProcess;
import gov.mof.fasp2.sec.syncuserinfo.filter.FilterStatic;
import gov.mof.fasp2.sec.syncuserinfo.manager.IUserSyncManager;
import gov.mof.fasp2.sec.syncuserinfo.manager.LogThread;
import gov.mof.fasp2.sec.syncuserinfo.manager.UserAliveThread;
import gov.mof.fasp2.sec.syncuserinfo.manager.UserSyncManager;
import gov.mof.fasp2.sec.util.SecUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.longtu.framework.annotation.FilterInfo;
import com.longtu.framework.cache.service.ICacheService;
import com.longtu.framework.exception.AppException;
import com.longtu.framework.exception.AppRuntimeException;
import com.longtu.framework.exception.SendException;
import com.longtu.framework.springexp.LoadAppidFactory;
import com.longtu.framework.util.ServiceFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
@FilterInfo(name = "UserSyncFilter", urlPatterns = { "*" })
public class UserSyncFilter implements Filter {
  private static final Logger logger = Logger.getLogger(UserSyncFilter.class);

  private static boolean thisIsCommon = LoadAppidFactory.newInstance().isCommon();

  private static final String HEARTBEAT = "/heartbeat";
  //开始前台调用,结束
  public static boolean beginFilter = false;
  private IUserSyncManager iUserSyncManager = null;

  public UserSyncFilter() {
    iUserSyncManager = (IUserSyncManager) ServiceFactory.getBean("fasp.ca.UserSyncManager");
  }

  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
    ServletException {
	  beginFilter = true;
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String url = httpRequest.getServletPath();
    long begin = System.currentTimeMillis();
    try{
    if ("/heartbeat".equals(url)) {
      response.getWriter().write((int) System.currentTimeMillis());
      return;
    }

    if (url.endsWith(".svg")) {
      RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
      requestDispatcher.forward(request, response);
      return;
    }

    if (url.equals("/df/fap/wf/editor-app/partials/stencil-item-template.html")) {
      RequestDispatcher requestDispatcher = request
        .getRequestDispatcher("/df/fap/wf/editor-app/partials/stencil-item-template.html");
      requestDispatcher.forward(request, response);
      return;
    }
    if (url.endsWith("install/webservice.rcp")) {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
        requestDispatcher.forward(request, response);
        return;
    }
    if (url.equals("/df/fap/wf/editor-app/partials/property-item-template.html")) {
      RequestDispatcher requestDispatcher = request
        .getRequestDispatcher("/df/fap/wf/editor-app/partials/property-item-template.html");
      requestDispatcher.forward(request, response);
      return;
    }

    if (url.equals("/df/fap/wf/editor-app/partials/root-stencil-item-template.html")) {
      RequestDispatcher requestDispatcher = request
        .getRequestDispatcher("/df/fap/wf/editor-app/partials/root-stencil-item-template.html");
      requestDispatcher.forward(request, response);
      return;
    }

    if (url.contains("/df/fap/wf/editor-app/configuration/properties")) {
      RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
      requestDispatcher.forward(request, response);
      return;

    }

    if (url.contains("/df/fap/wf/editor-app/popups")) {
      RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
      requestDispatcher.forward(request, response);
      return;

    }
    if (url.contains("/df/fap/wf/wf.html")) {
      RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
      requestDispatcher.forward(request, response);
      return;

    }

    if (url.equals("/df/fap/wf/editor-app/editor.html")) {
      RequestDispatcher requestDispatcher = request.getRequestDispatcher("/df/fap/wf/editor-app/editor.html");
      requestDispatcher.forward(request, response);
      return;
    }
    if (url.equals("/df/fap/wf/modeler.html")) {
      RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
      requestDispatcher.forward(request, response);
      return;
    }

    if (url.equals("/df/portal/login/login.html")) {
      RequestDispatcher requestDispatcher = request.getRequestDispatcher("/df/portal/login/login.html");
      requestDispatcher.forward(request, response);
      return;
    }
    if (url.equals("/df/portal/admin/login/login.html")) {
      RequestDispatcher requestDispatcher = request.getRequestDispatcher("/df/portal/admin/login/login.html");
      requestDispatcher.forward(request, response);
      return;
    }
    if (url.equals("/df/portal/getYearRgcode.do")) {
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    DomainProcess process = null;

    if (httpRequest.getParameter("ajax") != null && !"".equals(httpRequest.getParameter("ajax"))) {
      ((HttpServletResponse) response).setHeader("Cache-Control", "no-cache");
      if (url.equals("/df/login/userLogin.do")) {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/df/login/userLogin.do");
        requestDispatcher.forward(request, response);
        return;
      }
      String tokenid = httpRequest.getParameter("tokenid");
      UserInfo uinfo = null;
      try {
        uinfo = iUserSyncManager.getUser(tokenid);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return;
      }
      UserDTO userdto = uinfo.getUser();
      HashMap user_context = new HashMap();
      user_context.put("user_id", userdto.getGuid());
      user_context.put("user_name", userdto.getName());
      user_context.put("user_code", userdto.getCode());
      user_context.put("rg_code", userdto.getProvince());
      user_context.put("sys_id", "df");
      user_context.put("set_year", String.valueOf(userdto.getYear()));
      user_context.put("menu_id", httpRequest.getParameter("svMenuId"));
      user_context.put("role_id", httpRequest.getParameter("svRoleId"));
      user_context.put("role_code", httpRequest.getParameter("svRoleCode"));
      user_context.put("trans_date", httpRequest.getParameter("svTransDate"));
      user_context.put("fiscal_period", httpRequest.getParameter("svTransDate"));
      user_context.put("rg_name", httpRequest.getParameter("svRgName"));
      if("true".equalsIgnoreCase(httpRequest.getParameter("readonly")))
      {
    	  user_context.put("read", "read");
      }
      
      SessionUtil.getUserInfoContext().setContext(user_context);
    }
   
    Set commonURL = getURLFilterCache().getValues();

    String queryString = httpRequest.getQueryString();

    if (((commonURL.contains(url)) && (SecUtil.isSecUrlByAppid(url, thisIsCommon)))
      || ((queryString != null) && (queryString.indexOf("method=getValidateCode") != -1))) {
      try {
        chain.doFilter(httpRequest, response);
      } catch (AppRuntimeException te) {
        SendException.sendError(te, request, response);
      } catch (Throwable te) {
        SendException.sendError(te, request, response);
      }
      return;
    }
    if (thisIsCommon)
      process = CommonDomainProcess.newInstance();
    else
      process = BusinessDomainProcess.newInstance();
    try {
      process.doProcess(httpRequest, response, chain);
      SecureUtil.removeLocal();
    } catch (AppRuntimeException te) {
      SendException.sendError(te, request, response);
    } catch (AppException te) {
      SendException.sendError(te, request, response);
    } catch (Throwable te) {
      SendException.sendError(te, request, response);
    }
    }finally{
    	System.out.println(url+"耗时"+(System.currentTimeMillis()-begin));
    }
  }

  public void init(FilterConfig filterCfg) throws ServletException {
    LogThread.getInstance().start();
    if (LoadAppidFactory.newInstance().isCommon()) {
      new UserAliveThread().start();

      return;
    }

    String thirdparty = filterCfg.getInitParameter("thirdparty");
    String session = filterCfg.getInitParameter("session");
    String transaction = filterCfg.getInitParameter("transaction");
    String multyear = filterCfg.getInitParameter("multyear");
    FilterStatic.init(thirdparty, session, transaction, multyear);
  }

  public UserSyncManager getUserSyncManager() {
    return ((UserSyncManager) ServiceFactory.getBean("fasp.ca.UserSyncManager"));
  }

  private ICacheService getURLFilterCache() {
    return ((ICacheService) ServiceFactory.getBean("fasp2.sec.urlFilterCache"));
  }
}
