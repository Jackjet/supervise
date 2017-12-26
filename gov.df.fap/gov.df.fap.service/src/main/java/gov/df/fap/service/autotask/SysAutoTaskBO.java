package gov.df.fap.service.autotask;

import gov.df.fap.api.log.ILog;
import gov.df.fap.api.systemmanager.autotask.ibs.ISysAutoTask;
import gov.df.fap.bean.log.LogDTO;
import gov.df.fap.bean.user.UserDTO;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.log.Log;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 用户管理服务端接口实现类
 * 
 * @author a
 * @version 1.0
 * @since JDK1.6.2
 * @since quartz-1.83
 */
@Service
public class SysAutoTaskBO implements ISysAutoTask {

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao = null;

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  @Autowired
  public ILog log;

  /**
   * 得到接口的所有信息
   * @return list
   * @throws Exception
   *             向前抛出错误
   * @author 
   */
  public List getAllAutoTaskMonitor() throws Exception {
    StringBuffer sql = new StringBuffer();
    sql.append("select t.autotask_code,t.autotask_name, m.* from sys_autotask_monitor m,sys_autotask t ")
      .append("where m.autotask_id=t.autotask_id and m.task_status<>1")
      .append(" and t.rg_code=m.rg_code and t.set_year=m.set_year and t.rg_code=? and t.set_year=?");
    List list = null;
    try {
      list = dao.findBySql(sql.toString(), new Object[] { this.getRgCode(), this.getLoginYear() });
    } catch (Exception e) {
      throw e;
    }

    return list;
  }

  /**
   * 取得自动任务一览列表
   * 
   * @return list 任务信息（具体内容参考表sys_autotask）
   * @throws Exception
   *             向前抛出sql错误
   */
  public List findAllTasks() {
    StringBuffer sql = new StringBuffer();
    sql.append(
      "select a.*, m.task_status from sys_autotask a ,sys_autotask_monitor m  where a.autotask_id=m.autotask_id")
      .append(" and a.rg_code=m.rg_code and a.set_year=m.set_year and a.rg_code=? and a.set_year=?");
    List list = null;
    try {
      list = dao.findBySql(sql.toString(), new Object[] { this.getRgCode(), this.getLoginYear() });
    } catch (Exception e) {
      System.out.println("自动任务获取失败！" + e.getMessage());
    }

    return list;
  }

  /**
   * 根据任务sys_id取得自动任务一览列表
   * 
   * @param sys_id
   *            String sys_id
   * @return list 任务信息（具体内容参考表sys_autotask）
   * @throws Exception
   *             向前抛出sql错误
   * @author 
   */

  public List findAllTasksBySysId(String sys_id) {
    StringBuffer sb = new StringBuffer();
    sb.append("select a.*, m.task_status from sys_autotask a ,sys_autotask_monitor m ")
      .append(
        "where a.autotask_id=m.autotask_id and a.rg_code=m.rg_code and a.set_year=m.set_year and a.sys_id=? and a.rg_code=? and a.set_year=?");
    List list = dao.findBySql(sb.toString(), new Object[] { sys_id, this.getRgCode(), this.getLoginYear() });
    return list;
  }

  /**
   * 根据任务autotask_id取得自动任务一览列表
   * 
   * @param autotask_id
   *            int autotask_id
   * @return XMLData 任务信息（具体内容参考表sys_autotask）
   * @throws Exception
   *             向前抛出sql错误
   * @author 
   */
  public XMLData getTaskByAutoTaskId(int autotask_id) {
    XMLData task = null;
    // 取得所有的自动任务定义列表
    String sql = "select * from sys_autotask sa where sa.autotask_id=?";
    List task_List = dao.findBySql(sql, new Object[] { new Integer(autotask_id) });
    if (task_List.size() > 0) {
      task = (XMLData) task_List.get(0);
    }
    return task;
  }

