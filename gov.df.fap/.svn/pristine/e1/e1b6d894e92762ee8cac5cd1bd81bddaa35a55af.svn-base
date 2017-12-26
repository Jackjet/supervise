package gov.df.fap.api.workflow;

import java.util.List;

/**
 * <p>
 * Title:工作流规则组件接口
 * </p>
 * <p>
 * Description:工作流流转线规则回调接口
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015-2017 北京用友政务软件有限公司
 * </p>
 * <p>
 * Company:北京用友政务软件有限公司
 * </p>
 * 
 * @version 1.0
 * @author justin
 * @since java 1.6
 */
public interface ISysRegulationListener {
  /**
   * 工作流前流转线规则调用
   * @currentNodeId 当前节点
   * @nextNodeId  下一节点
   * @rulelist 规则集
   */
  public boolean beforeLinceRulePropertiesSet(long currentNodeId, long nextNodeId, List rulelist) throws Exception;

  /**
   * 工作流后流转线规则调用
   * @currentNodeId 当前节点
   * @nextNodeId  下一节点
   * @rulelist 规则集
   */
  public boolean afterLinceRulePropertiesSet(long currentNodeId, long nextNodeId, List rulelist) throws Exception;

}
