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
		return tokenid.substring(8);
	}
	return "";
}

/**
 * page key
 */
var ptd_jzzf_obj = {
	oftenUrl : {
		"0" : [ // 我要办
			// 转移性专项支付申请批量代编
			"",
			// 非部门预算单位支付申请代编
			"",
			// 授权支付汇总清算额度通知单生成
			"",
			// 授权支付汇总清算额度通知单打印
			"",
			// 预算单位额度到账通知书生成
			"",
			// 直接支付汇总清算额度通知单生成
			"",
			// 直接支付汇总清算额度通知单打印
			"",
			// 工资数据预导入
			"",
			// 直接支付凭证生成
			"",
			// 直接支付凭证打印
			"",
			// 工资支付凭证生成
			"",
			// 工资支付凭证打印
			"",
			// 非部门预算单位指标文件上传
			"",
			// 非部门预算单位直接支付汇总清算单excel表上传
			"",
			// 直接支付入账通知书生成
			"",
			// 授权支付入账通知书生成
			"",
			// 调度款录入
			"/df/sd/pay/realpay/input/dispatchFundsInput.html?billtype=378&realbilltype=325&busbilltype=330&acctype=71"
		],
		"1" : [ // 我要登
			
		],
		"2" : [ // 我要查
			
		],
		"3" : [ // 我要问
			
		]
	
	},
	dealing : {
		title : [
			"单位额度到账通知书登记",
			"授权支付退款通知书",
			"财政授权支付入账通知书登记",
			"财政授权支付退款入账通知",
			"财政直接支付入账通知书登记",
			"财政直接支付退款通知书"
		],
		menuid : [
			// 单位额度到账通知书登记  
			"",
			// 授权支付退款通知书             
			"",
			// 财政授权支付入账通知书登记     
			"",
			// 财政授权支付退款入账通知       
			"",
			// 财政直接支付入账通知书登记     
			"",
			// 财政直接支付退款通知书         
			""
		]
	},
	fiscal : {
		FISCAL_URL : portal_fiscal_url
	}
};

var portal_jzzf_often = {
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
	set : function(){
		this.bf();
		var oftenUrl = isObjNull(ptd_jzzf_obj.oftenUrl) ? {} : ptd_jzzf_obj.oftenUrl;
		$("div.hidContent").each(function(i){ // 单分类div
			$(this).find("li").each(function(n){ // 单功能li
				$(this).on("click", function(){
					window.parent.addTabToParent($(this).find("a").text(), fullUrlWithTokenid((oftenUrl[i])[n]));
				});
			});
		});
	}
};

/**
 * 国库动态监控
 */
var portal_jzzf_dtjk = {
	bf : function() {
		
	},
	show : function() {
		this.bf();
		$('#yjqs').highcharts({
			chart: {
				type: 'line'
			},
			exporting: {
				enabled: false
			},
			title: {
				text: '预警趋势图'
			},
			xAxis: {
				categories: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
			},
			yAxis: {
			 	title: {
	            align: 'high',
	            offset: 0,
	            text: '单位：次数',
	            rotation: 0,
	            y: -25
	           }
			},
			credits: {
				enabled: false
			},
			legend: {
				reversed: false,
				layout: 'horizontal',
				align: 'center',
				verticalAlign: 'top',
				x: 200,
				y: 30,
				floating: false,
				borderWidth: 0,
				backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
				shadow: false
			},
			colors: ['#91CCF1', '#7EB29E', '#FFBF00'],
			plotOptions: {
				line: {
					dataLabels: {
						enabled: true // 开启数据标签
					},
					enableMouseTracking: false // 关闭鼠标跟踪，对应的提示框、点击事件会失效
				}
			},
			series: [{
					name: '用款计划',
					data: [125, 120, 250, 240, 260, 375, 125, '', '', '', '', '']
				}, {
					name: '直接支付',
					data: [25, 20, 50, 40, 60, 75, 25, '', '', '', '', '']
				}, {
					name: '授权支付',
					data: ['', '', '', '', '', '', '', '', '', '', '']
				}

			]
		});
	}
};

