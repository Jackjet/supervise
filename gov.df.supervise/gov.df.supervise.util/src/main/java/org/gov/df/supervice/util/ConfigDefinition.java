package org.gov.df.supervice.util;

/**
 * <p>
 * Title:配置参数
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: 北京用友政务软件有限公司
 * </p>
 * 
 * @author hlf
 * @see
 * @CreateData 2006-6-2
 * @version 1.0
 */
public interface ConfigDefinition {

  /**
   * 授权支付退款凭证单参数名
   */
  public static final String COMMON_BILLTYPE_ACCREDIT_REFUND_BILL = "common.billtype.accreditRefundBill";

  /**
   * 直接支付退款凭证单参数名
   */
  public static final String COMMON_BILLTYPE_DIRECT_REFUND_BILL = "common.billtype.directRefundBill";

  /**
   * 托收支付单据类型参数名
   */
  public static final String COMMON_BILLTYPE_CARE_PAY_BILL = "common.billtype.carePayBill";

  /**
   * 计划录入保存送审按钮是否显示标识（1：显示，0：不显示）
   */
  public static final String PLAN_INPUT_SAVEANDCHECKBTN = "plan.input.saveAndCheckBtn";

  /**
   * 收支管理要素决定计划编制方式的显示方式 TRUE FALSE
   */
  public static final String PLAN_ISINPMREFER = "plan.isInpmRefer";

  /**
   * 是否右键显示预算执行报表
   */
  public static final String COMMON_MENUSHOWBUDGETREPORT = "common.menuShowBudgetReport";

  /**
   * 预算执行报表CPT
   */
  public static final String COMMON_PAYEXECUTINGREPORT = "common.payExecutingReport";

  /**
   * 是否允许普通退款功能录入公务卡退款
   */
  public static final String PAY_REFUND_ALLOWNORREFUNDFORPAYCARD = "pay.refund.allowNorlRefundForPayCard";

  /**
   *  中央快速录入origin_model标志
   */
  public static final String PAY_QUICKINPUTMODEL = "pay.quickInputModel";

  /**
   *  托收支付申请所用计划额度的过滤要素
   */
  public static final String PAY_CONSIGN_SUMFILTELES = "pay.consign.sumFiltElEs";

  /**
   *  托收是否设置支付日期 create by wangwei 区分是否设置支付日期
   */
  public static final String ISOR_PAY_DATE_FORCONSIGN = "isorSetPayDateForConsign";

  /**
   *  托收额度导出对应CCID字段
   */
  public static final String PAY_CONSIGN_CONSIGNVALUE = "pay.consign.consignValue";

  /**
   *  托收支付录入默认值规则（批量录入规则）
   */
  public static final String PAY_CONSIGN_CONSIGNDEFAULTVALUE = "pay.consign.consignDefaultValue";

  /**
   * 公务卡银联IP
   */
  public static final String PAYCARD_PAYCARD_BANKIP = "paycard.paycard_bankip";

  /**
   * 公务卡银联端口号，用于消费明细的下载
   */
  public static final String PAYCARD_PAYCARD_BANKPORT = "paycard.paycard_bankport";

  /**
   * 公务卡服务平台是否启用
   */
  public static final String PAYCARD_PAYCARDSERVICEENABLED = "paycard.paycardServiceEnabled";

  /**
   * 公务卡服务平台IP
   */
  public static final String PAYCARD_PAYCARD_SERVERIP = "paycard.paycard_serverip";

  /**
   * 公务卡服务平台端口号
   */
  public static final String PAYCARD_PAYCARD_SERVERPORT = "paycard.paycard_serverport";

  /**
   * 登录DXP用户名和密码 格式为 用户名@密码
   */
  public static final String PAYCARD_PAYCARDLOGININFO = "paycard.payCardLoginInfo";

