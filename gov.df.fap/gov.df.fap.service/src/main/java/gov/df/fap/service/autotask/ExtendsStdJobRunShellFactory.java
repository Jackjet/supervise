/**
 * Copyright 2013 by  Yonyou Software Co., Ltd.
 * All rights reserved.
 *
 * 版权所有：北京用友政务软件有限公司
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分，
 * 侵权者将受到法律追究。
 */
package gov.df.fap.service.autotask;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.core.JobRunShell;
import org.quartz.core.JobRunShellFactory;
import org.quartz.core.SchedulingContext;

/**
 * <p>
 * DESCRIPTION:
 * <p>
 * DESCRIPTION:
 * <p>
 * CALLED BY:
 * <p>
 * CREATE DATE: Feb 4, 2017
 * <p>
 * HISTORY: 1.0
 * 
 * @version 1.0
 * @author peak.edge
 * @since jdk 1.6.0_31
 * @see
 */
public class ExtendsStdJobRunShellFactory implements JobRunShellFactory {

  /*
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   * 
   * Data members.
   * 
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */
  private Scheduler scheduler;

  private SchedulingContext schedCtxt;

  /*
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   * 
   * Interface.
   * 
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */
  /**
   * <p>
   * Initialize the factory, providing a handle to the <code>Scheduler</code>
   * that should be made available within the <code>JobRunShell</code> and the
   * <code>JobExecutionCOntext</code> s within it, and a handle to the
   * <code>SchedulingContext</code> that the shell will use in its own
   * operations with the <code>JobStore</code>.
   * </p>
   */
  public void initialize(Scheduler scheduler, SchedulingContext schedCtxt) {
    this.scheduler = scheduler;
    this.schedCtxt = schedCtxt;
  }

  /**
   * <p>
   * Called by the <class>{@link org.quartz.core.QuartzSchedulerThread}
   * </code> to obtain instances of <code>
   * {@link org.quartz.core.JobRunShell}</code>.
   * </p>
   */
  public JobRunShell borrowJobRunShell() throws SchedulerException {
    return new ExtendsJobRunShell(this, this.scheduler, schedCtxt);
  }

  /**
   * <p>
   * Called by the <class>{@link org.quartz.core.QuartzSchedulerThread}
   * </code> to return instances of <code>
   * {@link org.quartz.core.JobRunShell}</code>.
   * </p>
   */
  public void returnJobRunShell(JobRunShell jobRunShell) {
    jobRunShell.passivate();
  }
}
