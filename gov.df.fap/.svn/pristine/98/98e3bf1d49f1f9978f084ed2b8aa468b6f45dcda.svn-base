var oldpwd, newpwd, confirmpwd, btn;
var tokenid = "";

window.onload = function(){
	btn = document.getElementById("btn");
	btn.onclick = function(){
		oldpwd = document.getElementById("oldpwd").value;
		newpwd = document.getElementById("newpwd");
		confirmpwd = document.getElementById("confirmowd");
		registerPwd();
	}
}

function registerPwd(){
	if(newpwd.value != confirmpwd.value){
		alert("两次输入的新密码不同");
		pwdWrongRecord();
		return;
	}

	var _oldpwd = hex_md5(oldpwd.value);
	$.ajax({
		url:"/df/portal/registerPwd.do",
		type:"GET",
		dataType:"json",
		data:{
			"ajax":"ajax",
			"tokenid":tokenid,
			"oldpwd":_oldpwd,
			"newpwd":newpwd.value,
			"confirmpwd":confirmpwd.value
		},
		success:function(data){
			var flag = data.flag;
			if(flag == "1"){
				alert("修改成功，请重新登录");
			}else if(flag == "0"){
				alert(data.msg);
			}
		}
	});
}

function pwdCheck() {
	var _rule = {
		"long" : {
			weak : 4,
			medium : 8,
			strong : 12
		}
	};
	var _long;
	if (_rule.long.weak > pwd.length)
		_long = 1;
	else if (_rule.long.medium > pwd.length)
		_long = 2;
	else
		_long = 3;
	var _strong = 0;
	var _pwd_letter = pwd.replace(/[0-9]/gi, '');
	var _pwd_num = pwd.replace(/[a-z,A-Z]/gi, '');
	if (pwd.length > _pwd_letter.length)
		_strong++;
	if (pwd.length > _pwd_num.length)
		_strong++;

	if (_long + _strong < 3)
		return true; 
	return false;
}

function pwdWrongRecord(){
	$.ajax({
		url:"/df/portal/registerPwdWrongRecord.do",
		type:"GET",
		dataType:"json",
		data:{"ajax":"ajax","tokenid":tokenid},
		success:function(data){
		}
	});
}
