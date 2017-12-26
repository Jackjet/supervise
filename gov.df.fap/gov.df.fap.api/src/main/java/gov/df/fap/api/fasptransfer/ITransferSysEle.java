package gov.df.fap.api.fasptransfer;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
/**
 * 历史数据迁移到平台
 * @author hult
 *
 */
public interface ITransferSysEle {
	/**
	 * 迁移用户
	 * @param rg_code
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public int transferUser(String rg_code,String year) throws SQLException;
	/**
	 * 普通基础数据迁移
	 * @param ele_code
	 * @param pt_code
	 * @param old_ele_code
	 * @param rg_code
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public int transferDatazSet(final String ele_code,String pt_code,final String old_ele_code, final String rg_code,final String year) throws SQLException;
	/**
	 * 迁移单位
	 * @param rg_code
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	
	public int transferAgency(String rg_code,String year) throws SQLException;
	/**
	 * 迁移银行
	 * @param rg_code
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public int transferBank(String rg_code,String year) throws SQLException;
	/**
	 * 迁移部门
	 * @param rg_code
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public int transferDepartment(String rg_code,String year) throws SQLException;
	/**
	 * 迁移角色
	 * @param rg_code
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	public int transferRole(final String admdiv,String rg_code,String year) throws SQLException;
	/**
	 * 迁移业务数据
	 * @param newtable
	 * @param oldtable
	 * @param rg_code
	 * @param year
	 * @throws SQLException
	 */
	public void transferBo(String newtable,String oldtable,String rg_code,String year) throws SQLException;
	/*
	 * 获取要迁移的基础数据
	 */
	public List getTransferEle(String rg_code,String year);
	/*
	 * 获取要迁移的业务表
	 */
	public List getTransferBo(String rg_code,String year);
	/**
	 * 取得区划年度
	 * @return
	 */
	public Map getRgcodeSetyear();
	/**
	 * 获取用户权限等相关的
	 * @return
	 */
 
	public List<Map> getTransferRight(String rg_code, String year);
	/**
	 * 迁移字段编码对照
	 * @param right_table
	 * @param right_sql
	 * @param is_same
	 * @param rg_code
	 * @param year
	 */
	public void transferRight(String right_table, String right_sql,
			String is_same, String rg_code, String year);
	/**
	 * 迁移字段对照
	 * @param table_name
	 * @param table_sql
	 * @param is_same
	 * @param eleCode
	 * @param rg_code
	 * @param year
	 */
	public void transferEleCode(final String table_name, String table_sql,
			String is_same,List<String> eleCode, String rg_code, String year);
	/**
	 * 迁移账户信息
	 * @param ele_code
	 * @param pt_code
	 * @param old_ele_code
	 * @param rg_code
	 * @param year
	 * @return
	 */
	public int transferAccount(final String ele_code, String pt_code,
			final String old_ele_code,final String rg_code, final String year);
	/**
	 * 生成ccids；
	 * @param tableName
	 * @return
	 */
	public int transferGlCcid(String tableName);
}
