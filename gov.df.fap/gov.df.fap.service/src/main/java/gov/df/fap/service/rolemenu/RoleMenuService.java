package gov.df.fap.service.rolemenu;

import gov.df.fap.api.menuEdit.ImenueditService;
import gov.df.fap.api.roleMenu.IRoleMenuService;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuService implements IRoleMenuService {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  @Autowired
  private ImenueditService imenueditService;

  public Map<String, Object> initTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String sql = "select t.sys_id id, " + SQLUtil.replaceLinkChar("t.sys_id || ' ' || t.sys_name") + " as  name, '0' pid from sys_app t order by t.sys_id ";
    List list = generalDAO.findBySql(sql, new Object[] {});
    String rolesql = "select t.role_id id, " + SQLUtil.replaceLinkChar("t.role_code || ' ' || t.role_name") + " as   name , t.user_sys_id  pid  from sys_role t where t.enabled = '1' and t.rg_code = ? and t.set_year = ?";
    List list2 = generalDAO.findBySql(rolesql, new Object[] { rg_code, year });
    String sql1 = "select t.menu_id , " + SQLUtil.replaceLinkChar("t.menu_code||' '||t.menu_name") + " as  menu_name, t.parent_id ,t.user_sys_id from sys_menu t where t.enabled = '1' order by t.menu_code,t.disp_order";
    String appsql = "select t.sys_id menu_id , " + SQLUtil.replaceLinkChar("t.sys_id||' '||t.sys_name") + " as  menu_name , '0' parent_id ,t.sys_id user_sys_id  from sys_app t where t.enabled = '1' order by t.sys_id";
    List list1 = generalDAO.findBySql(sql1, new Object[] {});
    List list12 = generalDAO.findBySql(appsql, new Object[] {});
    list1.addAll(list12);
    Map map1 = new HashMap();
    map1.put("id", "0");
    map1.put("name", "角色");
    map1.put("pid", "");
    list.add(map1);
    list.addAll(list2);
    map.put("roleDetail", list);
    map.put("dataDetail", list1);
    return map;
  }

  public Map<String, Object> TreeChecked(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String roleId = request.getParameter("roleid");
    String checkSql = " select t.menu_id from sys_role_menu t where t.role_id = ? and t.set_year = ? and t.rg_code = ?  ";
    List list = generalDAO.findBySql(checkSql, new Object[] { roleId, year, rg_code });
    map.put("dateDetail", list);
    return map;
  }

  public Map<String, Object> updateRolemenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String roleId = request.getParameter("roleid");
    String menuCheck = request.getParameter("menu_id");
    String checked = request.getParameter("checked");
    String delSql = " delete from sys_role_menu where role_id = ? and menu_id= ? and set_year = ? and rg_code = ? ";
    //    int a = generalDAO.executeBySql(delSql, new Object[] { roleId, year, rg_code });
    String inserSql = "insert into sys_role_menu( role_id,menu_id,set_year,last_ver,rg_code)values(?,?,?,"+SQLUtil.getSysdateToCharSql()+",?)";
    if ("".equals(menuCheck)) {
      map.put("flag", "1");
      return map;
    }
    String[] menuArray = menuCheck.split(",");
    if ("1".equals(checked)) {
      for (int i = 0; i < menuArray.length; i++) {
        generalDAO.executeBySql(inserSql, new Object[] { roleId, menuArray[i], year, rg_code });
      }
    } else {
      for (int i = 0; i < menuArray.length; i++) {
        generalDAO.executeBySql(delSql, new Object[] { roleId, menuArray[i], year, rg_code });
      }
    }
    map.put("flag", "1");
    return map;
  }
}
