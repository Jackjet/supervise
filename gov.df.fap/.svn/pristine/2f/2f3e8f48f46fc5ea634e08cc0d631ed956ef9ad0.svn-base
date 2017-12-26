package gov.df.fap.bean.gl.coa;

import gov.df.fap.bean.dictionary.element.ElementDefinition;
import gov.df.fap.bean.gl.configure.CommonKey;
import gov.df.fap.util.StringUtil;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * COA对象
 * @author 
 * @version
 *
 */
public class FCoaDTO implements Cloneable, Serializable {

  private static final long serialVersionUID = 1L;

  public static final String DEFAULT_CCID_TABLE = "gl_ccids";

  public static final String[] BUSINESS_STATUS = { "指标管理", "计划管理", "支付管理", "实拨管理", "其他" };

  private List<FCoaDetail> coaDetailList;

  private String coaId = StringUtil.EMPTY;

  private String coaCode = StringUtil.EMPTY;//

  private String coaName = StringUtil.EMPTY;//

  private String coaDesc = StringUtil.EMPTY;//

  private String ccidsTable = StringUtil.EMPTY;//

  //create_user 放的是user_id
  private String createUser = StringUtil.EMPTY;

  private String createDate = StringUtil.EMPTY;

  private String latestOpUser = StringUtil.EMPTY;

  private String lastVer = StringUtil.EMPTY;

  private int setYear = 2007;

  private String latestOpDate = StringUtil.EMPTY;

  private int enabled = 0;

  private String rgCode = StringUtil.EMPTY;

  /**是否存在任意级次*/
  private Boolean hasAutoLevelElement = Boolean.FALSE;

  //应用的子系统
  private String sysAppName = StringUtil.EMPTY;

  public String getCcidsTable() {
    if (StringUtil.isEmpty(ccidsTable))
      return DEFAULT_CCID_TABLE;
    return ccidsTable;
  }

  public void setCcidsTable(String ccidsTable) {
    this.ccidsTable = ccidsTable;
  }

  public Object getKey() {
    return new CommonKey(coaId, String.valueOf(setYear), rgCode);
  }

  public String getRgCode() {
    return rgCode;
  }

  public void setRgCode(String rgCode) {
    this.rgCode = rgCode;
  }

  public int getEnabled() {
    return enabled;
  }

  public void setEnabled(int enable) {
    this.enabled = enable;
  }

  public String getLatestOpDate() {
    return latestOpDate;
  }

  public void setLatestOpDate(String latestOpDate) {
    this.latestOpDate = latestOpDate;
  }

  public String getCoaCode() {
    return coaCode;
  }

  public void setCoaCode(String coaCode) {
    this.coaCode = coaCode;
  }

  public List getCoaDetailList() {
    return coaDetailList;
  }

  public void setCoaDetailList(List coaDetailList) {
    this.coaDetailList = coaDetailList;
    /* for (int i = 0; i < size(); i++) {
       get(i).setCoaDTO(this);
     }*/
  }

  public String getCoaId() {
    return coaId;
  }

  public void setCoaId(String coaId) {
    this.coaId = coaId;
  }

  public void setCoaDesc(String coaDesc) {
    this.coaDesc = coaDesc;
  }

  public String getCoaDesc() {
    return coaDesc;
  }

  public String getCoaName() {
    return coaName;
  }

  public void setCoaName(String coaName) {
    this.coaName = coaName;
  }

  public int getSetYear() {
    return setYear;
  }

  public void setSetYear(int setYear) {
    this.setYear = setYear;
  }

  public void setCoaDetail(List list) {
    coaDetailList = list;
  }

  public List getCoaDetail() {
    return coaDetailList;
  }

  public int size() {
    return coaDetailList.size();
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getCreateDate() {
    return createDate;
  }

  public void setLatestOpUser(String latest_op_user) {
    this.latestOpUser = latest_op_user;
  }

  public String getLatestOpUser() {
    return latestOpUser;
  }

  public void setLastVer(String last_ver) {
    this.lastVer = last_ver;
  }

  public String getLastVer() {
    return lastVer;
  }

  public FCoaDetail get(int i) {
    return (FCoaDetail) coaDetailList.get(i);
  }

  public void addCoaDetail(FCoaDetail coaDetail) {
    if (coaDetail == null)
      return;
    else {
      // coaDetail.setCoaDTO(this);
      coaDetailList.add(coaDetail);
    }
  }

  public boolean hasAutoLevelElement() {
    for (int i = 0; i < this.size(); i++)
      if (get(i).getLevelNum() == ElementDefinition.LEVEL_NUM_AUTO) {
        this.hasAutoLevelElement = Boolean.TRUE;
        break;
      }
    return hasAutoLevelElement.booleanValue();
  }

  public String toString() {
    return coaCode + " " + coaName;
  }

  public Object clone() {
     FCoaDTO coaDTO = null;
     try {
       coaDTO = (FCoaDTO) super.clone();
     } catch (CloneNotSupportedException e) {
       e.printStackTrace();
     }
     LinkedList coaDetailList = new LinkedList();
     Iterator it = this.getCoaDetail().iterator();
     while (it.hasNext()) {
       FCoaDetail coaDetail = (FCoaDetail) ((FCoaDetail) it.next()).clone();
       coaDetailList.add(coaDetail);
     }
     coaDTO.setCoaDetailList(coaDetailList);
     return coaDTO;
   }

  public String getSysAppName() {
    return sysAppName;
  }

  public void setSysAppName(String sysAppName) {
    this.sysAppName = sysAppName;
  }
}
