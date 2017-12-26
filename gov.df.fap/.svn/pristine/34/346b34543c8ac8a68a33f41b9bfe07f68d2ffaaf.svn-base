package gov.df.fap.service.workflow;

import gov.df.fap.api.workflow.ISysRegulationConf;
import gov.df.fap.bean.rule.SysRuleFunction;
import gov.df.fap.bean.rule.SysRuleFunctionParas;
import gov.df.fap.bean.rule.SysRulePara;
import gov.df.fap.bean.workflow.SysWfCondition;
import gov.df.fap.bean.workflow.SysWfConditionLine;
import gov.df.fap.service.util.UUIDRandom;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.xml.XMLData;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Title: 规则管理持久层操作实现类
 * 
 * Description: 规则管理持久层操作接口实现 操作对象包含(SysRuleFunction 规则函数对象, SysRulePara
 * 规则参数对象,SysWfCondition 规则定义对象, SysWfConditionLine 规则明细对象)
 * 
 * @author 
 * 
 * @since：2017-04-10
 * <br>
 * Company: 
 * 
 * @version 1.0
 */
@Service
public class SysRegulationManagerBO implements ISysRegulationConf {

  // 查询当前系统的所有表名称
  private final static String SELECT4AllTABLENAME = "select table_name from user_all_tables";

  // 删除私有参数， 并且没有指定规则ID的
  private final static String DELETE4SHARED = "delete from SYS_WF_CONDITION_PARAS  where CONDITION_ID is null and IS_SHARED = 0";

  // 查询所有的公有参数集合
  private final static String SELECT4ALLSHARED = "select * from SYS_WF_CONDITION_PARAS s where s.IS_SHARED = 1";

  // 查询所有的规则定义列表
  private final static String SELECT4ALLRULE = "select * from sys_wf_conditions";

  // 查询所有规则函数
  private final static String SELECT4ALLFUNCTION = "select * from sys_wf_function ";

  // 查询所有函数类型的参数
  private final static String SELECT4FUNCTION = "select fun_paras from SYS_WF_CONDITION_PARAS where fun_paras is not null";

  //查询指定表的所有字段
  private final static String SELECT4TABLEFIELD = "select column_name as para_name,data_type as type from user_tab_columns where table_name = ? ";

  // 持久层业务实现对象
  @Autowired
  @Qualifier("generalDAO")
  GeneralDAO dao = null;

  public GeneralDAO getDao() {
    return dao;
  }

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 根据规则id得到规则明细的详细信息
   * 
   * @param conditionId
   *            规则id
   * @return 返回包含XMLData类型的List
   * @throws Exception
   */
  public List getListByConditionById(String conditionId) throws Exception {
    List list = null;
    String rg_code = (String) SessionUtil.getRgCode();// String set_year = (String) SessionUtil.getLoginYear();
    String set_year = (String) SessionUtil.getLoginYear();
    StringBuffer sql = new StringBuffer();
    sql.append("select ");
    sql
      .append("t1.left_paraname,t1.left_paravaluetype,t1.left_paraid,t1.right_paraname,t1.right_paravaluetype,t1.right_paraid,t1.para_type,");
    sql
      .append("(select t3.fun_id from sys_wf_condition_paras t3 where t3.para_id = t1.right_paraid and t3.rg_code = t1.rg_code and t3.set_year = t1.set_year) as fun_id,");
    sql
      .append("(select t3.fun_paras from sys_wf_condition_paras t3 where t3.para_id = t1.right_paraid and t3.rg_code = t1.rg_code and t3.set_year = t1.set_year) as fun_paras,");
    sql.append("t1.left_pare,t1.operator,t1.right_pare, t1.logic_operator");
    sql
      .append(" from sys_wf_condition_lines t1 where t1.rg_code=? and t1.set_year=?  and t1.CONDITION_ID = ?   order by t1.line_sort ");
    list = dao.findBySql(sql.toString(), new Object[] { rg_code, set_year, conditionId });
    return list;
  }

