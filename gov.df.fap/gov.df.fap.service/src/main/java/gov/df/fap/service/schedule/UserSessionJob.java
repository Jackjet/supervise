package gov.df.fap.service.schedule;

import gov.df.fap.api.framework.IPreLogin;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * <p>
 * Title:调用LoginDAO中的removeInvalidateSession方法，清除sys_session表所有满足条件{login_date>
 * 当前时间-5天}的记录
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
@Component("gov.df.fap.service.schedule.UserSessionJob")
public class UserSessionJob implements Job {
  @Autowired
  IPreLogin preLoginBO;

  /**
   * 调用LoginDAO中的removeInvalidateSession方法，清除sys_session表所有满足条件{login_date>
   * 当前时间-5天}的记录
   * 
   * @param arg0
   *            job的内容信息
   * @throws JobExecutionException
   *             抛出Job运行错误
   * 
   */
  public void execute(JobExecutionContext arg0) throws JobExecutionException {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this); // 解决在JobListener中使用Autowired后bean为null的情况
    preLoginBO.removeInvalidateSession();

  }
}
