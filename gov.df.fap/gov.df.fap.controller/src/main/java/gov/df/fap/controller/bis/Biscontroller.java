package gov.df.fap.controller.bis;

import gov.df.fap.api.dictionary.bis.IBISInterface;
import gov.df.fap.api.dictionary.bis.IBISaveInterface;
import gov.df.fap.bean.dictionary.dto.BISDTO;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/df/bis")
public class Biscontroller {
  @Autowired
  private IBISInterface ibisInterface;

  @Autowired
  private IBISaveInterface saveInterface;

  @RequestMapping(value = "/saveConfigure.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveConfigure(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Map<String, String> data = new HashMap<String, String>();
      data.put("bis_add", request.getParameter("bis_add"));
      data.put("bis_autoCode", request.getParameter("bis_autoCode"));
      data.put("bis_bi", request.getParameter("bis_bi"));
      data.put("bis_supcoderule", request.getParameter("bis_supcoderule"));
      data.put("bis_en", request.getParameter("bis_en"));
      data.put("bis_mb", request.getParameter("bis_mb"));
      data.put("bis_codemode", request.getParameter("bis_codemode"));
      data.put("bis_coderule", request.getParameter("bis_coderule"));
      data.put("bis_subcoderule", request.getParameter("bis_subcoderule"));
      data.put("bis_supcode", request.getParameter("bis_supcode"));
      ibisInterface.saveBisConfigPara(data);
      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/initConfigure.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> initConfigure(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List data = ibisInterface
        .getBisConfigParas("bis_mb,bis_bm,bis_en,bis_bi,bis_codemode,bis_coderule,bis_supcoderule,bis_supcode,bis_subcoderule,bis_add,bis_autoCode");
      map.put("params", data);
      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/initBisMain.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> initBisMain(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();

    String condition = "";
    String pageIndex = "2";
    String pageRows = "10";
    XMLData queryData = new XMLData();
    String treeCondition = condition;
    String bis_id = request.getParameter("bis_id");
    if (bis_id != null) {
      condition += " and chr_id='" + bis_id + "' ";
    }
    int count = 0;
    try {
      //设置分页
      queryData.put("pageIndex", String.valueOf(pageIndex));
      queryData.put("pageRows", String.valueOf(pageRows));
      queryData.put("condition", condition);
      queryData.put("treeCondition", treeCondition);
      List data = ibisInterface.queryAllBisData(queryData, false);
      map = saveInterface.initPage(request, response);
      map.put("params", data);

      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
	  
  }

  @RequestMapping(value = "/selectBis.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> selectBis(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      map = saveInterface.selectBis(request, response);
      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/saveBisInput.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> saveBisInput(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();

    try {
      List<BISDTO> data = new ArrayList<BISDTO>();
      String bis_id = request.getParameter("bis_id");
      BISDTO bis = new BISDTO();
      bis.copy(saveInterface.getSaveData(request, response));
      String bis_autoCode=ibisInterface.getBisConfigPara("bis_autoCode");
      if("1".equals(bis_autoCode)){
    	  bis.setCHR_CODE(System.currentTimeMillis()+"");
    	  bis.setDISP_CODE(System.currentTimeMillis()+"");
      }
      data.add(bis);
      if (bis_id != null && !"".equals(bis_id)) {
        ibisInterface.updateBisData(bis);
      } else {

        ibisInterface.saveBisDataForInput(data);
      }
      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/editBis.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> editBisInput(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();

    try {
      BISDTO bis = new BISDTO();
      bis.copy(saveInterface.getSaveData(request, response));
      ibisInterface.updateBisData(bis);
      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/deleteBis.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> deleteBis(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = null;

    try {
      String bis_id = request.getParameter("bis_id");
      map = ibisInterface.deleteBisData(bis_id, "001");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/checkBis.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> checkBis(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();

    try {
      map = saveInterface.checkBis(request, response);
      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/getAddConfig.do", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, Object> getAddConfig(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();

    try {
      List configdata = ibisInterface.getBisConfigParas("bis_autoCode,bis_mb,bis_en,bis_bi");
      XMLData bis_add=saveInterface.getBisAdd();
      configdata.add(bis_add);
      map.put("configData", configdata);
      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/getEleTreeFile.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> getEleTreeFile(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> map = new HashMap<String, Object>();

    try {
      map = saveInterface.getEleTreeFile(request, response);

      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
  }

  @RequestMapping(value = "/pageChange.do", method = RequestMethod.POST)
  public @ResponseBody
  Map<String, Object> pageChange(HttpServletRequest request, HttpServletResponse response) {
    Map map = new HashMap();
    try {
      map = saveInterface.pageChange(request, response);
      map.put("flag", 1);
    } catch (Exception e) {
      map.put("flag", 0);
      e.printStackTrace();
    }
    return map;
  }

}
