package gov.df.fap.api.rule;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRuleOperation {

  //保存或者更新规则数据
  public Map<String, Object> saveOrUpdateRuleInfo(HttpServletRequest request, HttpServletResponse response);

  //修改规则数据
  public Map<String, Object> editRule(HttpServletRequest request, HttpServletResponse response);

  //删除规则数据
  public Map<String, Object> delRuleByRuleId(HttpServletRequest request, HttpServletResponse response);

  //规则预览
  public Map<String, Object> ruleDisplay(HttpServletRequest request, HttpServletResponse response);

  //通过rule_id得到ruledto对象 （用作修改规则时候使用）
  public Map<String, Object> getRuleDTODataByRuleId(HttpServletRequest request, HttpServletResponse response);

  //实施校验权限编码是否存在
  public Map<String, Object> checkRightCodeExist(HttpServletRequest request, HttpServletResponse response);

}
