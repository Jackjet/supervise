package gov.df.fap.service.gl.balance.impl;

import gov.df.fap.service.gl.core.DaoSupport;

import java.util.HashMap;
import java.util.List;

/**
 * 游关系对象 
 * @author LiuYan
 */
public class StreamAssociate {
	
	private String streamTableName;
	
	private String fieldName;
	
	private DaoSupport daoSupport;

	public void setDaoSupport(DaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public StreamAssociate() {
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getStreamTableName() {
		return streamTableName;
	}

	public void setStreamTableName(String tableName) {
		this.streamTableName = tableName;
	}
	
	public void checkTable() throws Exception {
		daoSupport.genericQuery("select 1 from " + getStreamTableName() + " where 1<>1", HashMap.class);
	}

	/**
	 * 更新表层业务数据,根据传入转换集合更新id
	 * ccid,fromctrlid
	 * @param transRecordList 转换对象集合
	 */
	public void updateSurfaceData(List transRecordList) {
		try {
			checkTable();
		} catch (Exception ex) {
			if (ex.getCause().getMessage().startsWith("ORA-00942"))
				return ;
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("update ").append(getStreamTableName())
			.append(" voucher set ").append(getFieldName())
			.append("=#positiveBalance.sum_id# where voucher.").append(getFieldName())
			.append("=#negativeBalance.sum_id#");
		daoSupport.batchExcute(buffer.toString(), transRecordList);
	}
	
	/**
	 * 更新表层数据ccid,根据传入临时表进行更新id
	 */
	public void updateTraceSurfaceCcid(final List ccidGenList) {
		try {
			checkTable();
		} catch (Exception ex) {
			return ;
		}
		
		final StringBuffer buffer = new StringBuffer();
		buffer.append("update ").append(getStreamTableName())
			.append(" voucher set ").append(getFieldName())
			.append("=#ccid# where voucher.id")
			.append("=#vou_id# and ccid=#remark#");
		daoSupport.batchExcute(buffer.toString(), ccidGenList);
	}
	
	/**
	 * 更新底层数据ccid,根据传入临时表进行更新id
	 */
	public void updateTraceBalanceCcid(final List ccidGenList) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("update ").append(getStreamTableName())
			.append(" voucher set ").append(getFieldName())
			.append("=#ccid# where voucher.sum_id")
			.append("=#sum_id#");
		daoSupport.batchExcute(buffer.toString(), ccidGenList);
	}
	
	/**
	 * 更新日志表ccid
	 * @param ccidGenList
	 */
	public void updateJournalCcid(final List ccidGenList) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("update gl_journal jou set ccid=#ccid# where jou.vou_id=#vou_id# and ccid=#remark#");
		daoSupport.batchExcute(buffer.toString(), ccidGenList);
	}
	
}
