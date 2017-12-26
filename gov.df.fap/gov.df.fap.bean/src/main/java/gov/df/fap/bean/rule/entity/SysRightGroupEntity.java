package gov.df.fap.bean.rule.entity;

import gov.df.fap.bean.rule.dto.RightGroupTypeDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SysRightGroupEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private String right_group_id;

    /** nullable persistent field */
    private String right_group_describe;
    
    /** nullable persistent field */
    private int right_type;

    /** nullable persistent field */
    private String rule_id;

 
    
    /** not nullable  field add by kongcy at 2012-03-28 13:51:23*/
    private String set_year; 
    

	public String getSet_year() {
		return this.set_year;
	}

	public void setSet_year(String set_year) {
		this.set_year = set_year;
	}
    /** not nullable  field add by kongcy at 2012-03-28 13:51:23*/
	public String getRg_code() {
		return this.rg_code;
	}

	public void setRg_code(String rg_code) {
		this.rg_code = rg_code;
	}

	/** not nullable persistent field */
    private String rg_code;

    /** full constructor */
    public SysRightGroupEntity(String right_group_id, String right_group_describe, int right_type, String rule_id) {
        this.right_group_id = right_group_id;
        this.right_group_describe = right_group_describe;
        this.right_type = right_type;
        this.rule_id = rule_id;
    }

    /** default constructor */
    public SysRightGroupEntity() {
    }

    /** minimal constructor */
    public SysRightGroupEntity(String right_group_id) {
        this.right_group_id = right_group_id;
    }

    public String getRight_group_id() {
        return this.right_group_id;
    }

    public void setRight_group_id(String right_group_id) {
        this.right_group_id = right_group_id;
    }

    public String getRight_group_describe() {
        return this.right_group_describe;
    }

    public void setRight_group_describe(String right_group_describe) {
        this.right_group_describe = right_group_describe;
    }

    public int getRight_type() {
        return this.right_type;
    }

    public void setRight_type(int right_type) {
        this.right_type = right_type;
    }

    public String getrule_id() {
        return this.rule_id;
    }

    public void setRule_id(String rule_id) {
        this.rule_id = rule_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("right_group_id", getRight_group_id())
            .toString();
    }

}
