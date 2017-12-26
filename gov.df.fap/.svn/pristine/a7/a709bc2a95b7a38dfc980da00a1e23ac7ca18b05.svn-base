package gov.df.fap.service.globalConfigDialog;

import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.api.globalConfigDialog.IGlobalConfigDialogService;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.Fasp2DaoSupport;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.SQLUtil;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.ReflectionUtil;
import gov.df.fap.util.date.DateHandler;
import gov.df.fap.util.xml.ParseXML;
import gov.df.fap.util.xml.XMLData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 标准值设置
 * @author qhd hp yyg  
 * @version 2017-4
 */

@Service
public class GlobalConfigDialogService implements IGlobalConfigDialogService {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDAO;

  @Autowired
  @Qualifier("fasp2.dao.df")
  private Fasp2DaoSupport dao;

  @Autowired
  @Qualifier("sys.dictionaryService")
  private IDictionary iDictionary;

  @Autowired
  TypeOfDB typeOfDB;

  /**
   * 更新过滤条件信息
   * @param moduleId  菜单id   moduleCode 菜单编码   moduleName 菜单名称
   * @param filterInfo 过滤信息
   */
  public Map<String, Object> updateFilter(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String moduleId = request.getParameter("moduleId");
    String moduleCode = request.getParameter("moduleCode");
    String moduleName = request.getParameter("moduleName");
    String filterInfo = request.getParameter("filterInfo");
    Session tmp_session = null;
    try {
      JSONObject jason = JSONObject.fromObject(filterInfo);
      Map<String, Object> map1 = (Map<String, Object>) jason;
      String byteStr = ParseXML.convertObjToXml(map1, "row");
      String year = SessionUtil.getLoginYear();
      String rg_code = SessionUtil.getRgCode();
      String sql = "delete from plan_func_config where module_id = ? and rg_code =? and set_year=? ";
      generalDAO.executeBySql(sql, new String[] { moduleId, rg_code, year });
      Blob blob = null;
      StringBuffer sSql = new StringBuffer();
      if (typeOfDB.isOracle()) {
        sSql.append(" insert into plan_func_config ");
        sSql.append(" (id ,module_id,module_code,module_name,configdata,set_year,last_ver,rg_code) ");
        sSql.append(" values(?,?,?,?,empty_blob(),?,?,?)");
      } else if (typeOfDB.isMySQL()) {
        sSql.append(" insert into plan_func_config ");
        sSql.append(" (id ,module_id,module_code,module_name,configdata,set_year,last_ver,rg_code) ");
        sSql.append(" values(?,?,?,?,'',?,?,?)");
      }
      String returnId = UUIDRandom.generate();
      tmp_session = generalDAO.getSession();
      Connection conn = tmp_session.connection();
      conn.setAutoCommit(false);
      PreparedStatement pstmt;
      pstmt = conn.prepareStatement("select configdata from plan_func_config where id= ? for update");
      pstmt.setString(1, returnId);
      ResultSet rt = pstmt.executeQuery();
      if (!rt.next()) {
        // 1.插入一个空的BLOB值
        while (true) {

          pstmt = conn.prepareStatement(sSql.toString());
          pstmt.setString(1, returnId);
          pstmt.setString(2, moduleId);
          pstmt.setString(3, moduleCode);
          pstmt.setString(4, moduleName);
          pstmt.setInt(5, Integer.parseInt(SessionUtil.getLoginYear()));
          pstmt.setString(6, DateHandler.getDateFromNow(0));

          /******add by fengdz  ***************************/
          pstmt.setString(7, rg_code);
          /******add by fengdz  ***************************/
          //如果插入失败，则说明可能存在重复id
          if (0 != pstmt.executeUpdate()) {
            break;
          }
        }
        pstmt.close();
      }
      // 2.重新取回BLOB字段值赋值
      pstmt = conn.prepareStatement("select configdata from plan_func_config where id= ? for update");
      pstmt.setString(1, returnId);
      ResultSet rt1 = pstmt.executeQuery();
      if (rt1.next()) {
        blob = rt1.getBlob(1);
      }
      if (null != blob) {
        OutputStream out = null;
        out = (OutputStream) ReflectionUtil.invokeMethod(blob, "getBinaryOutputStream", null, null);
        byte[] bytes = byteStr.getBytes();
        out.write(bytes);
        out.close();

        // 3. 再次提交
        pstmt = conn.prepareStatement("update plan_func_config set configdata=? where id=?");
        pstmt.setBlob(1, blob);
        pstmt.setString(2, returnId);
        int num = pstmt.executeUpdate();
        if (num == 0) {
          throw new Exception("更新失败！");
        }

        pstmt.close();
      }
      map.put("flag", "1");
    } catch (Exception e) {
      e.printStackTrace();
      map.put("flag", "0");
    } finally {
      if (tmp_session != null) {
        generalDAO.closeSession(tmp_session);
      }
    }
    return map;
  }

