package gov.df.supervise.bean.task;

import java.math.BigDecimal;

public class CsofTaskDep {
    /**
     * 处室任务id (外部dep_task_id)
     */
    private String id;

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
     * 专员办id
     */
    private String oid;

    /**
     * 实际监管数量
     */
    private String supNum;

    /**
     * 实际监管资金
     */
    private BigDecimal supMoney;

    /**
     * 计划人天成本
     */
    private Long planCost;

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
     * 处室备注
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
     * 批复用户
     */
    private String approveUser;

    /**
     * 批复日期
     */
    private String approveDate;

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
     * 处室任务id (外部dep_task_id)
     * @return ID 处室任务id (外部dep_task_id)
     */
    public String getId() {
        return id;
    }

    /**
     * 处室任务id (外部dep_task_id)
     * @param id 处室任务id (外部dep_task_id)
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 监管事项id
     * @return SUP_ID 监管事项id
     */
    public String getsid() {
        return sid;
    }

    /**
     * 监管事项id
     * @param supId 监管事项id
     */
    public void setsid(String sid) {
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
    public String getSupNum() {
        return supNum;
    }

    /**
     * 实际监管数量
     * @param num 实际监管数量
     */
    public void setSupNum(String num) {
        this.supNum = num;
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
     * 计划人天成本
     * @return PLAN_COST 计划人天成本
     */
    public Long getPlanCost() {
        return planCost;
    }

    /**
     * 计划人天成本
     * @param planCost 计划人天成本
     */
    public void setPlanCost(Long planCost) {
        this.planCost = planCost;
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
     * 处室备注
     * @return REMARK 处室备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 处室备注
     * @param remark 处室备注
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
     * 批复用户
     * @return APPROVE_USER 批复用户
     */
    public String getApproveUser() {
        return approveUser;
    }

    /**
     * 批复用户
     * @param approveUser 批复用户
     */
    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser == null ? null : approveUser.trim();
    }

    /**
     * 批复日期
     * @return APPROVE_DATE 批复日期
     */
    public String getApproveDate() {
        return approveDate;
    }

    /**
     * 批复日期
     * @param approveDate 批复日期
     */
    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate == null ? null : approveDate.trim();
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
}