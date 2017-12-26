package gov.df.fap.service.changergyear;

import gov.df.fap.api.changergyear.ChangeRgcodeSetyearInterface;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ChangeRgcodeSetyearBO implements ChangeRgcodeSetyearInterface {
  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  @Override
  public Map ChangeRgcodeSetyear() {
    String rg_code = SessionUtil.getRgCode();
    String set_year = SessionUtil.getLoginYear();
    int a = dao.changeRgYear(rg_code, set_year);
    Map map = new HashMap();
    map.put("flag", a);
    return map;

  }

  @Override
  public Map<String, Object> getRgcodeSetyear() {
    Map map = new HashMap();
    map.put("rgcode", SessionUtil.getRgCode());
    map.put("setyear",SessionUtil.getLoginYear());
    return map;
  }

}
