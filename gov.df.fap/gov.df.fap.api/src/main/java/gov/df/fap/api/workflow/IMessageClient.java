package gov.df.fap.api.workflow;

import java.util.List;

/**
 * 消息组件客户端接口
 * 
 * @version 1.0
 * @author 
 * @since java 1.6
 * 
 */
public interface IMessageClient {

  /**
   * 查询各子系统事务提醒。需要各子系统实现
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param Region-------------区划
   * @param set_year-----------年度
   * @param tokenId
   * @return List(FTaskDTO)
   * @throws Exception---------错误信息
   */
  public List findTasks(String UserId, String RoleId, String Region, String set_year) throws Exception;
}
