	 window.ctx = '/df';
	 var current_url = location.search;
	 var tokenid = current_url.substring(current_url
				.indexOf("tokenid") + 8,
				current_url.indexOf("tokenid") + 48);
//	$(document).ready(function(){
		SNS_BASE_BATH = window.getSNSBasePath();
		initSNSIM();
		// 初始化
		function initSNSIM(){
		YYIMChat.init({
			onOpened : SNSHandler.getInstance().onOpened,
			onClosed : SNSHandler.getInstance().onClosed,
			onAuthError : SNSHandler.getInstance().onAuthError,
			onStatusChanged : SNSHandler.getInstance().onStatusChanged,
			onConnectError : SNSHandler.getInstance().onConnectError,
			onUserBind : SNSHandler.getInstance().onUserBind,
			onPresence : SNSHandler.getInstance().onPresence,
			onSubscribe : SNSHandler.getInstance().onSubscribe,
			onRosterUpdateded : SNSHandler.getInstance().onRosterUpdateded,
			onRosterDeleted : SNSHandler.getInstance().onRosterDeleted,
			onRoomMemerPresence : SNSHandler.getInstance().onRoomMemerPresence,
			onReceipts : SNSHandler.getInstance().onReceipts,
			onTextMessage : SNSHandler.getInstance().onTextMessage,
			onPictureMessage : SNSHandler.getInstance().onPictureMessage,
			onFileMessage : SNSHandler.getInstance().onFileMessage,
			onShareMessage : SNSHandler.getInstance().onShareMessage,
			onSystemMessage: SNSHandler.getInstance().onSystemMessage,
			onPublicMessage: SNSHandler.getInstance().onPublicMessage,
			onLocationMessage: SNSHandler.getInstance().onLocationMessage,
			onAudoMessage : SNSHandler.getInstance().onAudoMessage,
			onWhiteBoardMessage : SNSHandler.getInstance().onWhiteBoardMessage,
			onMessageout : SNSHandler.getInstance().onMessageout,
			onGroupUpdate :  SNSHandler.getInstance().onGroupUpdate,
			onTransferGroupOwner :  SNSHandler.getInstance().onTransferGroupOwner,
			onKickedOutGroup : SNSHandler.getInstance().onKickedOutGroup,
			onPubaccountUpdate :SNSHandler.getInstance().onPubaccountUpdate
		});
	};
	
//});

/*
 * 登录的调用是在index.js调用的
 */
//调用这个方法
var loginIm = function (username,password,isToken) {
	alert(144);
    if (!SNSCommonUtil.isStringAndNotEmpty(username) && YYIMChat.enableAnonymous()) {
                YYIMChat.login(null, "anonymous", new Date().getTime() + 14 * (24 * 60 * 60 * 1000));
                return;
            }
            if (!username || username.isEmpty() || !password || password.isEmpty()) {
                //throw '用户名或密码为空';
            }

            SNSApplication._loginName = username;
            parseName(username);
            username = username.replace(/@/g, YYIMChat.getTenancy().SEPARATOR);

            if (DEVELOP_MODE) {
                YYIMChat.login(username, $.md5(password).toUpperCase());
                return;
            }
            if (isToken && isToken == true)
                YYIMChat.login(username, password);
            else
                SNSApplication.prototype.getToken(username, password);

            function parseName() {
                $.ajax({
                    type: 'POST',
                    url: "/df/imaccess/getImParam.do?tokenid="+tokenid,
                    data: {"ajax":1},
                    dataType: 'json',
                    async: false,
                    success: function (data) {
                        
                        var config={
                            "address":data.address,
                            "wsport":data.wsport,
                            "hbport" :data.hbport,
                            "servlet" :data.servlet,
                            "safeServlet" :data.safeServlet
                        };
                        YYIMChat.initSDK(data.appId, data.eptId,config);
                    }
                });
            }
};

//$(document).ready(function(){
	
	jQuery("div.link-item a").mouseover(function(event) {
		var img = $(this).find("img");
		var src = img.attr("src");
		img.attr("src", src.replace(".png", "_hl.png"));
		event.stopPropagation();
	});
	
	jQuery("div.link-item a").mouseout(function(event) {
		var img = $(this).find("img");
		var src = img.attr("src");
		img.attr("src", src.replace("_hl.png", ".png"));
		event.stopPropagation();
	});
	
	jQuery('#snsim_chat_window_tab_head').on('click','li',function(){
		setTimeout(function(){jQuery('#snsim_sendbox_content').focus();},0);
	});

	// 登陆的回车事件 -- start
	//jQuery("#username").focus();
	// 登陆的回车事件 --end
	/*
     * 登录的初始化方法
     */
	
    var username = jQuery("#user").text();
    var flag=false;  // 是否显示  ----已经登录并显示
	var islogin=false; // 是否登录---已经登录
	
	jQuery("img.loginBtn").bind("click", function() {
				if(userId!="" && userId!=null && userId!=undefined){
					if(navigator.appName == "Microsoft Internet Explorer" && (navigator.appVersion.match(/9./i)=="9." || navigator.appVersion.match(/8./i)=="8.")){
				       return;
				    }else{
						var username = jQuery("#user").text();
						if(flag){
							if(islogin){
								if(username){
									if(username && password){
										//SNSApplication.getInstance().login(username, '12346');
										//loginIm(username,'', false);
										islogin=false;
									}
								}	
							}else{
							    //alert(document.getElementById('snsim_window_narrow').style.display);
							    if(document.getElementById('snsim_window_narrow').style.display=='none' || document.getElementById('snsim_window_narrow').style.display==''){
									$("#snsim_window_wide").show();
								}else{
									$("#snsim_window_narrow").show();
								}
							}
							 flag=false;
						}else{
							flag=true;
							if(document.getElementById('snsim_window_narrow').style.display=='block'){
									$("#snsim_window_narrow").hide();
								}else{
									$("#snsim_window_wide").hide();
								}
						}
						}
				}else{
					return false;
				}
			});
//	});


