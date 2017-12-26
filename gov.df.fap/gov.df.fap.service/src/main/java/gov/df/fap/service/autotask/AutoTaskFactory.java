package gov.df.fap.service.autotask;

import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;

/**
 * <p>
 * Title:自动任务工程类
 * </p>
 * <p>
 * Description:用来加载、关闭、添加、挂起、恢复、删除任务
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
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
public class AutoTaskFactory {

  private static String JobListenerName = "JobListener";

  /**
       * AutoJobFactory单态对象
       */
  public static AutoTaskFactory taskFactory = null;

  /**
       * 调度器工厂
       */
  public static SchedulerFactory schedulerFactory = null;

  /**
       * 调度器
       */
  public static Scheduler scheduler = null;

  public boolean success = true;

  /**
       * 构造函数
       */
  private AutoTaskFactory() {

    if (schedulerFactory == null) {
      try {
        Properties props = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("quartz.properties");
        props.load(in);
        schedulerFactory = new ExtendsStdSchedulerFactory(props);
        scheduler = schedulerFactory.getScheduler();
        AutoJobScheduleListener scheduleListener = new AutoJobScheduleListener();
        System.out.println("autotask----已经加载完成quartz.properties");
        scheduler.addSchedulerListener(scheduleListener);
        // 添加jobListener
        AutoJobJobListener jobListener = new AutoJobJobListener();
        jobListener.setName(JobListenerName);
        jobListener.setGroupName("JobListenerGroup");
        scheduler.addJobListener(jobListener);
        scheduler.start();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
       * 单态取autotaskFactory对象
       * 
       * @return autotaskFactory对象
       * 
       */
  public static synchronized AutoTaskFactory getInstance() {

    if (taskFactory == null) {
      taskFactory = new AutoTaskFactory();
    }
    return taskFactory;
  }

  /**
       * 增加一个间隔性自动任务
       * 
       * @param job
       *                XMLData 任务数据
       * 
       * @throws ParseException
       *                 如果传进去的时间参数不符合设置的时间格式，抛出此错误
       * @throws SchedulerException
       *                 自动任务容器错误
       */
  public void addSimpleTrigger(XMLData job) throws ParseException, SchedulerException {
    success = true;
    Calendar ca = Calendar.getInstance();
    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 自动任务运行服务实体
    String autotask_bean = (String) job.get("autotask_bean");
    // 自动事务ID
    String autotask_id = (String) job.get("autotask_id");
    // 自动事务编码
    String autotask_code = (String) job.get("autotask_code");
    // 任务组名，取任务code的前三位，即各个子系统的编码
    String job_group = autotask_code.substring(0, 3);
    //区划
    String rg_code = (String) job.get("rg_code");
    //年份 
    String set_year = (String) job.get("set_year");

    // 任务实体参数
    String autotask_param = "";
    if (job.get("autotask_param") == null) {
      autotask_param = "";
    } else {
      autotask_param = (String) job.get("autotask_param");
      if ("null".equalsIgnoreCase(autotask_param)) {
        autotask_param = "";
      }
    }

    // 查找任务类
    Class bean;
    try {

      Object obj = SessionUtil.getServerBean(autotask_bean);
      //      Object obj = Class.forName(autotask_bean);
      if (obj == null) {
        throw new ClassNotFoundException(autotask_bean + "初始化失败");
      }
      bean = obj.getClass();
      //} catch (NoSuchBeanDefinitionException nb) {
      //启动先后顺序，可能spring没有初始化完成 延时一下 60秒
      //随产品启动后，不可能出现spring没有初始化完成的问题，故延时一下 60秒 注掉
      //      try {
      //        Thread.sleep(1000 * 60);
      //      } catch (InterruptedException e) {
      //      }
      //      try {
      //        Object obj = SessionUtil.getServerBean(autotask_bean);
      //
      //        bean = obj.getClass();
      //      } catch (Exception e) {
      //        System.out.println("自动任务加载出错，找不到指定的任务类：“" + autotask_bean + "”");
      //        success = false;
      //        return;
      //      }
    } catch (Exception e1) {
      System.out.println("自动任务加载出错，找不到指定的任务类：“" + autotask_bean + "”");
      success = false;
      return;
    }

    // 初始化作业
    JobDetail jobDetail = new JobDetail(autotask_id, job_group, bean);

    // 初始化参数：key（param）
    if (!"".equals(autotask_param)) {
      jobDetail.getJobDataMap().put("param", autotask_param);
    }
    //add by wl 20130328 把区划和年份放入到jobDetail中
    jobDetail.getJobDataMap().put("rg_code", rg_code);
    jobDetail.getJobDataMap().put("set_year", set_year);

    // 实例化任务监听类
    jobDetail.addJobListener(JobListenerName);

    // 调度规则类
    SimpleTrigger simpleTrigger = new SimpleTrigger(autotask_id, job_group);
    // 开始日期
    String start_date = "";

    if (job.get("start_date") == null) {
      start_date = "";
    } else {
      start_date = (String) job.get("start_date");
      if ("null".equalsIgnoreCase(start_date)) {
        start_date = "";
      }
    }
    //设置作业启动时间 
    if ("".equals(start_date)) {
      simpleTrigger.setStartTime(ca.getTime());
    } else {
      simpleTrigger.setStartTime(dateformat.parse(start_date));
    }

    if (job.get("end_date") != null) {
      // 截至日期
      String end_date = (String) job.get("end_date");
      if (!"null".equals(end_date)) {
        simpleTrigger.setEndTime(dateformat.parse(end_date));
      }
    }
    //设置作业执行次数
    if (job.get("run_times") != null) {
      // 运行次数
      try {
        int run_times = Integer.parseInt((String) job.get("run_times"));
        simpleTrigger.setRepeatCount(run_times);
      } catch (Exception e) {
        System.out.println("任务类：“" + autotask_bean + "”运行次数设置失败！");
        success = false;
        return;
      }
    }
    //设置作业执行间隔
    if (job.get("taskinterval") != null) {
      try {
        int jobinterval = Integer.parseInt((String) job.get("taskinterval"));
        simpleTrigger.setRepeatInterval(jobinterval * 1000);
      } catch (Exception e) {
        System.out.println("任务类：“" + autotask_bean + "”间隔时间设置失败！");
        success = false;
        return;
      }
    }
    // 注册任务
    scheduler.scheduleJob(jobDetail, simpleTrigger);
  }

  /**
       * 增加一个定时自动自动任务
       * 
       * @param job
       *                XMLData 任务数据
       * 
       * @throws ParseException
       *                 如果传进去的时间参数不符合设置的时间格式，抛出此错误
       * @throws SchedulerException
       *                 自动任务容器错误
       */
  public void addCronTrigger(XMLData job) throws ParseException, SchedulerException {
    success = true;
    // 自动事务ID
    String autotask_id = (String) job.get("autotask_id");
    // 自动事务编码
    String autotask_code = (String) job.get("autotask_code");
    // 任务组名，取任务code的前三位，即各个子系统的编码
    String job_group = autotask_code.substring(0, 3);
    //add by wl 20130328 取区划和年份
    String rg_code = (String) job.get("rg_code");
    String set_year = (String) job.get("set_year");
    // 第几月
    int month_of_year;
    // 每星期几
    int day_of_week;
    // 每月第几日
    int day_of_month;
    // 小时
    int hour_of_day;
    // 分钟
    int minute_of_day;

    // 自动任务运行服务实体
    String autotask_bean = (String) job.get("autotask_bean");
    // 查找任务类
    Class bean;
    try {
      Object obj = SessionUtil.getServerBean(autotask_bean);
      if (obj == null) {
        throw new ClassNotFoundException(autotask_bean + "初始化失败");
      }
      bean = obj.getClass();
      //} catch (NoSuchBeanDefinitionException nb) {
      //启动先后顺序，可能spring没有初始化完成 延时一下 60秒
      //随产品启动后，不可能出现spring没有初始化完成的问题，故延时一下 60秒 注掉
      //      try {
      //        Thread.sleep(1000 * 60);
      //      } catch (InterruptedException e) {
      //      }
      //      try {
      //        Object obj = SessionUtil.getServerBean(autotask_bean);
      //
      //        bean = obj.getClass();
      //      } catch (Exception e) {
      //        System.out.println("自动任务加载出错，找不到指定的任务类：“" + autotask_bean + "”");
      //        success = false;
      //        return;
      //      }
    } catch (Exception e1) {
      System.out.println("自动任务加载出错，找不到指定的任务类：“" + autotask_bean + "”");
      success = false;
      return;
    }
    // 任务实体参数
    String autotask_param = "";
    if (job.get("autotask_param") == null) {
      autotask_param = "";
    } else {
      autotask_param = (String) job.get("autotask_param");
      if ("null".equalsIgnoreCase(autotask_param)) {
        autotask_param = "";
      }
    }

    // 初始化job
    JobDetail jobDetail = new JobDetail(autotask_id, job_group, bean);

    // 初始化参数：key（param）
    if (!"".equals(autotask_param)) {
      jobDetail.getJobDataMap().put("param", autotask_param);
    }
    //add by wl 20130328 把区划和年份放入到jobDetail.getJobDataMap()中
    jobDetail.getJobDataMap().put("rg_code", rg_code);
    jobDetail.getJobDataMap().put("set_year", set_year);

    // 实例化任务监听类
    jobDetail.addJobListener(JobListenerName);

    StringBuffer sb = new StringBuffer();
    // 0：每年 1：每月 2：每周 3：每日
    int schedule_crontype = Integer.parseInt((String) job.get("schedule_crontype"));
    switch (schedule_crontype) {
    case 0: {// 每年
      // 第几月
      month_of_year = Integer.parseInt((String) job.get("month_of_year"));
      // 每月第几日
      day_of_month = Integer.parseInt((String) job.get("day_of_month"));
      // 小时
      hour_of_day = Integer.parseInt((String) job.get("hour_of_day"));
      // 分钟
      minute_of_day = Integer.parseInt((String) job.get("minute_of_day"));
      sb = new StringBuffer();
      sb.append("0 ");
      sb.append(minute_of_day + " ");
      sb.append(hour_of_day + " ");
      sb.append(day_of_month + " ");
      sb.append(month_of_year + " ");
      sb.append("? *");
      break;
    }
    case 1: {// 每月
      // 每月第几日
      day_of_month = Integer.parseInt((String) job.get("day_of_month"));
      // 小时
      hour_of_day = Integer.parseInt((String) job.get("hour_of_day"));
      // 分钟
      minute_of_day = Integer.parseInt((String) job.get("minute_of_day"));
      sb = new StringBuffer();
      sb.append("0 ");
      sb.append(minute_of_day + " ");
      sb.append(hour_of_day + " ");
      sb.append(day_of_month + " ");
      sb.append("* ");
      sb.append("? *");
      break;
    }
    case 2: {// 每周
      day_of_week = Integer.parseInt((String) job.get("day_of_week"));
      // 小时
      hour_of_day = Integer.parseInt((String) job.get("hour_of_day"));
      // 分钟
      minute_of_day = Integer.parseInt((String) job.get("minute_of_day"));

      sb = new StringBuffer();
      sb.append("0 ");
      sb.append(minute_of_day + " ");
      sb.append(hour_of_day + " ");
      sb.append("? ");
      sb.append("* ");
      switch (day_of_week) {
      case 1: {
        sb.append("MON");
        break;
      }
      case 2: {
        sb.append("TUE");
        break;
      }
      case 3: {
        sb.append("WED");
        break;
      }
      case 4: {
        sb.append("THU");
        break;
      }
      case 5: {
        sb.append("FRI");
        break;
      }

      case 6: {
        sb.append("SAT");
        break;
      }
      case 7: {
        sb.append("SUN");
        break;
      }
      }
      break;
    }
    case 3: {// 每日
      // 小时
      hour_of_day = Integer.parseInt((String) job.get("hour_of_day"));
      // 分钟
      minute_of_day = Integer.parseInt((String) job.get("minute_of_day"));
      sb = new StringBuffer();
      sb.append("0 ");
      sb.append(minute_of_day + " ");
      sb.append(hour_of_day + " ");
      sb.append("* ");
      sb.append("* ");
      sb.append("? *");
      // 定义Cron表达式

      break;
    }
    }
    // Cron表达式
    System.out.println("CronExpression:"+sb.toString());
    CronExpression cexp = new CronExpression(sb.toString());
    CronTrigger cronTrigger = new CronTrigger(autotask_id, job_group);
    // 设置Cron表达式
    cronTrigger.setCronExpression(cexp);
    scheduler.scheduleJob(jobDetail, cronTrigger);
  }

  /**
       * 新增自动任务
       * 
       * @param job
       *                XMLData 任务的信息
       * 
       * @throws ClassNotFoundException
       *                 如果指定的bean找不到，就抛出此错误
       * @throws ParseException
       *                 如果传进去的时间参数不符合设置的时间格式，抛出此错误
       * @throws SchedulerException
       *                 自动任务容器错误
       */
  public void addJob(final XMLData job) throws ClassNotFoundException, ParseException, SchedulerException {
    int autotask_type = Integer.parseInt((String) job.get("autotask_type"));
    if (autotask_type == 1)// 间隔运行
    {
      new Thread() {
        //job运行时类可能不能初始化，线程调用
        public void run() {
          try {
            System.out.println("-------------------addJob 间隔运行--------------------------");
            addSimpleTrigger(job);

          } catch (ParseException e) {
          } catch (SchedulerException e) {
          }
        }
      }.start();
    } else if (autotask_type == 2)// 定期运行
    {
      new Thread() {

        public void run() {
          try {
            System.out.println("-------------------addJob 定期运行 --------------------------");
            addCronTrigger(job);
          } catch (ParseException e) {
          } catch (SchedulerException e) {
          }
        }
      }.start();
    }
  }

  /**
       * 删除自动任务
       * 
       * @param job
       *                XMLData 任务的信息
       * 
       * @throws SchedulerException
       *                 自动任务容器错误
       */
  public void removeJob(XMLData job) throws SchedulerException {
    // 自动事务ID
    String autotask_id = (String) job.get("autotask_id");
    // 自动事务编码
    String autotask_code = (String) job.get("autotask_code");
    // 任务组名，取任务code的前三位，即各个子系统的编码
    String job_group = autotask_code.substring(0, 3);
    // 删除触发器
    scheduler.unscheduleJob(autotask_id, job_group);
    // 删除任务
    scheduler.deleteJob(autotask_id, job_group);
  }

  /**
       * 挂起当前自动任务
       * 
       * @param job
       *                XMLData 任务的信息
       * 
       * @throws SchedulerException
       *                 自动任务容器错误
       */
  public void pauseJob(XMLData job) throws SchedulerException {
    // 自动事务ID
    String autotask_id = (String) job.get("autotask_id");
    // 自动事务编码
    String autotask_code = (String) job.get("autotask_code");
    // 任务组名，取任务code的前三位，即各个子系统的编码
    String job_group = autotask_code.substring(0, 3);
    // 挂起任务
    scheduler.pauseJob(autotask_id, job_group);
  }

  /**
       * 恢复运行当前自动任务
       * 
       * @param job
       *                XMLData 任务的信息
       * 
       * @throws SchedulerException
       *                 自动任务容器错误
       */
  public void resumeJob(XMLData job) throws SchedulerException {
    // 自动事务ID
    String autotask_id = (String) job.get("autotask_id");
    // 自动事务编码
    String autotask_code = (String) job.get("autotask_code");
    // 任务组名，取任务code的前三位，即各个子系统的编码
    String job_group = autotask_code.substring(0, 3);
    // 删除任务
    scheduler.resumeJob(autotask_id, job_group);
  }
}