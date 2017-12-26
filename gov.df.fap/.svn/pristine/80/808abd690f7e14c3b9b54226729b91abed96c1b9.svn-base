function getTokenId(){
	var current_url = window.location.href;
	if(current_url.indexOf("?") > -1){
		var tokenid = "";
		var params = new Array();
		params = current_url.split("?");
		if(params[1].indexOf("tokenid=") > -1){
			if(params[1].indexOf("&") > -1){
				var neededParams = params[1].split("&");
				for(var i in neededParams){
					if(neededParams[i].indexOf("tokenid=") > -1){
						tokenid = neededParams[i];
						break;
					}
				}
			}else{
				tokenid = params[1];
			}
		}
//		var tokenidFromLS = dfp.localStorage.getItem(dfp.key.tokenid);
//		if(!dfp.Object.isNull(tokenidFromLS)){
//			var tokenidFromLSs = tokenidFromLS.split(dfp.key.dfSeparator1);
//			if(tokenidFromLSs[2] == tokenid.substring(8))
//				dfp.localStorage.setItem(dfp.key.tokenid, dfp.key.dfValidFlag+tokenid.substring(8));
//		}
		return tokenid.substring(8);
	}
	return "";
}

//预算指标表格样式设置
changeColorNormal = function(obj) {
	var value = obj.value;
	// 功能分类
	if("expfunc_name"==obj.gridCompColumn.options.field){
		value = obj.row.value.expfunc_code + " " + value;
	}
	// 经济分类
	if("expeco_name"==obj.gridCompColumn.options.field){
		value = obj.row.value.expeco_code + " " + value;
	}
	// 预算单位
	if("agency_name"==obj.gridCompColumn.options.field){
		value = obj.row.value.agency_code + " " + value;
	}
	var html = '<div style="font-size:14px;line-height:30px;font-family:微软雅黑;">' + value + '</div>';
	obj.element.innerHTML = html;
}
changeColorZhibiao = function (obj) {
	var html = '';
	if(!(obj.value==null||obj.value==""||obj.value==undefined))
		html = '<div style="text-align:right;font-size:14px;line-height:30px;font-family:微软雅黑;">' + ip.dealThousands(obj.value) + '</div>';
	obj.element.innerHTML = html;
}
changeColorKeyong = function (obj) { // 原 #f56a00， 蓝 0000ff 
	var html = '';
	if(!(obj.value==null||obj.value==""||obj.value==undefined))
		html = '<div style="text-align:right;color:#0000ff;font-size:14px;line-height:30px;font-family:微软雅黑;">' + ip.dealThousands(obj.value) + '</div>';
	obj.element.innerHTML = html;
}
tableSum = function (obj) {
	var fontColor = {
		"avi_money":"#000000",
		"canuse_money":"#000000"
	}
	var colName = obj.gridCompColumn.options.field;
	var html = '<div style="text-align:right;color:'+fontColor[colName]+';font-size:14px;line-height:30px;font-family:微软雅黑;">' + ip.dealThousands(obj.value) + '</div>';
	obj.element.innerHTML = html;
}
//预算指标单击事件
tableBeforeEdit = function(obj) {
	var colName = (obj.gridObj.gridCompColumnArr[obj.colIndex]).options.field;
	if("canuse_money"!=colName)
		return;
	//ptd_budget.tabledbClick(obj);
	return;
}
//预算指标双击事件
tableDbCLick = function(obj){
	//ptd_budget.tabledbClick(obj);
}

// 预算指标请求参数
var all_options_condition = " and paytype_code like '12%' ";
var all_options = {
	"tokenid": getTokenId(),
	"file_name": "",
	"agencyexp_name": "",
	"bis_name": "",
	"avi_money": "",
	"canuse_money": "",
	"expfunc_name": "",
	"expeco_name": "",
	"fundtype_name": "",
	"bgtsource_name": "",
	"agency_name": "",
	"mb_name": "",
	"budget_summary": "",
	"billtype":"366",
	"busbilltype":"311",
	"pageInfo":"99999,0",
	"ajax": "noCache",
	"condition": all_options_condition
};
// 预算指标table初始化
// 预算指标table列
// 预算指标table列 外加（expfunc_code、expeco_code、agency_code）
var budgetTableColTitle = {
	"sum_id": "sum_id",
	"fromctrlid": "fromctrlid",
	"file_name": "指标文号",
	"agencyexp_name": "项目分类",
	"bis_name": "预算项目",
	"avi_money": "指标金额",
	"canuse_money": "指标余额",
	"expfunc_name": "功能分类",
	"expeco_name": "经济分类",
	"fundtype_name": "资金性质",
	"bgtsource_name": "指标来源",
	"agency_name": "预算单位",
	"mb_name": "业务处室",
	"budget_summary": "指标摘要"
};
var viewModel = {
	dataTable: new u.DataTable({
		// 表头
		meta: budgetTableColTitle
	}, this),
};
ko.cleanNode($('#tableContent')[0]);
app = u.createApp({
	el: '#tableContent',
	model: viewModel
});

// 首次加载
var budgetTableInit = 1;
// 预算指标原始数据
var budgetTableDatadetail = [];
// 预算指标余额为零数据(元)
var budgetTableDatadetailNull = [];
// 预算指标原始数据(元，用于切换单位)
var budgetTableDatadetailOri = [];
// 预算指标原始数据-不含零数据(元，用于切换单位)
var budgetTableDatadetailOriNull = [];
// 预算指标万元数据
var budgetTableDatadetailWan = [];
// 预算指标-包含余额为零数据(万元)
var budgetTableDatadetailWanNull = [];
// 预算指标亿元数据
var budgetTableDatadetailYi = [];
// 预算指标-包含余额为零数据(亿元)
var budgeTableDatadetailYiNull = [];
// 预算指标余额为零标志，被选中为1，否则为0
var budgetTableYueNull = 0;
// 预算指标单位标志：0 元，1 万元，2 亿元
var budgetTableRadioWan = 0;
// 预算指标高级查询启动标志，启动为1，未启动为0
var budgetTableGaojiStart = 0;

// 常用操作图片
var oftenImg = {
	"ban":"img/icon-1.png",
	"banHover":"img/icon-1-w.png",
	"deng":"img/icon-2.png",
	"dengHover":"img/icon-2-w.png",
	"cha":"img/icon-3.png",
	"chaHover":"img/icon-3-w.png",
	"wen":"img/icon-4.png",
	"wenHover":"img/icon-4-w.png",
};

/**
 * page key
 */
var ptd_obj = {
	often : {
		oftenUrl : []
	},
	budget: {
		rightUrl : [
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=AN",
			// 新 普通转账
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=PT",
			// 新 代扣代缴
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=DK",
			// 新 柜台缴税
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=GT",
			// 新 批量支付
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=PLZF",
			// 新 公务卡
			"/df/sd/pay/centerpay/input/payCommonInput.html?billtype=366&busbilltype=322&model=model5&menuid=9588FA171826EE5F56C82AEF1C474E01&menuname=%u65B0%u5F55%u5165%u754C%u9762&type=GWK",
			// 政府采购
			"",
			// 预算执行情况
			"/df/sd/pay/commonModal/traceBalanceList/balanceForPortal.html",
			// 导出Excel
			""
		]
	},
	fiscal : {
		// 外网
		FISCAL_URL_PUB : "http://192.168.10.11:8800/jsp/solr/index_param.jsp?name=",
		// 内网
		FISCAL_URL_INS : "http://10.28.5.155:8800/jsp/solr/index_param.jsp?name=",
	},
	dealing : {},
	article : {
		// 更多
		ARTICLE_URL_MORE : "../../common/articleSearch.jsp?pgPletId=16&userId=sa",
		// 创建
		ARTICLE_URL_CREATE : "/df/portal/articleManagement/articleMain.html?a=1"
	}
};

/**
 * often
 */