  /**
   * 自动保存任务
   * 
   * @param xmlData
   *            XMLData xmlData
   * 
   * @throws Exception
   * 
   * @author
   * edit by wl 20130327 sys_autotask sys_autotask_monitor 分区划
   */
  public void saveTask(XMLData xmlData) throws Exception {
    String rgCode = this.getRgCode();
    String setYear = this.getLoginYear();
    xmlData.put("rg_code", rgCode);
    xmlData.put("set_year", setYear);
    String autoTaskId = (String) xmlData.get("autotask_id");
    if(autoTaskId != null) {
    	dao.deleteDataBySql("sys_autotask", xmlData, new String[] { "autotask_id" });
    }
    dao.saveDataBySql("sys_autotask", xmlData);
    String autotask_id = (String) xmlData.get("autotask_id");
    String sql = "select * from sys_autotask_monitor where autotask_id=?";
    List monitorList = dao.findBySql(sql, new Object[] { autotask_id });

    if (monitorList == null || monitorList.isEmpty()) {
      sql = "insert into sys_autotask_monitor (autotask_id, set_year,rg_code,totaltime,total_count,fail_count,success_count,task_status,token_id) values (?,?,?,0,0,0,0,1,?)";
      dao.executeBySql(sql,
        new Object[] { new Integer(autotask_id), new Integer(setYear), rgCode, UUIDRandom.generate() });

      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(getUserName() + "创建了自动任务，自动任务id为" + autotask_id);
      log.writeLog(logDto);
    } else {
      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(getUserName() + "修改了自动任务，自动任务autotask_id=" + autotask_id);
      log.writeLog(logDto);
    }

  }

  /**
   * 自动删除任务
   * 
   * @param taskId
   *            long taskId
   * 
   * @throws Exception
   * 
   * @author
   */
  public void deleteTask(long taskId) throws Exception {

    try {
      // 删除sys_autotask记录
      String sql = "delete from sys_autotask where autotask_id=?";
      dao.executeBySql(sql, new Object[] { new Long(taskId) });

      // 删除sys_autotask_monitor记录
      sql = "delete from sys_autotask_monitor where autotask_id=?";
      dao.executeBySql(sql, new Object[] { new Long(taskId) });

      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(getUserName() + "删除了自动任务，自动任务autotask_id=" + taskId);
      log.writeLog(logDto);
    } catch (Exception e) {

      throw e;
    }
  }

  /**
   * 执行自动任务
   * 
   * @param taskId
   *            int 任务id
   * @param jobStatus
   *            int 1：启用 2：挂起 3：恢复 4：停用
   * @throws Exception
   *             向前抛出异常错误
   * @author 
   */
  public void excuteTask(long taskId, int jobStatus) throws Exception {

    AutoTaskFactory autotaskFactory = AutoTaskFactory.getInstance();
    XMLData job = this.getTaskById(taskId);
    System.out.println("jobStatus:" + jobStatus);
    if (jobStatus == 1) {
      // 自动任务启用
      System.out.println(getUserName() + "启用自动任务，自动任务autotask_id=" + taskId);
      autotaskFactory.addJob(job);
      // 将自动任务置为运行中
      this.changeStatus(taskId, 2);

      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(getUserName() + "启用自动任务，自动任务autotask_id=" + taskId);
      log.writeLog(logDto);

    } else if (jobStatus == 2) {
      // 自动任务挂起
      autotaskFactory.pauseJob(job);
      // 将自动任务置为挂起
      this.changeStatus(taskId, 3);

      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(getUserName() + "挂起自动任务，自动任务autotask_id=" + taskId);
      log.writeLog(logDto);

    } else if (jobStatus == 3) {
      // 自动任务恢复
      autotaskFactory.resumeJob(job);
      // 将自动任务置为运行中
      this.changeStatus(taskId, 2);

      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(getUserName() + "恢复自动任务，自动任务autotask_id=" + taskId);
      log.writeLog(logDto);

    } else if (jobStatus == 4) {
      // 自动任务停用
      autotaskFactory.removeJob(job);
      // 将自动任务置为未启用
      this.changeStatus(taskId, 1);
      System.out.println(getUserName() + "停用自动任务，自动任务autotask_id=" + taskId);
      //			 保存操作日志
      LogDTO logDto = new LogDTO();
      logDto.setLog_type("1");
      logDto.setLog_level(new Integer(0));
      logDto.setRemark(getUserName() + "停用自动任务，自动任务autotask_id=" + taskId);
      log.writeLog(logDto);

    }
  }

