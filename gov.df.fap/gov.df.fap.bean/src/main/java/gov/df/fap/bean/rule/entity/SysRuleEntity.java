package gov.df.fap.bean.rule.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SysRuleEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private String rule_id;

    /** persistent field */
    private String rule_code;

    /** nullable persistent field */
    private String rule_name;

    /** nullable persistent field */
    private String remark;

    /** persistent field */
    private String set_year;

    /** nullable persistent field */
    private int enabled;

    /** nullable persistent field */
    private String rule_classify;

    /** nullable persistent field */
    private String sys_remark;

    /** nullable persistent field */
    private int right_type;

    /** nullable persistent field */
    private String create_user;

    /** nullable persistent field */
    private String create_date;

    /** nullable persistent field */
    private String latest_op_user;

    /** nullable persistent field */
    private String latest_op_date; 
    
    /** not nullable field  add by kongcy */
    private String rg_code;
	public String getRg_code() {
		return rg_code;
	}

	public void setRg_code(String rg_code) {
		this.rg_code = rg_code;
	}

	
 

	/** full constructor */
    public SysRuleEntity(String rule_id, String rule_code, String rule_name, String remark, String set_year, int enabled, String rule_classify, String sys_remark, int right_type, String create_user, String create_date, String latest_op_user, String latest_op_date) {
        this.rule_id = rule_id;
        this.rule_code = rule_code;
        this.rule_name = rule_name;
        this.remark = remark;
        this.set_year = set_year;
        this.enabled = enabled;
        this.rule_classify = rule_classify;
        this.sys_remark = sys_remark;
        this.right_type = right_type;
        this.create_user = create_user;
        this.create_date = create_date;
        this.latest_op_user = latest_op_user;
        this.latest_op_date = latest_op_date;
    }

    /** default constructor */
    public SysRuleEntity() {
    }

    /** minimal constructor */
    public SysRuleEntity(String rule_id, String rule_code, String set_year) {
        this.rule_id = rule_id;
        this.rule_code = rule_code;
        this.set_year = set_year;
    }

    public String getRule_id() {
        return this.rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String getRule_code() {
        return this.rule_code;
    }

    public void setRule_code(String rule_code) {
        this.rule_code = rule_code;
    }

    public String getRule_name() {
        return this.rule_name;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSet_year() {
        return this.set_year;
    }

    public void setSet_year(String set_year) {
        this.set_year = set_year;
    }

    public int getEnabled() {
        return this.enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getRule_classify() {
        return this.rule_classify;
    }

    public void setRule_classify(String rule_classify) {
        this.rule_classify = rule_classify;
    }

    public String getSys_remark() {
        return this.sys_remark;
    }

    public void setSys_remark(String sys_remark) {
        this.sys_remark = sys_remark;
    }

    public int getRight_type() {
        return this.right_type;
    }

    public void setRight_type(int right_type) {
        this.right_type = right_type;
    }

    public String getCreate_user() {
        return this.create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getCreate_date() {
        return this.create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getLatest_op_user() {
        return this.latest_op_user;
    }

    public void setLatest_op_user(String latest_op_user) {
        this.latest_op_user = latest_op_user;
    }

    public String getLatest_op_date() {
        return this.latest_op_date;
    }

    public void setLatest_op_date(String latest_op_date) {
        this.latest_op_date = latest_op_date;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("rule_id", getRule_id())
            .toString();
    }



}
