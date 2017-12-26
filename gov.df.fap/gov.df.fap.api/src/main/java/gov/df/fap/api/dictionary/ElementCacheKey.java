package gov.df.fap.api.dictionary;

import gov.df.fap.util.StringUtil;

/**
 * 缓存KEY,包含chr_id,set_year,rg_code,其实是要素表的复合主键结构
 * @author justin
 *
 */
public class ElementCacheKey {

	/**要素表id*/
	private String chrId = null;
	/**年度*/
	private String setYear = null;
	/**区域码*/
	private String rgCode = null;
	
	/**
	 * 要素唯一ID,年度,区域码
	 * @param chrId 
	 * @param setYear
	 * @param rgCode
	 */
	public ElementCacheKey(ElementInfo info){
		this.chrId = info.getChrId();
		this.setYear = info.getSetYear();
		this.rgCode = info.getRgCode();
	}
	
	/**
	 * 要素唯一ID,年度,区域码
	 * @param chrId 
	 * @param setYear
	 * @param rgCode
	 */
	public ElementCacheKey(String chrId, String setYear, String rgCode){
		this.chrId = chrId;
		this.setYear = setYear;
		this.rgCode = rgCode;
	}
	
	public boolean equals(Object key){
		if (key == null)
			return false;
		
		if (!(key instanceof ElementCacheKey))
			return false;
		
		ElementCacheKey keyObject = (ElementCacheKey) key;
		return StringUtil.equals(keyObject.chrId, chrId)
			&& StringUtil.equals(keyObject.rgCode, rgCode)
			&& StringUtil.equals(keyObject.setYear, setYear);
	}
	
	public int hashCode(){
		// 处理空指针问题， add by wanghongtao
		if(chrId==null){
			chrId="";
		}
		if(rgCode==null){
			rgCode="";
		}
		if(setYear==null){
			setYear="";
		}
		return chrId.hashCode()^rgCode.hashCode()^setYear.hashCode();
	}

	public String getChrId() {
		return chrId;
	}
	public String toString() {
    return chrId + rgCode + setYear;
  }
}