var ptd_often = {
	bf : function(){
		// 常用操作-div层显示
		$(".hid").mouseover(function(){
			//$(this).css({"background":"#D9EDF7"});
			$(this).css({"background":"#108EE9"});
			var imgPath = $(this).find("a").find("img").prop("src");
			if(imgPath.indexOf("ban")>0){
				imgPath = "img/icon-ban-w.png";
			}
			if(imgPath.indexOf("deng")>0){
				imgPath = "img/icon-deng-w.png";
			}
			if(imgPath.indexOf("cha")>0){
				imgPath = "img/icon-cha-w.png";
			}
			if(imgPath.indexOf("wen")>0){
				imgPath = "img/icon-wen-w.png";
			}
			$(this).find("a").find("img").prop("src", imgPath);
			$(this).find("a").find("span").css("color", "#FFFFFF");
			$(this).find("div").css({"display":"block"});
		}).mouseleave(function(){
			$(this).css({"background":"#ffffff"});
			var imgPath = $(this).find("a").find("img").prop("src");
			if(imgPath.indexOf("ban")>0){
				imgPath = "img/icon-ban.png";
			}
			if(imgPath.indexOf("deng")>0){
				imgPath = "img/icon-deng.png";
			}
			if(imgPath.indexOf("cha")>0){
				imgPath = "img/icon-cha.png";
			}
			if(imgPath.indexOf("wen")>0){
				imgPath = "img/icon-wen.png";
			}
			$(this).find("a").find("img").prop("src", imgPath);
			$(this).find("a").find("span").css("color", "#000000");
			$(this).find("div").css({"display":"none"});
		});
		$(".hidContent").mouseleave(function(){
			$(".hid").css({"background":"#ffffff"});
			$(this).find("div").css({"display":"none"});
		});
		// 常用操作-字体颜色
		$(".hidContent ul li").mouseover(function(){
			$(this).find("a").css("color", "#FFFFFF");
		});
		$(".hidContent ul li").mouseleave(function(){
			$(this).find("a").css("color", "#333");
		});
	},
	url : function(){
		return {};
	},
	set : function(oftenUrl){
		this.bf();
		oftenUrl = ptd_util.isNull(oftenUrl) ? {} : oftenUrl;
		$("div.hidContent").each(function(i){ // 单分类div
			$(this).find("li").each(function(n){ // 单功能li
				$(this).on("click", function(){
					window.parent.addTabToParent($(this).find("a").text(), fullUrlWithTokenid((oftenUrl[i])[n]));
				});
			});
		});
	}
}

/**
 * payprogress
 */
var ptd_payprogress = {
	bf : function(){
		// 支出进度截止时间初始化
		$('#budgetTime').fdatepicker({
			format: 'yyyy-mm-dd'
		});
		$("#budgetTime").val(datetimeSpe("pp"));
		var $timeFoot = $(".datepicker-days").find("tfoot").find("th.today");
		$timeFoot.css("display", "block !important");
		$timeFoot.css("border", "solid 1px #ccc");
		$timeFoot.click(function(){
			$("#budgetTime").val(datetimeSpe("pp"));
			$("div.datepicker.datepicker-dropdown.dropdown-menu").hide();
		});
		$("._portal_zhichu_select_yslb_select").on("change", function(e){
			ptd_payprogress.show();
		});
		$("._portal_zhichu_select_zclx_select").on("change", function(e){
			ptd_payprogress.show();
		});
		$("._portal_zhichu_select_zbly_select").on("change", function(e){
			ptd_payprogress.show();
		});
		$("#budgetTime").on("change", function(e){
			ptd_payprogress.show();
		});
		
	},
	show : function(){
		var fundtypeCode = $("#fundtypeCode").val();
		var expfuncCode = $("#expfuncCode").val();
		var bgtsourceCode = $("#bgtsourceCode").val();
		var selecttime = $("#budgetTime").val();
		var agency = Base64.decode($("#svAgencyCode", parent.document).val());
		$.ajax({
			url: "/df/portal/dubbo/payProgress.do",
			type: "GET",
			dataType: "json",
			data: {"tokenid":getTokenId(),"fundtypeCode":fundtypeCode,"expfuncCode":expfuncCode,"bgtsourceCode":bgtsourceCode,"selecttime":selecttime,"agency":agency},
			success: function(data) {
				
				if((data.data).length == 0){
					$("#tooltipXSJD").css("display", "none");
				}
				
				// 渲染图表
				//$("#dwzc").css("display","block");
				var dataDetail = data.data;
				if(dataDetail.length==0){
					//console.log("-- dubbo service is out");
					$("#_portal_zhichu_text_zbje_span").html("0");
					$("#_portal_zhichu_text_zfje_span").html("0");
					$("#_portal_zhichu_text_zbye_span").html("0");
					$("#dwzc").html('<span style="padding:20px;font-size:20px;color:#1b1005;line-height:120px;">当前条件下无指标及支出数据</span>');
					return ;
				}
				
				//var mofdepCode = dataDetail[0].mofdepCode;	// 处室
				//var deptCode = dataDetail[0].deptCode;	// 部门
				//var agencyCode = dataDetail[0].agencyCode;	// 单位
				//var queryDate = dataDetail[0].queryDate;	// 时间 yyyy-MM-dd
				//var fundtypeCode = dataDetail[0].fundtypeCode;	// 预算类别
				//var expfuncCode = dataDetail[0].expfuncCode;	// 支出类型
				var budgetMoney = dataDetail[0].budgetMoney;	// 指标金额
				var payMoney = dataDetail[0].payMoney;	// 支付金额
				var lastBgtMoney = dataDetail[0].lastBgtMoney;	// 指标余额
				
				// 渲染支出进度文字提示
				$("#_portal_zhichu_text_zbje_span").html(ip.dealThousands(budgetMoney?budgetMoney:"0"));
				$("#_portal_zhichu_text_zfje_span").html(ip.dealThousands(payMoney?payMoney:"0"));
				$("#_portal_zhichu_text_zbye_span").html(ip.dealThousands(lastBgtMoney?lastBgtMoney:"0"));
				//$("#payprogressXSJD").html(ptd_util.progressInYear() + "%");
				
				if(payMoney==0&&budgetMoney==0){
					$("#dwzc").html('<span style="padding:20px;font-size:20px;color:#1b1005;line-height:120px;">当前条件下无指标及支出数据</span>');
					return;
				}
				ptd_payprogress.hchart(payMoney, lastBgtMoney, fundtypeCode, expfuncCode, selecttime);
				
				// 序时进度
				$("#tooltipXSJD").css("display", "block");
				$payprogressXSJD = $("#payprogressXSJD");
				$payprogressXSJD.css("display", "none");
				$XSJD = $("text.highcharts-plot-line-label");
				$XSJD.on("mouseover", function(e){
					$payprogressXSJD.css("display", "block")
						.css("position", "fixed")
						.css("zIndex", "9999")
						.css("left", e.clientX+'px')
						.css("top", e.clientY+'px');
					$payprogressXSJD.find("ul").find("li").find("span").html(ptd_util.progressInYear() + "%");
				}).on("mouseout", function(){
					$payprogressXSJD.css("display", "none");
				});
				
			},
			error: function(){
				$("#tooltipXSJD").css("display", "none");
			}
		});	
	},
	hchart : function(payMoney, lastBgtMoney, fundtypeCode, expfuncCode, selecttime){
		var jsonSeries = [
			{name: '可用指标', data: [lastBgtMoney]},
			{name: '已支付', data: [payMoney]}
		];
		var xsjd = ptd_util.progressInYear();
		
		$('#dwzc').highcharts({
			chart: {type: 'bar'},
			credits: {enabled: false},
			exporting: {enabled: false},
			title: {text: ''},
			xAxis: {　　
				labels: {enabled: true},
				tickWidth: 0, // 次级刻线宽度
				categories: [''],
	            lineWidth: 0,
				lineColor:'#fff'
			},
			yAxis: {
				min: 0,
				tickWidth: 1,
				alternateGridColor: '#FDFFD5',
				lineWidth: 1,
				lineColor:'#000',
				offset: 1,
				gridLineColor: '#FFF', // 辅助轴线颜色
				title: {text: ''},
				labels: { //y轴刻度文字标签
					style:{
						color:'#000'
					},
					formatter: function() {
						return this.value + '%'; //y轴加上%  
					}
				},
		　　		plotLines: [{   //一条延伸到整个绘图区的线，标志着轴中一个特定值。
		　　			color: '#FFF', //'#108EE9',
                    dashStyle: 'solid', //Dash,Dot,Solid,默认Solid
                    width: 0,
                    value: xsjd,  // TODO 序时进度，y轴显示位置
                    zIndex: 5,
                    label: {
                    	text: '▶',//+xsjd+'%',
                        align: 'left',
                        rotation:90,
                        x: -5,
						y: -4,
                        style: {
                            'color': 'blue',
                            'fontWeight': 'bold',
                            'font-size':'20px',
							'cursor':'default'
                        }
                    },
                    events: {
            			mouseover: function(e) {

            			},
            			mouseout: function() {

            			}
            		}
                }]
			},
			tooltip: { // style="color:{series.color}"
				followTouchMove:true,
	            followPointer:true,
				headerFormat: '<small>{point.key}</small><br>',
				pointFormat: '<span >{series.name}</span>: <b>{point.y} (元)</b> ({point.percentage:.0f}%)<br/>',
				shared: false,
				style:{
					
				}
			},
			colors: ['#F8A23C', '#7DC338'],
			legend: {
				reversed: true,
				layout: 'horizontal',
				align: 'right',
				verticalAlign: 'top',
				//x: 200,
				//y: 0,
				floating: false,
				borderWidth: 0,
				backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
				shadow: false
			},
			plotOptions: {
				series: {
					stacking: 'percent',
					dataLabels: {
						enabled: true,
						color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
						style: {
							textShadow: '0 0 3px green'
						},
						formatter: function() {
							return (this.point.percentage).toFixed(2) + '%';
						}
					},
					events: {
						legendItemClick: function() {	// 禁止图例点击
							return false;
						},
						click: function(event) {
							//支付状态
							var zfzt = event.point.series.name;
							var agency = Base64.decode($("#svAgencyCode", parent.document).val());
							var htmlParam = "&fundtypeCode="+fundtypeCode+"&expfuncCode="+expfuncCode+"&agency="+agency;
							if(zfzt == '已支付') {
								window.parent.addTabToParent('已支付', '/df/pay/portalpay/statusquery/payrelated/payInfo.html?billtype=366&busbilltype=311&menuid=132C25064BD2BAE4627573EEA7BB9CA8&menuname=%u652F%u51FA%u8FDB%u5EA6%u652F%u4ED8%u4FE1%u606F&tokenid='+getTokenId()+htmlParam);
							} else if(zfzt == '可用指标') {
								window.parent.addTabToParent('可用指标', '/df/pay/portalpay/input/payQuota.html?billtype=366&busbilltype=311&model=model5&menuid=B249D0506FCAAADDE98A515AB777DD31&menuname=%u652F%u51FA%u8FDB%u5EA6%u6307%u6807%u4FE1%u606F&tokenid='+getTokenId()+htmlParam);
							}	
						}
					}
				}
			},
			series: jsonSeries
		});
	}
};

