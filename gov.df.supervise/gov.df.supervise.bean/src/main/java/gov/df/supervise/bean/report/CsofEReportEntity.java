package gov.df.supervise.bean.report;

public class CsofEReportEntity {
    /**
     * 报表id
     */
    private String chrId;

    /**
     * 报表编号
     */
    private String chrCode;

    /**
     * null
     */
    private String dispCode;

    /**
     * 报表名称
     */
    private String chrName;

    /**
     * 父级报表id
     */
    private String parentId;

    /**
     * 报表类型
     */
    private String reportType;

    /**
     * 报表文件（唯一报表标识）
     */
    private String reportFile;

    /**
     * 报表参数
     */
    private String reportParam;

    /**
     * 报表期间类型（1:年度，2:季度，3:月度，9:不定期）
     */
    private Short reportCycle;

    /**
     * 报表户要素编号
     */
    private String reportHolder;

    /**
     * 是否公共
     */
    private Short isPub;

    /**
     * 私有报表的所有者机构id
     */
    private String ownerOid;

    /**
     * 级次
     */
    private Short levelNum;

    /**
     * 是否底级
     */
    private Short isLeaf;

    /**
     * 是否已删除
     */
    private Short isDeleted;

    /**
     * 是否启用
     */
    private Short enabled;

    /**
     * 创建日期
     */
    private String createDate;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 最后操作日期
     */
    private String latestOpDate;

    /**
     * 最后操作人
     */
    private String latestOpUser;

    /**
     * 版本号
     */
    private String lastVer;

    /**
     * 年度
     */
    private Short setYear;

    /**
     * 区划
     */
    private String rgCode;

    /**
     * 报表id
     * @return CHR_ID 报表id
     */
    public String getChrId() {
        return chrId;
    }

    /**
     * 报表id
     * @param chrId 报表id
     */
    public void setChrId(String chrId) {
        this.chrId = chrId == null ? null : chrId.trim();
    }

    /**
     * 报表编号
     * @return CHR_CODE 报表编号
     */
    public String getChrCode() {
        return chrCode;
    }

    /**
     * 报表编号
     * @param chrCode 报表编号
     */
    public void setChrCode(String chrCode) {
        this.chrCode = chrCode == null ? null : chrCode.trim();
    }

    /**
     * null
     * @return DISP_CODE null
     */
    public String getDispCode() {
        return dispCode;
    }

    /**
     * null
     * @param dispCode null
     */
    public void setDispCode(String dispCode) {
        this.dispCode = dispCode == null ? null : dispCode.trim();
    }

    /**
     * 报表名称
     * @return CHR_NAME 报表名称
     */
    public String getChrName() {
        return chrName;
    }

    /**
     * 报表名称
     * @param chrName 报表名称
     */
    public void setChrName(String chrName) {
        this.chrName = chrName == null ? null : chrName.trim();
    }

    /**
     * 父级报表id
     * @return PARENT_ID 父级报表id
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 父级报表id
     * @param parentId 父级报表id
     */
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    /**
     * 报表类型
     * @return REPORT_TYPE 报表类型
     */
    public String getReportType() {
        return reportType;
    }

    /**
     * 报表类型
     * @param reportType 报表类型
     */
    public void setReportType(String reportType) {
        this.reportType = reportType == null ? null : reportType.trim();
    }

    /**
     * 报表文件（唯一报表标识）
     * @return REPORT_FILE 报表文件（唯一报表标识）
     */
    public String getReportFile() {
        return reportFile;
    }

    /**
     * 报表文件（唯一报表标识）
     * @param reportFile 报表文件（唯一报表标识）
     */
    public void setReportFile(String reportFile) {
        this.reportFile = reportFile == null ? null : reportFile.trim();
    }

    /**
     * 报表参数
     * @return REPORT_PARAM 报表参数
     */
    public String getReportParam() {
        return reportParam;
    }

    /**
     * 报表参数
     * @param reportParam 报表参数
     */
    public void setReportParam(String reportParam) {
        this.reportParam = reportParam == null ? null : reportParam.trim();
    }

    /**
     * 报表期间类型（1:年度，2:季度，3:月度，9:不定期）
     * @return REPORT_CYCLE 报表期间类型（1:年度，2:季度，3:月度，9:不定期）
     */
    public Short getReportCycle() {
        return reportCycle;
    }

