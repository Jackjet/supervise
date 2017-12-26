package gov.df.fap.service.fasptransfer;

import gov.df.fap.api.fasptransfer.ITransferSysEle;
import gov.df.fap.api.gl.coa.ibs.ICoa;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.datasource.TypeOfDB;
import gov.df.fap.service.util.sessionmanager.SessionUtil;
import gov.df.fap.util.factory.ServiceFactory;
import gov.df.fap.util.trans.SmallTransExecute;
import gov.df.fap.util.trans.SmallTransService;
import gov.mof.fasp2.ca.role.dto.RoleDTO;
import gov.mof.fasp2.ca.role.dto.UserAndRoleDTO;
import gov.mof.fasp2.ca.role.service.IRoleService;
import gov.mof.fasp2.ca.user.dto.UserDTO;
import gov.mof.fasp2.ca.user.service.IUserService;
import gov.mof.fasp2.dic.agency.service.IAgencyRangeService;
import gov.mof.fasp2.dic.dataset.client.range.DataSetRangeClient;
import gov.mof.fasp2.dic.dataset.dto.DataSetRangeDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.longtu.framework.exception.AppException;

/**
 * 
 * @author hult
 * 
 */
@Service
public class TransferSysEleBO implements ITransferSysEle {
	@Autowired
	@Qualifier("generalDAO")
	GeneralDAO dao;
	@Autowired
	SmallTransService smallTrans;
	  @Autowired
	  private ICoa coa;

