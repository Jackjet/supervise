package gov.df.fap.bean.rule.entity;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class RightGroupTypeEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3357730752501065817L;

	/** identifier field */
    private String right_group_id;

    /** identifier field */
    private String ele_code;
    
    private String ele_name;    

    /** nullable persistent field */
    private int right_type;

    /** nullable persistent field */
    private String set_year;
    
    public String getRg_code() {
		return this.rg_code;
	}

	public void setRg_code(String rg_code) {
		this.rg_code = rg_code;
	}

	/** not nullable  field add by kongcy at 2012-03-28 13:51:23*/
    private String rg_code;


    /** full constructor */
    public RightGroupTypeEntity(String right_group_id, String ele_code, int right_type, String set_year) {
        this.right_group_id = right_group_id;
        this.ele_code = ele_code;
        this.right_type = right_type;
        this.set_year = set_year;
    }

    /** default constructor */
    public RightGroupTypeEntity() {
    }

    /** minimal constructor */
    public RightGroupTypeEntity(String right_group_id, String ele_code) {
        this.right_group_id = right_group_id;
        this.ele_code = ele_code;
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

    public int getRight_type() {
        return this.right_type;
    }

    public void setRight_type(int right_type) {
        this.right_type = right_type;
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
            .toString();
    }

	public String getEle_name() {
		return this.ele_name;
	}

	public void setEle_name(String ele_name) {
		this.ele_name = ele_name;
	}

}
