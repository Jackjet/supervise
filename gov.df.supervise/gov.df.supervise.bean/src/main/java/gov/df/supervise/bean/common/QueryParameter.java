/**
 * 
 */
package gov.df.supervise.bean.common;

import gov.df.fap.bean.util.FPaginationDTO;
import gov.df.fap.util.web.CurrentUser;
import gov.df.fap.util.where.WhereObject;

import java.io.Serializable;

/**
 * <p>
 * Title:查询参数
 * </p>
 * <p>
 * Description:查询接口，必须是可复制的对象，且得保证复制时的线程安全
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * <p>
 * CreateDate 2010-10-29
 * </p>
 * 
 * @author hlf
 * @version 3.0.01.06
 */
public class QueryParameter implements Serializable {

  /**
  * 
  */
  private static final long serialVersionUID = -5528162326399151029L;

  /**
   * 是否查询明细
   */
  private boolean isDetailQuery = true;

  /**
   * 查询是否定值
   */
  private boolean isSetValue = false;

  /**
   * 查询条件对象，查询的过滤条件
   */
  private WhereObject condition = null;

  /**
   * 分页对象
   */
  private FPaginationDTO page = null;

  /**
   * 指定的查询列，如不指定则查询通用视图
   */
  private String[] columns = null;

  /**
   * 用户对象，包装很多客户端的属性
   */
  private CurrentUser user = null;

  /**
   * 是否关联工作流进行查询
   */
  private boolean isWorkFlowRelated = true;

  /**
   * 需要查询的物理表,可从外部直接指定，如不指定，则在取查询类的时候自动加载
   * 展现主表，可以是明细，也可以是单表
   */
  private String queryTable = null;

  /**
   * 明细表
   */
  private String detailTable = null;

  /**
   * 主表与明细ID 的关联字段
   */
  private String relationBillId = null;;

  /** 
   * 未审核（001）状态下包含被退回（004）数据
   * */
  private boolean isStatus001Include004 = false;

  public boolean isSetValue() {
    return isSetValue;
  }

  public void setSetValue(boolean isSetValue) {
    this.isSetValue = isSetValue;
  }

  public WhereObject getCondition() {
    return condition;
  }

  public void setCondition(WhereObject condition) {
    this.condition = condition;
  }

  public FPaginationDTO getPage() {
    return page;
  }

  public void setPage(FPaginationDTO page) {
    this.page = page;
  }

  public String[] getColumns() {
    return columns;
  }

  public void setColumns(String[] columns) {
    this.columns = columns;
  }

  public CurrentUser getUser() {
    return user;
  }

  public void setUser(CurrentUser user) {
    this.user = user;
  }

  public boolean isWorkFlowRelated() {
    return isWorkFlowRelated;
  }

  public void setWorkFlowRelated(boolean isWorkFlowRelated) {
    this.isWorkFlowRelated = isWorkFlowRelated;
  }

  public boolean isDetailQuery() {
    return isDetailQuery;
  }

  public void setDetailQuery(boolean isDetailQuery) {
    this.isDetailQuery = isDetailQuery;
  }

  public String getQueryTable() {
    return queryTable;
  }

  public void setQueryTable(String queryTable) {
    this.queryTable = queryTable;
  }

  public String getDetailTable() {
    return detailTable;
  }

  public void setDetailTable(String detailTable) {
    this.detailTable = detailTable;
  }

  public String getRelationBillId() {
    return relationBillId;
  }

  public void setRelationBillId(String relationBillId) {
    this.relationBillId = relationBillId;
  }

  public boolean isStatus001Include004() {
    return isStatus001Include004;
  }

  public void setStatus001Include004(boolean isStatus001Include004) {
    this.isStatus001Include004 = isStatus001Include004;
  }
}
