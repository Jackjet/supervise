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
var ptd_ywcs_obj = {
	oftenUrl : {
		wyb : [
			// 年初控制数录入
			"/df/useablebudget/control/controlInput.html?billtype=141105&amp;inputtype=21&amp;workflowtype=1&amp;is_ncs=1&amp;menuid=AD42A528973650DDC01EFB7D6FBDCC24&amp;menuname=%u5E74%u521D%u63A7%u5236%u6570%u5F55%u5165",
			// 专项指标录入
			"/df/useablebudget/specialbudget/specialBudgetInput.html?billtype=141101&amp;budgettype=141001&amp;inputtype=0&amp;menuid=EC0D16D7A4FDFDC7782222E3DC9BB651&amp;menuname=%u4E13%u9879%u6307%u6807%u5F55%u5165",
			// 用款计划代编
			"/df/pay/plan/input/plinput.html?billtype=261&amp;pk=centerPay&amp;menuid=B67BC0FC37E3B636272D190C6D6F9B76&amp;menuname=%u7528%u6B3E%u8BA1%u5212%u5F55%u5165"
		],
		wyc : [
			// 财政直接支付申请书查询
			"",
			// 国库集中支付年终结余资金对账单查询
			"",
			// 财政授权支付凭证查询
			"",
			// 指标明细查询
			"",
			// 支付名称查询
			""
		],
		wyw : [
			// xx
			"",
			// xx
			""
		]
	
	},
	dealing : {
		title : [
			"单位控制数指标审核",
			"指标审核",
			"专项指标审核"
		],
		menuid : [
			//"单位控制数指标审核",
			"",
			//"指标审核",
			"",
			//"专项指标审核",
			""
		]
	},
	fiscal : {
		FISCAL_URL : portal_fiscal_url
	}
};

/**
 * 专项指标余额
 */
var ptd_ywcs_zbye = {
	set : function(){
		$('#zbye').highcharts({
			chart: {
				plotBackgroundColor: null,
				plotBorderWidth: null,
				plotShadow: false
			},
			title: {
				text: ''
			},
			tooltip: {
				headerFormat: '{series.name}<br>',
				pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
			},
			exporting: {
				enabled: false
			},
			credits: {
				enabled: false
			},
			legend: {
				reversed: true,
				layout: 'vertical',
				align: 'center',
				verticalAlign: 'center',
				x: 120,
				y: 10,
				floating: false,
				borderWidth: 0,
				backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
				shadow: false
			},
			 plotOptions: {
				pie: {
					allowPointSelect: true,
					cursor: 'pointer',
					dataLabels: {
						enabled: true,
						format: '{point.y}',
					},
					showInLegend: true
				}
			},
			
			colors: ['#7ED321', '#108EE9', '#F56A00'
			],
			series: [{
				type: 'pie',
				name: '指标余额',
				data: [
					['本年', 16000.00],
					['1~2年', 3000.00],
					['2年以上', 1000.00],
				]
			}]
		});
	}
};

/**
 * 我要办
 */
var ptd_ywcs_wyb = {
	set : function(){
		$("div#woyaoban").find("a").each(function(i){
			$(this).click(function(){
				window.parent.addTabToParent($(this).text(), fullUrlWithTokenid(ptd_ywcs_obj.oftenUrl.wyb[i]));
			});
		});
	}
};

/**
 * 我要审
 */
var ptd_ywcs_wys = {
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
	show : function(){
		var dealingThing = this.get();
		var html = "";
		if(dealingThing){
			var selectName = ptd_ywcs_obj.dealing.title,
				selectMenuid = ptd_ywcs_obj.dealing.menuid;
			var dealingLength = dealingThing.length,
				selectMenuidLength = selectMenuid.length;
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
					html += '<li><a href="javascript:window.parent.addTabToParent(&quot;'+name+'&quot;, &quot;'+url+'&quot;);" title="'+ title +'">'+ selectName[n] +' <span class="c-red">'+ task +'</span></a></li>';
					isOk = 1;
					break;
				}
				if(isOk == 1){
					isOk = 0;
					continue;
				}
			}
		}
		
		if(!isObjNull(html)){
			$("#content-pa3").find("ul").html("").append(html);
		}
	}
};

/**
 * fiscal
 */
var ptd_ywcs_fiscal = {
	bf : function(){
		// 财政百度图片效果
		$("div#pic").on("mouseover", function(){
			$(this).find("img").prop("src", "img/icon-cha-w.png");
			//$(this).find("img").css("transform", "scale(1.5, 1.5)");
			$(this).css("background-color", "#108ee9");
		}).on("mouseleave", function(){
			$(this).find("img").prop("src", "img/dashboard/search1.png");
			//$(this).find("img").css("transform", "scale(1.3, 1.3)");
			$(this).css("background-color", "#FFFFFF");
		});
	},
	set : function(){
		this.bf();
		$("#pic").on("click", function(){
			var param = $("#_portal_fiscal_input").val();
			if(isObjNull(param)){
				alert("请输入查询内容");
				return;
			}
			window.parent.addTabToParent("财政百度", ptd_ywcs_obj.fiscal.FISCAL_URL + param + "&agencyCode=" + Base64.decode($("#svAgencyCode", parent.document).val()));
		});
	}
};

