/**
 * Created by yanqiong on 2017/8/8.
 */
require(
		[ 'jquery', 'knockout', '/df/supervise/ncrd.js','csscsof', 'bootstrap', 'dateZH',
				'uui', 'tree', 'grid', 'ip' ,'csof'],
		function($, ko, ncrd, csscsof) {
			window.ko = ko;
			var tokenid;
			var options;
			var pViewType = {
				VIEWTYPE_INPUT : "001",// 录入视图
				VIEWTYPE_LIST : "002",// 列表视图
				VIEWTYPE_QUERY : "003"// 查询视图
			};
			var fileData;
			var num = 0;
			var elecode,
			    menuId,
				menuName,
				billTypeCode,
				sumBillTypeCode,
				ACC_ID,
				BOOK_ID,
				BOOK_NAME,
				depId,
				DEP_CODE,
				DEP_NAME,
				SET_YEAR,
				SET_MONTH;
			//var BOOK_GROUP;

			//URL
			var testURL = '/df/cs/test.do';
			var getLeftTreeURL = '/df/csofacc/getBookDepById.do';//获取树的信息getBookDepById
			var getAccSupEndURL = '/df/csofacc/getAccSupEndByBook.do';//通过点击树获取工作记录
			var officeSumURL = '/df/csofacc/saveAccSupDep.do';//台账处汇总
			var officeUndoSumURL = '/df/csofacc/deleteAccSupDep.do';//台账撤销汇总
			var doPayWorkFlowURL= '/df/workflow/work.do';//工作流
			var getSumTableURL = '/df/csofacc/getAccSupDepBySetid.do';//点击树得到汇总表
			var getDtailAccSupEndURL = '/df/csofacc/getAccSupEndBysetid.do';//点击底级获取明细
			var updateAccSupDepURL = '/df/csofacc/updateAccSupDep.do';//处汇总后修改保存数据
			var getReportURL = '/ReportForm/selectBiReportByChrId.do'//获取汇总表报表
			var viewModel = {
				SearchBookSumTreeKEY : ko.observable(""),
				//模糊查询单位树
				searchBookSumTree : function() {
					ncrd.findTreeNode($.fn.zTree.getZTreeObj("bookSumTree"),viewModel.SearchBookSumTreeKEY());
				},
			    //工作GridDataTable
				workDataTable: new u.DataTable({
			        meta: {
			            
			        }
			    }),
			    //汇总修改
			    sumDataTable: new u.DataTable({
			        meta: {
			            
			        }
			    }),
			    //录入视图详情
			    inputViewDataTable: new u.DataTable({
			        meta: {
			            
			        }
			    }),
			    //按钮
			    btnDataTable: new u.DataTable({
			    	meta: {
			           
			        }
			    }),
			    bookSumTreeSetting: {
	                view:{
	                    showLine: false,
	                    selectedMulti: false,
	                    showIcon: false,
	                    showTitle: true,
	                    nameIsHTML: true
	                },
	                callback:{
	                    onClick:function(e,id,node){
	                        var selectNodeId = node.CHR_ID;	                        
	                        if(node.LEVEL_NUM == "3"){
	                        	var bookData = viewModel.bookSumTreeDataTable.getSimpleData();
	                        	for(var i=0;i<bookData.length;i++){
	                        		if(bookData[i].CHR_ID == node.PARENT_ID){
	                        			//$("#accType").html(BOOK_GROUP);
	    	                        	$("#accDate").html(bookData[i].CHR_NAME+node.CHR_NAME);
	    	                        	$("#recodeOffice").html(DEP_NAME);
	    	                        	$("#recodeUser").html(BOOK_NAME.split("台账")[0]);
	    	                        	$("#accStatus").html(node.STATUS);
	    	                        	viewModel.refresh(node.CHR_CODE,node.CHR_NAME.split("月")[0],node.PARENT_ID,getAccSupEndURL);
	    	                        	break;
	                        		}
	                        	}
	                        }else if(node.LEVEL_NUM == "4"){
	                        	var bookData = viewModel.bookSumTreeDataTable.getSimpleData();
	                        	for(var i=0;i<bookData.length;i++){
	                        		if(bookData[i].CHR_ID == node.PARENT_ID){
	                        			//$("#accType").html(BOOK_GROUP);
	    	                        	$("#accDate").html(node.CHR_CODE.split("-")[1]);
	    	                        	$("#recodeOffice").html(DEP_NAME);
	    	                        	$("#recodeUser").html(node.CHR_NAME.split("台账")[0]);
	    	                        	$("#accStatus").html(node.STATUS);
	    	                        	viewModel.refresh(node.CHR_CODE,node.CHR_NAME.split("月")[0],node.PARENT_ID,getDtailAccSupEndURL);
	    	                        	break;
	                        		}
	                        	}
	                        }else{
	                        	$("#accType").html("");
	                        	$("#accDate").html("");
	                        	$("#recodeOffice").html("");
	                        	$("#recodeUser").html("");
	                        	$("#accStatus").html("");
	                        	
	                        }
	                        var btnData = viewModel.btnDataTable.getSimpleData();
	                        for(var i=0;i<btnData.length;i++){
                        		var btnId = btnData[i].BUTTON_ID;
                        		if(btnData[i].DISPLAY_STATUS == "9" || btnData[i].DISPLAY_STATUS == node.STATUS_CODE2){
                        			$("#"+btnId).removeAttr("disabled","disabled");
                        		}else{
                        			$("#"+btnId).attr("disabled","disabled");
                        		}
                        	}
	                    }
	                }
	            },
	            bookSumTreeDataTable: new u.DataTable({
	                meta: {
	                	
	                }
	            }),
	            getIdLast: function(data){//将{***}的***提取出来
		        	var par = /[{}]/g;
		        	var text = data.replace(par,'');
		        	return text;
		        },
			};
			
			//生成表头
			viewModel.initData = function(order) {
				var current_url = location.search;
				$.ajax({
					url : "/df/init/initMsg.do",
					type : "GET",
					dataType : "json",
					async : false,
					data : options,
					success : function(datas) {
						viewModel.viewList = datas.viewlist;// 视图信息
						viewModel.resList = datas.reslist;// 资源信息
						viewModel.coaId = datas.coaId;// coaid
						viewModel.coaDetails = datas.coaDetail;// coa明细
						viewModel.relations = datas.relationlist;// 关联关系
						viewModel.initGridData(order); //调用初始化表格
					}
				});
			};
			
			 // 初始化表格
	    	viewModel.initGridData = function(order) {
	            var tableViewDetail;
	            for ( var n = 0; n < viewModel.viewList.length; n++) {
	                var view = viewModel.viewList[n];
                    if(view.orders == "0" && order == ""){
    	                viewModel.tableViewDetail = view;
     	        	    options["dep_code"] = DEP_CODE;
     	        	    options["chr_name"] = "";
    	        	    options["parent_id"] = "";
                        payViewId = view.viewid;
                        viewModel.detailgridViewModel = ip.initGrid(viewModel.tableViewDetail, "detailGrid", getAccSupEndURL,options, 1, false,false,false,false);
                    }else if(view.orders == "1" && order == ""){
	            		viewModel.tableViewDetail = view;
     	        	    options["book_id"] = BOOK_ID;
     	        	    options["chr_name"] = "";
    	        	    options["chr_code"] = "";
    	        	    options["parent_id"] = "";
                        payViewId = view.viewid;
                        viewModel.sumgridViewModel = ip.initGridWithCallBack(sumgridCallback,viewModel.tableViewDetail, "sumGrid", getSumTableURL,options, 1, false,true,false,false);
	            	}else if(view.orders == "2" && order == ""){
	            		viewModel.tableViewDetail = view;
     	        	    options["dep_code"] = DEP_CODE;
     	        	    options["chr_name"] = "";
    	        	    options["parent_id"] = "";
                        payViewId = view.viewid;
                        viewModel.workgridViewModel = ip.initGridWithCallBack(workgridCallback,viewModel.tableViewDetail, "workGrid", getAccSupEndURL,options, 1, false,false,false,false);
	            	}else if(view.orders == order){
                		var viewid = viewModel.getIdLast(view.viewid);
                		var aims = ip.initArea(view.viewDetail,"edit",viewid, "modifySum");
                		viewModel.inputViewDataTable.setSimpleData(aims);
            			var dep_array = [];
            			for(var i=0;i<aims.length;i++){
            				var rid = aims[i].id.split("-")[0];
            				var depArray = {
	                                "id": aims[i].id,
	                                "rid": rid,
	                                "type": aims[i].type,
	                                "source":aims[i].source,
	                            }
	            			dep_array.push(depArray);
            			}
            			viewModel.sumDataTable.setSimpleData(dep_array);
	            	}
	            }
	        };
	        
	        //汇总grid查询
	        viewModel.gridSearch = function () {
	        	ip.fuzzyQuery(viewModel.sumcurGridData, "gridSearchInput", viewModel.sumgridViewModel);
	        };
	        
	      //workgrid查询
	        viewModel.workgridSearch = function () {
	        	ip.fuzzyQuery(viewModel.workcurGridData, "workgridSearchInput", viewModel.workgridViewModel);
	        };
	        
	        //回调函数，定义全局变量viewModel.curGridData并赋值
	        sumgridCallback = function(data){
	        	viewModel.sumcurGridData = data;
	        };
	      //回调函数，定义全局变量viewModel.curGridData并赋值
	        workgridCallback = function(data){
	        	viewModel.workcurGridData = data;
	        };
	        
	        
	      //刷新视图
	        viewModel.refresh = function(chr_code,chr_name,parent_id,URL) {
	            var tableViewDetail;
	            for ( var n = 0; n < viewModel.viewList.length; n++) {
	                var view = viewModel.viewList[n];
	                if(view.orders == "0"){
	                	viewModel.tableViewDetail = view;
	                	options["book_id"] = BOOK_ID;
		                options["dep_code"] = DEP_CODE;
	 	        	    options["chr_name"] = chr_name;
	 	        	    options["chr_code"] = chr_code;
	 	        	    options["parent_id"] = parent_id;
	                    payViewId = view.viewid;
	                    ip.setGrid(viewModel.detailgridViewModel, URL,options);
                        $("#detailGridSumNum").html(viewModel.detailgridViewModel.gridData.getSimpleData().length);
	                }else if(view.orders == "1"){
	            		viewModel.tableViewDetail = view;
     	        	    options["book_id"] = BOOK_ID;
     	        	    options["chr_name"] = chr_name;
    	        	    options["chr_code"] = chr_code;
    	        	    options["parent_id"] = parent_id;
                        payViewId = view.viewid;
                        ip.setGridWithCallBack(sumgridCallback,viewModel.sumgridViewModel, getSumTableURL,options);
                        $("#sumGridSumNum").html(viewModel.sumgridViewModel.gridData.getSimpleData().length);
	            	}else if(view.orders == "2"){
	            		viewModel.tableViewDetail = view;
	                	options["book_id"] = BOOK_ID;
		                options["dep_code"] = DEP_CODE;
	 	        	    options["chr_name"] = chr_name;
	 	        	    options["chr_code"] = chr_code;
	 	        	    options["parent_id"] = parent_id;
	                    payViewId = view.viewid;
	                    ip.setGridWithCallBack(workgridCallback,viewModel.workgridViewModel, URL,options);
	                    $("#workGridSumNum").html(viewModel.workgridViewModel.gridData.getSimpleData().length);
	            	}
	            }
	        }
	        
	        //初始化树getLeftTreeURL
	        viewModel.initTree = function(){
	        	$.ajax({
		            type: 'get',
		            url: getLeftTreeURL + "?tokenid=" + tokenid + "&ajax=noCache",
		            data: {"book_id":BOOK_ID,"billtype_code":sumBillTypeCode,"menu_id":menuId},
		            dataType: 'JSON',
		            async: false,
		            success: function (result) {
		            	if(result.errorCode == 0) {
		            		var data = result.data;
		            		for(var i=0;i<data.length;i++){
		            			if(data[i].LEVEL_NUM == "1"){
		            				data[i]["PARENT_ID"] = "";
		            			}
		            			if(data[i].STATUS_NAME){
		            				if(data[i].STATUS_CODE2 == "9" || data[i].STATUS_CODE2 == "-1" || data[i].STATUS_CODE2 == "1"){
		            					data[i]["NAME"] = data[i].CHR_NAME+"<font color='green'>("+data[i].STATUS_NAME+")</font>";
		            				}else{
		            					data[i]["NAME"] = data[i].CHR_NAME+"<font color='red'>("+data[i].STATUS_NAME+")</font>";
		            				}
		            			}else{
		            				data[i]["NAME"] = data[i].CHR_NAME;
		            			}
		            		}
		            		viewModel.bookSumTreeDataTable.setSimpleData(data);
		            		var treeObj = $.fn.zTree.getZTreeObj("bookSumTree");
		                    treeObj.expandAll(true);
		                    treeObj.cancelSelectedNode();
		            	}else {
		            		ip.ipInfoJump(result.errorMsg, 'error');
		            	}
		            }, error: function () {
		            	ip.ipInfoJump("错误", 'error');
		            }		                
	            });
	        };
	        
	        //初始化期间
	        viewModel.initYearMonth = function() {
	        	var objSelect = document.getElementById("year");
	        	objSelect.options.length=0;
	        	objSelect.options.add(new Option(SET_YEAR , SET_YEAR));
	        	var objSelect2 = document.getElementById("month");
	        	objSelect2.options.length=0;
	        	objSelect2.options.add(new Option(SET_MONTH , SET_MONTH));
	        };
	        
	        $('#sumTable-li').on('click' , function() {    
				$("#workGridSearchDiv").hide();
				$("#gridSearchInput").val("");
				$("#gridSearchDiv").show();
				ip.fuzzyQuery(viewModel.sumcurGridData, "gridSearchInput", viewModel.sumgridViewModel);
			});
	        
	        $('#detailTable-li').on('click' , function() {    
				$("#workGridSearchDiv").show();
				$("#workGridSearchInput").val("");
				$("#gridSearchDiv").hide();
				ip.fuzzyQuery(viewModel.workcurGridData, "workgridSearchInput", viewModel.workgridViewModel);
			});
	        
	        //处汇总officeSumURL
	        summary = function(id){
	        	var btnData = viewModel.btnDataTable.getSimpleData();
	        	for(var bt = 0; bt<btnData.length;bt++) {
	        		if(btnData[bt].BUTTON_ID == id) {
	        			var btn = btnData[bt];
	        			break;
	        		}
	        	}
	        	var op_name = btn.DISPLAY_TITLE;
        		var op_type = btn.ACTION_TYPE;
	        	var treeObj = $.fn.zTree.getZTreeObj("bookSumTree");
				var nodes = treeObj.getSelectedNodes();
				var bookData = viewModel.bookSumTreeDataTable.getSimpleData();
	        	if(nodes.length > 0) {
	        		if(nodes[0].LEVEL_NUM == "3"){
	        			var workData = viewModel.detailgridViewModel.gridData.getSimpleData();
	        			var sid = "";
	        			var num = 0;
	    				var nodeChildFlag = 0;
	    				for(var b =0;b<bookData.length;b++){
	    					if(bookData[b].PARENT_ID == nodes[0].CHR_ID){
	    						num++;
	    						if(bookData[b].STATUS_CODE2 == "11"){
	        						nodeChildFlag++;
	        					}
	    					}
	    				}
	    				if(num == nodeChildFlag){
	    					if(nodes[0].STATUS_CODE2 == "10"){
	    						if(workData.length <= 0){
			        				var data = {
		        							"id":null,
		        							"dep_task_id":null,
		        		        		    "sid":null,
		        		        		    "set_id":null,
		        		        		    "sup_type_id":null,
		        		        		    "sup_name":null,
		        		        		    "dep_id":depId,
		        		        		    "dep_code":DEP_CODE,
		        		        		    "dep_name":DEP_NAME,
		        		        		    "chr_id":nodes[0].CHR_ID,
		        		        		    "chr_code":nodes[0].CHR_CODE,
		        		        		    "chr_name":nodes[0].CHR_NAME.split("月")[0],
		        		        		    "parent_id":nodes[0].PARENT_ID,
		        		        		    "book_id":BOOK_ID,
		        		        		    "acc_sup_dep_id":null,
		        		        		    "op_type":op_type,
		        		        		    "op_name":op_name,
		        		        		    "menu_id":menuId,
		        		        		    "billtype_code":sumBillTypeCode,
		        		        		    "billtype_code1":billTypeCode,
		        		        		    "obj_type_id":null,
		        		        	}
		    	    	        	$.ajax({
		    	    		            type: 'get',
		    	    		            url: officeSumURL + "?tokenid=" + tokenid + "&ajax=noCache",
		    	    		            data: data,
		    	    		            dataType: 'JSON',
		    	    		            async: false,
		    	    		            success: function (result) {
		    	    		            	if(result.errorCode == 0) {
		    	    		            		
		    	    		            	}else {
		    	    		            		monthlyerror++;
		    	    		            		ip.ipInfoJump(result.errorMsg, 'error');
		    	    		            	}
		    	    		            }, error: function () {
		    	    		            	monthlyerror++;
		    	    		            	ip.ipInfoJump("错误", 'error');
		    	    		            }		                
		    	    	            });
			        			}else{
//		        					var setNum = 1;
//		        					var SET_ID = [];
//		        					SET_ID[0] = workData[0].SET_ID;
//		        					for(var w=0;w<workData.length;w++){
//		        						var noflag=0;
//		        						for(var si=0;si<SET_ID.length;si++){
//		        							if(SET_ID[si] == workData[w].SET_ID){
//		        								noflag++;
//		        							}
//		        						}
//		        						if(noflag == 0){
//		        							SET_ID[setNum] = workData[w].SET_ID;
//		        							setNum++;
//		        						}
//		        					}
//		        					if(num == setNum && num != 0){
		        						var monthlyerror = 0;
				        				for(var i=0;i<workData.length;i++){
				        					if(monthlyerror > 0){
				        						break;
				        					}else{
					        					sid = workData[i].SID;
					        					var data = {
					        							"id":workData[i].ID,
					        							"dep_task_id":workData[i].DEP_TASK_ID,
					        		        		    "sid":sid,
					        		        		    "set_id":workData[i].SET_ID,
					        		        		    "sup_type_id":workData[i].SUP_TYPE_ID,
					        		        		    "sup_name":workData[i].SUP_NAME,
					        		        		    "dep_id":depId,
					        		        		    "dep_code":DEP_CODE,
					        		        		    "dep_name":DEP_NAME,
					        		        		    "chr_id":nodes[0].CHR_ID,
					        		        		    "chr_code":nodes[0].CHR_CODE,
					        		        		    "chr_name":nodes[0].CHR_NAME.split("月")[0],
					        		        		    "parent_id":nodes[0].PARENT_ID,
					        		        		    "book_id":BOOK_ID,
					        		        		    "acc_sup_dep_id":workData[i].ACC_SUP_DEP_ID,
					        		        		    "op_type":op_type,
					        		        		    "op_name":op_name,
					        		        		    "menu_id":menuId,
					        		        		    "billtype_code":sumBillTypeCode,
					        		        		    "billtype_code1":billTypeCode,
					        		        		    "obj_type_id":workData[i].OBJ_TYPE_ID,
					        		        	}
					    	    	        	$.ajax({
					    	    		            type: 'get',
					    	    		            url: officeSumURL + "?tokenid=" + tokenid + "&ajax=noCache",
					    	    		            data: data,
					    	    		            dataType: 'JSON',
					    	    		            async: false,
					    	    		            success: function (result) {
					    	    		            	if(result.errorCode == 0) {
					    	    		            		
					    	    		            	}else {
					    	    		            		monthlyerror++;
					    	    		            		ip.ipInfoJump(result.errorMsg, 'error');
					    	    		            	}
					    	    		            }, error: function () {
					    	    		            	monthlyerror++;
					    	    		            	ip.ipInfoJump("错误", 'error');
					    	    		            }		                
					    	    	            });
				        					}
					        			}
//		        					}else{
//		        						ip.ipInfoJump("月份未完结，不可汇总", 'info');
//		        					}
		        				}
			        			if(monthlyerror == 0){
		        					ip.ipInfoJump("处汇总成功", 'success');
		        				}
		        				viewModel.initTree();
		        				viewModel.refresh(nodes[0].CHR_CODE,nodes[0].CHR_NAME.split("月")[0],nodes[0].PARENT_ID,getAccSupEndURL);
		        				var selectnodes2 = treeObj.getNodeByParam("CHR_ID", nodes[0].CHR_ID, null);
		        				treeObj.selectNode(selectnodes2);
		        				for(var btnNum=0;btnNum<btnData.length;btnNum++){
		                    		var btnId = btnData[btnNum].BUTTON_ID;
		                    		if(btnData[btnNum].DISPLAY_STATUS == "9" || btnData[btnNum].DISPLAY_STATUS == selectnodes2.STATUS_CODE2){
		                    			$("#"+btnId).removeAttr("disabled","disabled");
		                    		}else{
		                    			$("#"+btnId).attr("disabled","disabled");
		                    		}
		                    	}
	    					}
	    				}else{
	    					ip.ipInfoJump("该月份未全部待处汇总，不可处汇总", 'info');
	    				}
	        		}else{
	        			ip.ipInfoJump("请选择处汇总月份", 'info');
	        		}
	        	}else{
	        		ip.ipInfoJump("请选择处汇总月份", 'info');
	        	}	        		        		        	
	        };
	        
	        //处撤销汇总officeUndoSumURL
	        UndoSummary = function(id) {
	        	var btnData = viewModel.btnDataTable.getSimpleData();
	        	for(var bt = 0; bt<btnData.length;bt++) {
	        		if(btnData[bt].BUTTON_ID == id) {
	        			var btn = btnData[bt];
	        			break;
	        		}
	        	}
	        	var op_name = btn.DISPLAY_TITLE;
        		var op_type = btn.ACTION_TYPE;
	        	var treeObj = $.fn.zTree.getZTreeObj("bookSumTree");
	        	var nodes = treeObj.getSelectedNodes();
	        	if(nodes.length > 0) {
	        		if(nodes[0].LEVEL_NUM == "3"){
	        			var workData = viewModel.detailgridViewModel.gridData.getSimpleData();
	        			var sid = "";
	        			var bookData = viewModel.bookSumTreeDataTable.getSimpleData();
	        			var num = 0;
	    				var nodeChildFlag = 0;
	    				for(var b =0;b<bookData.length;b++){
	    					if(bookData[b].PARENT_ID == nodes[0].CHR_ID){
	    						num++;
	    						if(bookData[b].STATUS_CODE2 == "9"){
	        						nodeChildFlag++;
	        					}
	    					}
	    				}
	    				if(num == nodeChildFlag){
	    					if(nodes[0].STATUS_CODE2 == "0"){
	    						if(workData.length <= 0){
			        				var data = {
		        							"id":null,
		        							"dep_task_id":null,
		        		        		    "sid":null,
		        		        		    "set_id":null,
		        		        		    "sup_type_id":null,
		        		        		    "sup_name":null,
		        		        		    "chr_id":nodes[0].CHR_ID,
		        		        		    "chr_code":nodes[0].CHR_CODE,
		        		        		    "chr_name":nodes[0].CHR_NAME.split("月")[0],
		        		        		    "parent_id":nodes[0].PARENT_ID,
		        		        		    "book_id":BOOK_ID,
		        		        		    "acc_sup_dep_id":null,
		        		        		    "op_type":op_type,
		        		        		    "op_name":op_name,
		        		        		    "menu_id":menuId,
		        		        		    "billtype_code":sumBillTypeCode,
		        		        		    "billtype_code1":billTypeCode,
		        		        		    "is_input":null,
		        		        		    "obj_type_id":null,
		        		        	}
		    	    	        	$.ajax({
		    	    		            type: 'get',
		    	    		            url: officeUndoSumURL + "?tokenid=" + tokenid + "&ajax=noCache",
		    	    		            data: data,
		    	    		            dataType: 'JSON',
		    	    		            async: false,
		    	    		            success: function (result) {
		    	    		            	if(result.errorCode == 0) {
		    	    		            		
		    	    		            	}else {
		    	    		            		monthlyerror++;
		    	    		            		ip.ipInfoJump(result.errorMsg, 'error');
		    	    		            	}
		    	    		            }, error: function () {
		    	    		            	monthlyerror++;
		    	    		            	ip.ipInfoJump("错误", 'error');
		    	    		            }		                
		    	    	            });
			        			}else{
		        					var monthlyerror = 0;
			        				for(var i=0;i<workData.length;i++){
			        					if(monthlyerror > 0){
			        						break;
			        					}else{
				        					sid = workData[i].SID;
				        					var data = {
				        							"id":workData[i].ID,
				        							"dep_task_id":workData[i].DEP_TASK_ID,
				        		        		    "sid":sid,
				        		        		    "set_id":workData[i].SET_ID,
				        		        		    "sup_type_id":workData[i].SUP_TYPE_ID,
				        		        		    "sup_name":workData[i].SUP_NAME,
				        		        		    "chr_id":nodes[0].CHR_ID,
				        		        		    "chr_code":nodes[0].CHR_CODE,
				        		        		    "chr_name":nodes[0].CHR_NAME.split("月")[0],
				        		        		    "parent_id":nodes[0].PARENT_ID,
				        		        		    "book_id":BOOK_ID,
				        		        		    "acc_sup_dep_id":workData[i].ACC_SUP_DEP_ID,
				        		        		    "op_type":op_type,
				        		        		    "op_name":op_name,
				        		        		    "menu_id":menuId,
				        		        		    "billtype_code":sumBillTypeCode,
				        		        		    "billtype_code1":billTypeCode,
				        		        		    "is_input":workData[i].IS_INPUT,
				        		        		    "obj_type_id":workData[i].OBJ_TYPE_ID,
				        		        	}
				    	    	        	$.ajax({
				    	    		            type: 'get',
				    	    		            url: officeUndoSumURL + "?tokenid=" + tokenid + "&ajax=noCache",
				    	    		            data: data,
				    	    		            dataType: 'JSON',
				    	    		            async: false,
				    	    		            success: function (result) {
				    	    		            	if(result.errorCode == 0) {
				    	    		            		
				    	    		            	}else {
				    	    		            		monthlyerror++;
				    	    		            		ip.ipInfoJump(result.errorMsg, 'error');
				    	    		            	}
				    	    		            }, error: function () {
				    	    		            	monthlyerror++;
				    	    		            	ip.ipInfoJump("错误", 'error');
				    	    		            }		                
				    	    	            });
			        					}
				        			}
			        				
		        				}
			        			if(monthlyerror == 0){
		        					ip.ipInfoJump("处撤销汇总成功", 'success');
		        				}
		        				viewModel.refresh(nodes[0].CHR_CODE,nodes[0].CHR_NAME.split("月")[0],nodes[0].PARENT_ID,getAccSupEndURL);
		        				viewModel.initTree();
		        				var selectnodes2 = treeObj.getNodeByParam("CHR_ID", nodes[0].CHR_ID, null);
		        				treeObj.selectNode(selectnodes2);
		        				for(var btnNum=0;btnNum<btnData.length;btnNum++){
		                    		var btnId = btnData[btnNum].BUTTON_ID;
		                    		if(btnData[btnNum].DISPLAY_STATUS == "9" || btnData[btnNum].DISPLAY_STATUS == selectnodes2.STATUS_CODE2){
		                    			$("#"+btnId).removeAttr("disabled","disabled");
		                    		}else{
		                    			$("#"+btnId).attr("disabled","disabled");
		                    		}
		                    	}
	    					}
	    				}else{
	    					ip.ipInfoJump("该月份未汇总，不可撤销", 'info');
	    				}
	        		}else{
	        			ip.ipInfoJump("请选择撤销汇总月份", 'info');
	        		}
	        	}else{
	        		ip.ipInfoJump("请选择撤销汇总月份", 'info');
	        	}	        		        		        	
	        };
	        
	        //退回
	        workFlowReturn = function(id){
	        	var btnData = viewModel.btnDataTable.getSimpleData();
	        	for(var bt = 0; bt<btnData.length;bt++) {
	        		if(btnData[bt].BUTTON_ID == id) {
	        			var btn = btnData[bt];
	        			break;
	        		}
	        	}
	        	var op_name = btn.DISPLAY_TITLE;
        		var op_type = btn.ACTION_TYPE;
	        	var treeObj = $.fn.zTree.getZTreeObj("bookSumTree");
				var nodes = treeObj.getSelectedNodes();
	        	if(nodes.length > 0) {
        			var workData = viewModel.detailgridViewModel.gridData.getSimpleData();
        			var sid = "";
    				if(nodes[0].STATUS_CODE2 == "11" && nodes[0].IS_LEAF == "1"){
    					if(workData.length <= 0){
            				var data = {
        							"entity_id":nodes[0].CHR_CODE,
    	    	        			"op_name" : op_name,
    	    	        			"op_type" : op_type,
    	    	        			"menu_id" : menuId,
                        			"billtype_code" : billTypeCode,
        		        	}
    	    	        	$.ajax({
    	    		            type: 'get',
    	    		            url: doPayWorkFlowURL + "?tokenid=" + tokenid + "&ajax=noCache",
    	    		            data: data,
    	    		            dataType: 'JSON',
    	    		            async: false,
    	    		            success: function (result) {
    	    		            	if(result.errorCode == 0) {
    	    		            		
    	    		            	}else {
    	    		            		monthlyerror++;
    	    		            		ip.ipInfoJump(result.errorMsg, 'error');
    	    		            	}
    	    		            }, error: function () {
    	    		            	monthlyerror++;
    	    		            	ip.ipInfoJump("错误", 'error');
    	    		            }		                
    	    	            });
            			}else{
        					var monthlyerror = 0;
            				for(var i=0;i<workData.length;i++){
            					if(monthlyerror > 0){
            						break;
            					}else{
    	        					sid = workData[i].SID;
    	        					var data = {
    	        							"entity_id":nodes[0].CHR_CODE,
    	    	    	        			"op_name" : op_name,
    	    	    	        			"op_type" : op_type,
    	    	    	        			"menu_id" : menuId,
    	                        			"billtype_code" : billTypeCode,
    	        		        	}
    	    	    	        	$.ajax({
    	    	    		            type: 'get',
    	    	    		            url: doPayWorkFlowURL + "?tokenid=" + tokenid + "&ajax=noCache",
    	    	    		            data: data,
    	    	    		            dataType: 'JSON',
    	    	    		            async: false,
    	    	    		            success: function (result) {
    	    	    		            	if(result.errorCode == 0) {
    	    	    		            		
    	    	    		            	}else {
    	    	    		            		monthlyerror++;
    	    	    		            		ip.ipInfoJump(result.errorMsg, 'error');
    	    	    		            	}
    	    	    		            }, error: function () {
    	    	    		            	monthlyerror++;
    	    	    		            	ip.ipInfoJump("错误", 'error');
    	    	    		            }		                
    	    	    	            });
            					}
    	        			}
            				
        				}
            			viewModel.refresh(nodes[0].CHR_CODE,nodes[0].CHR_NAME.split("月")[0],nodes[0].PARENT_ID,getAccSupEndURL);
        				viewModel.initTree();
        				if(monthlyerror == 0){
        					ip.ipInfoJump("退回成功", 'success');
        				}
        				var selectnodes2 = treeObj.getNodeByParam("CHR_ID", nodes[0].CHR_ID, null);
        				treeObj.selectNode(selectnodes2);
        				for(var btnNum=0;btnNum<btnData.length;btnNum++){
                    		var btnId = btnData[btnNum].BUTTON_ID;
                    		if(btnData[btnNum].DISPLAY_STATUS == "9" || btnData[btnNum].DISPLAY_STATUS == selectnodes2.STATUS_CODE2){
                    			$("#"+btnId).removeAttr("disabled","disabled");
                    		}else{
                    			$("#"+btnId).attr("disabled","disabled");
                    		}
                    	}
    				}else{
    					ip.ipInfoJump("该月月结未提交，不可退回", 'info');
    				}
	        	}else{
	        		ip.ipInfoJump("请选择退回月份", 'info');
	        	}	
	        }
	        
	        //撤销审核
	        workFlowUndo = function(id){
	        	var btnData = viewModel.btnDataTable.getSimpleData();
	        	for(var bt = 0; bt<btnData.length;bt++) {
	        		if(btnData[bt].BUTTON_ID == id) {
	        			var btn = btnData[bt];
	        			break;
	        		}
	        	}
	        	var op_name = btn.DISPLAY_TITLE;
        		var op_type = btn.ACTION_TYPE;
	        	var treeObj = $.fn.zTree.getZTreeObj("bookSumTree");
				var nodes = treeObj.getSelectedNodes();
	        	if(nodes.length > 0) {
	        		if(nodes[0].LEVEL_NUM == "3"){
	        			var workData = viewModel.sumgridViewModel.gridData.getSimpleData();
	        			var sid = "";
	        			var bookData = viewModel.bookSumTreeDataTable.getSimpleData();
	        			var num = 0;
	    				var nodeChildFlag = 0;
	    				for(var b =0;b<bookData.length;b++){
	    					if(bookData[b].PARENT_ID == nodes[0].CHR_ID){
	    						num++;
	    						if(bookData[b].STATUS_CODE2 == "9"){
	        						nodeChildFlag++;
	        					}
	    					}
	    				}
	    				if(num == nodeChildFlag){
	    					if(nodes[0].STATUS_CODE2 == "1"){
	    						if(workData.length <= 0){
			        				var data = {
		        							"entity_id":nodes[0].CHR_CODE,
		    	    	        			"op_name" : op_name,
		    	    	        			"op_type" : op_type,
		    	    	        			"menu_id" : menuId,
		                        			"billtype_code" : sumBillTypeCode,
		        		        	}
		    	    	        	$.ajax({
		    	    		            type: 'get',
		    	    		            url: doPayWorkFlowURL + "?tokenid=" + tokenid + "&ajax=noCache",
		    	    		            data: data,
		    	    		            dataType: 'JSON',
		    	    		            async: false,
		    	    		            success: function (result) {
		    	    		            	if(result.errorCode == 0) {
		    	    		            		
		    	    		            	}else {
		    	    		            		monthlyerror++;
		    	    		            		ip.ipInfoJump(result.errorMsg, 'error');
		    	    		            	}
		    	    		            }, error: function () {
		    	    		            	monthlyerror++;
		    	    		            	ip.ipInfoJump("错误", 'error');
		    	    		            }		                
		    	    	            });
			        			}else{
		        					var monthlyerror = 0;
			        				for(var i=0;i<workData.length;i++){
			        					if(monthlyerror > 0){
			        						break;
			        					}else{
				        					sid = workData[i].SID;
				        					var data = {
				        							"entity_id":nodes[0].CHR_CODE,
				    	    	        			"op_name" : op_name,
				    	    	        			"op_type" : op_type,
				    	    	        			"menu_id" : menuId,
				                        			"billtype_code" : sumBillTypeCode,
				        		        	}
				    	    	        	$.ajax({
				    	    		            type: 'get',
				    	    		            url: doPayWorkFlowURL + "?tokenid=" + tokenid + "&ajax=noCache",
				    	    		            data: data,
				    	    		            dataType: 'JSON',
				    	    		            async: false,
				    	    		            success: function (result) {
				    	    		            	if(result.errorCode == 0) {
				    	    		            		
				    	    		            	}else {
				    	    		            		monthlyerror++;
				    	    		            		ip.ipInfoJump(result.errorMsg, 'error');
				    	    		            	}
				    	    		            }, error: function () {
				    	    		            	monthlyerror++;
				    	    		            	ip.ipInfoJump("错误", 'error');
				    	    		            }		                
				    	    	            });
			        					}
				        			}
			        				
		        				}
			        			if(monthlyerror == 0){
		        					ip.ipInfoJump("撤销审核成功", 'success');
		        				}
		        				viewModel.initTree();
		        				viewModel.refresh(nodes[0].CHR_CODE,nodes[0].CHR_NAME.split("月")[0],nodes[0].PARENT_ID,getAccSupEndURL);
		        				var selectnodes2 = treeObj.getNodeByParam("CHR_ID", nodes[0].CHR_ID, null);
		        				treeObj.selectNode(selectnodes2);
		        				for(var btnNum=0;btnNum<btnData.length;btnNum++){
		                    		var btnId = btnData[btnNum].BUTTON_ID;
		                    		if(btnData[btnNum].DISPLAY_STATUS == "9" || btnData[btnNum].DISPLAY_STATUS == selectnodes2.STATUS_CODE2){
		                    			$("#"+btnId).removeAttr("disabled","disabled");
		                    		}else{
		                    			$("#"+btnId).attr("disabled","disabled");
		                    		}
		                    	}
	    					}
	    					
	    				}
	        		}else{
	        			ip.ipInfoJump("请该月份未审核，不可撤销审核", 'info');
	        		}
	        	}else{
	        		ip.ipInfoJump("请选择月结月份", 'info');
	        	}
	        };
	        
	        //修改汇总
	        publicModify = function(id) {
	        	var selectRow = viewModel.sumgridViewModel.gridData.getSelectedRows();
				if(selectRow.length == 1){
					$('#modifySum').html("");
					viewModel.initData("3");
					$('#modifySumModal').modal({
	                	show : true,
	                	backdrop : 'static'
	                });
					var sumData = viewModel.sumDataTable.getSimpleData();
					for(var i=0;i<sumData.length;i++) {
						$.each(selectRow[0].data, function(key, value){
    						if(key === sumData[i].rid){
    							if(key == "SUP_MONEY"){
    								$("#"+sumData[i].id).change( function(event) {
    									var money = $('#'+event.target.id).val();
    									if(money != "" && money != null){
    										if(csof.isNum(money) == false){
       								          ip.ipInfoJump("请输入数字!", 'info');
       								          $('#'+event.target.id).val("");
	       								    }else{
	       										var fomoney = csof.fmoney(money,2);
	       										$('#'+event.target.id).val(fomoney);
	       									}
    									}
    								});
    							}
    							if(key == "SUP_COST"){
    								//$('#'+sumData[i].id).attr('placeholder','单位为人天')
                                    $("#"+sumData[i].id).change( function(event) {
                                    	if($('#'+event.target.id).val() != "" && $('#'+event.target.id).val() != null){
                                    		if(csof.isNum($('#'+event.target.id).val()) == false){
                              		    	    ip.ipInfoJump("单位为人天，请输入数字!", 'info');
                              		    	    $('#'+event.target.id).val("");
                              		        }
                                    	}
    								});
    							}
    							$("#"+sumData[i].id).val(value.value);
    							if(sumData[i].type == "treeassist"){
	            					var all_options = {
	            							"element": sumData[i].source,
	            							"tokenid": tokenid,
	            							"ele_value": "",
	            							"ajax": "noCache",
	            							"condition": ""
	            						};
	            						$.ajax({
	            							url: "/df/dic/dictree.do",
	            							type: "GET",
	            							async: false,
	            							data: ip.getCommonOptions(all_options),
	            							success: function(map) {
	            								for(var j=0;j<map.eleDetail.length;j++){
	            									if(map.eleDetail[j].chr_name == value.value){
	            										$("#"+sumData[i].id+"-h").val(map.eleDetail[j].chr_id + "@" + encodeURI(map.eleDetail[j].chr_name, "utf-8") + "@" + map.eleDetail[j].chr_code + "@" + map.eleDetail[j].pId);
	            									}
	            								}
	            							}
	            						});
	            				}
    						}
    					})
					}
				}else{
					ip.ipInfoJump("请选择一个需要修改的汇总事项", 'info');
				}
	        }
	        
	        //修改汇总完成
	        viewModel.modifySum = function() {
	        	var aim = viewModel.inputViewDataTable.getSimpleData();
				var noVerifyRid =["REMARK"];
				if(verifyInputView(aim,noVerifyRid)) {
					var selectRow = viewModel.sumgridViewModel.gridData.getSelectedRows();
					var sumData = viewModel.sumDataTable.getSimpleData();
					var sumEx = [];
					sumEx[0] = {};
					$.each(selectRow[0].data, function(key, value){
						sumEx[0][key] = value.value;
						for(var i=0;i<sumData.length;i++) {
							if(key === sumData[i].rid){
								if(sumData[i].type == "treeassist") {
									var value = $('#'+sumData[i].id+"-h").val();
									var sArray = sumData[i].rid.split("_");
									var saBefore = "";
									for(var sa = 0;sa<sArray.length-1;sa++){
										saBefore = saBefore + sArray[sa] + "_";
									}
									var saId = saBefore + "ID";
									var saName = sumData[i].rid;
									var saCode = saBefore + "CODE";
									sumEx[0][saId] = value.split("@")[0];
									sumEx[0][saName] = decodeURI(value.split("@")[1]);
									sumEx[0][saCode] = value.split("@")[2];
								}else{
									var value = $('#'+sumData[i].id).val();
									sumEx[0][key] = value;
								}
								break;
							}
						}
					});
					var data = {
							"data" : JSON.stringify(sumEx[0]),
						}
						$.ajax({
				            type: 'get',
				            url: updateAccSupDepURL + "?tokenid=" + tokenid + "&ajax=noCache",
				            data: data,
				            dataType: 'JSON',
				            async: true,
				            success: function (result) {
				            	if(result.errorCode == 0) {
				            		$('#modifySumModal').modal('toggle');
				            		$('#modifySum').html("");
				            		var treeObj = $.fn.zTree.getZTreeObj("bookSumTree");
				    				var nodes = treeObj.getSelectedNodes();
				    	        	if(nodes.length > 0) {
				            			viewModel.refresh(nodes[0].CHR_CODE,nodes[0].CHR_NAME.split("月")[0],nodes[0].PARENT_ID,getAccSupEndURL);
				    	        	}
				            	}else {
				            		ip.ipInfoJump(result.message, 'error');
				            	}
				            }, error: function () {
				            	ip.ipInfoJump("错误", 'error');
				            }		                
			            });
				}else{
					ip.ipInfoJump("请完善红框数据", 'error');
				}
	        }
	        
	        //提交
	        workFlowSup = function(id) {
	        	var btnData = viewModel.btnDataTable.getSimpleData();
	        	for(var bt = 0; bt<btnData.length;bt++) {
	        		if(btnData[bt].BUTTON_ID == id) {
	        			var btn = btnData[bt];
	        			break;
	        		}
	        	}
	        	var op_name = btn.DISPLAY_TITLE;
        		var op_type = btn.ACTION_TYPE;
	        	var treeObj = $.fn.zTree.getZTreeObj("bookSumTree");
				var nodes = treeObj.getSelectedNodes();
	        	if(nodes.length > 0) {
	        		if(nodes[0].LEVEL_NUM == "3"){
	        			var workData = viewModel.sumgridViewModel.gridData.getSimpleData();
	        			var sid = "";
	        			var bookData = viewModel.bookSumTreeDataTable.getSimpleData();
	        			var num = 0;
	    				var nodeChildFlag = 0;
	    				for(var b =0;b<bookData.length;b++){
	    					if(bookData[b].PARENT_ID == nodes[0].CHR_ID){
	    						num++;
	    						if(bookData[b].STATUS_CODE2 == "9"){
	        						nodeChildFlag++;
	        					}
	    					}
	    				}
	    				if(num == nodeChildFlag){
	    					if(nodes[0].STATUS_CODE2 == "0"){
	    						if(workData.length <= 0){
			        				var data = {
		        							"entity_id":nodes[0].CHR_CODE,
		    	    	        			"op_name" : op_name,
		    	    	        			"op_type" : op_type,
		    	    	        			"menu_id" : menuId,
		                        			"billtype_code" : sumBillTypeCode,
		        		        	}
		    	    	        	$.ajax({
		    	    		            type: 'get',
		    	    		            url: doPayWorkFlowURL + "?tokenid=" + tokenid + "&ajax=noCache",
		    	    		            data: data,
		    	    		            dataType: 'JSON',
		    	    		            async: false,
		    	    		            success: function (result) {
		    	    		            	if(result.errorCode == 0) {
		    	    		            		
		    	    		            	}else {
		    	    		            		monthlyerror++;
		    	    		            		ip.ipInfoJump(result.errorMsg, 'error');
		    	    		            	}
		    	    		            }, error: function () {
		    	    		            	monthlyerror++;
		    	    		            	ip.ipInfoJump("错误", 'error');
		    	    		            }		                
		    	    	            });
			        			}else{
		        					var monthlyerror = 0;
			        				for(var i=0;i<workData.length;i++){
			        					if(monthlyerror > 0){
			        						break;
			        					}else{
				        					sid = workData[i].SID;
				        					var data = {
				        							"entity_id":nodes[0].CHR_CODE,
				    	    	        			"op_name" : op_name,
				    	    	        			"op_type" : op_type,
				    	    	        			"menu_id" : menuId,
				                        			"billtype_code" : sumBillTypeCode,
				        		        	}
				    	    	        	$.ajax({
				    	    		            type: 'get',
				    	    		            url: doPayWorkFlowURL + "?tokenid=" + tokenid + "&ajax=noCache",
				    	    		            data: data,
				    	    		            dataType: 'JSON',
				    	    		            async: false,
				    	    		            success: function (result) {
				    	    		            	if(result.errorCode == 0) {
				    	    		            		
				    	    		            	}else {
				    	    		            		monthlyerror++;
				    	    		            		ip.ipInfoJump(result.errorMsg, 'error');
				    	    		            	}
				    	    		            }, error: function () {
				    	    		            	monthlyerror++;
				    	    		            	ip.ipInfoJump("错误", 'error');
				    	    		            }		                
				    	    	            });
			        					}
				        			}
			        				
		        				}
			        			if(monthlyerror == 0){
		        					ip.ipInfoJump("提交成功", 'success');
		        				}
		        				viewModel.initTree();
		        				viewModel.refresh(nodes[0].CHR_CODE,nodes[0].CHR_NAME.split("月")[0],nodes[0].PARENT_ID,getAccSupEndURL);
		        				var selectnodes2 = treeObj.getNodeByParam("CHR_ID", nodes[0].CHR_ID, null);
		        				treeObj.selectNode(selectnodes2);
		        				for(var btnNum=0;btnNum<btnData.length;btnNum++){
		                    		var btnId = btnData[btnNum].BUTTON_ID;
		                    		if(btnData[btnNum].DISPLAY_STATUS == "9" || btnData[btnNum].DISPLAY_STATUS == selectnodes2.STATUS_CODE2){
		                    			$("#"+btnId).removeAttr("disabled","disabled");
		                    		}else{
		                    			$("#"+btnId).attr("disabled","disabled");
		                    		}
		                    	}
	    					}
	    					
	    				}
	        		}else{
	        			ip.ipInfoJump("请该月份未汇总，不可提交", 'info');
	        		}
	        	}else{
	        		ip.ipInfoJump("请选择月结月份", 'info');
	        	}
	        }
	         
	        //处汇总基础汇总导出
	        sumExpFun = function(id){
	        	var btnData = viewModel.btnDataTable.getSimpleData();
	        	for(var bt = 0; bt<btnData.length;bt++) {
	        		if(btnData[bt].BUTTON_ID == id) {
	        			var btn = btnData[bt];
	        			break;
	        		}
	        	}
	        	var acc_id = btn.PARAM.split("accId=")[1];
	        	if(acc_id == "#"){
	        		acc_id = ACC_ID;
	        	}
	        	$("#sumAccExpPage")[0].src = "";
	        	var treeObj = $.fn.zTree.getZTreeObj("bookSumTree");
				var nodes = treeObj.getSelectedNodes();
	        	if(nodes.length > 0) {
	        		if(nodes[0].LEVEL_NUM == "3"){
	        			var workData = viewModel.sumgridViewModel.gridData.getSimpleData();
	        			if(workData.length > 0){
	        				var data = {
									"setId"	:nodes[0].CHR_ID,
									"oId" :options.svOfficeId,
									"accId":ACC_ID,
									"bookId":BOOK_ID,
									"sumFlag":"2",//处汇总标识
								};
							$("#sumAccExpModal").modal({
			                	show : true,
			                	backdrop : 'static'
			                });
			            	$("#sumAccExpPage").attr("src", getReportURL + "?tokenid=" +  ip.getTokenId() + "&ajax=noCache" + "&setId="+nodes[0].CHR_ID+"&oId="+options.svOfficeId+"&accId="+acc_id+"&bookId="+BOOK_ID+"&sumFlag=2");
	        			}else{
		        			ip.ipInfoJump("请选择已汇总月份进行导出", 'info');
		        		}
	        		}else{
	        			ip.ipInfoJump("请选择已汇总月份进行导出", 'info');
	        		}
	        	}else{
	        		ip.ipInfoJump("请选择已汇总月份进行导出", 'info');
	        	}
	        }	        
//	        
//	        //处汇总台账导出
//	        repExport = function(id) {
//	        	$("#sumAccExpPage")[0].src = "";
//	        	var treeObj = $.fn.zTree.getZTreeObj("bookSumTree");
//				var nodes = treeObj.getSelectedNodes();
//	        	if(nodes.length > 0) {
//	        		if(nodes[0].LEVEL_NUM == "3"){
//	        			var data = {
//								"setId"	:nodes[0].CHR_ID,
//								"oId" :options.svOfficeId,
//								"accId":ACC_ID,
//								"bookId":BOOK_ID,
//								"sumFlag":"2",//处汇总标识
//							};
//						$("#sumAccExpModal").modal({
//		                	show : true,
//		                	backdrop : 'static'
//		                });
//		            	$("#sumAccExpPage").attr("src", getReportURL + "?tokenid=" +  ip.getTokenId() + "&ajax=noCache" + "&setId="+nodes[0].CHR_ID+"&oId="+options.svOfficeId+"&accId="+ACC_ID+"&bookId="+BOOK_ID+"&sumFlag=2");
//	        		}else{
//	        			ip.ipInfoJump("请选择已汇总月份进行导出", 'info');
//	        		}
//	        	}else{
//	        		ip.ipInfoJump("请选择已汇总月份进行导出", 'info');
//	        	}
//	        }
	        setInterval(csscsof.rightTwoHeight(), 500)//每半秒执行一次windowHeight函数
			//初始化
			function init() {
				app = u.createApp({
					el : document.body,
					model : viewModel
				});
				tokenid = ip.getTokenId();
				options = ip.getCommonOptions({});
				options['tokenid'] = tokenid;
				var url=window.location.href;
				elecode = url.split("elecode=")[1].split("&")[0];
	            menuId = url.split("menuid=")[1].split("&")[0];
	            menuName = decodeURI(url.split("menuname=")[1].split("&")[0]);
	            billTypeCode = url.split("billTypeCode=")[1].split("&")[0];
	            sumBillTypeCode = url.split("sumBillTypeCode=")[1].split("&")[0];
	            ACC_ID = url.split("ACC_ID=")[1].split("&")[0];
	            BOOK_ID = url.split("BOOK_ID=")[1].split("&")[0];
	            BOOK_NAME = decodeURI(url.split("BOOK_NAME=")[1].split("&")[0]);
	            SET_YEAR = url.split("SET_YEAR=")[1].split("&")[0];
	            SET_MONTH = url.split("SET_MONTH=")[1].split("&")[0];
	            depId = options.svDepId;
	            DEP_CODE = options.svDepCode;
	            DEP_NAME = options.svDepName;
				viewModel.initData("");//初始化grid
				viewModel.initTree();//初始化树
				//viewModel.initYearMonth();//初始化期间
				var data = initBtns(menuId);
        		if(data == false){
        			ip.ipInfoJump("加载按钮出错", 'error');
        		}else{
        			viewModel.btnDataTable.setSimpleData(data);
        		}//初始化按钮
        		for(var i=0;i<data.length;i++) {
        			if(data[i].PARAM != "" && data[i].PARAM != null){
        				if(ACC_ID == "#"){
        					if(data[i].PARAM.split("accId=")[1] == "#"){
        						$("#"+data[i].BUTTON_ID).hide();
            				}
        				}else{
                            if(data[i].PARAM.split("accId=")[1] == "1" || data[i].PARAM.split("accId=")[1] == "2"){
                            	$("#"+data[i].BUTTON_ID).hide();
            				}
        				}
        			}
        		}
        		csscsof.rightTwoHeight();
			}
			init();
		});