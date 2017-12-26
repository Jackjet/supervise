package gov.df.fap.bean.rule;

import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.util.xml.XMLData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 *交易令数据传输DTO对象
 * @version 1.0
 * @author justin
 * @since java 1.6
 */
public class FVoucherDTO extends FBaseDTO implements Serializable {
  private static final long serialVersionUID = 6709236504559914566L;

  /**
   * 监控信息
   */
  private int inspectFlag = 0;

  private List inspectRuleId = new ArrayList();

  /**
   * 违反规则列表
   */
  private List inspectRlues = new ArrayList();

  /**
   * 监控联系人
   */
  private List inspectUsers = new ArrayList();

  //交易令ID
  private String vou_id = "";

  //业务单ID
  private String vou_bill_id = "";

  //交易凭证类型ID
  private String billtype_id = "";

  //交易凭证类型编码
  private String billtype_code = "";

  //交易凭证名称
  private String billtype_name = "";

  //交易凭证单号
  private String vou_no = "";

  //交易令类型ID
  private String vou_type_id = "";

  //交易令类型code
  private String vou_type_code = "";

  //月份
  private int set_month = -9;//默认无效

  //交易令日期
  private String bill_date = "";

  //流程结束标志
  private int is_end = -9;//默认无效

  //流程序号
  private long flow_id = 0l;

  //作废标志
  private int is_valid = 1;//默认有效

  //业务增减标识,标定本业务单按照财政业务默认规则对应的正向或者逆向金额(追加为正1,调减为负0)
  private int is_busincrease = 1;//默认有效

  //创建时间
  private String create_date = "";

  //创建用户
  private String create_user = "";

  //最后版本
  private String last_ver = "";

  //来源控制表类型
  private String fromctrltype = "";

  //目标控制表类型
  private String toctrltype = "";

  //明细对象列表,存放FVoucherDTO对象
  private List details = null;

  //借方科目
  private String ds_id = "";

  //贷方科目
  private String cs_id = "";

  //摘要
  private String summary = "";

  //用途
  private String remark = "";

  //金额
  private String vou_money = "0";

  //来源控制记录id
  private String fromctrlid = "";

  //目的控制记录id
  private String toctrlid = "";

  //警告信息
  private String warning = "";

  //控制级别
  private String ctrllevel = "";

  //业务单据类别(目前设计为0年初控制数,1正式指标,其他业务如计划、支付统一为1)
  private int bal_version = 1;

  //是否进行无指标额度查询
  private int is_needchecknobudget = 0;

  //国债字段添加 
  private String tbv_id = "";

  //添加 优化工作流性能
  private String module_id = "";

  //	添加 优化工作流性能
  private String role_id = "";

  //增加结算号(支票号字段)
  private String check_no = "";

  public int getBal_version() {
    return bal_version;
  }

  public void setBal_version(int bal_version) {
    this.bal_version = bal_version;
  }

  public String getFromctrlid() {
    return fromctrlid;
  }

  public void setFromctrlid(String fromctrlid) {
    this.fromctrlid = fromctrlid;
  }

  public String getToctrlid() {
    return toctrlid;
  }

  public void setToctrlid(String toctrlid) {
    this.toctrlid = toctrlid;
  }

  public String getBill_date() {
    return bill_date;
  }

  public void setBill_date(String bill_date) {
    this.bill_date = bill_date;
  }

  public String getCreate_date() {
    return create_date;
  }

  public void setCreate_date(String create_date) {
    this.create_date = create_date;
  }

  public String getCreate_user() {
    return create_user;
  }

  public void setCreate_user(String create_user) {
    this.create_user = create_user;
  }

  public List getDetails() {
    return details;
  }

  public void setDetails(List details) {
    this.details = details;
  }

  public long getFlow_id() {
    return flow_id;
  }

  public void setFlow_id(long flow_id) {
    this.flow_id = flow_id;
  }

  public String getFromctrltype() {
    return fromctrltype;
  }

