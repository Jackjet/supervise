package gov.df.fap.controller.elerule;

import gov.df.fap.api.fapcommon.IUserSync;
import gov.df.fap.api.rule.IRuleConfigure;
import gov.df.fap.api.sysbilltype.ISysBillType;
import gov.df.fap.bean.form.SysEleRuleForm;
import gov.df.fap.bean.rule.dto.RuleDTO;
import gov.df.fap.bean.rule.entity.RuleEntity;
import gov.df.fap.bean.user.UserDTO;
import gov.df.fap.util.xml.XMLData;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 要素定值规则controller
 */
@Controller
@RequestMapping("/df/elerule")
public class SysEleRuleController implements Serializable {

  private static final long serialVersionUID = -1693798406362909895L;

  private static final Logger logger = LoggerFactory.getLogger(SysEleRuleController.class);

  @Autowired
  private ISysBillType iSysBillType;

  @Autowired
  private IRuleConfigure iRuleConfigure;

  @Autowired
  private IUserSync iUserSyncManager = null;

  // 调用类型
  private String create_type = "";

  private String rule_classify = "";

  /**
   * 查询要素定值规则主表列表
   * @param eleCode
   * @return
   */
  @RequestMapping(value = "/list.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> list(@RequestParam(value = "ele_code", required = false)
  String eleCode) {
    Map<String, Object> result = new HashMap<String, Object>();
    List<?> list = new ArrayList<XMLData>();
    try {
      //查询要素定值规则列表信息
      list = iSysBillType.getEleRuleWithField("ele_rule_id, ele_rule_code, ele_rule_name, ele_code, set_year", "");
      result.put("data", list);
      result.put("errorCode", 0);
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "查询要素定值规则主表失败");
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 根据定值规则id查询定值规则明细列表
   * @param eleRuleId
   * @return
   */
  @RequestMapping(value = "/detail/list.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getDetailList(@RequestParam("ele_rule_id")
  String eleRuleId) {
    Map<String, Object> result = new HashMap<String, Object>();
    List<?> list = new ArrayList<XMLData>();
    try {
      list = iSysBillType.getRuleInfo(eleRuleId);
      result.put("data", list);
      result.put("errorCode", 0);
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "查询要素定值规则明细表失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
    }
    return result;
  }

  /**
   * 根据eleRuleId删除要素定值规则
   * @param eleRuleId
   * @return
   */
  @RequestMapping(value = "/del.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> del(@RequestParam("ele_rule_id")
  String eleRuleId) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      //删除一条要素定值规则
      iSysBillType.deleteEleRule(eleRuleId);
      result.put("errorCode", 0);
      result.put("flag", "success");
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "删除要素定值规则失败," + e.getMessage());
      logger.error("删除要素定值规则失败:", e);
    }
    return result;
  }

  /**
   * 取消定值规则
   * @param eleRuleDetailId
   * @return
   */
  @RequestMapping(value = "/cancel.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> cancel(@RequestParam("ele_rule_detail_id")
  String eleRuleDetailId) {
    Map<String, Object> result = new HashMap<String, Object>();
    XMLData strMap = new XMLData();
    try {
      //根据要素定值规则明细id查询要素定值规则明细表
      strMap = iSysBillType.selectByDetailId(eleRuleDetailId);
      String eleRuleID = strMap.get("ele_rule_id").toString();
      String eleValue = strMap.get("ele_value").toString();
      String ruleId = strMap.get("rule_id").toString();
      //取消规则(删除定值规则详细表和规则相关数据)
      iSysBillType.deleteEleRuleDetailAndRule(eleRuleID, eleValue, ruleId);
      result.put("errorCode", 0);
      result.put("flag", "success");
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "取消规则发生错误," + e.getMessage());
      logger.error("取消规则失败：", e);
    }
    return result;
  }

  /**
   * 保存新增的要素定值规则
   * @param form
   * @return
   */
  @RequestMapping(value = "/save.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> save(SysEleRuleForm form) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Map<String, Object> data = new HashMap<String, Object>();
      //新增一条要素定值规则
      data = iSysBillType.insertEleRule(getXMLData(form));
      result.put("data", data);
      result.put("errorCode", 0);
      result.put("errorMsg", "保存成功");
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "保存失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
      logger.error("保存要素定值规则失败：", e);
    }
    return result;
  }

  /**
   * 保存编辑的要素定值规则
   * @param form
   * @return
   */
  @RequestMapping(value = "/update.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> update(SysEleRuleForm form) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Map<String, Object> data = new HashMap<String, Object>();
      //编辑要素定值规则
      data = iSysBillType.updateEleRule(getXMLData(form));
      result.put("data", data);
      result.put("errorCode", 0);
      result.put("errorMsg", "更新成功");
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "更新失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
      logger.error("更新要素定值规则失败：", e);
    }
    return result;
  }

  /**
   * 将form中数据取出放入XMLData中
   * @param form
   * @return value
   */
  private XMLData getXMLData(SysEleRuleForm form) {
    XMLData value = new XMLData();
    Class<?> classForm = form.getClass();
    Field[] fields = classForm.getDeclaredFields(); // 得到SysEleRuleForm全部属性
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), classForm);
        Method getMethod = pd.getReadMethod(); // 获得get方法
        Object obj = getMethod.invoke(form); // 执行get方法返回一个Object对象
        if (obj != null) {
          value.put(field.getName(), obj);
        }
      } catch (Exception e) {
        logger.error("form表单转XMLData出错：", e);
        e.printStackTrace();
      }
    }
    return value;
  }

  /**
   * 获取定值规则详细信息
   * 
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/get.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> SelectRule(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> result = new HashMap<String, Object>();
    List<Object> data = new ArrayList<Object>();
    String eleRuleDetailId = request.getParameter("ele_rule_detail_id");
    try {
      if (eleRuleDetailId != null && !eleRuleDetailId.equals("")) {
        XMLData data1 = iSysBillType.selectByDetailId(eleRuleDetailId);
        String ruleId = data1.get("rule_id").toString();
        RuleDTO data2 = iRuleConfigure.getRuleDto(ruleId);
        data.add(data1);
        data.add(data2);
      } else {
        RuleDTO data3 = null;
        XMLData data4 = null;
        data.add(data3);
        data.add(data4);
      }
      result.put("data", data);
      result.put("errorCode", 0);
    } catch (Exception e) {
      result.put("errorCode", -1);
      e.printStackTrace();
    }
    return result;

  }

  /**
   * 获取定值规则详细信息 通过规则ID
   * 
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/getRuleById.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getRuleById(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> result = new HashMap<String, Object>();
    String ruleId = request.getParameter("ruleId");
    try {
      if (ruleId != null && !ruleId.equals("")) {
        RuleDTO data = iRuleConfigure.getRuleDto(ruleId);
        result.put("errorCode", 0);
        result.put("data", data);
      }
    } catch (Exception e) {
      result.put("errorCode", -1);
      e.printStackTrace();
    }
    return result;

  }

  /**
   * 设定规则,保存规则
   * 
   * @param request
   * @param xmlData
   * @return
   */
  @RequestMapping(value = "/set.do", method = RequestMethod.POST)
  @ExceptionHandler({ HttpMessageNotReadableException.class })
  public @ResponseBody
  Map<String, Object> insertRule(String tokenid, String ele_value, String ele_code, String ele_name, String rule_id,
    String ele_rule_id, String ruledata, String is_relation_ele) {
    Map<String, Object> result = new HashMap<String, Object>();
    RuleEntity ruleData = JSONObject.parseObject(ruledata, RuleEntity.class);
    try {
      //获取年度和区划信息
      UserDTO userdto = (UserDTO) iUserSyncManager.getUser(tokenid);
      String setYear = String.valueOf(userdto.getSet_year());
      String rgCode = userdto.getRg_code();
      if ("".equals(rule_id) || "null".equals(rule_id)) {//新增
        ruleData.setSet_year(setYear);
        ruleData.setRg_code(rgCode);
        ruleData.setRule_id(iSysBillType.generateNumberBySeq("SEQ_OTHER_ID"));//新增时获取rule_id;
        create_type ="valueset";
        setRuleClassify();
        ruleData.setRule_classify(rule_classify);
        ruleData.getRight_group_list().get(0).setRight_group_id(UUID.randomUUID().toString());//新增时获取Right_group_id
        ruleData.getRight_group_list().get(0).setRight_group_describe("001");
        ruleData.getRight_group_list().get(0).setRule_id(ruleData.getRule_id());
        ruleData.getRight_group_list().get(0).setSet_year(setYear);
        ruleData.getRight_group_list().get(0).setRg_code(rgCode);
        for (int j = 0; j < ruleData.getRight_group_list().get(0).getType_list().size(); j++) {
          ruleData.getRight_group_list().get(0).getType_list().get(j)
            .setRight_group_id(ruleData.getRight_group_list().get(0).getRight_group_id());
          ruleData.getRight_group_list().get(0).getType_list().get(j).setSet_year(setYear);
          ruleData.getRight_group_list().get(0).getType_list().get(j).setRg_code(rgCode);
        }
        for (int i = 0; i < ruleData.getRight_group_list().get(0).getDetail_list().size(); i++) {
          ruleData.getRight_group_list().get(0).getDetail_list().get(i)
            .setRight_group_id(ruleData.getRight_group_list().get(0).getRight_group_id());
          ruleData.getRight_group_list().get(0).getDetail_list().get(i).setSet_year(setYear);
          ruleData.getRight_group_list().get(0).getDetail_list().get(i).setRg_code(rgCode);
        }
        iRuleConfigure.saveAndUpdateRule(ele_value, ele_code, ele_name, ruleData.getRule_id(), ele_rule_id, ruleData,
          is_relation_ele);

      } else {//修改
        ruleData.setSet_year(setYear);
        ruleData.setRg_code(rgCode);
        ruleData.getRight_group_list().get(0).setSet_year(setYear);
        ruleData.getRight_group_list().get(0).setRg_code(rgCode);
        for (int j = 0; j < ruleData.getRight_group_list().get(0).getType_list().size(); j++) {
          ruleData.getRight_group_list().get(0).getType_list().get(j)
            .setRight_group_id(ruleData.getRight_group_list().get(0).getRight_group_id());
          ruleData.getRight_group_list().get(0).getType_list().get(j).setSet_year(setYear);
          ruleData.getRight_group_list().get(0).getType_list().get(j).setRg_code(rgCode);
        }
        for (int i = 0; i < ruleData.getRight_group_list().get(0).getDetail_list().size(); i++) {
          ruleData.getRight_group_list().get(0).getDetail_list().get(i)
            .setRight_group_id(ruleData.getRight_group_list().get(0).getRight_group_id());
          ruleData.getRight_group_list().get(0).getDetail_list().get(i).setSet_year(setYear);
          ruleData.getRight_group_list().get(0).getDetail_list().get(i).setRg_code(rgCode);
        }
        iRuleConfigure
          .saveAndUpdateRule(ele_value, ele_code, ele_name, rule_id, ele_rule_id, ruleData, is_relation_ele);
      }
      result.put("errorCode", 0);
      result.put("ruleData", ruleData);
    } catch (Exception e) {
      result.put("errorCode", -1);
      logger.error("设定规则失败：", e);
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 设置规则类型
   * 
   * 
   */
  private void setRuleClassify() {
    // 角色授权时，规则类型为001
    if (("role").equalsIgnoreCase(this.create_type)) {
      rule_classify = "001";
    }

    // 工作流授权时，规则类型为002
    if (("workflow").equalsIgnoreCase(this.create_type)) {
      rule_classify = "002";
    }

    // 交易令授权时，规则类型为003
    if (("voucher").equalsIgnoreCase(this.create_type)) {
      rule_classify = "003";
    }

    // 账套授权时，规则类型为004
    if (("bookset").equalsIgnoreCase(this.create_type)) {
      rule_classify = "004";
    }

    // 定值授权时，规则类型为005
    if (("valueset").equalsIgnoreCase(this.create_type)) {
      rule_classify = "005";
    }
    // 权限组授权时，规则类型类006
    if (("newrule").equalsIgnoreCase(this.create_type)) {
      rule_classify = "006";
    }

    // 其他授权时，规则类型为999
    if (("other").equalsIgnoreCase(this.create_type) || "".equals(this.create_type)) {
      rule_classify = "999";
    }
  }

}
