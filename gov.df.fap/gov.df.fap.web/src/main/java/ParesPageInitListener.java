import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;

import com.longtu.framework.annotation.parse.ParseComponentAnnotation;
import com.longtu.framework.annotation.parse.ParsePageAnnotation;
import com.longtu.framework.component.ComponentService;
import com.longtu.framework.server.PageService;
import com.longtu.framework.springexp.ServerDetector;

public class ParesPageInitListener extends FaspDefaultReaderEventListener {
  public void componentRegistered(ComponentDefinition componentDefinition) {
    BeanDefinition[] bd = componentDefinition.getBeanDefinitions();
    if ((bd != null) && (bd.length > 0)) {
      String cln = bd[0].getBeanClassName();
      try {
        if (cln == null) {
          return;
        }

        Class clz = Class.forName(cln);
        if (ComponentService.class.isAssignableFrom(clz)) {
          ParseComponentAnnotation.getInstance().parseComponent(clz, componentDefinition.getName());
        }
        if (PageService.class.isAssignableFrom(clz)) {
          ParsePageAnnotation.getInstance().parseConsole(clz);
        }
        if ((Servlet.class.isAssignableFrom(clz)) && (!(ServerDetector.isTongWeb()))) {
          ParseServletAnnotation.getInstance().parseServer(clz);
        }

        if ((!(Filter.class.isAssignableFrom(clz))) || (ServerDetector.isTongWeb()))
          return;
        ParseServletAnnotation.getInstance().parseFilter(clz);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void afertReflash() {
    super.afertReflash();
    if (!(ServerDetector.isTongWeb()))
      ParseServletAnnotation.getInstance().finish();
  }
}