  /**
   * 手工执行自动任务
   * 
   * @param taskId
   *            long taskId 定期自动任务ID
   * @throws Exception
   *             异常抛出
   * @return 0-正常运行，1-有参数，不能执行，2-任务不存在，不能执行
   * @author 
   */
  public int excuteTaskByHand(long taskId) throws Exception {
    XMLData job = this.getTaskById(taskId);// 查找任务信息
    if (job != null) {//
      if (job.get("autotask_param") != null) {
        if (job.get("autotask_bean") != null) {
          //hult 20120606修改，在有参数的情况下，人工生成参数传递到运行，内部只支持系统设置参数应用
          Object obj = SessionUtil.getServerBean(String.valueOf(job.get("autotask_bean")));
          if (obj == null) {
            throw new Exception(job.get("autotask_bean") + "初始化失败");
          }
          Class job_class = obj.getClass();
          Job job_object = (Job) job_class.newInstance();
          //手动执行自动任务时把rg_code,set_year放入到JobDetail.jobDataMap中
          JobDetail jobDetail = new JobDetail();
          jobDetail.getJobDataMap().put("rg_code", this.getRgCode());
          jobDetail.getJobDataMap().put("set_year", this.getLoginYear());
          JobExecutionContext jec = (new JobExecutionContext(null, new TriggerFiredBundle(jobDetail,
            new SimpleTrigger(), null, false, null, null, null, null), job_object));
          jec.getMergedJobDataMap().put("param", job.get("autotask_param"));
          String token_id = this.toBeExe(taskId);
          job_object.execute(jec);
          this.wasExed(taskId, token_id, true);
          //保存操作日志
          LogDTO logDto = new LogDTO();
          logDto.setLog_type("1");
          logDto.setLog_level(new Integer(0));
          logDto.setRemark(getUserName() + "手动执行自动任务，自动任务autotask_id=" + taskId);
          log.writeLog(logDto);
        }
        return 0;

      } else {
        if (job.get("autotask_bean") != null) {
          Object obj = SessionUtil.getServerBean(String.valueOf(job.get("autotask_bean")));
          if (obj == null) {
            throw new Exception(job.get("autotask_bean") + "初始化失败");
          }
          Class job_class = obj.getClass();
          Job job_object = (Job) job_class.newInstance();
          //add by wl 20130328手动执行自动任务时把rg_code,set_year放入到JobDetail.jobDataMap中
          JobDetail jobDetail = new JobDetail();
          jobDetail.getJobDataMap().put("rg_code", this.getRgCode());
          jobDetail.getJobDataMap().put("set_year", this.getLoginYear());
          JobExecutionContext jec = (new JobExecutionContext(null, new TriggerFiredBundle(jobDetail,
            new SimpleTrigger(), null, false, null, null, null, null), job_object));
          String token_id = this.toBeExe(taskId);
          job_object.execute(jec);
          this.wasExed(taskId, token_id, true);

          //					 保存操作日志
          LogDTO logDto = new LogDTO();
          logDto.setLog_type("1");
          logDto.setLog_level(new Integer(0));
          logDto.setRemark(getUserName() + "手动执行自动任务，自动任务autotask_id=" + taskId);
          log.writeLog(logDto);

        }
        return 0;
      }
    }
    return 2;
  }

  /**
   * 根据任务id去任务具体信息
   * 
   * @param taskId
   *            long任务id
   * @return XMLData 任务信息（具体内容参考表sys_autotask）
   * @throws Exception
   *             向前抛出sql错误
   * @author 
   */
  public XMLData getTaskById(long taskId) throws Exception {
    XMLData job = null;
    // 取得所有的自动任务定义列表
    String sql = "select * from sys_autotask sa where sa.autotask_id=?";
    List job_List = dao.findBySql(sql, new Object[] { new Long(taskId) });
    if (job_List.size() > 0) {
      job = (XMLData) job_List.get(0);
    }
    return job;
  }

  /**
   * 设置某个任务的状态
   * 
   * @param taskId
   *            long 任务id
   * @param status
   *            int 任务状态--1:未启用 2:运行中 3:挂起 4:执行中
   * 
   * @author 
   */
  public void changeStatus(long taskId, int status) {
    String sql = "update sys_autotask_monitor sam set sam.task_status=? where sam.autotask_id=?";
    dao.executeBySql(sql, new Object[] { new Integer(status), new Long(taskId) });
  }

