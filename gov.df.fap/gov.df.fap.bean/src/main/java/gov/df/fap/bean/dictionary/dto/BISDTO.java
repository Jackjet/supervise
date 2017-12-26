package gov.df.fap.bean.dictionary.dto;

import gov.df.fap.bean.util.DTO;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

public class BISDTO extends DTO {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**要素ID*/
  private String CHR_ID;

  /**项目编码*/
  private String CHR_CODE;

  /**项目编码*/
  private String DISP_CODE;

  /**项目名称*/
  private String CHR_NAME;

  /**要素级次*/
  private String LEVEL_NUM;

  /**是否底级*/
  private String IS_LEAF;

  /**是否启用*/
  private String ENABLED;

  /**创建时间*/
  private String CREATE_DATE;

  /**创建用户*/
  private String CREATE_USER;

  /**最后修改时间*/
  private String LATEST_OP_DATE;

  /**最后修改用户*/
  private String LATEST_OP_USER;

  /**最后版本*/
  private String LAST_VER;

  /**是否删除*/
  private String IS_DELETED;

  /**父级CODE1*/
  private String CHR_CODE1;

  /**父级CODE2*/
  private String CHR_CODE2;

  /**父级CODE3*/
  private String CHR_CODE3;

  /**父级CODE4*/
  private String CHR_CODE4;

  /**父级CODE5*/
  private String CHR_CODE5;

  /**父级ID*/
  private String PARENT_ID;

  /**父级ID1*/
  private String CHR_ID1;

  /**父级ID2*/
  private String CHR_ID2;

  /**父级ID3*/
  private String CHR_ID3;

  /**父级ID4*/
  private String CHR_ID4;

  /**父级ID5*/
  private String CHR_ID5;

  /**区划*/
  private String RG_CODE;

  /**年度*/
  private String SET_YEAR;

  /***开始时间*/
  private String START_DATE;

  /***结束时间*/
  private String END_DATE;

  /***是否公用项目*/
  private String PUBLIC_SIGN;

  /***项目说明*/
  private String REMARK;

  private String SYS_ID;

  private String BIS_ID;

  /**上游主键*/
  private String UPPER_ID;

  /**所属信息
   * BELONG_LIST中存放的是XMLData，XMLData中存放BIS_ID,YEAR,MB_ID ,EN_ID,BI_ID,CHR_CODE_SUP
   * */
  private List BELONG_LIST;

  /**资金信息
   * MONEY_LIST中存放的是XMLData，XMLData中存放BIS_ID,YEAR,TOTAL_MONEY,MONEY,ADJUST_MONEY,USE_MONEY
   * */
  private List MONEY_LIST;

  public List getBELONG_LIST() {
    return BELONG_LIST;
  }

  public void setBELONG_LIST(List bELONG_LIST) {
    this.BELONG_LIST = bELONG_LIST;
  }

  public List getMONEY_LIST() {
    return MONEY_LIST;
  }

  public void setMONEY_LIST(List mONEY_LIST) {
    this.MONEY_LIST = mONEY_LIST;
  }

  public String getUPPER_ID() {
    return UPPER_ID;
  }

  public void setUPPER_ID(String uPPER_ID) {
    this.UPPER_ID = uPPER_ID;
  }

  public String getBIS_ID() {
    return BIS_ID;
  }

  public void setBIS_ID(String bIS_ID) {
    this.BIS_ID = bIS_ID;
  }

  public String getCHR_ID() {
    return CHR_ID;
  }

  public void setCHR_ID(String cHR_ID) {
    this.CHR_ID = cHR_ID;
  }

  public String getCHR_CODE() {
    return CHR_CODE;
  }

  public void setCHR_CODE(String cHR_CODE) {
    this.CHR_CODE = cHR_CODE;
  }

  public String getDISP_CODE() {
    return DISP_CODE;
  }

  public void setDISP_CODE(String dISP_CODE) {
    this.DISP_CODE = dISP_CODE;
  }

  public String getCHR_NAME() {
    return CHR_NAME;
  }

  public void setCHR_NAME(String cHR_NAME) {
    this.CHR_NAME = cHR_NAME;
  }

  public String getLEVEL_NUM() {
    return LEVEL_NUM;
  }

