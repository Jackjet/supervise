package gov.df.fap.service.dictionary.element;

import gov.df.fap.api.dictionary.ElementOperation;
import gov.df.fap.api.dictionary.interfaces.IDDSet;
import gov.df.fap.api.gl.coa.ibs.ICoa;
import gov.df.fap.bean.dictionary.dto.EleRelationSQLDTO;
import gov.df.fap.service.dictionary.DictionaryRight;
import gov.df.fap.service.util.gl.core.CommonUtil;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.StringUtil;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 
 * @author 
 * @version 
 */
public abstract class AbstractElementOperation implements ElementOperation {
	@Autowired
  protected DictionaryRight right = null;
  @Autowired
  IDDSet ddSet = null;

  public void setDdSet(IDDSet ddset) {
    this.ddSet = ddset;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.service.ElementOperationA#getRight()
   */
  public DictionaryRight getRight() {
    return right;
  }

  /* (non-Javadoc)
   * @see gov.gfmis.fap.dictionary.service.ElementOperationA#setRight(gov.gfmis.fap.dictionary.service.DictionaryRight)
   */
  public void setRight(DictionaryRight right) {
    this.right = right;
  }

  protected abstract String getSelectedField(String element, String tableName, String tableAlias,
    String[] column);

  protected String getQuerySql(int setYear, String element, String tableName, String[] column,
    boolean isNeedRight, Map relation, String coaId, String condition) throws Exception {

    if (tableName == null || "".equals(tableName))
      throw new Exception("根据要素简称" + element + "无法获取要素源表,无法执行查询!");
    //可能部分简称是oracle关键字,所以默认"element"作为别名,如"File"
    String tableAlias = "alias_" + element;
    StringBuffer strSQL = new StringBuffer("select ");
    strSQL.append(getSelectedField(element, tableName, tableAlias, column));
    strSQL.append(" from ").append(tableName).append(" ").append(tableAlias);
    StringBuffer wherePrefix = new StringBuffer(" where 1=1");
    if (relation != null && !relation.isEmpty()) {
      EleRelationSQLDTO relationSql = ddSet.getEleRelationSQLDTO(element, relation, tableAlias);
      strSQL.append(",").append(relationSql.getFromSQL());
      wherePrefix.append(relationSql.getWhereSQL());
    }
    strSQL.append(wherePrefix).append(" ")
      .append(getEleQueryWhereSql(setYear, element, isNeedRight, coaId, tableAlias, condition));
    //System.out.println("test>>>>"+strSQL.toString());
    return strSQL.toString();
  }

  protected String getQueryLikeSql(int setYear, String element, String tableName, String[] column,
    boolean isNeedRight, Map relation, String coaId, String condition) throws Exception {

    if (tableName == null || "".equals(tableName))
      throw new Exception("根据要素简称" + element + "无法获取要素源表,无法执行查询!");
    //可能部分简称是oracle关键字,所以默认"element"作为别名,如"File"
    String tableAlias = "alias_" + element;
    StringBuffer strSQL = new StringBuffer("select ");
    strSQL.append(getSelectedField(element, tableName, tableAlias, column));
    strSQL.append(" from ").append(tableName).append(" ").append(tableAlias);
    StringBuffer wherePrefix = new StringBuffer(" where 1=1");
    if (relation != null && !relation.isEmpty()) {
      EleRelationSQLDTO relationSql = ddSet.getEleRelationSQLDTO(element, relation, tableAlias);
      strSQL.append(",").append(relationSql.getFromSQL());
      wherePrefix.append(relationSql.getWhereSQL());
    }
    strSQL.append(wherePrefix).append(" ")
      .append(getEleQueryLikeSql(setYear, element, isNeedRight, coaId, tableAlias, condition));
    return strSQL.toString();
  }

  protected String getEleQueryWhereSql(int setYear, String element, boolean isNeedRight, String coaId,
    String tableAlias, String condition) throws Exception {
    StringBuffer strSQL = new StringBuffer();
    String rgCode = CommonUtil.getRgCode();
    if (isNeedRight) { //需要权限过滤
      if (StringUtil.isEmpty(CommonUtil.getRoleId()) || StringUtil.isEmpty(CommonUtil.getUserId())) {
        //return null;
      } else {
        strSQL.append(getRight().getSqlElemRight(CommonUtil.getUserId(), CommonUtil.getRoleId(), element,
          tableAlias));
      }
    }

    if (coaId != null && !coaId.equalsIgnoreCase("")) { //COA组件过滤
       ICoa coa = (ICoa) SessionUtil.getServerBean("coaService");
      strSQL.append(coa.getEleLevelNum(coaId, element, tableAlias)); // 调用COA组件，传入tableAlias及coa_id
    }
    if (!element.equalsIgnoreCase("rg")) {
      strSQL.append(" and ").append(tableAlias).append(".rg_code='").append(rgCode).append("'")
        .append("  and ").append(tableAlias).append(".set_year=").append(setYear);
    }
    return strSQL.append(condition == null ? "" : " " + condition).toString();
  }

  /**
   * 获取当前区划及下属相关已启用的区划的信息
   * 
   * <p> Modify by zhaoqiang TM:2012-04-20</p> 
   * 
  **/
  protected String getEleQueryLikeSql(int setYear, String element, boolean isNeedRight, String coaId,
    String tableAlias, String condition) throws Exception {
    StringBuffer strSQL = new StringBuffer();
    String rgCode = CommonUtil.getRgCode();

    if (isNeedRight) { //需要权限过滤
      if (StringUtil.isEmpty(CommonUtil.getRoleId()) || StringUtil.isEmpty(CommonUtil.getUserId())) {
      } else {
        strSQL.append(getRight().getSqlElemRight(CommonUtil.getUserId(), CommonUtil.getRoleId(), element,
          tableAlias));
      }
    }

    if (coaId != null && !coaId.equalsIgnoreCase("")) { //COA组件过滤
      ICoa  coa = (ICoa) SessionUtil.getOffServerBean("sys.coaService");
      strSQL.append(coa.getEleLevelNum(coaId, element, tableAlias)); // 调用COA组件，传入tableAlias及coa_id
    }

    if ("00".equals(rgCode.substring(4, 6))) {
      if ("00".equals(rgCode.substring(2, 4))) {
        return strSQL.append(" and ").append(tableAlias).append(".rg_code like'")
          .append(rgCode.subSequence(0, 2)).append("%'").append("  and ").append(tableAlias)
          .append(".set_year=").append(setYear).append(condition == null ? "" : " " + condition).toString();
      } else {
        return strSQL.append(" and ").append(tableAlias).append(".rg_code like '")
          .append(rgCode.subSequence(0, 4)).append("%'").append("  and ").append(tableAlias)
          .append(".set_year=").append(setYear).append(condition == null ? "" : " " + condition).toString();
      }

    } else {
      return strSQL.append(" and ").append(tableAlias).append(".rg_code ='").append(rgCode).append("'")
        .append("  and ").append(tableAlias).append(".set_year=").append(setYear)
        .append(condition == null ? "" : " " + condition).toString();

    }
    /*** Modify by zhaoqiang TM:2012-04-20 ***/
  }
}