  /**
   * 运行结束改状态信息
   * 
   * @param taskId
   *            long 任务id
   * @param isSuccess
   *            boolean是否成功
   * 
   * @throws Exception
   *             如果传进去的时间参数不符合设置的时间格式，抛出此错误
   * @author 
   */
  public void wasExed(long taskId, String tokenId, boolean isSuccess) throws Exception {
    //执行结束后 更新执行中状态 为运行状态，设置运行令牌 
	  StringBuffer sql = new StringBuffer();
	  if(TypeOfDB.isOracle()) {
		  sql.append("update sys_autotask_monitor sam set sam.last_exe_time=to_char(SYSDATE,'yyyy-MM-dd HH24:mi:ss'),sam.totaltime=SYSDATE-to_DATE(start_time,'yyyy-MM-dd HH24:mi:ss'),sam.fail_count=sam.fail_count+");
	  } else if(TypeOfDB.isMySQL()) {
		  sql.append("update sys_autotask_monitor sam set sam.last_exe_time=date_format(now(),'%Y-%m-%d %H:%i:%s'),sam.totaltime=now()-str_to_date(start_time,'%Y-%m-%d %H:%i:%s'),sam.fail_count=sam.fail_count+");
	  }
	  sql.append(isSuccess ? 0 : 1)
	  	.append(",sam.success_count=sam.success_count+")
	  	.append(isSuccess ? 1 : 0)
	  	.append(",sam.total_count=sam.total_count+1,sam.task_status=2,sam.token_id=? where sam.autotask_id=? and sam.token_id=? and sam.task_status=4");
    dao.executeBySql(sql.toString(), new Object[] { tokenId, new Long(taskId), tokenId });
  }

  /**
   * 即将运行时修改状态信息
   * 
   * @param taskId
   *            long 任务id
   * 
   * @author 
   */
  public String toBeExe(long taskId) throws Exception {
    //只有状态为运行中的任务再能被执行,获取任务锁 token_id
    String sql = "select sam.start_time,sam.token_id from sys_autotask_monitor sam where sam.autotask_id=? and sam.task_status=2";
    List count = dao.findBySql(sql, new Object[] { new Long(taskId) });
    //产生执行令牌 
    String newToken_id = UUIDRandom.generate();
    System.out.println("------newToken_id:" + newToken_id + "-----count:" + count);
    //根据任务锁，更新运行状态 为执行中状态，设置执行令牌 
    if(TypeOfDB.isOracle()) {
    	sql = "update sys_autotask_monitor sam set sam.task_status=4,sam.start_time=to_char(SYSDATE,'yyyy-MM-dd HH24:mi:ss'),sam.last_exe_time=?,sam.token_id=? where sam.autotask_id=? and sam.token_id=? and sam.task_status=2";
    } else if(TypeOfDB.isMySQL()) {
    	sql = "update sys_autotask_monitor sam set sam.task_status=4,sam.start_time=date_format(now(),'%Y-%m-%d %H:%m:%s'),sam.last_exe_time=?,sam.token_id=? where sam.autotask_id=? and sam.token_id=? and sam.task_status=2";
    }
    int updateCount = dao.executeBySql(sql,
      new Object[] { "", newToken_id, new Long(taskId), ((XMLData) count.get(0)).get("token_id") });
    //设置成功，则有当前节点执行，否则此任务已在其他节点执行
    if (updateCount == 0) {
      throw new Exception("任务" + taskId + "已在执行中");
    }
    return newToken_id;
  }

  /**
   * 取得所有可用的自动任务定义列表
   * 
   * @return List 任务列表(XMLData 参考表sys_autotask)
   * 
   * @throws Exception
   *             向前抛出sql错误
   * 
   * @author 
   */
  public List findAllEnableJob() throws Exception {
    // 取得所有的自动任务定义列表
    String sql = "select * from sys_autotask sa where sa.enabled=1 and sa.autotask_id in (select sam.autotask_id from sys_autotask_monitor sam where sam.task_status <> 1)";
    List all_Job = dao.findBySql(sql);
    return all_Job;
  }

  /**
   * 取得所有可用的自动任务定义列表
   * 
   * @return List 任务列表(XMLData 参考表sys_autotask)
   * 
   * @throws Exception
   *             向前抛出sql错误
   * 
   * @author 
   */
  public List findEnableJobBySysId(String sysId) throws Exception {
    // 取得所有的自动任务定义列表
    String sql = "select * from sys_autotask sa where sa.enabled=1 and sys_id =? and sa.autotask_id in (select sam.autotask_id from sys_autotask_monitor sam where sam.task_status <> 1)";
    List all_Job = dao.findBySql(sql, new Object[] { sysId });
    return all_Job;
  }