/**
 * budget
 */
// 预算指标选中的行的序号
var gridSelectObj; // 选中行对象
var focus_row_index; // 选中行序号
var focus_row_sum_id; // 选中行原始数据sum_id
var focus_row_fromctrlid; // 选中行原始数据fromctrlid
var ptd_budget = {
	bf : function(){
		// 预算指标高级查询层操作
		$("#demand").click(function(){
			var isNone = $(".demandContent").css("display");
			if(isNone=="none") {$(".demandContent").css("display", "block");return;}
			if(isNone=="block") $(".demandContent").css("display", "none");
		});
		$("#close").click(function(){
			$(".demandContent").css({"display":"none"});
		});
		// 预算指标 高级查询-更多
		$("._portal_table_gaoji_zbwh_span").on("click", function(){
			$("#_portal_yusuan_gaoji_type_input").val("");
			$("#_portal_table_input").val("");
			$("#zhibiaotree").html("");
			$("#_portal_yusuan_gaoji_type_input").val("zbwh");
			ptd_budget.tree(this.id, $(this).parent().text(), "zbwh");	// 指标文号
			$(".content_wrap").css({"display":"block"});
		});
		$("#budgetTableSelectClear_zbwh").on("click", function(){
			$("#_portal_yusuan_table_gaoji_zbwh_input").val("");
			$("#_portal_yusuan_table_gaoji_zbwh_input_chrid").val("");
		});
		$("._portal_table_gaoji_xmfl_span").on("click", function(){
			$("#_portal_yusuan_gaoji_type_input").val("");
			$("#_portal_table_input").val("");
			$("#zhibiaotree").html("");
			$("#_portal_yusuan_gaoji_type_input").val("xmfl");
			ptd_budget.tree(this.id, $(this).parent().text(), "xmfl");	// 项目分类
			$(".content_wrap").css({"display":"block"});
		});
		$("#budgetTableSelectClear_xmfl").on("click", function(){
			$("#_portal_yusuan_table_gaoji_xmfl_input").val("");
			$("#_portal_yusuan_table_gaoji_xmfl_input_chrid").val("");
		});
		$("._portal_table_gaoji_ysxm_span").on("click", function(){
			$("#_portal_yusuan_gaoji_type_input").val("");
			$("#_portal_table_input").val("");
			$("#zhibiaotree").html("");
			$("#_portal_yusuan_gaoji_type_input").val("ysxm");
			ptd_budget.tree(this.id, $(this).parent().text(), "ysxm");	// 预算项目
			$(".content_wrap").css({"display":"block"});
		});
		$("#budgetTableSelectClear_ysxm").on("click", function(){
			$("#_portal_yusuan_table_gaoji_ysxm_input").val("");
			$("#_portal_yusuan_table_gaoji_ysxm_input_chrid").val("");
		});
		$("._portal_table_gaoji_ysdw_span").on("click", function(){
			$("#_portal_yusuan_gaoji_type_input").val("");
			$("#_portal_table_input").val("");
			$("#zhibiaotree").html("");
			$("#_portal_yusuan_gaoji_type_input").val("ysdw");
			ptd_budget.tree(this.id, $(this).parent().text(), "ysdw");	// 预算单位
			$(".content_wrap").css({"display":"block"});
		});
		$("#budgetTableSelectClear_ysdw").on("click", function(){
			$("#_portal_yusuan_table_gaoji_ysdw_input").val("");
			$("#_portal_yusuan_table_gaoji_ysdw_input_chrid").val("");
		});
		$("._portal_table_gaoji_zffs_span").on("click", function(){
			$("#_portal_yusuan_gaoji_type_input").val("");
			$("#_portal_table_input").val("");
			$("#zhibiaotree").html("");
			$("#_portal_yusuan_gaoji_type_input").val("zffs");
			ptd_budget.tree(this.id, $(this).parent().text(), "zffs");	// 支付方式
			$(".content_wrap").css({"display":"block"});
		});
		$("#budgetTableSelectClear_zffs").on("click", function(){
			$("#_portal_yusuan_table_gaoji_zffs_input").val("");
			$("#_portal_yusuan_table_gaoji_zffs_input_chrid").val("");
		});
		// 预算指标 高级查询 确定按钮
		$("#_portal_table_gaoji_submit_input").on("click", function(){
			var treeNode = "zhibiaotree";
			var treeObj = $.fn.zTree.getZTreeObj(treeNode);  
			var selectedNode = treeObj.getSelectedNodes()[0];  
			$("#_portal_table_input").val(selectedNode.name);  
			$("#_portal_table_input_chrid").val(selectedNode.id);
			
			var type = $("#_portal_yusuan_gaoji_type_input").val();
			var type2Class = {
				"zbwh":"_portal_yusuan_table_gaoji_zbwh_input",
				"xmfl":"_portal_yusuan_table_gaoji_xmfl_input",
				"ysxm":"_portal_yusuan_table_gaoji_ysxm_input",
				"ysdw":"_portal_yusuan_table_gaoji_ysdw_input",
				"zffs":"_portal_yusuan_table_gaoji_zffs_input"
			};
			$("#"+type2Class[type]).val($("#_portal_table_input").val());
			$("#"+type2Class[type]+"_chrid").val($("#_portal_table_input_chrid").val());
			$(".content_wrap").css({"display":"none"});
		});
		// 预算指标 高级查询 关闭按钮
		$("#_portal_table_gaoji_close_input").click(function(){
			$("#_portal_table_input").val("");
			$(".content_wrap").css({"display":"none"});
		});
		$("#close-div").click(function(){
			$(".content_wrap").css({"display":"none"});
		});
		// 预算指标 高级查询 外层提交按钮
		$("#sure").on("click", function(){
			var file_name = $("#_portal_yusuan_table_gaoji_zbwh_input").val();
			var agencyexp_name = $("#_portal_yusuan_table_gaoji_xmfl_input").val();
			var bis_name = $("#_portal_yusuan_table_gaoji_ysxm_input").val();
			var agency_name = $("#_portal_yusuan_table_gaoji_ysdw_input").val();
			var paytype_name = $("#_portal_yusuan_table_gaoji_zffs_input").val();
			var file_name_chrid = $("#_portal_yusuan_table_gaoji_zbwh_input_chrid").val();
			var agencyexp_name_chrid = $("#_portal_yusuan_table_gaoji_xmfl_input_chrid").val();
			var bis_name_chrid = $("#_portal_yusuan_table_gaoji_ysxm_input_chrid").val();
			var agency_name_chrid = $("#_portal_yusuan_table_gaoji_ysdw_input_chrid").val();
			var paytype_name_chrid = $("#_portal_yusuan_table_gaoji_zffs_input_chrid").val();
			
			// 条件拼接
			var condition = all_options_condition;
			if(!isObjNull(file_name)){
				condition += " and file_id= '" + file_name_chrid + "' ";
			}
			if(!isObjNull(agencyexp_name)){
				condition += " and agencyexp_id= '" + agencyexp_name_chrid + "' ";
			}
			if(!isObjNull(bis_name)){
				condition += " and bis_id= '" + bis_name_chrid + "' ";
			}
			if(!isObjNull(agency_name)){
				condition += " and c.agency_id= '" + agency_name_chrid + "' ";
			}
			if(!isObjNull(paytype_name)){
				condition += " and paytype_id= '" + paytype_name_chrid + "' ";
			}
			all_options.condition = condition;
			
			$(".content_wrap").css({"display":"none"});
			$(".demandContent").css({"display":"none"});
			budgetTableGaojiStart = 1;
			ptd_budget.show();
		});
		// 预算指标余额为零
		$("#budgetTableYueNull").on("click", function(){
			if($(this).is(":checked")){
				budgetTableYueNull = 1;
			}else{
				budgetTableYueNull = 0;
			}
			ptd_budget.show();
		});
		
		// 预算指标单位切换
		$("#budgetDanweiChange").change(function(){
			budgetTableRadioWan = $(this).val();
			ptd_budget.show();
		});
		
		// 预算指标右键
		var $budgetWrapper = $("#budgetWrapper");
		$budgetWrapper.css("display", "none");
		$budgetWrapper.bind("contextmenu", function(){ return false;});
		//document.oncontextmenu = function(e){ return false;}
		// 绑定中间滚动事件
		var scrollFunc = function (e) {  
	        e = e || window.event;  
	        if (e.wheelDelta) {  //判断浏览器IE，谷歌滑轮事件               
	        	$budgetWrapper.css("display", "none");
	        } else if (e.detail) {  //Firefox滑轮事件  
	        	$budgetWrapper.css("display", "none");
	        }
	    }  
	    //给页面绑定滑轮滚动事件  
	    if (document.addEventListener) {//firefox  
	        document.addEventListener('DOMMouseScroll', scrollFunc, false);  
	    }  
	    //滚动滑轮触发scrollFunc方法  //ie 谷歌  
	    window.onmousewheel = document.onmousewheel = scrollFunc; 
		
		$("#mainGeriContentDiv").bind("contextmenu", function(e){
			var e = e || window.event;
			var browser = whichBrowser();
			var eventTarget = e.srcElement ? e.srcElement : e.target;
			// 取消全部tr选中状态
			$(this).find("tr").removeClass("u-grid-content-focus-row").removeClass("u-grid-content-sel-row");
			
			// 获取对应focus_row_index和focus_row_sum_id
			if(browser == "IE"){
				var cur = $(eventTarget)[0];
				while(true){
					cur = cur.parentNode;
					if(cur.tagName.toUpperCase() == "TR"){
						break;
					}
				}
				// 行选中状态
				$(cur).addClass("u-grid-content-focus-row").addClass("u-grid-content-sel-row");
				//focus_row_index = $('#gridShow_content_tbody > tr').index(cur);
				focus_row_sum_id = $(cur).find("td")[0].innerText;
				focus_row_fromctrlid = $(cur).find("td")[1].innerText;
			}else if(browser == "Chrome" || brower != ""){ // TODO 暂时只考虑IE和谷歌
				$(eventTarget.closest('tr')).addClass("u-grid-content-focus-row").addClass("u-grid-content-sel-row");
				//focus_row_index = $('#gridShow_content_tbody > tr').index(eventTarget.closest('tr'));
				focus_row_sum_id = ($(eventTarget.closest('tr')).find("td")[0]).innerText;
				focus_row_fromctrlid = ($(eventTarget.closest('tr')).find("td")[1]).innerText;
			}else{
				focus_row_index = -1;
			}
			
			if(focus_row_index < 0){
				// 取消右键点击的默认事件
				return false;
			}
			
			// 自定义右键菜单显示
			$("#budgetWrapper").css("display", "block")
				.css("position", "fixed")
				.css("zIndex", "9999")
				.css("left", e.clientX+'px')
				.css("top", e.clientY-110 +'px');
			return false;
		});
		// 预算指标右键菜单点击
		$budgetWrapper.find("ul").find("li").each(function(){
			$(this).click(function(e){
				var i = $(this).data("index");
				
				//TODO wait 政府采购
				if(i == 6){
					return false;
				}
				$budgetWrapper.css("display", "none");
				// 导出Excel
				if(i == 8){
					var fields = [];
					for(var key in budgetTableColTitle){
						var str = '{' + '"fieldName"' + ':' +'"' + key + '"' + "," + '"title"' + ':' + '"' + budgetTableColTitle[key] + '"' + '}';
						fields.push(JSON.parse(str));
					}
					var params = {
						"type" : "all",
						"fieldMap" : fields,
					};
					export2Excel(viewModel.dataTable, params, null);
					return false;
				}
				
				// 参数确认
				var url;
				var title = $(this).text();
				url = fullUrlWithTokenid(ptd_obj.budget.rightUrl[i]) + "&sum_id=" + focus_row_sum_id;
				// 预算执行情况
				if(i == 7){
					title = "预算执行情况";
					url = fullUrlWithTokenid(ptd_obj.budget.rightUrl[i]) + "&sum_id=" + focus_row_fromctrlid;
				}
				window.parent.addTabToParent(title, url);
			});
		});
		
	},
	_ajax : function(){
		if(budgetTableInit == 1){
			budgetTableInit = 0;
			return true;
		}
		if(budgetTableGaojiStart == 1){ // 首次请求或高级查询
			budgetTableGaojiStart = 0; // 高级查询启动，余额为零条件复原
			budgetTableYueNull = 0; // 余额显示复原
			$("#budgetTableYueNull").removeAttr("checked");
			budgetTableRadioWan = 0; // 单位显示复原
			$("#budgetDanweiChange").html('<option value="0" selected="selected" id="budgetDanweiChangeYuan">元</option><option value="1">万元</option><option value="2">亿元</option>');
			return true;
		}else{	// 未启动高级查询，使用静态数据
			if(budgetTableYueNull == 1){ // 包含余额为零数据
				switch(budgetTableRadioWan){
					case "0":
						budgetTableDatadetail = budgetTableDatadetailOri;
						break;
					case "1":
						budgetTableDatadetail = budgetTableDatadetailWan;
						break;
					case "2":
						budgetTableDatadetail = budgetTableDatadetailYi;
						break;
					default:
						budgetTableDatadetail = budgetTableDatadetailOri;
						break;
				}
			}else{
				switch(budgetTableRadioWan){
					case "0":
						budgetTableDatadetail = budgetTableDatadetailOriNull;
						break;
					case "1":
						budgetTableDatadetail = budgetTableDatadetailWanNull;
						break;
					case "2":
						budgetTableDatadetail = budgetTableDatadetailYiNull;
						break;
					default:
						budgetTableDatadetail = budgetTableDatadetailOriNull;
						break;
				}
			}
			return false;
		}
	},
	show : function(){
		var isNeedAjax = this._ajax();
		if(isNeedAjax){
			$.ajax({
				url: "/df/pay/centerpay/input/getPlanBoundData.do",
				type: "GET",
				dataType: "json",
				async: false,
				data: ip.getCommonOptions(all_options),
				success: function(data){
					budgetTableDatadetailOri = data.dataDetail;
					// 首次加载，显示余额不为零的数据
					if(isNeedAjax){
						ptd_budget.classify();
					}
					budgetTableDatadetail = budgetTableDatadetailOriNull;
				}
			});
		}
		viewModel.dataTable.removeAllRows();
		viewModel.dataTable.setSimpleData(budgetTableDatadetail,{
			unSelect: true
		});
	},
	classify : function(){
		// 数字列为：avi_money, canuse_money
		var length = budgetTableDatadetailOri.length||0;
		
		// 处理显示数据，原始数据为 budgetTableDatadetailOri
		budgetTableDatadetailNull = [];
		budgetTableDatadetailOriNull = [];
		budgetTableDatadetailWan = [];
		budgetTableDatadetailWanNull = [];
		budgetTableDatadetailYi = [];
		budgetTableDatadetailYiNull = [];
		for(var i=0; i<length; i++){
			if(!(budgetTableDatadetailOri[i].canuse_money == "0" ||
				budgetTableDatadetailOri[i].canuse_money == "0.0" ||
				budgetTableDatadetailOri[i].canuse_money == "0.00" ||
				budgetTableDatadetailOri[i].canuse_money == 0 ||
				budgetTableDatadetailOri[i].canuse_money == 0.0 ||
				budgetTableDatadetailOri[i].canuse_money == 0.00)) {
				
				// 余额不为零的原始数据：元
				budgetTableDatadetailOriNull.push(budgetTableDatadetailOri[i]);
			}
		}
		
		// 包含零 - 万/亿元显示
		var lengthOri = budgetTableDatadetailOri.length;
		for(var i=0; i<lengthOri; i++){
			for(var j=0; j<2; j++){
				var obj = {
					"file_name": budgetTableDatadetailOri[i].file_name,
					"agencyexp_name": budgetTableDatadetailOri[i].agencyexp_name,
					"bis_name": budgetTableDatadetailOri[i].bis_name,
					"avi_money":"",
					"canuse_money":"",
					"expfunc_code": budgetTableDatadetailOri[i].expfunc_code,
					"expfunc_name": budgetTableDatadetailOri[i].expfunc_name,
					"expeco_code": budgetTableDatadetailOri[i].expeco_code,
					"expeco_name": budgetTableDatadetailOri[i].expeco_name,
					"fundtype_name": budgetTableDatadetailOri[i].fundtype_name,
					"bgtsource_name": budgetTableDatadetailOri[i].bgtsource_name,
					"agency_code": budgetTableDatadetailOri[i].agency_code,
					"agency_name": budgetTableDatadetailOri[i].agency_name,
					"mb_name": budgetTableDatadetailOri[i].mb_name,
					"budget_summary": budgetTableDatadetailOri[i].budget_summary
				};
				if(j == 0){
					obj.avi_money = (budgetTableDatadetailOri[i].avi_money/1e4).toFixed(2);
					obj.canuse_money = (budgetTableDatadetailOri[i].canuse_money/1e4).toFixed(2);
					budgetTableDatadetailWan.push(obj);
				}
				if(j == 1){
					obj.avi_money = (budgetTableDatadetailOri[i].avi_money/1e8).toFixed(2);
					obj.canuse_money = (budgetTableDatadetailOri[i].canuse_money/1e8).toFixed(2);
					budgetTableDatadetailYi.push(obj);
				}
			}
		}
		
		// 不含零
		var lenthOriNull = budgetTableDatadetailOriNull.length;
		for(var m=0; m<lenthOriNull; m++){
			for(var n=0; n<2; n++){
				var obj = {
					"file_name": budgetTableDatadetailOriNull[m].file_name,
					"agencyexp_name": budgetTableDatadetailOriNull[m].agencyexp_name,
					"bis_name": budgetTableDatadetailOriNull[m].bis_name,
					"avi_money":"",
					"canuse_money":"",
					"expfunc_code": budgetTableDatadetailOriNull[m].expfunc_code,
					"expfunc_name": budgetTableDatadetailOriNull[m].expfunc_name,
					"expeco_code": budgetTableDatadetailOriNull[m].expeco_code,
					"expeco_name": budgetTableDatadetailOriNull[m].expeco_name,
					"fundtype_name": budgetTableDatadetailOriNull[m].fundtype_name,
					"bgtsource_name": budgetTableDatadetailOriNull[m].bgtsource_name,
					"agency_code": budgetTableDatadetailOriNull[m].agency_code,
					"agency_name": budgetTableDatadetailOriNull[m].agency_name,
					"mb_name": budgetTableDatadetailOriNull[m].mb_name,
					"budget_summary": budgetTableDatadetailOriNull[m].budget_summary
				};
				if(n == 0){
					obj.avi_money = (budgetTableDatadetailOriNull[m].avi_money/1e4).toFixed(2);
					obj.canuse_money = (budgetTableDatadetailOriNull[m].canuse_money/1e4).toFixed(2);
					budgetTableDatadetailWanNull.push(obj);
				}
				if(n == 1){
					obj.avi_money = (budgetTableDatadetailOriNull[m].avi_money/1e8).toFixed(2);
					obj.canuse_money = (budgetTableDatadetailOriNull[m].canuse_money/1e8).toFixed(2);
					budgetTableDatadetailYiNull.push(obj);
				}
			}
		}
		
	},
	tree : function(id, title, type){
		var elements = {
			"zbwh":"FILE",	// 指标文号
			"xmfl":"AGENCYEXP",	// 项目分类
			"ysxm":"BIS",	// 预算项目
			"ysdw":"AGENCY",	// 预算单位
			"zffs":"PAYTYPE"	// 支付方式
		};
		var element = elements[type];
		var all_options1 = {
			"element": element,
			"tokenid": getTokenId(),
			"ele_value": "",
			"ajax": "noCache"
		};
		$.ajax({
			url: "/df/dic/dictree.do",
			type: "GET",
			dataType: "json",
			//async: false,
			data: ip.getCommonOptions(all_options1),
			success: function(data) {
				var eleDetail = data.eleDetail;
				var setting = {
					view: {
						dblClickExpand: false,
						showLine: true,
						selectedMulti: false,
						showIcon: false
					},
					data: {
						simpleData: {
							enable:true,
							idKey: "id",
							pIdKey: "pId",
							rootPId: ""
						}
					},
					callback: {  
						onDblClick: function(event, treeId, treeNode){
							$("#_portal_table_gaoji_submit_input").click();
						}
					}
				};
				var zNodes = [];
				for(var i in eleDetail){
					if(!eleDetail.hasOwnProperty(i)){
						continue;
					}
					zNodes.push({id:eleDetail[i].chr_id, pId:eleDetail[i].parent_id, name:eleDetail[i].codename});
				}
				$.fn.zTree.init($("#zhibiaotree"), setting, zNodes);
			}
		});
	},
	tabledbClick : function(obj){
		var gridObj = obj.gridObj;	// 表格控件对象
		var rowIndex = obj.rowIndex;	// 数据行对应的index
		var rowValue = obj.rowObj.value;	// 数据行对象值
		var all_options2 = {
			"tokenid": getTokenId(),
			"file_name": rowValue.file_name,
			"expfunc_name": rowValue.expfunc_name,
			"agencyexp_name": rowValue.agencyexp_name,
			"expeco_name": rowValue.expfunc_name,
			"approve_date": rowValue.approve_date,
			"avi_money": rowValue.avi_money,
			"canuse_money": rowValue.canuse_money,
			"sum_id": rowValue.sum_id
		};
		
		var url = "/df/sd/pay/paymentinfo/paymentinfo.html?tokenid="+getTokenId();
		url += "&file_name="+all_options2.file_name;
		url += "&expfunc_name="+all_options2.expfunc_name;
		url += "&agencyexp_name="+all_options2.agencyexp_name;
		url += "&expeco_name="+all_options2.expeco_name;
		url += "&approve_date="+all_options2.approve_date;
		url += "&avi_money="+all_options2.avi_money;
		url += "&canuse_money="+all_options2.canuse_money;
		url += "&sum_id="+all_options2.sum_id;
		url += "&billtype=366";
		url += "&menuid=B357D1CA7B7E7B8B3D27562EFDDE1B6C";
		window.parent.addTabToParent("指标支付信息", url);
	}
};

