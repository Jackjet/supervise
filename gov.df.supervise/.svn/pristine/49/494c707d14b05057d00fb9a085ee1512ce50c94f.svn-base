package gov.df.supervise.bean.common;

import gov.df.fap.bean.util.FPaginationDTO;

import java.util.List;

import org.gov.df.supervice.util.TableData;

/**
* <p>Title:用于保存分页信息与数据list的bean</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: 北京用友政务软件有限公司</p>
* <p>CreateDate 2006-9-26</p>
* @author Tim
* @version 1.0
*/
public class PageListBean implements java.io.Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  FPaginationDTO page = null;

  TableData tableData = null;

  public PageListBean() {

  }

  /**
   * 构造方法，简单构造对象返回
   * @param page2
   * @param dataList
   */
  public PageListBean(FPaginationDTO page2, List dataList) {
    page = page2;
    this.dataList = dataList;
  }

  /**
   * 构造方法，压缩对象减少网络传输
   * @param page2  分页对象
   * @param dataList 原始列表对象
   * @param clumns 列对象
   */
  public PageListBean(FPaginationDTO page2, List dataList, Object[] clumns) {
    page = page2;
    totalrows = page2.getTotalrows();
    tableData = new TableData(clumns);
    tableData.addDataByDataList(dataList);
  }

  /**
   * 构造方法，压缩对象减少网络传输
   * @param page2 分页对象
   * @param data 网络传输对象
   */
  public PageListBean(FPaginationDTO page2, TableData data) {
    page = page2;
    tableData = data;
  }

  private int totalrows;

  private List dataList;

  public List getDataList() {
    if (tableData == null)
      return dataList;
    return tableData.toDataList();
  }

  public void setDataList(List dataList) {
    this.dataList = dataList;
  }

  public void setDataList(List dataList, Object[] clumns) {
    if (clumns == null) {
      setDataList(dataList);
      return;
    }
    tableData = new TableData(clumns);
    tableData.addDataByDataList(dataList);

  }

  public void setTableData(TableData tableData) {
    this.tableData = tableData;
  }

  public FPaginationDTO getPage() {
    return page;
  }

  public int getTotalrows() {
    return totalrows;
  }

  public void setTotalrows(int totalrows) {
    this.totalrows = totalrows;
  }

}
