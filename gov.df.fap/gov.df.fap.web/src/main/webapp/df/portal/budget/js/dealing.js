


// 获取待办事项
function getBudgetTask(){
    
	var tokenId = getTokenId();
	var userId = $("#svUserId").val();
	var roleId = $("#svRoleId").val();
	var region = $("#svRgCode").val();
	
	$.ajax({
		url:"/df/portal/getBudgetTask.do",
		type:"GET",
		data:{
			"tokenid":tokenId,
			"userid":userId,
			"roleid":roleId,
			"region":region
		},
		dataType:"json",
		success:function(data){
		    //TODO ?? 真实数据
			//alert(data[0].name);
			
			var html = "";
			var count = 1;
			for(var i=0; i<data.length; i++){
				//alert(i);
				var name = data[i].name;
				//alert(name);
				//var task = dealingThing[i].task_content;
				var url = "http://10.10.65.194:7007/gfmis".replace(/(^\s*)|(\s*$)/g, "")+data[i].url.replace(/(^\s*)|(\s*$)/g, "")+'&tokenid='+getTokenId();
				//html += '<li><span class="icon"></span><a href="'+ 'url' +'" target="_blank">'+ '专项指标录入' +' &nbsp;&nbsp;<span class="c-red">'+ '未送审1条' +'</span></a></li>';
				html += '<li><span class="icon"></span><a href="'+url+'" target="_blank">'+name+' &nbsp;&nbsp;<span class="c-red"></span></a></li>';
				count++;
				if(count==7){
					break;
				}
			}
			$("#list-r2").html(html);
			
		
		}
	});
}
