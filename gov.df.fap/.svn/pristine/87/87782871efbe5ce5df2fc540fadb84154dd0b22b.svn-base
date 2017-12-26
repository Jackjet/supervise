/**
 * page key
 */
var ptd_jb_obj_chaParam = "&id=admin&pw=admin&showmenu=false&fasp_t_agency_id=" + Base64.decode($("#svAgencyId", parent).val());
var ptd_sh_obj = {
	oftenUrl : {
		"0":[ // 我要查
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
		"1":[	// 我要问
			// 操作手册
			""+getTokenId(),	// 邹锦涛 提供
			// 操作规范
			"/doc/paybusiness/article.html?tokenid="+getTokenId(),
			// 公务卡
			""+getTokenId(),	// 张明辉 提供
			// 支付签章
			""+getTokenId(),
			// 凭证查询
			""+getTokenId(),
			// 凭证打印
			""+getTokenId(),
			// 资金监控
			""+getTokenId(),
			// 其他
			""+getTokenId()
		]
	},
	dealing : {
		// 条目筛选
		title : [
			"授权支付凭证负责人签私章",
			"授权支付凭证负责人签公章",
			"银行退回"
		],
		menuid : [
			//"授权支付凭证负责人签私章",
			"1716C33244EBDD07931D3F6A12CA7C1A",
			//"授权支付凭证负责人签公章",
			"746503EE32CF8D7EBB46F7C32C9C3F00",
			//"银行退回",
			""
		]
	}
};

$(function(){
var portal_d = new Portal_d();
	
	document.onclick = function(){
		$("#budgetWrapper").css("display", "none");
	};

	var oftenUrl = ptd_sh_obj.oftenUrl;
	portal_d.often.set(oftenUrl);

	portal_d.pp.bf();
	portal_d.pp.show();

	portal_d.budget.bf();
	portal_d.budget.show();

	portal_d.fiscal.set();

	portal_d.fm.show();

	portal_d.dl.show(ptd_sh_obj.dealing.title, ptd_sh_obj.dealing.menuid, "");
	$("#dealingMore").click(function(){
		portal_d.dl.show(ptd_sh_obj.dealing.title, ptd_sh_obj.dealing.menuid, "");
	});
	
	portal_d.at.show();

	$("#refreshFundmonitor").click(function(){ refreshFundmonitor();});
});