  public void setFromctrltype(String fromctrltype) {
    this.fromctrltype = fromctrltype;
  }

  public int getIs_busincrease() {
    return is_busincrease;
  }

  public void setIs_busincrease(int is_busincrease) {
    this.is_busincrease = is_busincrease;
  }

  public int getIs_end() {
    return is_end;
  }

  public void setIs_end(int is_end) {
    this.is_end = is_end;
  }

  public int getIs_valid() {
    return is_valid;
  }

  public void setIs_valid(int is_valid) {
    this.is_valid = is_valid;
  }

  public String getLast_ver() {
    return last_ver;
  }

  public void setLast_ver(String last_ver) {
    this.last_ver = last_ver;
  }

  public int getSet_month() {
    return set_month;
  }

  public void setSet_month(int set_month) {
    this.set_month = set_month;
  }

  public String getToctrltype() {
    return toctrltype;
  }

  public void setToctrltype(String toctrltype) {
    this.toctrltype = toctrltype;
  }

  public String getVou_id() {
    return vou_id;
  }

  public void setVou_id(String vou_id) {
    this.vou_id = vou_id;
  }

  public String getVou_type_code() {
    return vou_type_code;
  }

  public void setVou_type_code(String vou_type_code) {
    this.vou_type_code = vou_type_code;
  }

  public String getVou_type_id() {
    return vou_type_id;
  }

  public void setVou_type_id(String vou_type_id) {
    this.vou_type_id = vou_type_id;
  }

  public String getCs_id() {
    return cs_id;
  }

  public void setCs_id(String cs_id) {
    this.cs_id = cs_id;
  }

  public String getDs_id() {
    return ds_id;
  }

