package gov.df.fap.api.systemmanager.autotask.ibs;

import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.sql.SQLException;
import java.util.List;


/**
 * 日志管理服务端接口
 * 
 * @author 
 * @author 2017年5月9日
 * @version 1.0
 * @since JDK1.6
 * @since quartz-1.8.3
 */
public interface ISysAutoTask {

	public List getAllAutoTaskMonitor() throws Exception;

	/**
	 * 取得所有的自动任务定义列表
	 * 
	 * @return 自动任务定义列表
	 * @throws SQLException
	 */
	public List findAllTasks() throws SQLException;

	public List findAllTasksBySysId(String sys_id) throws SQLException;

	/**
	 * 根据任务sys_id取得自动任务一览列表
	 * 
	 * @param sys_id
	 *            String sys_id
	 * @return list 任务信息（具体内容参考表sys_autotask）
	 * @throws Exception
	 *             向前抛出sql错误
	 * @author 朱建
	 */

	public XMLData getTaskByAutoTaskId(int autotask_id) throws SQLException;

	/**
	 * 根据任务autotask_id取得自动任务一览列表
	 * 
	 * @param autotask_id
	 *            int autotask_id
	 * @return XMLData 任务信息（具体内容参考表sys_autotask）
	 * @throws Exception
	 *             向前抛出sql错误
	 * @author 朱建
	 */

	public void saveTask(XMLData xmlData) throws Exception;

	/**
	 * 根据taskId删除定期自动任务记录
	 * 
	 * @param taskId
	 *            taskId 定期自动任务ID
	 * @throws Exception
	 */
	public void deleteTask(long taskId) throws Exception;

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
	public XMLData getTaskById(long taskId) throws Exception;

	/**
	 * 手工执行自动任务
	 * 
	 * @param taskId
	 *            int 任务id
	 * @param jobStatus
	 *            int 1：启用 2：挂起 3：恢复 4：停用
	 * @throws Exception
	 *             向前抛出异常错误
	 * @author 
	 */
	public void excuteTask(long taskId, int jobStatus) throws Exception;

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
	public int excuteTaskByHand(long taskId) throws Exception;

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
	public void wasExed(long taskId,String token_id, boolean isSuccess) throws Exception;

	/**
	 * 即将运行时修改状态信息
	 * 
	 * 20150205 扩展，使其控制在集群环境下，只有一个生产机在执行
	 * 
	 * @param taskId
	 *            long 任务id
	 * 
	 * @throws Exception
	 *             抛出错误
	 *             
	 * @return String 返回令牌
	 * 
	 * @author 
	 */
	public String toBeExe(long taskId) throws Exception;

	/**
	 * 取得所有可用的自动任务定义列表
	 * 
	 * @return List 任务列表(XMLData 参考表sys_autotask)
	 * 
	 * @throws Exception
	 *             向前抛出sql错误
	 * @author 
	 */
	public List findAllEnableJob() throws Exception;
	
	/**
	 * 按照sysid取得可用的自动任务定义列表
	 * 
	 * @return List 任务列表(XMLData 参考表sys_autotask)
	 * 
	 * @param sysId 系统ID
	 * 
	 * @throws Exception
	 *             向前抛出sql错误
	 * @author 
	 */
	public List findEnableJobBySysId(String sysId) throws Exception;

	/**
	 * 加载sys_autotask表定义的所有的自动任务到容器
	 * 
	 * @throws Exception
	 *             向前抛出错误
	 * @author 
	 */
	public void loadAllJob() throws Exception;
	
	/**
	 * 按照sysid加载sys_autotask表定义的自动任务到容器
	 * 
	 * @throws Exception
	 *             向前抛出错误
	 *             
     * @param sysId 系统ID
     *
	 * @author 
	 */
	public void loadJobBySysId(String sysId) throws Exception;

	/**
	 * 根据子系统编号查询定时任务
	 * 
	 * @param sysId
	 * @author tyy
	 * @date 2008-2-2
	 * @return
	 */
	public List getSysAutoTaskBySysId(String sysId);

	/**
	 * 根据定时任务查询信息
	 * 
	 * @param sysId
	 * @author tyy
	 * @date 2008-2-2
	 * @return
	 */
	public List getSysAutoTaskByautotaskId(String autotaskId);

	/**
	 * 判断taskCode是否存在
	 * 
	 * @param newAutoTaskCode
	 *            String newAutoTaskCode
	 * @return true 为存在， false为不存在
	 */
	public boolean isAutoTaskCodeExist(String newAutoTaskCode)
			throws SQLException;
	
	 /**
     * 新增时获取autotask_id
     * @param seq
     * @return autotask_id
     */
  public String generateNumberBySeq(String seq);
  
	/**
	 * 查询有关构建树数据信息(查询子系统)
	 */
	public List findTreeSysAppInfo() throws Exception ;
	
	/**
	 * 查询有关构建树数据信息(查询单自动任务)
	 */
	public List findTreeSysAutoTaskInfo() throws Exception;
	
	
	public List getSysAutoTaskWithStatusByTaskId(String autotask_id)throws Exception;
 
}