  /**
   * 
   * 功能：删除规则定义对象和规则明细。
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:11:23
   * @param ruleDto
   *            规则定义对象
   * @throws Exception
   */
  public void deleteRule(SysWfCondition ruleDto) throws Exception {
    /* 删除规则是同时删除私有的规则参数 */
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();
    //modify by liuzw 20120410 增加对多财政多年度的支持
    String condition_id = ruleDto.getCONDITION_ID();
    String sql = "delete from SYS_WF_CONDITION_PARAS  where condition_id = ? and rg_code=? and set_year=?";
    dao.executeBySql(sql, new Object[] { condition_id, rg_code, set_year });

    sql = "delete from sys_wf_condition_lines where condition_id = ? and rg_code=? and set_year=?";
    dao.executeBySql(sql, new Object[] { condition_id, rg_code, set_year });

    sql = "delete from sys_wf_conditions where condition_id = ? and rg_code=? and set_year=?";
    dao.executeBySql(sql, new Object[] { condition_id, rg_code, set_year });

  }

  /**
   * 
   * 功能：删除规则参数定义对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:15:18
   * @param paraId
   *            参数ID
   * @return 删除结果字符串
   * @throws Exception
   */
  public String deleteRulePara(String paraId) throws Exception {
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();
    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sql = "select * from SYS_WF_CONDITION_LINES  where LEFT_PARAID = ?  or RIGHT_PARAID =  ? and rg_code=? and set_year=?";
    if (dao.findBySql(sql, new Object[] { paraId, paraId, rg_code, set_year }).size() > 0) {// 规则参数被规则引用不能删除
      return "参数已经被使用! 删除操作失败!";
    }
    /* 查询所有函数类型的参数值 */
    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sqlStr = SELECT4FUNCTION + " and rg_code=? and set_year=?";
    List list = dao.findBySql(sqlStr, new Object[] { rg_code, set_year });
    Iterator it = list.iterator();
    while (it.hasNext()) {
      XMLData fun_paras = (XMLData) it.next();
      String para = "";
      if (null != fun_paras.get("fun_paras")) {
        para = fun_paras.get("fun_paras").toString();
        String[] paras = para.split(",");
        int i = paras.length;
        for (int j = 0; j < i; j++) {
          /* 如果函数参数ID 等于当前要删除的ID */
          if (paras[j].equals(paraId)) {
            return "参数已经被使用! 删除操作失败!";
          }
        }
      }
    }

    /* 删除参数信息 */
    //modify by liuzw 20120410 增加对多财政多年度的支持
    dao.executeBySql("delete from sys_wf_condition_paras where para_id = ? and rg_code=? and set_year=?", new Object[] {
      paraId, rg_code, set_year });
    return null;
  }

  /**
   * 
   * 功能：从配置文件中取得所有的规则函数配置列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:15:47
   * @return XMLData类型的结果集
   * @throws Exception
   */
  // public List getAllRuleFuns() throws Exception{
  // List list = null;
  // String sql = " select * from SysRuleFunction order by fun_id ";
  // list = dao.findBySql(sql);
  // return list;
  // }
  /**
   * 
   * 功能：取得所有的规则定义列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:03:47
   * @return 返回包含XMLData类型的规则信息LIST
   * @throws Exception
   */
  public List getAllRules() throws Exception {
    List list = null;
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();
    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sqlStr = SELECT4ALLRULE + " where rg_code=? and set_year=?";
    list = dao.findBySql(sqlStr, new Object[] { rg_code, set_year });
    return list;
  }

  /**
   * 
   * 功能：取得指定类型的规则定义列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:04:49
   * @param ruleType
   *            规则类型
   * @return 返回包含XMLData类型的规则信息LIST
   * @throws Exception
   */
  public List getAllRulesByType(String ruleType) throws Exception {
    List list = null;
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();
    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sql = "select * from sys_wf_conditions where condition_type = ? and rg_code=? and set_year=? ";
    list = dao.findBySql(sql, new Object[] { ruleType, rg_code, set_year });
    return list;
  }

