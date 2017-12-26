package gov.df.fap.service.gl.configure.impl;

import gov.df.fap.api.gl.configure.AccountService;
import gov.df.fap.api.gl.configure.IBusVouAcctmdlService;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.configure.BusVouType;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.exceptions.gl.ExistOnWayDataOfBusVouException;
import gov.df.fap.service.util.exceptions.gl.GlException;
import gov.df.fap.service.util.gl.configure.IBusVouTypeService;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.exception.IllegalEleLevelOfDownStreamCoaException;
import gov.df.fap.util.exception.LackEleOfDownStreamCoaException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BusVouAcctmdlServiceImpl implements IBusVouAcctmdlService {


  @Autowired
  @Qualifier("busVouTypeServiceImpl")
  private IBusVouTypeService busVouTypeService = null;

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO generalDao = null;

  @Autowired
  @Qualifier("busVouTypeDao")
  private BusVouTypeDao busVouTypeDao = null;

  @Autowired
  @Qualifier("accountServiceImpl")
  private AccountService accountService = null;
  
  @Override
  public List getAccount() {

	String setYear = CommonUtil.getSetYear();
	String rgCode = CommonUtil.getRgCode();  
    List list = generalDao
      .findBeanBySql(
        "select account_id accountId, set_year setYear, rg_code rgCode, account_code accountCode, account_name accountName, balance_side balanceSide, balance_period_type balancePeriodType, coa_id coaId, table_name tableName, monthdetail_table_name monthDetailTableName, subject_kind subjectKind, subject_type subjectType, enabled, "
          + "disp_code dispCode, level_num levelNum, is_leaf isLeaf, create_date createDate, create_user createUser, is_deleted isDeleted, last_ver lastVer, latest_op_user latestOpUser, chr_code1 chrCode1, chr_code2 chrCode2, chr_code3 chrCode3, chr_code4 chrCode4, chr_code5 chrCode5, chr_id1 chrId1, chr_id2 chrId2,"
          + " chr_id3 chrId3, chr_id4 chrId4, chr_id5 chrId5, parent_id parentId, st_id stId, hint_code from vw_gl_account where set_year = ? and rg_code = ? order by account_code",
        new Object[] { setYear, rgCode }, BusVouAccount.class);
    Iterator it = list.iterator();
    while (it.hasNext()) {
      BusVouAccount busVouAccount = (BusVouAccount) it.next();
      busVouAccount.setCodename(busVouAccount.getAccountCode() + " " + busVouAccount.getAccountName());
      busVouAccount.setChr_code(busVouAccount.getAccountCode());
      busVouAccount.setChr_id(busVouAccount.getAccountId());
      busVouAccount.setChr_name(busVouAccount.getAccountName());
      busVouAccount.setParent_id(busVouAccount.getParentId());
    }
    return list;
  }

  @Override
  public List getVouType() {
    List list = busVouTypeService.allBusVouTypeSimple();
    Iterator it = list.iterator();
    while (it.hasNext()) {
      BusVouType bvt = (BusVouType) it.next();
      bvt.setVou_type_name(bvt.getVou_type_code() + " " + bvt.getVou_type_name());
    }
    return list;
  }

  @Override
  public List getBusVouAcctmdl(long vou_type_id) {
	String setYear = CommonUtil.getSetYear();
	String rgCode = CommonUtil.getRgCode();
    List list = busVouTypeService.loadVouAcctdml(vou_type_id);
    Iterator it = list.iterator();
    while (it.hasNext()) {
      BusVouAcctmdl busVouAcctdml = (BusVouAcctmdl) it.next();
      List acc_list = generalDao
        .findBeanBySql(
          "select account_id accountId, set_year setYear, rg_code rgCode, account_code accountCode, account_name accountName, balance_side balanceSide, balance_period_type balancePeriodType, coa_id coaId, table_name tableName, monthdetail_table_name monthDetailTableName, subject_kind subjectKind, subject_type subjectType, enabled, st_id stId  from vw_gl_account where set_year=? and rg_code=? and account_id=?",
          new Object[] { setYear, rgCode, busVouAcctdml.getAccount_id() }, BusVouAccount.class);
      Iterator acc_it = acc_list.iterator();
      if(acc_it.hasNext()){
    	  while (acc_it.hasNext()) {
  	        BusVouAccount busVouAccount = (BusVouAccount) acc_it.next();
  	        String code = busVouAccount.getAccountCode();
  	        busVouAcctdml.setAccount_name(busVouAccount.getAccountId() + "@" + busVouAccount.getAccountName() + "@"
  	          + busVouAccount.getAccountCode());
  	      }
      }else{
    	  	busVouAcctdml.setBusVouAccount(new BusVouAccount());
    	  	busVouAcctdml.setAccount_name("" + "@" + "" + "@"
      	          + "");
      }
    }
    return list;
  }
  
  @Override
  public List getBusVouAcctmdlByAccId(String account_id) {
	  String setYear = CommonUtil.getSetYear();
	  String rgCode = CommonUtil.getRgCode();
	  List list = new ArrayList();
	   List<BusVouAcctmdl> acctmdl_list = generalDao
        .findBeanBySql(
          "select t.acctmdl_id,t.set_year,t.rg_code,t.vou_type_id,t.account_id from gl_busvou_acctmdl t where t.set_year=? and t.rg_code=? and account_id=?",
          new Object[] { setYear, rgCode, account_id }, BusVouAcctmdl.class);
      Iterator acc_it = acctmdl_list.iterator();
      while (acc_it.hasNext()) {
        BusVouAcctmdl busVouAcctmdl = (BusVouAcctmdl) acc_it.next();
        List voutypelist = generalDao.findBeanBySql("select * from gl_busvou_type where set_year=? and rg_code = ? and vou_type_id = ?",
        		new Object[]{setYear,rgCode,busVouAcctmdl.getVou_type_id()}, BusVouType.class);
        Iterator voutypeit = voutypelist.iterator();
        while(voutypeit.hasNext()){
        	BusVouType busVouType = (BusVouType) voutypeit.next();
        	busVouAcctmdl.setBvType(busVouType);
        }
        List vouaccountlist = generalDao.findBeanBySql("select * from vw_gl_account where set_year=? and rg_code =? and account_id = ?",new Object[]{setYear,rgCode,busVouAcctmdl.getAccount_id()}, BusVouAccount.class);
        Iterator vouaccountit = vouaccountlist.iterator();
        while(vouaccountit.hasNext()){
        	BusVouAccount busVouAccount = (BusVouAccount) vouaccountit.next();
        	busVouAcctmdl.setBusVouAccount(busVouAccount);
        }
        list.add(busVouAcctmdl);
      }
      return list;
    
  }

  @Override
  public void delAcctmdlByAcctmdlId(String acctmdl) {

    String[] acc = acctmdl.split("#");
    for (int i = 0; i < acc.length; i++) {
      String[] voutype = acc[i].split(" ");
      BusVouType busVouType = busVouTypeService.loadBusVouType(Integer.parseInt(voutype[1]));
      if (busVouType != null) {
        if (busVouTypeDao.existOnWayData(busVouType.getVou_type_id())) {
          try {
            throw new ExistOnWayDataOfBusVouException("记账模板编码为" + busVouType.getVou_type_code() + "存在在途数据,不允许删除");
          } catch (ExistOnWayDataOfBusVouException e) {
            e.printStackTrace();
          }
        }
        busVouTypeDao.deleteAcctmdlByAcctmdlId(Integer.parseInt(voutype[0]));
        List list = busVouTypeService.loadVouAcctdml(busVouType.getVou_type_id());
        if (list.isEmpty()) {
          busVouTypeDao.deleteBusVouType(busVouType);
        }

      } else {
        if (busVouType == null || busVouType.getVou_type_id() == 0)
          throw new GlException("参数中缺少id");
      }
    }

  }

  @Override
  public List getCoaElements(String accountId){
	  List list = new ArrayList();
	  String sql = "select *  from sys_element t  where t.ele_code in (select upper(ele_code) from gl_coa_detail gd where gd.coa_id = (select coa_id from gl_sum_type t where t.sum_type_id = '" + accountId + "'))";
	  list = generalDao.findBySql(sql);
	  return list;
  }
  
  @Override
  public void saveAcctmdl(String busvoutype, String busvouacctmdl) {

    String[] voutype = busvoutype.split("#");
    String[] acctmdl = busvouacctmdl.split("#");
    BusVouType busVouType = busVouTypeService.loadBusVouTypeByCode(voutype[0]);
    if (busVouType != null) {
    	maintainAcctmdl(acctmdl, voutype, busVouType);
    } else {
      busVouType = new BusVouType();
      busVouType.setVou_type_code(voutype[0]);
      busVouType.setVou_type_name(voutype[1]);
      try {
        busVouTypeService.saveSetBusVouType(busVouType);
        maintainAcctmdl(acctmdl, voutype, busVouType);
      } catch (IllegalEleLevelOfDownStreamCoaException e) {
        e.printStackTrace();
      } catch (LackEleOfDownStreamCoaException e) {
        e.printStackTrace();
      }
    }
  }
  
  @Override
  public Map<String, Object> getEleValue(HttpServletRequest request, HttpServletResponse response)
		    throws Exception {
	    Map<String, Object> map = new HashMap<String, Object>();
	    String sql = "select * from sys_element where rg_code =? and set_year=? and ele_code='" + request.getParameter("ele_code") + "'";
	    List rs = null;
	    List result = new ArrayList();
	    rs = generalDao.findBySql(sql, new Object[] { (String) SessionUtil.getRgCode(), (String) SessionUtil.getLoginYear() });
	    for (int i = 0; i < rs.size(); i++) {
	      Map tempmap = (Map) rs.get(i);
	      String ele_source = (String) tempmap.get("ele_source");
	      String selectSql = null;
	      if(TypeOfDB.isOracle()) {
	    	  selectSql = "select chr_code|| ' ' ||chr_name as chr_name,t.chr_id as chr_id,t.parent_id as parent_id from "
	    			  + ele_source + " t where rg_code =? and set_year=?";
	      } else if(TypeOfDB.isMySQL()) {
	    	  selectSql = "select concat(chr_code, ' ',chr_name) as chr_name,t.chr_id as chr_id,t.parent_id as parent_id from "
	    			  + ele_source + " t where rg_code =? and set_year=?";
	      }
	      List tmprs = null;
	      tmprs = generalDao.findBySql(selectSql,
	        new Object[] { (String) SessionUtil.getRgCode(), (String) SessionUtil.getLoginYear() });
	      tempmap.put("element_list", tmprs);
	      result.add(tempmap);
	    }
	    map.put("enable_elements", result);
	    return map;
	  }
  @Override
  public Map<String, Object> getElements(HttpServletRequest request, HttpServletResponse response)
		    throws Exception {
	    Map<String, Object> map = new HashMap<String, Object>();
	    String str = request.getParameter("rule_id");
	    String sql = "select * from sys_right_group where (rg_code =? and set_year=? and rule_id=" + request.getParameter("rule_id") + ")";
	    List rs = null;
	    List result = new ArrayList();
	    rs = generalDao.findBySql(sql, new Object[] { (String) SessionUtil.getRgCode(), (String) SessionUtil.getLoginYear() });
	    for (int i = 0; i < rs.size(); i++) {
	      Map tempmap = (Map) rs.get(i);
	      String right_group_id = (String) tempmap.get("right_group_id");
	      String selectSql = "select distinct(ele_code) from sys_right_group_detail t where rg_code =? and set_year=? and right_group_id='" + right_group_id + "'";
	      List tmprs = generalDao.findBySql(selectSql,
	        new Object[] { (String) SessionUtil.getRgCode(), (String) SessionUtil.getLoginYear() });
	      for(int j=0;j<tmprs.size();j++){
	    	  Map detailMap = (Map) tmprs.get(j);
	    	  String ele_source = "select * from sys_element t where rg_code =? and set_year=? and ele_code='" + detailMap.get("ele_code") + "'";
	    	  List sourceList = generalDao.findBySql(ele_source,new Object[] { (String) SessionUtil.getRgCode(), (String) SessionUtil.getLoginYear() });
	    	  for(int k=0;k<sourceList.size();k++){
	    		  Map eleMap = (Map) sourceList.get(k);
	    		  String eleSql = null;
	    		  if(TypeOfDB.isOracle()) {
	    			  eleSql = "select chr_code|| ' ' ||chr_name as chr_name,t.chr_id as chr_id,t.parent_id as parent_id from "
	    					  + eleMap.get("ele_source") + " t where rg_code =? and set_year=?";;
	    		  } else if(TypeOfDB.isMySQL()) {
	    			  eleSql = "select concat(chr_code, ' ' ,chr_name) as chr_name,t.chr_id as chr_id,t.parent_id as parent_id from "
	    					  + eleMap.get("ele_source") + " t where rg_code =? and set_year=?";;
	    		  }
	    		  List eleRs = generalDao.findBySql(eleSql,new Object[] { (String) SessionUtil.getRgCode(), (String) SessionUtil.getLoginYear() });
	    		  eleMap.put("element_list", eleRs);
	    		  result.add(eleMap);
	    	  }
	      }
	    }
	    map.put("enable_elements", result);
	    return map;
	  }

  @Override
  public Map<String, Object> saveAcctmdlRuleId(HttpServletRequest request, HttpServletResponse response){
	  Map<String, Object> map = new HashMap<String, Object>();
	  
	  return map;
	  
  }
  public int transStrtoInt(String str) {
    int a = 0;
    if (str.equals("0")) {
      a = 0;
    }
    if (str.equals("1")) {
      a = 1;
    }
    if (str.equals("2")) {
      a = 2;
    }
    else{
    	a=Integer.valueOf(str);
    }
    return a;
  }
  
  public void maintainAcctmdl(String[] acctmdl, String[] voutype, BusVouType busVouType){
	  List<BusVouAcctmdl> list = new LinkedList<BusVouAcctmdl>();
      for (int i = 0; i < acctmdl.length; i += 7) {
        BusVouAcctmdl busVouAcctmdl = new BusVouAcctmdl();
        if (acctmdl[i] == null||acctmdl[i].equals("null")) {
          busVouAcctmdl.setAcctmdl_id(0);
        } else {
          busVouAcctmdl.setAcctmdl_id(transStrtoInt(acctmdl[i]));
        }
        busVouAcctmdl.setAccount_id(acctmdl[i + 1].substring(1));
        busVouAcctmdl.setEntry_side(transStrtoInt(acctmdl[2]));
        busVouAcctmdl.setIs_primary_source(transStrtoInt(acctmdl[i + 3]));
        busVouAcctmdl.setIs_primary_target(transStrtoInt(acctmdl[i + 4]));
        busVouAcctmdl.setCtrl_level(transStrtoInt(acctmdl[i + 5]));
        String[] temp = acctmdl[i + 6].split("\"");
        String rule_id = null;
        for(int j=0;j<temp.length;j++){
        	if(temp[j]==""){
        		
        	}else{
        		rule_id = temp[j];
        	}
        }
        busVouAcctmdl.setRule_id(rule_id);
        busVouAcctmdl.setVou_type_id(busVouType.getVou_type_id());
        list.add(busVouAcctmdl);
        busVouType.addAcctmdl(busVouAcctmdl);
      }
      if (!list.isEmpty()) {
        busVouType.setVou_type_code(voutype[0]);
        busVouType.setVou_type_name(voutype[1]);
        try {
          busVouTypeService.updateSetBusVouType(busVouType);
        } catch (IllegalEleLevelOfDownStreamCoaException e) {
          e.printStackTrace();
        } catch (LackEleOfDownStreamCoaException e) {
          e.printStackTrace();
        } catch (ExistOnWayDataOfBusVouException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
  }
}
