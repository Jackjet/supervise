package gov.df.fap.bean.portal;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户信息传输对象
 */
public class UserDTODF implements Serializable {

	private static final long serialVersionUID = 1533563892871902669L;
	
	/**
	 * 用户ID（Guid格式）-- 同fasp2.0 guid
	 */
	private String user_id;
	
	/**
	 * 登陆用户唯一代号 -- 同fasp2.0 code
	 */
	private String user_code;
	
	/**
	 * 用户名称 -- 同fasp2.0 name
	 */
	private String user_name;
	
	/**
	 * 用户登录密码（MD5加密）-- 同fasp2.0 password
	 */
	private String password;
	
	/**
	 * 区划 -- 同fasp2.0 province
	 */
	private String rg_code;
	
	/**
	 * 机构类型，记录要素简码
	 */
	private String org_type;
	
	/**
	 * 保存对应所属机构基础数据的值 -- 同fasp2.0 agency
	 */
	private String org_code;
	
	/**
	 * 用户级别
	 */
	private Integer level_num;
	
	/**
	 * 是否叶节点：1 是，0 否
	 */
	private Integer is_leaf;
	
	/**
	 * 性别：1 男，2 女
	 */
	private Integer gender;
	
	/**
	 * 用户固定联系电话 -- 同fasp2.0 phonenumber
	 */
	private String telephone;
	
	/**
	 * 用户移动电话
	 */
	private String mobile;
	
	/**
	 * 是否启用：1 启用，0 不启用 -- 同fasp2.0 status
	 */
	private Integer enabled;
	
	/**
	 * 用户的职务，例如处长、科员等
	 */
	private String headship_code;
	
	/**
	 * 用户的出生日期，统计用
	 */
	private String birthday;
	
	/**
	 * 用户的家庭住址，可用于紧急联系
	 */
	private String address;
	
	/**
	 * 用户的电子邮件，未来可能用于向用户发通知 -- 同fasp2.0 email
	 */
	private String email;
	
	/**
	 * 用户类型：0 普通用户，1 系统管理员，2 超级用户，3 路由用户 -- 同fasp2.0 usertype
	 */
	private Integer user_type;
	
	/**
	 * 是否审核：1 是，0 否
	 */
	private Integer is_audit;
	
	/**
	 * 审核时间： YYYY-MM-DD HH:MM:SS
	 */
	private String audit_date;
	
	/**
	 * 审核用户
	 */
	private String audit_user;
	
	/**
	 * 用户昵称
	 */
	private String nickname;
	private String last_ver;
	private String mb_id;
	private String belong_org;
	private String belong_type;
	private Integer security_level;
	
	/**
	 * xx -- 同fasp2.0 initialpassword
	 */
	private Integer init_password;
	private String gp_org;
	private String payment_password;
	private String title_tech;
	
	/**
	 * xx -- 同fasp2.0 idcode
	 */
	private String identity_card;
	private String rtxuin;
	private String photo;
	private String photo_blobid;
	private String ca_serial;
	private String co_name;
	private Integer emp_index;
	
	/**
	 * 年度 -- 同fasp2.0 year
	 */
	private String set_year;
	
	/**
	 * 是否修改过密码
	 */
	private Integer is_edit_pwd;
	
	/**
	 * 是否启用三级安全
	 */
	private Integer is_three_security;
	
	/**
	 * 是否永久锁定
	 */
	private Integer is_ever_locked;
	
	/**
	 * 自动锁定日期
	 */
	private String locked_date;
	
	/**
	 * 国际移动用户识别码(与SIM卡绑定)
	 */
	private String imsi;
	
	/**
	 * 移动设备国际身份码(与移动设备绑定)
	 */
	private String imei;
	
	/**
	 * 微信号
	 */
	private String weixin;
	
	/**
	 * 是否黑名单：1 是，0 否
	 */
	private String is_blacklist;

	public UserDTODF() {
		super();
	}

	public UserDTODF(String user_id, String user_code, String user_name, String password) {
		super();
		this.user_id = user_id;
		this.user_code = user_code;
		this.user_name = user_name;
		this.password = password;
	}
	