  /**
   * 财政区划码 公务卡和DXP签到用
   */
  public static final String INTF_DXP_PAYCARD_PAY_CARD_SRCREGION = "intf.dxp.paycard.PAY_CARD_SRCREGION";

  /**
   * 机构码    公务卡和DXP签到用 机构类型码
   */
  public static final String INTF_DXP_PAYCARD_PAY_CARD_SRCORGANIZE = "intf.dxp.paycard.PAY_CARD_SRCORGANIZE";

  /**
   * 公务卡单次下载返回最大条数
   */
  public static final String PAYCARD_PAYCARDMAXRETURNNUM = "paycard.paycardMaxReturnNum";

  /**
   * 公务卡单次下载返回最大条数
   */
  public static final String PAYCARD_PAYCARDDOWNTYPE = "paycard.payCardDownType";

  /**
   * 公务卡模式参数
   */
  public static final String PAYCARD_PAYCARD_MODEL = "paycard.paycard_model";

  /**
   * 判断受理支付时间限制。 hh:ss:mm限制时间格式
   */
  public static final String AGENTBANK_CANPAYTIME = "agentbank.canPayTime";

  /**
   * 支持单号修改的人行编码
   */
  public static final String CLEARBANK_BILLNOMODIFYBANKCODE = "clearbank.billNoModifyBankCode";

  /**
   * 工资是否定值的标志
   */
  public static final String INTF_SALARY_SALARYMATCHFLAG = "intf.salary.salaryMatchFlag";

  /**公司内部工资接口配置：调用的批量录入规则编码。（此规则主要用于补工资默认值）*/
  public static final String INTF_SALARY_SALARYBATCHCODE = "intf.salary.salaryBatchCode";

  /**公司内部工资接口配置：是否补充计划 （包括明细、额度、单据）*/
  public static final String INTF_SALARY_ISPATCHPLAN = "intf.salary.isPatchPlan";

  /**公司内部工资接口配置：调用工资接口生成支付申请，是否为明细*/
  public static final String INTF_SALARY_ISVOUCHER = "intf.salary.isVoucher";

  /**公司内部工资接口配置：调用工资接口生成支付申请，是否为明细*/
  public static final String INTF_SALARY_PK = "intf.salary.pk";

  /**
   * 河南工资指标类型过滤条件
   */
  public static final String ALONE_HN_HNSALARYBPCODE = "alone.hn.hnSalaryBpCode";

  /**
   * 工资查询指标时所需要比对的指标要素
   */
  public static final String INTF_SALARY_BUDGETMATCHELEMENTS = "intf.salary.budgetMatchElements";

  /**
   * 指标科目ID
   */
  public static final String INTF_IMPORT_BUDGETBLANCEACCOUNT = "intf.import.budgetBlanceAccount";

  /**
   * 指标额度COAID
   */
  public static final String INTF_IMPORT_BUDGETBLANCECOA = "intf.import.budgetBlanceCoa";

  /**
   * 支付明细COAID
   */
  public static final String INTF_IMPORT_PAYDETAILCOA = "intf.import.payDetailCoa";

  /**
   * 人行接口导出，是否将0 额度拆分
   */
  public static final String INTF_EXPORT_ISORDIVIDEBYMONEY = "intf.export.isOrDivideByMoney";

  /**
   * 指标修正要素范围，通过分号隔开不同要素
   */
  public static final String INTF_BUDGET_MODIFY_MODIFYELE = "intf.budget.modify.modifyELE";

  /**
   * 指标修正配置的定值规则code，跟授权支付计划billtype_code=263用的是同一个规则
   */
  public static final String INTF_BUDGET_MODIFY_MODIFYELERULECODE = "intf.budget.modify.modifyELERuleCode";

  /**
   * 子系统类别 sysId  115 ，601 代理银行，602 清算银行
   */
  public static final String INTF_EXP_SYSIDS = "intf.exp.sysIds";

  /**
   * sysIds  清算银行
   */
  public static final String INTF_EXP_SYSIDS_CLEARBANK = "intf.exp.sysIds.clearBank";

