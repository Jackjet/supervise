package gov.df.fap.service.workflow.billtype;

import gov.df.fap.api.rule.IRule;
import gov.df.fap.api.workflow.IBillTypeServices;
import gov.df.fap.bean.rule.FVoucherDTO;
import gov.df.fap.bean.util.FBaseDTO;
import gov.df.fap.bean.workflow.FBillTypeDTO;
import gov.df.fap.bean.workflow.FCombinationRuleDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.Tools;
import gov.df.fap.util.Properties.PropertiesUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交易凭证组件服务接口实现 对应设计：综合业务系统交易凭证详细设计 是对外接口IBillTypeServices的实现类
 * 
 * @version 1.0
 * @author victor
 * @author ydz 2011/04/14 重构整理，对外接口面向Object 
 * @since java 1.4.2
 */
@Service
public class BillTypeService implements IBillTypeServices {
  @Autowired
  protected BillTypeDao dao = null;

  /**
   * 规则服务
   */
  @Autowired
  protected IRule rule = null;

  /**
   * 根据交易凭证ID获取交易凭证详细信息
   * 
   * @param billypeID
   *            交易凭证ID
   * @return 交易凭证详细信息对象
   */
  @Override
  public FBillTypeDTO getBillTypeByID(String billTypeID) {
    FBillTypeDTO dto = new FBillTypeDTO();
    try {
      dto.copy(dao.getBillTypeByID(billTypeID));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dto;
  }

  /**
   * 根据交易凭证Code获取交易凭证详细信息
   * 
   * @param billTypeCode
   *            交易凭证编码
   * @return 交易凭证详细信息对象
   */
  @Override
  public FBillTypeDTO getBillTypeByCode(String billTypeCode) {
    FBillTypeDTO dto = new FBillTypeDTO();
    try {
      dto.copy(dao.getBillTypeByCode(billTypeCode));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dto;
  }

  /**
   * 根据附加过滤条件获取交易凭证详细信息
   * 
   * @param plusSql
   *            附加过滤条件(以 and 开始)
   * @return 交易凭证详细信息列表
   */
  @Override
  public List getBillType(String plusSql) {
    List dtoList = new ArrayList();
    List list = dao.getBillType(plusSql);
    for (int i = 0; i < list.size(); i++) {
      FBillTypeDTO dto = new FBillTypeDTO();
      try {
        dto.copy((XMLData) list.get(i));
      } catch (Exception e) {
        e.printStackTrace();
      }
      dtoList.add(dto);
    }
    return dtoList;
  }

  /**
   * 根据交易凭证类型获取交易凭证当前编号,单例模式保证编号的正确性 目前暂时未实现
   * 
   * @param billTypeCode
   *            交易凭证类型编码
   * @param billInfo
   *            凭证信息对象
   * @return 编号字符串
   * @throws 获取交易凭证编号过程异常
   */
  @Override
  public String getBillNo(String billTypeCode, Map billInfo) throws Exception {
    String billNo = "";
    if (billTypeCode != null && !billTypeCode.equals("")) {
      billNo = dao.getBillNo(billTypeCode, billInfo);
    } else {
      throw new Exception("未正确传入交易凭证类型编码!");
    }
    return billNo;
  }

  /**
   * add by 2017.8.3
   * 根据交易凭证类型获取交易凭证当前编号：先取编号前缀，根据编号前缀从断号表sys_billno_deleted表中查询
   * 如果断号存在，则取用断号并删除该断号
   * @param billTypeCode
   *            交易凭证类型编号
   * @param billInfo
   *            凭证信息对象
   * @return 编号字符串
   * @throws 获取交易凭证编号过程异常
   */
  @Override
  public String getDeletedBillNo(String billTypeCode, Map billInfo) throws Exception {
    String billNo = "";
    if (billTypeCode != null && !billTypeCode.equals("")) {
      billNo = dao.getDeletedBillNo(billTypeCode, billInfo);
    } else {
      throw new Exception("未正确传入交易凭证类型编码!");
    }
    return billNo;
  }

  @Override
  public void saveDeletedBillNo(List delPayVoucherBills, String billTypeCode) throws Exception {
    if (delPayVoucherBills != null && delPayVoucherBills.size() > 0) {
      dao.saveBillNoDeleted(delPayVoucherBills, billTypeCode);
    } else {
      throw new Exception("未正确传入需要保存的撤销凭证单号数据");
    }
  }

  /**
   * 根据具体单号规则编码获取单号规则信息,单例模式保证编号的正确性
   * 
   * @param billNoRuleCode
   *            单号规则编码
   * @param billInfo
   *            凭证信息对象
   * @return 编号字符串
   * @throws 获取交易凭证编号过程异常
   */
  @Override
  public String getBillNoByBillNoRuleCode(String billNoRuleCode, Map billInfo) throws Exception {
    String billNo = "";
    if (billNoRuleCode != null && !billNoRuleCode.equals("")) {
      billNo = dao.getBillNoByBillNoRuleCode(billNoRuleCode, billInfo);
    } else {
      throw new Exception("未正确传入单号规则编码!");
    }
    return billNo;
  }

  /**
   * 根据交易凭证ID获取交易凭证合单规则
   * 
   * @param billTypeCode
   *            交易凭证编码
   * @return 交易凭证合单规则对象
   */
  @Override
  public FCombinationRuleDTO getBillCombination(String billTypeCode) {
    FCombinationRuleDTO dto = new FCombinationRuleDTO();
    XMLData data = dao.getBillCombination(billTypeCode);
    try {
      dto.copy(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
    dto.setRuleMap(data.getRecordList());
    return dto;
  }

  /**
   * 获取定值处理后的列表
   * 
   * @param billTypeCode
   *            交易凭证类型编号
   * @param field_Code
   *            要素字段
   * @param baseDTO
   *            dto
   * @return 编号字符串
   * @throws 获取交易凭证编号过程异常
   */
  @Override
  public List findEleValues(String billTypecode, String field_Code, FBaseDTO baseDTO) throws Exception {
    List tmp_list = dao.getEleRule(billTypecode, field_Code);
    if (tmp_list != null && tmp_list.size() > 0) {
      String ele_rule_id = (String) ((Map) tmp_list.get(0)).get("ele_rule_id");
      return getRuleServer().getMatchedELECODES(ele_rule_id, baseDTO);
    }
    return null;
  }

  /**
   * 对FVoucherDTO进行定值处理
   * @param fvoucherdto
   * @param isForceWriteValue
   *            是否强制设定第一条匹配的定值
   * @return 编号字符串
   * @throws 获取交易凭证编号过程异常
   */
  @Override
  public FVoucherDTO getMatchedELECODE(FVoucherDTO fvoucherdto, boolean isForceWriteValue) throws Exception {

    String billtypeCode = fvoucherdto.getBilltype_code();
    if (billtypeCode == null || billtypeCode.toString().equals("")) {
      throw new Exception("请输入交易凭证号");
    }
    List tmp_list = dao.getEleRule(billtypeCode, null);
    for (int i = 0; tmp_list != null && i < tmp_list.size(); i++) {
      String ele_rule_id = (String) ((Map) tmp_list.get(i)).get("ele_rule_id");
      String value_set_type = (String) ((Map) tmp_list.get(i)).get("valueset_type");
      // 默认值
      if (value_set_type.equals("0")) {
        String default_value = (String) ((Map) tmp_list.get(i)).get("default_value");
        String field_code = (String) ((Map) tmp_list.get(i)).get("field_code");
        PropertiesUtil.setProperty(fvoucherdto, field_code.toLowerCase(), default_value);
      }
      // 要素规则
      if (value_set_type.equals("1")) {
        getRuleServer().updateFVoucherDto(ele_rule_id, fvoucherdto, isForceWriteValue);
      }
    }
    return fvoucherdto;

  }

  /**
   * 对一组FVoucherDTO进行定值处理,策略为：不论其中有没有出错的Dto均执行完毕,只不过出错的不赋值。
   * 
   * @param dtoList
   *            需定值对象列表
   * @param isForceWriteValue
   *            是否强制设定第一条匹配的定值
   * @return dtoList 定值后对象列表
   * @throws 获取交易凭证编号过程异常
   */
  @Override
  public List getMatchedELECODEToList(List dtoList, boolean isForceWriteValue) throws Exception {
    // modified by zhoulingling 2011-10-09 当支付明细单据361上给agent_account_id设置默认值时，界面显示单条数据报错： exception when set property:agent_account_id to Class:gov.gfmis.fap.rule.FVoucherDTO
    /*if (dtoList.size() == 1) {
    	//支持传入的list里面封装的是dto或者是xmlData
    	if(dtoList.get(0) instanceof FVoucherDTO){
    		this.getMatchedELECODE((FVoucherDTO) dtoList.get(0),
    				isForceWriteValue);
    	}
    	if(dtoList.get(0) instanceof XMLData){
    		XMLData xmlData = new XMLData();
    		xmlData = (XMLData)dtoList.get(0);
    		FVoucherDTO voucherDTO = new FVoucherDTO();
    		BeanUtils.copyProperties(voucherDTO,xmlData);
    		voucherDTO = this.getMatchedELECODE(voucherDTO,isForceWriteValue);
    		//把dto转化为xml
    		xmlData = voucherDTO.toXMLData();
    		//把新的数据替换原先list里面的数据
    		dtoList.remove(0);
    		dtoList.add(0,xmlData);
    	}
    	return dtoList;
    } else {*/
    return getRuleServer().updateFixedValueRuleInfo(dtoList);
    // }
  }

  /**
  * 对一组FVoucherDTO进行定值处理（通用）
  * @param dtoList 需定值对象列表
  * @param matchedList 需要定值的规则
  * @param isForceWriteValue 是否强制设定第一条匹配的定值
  * @return dtoList 定值后对象列表
  * @throws 异常
  */
  @Override
  public List getMatchedValues(List dtoList, String[] matchedList, boolean isForceWriteValue) throws Exception {

    List list = getRuleServer().getMatchedValues(dtoList, matchedList, isForceWriteValue);
    return list;
  }

  public BillTypeDao getDao() {
    return dao;
  }

  public void setDao(BillTypeDao dao) {
    this.dao = dao;
  }

  @Override
  public List getValueSetField(String billType_code) {
    StringBuffer strSQL = new StringBuffer();
    strSQL
      .append(
        "select distinct sbvs.field_code,"
          + (TypeOfDB.isOracle() ? "decode(valueset_type , 0,'',1,rule.ele_code)"
            : "(case valueset_type when 0 then '' when 1 then rule.ele_code end)")
          + " ele_code,sbvs.ele_rule_id,sbvs.billtype_id, sbvs.valueset_type, sbvs.default_value")
      .append(" from sys_billtype").append(Tools.addDbLink()).append(" billtype,sys_ele_rule")
      .append(Tools.addDbLink()).append(" rule,sys_billtype_valueset")
      .append(Tools.addDbLink())
      .append(" sbvs")
      .append(" where 1=1 and sbvs.billtype_id=billtype.billtype_id and billtype.billtype_code='")
      .append(billType_code)
      .append("' and (( rule.ele_rule_id = sbvs.ele_rule_id and sbvs.valueset_type=1) or ( sbvs.valueset_type=0)) ")
      .append(" and billtype.rg_code='")
      //add by kongcy at 2012-05-04 
      .append(SessionUtil.getRgCode()).append("' and billtype.set_year=").append(SessionUtil.getLoginYear())
      .append(" and sbvs.rg_code='").append(SessionUtil.getRgCode()).append("' and sbvs.set_year=")
      .append(SessionUtil.getLoginYear()).append(" and rule.rg_code='").append(SessionUtil.getRgCode())
      .append("' and rule.set_year=").append(SessionUtil.getLoginYear());
    BillTypeDao dao = this.getDao();
    List result = dao.getDao().findBySql(strSQL.toString());
    return result;
  }

  /**
   * 根据传入条件得到相应的编号规则项数据
   * 
   * @param condition
   *            默认格式为“and + 条件”
   * @return 编号规则项数据列表
   */
  @Override
  public List getBillNoRule(String condition) throws Exception {
    StringBuffer strSQL = new StringBuffer("select * from sys_billnorule").append(Tools.addDbLink())
      .append(" where 1=1 ").append(condition);
    List list = null;
    try {
      BillTypeDao dao = this.getDao();
      list = dao.getDao().findBySql(strSQL.toString());
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return list;
  }

  //lgc add 得到所有关联关系code
  @Override
  public List getAllRelationsCode(String priCode, String priEle, String secEle) {
    GeneralDAO dao = (GeneralDAO) SessionUtil.getServerBean("generalDAO");
    // String sql = "select SEC_ELE_VALUE from sys_relations where PRI_ELE_VALUE ='"+ priCode +"'" ;

    StringBuffer sql = new StringBuffer();
    sql.append(" select t2.sec_ele_value from sys_relation_manager t1, sys_relations t2 ")
      .append(" where t1.relation_id = t2.relation_id and lower(t1.pri_ele_code) = ? ")
      .append(" and lower(t1.sec_ele_code) = ? and t2.pri_ele_value = ? AND t1.rg_code = t2.rg_code ")
      .append(" AND t1.set_year = t2.set_year AND t1.rg_code = ? AND t1.set_year = ?");

    return dao.findBySql(sql.toString(), new Object[] { priEle.toLowerCase(), secEle.toLowerCase(), priCode,
      SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
  }

  /**
   * 根据单号规则编码使用单号（支持断号）
   * @param billNoRuleCode  单号规则编码
   * @param billInfo 凭证信息对象
   * @param isBreakNo  是否优先要求断号	ture:要求取得断号  false:不要求断号
   * @return  定值后对象列表
   * @throws 异常
   * @author lgc
  * @throws Exception 
  * @throws Exception 
   */
  @Override
  public String getBillNoDirect(String billTypeCode, Map billInfo, String breakFlag) throws Exception {
    String billNo = "";
    if (billTypeCode != null && !billTypeCode.equals("")) {
      billNo = dao.getBillNo1(billTypeCode, billInfo, breakFlag);
    } else {
      throw new Exception("未正确传入交易凭证类型编码!");
    }

    return billNo;
  }

  /**
   * 单号占用确认
   * @param billNo	单号
   * @return true:表示确认成功  False:表示该单号已经被使用，确认失败
   * @author lgc
  * @throws Exception 
  * @throws Exception 
   */
  @Override
  public boolean confirmBillNo(String billTypeCode, String billNo) throws Exception {
    return dao.isConfirm(billTypeCode, billNo);
  }

  /**
   * 单号回收
   * @param billNo 单号
   * @return 单号回收是否成功  true：回收成功  false:回收失败 
   * @author lgc
  * @throws Exception 
   */
  @Override
  public boolean recoveryBillNo(String billTypeCode, String billNo) throws Exception {
    return dao.recoveryBillNo(billTypeCode, billNo);
  }

  @Override
  public void clearCache(List list) throws Exception {
    String billTypeCode = "";
    for (int i = 0; list != null && i < list.size(); i++) {
      billTypeCode = ((Map) list.get(i)).get("billtype_code").toString();
      dao.clearCache(billTypeCode);
    }
  }

  /**
   * 根据单据类型编码查询打印模板信息
   * @param billtype_code
   * @return
   */
  @Override
  public List getPrintModeDataByBilltypeCode(String billtype_code) {
    // 获取所有的“打印模版”的数据
    StringBuffer strBuf = new StringBuffer();
    strBuf
      .append("select spm.*, rm.report_name ")
      .append("   from sys_print_models spm, reportcy_manager rm ")
      .append("  where rm.report_id = spm.report_id AND spm.rg_code = rm.rg_code AND spm.set_year  = ? ")
      .append("    and spm.billtype_id = ")
      .append(
        "        (select bt.billtype_id from sys_billtype bt where bt.billtype_code = ? and bt.rg_code = ? and bt.set_year =?)")
      .append("  order by spm.display_order");
    List tmp_report = dao.getDao().findBySql(strBuf.toString(),
      new Object[] { SessionUtil.getLoginYear(), billtype_code, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });

    // add by wanghongtao 打印模板中选择多个报表时，report_id以分号分隔，下面程序处理通过上面查询语句查询不到的打印模板信息
    String sql = "select * from sys_print_models where instr(report_id,';')>0 and billtype_id=(select bt.billtype_id from sys_billtype bt where bt.billtype_code = ? and bt.rg_code=? and bt.set_year=?)";
    List tmp_report_2 = dao.getDao().findBySql(sql,
      new Object[] { billtype_code, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    //如果存在配置多个报表的情况，执行下面程序，获取报表名称，名称以同样用分号分隔
    if (tmp_report_2 != null && tmp_report_2.size() > 0) {
      Iterator it = tmp_report_2.iterator();
      String[] reportIdArr = null;
      while (it.hasNext()) {
        // 拼接查询报表名称的sql
        String reportNameSql = "select report_id,report_name from reportcy_manager where report_id in(";
        Map tmpMap = (Map) it.next();
        // 分隔报表ID
        reportIdArr = tmpMap.get("report_id").toString().split(";");
        // 根据ID拼接sql
        for (int i = 0; i < reportIdArr.length; i++) {
          reportNameSql = reportNameSql + "'" + reportIdArr[i] + "',";
        }
        // 去掉字符串第一个字母“；”
        reportNameSql = reportNameSql.substring(0, reportNameSql.length() - 1) + ")";
        // 执行查询
        List reportNameList = dao.getDao().findBySql(reportNameSql);
        // 拼接report_name
        if (reportNameList != null) {
          Map reportNameMap = new HashMap();
          // 为了是报表名称与id顺序一致，先把报表名称放到map中，便于查找
          for (int i = 0; i < reportNameList.size(); i++) {
            Map map = (Map) reportNameList.get(i);
            reportNameMap.put(map.get("report_id"), map.get("report_name"));
          }
          // 拼接报表名称
          String reportName = "";
          for (int i = 0; i < reportIdArr.length; i++) {
            reportName = reportName + ";" + reportNameMap.get(reportIdArr[i]);
          }
          tmpMap.put("report_name", reportName.substring(1));
        }
        // 加到报表主列表中
        tmp_report.add(tmpMap);
      }
    }
    // wanghongtao add end

    XMLData tmp;
    // 将数据库中的enabled和is_default转换成Boolean
    Boolean enabled;
    Boolean is_default;
    int tmp_int;
    Iterator report = tmp_report.iterator();
    List report_id_List = new ArrayList();
    while (report.hasNext()) {
      tmp = (XMLData) report.next();
      // 整理字段“enabled”
      tmp_int = Integer.parseInt((String) tmp.get("enabled"));
      if (tmp_int == 1) {
        enabled = Boolean.TRUE;
        tmp.remove("enabled");
        tmp.put("enabled", enabled);
      } else {
        // enabled = Boolean.FALSE;
        tmp.remove("enabled");
      }
      // 整理字段“is_default”
      tmp_int = Integer.parseInt((String) tmp.get("is_default"));
      if (tmp_int == 1) {
        is_default = Boolean.TRUE;
        tmp.remove("is_default");
        tmp.put("is_default", is_default);
      } else {
        is_default = Boolean.FALSE;
        tmp.remove("is_default");
      }
      report_id_List.add(tmp);
    }
    return report_id_List;
  }

  private IRule getRuleServer() {
    if (rule == null) {
      rule = (IRule) SessionUtil.getServerBean("sys.ruleService");
    }
    return rule;
  }

  @Override
  public String getCoaIdByByBilltypeCode(String billtype_code) {
    String strSQL = "select coa_id from sys_billtype billtype where  billtype.billtype_code=? and billtype.rg_code=? and billtype.set_year=?";
    BillTypeDao dao = this.getDao();
    List result = dao.getDao().findBySql(strSQL.toString(),
      new Object[] { billtype_code, SessionUtil.getRgCode(), SessionUtil.getLoginYear() });
    if (result != null && result.size() > 0) {
      return (String) ((Map) result.get(0)).get("coa_id");
    }
    return null;
  }

}