	//TODO 完成构造
  public UserDTODF(Map map) {
    this.address = (String) isValidOrNull(map.get("address"));                       
    this.audit_date = (String) isValidOrNull(map.get("audit_date"));                 
    this.audit_user = (String) isValidOrNull(map.get("audit_user"));                 
    this.belong_org = (String) isValidOrNull(map.get("belong_org"));                 
    this.belong_type = (String) isValidOrNull(map.get("belong_type"));               
    this.birthday = (String) isValidOrNull(map.get("birthday"));                     
    this.ca_serial = (String) isValidOrNull(map.get("ca_serial"));                   
    this.co_name = (String) isValidOrNull(map.get("co_name"));                       
    this.email = (String) isValidOrNull(map.get("email"));                           
    //this.emp_index = (Integer) isValidOrNull(map.get("emp_index"));                   
    //this.enabled = (Integer) isValidOrNull(map.get("enabled"));                       
    //this.gender = (Integer) isValidOrNull(map.get("gender"));                         
    this.gp_org = (String) isValidOrNull(map.get("gp_org"));                         
    this.headship_code = (String) isValidOrNull(map.get("headship_code"));           
    this.identity_card = (String) isValidOrNull(map.get("identity_card"));           
    this.imei = (String) isValidOrNull(map.get("imei"));                             
    this.imsi = (String) isValidOrNull(map.get("imsi"));                             
    //this.init_password = (Integer) isValidOrNull(map.get("init_password"));           
    //this.is_audit = (Integer) isValidOrNull(map.get("is_audit"));                     
    this.is_blacklist = (String) isValidOrNull(map.get("is_blacklist"));             
    //this.is_edit_pwd = (Integer) isValidOrNull(map.get("is_edit_pwd"));               
    //this.is_ever_locked = (Integer) isValidOrNull(map.get("is_ever_locked"));         
    //this.is_leaf = (Integer) isValidOrNull(map.get("is_leaf"));                       
    //this.is_three_security = (Integer) isValidOrNull(map.get("is_three_security"));   
    this.last_ver = (String) isValidOrNull(map.get("last_ver"));                     
    //this.level_num = (Integer) isValidOrNull(map.get("level_num"));                   
    this.locked_date = (String) isValidOrNull(map.get("locked_date"));               
    this.mb_id = (String) isValidOrNull(map.get("mb_id"));                           
    this.mobile = (String) isValidOrNull(map.get("mobile"));                         
    this.nickname = (String) isValidOrNull(map.get("nickname"));                     
    this.org_code = (String) isValidOrNull(map.get("org_code"));                     
    this.org_type = (String) isValidOrNull(map.get("org_type"));                     
    this.password = (String) isValidOrNull(map.get("password"));                     
    this.payment_password = (String) isValidOrNull(map.get("payment_password"));     
    this.photo = (String) isValidOrNull(map.get("photo"));                           
    this.photo_blobid = (String) isValidOrNull(map.get("photo_blobid"));             
    this.rg_code = (String) isValidOrNull(map.get("rg_code"));                       
    this.rtxuin = (String) isValidOrNull(map.get("rtxuin"));                         
    //this.security_level = (Integer) isValidOrNull(map.get("security_level"));         
    this.set_year = (String) isValidOrNull(map.get("set_year"));                     
    this.telephone = (String) isValidOrNull(map.get("telephone"));                   
    this.title_tech = (String) isValidOrNull(map.get("title_tech"));                 
    this.user_code = (String) isValidOrNull(map.get("user_code"));                   
    this.user_id = (String) isValidOrNull(map.get("user_id"));                       
    this.user_name = (String) isValidOrNull(map.get("user_name"));                   
    //this.user_type = (Integer) isValidOrNull(map.get("user_type"));                   
    this.weixin = (String) isValidOrNull(map.get("weixin"));                         
	  
	}
  
