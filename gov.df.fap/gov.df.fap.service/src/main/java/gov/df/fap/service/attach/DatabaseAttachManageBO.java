package gov.df.fap.service.attach;

import gov.df.fap.api.attach.IAttachManage;
import gov.df.fap.api.portal.IPaginationService;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Service;

@Service("databaseAttachManageBO")
public class DatabaseAttachManageBO implements IAttachManage {

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  @Autowired
  private IPaginationService ips;

  /** SYS_ATTACH_DB表全部字段 */
  public static final String[] FIELDS_ALL_ATTACH_OB = new String[] { "attach_id", "busi_id", "attach_name",
    "attach_type", "status", "appid", "year", "rg_code", "remark", "create_by", "create_time", "update_by",
    "update_time", "category_id", "ext1", "ext2", "ext3" };

  @Override
  public List<Map<String, Object>> uploadattach(List<Map<String, Object>> fileList, String app_id, String orgcode,
    String extpath, String categoryId) throws Exception {

    String set_year = SessionUtil.getLoginYear();
    String create_by = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");
    String create_time = getServerTime();
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

    if (fileList != null) {
      for (int i = 0; i < fileList.size(); i++) {
        List<Map<String, Object>> attach_list = new ArrayList<Map<String, Object>>();
        Map<String, Object> mapData = (Map<String, Object>) fileList.get(i);
        Map<String, Object> temp = new HashMap<String, Object>();

        boolean hasAttachIdflag = false;
        String attach_id = null;
        if (mapData.containsKey("attach_id") && mapData.get("attach_id") != null
          && !"".equals((String) mapData.get("attach_id"))) {
          hasAttachIdflag = true;
          attach_id = (String) mapData.get("attach_id");
        } else {
          attach_id = getAttachID(); //取得附件ID
        }

        mapData.put("attach_id", attach_id);
        mapData.put("appid", app_id);
        mapData.put("status", "0");
        mapData.put("year", set_year);
        mapData.put("rg_code", orgcode);
        mapData.put("create_by", create_by);
        mapData.put("create_time", create_time);
        mapData.put("update_by", "");
        mapData.put("update_time", "");
        mapData.put("category_id", categoryId);
        mapData.put("ext1", "");
        mapData.put("ext2", "");
        mapData.put("ext3", "");

        String attachName = (String) mapData.get("attach_name");
        attach_list.add(mapData);
        String busiId = (String) mapData.get("busi_id");//BUSI_ID 业务主键id
        //        if (busiId == null || busiId.isEmpty() || "".equals(busiId)) {
        //          busiId = "";
        //        } else {
        //          if (fileList.size() > 1 && i > 0) {
        //            busiId += i;
        //          }
        //        }
        mapData.put("busi_id", busiId);
        String attachType = (String) mapData.get("attach_type");
        temp.put("attachID", attach_id);
        temp.put("attacName", attachName + "." + attachType);
        resultList.add(temp);
        if (attach_list.size() > 0) {
          if (hasAttachIdflag) {
            mapData.put("category_id", categoryId);
            updateAttachInfo(mapData);
          } else {
            dao.batchSaveDataBySql("sys_attach_db", FIELDS_ALL_ATTACH_OB, attach_list);
          }
        }
        byte[] attach_ob = (byte[]) mapData.get("bytes");
        StringBuffer sb1 = new StringBuffer();
        sb1.append("update SYS_ATTACH_DB set attach_ob = ? where attach_id = ?");
        Session session = SessionFactoryUtils.getSession(dao.getHibernateTemplate().getSessionFactory(), dao
          .getHibernateTemplate().getEntityInterceptor(), dao.getHibernateTemplate().getJdbcExceptionTranslator());
        Connection cnt = session.connection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        ByteArrayInputStream bis = null;
        try {
          pst = cnt.prepareStatement(sb1.toString());
          bis = new ByteArrayInputStream(attach_ob);
          pst.setBinaryStream(1, bis, attach_ob.length);
          pst.setString(2, attach_id);
          rs = pst.executeQuery();
        } catch (SQLException e) {
          e.printStackTrace();
          //throw new Exception("保存附件文件失败！");
        } finally {
          if (pst != null) {
            pst.close();
          }
          if (rs != null) {
            rs.close();
          }
          if (bis != null) {
            bis.close();
          }
        }

        try {
          String sql = "select category_id from SYS_ATTACH_DB where attach_id = ?";
          List categoryIdList = dao.findBySql(sql, new String[] { attach_id });
          if (categoryIdList != null && !categoryIdList.isEmpty()) {
            String sqlStr = "update SYS_ATTACH_DB set ATTACH_NAME=? , ATTACH_TYPE=? where ATTACH_ID = ? ";
            dao.executeBySql(sqlStr, new Object[] { attachName, attachType, attach_id });
          }
        } catch (Exception e) {
          e.printStackTrace();
          throw new Exception("附件上传失败，请联系管理员！");
        }

      }
    }

    return resultList;
  }