  /**
   * 
   * 功能：根据规则编号取得规则定义对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:09:43
   * @param ruleId
   *            规则ID
   * @return 规则对象
   * @throws Exception
   */
  public SysWfCondition getRuleById(String ruleId) throws Exception {

    SysWfCondition sysWfCondition = new SysWfCondition();
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();
    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sql = "select * from sys_wf_conditions  where condition_id = ? and rg_code=? and set_year=?";
    List list = dao.findBySql(sql, new Object[] { ruleId, rg_code, set_year });

    /* 对查询结果进行组合, 转换成规则对象 */
    if (list.size() > 0) {
      XMLData xmldata = (XMLData) list.get(0);
      sysWfCondition.setBSH_EXPRESSION(getParaNotNull(xmldata, "bsh_expression"));
      sysWfCondition.setCONDITION_CODE(getParaNotNull(xmldata, "condition_code"));
      sysWfCondition.setCONDITION_ID(getParaNotNull(xmldata, "condition_id"));
      sysWfCondition.setCONDITION_NAME(getParaNotNull(xmldata, "condition_name"));
      sysWfCondition.setCONDITION_TYPE(getParaNotNull(xmldata, "condition_type"));
      sysWfCondition.setEXPRESSION(getParaNotNull(xmldata, "expression"));
      sysWfCondition.setCREATE_DATE(getParaNotNull(xmldata, "create_date"));
      sysWfCondition.setCREATE_USER(getParaNotNull(xmldata, "create_user"));
      sysWfCondition.setLAST_VER(getParaNotNull(xmldata, "last_ver"));
      sysWfCondition.setLATEST_OP_DATE(getParaNotNull(xmldata, "latest_op_date"));
      sysWfCondition.setLATEST_OP_USER(getParaNotNull(xmldata, "latest_op_user"));
      sysWfCondition.setREMARK(getParaNotNull(xmldata, "remark"));
      sysWfCondition.setRG_CODE(rg_code);
      sysWfCondition.setSET_YEAR(Integer.parseInt(set_year));
      return sysWfCondition;
    } else {
      return null;
    }

  }

  /**
   * 
   * 功能：根据规则参数编号取得规则参数定义对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:12:54
   * @param paraId
   *            规则ID
   * @return 规则参数对象
   * @throws Exception
   */
  public SysRulePara getRuleParaById(String paraId) throws Exception {
    List list = null;
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();
    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sql = " select * from SYS_WF_CONDITION_PARAS  where para_id = ? and rg_code=? and set_year=?";
    list = dao.findBySql(sql, new Object[] { paraId, rg_code, set_year });
    SysRulePara sysRuleParaDto = new SysRulePara();
    if (list.size() < 1) {
      return null;
    } else {
      XMLData rulePara = (XMLData) list.get(0);
      sysRuleParaDto.setFUN_ID(getParaNotNull(rulePara, "fun_id"));
      sysRuleParaDto.setFUN_PARAS(getParaNotNull(rulePara, "fun_id"));
      sysRuleParaDto.setRG_CODE(rg_code);
      sysRuleParaDto.setSET_YEAR(Integer.parseInt(set_year));
      Object tempPara = getParaNotNull(rulePara, "is_shared");
      if (null != tempPara)
        sysRuleParaDto.setIS_SHARED(new Long(tempPara.toString()));
      sysRuleParaDto.setPARA_DESC(getParaNotNull(rulePara, "para_desc"));
      sysRuleParaDto.setPARA_ID(paraId);
      sysRuleParaDto.setPARA_NAME(getParaNotNull(rulePara, "para_name"));
      sysRuleParaDto.setPARA_REMARK(getParaNotNull(rulePara, "para_remark"));
      Object tempParaType = getParaNotNull(rulePara, "para_type");
      if (null != tempParaType)
        sysRuleParaDto.setPARA_TYPE(new Long(tempParaType.toString()));
      sysRuleParaDto.setPARA_VALUETYPE(getParaNotNull(rulePara, "para_valuetype"));
      sysRuleParaDto.setCONDITION_ID(getParaNotNull(rulePara, "condition_id"));
    }
    return sysRuleParaDto;
  }

