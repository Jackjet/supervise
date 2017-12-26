package gov.df.fap.service.schedule;

import gov.df.fap.api.systemmanager.log.ibs.ISysLog;
import gov.df.fap.service.util.sessionmanager.Session;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * <p>
 * Title:调用ISysLog接口中的removeInvalidateLog方法，清除清除过期会话信息
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
 * CreateData 2017-5-09
 * </p>
 * 
 * @author justin
 * @version 1.0
 */
@Component("gov.df.fap.service.schedule.UserLogJob")
public class UserLogJob implements Job {
  @Autowired
  ISysLog sysLogBO;

  /**
       * 调用ISysLog接口中的removeInvalidateLog方法，清除清除过期会话信息
       * 
       * @param arg0
       *                job的内容信息
       * @throws JobExecutionException
       *                 抛出Job运行错误
       */
  public void execute(JobExecutionContext arg0) throws JobExecutionException {

    // ISysLog sysLogBO = (ISysLog) SessionUtil.getServerBean("sys.sysLogService");
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this); // 解决在Job中使用Autowired后bean为null的情况
    Map map = arg0.getJobDetail().getJobDataMap();
    Session session = (Session) SessionUtil.getHttpSession().getAttribute("usercontext");
    session.getUserInfoContext().setRgCode((String) map.get("rg_code"));
    session.getUserInfoContext().setSetYear((String) map.get("set_year"));
    sysLogBO.removeInvalidateLog();
  }

}