/**
 * fiscal
 */
var ptd_fiscal = {
	bf : function(){
		// 财政百度图片效果
		$("div#pic").on("mouseover", function(){
			$(this).find("img").prop("src", "img/icon-cha-w.png");
			$(this).find("img").css("transform", "scale(1.5, 1.5)");
			$(this).css("background-color", "#108ee9");
		}).on("mouseleave", function(){
			$(this).find("img").prop("src", "img/dashboard/search1.png");
			$(this).find("img").css("transform", "scale(1.3, 1.3)");
			$(this).css("background-color", "#FFFFFF");
		});
	},
	set : function(){
		this.bf();
		$("._portal_fiscal_sub").on("click", function(){
			var param = $("#_portal_fiscal_input").val();
			if(isObjNull(param)){
				alert("请输入查询内容");
				return;
			}
			window.parent.addTabToParent("财政百度", ptd_obj.fiscal.FISCAL_URL_PUB + param + "&agencycode="+Base64.decode($("#svAgencyCode", parent.document).val()) );
		});
	}
};

/**
 * fundmonitor
 */
var ptd_fundmonitor = {
	bf : function(){
		// 资金监控页签切换
		$('#head-r >span').each(
		    function(index){
		        $(this).click(function(){
		            $('.tab2').addClass('hidden');
		            $('.tab2:eq('+index+')').removeClass('hidden');
		            $('#head-r > span').removeClass('ac');
		            $('#head-r > span:eq('+index+')').addClass('ac');
		        })
		    }
		);
	},
	show : function(){
		this.bf();
		var _zhifu = "",
			_jiankong = "",
			isBudgetOk = 0,
			isGuokuOk = 0,
			all_options3 = {
				"tokenid":getTokenId()
			};
		// 支付
		$.ajax({
			url: "/df/pay/search/mainpage/getEnPayHeadPage.do",
			type: "GET",
			dataType: "json",
			async: false,
			data: ip.getCommonOptions(all_options3),
			success: function(data) {
				_zhifu = data.dataDetail;
				isBudgetOk = 1;
			},
			error:function(){
				//console.log("-- 支付信息调用失败");
				isBudgetOk = 2;
			}
		});
		// 监控
		$.ajax({
			url: "/df/fi_fip/inspectanalysis/queryRuleAnaylysisDataSum.do",
			type: "GET",
			dataType: "json",
			async: false,
			data: ip.getCommonOptions(all_options3),
			success: function(data) {
				_jiankong = (data.dataDetail)[0];
				isGuokuOk = 1;
			},
			error:function(){
				//console.log("-- 监控信息调用失败");
				isBudgetOk = 2;
			}
		});	
		// 确认ajax全部跳出
		var fundmonitor_dbAjax = setTimeout(function() {
			if(isBudgetOk==2){	// 支付异常
				isBudgetOk = 1;
				_zhifu = {"result":"","flag":true,
					"dataDetail":[
						{"val":[{"count":"0","money":"0","menu_name":"已支付","type":"day","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=day"},{"count":"0","money":"0","menu_name":"已支付","type":"month","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=month"},{"count":"0","money":"0","menu_name":"已支付","type":"month","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=year"}],"name":"已支付"},
						{"val":[{"count":"0","money":"0","menu_name":"已发送","type":"day","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=day"},{"count":"0","money":"0","menu_name":"已发送","type":"month","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=month"},{"count":"0","money":"0","menu_name":"已发送","type":"month","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=year"}],"name":"已发送"},
						{"val":[{"count":"0","money":"0","menu_name":"被退回","type":"day","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=day"},{"count":"0","money":"0","menu_name":"被退回","type":"month","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=month"},{"count":"0","money":"0","menu_name":"被退回","type":"month","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=year"}],"name":"被退回"},
						{"val":[{"count":"0","money":"0","menu_name":"已退款","type":"day","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=day"},{"count":"0","money":"0","menu_name":"已退款","type":"month","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=month"},{"count":"0","money":"0","menu_name":"已退款","type":"month","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=year"}],"name":"已退款"},
						{"val":[{"count":"0","money":"0","menu_name":"在途","type":"day","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=day"},{"count":"0","money":"0","menu_name":"在途","type":"month","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=month"},{"count":"0","money":"0","menu_name":"在途","type":"month","url":"/df/pay/portal/statusquery/payed/agencyPayed.html?billtype=364&busbilltype=344&type=year"}],"name":"在途"}
					]};
			}
			if(isGuokuOk==2){	// 监控异常
				isGuokuOk = 1;
				_jiankong = {"result":"","flag":true,"dataDetail":[{"val":[{"count":"0","money":"0","type":"day","url":"/df/fi_fip/portalpage/fifipRuleAnalysis.html?type=day"},{"count":"0","money":"0","type":"month","url":"/df/fi_fip/portalpage/fifipRuleAnalysis.html?type=month"},{"count":"0","money":"0","type":"year","url":"/df/fi_fip/portalpage/fifipRuleAnalysis.html?type=year"}],"name":"疑点数"}]};
			}
			if(isBudgetOk==1 && isGuokuOk==1){
				clearTimeout(fundmonitor_dbAjax);
			}
		}, 100);
		
		var table_head = '<tr style="font-size:15px; font-weight:bold;"><td width="100px"align="center">状态</td><td width="70px"align="center">笔数</td><td width="160px"align="center">金额';
		var html_day = table_head+'（元）</td></tr>';
		var html_month = table_head+'（万元）</td></tr>';
		var html_year = table_head+'（万元）</td></tr>';
		// 支付，前五行
		for(var i in _zhifu){
			var _zhifu_one = (_zhifu[i]).val;
			var tr_name = (_zhifu[i]).name;
			for(var n in _zhifu_one){
				var _tr_html = '<tr onclick="javascript:window.parent.addTabToParent(&quot;'+_zhifu_one[n].menu_name+'&quot;, &quot;'+_zhifu_one[n].url+'&tokenid='+getTokenId()+'&quot;);"><td>'+tr_name+'</td><td>'+_zhifu_one[n].count+' 笔</td><td align="right" class="_font_color_fundmonitor">';
				if(_zhifu_one[n].type=='day') 
					html_day += _tr_html + ip.dealThousands(_zhifu_one[n].money)+'</td></tr>';
				if(_zhifu_one[n].type=='month') 
					html_month += _tr_html + ip.dealThousands((_zhifu_one[n].money/1e4))+'</td></tr>';
				if(_zhifu_one[n].type=='year') 
					html_year += _tr_html + ip.dealThousands((_zhifu_one[n].money/1e4))+'</td></tr>';
			}
		}
		// 监控，最后一行
		var _jiankong_name = _jiankong.name;
		var _jiankong_val = _jiankong.val;
		for(var m in _jiankong_val){
			var _tr_html = '<tr onclick="javascript:window.parent.addTabToParent(&quot;'+_jiankong_val[m].menu_name+'&quot;, &quot;'+_jiankong_val[m].url+'&tokenid='+getTokenId()+'&quot;);"><td>'+_jiankong_name+'</td><td>'+_jiankong_val[m].count+' 笔</td><td align="right" class="_font_color_fundmonitor">';
			if(_jiankong_val[m].type=='day') 
				html_day += _tr_html + ip.dealThousands(_jiankong_val[m].money)+'</td></tr>';
			if(_jiankong_val[m].type=='month') 
				html_month += _tr_html + ip.dealThousands((_jiankong_val[m].money/1e4))+'</td></tr>';
			if(_jiankong_val[m].type=='year') 
				html_year += _tr_html + ip.dealThousands((_jiankong_val[m].money/1e4))+'</td></tr>';
		}
		$("#fundmonitor_tab_day table").html(html_day);
		$("#fundmonitor_tab_month table").html(html_month);
		$("#fundmonitor_tab_year table").html(html_year);
		
		// 资金监控鼠标滑动效果
		$("#fundmonitor_tab_day").find("tr").each(function(i){ ptd_fundmonitor.mouseover(i, this);});
		$("#fundmonitor_tab_month").find("tr").each(function(i){ ptd_fundmonitor.mouseover(i, this);});
		$("#fundmonitor_tab_year").find("tr").each(function(i){ ptd_fundmonitor.mouseover(i, this);});
	},
	mouseover : function(i, obj){
		if(i>0){ // 跳过标题行
			$(obj).on("mouseover", function(){ 
				$(obj).css("background-color", "#108EE9").css("color", "#FFFFFF").css("text-decoration", "underline");
				$(obj).find("td._font_color_fundmonitor").css("color", "#FFFFFF");
			}).on("mouseleave", function(){ 
				var bgcolor = i%2==0?"#E9E9E9":"#FFFFFF"; // 奇偶行不同色
				$(obj).css("background-color", bgcolor).css("color", "#000000").css("text-decoration", "none");
				$(obj).find("td._font_color_fundmonitor").css("color", "#F56A00");
			});
		}
	}
};

