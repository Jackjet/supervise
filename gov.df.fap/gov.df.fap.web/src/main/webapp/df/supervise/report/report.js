/**
 * 附件管理JS处理
 * 2017-08-17 whd
 */
require(['jquery', 'knockout','bootstrap', 'uui','tree', 'jquery.file.upload','grid', 'ip'],
    function ($, ko, _) {
        window.ko = ko;
        var tokenid;
        var options;
        tokenid = ip.getTokenId();
        options = ip.getCommonOptions({});
        options['tokenid'] = tokenid;
        
        //初始化
        function init() {
            u.createApp({
                el: document.body
            });
            var url = window.location.href;
            var getReportURL = '/ReportForm/selectEReportByChrId.do';//获取报表

            var rid ="";
            var param_add_str="" ;
            if(url.indexOf("rid=")>0){
            	rid=url.split("rid=")[1].split("&")[0];           //报表id
            }
            if(url.indexOf("param")>0){
            	param_add_str = url.split("param=")[1].split("&")[0];     //
            }
            
            var bbq_date = ""; 
            var readonly = "";
        	var data = {
					"chrId"	:rid,
					"obj_id" :"",
					"param_add_str":param_add_str,
					"bbq_date":bbq_date,
					"readonly":readonly,
				};
        	$("#report").attr("src", getReportURL + "?tokenid=" +  ip.getTokenId() + "&ajax=noCache" + "&chrId="+rid+"&obj_id="+""+"&param_add_str=" +param_add_str+"&bbq_date="+bbq_date+"&readonly="+readonly);
//				$.ajax({
//			        type: 'post',
//			        url: getReportURL + "?tokenid=" + tokenid + "&ajax=noCache",
//			        data: data,
//			        dataType: 'JSON',
//			        async: false,
//			        success: function (result) {
//			            if (result.errorCode == 0) {
//			            	$("#report")[0].src = result.data;
//			            } else {
//			                ip.ipInfoJump("错误", 'error');
//			            }
//			        }, error: function () {
//			            ip.ipInfoJump("错误", 'error');
//			        }
//			    });
		
            
        }
        
        init();
    }
);