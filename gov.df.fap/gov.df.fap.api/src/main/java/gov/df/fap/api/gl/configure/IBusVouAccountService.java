package gov.df.fap.api.gl.configure;

import java.util.List;

public interface IBusVouAccountService {

	public List getAllCoa();
	public String getBookSet();
	public boolean updateAccount(String account);
	public boolean addAccount(String account);
	public void deleteAccount(String account);

}
