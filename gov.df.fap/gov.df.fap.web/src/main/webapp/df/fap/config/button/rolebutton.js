require(['jquery', 'knockout', 'bootstrap', 'uui','tree','grid','director','ip'],function ($, ko) {
	window.ko = ko;
	var tokenid = ip.getTokenId();
	var roleid = null;
	var guid = null;
	var status_id = null;
	var menuViewModel = {
			data: ko.observable({}),
			treeSetting:{
				view:{
				},
				callback:{
					onClick:function(e,id,node){
						$("#checklist").empty();
						menuViewModel.treeDataTable3.setSimpleData("");
						if(!node.isParent){
							var guid = node.id;
							roleid =guid;
							var tokenid = ip.getTokenId();
							$.ajax({
								url: "/df/menu/getbyrole.do?tokenid="+tokenid,
								type: 'GET',
								dataType: 'json',
								data: {"guid":guid,"tokenId":tokenid ,"ajax":"noCache"},
								success: function (data) {
									for(var i=0;i<data.mapMenu.length;i++){
										data.mapMenu[i].url = null;
									}
									menuViewModel.treeDataTable1.setSimpleData(data.mapMenu);
									var data_tree = $("#robtnRoleTree2")[0]['u-meta'].tree;
									var nodes  = data_tree.getNodes();
									data_tree.expandNode(nodes[0], true, false, true);
								}
							});

						}else{
							menuViewModel.treeDataTable1.setSimpleData("");
						}
					}
				}
			},
			treeSetting1:{
				view:{
				},
				callback:{
					onClick:function(e,id,node){
						$("#checklist").empty();
						if(!node.isParent){
							guid = node.id;
							var tokenid = ip.getTokenId();
							$.ajax({
								url: "/df/menu/menuStatusTree.do?tokenid="+tokenid,
								type: 'GET',
								dataType: 'json',
								data: {"menu_id":guid,"role_id":roleid,"ajax":"noCache"},
								success: function (data) {
									var statustree = data.statree;
									menuViewModel.treeDataTable3.setSimpleData(statustree);
									if(data.flag == "1"){
										var check = data.check;
										var treeObj = $.fn.zTree.getZTreeObj("statusMenuTree");
										for(var k = 0 ; k <check.length ; k++ ){
											var id = check[k].status_id;
											var search_nodes = treeObj.getNodesByParam("id",id,null);
											treeObj.checkNode(search_nodes[0], true, true);
										}
									}else{
										var treeObj = $.fn.zTree.getZTreeObj("statusMenuTree");
										var search_nodes = treeObj.getNodesByParam("id","#",null);
										treeObj.setChkDisabled(search_nodes[0], true);
										treeObj.checkNode(search_nodes[0], true, true);
									}
								}
							});

						}else{
							menuViewModel.treeDataTable3.setSimpleData("");
						}
					}
				}
			},
			treeSetting2:{
				view:{
					showLine:false,
					selectedMulti:false
				},
				check: {
					enable: true,
					chkStyle: "checkbox",
					chkboxType: { "Y": "ps", "N": "ps" }
				},
				callback:{
					onClick:function(e,id,node){
						$("#checklist").empty();
						if(!node.isParent){
							status_id = node.id;
							$.ajax({
								url: "/df/menu/BtnCheck.do?tokenid="+tokenid,
								type: 'GET',
								dataType: 'json',
								data: {"menu_id":guid,"role_id":roleid,"status_id":status_id,"ajax":"noCache"},
								success: function (data) {
									var innerHTML = "";
									for(var i = 0; i < data.btncheck.length; i++ ){
										var btnid = data.btncheck[i].action_id;
										var btnName = data.btncheck[i].name;
										var roleId = data.btncheck[i].chr_id;
										if(roleId == null ||　roleId　==　""){
											innerHTML+="	<div class='checkbox'><label> <input type='checkbox' id='"+status_id+"' onclick='change(this)' value='"+btnid+"'> "+btnName+" </label></div>"
										}else{
											innerHTML+="	<div class='checkbox'><label> <input type='checkbox' checked='checked' id='"+status_id+"' onclick='change(this)'  value='"+roleId+"'> "+btnName+" </label></div>"
										}

									}
									$("#checklist").append(innerHTML);
								}
							});

						}
					},
					onCheck: function(event, treeId, treeNode){
						var flag = treeNode.checked;
						var status_id = treeNode.status_id;
						if(flag == true){
							$.ajax({
								url: "/df/menu/insertmenuStatus.do?tokenid="+tokenid,
								type: 'POST',
								dataType: 'json',
								data: {"menu_id":guid,"role_id":roleid,"status_id":status_id,"ajax":"noCache"},
								success: function (data) {

								}
							});
						}else {
							$.ajax({
								url: "/df/menu/delmenuStatus.do?tokenid="+tokenid,
								type: 'POST',
								dataType: 'json',
								data: {"menu_id":guid,"role_id":roleid,"status_id":status_id,"ajax":"noCache"},
								success: function (data) {

								}
							});
						}
					}
				}
			},
			treeDataTable: new u.DataTable({
				meta: {
					'guid': {
						'value':""
					},
					'roletype': {
						'value':""
					},
					'name':{
						'value':""
					}
				}
			}),
			treeDataTable1: new u.DataTable({
				meta: {
					'guid': {
						'value':""
					},
					'roletype': {
						'value':""
					},
					'name':{
						'value':""
					}
				}
			}),
			treeDataTable2: new u.DataTable({
				meta: {
					'guid': {
						'value':""
					},
					'roletype': {
						'value':""
					},
					'name':{
						'value':""
					}
				}
			}),
			treeDataTable3: new u.DataTable({
				meta: {
					'status_id': {
						'value':""
					},
					'pid': {
						'value':""
					},
					'name':{
						'value':""
					}
				}
			})
	}
	menuViewModel.getInitData = function(){
		var tokenid = ip.getTokenId();
		$.ajax({
			url: "/df/role/getAllRole.do?tokenid=" + tokenid,
			type: 'GET',
			dataType: 'json',
			data: tokenid,
			success: function (data) {
				var treedata = ip.treeJump(data.rolelist);
				menuViewModel.treeDataTable.setSimpleData(treedata);
//				var data_tree = $("#robtnRoleTree1")[0]['u-meta'].tree;
//				var nodes  = data_tree.getNodes();
//				data_tree.expandNode(nodes[0], true, false, true);
			}
		});

	}
	change = function (obj){
		var tokenid = ip.getTokenId();
		var btnid = obj.value;
		var status_id = obj.id;
		var checked = obj.checked;
		if(checked == true){
			$.ajax({
				url: "/df/menu/insertBtnCheck.do?tokenid=" + tokenid,
				type: 'post',
				dataType: 'json',
				data: {"action_id":btnid,"menu_id":guid,"role_id":roleid,"status_id":status_id,"ajax":"noCache"},
				success: function (data) {
					$("#checklist").empty();
					$.ajax({
						url: "/df/menu/BtnCheck.do?tokenid="+tokenid,
						type: 'GET',
						dataType: 'json',
						data: {"menu_id":guid,"role_id":roleid,"status_id":status_id,"ajax":"noCache"},
						success: function (data) {
							var innerHTML = "";
							for(var i = 0; i < data.btncheck.length; i++ ){
								var btnid = data.btncheck[i].action_id;
								var btnName = data.btncheck[i].name;
								var roleId = data.btncheck[i].chr_id;
								if(roleId == null ||　roleId　==　""){
									innerHTML+="	<div class='checkbox'><label> <input type='checkbox' id='"+status_id+"' onclick='change(this)' value='"+btnid+"'> "+btnName+" </label></div>"
								}else{
									innerHTML+="	<div class='checkbox'><label> <input type='checkbox' checked='checked' id='"+status_id+"' onclick='change(this)'  value='"+roleId+"'> "+btnName+" </label></div>"
								}

							}
							$("#checklist").append(innerHTML);
						}
					});
				}
			});
		}else{
			$.ajax({
				url: "/df/menu/delBtnCheck.do?tokenid=" + tokenid,
				type: 'post',
				dataType: 'json',
				data: {"chr_id":btnid,"ajax":"noCache"},
				success: function (data) {
					$("#checklist").empty();
					$.ajax({
						url: "/df/menu/BtnCheck.do?tokenid="+tokenid,
						type: 'GET',
						dataType: 'json',
						data: {"menu_id":guid,"role_id":roleid,"status_id":status_id,"ajax":"noCache"},
						success: function (data) {
							var innerHTML = "";
							for(var i = 0; i < data.btncheck.length; i++ ){
								var btnid = data.btncheck[i].action_id;
								var btnName = data.btncheck[i].name;
								var roleId = data.btncheck[i].chr_id;
								if(roleId == null ||　roleId　==　""){
									innerHTML+="	<div class='checkbox'><label> <input type='checkbox' id='"+status_id+"' onclick='change(this)' value='"+btnid+"'> "+btnName+" </label></div>"
								}else{
									innerHTML+="	<div class='checkbox'><label> <input type='checkbox' checked='checked' id='"+status_id+"' onclick='change(this)'  value='"+roleId+"'> "+btnName+" </label></div>"
								}

							}
							$("#checklist").append(innerHTML);
						}
					});
				}
			});
		}
	}
	var val="";
	quickQuery = function (){  
		var user_write = $("#quickquery").val();
		if(val == user_write){
			return;
		}
		val = user_write
		var data_tree = $("#robtnRoleTree1")[0]['u-meta'].tree;
		var search_nodes = data_tree.getNodesByParamFuzzy("name",user_write,null);
		data_tree.expandNode(search_nodes[0],true,false,true);
		data_tree.selectNode(search_nodes[0]);
		$("#quickquery").focus();
		i=1;
	}
	var i = 0;
	menuTreeNext = function (){
		var user_write = $("#quickquery").val();
		var data_tree = $("#robtnRoleTree1")[0]['u-meta'].tree;
		var search_nodes = data_tree.getNodesByParamFuzzy("name",user_write,null);
		if(i < search_nodes.length){
			data_tree.selectNode(search_nodes[i++]);
		}else{
			i = 0;
			ip.ipInfoJump("最后一个");
		}
		$("#quickquery").focus();
	}

	var val1 = "";
	quickQuery1 = function (){  
		var user_write = $("#quickquery1").val();
		if(val1 == user_write){
			return;
		}
		val1 = user_write
		var data_tree = $("#robtnRoleTree2")[0]['u-meta'].tree;
		var search_nodes = data_tree.getNodesByParamFuzzy("name",user_write,null);
		data_tree.expandNode(search_nodes[0],true,false,true);
		data_tree.selectNode(search_nodes[0]);
		$("#quickquery1").focus();
		j = 1;
	}
	var j = 0;
	menuTreeNext1 = function (){
		var user_write = $("#quickquery1").val();
		var data_tree = $("#robtnRoleTree2")[0]['u-meta'].tree;
		var search_nodes = data_tree.getNodesByParamFuzzy("name",user_write,null);
		if(j < search_nodes.length){
			data_tree.selectNode(search_nodes[j++]);
		}else{
			j = 0;
			ip.ipInfoJump("最后一个");
		}
		$("#quickquery1").focus();
	}

	$(function () {
		ko.cleanNode($('body')[0]);
		app = u.createApp({
			el: 'body',
			model: menuViewModel
		});
		menuViewModel.getInitData();
	});
});
