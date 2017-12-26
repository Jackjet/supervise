package gov.df.fap.service.gl.balance;

import gov.df.fap.bean.gl.configure.BusVouAcctmdl;
import gov.df.fap.bean.gl.dto.CtrlRecordDTO;
import gov.df.fap.bean.gl.dto.JournalDTO;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.service.util.exceptions.gl.GlException;
import gov.df.fap.util.number.NumberUtil;

/**
 * 额度追溯对象
 * @author 
 * @version 
 */
public class BalanceTracer {

	public static final int CTRL_SIDE_TARGET = 0;
	public static final int CTRL_SIDE_SOURCE = 1;
	
	
	private JournalDTO journal = null;
	private CtrlRecordDTO balance = null;
	private BusVouAcctmdl acctmdl = null;
    private FVoucherDTO voucherDTO = null;
	
    public BalanceTracer() {
    }
    
    public BalanceTracer(JournalDTO journal, CtrlRecordDTO balance, BusVouAcctmdl acctmdl){
        
        if (journal == null)
            throw new GlException("额度追溯对象必须含有凭证信息");
        if (balance == null)
            throw new GlException("额度追溯对象必须含有额度信息!");
        if (acctmdl == null)
            throw new GlException("额度追溯对象必须含有科目信息!");
        this.journal = journal;
        this.balance = balance;
        this.acctmdl = acctmdl;
    }
    
	public CtrlRecordDTO getBalance() {
		return balance;
	}
	public void setBalance(CtrlRecordDTO balance) {
		this.balance = balance;
	}
	public JournalDTO getJournal() {
		return journal;
	}
	public void setJournal(JournalDTO journal) {
		this.journal = journal;
	}
    
    public BusVouAcctmdl getAcctmdl() {
        return acctmdl;
    }

    public void setAcctmdl(BusVouAcctmdl acctmdl) {
        this.acctmdl = acctmdl;
    }
    
    private String vou_id;
    
    private String set_year;
    
    private String rg_code;
    
    private String vou_type_id;
    
    private String ctrlId;
    
    private int is_primary;
    
    private int ctrl_side;
    
    private String ctrl_type;
    
    private String last_ver;
    
    private String latest_op_date;
    
    private String create_date;
    
    public FVoucherDTO getVoucherDTO() {
        return voucherDTO;
    }

    public void setVoucherDTO(FVoucherDTO voucherDTO) {
        this.voucherDTO = voucherDTO;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public void setCtrl_side(int ctrl_side) {
        this.ctrl_side = ctrl_side;
    }

    public void setCtrl_type(String ctrl_type) {
        this.ctrl_type = ctrl_type;
    }

    public void setCtrlId(String ctrlId) {
        this.ctrlId = ctrlId;
    }

    public void setIs_primary(int is_primary) {
        this.is_primary = is_primary;
    }

    public void setLast_ver(String last_ver) {
        this.last_ver = last_ver;
    }

    public void setLatest_op_date(String latest_op_date) {
        this.latest_op_date = latest_op_date;
    }

    public void setRg_code(String rg_code) {
        this.rg_code = rg_code;
    }

    public void setSet_year(String set_year) {
        this.set_year = set_year;
    }

    public void setVou_id(String vou_id) {
        this.vou_id = vou_id;
    }

    public void setVou_type_id(String vou_type_id) {
        this.vou_type_id = vou_type_id;
    }

    public String getVou_id() {
        return journal == null ? this.vou_id : journal.getVou_id();
    }
    
    public int getSet_year() {
        return journal.getSet_year();
    }
    
    public String getRg_code() {
        return journal.getRg_code();
    }
    
    public long getVou_type_id() {
        return journal.getVou_type_id();
    }
    
    public String getCtrlId() {
        return balance == null ? this.ctrlId : balance.getSum_id();
    }
    
    public int getSet_month() {
        return journal.getSet_month();
    }
    
    public int getIs_primary() {
        return acctmdl == null ? this.is_primary : acctmdl.getIs_primary_source()==1? 1 : acctmdl.getIs_primary_target();
//        return acctmdl == null ? this.is_primary : acctmdl.getEntry_side() == 1 ? acctmdl.getIs_primary_source() : acctmdl.getIs_primary_target();
    }
    
    public String getCreate_date() {
        return journal.getCreate_date();
    }
    
    public String getLatest_op_date() {
        return journal.getLatest_op_date();
    }

    public String getLast_ver() {
        return journal.getLast_ver();
    }
    
    public String getCtrl_type() {
        return acctmdl == null ? this.ctrl_type : acctmdl.getBusVouAccount().getAccountId();
    }
    
    public int getCtrl_side() {
        return acctmdl == null ? this.ctrl_side : NumberUtil.xor(acctmdl.getEntry_side(), acctmdl.getBusVouAccount().getBalanceSide());
    }
 
}