    /**
     * 报表期间类型（1:年度，2:季度，3:月度，9:不定期）
     * @param reportCycle 报表期间类型（1:年度，2:季度，3:月度，9:不定期）
     */
    public void setReportCycle(Short reportCycle) {
        this.reportCycle = reportCycle;
    }

    /**
     * 报表户要素编号
     * @return REPORT_HOLDER 报表户要素编号
     */
    public String getReportHolder() {
        return reportHolder;
    }

    /**
     * 报表户要素编号
     * @param reportHolder 报表户要素编号
     */
    public void setReportHolder(String reportHolder) {
        this.reportHolder = reportHolder == null ? null : reportHolder.trim();
    }

    /**
     * 是否公共
     * @return IS_PUB 是否公共
     */
    public Short getIsPub() {
        return isPub;
    }

    /**
     * 是否公共
     * @param isPub 是否公共
     */
    public void setIsPub(Short isPub) {
        this.isPub = isPub;
    }

    /**
     * 私有报表的所有者机构id
     * @return OWNER_OID 私有报表的所有者机构id
     */
    public String getOwnerOid() {
        return ownerOid;
    }

    /**
     * 私有报表的所有者机构id
     * @param ownerOid 私有报表的所有者机构id
     */
    public void setOwnerOid(String ownerOid) {
        this.ownerOid = ownerOid == null ? null : ownerOid.trim();
    }

    /**
     * 级次
     * @return LEVEL_NUM 级次
     */
    public Short getLevelNum() {
        return levelNum;
    }

    /**
     * 级次
     * @param levelNum 级次
     */
    public void setLevelNum(Short levelNum) {
        this.levelNum = levelNum;
    }

    /**
     * 是否底级
     * @return IS_LEAF 是否底级
     */
    public Short getIsLeaf() {
        return isLeaf;
    }

    /**
     * 是否底级
     * @param isLeaf 是否底级
     */
    public void setIsLeaf(Short isLeaf) {
        this.isLeaf = isLeaf;
    }

    /**
     * 是否已删除
     * @return IS_DELETED 是否已删除
     */
    public Short getIsDeleted() {
        return isDeleted;
    }

    /**
     * 是否已删除
     * @param isDeleted 是否已删除
     */
    public void setIsDeleted(Short isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 是否启用
     * @return ENABLED 是否启用
     */
    public Short getEnabled() {
        return enabled;
    }

    /**
     * 是否启用
     * @param enabled 是否启用
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /**
     * 创建日期
     * @return CREATE_DATE 创建日期
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * 创建日期
     * @param createDate 创建日期
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    /**
     * 创建人
     * @return CREATE_USER 创建人
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 创建人
     * @param createUser 创建人
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * 最后操作日期
     * @return LATEST_OP_DATE 最后操作日期
     */
    public String getLatestOpDate() {
        return latestOpDate;
    }

    /**
     * 最后操作日期
     * @param latestOpDate 最后操作日期
     */
    public void setLatestOpDate(String latestOpDate) {
        this.latestOpDate = latestOpDate == null ? null : latestOpDate.trim();
    }

    /**
     * 最后操作人
     * @return LATEST_OP_USER 最后操作人
     */
    public String getLatestOpUser() {
        return latestOpUser;
    }

    /**
     * 最后操作人
     * @param latestOpUser 最后操作人
     */
    public void setLatestOpUser(String latestOpUser) {
        this.latestOpUser = latestOpUser == null ? null : latestOpUser.trim();
    }

    /**
     * 版本号
     * @return LAST_VER 版本号
     */
    public String getLastVer() {
        return lastVer;
    }

    /**
     * 版本号
     * @param lastVer 版本号
     */
    public void setLastVer(String lastVer) {
        this.lastVer = lastVer == null ? null : lastVer.trim();
    }

    /**
     * 年度
     * @return SET_YEAR 年度
     */
    public Short getSetYear() {
        return setYear;
    }

    /**
     * 年度
     * @param setYear 年度
     */
    public void setSetYear(Short setYear) {
        this.setYear = setYear;
    }

    /**
     * 区划
     * @return RG_CODE 区划
     */
    public String getRgCode() {
        return rgCode;
    }

    /**
     * 区划
     * @param rgCode 区划
     */
    public void setRgCode(String rgCode) {
        this.rgCode = rgCode == null ? null : rgCode.trim();
    }
}