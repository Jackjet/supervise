package gov.df.fap.bean.gl.configure;

import gov.df.fap.util.StringUtil;
import gov.df.fap.bean.rule.dto.RightGroupTypeDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 记账模板Bean
 * @author LiuYan
 * @version 2008-03-07
 */
public class BusVouType implements Cloneable, Serializable{

	private static final long serialVersionUID = -4355802850640362327L;

	private long vou_type_id = 0;
    
    private String vou_type_code = StringUtil.EMPTY;
    
    private String vou_type_name = StringUtil.EMPTY;
    
    private List busVouAcctmdl = new ArrayList();
    
    private String latest_op_date;
    
    private String latest_op_user;
    
    private String last_ver;
    
    private String rg_code;
    
    private int set_year;
    
    private int enabled = 1;
    
    //会计分录
    private List accountEntryList = new LinkedList();
    
	public List getAccountEntryList() {
		return accountEntryList;
	}

	public void setAccountEntryList(List accountEntryList) {
		this.accountEntryList = accountEntryList;
	}

	public BusVouType() {
        super();
    }
    
	public Object getKey(){
		return new CommonKey(String.valueOf(vou_type_id), String.valueOf(set_year), rg_code);
	}
	
    //界面中显示自动调用的方法
    public String toString() {
    	String bvTypeCode = vou_type_code != null ? vou_type_code : StringUtil.EMPTY;
    	String bvTypeName = vou_type_name != null ? vou_type_name : StringUtil.EMPTY;
        return bvTypeCode + " " + bvTypeName;
    }

    public String getLast_ver() {
        return last_ver;
    }

    public void setLast_ver(String last_ver) {
        this.last_ver = last_ver;
    }

    public String getLatest_op_date() {
        return latest_op_date;
    }

    public void setLatest_op_date(String latest_op_date) {
        this.latest_op_date = latest_op_date;
    }

    public String getLatest_op_user() {
        return latest_op_user;
    }

    public void setLatest_op_user(String latest_op_user) {
        this.latest_op_user = latest_op_user;
    }

    public String getRg_code() {
        return rg_code;
    }

    public void setRg_code(String rg_code) {
        this.rg_code = rg_code;
    }

    public int getSet_year() {
        return set_year;
    }

    public void setSet_year(int set_year) {
        this.set_year = set_year;
    }

    public List getBusVouAcctmdl() {
        return busVouAcctmdl;
    }

    public void setBusVouAcctmdl(List busVouAcctmdl) {
    	if (busVouAcctmdl == null || busVouAcctmdl.isEmpty())
    		return;
    	for (int i = 0; i < busVouAcctmdl.size(); i++){
    		final BusVouAcctmdl acctmdl = (BusVouAcctmdl) busVouAcctmdl.get(i);
    		if (acctmdl.getBvType() != null
    				&& this.vou_type_id != acctmdl.getBvType().getVou_type_id()
    				&& busVouAcctmdl != acctmdl.getBvType().getBusVouAcctmdl())
    			acctmdl.getBvType().removeAcctmdl(acctmdl);
    			
    		acctmdl.setBvType(this);
    	}
        this.busVouAcctmdl = busVouAcctmdl;
    }
    
    public String getVou_type_code() {
        return vou_type_code;
    }

    public void setVou_type_code(String vou_type_code) {
        this.vou_type_code = vou_type_code;
    }

    public long getVou_type_id() {
        return vou_type_id;
    }

    public void setVou_type_id(long vou_type_id) {
        this.vou_type_id = vou_type_id;
    }

    public String getVou_type_name() {
        return vou_type_name;
    }

    public void setVou_type_name(String vou_type_name) {
        this.vou_type_name = vou_type_name;
    }

    /**
     * 插入与科目连接，先使记账模板与旧科目断开
     * @param acctmdl
     * @author 
     * @since 6.2.61.07
     */
    public void addAcctmdl(BusVouAcctmdl acctmdl){
    	BusVouType parent = acctmdl.getBvType();
    	if (parent == this)
    		return;
    	
    	if (parent != null && parent.getBusVouAcctmdl() != null){
    		parent.removeAcctmdl(acctmdl);
    	}
    	acctmdl.setBvType(this);
    	busVouAcctmdl.add(acctmdl);
    }
    
    /**
     * 
     * @param acctmdl
     */
    public void removeAcctmdl(BusVouAcctmdl acctmdl){
    	acctmdl.setBvType(null);
    	busVouAcctmdl.remove(acctmdl);
    }
    
    public Object clone() throws CloneNotSupportedException{
    	return super.clone();
    }

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enable) {
		this.enabled = enable;
	}
	
//	public List getRuleConfigure() {
//		final List eleTypeList = new RuleList();
//		try {
//			final BusVouType bvType = EngineConfiguration.configureEleCode(this);
//			final Iterator interator = bvType.getBusVouAcctmdl().iterator();
//			BusVouAcctmdl acctmdl = null;
//			Iterator tmpIt = null;
//			RightGroupTypeDTO groupTypeDto = null;
//			while (interator.hasNext()) {
//				acctmdl = (BusVouAcctmdl) interator.next();
//				tmpIt = acctmdl.getRuleEleConfigure().iterator();
//				while (tmpIt.hasNext()) {
//					groupTypeDto = (RightGroupTypeDTO) tmpIt.next();
//					if (!eleTypeList.contains(groupTypeDto))
//						eleTypeList.add(groupTypeDto);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return eleTypeList;
//	}
	
	/**
	 * copy部分重要属性
	 */
	public void copy(BusVouType bv){
		if (bv == null)
			return;
		this.setVou_type_code(bv.getVou_type_code());
		this.setVou_type_name(bv.getVou_type_name());
		this.setBusVouAcctmdl(bv.getBusVouAcctmdl());
		this.setEnabled(bv.getEnabled());
	}
	
	public boolean equals(Object o){
		if (o == null)
			return false;
		
		if (!(o instanceof BusVouType))
			return false;
		
		BusVouType a = (BusVouType)o;
		
		return this.vou_type_id == a.vou_type_id;
	}
	
	public int hashCode(){
		return (int)this.vou_type_id;
	}
	
	class RuleList extends LinkedList {
		private static final long serialVersionUID = 1L;
		
		final Set codeSet = new HashSet();
		
		public boolean contains(Object obj) {
			if (obj instanceof RightGroupTypeDTO) {
				return codeSet.contains(((RightGroupTypeDTO) obj).getELE_CODE());
			} else
				return super.contains(obj);
		}
		
		public boolean add(Object obj) {
			if (obj instanceof RightGroupTypeDTO)
				codeSet.add(((RightGroupTypeDTO) obj).getELE_CODE());
			return super.add(obj);
        }
	}
}
