package gov.df.fap.controller.datatransfer;

import gov.df.fap.api.fasptransfer.ITransferSysEle;
import gov.mof.fasp2.ca.user.dto.UserDTO;
import gov.mof.fasp2.sec.syncuserinfo.UserInfo;
import gov.mof.fasp2.sec.syncuserinfo.manager.IUserSyncManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longtu.framework.cache.exceptions.NoPrivilegeException;
import com.longtu.framework.exception.AppException;
import com.longtu.framework.util.ServiceFactory;

/**
 * 数据迁移
 * 
 * @author hult
 * 
 */
@Controller
@RequestMapping(value = "/df/datatransfer")
public class SysDataTransferController {

	 @Autowired
	ITransferSysEle transSys;
	/**
	 * 迁移用户
	 * 
	 */
	@RequestMapping(value = "/sys/transfer.do")
	@ResponseBody
	public Map<String, Object> transferData(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		List data = new ArrayList();
		try {
			String tokenid = request.getParameter("tokenid");
			String rg_code = request.getParameter("rg_code");
			String year = request.getParameter("set_year");
			String ele_code  = request.getParameter("ele_code");
			String pt_code = request.getParameter("pt_code");
			String old_code = request.getParameter("old_code");
			System.out.println(ele_code);
			System.out.println(old_code);
			if("USER".equals(ele_code))
			{
				transSys.transferUser(rg_code, year);
			}else if  ("BANK".equals(ele_code))
			{
				transSys.transferBank(rg_code, year);
			}
			else if  ("DEPARTMENT".equals(ele_code))
			{
				transSys.transferDepartment(rg_code, year);
			}
			else if  ("BAP".equals(ele_code)||"BAC".equals(ele_code))
			{
				transSys.transferAccount(ele_code, pt_code,old_code,rg_code, year);
			}
			else if  ("ROLE".equals(ele_code))
			{
				UserInfo uinfo = null;
			      try {
			    	  IUserSyncManager  iUserSyncManager = (IUserSyncManager) ServiceFactory.getBean("fasp.ca.UserSyncManager");
			        uinfo = iUserSyncManager.getUser(tokenid);
			        UserDTO userdto = uinfo.getUser();
					transSys.transferRole(userdto.getAdmdiv(),rg_code, year);
			      } catch (Exception e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			         
			      }
			      
			}else if("AGENCY".equals(ele_code))
			{
				transSys.transferAgency(rg_code, year);
			}else if("CCID".equals(ele_code))
			{
				transSys.transferGlCcid("gl_ccids_transfer");
			}
			else{
				transSys.transferDatazSet(ele_code, pt_code,old_code,rg_code, year);
			}
			
			
			

 			result.put("data", data);
			result.put("errorCode", 0);
		} catch (Exception e) {
			result.put("errorCode", -1);
			result.put("data", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
  
 
		@RequestMapping(value = "/getRgcode.do")
		@ResponseBody
	  public Map<String, Object> getRgcode(HttpServletRequest request) {
			String tokenid = request.getParameter("tokenid");
			 Map<String, Object> map = new HashMap<String, Object>();
			UserInfo uinfo = null;
			 IUserSyncManager  iUserSyncManager = (IUserSyncManager) ServiceFactory.getBean("fasp.ca.UserSyncManager");
		        try {
					uinfo = iUserSyncManager.getUser(tokenid);
					map.put("rgcode",uinfo.getProperty("PROVINCE"));
					map.put("setyear",uinfo.getProperty("YEAR"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					map.put("rgcode", "用户已经失效");
					map.put("setyear", e.getMessage());
				}
	   
		 
		return map;
	  }
		@RequestMapping(value = "/getTransferEle.do")
		@ResponseBody
	  public List getTransferEle(HttpServletRequest request) {
			String rg_code = request.getParameter("rg_code");
			String year = request.getParameter("set_year");
	    List<Map> list = transSys.getTransferEle(rg_code,year);
	    return list;
	  }
		@RequestMapping(value = "/getTransferRight.do")
		@ResponseBody
	  public List getTransferRight(HttpServletRequest request) {
			String rg_code = request.getParameter("rg_code");
			String year = request.getParameter("set_year");
	    List<Map> list = transSys.getTransferRight(rg_code,year);
	    return list;
	  }
		@RequestMapping(value = "/getTransferBo.do")
		@ResponseBody
	  public List getTransferBo(HttpServletRequest request) {
			String rg_code = request.getParameter("rg_code");
			String year = request.getParameter("set_year");
	    List<Map> list = transSys.getTransferBo(rg_code,year);
	    return list;
	  }
		/**
		 * 迁移历史数据
		 * 
		 * 
		 * @return
		 */
		@RequestMapping(value = "/sys/transferdata.do")
		@ResponseBody
		public Map<String, Object> transferHisData(HttpServletRequest request) { 
			Map<String, Object> result = new HashMap<String, Object>();
			List data = new ArrayList();
			try {
				String tokenid = request.getParameter("tokenid");
				String rg_code = request.getParameter("rg_code");
				String year = request.getParameter("set_year");
				String newtable  = request.getParameter("newtable");
				System.out.println(newtable);
				String oldtable  = request.getParameter("oldtable");
				System.out.println(oldtable);
				transSys.transferBo(newtable, oldtable, rg_code, year);
				

	 			result.put("data", data);
				result.put("errorCode", 0);
			} catch (Exception e) {
				result.put("errorCode", -1);
				result.put("data", e.getMessage());
				e.printStackTrace();
			}
			return result;
		}
		/**
		 * 迁移权限数据
		 * @param request
		 * @return
		 */
		@RequestMapping(value = "/sys/transferright.do")
		@ResponseBody
		public Map<String, Object> transferRight(HttpServletRequest request) {
			Map<String, Object> result = new HashMap<String, Object>();
			List data = new ArrayList();
			try {
				String tokenid = request.getParameter("tokenid");
				String rg_code = request.getParameter("rg_code");
				String year = request.getParameter("set_year");
				String rgiht_table  = request.getParameter("right_table");
				String right_sql = request.getParameter("right_sql");
				String is_same = request.getParameter("is_same");
				System.out.println(rgiht_table);
				System.out.println(right_sql);
                if(is_same.equals("0") || is_same.equals("1"))
                {
			       transSys.transferRight(rgiht_table,right_sql,is_same,rg_code, year);
                }else if (is_same.equals("2") ||is_same.equals("3")  ){
                	List<String> list = new ArrayList<String>();
                	list.add("ele_code");
                	list.add("pri_ele_code");
                	list.add("sec_ele_code");
                	list.add("sec_ele_code");
                	list.add("field_code");
                	 
                	transSys.transferEleCode(rgiht_table,right_sql,is_same,list,rg_code, year);
                }else if (is_same.equals("4")){
                	transSys.transferBo(rgiht_table, right_sql, rg_code, year);
                }
				 
				

	 			result.put("data", data);
				result.put("errorCode", 0);
			} catch (Exception e) {
				result.put("errorCode", -1);
				result.put("data", e.getMessage());
				e.printStackTrace();
			}
			return result;
		}


}
