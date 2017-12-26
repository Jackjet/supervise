package gov.df.fap.service.autotask;

import gov.df.fap.api.systemmanager.autotask.ibs.ISysAutoTask;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * <p>
 * Title:任务监听类
 * </p>
 * <p>
 * Description:任务监听类：监听自动任务执行完成事件、
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 
 * </p>
 * <p>
 * CreateData 2017-5-9
 * </p>
 * 
 * @author 
 * @version 1.0
 * @since JDK1.6.2
 * @since quartz-1.8.3
 */

public class AutoJobJobListener implements JobListener {

  @Autowired
  public ISysAutoTask autJobBo = null;

  String name = null;

  String groupName = null;

  /**
   * @return groupName
   */
  public String getGroupName() {
    return groupName;
  }

  /**
   * @param groupName
   *            要设置的 groupName
   */
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *            要设置的 name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 重载“自动任务执行完成事件”
   * 
   * @param context
   *            JobExecutionContext 上下文环境
   * @param ex
   *            JobExecutionException 自动任务异常抛出
   * 
   */
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException ex) {
    // ISysAutoTask autJobBo = (ISysAutoTask)SessionUtil.getServerBean("gov.df.fap.service.autotask.SysAutoTaskBO");
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this); // 解决在JobListener中使用Autowired后bean为null的情况
    // 获取job的名字
    int job_id = Integer.parseInt(context.getJobDetail().getName());
    String token_id = context.get("TOKEN_ID").toString();
    try {
      if (ex != null) {
        autJobBo.wasExed(job_id, token_id, false);
      } else {
        autJobBo.wasExed(job_id, token_id, true);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 实现“执行被拒绝的响应事件”
   * 
   * 不做任何操作
   * 
   * @param jobexecutioncontext
   *            JobExecutionContext 上下文环境
   * 
   */
  public void jobExecutionVetoed(JobExecutionContext jobexecutioncontext) {
  }

  /**
   * 实现“即将执行的响应事件”
   * 
   * @param jobexecutioncontext
   *            JobExecutionContext 上下文环境
   * 
   */
  public void jobToBeExecuted(JobExecutionContext jobexecutioncontext) {
    // ISysAutoTask autJobBo = (ISysAutoTask)SessionUtil.getServerBean("gov.df.fap.service.autotask.SysAutoTaskBO");
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this); // 解决在JobListener中使用Autowired后bean为null的情况
    // 获取job的名字
    long taskId = Long.parseLong(jobexecutioncontext.getJobDetail().getName());

    try {
      String token_id = autJobBo.toBeExe(taskId);
      // 使其控制在集群环境下，只有一个生产机在执行
      jobexecutioncontext.put("TOKEN_ID", token_id);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}