package gov.df.fap.api.workflow.message;

import gov.df.fap.bean.message.MsgServiceParam;

import java.util.List;

/**
 * <p>Title:消息平台服务接口</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: 北京用友政务软件有限公司</p>
 * @author Gavin.Guo
 * @see 
 * @CreateDate Jul 16, 2014
 * @version 1.0
 */
public interface MsgService {

  /**
   * 发送消息接口
   * @param msgParam
   * @return
   */
  public List sendMessage(MsgServiceParam msgParam);

  /**
   * 发送消息接口
   * @param msgParam
   * @return
   */
  public List buildMessage(MsgServiceParam msgParam);

}