/**
 * dealing
 */
var ptd_dealing = {
	get : function(){
		var params = {
			tokenid : getTokenId(),
			userid : $("#svUserId", parent.document).val(),
			roleid : $("#svRoleId", parent.document).val(),
			region : $("#svRgCode", parent.document).val(),
			year : $("#svSetYear", parent.document).val()
		};
		var dealingThing = {};
		$.ajax({
			url : "/df/portal/getDealingThing.do",
			type : "GET",
			data : params,
			dataType : "json",
			async: false,
			success : function(data){
				dealingThing = data.dealingThing;
			}
		});
		return dealingThing;
	},
	show : function(selectTitle, selectMenuid, setHeight, setElementId){
		var dealingThing = this.get();
		var html = "";
		if(dealingThing){
			var selectName = selectTitle,
				selectMenuid = selectMenuid;
			var dealingLength = dealingThing.length,
				selectMenuidLength = selectMenuid.length;
			// 待办区域高度
			var height = 50;
			// 获取指定待办，匹配menuid
			for(var n=0; n<selectMenuidLength; n++){
				var isOk = 0;
				for(var i=0; i<dealingLength; i++){
					var menuid = dealingThing[i].menu_id;
					if(!(menuid==selectMenuid[n])){
						continue;
					}
					var name = (dealingThing[i].menu_name).replace(/[\n]/g, ""),
						url = fullUrlWithTokenid(dealingThing[i].menu_url)+'&menuid='+menuid+'&menuname='+escape(name),
						task = dealingThing[i].task_content,
						title = selectName[n] + " " + task;
					
					// 特定页面
					if(n == 6){
						name = '我的单据';
						url = '/df/sd/pay/order/order.html?billtype=366&busbilltype=322&menuid=2E8B00AE30A562200CC558307069B4D9&menuname=%u6211%u7684%u5355%u636E&wfStatus=201&tokenid='+getTokenId();
					}
					
					// <li><span class="icon1"></span><a>...
					html += '<li><a href="javascript:window.parent.addTabToParent(&quot;'+name+'&quot;, &quot;'+url+'&quot;);" title="'+ title +'">'+ selectName[n] +' <span class="c-red">'+ task +'</span></a></li>';
					isOk = 1;
					height += 28;
					break;
				}
				if(isOk == 1){
					isOk = 0;
					continue;
				}
			}
		}
		
		if(!isObjNull(html)){
			//$("div.cen2").css("height", height + "px");
			if(!ptd_util.isNull(setHeight)){
				$("div.cen2").css("height", setHeight);
			}
			$("#"+setElementId).find("ul").html("").append(html);
		}
		
		//ptd_d.dealing.height();
		
	},
	refresh : function(){ // 部分更新
		this.show();
	},
	height : function(){ // 调整区域高度
		// 常用操作
		var height_often = $(".cen-1").css("height"),
			height_payprogress = $(".cen-2").css("height"),
			height_budget = $(".cen-3").css("heiht"),
			height_fiscal = $("#search").css("height"),
			height_funcmonitor = $(".cen1").css("height"),
			height_article = $(".cen3").css("height");
		var $article = $("div.cen2");
		$article.css("height", height_often + height_payprogress + height_budget - height_fiscal - height_funcmonitor - height_article);
	}
};

