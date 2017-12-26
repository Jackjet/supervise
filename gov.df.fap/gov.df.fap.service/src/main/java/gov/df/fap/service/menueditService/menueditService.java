package gov.df.fap.service.menueditService;

import gov.df.fap.api.menuEdit.ImenueditService;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.datasource.TypeOfDB;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class menueditService implements ImenueditService {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  public Map<String, Object> initMenuTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String sql = "select t.menu_id , " + SQLUtil.replaceLinkChar("t.menu_code||' '||t.menu_name")
      + " as menu_name, t.parent_id ,t.user_sys_id from sys_menu t order by t.menu_code,t.disp_order";
    String appsql = "select t.sys_id menu_id , " + SQLUtil.replaceLinkChar("t.sys_id||' '||t.sys_name")
      + " as menu_name , '0' parent_id ,t.sys_id user_sys_id  from sys_app t order by t.sys_id";
    List list = generalDAO.findBySql(sql, new Object[] {});
    List list2 = generalDAO.findBySql(appsql, new Object[] {});
    list.addAll(list2);
    String sql1 = "select t.sys_id , " + SQLUtil.replaceLinkChar("t.sys_id || ' ' || t.sys_name")
      + " as  sys_name, '0' pid from sys_app t order by t.sys_id ";
    List list3 = generalDAO.findBySql(sql1, new Object[] {});

    map.put("dataDetail", list);
    map.put("selectDetail", list3);
    return map;
  }

  public Map<String, Object> queryGrid(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String pid = request.getParameter("menu_id");
    String sql = "select t.menu_id ,t.menu_code,t.menu_name,t.url ,t.enabled ,t.disp_order,t.user_sys_id ,t.tips, t.parent_id , t.menu_parameter  ,a.sys_name ,t.is_leaf from sys_menu t , sys_app a  where t.user_sys_id =a.sys_id and t.parent_id = ? order by t.disp_order ";
    List list = generalDAO.findBySql(sql, new Object[] { pid });
    map.put("dataDetail", list);
    return map;
  }

  public Map<String, Object> insertMenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String sql2 = "select count(1) a from sys_menu where menu_code = ?";
    String menu_code = request.getParameter("menu_code");
    String menu_id = null;
    List list2 = generalDAO.findBySql(sql2, new Object[] { menu_code });
    if (list2.size() > 0) {
      Map map2 = (Map) list2.get(0);
      String k = (String) map2.get("a");
      if (!"0".equals(k)) {
        map.put("flag", "0");
        return map;
      }
    }

    if (menu_code.startsWith("0")) {
      menu_id = "1" + menu_code;
    } else {
      menu_id = menu_code;
    }
    String menu_name = request.getParameter("menu_name");
    String url = request.getParameter("url");
    String enabled = request.getParameter("enabled");
    String tips = request.getParameter("tips");
    String parent_id = request.getParameter("parentid");
    String menu_parameter = request.getParameter("menu_parameter");
    String user_sys_id = request.getParameter("user_sys_id");
    String disp_order = request.getParameter("disp_order");
    String lvnum = "1";
    String isleaf = "1";
    String lvnumsql = "select t.level_num+1 a  from sys_menu t where t.menu_id = ?";
    List list1 = generalDAO.findBySql(lvnumsql, new Object[] { parent_id });
    if (list1.size() > 0) {
      Map map2 = (Map) list1.get(0);
      lvnum = (String) map2.get("a");
    }
    String sql = "insert into sys_menu  (menu_id,menu_code,menu_name,url,enabled,tips,parent_id,menu_parameter,user_sys_id,last_ver,disp_order,level_num,is_leaf)"
      + " values (?,?,?,?,?,?,?,?,?,"
      + (TypeOfDB.isOracle() ? "to_char(sysdate,'yyyy:mi:ss')" : "DATE_FORMAT(sysdate(),'%Y:%i:%s')") + ",?,?,?)";
    int num = generalDAO.executeBySql(sql, new Object[] { menu_id, menu_code, menu_name, url, enabled, tips, parent_id,
      menu_parameter, user_sys_id, disp_order, lvnum, isleaf });
    map.put("flag", "1");
    map.put("pid", parent_id);
    map.put("id", menu_id);
    return map;
  }

  public Map<String, Object> updateMenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String menu_id = request.getParameter("menu_id");
    String menu_code = request.getParameter("menu_code");
    String menu_name = request.getParameter("menu_name");
    String menu_oldcode = request.getParameter("menu_oldcode");
    String url = request.getParameter("url");
    String enabled = request.getParameter("enabled");
    String tips = request.getParameter("tips");
    String parent_id = request.getParameter("parentid");
    String menu_parameter = request.getParameter("menu_parameter");
    String user_sys_id = request.getParameter("user_sys_id");
    String disp_order = request.getParameter("disp_order");
    String oldparentid = request.getParameter("oldparentid");
    String isleaf = request.getParameter("is_leaf");

    String lvnum = "1";
    if (!parent_id.equals(oldparentid)) {
      String pidsql = (TypeOfDB.isOracle() ? "select 1 from (select * from sys_menu t start with t.menu_id = ? "
        + " connect by prior t.parent_id = t.menu_id ) a where a.menu_id = ?   "
        : "select 1 from (SELECT @id:= CONCAT( @id, '$@', t.parent_id, '$@') ids, t.* "
          + " FROM (SELECT @id := CONCAT('$@', ?, '$@')) idt, (select * from sys_menu order by level_num desc) t "
          + " where POSITION(CONCAT('$@',t.menu_id, '$@') IN @id) > 0 ) a where a.menu_id = ? ");
      List list = generalDAO.findBySql(pidsql, new Object[] { parent_id, menu_id });
      if (list.size() > 0) {
        map.put("flag", "0");
        map.put("message", "不可设置该菜单为父级");
        return map;
      }
      if (!"".equals(parent_id) && parent_id != null) {
        String selsql = " select t.level_num , t.is_leaf from sys_menu t  where t.menu_id = ?  ";
        List list2 = generalDAO.findBySql(selsql, new Object[] { parent_id });
        if (list.size() > 0) {
          Map map2 = (Map) list.get(0);
          String is_leaf = (String) map2.get("is_leaf");
          if ("1".equals(is_leaf)) {
            String upsql = "update  sys_menu t set t.is_leaf = '0' where t.menu_id = ?";
            generalDAO.executeBySql(upsql, new Object[] { parent_id });
          }
        }
      }

      if (!"".equals(oldparentid) && oldparentid != null) {
        String selsql = " select 1 from sys_menu t  where t.menu_id = ?  ";
        List list2 = generalDAO.findBySql(selsql, new Object[] { oldparentid });
        if (list.size() == 0) {
          String upsql = "update  sys_menu t set t.is_leaf = '1' where t.menu_id = ?";
          generalDAO.executeBySql(upsql, new Object[] { oldparentid });
        }
      }
    }

    if (!menu_code.equals(menu_oldcode)) {
      String sql2 = "select count(1) a from sys_menu where menu_code = ?";
      List list2 = generalDAO.findBySql(sql2, new Object[] { menu_code });
      if (list2.size() > 0) {
        Map map2 = (Map) list2.get(0);
        String k = (String) map2.get("a");
        if (!"0".equals(k)) {
          map.put("flag", "0");
          map.put("message", "编码已存在");
          return map;
        }
      }
    }
    String lvnumsql = "select t.level_num+1 a  from sys_menu t where t.menu_id = ?";
    List list1 = generalDAO.findBySql(lvnumsql, new Object[] { parent_id });
    if (list1.size() > 0) {
      Map map2 = (Map) list1.get(0);
      lvnum = (String) map2.get("a");
    }
    String delsql = "delete sys_menu t where t.menu_id = ?";
    generalDAO.executeBySql(delsql, new Object[] { menu_id });
    String sql = "insert into sys_menu  (menu_id,menu_code,menu_name,url,enabled,tips,parent_id,menu_parameter,user_sys_id,last_ver,disp_order,level_num,is_leaf)"
      + " values (?,?,?,?,?,?,?,?,?,"
      + (TypeOfDB.isOracle() ? "to_char(sysdate,'yyyy:mi:ss')" : "DATE_FORMAT(sysdate(),'%Y:%i:%s')") + ",?,?,?)";
    int num = generalDAO.executeBySql(sql, new Object[] { menu_id, menu_code, menu_name, url, enabled, tips, parent_id,
      menu_parameter, user_sys_id, disp_order, lvnum, isleaf });
    String sysUpsql = "update sys_menu a set a.user_sys_id = ? where a.menu_id in ("
      + (TypeOfDB.isOracle() ? "select t.menu_id from sys_menu t start with t.menu_id = ? connect by  prior t.menu_id =  t.parent_id"
        : " select menu_id from (SELECT case when @id:= CONCAT( @id, '$@', t.menu_id, '$@') then  t.menu_id else t.menu_id end menu_id "
          + " FROM (SELECT @id := CONCAT('$@', ?, '$@')) idt, (select * from sys_menu order by level_num asc) t "
          + " where POSITION(CONCAT('$@',t.menu_id, '$@') IN @id) > 0 or POSITION(CONCAT('$@',t.parent_id, '$@') IN @id) > 0 ) t )");
    generalDAO.executeBySql(sysUpsql, new Object[] { user_sys_id, menu_id });
    map.put("flag", "1");
    map.put("pid", parent_id);
    map.put("id", menu_id);
    return map;
  }

  public Map<String, Object> delMenu(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String sql2 = "select count(1) from sys_menu where parent_id = ?";
    String menu_id = request.getParameter("menu_id");
    List list1 = generalDAO.findBySql(sql2, new Object[] { menu_id });
    if (list1.size() > 0) {
      Map map2 = (Map) list1.get(0);
      String num = (String) map2.get("a");
      if ("0".equals(num)) {
        map.put("flag", "0");
        return map;
      }
    }
    String[] menuall = menu_id.split("@");
    String sql = "delete sys_menu where menu_id = ?";
    int cout = 0;
    for (int k = 0; k < menuall.length; k++) {
      int num = generalDAO.executeBySql(sql, new Object[] { menuall[k] });
      cout++;
    }
    map.put("cout", cout);
    map.put("flag", "1");
    return map;
  }

  public Map<String, Object> parenttree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String user_sys_id = request.getParameter("user_sys_id");
    String sys_name = "";
    try {
      sys_name = URLDecoder.decode(request.getParameter("sys_name"), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String sql = "select t.menu_id chr_id ,t.parent_id ,"
      + SQLUtil.replaceLinkChar("t.menu_code||' '|| t.menu_name")
      + " codename from "
      + (TypeOfDB.isOracle() ? "sys_menu t where 1=1 start with t.parent_id =? connect by  t.parent_id = prior t.menu_id "
        : "(SELECT @id:= CONCAT( @id, '$@', t.menu_id, '$@') ids, t.* "
          + " FROM (SELECT @id := CONCAT('$@', ?, '$@')) idt, (select * from sys_menu order by level_num asc) t "
          + " where POSITION(CONCAT('$@',t.menu_id, '$@') IN @id) > 0 or POSITION(CONCAT('$@',t.parent_id, '$@') IN @id) > 0 ) t ");
    List list = generalDAO.findBySql(sql, new Object[] { user_sys_id });
    Map map1 = new HashMap();
    map1.put("chr_id", user_sys_id);
    map1.put("parent_id", "0");
    map1.put("codename", sys_name);
    list.add(map1);
    map.put("datadetail", list);
    return map;
  }
}