  /**
   * 
   * 功能：取得所给出的规则对应的参数定义列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:14:45
   * @param ruleId
   *            规则ID
   * @return 包含XMLData类型规则的私有参数列表
   * @throws Exception
   */
  public List getRuleParasByRule(String ruleId) throws Exception {
    List list = null;
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();
    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sql = "select * from SYS_WF_CONDITION_LINES s where s.CONDITION_ID = ? and s.RG_CODE=? and s.SET_YEAR=? order by line_sort";
    list = dao.findBySql(sql, new Object[] { ruleId, rg_code, set_year });
    return list;
  }

  /**
   * 
   * 功能：取得所有的公用的规则参数定义列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:14:16
   * @return 返回XMLData类型的公有参数列表
   * @throws Exception
   */
  public List getSharedRuleParas() throws Exception {
    List list = null;
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();
    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sqlStr = SELECT4ALLSHARED + " and s.rg_code=? and s.set_year=?";
    list = dao.findBySql(sqlStr, new Object[] { rg_code, set_year });
    return list;
  }

  /**
   * 
   * 功能：保存规则定义对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:13:27
   * @param ruleDto
   *            规则对象
   * @return 返回当前对象的ID
   * @throws Exception
   */
  public String saveRule(SysWfCondition ruleDto) throws Exception {
    /* 删除所有的规则明细 */
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    //modify by liuzw 20120410 增加对多财政多年度的支持
    if (ruleDto.getCONDITION_ID() != null) {
      dao.executeBySql(
        "delete from SYS_WF_CONDITION_LINES s where s.CONDITION_ID = ? and s.rg_code=? and s.set_year=?", new Object[] {
          ruleDto.getCONDITION_ID(), rg_code, set_year });
    }
    SysWfCondition sysWfCondition = new SysWfCondition();
    BeanUtils.copyProperties(sysWfCondition, ruleDto);

    //		dao.saveOrUpdate(sysWfCondition);
    dao.deleteDataBySql("sys_wf_conditions", sysWfCondition, new String[] { "condition_id" });
    sysWfCondition.setCONDITION_ID(UUIDRandom.generate());
    dao.saveDataBySql("sys_wf_conditions", sysWfCondition);
    Set lineSet = sysWfCondition.getSysWfConditionLineses();
    Iterator it = lineSet.iterator();
    while (it.hasNext()) {
      SysWfConditionLine tempLine = (SysWfConditionLine) it.next();
      tempLine.setCONDITION_ID(sysWfCondition.getCONDITION_ID());
      //modify by liuzw 20120515 修正流转线条件BUG
      //tempLine.setLINE_ID(Long.valueOf(UUIDRandom.generateNumberBySeq("SEQ_SYS_FRAME_ID")));
      tempLine.setLINE_ID(UUIDRandom.generate());
      dao.saveDataBySql("SYS_WF_CONDITION_LINES", tempLine);
      //			dao.save(tempLine);
    }
    return sysWfCondition.getCONDITION_ID();
  }

