function getTokenId(){
	return dfp.tokenid.getTokenId();
}

$(function(){
	window.onload = function(){
		var tokenId = getTokenId();
		var caroleguid = localStorage.select_role_guid==undefined?"":localStorage.select_role_guid;
		$.ajax({
			url:"/df/portal/initBudget.do",
			type:"GET",
			data:{"tokenid":tokenId, "caroleguid":Base64.encode(caroleguid)},
			dataType:"json",
			success: function(data){
				// public params to localStorage
            	//toLocalStorage(data.publicParam);
            	
            	// public params to hidden label
            	//toHiddenLabel(data.publicParam);

				$("#svFiscalPeriod").val(dfp.encrypt.base64.encode(data.publicParam.svFiscalPeriod));	// 会计期间
				$("#svMenuId").val(dfp.encrypt.base64.encode(data.publicParam.svMenuId));	// 菜单ID
				$("#svRgCode").val(dfp.encrypt.base64.encode(data.publicParam.svRgCode));	// 区划CODE
				$("#svRgName").val(dfp.encrypt.base64.encode(data.publicParam.svRgName));	// 区划ID
				$("#svRoleCode").val(dfp.encrypt.base64.encode(data.publicParam.svRoleCode));	// 角色CODE
				$("#svRoleId").val(dfp.encrypt.base64.encode(data.publicParam.svRoleId));	// 角色ID
				$("#svRoleName").val(dfp.encrypt.base64.encode(data.publicParam.svRoleName));	// 角色名称
				$("#svSetYear").val(dfp.encrypt.base64.encode(data.publicParam.svSetYear));	// 年度
				$("#svTransDate").val(dfp.encrypt.base64.encode(data.publicParam.svTransDate));	// 业务日期
				$("#svUserCode").val(dfp.encrypt.base64.encode(data.publicParam.svUserCode));	// 用户CODE
				$("#svUserId").val(dfp.encrypt.base64.encode(data.publicParam.svUserId));	// 用户ID
				$("#svUserName").val(dfp.encrypt.base64.encode(data.publicParam.svUserName));	// 用户名称
				$("#svAgencyCode").val(dfp.encrypt.base64.encode(data.publicParam.svAgencyCode));	// 单位Code
				$("#svAgencyName").val(dfp.encrypt.base64.encode(data.publicParam.svAgencyName));	// 单位名称
        		
        		
    	    	
    	    	// 获取代办事项
				getBudgetTask();
            	
			}
		});
	}
	
	

	
});