  /**
   * 加载sys_autotask表定义的所有的自动任务到容器
   * 
   * @throws Exception
   *             向前抛出错误
   * @author 
   */
  public void loadAllJob() throws Exception {
    int success = 0;
    AutoTaskFactory autotaskFactory = AutoTaskFactory.getInstance();
    // 1：间隔运行 2：定期运行
    int autotask_type;
    // 取得所有可用的自动任务定义列表
    List all_Job = this.findAllEnableJob();
    int size = all_Job.size();
    // 循环取任务，并加载
    Iterator itr = all_Job.iterator();
    // 一个临时任务对象
    XMLData job;
    int job_id = 0;
    while (itr.hasNext()) {
      job = (XMLData) itr.next();
      String autotasktype = (String) job.get("autotask_type");
      if (!"".equals(autotasktype) || autotasktype != null) {
        autotask_type = Integer.parseInt(autotasktype);
        if (autotask_type == 1)// 间隔运行
        {
          autotaskFactory.addSimpleTrigger(job);
          if (autotaskFactory.success) {
            success++;
          }
        } else if (autotask_type == 2)// 定期运行
        {
          if (job != null) {
            autotaskFactory.addCronTrigger(job);
            if (autotaskFactory.success) {
              success++;
            }
          }

        }
        String autotaskid = (String) job.get("autotask_id");
        if (autotaskid != null && !autotaskid.equals(""))
          job_id = Integer.parseInt(autotaskid);
        // 修改自动任务状态为：运行中
        this.changeStatus(job_id, 2);
      }

    }
    System.out.println("当前服务端有启用自动任务" + size + "个，成功加载" + success + "个");
  }

  public void loadJobBySysId(String sysId) throws Exception {
    int success = 0;
    AutoTaskFactory autotaskFactory = AutoTaskFactory.getInstance();
    // 1：间隔运行 2：定期运行
    int autotask_type;
    // 取得所有可用的自动任务定义列表
    List all_Job = this.findEnableJobBySysId(sysId);
    int size = all_Job.size();
    // 循环取任务，并加载
    Iterator itr = all_Job.iterator();
    // 一个临时任务对象
    XMLData job;
    int job_id = 0;
    while (itr.hasNext()) {
      job = (XMLData) itr.next();
      String autotasktype = (String) job.get("autotask_type");
      if (!"".equals(autotasktype) || autotasktype != null) {
        autotask_type = Integer.parseInt(autotasktype);
        if (autotask_type == 1)// 间隔运行
        {
          autotaskFactory.addSimpleTrigger(job);
          if (autotaskFactory.success) {
            success++;
          }
        } else if (autotask_type == 2)// 定期运行
        {
          if (job != null) {
            autotaskFactory.addCronTrigger(job);
            if (autotaskFactory.success) {
              success++;
            }
          }

        }
        String autotaskid = (String) job.get("autotask_id");
        if (autotaskid != null && !autotaskid.equals(""))
          job_id = Integer.parseInt(autotaskid);
        // 修改自动任务状态为：运行中
        this.changeStatus(job_id, 2);
      }

    }
    System.out.println("当前" + sysId + "产品有启用自动任务" + size + "个，成功加载" + success + "个");
  }

  /**
   * 根据子系统查询自动任务
   * 
   * @param sysId
   *            子系统id
   * @author 2008-2-2 tyy
   * 
   * @return
   * edit by wl 20130327 sys_autotask加入区划过滤
   */
  public List getSysAutoTaskBySysId(String sysId) {
    List list = null;
    String sql = "select autotask_id,autotask_id as chr_id,autotask_code as chr_code,autotask_name chr_name,"
      + "'autotaskRoot' as parent_id from sys_autotask where sys_id=? and rg_code=? and set_year=?";
    try {
      list = dao.findBySql(sql, new Object[] { sysId, this.getRgCode(), this.getLoginYear() });
    } catch (Exception e) {
      Log.error(e.getMessage());
    }
    return list;
  }

