package gov.df.supervise.service.user;

import gov.df.supervise.bean.user.depDTO;
import gov.df.supervise.bean.user.officeDTO;
import gov.df.supervise.service.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface userDao {
  public officeDTO queryOfficeByOrgCode(String oid);

  public depDTO queryDepByBelongOrg(String dep_id);

  public List getMenu(Map<String, Object> param);
}
