package gov.df.fap.service.dictionary.elecache;

import gov.df.fap.api.dictionary.ElementCacheKey;
import gov.df.fap.api.dictionary.ElementInfo;
import gov.df.fap.bean.dictionary.element.ElementDefinition;
import gov.df.fap.service.util.dictionary.interfaces.CachedObject;
import gov.df.fap.util.number.NumberUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.List;

public class CachedElement implements CachedObject, Cloneable, ElementInfo{
	
    public static final XMLData ROOT_OBJECT = new XMLData();
    
	public static final String CHR_CODE = "chr_code";
	public static final String CHR_ID = "chr_id";
	public static final String DISP_NAME = "disp_name";
	public static final String CHR_NAME = "chr_name";
	public static final String DISP_CODE = "disp_code";
	public static final String SET_YEAR = "set_year";
	public static final String LEVEL_NUM = "level_num";
	public static final String PARENT_ID = "parent_id";
	public static final String RG_CODE = "rg_code";
	
	private XMLData element = null;
	private long createTime = -1;
	private CachedElement parent = null;
	private List children = new ArrayList();
	
	/**最近命中时间*/
	long lastHitTime = 0;
	/***/
	long hitCount = 0;
	
	public CachedElement(XMLData element){
		this(element, null);
	}
    
	public CachedElement(XMLData element, CachedElement parent){
		this.element = element;
		this.parent = parent;
		this.createTime = System.currentTimeMillis();
	}
	
	/* (non-Javadoc)
	 * @see gov.gfmis.fap.dictionary.service.elecache.ElementInfo#getChrCode()
	 */
	public String getChrCode() {
		return (String)element.get(CHR_CODE);
	}

	/* (non-Javadoc)
	 * @see gov.gfmis.fap.dictionary.service.elecache.ElementInfo#getChrId()
	 */
	public String getChrId() {
		return (String)element.get(CHR_ID);
	}

	/* (non-Javadoc)
	 * @see gov.gfmis.fap.dictionary.service.elecache.ElementInfo#getLevelNum()
	 */
	public int getLevelNum() {
		return NumberUtil.toInt((String)element.get(LEVEL_NUM));
	}

	/* (non-Javadoc)
	 * @see gov.gfmis.fap.dictionary.service.elecache.ElementInfo#getParentId()
	 */
	public String getParentId(){
		return (String)element.get(PARENT_ID);
	}
	
	public long getCreateTime() {
		return createTime;
	}

	public Object getObjectCached() {
		return element.clone();
	}

	public int size() {
		return 1;
	}
	
	protected void setParent(CachedElement element){
		this.parent = element;
	}
	
	public void removeChild(ElementInfo element){
		if (children.contains(element))
			children.remove(element);
	}
	
	protected void addChild(CachedElement childElement){
		if (childElement.getParent() != null)
			childElement.getParent().removeChild(childElement);
		childElement.setParent(this);
		children.add(childElement);
	}
	
	protected void addChild(XMLData childElement){
		addChild(new CachedElement(childElement));
	}

	/* (non-Javadoc)
	 * @see gov.gfmis.fap.dictionary.service.elecache.ElementInfo#getChildren()
	 */
	public List getChildren() {
		return children;
	}

	/* (non-Javadoc)
	 * @see gov.gfmis.fap.dictionary.service.elecache.ElementInfo#getParent()
	 */
	public ElementInfo getParent() {
		return parent;
	}
	
	public void setLastHitTime(long time){
		this.lastHitTime = time;
	}
	
	public long getLastHitTime(){
		return lastHitTime;
	}

	public void setHitCount(long count){
		this.hitCount = count;
	}
	
	public long getHitCount() {
		return hitCount;
	}
	
	public Object getKey(){
		return new ElementCacheKey((String) getChrId(), (String) getSetYear(), (String) getRgCode());
	}

	public String getChrName() {
		return (String)element.get(CHR_NAME);
	}

	public boolean isLeaf() {
		return "1".equals(element.get(ElementDefinition.IS_LEAF));
	}

	public String getAccountNo() {
		return (String)element.get(ElementDefinition.ACCOUNT_NO);
	}

	public String getAccountName() {
		return (String)element.get(ElementDefinition.ACCOUNT_NAME);
	}

	public String getBankName() {
		return (String)element.get(ElementDefinition.BANK_NAME);
	}

    public boolean isRoot() {
        return element == ROOT_OBJECT;
    }
    
    /**
     * 为了remove方法所写的equals方法
     */
    public boolean equals(Object obj){
    	if (obj == null || !(obj instanceof CachedElement))
    		return false;
    	
    	CachedElement element = (CachedElement)obj;
    	if (element.getChrId() != null && this.getChrId() != null)
    		return getChrId().equals(element.getChrId());
    	else if (element.getChrId() == null && this.getChrId() == null)
    		return element == this;
    	else 
    		return false;
    	
    }

	public String getSetYear() {
		return (String) element.get(ElementDefinition.SET_YEAR);
	}

	public String getRgCode() {
		return (String) element.get(ElementDefinition.RG_CODE);
	}

	public Object getParentKey() {
		return new ElementCacheKey((String) getParentId(), (String) getSetYear(), (String) getRgCode());
	}
}