  public void setLEVEL_NUM(String lEVEL_NUM) {
    this.LEVEL_NUM = lEVEL_NUM;
  }

  public String getIS_LEAF() {
    return IS_LEAF;
  }

  public void setIS_LEAF(String iS_LEAF) {
    this.IS_LEAF = iS_LEAF;
  }

  public String getENABLED() {
    return ENABLED;
  }

  public void setENABLED(String eNABLED) {
    this.ENABLED = eNABLED;
  }

  public String getCREATE_DATE() {
    return CREATE_DATE;
  }

  public void setCREATE_DATE(String cREATE_DATE) {
    this.CREATE_DATE = cREATE_DATE;
  }

  public String getCREATE_USER() {
    return CREATE_USER;
  }

  public void setCREATE_USER(String cREATE_USER) {
    this.CREATE_USER = cREATE_USER;
  }

  public String getLATEST_OP_DATE() {
    return LATEST_OP_DATE;
  }

  public void setLATEST_OP_DATE(String lATEST_OP_DATE) {
    this.LATEST_OP_DATE = lATEST_OP_DATE;
  }

  public String getLATEST_OP_USER() {
    return LATEST_OP_USER;
  }

  public void setLATEST_OP_USER(String lATEST_OP_USER) {
    this.LATEST_OP_USER = lATEST_OP_USER;
  }

  public String getLAST_VER() {
    return LAST_VER;
  }

  public void setLAST_VER(String lAST_VER) {
    this.LAST_VER = lAST_VER;
  }

  public String getIS_DELETED() {
    return IS_DELETED;
  }

  public void setIS_DELETED(String iS_DELETED) {
    this.IS_DELETED = iS_DELETED;
  }

  public String getCHR_CODE1() {
    return CHR_CODE1;
  }

  public void setCHR_CODE1(String cHR_CODE1) {
    this.CHR_CODE1 = cHR_CODE1;
  }

  public String getCHR_CODE2() {
    return CHR_CODE2;
  }

  public void setCHR_CODE2(String cHR_CODE2) {
    this.CHR_CODE2 = cHR_CODE2;
  }

  public String getCHR_CODE3() {
    return CHR_CODE3;
  }

  public void setCHR_CODE3(String cHR_CODE3) {
    this.CHR_CODE3 = cHR_CODE3;
  }

  public String getCHR_CODE4() {
    return CHR_CODE4;
  }

  public void setCHR_CODE4(String cHR_CODE4) {
    this.CHR_CODE4 = cHR_CODE4;
  }

  public String getCHR_CODE5() {
    return CHR_CODE5;
  }

  public void setCHR_CODE5(String cHR_CODE5) {
    this.CHR_CODE5 = cHR_CODE5;
  }

  public String getPARENT_ID() {
    return PARENT_ID;
  }

  public void setPARENT_ID(String pARENT_ID) {
    this.PARENT_ID = pARENT_ID;
  }

  public String getCHR_ID1() {
    return CHR_ID1;
  }

  public void setCHR_ID1(String cHR_ID1) {
    this.CHR_ID1 = cHR_ID1;
  }

  public String getCHR_ID2() {
    return CHR_ID2;
  }

  public void setCHR_ID2(String cHR_ID2) {
    this.CHR_ID2 = cHR_ID2;
  }

  public String getCHR_ID3() {
    return CHR_ID3;
  }

  public void setCHR_ID3(String cHR_ID3) {
    this.CHR_ID3 = cHR_ID3;
  }

  public String getCHR_ID4() {
    return CHR_ID4;
  }

  public void setCHR_ID4(String cHR_ID4) {
    this.CHR_ID4 = cHR_ID4;
  }

  public String getCHR_ID5() {
    return CHR_ID5;
  }

  public void setCHR_ID5(String cHR_ID5) {
    this.CHR_ID5 = cHR_ID5;
  }

  public String getRG_CODE() {
    return RG_CODE;
  }

  public void setRG_CODE(String rG_CODE) {
    this.RG_CODE = rG_CODE;
  }

  public String getSET_YEAR() {
    return SET_YEAR;
  }

  public void setSET_YEAR(String sET_YEAR) {
    this.SET_YEAR = sET_YEAR;
  }

