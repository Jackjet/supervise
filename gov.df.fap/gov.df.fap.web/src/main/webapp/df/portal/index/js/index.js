$(function(){
	window.onload=function(){
		// 获取head一级菜单
		getInitMenu();
		// 获取报表数据
		getEchartData();
		// head获取当前年月日
		getTime();
	}
	// head获取当前年月日
	function getTime(){
		var myDate = new Date();
		var Year=myDate.getFullYear();
		var Month=myDate.getMonth()+1;       // 获取当前月份(0-11,0代表1月)
		var Today=myDate.getDate();        // 获取当前日(1-31)
		var Day=myDate.getDay(); 
		var week = "星期" + "日一二三四五六".split("")[Day];
		var time="";
		time+=Year+'年'+Month+'月'+Today+'日'+'&nbsp;&nbsp;'+week;
		$("#time").append(time);
	}

	// 获取tokenId方法
	getTokenId = function(){
    	var current_url = location.search;
		var tokenId = current_url.substring(current_url.indexOf("tokenid") + 8);
		return tokenId;
	}
	
	// 菜单初始化方法
	// 一级菜单初始化方法
	getInitMenu = function(){
		var tokenId = getTokenId();
		window.open("/df/portal/articleManagement/articleMain.html?tokenid="+ tokenId);
        $.ajax({
            url: "/df/portal/getMenu.do",
            type: 'GET',
            dataType: 'json',
			data: {"tokenid":tokenId},
            success: function (data) {
            	toLocalStorage(data.userDto);
            	toHiddenLabel(data.userDto);
            	var menuList = data.mapMenu;
            	// 一级菜单List
            	var menuFirstLevelList = new Array();
            	// 所有二级菜单List
            	var menuSecondLevelList = new Array();
            	// 所有三级菜单List
            	var menuThirdLevelList = new Array();
            	var j = 0;
            	var s = 0;
            	var t = 0;
            	for (var i = 0; i < menuList.length; i++){
            		if(menuList[i].levelno=='1'){
            			menuFirstLevelList[j] = menuList[i];
            			j++;
            		}else if(menuList[i].levelno=='2'){
            			menuSecondLevelList[s] = menuList[i];
            			s++;
            		}else if(menuList[i].levelno=='3'){
            			menuThirdLevelList[t] = menuList[i];
            			t++;
            		}
            	}
            	var firstMenuHtml = '<ul>';
            	firstMenuHtml += '<li class="bg"><a href="#">首页</a></li>';
            	for (var i = 0; i < menuFirstLevelList.length; i++){
            		var _tokenid = "";
            		if (menuFirstLevelList[i].url.indexOf("?") >-1 ) {
            			_tokenid = "&tokenid="+ tokenId;
            		}else{
            			_tokenid = "?tokenid="+ tokenId;
            		}
            		firstMenuHtml +='<li><a href="'+ menuFirstLevelList[i].url + _tokenid  +'">' +menuFirstLevelList[i].name +'</a></li>';
            	}
            	firstMenuHtml +='</ul>';
            	$("#h-nav").append(firstMenuHtml);
            	
            	
            	// 二三级菜单处理
            	var nextMenuListHtml ='<div class="menu null"></div>';
            	    nextMenuListHtml+= '<div class="one menu ">';
            	    nextMenuListHtml+= '<ul class="level-2"id="level-21"></ul>';
            	    nextMenuListHtml+= '</div>';
            	    
            	    for (var i = 0; i< menuFirstLevelList.length; i++){
            	    	var guid = menuFirstLevelList[i].guid;
            	    	
            	    	
            	    	// 某一级菜单对应的所有二级菜单
            	    	var tmpMenuSecondLevelList = new Array();
            	    	var j =0;
            	    	var k =0;
            	    	for (var j =0; j<menuSecondLevelList.length;j++){
            	    		if(menuSecondLevelList.length>0 && guid == menuSecondLevelList[j].parentid){
            	    			tmpMenuSecondLevelList[k] = menuSecondLevelList[i];
            	    			j++;
            	    			k++;
            	    		}
            	    	}
            	    	// 循环二级菜单并筛选其对应的三级菜单
            	    	var m=0;
            	    	var n=0;
            	    	for(var t =0; t<tmpMenuSecondLevelList.length;t++){
            	    		var guidSecond = tmpMenuSecondLevelList[t].guid;
            	    		var tmpMenuThirdLevelList = new Array();
            	    		if(menuThirdLevelList.length >0 && guidSecond == menuThirdLevelList[m].parentid ){
            	    			tmpMenuThirdLevelList[n]=menuThirdLevelList[m];
            	    			m++;
            	    			n++;
            	    		}
//            	    		
// //拼接二三级菜单html
            	    		nextMenuListHtml += '<div class="menu">';
            	    		nextMenuListHtml +='<img id="jiantou" src="img/jiantou.png"/>';
				            nextMenuListHtml += '<ul class="level-2" id="level-2">'
				            nextMenuListHtml += '<li>' + tmpMenuSecondLevelList[t].name +'<div class="level-3" id="level-3">';
				            for (var len =0;len< tmpMenuThirdLevelList.length; len++){
				            	nextMenuListHtml += '<p><a href="' + tmpMenuThirdLevelList[len].url +'">' + tmpMenuThirdLevelList[len].name +'</a></p>';
				            }
				            
				            // 测试
				            nextMenuListHtml +='<p><a href="">单笔录入</a></p>';
				            nextMenuListHtml +='<p><a href="">单据审核</a></p>';
				            nextMenuListHtml +='<p><a href="">零余额账户变更</a></p>';
				            nextMenuListHtml +='<p><a href="">自筹资金录入</a></p>';
				            nextMenuListHtml +='<p><a href="">道具卡刷</a></p>';
				            
				            // 测试end
				            nextMenuListHtml += '</div>';
				            nextMenuListHtml += '</li>';
				            nextMenuListHtml += '</ul>';
				            nextMenuListHtml += '</div>';
            	    	}
            	    }
            	    
            	    $("#menuList").append(nextMenuListHtml);

            		// head隐藏部分
            		$("#h-nav li a").mouseover(function(){
            			$("#h-nav li").removeClass('bg');
            			var i = $(this).parent("li").index();
            			$(".menulist .menu").each(function(){
            				$(this).hide();
            			});
            			if(!$(".menulist .menu").eq(i).hasClass("null")){
            				$('.menulist').slideDown();
            				$(".menulist .menu").eq(i).slideDown();
            			}			
            		});
            		$(".menulist .menu").mouseleave(function(){
            			$(".menulist .menu").each(function(){
            				$(this).slideUp();
            			})
            			$("#h-nav li").eq(0).addClass('bg');
            		});
            }
        });
	}
		getEchartData= function(){
		   var myChart = echarts.init(document.getElementById('cen-1'));  
		    // 预算执行率
				params = {};
				params['ruleID'] = 'getdwzxlData';
				params['start'] = '0';
				params['limit'] = '7';
				paramsRate ={};
				paramsRate['ruleID'] = 'getdwzxlDataRate';
				paramsRate['start'] = '0';
				paramsRate['limit'] = '1';
				
				// X轴名字
				var dataNameList = [];
				// 前月
				var MonthNm;
				// 前前月
				var LaterMonthNm;
				// 前月执行率
				var dataPreList = [];
				// 前前月执行率
				var dataPPreList = [];
				// 前月总执行率
				var preRate='';
				// 前前月总执行率
				var ppreRate='';
				var tokenId = getTokenId();
		$.ajax({
	        type: 'GET',
	        url: "/portal/GetPageJsonData.do?tokenid=" + tokenId,
	        dataType : 'json',
	        data: paramsRate,
	        success: function(result){
	    	preRate = result[0].prerate;
	    	ppreRate = result[0].pprerate;
	    	$.ajax({
		        type: 'GET',
		        url: "/portal/GetPageJsonData.do?tokenid=" + tokenId,
		        dataType : 'json',
		        data: params,
		        success: function(result){
		        	// 获取月份
	            	MonthNm = result[0].fis_perd;
	            	LaterMonthNm = MonthNm - 1;
	            	if (LaterMonthNm==0){
	            		LaterMonthNm = 12;
	            	}
		            for(var i=0;i<result.length;i++){
		            	dataNameList[i] = result[i].dw_jc;
		            	dataPreList[i] = result[i].byzx;
		            	dataPPreList[i] = result[i].syzx;
		            }
			         option = {
			        		 
			        		 title: {
			        		        subtext: '          备注：财政预算'+LaterMonthNm+'月总执行率为：'+ppreRate+';财政预算'+MonthNm+'月总执行率为：'+preRate
			        		  },

				             tooltip : {
				                 trigger: 'axis'
				             },
				             legend: {
				                 data:[MonthNm+'月执行率',LaterMonthNm+'月执行率']
				             },
				             toolbox: {
				                 show : false,
				                 feature : {
				                     mark : {show: true},
				                     dataView : {show: true, readOnly: false},
				                     magicType : {show: true, type: ['line', 'bar']},
				                     restore : {show: true},
				                     saveAsImage : {show: true}
				                 }
				             },
				             calculable : true,
				             xAxis : [
				                 {
				                     type : 'category',
				                     data : dataNameList
				                 }
				             ],
				             yAxis : [
				                 {
				                     type : 'value'
				                 }
				             ],
				             series : [
				                 {
				                     name:MonthNm+'月执行率',
				                     type:'bar',
				                     data:dataPreList,
				                 },
				                 {
				                     name:LaterMonthNm+'月执行率',
				                     type:'bar',
				                     data:dataPPreList,
				                 }
				             ],
				             color:['#AFD8F8','#F6BD0E']
				         };
	            // 使用刚指定的配置项和数据显示图表。
	            myChart.setOption(option); 	
		        }
	    		});
	            
	        }
				});
	}
/*
 * //左栏隐藏事件 $("#ss").click(function(){ $("#main #left").removeClass('active');
 * $("#main #left").addClass('hid'); $('#main #center').css({ "width":"985px",
 * "left":"-=200px" }); $("#center #set1").addClass("active"); }); //左栏显示事件
 * $("#center #set1").click(function(){ $(this).removeClass('hid'); $("#main
 * #left").addClass('active'); $('#main #center').css({ "width":"786px",
 * "left":"218px", }); $(this).removeClass('active'); $(this).addClass("hid"); })
 */
	
	// 左栏隐藏事件
	$("#ss").click(function(){
		$("#main #left").hide(500,function(){
			$('#main #center').css({
				"left":"-=200px",
				"width":"+=200px",
			});
			$("#center #set1").addClass("active");
		});

	});
// 左栏显示事件
	$("#center #set1").click(function(){
		$(this).removeClass('hid');
		$("#main #left").show(500);
		$('#main #center').css({
			"left":"218px",
			"width":"786px",
		});
		$(this).removeClass('active');
		$(this).addClass("hid");
	})
	

	// head隐藏部分
	$("#h-nav li a").mouseover(function(){
		$("#h-nav li").removeClass('bg');
		var i = $(this).parent("li").index();
		$(".menulist .menu").each(function(){
			$(this).hide();
		});
		if(!$(".menulist .menu").eq(i).hasClass("null")){
			$('.menulist').slideDown();
			$(".menulist .menu").eq(i).slideDown();
		}			
	});
	$(".menulist .menu").mouseleave(function(){
		$(".menulist .menu").each(function(){
			$(this).slideUp();
		})
		$("#h-nav li").eq(0).addClass('bg');
	});
// head右侧按钮
	$("#h-btn").click(function(){
		var displayCss = $("#hid-btn").css("display");
		if(displayCss=='none'){
			$("#hid-btn").removeClass();
			$("#hid-btn").addClass('hid-btn-disp');
		}else{
			$("#hid-btn").removeClass();
			$("#hid-btn").addClass('hid-btn-none') 
		}
		
	});
	// left左边部分中的内容鼠标滑过事件
	$("#list-l li").mouseover(function(){
		var i =$(this).index();
		$("#list-l li").each(function(){
			$(this).css({"background":"#ffffff"});
			$("#list-l li").eq(i).css({"background":"#f5f5f5"})
		})
		$("#list-l li").mouseleave(function(){
			$("#list-l li").each(function(){
				$(this).css({"background":"#ffffff"});
			})
		})
	})
});