  /**
   * 更新过滤条件信息-主界面
   * @param filterInfo 过滤信息
   */
  public Map<String, Object> updateCommonFilter(HttpServletRequest request, HttpServletResponse response) {
    String filterInfo = request.getParameter("filterInfo");
    JSONObject jason = JSONObject.fromObject(filterInfo);
    Map<String, Object> map1 = (Map<String, Object>) jason;
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    Object[] object = map1.keySet().toArray();
    String sql = "update pay_default_configure set value = ? where key = ? and set_year = ? and rg_code = ?";
    for (int i = 0; i < object.length; i++) {
      generalDAO.executeBySql(sql, new Object[] { map1.get(object[i]), object[i], year, rg_code });

    }
    map.put("flag", "1");
    return map;
  }

  /**
   * 获取要素树
   */
  public Map<String, Object> getElementTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String sql = "select t.chr_id , t.ele_code , " + SQLUtil.replaceLinkChar("t.ele_code ||'  '|| t.ele_name")
      + " elename , '1' parentid from  sys_element t where t.rg_code = ? and t.set_year =? order by t.ele_code ";
    try {
      List list = generalDAO.findBySql(sql, new Object[] { rg_code, year });
      Map<String, Object> map1 = new HashMap<String, Object>();
      map1.put("chr_id", "1");
      map1.put("ele_code", "1");
      map1.put("elename", "全部");
      map1.put("parentid", "0");
      list.add(map1);
      map.put("flag", "1");
      map.put("eledata", list);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", "0");
    }
    return map;
  }

  /**
   * 获取左侧展示菜单树，
   * @param moduleId  菜单id   moduleCode 菜单编码   moduleName 菜单名称
   * @param filterInfo 过滤信息
   */
  public Map<String, Object> getMenuTree(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    InputStream in = null;
    List defaultList = new ArrayList();
    try {
      in = this.getClass().getResourceAsStream("config_items.xml");
      String inStr = IOUtil.readIOString(in);
      XMLData xml = new XMLData();
      xml.loadXML(inStr);
      defaultList = xml.getRecordListByTag("item");

    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtil.closeStream(in);
    }

    String sql = "SELECT * from (SELECT t.guid id, t.code ,t.name , '112' parent ,'/df/fap/standardSetting/planmanager/payPlan.html' panel_class FROM fasp_t_pubmenu t WHERE t.appid = 'df'  AND t.url LIKE '/df/pay/plan/input%'"
      + "  union  "
      + "SELECT t.guid id, t.code ,t.name , '115' parent ,'/df/fap/standardSetting/paymanager/payPlan.html' panel_class FROM fasp_t_pubmenu t WHERE t.appid = 'df'  AND t.url LIKE '/df/pay/centerpay/input%' ) ut order by code";
    Connection conn = null;
    try {
      conn = dao.getConnection();
      Statement stam = conn.createStatement();
      List list = (List) stam.executeQuery(sql);
      list.addAll(defaultList);
      map.put("menulist", list);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    return map;
  }

  /**
   * 初始化（批量录入规则）下拉列表
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> initDropDownList(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    map.put("flag", "1");
    String sql = "select " + SQLUtil.replaceLinkChar("t.code||' '||t.name")
      + " as value,t.code from pl_configure_batch t where t.type='rule_set' and t.rg_code = ? and t.set_year =?";
    try {
      List list = generalDAO.findBySql(sql, new Object[] { rg_code, year });
      map.put("rows", list);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", "0");
    }
    return map;
  }

  /**
   * 初始化基本的配置信息
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> initBasePageInfo(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("flag", "1");
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String belong_page = request.getParameter("belong_page");
    String sql = "select * from pay_default_configure t where t.rg_code = ? and t.set_year =? and t.belong_page=?";
    try {
      List list = generalDAO.findBySql(sql, new Object[] { rg_code, year, belong_page });
      map.put("rows", list);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", "0");
    }

    return map;
  }

  /**
   * （保存）公务卡页面信息
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> savePayCardInfo(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    map.put("flag", "1");
    String commonSetting = request.getParameter("commonSetting");
    String aloneSetting = request.getParameter("aloneSetting");

    try {
      //插入公共的key value
      updateCommonDB(commonSetting);

      JSONObject aloneSettingJson = JSONObject.fromObject(aloneSetting);
      Map tempMap = (Map) aloneSettingJson;

      String model = (String) tempMap.get("model");
      //公务卡模式参数 前三种模式
      if ("1".equals(model) || "2".equals(model) || "3".equals(model)) {
        //公务卡银联IP地址
        String paycardpaycard_bankip = (String) tempMap.get("paycardpaycard_bankip");
        updateAloneDB(paycardpaycard_bankip, "公务卡银联IP地址", "paycardpaycard_bankip");
        //公务卡银联端口
        String paycardpaycard_bankport = (String) tempMap.get("paycardpaycard_bankport");
        updateAloneDB(paycardpaycard_bankport, "公务卡银联端口", "paycardpaycard_bankport");
        //财政区划码   
        String intfdxppaycardPAY_CARD_SRCREGION = (String) tempMap.get("intfdxppaycardPAY_CARD_SRCREGION");
        updateAloneDB(intfdxppaycardPAY_CARD_SRCREGION, "财政区划码", "intfdxppaycardPAY_CARD_SRCREGION");
        //财政机构码  
        String intfdxppaycardPAY_CARD_SRCORGANIZE = (String) tempMap.get("intfdxppaycardPAY_CARD_SRCORGANIZE");
        updateAloneDB(intfdxppaycardPAY_CARD_SRCORGANIZE, "财政机构码", "intfdxppaycardPAY_CARD_SRCORGANIZE");
      } else {
        //公务卡银联IP地址
        String paycardpaycard_bankip = (String) tempMap.get("paycardpaycard_bankip");
        updateAloneDB(paycardpaycard_bankip, "默认银联的区划码", "paycardpaycard_bankip");
        //公务卡银联端口
        String paycardpaycard_bankport = (String) tempMap.get("paycardpaycard_bankport");
        updateAloneDB(paycardpaycard_bankport, "默认银联的机构类型", "paycardpaycard_bankport");
        //财政区划码   
        String intfdxppaycardPAY_CARD_SRCREGION = (String) tempMap.get("intfdxppaycardPAY_CARD_SRCREGION");
        updateAloneDB(intfdxppaycardPAY_CARD_SRCREGION, "发送方区划码", "intfdxppaycardPAY_CARD_SRCREGION");
        //财政机构码  
        String intfdxppaycardPAY_CARD_SRCORGANIZE = (String) tempMap.get("intfdxppaycardPAY_CARD_SRCORGANIZE");
        updateAloneDB(intfdxppaycardPAY_CARD_SRCORGANIZE, "发送方机构类型码", "intfdxppaycardPAY_CARD_SRCORGANIZE");
      }
      //身份证号校验逻辑
      String paycardPAY_CARD_CHECK_IDENTITY = (String) tempMap.get("paycardPAY_CARD_CHECK_IDENTITY");
      if ("1".equals(paycardPAY_CARD_CHECK_IDENTITY)) {
        String pattern = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{2}(\\d|X|x)$;^[1-9]\\d{5}(19|20)\\d{2}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)$;";
        updateSql(pattern, "paycardPAY_CARD_CHECK_IDENTITY");
      } else {
        updateSql("", "paycardPAY_CARD_CHECK_IDENTITY");
      }

    } catch (Exception e) {
      // TODO Auto-generated catch block
      map.put("flag", "0");
      e.printStackTrace();
    }
    return map;
  }

  /**
   * 需要更新两个字段的情况
   * @param tempValue    value字段
   * @param tempDescript 描述字段
   * @param tempKey      plt_key字段
   * @throws Exception
   */
  public void updateAloneDB(String tempValue, String tempDescript, String tempKey) throws Exception {

    try {
      String year = SessionUtil.getLoginYear();
      String rg_code = SessionUtil.getRgCode();
      String tempInsSql = "update pay_default_configure t set t.value = ? ,t.descript = ? where t.plt_key= ? and t.rg_code = ? and t.set_year =?";
      generalDAO.executeBySql(tempInsSql, new String[] { tempValue, tempDescript, tempKey, rg_code, year });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 更新数据库公共方法
   * 适用于文本框id作为key,文本框值作为value的json字符串
   * @param commonSettingStr 前端传回的公共字符串
   */
  public void updateCommonDB(String commonSettingStr) throws Exception {

    try {
      JSONObject commonSettingJson = JSONObject.fromObject(commonSettingStr);
      Map tempMap = (Map) commonSettingJson;

      Iterator<Map.Entry<String, String>> it = tempMap.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<String, String> entry = it.next();
        String tempKey = entry.getKey();
        String tempValue = entry.getValue();

        updateSql(tempValue, tempKey);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 更新sql的公共方法
   * @param tempValue
   * @param tempKey
   * @throws Exception
   */
  public void updateSql(String tempValue, String tempKey) throws Exception {
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    try {
      String tempSql = "update pay_default_configure t set t.value = ? where t.plt_key= ? and t.rg_code = ? and t.set_year =?";
      generalDAO.executeBySql(tempSql, new String[] { tempValue, tempKey, rg_code, year });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * 载入数据
   * @param code
   * @return
   * @throws Exception
   */
  public XMLData loadData(String code) throws Exception {
    Session tmp_session = null;
    try {
      Blob blob = null;
      tmp_session = generalDAO.getSession();
      Connection conn = tmp_session.connection();

      // 取BLOB字段值赋值，暂定为用module_id查询
      PreparedStatement pstmt = conn
        .prepareStatement("select id,module_id,module_code,module_name,billtype_code,bill_code,configdata,sum_type_id from plan_func_config where module_id=? and rg_code=? and set_year=? ");
      pstmt.setString(1, code);
      pstmt.setString(2, SessionUtil.getRgCode());
      pstmt.setString(3, SessionUtil.getLoginYear());
      ResultSet rt = pstmt.executeQuery();
      String id = "";
      String moduleId = "";
      String moduleCode = "";
      String moduleName = "";
      String billtypeCode = "";
      String billCode = "";
      String sumTypeId = "";

      if (rt.next()) {
        id = (String) rt.getObject("id");
        moduleId = rt.getString("module_id");
        moduleCode = rt.getString("module_code");
        moduleName = rt.getString("module_name");
        billtypeCode = (String) rt.getObject("billtype_code");
        billCode = (String) rt.getObject("bill_code");
        blob = rt.getBlob("configdata");
        sumTypeId = (String) rt.getObject("sum_type_id");
      } else {
        return null;
      }

      if (null != blob) {
        int iLength = (int) blob.length();
        byte[] bytes = new byte[iLength];
        blob.getBinaryStream().read(bytes);
        String strD = new String(bytes);

        pstmt.close();
        XMLData data = new XMLData();
        data.put("id", id);
        data.put("moduleId", moduleId);
        data.put("moduleCode", moduleCode);
        data.put("moduleName", moduleName);
        data.put("billtypeCode", billtypeCode);
        data.put("billCode", billCode);
        data.put("configdata", strD);
        data.put("sumTypeId", sumTypeId);

        return data;
      }

      return null;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Exception(ex.getMessage());
    } finally {
      if (tmp_session != null) {
        generalDAO.closeSession(tmp_session);
      }
    }
  }

  /**
   * 初始化过滤界面
   * @param moduleId  菜单id  
   */
  public Map<String, Object> filterInit(HttpServletRequest request, HttpServletResponse response) {
    String moduleId = request.getParameter("moduleId");
    Map<String, Object> map = new HashMap();
    String sql = "select t.ele_name from sys_element t where t.rg_code = ? and t.set_year = ? and t.ele_code = ?";
    String rgCode = SessionUtil.getRgCode();
    String year = SessionUtil.getLoginYear();
    try {
      XMLData data = loadData(moduleId);
      String config = data.getSubObject("configdata").toString();
      data = ParseXML.convertXmlToObj(config);
      Object a = data.getSubObject("specialSql1");
      Object obj = data.getSubObject("control");
      if (obj instanceof XMLData) {
        List list = new ArrayList();
        list.add(obj);
        obj = list;
      }
      List control = new ArrayList();
      if (obj instanceof List) {
        control = (List) obj;
      } else if (obj != null) {
        control.add(obj);
      }
      List eleDet = new ArrayList();
      for (Object tmp : control) {
        Map maptmp = (Map) tmp;
        Object[] object = maptmp.keySet().toArray();
        for (int i = 0; i < object.length; i++) {
          if (maptmp.get(object[i]) instanceof String) {
            String a1 = (String) maptmp.get(object[i]);
          } else if (maptmp.get(object[i]) instanceof Map) {
            eleDet.add(object[i]);
            List list = iDictionary.findEleValues(object[i].toString(), null, null, false, null, null,
              " order by chr_code ");
            List list2 = generalDAO.findBySql(sql, new Object[] { rgCode, year, object[i] });
            Map mapName = (Map) list2.get(0);
            map.put(object[i].toString(), mapName.get("ele_name"));
            Map a2 = (Map) maptmp.get(object[i]);
            Map a1 = new HashMap();
            List listCheck = new ArrayList();
            a1.put("checkEle", listCheck.add(a2));
            a1.put("operFlag", a2.get("operFlag"));
            a1.put("eleNum", a2.get("eleNum"));
            a1.put("specialSql", a2.get("specialSql"));
            map.put(object[i] + "_info", a1);
            map.put(object[i] + "_all", list);
          } else if (maptmp.get(object[i]) instanceof List) {
            eleDet.add(object[i]);
            List list = iDictionary.findEleValues(object[i].toString(), null, null, false, null, null,
              " order by chr_code ");
            List list2 = generalDAO.findBySql(sql, new Object[] { rgCode, year, object[i] });
            Map mapName = (Map) list2.get(0);
            map.put(object[i].toString(), mapName.get("ele_name"));
            List a2 = (List) maptmp.get(object[i]);
            Map a1 = new HashMap();
            a1.put("checkEle", a2);
            a1.put("operFlag", ((Map) a2.get(0)).get("operFlag"));
            a1.put("eleNum", ((Map) a2.get(0)).get("eleNum"));
            a1.put("specialSql", ((Map) a2.get(0)).get("specialSql"));
            map.put(object[i] + "_info", a1);
            map.put(object[i] + "_all", list);
          }
        }
      }
      map.put("initinfo", eleDet);
      map.put("specialSql1", a + "");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return map;
  }

  public Map<String, Object> saveConfig(Map DataMap) {
    // 更新 配置值、显示值
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("flag", "1");
    map.put("msg", "保存成功！");
    String sql = "update pay_default_configure set value=?,show_value=? where plt_key=? and set_year =? and rg_code =? ";
    try {
      Iterator it = DataMap.keySet().iterator();
      while (it.hasNext()) {
        final Object key = it.next();
        final Object showVlaue = DataMap.get(key);
        String value = "";
        if (showVlaue != null) {
          String[] codeName = showVlaue.toString().split("\\s+");
          value = codeName[0];
        }
        Object[] params = new Object[] { value, showVlaue, key, SessionUtil.getLoginYear(), SessionUtil.getRgCode() };
        generalDAO.executeBySql(sql, params);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      map.put("flag", "0");
      map.put("msg", "保存失败，请确认参数是否正确！");
    }
    return map;
  }

  /**
   * 根据billtype_class得到类型
   * @param type
   * @return
   */
  public List loadBilltype(String type) {
    StringBuffer sql = new StringBuffer();
    sql.append("select billtype_code,billtype_name from sys_billtype where ");
    sql.append("(billtype_code like '2%' or billtype_code like '3%') and ");
    sql.append("length(billtype_code)<4 and billtype_class=? and rg_code= ? and set_year=? order by billtype_code");

    List list = generalDAO.findBySql(sql.toString(),
      new String[] { type, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    if (list == null) {
      list = new ArrayList();
    }
    return list;
  }

  /**
   * 保存工资接口配置数据
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> saveSalaryInterface(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    map.put("flag", "1");
    String commonSetting = request.getParameter("commonSetting");
    String aloneSetting = request.getParameter("aloneSetting");
    try {
      //插入公共的key value
      updateCommonDB(commonSetting);

      JSONObject aloneSettingJson = JSONObject.fromObject(aloneSetting);
      Map tempMap = (Map) aloneSettingJson;

      //批量录入规则
      String intfsalarysalaryBatchCode = (String) tempMap.get("intfsalarysalaryBatchCode");

      String tempInsSql = "update pay_default_configure t set t.value = ? ,t.show_value = ? where t.plt_key= ? and t.rg_code = ? and t.set_year =?";
      generalDAO.executeBySql(tempInsSql, new String[] { intfsalarysalaryBatchCode.split(" ")[0],
        intfsalarysalaryBatchCode, "intfsalarysalaryBatchCode", rg_code, year });

      //工资数据的支付方式
      String intfsalarypk = (String) tempMap.get("intfsalarypk");

      generalDAO.executeBySql(tempInsSql, new String[] { intfsalarypk.split(" ")[0], intfsalarypk, "intfsalarypk",
        rg_code, year });

    } catch (Exception e) {
      map.put("flag", "0");
      e.printStackTrace();
    }
    return map;
  }

  /**
   * 保存常规接口配置数据
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> saveConventionalInterface(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    map.put("flag", "1");
    String commonSetting = request.getParameter("commonSetting");
    String aloneSetting = request.getParameter("aloneSetting");
    try {
      //插入公共的key value
      updateCommonDB(commonSetting);

      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String now_time = formatter.format(new Date());

      //删除原有的数据
      String delSQL = "delete from pay_default_configure t where t.belong_page='conventionalInterface_grid' and t.rg_code = ? and t.set_year =?";
      generalDAO.executeBySql(delSQL, new String[] { rg_code, year });
      //插入新的数据
      JSONArray getJsonArray = JSONArray.fromObject(aloneSetting);
      for (int i = 0; i < getJsonArray.size(); i++) {
        Map<String, String> tempMap = (Map<String, String>) getJsonArray.getJSONObject(i);
        int j = i + 1;
        String key = "intf.budget.feedback.verifiers." + j + "th";
        String value = tempMap.get("value");
        String plt_key = key.replace(".", "");
        String descript = tempMap.get("descript");
        insertNewGridData(key, value, plt_key, descript, now_time, rg_code, year);
      }

    } catch (Exception e) {
      map.put("flag", "0");
      e.printStackTrace();
    }
    return map;
  }

  /**
   * 插入核销器配置数据 插入数据库
   * @param key  key值
   * @param value 配置值
   * @param plt_key 配置使用的外键
   * @param descript 描述信息
   * @param now_time 时间
   * @param rg_code 区划
   * @param year 年度
   * @throws Exception
   */
  public void insertNewGridData(String key, String value, String plt_key, String descript, String now_time,
    String rg_code, String year) throws Exception {
    String user_code = (String) SessionUtil.getUserInfoContext().getContext().get("user_code");
    String guid = UUIDRandom.generate();

    String insSql = "insert into pay_default_configure (ID, KEY, VALUE, DESCRIPT, IS_LEAF, PARENT_ID, ENABLE, CREATE_TIME, CREATE_USER, BELONG, RG_CODE, SET_YEAR, PLT_KEY, BELONG_PAGE, SHOW_VALUE,CREATE_USER)"
      + "values (?, ?, ?, ?, 0, null, 0, ?, null, 0, ?, ?, ?, 'conventionalInterface_grid',null,?)";
    generalDAO.executeBySql(insSql, new String[] { guid, key, value, descript, now_time, rg_code, year, plt_key,
      user_code });
  }

  /**
   * 保存银行配置数据
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> saveBankSettingData(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    map.put("flag", "1");
    String commonSetting = request.getParameter("commonSetting");
    String aloneSetting = request.getParameter("aloneSetting");
    try {
      //插入公共的key value
      updateCommonDB(commonSetting);

      JSONObject aloneSettingJson = JSONObject.fromObject(aloneSetting);
      Map tempMap = (Map) aloneSettingJson;

      //批量录入规则
      String clearbankbillNoModifyBankCode = (String) tempMap.get("clearbankbillNoModifyBankCode");

      String tempInsSql = "update pay_default_configure t set t.value = ? ,t.show_value = ? where t.plt_key= ? and t.rg_code = ? and t.set_year =?";
      generalDAO.executeBySql(tempInsSql, new String[] { clearbankbillNoModifyBankCode.split(" ")[0],
        clearbankbillNoModifyBankCode, "clearbankbillNoModifyBankCode", rg_code, year });

    } catch (Exception e) {
      map.put("flag", "0");
      e.printStackTrace();
    }
    return map;
  }

  /**
   * 保存政府采购接口配置数据
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> saveGovProcurementInterfaceData(HttpServletRequest request, HttpServletResponse response) {

    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    map.put("flag", "1");
    String commonSetting = request.getParameter("commonSetting");
    String aloneSetting = request.getParameter("aloneSetting");
    try {
      //插入公共的key value
      updateCommonDB(commonSetting);

      JSONObject aloneSettingJson = JSONObject.fromObject(aloneSetting);
      Map tempMap = (Map) aloneSettingJson;

      //有指标生成支付批量录入规则
      String intfzczcBatchCode = (String) tempMap.get("intfzczcBatchCode");
      //无指标生成支付批量录入规则
      String intfzczcNoBudgetBatchCode = (String) tempMap.get("intfzczcNoBudgetBatchCode");

      String tempInsSql = "update pay_default_configure t set t.value = ? ,t.show_value = ? where t.plt_key= ? and t.rg_code = ? and t.set_year =?";
      generalDAO.executeBySql(tempInsSql, new String[] { intfzczcBatchCode.split(" ")[0], intfzczcBatchCode,
        "intfzczcBatchCode", rg_code, year });

      generalDAO.executeBySql(tempInsSql, new String[] { intfzczcNoBudgetBatchCode.split(" ")[0],
        intfzczcNoBudgetBatchCode, "intfzczcNoBudgetBatchCode", rg_code, year });

    } catch (Exception e) {
      map.put("flag", "0");
      e.printStackTrace();
    }
    return map;
  }

  /**
   * 保存用户参数-支付管理配置数据
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> savePayManagementData(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    map.put("flag", "1");
    String commonSetting = request.getParameter("commonSetting");
    try {
      //插入公共的key value
      updateCommonDB(commonSetting);
    } catch (Exception e) {
      map.put("flag", "0");
      e.printStackTrace();
    }
    return map;
  }

  /**
   * 初始化客户端查询参数配置界面数据
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> initClientQuerySetting(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();

    map.put("flag", "1");
    try {
      String sql = "select t.enable,t.plt_key as plt_key,"
        + (TypeOfDB.isOracle() ? "regexp_substr(t.key,'[^.]+',1,3)"
          : " substring_index(substring_index(t.key, '.', 3), '.', -1) ")
        + " as queryTable, "
        + (TypeOfDB.isOracle() ? "regexp_substr(t.key,'[^.]+',1,4)"
          : "substring_index(substring_index(t.key, '.', 4), '.', -1)")
        + " as field,t.descript as descript,t.value as value  from  pay_default_configure t where t.belong_page='clientQueryParamSetting' and t.rg_code = ? and t.set_year =?";
      List list = generalDAO.findBySql(sql, new Object[] { rg_code, year });
      map.put("rows", list);
    } catch (Exception e) {
      map.put("flag", "0");
      e.printStackTrace();
    }
    return map;
  }

  /**
   * 保存用户参数-支付管理配置数据
   * @param request
   * @param response
   * @return
   */
  public Map<String, Object> saveClientQuerySettingData(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    String year = SessionUtil.getLoginYear();
    String rg_code = SessionUtil.getRgCode();
    String user_code = (String) SessionUtil.getUserInfoContext().getContext().get("user_code");
    map.put("flag", "1");
    String aloneSetting = request.getParameter("aloneSetting");
    try {

      JSONObject aloneSettingJson = JSONObject.fromObject(aloneSetting);
      Map tempMap = (Map) aloneSettingJson;
      String plt_key = (String) tempMap.get("plt_key");
      String enable = (String) tempMap.get("enable");
      String descript = (String) tempMap.get("descript");
      String value = (String) tempMap.get("value");

      String tempInsSql = "update pay_default_configure t set t.value = ? ,t.enable = ?,t.descript=?, t.create_user=? where t.plt_key= ? and t.rg_code = ? and t.set_year =?";
      generalDAO.executeBySql(tempInsSql, new String[] { value, enable, descript, user_code, plt_key, rg_code, year });

    } catch (Exception e) {
      map.put("flag", "0");
      e.printStackTrace();
    }
    return map;
  }

}
