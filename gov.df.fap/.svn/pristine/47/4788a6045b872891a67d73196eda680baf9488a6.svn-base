package gov.df.fap.controller.system;

import gov.df.fap.api.fapcommon.IUserSync;
import gov.df.fap.api.sysbilltype.ISysBillType;
import gov.df.fap.bean.system.billtype.BillTypeForm;
import gov.df.fap.bean.user.UserDTO;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 单据类型Controller
 * <p>
 * Created by Hujhb on 2017/3/15.
 */
@Controller
@RequestMapping(value = "/df/billType")
public class BillTypeController {

  private final static Logger logger = LoggerFactory.getLogger(BillTypeController.class);

  @Autowired
  private ISysBillType iSysBillType;

  @Autowired
  private IUserSync iUserSyncManager;

  public String getUserCode(HttpServletRequest request) {
    String tokenid = request.getParameter("tokenid");
    UserDTO uinfo = null;
    try {
      uinfo = (UserDTO) iUserSyncManager.getUser(tokenid);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return uinfo.getUser_code();
  }

  /**
   * 初始化单据类型菜单树
   * 
   * @return
   */
  @RequestMapping(value = "/initTree")
  @ResponseBody
  public Map<String, Object> initTree() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map.put("data", assembleTree(iSysBillType.findAllSysBillTypes()));// 所有的单据类型
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("初始化单据类型菜单树出现异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 根据billtypeId查询单据类型基本信息、打印模版、辅助要素、定值规则、单号规则等信息
   * 
   * @param billtypeId
   * @return
   */
  @RequestMapping(value = "/findOneByBillTypeId")
  @ResponseBody
  public Map<String, Object> findOneByBillTypeId(@RequestParam("billTypeId")
  String billTypeId) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map.put("data", iSysBillType.getOneSysBilltypeById(billTypeId));
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("查询单据类型[" + billTypeId + "]时出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 新增、修改下拉框数据初始化 初始化对应子系统、记账凭证类型、参考录入额度控制类型
   * 
   * @return
   */
  @RequestMapping(value = "/initBaseType")
  @ResponseBody
  public Map<String, Object> initBaseType() {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> datamap = new HashMap<String, Object>();
    try {
      datamap.put("sys", assembleName(iSysBillType.getApp_id(), "sys_name", "sys_id"));// 对应子系统
      datamap.put("busvouTypes", assembleName(iSysBillType.findGlBusvouTypes(), "vou_type_name", "vou_type_code"));// 记账凭证类型
      datamap.put("vouControl", assembleName(iSysBillType.getVou_control(), "sum_type_name", "sum_type_code"));// 参考录入额度控制类型
      map.put("data", datamap);
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("初始化下拉框对应子系统或记账凭证类型或参考录入额度控制类型出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 新增、修改下拉框数据初始化 初始化COA
   * 
   * @return
   */
  @RequestMapping(value = "/initCOA")
  @ResponseBody
  public Map<String, Object> initCOA() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map.put("data", assembleName(iSysBillType.findAllGlCoas(), "coa_name", "coa_code"));// 查找所有的COA
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("初始化下拉框COA出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 新增、修改下拉框数据初始化 初始化单号规则
   * 
   * @return
   */
  @RequestMapping(value = "/initBillnorule")
  @ResponseBody
  public Map<String, Object> initBillnorule() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map.put("data", assembleName(iSysBillType.getBillnorule(), "billnorule_name", "billnorule_code"));// 单号规则
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("初始化下拉框单号规则出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 新增、修改下拉框数据初始化 初始化来源单据类型/去向单据类型
   * 
   * @return
   */
  @RequestMapping(value = "/initBilltype")
  @ResponseBody
  public Map<String, Object> initBilltype(@RequestParam(value = "sysId", required = false)
  String sysId) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (sysId != null) {
        map.put("data", assembleName(iSysBillType.getBilltype(sysId), "billtype_name", "billtype_code"));// 来源单据类型/去向单据类型
        // 两个值相同
      } else {
        map.put("data", assembleName(iSysBillType.getBilltype(), "billtype_name", "billtype_code"));
      }
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("初始化来下拉框单据类型/去向单据类型出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 新增、修改下拉框数据初始化 初始化主单
   * 
   * @return
   */
  @RequestMapping(value = "/initMasterlist")
  @ResponseBody
  public Map<String, Object> initMasterlist() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

      map.put("data", assembleName(iSysBillType.geTable_name(202), "table_name", "table_code"));// 所有主单
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("初始化下拉框主单时出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 新增、修改下拉框数据初始化 初始化明细单
   * 
   * @return
   */
  @RequestMapping(value = "/initDetaillist")
  @ResponseBody
  public Map<String, Object> initDetaillist() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

      map.put("data", assembleName(iSysBillType.geTable_name(201), "table_name", "table_code"));// 所有明细单
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("初始化下拉框明细单时出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 新增、修改下拉框数据初始化 初始化打印模版打印格式
   * 
   * @return
   */
  @RequestMapping(value = "/initPrintFormat")
  @ResponseBody
  public Map<String, Object> initPrintFormat() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

      // 打印模板的打印格式列表
      //map.put("data", iSysBillType.getReport_id());
      map.put("data", iSysBillType.getReport());
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error(" 初始化打印模版打印格式时出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 新增、修改辅助要素下拉框初始化数据
   * 
   * @return
   */
  @RequestMapping(value = "/initassistElements")
  @ResponseBody
  public Map<String, Object> initassistElements() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

      // 辅助要素
      map.put("assistElements", iSysBillType.findAssistSysElements());
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("查询新增和编辑页面辅助要素下拉框初始化数据出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 新增、修改下拉框数据初始化 初始化定值规则
   * 
   * @return
   */
  @RequestMapping(value = "/initvalueset")
  @ResponseBody
  public Map<String, Object> initvalueset() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

      // 辅助要素
      map.put("data", iSysBillType.getsBilltypeAsselesInit());
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("初始化单据类型下拉框定值规则时出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 通过英文名查询中文名
   * @param fieldCode 英文名
   * @return
   */
  @RequestMapping(value = "/findFieldNameByFieldCode")
  @ResponseBody
  public Map<String, Object> findFieldNameByFieldCode(String fieldCode) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map.put("data", iSysBillType.getFieldNameByFieldCode(fieldCode));
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("通过英文名【" + fieldCode + "】查询中文名时出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 打印模版打印格式查询
   * 
   * @param flag
   *            1 包含,2 左包含，3 右包含，4 其他 精确定位        name 输入的名字
   * @return
   */
  @RequestMapping(value = "/findPrintFormat", method = { RequestMethod.GET })
  @ResponseBody
  public Map<String, Object> findPrintFormat(String flag, String name) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

      map.put("data", iSysBillType.findPrintFormat(flag, new String((name).getBytes("iso-8859-1"), "utf-8")));
      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("打印格式查询[" + name + "]时出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 新增
   *
   * @param form
   * @return
   */
  @RequestMapping(value = "/insertOrUpdate", method = { RequestMethod.POST })
  @ResponseBody
  public Map<String, Object> insertBillType(HttpServletRequest request, BillTypeForm form) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String flag = validation(form);
      if (flag != "0") {
        if (flag == "-4") {
          map.put("errorCode", flag);
          map.put("errorMsg", "单据类型编号重复！");
        } else if (flag == "-2" || flag == "-3" || flag == "-5" || flag == "-6" || flag == "-7") {
          if (flag == "-2") {
            map.put("errorCode", "-2");
            map.put("errorMsg", "单据类型名称不能为空！");
          }
          if (flag == "-3") {
            map.put("errorCode", "-3");
            map.put("errorMsg", "单据类型编号不能为空！");
          }
          if (flag == "-5") {
            map.put("errorCode", "-5");
            map.put("errorMsg", "打印模板存在空值！");
          }
          if (flag == "-6") {
            map.put("errorCode", "-6");
            map.put("errorMsg", "辅助要素存在空值！");
          }
          if (flag == "-7") {
            map.put("errorCode", "-7");
            map.put("errorMsg", "定值规则存在空值！");
          }
        }
      } else {
        String userCode = getUserCode(request);
        iSysBillType.insertBillType(form, userCode);
        map.put("errorCode", "0");
        map.put("errorMsg", "保存成功");
      }
    } catch (Exception e) {
      e.printStackTrace();
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  /**
   * 根据billtypeId删除单个单据类型
   * <p>
   * 功能：删除
   *
   * @param billtypeId
   * @return
   */
  @RequestMapping(value = "/delete")
  @ResponseBody
  public Map<String, Object> delete(HttpServletRequest request, BillTypeForm form) {

    Map<String, Object> map = new HashMap<String, Object>();
    try {// 判断是否要级联删除，如果有辅助要素，则需要级联删除

      iSysBillType.delete(form);

      map.put("errorCode", "0");
    } catch (Exception e) {
      logger.error("删除单据类型[" + form.getBillTypeId() + "]时出现未知异常！！", e);
      map.clear();
      map.put("errorCode", "-1");
      map.put("errorMsg", "系统出现异常，请稍后重试");
    }
    return map;
  }

  private List<Map<String, Object>> assembleName(List<Map<String, Object>> list, String name, String code)
    throws Exception {
    List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
    if (list != null || list.size() > 0) {
      for (Map<String, Object> temp : list) {
        temp.put(name, temp.get(code) + " " + temp.get(name));
        newList.add(temp);
      }
    } else {
      newList = list;
    }
    return newList;
  }

  /**
     * 组装树
     * 
     * @param list
     * @return
     * @throws Exception
     */
  private List<Map<String, Object>> assembleTree(List<Map<String, Object>> list) throws Exception {
    // 手动添加单据类型、主单、明细单节点
    Map<String, Object> map1 = new HashMap<String, Object>();
    map1.put("tree_id", "0");
    map1.put("tree_pid", "root");
    map1.put("title", "单据类型");

    Map<String, Object> map2 = new HashMap<String, Object>();
    map2.put("tree_id", "1");
    map2.put("tree_pid", "0");
    map2.put("title", "主单");

    Map<String, Object> map3 = new HashMap<String, Object>();
    map3.put("tree_id", "2");
    map3.put("tree_pid", "0");
    map3.put("title", "明细单");

    List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
    for (Map<String, Object> temp : list) {
      Map<String, Object> treeNode = new HashMap<String, Object>();
      treeNode.put("tree_id", temp.get("billtype_id"));
      treeNode.put("billtype_id", temp.get("billtype_id"));
      treeNode.put("title", (String) temp.get("billtype_code") + " " + (String) temp.get("billtype_name"));
      String billTypeClass = (String) temp.get("billtype_class");
      // 主单
      if ("0".equalsIgnoreCase(billTypeClass)) {
        treeNode.put("tree_pid", "1");
        // 考虑来源
        String from = (String) temp.get("from_billtype_id");
        if (from != null) {
          Map<String, Object> fromMap = new HashMap<String, Object>();
          XMLData data = iSysBillType.getBasicSysBilltypeById(from);
          if (data != null) {
            fromMap.put("tree_id", UUID.randomUUID());
            fromMap.put("billtype_id", from);
            fromMap.put("tree_pid", temp.get("billtype_id"));
            fromMap
              .put("title", (String) data.get("billtype_code") + " " + (String) data.get("billtype_name") + "(来源)");
            treeList.add(fromMap);
          }
        }
        // 考虑去向
        String to = (String) temp.get("to_billtype_id");
        if (to != null) {
          Map<String, Object> toMap = new HashMap<String, Object>();
          XMLData data = iSysBillType.getBasicSysBilltypeById(to);
          if (data != null) {
            toMap.put("tree_id", UUID.randomUUID());
            toMap.put("billtype_id", to);
            toMap.put("tree_pid", temp.get("billtype_id"));
            toMap.put("title", (String) data.get("billtype_code") + " " + (String) data.get("billtype_name") + "(去向)");
            treeList.add(toMap);
          }
        }
        treeList.add(treeNode);
      } else if ("1".equals(billTypeClass)) {// 明细单
        treeNode.put("tree_pid", "2");
        treeList.add(treeNode);
      }

    }

    treeList.add(map1);
    treeList.add(map2);
    treeList.add(map3);
    return treeList;

  }

  /**
  * 校验保存和更新
  * @param  form
  * @return flag：－２：单据类型名称为空；
  * 				－３：单据类型编号为空；
  * 				－４：新增时单据类型编号重复；
  */
  public String validation(BillTypeForm form) {
    String flag = "0";
    try {
      System.out.println("----validation---");
      // 判断定值规则有关字段不能为空
      if (!"".equals(form.getCusRoleData()) && form.getCusRoleData() != null) {
        List cusRoleData = parseList(form.getCusRoleData());
        for (int i = 0; cusRoleData != null && i < cusRoleData.size(); i++) {
          XMLData cusRole = (XMLData) cusRoleData.get(i);
          String fieldCode = (String) cusRole.get("field_code");
          String valuesetType = (String) cusRole.get("valueset_type");
          if ("".equals(fieldCode) || fieldCode == null || "".equals(valuesetType) || valuesetType == null) {
            flag = "-7";
          }
        }
      }
      // 判断辅助要素有关字段不能为空
      if (!"".equals(form.getAssistData()) && form.getAssistData() != null) {
        List AssitData = parseList(form.getAssistData());
        for (int i = 0; AssitData != null && i < AssitData.size(); i++) {
          XMLData assele = (XMLData) AssitData.get(i);
          String eleCode = (String) assele.get("ele_code");
          String levelNum = (String) assele.get("level_num");
          if ("".equals(eleCode) || eleCode == null || "".equals(levelNum) || levelNum == null) {
            flag = "-6";
          }
        }
      }

      // 判断打印模板有关字段不能为空
      if (!"".equals(form.getPrintData()) && form.getPrintData() != null) {
        List printModeData = parseList(form.getPrintData());
        Iterator printModeDataitr = printModeData.iterator();
        while (printModeDataitr.hasNext()) {
          XMLData tmp = (XMLData) printModeDataitr.next();
          String reportId = (String) tmp.get("report_id");
          String enabled = (String) tmp.get("enabled");
          String isDefault = (String) tmp.get("is_default");
          if ("".equals(reportId) || reportId == null || "".equals(enabled) || enabled == null || "".equals(isDefault)
            || isDefault == null) {
            flag = "-5";
          }
        }
      }

      // 判断新增时单据类型编号不能有重复值
      if (!"".equals(form.getBillTypeCode()) && form.getBillTypeCode() != null) {
        if ("".equals(form.getBillTypeId()) || form.getBillTypeId() == null) {
          // 新增
          List<Map<String, Object>> list = iSysBillType.findAllSysBillTypes();
          for (Map<String, Object> map : list) {
            String billtype_code = (String) map.get("billtype_code");
            if (billtype_code.equals(form.getBillTypeCode())) {
              flag = "-4";// 新增时有重复的billtype_code单据类型编号
            }
          }
        }
      }

      // 判断单据类型名称是否为空
      if ("".equals(form.getBillTypeName()) || form.getBillTypeName() == null) {
        flag = "-2";// 单据类型名称为空
      }

      // 判断单据类型编号是否为空
      if ("".equals(form.getBillTypeCode()) || form.getBillTypeCode() == null) {
        flag = "-3"; // 单据类型编号为空
      }

    } catch (Exception e) {
      e.printStackTrace();
      logger.error("校验方法有问题！");
    }
    return flag;
  }

  /**
   * json字符串转换为list对象
   * 
   * @param jsonStr
   * @return
   */
  public List<XMLData> parseList(String jsonStr) {

    List<XMLData> xmlDataList = new ArrayList<XMLData>();
    if (null != jsonStr) {
      xmlDataList = JSON.parseObject(jsonStr, new TypeReference<List<XMLData>>() {
      }.getType());
    }

    return xmlDataList;
  }

}
