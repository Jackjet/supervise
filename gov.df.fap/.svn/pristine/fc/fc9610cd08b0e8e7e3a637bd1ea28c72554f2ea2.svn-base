import factory.SqlFactory;
import gov.df.fap.util.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.longtu.framework.exception.AppException;

public abstract class AbstractLoadAppid implements ILoadAppid {
  public static Logger logger = Logger.getLogger(AbstractLoadAppid.class);

  private Collection<String> appids = null;

  private final Collection<CommonDTO> myappdto = new ArrayList();

  private static final String DOMAINMARK = "domainmark";

  private String rootpath = "";

  private String crootpath = "";

  private String guid = null;

  private String serverGuid = StringUtil.createUUID();

  private final Map<String, Map<String, CommonDTO>> dtos = new HashMap();

  private final Map<String, CommonDTO> defdtos = new HashMap();

  private boolean isCommon = false;

  private static final RowMapper rm = new RowMapper() {
    public Object mapRow(ResultSet rs, int arg1) throws SQLException {
      CommonDTO dto = new CommonDTO();

      dto.put("appid", rs.getString("appid"));
      dto.put("domainip", rs.getString("domainip"));
      dto.put("domainport", rs.getString("domainport"));
      dto.put("domainmark", rs.getString("domainmark"));
      // dto.put("appname", rs.getString("appname"));
      dto.put("appname", "");
      String isLocal = rs.getString("islocal");
      dto.put("islocal", isLocal);

      String appid = rs.getString("appid");
      try {
        rs.getString("datatype");
      } catch (Exception localException1) {
      }
      String dsGuid = "";
      String dsName = "";
      try {
        dsGuid = rs.getString("dsguid");
        dsName = rs.getString("dataname");
      } catch (Exception localException2) {
        dsGuid = "";
        dsName = "";
      }

      dto.put("dsname", dsName);
      dto.put("dsguid", dsGuid);

      if (((isLocal != null) && (isLocal.equals("1"))) || (appid.equals("xch")) || (appid.equals("fuds"))) {
        Map map = DBDetector.getJNDIInfo();
        String dbUrl = (String) map.get("url");

        int index = dbUrl.indexOf("@");
        if (index != -1) {
          dbUrl = dbUrl.substring(index + 1);
        }

        if (!(dbUrl.contains("/"))) {
          String[] parts = dbUrl.split(":");
          if (parts.length == 3) {
            dbUrl = parts[0] + ":" + parts[1] + "/" + parts[2];
          }
        }
        dbUrl = dbUrl + ":" + ((String) map.get("userName"));
        dto.put("dsinfo", dbUrl.toUpperCase());
      } else {
        try {
          String dsinfo = rs.getString("dataip") + ":" + rs.getString("dataport");
          dsinfo = dsinfo + "/" + rs.getString("dataschme");
          dsinfo = dsinfo + ":" + rs.getString("datausername");
          if (!(dsinfo.contains("null")))
            dto.put("dsinfo", dsinfo.toUpperCase());
        } catch (Exception localException3) {
          dto.put("dsinfo", "");
        }
      }
      try {
        dto.put("partitiontype", rs.getString("partitiontype"));
      } catch (Exception localException4) {
        dto.put("partitiontype", "");
      }
      try {
        dto.put("year", (rs.getString("year") == null) ? "9999" : rs.getString("year"));
        dto.put("province", (rs.getString("province") == null) ? "999999" : rs.getString("province"));
      } catch (Exception localException5) {
        System.out.println("版本需要升级");
        dto.put("year", "9999");
        dto.put("province", "999999");
      }
      try {
        dto.put("rootpath", (rs.getString("rootpath") == null) ? "" : rs.getString("rootpath"));
      } catch (Exception localException6) {
        System.out.println("版本需要升级");
      }
      return dto;
    }
  };

  private static final RowMapper rmJudge = new RowMapper() {
    public Object mapRow(ResultSet rs, int arg1) throws SQLException {
      return rs.getString("column_name");
    }
  };

  private boolean appupgrade = false;

  private String dbguid = null;

  private static String zkServerAddress = null;

  private static Boolean zkServerStartup = null;

  public String getRootPath() {
    return this.rootpath;
  }

  public String getContentRootPath() {
    return this.crootpath;
  }

  public void setContentRootPath(String crootpath) {
    this.crootpath = crootpath;
    if (this.guid == null) {
      StringBuffer bs = new StringBuffer();
      for (String s : this.appids) {
        bs.append(s);
      }
      bs.append(crootpath);
    }
  }

  public Map<String, Map<String, CommonDTO>> getAppCommons() {
    return this.dtos;
  }

  public Map<String, CommonDTO> getAllApp() {
    return this.defdtos;
  }

  public CommonDTO getDTOByAppid(String appid) {
    if ((this.defdtos == null) || (this.defdtos.size() == 0))
      return null;
    if ((appid.equals("common")) && (this.defdtos.get(appid) == null)) {
      appid = "fasp";
    }
    return ((CommonDTO) this.defdtos.get(appid));
  }

  public Collection<CommonDTO> getAppDTO() {
    return this.myappdto;
  }

  public CommonDTO getDTOByAppid(String appid, String year, String province) {
    logger.debug("主域加载项:::::::获取财年财政信息");
    if ((this.dtos == null) || (this.dtos.size() == 0))
      return null;
    if ((year == null) || (year.trim().length() == 0)) {
      year = "9999";
    }
    if ((province == null) || (province.trim().length() == 0)) {
      province = "999999";
    }
    Map map = (Map) this.dtos.get(appid);
    if (map == null) {
      return null;
    }
    if (map.get(year + "_" + province) != null) {
      return ((CommonDTO) map.get(year + "_" + province));
    }
    if (map.get("9999_" + province) != null) {
      return ((CommonDTO) map.get("9999_" + province));
    }
    if (map.get(year + "_999999") != null) {
      return ((CommonDTO) map.get(year + "_999999"));
    }
    return ((CommonDTO) this.defdtos.get(appid));
  }