  /**
   * 
   * 功能：保存规则参数定义对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-11-30 上午10:13:27
   * @param paraDto
   *            规则参数对象
   * @return 返回当前对象的ID
   * @throws Exception
   */
  public String saveRulePara(SysRulePara paraDto) throws Exception {
    String str = null;
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    try {
      SysRulePara sysRulePara = new SysRulePara();
      BeanUtils.copyProperties(sysRulePara, paraDto);
      //		dao.saveOrUpdate(sysRulePara);
      sysRulePara.setPARA_ID(UUIDRandom.generate());
      sysRulePara.setRG_CODE(rg_code);
      sysRulePara.setSET_YEAR(Integer.parseInt(set_year));
      dao.deleteDataBySql("sys_wf_condition_paras", sysRulePara, new String[] { "para_id" });
      dao.saveDataBySql("sys_wf_condition_paras", sysRulePara);
      str = sysRulePara.getPARA_ID();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return str;
  }

  /**
   * 
   * 功能：从函数表中取得所有的函数列表.
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-6 上午09:15:24
   * @return XMLData类型的结果集
   * @throws Exception
   */
  public List getAllFunction() throws Exception {
    List list = null;
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sqlStr = SELECT4ALLFUNCTION + " where rg_code=? and set_year=?";
    list = dao.findBySql(sqlStr, new Object[] { rg_code, set_year });
    return list;
  }

  /**
   * 
   * 功能：通过参数类型查询匹配的相应的参数集合
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-4 下午04:54:07
   * @param paraType
   * @param ruleId
   *            公有时为null
   * @return XMLData类型的结果集
   * @throws Exception
   */
  public List getRuleParasByType(String paraType, String ruleId) throws Exception {

    List list = null;
    String sql = "";
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    //modify by liuzw 20120410 增加对多财政多年度的支持
    /* 如果规则ID 存在直接查询得规则对应的参数和工友参数 */
    if (ruleId != null) {
      sql = " select distinct * from SYS_WF_CONDITION_PARAS  where "
        + " (CONDITION_ID = ? or IS_SHARED = 1 or (CONDITION_ID is null and IS_SHARED = 0)) AND PARA_TYPE = ? and rg_code=? and set_year=?";
      list = dao.findBySql(sql, new Object[] { ruleId, paraType, rg_code, set_year });

      /* 直接查询公有的规则参数 */
    } else {
      sql = "select distinct * from SYS_WF_CONDITION_PARAS  where IS_SHARED = 1 AND PARA_TYPE = ? and rg_code=? and set_year=?";
      list = dao.findBySql(sql, new Object[] { paraType, rg_code, set_year });
    }

    return list;
  }

  /**
   * 
   * 功能：获得数据库所有表信息
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-4 下午04:55:29
   * @return XMLData类型的结果集
   * @throws Exception
   */
  public List getAllTables() throws Exception {
    List list = null;

    list = dao.findBySql(SELECT4AllTABLENAME);
    return list;
  }

  /**
   * 
   * 功能：通过函数ID 取得函数参数列表
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-6 下午12:57:14
   * @param fun_id
   *            函数ID
   * @return XMLData类型的结果集
   * @throws Exception
   */
  public List getFunctionParasByFunId(String fun_id) throws Exception {
    List list = null;
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sql = "select * from sys_wf_function_paras where fun_id= ? and rg_code=? and set_year=? order by fun_parasort ";
    list = dao.findBySql(sql, new Object[] { fun_id, rg_code, set_year });
    return list;
  }

  /**
   * 
   * 功能：获得指定表的所有字段信息
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-4 下午04:56:48
   * @param tableName
   *            表名称
   * @return XMLData类型的结果集
   * @throws Exception
   */
  public List getTablesField(String tableName) throws Exception {
    List list = null;
    list = dao.findBySql(SELECT4TABLEFIELD, new Object[] { tableName });
    return list;
  }

  /**
   * 
   * 功能：保存规则函数
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-6 上午09:42:00
   * @param function
   *            规则函数对象
   * @return 返回当前对象的ID
   * @throws Exception
   */
  public String saveFuntion(SysRuleFunction function) throws Exception {

    /* 保存规则对象时删除规则明细 重新级联添加 */
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    //modify by liuzw 20120410 增加对多财政多年度的支持
    if (function.getFunId() != null) {
      dao.executeBySql("delete from sys_wf_function_paras  where  FUN_ID = ? and rg_code=? and set_year=?",
        new Object[] { function.getFunId(), rg_code, set_year });
    }
    SysRuleFunction sysRuleFunction = new SysRuleFunction();
    BeanUtils.copyProperties(sysRuleFunction, function);
    //		dao.saveOrUpdate(sysRuleFunction);
    dao.deleteDataBySql("sys_wf_function", sysRuleFunction, new String[] { "fun_id" });
    dao.saveDataBySql("sys_wf_function", sysRuleFunction);

    Set functionParas = sysRuleFunction.getSysWfFunctionParases();
    Iterator itParas = functionParas.iterator();
    while (itParas.hasNext()) {
      SysRuleFunctionParas sysRuleFunctionParas = (SysRuleFunctionParas) itParas.next();
      sysRuleFunctionParas.setFun_id(sysRuleFunction.getFunId());
      dao.saveDataBySql("sys_wf_function_paras", sysRuleFunctionParas);
    }
    return sysRuleFunction.getFunId();
  }

  /**
   * 
   * 功能：删除函数
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-6 下午02:30:20
   * @param srf
   *            函数对象
   * @return 返回删除结果信息 成功为空
   * @throws Exception
   */
  public String deleteFunction(SysRuleFunction srf) throws Exception {
    //add by liuzw 20120410
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    //modify by liuzw 20120410 增加对多财政多年度的支持
    String sql = "select 1 from SYS_WF_CONDITION_PARAS  where  FUN_ID =  ? and rg_code=? and set_year=?";
    if (dao.findBySql(sql, new Object[] { srf.getFunId(), rg_code, set_year }).size() > 0) {// 函数被参数引用不能删除
      return "函数已经被使用! 删除操作失败!";
    }

    sql = "delete from  SYS_WF_FUNCTION where fun_id = ?  and rg_code=? and set_year=?";
    dao.executeBySql(sql, new Object[] { srf.getFunId(), rg_code, set_year });
    sql = "delete from  SYS_WF_FUNCTION_PARAS where fun_id = ?  and rg_code=? and set_year=?";
    dao.executeBySql(sql, new Object[] { srf.getFunId(), rg_code, set_year });
    return null;
  }

  /**
   * 
   * 功能：通过规则ID 查询对应的组成信息.
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-16 下午10:39:41
   * @param rule_id
   *            规则ID
   * @return 返回XMLData类型的结果集
   * @throws Exception
   */
  public List getRule4RuleFactory(String rule_id) throws Exception {
    List list = null;
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    //增加对多财政多年度的支持
    StringBuffer sql = new StringBuffer();
    sql.append(" select t2.para_desc as left_paraname,t1.left_paraid,t2.fun_id,t1.right_paraid,");
    sql
      .append(" (select t3.fun_id from sys_wf_condition_paras t3  where t3.para_id = t1.right_paraid and t3.rg_code=? and t3.set_year=?) as right_parafun,");
    sql
      .append(" (select t3.para_type from sys_wf_condition_paras t3  where t3.para_id = t1.right_paraid and t3.rg_code=? and t3.set_year=?) as right_paratype,");
    sql
      .append(" (select t3.para_desc from sys_wf_condition_paras t3 where t3.para_id = t1.right_paraid and t3.rg_code=? and t3.set_year=?) as right_paraname,");
    sql
      .append(" t2.para_type,t2.fun_paras ,t1.left_pare,t1.operator,t1.right_pare, t1.logic_operator from sys_wf_condition_paras t2, ");
    sql
      .append(" sys_wf_condition_lines t1 where t2.rg_code=? and t2.set_year=? and t2.rg_code=t1.rg_code and t2.set_year=t1.set_year and t2.para_id = t1.left_paraid and t1.CONDITION_ID = ? order by t1.line_sort ");
    list = dao.findBySql(sql.toString(), new Object[] { rg_code, set_year, rg_code, set_year, rg_code, set_year,
      rg_code, set_year, rule_id });
    return list;
  }

  /**
   * 
   * 功能：加载规则参数对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-17 上午12:10:36
   * @param para_id
   *            参数ＩＤ
   * @return 规则参数对象
   * @throws Exception
   */
  public SysRulePara getSysRulePara(String para_id) throws Exception {
    //modify by liuzw 20120518 修改规则参数BUG
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();
    SysRulePara sysRulePara = new SysRulePara();
    SysRulePara sysRuleParaTemp = new SysRulePara();

    String sql = "select * from sys_wf_condition_paras  where para_id = ? and rg_code=? and set_year=?";
    List list = dao.findBySql(sql, new Object[] { para_id, rg_code, set_year });

    if (list.size() > 0) {
      XMLData xmldata = (XMLData) list.get(0);
      sysRuleParaTemp.setPARA_ID(getParaNotNull(xmldata, "para_id"));
      sysRuleParaTemp.setCONDITION_ID(getParaNotNull(xmldata, "condition_id"));
      sysRuleParaTemp.setFUN_ID(getParaNotNull(xmldata, "fun_id"));
      sysRuleParaTemp.setPARA_DESC(getParaNotNull(xmldata, "para_desc"));
      sysRuleParaTemp.setPARA_TYPE(Long.getLong(getParaNotNull(xmldata, "para_type")));
      sysRuleParaTemp.setPARA_NAME(getParaNotNull(xmldata, "para_name"));
      sysRuleParaTemp.setPARA_REMARK(getParaNotNull(xmldata, "para_remark"));
      sysRuleParaTemp.setPARA_VALUETYPE(getParaNotNull(xmldata, "para_valuetype"));
      sysRuleParaTemp.setFUN_PARAS(getParaNotNull(xmldata, "fun_paras"));
      sysRuleParaTemp.setIS_SHARED(Long.getLong(getParaNotNull(xmldata, "is_shared")));
      sysRuleParaTemp.setRG_CODE(rg_code);
      sysRuleParaTemp.setSET_YEAR(Integer.parseInt(set_year));
    }

    BeanUtils.copyProperties(sysRulePara, sysRuleParaTemp);
    return sysRulePara;
  }

  /**
   * 
   * 功能：加载规则函数对象
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-17 上午12:10:36
   * @param fun_id
   *            函数ID
   * @return 规则函数对象
   */
  public SysRuleFunction getSysRuleFunction(String fun_id) throws Exception {
    SysRuleFunction sysRuleFunction = new SysRuleFunction();
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    //增加对多财政多年度的支持
    String sql = "select * from sys_wf_function  where fun_id = ? and rg_code=? and set_year=?";
    List list = dao.findBySql(sql, new Object[] { fun_id, rg_code, set_year });

    /* 循环结果集 组合成函数对象 */
    if (list.size() > 0) {
      XMLData xmldata = (XMLData) list.get(0);
      sysRuleFunction.setFunClassname(getParaNotNull(xmldata, "fun_classname"));
      sysRuleFunction.setFunMethod(getParaNotNull(xmldata, "fun_method"));
      sysRuleFunction.setFunId(getParaNotNull(xmldata, "fun_id"));
      sysRuleFunction.setFunName(getParaNotNull(xmldata, "fun_name"));
      sysRuleFunction.setFunRemark(getParaNotNull(xmldata, "fun_remark"));
      sysRuleFunction.setFunValuetype(getParaNotNull(xmldata, "fun_valuetype"));
      sysRuleFunction.setSysId(getParaNotNull(xmldata, "sys_id"));
      sysRuleFunction.setRG_CODE(rg_code);
      sysRuleFunction.setSET_YEAR(Integer.parseInt(set_year));
      return sysRuleFunction;
    } else {
      return null;
    }

  }

  /**
   * 
   * 功能：从XMLData中取得para的属性值转换成字符串
   * 
   * @author bing-zeng <br>
   *         Date 创建时间：2007-12-17 下午07:23:15
   * @param xmlData
   *            包含需要转换的数据MAP
   * @param para
   *            需要转换的属性
   * @return 属性值
   * 
   */
  private String getParaNotNull(XMLData xmlData, String para) {

    if (null != xmlData.get(para)) {
      return xmlData.get(para).toString();
    }
    return null;
  }

  /**
   * 
   * 功能： 删除没有规则ＩＤ的私有参数
   * 
   * @author bing-zeng <br>
   *         Date ：2008-1-17 下午01:52:37
   * @throws Exception
   */
  public void delConditionPara4Private() throws Exception {
    /* 保存参数前删除所有私有参数没有规则ＩＤ的记录 */
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    //增加对多财政多年度的支持
    String sqlStr = DELETE4SHARED + " and rg_code=? and set_year=?";
    dao.executeBySql(sqlStr, new Object[] { rg_code, set_year });
  }

  /**
   *  
   * 
   * @param chr_code
   * @return
   * @see gov.gfmis.fap.workflow.sysregulation.ibs.ISysRegulationConf#getEleByCode(java.lang.String)
   * @author Songsong at 2009-4-18日11:03:55
   */
  public List getEleByCode(String chr_code) {
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    String sql = "select * from sys_element  where ele_code = ? and rg_code=? and set_year=?";
    chr_code = chr_code.toUpperCase();
    List list = dao.findBySql(sql, new Object[] { chr_code, rg_code, set_year });
    return list;
  }

  /**
   *  
   * 
   * @param tableName
   * @param field
   * @return
   * @see gov.gfmis.fap.workflow.sysregulation.ibs.ISysRegulationConf#findComments(java.lang.String, java.lang.String)
   * @author Songsong at 2009-4-18日11:03:49
   */
  public List findComments(String tableName, String field) {
    List list = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT * "
      + (TypeOfDB.isOracle() ? " FROM USER_COL_COMMENTS"
        : ", COLUMN_COMMENT as COMMENTS from information_schema.`COLUMNS`") + " WHERE TABLE_NAME=? and column_name=?");
    list = dao.findBySql(sql.toString(), new Object[] { tableName, field });
    return list;
  }

