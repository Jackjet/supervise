package gov.df.fap.service.gl.configure.impl;

import gov.df.fap.bean.gl.GlConstant;
import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.service.gl.core.DaoSupport;
import gov.df.fap.service.gl.core.interfaces.PreparedStatementHandler;
import gov.df.fap.service.gl.core.interfaces.PreparedStatementParamSetter;
import gov.df.fap.service.gl.core.interfaces.ResultSetMapper;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.gl.configure.IAccountDao;
import gov.df.fap.service.util.gl.configure.IBusVouTypeDao;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.ReflectionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.number.NumberUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 记账模板对象BusVouType的数据访问类,封装数据库操作.
 * @author justin
 * @version 2017-03-30
 */
@Component("busVouTypeDao")
public class BusVouTypeDao implements IBusVouTypeDao {

  @Autowired
  DaoSupport daoSupport;

  @Autowired
  IAccountDao accountDao;
  
  public void setAccountDao(IAccountDao dao) {
    this.accountDao = dao;
  }

  public void setDaoSupport(DaoSupport daoSupport) {
    this.daoSupport = daoSupport;
  }

  /**
   * 新增记账模板,同时会补记账模板ID及年度,时间,区划.
   * @param bvType 记账模板对象
   */
  public void saveBusVouType(BusVouType bvType) {
    if (StringUtil.equals(new Long(bvType.getVou_type_id()).toString(), StringUtil.EMPTY)
      || bvType.getVou_type_id() == 0)
      bvType.setVou_type_id(CommonUtil.generateSequence(GlConstant.SEQ_BVTYPE_KEY));
    bvType.setLast_ver(DateHandler.getDateFromNow(0));
    bvType.setLatest_op_date(DateHandler.getDateFromNow(0));
    bvType.setSet_year(CommonUtil.getIntSetYear());
    bvType.setRg_code(CommonUtil.getRgCode());
    StringBuffer insertSQL = new StringBuffer();
    insertSQL
      .append(
        "insert into gl_busvou_type(vou_type_id, vou_type_code, vou_type_name, last_ver, set_year, rg_code, latest_op_date, enabled) values (")
      .append(" #vou_type_id#").append(",#vou_type_code#").append(",#vou_type_name#").append(",#last_ver#")
      .append(",#set_year#").append(",#rg_code#").append(",#latest_op_date#").append(",#enabled#").append(")");
    daoSupport.executeUpdate(insertSQL.toString(), bvType);
  }

  /**
   * 更新记账模板,同时更新最新版本号.
   * @param bvType 记账模板对象
   */
  public void updateBusVouType(BusVouType bvType) {
    StringBuffer updateSQL = new StringBuffer();
    bvType.setLast_ver(DateHandler.getDateFromNow(0));
    bvType.setLatest_op_date(DateHandler.getDateFromNow(0));
    updateSQL.append("update gl_busvou_type set vou_type_code=#vou_type_code#,")
      .append("vou_type_name=#vou_type_name#,").append("latest_op_date=#latest_op_date#,")
      .append("last_ver=#last_ver#,").append("enabled=#enabled# ").append("where vou_type_id=#vou_type_id#");
    daoSupport.executeUpdate(updateSQL.toString(), bvType);
  }

  /**
   * 删除记账模板
   * @param busVouType
   */
  public void deleteBusVouType(BusVouType busVouType) {
    String deleteSql = "delete from gl_busvou_type where vou_type_id=" + busVouType.getVou_type_id();
    daoSupport.executeUpdate(deleteSql);
  }

  public void deleteAllBusVouType() {
    String rgCode = CommonUtil.getRgCode();
    String setYear = CommonUtil.getSetYear();
    String deleteSql = "delete from gl_busvou_acctmdl where rg_code='" + rgCode + "' and set_year='" + setYear + "'";
    daoSupport.executeUpdate(deleteSql);
    deleteSql = "delete from gl_busvou_type where rg_code='" + rgCode + "' and set_year='" + setYear + "'";
    daoSupport.execute(deleteSql);
  }

