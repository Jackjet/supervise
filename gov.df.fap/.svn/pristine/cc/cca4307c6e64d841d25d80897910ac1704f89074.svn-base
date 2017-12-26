package gov.df.fap.service.util.log;

import gov.df.fap.api.util.interfaces.IFindLog;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("sys.userLogFinderService")
public class UserLogFinderBO implements IFindLog {
  private static Logger log = Logger.getLogger(UserLogFinderBO.class);

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao = null;

  public List findLog() {
    String sql = "select u.user_name,r.role_name,s.user_ip,app.sys_name,s.login_date oper_time "
      + "from sys_session s left join sys_usermanage u on s.user_id=u.user_id "
      + "left join sys_role r on s.role_id=r.role_id "
      + "left join sys_app app on s.sys_id=app.sys_id where s.logout_date is null";
    List list = dao.findBySql(sql);
    return list;
  }

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  public String generateNumberBySeq(String seq) {
    String sql = "select "+SQLUtil.getSeqExpr(seq)+" id from dual";

    List list = null;
    try {
      list = dao.findBySql(sql);

      if (((XMLData) list.get(0)).get("id").toString() != null) {
        return ((XMLData) list.get(0)).get("id").toString();
      }

    } catch (Exception e) {
      e.printStackTrace();
      log.error("取得Sequence" + seq + "错误:" + e.getMessage());
    }
    return null;
  }
}
