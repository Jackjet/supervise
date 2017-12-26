package gov.df.fap.util.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 使用财政部平台bean获取方式
 * @author justin
 *
 */
//public class ServiceFactory extends com.longtu.framework.util.ServiceFactory {
//
//}

public class ServiceFactory implements ApplicationContextAware {

  protected static ApplicationContext ctx = null;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ServiceFactory.ctx = applicationContext;
  }

  public static ApplicationContext getCtx() {
    return ctx;
  }

  public static Object getBean(String beanid) {
    return ctx.getBean(beanid);
  }
}