function toLocalStorage(data){
	var _data = JSON.stringify(data);
	if(!localStorage.commonData){
		localStorage.commonData = _data;
	} else {
		localStorage.commonData = _data;
	}
}

function toHiddenLabel(data){
	// TODO 页面标签存值
	$("#guid").val(data.guid);
	
}

function switchRole(){
	var tokenId = getTokenId();
	alert("ok " + tokenId);
	return;
	
	// TODO 切换角色，用户有几个角色??

	$.ajax({
		url:"/df/portal/switchRole.do",
		type:"GET",
		dataType:"json",
		data:{"tokenid":tokenId},
		success:function(data){
			window.location.href="/df/portal/admin/index/index.html?tokenid="+data.tokenid;
		}
	});
}

function registerPwd(){
	var tokenId = getTokenId();
	alert("ok " + tokenId);
	return;
	
	$.ajax({
		url:"/df/portal/registerPwd.do",
		type:"GET",
		dataType:"json",
		data:{"tokenid":tokenId},
		success:function(data){
			window.location.href = "..";
		}
	});
}

function loginout(){
	var tokenId = getTokenId();
	$.ajax({
		url:"/df/login/loginout.do",
		type:"GET",
		dataType:"json",
		data:{"tokenid":tokenId},
		success:function(data){
			window.location.href = "/df/portal/login/login.html";
		}
	});
}