  /**
   * sysIds  代理银行
   */
  public static final String INTF_EXP_SYSIDS_PROXYBANK = "intf.exp.sysIds.proxyBank";

  /**
   * sysIds  财政
   */
  public static final String INTF_EXP_SYSIDS_FINANCEMINISTRY = "intf.exp.sysIds.financeMinistry";

  /**
   *金额显示格式，用于银行报文种，金额显示的不同规则
   */
  public static final String INTF_DXP_MONEYSHOWPATTERN = "intf.dxp.moneyShowPattern";

  /**
   * 直接支付
   */
  public static final String COMMON_ELE_PK_DIRECTPKCODE = "common.ele.pk.directPKCode";

  /**
   * 集中支付
   */
  public static final String COMMON_ELE_PK_CENTERPKCODE = "common.ele.pk.centerPKCode";

  /**
   * 授权支付
   */
  public static final String COMMON_ELE_PK_ACCREDITPKCODE = "common.ele.pk.accreditPKCode";

  /**
   * 实拨支付
   */
  public static final String COMMON_ELE_PK_REALPKCODE = "common.ele.pk.realPKCode";

  /**
   * 收支管理 基本支出
   */
  public static final String COMMON_ELE_INPM_BASICOUTCOME = "common.ele.inpm.basicOutcome";

  /**
   * 收支管理 项目支出
   */
  public static final String COMMON_ELE_INPM_PROJECTOUTCOME = "common.ele.inpm.projectOutcome";

  /**
   * 结算类型要素简称
   */
  public static final String COMMON_ELE_PM = "common.ele.pm";

  /**
   * 结算类型:现金支付编码
   */
  public static final String COMMON_ELE_PM_CASHCODE = "common.ele.pm.cashCode";

  /**
   * 结算类型:空白支票编码
   */
  public static final String COMMON_ELE_PM_BLANKCHECKVALUE = "common.ele.pm.blankCheckValue";

  /**
   * 结算类型:转账支付编码
   */
  public static final String COMMON_ELE_PM_TRANSITIONCODE = "common.ele.pm.transitionCode";

  /**
   * 资金性质-预算内编码
   */
  public static final String COMMON_ELE_MK_INNERMKCODE = "common.ele.mk.innerMkcode";

  /**
   * 资金性质-自有资金编码
   */
  public static final String COMMON_ELE_MK_SELFMK = "common.ele.mk.selfMk";

  /**
   * 支付管理类型要素简称
   */
  public static final String COMMON_ELE_PF = "common.ele.pf";

  /**
   * 支付管理类型-年结列支
   */
  public static final String COMMON_ELE_PF_DISBURSEDYEAREND = "common.ele.pf.disbursedYearEnd";

  /**
   * 支付管理类型-年结不列支
   */
  public static final String COMMON_ELE_PF_NORMALYEAREND = "common.ele.pf.normalYearEnd";

  /**
   * 支付管理类型-正常支付
   */
  public static final String COMMON_ELE_PF_NORMALPAY = "common.ele.pf.normalPay";

  /**
   * 支付管理类型-空白支票
   */
  public static final String COMMON_ELE_PF_BLCHECK = "common.ele.pf.blCheck";

  /**
   * 支付管理类型-托收支付
   */
  public static final String COMMON_ELE_PF_CAREPAY = "common.ele.pf.carePay";

  /**
   * 支付管理类型：公务卡支付
   */
  public static final String COMMON_ELE_PF_PAYCARDPAY = "common.ele.pf.payCardPay";

  /**
   * 指标核销数据类型
   */
  public static final String INTF_BUDGET_FEEDBACK_VERIFIERS = "intf.budget.feedback.verifiers";

  /**
   * 是否执行自动核销
   */
  public static final String INTF_BUDGET_FEEDBACK_ENABLED = "intf.budget.feedback.autoPayBudget";

