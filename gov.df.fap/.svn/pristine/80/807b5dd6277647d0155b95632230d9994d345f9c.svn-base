package gov.df.fap.bean.rule.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SysRightGroupDetailEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private String right_group_id;

    /** identifier field */
    private String ele_code;
    
    private String ele_name;

    /** identifier field */
    private String ele_value;

    /** identifier field */
    private String set_year;
    
    /** not nullable  field add by kongcy at 2012-03-28 13:51:23*/
    private String rg_code;

    public String getRg_code() {
		return this.rg_code;
	}

	public void setRg_code(String rg_code) {
		this.rg_code = rg_code;
	}

    /** full constructor */
    public SysRightGroupDetailEntity(String right_group_id, String ele_code, String ele_value, String set_year,String ele_name) {
        this.right_group_id = right_group_id;
        this.ele_code = ele_code;
        this.ele_name = ele_name;
        this.ele_value = ele_value;
        this.set_year = set_year;
    }

    /** default constructor */
    public SysRightGroupDetailEntity() {
    }

    public String getRight_group_id() {
        return this.right_group_id;
    }

    public void setRight_group_id(String right_group_id) {
        this.right_group_id = right_group_id;
    }

    public String getEle_code() {
        return this.ele_code;
    }

    public void setEle_code(String ele_code) {
        this.ele_code = ele_code;
    }

    public String getEle_value() {
        return this.ele_value;
    }

    public void setEle_value(String ele_value) {
        this.ele_value = ele_value;
    }

    public String getSet_year() {
        return this.set_year;
    }

    public void setSet_year(String set_year) {
        this.set_year = set_year;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("right_group_id", getRight_group_id())
            .append("ele_code", getEle_code())
            .append("ele_value", getEle_value())
            .append("set_year", getSet_year())
            .append("ele_name", getEle_name())
            .toString();
    }

	public String getEle_name() {
		return ele_name;
	}

	public void setEle_name(String ele_name) {
		this.ele_name = ele_name;
	}

}