/**
 * article
 */
var ptd_article = {
	show : function(){
		$("#_portal_article_more").on("click", function(){
			window.parent.addTabToParent("公告信息", fullUrlWithTokenid(ptd_obj.article.ARTICLE_URL_MORE));
		});
		$("#_portal_article_add").on("click", function(){
			window.parent.addTabToParent("公告创建", fullUrlWithTokenid(ptd_obj.article.ARTICLE_URL_CREATE));
		});
		
		var params = {
			ruleID:'getArticleData',
			pgPletId:'16',
			userId:'sa',
			start:'0',
			limit:'6'
		};
	    $.ajax({
	    	url: "/portal/GetPageJsonData.do?tokenid=" + getTokenId(),
	        type: 'GET',
	        data :params,
	        dataType : 'json',
	        success: function(result){
	        	//var ss = result.dataList;
	        	var html = "";
	        	var path ="../../common/articleDetail.jsp?";
	        	for(var i=0;i<3;i++){
	        		var name = (result[i].article_title).replace(/(^\s+)|(\s+$)/g, "");
	        		var url = path+'articleId='+result[i].article_id+'&title='+name+'&tokenid='+getTokenId();
	        		html+= '<li style="width:80%;"><span class="icon1"></span><a href="javascript:window.parent.addTabToParent(&quot;'+name+'&quot;, &quot;'+url+'&quot;);" title="'+name+'">'+name+'</a></li>';
	        	}
	        	$("#m-content").find("ul").html(html);
	        }
	    });
	}
};

