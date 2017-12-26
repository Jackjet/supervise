package gov.df.supervise.controller.common;

import gov.df.fap.api.workflow.IWorkFlowNew;
import gov.df.fap.api.workflow.activiti.design.IGetBpmJson;
import gov.df.fap.api.workflow.activiti.design.IGetModelId;
import gov.df.supervise.api.common.Query;
import gov.df.supervise.bean.common.PageListBean;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 通用查询控制层，根据列表视图和查询视图字段配置查询数据。
 * 可根据用户权限，从工作流中查询数据，可查询虚拟字段。
 * @author 周玲玲
 * @time 20170413
 */
@SuppressWarnings("unchecked")
@Controller
@RequestMapping(value = "/df/csof/common/query")
public class CommonQueryController {

  @Autowired
  private Query query;

  @Autowired
  IWorkFlowNew workFlowNew;

  @Autowired
  IGetBpmJson iGetBpmJson;

  @Autowired
  IGetModelId iGetModelId;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 根据列表视图和查询视图字段配置，查询数据。
   */
  @RequestMapping(value = "/doFind.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> doFind(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      PageListBean pageListBean = query.queryByRequest(request, response);
      result.put("totalElements", pageListBean.getTotalrows());
      result.put("dataDetail", pageListBean.getDataList());
      result.put("flag", true);
      result.put("result", "");
    } catch (Exception e) {
      result.put("flag", false);
      result.put("result", "异常");
    }

    return result;
  }

}
