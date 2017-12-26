package gov.df.fap.api.workflow.message;

import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.bean.workflow.FMessageDTO;
import gov.df.fap.util.data.TableData;

import java.util.List;

/**
 * 消息组件接口
 * 
 * @version 1.0
 * @author zhadaopeng
 * @since java 1.4.1
 * 
 */
public interface IMessage {

  /**
   * 查询全局事务提醒。
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @return List(内部含FTaskItemDTO)
   * @throws Exception---------错误信息
   */
  public TableData findAllTasks(String UserId, String RoleId, String rgcode, String set_year) throws Exception;

  /**
   * 发送消息接口。
   * 
   * @param FMessageDTO-------------FMessageDTO
   * @return true或false
   * @throws Exception---------错误信息
   */
  public boolean sendMessage(FMessageDTO msg) throws Exception;

  /**
   * 检索消息接口。
   * 
   * @param UserId-------------用户ID
   * @param RoleId-------------角色ID
   * @param plussql------------附加SQL
   * @param FPageDTO-----------分页
   * @return List(内部含FMessageDTO)
   * @throws Exception---------错误信息
   */
  public List findMessage(String user_id, String role_id, String plussql, FPaginationDTO page) throws Exception;

  /**
   * 写消息接收成功标志。
   * 
   * @param msg_id-------------消息ID
   * @return true或false
   * @throws Exception---------错误信息
   */
  public boolean setReceiveflag(String msg_id) throws Exception;

  /**
   * findAllTask的扩展，根据rgcode、用户ID得到所有待办事宜
   * @param userId
   * @param sessionId
   * @param count
   * @param rgcode
   * @return
   * @throws Exception
  * @author zwh 20120517
   */
  public List findAllTaskByRegion(String userId, String sessionId, int count, String rgcode, String set_year)
    throws Exception;

  /**
   * 从ele_region中获取is_valid=1的记录，返回list中装载map,select e.chr_code,e.chr_name,e.is_top  from ele_region e  where  e.is_valid=1
   * @return
   * @throws Exception
   * @author zwh 20120517
   */
  public List getValidRegion() throws Exception;
}