/**
 * 支出进度
 */
var portal_jzzf_payprogress = {
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
			portal_jzzf_payprogress.show();
		});
		$("._portal_zhichu_select_zclx_select").on("change", function(e){
			portal_jzzf_payprogress.show();
		});
		$("#budgetTime").on("change", function(e){
			portal_jzzf_payprogress.show();
		});
		$("#_portal_fiscal_input").on("focus", function(){
			$(this).prop("placeholder", "");
		}).on("blur",function(){
			if(!$(this).prop("value")){
				$(this).prop("placeholder", "请输入关键词");
			}
		});
	},
	show : function(){
		var fundtypeCode = $("#fundtypeCode").val();
		var expfuncCode = $("#expfuncCode").val();
		var selecttime = $("#budgetTime").val();
		var agency = Base64.decode($("#svAgencyCode", parent.document).val());
		$.ajax({
			url: "/df/portal/dubbo/payProgress.do",
			type: "GET",
			dataType: "json",
			data: {"tokenid":getTokenId(),"fundtypeCode":fundtypeCode,"expfuncCode":expfuncCode,"selecttime":selecttime,"agency":agency},
			success: function(data) {
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
				portal_jzzf_payprogress.hchart(payMoney, lastBgtMoney, fundtypeCode, expfuncCode, selecttime);
				
				// 序时进度
				$payprogressXSJD = $("#payprogressXSJD");
				$payprogressXSJD.css("display", "none");
				$XSJD = $("text.highcharts-plot-line-label");
				$XSJD.on("mouseover", function(e){
					$payprogressXSJD.css("display", "block")
						.css("position", "fixed")
						.css("zIndex", "9999")
						.css("left", e.clientX+'px')
						.css("top", e.clientY+'px');
					$payprogressXSJD.find("ul").find("li").find("span").html(progressInYear() + "%");
				}).on("mouseout", function(){
					$payprogressXSJD.css("display", "none");
				});
				
			}
		});	
	},
	hchart : function(payMoney, lastBgtMoney, fundtypeCode, expfuncCode, selecttime){
		var jsonSeries = [
			{name: '可用指标', data: [lastBgtMoney]},
			{name: '已支付', data: [payMoney]}
		];
		var xsjd = progressInYear();
		
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
 * 资金监控
 */
var portal_jzzf_fundmonitor = {
	bf : function() {
		// 资金监控页签切换
		$('#head-r >span').each(
		    function(index){
		        $(this).click(function(){
		            $('.tab1').addClass('hidden');
		            $('.tab1:eq('+index+')').removeClass('hidden');
		            $('#head-r > span').removeClass('ac');
		            $('#head-r > span:eq('+index+')').addClass('ac');
		        })
		    }
		);
	},
	show : function() {
		
	}
};

/**
 * 待办事项
 */
var portal_jzzf_dealing = {
	bf : function() {
		
	},
	show : function() {
		
	}
};

/**
 * 公告
 */
var portal_jzzf_article = {
	bf : function() {
		
	},
	show : function() {
		
	}
};

$(function(){
	// 常用操作
	portal_jzzf_often.set();
	// 国库动态监控
	portal_jzzf_dtjk.show();
	// 支出进度
	portal_jzzf_payprogress.bf();
	portal_jzzf_payprogress.show();
	// 资金监控
	portal_jzzf_fundmonitor.bf();
	portal_jzzf_fundmonitor.show();
	// 待办事项
	portal_jzzf_dealing.bf();
	//portal_jzzf_dealing.show();
	// 公告
	portal_jzzf_article.bf();
	portal_jzzf_article.show();
});

/**
 * 对象为空
 */
function isObjNull(obj){
	if(obj != null && obj != "" && obj != undefined) return false;
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
 * 序时进度
 */
function progressInYear(_time) {
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
}


