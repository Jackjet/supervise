package gov.df.fap.controller.datasourceconfig;

import gov.df.fap.api.datasourceconfig.IDatasourceConfig;

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
@RequestMapping("/df/datasourceconfig")
public class DatasourceConfigController {

	@Autowired
	IDatasourceConfig idc = null;
	
	@RequestMapping(value = "/savedatasourceconfig.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveDatasourceConfig(HttpServletRequest rq, HttpServletResponse rp){
		Map map = new HashMap();
		map = idc.saveDatasourceConfig(rq,rp);
		return map;
	}
	
	@RequestMapping(value = "/initdatasourceconfig.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> initDatasourceConfig(HttpServletRequest rq, HttpServletResponse rp){
		Map map = new HashMap();
		map = idc.initDatasourceConfig(rq,rp);
		return map;
	}
	
	@RequestMapping(value = "/deletedatasourceconfig.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteDatasourceConfig(HttpServletRequest rq, HttpServletResponse rp){
		Map map = new HashMap();
		map = idc.deleteDatasourceConfig(rq,rp);
		return map;
	}
	
	@RequestMapping(value = "/initappdata.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAppData(HttpServletRequest rq, HttpServletResponse rp){
		Map map = new HashMap();
		map = idc.getAppData(rq,rp);
		return map;
	}
	
	@RequestMapping(value = "/getdatasourcetree.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getDataSourceTree(HttpServletRequest rq, HttpServletResponse rp){
		Map map = new HashMap();
		map = idc.getDataSourceTree(rq,rp);
		return map;
	}
	
	@RequestMapping(value = "/saveconfigrelations.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveConfigRelations(HttpServletRequest rq, HttpServletResponse rp){
		Map map = new HashMap();
		map = idc.saveConfigRelations(rq,rp);
		return map;
	}
	
	@RequestMapping(value = "/getrelations.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getConfigRelationsByDS(HttpServletRequest rq, HttpServletResponse rp){
		Map map = new HashMap();
		map = idc.getConfigRelationsByDS(rq,rp);
		return map;
	}
	
	@RequestMapping(value = "/testconnection.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> testConnection(HttpServletRequest rq, HttpServletResponse rp){
		Map map = new HashMap();
		map = idc.testConnection(rq,rp);
		return map;
	}
}
