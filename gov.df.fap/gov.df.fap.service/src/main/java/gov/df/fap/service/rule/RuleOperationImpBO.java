package gov.df.fap.service.rule;

import gov.df.fap.api.rule.IRuleOperation;
import gov.df.fap.bean.rule.dto.RightGroupDTO;
import gov.df.fap.bean.rule.dto.RightGroupDetailDTO;
import gov.df.fap.bean.rule.dto.RightGroupTypeDTO;
import gov.df.fap.bean.rule.dto.RuleDTO;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleOperationImpBO implements IRuleOperation {

  @Autowired
  private UIRuleOperation rlop;

  private RuleDTO returnRuleDto = new RuleDTO();

  private RuleDTO ruleDto = new RuleDTO();

  public UIRuleOperation getRlop() {
    return rlop;
  }

  public void setRlop(UIRuleOperation rlop) {
    this.rlop = rlop;
  }

  @Override
  public Map<String, Object> saveOrUpdateRuleInfo(HttpServletRequest request, HttpServletResponse response) {

    //新增权限组
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String now_time = formatter.format(new Date());

    String create_type = request.getParameter("create_type");
    String edit_type = request.getParameter("edit_type");
    String rule_classify = setRuleClassifyByCreateType(create_type);

    String codeField = request.getParameter("rule_code");
    String nameField = request.getParameter("rule_name");
    String remarkField = request.getParameter("remark");
    String rule_id = request.getParameter("rule_id");

    Object rule_List = request.getParameter("rule_list");
    int right_type = Integer.parseInt(request.getParameter("right_type"));

    JSONArray getJsonArray = JSONArray.fromObject(rule_List);

    int ruledto_right_type = 0;

    //如果为新增权限组
    if (("new").equals(edit_type)) {
      returnRuleDto.setRULE_ID(UUIDRandom.generateNumberBySeqServer("SEQ_OTHER_ID"));
      returnRuleDto.setCREATE_DATE(now_time);

      try {
        //判断权限编码是否已被使用
        if (rlop.isExist(codeField)) {
          map.put("result", "fail");
          map.put("reason", "权限编码已存在！");
          return map;
        }
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

    } else if (("modify").equals(edit_type)) { ////如果为修改权限组
      returnRuleDto.setRULE_ID(rule_id);
      returnRuleDto.setLATEST_OP_DATE(now_time);
    }

    try {
      returnRuleDto.setRULE_CODE(codeField);
      returnRuleDto.setRULE_NAME(nameField);
      returnRuleDto.setREMARK(remarkField);
      returnRuleDto.setRG_CODE(getRgCode());
      returnRuleDto.setSET_YEAR(getSetYear());
      returnRuleDto.setENABLED(1);
      returnRuleDto.setRULE_CLASSIFY(rule_classify);

      String right_group_id = UUIDRandom.generate();

      //RightGroupDTO 表数据
      RightGroupDTO rightGroupDTO = new RightGroupDTO();
      rightGroupDTO.setRIGHT_GROUP_ID(right_group_id);
      rightGroupDTO.setRIGHT_GROUP_DESCRIBE("001");
      rightGroupDTO.setRIGHT_TYPE(right_type);
      rightGroupDTO.setRULE_ID(returnRuleDto.getRULE_ID());
      rightGroupDTO.setRG_CODE(getRgCode());
      rightGroupDTO.setSET_YEAR(getSetYear());

      List listRightGroupDTO = new ArrayList();

      for (int i = 0; i < getJsonArray.size(); i++) {

        Map<String, String> tempMap = (Map<String, String>) getJsonArray.getJSONObject(i);

        int rightGroupType_rightType = 0;

        for (Map.Entry<String, String> entry : tempMap.entrySet()) {

          String ele_code = entry.getKey();
          String ele_type = entry.getValue();
          try {
            if (Integer.parseInt(ele_type) == 1) {
              //全部权限 数据库中对应字段为0
              rightGroupType_rightType = 0;

              RightGroupDetailDTO detail_row_data = new RightGroupDetailDTO();
              detail_row_data.setRIGHT_GROUP_ID(right_group_id);
              detail_row_data.setELE_CODE(ele_code);
              detail_row_data.setELE_VALUE("#");
              detail_row_data.setSET_YEAR(getSetYear());
              detail_row_data.setRG_CODE(getRgCode());
              rightGroupDTO.detail_list.add(detail_row_data);

            }
          } catch (Exception e) {
            rightGroupType_rightType = 1;

            //如果有一个为部分权限则设置为1
            ruledto_right_type = 1;

            for (int ets = 0; ets < ele_type.split(",").length; ets++) {
              RightGroupDetailDTO detail_row_data = new RightGroupDetailDTO();
              detail_row_data.setRIGHT_GROUP_ID(right_group_id);
              detail_row_data.setELE_CODE(ele_code);
              detail_row_data.setELE_NAME(ele_type.split(",")[ets].split(" ")[1]);
              detail_row_data.setELE_VALUE(ele_type.split(",")[ets].split(" ")[0]);
              detail_row_data.setSET_YEAR(getSetYear());
              detail_row_data.setRG_CODE(getRgCode());
              rightGroupDTO.detail_list.add(detail_row_data);
            }

          }

          RightGroupTypeDTO type_row_data = new RightGroupTypeDTO();
          type_row_data.setRIGHT_GROUP_ID(right_group_id);
          type_row_data.setELE_CODE(ele_code);
          type_row_data.setRIGHT_TYPE(rightGroupType_rightType);
          type_row_data.setSET_YEAR(getSetYear());
          type_row_data.setRG_CODE(getRgCode());

          rightGroupDTO.type_list.add(type_row_data);
        }

      }
      listRightGroupDTO.add(rightGroupDTO);
      returnRuleDto.right_group_list = listRightGroupDTO;
      //设置right_type值
      returnRuleDto.setRIGHT_TYPE(ruledto_right_type);

      rlop.saveOrUpdateRule(returnRuleDto);
      map.put("rule_id",returnRuleDto.getRULE_ID());

      //校验
      //      if (inputValidated(nameField, codeField, remarkField, rule_classify, edit_type)) {
      //        if (convertToReturnRuleDto(remarkField, edit_type, returnRuleDto, rule_classify)) {
      //          try {
      //            returnRuleDto.setRULE_CODE(codeField);
      //            returnRuleDto.setRULE_NAME(nameField);
      //            returnRuleDto.setRG_CODE(getSetYear());
      //            returnRuleDto.setSET_YEAR(getRgCode());
      //            rlop.saveOrUpdateRule(returnRuleDto);
      //            rule_id = returnRuleDto.getRULE_ID().toString();
      //
      //            if (create_type.equals("newrule")) {
      //              // 保存关联数据
      //              SysRoleRule roleRule = new SysRoleRule();
      //              roleRule.setRULE_ID(returnRuleDto.getRULE_ID());
      //              // 如果返回RULE_ID不为空，则保存关联数据
      //              rlop.saveOrUpdateRule(returnRuleDto);
      //            }
      //          } catch (Exception e1) {
      //            //保存失败
      //            map.put("result", "fail");
      //            e1.printStackTrace();
      //          }
      //        }
      //      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return map;

  }

  @Override
  public Map<String, Object> editRule(HttpServletRequest request, HttpServletResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * 删除规则 2017-0326 by yanyga
   */
  public Map<String, Object> delRuleByRuleId(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    //前台要弹出提示框  （确定删除此权限？）
    String rule_id = request.getParameter("rule_id");

    if ("".equals("rule_id") || rule_id == null) {
      map.put("result", "fail");
    } else {
      try {
        if (rlop.isUsedInUserRoleRule(rule_id) || rlop.isUsedInUserRule(rule_id)) {
          map.put("result", "fail");
          map.put("reason", "该规则已经被使用,不能删除!");
        } else {
          rlop.deleteRule(rule_id);
          //删除完毕后请注意在前台重新调用下或者权限组列表ajax刷新页面
        }
      } catch (Exception ex) {
        map.put("result", "fail");
        ex.printStackTrace();
      }
    }
    return map;
  }

  @Override
  public Map<String, Object> ruleDisplay(HttpServletRequest request, HttpServletResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * 设置规则类型
   * @param  create_type  // 调用类型
   * @return 权限类型
   * 
   */
  private String setRuleClassifyByCreateType(String create_type) {
    // 角色授权时，规则类型为001
    if (("role").equalsIgnoreCase(create_type)) {
      return "001";
    }

    // 工作流授权时，规则类型为002
    if (("workflow").equalsIgnoreCase(create_type)) {
      return "002";
    }

    // 交易令授权时，规则类型为003
    if (("voucher").equalsIgnoreCase(create_type)) {
      return "003";
    }

    // 账套授权时，规则类型为004
    if (("bookset").equalsIgnoreCase(create_type)) {
      return "004";
    }

    // 定值授权时，规则类型为005
    if (("valueset").equalsIgnoreCase(create_type)) {
      return "005";
    }
    // 权限组授权时，规则类型类006
    if (("newrule").equalsIgnoreCase(create_type)) {
      return "006";
    }

    // 其他授权时，规则类型为999
    if (("other").equalsIgnoreCase(create_type) || "".equals(create_type)) {
      return "999";
    }
    return "999";
  }

  /**
   * 检验
   * @param nameField 权限名称
   * @param codeField 权限编码
   * @param remarkField 权限说明
   * @param rule_classify 权限类型
   * @param edit_type 创建类型 （决定是save还是update）
   * @return 返回是否校验通过
   * @throws Exception
   */
  private boolean inputValidated(String nameField, String codeField, String remarkField, String rule_classify,
    String edit_type) throws Exception {
    if (codeField.toString() == null || codeField.equals("")) {
      //权限编码不能为空!
      return false;
    }
    if (codeField.toString() != null && codeField.indexOf("'") > -1) {
      //权限编码中不能含有特殊字符 
      return false;
    }
    if ("006".equals(rule_classify)) {
      if (nameField.toString() == null || nameField.equals("")) {
        //权限名称不能为空!
        return false;
      }
    }
    if (nameField.toString() != null && nameField.indexOf("'") > -1) {
      //权限名称中不能含有特殊字符 
      return false;
    }
    if (remarkField.toString() != null && remarkField.toString().indexOf("'") > -1) {
      //说明中不能含有特殊字符
      return false;
    }

    if (edit_type.equalsIgnoreCase("modify")) {
      if (isExist(codeField)) {
        //权限编码已经存在!
        return false;
      }
    }
    return true;
  }

  /**
   * 判断编码是否存在
   * @param ruleCode 权限编码
   * @return
   * @throws Exception
   */
  private boolean isExist(String ruleCode) throws Exception {
    return rlop.isExist(ruleCode);
  }

  /**
   * 将当前界面数据保存到returnRuleDto中
   * 
   * @param e
   */
  private boolean convertToReturnRuleDto(String remarkField, String edit_type, RuleDTO ruleDto, String rule_classify) {
    //    String ele_code = "";
    //    returnRuleDto.setENABLED(1);
    //    returnRuleDto.setSET_YEAR(getSetYear());
    //    returnRuleDto.setREMARK(remarkField);
    //
    //    // 新增时重新删除规则ID，修改时保存原有ID
    //    if (edit_type.equals("new") || this.ruleDto.getRULE_ID() == null || this.ruleDto.getRULE_ID().toString().equals("")) {
    //      this.returnRuleDto.setRULE_ID(UUIDRandom.generateNumberBySeqServer("SEQ_OTHER_ID"));
    //    } else {
    //      this.returnRuleDto.setRULE_ID(this.ruleDto.getRULE_ID().toString());
    //    }
    //    returnRuleDto.setRULE_CODE(returnRuleDto.getRULE_ID().toString());
    //
    //    // 角色授权时，规则类型为001
    //    // if (this.create_type.equals("role")) {
    //    returnRuleDto.setRULE_CLASSIFY(rule_classify);
    //    // }
    //
    //    // 考虑到预览时也要重新赋值，所以这里将列表清空
    //    returnRuleDto.right_group_list.clear();
    //
    //    //UIList list<RightGroupDto>
    //    List listRightGroupDTO = null; //改成从前台获取
    //    // 更加权限组循环
    //    for (int k = 0; k < listRightGroupDTO.size(); k++) {
    //
    //      // 权限组基本信息
    //      RightGroupDTO tmpRightGroupDto = new RightGroupDTO();
    //      tmpRightGroupDto.setRIGHT_GROUP_DESCRIBE(((RightGroupDTO) listRightGroupDTO.get(k)).getRIGHT_GROUP_DESCRIBE()
    //        .toString());
    //      tmpRightGroupDto.setRIGHT_GROUP_ID(UUIDRandom.generate());
    //      tmpRightGroupDto.setRULE_ID(returnRuleDto.getRULE_ID());
    //      int right_type = 0;
    //
    //      // 如果该权限组没有选择任何要素，则提示
    //      if (tmpParent.sub_panel_list == null || tmpParent.sub_panel_list.size() == 0) {
    //        MessageBox msg1 = new MessageBox("范围必须选择至少一个要素！", 0, 1);
    //        msg1.setVisible(true);
    //        return false;
    //      }
    //
    //      // 权限组TYPE和DETAIL信息
    //      for (int l = 0; l < tmpParent.sub_panel_list.size(); l++) {
    //        UISubPanelDTO tmpSubPanel = (UISubPanelDTO) tmpParent.sub_panel_list.get(l);
    //        ele_code = tmpSubPanel.getEle_code().toString();
    //
    //        // 全部权限按钮
    //        JRadioButton tempBtn = tmpSubPanel.getAllRadioButton();
    //        // 全部权限按钮被选中
    //        if (tempBtn.isSelected()) {
    //          // 权限组TYPE信息
    //          RightGroupTypeDTO type_row_data = new RightGroupTypeDTO();
    //          type_row_data.setRIGHT_GROUP_ID(tmpRightGroupDto.getRIGHT_GROUP_ID().toString());
    //          type_row_data.setELE_CODE(ele_code);
    //          type_row_data.setRIGHT_TYPE(0);
    //          type_row_data.setSET_YEAR(Global.loginYear);
    //          type_row_data.setSET_YEAR(Global.loginYear);//add by kongcy at 2012-03-28 16:22:45
    //          type_row_data.setRG_CODE(Global.getLoginRgCode());//add by kongcy at 2012-03-28 16:22:45
    //          tmpRightGroupDto.type_list.add(type_row_data);
    //
    //          // 权限组DETAIL信息
    //          RightGroupDetailDTO detail_row_data = new RightGroupDetailDTO();
    //          detail_row_data.setRIGHT_GROUP_ID(tmpRightGroupDto.getRIGHT_GROUP_ID().toString());
    //          detail_row_data.setELE_CODE(ele_code);
    //          detail_row_data.setELE_VALUE("#");
    //          detail_row_data.setSET_YEAR(Global.loginYear);
    //          detail_row_data.setRG_CODE(Global.getLoginRgCode());//add by kongcy at 2012-03-28 16:29:23
    //          tmpRightGroupDto.detail_list.add(detail_row_data);
    //        }
    //
    //        // 部分权限按钮
    //        tempBtn = tmpSubPanel.getNotAllRadioButton();
    //        // 部分权限按钮被选中
    //        if (tempBtn.isSelected()) {
    //          // 权限组TYPE信息
    //          RightGroupTypeDTO type_row_data = new RightGroupTypeDTO();
    //          type_row_data.setRIGHT_GROUP_ID(tmpRightGroupDto.getRIGHT_GROUP_ID().toString());
    //          type_row_data.setELE_CODE(ele_code);
    //          type_row_data.setRIGHT_TYPE(1);
    //          type_row_data.setSET_YEAR(Global.loginYear);
    //          type_row_data.setRG_CODE(Global.getLoginRgCode());//add by kongcy at 2012-03-28 16:22:45
    //          tmpRightGroupDto.type_list.add(type_row_data);
    //
    //          // 权限组DETAIL信息
    //          FTree tempTree = tmpSubPanel.getTmp_tree();
    //          if (tempTree.getValue() != null && !tempTree.getValue().equals("")) {
    //            if (tempTree.getAllSelectedDatas() == null || tempTree.getAllSelectedDatas().size() == 0) {
    //              MessageBox msg2 = new MessageBox("选择部分权限不能选取根结点", 0, 1);
    //              msg2.setVisible(true);
    //              return false;
    //            }
    //            Iterator it = tempTree.getAllSelectedDatas().iterator();
    //            while (it.hasNext()) {
    //              Object tmpRowData = it.next();
    //
    //              if (MapBeanWrapper.getPropertyValueAsString(tmpRowData, "chr_id") != null
    //                && !MapBeanWrapper.getPropertyValueAsString(tmpRowData, "chr_id").equals("")) {
    //                RightGroupDetailDTO detail_row_data = new RightGroupDetailDTO();
    //                detail_row_data.setRIGHT_GROUP_ID(tmpRightGroupDto.getRIGHT_GROUP_ID().toString());
    //                detail_row_data.setELE_CODE(ele_code);
    //                detail_row_data.setELE_NAME(MapBeanWrapper.getPropertyValueAsString(tmpRowData, "chr_name"));
    //                detail_row_data.setELE_VALUE(MapBeanWrapper.getPropertyValueAsString(tmpRowData, "chr_code"));
    //                detail_row_data.setSET_YEAR(Global.loginYear);
    //                detail_row_data.setRG_CODE(Global.getLoginRgCode());//add by kongcy at 2012-03-28 16:29:23
    //                tmpRightGroupDto.detail_list.add(detail_row_data);
    //              }
    //            }
    //          } else {
    //            //选择部分权限必须勾选数据！
    //            return false;
    //          }
    //          right_type = 1;
    //        }
    //
    //      }
    //      tmpRightGroupDto.setRIGHT_TYPE(right_type);
    //      tmpRightGroupDto.setRG_CODE(getRgCode());//add by kongcy at 2012-03-28 16:22:45
    //      tmpRightGroupDto.setSET_YEAR(getSetYear());//add by kongcy at 2012-03-28 16:22:45
    //      returnRuleDto.right_group_list.add(tmpRightGroupDto);
    //    }
    //
    //    // 根据当前的权限组的权限类型
    //    returnRuleDto.setRIGHT_TYPE(rlop.getRightType(returnRuleDto.right_group_list));
    return true;
  }

  public String getRgCode() {
    String rg_code = (String) SessionUtil.getUserInfoContext().getContext().get("rg_code");
    //return "000000";
    return rg_code;
  }

  public String getSetYear() {
    String set_year = (String) SessionUtil.getUserInfoContext().getAttribute("set_year");

    //return "2017";
    return set_year;
  }

  @Override
  public Map<String, Object> getRuleDTODataByRuleId(HttpServletRequest request, HttpServletResponse response) {
    // TODO Auto-generated method stub
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    String rule_id = request.getParameter("rule_id");
    RuleDTO ruleDto = new RuleDTO();

    try {
      ruleDto = rlop.getRuleDto(rule_id);
      map.put("ruleDto", ruleDto);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      map.put("result", "fail");
      e.printStackTrace();
    }

    return map;
  }

  //实施校验权限编码是否存在
  @Override
  public Map<String, Object> checkRightCodeExist(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("result", "success");
    String rule_id = request.getParameter("rule_id");
    String edit_type = request.getParameter("edit_type");
    String codeField = request.getParameter("rule_code");

    if (("new").equals(edit_type)) {
      try {
        //判断权限编码是否已被使用
        if (rlop.isExist(codeField)) {
          map.put("result", "fail");
          map.put("reason", "权限编码已存在！");
          return map;
        }
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

    } else if (("modify").equals(edit_type)) { //如果为修改权限组

      try {
        List list = rlop.checkRightCodeExist(codeField);
        if (list == null || list.size() == 0) {
          return map;
        } else {
          String ruleId = ((Map) list.get(0)).get("rule_id").toString();
          if (!(ruleId.equals(rule_id))) {
            map.put("result", "fail");
            map.put("reason", "权限编码已存在！");
          }
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        map.put("result", "fail");
        e.printStackTrace();
      }
      return map;
    }
    return map;
  }

}
