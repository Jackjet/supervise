package gov.df.fap.api.workflow;

/**
 * <p>Title: 待办事宜接口</p>
 *
 * <p>Description: 提供给客户端、由应用系统实现的待办事宜接口</p>
 *
 * <p>Copyright: Copyright (c) 2017</p>
 *
 * <p>Company: 北京用友政务软件有限公司</p>
 *
 * <p>Author: justin</p>
 * 
 * <p>Version 8.0</p> 
 */
public interface IPendingWorkService {

  /**
   * 根据userid、sessionid、count、区划查询待办事项列表
   * @param userId
   * @param sessionId
   * @param count
   * @param region
   * @return
   * @throws Exception
   * @author zhaoqiang 20140623
   */
  public Object[] findAllTaskByRegionAndYear(String userId, String sessionId, String count, String region, String year)
    throws Exception;
}
