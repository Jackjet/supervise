package gov.df.fap.service.attach;

import gov.df.fap.api.attach.IAttachManage;
import gov.df.fap.api.portal.IPaginationService;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.file2html.Excel2HtmlUtil;
import gov.df.fap.util.file2html.PPT2HtmlUtil;
import gov.df.fap.util.file2html.Txt2HtmlUtil;
import gov.df.fap.util.file2html.Word2HtmlUtil;
import gov.df.fap.util.xml.XMLData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.jiuqi.util.zip.ZipEntry;
import com.jiuqi.util.zip.ZipFile;
import com.jiuqi.util.zip.ZipOutputStream;

@Service("fileSystemAttachManageBO")
public class FileSystemAttachManageBO implements IAttachManage {

  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao;

  @Autowired
  private IPaginationService ips;
  
  /**GBK编码方式 */
  public static final String CHINESE_CHARSET = "GBK";

  public static final String[] IMG_FILE_TYPE = new String[] { "jpg", "jpeg", "bmp", "png", "gif", "tiff" };

  public static final String[] COMPRESS_FILE_TYPE = new String[] { "zip", "7z", "rar" };

  /** SYS_ATTACH_FILE表全部字段 */
  public static final String[] FIELDS_ALL_ATTACH = new String[] { "attach_id", "busi_id", "attach_name", "attach_type",
    "attach_path", "status", "appid", "year", "rg_code", "remark", "create_by", "create_time", "update_by",
    "update_time", "category_id", "ext1", "ext2", "ext3" };

  public static final String[] IM_FIELDS_ALL_ATTACH = new String[] { "attm_id", "file_name", "time_path" };

  @Override
  public List<Map<String, Object>> uploadattach(List<Map<String, Object>> fileList, String app_id, String orgcode,
    String extpath, String categoryId) throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    Map<String, Object> map = new HashMap<String, Object>();
    String uploadRootPath = getUploadRootPath();
    Long limitSize = 1024 * 1024 * 1024 * 10L;//默认上传路径文件总大小上限10240M

    String set_year = SessionUtil.getLoginYear();
    String create_by = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");