  @Override
  public Map getAttach(String attachId) throws Exception {
    //    String sql = "select a.* ,b.* from SYS_ATTACH_DB a, SYS_CATEGORY b where a.category_id= b.category_id and a.status= '0' and a.attach_id=?";
    //    Map map = (Map) dao.findBySql(sql, new Object[] { attachId }).get(0);
    Map map = getAttachOB(attachId);
    return map;
  }

  @Override
  public Map getAttach(String attachId, boolean isAll) throws Exception {
    Map map = null;
    if (isAll) {
      String sql = "select a.* ,b.* from SYS_ATTACH_DB a, SYS_CATEGORY b  where a.category_id= b.category_id and a.status= '0' and a.attach_id=?";
      map = (Map) dao.findBySql(sql, new Object[] { attachId }).get(0);
    } else {
      map = getAttachOB(attachId);
    }

    return map;
  }

  @Override
  public List getAttachList(List attachIds) throws Exception {
    List attachList = new ArrayList();
    for (int i = 0; i < attachIds.size(); i++) {
      String sql = "select a.* ,b.* from SYS_ATTACH_DB a, SYS_CATEGORY b  where a.category_id= b.category_id and a.status= '0' and a.attach_id=?";
      String attach_id = (String) attachIds.get(i);
      List tempList = dao.findBySql(sql, new Object[] { attach_id });
      attachList.add(tempList);
    }
    return attachList;
  }

  @Override
  public boolean deleteAttach(List attachIds) throws Exception {

    boolean executeResult = false;
    String sql = "select ATTACH_ID,STATUS from SYS_ATTACH_DB where ATTACH_ID = ?";
    for (int i = 0; i < attachIds.size(); i++) {
      String attachId = (String) attachIds.get(i);
      List fileList = dao.findBySql(sql, new Object[] { attachId });
      int size = fileList.size();
      for (int j = 0; j < size; j++) {
        XMLData data = (XMLData) fileList.get(j);
        String status = (String) data.get("status");
        String attach_id = (String) data.get("attach_id");
        if (status.equals("0")) {
          try {
            dao.executeBySql("update SYS_ATTACH_DB set STATUS='1',attach_ob = '' where attach_id=? ",
              new Object[] { attach_id });
            executeResult = true;
          } catch (Exception e) {
            executeResult = false;
          }
        }
      }
    }
    return executeResult;
  }

  @Override
  public boolean updateRemark(List<Map> list) throws Exception {
    boolean executeResult = false;
    for (int i = 0; i < list.size(); i++) {
      Map tempMap = list.get(i);
      String attachId = (String) tempMap.get("attachId");
      String remark = (String) tempMap.get("remark");
      String updateBy = SessionUtil.getUserInfoContext().getAuthorizedUserName();
      String updateTime = getServerTime();
      try {
        dao.executeBySql("update SYS_ATTACH_DB set REMARK=? ,update_by=? , update_time=? where attach_id=?",
          new Object[] { remark, updateBy, updateTime, attachId });
        executeResult = true;
      } catch (Exception e) {
        executeResult = false;
      }
    }
    return executeResult;
  }

  @Override
  public boolean updateAttachName(List<Map> list) throws Exception {
    boolean executeResult = false;
    for (int i = 0; i < list.size(); i++) {
      Map tempMap = list.get(i);
      String attachId = (String) tempMap.get("attachId");
      String attachName = (String) tempMap.get("attachName");
      String updateBy = SessionUtil.getUserInfoContext().getAuthorizedUserName();
      String updateTime = getServerTime();
      try {
        dao.executeBySql("update SYS_ATTACH_DB set ATTACH_NAME=? ,update_by=? , update_time=? where attach_id=?",
          new Object[] { attachName, updateBy, updateTime, attachId });
        executeResult = true;

      } catch (Exception e) {
        executeResult = false;
      }
    }
    return executeResult;
  }

