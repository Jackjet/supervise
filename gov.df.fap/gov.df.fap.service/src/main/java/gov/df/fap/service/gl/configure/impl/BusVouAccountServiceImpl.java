package gov.df.fap.service.gl.configure.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import gov.df.fap.api.gl.coa.ibs.ICoa;
import gov.df.fap.api.gl.configure.AccountService;
import gov.df.fap.api.gl.configure.IBusVouAccountService;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.gl.core.CommonUtil;

@Service
public class BusVouAccountServiceImpl implements IBusVouAccountService {

	@Autowired
	@Qualifier("generalDAO")
	private GeneralDAO generalDao = null;	

	@Autowired
	private ICoa coa = null;
	
	@Autowired
	@Qualifier("accountServiceImpl")
	private AccountService accountService = null;
	
	@Override
	public String getBookSet() {
		String stId = "";
		String set_year = CommonUtil.getSetYear();
		String rg_code = CommonUtil.getRgCode();
		String sql = "select chr_id from ele_book_set where set_year = ? and rg_code= ? and chr_code = '000000'";
		List list = generalDao.findBySql(sql, new Object[]{ set_year,rg_code});
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (stId == null) {
				stId = "";
			} else {
				stId = (String) map.get("chr_id");
			}
		}
		return stId;
	}
	
	@Override
	public List getAllCoa() {
		List list = new ArrayList();
		list = (List) coa.getCoalist(StringUtils.EMPTY).get("row");
		return list;
	}
	@Override
	public boolean updateAccount(String account){
		JSONObject jsonObject = JSONObject.fromObject(account);
		BusVouAccount busVouAccount = new BusVouAccount();
		if(jsonObject.get("subject_kind")!=null&&!jsonObject.get("subject_kind").equals("null")){
			busVouAccount.setSubjectKind(Integer.valueOf((String)jsonObject.get("subject_kind")));
		}
		
		if(jsonObject.get("coa")!=null&&!jsonObject.get("coa").equals("null")){
			busVouAccount.setCoaId(Integer.valueOf((String)jsonObject.get("coa")));
		}
		
		if(jsonObject.get("balancePeriodType")!=null&&!jsonObject.get("balancePeriodType").equals("null")){
			busVouAccount.setBalancePeriodType(Integer.valueOf((String)jsonObject.get("balancePeriodType")));
		}
		
		if(jsonObject.get("table")!=null&&!jsonObject.get("table").equals("null")){
			busVouAccount.setTableName((String)jsonObject.get("table"));
		}
		if(jsonObject.get("detail_table")!=null&&!jsonObject.get("detail_table").equals("null")){
			busVouAccount.setMonthDetailTableName((String)jsonObject.get("detail_table"));
		}
		busVouAccount.setAccountName((String)jsonObject.get("accountName"));
		busVouAccount.setAccountCode((String)jsonObject.get("accountCode"));
		busVouAccount.setStId((String)jsonObject.get("book_set"));
		busVouAccount.setBalanceSide(Integer.valueOf((String)jsonObject.get("isDebit")));
		busVouAccount.setEnabled(Integer.valueOf((String)jsonObject.get("enable")));
		busVouAccount.setSubjectType(Integer.valueOf((String)jsonObject.get("subject_type")));
		busVouAccount.setAccountId((String)jsonObject.get("accountId"));
		accountService.updateBusVouAccount(busVouAccount, busVouAccount);
		return true;
	}
	@Override
	public boolean addAccount(String account){
		JSONObject jsonObject = JSONObject.fromObject(account);
		
		BusVouAccount tempAccount = accountService.loadBusVouAccountByCode((String)jsonObject.get("accountCode"));
		if(tempAccount!=null){
			return false;
		}
		BusVouAccount busVouAccount = new BusVouAccount();
		if(jsonObject.get("subject_kind")!=null&&!jsonObject.get("subject_kind").equals("null")){
			busVouAccount.setSubjectKind(Integer.valueOf((String)jsonObject.get("subject_kind")));
		}
		
		if(jsonObject.get("coa")!=null&&!jsonObject.get("coa").equals("null")){
			busVouAccount.setCoaId(Integer.valueOf((String)jsonObject.get("coa")));
		}
		
		if(jsonObject.get("balancePeriodType")!=null&&!jsonObject.get("balancePeriodType").equals("null")){
			busVouAccount.setBalancePeriodType(Integer.valueOf((String)jsonObject.get("balancePeriodType")));
		}
		
		if(jsonObject.get("table")!=null&&!jsonObject.get("table").equals("null")){
			busVouAccount.setTableName((String)jsonObject.get("table"));
		}
		if(jsonObject.get("detail_table")!=null&&!jsonObject.get("detail_table").equals("null")){
			busVouAccount.setMonthDetailTableName((String)jsonObject.get("detail_table"));
		}
		busVouAccount.setAccountName((String)jsonObject.get("accountName"));
		busVouAccount.setAccountCode((String)jsonObject.get("accountCode"));
		busVouAccount.setStId((String)jsonObject.get("book_set"));
		busVouAccount.setBalanceSide(Integer.valueOf((String)jsonObject.get("isDebit")));
		busVouAccount.setEnabled(Integer.valueOf((String)jsonObject.get("enable")));
		busVouAccount.setSubjectType(Integer.valueOf((String)jsonObject.get("subject_type")));
		busVouAccount.setRgCode(CommonUtil.getRgCode());
		accountService.saveBusVouAccount(busVouAccount);
		return true;
	}
	@Override
	public void deleteAccount(String account){
		JSONObject jsonObject = JSONObject.fromObject(account);
		BusVouAccount busVouAccount = new BusVouAccount();
		busVouAccount.setAccountId((String)jsonObject.get("accountId"));
		busVouAccount.setAccountName((String)jsonObject.get("accountName"));
		busVouAccount.setAccountCode((String)jsonObject.get("accountCode"));
		busVouAccount.setStId((String)jsonObject.get("book_set"));
		busVouAccount.setBalanceSide(Integer.valueOf(jsonObject.get("isDebit").toString()));
		busVouAccount.setEnabled(Integer.valueOf(jsonObject.get("enable").toString()));
		busVouAccount.setSubjectKind(Integer.valueOf(jsonObject.get("subject_kind").toString()));
		busVouAccount.setSubjectType(Integer.valueOf(jsonObject.get("subject_type").toString()));
		busVouAccount.setCoaId(Integer.valueOf(jsonObject.get("coa").toString()));
		busVouAccount.setBalancePeriodType(Integer.valueOf(jsonObject.get("balancePeriodType").toString()));
		busVouAccount.setTableName((String)jsonObject.get("table"));
		busVouAccount.setMonthDetailTableName((String)jsonObject.get("detail_table"));
		busVouAccount.setRgCode(CommonUtil.getRgCode());
		accountService.deleteBusVouAccount(busVouAccount);
	}
	
}
