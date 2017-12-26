package gov.df.fap.controller.billnorule;

import gov.df.fap.api.dictionary.interfaces.IDictionary;
import gov.df.fap.api.rule.ISysBillNoRule;
import gov.df.fap.api.sysbilltype.ISysBillType;
import gov.df.fap.api.util.ISysAppUtil;
import gov.df.fap.bean.system.billnorule.BillnoRuleForm;
import gov.df.fap.bean.system.billnorule.BillnoRuleLines;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 单号规则
 * 
 * @author songlr3
 * 
 */
@Controller
@RequestMapping(value = "/df/billNoRule")
public class SysBillnoController {

	@Autowired
	ISysBillNoRule iSysBillNoRule;

	@Autowired
	ISysAppUtil iSysAppUtil;

	@Autowired
	IDictionary iDictionary;

	@Autowired
	ISysBillType sysBillTypeService;

	/**
	 * 查询所有单号规则数据
	 * 
	 * @表sys_billnorule
	 * @return
	 */
	@RequestMapping(value = "/list/findAllSysRules.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findAllSysRules(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		List data = new ArrayList();
		try {
			data = iSysBillNoRule.findAllSysBillNoRules();
			result.put("data", data);
			result.put("errorCode", 0);
		} catch (Exception e) {
			result.put("errorCode", -1);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询所有应用系统数据
	 * 
	 * @表sys_app
	 * @return
	 */
	@RequestMapping(value = "/list/findAllSysApp.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findAllSysApp(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		List data = new ArrayList();
		try {
			data = iSysAppUtil.findAllSysApps();
			result.put("data", data);
			result.put("errorCode", 0);
		} catch (Exception e) {
			result.put("errorCode", -1);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 页面确定按钮
	 * 
	 * @表SYS_BILLNORULELINE 和SYS_BILLNORULEELE
	 */
	@RequestMapping(value = "/ruleItem/saveorUpdateSysBillNoRule.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveorUpdateSysBillNoRule(
			@RequestBody BillnoRuleForm billnoruleform) {
		Map<String, Object> result = new HashMap<String, Object>();
		XMLData xmlData = getXmlData(billnoruleform);
		if ("".equals(xmlData.get("billnorule_id"))
				|| xmlData.get("billnorule_id") == null) {
			xmlData.put("billnorule_id", SysBillnoController.generate());
		}
		List<BillnoRuleLines> billnoRuleLines = (List<BillnoRuleLines>) xmlData
				.get("billnorulelines");
		for (int i = 0; i < billnoRuleLines.size(); i++) {
			if ("".equals(billnoRuleLines.get(i).getBillnoruleline_id())
					|| billnoRuleLines.get(i).getBillnoruleline_id() == null) {
				billnoRuleLines.get(i).setBillnoruleline_id(
						SysBillnoController.generate());
			}
		}
		try {
			iSysBillNoRule.saveorUpdateSysBillNoRule(xmlData);
			result.put("errorCode", 0);
		} catch (Exception e) {
			result.put("errorCode", -1);
			e.printStackTrace();
		}
		return result;
	}

	private XMLData getXmlData(BillnoRuleForm billnoruleform) {
		XMLData xmlData = new XMLData();
		xmlData.put("billnorule_id", billnoruleform.getBillnorule_id());
		xmlData.put("billnorule_code", billnoruleform.getBillnorule_code());
		xmlData.put("billnorule_name", billnoruleform.getBillnorule_name());
		xmlData.put("sys_id", billnoruleform.getSys_id());
		xmlData.put("lineIdsToBeDeleted",
				billnoruleform.getLineIdsToBeDeleted());
		xmlData.put("billnorulelines", billnoruleform.getBillnorulelines());
		return xmlData;
	}

	/**
	 * 要素编号、要素名称的初始化
	 * 
	 * @return Map
	 */
	@RequestMapping(value = "/getElement.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getEleCodesAndEleNames() {
		Map<String, Object> result = new HashMap<String, Object>();
		// this.setUserInfo();
		try {
			List list = getEleSet();
			result.put("data", list);
			result.put("errorCode", 0);
		} catch (Exception e) {
			result.put("errorCode", -1);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据树叉节点，筛选table内容(列表和编辑框公共)
	 * 
	 */
	@RequestMapping(value = "/findSysRulesByBillNoRuleId.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findSysRulesByBillNoRuleId(
			HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		String billnoruleId = request.getParameter("billnoRuleId");
		List data = new ArrayList();
		try {
			data = iSysBillNoRule
					.findSysBillNoRuleLinesByBillnoruleId(billnoruleId);
			result.put("data", data);
			result.put("errorCode", 0);
		} catch (Exception e) {
			result.put("errorCode", -1);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除单号规则数据
	 * 
	 */
	@RequestMapping(value = "/deleteSysBillNoRuleByBillNoRuleId.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> deleteSysBillNoRuleByBillNoRuleId(
			HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		String billnoruleId = request.getParameter("billnorule_Id");
		try {
			iSysBillNoRule.deleteSysBillNoRuleByBillNoRuleId(billnoruleId);
			result.put("errorCode", 0);
		} catch (Exception e) {
			result.put("errorCode", -1);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据要素编码在相应基础数据表中找到最大级次
	 * 
	 * @param eleCode
	 * @return
	 */
	@RequestMapping(value = "/getMaxLevelNumByEleCode.do", method = RequestMethod.GET)
	@ResponseBody
	public Integer getMaxLevelNumByEleCode(
			@RequestParam("eleCode") String eleCode) {
		XMLData sysEle = iDictionary.getElementSetByCode(eleCode);
		Integer maxLevelNum = null;
		if (sysEle != null) {// 如果要素不为空
			String eleSource = (String) sysEle.get("ele_source");
			try {
				if (null != sysBillTypeService)
					maxLevelNum = sysBillTypeService
							.getMaxLevelNumByEleSource(eleSource);
				if (maxLevelNum == null) {
					return 0;
				} else {
					return maxLevelNum;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	/**
	 * getEleSet方法
	 * 
	 * @return
	 */
	public List getEleSet() {
		// this.setUserInfo();
		return iDictionary.getElementSet("and enabled=1");
	}

	/* *//**
	 * 得到所有要素编码
	 * 
	 * @return
	 */
	/*
	 * public Vector getEleCodes() { List eleList = getEleSet(); Vector v = new
	 * Vector(eleList.size()); // 循环获得所有的要素编码 for (int i = 0; i <
	 * eleList.size(); i++) { v.add(((Map) eleList.get(i)).get("ele_code")); }
	 * return v; }
	 *//**
	 * 得到所有要素名称
	 * 
	 * @return
	 */
	/*
	 * public String[] getEleNames() { List eleList = getEleSet(); String[] ens
	 * = new String[eleList.size()]; // 循环获得所有要素的名称 for (int i = 0; i <
	 * eleList.size(); i++) { ens[i] = (String) ((Map)
	 * eleList.get(i)).get("ele_name"); } return ens; }
	 */

	/**
	 * 取得一个UUID的方法
	 * 
	 * @return 一个UUID
	 */
	public static String generate() {
		UUID uuid = UUID.randomUUID();
		String id = "{" + uuid.toString() + "}";
		return id.toUpperCase();
	}

	/**
	 * 新树的构建
	 * 
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value = "/treeInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> newTreeInfo(HttpServletRequest request)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List dataBillNoInfo = new ArrayList();
			List dataSysAppInfo = new ArrayList();
			XMLData xmlData = new XMLData();
			xmlData.put("id", 0);
			xmlData.put("cid", 0);
			xmlData.put("sys_id", null);
			xmlData.put("name", "单号规则");
			dataBillNoInfo = iSysBillNoRule.findTreeBillNoInfo();
			dataSysAppInfo = iSysBillNoRule.findTreeSysAppInfo();
			dataBillNoInfo.add(xmlData);
			dataBillNoInfo.addAll(dataSysAppInfo);
			result.put("rows", dataBillNoInfo);
			result.put("errorCode", 0);
		} catch (Exception e) {
			result.put("errorCode", -1);
			e.printStackTrace();
		}
		return result;
	}

}
