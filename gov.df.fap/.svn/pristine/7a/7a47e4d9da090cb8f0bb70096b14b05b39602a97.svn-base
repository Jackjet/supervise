package gov.df.fap.bean.gl.configure;

import gov.df.fap.util.StringUtil;

import java.io.Serializable;


/**
 * 普通的对象KEY对象,主要用于对象的跨年度缓存
 * 
 * @author 
 * 
 */
public class CommonKey implements Serializable {

	private static final long serialVersionUID = 1L;

	public String id = null;

	public String setYear = null;

	public String rgCode = null;

	/**
	 * 
	 * @param id
	 * @param year
	 * @param rgCode
	 */
	public CommonKey(String id, String year, String rgCode) {
		this.id = id;
		this.setYear = year;
		this.rgCode = rgCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRgCode() {
		return rgCode;
	}

	public void setRgCode(String rgCode) {
		this.rgCode = rgCode;
	}

	public String getSetYear() {
		return setYear;
	}

	public void setSetYear(String setYear) {
		this.setYear = setYear;
	}

	public boolean equals(Object key) {
		if (key == null)
			return false;

		if (!(key instanceof CommonKey))
			return false;

		CommonKey keyObject = (CommonKey) key;
		return StringUtil.equals(keyObject.id, id)
				&& StringUtil.equals(keyObject.rgCode, rgCode)
				&& StringUtil.equals(keyObject.setYear, setYear);
	}

	public int hashCode() {
		return id.hashCode() ^ rgCode.hashCode() ^ setYear.hashCode();
	}

	public String toString() {
		return id + rgCode + setYear;
	}
}
