package gov.df.fap.controller.gl;

import gov.df.fap.api.gl.configure.AccountService;
import gov.df.fap.api.gl.configure.IBusVouAccountService;
import gov.mof.fasp2.gl.account.service.IAccountService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/df/gl/account")
public class AccountController {
	
	@Autowired
	@Qualifier("accountServiceImpl")
	AccountService accountService = null;
	
	@Autowired
	IBusVouAccountService busVouAccountService = null;
	
	@RequestMapping(value = "/getaccount.do", method = RequestMethod.POST)
	  public @ResponseBody
	  List getAccount() {
		List list = new ArrayList();
		list = accountService.allBusVouAccount();
		return list;
	}
	
	@RequestMapping(value = "/getbookset.do", method = RequestMethod.POST)
	  public @ResponseBody
	  List getSt(HttpServletRequest rq) {
		List list = new ArrayList();
		String stId = busVouAccountService.getBookSet();
		Map map = new HashMap();
		if(stId==null||stId==""){
			map.put("msg", "缺少当前年度区划指标账套信息");
		}else{
			map.put("msg", "success");
			map.put("stId", stId);
		}
		list.add(map);
		return list;
	}
	
	@RequestMapping(value = "/getcoa.do", method = RequestMethod.POST)
	  public @ResponseBody
	  List getCoa(HttpServletRequest rq) {
		String st_id = rq.getParameter("st_id");
		List list = new ArrayList();
		list = busVouAccountService.getAllCoa();
		return list;
	}
	@RequestMapping(value = "/updateAccount.do", method = RequestMethod.POST)
	  public @ResponseBody
	  List updateAccount(HttpServletRequest rq) {
		String account = rq.getParameter("account");
		List list = new ArrayList();
		busVouAccountService.updateAccount(account);
		Map map = new HashMap();
		map.put("msg", "保存成功");
		list.add(map);
		return list;
	}
	
	@RequestMapping(value = "/addAccount.do", method = RequestMethod.POST)
	  public @ResponseBody
	  List addAccount(HttpServletRequest rq) {
		String account = rq.getParameter("account");
		
		List list = new ArrayList();
		boolean flag = busVouAccountService.addAccount(account);
		if(flag){
			Map map = new HashMap();
			map.put("msg", "保存成功");
			list.add(map);
		}else{
			Map map = new HashMap();
			map.put("msg", "编码重复，保存失败");
			list.add(map);
		}
		return list;
	}
	
	@RequestMapping(value = "/deleteaccount.do", method = RequestMethod.POST)
	  public @ResponseBody
	  List deleteAccount(HttpServletRequest rq) {
		String account = rq.getParameter("account");
		List list = new ArrayList();
		busVouAccountService.deleteAccount(account);
		Map map = new HashMap();
		map.put("msg", "删除成功");
		list.add(map);
		return list;
	}

}