  /**
   *  
   * 
   * @return
   * @see gov.gfmis.fap.workflow.sysregulation.ibs.ISysRegulationConf#getEles()
   * @author Songsong at 2009-4-18日11:03:44
   */
  public List getEles() {
    String rg_code = (String) SessionUtil.getRgCode();
    String set_year = (String) SessionUtil.getLoginYear();

    String sql = "select * from sys_element where rg_code=? and set_year=?";
    List list = dao.findBySql(sql, new Object[] { rg_code, set_year });
    return list;
  }

  public List getUserTree() throws Exception {
    String rg_code = (String) SessionUtil.getRgCode();
    String sql = "select * from sys_usertree sut left join sys_user su on sut.chr_id=su.user_id where sut.rg_code=?";
    List list = null;
    try {
      List userList = dao.findBySql(sql, new Object[] { rg_code });
      list = userList;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 得到所有角色数据
   */
  public List getAllSysRolesAsTree() throws Exception {
    String hql = "select a.role_id as chr_id,a.role_name as chr_name,a.role_code as chr_code,substr(a.role_id,0,length(a.role_id)-3) as parent_id,a.* from Sys_Role a order by ROLE_CODE";
    List list = null;
    try {
      list = dao.findBySql(hql);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 得到所有功能数据
   */
  public List getAllSysModulesAsTree() throws Exception {
    String hql = "select a.module_id as chr_id,a.module_code as chr_code,a.module_name as chr_name,substr(a.module_id,0,length(a.module_id)-3) as parent_id,a.* from Sys_Module a order by MODULE_CODE";
    List list = null;
    try {
      list = dao.findBySql(hql);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return list;
  }

  /**
   * 得到行政区划代码
   * 
   * @return
   * 
   * 
   */
  private String getRgCode() {
    return SessionUtil.getRgCode();
  }
}
