package gov.df.fap.controller.attach;

import gov.df.fap.api.attach.IAttachCategory;
import gov.df.fap.api.attach.IAttachManage;
import gov.df.fap.bean.attach.AttachCategoryForm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

@Controller
@RequestMapping(value = "/df/attach")
public class AttachManageController {
  private static final Logger logger = LoggerFactory.getLogger(AttachManageController.class);

  final String FILE_SYSTEM_MODE = "0";

  final String DATABASE_MODE = "1";

  @Autowired
  @Qualifier("fileSystemAttachManageBO")
  IAttachManage fileSystemAttachManage;

  @Autowired
  @Qualifier("databaseAttachManageBO")
  IAttachManage databaseAttachManage;

  @Autowired
  IAttachCategory attachCategoryBO;

  @RequestMapping(value = "/uploadattach.do", method = { RequestMethod.POST })
  @ResponseBody
  public Map<String, Object> uploadattach(MultipartRequest request, String sys_id, String attachNewCodeValue,
    String selectedSort, String pathNameValue, String attachInfoValue) {
    Map<String, Object> map = new HashMap<String, Object>();

    String appId = sys_id;
    String busiId = attachNewCodeValue;
    String remark = attachInfoValue;
    String categoryId = selectedSort;
    String extpath = pathNameValue;

    String uploadMode = fileSystemAttachManage.getUploadMode();
    String uploadRootPath = fileSystemAttachManage.getUploadRootPath();
    if (uploadMode == null || "".equals(uploadMode) || uploadMode.trim().isEmpty()
      || !(FILE_SYSTEM_MODE.equals(uploadMode) || DATABASE_MODE.equals(uploadMode))) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "上传模式类型无法获知！");
      return map;
    }

    if (uploadRootPath == null || "".equals(uploadRootPath) || uploadRootPath.trim().isEmpty()) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "上传根目录不存在！");
      return map;
    }

    try {
      List<MultipartFile> files = request.getFiles("files");
      if (files != null && !files.isEmpty()) {
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        for (MultipartFile file : files) {

          Map<String, Object> attachInfoMap = new HashMap<String, Object>();

          attachInfoMap.put("remark", remark);
          attachInfoMap.put("busi_id", busiId);//busi_id 业务主键id

          byte[] attachFileBytes = file.getBytes();
          attachInfoMap.put("bytes", attachFileBytes);//附件文件字节码
          long fileSize = file.getSize();
          attachInfoMap.put("attach_size", fileSize);//附件大小
          String originalFilename = file.getOriginalFilename();//获取附件原始名
          String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);//文件类型
          String filenameWithoutSuffix = originalFilename.split("." + suffix)[0];
          String attachType = suffix;
          attachInfoMap.put("attach_type", attachType);
          attachInfoMap.put("attach_name", filenameWithoutSuffix);//文件名

          fileList.add(attachInfoMap);
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (FILE_SYSTEM_MODE.equals(uploadMode)) {
          list = fileSystemAttachManage.uploadattach(fileList, appId, fileSystemAttachManage.getRgCode(), extpath,
            categoryId);
        }
        if (DATABASE_MODE.equals(uploadMode)) {
          list = databaseAttachManage.uploadattach(fileList, appId, databaseAttachManage.getRgCode(), extpath,
            categoryId);
        }
        for (Map<String, Object> m : list) {
          if (m.containsKey("errorCode")) {
            return m;
          }
        }
        map.put("errorCode", "0");
        map.put("data", "上传成功！");

      } else {
        map.put("errorCode", "-1");
        map.put("errorMsg", "上传的文件为空");
      }
      return map;

    } catch (Exception e) {
      map.clear();
      logger.error("文件上传时出现未知异常", e);
      map.put("errorCode", "-1");
      map.put("errorMsg", "文件上传时出现未知异常");
      return map;
    }
  }

  @RequestMapping(value = "/download.do", method = { RequestMethod.POST })
  @ResponseBody
  public Map<String, Object> download(HttpServletRequest request, HttpServletResponse response, String attach_id) {
    String uploadMode = fileSystemAttachManage.getUploadMode();
    Map<String, Object> map = new HashMap<String, Object>();
    Map attach = new HashMap();
    if (FILE_SYSTEM_MODE.equals(uploadMode)) {
      try {
        attach = fileSystemAttachManage.getAttach(attach_id);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      String filePath = (String) attach.get("attach_path");
      File file = new File(filePath);
      //      if (!file.exists()) {
      //        map.put("errorCode", "-1");
      //        map.put("errorMsg", "下载的文件不存在，可能已经被移除！");
      //        return map;
      //
      //      }
      OutputStream out = null;
      InputStream in = null;
      try {
        String fileName = file.getName();

        String prefix = null;
        if (fileName.lastIndexOf(".") != -1) {
          prefix = fileName.substring(fileName.lastIndexOf("."));
        } else {
          map.put("errorCode", "-1");
          map.put("errorMsg", "文件路径不正确！");
          return map;
        }
        fileName = fileName.split("=")[0] + prefix;
        out = response.getOutputStream();
        response.setHeader("Content-Type", "application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename="
          + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        in = new FileInputStream(file);
        byte[] b = new byte[2048];
        int length;
        while ((length = in.read(b)) > 0) {
          out.write(b, 0, length);
          out.flush();
        }
      } catch (Exception e) {
        logger.error("下载文件出现异常[" + filePath + "]", e);
        map.put("errorCode", "-1");
        map.put("errorMsg", "下载文件出现异常");
      } finally {
        try {
          in.close();
          out.close();
        } catch (IOException e) {
          logger.error("关闭数据流出现异常", e);
          map.put("errorCode", "-1");
          map.put("errorMsg", "下载文件出现异常");
        }
      }
    }
    if (DATABASE_MODE.equals(uploadMode)) {
      OutputStream outps = null;
      InputStream inps = null;
      try {
        attach = databaseAttachManage.getAttach(attach_id);
      } catch (Exception e2) {
        e2.printStackTrace();
      }
      try {
        outps = response.getOutputStream();
        String attachName = (String) attach.get("attach_name");
        String attachType = (String) attach.get("attach_type");
        Blob attachBlob = (Blob) attach.get("attach_ob");
        //        if (attachBlob == null) {
        //          map.put("errorCode", "-1");
        //          map.put("errorMsg", "下载的文件不存在，可能已经被移除！");
        //          return map;
        //        }
        inps = new BufferedInputStream(attachBlob.getBinaryStream());
        String fileName = attachName + "." + attachType;
        response.setHeader("Content-Type", "application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename="
          + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        byte[] b = new byte[2048];
        int length;
        while ((length = inps.read(b)) > 0) {
          outps.write(b, 0, length);
          outps.flush();
        }
      } catch (Exception e) {
        try {
          inps.close();
          outps.close();
        } catch (IOException e1) {
          logger.error("关闭数据流出现异常", e);
          map.put("errorCode", "-1");
          map.put("errorMsg", "下载文件出现异常");
          e1.printStackTrace();
        }
        e.printStackTrace();
      }
    }
    return map;
  }

  @RequestMapping(value = "/checkAttachPath.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> checkAttachPath(@RequestParam("attach_id")
  String attachId) {
    String uploadMode = fileSystemAttachManage.getUploadMode();
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> attachMap = new HashMap<String, Object>();
    try {
      if (FILE_SYSTEM_MODE.equals(uploadMode)) {
        attachMap = fileSystemAttachManage.getAttach(attachId);
        String filePath = (String) attachMap.get("attach_path");
        if (filePath == null || "".equals(filePath)) {
          map.put("errorCode", "-1");
          map.put("errorMsg", "下载的地址为空！");
        }
        File file = new File(filePath);
        if (!file.exists()) {
          map.put("errorCode", "-1");
          map.put("errorMsg", "下载的文件不存在，可能已经被移除！");
          return map;

        }
      }
      if (DATABASE_MODE.equals(uploadMode)) {
        attachMap = databaseAttachManage.getAttach(attachId);
        Blob attachBlob = (Blob) attachMap.get("attach_ob");
        if (attachBlob == null) {
          map.put("errorCode", "-1");
          map.put("errorMsg", "下载的文件不存在，可能已经被移除！");
          return map;
        }
      }

      map.put("errorCode", "0");
      // map.put("data", attachMap);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("获取附件信息出现异常", e);
      map.put("errorCode", "-1");
      map.put("errorMsg", "获取附件信息出现异常");
    }
    return map;
  }

  @RequestMapping(value = "/checkUploadCondition.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> checkUploadCondition(@RequestParam("attachSize")
  String attachSize) {
    String uploadMode = fileSystemAttachManage.getUploadMode();
    String uploadRootPath = fileSystemAttachManage.getUploadRootPath();
    Long limitSize = 1024 * 1024 * 1024 * 10L;//默认上传路径文件总大小上限10240M
    Map<String, Object> map = new HashMap<String, Object>();
    if (uploadMode == null || "".equals(uploadMode) || uploadMode.trim().isEmpty()
      || !(FILE_SYSTEM_MODE.equals(uploadMode) || DATABASE_MODE.equals(uploadMode))) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "上传模式类型无法获知！");
      return map;
    }
    try {
      if (FILE_SYSTEM_MODE.equals(uploadMode)) {
        if (uploadRootPath == null || "".equals(uploadRootPath) || uploadRootPath.trim().isEmpty()) {
          map.put("errorCode", "-1");
          map.put("errorMsg", "上传根目录不存在！");
          return map;
        }
        if (attachSize == null || "".equals(attachSize)) {
          attachSize = "0";
        }
        Long fileSize = Long.parseLong(attachSize);
        map = fileSystemAttachManage.checkDiskSize(uploadRootPath, fileSize, limitSize);
        if (map.containsKey("errorCode")) {
          return map;
        }
      }
      map.put("errorCode", "0");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("检测上传环境出现异常", e);
      map.put("errorCode", "-1");
      map.put("errorMsg", "检测上传环境出现异常");
      return map;
    }
    return map;
  }

  @RequestMapping(value = "/getAttach.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> getAttach(@RequestParam("attach_id")
  String attachId) {
    String uploadMode = fileSystemAttachManage.getUploadMode();
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> attachMap = new HashMap<String, Object>();
    try {
      if (FILE_SYSTEM_MODE.equals(uploadMode)) {
        attachMap = fileSystemAttachManage.getAttach(attachId);
      }
      if (DATABASE_MODE.equals(uploadMode)) {
        attachMap = databaseAttachManage.getAttach(attachId);
      }
      map.put("errorCode", "0");
      map.put("data", attachMap);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("获取附件出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "获取附件出现异常");
    }
    return map;
  }

  @RequestMapping(value = "/getAttachList.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> getAttachList(@RequestParam("attachIds[]")
  List<String> attachIds) {
    String uploadMode = fileSystemAttachManage.getUploadMode();
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List attachList = null;
      if (FILE_SYSTEM_MODE.equals(uploadMode)) {
        attachList = fileSystemAttachManage.getAttachList(attachIds);
      }
      if (DATABASE_MODE.equals(uploadMode)) {
        attachList = databaseAttachManage.getAttachList(attachIds);
      }
      map.put("errorCode", "0");
      map.put("data", attachList);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("获取附件出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "获取附件出现异常");
    }
    return map;
  }

  @RequestMapping(value = "/deleteAttach.do", method = { RequestMethod.POST })
  @ResponseBody
  public Map<String, Object> deleteAttach(@RequestParam("attach_id[]")
  List<String> attachIds) {
    String uploadMode = fileSystemAttachManage.getUploadMode();
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (FILE_SYSTEM_MODE.equals(uploadMode)) {
        fileSystemAttachManage.deleteAttach(attachIds);
      }
      if (DATABASE_MODE.equals(uploadMode)) {
        databaseAttachManage.deleteAttach(attachIds);
      }
      map.put("errorCode", "0");
      map.put("data", "删除成功");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("删除文件出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "删除出现异常");
    }
    return map;

  }

  @RequestMapping(value = "/updateRemark.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> updateRemark(@RequestParam("attachIds[]")
  List<String> attachIds, @RequestParam("attachName[]")
  List<String> remark) {
    String uploadMode = fileSystemAttachManage.getUploadMode();
    Map<String, Object> map = new HashMap<String, Object>();
    List<Map> list = new ArrayList<Map>();
    Map<String, Object> tempMap = new HashMap<String, Object>();
    for (int i = 0; i < attachIds.size(); i++) {
      tempMap.put("attachId", attachIds.get(i));
      tempMap.put("remark", remark.get(i));
      list.add(tempMap);
    }
    try {

      if (FILE_SYSTEM_MODE.equals(uploadMode)) {
        fileSystemAttachManage.updateRemark(list);
      }
      if (DATABASE_MODE.equals(uploadMode)) {
        databaseAttachManage.updateRemark(list);
      }
      map.put("errorCode", "0");
      map.put("data", "更新附件说明成功");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("更新附件说明出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "更新附件说明出现异常");
    }
    return map;
  }

  @RequestMapping(value = "/updateAttachName.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> updateAttachName(@RequestParam("attachIds[]")
  List<String> attachIds, @RequestParam("attachName[]")
  List<String> attachName) {
    String uploadMode = fileSystemAttachManage.getUploadMode();
    Map<String, Object> map = new HashMap<String, Object>();
    List<Map> list = new ArrayList<Map>();
    Map<String, Object> tempMap = new HashMap<String, Object>();
    for (int i = 0; i < attachIds.size(); i++) {
      tempMap.put("attachId", attachIds.get(i));
      tempMap.put("attachName", attachName.get(i));
      list.add(tempMap);
    }
    try {
      if (FILE_SYSTEM_MODE.equals(uploadMode)) {
        fileSystemAttachManage.updateAttachName(list);
      }
      if (DATABASE_MODE.equals(uploadMode)) {
        databaseAttachManage.updateAttachName(list);
      }
      map.put("errorCode", "0");
      map.put("data", "更新附件名成功");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("更新附件名出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "更新附件名出现异常");
    }
    return map;
  }

  /**
   * 根据附件分类号删除附件分类信息
   * @param attachCategroyId
   * @return
   */
  @RequestMapping(value = "/deleteAttachCategory.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> deleteAttachCategory(@RequestParam("sortnumber")
  String attachCategroyId) {
    Map<String, Object> map = new HashMap<String, Object>();
    String uploadMode = fileSystemAttachManage.getUploadMode();
    try {
      attachCategoryBO.deleteAttachCategory(attachCategroyId, uploadMode);
      map.put("errorCode", "0");
      map.put("data", "删除分类成功");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("更新分类出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "删除分类出现异常");
    }
    return map;
  }

  /**
   * 更新附件分类信息
   * @param attachCategory
   * @return
   */
  @RequestMapping(value = "/updateAttachCategory.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> updateAttachCategory(String category_id, String category_code, String category_name,
    String remark) {
    Map<String, Object> map = new HashMap<String, Object>();
    AttachCategoryForm attachCategory = new AttachCategoryForm();
    attachCategory.setCategory_id(category_id);
    attachCategory.setCategory_code(category_code);
    attachCategory.setCategory_name(category_name);
    attachCategory.setRemark(remark);
    try {
      attachCategoryBO.updateAttachCategory(attachCategory);
      map.put("errorCode", "0");
      map.put("data", "更新附件分类信息成功");

    } catch (Exception e) {
      e.printStackTrace();
      logger.error("更新附件分类信息出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "更新附件分类信息出现异常");
    }
    return map;
  }

  /**
   * 保存附件分类信息
   * @param attachCategory
   * @return
   */
  @RequestMapping(value = "/saveAttachCategory.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> saveAttachCategory(@RequestParam("sortCodeValue")
  String category_code, @RequestParam("sortNameValue")
  String category_name, @RequestParam("sortInfoValue")
  String remark, String sys_id) {
    AttachCategoryForm attachCategory = new AttachCategoryForm();
    String categroy_id = generateId();
    attachCategory.setCategory_id(categroy_id);
    attachCategory.setCategory_code(category_code);
    attachCategory.setCategory_name(category_name);
    attachCategory.setRemark(remark);
    attachCategory.setAppid(sys_id);
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      attachCategoryBO.saveAttachCategory(attachCategory);
      map.put("errorCode", "0");
      map.put("data", "保存附件分类信息成功");
      map.put("sortnumber", categroy_id);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("保存附件分类信息出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "保存附件分类信息出现异常");
    }
    return map;
  }

  /**
   * 根据附件分类号查询附件
   * @param attachCategoryId
   * @return
   */
  @RequestMapping(value = "/findAttachByCategoryIdNoPageData.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> findAttachByCategoryId(@RequestParam("sortnumber")
  String attachCategoryId) {
    Map<String, Object> map = new HashMap<String, Object>();
    List attachList = new ArrayList();
    String uploadMode = fileSystemAttachManage.getUploadMode();
    try {
      attachList = attachCategoryBO.findAttachByCategoryId(attachCategoryId, uploadMode);
      int count = 0;
      if (FILE_SYSTEM_MODE.equals(uploadMode)) {
        count = fileSystemAttachManage.getDataCount("SYS_ATTACH_FILE");
      }
      if (DATABASE_MODE.equals(uploadMode)) {
        count = databaseAttachManage.getDataCount("SYS_ATTACH_DB");
      }
      map.put("count", count);
      map.put("errorCode", "0");
      map.put("data", attachList);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("获取附件出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "获取附件出现异常");
    }
    return map;
  }

  /**
   * 获取所有附件分类
   */
  @RequestMapping(value = "/getAttachCategroy.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> getAllAttachCategory(String sys_id) {
    Map<String, Object> map = new HashMap<String, Object>();
    List categroyList = new ArrayList();
    try {
      categroyList = attachCategoryBO.getAllAttachCategory(sys_id);
      map.put("errorCode", "0");
      map.put("data", categroyList);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("获取附件出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "获取附件出现异常");
    }
    return map;

  }

  @RequestMapping(value = "/findAttachByCategoryId.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> GetPageJsonData(@RequestParam("sortnumber")
  String categoryId, @RequestParam(value = "pageStart")
  String pageNumber, @RequestParam(value = "pageSize")
  int pageSize, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
    Map<String, Object> map = new HashMap<String, Object>();
    String uploadMode = fileSystemAttachManage.getUploadMode();
    String sql = null;
    if (FILE_SYSTEM_MODE.equals(uploadMode)) {
      sql = "select a.*  from SYS_ATTACH_FILE a, SYS_CATEGORY b where a.category_id = b.category_id and a.status= '0' and b.category_id='"
        + categoryId + "'";
    }
    if (DATABASE_MODE.equals(uploadMode)) {
      sql = "select a.ATTACH_ID, a.BUSI_ID, a.ATTACH_NAME, a.ATTACH_TYPE, a.STATUS, a.APPID, a.YEAR, a.RG_CODE, a.REMARK, a.CREATE_BY, a.CREATE_TIME, a.UPDATE_BY, a.UPDATE_TIME, a.CATEGORY_ID, a.EXT1, a.EXT2, a.EXT3   from SYS_ATTACH_DB a, SYS_CATEGORY b where a.category_id  = b.category_id and a.status= '0' and b.category_id='"
        + categoryId + "'";
    }
    PageImpl pageimpl = null;
    int pageNumber2 = Integer.parseInt(pageNumber) + 1;
    try {
      pageimpl = fileSystemAttachManage.getPageData(sql, pageNumber2, pageSize);
      map.put("errorCode", "0");
      map.put("data", pageimpl);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("获取分页数据出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "获取分页数据出现异常");
    }
    return map;
  }

  @RequestMapping(value = "/updateAttachInfo.do", method = { RequestMethod.POST })
  @ResponseBody
  public Map<String, Object> updateAttachInfo(MultipartRequest request, @RequestParam("attachCodeValue")
  String busi_id, @RequestParam("attachNameValue")
  String attach_name, String attach_id, String selectedSort) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map attachInfo = new HashMap();
    String uploadMode = fileSystemAttachManage.getUploadMode();
    String uploadRootPath = fileSystemAttachManage.getUploadRootPath();
    if (uploadMode == null || "".equals(uploadMode) || uploadMode.trim().isEmpty()
      || !(FILE_SYSTEM_MODE.equals(uploadMode) || DATABASE_MODE.equals(uploadMode))) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "上传模式类型无法获知！");
      return map;
    }

    if (uploadRootPath == null || "".equals(uploadRootPath) || uploadRootPath.trim().isEmpty()) {
      map.put("errorCode", "-1");
      map.put("errorMsg", "上传根目录不存在！");
      return map;
    }

    String attachId = attach_id;

    if (FILE_SYSTEM_MODE.equals(uploadMode)) {
      try {
        attachInfo = fileSystemAttachManage.getAttach(attachId);

      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }

    if (DATABASE_MODE.equals(uploadMode)) {
      try {
        attachInfo = databaseAttachManage.getAttach(attachId, true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    String appId = (String) attachInfo.get("appid");
    String rgCode = (String) attachInfo.get("rg_code");
    String busiId = busi_id;
    //   String remark = attachInfoValue;
    String categoryId = (String) attachInfo.get("category_id");
    categoryId = selectedSort;

    String extpath = null;
    if (FILE_SYSTEM_MODE.equals(uploadMode)) {
      String attachPath = (String) attachInfo.get("attach_path");
      if (attachPath != null && !"".equals(attachPath)) {
        String fileDir = attachPath.split("//")[0];
        String[] splitStr = fileDir.split(uploadRootPath + "/" + appId + "/" + rgCode + "/");
        if (splitStr.length == 2) {
          extpath = fileDir.split(uploadRootPath + "/" + appId + "/" + rgCode + "/")[1];//原自定义路径
        }
      }
    }

    try {
      List<MultipartFile> files = request.getFiles("files");
      if (files != null && !files.isEmpty()) {
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        for (MultipartFile file : files) {

          Map<String, Object> attachInfoMap = new HashMap<String, Object>();

          //attachInfoMap.put("remark", remark);
          attachInfoMap.put("busi_id", busiId);
          attachInfoMap.put("attach_id", attach_id);
          byte[] attachFileBytes = file.getBytes();
          attachInfoMap.put("bytes", attachFileBytes);//附件文件字节码
          long fileSize = file.getSize();
          attachInfoMap.put("attach_size", fileSize);//附件大小
          String originalFilename = file.getOriginalFilename();//获取附件原始名
          String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);//文件类型
          String filenameWithoutSuffix = originalFilename.split("." + suffix)[0];
          String attachType = suffix;
          attachInfoMap.put("attach_type", attachType);
          //attachInfoMap.put("attach_name", filenameWithoutSuffix);//文件名
          attachInfoMap.put("attach_name", attach_name);

          fileList.add(attachInfoMap);
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (FILE_SYSTEM_MODE.equals(uploadMode)) {
          list = fileSystemAttachManage.uploadattach(fileList, appId, fileSystemAttachManage.getRgCode(), extpath,
            categoryId);
        }
        if (DATABASE_MODE.equals(uploadMode)) {
          list = databaseAttachManage.uploadattach(fileList, appId, databaseAttachManage.getRgCode(), extpath,
            categoryId);
        }
        for (Map<String, Object> m : list) {
          if (m.containsKey("errorCode")) {
            return m;
          }
        }
        map.put("errorCode", "0");
        map.put("data", "上传成功！");
      } else {
        map.put("errorCode", "-1");
        map.put("errorMsg", "上传的文件为空");
      }
      return map;

    } catch (Exception e) {
      map.clear();
      logger.error("文件上传时出现未知异常", e);
      map.put("errorCode", "-1");
      map.put("errorMsg", "文件上传时出现未知异常");
      return map;
    }
  }

  /**
   * 更新附件信息
   * @param attachCategory
   * @return
   */
  @RequestMapping(value = "/updateAttachInfoWithoutAttach.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> updateAttachInfo(@RequestParam("attachCodeValue")
  String busi_id, @RequestParam("attachNameValue")
  String attach_name, String attach_id, String selectedSort, String stauts) {
    String uploadMode = fileSystemAttachManage.getUploadMode();
    Map<String, Object> map = new HashMap<String, Object>();

    Map data = new HashMap();
    data.put("busi_id", busi_id);

    // xmlData.put("remark", remark);
    data.put("attach_name", attach_name);
    data.put("category_id", selectedSort);
    data.put("attach_id", attach_id);
    data.put("state", stauts);

    try {
      if (FILE_SYSTEM_MODE.equals(uploadMode)) {
        fileSystemAttachManage.updateAttachInfo(data);
      }
      if (DATABASE_MODE.equals(uploadMode)) {
        databaseAttachManage.updateAttachInfo(data);
      }
      map.put("errorCode", "0");
      map.put("data", "更新附件信息成功");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("更新附件信息出现异常", e);
      map.put("errorCode", "1");
    }
    return map;
  }

  @RequestMapping(value = "/previewFile.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> previewFile(HttpServletRequest request, HttpServletResponse response, String attach_id) {
    ServletContext sc = request.getSession().getServletContext();
    Map<String, Object> result = new HashMap<String, Object>();
    String uploadMode = fileSystemAttachManage.getUploadMode();
    /*String rootPath = sc.getRealPath("html") + File.separator;
    String relativelyPath=System.getProperty("user.dir"); 
    URL a = Thread.currentThread().getContextClassLoader().getResource("");
    String b = a.toString();
    String d = sc.getResource("/") + "html" + File.separator;*/
    Map attach = new HashMap();
    if (FILE_SYSTEM_MODE.equals(uploadMode)) {
      try {
        attach = fileSystemAttachManage.getAttach(attach_id);
        String filePath = (String) attach.get("attach_path");
        String fileType = (String) attach.get("attach_type");
        if (filePath == null || "".equals(filePath)) {
          result.put("errorCode", "-3");
          result.put("message", "预览文件的地址为空！");
          return result;
        }
        File file = new File(filePath);
        if (!file.exists()) {
          result.put("errorCode", "-4");
          result.put("message", "预览文件不存在，可能已经被移除！");
          return result;
    
        }
        String rootPath = sc.getResource("/").getPath() + "html" + File.separator;
        Map htmlMap = fileSystemAttachManage.previewFile(filePath, fileType, rootPath, response);
        result.put("errorCode", "0");
        result.put("data", htmlMap);
      } catch (Exception e) {
        e.printStackTrace();
        result.put("errorCode", "-1");
        result.put("message", e.getMessage() + " 预览失败，请升级jdk后重试");
      }
    } else {
      result.put("errorCode", "-2");
      result.put("message", "大字段当前无法预览");
    }
    return result;
  }

  @RequestMapping(value = "/closePreview.do", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> closePreview(HttpServletRequest request, String filePath) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (fileSystemAttachManage.deletePreviewFile(filePath)) {
        result.put("errorCode", "0");
      } else {
        result.put("errorCode", "-1");
        result.put("message", "预览产生文件删除失败");
      }
    } catch (Exception e) {
      result.put("errorCode", "-2");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 获取所有sys_id
   */
  @RequestMapping(value = "/getSysApp.do", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> getSysApp() {
    Map<String, Object> map = new HashMap<String, Object>();
    List sysAppList = new ArrayList();
    try {
      sysAppList = fileSystemAttachManage.getSysApp();
      map.put("errorCode", "0");
      map.put("data", sysAppList);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("获取sysApp出现异常", e);
      map.put("errorCode", "1");
      map.put("errorMsg", "获取sysApp出现异常");
    }
    return map;

  }

  /**
   * 取得一个id
   * @return 一个UUID
   */
  public static String generateId() {
    UUID uuid = UUID.randomUUID();
    String id = uuid.toString();
    return id.toUpperCase();
  }

}
