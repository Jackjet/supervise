import gov.df.fap.util.Properties.WebContextParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;

public class DFInitClasspathXmlApplicationContext extends AbstractRefreshableWebApplicationContext {
  private static DFInitClasspathXmlApplicationContext icxa = null;

  public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";

  public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";

  private final Object startupShutdownMonitor = new Object();

  public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

  private static boolean init = false;

  private static boolean isCommon = false;

  private DataSource ds = null;

  boolean isOa = false;

  boolean isIndi = false;

  boolean oaDrive = false;

  private final FaspReaderEventListener listener = new FaspReaderEventListener();

  public static String NEST_VERSION = null;

  public static String FASP_VERSION = null;

  public static String NESTDB_VERSION = null;

  private Set<String> pathxml = new HashSet();

  protected DefaultListableBeanFactory createBeanFactory() {
    return new FaspDefaultListableBeanFactory(getInternalParentBeanFactory());
  }

  public FaspReaderEventListener getListener() {
    return this.listener;
  }

  protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws IOException {
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

    beanDefinitionReader.setDocumentReaderClass(FaspBeanDefinitionDocumentReader.class);
    beanDefinitionReader.setResourceLoader(this);
    beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

    initBeanDefinitionReader(beanDefinitionReader);
    loadBeanDefinitions(beanDefinitionReader);
  }

  public void refresh() throws BeansException, IllegalStateException {
    if (init) {
      return;
    }
    icxa = this;
    init = false;
    //加载所有bean
    super.refresh();
    //获取数据源
    this.ds = ((DataSource) getBean("dfdatasource"));
    initContext();
    init = true;
    super.refresh();
    System.out.println("加载完毕");
    this.listener.afertReflash();
  }

  public void refresh(boolean boo) throws BeansException, IllegalStateException {
    super.refresh();
  }

  public void addXml(String path) {
    this.pathxml.add(path);
  }

  private void initContext() {
    String starttype = getServletContext().getInitParameter("startup");
    WebContextParam.setStartup(starttype);
    String develop = getServletContext().getInitParameter("develop");
    if ("true".equals(develop)) {
      WebContextParam.setDevelop(true);
    }
  }

  public DataSource getDataSource() {
    return this.ds;
  }

  protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
    this.listener.clear();
    this.listener.addListener(new ParesPageInitListener());
    this.listener.addListener(new HearbeatStartListener());
    this.listener.addListener(new AfterReflashEeventInit());
    ServletContext sc = getServletContext();
    String readerEventListener = sc.getInitParameter("readerEventListener");
    if ((readerEventListener != null) && (readerEventListener.trim().length() > 0)) {
      String[] str = readerEventListener.split(";");
      for (String s : str) {
        try {
          Class cls = Class.forName(s);
          this.listener.addListener((IFaspReaderEventListener) cls.newInstance());
        } catch (Exception localException) {
          this.logger.error("spring监听器" + s
            + "未找到或者接口对象不是org.springframework.beans.factory.parsing.ReaderEventListener！");
        }
      }
    }
    beanDefinitionReader.setEventListener(this.listener);
  }

  protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
    String[] configLocations = getConfigLocations();
    if (configLocations != null)
      for (int i = 0; i < configLocations.length; ++i)
        reader.loadBeanDefinitions(configLocations[i]);
  }

  private String[] getSuperConfigLocations() {
    String[] strs = super.getConfigLocations();
    if ((strs == null) || (strs.length == 0)) {
      strs = new String[] { "classpath:yy-datasource.xml" };
    }
    return strs;
  }

  public String[] getConfigLocations() {
    if (!(init)) {
      return getSuperConfigLocations();
    }
    return getDefConfigLocations();
  }

  public String[] getDefConfigLocations() {
    String str = getServletContext().getInitParameter("defcontextConfigLocation");
    ArrayList list = new ArrayList();
    String[] sup = getSuperConfigLocations();
    for (String s : sup) {
      list.add(s);
    }
    //list.add("classpath:common-context.xml");
    //list.add("classpath:common-appupgrade.xml");
    if ((str != null) && (str.trim().length() > 0)) {
      String[] files = (String[]) null;
      files = str.split(",");
      for (String s : files) {
        list.add(s);
      }
    }

    Collection<CommonDTO> myappdto = LoadAppidFactory.newInstance().getAppDTO();
    ServletContext sc = getServletContext();
    String cp = sc.getContextPath();
    Object myapp = new HashSet();
    String key;
    Set<String> apps = LoadAppidFactory.newInstance().getAllApp().keySet();
    String[] s = hashContext(list);
    return s;
  }

  private ArrayList<String> faspModules() {
    String[] str = (String[]) null;
    if (LoadAppidFactory.newInstance().isAppupgrade())
      str = FaspModule.DEF_MODULES;
    else {
      str = FaspModule.FASP_MODULES;
    }
    ArrayList list = new ArrayList();
    for (String s : str) {
      list.add("classpath:fasp2-" + s.toLowerCase() + ".xml");

      if (isCommon)
        list.add("classpath:fasp2-" + s.toLowerCase() + "-server.xml");
      else {
        list.add("classpath:fasp2-" + s.toLowerCase() + "-client.xml");
      }
    }
    return list;
  }

  private String[] hashContext(List<String> contexts) {
    ArrayList ctx = new ArrayList();
    for (String c : contexts) {
      try {
        if (c.indexOf("classpath:") == 0) {
          if (getClassLoader().getResourceAsStream(c.replaceFirst("classpath:", "")) != null) {
            ctx.add(c);
            this.logger.debug("找到配置文件：" + c);
          }
          this.logger.warn("配置文件" + c + "未找到！");
        }

        if (getServletContext().getResourceAsStream(c) != null) {
          ctx.add(c);
          this.logger.debug("找到配置文件：" + c);
        }
        this.logger.warn("配置文件" + c + "未找到！");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return ((String[]) ctx.toArray(new String[ctx.size()]));
  }

  public static DFInitClasspathXmlApplicationContext getThis() {
    return icxa;
  }

  public static void setIscommon(boolean b) {
    isCommon = b;
  }
}
