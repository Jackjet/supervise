
/*
 * @(#)FPaginationDTO.java	1.0 09/06/06
 *
 * Copyright 2006 by Founder Sprint 1st, Inc. All rights reserved.
 */
package gov.df.fap.bean.util;

/**
 * 分页DTO对象
 * @version 1.0
 * @author 
 * @since java 1.6
 */
public class FPaginationDTO extends DTO
{
	public static final long serialVersionUID = -1L;	
	public static final int INIT_SIZE_BROWSER = 20;
	/*当前页*/
	private int currpage=0;
	/*行数*/
	private int rows;
	/*页面大小*/
	private int pagecount = 20;
	/*总行数*/
	private int totalrows;
	/*总页数*/
	private int totalpages;
	/*全部显示标志*/
	private int allflag;
	public int getAllflag() {
		return allflag;
	}
	public void setAllflag(int allflag) {
		this.allflag = allflag;
	}
	public int getCurrpage() {
		return currpage;
	}
	public void setCurrpage(int currpage) {
		this.currpage = currpage;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getTotalpages() {
		return totalpages;
	}
	public void setTotalpages(int totalpages) {
		this.totalpages = totalpages;
	}
	public int getTotalrows() {
		return totalrows;
	}
	public void setTotalrows(int totalrows) {
		this.totalrows = totalrows;
	}
	public int getPagecount() {
		return pagecount;
	}
	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}
}
