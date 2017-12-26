package gov.df.fap.service.userparaConfig;

import gov.df.fap.api.userparaConfig.IUserparaConfigService;
import gov.df.fap.service.util.dao.GeneralDAO;
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
public class UserparaConfigService implements IUserparaConfigService {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  public Map<String, Object> dataQuery(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String checkitem = request.getParameter("checkItems");
    String sys_id = request.getParameter("sys_id");
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    StringBuffer sqlbuffer = new StringBuffer();
    String[] check = checkitem.split("@");
    sqlbuffer
      .append("select p.chr_id,p.chr_code,p.chr_name,p.chr_value,p.chr_desc, a.sys_id || ' ' || a.sys_name as sys_name from sys_userpara p  left join sys_app a  on p.sys_id = a.sys_id where p.IS_VISIBLE = 1  and p.sys_id =? ");
    sqlbuffer.append("   and p.rg_code = ? and p.set_year = ?");
    sqlbuffer.append("  and (1 = 2");
    for (int i = 0; i < check.length; i++) {
      sqlbuffer.append(" or  p.is_edit ='");
      sqlbuffer.append(check[i]);
      sqlbuffer.append("' ");
    }
    sqlbuffer.append(" )  order by p.chr_code");
    List list = generalDAO.findBySql(sqlbuffer.toString(), new Object[] { sys_id, rg_code, year });
    map.put("dataDetail", list);
    return map;
  }

  public Map<String, Object> updatePara(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String chr_id = request.getParameter("chr_id");
    String chr_code = request.getParameter("chr_code");
    String chr_name = request.getParameter("chr_name");
    String chr_value = request.getParameter("chr_value");
    String sql = "update sys_userpara set chr_code = ? ,chr_name = ? , chr_value =? where chr_id = ? ";
    int num = generalDAO.executeBySql(sql, new Object[] { chr_code, chr_name, chr_value, chr_id });
    map.put("num", num);
    return map;
  }

  public Map<String, Object> initpara(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String sql = "SELECT t.sys_id , t.sys_name FROM SYS_APP t where t.enabled = '1' order by t.sys_id";
    List list = generalDAO.findBySql(sql, new Object[] {});
    map.put("lilist", list);
    return map;
  }
}
