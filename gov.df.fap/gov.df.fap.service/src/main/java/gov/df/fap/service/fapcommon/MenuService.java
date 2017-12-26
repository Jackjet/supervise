package gov.df.fap.service.fapcommon;

import gov.df.fap.api.fapcommon.IMenuDfService;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.util.factory.ServiceFactory;
import gov.mof.fasp2.ca.menu.service.IMenuService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;

import com.longtu.framework.exception.AppException;

@Service
public class MenuService implements IMenuDfService {

  private IMenuService iMenuService = null;

  @Autowired
  private AbstractRefreshableWebApplicationContext abstractRefreshableWebApplicationContext;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  private static String starttype = "df";

  public MenuService() {

  }

  public List getMenusTreeByWhereSql(String whereSql) {
    List list = new ArrayList();
    starttype = abstractRefreshableWebApplicationContext.getServletContext().getInitParameter("startup");
    if ("fasp2.0".equals(starttype)) {
      iMenuService = (IMenuService) ServiceFactory.getBean("fasp2.ca.menu.service");
    }
    if ("df".equals(starttype)) {
      StringBuffer sql = new StringBuffer();
      sql.append("select * from (");
      sql
        .append("select t.menu_id guid , t.parent_id parentid , " + SQLUtil.replaceLinkChar("t.menu_code ||' '|| t.menu_name") + " as name , 'df' appid ,t.is_leaf isleaf, t.disp_order menuorder from sys_menu t where t.ENABLED=1 ");
      sql.append(" ) a ");
      if (!"".equals(whereSql) && whereSql != null) {
        sql.append(" where ");
        sql.append(whereSql);
      }
      list = generalDAO.findBySql(sql.toString());
      String appsql = "select t.sys_id guid , " + SQLUtil.replaceLinkChar("t.sys_id||' '||t.sys_name") + " as  name , '0' parentid ,t.sys_id user_sys_id,'0' isleaf  from sys_app t order by t.sys_id";
      List list2 = generalDAO.findBySql(appsql);
      list.addAll(list2);
    } else if ("fasp2.0".equals(starttype)) {
      try {
        list = iMenuService.getMenusByWhereSql(whereSql);
      } catch (AppException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return list;
  }

  @Override
  public List getMenuByCode(String menuCode) {
    starttype = abstractRefreshableWebApplicationContext.getServletContext().getInitParameter("startup");
    if ("fasp2.0".equals(starttype)) {
      iMenuService = (IMenuService) ServiceFactory.getBean("fasp2.ca.menu.service");
    }
    String whereSql = "";
    if ("df".equals(starttype)) {
      whereSql = " menu_code='" + menuCode + "'";
    } else if ("fasp2.0".equals(starttype)) {
      whereSql = " code='" + menuCode + "'";
    } else {
      whereSql = " 1=0 ";
    }
    return getMenusTreeByWhereSql(whereSql);
  }
}