  /**
   * 读取记账模板
   * @param vouTypeCode 记账模板编码
   * @return
   */
  public BusVouType loadBusVouTypeByCode(String vouTypeCode) {
    return (BusVouType) daoSupport.genericQueryForObject(
      "select * from gl_busvou_type where vou_type_code=? and rg_code=? and set_year=?", new Object[] { vouTypeCode,
        CommonUtil.getRgCode(), CommonUtil.getSetYear() }, BusVouType.class);
  }

  /**
  * 读取记账模板
  * @param vouTypeId
  * @return
  */
  public BusVouType loadBusVouType(long vouTypeId) {
    return (BusVouType) daoSupport.genericQueryForObject("select * from gl_busvou_type where vou_type_id='" + vouTypeId
      + "'", BusVouType.class);
  }

  /**
   * 读取记账模板编码
   * @param billTypeCode
   * @return
   */
  public long getVouTypeIdByBill(String billTypeCode) {
    String sql = "select busvou_type_id from sys_billtype where billtype_code=? and rg_code=? and set_year=?";
    return NumberUtil.toLong(daoSupport.queryForString(sql, new String[] { billTypeCode, CommonUtil.getRgCode(),
      CommonUtil.getSetYear() }));
  }

  /**
   * 查询所有记账模板信息
   * @return
   */
  public List allBusVouType() {
    String setYear = CommonUtil.getSetYear();
    String rgCode = CommonUtil.getRgCode();

    return daoSupport.genericQuery("select * from gl_busvou_type where set_year=" + setYear + " and rg_code='" + rgCode
      + "' order by vou_type_code", BusVouType.class);
  }

  /**
   * 新增记账模板与科目关系.
   * @param busVouAcctmdlList
   * @param busVouTypeId
   */
  public void saveBusVouAcctmdl(List busVouAcctmdlList) {
    //int setYear = DateHandler.getCurrentYear();
    int setYear = Integer.parseInt(CommonUtil.getSetYear());
    String rgCode = CommonUtil.getRgCode();
    for (int i = 0; i < busVouAcctmdlList.size(); i++) {
      final BusVouAcctmdl acctmdl = (BusVouAcctmdl) busVouAcctmdlList.get(i);
      if (acctmdl.getAcctmdl_id() == 0)
        acctmdl.setAcctmdl_id(CommonUtil.generateSequence(GlConstant.SEQ_ACCTMDL_KEY));
    }
    StringBuffer insertSQL = new StringBuffer();
    insertSQL.append("insert into gl_busvou_acctmdl values (").append(" #acctmdl_id#").append(",").append(setYear)
      .append(",").append("'" + rgCode + "'").append(",").append("#vou_type_id#").append(",#account_id#")
      .append(",#entry_side#").append(",#is_primary_source#").append(",#is_primary_target#").append(",#ctrl_level#")
      .append(",#rule_id#").append(")");
    daoSupport.batchExcute(insertSQL.toString(), busVouAcctmdlList);
  }

  /**
   * 更新记账模板科目关系对象,采用先删除后插入的更新方法.
   * @param busVouAcctmdlList
   * @param busVouTypeId
   */
  public void updateBusVouAcctmdl(List busVouAcctmdlList, long busVouTypeId) {
    deleteAcctmdlByBvTypeId(busVouTypeId);
    BusVouAcctmdl busVouAcctmdl = null;
    for (int i = 0; i < busVouAcctmdlList.size(); i++) {
      busVouAcctmdl = (BusVouAcctmdl) busVouAcctmdlList.get(i);
      if (busVouAcctmdl.getAcctmdl_id() == 0)
        busVouAcctmdl.setAcctmdl_id(CommonUtil.generateSequence(GlConstant.SEQ_ACCTMDL_KEY));
      busVouAcctmdl.setVou_type_id(busVouTypeId);
    }
    this.saveBusVouAcctmdl(busVouAcctmdlList);
  }

  public void deleteAcctmdlByBvTypeId(long bvTypeId) {
    daoSupport.executeUpdate("delete from gl_busvou_acctmdl where vou_type_id=" + bvTypeId);
  }

  public void deleteAcctmdlByAcctmdlId(long acctmdlId) {
    daoSupport.executeUpdate("delete from gl_busvou_acctmdl where acctmdl_id=" + acctmdlId);
  }