  /**
   * 判断指标核销的job是否已经有正在执行的，用于同时有多个服务启动的情况.
   */
  public static final String INTF_BUDGET_FEEDBACK_ISRUNNING = "intf.budget.feedback.payBudgetIsUse";

  /**
   * 华青接口按月份扣减额度规则，1为从大到小扣减，0为从小到大扣减
   */
  public static final String INTF_BUDGET_FEEDBACK_MONTH_MODE = "intf.budget.feedback.payBudgetMonthConf";

  /**
   * 指标系统，0：华青，1：F3
   */
  public static final String INTF_BUDGET_FEEDBACK_SYS = "intf.budget.feedback.payBudgetType";

  /**
   * 用于配置关联字段的地方（一般情况下为id）
   */
  public static final String INTF_BUDGET_FEEDBACK_RELATE_FIELD = "intf.budget.feedback.relate_field";

  /**
   * 交换中心支付子系统
   */
  public static final String UPARA_PLAN_PLAN_EXCHANGE_SENDAPP = "upara.plan.PLAN_EXCHANGE_SENDAPP";

  /**s
   * 是否计划自动审核
   */
  public static final String UPARA_PLAN_AUTO_CHECK = "upara.plan.AUTO_CHECK";

  /**
   * 计划自动审核用户
   */
  public static final String UPARA_PLAN_AUTO_CHECK_USER = "upara.plan.AUTO_CHECK_USER";

  /**
   * 计划自动审核角色
   */
  public static final String UPARA_PLAN_AUTO_CHECK_ROLE = "upara.plan.AUTO_CHECK_ROLE";

  /**
   * 计划自动审核功能号
   */
  public static final String UPARA_PLAN_AUTO_CHECK_MODULE = "upara.plan.AUTO_CHECK_MODULE";

  /**
   * 计划自动审核天数
   */
  public static final String UPARA_PLAN_AUTO_CHECK_DAY = "upara.plan.AUTO_CHECK_DAY";

  /**
   * 单位可执行指标的sum_type_code
   */
  public static final String UPARA_PAY_PAY_BUDGET_SUMTYPECODE = "upara.pay.PAY_BUDGET_SUMTYPECODE";

  /**
   * card server socket
   */
  public static final String UPARA_PAY_PAY_CARD_SOCKET_PORT = "upara.pay.PAY_CARD_SOCKET_PORT";

  /**
   * 交换中心下载文件目录
   */
  public static final String UPARA_PAY_PAY_EXCHANGE_DIR = "upara.pay.PAY_EXCHANGE_DIR";

  /**
   * 下载的错误xml文件的存放目录
   */
  public static final String UPARA_PAY_PAY_EXCHANGE_ERROR_DIR = "upara.pay.PAY_EXCHANGE_ERROR_DIR";

  /**
   * 交换中心地址
   */
  public static final String UPARA_PAY_PAY_EXCHANGE_IP = "upara.pay.PAY_EXCHANGE_IP";

  /**
   * 交换中心用密码
   */
  public static final String UPARA_PAY_PAY_EXCHANGE_PASSWORD = "upara.pay.PAY_EXCHANGE_PASSWORD";

  /**
   * 交换中心端口
   */
  public static final String UPARA_PAY_PAY_EXCHANGE_PORT = "upara.pay.PAY_EXCHANGE_PORT";

  /**
   * 交换中心支付子系统
   */
  public static final String UPARA_PAY_PAY_EXCHANGE_SENDAPP = "upara.pay.PAY_EXCHANGE_SENDAPP";

  /**
   * 交换中心线程休眠时间
   */
  public static final String UPARA_PAY_PAY_EXCHANGE_SLEEPTIME = "upara.pay.PAY_EXCHANGE_SLEEPTIME";

  /**
   * 交换中心用户名
   */
  public static final String UPARA_PAY_PAY_EXCHANGE_USER = "upara.pay.PAY_EXCHANGE_USER";

