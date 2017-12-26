package gov.df.fap.api.workflow;

import gov.df.fap.util.exception.FAppException;
import gov.df.fap.util.xml.XMLData;

import java.util.List;

/**
 * <p>
 * Title: 工作流组件接口
 * </p>
 * <p>
 * Description:工作流组件接口
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005-2008 北京方正春元科技发展有限公司
 * </p>
 * <p>
 * Company:北京方正春元科技发展有限公司
 * </p>
 * 
 * @version 1.0
 * @author ymj
 * 
 * @since java 1.4.2
 */
public interface IDoWorkFlowForRecall {
  /**
   * 撤销数据所走接口
   * 
   * @param TableName
   *            表名称
   * @param EntityId
   *            数据ID
   * @return List对象（内部含FWFLogDTO)
   * @throws FAppException
   *             错误信息
   */
  public List doSingleProcessSimplyRecall(String moduleid, String roleid, String operationname, String operationdate,
    String operationremark, List inputFVoucherDtos, boolean auto_tolly_flag, boolean isBillDetail, XMLData tmpCanGoData)
    throws FAppException;
}
