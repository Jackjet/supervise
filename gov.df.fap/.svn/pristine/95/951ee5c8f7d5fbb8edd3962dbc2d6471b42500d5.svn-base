/**
 * Copyright 2013 by  Yonyou Software Co., Ltd.
 * All rights reserved.
 *
 * 版权所有：北京用友政务软件有限公司
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * 侵权者将受到法律追究。
 */
package gov.df.fap.service.autotask;

import gov.df.fap.service.util.sessionmanager.SessionUtil;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.core.JobRunShell;
import org.quartz.core.JobRunShellFactory;
import org.quartz.core.SchedulingContext;

/**
 * <p>DESCRIPTION: 扩展 quartz任务执行shell，在执行前设置rgcode setyear
 * <p>DESCRIPTION: 
 * <p>CALLED BY:
 * <p>CREATE DATE: Feb 4, 2017
 * <p>HISTORY: 1.0
 * 
 * @version 1.0
 * @author peak.edge
 * @since jdk 1.6.0_31
 * @see
 */
public class ExtendsJobRunShell extends JobRunShell {

  public ExtendsJobRunShell(JobRunShellFactory jobRunShellFactory, Scheduler scheduler, SchedulingContext schdCtxt) {
    super(jobRunShellFactory, scheduler, schdCtxt);
  }

  protected void begin() throws SchedulerException {
    JobDetail jobDetail = this.jec.getJobDetail();
    String rgCode = (null == jobDetail.getJobDataMap().get("rg_code") ? "" : jobDetail.getJobDataMap().get("rg_code")
      .toString());
    String setYear = (null == jobDetail.getJobDataMap().get("set_year") ? "" : jobDetail.getJobDataMap()
      .get("set_year").toString());
    SessionUtil.getUserInfoContext().setRgCode(rgCode);
    SessionUtil.getUserInfoContext().setSetYear(setYear);
    super.begin();
  }
}