  static Object isValidOrNull(Object obj) {
    return obj==null ? null : obj;
  }

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_code() {
		return user_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOrg_type() {
		return org_type;
	}

	public void setOrg_type(String org_type) {
		this.org_type = org_type;
	}

	public String getOrg_code() {
		return org_code;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public Integer getLevel_num() {
		return level_num;
	}

	public void setLevel_num(Integer level_num) {
		this.level_num = level_num;
	}

	public Integer getIs_leaf() {
		return is_leaf;
	}

	public void setIs_leaf(Integer is_leaf) {
		this.is_leaf = is_leaf;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getHeadship_code() {
		return headship_code;
	}

	public void setHeadship_code(String headship_code) {
		this.headship_code = headship_code;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUser_type() {
		return user_type;
	}

	public void setUser_type(Integer user_type) {
		this.user_type = user_type;
	}

	public Integer getIs_audit() {
		return is_audit;
	}

	public void setIs_audit(Integer is_audit) {
		this.is_audit = is_audit;
	}

	public String getAudit_date() {
		return audit_date;
	}

	public void setAudit_date(String audit_date) {
		this.audit_date = audit_date;
	}

	public String getAudit_user() {
		return audit_user;
	}

	public void setAudit_user(String audit_user) {
		this.audit_user = audit_user;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLast_ver() {
		return last_ver;
	}

	public void setLast_ver(String last_ver) {
		this.last_ver = last_ver;
	}

	public String getMb_id() {
		return mb_id;
	}

	public void setMb_id(String mb_id) {
		this.mb_id = mb_id;
	}

	public String getBelong_org() {
		return belong_org;
	}

	public void setBelong_org(String belong_org) {
		this.belong_org = belong_org;
	}

	public String getBelong_type() {
		return belong_type;
	}

	public void setBelong_type(String belong_type) {
		this.belong_type = belong_type;
	}

	public Integer getSecurity_level() {
		return security_level;
	}

	public void setSecurity_level(Integer security_level) {
		this.security_level = security_level;
	}

	public Integer getInit_password() {
		return init_password;
	}

	public void setInit_password(Integer init_password) {
		this.init_password = init_password;
	}

	public String getGp_org() {
		return gp_org;
	}

	public void setGp_org(String gp_org) {
		this.gp_org = gp_org;
	}

	public String getPayment_password() {
		return payment_password;
	}

	public void setPayment_password(String payment_password) {
		this.payment_password = payment_password;
	}

	public String getTitle_tech() {
		return title_tech;
	}

	public void setTitle_tech(String title_tech) {
		this.title_tech = title_tech;
	}

	public String getIdentity_card() {
		return identity_card;
	}

	public void setIdentity_card(String identity_card) {
		this.identity_card = identity_card;
	}

	public String getRtxuin() {
		return rtxuin;
	}

	public void setRtxuin(String rtxuin) {
		this.rtxuin = rtxuin;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhoto_blobid() {
		return photo_blobid;
	}

	public void setPhoto_blobid(String photo_blobid) {
		this.photo_blobid = photo_blobid;
	}

	public String getCa_serial() {
		return ca_serial;
	}

	public void setCa_serial(String ca_serial) {
		this.ca_serial = ca_serial;
	}

	public String getCo_name() {
		return co_name;
	}

	public void setCo_name(String co_name) {
		this.co_name = co_name;
	}

	public Integer getEmp_index() {
		return emp_index;
	}

	public void setEmp_index(Integer emp_index) {
		this.emp_index = emp_index;
	}

	public String getSet_year() {
		return set_year;
	}

	public void setSet_year(String set_year) {
		this.set_year = set_year;
	}

	public Integer getIs_edit_pwd() {
		return is_edit_pwd;
	}

	public void setIs_edit_pwd(Integer is_edit_pwd) {
		this.is_edit_pwd = is_edit_pwd;
	}

	public Integer getIs_three_security() {
		return is_three_security;
	}

	public void setIs_three_security(Integer is_three_security) {
		this.is_three_security = is_three_security;
	}

	public Integer getIs_ever_locked() {
		return is_ever_locked;
	}

	public void setIs_ever_locked(Integer is_ever_locked) {
		this.is_ever_locked = is_ever_locked;
	}

	public String getLocked_date() {
		return locked_date;
	}

	public void setLocked_date(String locked_date) {
		this.locked_date = locked_date;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getIs_blacklist() {
		return is_blacklist;
	}

	public void setIs_blacklist(String is_blacklist) {
		this.is_blacklist = is_blacklist;
	}

  public String getRg_code() {
    return rg_code;
  }

  public void setRg_code(String rg_code) {
    this.rg_code = rg_code;
  }

  @Override
  public String toString() {
    return "UserDTODF [user_id=" + user_id + ", user_code=" + user_code
      + ", user_name=" + user_name + ", password=" + password + ", rg_code="
      + rg_code + ", org_type=" + org_type + ", org_code=" + org_code
      + ", level_num=" + level_num + ", is_leaf=" + is_leaf + ", gender=" + gender
      + ", telephone=" + telephone + ", mobile=" + mobile + ", enabled=" + enabled
      + ", headship_code=" + headship_code + ", birthday=" + birthday + ", address="
      + address + ", email=" + email + ", user_type=" + user_type + ", is_audit="
      + is_audit + ", audit_date=" + audit_date + ", audit_user=" + audit_user
      + ", nickname=" + nickname + ", last_ver=" + last_ver + ", mb_id=" + mb_id
      + ", belong_org=" + belong_org + ", belong_type=" + belong_type
      + ", security_level=" + security_level + ", init_password=" + init_password
      + ", gp_org=" + gp_org + ", payment_password=" + payment_password
      + ", title_tech=" + title_tech + ", identity_card=" + identity_card
      + ", rtxuin=" + rtxuin + ", photo=" + photo + ", photo_blobid=" + photo_blobid
      + ", ca_serial=" + ca_serial + ", co_name=" + co_name + ", emp_index="
      + emp_index + ", set_year=" + set_year + ", is_edit_pwd=" + is_edit_pwd
      + ", is_three_security=" + is_three_security + ", is_ever_locked="
      + is_ever_locked + ", locked_date=" + locked_date + ", imsi=" + imsi
      + ", imei=" + imei + ", weixin=" + weixin + ", is_blacklist=" + is_blacklist
      + "]";
  }

}