  /**
   * 读取记账模板科目关系
   * @param vouTypeId 记账模板ID
   * @return
   */
  public List loadVouAcctdml(long vouTypeId) {
    String whereSql = "";
    if (vouTypeId != 0)
      whereSql = " and vou_type_id='" + vouTypeId + "'";
    String sql = "select * from gl_busvou_acctmdl where rg_code=? and set_year=?" + whereSql;
    List acctmdlList = daoSupport.genericQuery(sql,
      new Object[] { SessionUtil.getRgCode(), SessionUtil.getLoginYear() }, BusVouAcctmdl.class);
    BusVouAcctmdl acctmdl = null;
    for (int i = 0; i < acctmdlList.size(); i++) {
      acctmdl = (BusVouAcctmdl) acctmdlList.get(i);
      acctmdl.setBusVouAccount(accountDao.loadAccount(acctmdl.getAccount_id()));
    }
    return acctmdlList;
  }

  /**
  * 校验此记账模板是否已存在在途数据
  * @param vouTypeId
  * @return
  */
  public boolean existOnWayData(long vouTypeId) {
    String sql = "select count(1) from gl_journal where vou_type_id=" + vouTypeId;
    int count = daoSupport.queryForInt(sql);
    if (count > 0)
      return true;
    return false;
  }

  public void saveConfigString(final byte[] content){
    final String rgCode = CommonUtil.getRgCode();
    final String setYear = CommonUtil.getSetYear();
    String sql = "delete gl_busvou_type_byte where rg_code='" + rgCode + "' and set_year=" + setYear + "";
    daoSupport.execute(sql);

    String sql2 = null;
    if(TypeOfDB.isOracle()) {
    	sql2 = "insert into gl_busvou_type_byte(bus_vou_type_byte,rg_code,set_year) values(empty_blob(), ?, ?)";
    } else if(TypeOfDB.isMySQL()) {
    	sql2 = "insert into gl_busvou_type_byte(bus_vou_type_byte,rg_code,set_year) values('', ?, ?)";
    }
    daoSupport.execute(sql2,
      new PreparedStatementHandler() {
        public Object handler(PreparedStatement ps) {
          try {
            ps.executeUpdate();
          } catch (SQLException e) {
            e.printStackTrace();
          }
          return null;
        }
      }, new PreparedStatementParamSetter() {
        public void setter(PreparedStatement ps, int i) throws SQLException {
          ps.setString(1, rgCode);
          ps.setString(2, setYear);
        }

        public int size() {
          return 1;
        }
      });

    daoSupport.genericQuery("select * from gl_busvou_type_byte where rg_code='" + rgCode + "' and set_year=" + setYear
      + " for update", new ResultSetMapper() {
      public Object handleRow(ResultSet rs) {
        try {
          OutputStream os = new BlobWrapper(rs.getBlob(1)).outputStream();
          os.write(content);
          os.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }
    });
  }

  public byte[] loadConfigString() {
    List resultList = daoSupport.genericQuery(
      "select * from gl_busvou_type_byte where rg_code='" + CommonUtil.getRgCode() + "' and set_year='"
        + CommonUtil.getSetYear() + "'", new ResultSetMapper() {
        public Object handleRow(ResultSet rs) {
          Blob b;
          try {
            b = rs.getBlob(1);
            InputStream is = b.getBinaryStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int byteRead = 0;
            while ((byteRead = is.read(buffer, 0, 2048)) > 0) {
              baos.write(buffer, 0, byteRead);
            }
            return baos.toByteArray();
          } catch (Exception e) {
            e.printStackTrace();
          }
          return null;
        }

      });
    return (byte[]) (resultList.size() > 0 ? resultList.get(0) : null);
  }

  public class BlobWrapper {

    private Blob blob = null;

    public BlobWrapper(Blob blob) {
      this.blob = blob;
    }

    public OutputStream outputStream() throws SQLException {

      try {
        return (OutputStream) ReflectionUtil.invokeMethod(blob, "getBinaryOutputStream", null, null);
      } catch (SecurityException e) {
      } catch (IllegalArgumentException e) {
      } catch (NoSuchMethodException e) {
      } catch (IllegalAccessException e) {
      } catch (InvocationTargetException e) {
      }

      return null;
    }

  }

  public static void main(String[] args) {
    char c = 0xb5;
    System.out.println(c);
  }
}
