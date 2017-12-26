package gov.df.fap.bean.form;


public class SysEleRuleForm {

	/**
	 * 要素定值规则id
	 */
	private String ele_rule_id;
	/**
	 * 要素定值规则编码
	 */
	private String ele_rule_code;
	/**
	 * 要素定值规则名称
	 */
	private String ele_rule_name;
	/**
	 * 要素简称
	 */
	private String ele_code;
	/**
	 * 创建时间： YYYY-MM-DD HH:mm:SS
	 */
	private String create_date;
	/**
	 * 创建用户
	 */
	private String create_user;
	/**
	 * 最后修改时间： YYYY-MM-DD HH:mm:SS
	 */
	private String last_op_date;
	/**
	 * 最后修改用户
	 */
	private String last_op_user;
	/**
	 * 最后版本
	 */
	private String last_ver;
	/**
	 * 业务年度
	 */
	private int set_year;
	/**
	 * 区划
	 */
	private String rg_code;
	
	public String getEle_rule_id() {
		return ele_rule_id;
	}
	public void setEle_rule_id(String ele_rule_id) {
		this.ele_rule_id = ele_rule_id;
	}
	public String getEle_rule_code() {
		return ele_rule_code;
	}
	public void setEle_rule_code(String ele_rule_code) {
		this.ele_rule_code = ele_rule_code;
	}
	public String getEle_rule_name() {
		return ele_rule_name;
	}
	public void setEle_rule_name(String ele_rule_name) {
		this.ele_rule_name = ele_rule_name;
	}
	public String getEle_code() {
		return ele_code;
	}
	public void setEle_code(String ele_code) {
		this.ele_code = ele_code;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getLast_op_date() {
		return last_op_date;
	}
	public void setLast_op_date(String last_op_date) {
		this.last_op_date = last_op_date;
	}
	public String getLast_op_user() {
		return last_op_user;
	}
	public void setLast_op_user(String last_op_user) {
		this.last_op_user = last_op_user;
	}
	public String getLast_ver() {
		return last_ver;
	}
	public void setLast_ver(String last_ver) {
		this.last_ver = last_ver;
	}
	public int getSet_year() {
		return set_year;
	}
	public void setSet_year(int set_year) {
		this.set_year = set_year;
	}
	public String getRg_code() {
		return rg_code;
	}
	public void setRg_code(String rg_code) {
		this.rg_code = rg_code;
	}

}
