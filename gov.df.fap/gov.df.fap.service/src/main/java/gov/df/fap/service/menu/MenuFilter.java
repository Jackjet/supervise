package gov.df.fap.service.menu;

import gov.df.fap.api.fapcommon.IMenuDfService;
import gov.df.fap.api.fapcommon.IRoleDfCommonService;
import gov.df.fap.api.menu.IBtnService;
import gov.df.fap.api.menu.IMenuFilter;
import gov.df.fap.api.menu.IStatusService;
import gov.df.fap.service.util.dao.GeneralDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MenuFilter implements IMenuFilter {

  @Autowired
  private IRoleDfCommonService iRoleService;

  @Autowired
  private IMenuDfService iMenuService;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  @Autowired
  private IStatusService statusService;

  @Autowired
  private IBtnService iBtnService;

  public Map<String, Object> getMenuByRole(HttpServletRequest request, HttpServletResponse response) {

    String roleId = request.getParameter("guid");
    List menulist = new ArrayList();
    try {
      menulist = iRoleService.getMenus(roleId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("mapMenu", menulist);
    return map;

  }

  public Map<String, Object> getAllMenu(HttpServletRequest request, HttpServletResponse response) {

    String roleId = request.getParameter("guid");
    List menulist = new ArrayList();
    try {
      String sql = " appid='df'  order by menuorder";
      menulist = iMenuService.getMenusTreeByWhereSql(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("mapMenu", menulist);
    List list = statusService.getAllStatus();
    Map map1 = iBtnService.BtnTree(request, response);
    map.put("status", list);
    map.putAll(map1);
    return map;

  }

}
