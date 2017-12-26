package gov.df.fap.service.gl.core.sqlgen;

import gov.df.fap.api.dictionary.ElementInfo;
import gov.df.fap.api.dictionary.ElementOperation;
import gov.df.fap.bean.dictionary.element.ElementDefinition;
import gov.df.fap.bean.gl.configure.BusVouAccount;
import gov.df.fap.bean.gl.core.sqlgen.SqlGenerator;
import gov.df.fap.bean.gl.dto.Condition;
import gov.df.fap.bean.gl.dto.IConditionItem;
import gov.df.fap.service.dictionary.element.ElementConfiguration;
import gov.df.fap.service.dictionary.element.ElementOperationWrapperBO;
import gov.df.fap.util.Tools;
import gov.df.fap.util.xml.XMLData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 要素查询语句解析类
 * @author 
 * @version 2017-04-30
 *
 */
@Service
public class ElementSqlGenerator implements SqlGenerator {

  private static final long serialVersionUID = 1L;

  @Autowired
  @Qualifier("elementOperationWrapper")
  private ElementOperation eleOp = null;

  private boolean isCache = false;

  public void setEleOp(ElementOperation eleOp) {
    this.eleOp = eleOp;
    if (eleOp instanceof ElementOperationWrapperBO) {
      isCache = true;
    }
  }

  public String generateStatement(IConditionItem conditionItem, BusVouAccount sumType) {
    StringBuffer strSQL = new StringBuffer();
    strSQL.append(" " + conditionItem.getConnectOper());

    String ele = conditionItem.getField().substring(0, conditionItem.getField().lastIndexOf("_"));
    String field = conditionItem.getField();
    String value = conditionItem.getValue();
    String opera = conditionItem.getOperation();
    String ele_source = (String) eleOp.getEleSetByCode(ele).get("ele_source");

    if (ele_source == null || ele_source.equals("")) {
      throw new RuntimeException("无法定位要素" + ele + "对应的数据源,查询失败!");
    }
    if ((ele + "_name").equalsIgnoreCase(field))//如果是名称
    {
      strSQL.append(" (exists (select 1 from ").append(ele_source).append("" + Tools.addDbLink() + " d ")
        .append("where c.").append(ele).append("_id = d.chr_id and chr_name like '%").append(value).append("%'))");
      return strSQL.toString();
    } else {
      if (isCache && ElementConfiguration.getConfig().isElementSourceCache()
        && ElementConfiguration.getConfig().isEleCached(ele))
        return generateStatementByCache(conditionItem, sumType, ele, field, opera, value);
      else
        return generateStatementPureDb(ele, ele_source, field, opera, value);
    }
  }

  /**
   * 纯数据库的生成方式
   * @param conditionItem
   * @param sumType
   * @return
   */
  protected String generateStatementPureDb(String ele, String ele_source, String field, String opera, String value) {
    StringBuffer strSQL = new StringBuffer();
    //既支持从上往下like查询,又支持从下往上模糊匹配
    strSQL.append(" and (exists (select 1 from ").append(ele_source).append("" + Tools.addDbLink() + " d ")
      .append("where c.").append(ele).append("_id = d.chr_id and d.chr_id in (select chr_id from ").append(ele_source)
      .append(" dd where ");
    if ((ele + "_id").equalsIgnoreCase(field))
      strSQL.append("dd.chr_id ").append(opera).append(" ");
    else
      strSQL.append("dd.chr_code ").append(opera).append(" ");

    if (opera.equals(Condition.IN) || opera.equals(Condition.NOTIN)) {
      strSQL.append("(");
      StringTokenizer sz = new StringTokenizer(value, ",");
      while (sz.hasMoreTokens())
        strSQL.append("'").append(sz.nextToken()).append("',");

      strSQL.deleteCharAt(strSQL.length() - 1);
      strSQL.append(")");
    } else if (opera.equals(Condition.LIKE) || opera.equals(Condition.NOTLIKE))
      strSQL.append("'").append(value).append("%'");
    else
      strSQL.append("'").append(value).append("'");

    strSQL.append(" and (d.chr_id=dd.chr_id1 or d.chr_id=dd.chr_id2 or d.chr_id=dd.chr_id3 ")
      .append("or d.chr_id=dd.chr_id4 or d.chr_id=dd.chr_id5 or d.chr_id1=dd.chr_id ")
      .append("or d.chr_id2=dd.chr_id or d.chr_id3=dd.chr_id or d.chr_id4=dd.chr_id ")
      .append("or d.chr_id5=dd.chr_id) or c.").append(ele).append("_id is null))) ");

    return strSQL.toString();
  }

