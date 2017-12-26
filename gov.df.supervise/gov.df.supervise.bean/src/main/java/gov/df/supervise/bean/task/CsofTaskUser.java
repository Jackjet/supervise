package gov.df.supervise.bean.task;

import java.math.BigDecimal;

public class CsofTaskUser {
    /**
     * 执行任务id
     */
    private String id;

    /**
     * 处室任务id
     */
    private String depTaskId;

    /**
     * 监管任务编号
     */
    private String taskNo;

    /**
     * 监管任务名称
     */
    private String taskName;

    /**
     * 监管事项id
     */
    private String sid;

    /**
     * 监管事项编号
     */
    private String supNo;

    /**
     * 监管事项名称
     */
    private String supName;

    /**
     * 监管方式
     */
    private Short supMode;

    /**
     * 处室id
     */
    private String depId;

    /**
     * 处室编号
     */
    private String depCode;

    /**
     * 处室名称
     */
    private String depName;

    /**
     * 执行人id
     */
    private String userId;

    /**
     * 执行人编号
     */
    private String userCode;

    /**
     * 执行人名称
     */
    private String userName;

    /**
     * 专员办id
     */
    private String oid;

    /**
     * 实际监管数量
     */
    private Long supNum;

    /**
     * 实际监管资金
     */
    private BigDecimal supMoney;

    /**
     * 实际人天成本
     */
    private Long supCost;

    /**
     * 计划开始日期
     */
    private String planBeginDate;

    /**
     * 计划完成日期
     */
    private String planEndDate;

    /**
     * 实际开始日期
     */
    private String beginDate;

    /**
     * 实际完成日期
     */
    private String endDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（备用）
     */
    private String status;

    /**
     * 是否发布（0:未发布 1:已发布）
     */
    private Short isPub;

    /**
     * 是否有效（0:作废的 1:有效的）
     */
    private Short isValid;

    /**
     * 是否流程结束（0:未开始流程 1:在途 9:结束）
     */
    private Short isEnd;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建日期
     */
    private String createDate;

    /**
     * 发布用户
     */
    private String publishUser;

    /**
     * 发布日期
     */
    private String publishDate;

    /**
     * 完成用户
     */
    private String finishUser;

    /**
     * 完成日期
     */
    private String finishDate;

    /**
     * 最后操作用户
     */
    private String latestOpUser;

    /**
     * 最后操作日期
     */
    private String latestOpDate;

    /**
     * 单据类型编号
     */
    private String billtypeCode;

    /**
     * 单据类型名称
     */
    private String billtypeName;

    /**
     * 区划
     */
    private String rgCode;

    /**
     * 年度
     */
    private String setYear;

    /**
     * 监管期间类型（1:年报、2:季报、3:月报、4:旬报、5:周报、6:日报、7:半年报、8:半月报、0:不定期）
     */
    private String supCycle;

    /**
     * 监管数据期间
     */
    private String supDate;

    /**
     * 执行任务id
     * @return ID 执行任务id
     */
    public String getId() {
        return id;
    }

    /**
     * 执行任务id
     * @param id 执行任务id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 处室任务id
     * @return DEP_TASK_ID 处室任务id
     */
    public String getDepTaskId() {
        return depTaskId;
    }

    /**
     * 处室任务id
     * @param depTaskId 处室任务id
     */
    public void setDepTaskId(String depTaskId) {
        this.depTaskId = depTaskId == null ? null : depTaskId.trim();
    }

    /**
     * 监管任务编号
     * @return TASK_NO 监管任务编号
     */
    public String getTaskNo() {
        return taskNo;
    }

