define(['jquery', 'knockout', 'bootstrap', 'uui', 'tree', 'grid', 'ip','jqxsplitter','jqxcore','csof'],
    function ($, ko) {
        window.ko = ko;

        function windowHeight() {
            var h = document.body.clientHeight; //获取当前窗口可视操作区域高度
            var bodyHeight = document.getElementById("main-content"); //寻找ID为content的对象
            $('#main-content').jqxSplitter({ width: '98.3%', height: (h - 65) + "px", panels: [{ size: '23.2%', min: 200 }, { min: 800, size: '76.8%' }] });
            $('.treeDiv').height($("#left-main-content").height() - 35 + "px");
            $('.tab-panel-height').height($("#right-main-content").height() - 32 + "px");
            $('.rightTableGrid').height($(".tab-panel-height").height() - 26 + "px");
        }
        
        function windowHeightNoNav() {
            var h = document.body.clientHeight; //获取当前窗口可视操作区域高度
            var bodyHeight = document.getElementById("main-content"); //寻找ID为content的对象
            $('#main-content').jqxSplitter({ width: '98.3%', height: (h - 65) + "px", panels: [{ size: '23.2%', min: 200 }, { min: 800, size: '76.8%' }] });
            $('.treeDiv').height($("#left-main-content").height() - 35 + "px");
            $('.rightTableGrid-nonav').height($("#right-main-content").height() - 26 + "px");
        }
        
        function rightTwoHeight() {
        	 var h = document.body.clientHeight; //获取当前窗口可视操作区域高度
             var bodyHeight = document.getElementById("main-content"); //寻找ID为content的对象
             $('#main-content').jqxSplitter({ width: '98.3%', height: (h - 65) + "px", panels: [{ size: '23.2%', min: 200 }, { min: 800, size: '76.8%' }] });
	    	 $('#right-top-bottom-content').jqxSplitter({ width: '100%', height: '100%', orientation: 'horizontal', panels: [{ size: '60%',  min: 60 , collapsible: false },{ size: '40%',  min: 27 }] });
             $('.treeDiv').height($("#left-main-content").height() - 35 + "px");
             $('.tab-panel-height').height($("#right-main-content").height() - 32 + "px");
            // $('.top-rightTableGrid').height($(".top-panel-div").height() - 21 + "px");
            // $('.bottom-rightTableGrid').height($(".bottom-panel-div").height() - 21 + "px");
        }
        
        function rightTwoHeightNoNav() {
       	    var h = document.body.clientHeight; //获取当前窗口可视操作区域高度
            var bodyHeight = document.getElementById("main-content"); //寻找ID为content的对象
            $('#main-content').jqxSplitter({ width: '98.3%', height: (h - 65) + "px", panels: [{ size: '23.2%', min: 200 }, { min: 800, size: '76.8%' }] });
	    	 $('#right-top-bottom-content').jqxSplitter({ width: '100%', height: '100%', orientation: 'horizontal', panels: [{ size: '60%',  min: 60 , collapsible: false },{ size: '40%',  min: 27 }] });
            $('.treeDiv').height($("#left-main-content").height() - 35 + "px");
            $('.rightTableGrid-nonav').height($("#right-main-content").height() - 26 + "px");
           // $('.top-rightTableGrid').height($(".top-panel-div").height() - 21 + "px");
           // $('.bottom-rightTableGrid').height($(".bottom-panel-div").height() - 21 + "px");
       }

        return {
            'windowHeight' : windowHeight,
            'rightTwoHeight' : rightTwoHeight,
            'windowHeightNoNav' : windowHeightNoNav,
            'rightTwoHeightNoNav' : rightTwoHeightNoNav,
        };
    }
)
;
