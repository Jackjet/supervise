package gov.df.fap.controller.gl;

import gov.df.fap.api.gl.configure.IBusVouAcctmdlService;
import gov.df.fap.api.rule.IDataRight;
import gov.df.fap.bean.gl.configure.BusVouAcctmdl;

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
@RequestMapping("/df/gl/configure")
public class BusVouAcctmdlController {

  
  @Autowired
  IBusVouAcctmdlService busVouAcctmdlService = null;

  @RequestMapping(value = "/getaccount.do", method = RequestMethod.GET)
  public @ResponseBody
  List getAccount() {
    List list = busVouAcctmdlService.getAccount();
    return list;
  }

  @RequestMapping(value = "/getvoutype.do", method = RequestMethod.GET)
  public @ResponseBody
  List getVouType() {
    List list = busVouAcctmdlService.getVouType();
    return list;
  }

  @RequestMapping(value = "/getacctmdl.do", method = RequestMethod.POST)
  public @ResponseBody
  List getBusVouAcctmdl(HttpServletRequest rq) {

    String vou_type_id = rq.getParameter("vou_type_id");
    long id = Long.parseLong(vou_type_id);
    List list = busVouAcctmdlService.getBusVouAcctmdl(id);
    return list;
  }
  
  @RequestMapping(value = "/getacctmdlbyaccid.do", method = RequestMethod.GET)
  public @ResponseBody
  List<BusVouAcctmdl> getBusVouAcctmdlByAccId(HttpServletRequest rq) {
	  
    String account_id = rq.getParameter("account_id");
    List lists = busVouAcctmdlService.getBusVouAcctmdlByAccId(account_id);
    return lists;
    
  }
  
  @RequestMapping(value = "/getCoaElements.do", method = RequestMethod.POST)
  public @ResponseBody
  List getCoaElements(HttpServletRequest rq) {
	String accountId = rq.getParameter("accountId");  
    List lists = busVouAcctmdlService.getCoaElements(accountId);
    return lists;
    
  }

  @RequestMapping(value = "/delacctmdl.do", method = RequestMethod.POST)
  public @ResponseBody Map<String, Object> delAcctmdl(HttpServletRequest rq) {
	Map<String, Object> map = new HashMap<String, Object>();
    String acctmdl_id = rq.getParameter("acctmdl");
    busVouAcctmdlService.delAcctmdlByAcctmdlId(acctmdl_id);
    map.put("msg", "删除成功");
    return map;
  }

  @RequestMapping(value = "/saveacctmdl.do", method = RequestMethod.POST)
  public @ResponseBody Map<String, Object> saveAcctmdl(HttpServletRequest rq) {
	Map<String, Object> map = new HashMap<String, Object>();
    String busvoutype = rq.getParameter("busvoutype");
    String busvouacctmdl = rq.getParameter("busvouacctmdl");
    try{
    	busVouAcctmdlService.saveAcctmdl(busvoutype, busvouacctmdl);
    }catch(Exception e){
    	map.put("msg", "在途记录-不能保存");
    	return map;
    }
    map.put("msg", "保存成功");
    return map;
  }
  
  @RequestMapping(value = "/getEleValue.do", method = RequestMethod.POST)
  public @ResponseBody Map<String, Object> getEleValue(HttpServletRequest rq,HttpServletResponse rp) {
	Map<String, Object> map = new HashMap<String, Object>();
    try {
      map = busVouAcctmdlService.getEleValue(rq, rp);
      map.put("result", "success");
    } catch (Exception e) {
      e.printStackTrace();
      map.put("result", "fail");
    }
    return map;
  }
  @RequestMapping(value = "/getElements.do", method = RequestMethod.POST)
  public @ResponseBody Map<String, Object> getElements(HttpServletRequest rq,HttpServletResponse rp) {
	Map<String, Object> map = new HashMap<String, Object>();
    try {
      map = busVouAcctmdlService.getElements(rq, rp);
      map.put("result", "success");
    } catch (Exception e) {
      e.printStackTrace();
      map.put("result", "fail");
    }
    return map;
  }
  
  @RequestMapping(value = "/saveacctmdlruleid.do", method = RequestMethod.POST)
  public @ResponseBody Map<String, Object> saveAcctmdlRuleId(HttpServletRequest rq,HttpServletResponse rp) {
	Map<String, Object> map = new HashMap<String, Object>();
    try {
      map = busVouAcctmdlService.saveAcctmdlRuleId(rq, rp);
      map.put("result", "success");
    } catch (Exception e) {
      e.printStackTrace();
      map.put("result", "fail");
    }
    return map;
  }
  
}