  protected List<CommonDTO> loadAppid2DB(DataSource ds) throws AppException {
    try {
      String querySql = SqlFactory.getSQL("common", "loadappid");

      List columnsList = CommonJdbcDaoSupport.instanceDao(ds).query(
        "select column_name from user_tab_cols where table_name='FW_T_SYSDOMAIN'", rmJudge);
      if (columnsList.contains("DSGUID")) {
        //querySql = SqlFactory.getSQL("common", "loadappidwithds");
        querySql = " select dbversion, partitiontype, dsguid, province, year, rootpath, islocal, domainmark, domainport, domainip, appid from FW_T_SYSDOMAIN ";
      }

      List list = CommonJdbcDaoSupport.instanceDao(ds).query(querySql, rm);
      if (list.size() == 0) {
        throw new AppException("", "");
      }
      return list;
    } catch (Exception e) {
      e.printStackTrace();
      throw new AppException("", "");
    }
  }

  public Collection<String> getAppid() {
    return this.appids;
  }

  public Collection<String> init(DataSource ds) {
    if (this.appids != null) {
      return this.appids;
    }
    return getDomainInfo(ds);
  }

  private Collection<String> getDomainInfo(DataSource ds) {
    Set<String> list = null;
    list = new HashSet();
    list.add("common");
    list.add("fasp");
    //    try {
    //      list = filterDomain(loadAppid2DB(ds));
    //    } catch (AppException localAppException) {
    //      list = new HashSet();
    //      list.add("common");
    //      list.add("fasp");
    //    }
    for (String s : list) {
      if (("common".equals(s)) || ("fasp".equals(s))) {
        logger.info("当前启动域是平台域");
        this.isCommon = true;
        list.add("common");
        list.add("fasp");
        this.guid = "fasp";
        break;
      }
    }
    this.appids = list;
    return this.appids;
  }

  public boolean isCommon() {
    return this.isCommon;
  }

  public Set<String> filterDomain(List<CommonDTO> list) {
    Set ret = new HashSet();
    String domianName = getDomainName();
    for (CommonDTO dto : list) {
      if (this.dtos.get(dto.getString("appid")) == null) {
        this.dtos.put(dto.getString("appid"), new HashMap());
      }
      ((Map) this.dtos.get(dto.getString("appid"))).put(dto.getString("year") + "_" + dto.getString("province"), dto);

      this.defdtos.put(dto.getString("appid"), dto);
      if ((dto.getString("islocal") == null) || (!("1".equals(dto.getString("islocal"))))) {
        dto.put("islocal", "0");
      }

      if (domianName.indexOf(dto.getString("domainmark")) == 0) {
        dto.put("isDomain", Boolean.TRUE);
        ret.add(dto.getString("appid"));
        this.rootpath = dto.getString("rootpath");
        this.myappdto.add(dto);
      }
    }
    return ret;
  }

  public abstract String getDomainName();

  public void setThisIsCommon() {
    this.isCommon = true;
  }

  public void initAppupgrade() {
    CommonDTO com = new CommonDTO();
    com.put("appid", "common");
    com.put("domainip", "");
    com.put("domainport", "");
    com.put("domainmark", getDomainName());
    com.put("appname", "common");
    com.put("isDomain", Boolean.TRUE);
    com.put("islocal", "1");
    com.put("rootpath", "");
    com.put("year", "9999");
    com.put("province", "999999");
    CommonDTO fasp = new CommonDTO();
    fasp.put("appid", "fasp");
    fasp.put("domainip", "");
    fasp.put("domainport", "");
    fasp.put("domainmark", getDomainName());
    fasp.put("appname", "平台-支撑平台");
    fasp.put("isDomain", Boolean.TRUE);
    fasp.put("islocal", "1");
    fasp.put("rootpath", "");
    fasp.put("year", "9999");
    fasp.put("province", "999999");
    this.defdtos.clear();
    this.defdtos.put("common", com);
    this.defdtos.put("fasp", fasp);
    this.isCommon = true;
    this.appids = new HashSet();
    this.appupgrade = true;
  }

  public boolean isAppupgrade() {
    return this.appupgrade;
  }

  public String getDbGuid() {
    if (this.dbguid == null) {
      CommonJdbcDaoSupport.instanceDao().query("select * from tmp_v_dbguid", new RowMapper() {
        public Object mapRow(ResultSet rs, int i) throws SQLException {
          AbstractLoadAppid.this.dbguid = rs.getString("guid");
          return null;
        }
      });
    }
    return this.dbguid;
  }

  public String getGuid() {
    return this.guid;
  }

  public String getZkAddress() {
    if (zkServerAddress == null) {
      zkServerAddress = System.getProperty("fasp2.zkserver.address");
    }
    return zkServerAddress;
  }

  public boolean isZkStrat() {
    if (zkServerStartup == null) {
      zkServerStartup = Boolean.valueOf("true".equals(System.getProperty("fasp2.zkserver.startup")));
    }
    return zkServerStartup.booleanValue();
  }

  public String getServerGuid() {
    return this.serverGuid;
  }

  public void reset() {
    this.appids = null;
    this.myappdto.clear();
    this.guid = null;
    this.defdtos.clear();
    this.dtos.clear();
    init(null);
  }

  public void reset(String appid) {
    this.appids.add(appid);
    this.myappdto.clear();
    this.guid = null;
    this.defdtos.clear();
    this.dtos.clear();
    getDomainInfo(null);
  }
}
