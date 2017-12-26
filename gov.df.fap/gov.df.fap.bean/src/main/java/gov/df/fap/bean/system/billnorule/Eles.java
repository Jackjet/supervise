package gov.df.fap.bean.system.billnorule;


/**
 *  sys_billnorule、sys_billnoruleline、sys_billnoruleele
 * 新增保存功能使用
 * @author songlr3
 *
 */
public class Eles {
	
	/**
	 * 要素值串
	 */
	private String ele_value;

	/**
	 * 当前自增值
	 */
	private String max_no;
	
	private String ele_code;
	
	private String level_num;
	
	private String table;
	
	private String billnoruleline_id;
	
	private String set_year;
	
	private String rg_code;
	
	public String getEle_code() {
		return ele_code;
	}
	public void setEle_code(String ele_code) {
		this.ele_code = ele_code;
	}
	public String getLevel_num() {
		return level_num;
	}
	public void setLevel_num(String level_num) {
		this.level_num = level_num;
	}
	public String getEle_value() {
		return ele_value;
	}
	public void setEle_value(String ele_value) {
		this.ele_value = ele_value;
	}
	public String getMax_no() {
		return max_no;
	}
	public void setMax_no(String max_no) {
		this.max_no = max_no;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getBillnoruleline_id() {
		return billnoruleline_id;
	}
	public void setBillnoruleline_id(String billnoruleline_id) {
		this.billnoruleline_id = billnoruleline_id;
	}
	public String getSet_year() {
		return set_year;
	}
	public void setSet_year(String set_year) {
		this.set_year = set_year;
	}
	public String getRg_code() {
		return rg_code;
	}
	public void setRg_code(String rg_code) {
		this.rg_code = rg_code;
	}
	
}