    if (uploadRootPath == null || "".equals(uploadRootPath) || uploadRootPath.trim().isEmpty()) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "上传根目录不存在！");
      resultList.add(map);
      return resultList;
    }
    String filePath = uploadRootPath;
    if (app_id == null || "".equals(app_id) || orgcode == null || "".equals(orgcode)) {
      if (extpath == null || "".equals(extpath) || extpath.trim().isEmpty()) {
        filePath = filePath + "/pub" + "/";
      } else {
        filePath = filePath + "/pub" + "/" + extpath + "/";
      }
    } else {
      if (extpath == null || "".equals(extpath) || extpath.trim().isEmpty()) {
        filePath = filePath + "/" + app_id + "/" + set_year + "/" + orgcode + "/" + getCategoryCode(categoryId) + "/";
      } else {
        filePath = filePath + "/" + app_id + "/" + set_year + "/" + orgcode + "/" + getCategoryCode(categoryId) + "/"
          + extpath + "/";
      }
    }

    if (fileList != null) {
      for (int i = 0; i < fileList.size(); i++) {

        Map<String, Object> mapData = (Map<String, Object>) fileList.get(i);
        Map<String, Object> temp = new HashMap<String, Object>();
        Long attachSize = (Long) mapData.get("attach_size");
        map = checkDiskSize(uploadRootPath, attachSize, limitSize);//检查磁盘空间大小是否符合附件上传需求
        if ("-1".equals((String) map.get("errorCode"))) {
          resultList.add(map);
          return resultList;
        }
        String attachName = (String) mapData.get("attach_name");
        String busiId = (String) mapData.get("busi_id");

        mapData.put("busi_id", busiId);
        String attachType = (String) mapData.get("attach_type");
        byte[] attachFileBytes = (byte[]) mapData.get("bytes");
        File dir = new File(filePath);
        if (!dir.exists()) {
          dir.mkdirs();
        }
        if (dir.isDirectory()) {
          if (!dir.exists()) {//判断文件目录是否存在  
            if (!dir.mkdirs()) {
              map.put("errorCode", "-1");
              map.put("errorMsg", "无法创建：[" + dir + "]");
              resultList.add(map);
              return resultList;
            }
          }
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String fileName = attachName + "=" + timeStamp + "." + attachType;
        StringBuffer sb = new StringBuffer();
        sb.append(filePath).append("/").append(fileName);

        File newfile = new File(sb.toString());
        if (newfile.exists()) {
          map.put("errorCode", "-1");
          map.put("errorMsg", "存在同名文件");
          resultList.add(map);
          return resultList;
        } else {
          System.out.println("loading");
          List<String> typeList = new ArrayList<String>(Arrays.asList(COMPRESS_FILE_TYPE));
          if (typeList.contains(attachType)) { // 如果为压缩文件，不再次进行压缩
            makeFile(attachFileBytes, filePath, fileName);//生成文件
          } else {
            File zipFile = zipFile(attachFileBytes, filePath, fileName);//生成zip文件
            System.out.println(zipFile.getName());
          }
        }
        String create_time = getServerTime();

        boolean hasAttachIdflag = false;
        String attach_id = null;
        if (mapData.containsKey("attach_id") && mapData.get("attach_id") != null
          && !"".equals((String) mapData.get("attach_id"))) {
          hasAttachIdflag = true;
          attach_id = (String) mapData.get("attach_id");
        } else {
          attach_id = getAttachID(); //取得附件ID
        }
        String changeFileFormat = filePath + "/" + attachName + "=" + timeStamp + ".zip";//
        mapData.put("attach_path", changeFileFormat);
        //mapData.put("attach_path", sb);
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
        List attach_list = new ArrayList();
        attach_list.add(mapData);
        temp.put("attachID", attach_id);
        temp.put("attacName", attachName + "." + attachType);
        resultList.add(temp);

        try {
          if (attach_list.size() > 0) {
            if (hasAttachIdflag) {
              String sql = "select ATTACH_ID,STATUS,ATTACH_PATH from SYS_ATTACH_FILE where ATTACH_ID = ?";
              List attachList = dao.findBySql(sql, new Object[] { attach_id });
              int size = attachList.size();
              for (int j = 0; j < size; j++) {
                XMLData data = (XMLData) attachList.get(j);
                String status = (String) data.get("status");
                String attachPath = (String) data.get("attach_path");
                if (status.equals("0")) {
                  deleteFile(attachPath);//删除文件
                }
              }
              mapData.put("category_id", categoryId);
              updateAttachInfo(mapData);
            } else {
              dao.batchSaveDataBySql("sys_attach_file", FIELDS_ALL_ATTACH, attach_list);
            }
          }
          if (categoryId != null && !"".equals(categoryId)) {
            String sql = "select category_id from SYS_ATTACH_FILE where attach_id = ?";
            List categoryIdList = dao.findBySql(sql, new String[] { attach_id });

            if (categoryIdList != null && !categoryIdList.isEmpty()) {
              String sqlStr = "update SYS_ATTACH_FILE set ATTACH_NAME=? , ATTACH_PATH=? , ATTACH_TYPE=? where ATTACH_ID = ? ";
              // dao.executeBySql(sqlStr, new Object[] { attachName, sb.toString(), attachType, attach_id });
              dao.executeBySql(sqlStr, new Object[] { attachName, changeFileFormat, attachType, attach_id });

            }
          }
          sb = null;
        } catch (Exception e) {
          e.printStackTrace();
          throw new Exception("附件上传失败，请联系管理员！");
        }

      }
    }

    return resultList;
  }

  public String getCategoryCode(String categoryId) {
    String categoryCode = null;

    if (categoryId != null && !categoryId.isEmpty()) {
      String sql = "select category_code from sys_category where category_id = ?";
      List list = dao.findBySql(sql, new Object[] { categoryId });
      if (list != null && !list.isEmpty()) {
        categoryCode = (String) ((Map) list.get(0)).get("category_code");
      }
    } else {
      categoryCode = getDefaultCategory();
    }
    return categoryCode;
  }

  public String getDefaultCategory() {
    return "001";
  }

  @Override
  public Map getAttach(String attachId) throws Exception {
    String sql = "select a.* ,b.* from SYS_ATTACH_FILE a, SYS_CATEGORY b where a.category_id = b.category_id and a.status= '0' and a.attach_id=?";
    Map map = (Map) dao.findBySql(sql, new Object[] { attachId }).get(0);
    return map;
  }

  @Override
  public List getAttachList(List attachIds) throws Exception {
    List attachList = new ArrayList();
    for (int i = 0; i < attachIds.size(); i++) {
      String sql = "select a.* ,b.* from SYS_ATTACH_FILE a, SYS_CATEGORY b where a.category_id = b.category_id and a.status= '0' and a.attach_id=?";
      String attach_id = (String) attachIds.get(i);
      List tempList = dao.findBySql(sql, new Object[] { attach_id });
      attachList.add(tempList);
    }
    return attachList;
  }

  @Override
  public boolean deleteAttach(List attachIds) throws Exception {
    boolean executeResult = false;
    String sql = "select ATTACH_ID,STATUS,ATTACH_PATH from SYS_ATTACH_FILE where ATTACH_ID = ?";
    for (int i = 0; i < attachIds.size(); i++) {
      String attachId = (String) attachIds.get(i);
      List fileList = dao.findBySql(sql, new Object[] { attachId });
      int size = fileList.size();
      for (int j = 0; j < size; j++) {
        XMLData data = (XMLData) fileList.get(j);
        String status = (String) data.get("status");
        String attach_id = (String) data.get("attach_id");
        String attachPath = (String) data.get("attach_path");
        if (status.equals("0")) {
          try {
            dao.executeBySql("update SYS_ATTACH_FILE set STATUS='1' where attach_id=? ", new Object[] { attach_id });
            deleteFile(attachPath);
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
        dao.executeBySql("update SYS_ATTACH_FILE set REMARK=? ,update_by=? , update_time=? where attach_id=?",
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
        dao.executeBySql("update SYS_ATTACH_FILE set ATTACH_NAME=? ,update_by=? , update_time=? where attach_id=?",
          new Object[] { attachName, updateBy, updateTime, attachId });
        executeResult = true;
      } catch (Exception e) {
        executeResult = false;
      }
    }
    return executeResult;
  }

  public String getServerTime() {
    Calendar calender = Calendar.getInstance();
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return f.format(calender.getTime());
  }

  public String getAttachID() {
    UUID uuid = UUID.randomUUID();
    String id = uuid.toString();
    return id.toUpperCase();
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
        if (StringUtil.isNull(temp.get("ATTACH_PATH"))) {
          attachMap.put("attach_path", "");
        } else {
          attachMap.put("attach_path", temp.get("ATTACH_PATH").toString());
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
          attachMap.put("updte_time", temp.get("UPDATE_TIME").toString());
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
      // 建立page对象
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
    Enumeration en = pps.propertyNames(); // 得到配置文件的名字
    while (en.hasMoreElements()) {
      String strKey = (String) en.nextElement();
      String strValue = pps.getProperty(strKey);
      map.put(strKey, strValue);
    }
    return map;
  }

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
    if ("".equals(hasAttachId) || hasAttachId.isEmpty() || hasAttachId == null) {
      return false;
    } else {
      sql = "select * from SYS_ATTACH_FILE where attach_id = ?";
      list = dao.findBySql(sql, new String[] { hasAttachId });

      sql = "select category_id from SYS_ATTACH_FILE where attach_id = ?";
      categoryIdList = dao.findBySql(sql, new String[] { hasAttachId });
    }
    try {
      for (int i = 0; i < list.size(); i++) {
        Map map = (Map) list.get(i);
        String attach_id = (String) map.get("attach_id");
        String attach_path = (String) map.get("attach_path");
        String create_time = (String) map.get("create_time");
        String create_by = (String) map.get("create_by");
        String attachNewName = (String) attachInfo.get("attach_name");
        String attach_name = (String) map.get("attach_name");
        String attach_type = (String) map.get("attach_type");
        String app_id = (String) map.get("appid");

        String finalPath = null;
        String finalRootPath = null;
        if (attach_path != null && !"".equals(attach_path)) {
          String prefix = attach_path.substring(attach_path.lastIndexOf("."));
          String[] newPath = attach_path.split(attach_name + "=");
          String realName = attach_name + "=" + newPath[1];
          String timeStamp = String.valueOf(System.currentTimeMillis());
          renameFile(newPath[0], realName, attachNewName + "=" + timeStamp + prefix, attach_type);//重命名文件
          attachInfo.put("attach_path", newPath[0] + attachNewName + "=" + timeStamp + prefix);
          attachInfo.put("attach_type", attach_type);
          finalPath = newPath[0] + attachNewName + "=" + timeStamp + prefix;
        }

        attachInfo.put("attach_name", attachNewName);
        attachInfo.put("create_time", create_time);
        attachInfo.put("create_by", create_by);
        attachInfo.put("attach_id", attach_id);

        attachInfo.put("update_by", update_by);
        attachInfo.put("update_time", update_time);
        attachInfo.put("rg_code", rg_code);
        attachInfo.put("year", set_year);
        attachInfo.put("status", "0");
        attachInfo.put("appid", app_id);
        //更新分类信息
        if (attachInfo.containsKey("category_id") && categoryIdList != null && !categoryIdList.isEmpty()) {
          String newCategoryId = (String) attachInfo.get("category_id");
          String oldCategoryId = (String) ((Map) categoryIdList.get(0)).get("category_id");
          if (!oldCategoryId.equals(newCategoryId) && newCategoryId != null && !newCategoryId.isEmpty()) {
            dao.executeBySql("update SYS_ATTACH_FILE set category_id = ?  where attach_id=?", new Object[] {
              newCategoryId, hasAttachId });
          }
          // attachInfo.remove("category_id");
        }
        //更新附件信息
        dao.deleteDataBySql("SYS_ATTACH_FILE", attachInfo, new String[] { "attach_id" });
        dao.saveDataBySql("SYS_ATTACH_FILE", attachInfo);

        if (attachInfo.containsKey("state")) {
          if ("1".equals((String) attachInfo.get("state"))) {
            dao.executeBySql(
              "update sys_attach_file set ATTACH_NAME=? ,ATTACH_PATH = ? , ATTACH_TYPE ='' where attach_id=?",
              new Object[] { attachNewName, "", hasAttachId });
            deleteFile(finalPath);//删除文件

          }
        }

        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
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
    if (sPath == null || "".equals(sPath)) {
      return flag;
    }
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
  * @param oldname     原来的文件名 
  * @param newname     新文件名 
  * @param attach_type 文件类型 
  */
  public void renameFile(String path, String oldName, String newName, String attach_type) {
    if (!oldName.equals(newName)) {//新的文件名和以前文件名不同时,才有必要进行重命名 
      File oldfile = new File(path + "/" + oldName);
      File newfile = new File(path + "/" + newName);
      if (!oldfile.exists()) {
        return;
      }
      if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名 
        System.out.println(newName + "已经存在！");
      else {
        if (oldName.endsWith(".zip")) {// 压缩文件重命名
          //1.获取新的文件名(压缩文件内部文件)
          String newfileStr = path + "/" + newName.split(".zip")[0] + "." + attach_type;
          //2.解压缩文件,获得其文件路径,并删除原压缩文件
          String unzipStr = unzip(path + "/" + oldName, path);
          oldfile.delete();
          if (!StringUtil.isNull(unzipStr)) {
            //3.重命名解压缩后的文件
            (new File(unzipStr)).renameTo(new File(newfileStr));
            //4.压缩重命名后的文件,并删除原文件
            zipFile2(path, newName.split(".zip")[0] + "." + attach_type);
            new File(newfileStr).delete();
          }
        } else {
          oldfile.renameTo(newfile);
        }
      }
    } else {
      System.out.println("新文件名和旧文件名相同...");
    }
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

  /** 
   * 根据byte数组，生成文件 
   */
  public static void makeFile(byte[] bfile, String filePath, String fileName) {
    BufferedOutputStream bos = null;
    FileOutputStream fos = null;
    File file = null;
    try {
      File dir = new File(filePath);
      if (!dir.exists()) {//判断文件目录是否存在  
        if (!dir.mkdirs()) {
          throw new Exception("无法创建路径：[" + dir + "]");
        }
      }
      file = new File(filePath + "\\" + fileName);
      fos = new FileOutputStream(file);
      bos = new BufferedOutputStream(fos);
      bos.write(bfile);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (bos != null) {
        try {
          bos.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }
  }

  public long getTotalSizeOfFilesInDir(final File file) {
    if (file.isFile())
      return file.length();
    final File[] children = file.listFiles();
    long total = 0;
    if (children != null)
      for (final File child : children)
        total += getTotalSizeOfFilesInDir(child);
    return total;
  }

  /**
   * 检查磁盘空间大小是否符合附件上传需求
   * @param uploadPath 附件上传目录
   * @param attachSize 附件大小
   * @param limitSize 文件夹下总的文件大小上限
   * @return
   */
  public Map<String, Object> checkDiskSize(String uploadPath, long attachSize, long limitSize) {
    Map<String, Object> map = new HashMap<String, Object>();
    String partition = null;
    String os = System.getProperty("os.name");
    if (os != null) {
      os = os.toLowerCase();
    } else {
      map.put("errorCode", "-1");
      map.put("errorMsg", "无法获知操作系统的类型！");
      return map;
    }
    if (os != null && os.indexOf("windows") != -1) {
      String partitionLable = uploadPath.split(":")[0];
      if (partitionLable == null || "".equals(partitionLable) || partitionLable.trim().isEmpty()) {
        map.put("errorCode", "-1");
        map.put("errorMsg", "磁盘分区未知");
        return map;
      }
      partition = uploadPath.split(":")[0] + ":";
    }
    if (os != null && os.indexOf("linux") != -1) {
      partition = uploadPath;
    }

    if (partition == null || "".equals(partition) || partition.trim().isEmpty()) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "磁盘分区未知");
      return map;
    }

    File diskPartition = new File(partition);
    if (!diskPartition.exists()) {//判断文件目录是否存在  
      if (!diskPartition.mkdirs()) {
        map.put("errorCode", "-1");
        map.put("errorMsg", "无法创建目录：[" + partition + "]");
        return map;
      }
    }
    if (!diskPartition.isDirectory()) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "非合法的文件目录：[" + partition + "]");
      return map;
    }

    //long totalCapacity = diskPartition.getTotalSpace();
    // long usablePatitionSpace = diskPartition.getUsableSpace();
    long freePartitionSpace = diskPartition.getFreeSpace();

    if (freePartitionSpace < attachSize) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "该分区磁盘容量不足！");
      return map;
    }

    File file = new File(uploadPath);
    if (!file.exists()) {//判断文件目录是否存在  
      if (!file.mkdirs()) {
        map.put("errorCode", "-1");
        map.put("errorMsg", "无法创建目录：[" + file + "]");
        return map;
      }
    }
    if (!file.isDirectory()) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "非合法的文件目录：[" + file + "]");
      return map;
    }
    long totalSizeOfFilesInDir = getTotalSizeOfFilesInDir(file);
    if (totalSizeOfFilesInDir > limitSize) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "该目录[" + uploadPath + "]下所有文件的总大小(" + totalSizeOfFilesInDir / (1024.0 * 1024) + "M)超出预期("
        + limitSize / (1024.0 * 1024) + "M)限制大小！");
      return map;
    }

    if (attachSize > limitSize - totalSizeOfFilesInDir) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "该附件大小超过上传目录可利用的剩余空间[" + (limitSize - totalSizeOfFilesInDir) / (1024.0 * 1024) + "M]！");
      return map;
    }

    return map;

  }

  /**
   * 检查附件大小是否超出要求
   * @param attachSize 附件大小
   * @return
   */
  public Map<String, Object> checkMaxSize(long attachSize) {
    Map<String, Object> map = new HashMap<String, Object>();
    StringBuffer strSQL = new StringBuffer("select chr_value from sys_userpara" + Tools.addDbLink()
      + " where  chr_code = 'UPLOAD_MAXSIZE' ");
    List list = null;
    try {
      list = dao.findBySql(strSQL.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    Map temp = (Map) list.get(0);
    String size = (String) temp.get("chr_value");
    Long maxSize = Long.parseLong(size) * 1024 * 1024;
    if (attachSize > maxSize) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "您上传的附件超过限制！请上传小于" + size + "M的附件");
      return map;
    }
    return map;

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
    return "R" + str + rannum;// 当前时间
  }

  @Override
  public Map getAttach(String attachId, boolean isAll) throws Exception {
    return getAttach(attachId);
  }

  @Override
  public List<Map<String, Object>> uploadIMattach(List<Map<String, Object>> fileList) throws Exception {

    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> tempCheckSize = new HashMap<String, Object>();
    String uploadRootPath = getUploadRootPath();
    Long limitSize = 1024 * 1024 * 1024 * 10L;//默认上传路径文件总大小上限10240M

    String set_year = SessionUtil.getLoginYear();
    String create_by = (String) SessionUtil.getUserInfoContext().getAttribute("user_id");

    if (uploadRootPath == null || "".equals(uploadRootPath) || uploadRootPath.trim().isEmpty()) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "上传根目录不存在！");
      resultList.add(map);
      return resultList;
    }
    String sep = File.separator;
    String filePath = uploadRootPath;
    filePath = filePath + sep + "message" + sep + create_by + sep + set_year + sep;

    if (fileList != null) {
      for (int i = 0; i < fileList.size(); i++) {

        Map<String, Object> mapData = (Map<String, Object>) fileList.get(i);
        Map<String, Object> temp = new HashMap<String, Object>();
        Long attachSize = (Long) mapData.get("attach_size");

        //附件大小限制检查 从数据库配置进行获取
        tempCheckSize = checkMaxSize(attachSize);
        if ("-1".equals((String) tempCheckSize.get("errorCode"))) {
          resultList.add(tempCheckSize);
          return resultList;
        }

        map = checkDiskSize(uploadRootPath, attachSize, limitSize);//检查磁盘空间大小是否符合附件上传需求
        if ("-1".equals((String) map.get("errorCode"))) {
          resultList.add(map);
          return resultList;
        }
        String attachName = (String) mapData.get("attach_name");
        String attachType = (String) mapData.get("attach_type");
        byte[] attachFileBytes = (byte[]) mapData.get("bytes");
        File dir = new File(filePath);
        if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在  
          if (!dir.mkdirs()) {
            map.put("errorCode", "-1");
            map.put("errorMsg", "无法创建：[" + dir + "]");
            resultList.add(map);
            return resultList;
          }
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String fileName = attachName + "=" + timeStamp + "." + attachType;
        StringBuffer sb = new StringBuffer();
        sb.append(filePath).append("/").append(fileName);

        File newfile = new File(sb.toString());
        if (newfile.exists()) {
          map.put("errorCode", "-1");
          map.put("errorMsg", "存在同名文件");
          resultList.add(map);
          return resultList;
        } else {
          makeFile(attachFileBytes, filePath, fileName);//生成文件
        }

        String attach_id = getAttachID(); //取得附件ID

        mapData.put("time_path", sb);
        mapData.put("attm_id", attach_id);
        mapData.put("file_name", attachName + "." + attachType);
        List attach_list = new ArrayList();
        attach_list.add(mapData);
        temp.put("attachID", attach_id);
        temp.put("attacName", attachName + "." + attachType);
        resultList.add(temp);

        try {
          if (attach_list.size() > 0) {
            dao.batchSaveDataBySql("sys_message_attachment", IM_FIELDS_ALL_ATTACH, attach_list);
          }
          sb = null;
        } catch (Exception e) {
          e.printStackTrace();
          throw new Exception("附件发送失败，请联系管理员！");
        }

      }
    }
    return resultList;
  }

  /**
   * 根据字节码压缩单个文件
   * @param bfile 
   *            待压缩的文件的字节码
   * @param filePath
   *            压缩文件存放的根目录
   * @param fileName
   *            待压缩文件的文件名(带后缀)
   * @return 压缩后的文件
   */
  public File zipFile(byte[] bfile, String filePath, String fileName) {
    InputStream in = new ByteArrayInputStream(bfile);
    BufferedInputStream bis = new BufferedInputStream(in);
    FileOutputStream outStream = null;
    File fileZip = null;
    try {
      int nameIndex = fileName.lastIndexOf(".");
      String namePre = fileName.substring(0, nameIndex);

      //路径不存在时要创建
      File directory = new File(filePath);
      if (!directory.exists()) {
        directory.mkdir();
      }
      //文件输出目录
      fileZip = new File(filePath + File.separator + namePre + ".zip");
      // 文件输出流
      outStream = new FileOutputStream(fileZip);
      int preIndex = fileName.lastIndexOf("=");
      int sufIndex = fileName.lastIndexOf(".");
      String prefix = fileName.substring(0, preIndex);
      String suffix = fileName.substring(sufIndex);
      String fileNameNoTime = prefix + suffix;
      writeZipFile(bis, outStream, filePath, fileNameNoTime);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      closeQuietly(bis, outStream);
    }
    return fileZip;
  }

  /**
   * 根据文件路径压缩单个文件，压缩文件路径与源文件路径一致
   * @param filePath
   *            文件父级目录
   * @param fileName
   *            文件名(带后缀)
   * @return 压缩后的文件
   */
  public File zipFile2(String filePath, String fileName) {
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    FileOutputStream outStream = null;
    File fileZip = null;
    try {
      int nameIndex = fileName.lastIndexOf(".");
      String namePre = fileName.substring(0, nameIndex);
      fis = new FileInputStream(new File(filePath + File.separator + fileName));
      bis = new BufferedInputStream(fis);
      //文件输出目录
      fileZip = new File(filePath + File.separator + namePre + ".zip");
      // 文件输出流
      outStream = new FileOutputStream(fileZip);
      writeZipFile(bis, outStream, filePath, fileName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      closeQuietly(bis, outStream);
    }
    return fileZip;
  }

  /**
   * 根据缓冲流读写压缩文件
   * @param bis 
   *            缓冲输入流
   * @param fos
   *            输出流
   * @param filePath
   *            文件父目录
   * @param fileName
   *            文件名称(带后缀)
   */
  public void writeZipFile(BufferedInputStream bis, FileOutputStream fos, String filePath, String fileName) {
    ZipOutputStream zos = null;
    try {
      File dirFile = new File(filePath);
      if (!dirFile.exists()) {//判断文件目录是否存在  
        if (!dirFile.mkdirs()) {
          throw new Exception("无法创建路径：[" + dirFile + "]");
        }
      }
      // 压缩流
      zos = new ZipOutputStream(fos);
      // 解决中文文件名乱码  
      zos.setEncoding(CHINESE_CHARSET);
      ZipEntry entry = new ZipEntry(fileName);//设置被压缩的文件名
      zos.putNextEntry(entry);
      int index;
      byte[] b = new byte[1 * 1024 * 1024];
      while ((index = bis.read(b, 0, b.length)) != -1) {
        zos.write(b, 0, index); //写出流
      }
      zos.closeEntry();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      closeQuietly(zos, fos);
    }
  }

  /**
   * 解压缩文件
   * @param filePath
   *          压缩文件的路径
   * @param targetPath
   *          项目根路径
   * @return 解压缩文件的全路径
   */
  @SuppressWarnings("unchecked")
  public String unzip(String filePath, String targetPath) {
    //解压缩流
    ZipFile zipFile = null;
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    //解压缩文件路径
    String fileName = null;
    try {
      File src = new File(filePath);
      if (!src.exists()) {//判断文件目录是否存在  
        throw new Exception("文件:" + filePath + "不存在");
      }
      zipFile = new ZipFile(filePath, CHINESE_CHARSET);
      Enumeration<ZipEntry> zipEntries = zipFile.getEntries();
      ZipEntry entry = null;
      while (zipEntries.hasMoreElements()) {
        entry = zipEntries.nextElement();
        fileName = targetPath + System.currentTimeMillis() + entry.getName();
        File target = new File(fileName);
        if (!target.getParentFile().exists()) { // 父级目录不存在，创建文件父目录
          target.getParentFile().mkdirs();
        }
        //缓冲输入流，读入文件
        bis = new BufferedInputStream(zipFile.getInputStream(entry));
        //缓冲输出流，写入文件
        bos = new BufferedOutputStream(new FileOutputStream(target));
        int index;
        byte[] buffer = new byte[1 * 1024 * 1024]; //byte数组接收文件的数据,最大流为1M
        while ((index = bis.read(buffer, 0, buffer.length)) != -1) {
          bos.write(buffer, 0, index);
          bos.flush();
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (zipFile != null) {
          zipFile.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      closeQuietly(bis, bos);
    }
    return fileName;
  }

  /**
   * 关闭一个或多个流对象
   * 
   * @param closeables
   *            可关闭的流对象列表
   */
  public void closeQuietly(Closeable... closeables) {
    try {
      close(closeables);
    } catch (IOException e) {
      e.printStackTrace();
      // do nothing
    }
  }

  /**
   * 关闭一个或多个流对象
   * 
   * @param closeables
   *            可关闭的流对象列表
   * @throws IOException
   */
  public void close(Closeable... closeables) throws IOException {
    if (closeables != null) {
      for (Closeable closeable : closeables) {
        if (closeable != null) {
          closeable.close();
        }
      }
    }
  }

  @Override
  public Map<String, String> previewFile(String filePath, String fileType, String rootPath, HttpServletResponse response)
    throws Exception {
    Map<String, String> htmlMap = null;
    String delFilePath = null;
    List<String> typeList = new ArrayList<String>(Arrays.asList(IMG_FILE_TYPE));
    List<String> compressList = new ArrayList<String>(Arrays.asList(COMPRESS_FILE_TYPE));
    if (compressList.contains(fileType)) { //如果原文件就是压缩文件，则无法预览
      throw new Exception("暂不支持预览压缩文件");
    }
    if (filePath.endsWith(".zip") || filePath.endsWith(".ZIP")) { // 如果为压缩文件 ，需要解压缩处理
      filePath = unzip(filePath, rootPath);
      delFilePath = filePath;
    }
    //文件后缀
    String suffix = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
    suffix = (suffix == null ? null : suffix.toLowerCase());
//    if (typeList.contains(suffix)) { // 处理图片附件
//      response.setContentType("image/jpeg");
//      response.setCharacterEncoding("UTF-8");
//      BufferedInputStream bis = null; // 输入流
//      OutputStream ops = response.getOutputStream(); // 返回数据流
//      try {
//        bis = new BufferedInputStream(new FileInputStream(filePath));
//        byte[] b = new byte[bis.available()]; // 最大只能处理64M
//        bis.read(b);
//        ops.write(b);
//        ops.flush();
//        bis.close();
//      } finally {
//        if (bis != null) {
//          bis.close();
//        }
//      }
//      new File(delFilePath).delete();
//      htmlMap = new HashMap<String, String>();
//    }
    Map<String, Integer> map = new HashMap<String, Integer>();
    map.put("doc", 0);
    map.put("docx", 1);
    map.put("xls", 2);
    map.put("xlsx", 2);
    map.put("ppt", 3);
    map.put("pptx", 4);
    map.put("txt", 5);
    map.put("pdf", 6);
    Integer suffixFlag = map.get(suffix);
    if (typeList.contains(suffix)) { // 处理图片附件
      suffixFlag = 7;
    }
    //java 1.6不支持String比较, 这里通过map实现比较
    switch (suffixFlag) {
    case 0:
      htmlMap = Word2HtmlUtil.doc2Html(filePath, rootPath);
      break;
    case 1:
      htmlMap = Word2HtmlUtil.docx2Html(filePath, rootPath);
      break;
    case 2:
      htmlMap = Excel2HtmlUtil.readExcelToHtml(filePath, true);
      break;
    case 3:
      htmlMap = PPT2HtmlUtil.ppt2Html(filePath, rootPath, "png");
      break;
    case 4:
      htmlMap = PPT2HtmlUtil.pptx2Html(filePath, rootPath, "png");
      break;
    case 5:
      htmlMap = Txt2HtmlUtil.txt2Html(filePath, rootPath);
      break;
    case 6:
      htmlMap = previewPdfAndImg(filePath, rootPath, 0);
      break;
    case 7:
      htmlMap = previewPdfAndImg(filePath, rootPath, 1);
    }
    if (delFilePath != null) {
      new File(delFilePath).delete();
    }
    return htmlMap;
  }

  public Map<String, String> previewPdfAndImg(String filePath, String rootPath, Integer flag) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    final long timeStamp = System.currentTimeMillis();
    File root = new File(rootPath);
    if (!root.exists()) {
      root.mkdirs();
    }
    //源文件
    File srcfile = new File(filePath);
    //输出文件名,添加时间戳，防止多人同时写一个文件
    String fileName = rootPath + timeStamp + srcfile.getName();
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    try {
      bis = new BufferedInputStream(new FileInputStream(srcfile));
      bos = new BufferedOutputStream(new FileOutputStream(fileName));
      byte[] buffer = new byte[2 * 1024 * 1024];
      int index;
      while ((index = bis.read(buffer, 0, buffer.length)) != -1) {
        bos.write(buffer, 0, index);
        bos.flush();
      }
    } catch (Exception e) {
      throw new Exception("pdf读取失败：" + e.getMessage());
    } finally {
      if (bis != null) {
        bis.close();
      }
      if (bos != null) {
        bos.close();
      }
    }
    if (0 == flag) { //pdf
      map.put("typeFlag", "0");
    }
    if (1 == flag) { //图片
      map.put("typeFlag", "1");
    }
    map.put("htmlString", "/html/" + timeStamp + srcfile.getName());
    map.put("srcPath", fileName);
    return map;
  }

  @Override
  public boolean deletePreviewFile(String srcPath) throws Exception {
    File dirFile = new File(srcPath);
    return deleteDir(dirFile);
  }

  /**
   * 递归删除目录下所有文件和子目录下的文件
   * @param dirFile
   *          需删除的文件目录
   * @return
   *          true:删除成功， false:删除失败
   */
  private static boolean deleteDir(File dirFile) {
    if (dirFile.isDirectory()) {
      String[] children = dirFile.list(); // 获取该目录下全部文件
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dirFile, children[i]));
        if (!success) {
          return false;
        }
      }
    }
    return dirFile.delete(); //删除空目录
  }

}