  /**
   * 政府采购模式
   */
  public static final String UPARA_PAY_PAY_GB_FLAG = "upara.pay.PAY_GB_FLAG";

  /**
   * 支付系统许可证到期提示时间
   */
  public static final String UPARA_PAY_PAY_LICENSE_DATE = "upara.pay.PAY_LICENSE_DATE";

  /**
   * 生成支付所需的工资要素
   */
  public static final String UPARA_PAY_PAY_SALARY_REWRITING_ELE = "upara.pay.PAY_SALARY_REWRITING_ELE";

  /**
   * 
   */
  public static final String UPARA_PAY_CA_SVS_BCIPHER = "upara.pay.CA_SVS_BCIPHER";

  /**
   * svs服务器的等待时间
   */
  public static final String UPARA_PAY_CA_SVS_MAX_WAIT_TIME = "upara.pay.CA_SVS_MAX_WAIT_TIME";

  /**
   * svs服务器的IP
   */
  public static final String UPARA_PAY_CA_SVS_IP = "upara.pay.CA_SVS_IP";

  /**
   * svs服务器的端口号
   */
  public static final String UPARA_PAY_CA_SVS_PORT = "upara.pay.CA_SVS_PORT";

  /**
   * svs服务器的超时
   */
  public static final String UPARA_PAY_CA_SVS_SOCKET_TIMEOUT = "upara.pay.CA_SVS_SOCKET_TIMEOUT";

  /**
   * 是否效验超期
   */
  public static final String UPARA_PAY_ISORVERIFY = "upara.pay.isOrVerify";

  /**
   * 直接支付入账通知书生成是否合单标识
   */
  public static final String PAY_ACC_DOUNITETTYPE = "doUnitType";

  /**
   * 身份证号验证是否启用
   * add by fangbb
   */
  public static final String PAYCARD_PAYCARDCHECKIDENTITY = "paycard.PAY_CARD_CHECK_IDENTITY";

  /**
   * 上传文件夹的绝对路径
   */
  public static final String PAY_UPLOAD_PATH = "pay.upload_path";

  /**
   * 核算类型
   * add by zgh
   */
  public static final String Budget_Origin = "common.ele.bo";

  /**
   * 核算类型过滤往年结转的值
   * add by zgh
   */
  public static final String Budget_Origin_Value = "common.ele.bo.disbursed";

  /**
   * 上传文件的大小
   */
  public static final String PAY_UPLOAD_SIZE = "pay.upload_size";

  /**
   * 支付导入挂接指标失败或超支是否全部回退
   */
  public static final String PAY_IMPORT_IS_ROLLBACK = "payImport.isRollBack";

  /**
   * 录入支付时是否默认自动显示收款人
   */
  public static final String PAY_AUTO_SHOWACC = "pay.autoShowAcc";

  /**
   * 直接打印和预览打印走工作流，打印成功
   */
  public static final String printOK_flag = "printOK_flag";

  /***
   * 支持单号修改的人行编码
   */
  public static final String clearbank_billNoModifyBankCode = "clearbank.billNoModifyBankCode";

  /**
   * 银行是否参与指标修正流程
   */
  public static final String BUDGETMODIFY_BANKCARE = "pay_budgetModify_bankcare";

  /**
   * 是否启用客户端优化
   */
  public static final String CLIENTQUERY_ISENABLE = "clientquery.isEnable";

  /**
   * 结算类型:网银支付编码
   */
  public static final String COMMON_ELE_PM_NETPAYCODE = "common.ele.pm.netpayCode";

  /**政府采购虚拟节点功能id*/
  public static final String INTF_ZC_VOUCHERVIRTUALNODE = "intf.zc.voucherVirtualNode";

  /**政府采购虚拟节点功能id*/
  public static final String INTF_ZC_BILLVIRTUALNODE = "intf.zc.billVirtualNode";

