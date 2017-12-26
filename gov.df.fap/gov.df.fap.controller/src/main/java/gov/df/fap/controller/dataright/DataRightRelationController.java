package gov.df.fap.controller.dataright;

import gov.df.fap.api.dictionary.ElementOperation;
import gov.df.fap.api.fapcommon.IRoleDfCommonService;
import gov.df.fap.api.fapcommon.IUserSync;
import gov.df.fap.api.orgtype.ISysOrgtype;
import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.api.user.ISysUser;
import gov.df.fap.bean.control.FAssistObjectDTO;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/datarightrelation")
public class DataRightRelationController {
  @Autowired
  private IUserSync iuserService;

  @Autowired
  private IRoleDfCommonService iroleService;

  @Autowired
  private ISysUser isysUser;

  @Autowired
  private ISysOrgtype isysOrgtype;

  @Autowired
  private IDataRight iDataRight;

  @Autowired
  @Qualifier("elementOperationBO")
  private ElementOperation elementOperation;

  @RequestMapping(value = "/getUserTree.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> getUserTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List userList = iuserService.findAllUsers();
     
      Map<String, Object> map2 = new HashMap<String, Object>();
      map2.put("guid", "-1");
      map2.put("name", "管理类用户");
      map2.put("usertype", "");
      userList.add(map2);

      Map<String, Object> map3 = new HashMap<String, Object>();
      map3.put("guid", "1");
      map3.put("name", "财政类用户");
      map3.put("usertype", "");
      userList.add(map3);

      Map<String, Object> map4 = new HashMap<String, Object>();
      map4.put("guid", "0");
      map4.put("name", "单位类用户");
      map4.put("usertype", "");
      userList.add(map4);

      Map<String, Object> map5 = new HashMap<String, Object>();
      map5.put("guid", "3");
      map5.put("name", "银行类用户");
      map5.put("usertype", "");
      userList.add(map5);
      map.put("users", userList);
    } catch (Exception e) {

      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/getRightInfoByUserId.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> getRightInfoByUserId(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String userGuid = request.getParameter("userGuid");
    List userRight = null;
    List roleRight = null;
    try {
      String orgType = isysOrgtype.getOrgTypeByUserId(userGuid);
      if (orgType == null) {
        orgType = request.getParameter("orgType");
      }
      map.put("org_type", orgType);
      request.setAttribute("org_type", orgType);
      List userInfoArr = isysUser.getRightInfo(userGuid);
      if (userInfoArr != null && userInfoArr.size() > 0) {
        userRight = (List) userInfoArr.get(0);
        roleRight = (List) userInfoArr.get(1);
      }
      map.put("userRight", userRight);
      map.put("roleRight", roleRight);
      Map<String, Object> map1 = new HashMap<String, Object>();
      map1 = getEleByConditionAsObj(request, response);
      if (map1 != null) {
        map.put("orgTypes", (List) map1.get("orgTypes"));
        map.put("selectedorgTypes", (List) map1.get("selectedorgTypes"));
      }
    } catch (Exception e) {

      e.printStackTrace();
    }

    return map;
  }

  @RequestMapping(value = "/addCaRole.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> getCaRole(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String roleType = request.getParameter("userType");
      if ("0".equals(roleType)) {
        roleType = "2";
      }
      String sql = " roletype='" + roleType + "'";
      List roleList = iroleService.queryRolesBySql(sql);
      if ("-1".equals(roleType)) {
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("guid", "-1");
        map2.put("name", "管理类角色");
        map2.put("roletype", 0);
        roleList.add(map2);
      } else if ("1".equals(roleType)) {
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("guid", "1");
        map3.put("name", "财政类角色");
        map3.put("roletype", 0);
        roleList.add(map3);
      } else if ("2".equals(roleType)) {
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("guid", "2");
        map4.put("name", "单位类角色");
        map4.put("roletype", 0);
        roleList.add(map4);
      } else if ("4".equals(roleType)) {
        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("guid", "4");
        map5.put("name", "银行类角色");
        map5.put("roletype", 0);
        roleList.add(map5);
      }
      map.put("addroles", roleList);
    } catch (Exception e) {

      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/saveDataRightReltion.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveDataRightReltion(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    List list = new ArrayList();
    try {
      String userId = request.getParameter("guid");
      String organTreeId = request.getParameter("organTreeId");
      String rule_id = request.getParameter("ruleId");
      String roleIds = request.getParameter("roleTreeId");
      String[] org_ids = organTreeId.split(",");
      String org_type = request.getParameter("org_type");
      JSONArray getJsonArray = JSONArray.fromObject(roleIds);
      //      JSONArray getJsonArray = JSONArray.fromObject(roleIds);
      List listRight = new ArrayList();
      Map<String, String> tempMap = null;
      for (int i = 0; i < getJsonArray.size(); i++) {
        tempMap = (Map<String, String>) getJsonArray.get(i);
        String role_id = null;
        String role_name = null;
        if (tempMap != null && tempMap.size() > 0) {
          role_id = tempMap.get("role_id");
          role_name = tempMap.get("role_name");
          XMLData roleXd = new XMLData();
          roleXd.put("role_id", role_id);
          roleXd.put("role_name", role_name);
          listRight.add(roleXd);
        }

      }

      List org_idList = new ArrayList();
      for (int i = 0; i < org_ids.length; i++) {
        if (!org_ids[i].isEmpty()) {
          XMLData orgmap = new XMLData();
          orgmap.put("chr_id", org_ids[i]);
          org_idList.add(orgmap);
        }
      }
      XMLData xmd1 = new XMLData();
      xmd1.put("user_id", userId);
      xmd1.put("org_id", org_idList);
      xmd1.put("rule_id", rule_id);
      xmd1.put("org_type", org_type);
      list.add(xmd1);
      XMLData xmd2 = new XMLData();
      list.add(xmd2);
      list.add(listRight);
      isysUser.saveorUpdateSysUser(list);
      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/showAllGROUPlist.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> showAllGROUPlist(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map = iDataRight.showAllGROUPlist();

    } catch (Exception e) {

      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/findAllSysOrgtypes.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> findAllSysOrgtypes(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List orgList = isysOrgtype.findAllSysOrgtypes();
      map.put("orgTypes", orgList);
    } catch (Exception e) {

      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/getEleByConditionAsObjxs.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> getEleByConditionAsObj(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String orgType = (String) request.getAttribute("org_type");
      if (orgType == null) {
        orgType = request.getParameter("orgType");
      }
      if ("001".equals(orgType)) {
        return null;
      }
      String userGuid = request.getParameter("userGuid");
      List orgTypes = null;
      List selectedorgTypes = null;
      if (orgType != null) {
        String eleCode = isysOrgtype.findEleCodeByOrgtypeCode(orgType);
        String sql = "order by chr_code";
        orgTypes = elementOperation.getEleByConditionAsObj(eleCode, null, false, "", null, sql, FAssistObjectDTO.class);

        selectedorgTypes = isysOrgtype.findSelectedOrgtypesByUserId(userGuid);

      }
      map.put("orgTypes", orgTypes);
      map.put("selectedorgTypes", selectedorgTypes);
    } catch (Exception e) {

      e.printStackTrace();
    }
    return map;
  }
}
