package gov.df.fap.bean.rule.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class RightGroupDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8110540493878179808L;

	/** identifier field */
    private String RIGHT_GROUP_ID;

    /** nullable persistent field */
    private String RIGHT_GROUP_DESCRIBE;
    
    /** nullable persistent field */
    private int RIGHT_TYPE;

    /** nullable persistent field */
    private String RULE_ID;

    public List type_list = new ArrayList();
    
    public List detail_list = new ArrayList();
    
    
    /** not nullable  field add by kongcy at 2012-03-28 13:51:23*/
    private String SET_YEAR; 
    
    public String getSET_YEAR() {
		return SET_YEAR;
	}

	public void setSET_YEAR(String sET_YEAR) {
		SET_YEAR = sET_YEAR;
	}
    /** not nullable  field add by kongcy at 2012-03-28 13:51:23*/
	public String getRG_CODE() {
		return RG_CODE;
	}

	public void setRG_CODE(String rG_CODE) {
		RG_CODE = rG_CODE;
	}

	/** not nullable persistent field */
    private String RG_CODE;

    /** full constructor */
    public RightGroupDTO(String RIGHT_GROUP_ID, String RIGHT_GROUP_DESCRIBE, int RIGHT_TYPE, String RULE_ID) {
        this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
        this.RIGHT_GROUP_DESCRIBE = RIGHT_GROUP_DESCRIBE;
        this.RIGHT_TYPE = RIGHT_TYPE;
        this.RULE_ID = RULE_ID;
    }

    /** default constructor */
    public RightGroupDTO() {
    }

    /** minimal constructor */
    public RightGroupDTO(String RIGHT_GROUP_ID) {
        this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
    }

    public String getRIGHT_GROUP_ID() {
        return this.RIGHT_GROUP_ID;
    }

    public void setRIGHT_GROUP_ID(String RIGHT_GROUP_ID) {
        this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
    }

    public String getRIGHT_GROUP_DESCRIBE() {
        return this.RIGHT_GROUP_DESCRIBE;
    }

    public void setRIGHT_GROUP_DESCRIBE(String RIGHT_GROUP_DESCRIBE) {
        this.RIGHT_GROUP_DESCRIBE = RIGHT_GROUP_DESCRIBE;
    }

    public int getRIGHT_TYPE() {
        return this.RIGHT_TYPE;
    }

    public void setRIGHT_TYPE(int right_type) {
        this.RIGHT_TYPE = right_type;
    }

    public String getRULE_ID() {
        return this.RULE_ID;
    }

    public void setRULE_ID(String RULE_ID) {
        this.RULE_ID = RULE_ID;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("RIGHT_GROUP_ID", getRIGHT_GROUP_ID())
            .toString();
    }
}