	/**
	 * 将旧系统的用户导入新系统
	 */
	public int transferUser(final String rg_code, final String year)
			throws SQLException {
		IUserService userservice = (IUserService) ServiceFactory
				.getBean("fasp2.ca.user.service");

		UserDTO paramUserDTO = new UserDTO();
		String sql = null;
		if(TypeOfDB.isOracle()) {
			sql = "select 'c4ca4238a0b923820dcc509a6f75849b' as initialpassword,'1' status,"
					+ " user_code as code,user_id,lpad(user_id,32,'0')  as guid,"
					+ " password ,user_name name,2 loginmode,3 admintype,"
					+ " decode(org_type,'007',replace(replace(replace(org_code,'{',''),'}',''),'-','')) as division,"
					+ " replace(replace(replace(decode(org_type,'003',org_code,'004',org_code,'002',org_code),'{',''),'}',''),'-','') as agency,"
					+ " decode(org_type,'001',-1,'002',0,'007',1,'003',3,'004',3) usertype "
					+ " from sys_user";
		} else if(TypeOfDB.isMySQL()) {
			sql = "select 'c4ca4238a0b923820dcc509a6f75849b' as initialpassword,'1' status,"
					+ " user_code as code,user_id,lpad(user_id,32,'0')  as guid,"
					+ " password ,user_name name,2 loginmode,3 admintype,"
					+ " case org_type when '007' then replace(replace(replace(org_code,'{',''),'}',''),'-','') END as division,"
					+ " replace(replace(replace((case org_type when '002' then org_code when '003' then org_code when '004' then org_code END),'{',''),'}',''),'-','') as agency,"
					+ " decode(org_type,'001',-1,'002',0,'007',1,'003',3,'004',3) usertype "
					+ " from sys_user";
		}
		List<Map> adddata = OldDataUtil.getData(sql);
		String admdiv = "";
		List datalist = dao.findBySql(
				"select * from FASP_T_PUBADMDIV where code=?",
				new Object[] { rg_code });
		if (datalist.size() > 0) {
			admdiv = (String) ((Map) datalist.get(0)).get("guid");
		}

		try {
			List<UserDTO> list = userservice.findAllUsers();
			List<UserDTO> dtolist = new ArrayList();
			for (int i = 0; i < adddata.size(); i++) {
				Map<String, Object> dtomap = (Map) adddata.get(i);
				dtomap.put("admdiv", admdiv);
				UserDTO userDTO = new UserDTO();
				userDTO.putAll(dtomap);
				boolean isFasp = false;
				for (UserDTO dto : list) {
					if (userDTO.getCode().equals(dto.getCode())) {
						isFasp = true;
						break;
					}
				}
				if (!isFasp) {
					dtolist.add(userDTO);
				}
			}
			if (dtolist.size() > 0) {
				userservice.createNewUsers(dtolist);

			}
			list = userservice.findAllUsers();
			final ArrayList transList = new ArrayList();
			for (int i = 0; i < adddata.size(); i++) {
				Map<String, Object> dtomap = (Map) adddata.get(i);
				UserDTO userDTO = new UserDTO();
				userDTO.putAll(dtomap);
				boolean isFasp = false;
				Map transferEle = new HashMap();
				transferEle.put("ele_code", "USER");
				transferEle.put("old_ele_code", "USER");
				transferEle.put("old_id", dtomap.get("user_id"));
				transferEle.put("new_id", userDTO.getGuid());
				transferEle.put("old_code", userDTO.getCode());
				transferEle.put("new_code", userDTO.getCode());
				transferEle.put("old_name", userDTO.getName());

				transferEle.put("rg_code", rg_code);
				transferEle.put("set_year", year);

				for (UserDTO dto : list) {
					if (userDTO.getCode().equals(dto.getCode())) {
						transferEle.put("new_id", dto.getGuid());
						transferEle.put("new_name", dto.getName());
						isFasp = true;
						break;
					}
				}

				transList.add(transferEle);

			}
			try {
				smallTrans.newTransExecute(new SmallTransExecute() {
					public void doExecute() throws Exception {

						dao.executeBySql(
								"delete sys_transfer_ele where ele_code=?",
								new Object[] { "USER" });
						dao.executeBySql(
								"update  sys_transfer_base set is_transfer=1  where ele_code=? and rg_code =? and set_year = ? ",
								new Object[] { "USER", rg_code, year });

						dao.batchSaveDataBySql("sys_transfer_ele", transList);
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return transList.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 将旧系统的角色导入新系统
	 */
	public int transferRole(String admdiv, final String rg_code,
			final String year) throws SQLException {
		IRoleService roleservice = (IRoleService) ServiceFactory
				.getBean("fasp2.ca.role.service");
		List list = dao.findBySql(
				"select * from FASP_T_PUBADMDIV where code=?",
				new Object[] { rg_code });
		if (list.size() > 0) {
			admdiv = (String) ((Map) list.get(0)).get("guid");
		}
		RoleDTO paramUserDTO = new RoleDTO();
		List<Map> adddata = OldDataUtil
				.getData("select role_id,lpad(role_id,32,'0') as guid,0 as ROLELEVEL,1 as status,role_code as code,'df' as REMARK1,"
						+ year
						+ " as YEAR  ,'"
						+ rg_code
						+ "' as PROVINCE,'',0 rolenature, "
						+"(case when (select count(1) from sys_user_role_rule rr ,sys_user su where su.user_code not like '%999%' and rr.user_id = su.USER_ID and rr.role_id = t.role_id and su.ORG_TYPE='002')"
						+">(select count(1) from sys_user_role_rule rr ,sys_user su where  su.user_code not like '%999%' and rr.user_id = su.USER_ID and rr.role_id = t.role_id and su.ORG_TYPE='007')"
						+" then 2 else 1 end) as roletype,'"
						+ admdiv + "'admdiv,role_name as name  from  sys_role t ");

		try {
			final List transList = new ArrayList();
			List<RoleDTO> dtolist = new ArrayList();
			for (int i = 0; i < adddata.size(); i++) {
				Map<String, Object> dtomap = (Map) adddata.get(i);
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.putAll(dtomap);
				dtolist.add(roleDTO);

			}
			if (dtolist.size() > 0) {
				roleservice.createRole(dtolist);

			}
			List<Map> userroledata = null;
			if(TypeOfDB.isOracle()) {
				userroledata = OldDataUtil.getData("select lpad(role_id,32,'0') roleguid,lpad(user_id,32,'0') userguid,SYS_guid() guid from sys_user_role_rule ");
			} else if(TypeOfDB.isMySQL()) {
				userroledata = OldDataUtil.getData("select lpad(role_id,32,'0') roleguid,lpad(user_id,32,'0') userguid,replace(UUID(), '-', '') guid from sys_user_role_rule ");
			}
			
			List<UserAndRoleDTO> userrole = new ArrayList();
			for(Map map:userroledata){
				UserAndRoleDTO dto = new UserAndRoleDTO();
				dto.putAll(map);
				userrole.add(dto);
			}
			roleservice.addUsers(userrole);
			for (int i = 0; i < adddata.size(); i++) {
				Map<String, Object> dtomap = (Map) adddata.get(i);
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.putAll(dtomap);
				boolean isFasp = false;
				Map transferEle = new HashMap();
				transferEle.put("ele_code", "ROLE");
				transferEle.put("old_ele_code", "ROLE");
				transferEle.put("old_id", dtomap.get("role_id"));
				transferEle.put("new_id", roleDTO.getGuid());
				transferEle.put("old_code", roleDTO.getCode());
				transferEle.put("new_code", roleDTO.getCode());
				transferEle.put("old_name", roleDTO.getName());
				transferEle.put("new_name", roleDTO.getName());
				transferEle.put("rg_code", rg_code);
				transferEle.put("set_year", year);
				transList.add(transferEle);

			}
			try {
				smallTrans.newTransExecute(new SmallTransExecute() {
					public void doExecute() throws Exception {

						dao.executeBySql(
								"delete sys_transfer_ele where ele_code=?",
								new Object[] { "ROLE" });
						dao.executeBySql(
								"update  sys_transfer_base set is_transfer=1  where ele_code=? and rg_code =? and set_year = ? ",
								new Object[] { "ROLE", rg_code, year });

						dao.batchSaveDataBySql("sys_transfer_ele", transList);
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return transList.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 导入对应的要素
	 */
	public int transferDatazSet(final String ele_code, String pt_code,
			final String old_ele_code, final String rg_code, final String year)
			throws SQLException {
		try {
			List ele = dao
					.findBySql(
							"select * from sys_element where ele_code=? and rg_code = ? and set_year=?",
							new Object[] { ele_code, rg_code, year });
			if (ele.size() == 0) {
				System.out.println(ele_code + "没有在sys_element维护");
				return 0;
			}
			List divlist = dao.findBySql(
					"select * from FASP_T_PUBADMDIV where code=?",
					new Object[] { rg_code });
			String admdiv = "";
			if (divlist.size() > 0) {
				admdiv = (String) ((Map) divlist.get(0)).get("guid");
			}
			String ele_table = (String) ((Map) ele.get(0))
					.get("old_ele_source");

			DataSetRangeClient ds = (DataSetRangeClient) ServiceFactory
					.getBean("fasp2.dic.dataset.range.service");
			String sql = "select chr_id, replace(replace(replace(chr_id,'{',''),'}',''),'-','') as guid"
					+ ",chr_code as code ,chr_name as name ,1 as status,"
					+ year
					+ " as year,'"
					+ rg_code;
					if(TypeOfDB.isOracle()) {
						sql += "' as province,replace(replace(replace(nvl(parent_id,'0'),'{',''),'}',''),'-','') as superguid,is_leaf as isleaf, level_num as levelno,1 as version ,'支付系统导入' as remark ";
					} else if(TypeOfDB.isMySQL()) {
						sql += "' as province,replace(replace(replace(ifnull(parent_id,'0'),'{',''),'}',''),'-','') as superguid,is_leaf as isleaf, level_num as levelno,1 as version ,'支付系统导入' as remark ";
					}
					sql += ",(select chr_code from    "
					+ ele_table
					+ " where chr_id =ele.parent_id and rg_code=ele.rg_code and set_year=ele.set_year ) as super_code from "
					+ ele_table
					+ " ele where set_year="
					+ year
					+ " order by chr_code";

			List<DataSetRangeDTO> list;
			try {
				list = (List<DataSetRangeDTO>) ds.getAllLowerDsRanges(
						"FASP_T_PUB" + pt_code, "0");
			} catch (AppException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return 0;
			}
			List<Map> adddata = OldDataUtil.getData(sql);
			final Map mapTranfer = new HashMap();

			List<DataSetRangeDTO> dtolist = new ArrayList();
			for (int i = 0; i < adddata.size(); i++) {
				Map<String, Object> dtomap = (Map) adddata.get(i);
				DataSetRangeDTO dataDTO = new DataSetRangeDTO();
				dataDTO.putAll(dtomap);
				Map transferEle = new HashMap();
				transferEle.put("ele_code", ele_code);
				transferEle.put("old_ele_code", old_ele_code);
				transferEle.put("old_id", dtomap.get("chr_id"));
				transferEle.put("new_id", dataDTO.getGuid());
				transferEle.put("old_code", dataDTO.getCode());
				transferEle.put("new_code", dataDTO.getCode());
				transferEle.put("old_name", dataDTO.getName());
				transferEle.put("new_name", dataDTO.getName());
				transferEle.put("rg_code", rg_code);
				transferEle.put("set_year", year);
				mapTranfer.put(dataDTO.getCode(), transferEle);
				dataDTO.put("admdiv", admdiv);
				// 是否平台已维护
				boolean isFasp = false;
				for (DataSetRangeDTO dto : list) {
					// 平台已经维护，将本系统的newid ，newname已经superid都要维护
					if (dto.getCode().equals(dataDTO.getCode())) {
						transferEle.put("new_id", dto.getGuid());
						transferEle.put("new_name", dto.getName());
						isFasp = true;
						break;
					}
				}
				if (!"0".equals(dataDTO.getSuperguid())) {
					dataDTO.setSuperguid((String) ((Map) mapTranfer.get(dtomap
							.get("super_code"))).get("new_id"));
				}
				if (!isFasp) {
					dtolist.add(dataDTO);
				}
			}

			if (dtolist.size() > 0) {
				ds.addRanges(pt_code, dtolist);
			}

			try {
				smallTrans.newTransExecute(new SmallTransExecute() {
					public void doExecute() throws Exception {
						List list = new ArrayList();
						for (Object obj : mapTranfer.values()) {
							list.add(obj);
						}
						dao.executeBySql(
								"delete sys_transfer_ele where ele_code=?",
								new Object[] { ele_code });
						dao.executeBySql(
								"update  sys_transfer_base set is_transfer=1  where ele_code=? and rg_code =? and set_year = ? ",
								new Object[] { ele_code, rg_code, year });

						dao.batchSaveDataBySql("sys_transfer_ele", list);
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 导入业务处室
	 */
	public int transferDepartment(final String rg_code, final String year)
			throws SQLException {
		try {
			String admdiv = "";
			List datalist = dao.findBySql(
					"select * from FASP_T_PUBADMDIV where code=?",
					new Object[] { rg_code });
			if (datalist.size() > 0) {
				admdiv = (String) ((Map) datalist.get(0)).get("guid");
			}
			final String ele_code = "DEPARTMENT";
			String ele_table = "ele_manage_branch";
			String old_ele_code = "MB";
			String pt_code = "DEPARTMENT";
			DataSetRangeClient ds = (DataSetRangeClient) ServiceFactory
					.getBean("fasp2.dic.dataset.range.service");
			String sql = "select chr_id, replace(replace(replace(chr_id,'{',''),'}',''),'-','') as guid"
					+ ",chr_code as code ,chr_name as name ,1 as status,"
					+ year
					+ " as year,'"
					+ rg_code;
					if(TypeOfDB.isOracle()) {
						sql += "' as province,replace(replace(replace(nvl(parent_id,'0'),'{',''),'}',''),'-','') as superguid,is_leaf as isleaf, level_num as levelno,1 as version ,'支付系统导入' as remark ";
					} else if(TypeOfDB.isMySQL()) {
						sql += "' as province,replace(replace(replace(ifnull(parent_id,'0'),'{',''),'}',''),'-','') as superguid,is_leaf as isleaf, level_num as levelno,1 as version ,'支付系统导入' as remark ";
					}
					sql += ",(select chr_code from    "
					+ ele_table
					+ " where chr_id =ele.parent_id and rg_code=ele.rg_code and set_year=ele.set_year ) as super_code from "
					+ ele_table
					+ " ele where set_year="
					+ year
					+ " order by chr_code";

			List<DataSetRangeDTO> list;
			try {
				list = (List<DataSetRangeDTO>) ds.getAllLowerDsRanges(
						"FASP_T_PUB" + ele_code, "0");
			} catch (AppException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return 0;
			}
			List<Map> adddata = OldDataUtil.getData(sql);
			final Map mapTranfer = new HashMap();

			List<DataSetRangeDTO> dtolist = new ArrayList();
			for (int i = 0; i < adddata.size(); i++) {
				Map<String, Object> dtomap = (Map) adddata.get(i);
				DataSetRangeDTO dataDTO = new DataSetRangeDTO();
				dataDTO.putAll(dtomap);
				dataDTO.put("admdiv", admdiv);
				Map transferEle = new HashMap();
				transferEle.put("ele_code", "MB");
				transferEle.put("old_ele_code", old_ele_code);
				transferEle.put("old_id", dtomap.get("chr_id"));
				transferEle.put("new_id", dataDTO.getGuid());
				transferEle.put("old_code", dataDTO.getCode());
				transferEle.put("new_code", dataDTO.getCode());
				transferEle.put("old_name", dataDTO.getName());
				transferEle.put("new_name", dataDTO.getName());
				transferEle.put("rg_code", rg_code);
				transferEle.put("set_year", year);
				mapTranfer.put(dataDTO.getCode(), transferEle);
				// 是否平台已维护
				boolean isFasp = false;
				for (DataSetRangeDTO dto : list) {
					// 平台已经维护，将本系统的newid ，newname已经superid都要维护
					if (dto.getCode().equals(dataDTO.getCode())) {
						transferEle.put("new_id", dto.getGuid());
						transferEle.put("new_name", dto.getName());
						isFasp = true;
						break;
					}
				}
				if (!"0".equals(dataDTO.getSuperguid())) {
					dataDTO.setSuperguid((String) ((Map) mapTranfer.get(dtomap
							.get("super_code"))).get("new_id"));
				}
				if (!isFasp) {
					dtolist.add(dataDTO);
				}
			}

			if (dtolist.size() > 0) {
				ds.addRanges(pt_code, dtolist);
			}

			try {
				smallTrans.newTransExecute(new SmallTransExecute() {
					public void doExecute() throws Exception {
						List list = new ArrayList();
						for (Object obj : mapTranfer.values()) {
							list.add(obj);
						}
						dao.executeBySql(
								"delete sys_transfer_ele where ele_code=?",
								new Object[] { ele_code });
						dao.executeBySql(
								"update  sys_transfer_base set is_transfer=1  where ele_code=? and rg_code =? and =? set_year = ? ",
								new Object[] { ele_code, rg_code, year });

						dao.batchSaveDataBySql("sys_transfer_ele", list);
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/*
	 * 按第一级上一级编码是0 获取全部数据
	 */

	public List getFasp2EleDataByCode(String elementcode) {
		DataSetRangeClient ds = (DataSetRangeClient) ServiceFactory
				.getBean("fasp2.dic.dataset.range.service");
		List alldata = new ArrayList();
		try {
			List list = ds.getAllLowerDsRanges(elementcode, "0");
			alldata = list;

		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return alldata;

	}

	/**
	 * 导入单位
	 */
	public int transferAgency(final String rg_code, final String year)
			throws SQLException {
		try {
			String admdiv = "";
			List datalist = dao.findBySql(
					"select * from FASP_T_PUBADMDIV where code=?",
					new Object[] { rg_code });
			if (datalist.size() > 0) {
				admdiv = (String) ((Map) datalist.get(0)).get("guid");
			}
			final String ele_code = "AGENCY";
			String ele_table = "ele_enterprise";
			String old_ele_code = "EN";
			String pt_code = "AGENCY";
			IAgencyRangeService agencyRangeService = (IAgencyRangeService) ServiceFactory
					.getBean("fasp2.dic.agency.service");
			String sql = "select chr_id, replace(replace(replace(chr_id,'{',''),'}',''),'-','') as guid"
					+ ",chr_code as code ,chr_name as name ,1 as status,"
					+ year
					+ " as year,'"
					+ rg_code;
					if(TypeOfDB.isOracle()) {
						sql += "' as province,replace(replace(replace(nvl(parent_id,'0'),'{',''),'}',''),'-','') as superguid,is_leaf as isleaf, level_num as levelno,1 as version ,'支付系统导入' as remark ";
					} else if(TypeOfDB.isMySQL()) {
						sql += "' as province,replace(replace(replace(ifnull(parent_id,'0'),'{',''),'}',''),'-','') as superguid,is_leaf as isleaf, level_num as levelno,1 as version ,'支付系统导入' as remark ";
					}
					sql += ",(select chr_code from    "
					+ ele_table
					+ " where chr_id =ele.parent_id and rg_code=ele.rg_code and set_year=ele.set_year ) as super_code from "
					+ ele_table
					+ " ele where set_year="
					+ year
					+ " order by chr_code";

			List<DataSetRangeDTO> list;
			try {
				list = (List<DataSetRangeDTO>) agencyRangeService
						.getAllAgencys();
			} catch (AppException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return 0;
			}
			List<Map> adddata = OldDataUtil.getData(sql);
			final Map mapTranfer = new HashMap();

			List<DataSetRangeDTO> dtolist = new ArrayList();
			for (int i = 0; i < adddata.size(); i++) {
				Map<String, Object> dtomap = (Map) adddata.get(i);
				DataSetRangeDTO dataDTO = new DataSetRangeDTO();
				dataDTO.putAll(dtomap);
				dataDTO.put("admdiv", admdiv);
				Map transferEle = new HashMap();
				transferEle.put("ele_code", ele_code);
				transferEle.put("old_ele_code", old_ele_code);
				transferEle.put("old_id", dtomap.get("chr_id"));
				transferEle.put("new_id", dataDTO.getGuid());
				transferEle.put("old_code", dataDTO.getCode());
				transferEle.put("new_code", dataDTO.getCode());
				transferEle.put("old_name", dataDTO.getName());
				transferEle.put("new_name", dataDTO.getName());
				transferEle.put("rg_code", rg_code);
				transferEle.put("set_year", year);
				mapTranfer.put(dataDTO.getCode(), transferEle);
				// 是否平台已维护
				boolean isFasp = false;
				for (DataSetRangeDTO dto : list) {
					// 平台已经维护，将本系统的newid ，newname已经superid都要维护
					if (dto.getCode().equals(dataDTO.getCode())) {
						transferEle.put("new_id", dto.getGuid());
						transferEle.put("new_name", dto.getName());
						isFasp = true;
						break;
					}
				}
				if (!"0".equals(dataDTO.getSuperguid())) {
					dataDTO.setSuperguid((String) ((Map) mapTranfer.get(dtomap
							.get("super_code"))).get("new_id"));
				}
				if (!isFasp) {
					dtolist.add(dataDTO);
				}
			}

			if (dtolist.size() > 0) {

				agencyRangeService.addRanges(dtolist);
			}

			try {
				smallTrans.newTransExecute(new SmallTransExecute() {
					public void doExecute() throws Exception {
						List list = new ArrayList();
						for (Object obj : mapTranfer.values()) {
							list.add(obj);
						}
						dao.executeBySql(
								"delete sys_transfer_ele where ele_code=?",
								new Object[] { ele_code });
						dao.executeBySql(
								"update  sys_transfer_base set is_transfer=1  where ele_code=? and rg_code =? and set_year = ? ",
								new Object[] { ele_code, rg_code, year });

						dao.batchSaveDataBySql("sys_transfer_ele", list);
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

	/**
	 * 导入零余额
	 */
	public int transferAccount(final String ele_code, String pt_code,
			final String old_ele_code,final String rg_code, final String year) {
		try {
			List ele = dao
					.findBySql(
							"select * from sys_element where ele_code=? and rg_code = ? and set_year=?",
							new Object[] { ele_code, rg_code, year });
			if (ele.size() == 0) {
				System.out.println(ele_code + "没有在sys_element维护");
				return 0;
			}
			List divlist = dao.findBySql(
					"select * from FASP_T_PUBADMDIV where code=?",
					new Object[] { rg_code });
			String admdiv = "";
			if (divlist.size() > 0) {
				admdiv = (String) ((Map) divlist.get(0)).get("guid");
			}
			String ele_table = (String) ((Map) ele.get(0))
					.get("old_ele_source");

			DataSetRangeClient ds = (DataSetRangeClient) ServiceFactory
					.getBean("fasp2.dic.dataset.range.service");
			String sql = "select chr_id, replace(replace(replace(chr_id,'{',''),'}',''),'-','') as guid"
					+ ",chr_code as code ,chr_name as name ,1 as status,"
					+ year
					+ " as year,'"
					+ rg_code;
					if(TypeOfDB.isOracle()) {
						sql += "' as province,replace(replace(replace(nvl(parent_id,'0'),'{',''),'}',''),'-','') as superguid,is_leaf as isleaf, level_num as levelno,1 as version ,'支付系统导入' as remark ";
					} else if(TypeOfDB.isMySQL()) {
						sql += "' as province,replace(replace(replace(ifnull(parent_id,'0'),'{',''),'}',''),'-','') as superguid,is_leaf as isleaf, level_num as levelno,1 as version ,'支付系统导入' as remark ";
					}
					sql += ",(select chr_code from    "
					+ ele_table
					+ " where chr_id =ele.parent_id and rg_code=ele.rg_code and set_year=ele.set_year ) as super_code,account_type,account_no,account_name,enabled,replace(replace(replace(bank_id,'{',''),'}',''),'-','') bank,(SELECT replace(replace(replace(chr_id,'{',''),'}',''),'-','') FROM ELE_ENTERPRISE WHERE CHR_CODE = ele.OWNER_CODE and rg_code=ele.rg_code and set_year = ele.set_year and rownum=1) agency from "
					+ ele_table
					+ " ele where set_year="
					+ year
					+ " order by chr_code";

			List<DataSetRangeDTO> list;
			try {
				list = (List<DataSetRangeDTO>) ds.getAllLowerDsRanges(
						"FASP_T_PUB" + pt_code, "0");
			} catch (AppException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return 0;
			}
			List<Map> adddata = OldDataUtil.getData(sql);
			final Map mapTranfer = new HashMap();

			List<DataSetRangeDTO> dtolist = new ArrayList();
			for (int i = 0; i < adddata.size(); i++) {
				Map<String, Object> dtomap = (Map) adddata.get(i);
				DataSetRangeDTO dataDTO = new DataSetRangeDTO();
				dataDTO.putAll(dtomap);
				Map transferEle = new HashMap();
				transferEle.put("ele_code", ele_code);
				transferEle.put("old_ele_code", old_ele_code);
				transferEle.put("old_id", dtomap.get("chr_id"));
				transferEle.put("new_id", dataDTO.getGuid());
				transferEle.put("old_code", dataDTO.getCode());
				transferEle.put("new_code", dataDTO.getCode());
				transferEle.put("old_name", dataDTO.getName());
				transferEle.put("new_name", dataDTO.getName());
				transferEle.put("rg_code", rg_code);
				transferEle.put("set_year", year);
				mapTranfer.put(dataDTO.getCode(), transferEle);
				dataDTO.put("admdiv", admdiv);
				// 是否平台已维护
				boolean isFasp = false;
				for (DataSetRangeDTO dto : list) {
					// 平台已经维护，将本系统的newid ，newname已经superid都要维护
					if (dto.getCode().equals(dataDTO.getCode())) {
						transferEle.put("new_id", dto.getGuid());
						transferEle.put("new_name", dto.getName());
						isFasp = true;
						break;
					}
				}
				if (!"0".equals(dataDTO.getSuperguid())) {
					dataDTO.setSuperguid((String) ((Map) mapTranfer.get(dtomap
							.get("super_code"))).get("new_id"));
				}
				if (!isFasp) {
					dtolist.add(dataDTO);
				}
			}

			if (dtolist.size() > 0) {
				ds.addRanges(pt_code, dtolist);
			}

			try {
				smallTrans.newTransExecute(new SmallTransExecute() {
					public void doExecute() throws Exception {
						List list = new ArrayList();
						for (Object obj : mapTranfer.values()) {
							list.add(obj);
						}
						dao.executeBySql(
								"delete sys_transfer_ele where ele_code=?",
								new Object[] { ele_code });
						dao.executeBySql(
								"update  sys_transfer_base set is_transfer=1  where ele_code=? and rg_code =? and set_year = ? ",
								new Object[] { ele_code, rg_code, year });

						dao.batchSaveDataBySql("sys_transfer_ele", list);
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 导入银行
	 */
	public int transferBank(String rg_code, String year) throws SQLException {

		String admdiv = "";
		List datalist = dao.findBySql(
				"select * from FASP_T_PUBADMDIV where code=?",
				new Object[] { rg_code });
		if (datalist.size() > 0) {
			admdiv = (String) ((Map) datalist.get(0)).get("guid");
		}

		DataSetRangeClient ds = (DataSetRangeClient) ServiceFactory
				.getBean("fasp2.dic.dataset.range.service");
		String sql = null;
		if(TypeOfDB.isOracle()) {
			sql = "select chr_id,decode(AGENTFLAG,1,'PB','CB') ele_code,  decode(AGENTFLAG,1,'003','004') as banktype, replace(replace(replace(chr_id,'{',''),'}',''),'-','') as guid"
				+ ",chr_code as code ,chr_name as name ,1 as status,"
				+ year
				+ " as year,'"
				+ rg_code
				+ "' as province,replace(replace(replace(nvl(parent_id,'0'),'{',''),'}',''),'-','') as superguid,is_leaf as isleaf, level_num as levelno,1 as version ,'支付系统导入' as remark "
				+ " from ele_bank";
		} else if(TypeOfDB.isMySQL()) {
			sql = "select chr_id,case AGENTFLAG when 1 then 'PB' else 'CB' end as ele_code, case AGENTFLAG when 1 then '003' else '004' end as banktype, replace(replace(replace(chr_id,'{',''),'}',''),'-','') as guid"
					+ ",chr_code as code ,chr_name as name ,1 as status,"
					+ year
					+ " as year,'"
					+ rg_code
					+ "' as province,replace(replace(replace(ifnull(parent_id,'0'),'{',''),'}',''),'-','') as superguid,is_leaf as isleaf, level_num as levelno,1 as version ,'支付系统导入' as remark "
					+ " from ele_bank";
		}

		List<DataSetRangeDTO> typelist = new ArrayList();
		DataSetRangeDTO typeDTO = new DataSetRangeDTO();
		typelist.add(typeDTO);
		typeDTO.put("guid", "003");
		typeDTO.put("code", "003");
		typeDTO.put("name", "代理银行");
		typeDTO.put("status", "1");
		typeDTO.put("province", rg_code);
		typeDTO.put("year", year);
		typeDTO.put("levelno", "1");
		typeDTO.put("admdiv", admdiv);
		typeDTO = new DataSetRangeDTO();
		typelist.add(typeDTO);
		typeDTO.put("guid", "004");
		typeDTO.put("code", "004");
		typeDTO.put("name", "清算银行");
		typeDTO.put("status", "1");
		typeDTO.put("province", rg_code);
		typeDTO.put("year", year);
		typeDTO.put("levelno", "1");
		typeDTO.put("admdiv", admdiv);
		try {
			ds.addRanges("BANKTYPE", typelist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<DataSetRangeDTO> list;
		try {
			list = (List<DataSetRangeDTO>) ds.getAllLowerDsRanges("FASP_T_PUB"
					+ "BANK", "0");
		} catch (AppException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return 0;
		}
		List<Map> adddata = OldDataUtil.getData(sql);
		final Map mapTranfer = new HashMap();

		List<DataSetRangeDTO> dtolist = new ArrayList();
		for (int i = 0; i < adddata.size(); i++) {
			Map<String, Object> dtomap = (Map) adddata.get(i);
			DataSetRangeDTO dataDTO = new DataSetRangeDTO();
			dataDTO.putAll(dtomap);
			dataDTO.put("admdiv", admdiv);
			Map transferEle = new HashMap();
			transferEle.put("ele_code", dataDTO.get("ele_code"));
			transferEle.put("old_ele_code", dataDTO.get("ele_code"));
			transferEle.put("old_id", dtomap.get("chr_id"));
			transferEle.put("new_id", dataDTO.getGuid());
			transferEle.put("old_code", dataDTO.getCode());
			transferEle.put("new_code", dataDTO.getCode());
			transferEle.put("old_name", dataDTO.getName());
			transferEle.put("new_name", dataDTO.getName());
			transferEle.put("rg_code", rg_code);
			transferEle.put("set_year", year);
			mapTranfer.put(dataDTO.getCode(), transferEle);
			// 是否平台已维护
			boolean isFasp = false;
			for (DataSetRangeDTO dto : list) {
				// 平台已经维护，将本系统的newid ，newname已经superid都要维护
				if (dto.getCode().equals(dataDTO.getCode())) {
					transferEle.put("new_id", dto.getGuid());
					transferEle.put("new_name", dto.getName());
					isFasp = true;
					break;
				}
			}
			// if(!"0".equals(dataDTO.getSuperguid()))
			// {
			// dataDTO.setSuperguid((String)
			// ((Map)mapTranfer.get(dtomap.get("super_code"))).get("new_id"));
			// }
			if (!isFasp) {
				dtolist.add(dataDTO);
			}
		}

		try {
			if (dtolist.size() > 0) {
				ds.addRanges("BANK", dtolist);
			}
			dao.executeBySql("delete sys_transfer_ele where ele_code=?",
					new Object[] { "PB" });
			dao.executeBySql("delete sys_transfer_ele where ele_code=?",
					new Object[] { "CB" });
			List elelist = new ArrayList();
			for (Object obj : mapTranfer.values()) {
				elelist.add(obj);
			}
			dao.batchSaveDataBySql("sys_transfer_ele", elelist);

			dao.executeBySql(
					"update  sys_transfer_base set is_transfer=1  where ele_code=? and rg_code =? and set_year = ? ",
					new Object[] { "BANK", rg_code, year });

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dtolist.size();
	}

	@Override
	public Map getRgcodeSetyear() {
		Map map = new HashMap();
		map.put("rgcode", SessionUtil.getRgCode());
		map.put("setyear", SessionUtil.getLoginYear());
		return map;
	}

	public List getTransferEle(String rg_code, String year) {
		String sql = "select   ELE_CODE,OLD_CODE,PT_CODE,ELE_NAME,IS_TRANSFER,MSG,RG_CODE,SET_YEAR from sys_transfer_base where ((rg_code =? and set_year=?))  order by IS_TRANSFER,ele_code";

		return dao.findBySql(sql, new Object[] { rg_code, year });
	}

	public List getTransferBo(String rg_code, String year) {
		return dao.findBySql("select * from sys_transfer_bo");
	}

	public void transferBo(final String newtable, final String oldtable,
			String rg_code, String year) throws SQLException {
		List list = dao
				.findBySql("select ELE_CODE ,OLD_ELE_CODE,OLD_ID	,NEW_ID from sys_transfer_ele");
		HashMap<String, Map> eleIdMap = new HashMap<String, Map>();
		for (Map map : (List<Map>) list) {
			Map<String, String> m = new HashMap<String, String>();
			m.put(map.get("ele_code").toString().toLowerCase() + "_id",
					(String) map.get("new_id"));
			eleIdMap.put(map.get("old_ele_code").toString().toLowerCase()
					+ "_id" + map.get("old_id"), m);
		}

 		final List<Map> dtos = new ArrayList();
		List<Map> data = OldDataUtil.getData("select count(*) count from "
				+ oldtable);
		int count = Integer.parseInt(data.get(0).get("count").toString());
		for (int i = 0; i < count; i += 10000) {
			if(TypeOfDB.isOracle()) {
				data = OldDataUtil
						.getData("select * from  (select rownum row_,t.* from "
								+ oldtable + " t) where row_>" + i
								+ " and row_<=" + (i + 10000));
			} else if(TypeOfDB.isMySQL()) {
				data = OldDataUtil
						.getData("select * from " + oldtable + " limit" + i + ",10000");
			}
	 		for (Map map : data) {
				dtos.add(map);
				for (Object key : map.keySet().toArray()) {
					if (eleIdMap.containsKey(key)) {
						map.putAll(eleIdMap.get(key));
					}
				}
			}
		 
			try {
				smallTrans.newTransExecute(new SmallTransExecute() {
					public void doExecute() throws Exception {

						dao.batchSaveDataBySql(newtable, dtos);
						dtos.clear();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dao.executeBySql(
				"update  sys_transfer_right set is_transfer=1  where right_table=? and rg_code =? and set_year = ? ",
				new Object[] { newtable, rg_code, year });
	}

	public List<Map> getTransferRight(String rg_code, String year) {
		String sql = "select   * from sys_transfer_right where ((rg_code =? and set_year=?))  order by IS_TRANSFER,right_table ";

		return dao.findBySql(sql, new Object[] { rg_code, year });
	}

	public void transferRight(final String right_table, String right_sql,
			String is_same, String rg_code, String year) {
		String sql = "select * from " + right_sql;
		try {
			if ("1".equals(is_same)) {
				List<Map> list = OldDataUtil.getData(sql);
				for (Map map : list) {
					map.put("rg_code", rg_code);
					map.put("set_year", year);
				}
				dao.batchSaveDataBySql(right_table, list);

			} else {
				List list = dao
						.findBySql("select ELE_CODE ,OLD_ELE_CODE,OLD_ID	,NEW_ID from sys_transfer_ele");
				HashMap<String, Map> eleIdMap = new HashMap<String, Map>();
				for (Map map : (List<Map>) list) {
					Map<String, String> m = new HashMap<String, String>();
					m.put(map.get("ele_code").toString().toLowerCase() + "_id",
							(String) map.get("new_id"));
					eleIdMap.put(map.get("old_ele_code").toString()
							.toLowerCase()
							+ "_id" + map.get("old_id"), m);
				}

				List<Map> data = OldDataUtil
						.getData("select count(*) count from " + right_sql);
				int count = Integer.parseInt(data.get(0).get("count")
						.toString());
				for (int i = 0; i < count; i += 10000) {
					if(TypeOfDB.isOracle()) {
						data = OldDataUtil.getData("select * from  (select rownum row_,t.* from "
								+ right_sql
								+ " t) where row_>"
								+ i
								+ " and row_<=" + (i + 10000));
					} else if(TypeOfDB.isMySQL()) {
						data = OldDataUtil.getData("select * from " + right_sql + " limit " + i + ", 10000");
					}

					final List<Map<String, String>> dtos = new ArrayList();
					for (Map map : data) {
						dtos.add(map);
						for (Object key : map.keySet().toArray()) {
							if (eleIdMap.containsKey((String) key
									+ map.get(key))) {
								map.putAll(eleIdMap.get((String) key
										+ map.get(key)));
							}
						}
						map.put("rg_code", rg_code);
						map.put("set_year", year);

					}
					if (dtos.size() > 0) {
						try {
							smallTrans.newTransExecute(new SmallTransExecute() {
								public void doExecute() throws Exception {

									dao.batchSaveDataBySql(right_table, dtos);
									dtos.clear();
								}
							});
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dao.executeBySql(
				"update  sys_transfer_right set is_transfer=1  where right_table=? and rg_code =? and set_year = ? ",
				new Object[] { right_table, rg_code, year });
	}

	public void transferEleCode(final String table_name, String table_sql,
			String is_same, List<String> eleCode, String rg_code, String year) {
		try {
			String id = "";
			if ("3".equals(is_same)) {
				id = "_ID";

			}
			List list = dao
					.findBySql("select ELE_CODE ,OLD_ELE_CODE from sys_transfer_ele group by ele_code,old_ele_code");
			HashMap<String, String> eleIdMap = new HashMap<String, String>();
			for (Map map : (List<Map>) list) {
				eleIdMap.put((String) map.get("old_ele_code"),
						(String) map.get("ele_code") + id);
			}

			List<Map> data = OldDataUtil.getData("select count(*) count from "
					+ table_sql);
			int count = Integer.parseInt(data.get(0).get("count").toString());
			for (int i = 0; i < count; i += 10000) {
				if(TypeOfDB.isOracle()) {
					data = OldDataUtil
							.getData("select * from  (select rownum row_,t.* from "
									+ table_sql + " t) where row_>" + i
									+ " and row_<=" + (i + 10000));
				} else if(TypeOfDB.isMySQL()) {
					data = OldDataUtil.getData("select * from " + table_sql + " limit " + i + ",10000");
				}

				final List<Map<String, String>> dtos = new ArrayList();

				for (Map map : data) {
					dtos.add(map);

					for (String fieldName : eleCode) {
						if (map.containsKey(fieldName.toLowerCase())
								&& eleIdMap.containsKey(map.get(fieldName
										.toLowerCase() + id.toLowerCase()))) {

							map.put(fieldName,
									eleIdMap.get(map.get(fieldName
											.toLowerCase() + id.toLowerCase())));
						}
					}
					map.put("rg_code", rg_code);
					map.put("set_year", year);

				}
				if (dtos.size() > 0) {
					try {
						smallTrans.newTransExecute(new SmallTransExecute() {
							public void doExecute() throws Exception {

								dao.batchSaveDataBySql(table_name, dtos);
								dtos.clear();
							}
						});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dao.executeBySql(
				"update  sys_transfer_right set is_transfer=1  where right_table=? and rg_code =? and set_year = ? ",
				new Object[] { table_name, rg_code, year });
	}
	public int transferGlCcid(String tableName){
		return coa.checkTempTable(tableName);
	}
}