var ptd_util = {
	/**
	 * 对象是否为空
	 */
	isNull : function(obj){
		if (obj === null) return true;
		if (obj === undefined) return true;
		if (obj === "undefined") return true;
		if (obj === "") return true;
		if (obj === "[]") return true;
		if (obj === "{}") return true;
		return false;
	},
	/**
	 * 序时进度，参数格式 2017/07/07
	 */
	progressInYear : function(_time){
		var _now = _time;
		if(!_time){
			_now = new Date();
		}
		var firstDay = new Date(_now.getFullYear(), 0, 1);
		var dateDiff = _now - firstDay;
		var msPerDay = 1000 * 60 * 60 * 24;
		// 计算天数
		var passDay =  Math.ceil(dateDiff/msPerDay);
		// 今年天数
		var _yearday = 365;
		var _year = 2000 + parseInt(_now.getYear());
		if (((_year % 4)==0) && ((_year % 100)!=0) || ((_year % 400)==0)) {
			_yearday += 1;
		} 
		// 序时进度
		return ((passDay/_yearday)*100).toFixed(2); // 两位小数
	}
};

function Portal_d(){
	this.obj = ptd_obj;
	this.often = ptd_often;
	this.pp = ptd_payprogress;
	this.budget = ptd_budget;
	this.fiscal = ptd_fiscal;
	this.fm = ptd_fundmonitor;
	this.dl = ptd_dealing;
	this.at = ptd_article;
	this.util = ptd_util;
}

/**
 * 判断tokenid前是否已存在参数
 * @params url
 * @return 拼接好tokenid的url
 */ 
function fullUrlWithTokenid(url){
	if(url == null || url == undefined)
		return url;
	url = url.replace(/\s/g, "");
	if(url.indexOf("?") > -1)
		return url + "&tokenid=" + getTokenId();
	if(url.indexOf("?") < 0)
		return url + "?tokenid=" + getTokenId();
}

// 对象为空
function isObjNull(obj){
	if(obj != null || obj != "" || obj != undefined) return false;
	var length = obj.length;
	if(!length) return true; // undefined
	if(length == 0)	return true;
	var count = 0;
	for(var i = 0; i < length; i++){
		if(obj[i] == null || obj[i] == "" || obj[i] == undefined)
			count += 1;
	}
	return count == length ? true : false;
}

/**
 * 获取当前时间
 * @params pp: 支付进度
 */
function datetimeSpe(type) {
	var SEP_1 = "-";
	var SEP_2 = ":";
	var myDate = new Date();
	var Year = myDate.getFullYear();
	var Month = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
	Month = Month>=1&&Month<=9 ? "0"+Month : Month;
	var Today = myDate.getDate(); //获取当前日(1-31)
	Today = Today>=1&&Today<=9 ? "0"+Today : Today;
	var Day = myDate.getDay();
	if ("pp" == type) {
		return Year + SEP_1 + Month + SEP_1 + Today; // 2017-07-22
	}
//var Hour = myData.getHours();
//var Minute = myDate.getMinutes();
//var Second = myDate.getSeconds();
	
	
}

/**
 * 手动更新资金监控
 */
function refreshFundmonitor(){
	ptd_fundmonitor.show();
}
// 五分钟自动刷新
setInterval(function() {
	refreshFundmonitor();
}, 5*60*1000);

/**
 * 实时更新待办事项
 */
function refreshDealingDashboardPart(menuid){
	//ptd_dealing.refresh(menuid);
}

/**
 * 手动更新待办
 */
function refreshDealing(){
	ptd_dealing.refresh();
}

/**
 * excel工具
 */