  public String getSTART_DATE() {
    return START_DATE;
  }

  public void setSTART_DATE(String sTART_DATE) {
    this.START_DATE = sTART_DATE;
  }

  public String getEND_DATE() {
    return END_DATE;
  }

  public void setEND_DATE(String eND_DATE) {
    this.END_DATE = eND_DATE;
  }

  public String getPUBLIC_SIGN() {
    return PUBLIC_SIGN;
  }

  public void setPUBLIC_SIGN(String pUBLIC_SIGN) {
    this.PUBLIC_SIGN = pUBLIC_SIGN;
  }

  public String getREMARK() {
    return REMARK;
  }

  public void setREMARK(String rEMARK) {
    this.REMARK = rEMARK;
  }

  public String getSYS_ID() {
    return SYS_ID;
  }

  public void setSYS_ID(String sYS_ID) {
    SYS_ID = sYS_ID;
  }

  /**
   * 将Map转换成DTO
   * 
   * @param data
   */
  public void copy(Map data) {
    if (data == null || data.isEmpty())
      return;
    if (data.get("chr_id") != null)
      this.setCHR_ID(data.get("chr_id").toString());
    if (data.get("chr_code") != null)
      this.setCHR_CODE(data.get("chr_code").toString());
    if (data.get("disp_code") != null)
      this.setDISP_CODE(data.get("disp_code").toString());
    if (data.get("chr_name") != null)
      this.setCHR_NAME(data.get("chr_name").toString());
    if (data.get("level_num") != null)
      this.setLEVEL_NUM(data.get("level_num").toString());
    if (data.get("is_leaf") != null)
      this.setIS_LEAF(data.get("is_leaf").toString());
    if (data.get("enabled") != null)
      this.setENABLED(data.get("enabled").toString());
    if (data.get("create_date") != null)
      this.setCREATE_DATE(data.get("create_date").toString());
    if (data.get("create_user") != null)
      this.setCREATE_USER(data.get("create_user").toString());
    if (data.get("latest_op_date") != null)
      this.setLATEST_OP_DATE(data.get("latest_op_date").toString());
    if (data.get("latest_op_user") != null)
      this.setLATEST_OP_USER(data.get("latest_op_user").toString());
    if (data.get("last_ver") != null)
      this.setLAST_VER(data.get("last_ver").toString());
    if (data.get("is_deleted") != null)
      this.setIS_DELETED(data.get("is_deleted").toString());
    if (data.get("chr_code1") != null)
      this.setCHR_CODE1(data.get("chr_code1").toString());
    if (data.get("chr_code2") != null)
      this.setCHR_CODE2(data.get("chr_code2").toString());
    if (data.get("chr_code3") != null)
      this.setCHR_CODE3(data.get("chr_code3").toString());
    if (data.get("chr_code4") != null)
      this.setCHR_CODE4(data.get("chr_code4").toString());
    if (data.get("chr_code5") != null)
      this.setCHR_CODE5(data.get("chr_code5").toString());
    if (data.get("parent_id") != null)
      this.setPARENT_ID(data.get("parent_id").toString());
    if (data.get("chr_id1") != null)
      this.setCHR_ID1(data.get("chr_id1").toString());
    if (data.get("chr_id2") != null)
      this.setCHR_ID2(data.get("chr_id2").toString());
    if (data.get("chr_id3") != null)
      this.setCHR_ID3(data.get("chr_id3").toString());
    if (data.get("chr_id4") != null)
      this.setCHR_ID4(data.get("chr_id4").toString());
    if (data.get("chr_id5") != null)
      this.setCHR_ID5(data.get("chr_id5").toString());
    if (data.get("rg_code") != null)
      this.setRG_CODE(data.get("rg_code").toString());
    if (data.get("set_year") != null)
      this.setSET_YEAR(data.get("set_year").toString());
    if (data.get("start_date") != null)
      this.setSTART_DATE(data.get("start_date").toString());
    if (data.get("end_date") != null)
      this.setEND_DATE(data.get("end_date").toString());
    if (data.get("public_sign") != null)
      this.setPUBLIC_SIGN(data.get("public_sign").toString());
    if (data.get("remark") != null)
      this.setREMARK(data.get("remark").toString());
    if (data.get("bis_id") != null)
      this.setBIS_ID(data.get("bis_id").toString());
    if (data.get("sys_id") != null)
      this.setSYS_ID(data.get("sys_id").toString());
    if (data.get("upper_id") != null)
      this.setUPPER_ID(data.get("upper_id").toString());
    if (data.get("belongList") != null && !((List) data.get("belongList")).isEmpty())
      this.setBELONG_LIST((List) data.get("belongList"));
    if (data.get("moneyList") != null && !((List) data.get("moneyList")).isEmpty())
      this.setMONEY_LIST((List) data.get("moneyList"));

  }

