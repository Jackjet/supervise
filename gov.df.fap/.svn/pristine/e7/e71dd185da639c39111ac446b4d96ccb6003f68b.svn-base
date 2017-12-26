/**
 * page key
 */
//var SERVER_URL = "";
//var SERVER_URL = "";
var ptd_jb_obj_chaParam = "&id=admin&pw=admin&showmenu=false&fasp_t_agency_id=" + Base64.decode($("#svAgencyId", parent).val());
var ptd_jb_obj = {
	oftenUrl : {
		"0":[
			// 新 现金业务
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=AN&tokenid="+getTokenId(),
			// 新 普通转账
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=PT&tokenid="+getTokenId(),
			// 新 代扣代缴
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=DK&tokenid="+getTokenId(),
			// 新 柜台缴税
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=GT&tokenid="+getTokenId(),
			// 新 批量支付
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=PLZF&tokenid="+getTokenId(),
			// 新 公务卡
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=GWK&tokenid="+getTokenId(),
			// 政府采购
			""
		],
		"1":[
			// 额度到账通知书
			"/df/pay/plan/bills/plAgentenRegister.html?menuid=14C5F873520F87F22EEB06FCEB4950BD&menuname=%u5355%u4F4D%u989D%u5EA6%u5230%u8D26%u901A%u77E5%u4E66%u767B%u8BB0%0A&tokenid="+getTokenId(),
			// 授权支付退款通知书
			"/df/pay/centerpay/bills/paAccountBillRegister.html?billtype=365&menuid=F572C1F1142AD82C3663F1B3768A6E32&menuname=%u6388%u6743%u652F%u4ED8%u51ED%u8BC1%u9000%u6B3E%u56DE%u5355%u767B%u8BB0&tokenid="+getTokenId(),
			// 授权支付入账通知书
			"/df/pay/centerpay/bills/paAccountBillRegister.html?menuid=02BF79AF73F700B10A9D4B7DE442681C&menuname=%u6388%u6743%u652F%u4ED8%u5165%u8D26%u901A%u77E5%u4E66%u767B%u8BB0&tokenid="+getTokenId(),
			// 授权支付退款入账通知书
			"/df/pay/centerpay/bills/paFundReturnBill.html?menuid=451C4DA34F040F0CF0626223D0ABCF3E&menuname=%u6388%u6743%u652F%u4ED8%u9000%u6B3E%u5165%u8D26%u901A%u77E5%u4E66%u767B%u8BB0&tokenid="+getTokenId(),
			// 直接支付入账通知书
			"/df/pay/centerpay/bills/paAccountBillRegister.html?menuid=2C7C442EF357E721C948CF59521DB3CE&menuname=%u8D22%u653F%u76F4%u63A5%u652F%u4ED8%u5165%u8D26%u901A%u77E5%u4E66%u767B%u8BB0&tokenid="+getTokenId(),
			// 直接支付退款入账通知书
			"/df/pay/centerpay/bills/paAccountBillRegister.html?billtype=365&menuid=4726D8EB91D92438B8FAC33F0B9E3247&menuname=%u8D22%u653F%u76F4%u63A5%u652F%u4ED8%u9000%u6B3E%u5165%u8D26%u901A%u77E5%u4E66%u767B%u8BB0&tokenid="+getTokenId()
		],
		"2":[	// TODO 未开发
			// 财政直接支付申请书查询
			""+getTokenId(),
			// 财政授权支付凭证查询
			""+getTokenId(),
			// 指标明细查询
			"http://10.28.5.155:8080/bi422-20160603/showreport.do?resid=EBI$12$MMOWUNDUXN03OUXMKF5YM3MI45M8U4TZ$1$C5IL13357KQP6FU5KZFJLRKCLU5CKLXM.rpttpl&tokenid="+getTokenId()+ptd_jb_obj_chaParam,
			// 支付明细查询
			"http://10.28.5.155:8080/bi422-20160603/showreport.do?resid=EBI$12$KWNVUEM4LRCOXZLWK9SO3MLNNSWYO5DK$1$UMUYSPL6E5TUSZRMKWZMTKSIOXMKCK8U.rpttpl&tokenid="+getTokenId()+ptd_jb_obj_chaParam,
			//"http://192.168.10.11:9090/ESEN/showreport.do?resid=EBI$12$UYXTC5MKUMYXULKY73LN5QXW78YSQYM1$1$TC3B0QM8DMNVUC3CUKC1ZVK86OCJMAIK.rpttpl&tokenid="+getTokenId()+ptd_jb_obj_chaParam,
			// 预算执行情况查询
			"http://10.28.5.155:8080/bi422-20160603/showreport.do?resid=EBI$12$UYXTC5MKUMYXULKY73LN5QXW78YSQYM1$1$TC3B0QM8DMNVUC3CUKC1ZVK86OCJMAIK.rpttpl&tokenid="+getTokenId()+ptd_jb_obj_chaParam,
			// 国库集中支付年终结余资金对账单
			"http://10.28.5.155:8080/bi422-20160603/showreport.do?resid=EBI$12$MVURSBULV1UKITVV60L1XKMI4LVOI238$1$8UNKFCC5XLXNCNFUQ0NRIJJ9O9UQDOAL.rpttpl&tokenid="+getTokenId()+ptd_jb_obj_chaParam,
			// 预算单位分预算项目查询
			""+getTokenId()
			// 自定义查询
			//""+getTokenId()
		],
		"3":[
			// 操作手册
			""+getTokenId(),	// 邹锦涛 提供
			// 操作规范
			"/doc/paybusiness/article.html?tokenid="+getTokenId(),
			// 公务卡
			""+getTokenId(),	// 张明辉 提供
			// 支付签章
			""+getTokenId(),	// 张明辉 提供
			// 凭证查询
			""+getTokenId(),	// 张明辉 提供
			// 凭证打印
			""+getTokenId(),	// 张明辉 提供
			// 资金监控
			""+getTokenId(),	// 张明辉 提供
			// 其他
			""+getTokenId()	// 张明辉 提供
		]
	},
	dealing : {
		title : [
			"单位额度到账通知书登记",
			"授权支付退款通知书",
			"财政授权支付入账通知书登记",
			"财政授权支付退款入账通知书登记",
			"财政直接支付入账通知书登记",
			"财政直接支付退款入账通知书",
			"授权支付凭证经办人签私章"
		],
		menuid : [
			//"单位额度到账通知书登记",
			"14C5F873520F87F22EEB06FCEB4950BD",
			//"授权支付退款通知书",
			"7B4D1BA03A0AB37310660E90B98193D5",
			//"财政授权支付入账通知书登记",
			"02BF79AF73F700B10A9D4B7DE442681C",
			//"财政授权支付退款入账通知书登记",
			"F572C1F1142AD82C3663F1B3768A6E32",
			//"财政直接支付入账通知书登记",
			"2C7C442EF357E721C948CF59521DB3CE",
			//"财政直接支付退款入账通知书"
			"4726D8EB91D92438B8FAC33F0B9E3247",
			//"授权支付凭证经办人签私章"
			"9588FA171826EE5F56C82AEF1C474E01"
		]
	}
};

$(function(){
	
	document.onclick = function(){
		$("#budgetWrapper").css("display", "none");
	};
	
	var portal_d = new Portal_d();
	
	var oftenUrl = ptd_jb_obj.oftenUrl;
	portal_d.often.set(oftenUrl);

	portal_d.pp.bf();
	portal_d.pp.show();

	portal_d.budget.bf();
	portal_d.budget.show();

	portal_d.fiscal.set();

	portal_d.fm.show();

	portal_d.dl.show(ptd_jb_obj.dealing.title, ptd_jb_obj.dealing.menuid, "200px", "m-content1");
	$("#dealingMore").click(function(){
		portal_d.dl.show(ptd_jb_obj.dealing.title, ptd_jb_obj.dealing.menuid, "200px", "m-content1");
	});

	portal_d.at.show();

	$("#refreshFundmonitor").click(function(){ refreshFundmonitor();});
	
});
