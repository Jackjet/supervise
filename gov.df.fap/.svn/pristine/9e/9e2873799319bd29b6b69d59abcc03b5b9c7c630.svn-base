package gov.df.fap.service.dictionary;

import gov.df.fap.api.dictionary.ElementOperation;
import gov.df.fap.api.dictionary.interfaces.IDDElement;
import gov.df.fap.bean.dictionary.dto.FElementDTO;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.util.xml.XMLData;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * IDD_Element 数据字典服务端组件接口实现类（要素相关）
 * @version 1.0
 * @author Zhang Peng
 * @since java 1.6
 */
@Component
public class DDElement implements IDDElement {
  @Autowired
  @Qualifier("elementOperationWrapper")
  protected ElementOperation eleOperation = null;
	@Autowired
	@Qualifier("generalDAO")
  private GeneralDAO dao = null;

  public void setDao(GeneralDAO dao) {
    this.dao = dao;
  }

  /**
   * 是否要素判断函数
   * @param element 要素简称
   * @return 判断结果
   */
  public boolean isElement(String element) {
    return eleOperation.isElement(element);
  }

  /**
   * 得到自定义级次的数据
   * @param coa_id
   * @param element
   * @return List
   */
  public List getCOADetailCode(String coa_id, String element) {
    return eleOperation.getCOADetailCode(coa_id, element);
  }

  /**
   * 根据要素表别名、分页当前页、分页页大小、显示字段、
   * 是否需要权限过滤、要素COA、要素关联关系、查询条件字符串查询要素表数据，
   * 按显示字段返回分页处理后的查询结果。
   * @param element 要素简称
   * @param pageIndex 当前页
   * @param pageCount 当前页大小
   * @param column 需要显示的字段
   * @param isNeedRight 是否需要权限过滤
   * @param coa_id 要素表COA
   * @param relation 要素关联关系
   * @param condition 查询条件字符串
   * @param tableAlias 要素表名
   * @return 查询结果XMLData对象
   */
  public XMLData getEleByCondition(String element, int pageIndex, int pageCount, String[] column, boolean isNeedRight,
    String coa_id, Map relation, String condition) {
    XMLData result = new XMLData();
    try {
      // 用ElementOperation类的getEleByCondition()实现
      result = eleOperation.getEleByCondition(element, pageIndex, pageCount, column, isNeedRight, coa_id, relation,
        condition);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 根据要素表别名、要素值查询处理后的查询结果。
   * @param elementDto 传入的dto对象
   * @param tableAlias 要素表名
   * @return 条件SQL字符串
   */
  public String getCondition(FElementDTO elementDto, String tableAlias) {
    String result = null;
    try {
      // 用ElementOperation类的getEleByCondition()实现
      result = eleOperation.getCondition(elementDto, tableAlias);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 根据传入的xml对象，执行相应的要素表数据插入
   * @param inObjXml 客户端组件构造的xml
   * @return 插入对象
   * @exception 操作失败原因
   */
  public XMLData insertEle(String inObjXml) throws Exception {
    // 用ElementOperation类的insertEleByXml()实现
    return eleOperation.insertEleByXml(inObjXml);
  }

  /**
   * 根据传入的xml对象，执行相应的要素表数据修改
   * @param inObjXml 客户端组件构造的xml
   * @return 修改后的对象
   * @exception 操作失败原因
   */
  public XMLData modifyEle(String inObjXml) throws Exception {
    // 用ElementOperation类的modifyEleByXml()实现
    return eleOperation.modifyEleByXml(inObjXml);
  }

  /**
     * 级联删除要素表数据
   * @param element 要素简称
   * @param id 数据id
     * @return 操作状态
     * @throws Exception 操作失败原因
     */
  public boolean deleteEle(String element, String id) throws Exception {
    // 用ElementOperation类的deleteEle()实现
    return eleOperation.deleteEle(element, id);
  }

  /**
   * 根据传入的xml对象，执行相应的要素管理表查询
   * @param inObjXml 客户端组件构造的xml
   * @param isNeedCount 是否需要分页
   * @return 查询结果xml
   */
  public String doEleSetQuery(String inObjXml, boolean isNeedCount) {
    String result = null;
    try {
      // 用ElementOperation类的getEleSetByXml()实现
      result = eleOperation.getEleSetByXml(inObjXml, isNeedCount);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 根据传入的xml对象，执行相应的要素管理表数据插入
   * @param inObjXml 客户端组件构造的xml
   * @return 插入对象
   * @exception 操作失败原因
   */
  public XMLData insertEleSet(String inObjXml) throws Exception {
    // 用ElementOperation类的insertEleSetByXml()实现
    return eleOperation.insertEleSetByXml(inObjXml);
  }

  /**
   * 根据传入的xml对象，执行相应的要素管理表数据修改
   * @param inObjXml 客户端组件构造的xml
   * @return 修改后的对象
   * @exception 操作失败原因
   */
  public XMLData modifyEleSet(String inObjXml) throws Exception {
    // 用ElementOperation类的modifyEleSetByXml()实现
    return eleOperation.modifyEleSetByXml(inObjXml);
  }

  /**
   * 根据传入的id，执行相应的要素管理表数据删除
   * @param id 要素配置信息唯一ID
   * @return 操作是否成功
   * @exception 操作失败原因
   */
  public boolean deleteEleSet(String id) throws Exception {
    // 用ElementOperation类的deleteEleSet实现
    return eleOperation.deleteEleSet(id);
  }

  /**
   * 实现返回基础要素表的数据权限sql语句
   * @param userid-------------用户id
   * @param roleid-------------角色id
   * @param elemcode-----------要素简称
   * @param tablealias---------基础表别名
   * @return sql语句
   * @throws Exception---------错误信息
   */
  public String getSqlElemRight(String userid, String roleid, String elemcode, String tablealias) throws Exception {
    return eleOperation.getSqlElemRight(userid, roleid, elemcode, tablealias);
  }

  public ElementOperation getEleOperation() {
    return eleOperation;
  }

  public void setEleOperation(ElementOperation eleOperation) {
    this.eleOperation = eleOperation;
  }

  /**判断当预算单位修改时，此时的预算单位属性是否可以修改*/
  public boolean checkIsReform(Map fieldInfo) {
    String chr_code = (String) fieldInfo.get("chr_code");
    String isReform = (String) fieldInfo.get("is_reform");
    StringBuffer isCheck = new StringBuffer();
    isCheck.append("select is_reform from ele_enterprise where chr_code='").append(chr_code).append("'");
    List isCheckList = dao.findBySql(isCheck.toString());

    if (isCheckList.size() > 0 && !isCheckList.equals("")) {
      XMLData dat = (XMLData) isCheckList.get(0);
      String str = (String) dat.get("is_reform");
      if (!str.equalsIgnoreCase(isReform)) {
        StringBuffer validateSql1 = new StringBuffer();
        StringBuffer validateSql2 = new StringBuffer();
        validateSql1
          .append(
            "select count(*) from gl_bus_voucher_detail  where ccid in (select ccid from gl_ccids where en_id = '")
          .append(chr_code).append("')");
        validateSql2
          .append(
            "select count(*) from gl_bus_voucher_detail_his where ccid in (select ccid from gl_ccids where en_id = '")
          .append(chr_code).append("')");
        List list1 = dao.findBySql(validateSql1.toString());
        XMLData xml = (XMLData) list1.get(0);
        List list2 = dao.findBySql(validateSql2.toString());
        XMLData xml2 = (XMLData) list2.get(0);
        if (xml.size() + xml2.size() > 0) {
          return false;
        } else {
          return true;
        }
      }
    }
    return true;
  }
}