/**
 * 支出进度
 */
var ptd_ywcs_payprogress = {
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
			ptd_ywcs_payprogress.show();
		});
		$("._portal_zhichu_select_zclx_select").on("change", function(e){
			ptd_ywcs_payprogress.show();
		});
		$("#budgetTime").on("change", function(e){
			ptd_ywcs_payprogress.show();
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
				ptd_ywcs_payprogress.hchart(payMoney, lastBgtMoney, fundtypeCode, expfuncCode, selecttime);
				
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
 * 调整指标统计
 */
var ptd_ywcs_zbtj = {
	set : function(){
		$('#zbtj').highcharts({
			chart: {
				type: 'column'
			},
			exporting: {
				enabled: false
			},
			title: {
				text: ''
			},
			xAxis: {
				categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
				//		    title: {
				//				text: '月份'
				//			}
			},
			yAxis: {
				title: {
				align: 'high',
				offset: 0,
				text: '单位：万元',
				rotation: 0,
				y: -25
			   }
	//		    visible:false
			},
			
			legend: {
				reversed: false,
				layout: 'horizontal',
				align: 'center',
				verticalAlign: 'top',
				x: 200,
				y: 10,
				floating: false,
				borderWidth: 0,
				backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
				shadow: false
			},
			colors: ['#FFBF00', '#EF564B', '#108EE9'],
			credits: {
				enabled: false
			},
			series: [{
					name: '净增指标',
					data: [50, 30, 40, 70, 20, 60, 50, 30, 40, 70, 20, 60]
				},
				{
					name: '追减指标',
					data: [-20, -20, -20, -20, -10, -10, -20, -15, -20, -20, -10, -20]
				},
				{
					name: '追加指标',
					data: [20, 40, 30, 60, 30, 50, 40, 60, 30, 60, 30, 50]
				}
			]
		});
	}
}

/**
 * 公告
 */
var ptd_ywcs_at = {
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
	        	for(var i=0;i<4;i++){
	        		var name = (result[i].article_title).replace(/(^\s+)|(\s+$)/g, "");
	        		var url = path+'articleId='+result[i].article_id+'&title='+name+'&tokenid='+getTokenId();
	        		html+= '<li style="width:80%;"><span class="icon1"></span><a href="javascript:window.parent.addTabToParent(&quot;'+name+'&quot;, &quot;'+url+'&quot;);" title="'+name+'">'+name+'</a></li>';
	        	}
	        	$("#m-content").find("ul").html(html);
	        }
	    });
	}
}

/**
 * 支出进度排名
 */
var ptd_ywcs_zcjdpm = {
	bf : function() {
//		$('#head-r1 >span').each( function(index){
//		        $(this).click(function(){
//		            $('.tab1').addClass('hidden');
//		            $('.tab1:eq('+index+')').removeClass('hidden');
//		            $('#head-r1 > span').removeClass('ac');
//		            $('#head-r1 > span:eq('+index+')').addClass('ac');
//		        })
//		    }
//		);
		
	},
	set : function(){
		$.ajax({
			url : "",
			type : "GET",
			data : {"tokenid": getTokenId()}, 
			dataType : "json",
			success : function(data){
				
			}
		});
	}
}

/**
 * 我要查、我要问
 */
var ptd_ywcs_wycw = {
	bf : function() {
		$('#head-r3 >span').each( function(index){
	        $(this).click(function(){
	            $('.tab3').addClass('hidden');
	            $('.tab3:eq('+index+')').removeClass('hidden');
	            $('#head-r3 > span').removeClass('ac');
	            $('#head-r3 > span:eq('+index+')').addClass('ac');
	        });
	    });
	},
	cha : function() {
		
	},
	wen : function() {
		
	}
}

$(function(){
	
	// 专项指标余额
	ptd_ywcs_zbye.set();
	
	// 我要办
	ptd_ywcs_wyb.set();
	
	// 我要审（待办）
	ptd_ywcs_wys.show();
	//$("#dealingMore").click(function(){
		//portal_d.dl.show(ptd_ywcs_obj.dealing.title, ptd_ywcs_obj.dealing.menuid, "", "content-pa3");
	//});
	
	// 财政百度
	ptd_ywcs_fiscal.set();
	
	// 支出进度
	ptd_ywcs_payprogress.bf();
	ptd_ywcs_payprogress.show();
	
	// 指标统计
	ptd_ywcs_zbtj.set();

	// 公告
	ptd_ywcs_at.show();
	
	// 支出进度
	ptd_ywcs_zcjdpm.bf();
	//ptd_ywcs_zcjdpm.set();
	
	ptd_ywcs_wycw.bf();
	// 我要问
	ptd_ywcs_wycw.wen();
	// 我要查
	ptd_ywcs_wycw.cha();
	
});

// 对象为空
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
