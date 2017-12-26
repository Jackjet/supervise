package gov.df.fap.service.roleConfig;

import gov.df.fap.api.roleConfig.IRoleConfigService;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RoleConfigService implements IRoleConfigService {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  public Map<String, Object> initRoleTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String sql = "select t.sys_id , " + SQLUtil.replaceLinkChar("t.sys_id || ' ' || t.sys_name") + " as  sys_name, '0' pid from sys_app t order by t.sys_id ";
    List list = generalDAO.findBySql(sql, new Object[] {});
    List list2 = list;
    Map map1 = new HashMap();
    map1.put("sys_id", "0");
    map1.put("sys_name", "角色");
    map1.put("pid", "");
    list.add(map1);
    map.put("dataDetail", list);
    map.put("selectDetail", list2);
    return map;
  }

  public Map<String, Object> queryRole(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String user_sys_id = request.getParameter("sys_id");
    String sql = "";
    List list = null;
    if (!"0".equals(user_sys_id)) {
      sql = "select t.role_id,t.role_code,t.role_name,t.enabled , a.sys_id , a.sys_id||' ' ||a.sys_name sys_name from sys_role t , sys_app a where t.user_sys_id = a.sys_id and t.rg_code = ? and t.set_year = ? and t.user_sys_id = ? order by t.role_code";
      list = generalDAO.findBySql(sql, new Object[] { rg_code, year, user_sys_id });
    } else {
      sql = "select t.role_id,t.role_code,t.role_name,t.enabled , a.sys_id , a.sys_id||' ' ||a.sys_name sys_name from sys_role t , sys_app a where t.user_sys_id = a.sys_id and t.rg_code = ? and t.set_year = ? order by t.role_code";
      list = generalDAO.findBySql(sql, new Object[] { rg_code, year });
    }

    map.put("dataDetail", list);
    return map;
  }

  public Map<String, Object> saveRole(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String role_id = getNumberBySeq("SEQ_SYS_FRAME_ID");
    String role_code = request.getParameter("role_code");
    String role_name = request.getParameter("role_name");
    String user_sys_id = request.getParameter("user_sys_id");
    String enabled = request.getParameter("enabled");
    String sql = " select 1 from sys_role where  role_code = ? and rg_code =? and set_year=? ";
    List list1 = generalDAO.findBySql(sql, new Object[] { role_code, rg_code, year });
    if (list1.size() > 0) {
      map.put("flag", "0");
      map.put("message", "编码已存在");
      return map;
    }
    String insertsql = " insert into sys_role(role_id,role_code,role_name,user_sys_id,enabled,rg_code,set_year)  values (?,?,?,?,?,?,?) ";
    int num = generalDAO.executeBySql(insertsql, new Object[] { role_id, role_code, role_name, user_sys_id, enabled,
      rg_code, year });
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> updateRole(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String role_id = request.getParameter("role_id");
    String role_code = request.getParameter("role_code");
    String role_name = request.getParameter("role_name");
    String user_sys_id = request.getParameter("user_sys_id");
    String enabled = request.getParameter("enabled");
    String updateSql = "update sys_role set role_code = ? , role_name = ? ,user_sys_id = ? ,enabled = ? where role_id = ?";
    int num = generalDAO.executeBySql(updateSql, new Object[] { role_code, role_name, user_sys_id, enabled, role_id });
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> delRole(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String role_id = request.getParameter("role_id");
    String[] roleall = role_id.split("@");
    String sql = "delete from sys_role where role_id = ?";
    int cout = 0;
    for (int k = 0; k < roleall.length; k++) {
      int num = generalDAO.executeBySql(sql, new Object[] { roleall[k] });
      cout++;
    }
    map.put("cout", cout);
    map.put("flag", "1");
    return map;
  }

  /**
   * 角色新增--序列
   * 
   */
  private String getNumberBySeq(String seq) {
    String sql = "select " + SQLUtil.getSeqExpr(seq) + " as id from dual";

    List list = null;
    try {
      list = generalDAO.findBySql(sql);

      if (((XMLData) list.get(0)).get("id").toString() != null) {
        return ((XMLData) list.get(0)).get("id").toString();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
