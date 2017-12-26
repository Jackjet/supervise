package gov.df.fap.api.gl.configure;

import gov.df.fap.bean.gl.configure.BusVouAcctmdl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IBusVouAcctmdlService {

  public List getAccount();

  public List getVouType();
  
  public List getCoaElements(String accountId);

  public List getBusVouAcctmdl(long vou_type_id);

  public void saveAcctmdl(String busvoutype, String busvouacctmdl);

  public void delAcctmdlByAcctmdlId(String acctmdl);

  public List getBusVouAcctmdlByAccId(String account_id);

  Map<String, Object> getEleValue(HttpServletRequest request,
		HttpServletResponse response) throws Exception;

Map<String, Object> getElements(HttpServletRequest request,
		HttpServletResponse response) throws Exception;

Map<String, Object> saveAcctmdlRuleId(HttpServletRequest request,
		HttpServletResponse response);

	}
