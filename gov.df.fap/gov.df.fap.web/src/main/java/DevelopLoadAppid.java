import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import com.longtu.framework.exception.AppException;
import com.longtu.framework.rpcfw.mapper.ObjectWriter;

public class DevelopLoadAppid extends AbstractLoadAppid {
  private AbstractLoadAppid loadAppid = null;

  public AbstractLoadAppid getLoadAppid() {
    return this.loadAppid;
  }

  public void setLoadAppid(AbstractLoadAppid loadAppid) {
    this.loadAppid = loadAppid;
  }

  protected List<CommonDTO> loadAppid2DB(DataSource ds) throws AppException {
    List<CommonDTO> list = new ArrayList<CommonDTO>();
    try {
      list = super.loadAppid2DB(ds);
    } catch (Exception localException1) {
    }
    Properties ps = new Properties();
    try {
      ps.load(super.getClass().getResourceAsStream("/develop.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Map m2 = new HashMap();
    for (CommonDTO dto : list) {
      m2.put(dto.get("appid"), dto);
    }

    List ret = new ArrayList();
    Set keys = ps.keySet();
    for (Iterator localIterator2 = keys.iterator(); localIterator2.hasNext();) {
      Object key = localIterator2.next();
      String v = (String) ps.get(key);
      String app = (String) key;
      ObjectWriter writer = new ObjectWriter();
      try {
        Map m = (Map) writer.getObjectWithOutStructure(v);
        CommonDTO c = new CommonDTO();
        c.put("appid", app);
        c.putAll(m);
        if ((c.get("year") == null) || (c.get("year").toString().trim().length() == 0)) {
          c.put("year", "9999");
        }
        if ((c.get("province") == null) || (c.get("province").toString().trim().length() == 0)) {
          c.put("province", "999999");
        }
        m2.remove(app);
        if (c.getString("rootpath") == null) {
          c.put("rootpath", "");
        }
        ret.add(c);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    ret.addAll(m2.values());
    return ret;
  }

  public String getDomainName() {
    return this.loadAppid.getDomainName();
  }

  public Map<String, String> getServerInfo() {
    return this.loadAppid.getServerInfo();
  }

  public String getServerArguments() {
    return this.loadAppid.getServerArguments();
  }
}
