package gov.df.fap.service.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * <p>Title: 消息平台-消息定时发送任务</p>
 * <p>Description: 消息定时发送任务</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 北京用友政务软件有限公司</p>
 * @author 
 * @see
 * @CreateDate fri 21, 2017
 * @version 1.0
 */
public class MsgSendJob implements Job {

  public void execute(JobExecutionContext context) throws JobExecutionException {

    //    IMsgTaskService taskService = MsgPlatformBeanFactory.getServerMsgTaskService();

    //    String task_status = taskService.getLockStatus();
    //    if ("1".equals(task_status)) {//占用状态直接返回
    //      return;
    //
    //    }
    //    //更新锁状态为占用
    //    List lockList = new ArrayList();
    //    Map lockStatus = new HashMap();
    //    lockStatus.put("status", "1");
    //    lockList.add(lockStatus);
    //    taskService.updateLockStatus(lockList);
    //    List taskList = null;
    //
    //    try {
    //      //任务的区划年度
    //      Map map = context.getJobDetail().getJobDataMap();
    //      SessionUtil.getUserInfoContext().setRgCode((String) map.get("rg_code"));
    //      SessionUtil.getUserInfoContext().setSetYear((String) map.get("set_year"));
    //
    //      MsgProducerInstance msgProducer = new MsgProducerInstance();
    //      //从序列化对象表中读取代发任务的业务数据
    //      List rsList = taskService.querySerializableObjs(SessionUtil.getRgCode(), SessionUtil.getLoginYear());
    //      if (rsList != null && rsList.size() == 2) {
    //        List ids = (List) rsList.get(0);
    //        List objs = (List) rsList.get(1);
    //        int size = objs.size();
    //        for (int i = 0; i < size; i++) {
    //          MsgServiceParam msgParam = (MsgServiceParam) objs.get(i);
    //          Map id = (Map) ids.get(i);
    //          String msgRuleCode = msgParam.getMsgRuleCode();
    //          try {
    //            if (msgRuleCode != null) {//功能上配置了msgRuleCode的是手动自动任务模式
    //              taskList = msgProducer.buildMsg(msgParam, MsgConstant.MSG_TYPE_MANUAL_AUTOTASK);
    //            } else {//工作流自动任务模式
    //              taskList = msgProducer.buildMsg(msgParam, MsgConstant.MSG_TYPE_WORKFLOW_AUTOTASK);
    //            }
    //          } catch (Exception e1) {
    //            e1.printStackTrace();
    //            continue;//进行下一个obj业务数据的操作
    //          }
    //          try {
    //            if (taskList != null && !taskList.isEmpty()) {
    //              //消息发送
    //              MsgSender msgSender = new MsgSenderInstance();
    //              msgSender.sendMsg(taskList);
    //            }
    //          } catch (Exception e) {
    //            e.printStackTrace();
    //          } finally {
    //            //只要开始发送消息，最后都要删除序列化数据，避免下次重复发送，异常后面的消息将不能发送
    //            //删除序列化数据
    //            List deleteId = new ArrayList();
    //            deleteId.add(id);
    //            taskService.deleteSerializableObjs(deleteId);
    //          }
    //
    //        }
    //
    //      }
    //
    //      //从代发任务表中得到要发送的消息任务(代发任务表数据由定时存储过程或手动维护)
    //      taskList = taskService.getCurrentTasks(SessionUtil.getRgCode(), SessionUtil.getLoginYear(), "1", "0");
    //
    //      if (taskList != null && !taskList.isEmpty()) {
    //        //消息发送
    //        MsgSender msgSender = new MsgSenderInstance();
    //        List result = msgSender.sendMsg(taskList);
    //        //发送后任务处理
    //        taskService.handleTasksAfterSend(result);
    //      }
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //    } finally {
    //      //更新排它锁为自由态
    //      List unlockList = new ArrayList();
    //      Map unlockStatus = new HashMap();
    //      unlockStatus.put("status", "0");
    //      unlockList.add(unlockStatus);
    //      taskService.updateLockStatus(unlockList);
    //    }
  }
}
