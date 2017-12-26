package gov.df.fap.service.attach;

import gov.df.fap.api.attach.IAttachCategory;
import gov.df.fap.bean.attach.AttachCategoryForm;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AttachCategoryBO implements IAttachCategory {
  final String FILE_SYSTEM_MODE = "0";

  final String DATABASE_MODE = "1";

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  @Override
  public boolean deleteAttachCategory(String categoryId, String uploadMode) throws Exception {
    boolean exectuteResult = false;
    String sql = null;
    if (FILE_SYSTEM_MODE.equals(uploadMode)) {
      sql = "select * from SYS_ATTACH_FILE where STATUS='0' and category_id = ? ";
      List list = dao.findBySql(sql, new Object[] { categoryId });
      if (list != null && !list.isEmpty()) {
        for (int i = 0; i < list.size(); i++) {
          String attachId = (String) ((Map) list.get(i)).get("attach_id");
          try {
            dao.executeBySql("update SYS_ATTACH_FILE set STATUS='1' where STATUS='0' and attach_id=? ",
              new Object[] { attachId });
          } catch (Exception e) {
            e.printStackTrace();
          }
          sql = "select attach_path from SYS_ATTACH_FILE where attach_id =?";
          List path = dao.findBySql(sql, new Object[] { attachId });
          if (path != null && !path.isEmpty()) {
            String attach_path = (String) ((Map) path.get(0)).get("attach_path");
            File file = new File(attach_path);
            if (file.exists() && file.isFile()) {
              file.delete();
            }
          }
        }
      }
    }
    if (DATABASE_MODE.equals(uploadMode)) {

      sql = "select * from SYS_ATTACH_DB where  STATUS='0' and category_id = ?";
      List list = dao.findBySql(sql, new Object[] { categoryId });
      if (list != null && !list.isEmpty()) {
        for (int i = 0; i < list.size(); i++) {
          String attachId = (String) ((Map) list.get(i)).get("attach_id");
          try {
            dao.executeBySql("update SYS_ATTACH_DB set STATUS='1',attach_ob = '' where attach_id=? ",
              new Object[] { attachId });
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    try {
      String sql2 = "delete from SYS_CATEGORY where category_id =?";
      dao.executeBySql(sql2, new Object[] { categoryId });
      exectuteResult = true;
    } catch (Exception e) {
      exectuteResult = false;
    }
    return exectuteResult;
  }

  @Override
  public boolean updateAttachCategory(AttachCategoryForm attachCategory) throws Exception {
    boolean result = false;
    String year = SessionUtil.getLoginYear();
    String rgCode = SessionUtil.getRgCode();
    String updateBy = (String) SessionUtil.getUserInfoContext().getAttribute("user_name");
    String updateTime = getServerTime();
    String appId = SessionUtil.getUserInfoContext().getSysID();

    String catgroyId = attachCategory.getCategory_id();
    String sql = "select * from SYS_CATEGORY where category_id = ?";
    List list = dao.findBySql(sql, new String[] { catgroyId });
    if (list == null || list.isEmpty()) {
      return false;
    }
    Map map = (Map) list.get(0);
    String createTime = (String) map.get("create_time");
    String createBy = (String) map.get("create_by");

    dao.deleteDataBySql("SYS_CATEGORY", attachCategory, new String[] { "category_id" });

    attachCategory.setCreate_by(createBy);
    attachCategory.setCreate_time(createTime);
    attachCategory.setAppid(appId);
    attachCategory.setYear(year);
    attachCategory.setRg_code(rgCode);

    attachCategory.setUpdate_by(updateBy);
    attachCategory.setUpdate_time(updateTime);
    try {
      dao.saveDataBySql("SYS_CATEGORY", attachCategory);
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public boolean saveAttachCategory(AttachCategoryForm attachCategory) throws Exception {

    String year = SessionUtil.getLoginYear();
    String rgCode = SessionUtil.getRgCode();
    String createBy = (String) SessionUtil.getUserInfoContext().getAttribute("user_name");
    String createTime = getServerTime();
    attachCategory.setCreate_by(createBy);
    attachCategory.setYear(year);
    attachCategory.setRg_code(rgCode);
    attachCategory.setCreate_time(createTime);
    boolean result = false;
    try {
      dao.saveDataBySql("SYS_CATEGORY", attachCategory);
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }

  @Override
  public List findAttachByCategoryId(String categoryId, String uploadMode) {
    String sql = null;
    if (FILE_SYSTEM_MODE.equals(uploadMode)) {
      sql = "select a.* ,b.* from SYS_ATTACH_FILE a, SYS_CATEGORY b where a.category_id = b.category_id and a.status= '0' and b.category_id=?";
    }
    if (DATABASE_MODE.equals(uploadMode)) {
      sql = "select a.ATTACH_ID, a.BUSI_ID, a.ATTACH_NAME, a.ATTACH_TYPE, a.STATUS, a.APPID, a.YEAR, a.RG_CODE, a.REMARK, a.CREATE_BY, a.CREATE_TIME, a.UPDATE_BY, a.UPDATE_TIME, a.CATEGORY_ID, a.EXT1, a.EXT2, a.EXT3 , b.* from SYS_ATTACH_DB a, SYS_CATEGORY b  where a.category_id = b.category_id and a.status= '0' and b.category_id=?";
    }
    return dao.findBySql(sql, new Object[] { categoryId });

  }

  @Override
  public List getAllAttachCategory(String sysId) {
    String sql = "select category_id as sortNumber,category_name as sortName, category_code as sortCode ,remark as sortInfo,"
      + (TypeOfDB.isOracle() ? "'['||category_code||']'||category_name" : "concat('[',category_code,']',category_name)")
      + " as categoryName from SYS_CATEGORY where appid = ?";
    return dao.findBySql(sql, new String[] { sysId });
  }

  public String getServerTime() {
    Calendar calender = Calendar.getInstance();
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return f.format(calender.getTime());
  }
}
