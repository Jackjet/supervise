/**
 * Created by yanqiong on 2017/11/6.
 */
require(['jquery','knockout', '/df/supervise/ncrd.js', 'bootstrap','dateZH', 'uui','tree', 'grid', 'ip','csof'], function($,ko, ncrd) {
        window.ko = ko;
        var tokenid,
            options,
            elecode,
            menuId,
            menuName,
            elecode,
            billTypeCode,
            num = 0;//根据页签的长度定值

        var pViewType = {
            VIEWTYPE_INPUT : "001",// 录入视图
            VIEWTYPE_LIST : "002",// 列表视图
            VIEWTYPE_QUERY : "003"// 查询视图
        };
        var viewData = {};//视图的数据
        var postData = {};//通过post传到后台的数据
        var treeState = {};//点击主树节点的状态
        var postAction;

        var dealData = {//处理数据
            showTreeColor: function (treeId, treeNode) {//显示非最终子节点的树节点为蓝色
                return treeNode.level == 0 ? {color:"blue"} : {};
            },
            getIdLast: function(data){//将{***}的***提取出来
                var par = /[{}]/g;
                var text = data.replace(par,'');
                return text;
            },
            delQuotation : function(data){//将"***"的***提取出来
                var par = /\"/g;
                var text = data.replace(par,'');
                return text;
            },
        };

        var viewModel = {
            SearchTreeKEY: ko.observable(""),
            //模糊查询单位树
            searchTree : function () {
                ncrd.findTreeNode($.fn.zTree.getZTreeObj("tree1"), viewModel.SearchTreeKEY());
            },
            //按钮
            btnDataTable: new u.DataTable({
                meta: {
                	
                }
            }),
            //录入视图表格
		    inputViewDataTable: new u.DataTable({
		        meta: {
		           
		        }
		    }),
            treeDataTable: new u.DataTable({
                meta: {
                    'chr_id': {},
                    'PARENT_ID': {},
                    'chr_name':{}
                }
            }),//监管类型事项树
            treeSetting:{
                view:{
                    showLine: false,
                    selectedMulti: false,
                    showIcon: false,
                    showTitle: true
                },
                callback:{
                    onClick:function(e,id,node){
                    	var btnData = viewModel.btnDataTable.getSimpleData();
                    	treeState = node;
                        treeState.isLastNode = node.isLastNode;
                        treeState.isParent = node.isParent;
                        var listCode = node.chr_code || node.CHR_CODE;//字段id不确定,根据后台传来的字段确定
                        var listCodeName = elecode.substring(5) + "_CODE";
                        var conditon_rela ={};
                        var conditon_data ={};
                    	if(node.CHR_NAME == '全部' || node.chr_name == '全部'){
                            viewModel.gridRefresh(conditon_rela,conditon_data);
                        }else{
                            conditon_rela[listCodeName] = "2";
                            conditon_data[listCodeName] = listCode;
                            viewModel.gridRefresh(conditon_rela,conditon_data);
                        }
                    }
                }
            },
        };        
        
      //生成表头
        viewModel.initData = function() {
            var current_url = location.search;
            $.ajax({
                url : "/df/init/initMsg.do",
                type : "GET",
                dataType : "json",
                async : true,
                data : options,
                success : function(datas) {
                    viewModel.viewList = datas.viewlist;// 视图信息
                    viewModel.resList = datas.reslist;// 资源信息
                    viewModel.coaId = datas.coaId;// coaid
                    viewModel.coaDetails = datas.coaDetail;// coa明细
                    viewModel.relations = datas.relationlist;// 关联关系
                    viewModel.inputViewList = []; 
                    viewModel.listViewList = []; 
                    for(var i=0;i<datas.viewlist.length;i++) {
                    	if(datas.viewlist[i].viewtype == pViewType.VIEWTYPE_INPUT){
                    		viewModel.inputViewList.push(datas.viewlist[i]);
                    	}else if(datas.viewlist[i].viewtype == pViewType.VIEWTYPE_LIST){
                    		viewModel.listViewList.push(datas.viewlist[i]);
                    	}
                    }
                    viewModel.initGridData(); //调用初始化查询表格
                    //初始化录入视图
//                    if(viewModel.inputViewList.length>0){
//                        viewModel.vailData = ip.initArea(viewModel.inputViewList[0].viewDetail,"edit",dealData.getIdLast(viewModel.inputViewList[0].viewid), "infoAdd");
//                    }
                }
            });
        };
        
        viewModel.initGridData = function() {
            var queryViewId;
            var tableViewDetail;
            var queryViewDetail;
            getOptions();
            for ( var n = 0; n < viewModel.listViewList.length; n++) {
                var view = viewModel.listViewList[n];
                if(view.orders == '0') {
                	options["action"] = "Q";
                	options["billtype_code"] = billtype_code;
                	viewModel.listGridViewModel = ip.initGridWithCallBack(gridCallback,viewModel.listViewList[n],'mainGrid', viewModel.listViewList[n].remark + '?', options, 1, false,true,true,false);
//                    if(viewModel.listGridViewModel.gridData.getSimpleData()){
//                        $("#mainGridSumNum").html(viewModel.listGridViewModel.gridData.getSimpleData().length);
//                    }
                }    
            }
        };
        
        //grid查询
        viewModel.gridSearch = function () {
            ip.fuzzyQuery(viewModel.curGridData, "gridSearchInput", viewModel.listGridViewModel);
        };

        //回调函数，定义全局变量viewModel.curGridData并赋值
        gridCallback = function(data){
            viewModel.curGridData = data;
        };

        //刷新Grid表格数据
        viewModel.gridRefresh = function(conditionRela,conditionMap) {
        	getOptions();
        	options["action"] = "Q";
        	options["billtype_code"] = billtype_code;
        	var sartTime = $('#start_time').val();
            var endTime = $('#end_time').val();
            var d1 = new Date(sartTime.replace(/\-/g, "\/"));
            var d2 = new Date(endTime.replace(/\-/g, "\/"));
        	
            if(sartTime != "" && endTime != "" && d1 < d2){
            	conditionMap["op_date"] = sartTime+"op_date"+endTime;
            	conditionRela["op_date"] ="8";
            }else if(sartTime!=""){
            	conditionMap["op_date"] = sartTime;
            	conditionRela["op_date"] ="5";
            }else if(endTime !=""){
            	conditionMap["op_date"] = endTime;
            	conditionRela["op_date"] ="3";
            }
            if(conditionMap || conditionMap != ""){     		
       	        options["condition_data"] = JSON.stringify(conditionMap);
            	options["condition_rela"] = JSON.stringify(conditionRela);
       	    }
            
            ip.setGridWithCallBack(gridCallback,viewModel.listGridViewModel, viewModel.listViewList[0].remark + '?',options);
           // $("#mainGridSumNum").html(viewModel.listGridViewModel.gridData.getSimpleData().length);        
        };
        
        
        //公共新增
        publicAdd = function(id){
        	$("#infoAdd").html("");
        	$('#addModal').modal({
            	show : true,
            	backdrop : 'static'
            });
        	$("#addModalLabel").html(menuName + "新增");
            var aim = ip.createArea("edit",dealData.getIdLast(viewModel.inputViewList[0].viewid), "infoAdd");
        	viewModel.inputViewDataTable.setSimpleData(aim);
        	postAction = "I";
        } 
        
        //新增、修改保存
        viewModel.save = function(){
        	var aim2 = viewModel.inputViewDataTable.getSimpleData();
			var noVerifyRid =["REMARK","BILL_NO"];
			if(verifyInputView(aim2,noVerifyRid)) {
				var sup_array = {};
				for(var i=0;i<aim2.length;i++){
					if(aim2[i].type == "treeassist") {
						var value = $('#'+aim2[i].id+"-h").val();
					}else{
						var value = $('#'+aim2[i].id).val();
					}
					sup_array[aim2[i].id.split("_")[0]] = value;
				}
				viewModel.commonAction(postAction,sup_array);
			}else{
				ip.ipInfoJump("请完善红框数据", 'error');
			}
        }
        
        //公共修改
        publicModify = function(id){
        	var indeces = viewModel.listGridViewModel.gridData.getSelectedIndices();
            var datas = viewModel.listGridViewModel.gridData.getSimpleData();
            if(indeces.length == 1){
            	$("#infoAdd").html("");
            	$('#addModal').modal({
                	show : true,
                	backdrop : 'static'
                });
            	$("#addModalLabel").html(menuName + "新增");
                var aim = ip.createArea("edit",dealData.getIdLast(viewModel.inputViewList[0].viewid), "infoAdd");
            	viewModel.inputViewDataTable.setSimpleData(aim);
            	postAction = "U";
            }else{
            	  ip.ipInfoJump('请选择一个你要修改的内容，勿多选或不选！','info');
            }
        } 
        
        //公共删除
        publicDelete = function() {
        	var indeces = viewModel.listGridViewModel.gridData.getSelectedIndices();
            var datas = viewModel.listGridViewModel.gridData.getSimpleData();
            if(indeces.length > 0){
                ip.warnJumpMsg('是否删除所选择事项',"delConfirmSureId","delConfirmCancelCla");
                $('#delConfirmSureId').on('click',function(){
                    $('#config-modal').remove();
                    var delIds =  datas[indeces[0]].chr_id ;
                    var conditionMap = {};
                    var conditionRela = {};
                    if(indeces.length = 1){
                    	 conditionMap["ID"] = delIds;
                    	 conditionRela["ID"] ="0";
                    }else if(indeces.length > 1){
                    	 for(var i = 1 ; i<indeces.length ; i++){
                             var index = indeces[i];
                             var focusData = datas[index];
                             var delId = focusData.chr_id;
                             delIds = delIds + ","+ delId ;
                         }
                    	 conditionMap["IDS"] = delIds;
                    	 conditionRela["IDS"] ="-1";
                    }
                    viewModel.commonAction("D","","",conditionMap);
                });
                $('.delConfirmCancelCla').on('click',function(){
                    $('#config-modal').remove();
                });
            }else{
                ip.ipInfoJump('请选择你要删除的事项！','info');
            }
        }
        
        //导出按钮
        publicExp = function(){
        	var data = {
            		"action":"Q",
            		"billtype_code":billtype_code,
            	}
        	if(options.hasOwnProperty("condition_rela")){ 
        		if(options["condition_rela"] && options["condition_rela"]!= ""){
            		data["condition_rela"] = options["condition_rela"];
            	}
        	} 
        	if(options.hasOwnProperty("condition_data")){ 
        		if(options["condition_data"] && options["condition_data"]!= ""){
            		data["condition_data"] = options["condition_data"];
            	}
        	} 
        	var form = $("<form>");
    	    form.attr('style', 'display:none');
    	    form.attr('target', '');
    	    form.attr('method', 'post');
    	    var url = "/LogExcel/expLogExcel.do?tokenid=" + tokenid + "&ajax=noCache";
    	    form.attr('action', url);
    	    var input1 = $('<input>');
			input1.attr('type', 'hidden');
			input1.attr('name', 'action');
			input1.attr('value', "Q");
    	    var input2 = $('<input>');
			input2.attr('type', 'hidden');
			input2.attr('name', 'billtype_code');
			input2.attr('value', billtype_code);
    	    form.append(input1);
			form.append(input2);
    	    if(options.hasOwnProperty("condition_rela")){ 
        		if(options["condition_rela"] && options["condition_rela"]!= ""){
              	    var input3 = $('<input>');
          			input3.attr('type', 'hidden');
          			input3.attr('name', 'condition_rela');
          			input3.attr('value', options["condition_rela"]);
      			    form.append(input3);
            	}
        	} 
        	if(options.hasOwnProperty("condition_data")){ 
        		if(options["condition_data"] && options["condition_data"]!= ""){
            	    var input4 = $('<input>');
        			input4.attr('type', 'hidden');
        			input4.attr('name', 'condition_data');
        			input4.attr('value', JSON.stringify(options["condition_data"]));
        			form.append(input4);
            	}
        	} 
        	$('body').append(form);
    	    form.submit();
    	    form.remove();
        	
//        	$.ajax({
//	            type: 'GET',
//	            url: "/LogExcel/expLogExcel.do" + "?tokenid=" + tokenid + "&ajax=noCache",
//	            data: data,
//	            dataType: 'JSON',
//	            async: true,
//	            success: function (result) {
//	            	if(result.errorCode == 0) {
//	         
//	            	}else {
//	            		ip.ipInfoJump(result.message, 'error');
//	            	}
//	            }, error: function () {
//	            	ip.ipInfoJump("错误", 'error');
//	            }		                
//            });
        }
        
        //公共ACTIOIN
        viewModel.commonAction = function(action,operation_data,condition_rela,condition_data){
        	var data = {
            		"action":action,
            		"billtype_code":billtype_code,
            	}
        	if(condition_rela && condition_rela!= ""){
        		data["condition_rela"] = condition_rela;
        	}
        	if(condition_data && condition_data!= ""){
        		data["condition_data"] = JSON.stringify(condition_data);
        	}
        	if(operation_data && operation_data!= ""){
        		data["operation_data"] = JSON.stringify(operation_data);
        	}
        	$.ajax({
	            type: 'GET',
	            url: "/df/common/commonAction.do" + "?tokenid=" + tokenid + "&ajax=noCache",
	            data: data,
	            dataType: 'JSON',
	            async: true,
	            success: function (result) {
	            	if(result.errorCode == 0) {
	            		if(action == "I" || action == "U"){
	            			$('#addModal').modal('toggle');
		            		$('#infoAdd').html("");
	            		}
	            		viewModel.initTree();
	            		viewModel.gridRefresh();
	            	}else {
	            		ip.ipInfoJump(result.message, 'error');
	            	}
	            }, error: function () {
	            	ip.ipInfoJump("错误", 'error');
	            }		                
            });
        }
        
        //截止时间大于开始时间
        checkData = function(){
        	var sartTime = $('#start_time').val();
            var endTime = $('#end_time').val();

            var d1 = new Date(sartTime.replace(/\-/g, "\/"));
            var d2 = new Date(endTime.replace(/\-/g, "\/"));


            if(sartTime != "" && endTime != "" && d1 >= d2){
                ip.ipInfoJump("开始日期不能大于结束日期", 'error');
                flag++;
            }else if(sartTime != "" && endTime != "" && d1 < d2){
            	var condition_rela = "";
            	var condition_data ="";
            	if(options.hasOwnProperty("condition_rela")){ 
            		if(options[""] && options["condition_rela"]!= ""){
            			condition_rela = options["condition_rela"];
                	}
            	} 
            	if(options.hasOwnProperty("condition_data")){ 
            		if(options[""] && options["condition_data"]!= ""){
            			condition_data = options["condition_data"];
                	}
            	} 
            	viewModel.gridRefresh(condition_rela,condition_data);
            }
        }
        
      //获取options
        getOptions = function() {
        	var pageInfo = options["pageInfo"];
        	options = ip.getCommonOptions({});
            options['tokenid'] = tokenid;
            options['ajax'] = "noCache";
            options['pageInfo'] = pageInfo;
        }
        
        //初始化
        function init() {
            app = u.createApp({
                el: document.body,
                model: viewModel
            });
            tokenid = ip.getTokenId();
            options = ip.getCommonOptions({});
            options['tokenid'] = tokenid;
            var url=window.location.href;
            menuId = url.split("menuid=")[1].split("&")[0];
            menuName = decodeURI(url.split("menuname=")[1].split("&")[0]);
            elecode = url.split("elecode=")[1].split("&")[0];
            billtype_code = url.split("billTypeCode=")[1].split("&")[0];
            //初始化按钮
            var data = initBtns(menuId);
            if(data == false){
                ip.ipInfoJump("加载按钮出错", 'error');
            }else{
                viewModel.btnDataTable.setSimpleData(data);
            }
            initTreeByElecode(elecode,viewModel.treeDataTable,"tree1");//初始化要素树
            viewModel.initData();//初始化Grid
            var today=new Date();  
            var submitTime=today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();   
            $("#end_time").val(submitTime); 
            $("#start_time").val(submitTime); 
        };
        init();
    }
);