    /**
     * 监管任务编号
     * @param taskNo 监管任务编号
     */
    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo == null ? null : taskNo.trim();
    }

    /**
     * 监管任务名称
     * @return TASK_NAME 监管任务名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 监管任务名称
     * @param taskName 监管任务名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    /**
     * 监管事项id
     * @return SID 监管事项id
     */
    public String getSid() {
        return sid;
    }

    /**
     * 监管事项id
     * @param sid 监管事项id
     */
    public void setSid(String sid) {
        this.sid = sid == null ? null : sid.trim();
    }

    /**
     * 监管事项编号
     * @return SUP_NO 监管事项编号
     */
    public String getSupNo() {
        return supNo;
    }

    /**
     * 监管事项编号
     * @param supNo 监管事项编号
     */
    public void setSupNo(String supNo) {
        this.supNo = supNo == null ? null : supNo.trim();
    }

    /**
     * 监管事项名称
     * @return SUP_NAME 监管事项名称
     */
    public String getSupName() {
        return supName;
    }

    /**
     * 监管事项名称
     * @param supName 监管事项名称
     */
    public void setSupName(String supName) {
        this.supName = supName == null ? null : supName.trim();
    }

    /**
     * 监管方式
     * @return SUP_MODE 监管方式
     */
    public Short getSupMode() {
        return supMode;
    }

    /**
     * 监管方式
     * @param supMode 监管方式
     */
    public void setSupMode(Short supMode) {
        this.supMode = supMode;
    }

    /**
     * 处室id
     * @return DEP_ID 处室id
     */
    public String getDepId() {
        return depId;
    }

    /**
     * 处室id
     * @param depId 处室id
     */
    public void setDepId(String depId) {
        this.depId = depId == null ? null : depId.trim();
    }

    /**
     * 处室编号
     * @return DEP_CODE 处室编号
     */
    public String getDepCode() {
        return depCode;
    }

    /**
     * 处室编号
     * @param depCode 处室编号
     */
    public void setDepCode(String depCode) {
        this.depCode = depCode == null ? null : depCode.trim();
    }

    /**
     * 处室名称
     * @return DEP_NAME 处室名称
     */
    public String getDepName() {
        return depName;
    }

    /**
     * 处室名称
     * @param depName 处室名称
     */
    public void setDepName(String depName) {
        this.depName = depName == null ? null : depName.trim();
    }

    /**
     * 执行人id
     * @return USER_ID 执行人id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 执行人id
     * @param userId 执行人id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 执行人编号
     * @return USER_CODE 执行人编号
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * 执行人编号
     * @param userCode 执行人编号
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    /**
     * 执行人名称
     * @return USER_NAME 执行人名称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 执行人名称
     * @param userName 执行人名称
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 专员办id
     * @return OID 专员办id
     */
    public String getOid() {
        return oid;
    }

    /**
     * 专员办id
     * @param oid 专员办id
     */
    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
    }

    /**
     * 实际监管数量
     * @return SUP_NUM 实际监管数量
     */
    public Long getSupNum() {
        return supNum;
    }

    /**
     * 实际监管数量
     * @param supNum 实际监管数量
     */
    public void setSupNum(Long supNum) {
        this.supNum = supNum;
    }

    /**
     * 实际监管资金
     * @return SUP_MONEY 实际监管资金
     */
    public BigDecimal getSupMoney() {
        return supMoney;
    }

    /**
     * 实际监管资金
     * @param supMoney 实际监管资金
     */
    public void setSupMoney(BigDecimal supMoney) {
        this.supMoney = supMoney;
    }

    /**
     * 实际人天成本
     * @return SUP_COST 实际人天成本
     */
    public Long getSupCost() {
        return supCost;
    }

    /**
     * 实际人天成本
     * @param supCost 实际人天成本
     */
    public void setSupCost(Long supCost) {
        this.supCost = supCost;
    }

    /**
     * 计划开始日期
     * @return PLAN_BEGIN_DATE 计划开始日期
     */
    public String getPlanBeginDate() {
        return planBeginDate;
    }

    /**
     * 计划开始日期
     * @param planBeginDate 计划开始日期
     */
    public void setPlanBeginDate(String planBeginDate) {
        this.planBeginDate = planBeginDate == null ? null : planBeginDate.trim();
    }

    /**
     * 计划完成日期
     * @return PLAN_END_DATE 计划完成日期
     */
    public String getPlanEndDate() {
        return planEndDate;
    }

    /**
     * 计划完成日期
     * @param planEndDate 计划完成日期
     */
    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate == null ? null : planEndDate.trim();
    }

    /**
     * 实际开始日期
     * @return BEGIN_DATE 实际开始日期
     */
    public String getBeginDate() {
        return beginDate;
    }

    /**
     * 实际开始日期
     * @param beginDate 实际开始日期
     */
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate == null ? null : beginDate.trim();
    }

    /**
     * 实际完成日期
     * @return END_DATE 实际完成日期
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 实际完成日期
     * @param endDate 实际完成日期
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    /**
     * 备注
     * @return REMARK 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 状态（备用）
     * @return STATUS 状态（备用）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 状态（备用）
     * @param status 状态（备用）
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 是否发布（0:未发布 1:已发布）
     * @return IS_PUB 是否发布（0:未发布 1:已发布）
     */
    public Short getIsPub() {
        return isPub;
    }

    /**
     * 是否发布（0:未发布 1:已发布）
     * @param isPub 是否发布（0:未发布 1:已发布）
     */
    public void setIsPub(Short isPub) {
        this.isPub = isPub;
    }

    /**
     * 是否有效（0:作废的 1:有效的）
     * @return IS_VALID 是否有效（0:作废的 1:有效的）
     */
    public Short getIsValid() {
        return isValid;
    }

    /**
     * 是否有效（0:作废的 1:有效的）
     * @param isValid 是否有效（0:作废的 1:有效的）
     */
    public void setIsValid(Short isValid) {
        this.isValid = isValid;
    }

    /**
     * 是否流程结束（0:未开始流程 1:在途 9:结束）
     * @return IS_END 是否流程结束（0:未开始流程 1:在途 9:结束）
     */
    public Short getIsEnd() {
        return isEnd;
    }

    /**
     * 是否流程结束（0:未开始流程 1:在途 9:结束）
     * @param isEnd 是否流程结束（0:未开始流程 1:在途 9:结束）
     */
    public void setIsEnd(Short isEnd) {
        this.isEnd = isEnd;
    }

    /**
     * 创建用户
     * @return CREATE_USER 创建用户
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 创建用户
     * @param createUser 创建用户
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
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
     * 发布用户
     * @return PUBLISH_USER 发布用户
     */
    public String getPublishUser() {
        return publishUser;
    }

    /**
     * 发布用户
     * @param publishUser 发布用户
     */
    public void setPublishUser(String publishUser) {
        this.publishUser = publishUser == null ? null : publishUser.trim();
    }

    /**
     * 发布日期
     * @return PUBLISH_DATE 发布日期
     */
    public String getPublishDate() {
        return publishDate;
    }

    /**
     * 发布日期
     * @param publishDate 发布日期
     */
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate == null ? null : publishDate.trim();
    }

    /**
     * 完成用户
     * @return FINISH_USER 完成用户
     */
    public String getFinishUser() {
        return finishUser;
    }

    /**
     * 完成用户
     * @param finishUser 完成用户
     */
    public void setFinishUser(String finishUser) {
        this.finishUser = finishUser == null ? null : finishUser.trim();
    }

    /**
     * 完成日期
     * @return FINISH_DATE 完成日期
     */
    public String getFinishDate() {
        return finishDate;
    }

    /**
     * 完成日期
     * @param finishDate 完成日期
     */
    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate == null ? null : finishDate.trim();
    }

    /**
     * 最后操作用户
     * @return LATEST_OP_USER 最后操作用户
     */
    public String getLatestOpUser() {
        return latestOpUser;
    }

    /**
     * 最后操作用户
     * @param latestOpUser 最后操作用户
     */
    public void setLatestOpUser(String latestOpUser) {
        this.latestOpUser = latestOpUser == null ? null : latestOpUser.trim();
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
     * 单据类型编号
     * @return BILLTYPE_CODE 单据类型编号
     */
    public String getBilltypeCode() {
        return billtypeCode;
    }

    /**
     * 单据类型编号
     * @param billtypeCode 单据类型编号
     */
    public void setBilltypeCode(String billtypeCode) {
        this.billtypeCode = billtypeCode == null ? null : billtypeCode.trim();
    }

    /**
     * 单据类型名称
     * @return BILLTYPE_NAME 单据类型名称
     */
    public String getBilltypeName() {
        return billtypeName;
    }

    /**
     * 单据类型名称
     * @param billtypeName 单据类型名称
     */
    public void setBilltypeName(String billtypeName) {
        this.billtypeName = billtypeName == null ? null : billtypeName.trim();
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

    /**
     * 年度
     * @return SET_YEAR 年度
     */
    public String getSetYear() {
        return setYear;
    }

    /**
     * 年度
     * @param setYear 年度
     */
    public void setSetYear(String setYear) {
        this.setYear = setYear == null ? null : setYear.trim();
    }

    /**
     * 监管期间类型（1:年报、2:季报、3:月报、4:旬报、5:周报、6:日报、7:半年报、8:半月报、0:不定期）
     * @return SUP_CYCLE 监管期间类型（1:年报、2:季报、3:月报、4:旬报、5:周报、6:日报、7:半年报、8:半月报、0:不定期）
     */
    public String getSupCycle() {
        return supCycle;
    }

    /**
     * 监管期间类型（1:年报、2:季报、3:月报、4:旬报、5:周报、6:日报、7:半年报、8:半月报、0:不定期）
     * @param supCycle 监管期间类型（1:年报、2:季报、3:月报、4:旬报、5:周报、6:日报、7:半年报、8:半月报、0:不定期）
     */
    public void setSupCycle(String supCycle) {
        this.supCycle = supCycle == null ? null : supCycle.trim();
    }

    /**
     * 监管数据期间
     * @return SUP_DATE 监管数据期间
     */
    public String getSupDate() {
        return supDate;
    }

    /**
     * 监管数据期间
     * @param supDate 监管数据期间
     */
    public void setSupDate(String supDate) {
        this.supDate = supDate == null ? null : supDate.trim();
    }
}