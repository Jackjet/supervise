import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.longtu.framework.annotation.FilterInfo;
import com.longtu.framework.annotation.FilterInitParam;
import com.longtu.framework.annotation.ServletInfo;
import com.longtu.framework.annotation.ServletInitParam;

public class ParseServletAnnotation {
  private Map<Class, ServletInfo> serverMap;

  private Map<Class, FilterInfo> filterMap;

  private ServletContext sc;

  private Set<String> servletSet;

  private Set<String> filterSet;

  public static ParseServletAnnotation getInstance() {
    return innerClass.annotation;
  }

  private ParseServletAnnotation() {
    this.serverMap = new HashMap();
    this.filterMap = new HashMap();
    this.servletSet = new HashSet();
    this.filterSet = new HashSet();
    this.sc = DFInitClasspathXmlApplicationContext.getThis().getServletContext();
    try {
      Method m = this.sc.getClass().getDeclaredMethod("addMimeMapping", new Class[] { String.class, String.class });
      m.invoke(this.sc, new Object[] { "xls", "application/msexcel" });
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document root = db.parse(this.sc.getResourceAsStream("WEB-INF/web.xml"));
      NodeList portList = root.getElementsByTagName("servlet-name");
      int i = 0;
      for (int len = portList.getLength(); i < len; ++i) {
        this.servletSet.add(portList.item(i).getTextContent().trim());
      }
      portList = root.getElementsByTagName("filter-name");
      i = 0;
      for (int len = portList.getLength(); i < len; ++i)
        this.filterSet.add(portList.item(i).getTextContent().trim());
    } catch (Exception localException) {
      System.out.println("自启的Servlet filter 出现问题");
    }
  }

  private Method getAddFilter() throws Exception {
    return this.sc.getClass().getMethod("addFilter", new Class[] { String.class, Class.class });
  }

  private Method getFilterRegistration() throws Exception {
    return this.sc.getClass().getMethod("getFilterRegistration", new Class[] { String.class });
  }

  private Method getRegisterFilter() throws Exception {
    return this.sc.getClass().getDeclaredMethod("registerFilter",
      new Class[] { String.class, String.class, java.lang.String.class, java.lang.String.class, Map.class });
  }

  private Method getAddServlet() throws Exception {
    return this.sc.getClass().getMethod("addServlet", new Class[] { String.class, Class.class });
  }

  private Method getServletRegistration() throws Exception {
    return this.sc.getClass().getMethod("getServletRegistration", new Class[] { String.class });
  }

  private Method getRegisterServlet() throws Exception {
    return this.sc.getClass().getDeclaredMethod("registerServlet",
      new Class[] { String.class, String.class, String.class, Map.class });
  }

  public void servlet3Register() throws Exception {
    Method addServlet = getAddServlet();
    Method registration = getServletRegistration();
    Set mapSet = this.serverMap.entrySet();
    Iterator ite = mapSet.iterator();
    while (ite.hasNext()) {
      Map.Entry entry = (Map.Entry) ite.next();
      ServletInfo sInfo = (ServletInfo) entry.getValue();
      ServletInitParam[] initParams = sInfo.initParam();
      addServlet.invoke(this.sc, new Object[] { sInfo.name(), entry.getKey() });
      ServletRegistration sr = (ServletRegistration) registration.invoke(this.sc, new Object[] { sInfo.name() });
      sr.addMapping(sInfo.urlPatterns());
      if ((initParams != null) && (initParams.length > 0))
        for (ServletInitParam sInitParam : initParams)
          sr.setInitParameter(sInitParam.name(), sInitParam.value());
    }
  }

  public void servletRegister() throws Exception {
    Method registerServlet = getRegisterServlet();
    registerServlet.setAccessible(true);
    Set mapSet = this.serverMap.entrySet();
    Iterator ite = mapSet.iterator();
    while (ite.hasNext()) {
      Map.Entry entry = (Map.Entry) ite.next();
      ServletInfo sInfo = (ServletInfo) entry.getValue();
      String[] urls = sInfo.urlPatterns();
      ServletInitParam[] initParams = sInfo.initParam();
      Map initMap = new HashMap();
      if ((initParams != null) && (initParams.length > 0)) {
        for (ServletInitParam sInitParam : initParams) {
          initMap.put(sInitParam.name(), sInitParam.value());
        }
      }
      for (String url : urls)
        registerServlet
          .invoke(this.sc, new Object[] { sInfo.name(), url, ((Class) entry.getKey()).getName(), initMap });
    }
  }

  public void filter3Register() throws Exception {
    Method addFilter = getAddFilter();
    Method registration = getFilterRegistration();
    Set mapSet = this.filterMap.entrySet();
    Iterator ite = mapSet.iterator();
    while (ite.hasNext()) {
      Map.Entry entry = (Map.Entry) ite.next();
      FilterInfo sInfo = (FilterInfo) entry.getValue();
      FilterInitParam[] initParams = sInfo.initParam();
      addFilter.invoke(this.sc, new Object[] { sInfo.name(), entry.getKey() });
      FilterRegistration sr = (FilterRegistration) registration.invoke(this.sc, new Object[] { sInfo.name() });

      sr.addMappingForUrlPatterns(null, false, sInfo.urlPatterns());
      if ((initParams != null) && (initParams.length > 0))
        for (FilterInitParam sInitParam : initParams)
          sr.setInitParameter(sInitParam.name(), sInitParam.value());
    }
  }

  public void filterRegister() throws Exception {
    Method registerFilter = getRegisterFilter();
    registerFilter.setAccessible(true);
    Set mapSet = this.filterMap.entrySet();
    Iterator ite = mapSet.iterator();
    while (ite.hasNext()) {
      Map.Entry entry = (Map.Entry) ite.next();
      FilterInfo sInfo = (FilterInfo) entry.getValue();
      String[] urls = sInfo.urlPatterns();
      FilterInitParam[] initParams = sInfo.initParam();
      Map initMap = new HashMap();
      if ((initParams != null) && (initParams.length > 0)) {
        for (FilterInitParam sInitParam : initParams) {
          initMap.put(sInitParam.name(), sInitParam.value());
        }
      }
      registerFilter.invoke(this.sc,
        new Object[] { sInfo.name(), ((Class) entry.getKey()).getName(), urls, 0, initMap });
    }
  }

  public void finish() {
    if (!(this.serverMap.isEmpty())) {
      try {
        servlet3Register();
      } catch (Exception localException1) {
        try {
          servletRegister();
        } catch (Exception localException2) {
          System.out.println("自定义Servlet注解失败！");
        }
      }
    }
    if (this.filterMap.isEmpty())
      return;
    try {
      filter3Register();
    } catch (Exception localException3) {
      try {
        filterRegister();
      } catch (Exception e1) {
        System.out.println("自定义Filter注解失败！");
        e1.printStackTrace();
      }
    }
  }

  public void parseServer(Class clz) {
    if (clz.isAnnotationPresent(ServletInfo.class)) {
      ServletInfo sInfo = (ServletInfo) clz.getAnnotation(ServletInfo.class);
      if (!(this.servletSet.contains(sInfo.name())))
        this.serverMap.put(clz, sInfo);
    }
  }

  public void parseFilter(Class clz) {
    if (clz.isAnnotationPresent(FilterInfo.class)) {
      FilterInfo fInfo = (FilterInfo) clz.getAnnotation(FilterInfo.class);
      if (!(this.filterSet.contains(fInfo.name())))
        this.filterMap.put(clz, fInfo);
    }
  }

  private static class innerClass {
    private static ParseServletAnnotation annotation = new ParseServletAnnotation();
  }
}