package gov.df.fap.controller.wf.activiti;

import gov.df.fap.api.menu.IMenuFilter;
import gov.df.fap.api.role.IRoleDfService;
import gov.df.fap.api.workflow.activiti.design.IGetModuleAndRoleTreeData;
import gov.df.fap.api.workflow.activiti.design.IWFRuleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

/**
 * @author chouhl
 * @param <V>
 * @param <K>
 * 
 */
@Controller
public class UBPMUserTreeRefCtrl<V, K> extends UBPMBaseRefCtrl {

  @Autowired
  private IGetModuleAndRoleTreeData iGetModuleAndRoleTreeData;

  @Autowired
  private IMenuFilter iMenuFilter;

  @Autowired
  private IRoleDfService iRoleDfService;

  @Autowired
  private IWFRuleService iWFRuleService;

  /**
   * 加载菜单树
   * 
   * @param data
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/df/service/reference/tree/module", method = RequestMethod.GET)
  public @ResponseBody
  String getMenu_Post(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String mId = "";
    String parentid = "pId";
    String guid = "guid";
    String name = "name";
    String isleaf = "isleaf";
    String isParent = "isParent";
    String code = "code";
    int count = 0;
    JSONArray jsonArray = new JSONArray();
    List<Map> list = new ArrayList<Map>();
    List<Map> li = new ArrayList<Map>();
    if (null == mId || "".equals(mId)) {
      Map allMenu = iMenuFilter.getAllMenu(request, response);
      List<Map> list2 = (List<Map>) allMenu.get("mapMenu");
      for (Map map : list2) {
        String code_ = (String) map.get(code);
        code_ = code_ == null ? "" : code_;
        String name_ = (String) map.get(name);
        String pid_ = (String) map.get("parentid");
        String id_ = (String) map.get(guid);

        String isleaf_ = String.valueOf(map.get(isleaf));
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(parentid, pid_);
        hashMap.put(guid, id_);
        hashMap.put(name, code_ + " " +name_);
        hashMap.put(isleaf, isleaf_);
        hashMap.put(isParent, "true");

        list.add(hashMap);
      }

      //过滤2
      Iterator<Map> iterator = list.iterator();
      while (iterator.hasNext()) {

        Map next = iterator.next();
        if ("faspmenu0700".equals(next.get(parentid))) {
          iterator.remove();
        }

      }

      for (Map<String, String> map : list) {
        // TODO 现根据是否是叶子节点（isleaf）判断 isParent
        String flag = (String) map.get(isleaf);
        if ("1".equals(flag)) {
          map.put(isParent, "false");
        }
        count++;
        map.put("pk", map.get(guid));
        map.put("id", map.get(guid));
        map.put("nocheck", "false");
        map.put("canselect", "true");
        map.put("user_guid", map.get(guid));
        jsonArray.add(map);
        map.put("totalCount", String.valueOf(count));

      }
    }
    return jsonArray.toString();
  }

  /**
   * 加载角色树
   * 
   * @param data
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/df/service/reference/tree/role", method = RequestMethod.GET)
  public @ResponseBody
  String getRole_Post(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String mId = "";
    int count = 0;
    String guid = "guid";
    String name = "name";
    JSONArray jsonArray = new JSONArray();
    if (null == mId || "".equals(mId)) {
      Map<String, Object> allRole = iRoleDfService.getAllRole();
      List<Map> list = (List) allRole.get("rolelist");
      for (Map map : list) {
        count++;
        map.put(name, map.get(name));
        map.put("pk", map.get(guid));
        map.put("id", map.get(guid));
        map.put("pId", map.get("roletype"));
        map.put("nocheck", "false");
        map.put("canselect", "true");
        map.put("user_guid", map.get(guid));
        jsonArray.add(map);
        map.put("totalCount", String.valueOf(count));
      }
    }
    return jsonArray.toString();
  }

  /**
   * 加载规则树
   * 
   * @param data
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/df/service/reference/tree/rule", method = RequestMethod.GET)
  public @ResponseBody
  String getRule_Post(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return iWFRuleService.getTreeStr();
  }
}
