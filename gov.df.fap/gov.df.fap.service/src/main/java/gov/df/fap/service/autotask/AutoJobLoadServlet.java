package gov.df.fap.service.autotask;

import gov.df.fap.api.systemmanager.autotask.ibs.ISysAutoTask;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * <p>
 * Title:自动任务启动时启动所有任务
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: 
 * </p>
 * <p>
 * CreateData 2017-5-9
 * </p>
 * 
 * @author justin
 * @version 1.0
 * @since JDK1.6.2
 * @since quartz-1.8.3
 */
public class AutoJobLoadServlet extends HttpServlet {

  private static final long serialVersionUID = 1050649223822921077L;

  public ISysAutoTask autoTaskBO = null;

  public void init() throws ServletException {
    if (autoTaskBO == null) {
      autoTaskBO = (ISysAutoTask) SessionUtil.getServerBean("sysAutoTaskBO");
    }
    // 加载所有任务
    new Thread() {
      public void run() {
        try {
          try {
            sleep(10 * 1000);
          } catch (Exception ex) {

          }
          autoTaskBO.loadAllJob();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start();
  }
}
