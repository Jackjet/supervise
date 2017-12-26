import java.io.InputStream;
import java.util.Collection;

import javax.servlet.ServletContext;

import com.longtu.businessframework.common.sql.ParseXmlFactory;
import com.longtu.businessframework.common.sql.exception.ClassNotFontException;
import com.longtu.businessframework.common.sql.listener.ISqlLoaderListener;

public class WebContextLoader {
  public static final String LISTENER_CLASS_PARAM = "SQLListenerClass";

  public static final String CONFIG_LOCATION_PATH = "SQLConfigPath";

  private ISqlLoaderListener listener = null;

  private String filepath;

  public void initCommonSqlContext(ServletContext servletContext) {
    initInfo(servletContext);
    ParseXmlFactory.setListener(this.listener);
    ParseXmlFactory.parseXml(super.getClass().getResourceAsStream("/common-contextsql.xml"));
    if ((this.filepath != null) && (this.filepath.trim().length() > 0))
      ParseXmlFactory.parseXml(servletContext.getResourceAsStream(this.filepath));
  }

  public void initSqlContext(ServletContext servletContext) {
    ILoadAppid ils = LoadAppidFactory.newInstance();
    Collection<String> appids = ils.getAppid();
    for (String app : appids)
      try {
        InputStream io = super.getClass().getResourceAsStream("/" + app + "-contextsql.xml");
        if (io != null) {
          ParseXmlFactory.parseXml(io);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  private void initInfo(ServletContext servletContext) {
    String listenerClass = servletContext.getInitParameter("SQLListenerClass");
    this.filepath = servletContext.getInitParameter("SQLConfigPath");
    if ((listenerClass == null) || (listenerClass.trim().length() == 0))
      return;
    try {
      this.listener = ((ISqlLoaderListener) Class.forName(listenerClass).newInstance());
    } catch (Exception e) {
      new ClassNotFontException(e);
    }
  }
}