  /**政府采购接口，根据指标生成计划、根据计划生成支付虚拟节点功能id*/
  public static final String INTF_ZC_PLANVOUCHERNODE = "intf.zc.planVoucherNode";

  /**政府采购接口配置：调用的批量录入规则编码。（此规则主要用于补政府采购根据指标生成支付的默认值）*/
  public static final String INTF_ZC_ZCBATCHCODE = "intf.zc.zcBatchCode";

  /**政府采购接口配置：调用的批量录入规则编码。（此规则主要用于补政府采购无指标生成支付的默认值）*/
  public static final String INTF_ZC_ZCNOBUDGETBATCHCODE = "intf.zc.zcNoBudgetBatchCode";

  /**政府采购根据指标生成计划批量录入规则编码，用于补充默认值*/
  public static final String INTF_ZC_PLANBATCHCODE = "intf.zc.planBatchCode";

  /**政府采购根据计划生成支付批量录入规则编码，用于补充默认值*/
  public static final String INTF_ZC_PAYBATCHCODE = "intf.zc.payBatchCode";

  /** 政府采购冻结指标（生成在途计划，不走工作流）规则编码，用于补充默认值 */
  public static final String INTF_ZC_FREEZEBATCHCODE = "intf.zc.freezeBatchCode";

  /** 支付退回申请时调用政府采购系统的webservice地址 */
  public static final String INTF_ZC_WEBSERIVEIP = "intf.zc.webserviceIp";

  /** 政府采购webservice服务提供的接口方法 */
  public static final String INTF_ZC_WEBSERIVEOPERATIONNAME = "intf.zc.webserviceOperationName";

  /**网银支付是否可用*/
  public static final String EBANK_USEABLE = "ebank.useable";

  /**网银支付开始时间*/
  public static final String EBANK_STARTTIME = "ebank.starttime";

  /**网银支付结束时间*/
  public static final String EBANK_ENDTIME = "ebank.endtime";

  /**网银支付确认用户编码*/
  public static final String EBANK_USEERCODE = "ebank.usercode";

  /**网银支付模式*/
  public static final String EBANK_USEMODE = "ebank.usemode";

  /**网银支付垫资总线IP*/
  public static final String EBANK_PREPAY_DXP_IP = "ebank.prepay.dxp.ip";

  /**网银支付垫资总线端口*/
  public static final String EBANK_PREPAY_DXP_PORT = "ebank.prepay.dxp.port";

  /**网银支付垫资总线用户*/
  public static final String EBANK_PREPAY_DXP_USER = "ebank.prepay.dxp.user";

  /**网银支付垫资总线密码*/
  public static final String EBANK_PREPAY_DXP_PWD = "ebank.prepay.dxp.pwd";

  /**计划、支付明细和额度追溯按钮弹出界面所使用的指标额度列表视图*/
  public static final String COMMON_TRACE_BUDGETBALANCEUI = "common.trace.budgetBalanceUI";

  /**计划、支付明细和额度追溯按钮弹出界面所使用的指标明细列表视图*/
  public static final String COMMON_TRACE_BUDGETVOUCHERUI = "common.trace.budgetVoucherUI";

  /**计划、支付明细和额度追溯按钮弹出界面所使用的指标额度列表视图*/
  public static final String COMMON_TRACE_PLANBALANCEUI = "common.trace.planBalanceUI";

  /**计划、支付明细和额度追溯按钮弹出界面所使用的指标额度列表视图*/
  public static final String COMMON_TRACE_PLANVOUCHERUI = "common.trace.planVoucherUI";

  /**计划、支付明细和额度追溯按钮弹出界面所使用的指标额度列表视图*/
  public static final String COMMON_TRACE_PAYVOUCHERUI = "common.trace.payVoucherUI";

  /**计划、支付明细和额度追溯按钮查询指标明细视图*/
  public static final String COMMON_TRACE_BUDGETVIEW = "common.trace.budgetView";

}
