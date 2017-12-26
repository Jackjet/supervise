package gov.df.supervise.controller.upgrade;

import gov.df.supervise.api.attachment.AttachmentService;
import gov.df.supervise.api.upgrade.csofUpgradeService;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 补丁升级
 * @author tongya
 *
 */
@Controller
@RequestMapping(value = "/df/csofupgrade")
public class csofUpgradeController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private csofUpgradeService csofUpgradeService;

  @Autowired
  private AttachmentService attachmentService; //附件共用Service

  //查询补丁列表
  @RequestMapping(value = "/getUpgrade.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getUpgrade(HttpServletRequest request) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      List data = csofUpgradeService.getUpgrade();
      Iterator it = data.iterator();
      while (it.hasNext()) {
        Map map = (Map) it.next();
        int isCommit = Integer.parseInt(map.get("ISCOMMIT").toString());
        if (0 == isCommit) {
          map.put("ISCOMMIT", "待升级");
        } else if (1 == isCommit) {
          map.put("ISCOMMIT", "已升级");
        }
      }
      result.put("errorCode", 0);
      result.put("flag", true);
      result.put("totalElements", data.size());
      result.put("dataDetail", data);
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "查询数据失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
    }
    return result;
  }

  //通过条件查询补丁列表
  @RequestMapping(value = "/getUpgradeByIscommit.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> getUpgradeByIscommit(HttpServletRequest request) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    int iscommit = Integer.parseInt(request.getParameter("iscommit"));
    try {
      List data = csofUpgradeService.getUpgradeByIscommit(iscommit);
      Iterator it = data.iterator();
      while (it.hasNext()) {
        Map map = (Map) it.next();
        int isCommit = Integer.parseInt(map.get("ISCOMMIT").toString());
        if (0 == isCommit) {
          map.put("ISCOMMIT", "待升级");
        } else if (1 == isCommit) {
          map.put("ISCOMMIT", "已升级");
        }
      }
      result.put("errorCode", 0);
      result.put("flag", true);
      result.put("totalElements", data.size());
      result.put("dataDetail", data);
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "查询数据失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
    }
    return result;
  }

  //删除待升级的补丁
  @RequestMapping(value = "/deleteUpgrade.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> deleteUpgrade(HttpServletRequest request) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    String id = request.getParameter("id");
    String file_id = request.getParameter("file_id");
    try {
      csofUpgradeService.deleteUpgrade(id);
      int num = attachmentService.deleteAttachments(file_id);
      result.put("data", "成功删除【" + num + "】条记录");
      result.put("errorCode", 0);
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "删除数据失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
    }
    return result;
  }

  //上传
  @RequestMapping(value = "/saveUpgrade.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> saveUpgrade(HttpServletRequest request) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    String id = request.getParameter("id");
    String remark = null != request.getParameter("remark") ? request.getParameter("remark") : "";
    try {
      csofUpgradeService.saveUpgrade(id, remark);
      result.put("errorCode", 0);

    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("errorMsg", "新增数据失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
    }
    return result;
  }

  //执行
  @RequestMapping(value = "/doUpgrade.do", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> doUpgrade(HttpServletRequest request) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    String attachId = request.getParameter("file_id");
    String id = request.getParameter("id");
    String usercode = request.getParameter("usercode");
    String pwd = null != request.getParameter("pwd") ? request.getParameter("pwd") : "";
    Map<String, Object> map = csofUpgradeService.getUpdatePswd(usercode, pwd);
    try {
      if (map.get("errorCode").equals("0")) {
        Map fileData = attachmentService.getFileById(attachId);
        if (fileData.get("errorCode").equals("0")) {
          String isTempFile = fileData.get("isTempFile").toString();
          String filePath = fileData.get("localPath").toString();
          String fileName = fileData.get("fileName").toString();
          File file = new File(filePath);
          csofUpgradeService.doUpgrade(id, filePath);
          if (isTempFile.equals("1")) {
            file.delete();//删除临时文件
          }
          result.put("errorCode", 0);
          result.put("message", "执行开始");
          result.put("message1", "执行进行中");
          result.put("message2", "执行完成");
        }
      } else {
        result.put("errorCode", "-1");
        result.put("errorMsg", "密码错误，");
      }
    } catch (Exception e) {
      result.put("errorCode", -1);
      result.put("message", "执行开始");
      result.put("errorMsg", "执行数据失败");
      result.put("message", "异常"); // zfn 未来改成标准消息异常
    }
    return result;
  }
}
