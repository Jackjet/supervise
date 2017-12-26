package gov.df.fap.service.gl.coa.impl;

import gov.df.fap.service.gl.core.DaoSupport;
/**
 * 创建CCID表
 * @author ydz
 *
 */
public class CcidsTable {
	private String ccids_table;
	private DaoSupport daoSupport = null;
	public CcidsTable(String ccids_table){
		this.ccids_table=ccids_table;
	}
	public void create(){
		String createSQL="create table  "+ccids_table
			  +" as select * from gl_ccids where 1<>1";
		daoSupport.executeUpdate(createSQL);
		
		String MD5index="create unique index IDX_"+ccids_table+"_MD5 on  "+ccids_table+"(MD5)";
		daoSupport.executeUpdate(MD5index);
		String alterSQL1="alter table "+ccids_table
			+" add constraint "+ccids_table+"_PRO_PK primary key (CCID)";
		daoSupport.executeUpdate(alterSQL1);
		String alterSQL2="alter table "+ ccids_table
		  +" add constraint "+ ccids_table+"_F_PK foreign key (COA_ID)"
		  +"references GL_COA (COA_ID)";
		daoSupport.executeUpdate(alterSQL2);
	}
	public void setDaoSupport(DaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
}