  /**
   * 根据autotask_id查询记录
   * 
   * @param sysId
   *            子系统id
   * @author 2008-2-2 tyy
   * @return
   */
  public List getSysAutoTaskByautotaskId(String autotaskId) {
    List list = null;
    String sql = "select * from sys_autotask where autotask_id=?";
    try {
      list = dao.findBySql(sql, new Object[] { autotaskId });
    } catch (Exception e) {
      Log.error(e.getMessage());
    }
    return list;
  }

  /**
   * 判断taskCode是否存在
   * 
   * @param newAutoTaskCode
   *            String newAutoTaskCode
   * @return true 为存在， false为不存在
   * edit by wl 20130327 sys_autotask分区划
   */
  public boolean isAutoTaskCodeExist(String newAutoTaskCode) throws SQLException {
    String sql = "select 1 from sys_autotask where autotask_code=? and rg_code=? and set_year=?";
    List list = dao.findBySql(sql, new Object[] { newAutoTaskCode, this.getRgCode(), this.getLoginYear() });
    if (list.size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 从session中取当前区划
   * @return rg_code
   */
  private String getRgCode() {
    return SessionUtil.getRgCode();
  }

  /**
   * 从session中取当前年度
   * @return set_year
   */
  private String getLoginYear() {
    return SessionUtil.getLoginYear();
  }

  private String getUserName() {
    String userName = "";
    UserDTO userDto = SessionUtil.getOnlineUser();
    if (null == userDto) {
      if (null != SessionUtil.getUserInfoContext()) {
        userName = (String) SessionUtil.getUserInfoContext().getAttribute("user_name");
      }
    } else {
      userName = userDto.getUser_name();
    }
    return userName;
  }

  @Override
  /**
   * 新增时获取rule_id
   * @param seq
   * @return rule_id
   */
  public String generateNumberBySeq(String seq) {
	  String result = null;
	  if(TypeOfDB.isOracle()) {
		  String sql = "select " + seq + ".NEXTVAL as id from dual";
		  List list = null;
		  list = dao.findBySql(sql);
		  if (list != null && ((XMLData) list.get(0)).get("id").toString() != null) {
			  result = ((XMLData) list.get(0)).get("id").toString();
		  }
	  } else if(TypeOfDB.isMySQL()) {
		  String sql = "select nextval('" + seq + "') as value";
		  List list = dao.findBySql(sql);
		  if(list != null && !list.isEmpty()) {
			  result = (String) ((XMLData)list.get(0)).get("value");
		  }
	  }
	  return result;
  }

  @Override
  public List findTreeSysAppInfo() throws Exception {
	  String sql = null;
	  if(TypeOfDB.isOracle()) {
		  sql = "select sys_id as ID,sys_id as Cid,sys_id as Sys_ID,sys_id||' '||sys_name as NAME from sys_app where sys_id<>'000' order by sys_id";
	  } else if(TypeOfDB.isMySQL()) {
		  sql = "select sys_id as ID,sys_id as Cid,sys_id as Sys_ID,concat(sys_id, ' ', sys_name) as NAME from sys_app where sys_id<>'000' order by sys_id";
	  }
    return dao.findBySql(sql);
  }

	@Override
	public List findTreeSysAutoTaskInfo() throws Exception {
		String sql = null;
		if (TypeOfDB.isOracle()) {
			sql = "select autotask_id as ID,autotask_code||'t' as Cid,sys_id as Sys_ID,autotask_code||' '||autotask_name as NAME from sys_autotask order by autotask_code";
		} else if (TypeOfDB.isMySQL()) {
			sql = "select autotask_id as ID, concat(autotask_code, 't') as Cid,sys_id as Sys_ID,concat(autotask_code, ' ', autotask_name) as NAME from sys_autotask order by autotask_code";
		}
		return dao.findBySql(sql);
	}

  @Override
  public List getSysAutoTaskWithStatusByTaskId(String autotask_id) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sql = new StringBuffer();
    sql.append("select t.*, m.* from sys_autotask_monitor m,sys_autotask t ").append(
      "where m.autotask_id=t.autotask_id and t.autotask_id=?");
    List list = null;
    try {
      list = dao.findBySql(sql.toString(), new Object[] { autotask_id });
    } catch (Exception e) {
      throw e;
    }

    return list;

  }

}