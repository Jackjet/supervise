package gov.df.fap.api.dictionary;

import gov.df.fap.bean.dictionary.element.ElementDefinition;
import gov.df.fap.util.xml.XMLData;

/**
 * 
 * @author 
 *
 */
public class ElementSetXMLData extends XMLData implements ElementSet{

  private static final long serialVersionUID = 1L;

  public String getChrId() {
		return (String) this.get(ElementDefinition.CHR_ID);
	}

	public String getEleCode() {
		return (String) this.get(ElementDefinition.ELE_CODE);
	}

	public String getEleSource() {
		return (String) this.get(ElementDefinition.ELE_SOURCE);
	}

	public String getEleName() {
		return (String) this.get(ElementDefinition.ELE_NAME);
	}

	public int getMaxLevel() {
		return ((Integer) this.get("max_level")).intValue();
	}

	public String getSetYear() {
		return (String) this.get("set_year");
	}

	public String getDispOrder() {
		return (String) this.get("disp_order");
	}

}