  public void setDs_id(String ds_id) {
    this.ds_id = ds_id;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getVou_money() {
    return vou_money;
  }

  public void setVou_money(String vou_money) {
    this.vou_money = vou_money;
  }

  /**
   * 添加子项
   * @param dto 子项dto
   */
  public void addDetail(FVoucherDTO dto) {
    if (dto == null)
      return;
    List detail = this.getDetails();
    if (detail == null) {
      detail = new ArrayList();
      detail.add(dto);
      this.setDetails(detail);
    } else {
      detail.add(dto);
    }
  }

  /**
   * 转换Iterator方法
   * @return Iterator对象
   */
  public Iterator iterator() {
    List detail = this.getDetails();
    if (detail != null) {
      return detail.iterator();
    } else {
      return null;
    }
  }

  /**
   * 从DTO映射成Map
   * @return 映射值对
   */
  public Map mapping() {
    Map map = null;
    try {
      map = BeanUtils.describe(this);
      map.remove("details");
      map.remove("inspectRlues");
      map.remove("inspectRuleId");
      map.remove("inspectUsers");

      if (this.getDetails() != null) {
        map.put("row", this.getDetails());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }

  /**
  * 从DTO映射成XMLData
  * @return XMLData 映射对象
  */
  public XMLData toXMLData() {
    XMLData xmlData = null;
    try {
      xmlData = new XMLData();
      xmlData.putAll(mapping());
      xmlData.remove("row");
      if (this.getDetails() != null) {
        xmlData.put("row", convertXMLData(this.getDetails()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return xmlData;
  }

  /**
   * 循环构造XMLData
   * @param details 子对象dto列表
   * @return 子对象XMLData列表
   */
  private List convertXMLData(List details) {
    List list = new ArrayList();
    for (int i = 0; i < details.size(); i++) {
      XMLData data = new XMLData();
      FVoucherDTO dto = (FVoucherDTO) details.get(i);
      if (dto.getDetails() != null) {
        data.put("row", convertXMLData(dto.getDetails()));
      } else {
        Map map = dto.mapping();
        map.remove("row");
        data.putAll(map);
      }
      list.add(data);
    }
    return list;
  }

  /**
  * 从Map拷贝到DTO对象
  * @param map 对象 
  */
  public void copy(Map map) {
    if (map == null)
      return;
    else {
      try {
        BeanUtils.copyProperties(this, map);
        this.setDetails(null);
        if (map.get("row") != null && (map.get("row") instanceof List || map.get("row") instanceof Map)) {
          List rowList = null;
          if (map.get("row") instanceof Map) {
            rowList = new ArrayList();
            rowList.add(map.get("row"));
          } else {
            rowList = (List) map.get("row");
          }
          this.setDetails(convertDTO(rowList));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 循环构造DTO
   * @param row 子对象XMLData列表
   * @return 子对象dto列表
   */
  private List convertDTO(List row) {
    List list = new ArrayList();
    for (int i = 0; i < row.size(); i++) {
      FVoucherDTO dto = new FVoucherDTO();
      Map data = (Map) row.get(i);
      if (data.get("row") != null && (data.get("row") instanceof List || data.get("row") instanceof Map)) {
        List rowList = null;
        if (data.get("row") instanceof Map) {
          rowList = new ArrayList();
          rowList.add(data.get("row"));
        } else {
          rowList = (List) data.get("row");
        }
        dto.setDetails(convertDTO(rowList));
      } else {
        try {
          BeanUtils.copyProperties(dto, data);
          dto.setDetails(null);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      list.add(dto);
    }
    return list;
  }

  public String getWarning() {
    return warning;
  }

  public void setWarning(String warning) {
    this.warning = warning;
  }

  public String getBilltype_id() {
    return billtype_id;
  }

  public void setBilltype_id(String billtype_id) {
    this.billtype_id = billtype_id;
  }

  public String getBilltype_code() {
    return billtype_code;
  }

  public void setBilltype_code(String billtype_code) {
    this.billtype_code = billtype_code;
  }

  public String getVou_no() {
    return vou_no;
  }

  public void setVou_no(String vou_no) {
    this.vou_no = vou_no;
  }

  public String getCtrllevel() {
    return ctrllevel;
  }

  public void setCtrllevel(String ctrllevel) {
    this.ctrllevel = ctrllevel;
  }

  public int getIs_needchecknobudget() {
    return is_needchecknobudget;
  }

  public void setIs_needchecknobudget(int is_needchecknobudget) {
    this.is_needchecknobudget = is_needchecknobudget;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getVou_bill_id() {
    return vou_bill_id;
  }

  public void setVou_bill_id(String vou_bill_id) {
    this.vou_bill_id = vou_bill_id;
  }

  public String getBilltype_name() {
    return billtype_name;
  }

  public void setBilltype_name(String billtype_name) {
    this.billtype_name = billtype_name;
  }

  public String getTbv_id() {
    return tbv_id;
  }

  public void setTbv_id(String tbv_id) {
    this.tbv_id = tbv_id;
  }

  public String getModule_id() {
    return module_id;
  }

  public void setModule_id(String module_id) {
    this.module_id = module_id;
  }

  public String getRole_id() {
    return role_id;
  }

  public void setRole_id(String role_id) {
    this.role_id = role_id;
  }

  public int getInspectFlag() {
    return inspectFlag;
  }

  public void setInspectFlag(int inspectFlag) {
    this.inspectFlag = inspectFlag;
  }

  public List getInspectRlues() {
    return inspectRlues;
  }

  public void setInspectRlues(List inspectRlues) {
    this.inspectRlues = inspectRlues;
  }

  public List getInspectUsers() {
    return inspectUsers;
  }

  public void setInspectUsers(List inspectUsers) {
    this.inspectUsers = inspectUsers;
  }

  public String getCheck_no() {
    return check_no;
  }

  public void setCheck_no(String check_no) {
    this.check_no = check_no;
  }

  public List getInspectRuleId() {
    return inspectRuleId;
  }

  public void setInspectRuleId(List inspectRuleId) {
    this.inspectRuleId = inspectRuleId;
  }

}