  /**
   * 使用缓存的生成方式
   * @param conditionItem
   * @param sumType
   * @return
 * @throws SQLException 
   */
  protected String generateStatementByCache(IConditionItem conditionItem, BusVouAccount sumType, String ele,
    String field, String opera, String value) {
    //modify xuzx1 2013-05-02 bugZWCZ00163572，来源额度-not like时：查询出的结果不正确。
    StringBuffer strSQL = null;
    //既支持从上往下like查询,又支持从下往上模糊匹配
    if (opera.equals(Condition.IN) || opera.equals(Condition.NOTIN)) {
      StringBuffer tempSQL = new StringBuffer();
      // begin: modified by zhoulingling 20131010 ZWCZ00201109额度过滤条件sql时，where条件中in大于1000条时报错。
      tempSQL.append(" and (");
      if ((ele + "_id").equalsIgnoreCase(field)) {
        tempSQL.append(" chr_id ").append(opera).append(" ");
      } else {
        tempSQL.append(" chr_code ").append(opera).append(" ");
      }
      tempSQL.append("(");
      StringTokenizer sz = new StringTokenizer(value, ",");
      int count1000 = 0;
      while (sz.hasMoreTokens()) {
        if (++count1000 >= 1000) {
          tempSQL.deleteCharAt(tempSQL.length() - 1);
          if (opera.equals(Condition.IN))
            tempSQL.append(") or ");
          else if (opera.equals(Condition.NOTIN))
            tempSQL.append(") and ");
          if ((ele + "_id").equalsIgnoreCase(field)) {
            tempSQL.append(" chr_id ").append(opera).append(" (");
          } else {
            tempSQL.append(" chr_code ").append(opera).append(" (");
          }
          count1000 = 0;
        }
        tempSQL.append("'").append(sz.nextToken()).append("',");
      }
      tempSQL.deleteCharAt(tempSQL.length() - 1);
      tempSQL.append("))");
      // end: modified by zhoulingling 20131010 ZWCZ00201109额度过滤条件sql时，where条件中in大于1000条时报错。
      strSQL = new StringBuffer();
      strSQL.append(tempSQL);
    } else if (opera.equals(Condition.NOTLIKE)) {
      StringTokenizer st = new StringTokenizer(value, ",");
      StringBuffer tempSQL = new StringBuffer();
      if ((ele + "_id").equalsIgnoreCase(field)) {
        while (st.hasMoreTokens()) {
          String v = st.nextToken();
          tempSQL.append(" and chr_id ").append(opera).append("'").append(v).append("%'");
        }
      } else {
        while (st.hasMoreTokens()) {
          String v = st.nextToken();
          tempSQL.append(" and chr_code ").append(opera).append("'").append(v).append("%'");
        }
      }
      strSQL = new StringBuffer();
      strSQL.append(tempSQL);
    } else if (opera.equals(Condition.LIKE)) {
      StringTokenizer st = new StringTokenizer(value, ",");
      StringBuffer tempSQL = new StringBuffer(" and (");
      if ((ele + "_id").equalsIgnoreCase(field)) {
        while (st.hasMoreTokens()) {
          String v = st.nextToken();
          tempSQL.append("chr_id ").append(opera).append(" '").append(v).append("%'").append(" or ");
        }
        tempSQL.delete(tempSQL.length() - 4, tempSQL.length());
        tempSQL.append(")");
      } else {
        while (st.hasMoreTokens()) {
          String v = st.nextToken();
          tempSQL.append("chr_code ").append(opera).append(" '").append(v).append("%'").append(" or ");
        }
        tempSQL.delete(tempSQL.length() - 4, tempSQL.length());
        tempSQL.append(")");
      }
      strSQL = new StringBuffer();
      strSQL.append(tempSQL);
    } else {
      StringBuffer tempSQL = new StringBuffer();
      tempSQL.append(" and ");
      if ((ele + "_id").equalsIgnoreCase(field)) {
        tempSQL.append(" chr_id ").append(opera).append(" ");
      } else {
        tempSQL.append(" chr_code ").append(opera).append(" ");
      }
      tempSQL.append("'").append(value).append("'");
      strSQL = new StringBuffer();
      strSQL.append(tempSQL);
    }
    //modify xuzx1 end
    List list = null;
    try {
      list = (List) eleOp.getEleByCondition(ele, -1, -1, null, false, null, null, strSQL.toString()).get("row");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    //modify xuzx1 2013-05-02 bugZWCZ00163572 方法加个参数
    List allElement = fetchAllQueryElements(ele, list, opera);
    if (allElement == null)
      return PlusSql.getDefaultSqlGenrator(opera).generateStatement(conditionItem, sumType);

    strSQL = new StringBuffer(" and (c." + ele + "_id in (");
    Iterator it = allElement.iterator();
    int n = 0;
    while (it.hasNext()) {
      if (++n >= 1000) {
        strSQL.delete(strSQL.length() - 1, strSQL.length());
        strSQL.append(" ) or c." + ele + "_id in (");
        n = 0;
      }
      final ElementInfo elementInfo = (ElementInfo) it.next();
      strSQL.append("'").append(elementInfo.getChrId()).append("'").append(",");
    }
    strSQL.delete(strSQL.length() - 1, strSQL.length());
    if (ele.equalsIgnoreCase("pk")) {
      strSQL.append("))");
    } else {
      strSQL.append(") or c." + ele + "_id is null) ");
    }
    return strSQL.toString();
  }

  /**
   * 读取所有上下游要素
   * @param list
   * @return
   */
  protected List fetchAllQueryElements(String eleCode, List list, String opera) {
    if (list == null || list.isEmpty())
      return null;
    List allElements = new ArrayList();
    for (int i = 0; i < list.size(); i++) {
      final String chrId = (String) ((XMLData) list.get(i)).get(ElementDefinition.CHR_ID);
      ElementInfo eleInfo = eleOp.getElementInfo(eleCode, chrId);
      //modify xuzx1 2013-05-02 bugZWCZ00163572 方法加个参数
      pickUpNodes(allElements, eleInfo, opera);
    }
    return allElements;
  }

  /**
   * 读取某要素节点上下游相关要素节点
   * @param elementInfo 指定要素节点
   * @return 所有相关节点
   */
  protected void pickUpNodes(List container, ElementInfo elementInfo, String opera) {
    container.add(elementInfo);
    //modify xuzx1 2013-05-02 给该方法加个opera参数， 如果是NOTLIKE和NOTIN不取上下节点，bugZWCZ00163572
    if (!opera.equals(Condition.NOTLIKE) && !opera.equals(Condition.NOTIN) && !opera.equals(Condition.NOTEQUAL)) {
      pickParentNodes(container, elementInfo);
      pickChildNodes(container, elementInfo);
    }
  }

  /**
   * 读取父节点
   * @param container
   * @param elementInfo
   */
  protected void pickParentNodes(List container, ElementInfo elementInfo) {
    //读取父节点
    ElementInfo elementInfoDo = elementInfo;
    while (elementInfoDo.getParent() != null && !elementInfoDo.getParent().isRoot()) {
      elementInfoDo = elementInfoDo.getParent();
      if (!container.contains(elementInfoDo)) {
        container.add(elementInfoDo);
      }
    }
  }

  /**
   * 读取子节点
   * @param container
   * @param element
   * @return
   */
  protected void pickChildNodes(List container, ElementInfo element) {
    if (element.getChildren() == null)
      return;
    List children = element.getChildren();
    Iterator it = children.iterator();
    while (it.hasNext()) {
      final ElementInfo childElement = (ElementInfo) it.next();
      if (!container.contains(childElement)) {
        container.add(childElement);
      }
      pickChildNodes(container, childElement);
    }
  }
}