//Excel_util = {
//	capital : "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
//	digit : [1, 26, 676, 17576],
//	capital_zero : "-0ABCDEFGHIJKLMNOPQRSTUVWXYZ",
//	//导出dataTable数据到Excel文件中
//	export2Excel : function(dataTable, options, fileName){
//		if (!(dataTable instanceof u.DataTable)) {
//	        ip.ipInfoJump("dataTable参数不正确!", "error");
//	        return;
//	    }
//	    options = options || {};
//	    options.type = options.type === "select" ? options.type : "all";
//	    options.fieldMap = (options.fieldMap instanceof Array ? options.fieldMap : []);
//	    fileName = fileName || "导出文件" + this.getCurrentDate() + ".xlsx";
//	    if (fileName.slice(-5).toLowerCase() != '.xlsx') {
//	        fileName = fileName + ".xlsx";
//	    }
//	    var sheet = this.dtData2Sheet(dataTable, options);
//	    this.saveExcelFile(sheet, fileName);
//	},
//	getCurrentDate : function(){
//		var d = new Date();
//	    return "" + d.getFullYear() +
//	        (d.getMonth() < 9 ? "0" : "") + (d.getMonth() + 1) +
//	        (d.getDate() < 10 ? "0" : "") + d.getDate() +
//	        (d.getHours() < 10 ? "0" : "") + d.getHours() +
//	        (d.getMinutes() < 10 ? "0" : "") + d.getMinutes() +
//	        (d.getSeconds() < 10 ? "0" : "") + d.getSeconds();
//	},
//	//将dataTable中数据转为Sheet数据格式
//	dtData2Sheet : function(dataTable, options){
//		var rows = (options.type === "select" ? dataTable.getSelectedRows() : dataTable.getAllRows());
//	    var sheet = {};
//	    var fields = options.fieldMap;
//	    for (var h = 0; h < fields.length; h++) {
//	        if (fields[h] && fields[h].fieldName) {
//	            sheet[this.index2ColName(h + 1) + "1"] = {"v": (fields[h].title ? fields[h].title : fields[h].fieldName)};
//	        }
//	    }
//	    for (var r = 0; r < rows.length; r++) {
//	        for (var c = 0; c < fields.length; c++) {
//	            if (fields[c] && fields[c].fieldName) {
//	                var v = rows[r].getValue(fields[c].fieldName);
//	                if (v) {
//	                    sheet[this.index2ColName(c + 1) + (r + 2)] = {"v": v};
//	                }
//	            }
//	        }
//	    }
//	    sheet["!ref"] = "A1:" + this.index2ColName(Math.max(fields.length, 1)) + (r + 1);
//	    return sheet;
//	},
//	//index转为列名，如：28 转为 AB
//	index2ColName : function(index){
//		var colName = "";
//	    var j = 0;
//	    for (var i = this.digit.length - 1; i >= 0; i--) {
//	        j = Math.floor(index / this.digit[i]);
//	        if (j > 0) {
//	            colName += this.capital[j - 1];
//	            index = index % this.digit[i];
//	        } else {
//	            if (colName.length > 0) {
//	                colName += "0"
//	            }
//	        }
//	    }
//	    colName = colName.split("");
//	    for (var x = colName.length - 1; x >= 0; x--) {
//	        if (colName[x] == "0") {
//	            if (colName.join("").substring(0, x).replace(/0/g, "") != "") { //向高位借位处理0
//	                colName[x] = "Z";
//	                colName[x - 1] = this.capital_zero[this.capital_zero.indexOf(colName[x - 1]) - 1];
//	            } else {
//	                break;
//	            }
//	        } else if (colName[x] == "-") {  //向高位借位，还低位的借位
//	            colName[x] = "Y";
//	            colName[x - 1] = this.capital_zero[this.capital_zero.indexOf(colName[x - 1]) - 1];
//	        }
//	    }
//	    return colName.join("").replace(/0/g, "");
//	},
//	//以下载文件的方式保存导出的Excel文件
//	saveExcelFile : function(sheet, fileName){
//		var wb = {
//	        SheetNames: ['Sheet1'],
//	        Sheets: {
//	            'Sheet1': sheet
//	        }
//	    };
//	    var wopts = {bookType: 'xlsx', bookSST: false, type: 'binary'};
//	    var wbout = XLSX.write(wb, wopts);
//
//	    saveAs(new Blob([s2ab(wbout)], {type: ""}), fileName);
//	}
//}
var capital = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
var digit = [1, 26, 676, 17576];
var capital_zero = "-0ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//导出dataTable数据到Excel文件中
function export2Excel(dataTable, options, fileName) {
    if (!(dataTable instanceof u.DataTable)) {
        ip.ipInfoJump("dataTable参数不正确!", "error");
        return;
    }
    options = options || {};
    options.type = options.type === "select" ? options.type : "all";
    options.fieldMap = (options.fieldMap instanceof Array ? options.fieldMap : []);
    fileName = fileName || "导出文件" + getCurrentDate() + ".xlsx";
    if (fileName.slice(-5).toLowerCase() != '.xlsx') {
        fileName = fileName + ".xlsx";
    }

    var sheet = dtData2Sheet(dataTable, options);
    saveExcelFile(sheet, fileName);
}
//以下载文件的方式保存导出的Excel文件
function saveExcelFile(sheet, fileName) {
    var wb = {
        SheetNames: ['Sheet1'],
        Sheets: {
            'Sheet1': sheet
        }
    };
    var wopts = {bookType: 'xlsx', bookSST: false, type: 'binary'};
    var wbout = XLSX.write(wb, wopts);
    saveAs(new Blob([s2ab(wbout)], {type: ""}), fileName);
}
//将dataTable中数据转为Sheet数据格式
function dtData2Sheet(dataTable, options) {
    var rows = (options.type === "select" ? dataTable.getSelectedRows() : dataTable.getAllRows());
    var sheet = {};
    var fields = options.fieldMap;
    for (var h = 0; h < fields.length; h++) {
        if (fields[h] && fields[h].fieldName) {
            sheet[index2ColName(h + 1) + "1"] = {"v": (fields[h].title ? fields[h].title : fields[h].fieldName)};
        }
    }
    for (var r = 0; r < rows.length; r++) {
        for (var c = 0; c < fields.length; c++) {
            if (fields[c] && fields[c].fieldName) {
                var v = rows[r].getValue(fields[c].fieldName);
                if (v) {
                    sheet[index2ColName(c + 1) + (r + 2)] = {"v": v};
                }
            }
        }
    }
    sheet["!ref"] = "A1:" + index2ColName(Math.max(fields.length, 1)) + (r + 1);
    return sheet;
}
//String转换为ArrayBuffer
function s2ab(s) {
    var buf = new ArrayBuffer(s.length);
    var view = new Uint8Array(buf);
    for (var i = 0; i < s.length; ++i) {
        view[i] = s.charCodeAt(i) & 0xFF;
    }
    return buf;
}
//index转为列名，如：28 转为 AB
function index2ColName(index) {
    var colName = "";
    var j = 0;
    for (var i = digit.length - 1; i >= 0; i--) {
        j = Math.floor(index / digit[i]);
        if (j > 0) {
            colName += capital[j - 1];
            index = index % digit[i];
        } else {
            if (colName.length > 0) {
                colName += "0"
            }
        }
    }
    colName = colName.split("");
    for (var x = colName.length - 1; x >= 0; x--) {
        if (colName[x] == "0") {
            if (colName.join("").substring(0, x).replace(/0/g, "") != "") { //向高位借位处理0
                colName[x] = "Z";
                colName[x - 1] = capital_zero[capital_zero.indexOf(colName[x - 1]) - 1];
            } else {
                break;
            }
        } else if (colName[x] == "-") {  //向高位借位，还低位的借位
            colName[x] = "Y";
            colName[x - 1] = capital_zero[capital_zero.indexOf(colName[x - 1]) - 1];
        }
    }
    return colName.join("").replace(/0/g, "");
}
function getCurrentDate() {
    var d = new Date();
    return "" + d.getFullYear() +
        (d.getMonth() < 9 ? "0" : "") + (d.getMonth() + 1) +
        (d.getDate() < 10 ? "0" : "") + d.getDate() +
        (d.getHours() < 10 ? "0" : "") + d.getHours() +
        (d.getMinutes() < 10 ? "0" : "") + d.getMinutes() +
        (d.getSeconds() < 10 ? "0" : "") + d.getSeconds();
}


