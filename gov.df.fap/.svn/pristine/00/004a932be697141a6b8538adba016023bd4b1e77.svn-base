package gov.df.fap.bean.rule.dto;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class RightGroupTypeDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3357730752501065817L;

	/** identifier field */
    private String RIGHT_GROUP_ID;

    /** identifier field */
    private String ELE_CODE;
    
    private String ELE_NAME;    

    /** nullable persistent field */
    private int RIGHT_TYPE;

    /** nullable persistent field */
    private String SET_YEAR;
    
    public String getRG_CODE() {
		return RG_CODE;
	}

	public void setRG_CODE(String rG_CODE) {
		RG_CODE = rG_CODE;
	}

	/** not nullable  field add by kongcy at 2012-03-28 13:51:23*/
    private String RG_CODE;


    /** full constructor */
    public RightGroupTypeDTO(String RIGHT_GROUP_ID, String ELE_CODE, int RIGHT_TYPE, String SET_YEAR) {
        this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
        this.ELE_CODE = ELE_CODE;
        this.RIGHT_TYPE = RIGHT_TYPE;
        this.SET_YEAR = SET_YEAR;
    }

    /** default constructor */
    public RightGroupTypeDTO() {
    }

    /** minimal constructor */
    public RightGroupTypeDTO(String RIGHT_GROUP_ID, String ELE_CODE) {
        this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
        this.ELE_CODE = ELE_CODE;
    }

    public String getRIGHT_GROUP_ID() {
        return this.RIGHT_GROUP_ID;
    }

    public void setRIGHT_GROUP_ID(String RIGHT_GROUP_ID) {
        this.RIGHT_GROUP_ID = RIGHT_GROUP_ID;
    }

    public String getELE_CODE() {
        return this.ELE_CODE;
    }

    public void setELE_CODE(String ELE_CODE) {
        this.ELE_CODE = ELE_CODE;
    }

    public int getRIGHT_TYPE() {
        return this.RIGHT_TYPE;
    }

    public void setRIGHT_TYPE(int RIGHT_TYPE) {
        this.RIGHT_TYPE = RIGHT_TYPE;
    }

    public String getSET_YEAR() {
        return this.SET_YEAR;
    }

    public void setSET_YEAR(String SET_YEAR) {
        this.SET_YEAR = SET_YEAR;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("RIGHT_GROUP_ID", getRIGHT_GROUP_ID())
            .append("ELE_CODE", getELE_CODE())
            .toString();
    }

	public String getELE_NAME() {
		return ELE_NAME;
	}

	public void setELE_NAME(String ele_name) {
		ELE_NAME = ele_name;
	}

}