  @Override
  public PageRequest buildPageRequest(int pageNumber, int pageSize) {
    Sort sort = null;
    List<String> orders = new ArrayList<String>();
    orders.add("create_time");
    sort = new Sort(Direction.DESC, orders);
    return new PageRequest(pageNumber - 1, pageSize, sort);
  }

  @Override
  public List<Map<String, String>> changePageFormat(List<Map<String, Object>> map_list) {
    List<Map<String, String>> AttachList = new ArrayList<Map<String, String>>();
    try {
      for (Map<String, Object> temp : map_list) {
        Map<String, String> attachMap = new HashMap<String, String>();
        attachMap.put("attach_id", temp.get("ATTACH_ID").toString());
        if (StringUtil.isNull(temp.get("BUSI_ID"))) {
          attachMap.put("busi_id", "");
        } else {
          attachMap.put("busi_id", temp.get("BUSI_ID").toString());
        }
        if (StringUtil.isNull(temp.get("ATTACH_NAME"))) {
          attachMap.put("attach_name", "");
        } else {
          attachMap.put("attach_name", temp.get("ATTACH_NAME").toString());
        }
        if (StringUtil.isNull(temp.get("ATTACH_TYPE"))) {
          attachMap.put("attach_type", "");
        } else {
          attachMap.put("attach_type", temp.get("ATTACH_TYPE").toString());
        }
        if (StringUtil.isNull(temp.get("STATUS"))) {
          attachMap.put("status", "");
        } else {
          attachMap.put("status", temp.get("STATUS").toString());
        }
        if (StringUtil.isNull(temp.get("APPID"))) {
          attachMap.put("appid", "");
        } else {
          attachMap.put("appid", temp.get("APPID").toString());
        }
        if (StringUtil.isNull(temp.get("YEAR"))) {
          attachMap.put("year", "");
        } else {
          attachMap.put("year", temp.get("YEAR").toString());
        }
        if (StringUtil.isNull(temp.get("RG_CODE"))) {
          attachMap.put("rg_code", "");
        } else {
          attachMap.put("rg_code", temp.get("RG_CODE").toString());
        }
        if (StringUtil.isNull(temp.get("REMARK"))) {
          attachMap.put("remark", "");
        } else {
          attachMap.put("remark", temp.get("REMARK").toString());
        }
        if (StringUtil.isNull(temp.get("CREATE_BY"))) {
          attachMap.put("create_by", "");
        } else {
          attachMap.put("create_by", temp.get("CREATE_BY").toString());
        }
        if (StringUtil.isNull(temp.get("CREATE_TIME"))) {
          attachMap.put("createTime", "");
        } else {
          attachMap.put("createTime", temp.get("CREATE_TIME").toString());
        }
        if (StringUtil.isNull(temp.get("UPDATE_BY"))) {
          attachMap.put("update_by", "");
        } else {
          attachMap.put("update_by", temp.get("UPDATE_BY").toString());
        }
        if (StringUtil.isNull(temp.get("UPDATE_TIME"))) {
          attachMap.put("update_time", "");
        } else {
          attachMap.put("updte_time", temp.get("UPDAE_TIME").toString());
        }
        if (StringUtil.isNull(temp.get("CATEGORY_ID"))) {
          attachMap.put("category_id", "");
        } else {
          attachMap.put("category_id", temp.get("CATEGORY_ID").toString());
        }
        if (StringUtil.isNull(temp.get("EXT1"))) {
          attachMap.put("ext1", "");
        } else {
          attachMap.put("ext1", temp.get("EXT1").toString());
        }
        if (StringUtil.isNull(temp.get("EXT2"))) {
          attachMap.put("ext2", "");
        } else {
          attachMap.put("ext2", temp.get("EXT2").toString());
        }
        if (StringUtil.isNull(temp.get("EXT3"))) {
          attachMap.put("ext3", "");
        } else {
          attachMap.put("ext3", temp.get("EXT3").toString());
        }
        AttachList.add(attachMap);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return AttachList;
  }

  @Override
  public PageImpl getPageData(String sql, int pageNumber2, int pageSize) {
    PageImpl pageimpl = null;
    PageRequest pageRequest = buildPageRequest(pageNumber2, pageSize);
    try {
      List<Map<String, Object>> map_list = ips.getPaginationBeans(sql, pageRequest);
      int dataCount = ips.getDataCount(sql);
      List<Map<String, Object>> List = ips.getPaginationData(sql, pageRequest);
      java.util.List<Map<String, String>> PageList = changePageFormat(List);
      //建立page对象
      pageimpl = new PageImpl(PageList, pageRequest, dataCount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return pageimpl;
  }

  @Override
  public Integer getDataCount(String tableName) throws Exception {
    return ips.getDataCount(tableName);
  }

  @Override
  public String getPropertiesValueByKey(String filePath, String key) {
    Properties pps = new Properties();
    try {
      InputStream in = new BufferedInputStream(new FileInputStream(filePath));
      pps.load(in);
      String value = pps.getProperty(key);
      return value;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Map<String, Object> getAllProperties(String filePath) throws IOException {
    Map<String, Object> map = new HashMap<String, Object>();
    Properties pps = new Properties();
    InputStream in = new BufferedInputStream(new FileInputStream(filePath));
    pps.load(in);
    Enumeration en = pps.propertyNames(); //得到配置文件的名字
    while (en.hasMoreElements()) {
      String strKey = (String) en.nextElement();
      String strValue = pps.getProperty(strKey);
      map.put(strKey, strValue);
    }
    return map;
  }

  @Override
  public boolean updateAttachInfo(Map attachInfo) {
    boolean result = false;
    String set_year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String update_by = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");
    String update_time = getServerTime();
    String hasAttachId = (String) attachInfo.get("attach_id");
    String sql = null;
    List list = new ArrayList();
    List categoryIdList = new ArrayList();
    if ("".equals(hasAttachId) || hasAttachId.trim().isEmpty() || hasAttachId == null) {
      return false;
    } else {
      sql = "select category_id from sys_attach_db where attach_id = ?";
      categoryIdList = dao.findBySql(sql, new String[] { hasAttachId });

      String attachNewName = (String) attachInfo.get("attach_name");
      String busiId = (String) attachInfo.get("busi_id");

      //      if (attachCode == null || "".equals(attachCode)) {
      //        attachCode = getRandomName();
      //      }

      String remark = (String) attachInfo.get("remark");
      sql = "update SYS_ATTACH_DB set busi_id = ?,attach_name = ?,remark = ?,update_by = ?,update_time = ? where attach_id = ?";
      dao.executeBySql(sql, new String[] { busiId, attachNewName, remark, update_by, update_time, hasAttachId });

      //更新分类信息
      if (attachInfo.containsKey("category_id") && categoryIdList != null && !categoryIdList.isEmpty()) {
        String newCategoryId = (String) attachInfo.get("category_id");
        String oldCategoryId = (String) ((Map) categoryIdList.get(0)).get("category_id");
        if (!oldCategoryId.equals(newCategoryId) && newCategoryId != null && !newCategoryId.isEmpty()) {
          dao.executeBySql("update sys_attach_db set category_id = ?  where attach_id=?", new Object[] { newCategoryId,
            hasAttachId });
        }
        // attachInfo.remove("category_id");
      }

      if (attachInfo.containsKey("state")) {
        if ("1".equals((String) attachInfo.get("state"))) {
          dao.executeBySql(
            "update sys_attach_db set ATTACH_NAME=? ,ATTACH_OB = '' , ATTACH_TYPE ='' where attach_id=?", new Object[] {
              attachNewName, hasAttachId });
        }
      }

      result = true;
    }
    return result;
  }

  @Override
  public List getSysApp() {
    String strSQL = "select sys_id,"
      + (TypeOfDB.isOracle() ? "'['||sys_id||']'||sys_name " : "concat('[',sys_id,']',sys_name) ")
      + " as sys_name from sys_app  where sys_id<>'000' order by sys_id ";
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {
      gov.df.fap.service.util.log.Log.error(e.getMessage()
        + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
    }
    return list;
  }

  @Override
  public String getUploadMode() {
    StringBuffer strSQL = new StringBuffer("select chr_value from sys_userpara" + Tools.addDbLink()
      + " where  chr_code = 'UPLOAD_MODE' ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {
      gov.df.fap.service.util.log.Log.error(e.getMessage()
        + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
    }
    XMLData data = (XMLData) list.get(0);
    String uploadMode = (String) data.get("chr_value");
    return uploadMode;
  }

  /**
   * 删除单个文件
   * @param   sPath    被删除文件的文件名
   * @return 单个文件删除成功返回true，否则返回false
   */
  public boolean deleteFile(String sPath) {
    boolean flag = false;
    File file = new File(sPath);
    // 路径为文件且不为空则进行删除
    if (file.isFile() && file.exists()) {
      file.delete();
      flag = true;
    }
    return flag;
  }

  /**文件重命名 
  * @param path 文件目录 
  * @param oldname  原来的文件名 
  * @param newname 新文件名 
  */
  public void renameFile(String path, String oldName, String newName) {
    if (!oldName.equals(newName)) {//新的文件名和以前文件名不同时,才有必要进行重命名 
      File oldfile = new File(path + "/" + oldName);
      File newfile = new File(path + "/" + newName);
      if (!oldfile.exists()) {
        return;
      }
      if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名 
        System.out.println(newName + "已经存在！");
      else {
        oldfile.renameTo(newfile);
      }
    } else {
      System.out.println("新文件名和旧文件名相同!");
    }
  }

  public String getAttachID() {
    UUID uuid = UUID.randomUUID();
    String id = uuid.toString();
    return id.toUpperCase();
  }

  public String getServerTime() {
    Calendar calender = Calendar.getInstance();
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return f.format(calender.getTime());
  }

  @Override
  public String getRgCode() {
    String rg_code = SessionUtil.getRgCode();
    return rg_code;
  }

  @Override
  public String getUploadRootPath() {
    StringBuffer strSQL = new StringBuffer("select chr_value from sys_userpara" + Tools.addDbLink()
      + " where  chr_code = 'UPLOAD_ROOT_PATH' ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
      strSQL = null;
    } catch (Exception e) {
      gov.df.fap.service.util.log.Log.error(e.getMessage()
        + org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
    }
    XMLData data = (XMLData) list.get(0);
    String uploadRootPath = (String) data.get("chr_value");
    return uploadRootPath;
  }

  public Map<String, Object> getAttachOB(String attachId) throws SQLException {
    Map<String, Object> result = new HashMap<String, Object>();
    StringBuffer sb1 = new StringBuffer();
    sb1.append("select attach_name,attach_ob,attach_type from  SYS_ATTACH_DB  where attach_id = ?");
    Session session = SessionFactoryUtils.getSession(dao.getHibernateTemplate().getSessionFactory(), dao
      .getHibernateTemplate().getEntityInterceptor(), dao.getHibernateTemplate().getJdbcExceptionTranslator());

    Connection cnt = session.connection();
    PreparedStatement pst = null;
    ResultSet rs = null;
    ByteArrayInputStream bis = null;

    try {
      pst = cnt.prepareStatement(sb1.toString());
      pst.setString(1, attachId);
      rs = pst.executeQuery();
      if (rs.next()) {
        String attachName = rs.getString(1);
        Blob attachOB = rs.getBlob(2);
        String attachType = rs.getString(3);
        result.put("attach_name", attachName);
        result.put("attach_ob", attachOB);
        result.put("attach_type", attachType);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      //throw new Exception("获取附件文件失败！");
    } finally {
      if (pst != null) {
        pst.close();
      }
      if (rs != null) {
        rs.close();
      }
    }
    return result;
  }

  @Override
  public Map<String, Object> checkDiskSize(String uploadPath, long attachSize, long limitSize) {

    return null;
  }

  /**
   * 生成随机附件编号：当前年月日时分秒+五位随机数
   * 
   * @return
   */
  public static String getRandomName() {
    SimpleDateFormat simpleDateFormat;
    simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    Date date = new Date();
    String str = simpleDateFormat.format(date);
    Random random = new Random();
    int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
    return "R" + rannum + str;// 当前时间
  }

  @Override
  public List<Map<String, Object>> uploadIMattach(List<Map<String, Object>> fileList) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, String> previewFile(String filePath, String fileType, String rootPath, HttpServletResponse response)
    throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean deletePreviewFile(String srcPath) throws Exception {
    // TODO Auto-generated method stub
    return false;
  }

}