  public XMLData toXMLData() {
    XMLData data = new XMLData();
    if (this.getCHR_ID() != null)
      data.put("chr_id", this.getCHR_ID());
    if (this.getCHR_CODE() != null)
      data.put("chr_code", this.getCHR_CODE());
    if (this.getDISP_CODE() != null)
      data.put("disp_code", this.getDISP_CODE());
    if (this.getCHR_NAME() != null)
      data.put("chr_name", this.getCHR_NAME());
    if (this.getLEVEL_NUM() != null)
      data.put("level_num", this.getLEVEL_NUM());
    if (this.getIS_LEAF() != null)
      data.put("is_leaf", this.getIS_LEAF());
    if (this.getENABLED() != null)
      data.put("enabled", this.getENABLED());
    if (this.getCREATE_DATE() != null)
      data.put("create_date", this.getCREATE_DATE());
    if (this.getCREATE_USER() != null)
      data.put("create_user", this.getCREATE_USER());
    if (this.getLATEST_OP_DATE() != null)
      data.put("latest_op_date", this.getLATEST_OP_DATE());
    if (this.getLATEST_OP_USER() != null)
      data.put("latest_op_user", this.getLATEST_OP_USER());
    if (this.getLAST_VER() != null)
      data.put("last_ver", this.getLAST_VER());
    if (this.getIS_DELETED() != null)
      data.put("is_deleted", this.getIS_DELETED());
    if (this.getCHR_CODE1() != null)
      data.put("chr_code1", this.getCHR_CODE1());
    if (this.getCHR_CODE2() != null)
      data.put("chr_code2", this.getCHR_CODE2());
    if (this.getCHR_CODE3() != null)
      data.put("chr_code3", this.getCHR_CODE3());
    if (this.getCHR_CODE4() != null)
      data.put("chr_code4", this.getCHR_CODE4());
    if (this.getCHR_CODE5() != null)
      data.put("chr_code5", this.getCHR_CODE5());
    if (this.getPARENT_ID() != null)
      data.put("parent_id", this.getPARENT_ID());
    if (this.getCHR_ID1() != null)
      data.put("chr_id1", this.getCHR_ID1());
    if (this.getCHR_ID2() != null)
      data.put("chr_id2", this.getCHR_ID2());
    if (this.getCHR_ID3() != null)
      data.put("chr_id3", this.getCHR_ID3());
    if (this.getCHR_ID4() != null)
      data.put("chr_id4", this.getCHR_ID4());
    if (this.getCHR_ID5() != null)
      data.put("chr_id5", this.getCHR_ID5());
    if (this.getRG_CODE() != null)
      data.put("rg_code", this.getRG_CODE());
    if (this.getSET_YEAR() != null)
      data.put("set_year", this.getSET_YEAR());
    if (this.getSTART_DATE() != null)
      data.put("start_date", this.getSTART_DATE());
    if (this.getEND_DATE() != null)
      data.put("end_date", this.getEND_DATE());
    if (this.getPUBLIC_SIGN() != null)
      data.put("public_sign", this.getPUBLIC_SIGN());
    if (this.getREMARK() != null)
      data.put("remark", this.getREMARK());
    if (this.getBIS_ID() != null)
      data.put("bis_id", this.getBIS_ID());
    if (this.getSYS_ID() != null)
      data.put("sys_id", this.getSYS_ID());
    if (this.getBELONG_LIST() != null)
      data.put("belongList", this.getBELONG_LIST());
    if (this.getMONEY_LIST() != null)
      data.put("moneyList", this.getMONEY_LIST());

    return data;
  }

}
