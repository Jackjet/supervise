package gov.df.fap.api.message;

import java.util.List;

/**
 * 
 * <p>Title: 消息平台-消息任务服务接口</p>
 * <p>Description: 消息平台-消息任务服务接口</p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: 北京用友政务软件有限公司</p>
 * @author lirf
 * @see
 * @CreateDate Jul 23, 2017
 * @version 1.0
 */
public interface IMsgTaskService {

  /**
   * 批量添加任务 参数 List中元素为MsgBaseDTO
   */
  public void batchAddCurrentTasks(List msgDtoList);

  /**
   * 查询任务 返回List中元素为MsgTaskDTO
   * @return List
   */
  public List getCurrentTasks(String rgCode, String setYear, String isValid, String status);

  /**
   * 消息发送后处理任务数据
   */
  public void handleTasksAfterSend(List taskDtoList);

  /**
   * 插入一个序列化对象
   */
  public boolean saveSerializableObj(Object obj, String rg_code, String set_year);

  /**
   * 读取序列化的对象
   * 
   * @return List 0 ids, 1 objs
   */
  public List querySerializableObjs(String rg_code, String set_year);

  /**
   * 删除序列化数据
   * 
   */
  public void deleteSerializableObjs(List ids);

  /**
   * 更新排他锁sql
   * @return
   */
  public void updateLockStatus(List lockStatusList);

  /**
   * 查询排他锁sql
   * @return
   */
  public String getLockStatus();

}
