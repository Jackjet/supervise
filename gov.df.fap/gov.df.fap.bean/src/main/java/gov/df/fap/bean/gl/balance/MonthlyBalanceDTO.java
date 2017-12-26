package gov.df.fap.bean.gl.balance;


/**
 * 月结情况的bean,对应数据库表gl_monthly_balance
 * @author 张轲
 *
 */
public class MonthlyBalanceDTO {
	private String set_year;
	private String set_month;
	private String closing_time;
	
	public MonthlyBalanceDTO(){
		
	}
	
	public MonthlyBalanceDTO(String set_year,String set_month){
		this.set_year=set_year;
		this.set_month=set_month;
	}
	public String getSet_year() {
		return set_year;
	}
	public void setSet_year(String set_year) {
		this.set_year = set_year;
	}
	public String getSet_month() {
		return set_month;
	}
	public void setSet_month(String set_month) {
		this.set_month = set_month;
	}
	public String getClosing_time() {
		return closing_time;
	}
	public void setClosing_time(String closing_time) {
		this.closing_time = closing_time;
	}
}
