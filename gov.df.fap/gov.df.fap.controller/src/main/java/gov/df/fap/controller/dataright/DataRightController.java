package gov.df.fap.controller.dataright;

import gov.df.fap.api.dictionary.IElementSetOperation;
import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.api.rule.IRuleOperation;
import gov.df.fap.api.rule.ISysRight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/dataright")
public class DataRightController {

  @Autowired
  private IDataRight iDataRight;

  @Autowired
  private ISysRight iSysRight;

  @Autowired
  private IRuleOperation iRuleOperation;

  @Autowired
  private IElementSetOperation smop;

  public IElementSetOperation getSmop() {
    return smop;
  }

  public void setSmop(IElementSetOperation smop) {
    this.smop = smop;
  }

  public ISysRight getiSysRight() {
    return iSysRight;
  }

  public void setiSysRight(ISysRight iSysRight) {
    this.iSysRight = iSysRight;
  }

  /**
   * 插入或者更新权限组织
   * 
   */
  @RequestMapping(value = "/saveorupdate.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();

    try {
      map = iRuleOperation.saveOrUpdateRuleInfo(request, response);
    } catch (Exception e) {
    }
    return map;
  }

  /**
   * 校验权限编码是否存在
   * 
   */
  @RequestMapping(value = "/checkRightCodeExist.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> checkRightCodeExist(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();

    try {
      map = iRuleOperation.checkRightCodeExist(request, response);
    } catch (Exception e) {
    }
    return map;
  }

  /**
   * 获取权限组列表
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/showGroupList.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> showGroupList(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    try {
      map = iDataRight.showAllGROUPlist();
    } catch (Exception e) {
      map.put("result", "fail");
      map.put("reason", "获取权限组失败！");
    }
    return map;
  }

  /**
   * 获取右边的展示列表
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/getSysRightList.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> getSysRightList(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    String rule_id = request.getParameter("rule_id");
    try {
      List rightList = iSysRight.getMainInfoTreeByRuleIdNew(rule_id);
      //List rightList = iSysRight.getMainRightInfoTreeByRuleId(rule_id);
      map.put("rows", rightList);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return map;
  }

  /**
   * 获取右边的展示列表
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/setupElements.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> setupElements(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    try {
      List right_list = (List) iSysRight.getElementByCondition(" and is_rightfilter=1");
      List left_list = (List) iSysRight.getElementByCondition(" and is_rightfilter=0");
      //bottonCanEditflag 表中有数据 返回false 无数据 返回true
      boolean bottonCanEditflag = smop.checkRight();
      map.put("right_list", right_list);
      map.put("left_list", left_list);
      map.put("flag", bottonCanEditflag);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return map;
  }

  /**
   * 获取右边的展示列表
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/saveElements.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveElements(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    try {
      boolean bottonCanEditflag = smop.checkRight();
      Map<String, Object> rightSet = new HashMap<String, Object>();
      String a = request.getParameter("left_list");
      String b = request.getParameter("right_list");
      String[] left = a.split(",");
      String[] right = b.split(",");
      rightSet.put("left_list", left);
      rightSet.put("right_list", right);

      if (bottonCanEditflag) {
        //保存数据逻辑
        smop.saveRightSet(rightSet);
        return map;

      }
      map.put("result", "fail");
      map.put("reason", "系统已经使用该权限要素集合的数据，不能修改权限要素！");

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return map;
  }

  /**
   * 获取已启用的权限要素分配到标签上
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/getEnabledEle.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> getEnabledEle(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map = iDataRight.initEnabledElement(request, response);
      map.put("result", "success");
    } catch (Exception e) {
      e.printStackTrace();
      map.put("result", "fail");
    }
    return map;
  }

  /**
   * 删除规则数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/delRuleByRuleId.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> delRuleByRuleId(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map = iRuleOperation.delRuleByRuleId(request, response);
    return map;
  }

  /**
   * 通过rule_id得到ruledto对象 
   * 用作修改规则时候使用
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/getRuleDTODataByRuleId.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> getRuleDTODataByRuleId(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map = iRuleOperation.getRuleDTODataByRuleId(request, response);
    return map;
  }

  public IDataRight getiDataRight() {
    return iDataRight;
  }

  public void setiDataRight(IDataRight iDataRight) {
    this.iDataRight = iDataRight;
  }
}
