/**
 * Created by yanqiong on 2017/3/23.
 */
require(['jquery', 'knockout', '/df/fap/system/config/ncrd.js', 'bootstrap', 'uui', 'tree', 'grid', 'ip'], function ($, ko, ncrd) {
  window.els = [] // elements
  // 不需要双向绑定的变量
  var state = {
    coas: [],
    els: [],
    selectData: null, // 被选中的单元格里面保存的数据
    coaData: null, // 更新coa需要传给后台的数据
    tokenid: ip.getTokenId()+'&ajax=noCache',
    GET_COA: '/df/coa/getCoa.do',
    LIST_APP_DO_URL : '/df/coa/getAllCoa.do',//获取所有coa
    LIST_Element_APP_DO_URL : '/df/coa/getAllElement.do', //获取当前所有启用要素信息
    DEL_COA_DO_URL : '/df/coa/deleteCoa.do', //删除Coa
    // level01: [
    //   {title: '指标管理', children: ['要素级次']},
    //   {title: '指标管理', children :['总待分指标明细COA','处室待分指标明细COA']},
    //   {title: '指标管理', children :['总待分指标明细COA','处室待分指标明细COA']},
    //   {title: '指标管理', children :['总待分指标明细COA','处室待分指标明细COA']},
    //   {title: '指标管理', children :['总待分指标明细COA','处室待分指标明细COA']},
    //   {title: '指标管理', children :['总待分指标明细COA','处室待分指标明细COA']},
    // ],
    // 第二行的编号
    UPDATE_CASCADE_COD_DTO: '/df/coa/updateCascadeCoaDto.do',
    UPDATE_COA_DTO: '/df/coa/updateCoaDto.do',
    timer: null,
    loadCount: 0,
    editApp: null,
  }
  var vm = {
    row2col1: {width: ko.observable(110), height: ko.observable(64)}, // 第二行第一列的宽高
    cascadeText: ko.observable(''),  // 级联保存提示信息
    cellWidthAndHeight: {width: 100, height: 36}, // 内容区单个单元格预定义宽高
    bgColors: ['pink','yellow','green','orange'],
    level: [
      {text: '', value: ''},
      {text: '一级', value: 1},
      {text: '二级', value: 2},
      {text: '三级', value: 3},
      {text: '四级', value: 4},
      {text: '五级', value: 5},
      {text: '六级', value: 6},
      {text: '七级', value: 7},
      {text: '八级', value: 8},
      {text: '九级', value: 9},
    ],
    levelAny: [
      {text: '自定义级次', value: 0},
      {text: '任意级次', value: -2},
      {text:'底级', value: -1},
    ],
    levelAll: function() {
      return this.level.concat(this.levelAny)
    },
    // 被选中coa的信息
    selectCoa: {selected: ko.observable(false), coaId: ko.observable(-1), rowNum: ko.observable(-1), colNum: ko.observable(1)},
    els: ko.observableArray(),
    coas: ko.observableArray(),
    row2: function() {
      var arr = []
      this.level01().forEach(function(item) {
        item.children.forEach(function(coa) {
          // push coa的编号
          arr.push(coa)
        })
      })
      return arr
    },
    // 第一行和第二行的viewModel
    level01: function() {
      var arr = [
        {title: '业务阶段', children: [['要素级次']]},
        {title: '指标管理', children: []},
        {title: '计划管理', children: []},
        {title: '支付管理', children: []},
        {title: '实拨管理', children: []},
        {title: '其他', children: []},
      ]
      this.coas().forEach(function(coa) {
        var index = -1
        arr.some(function(item, j) {
          if(item.title === coa[2]) {
            index = j
            return true
          }
        })
        if(index > -1) {
          arr[index].children.push(coa)
        }
      }.bind(this))
      // 从新排序
      return arr
    },
    // body: [
    //   [
    //     {
    //       "last_ver":"2006-08-29 16:45:00",
    //       "disp_order":"16",
    //       "is_view":"0",
    //       "czgb_code":"ADMCHARGE",
    //       "is_operate":"1",
    //       "ele_source":"ELE_INCOME_ITEM",
    //       "ele_type":"1",
    //       "old_ele_code":"IN_BIS",
    //       "ele_code":"IN_BIS",
    //       "level_name":"001004001&001004002",
    //       "ref_mode":"0",
    //       "rg_code":"3700",
    //       "old_ele_source":"ELE_INCOME_ITEM",
    //       "is_rightfilter":"0",
    //       "max_level":"4",
    //       "enabled":"1",
    //       "latest_op_date":"2006-09-28 12:29:03",
    //       "chr_id":"{35D007EA-E835-4111-B309-BDB0FAA1BB2D}",
    //       "sys_id":"001",
    //       "set_year":"2016",
    //       "is_system":"1",
    //       "is_nolevel":"0",
    //       "is_deleted":"0",
    //       "old_ele_name":"预算收入项目",
    //       "code_rule":"3-3-3-3",
    //       "ele_name":"预算收入项目",
    //       "dispmode":"1",
    //       "is_local":"0"
    //     },
    //     {
    //       "levelNum":-1,
    //       "coaDto":null,
    //       "eleCode":"AGENCYEXP",
    //       "eleName":"预算单位支出结构代码",
    //       "coaId":"30",
    //       "coaDetailId":"{2D0AA920-B9FD-11DC-9D46-D0ED5BACD289}",
    //       "coaDetailCode":null,
    //       "defaultValue":null,
    //       "setYear":0,
    //       "dispOrder":0,
    //       "isMustInput":0,
    //       "rgCode":"",
    //       "levelName":"底级"
    //     }
    //   ]
    // ],

    body: function() { // 表格第三行及以下行
      var arr = []
      var rows = [] // 第一列的eleCode
      var row2coaIdList = this.row2().map(function(coa) {return coa[1]}) // 第二行的coa编号
      var that = this
      var level01 = this.level01()
      var bgColors = this.bgColors
      var coaParentArr = ['指标管理','计划管理','支付管理','实拨管理','其他']
      // 每行的行首
      this.els().forEach(function(el, i) {
        arr[i] = arr[i] || []
        arr[i].push(el)
        rows.push(el.ele_code)
      })
      for(var i = 0, len1 = rows.length; i < len1; i++) {
        for(var j = 1, len2 = row2coaIdList.length; j < len2; j++) {
          arr[i][j] = {}
          this.coas().some(function(coa) {
            if(coa[1] === row2coaIdList[j]) {
              arr[i][j].bgColor = bgColors[coaParentArr.indexOf(coa[2])]
              arr[i][j].rowNum = i
              arr[i][j].coaId = coa[1]
              arr[i][j].levelNum = ko.observable('')
              arr[i][j].oldLevelName = ''
              arr[i][j].levelName = ko.observable('')
              arr[i][j].eleCode = rows[i]
              arr[i][j].eleName = arr[i][0].ele_name
              arr[i][j].empty = true
              arr[i][j].max_level = arr[i][0].max_level
              return true
            }
          })
        }
      }
      // 每行的其它
      this.coas().forEach(function(coa) {
        coa[3].forEach(function(item) {
          item.bgColor = bgColors[coaParentArr.indexOf(coa[2])]
          var i = rows.indexOf(item.eleCode)
          var j = row2coaIdList.indexOf(item.coaId)
          if(i > -1) {
            item.rowNum = i
            item.oldLevelName = item.levelName
            item.levelNum = ko.observable(item.levelNum)
            item.levelName = ko.observable(item.levelName)
            item.max_level = arr[i][0].max_level
            arr[i][j] = item
          }
        })
      })
      return arr
    },

    // 切换级别
    changeSelect: function(data, obj, e) {
      if(data.levelName() === obj.text) return
      data.levelName(obj.text)
      data.levelNum(obj.value)
      state.selectData = data
      getCoa(data.coaId, data, update)

      // ip.ipInfoJump('更改成功')
    },

    // 点击第二行及内容区域, 获取列信息
    getCoa: function(data, e) {
      var coaId = data.coaId ? data.coaId : data[1]
      var colNum
      vm.row2().some(function(col, i) {
        if(col[1] === coaId) {
          colNum = i
          return true
        }
      })
      if(!data[1]) { // 点击内容区域
        vm.selectCoa.selected(false)
        vm.selectCoa.coaId(data.coaId)
        vm.selectCoa.rowNum(data.rowNum)
      } else { // 点击标题
        vm.selectCoa.selected(true)
        vm.selectCoa.coaId(data[1])
        // vm.selectCoa.rowNum(data.rowNum)
      }
      vm.selectCoa.colNum(colNum)
    },
    btnAddClick: function() {
      var code = 0;
      loadEditApp(onEditAppLoaded, code);
    },
    saveCascadeCoa: function() {
      $('#cascadeModal').modal('hide')
      cascadeUpdate(state.coaData)
    },
    cancelSaveCoa: function() {
      revertLevel()
      $('#cascadeModal').modal('hide')
    },
    btnDelClick: function() {
      var coaId = this.selectCoa.coaId()
      if(coaId === -1) {
        ip.ipInfoJump('未选中要删除的coa', 'info')
      } else {
        $('#confirmModal').modal({backdrop: false})
      }
    },
    btnEditClick: function() {
      var coaId = this.selectCoa.coaId()
      if(coaId === -1) {
        ip.ipInfoJump('未选中要编辑的coa', 'info')
        return
      }
      loadEditApp(onEditAppLoaded, coaId)
    },
    confirmDelCoa: function() {
      delCoa(this.selectCoa.coaId())
      $('#confirmModal').modal('hide')
    },
    // 获取第二行第一列的表格的宽高
    getRow2Col1: function() {
      if(state.timer) {
        clearInterval(state.timer)
        state.timer = null
      }
      state.timer = setTimeout(function() {
        var $row2col1 = $('[data-name="row2col1"]')
        vm.row2col1.width($row2col1.outerWidth())
        vm.row2col1.height($row2col1.outerHeight())
      },0)
    },
  }


  // 要素级次回到上一次的值(更新要素级次失败时需回到原先状态)
  function revertLevel() {
    // state.selectData.levelNum(state.selectData.oldLevelNum)
    state.selectData.levelName(state.selectData.oldLevelName)
  }

  // 级联更新coaDto
  function cascadeUpdate(data) {
    loading(true)
    $.ajax({
      url: state.UPDATE_CASCADE_COD_DTO +'?tokenid='+state.tokenid,
      method: 'POST',
      data:JSON.stringify(data),
      contentType: 'application/json',
      success: function(data) {
        loading(false)
        if(data.errorCode === -1) {
          getAllCoa({async: false})
          // revertLevel()
          ip.ipInfoJump(data.errorMessage, 'error')
        } else {
          state.selectData.oldLevelNum = state.selectData.levelNum()
          getAllCoa({async: false})
          ip.ipInfoJump('保存成功', 'success')
        }
      },
      error: function() {
        loading(false)
        revertLevel()
      }
    })
  }

  // 更新coaDto
  function update(data) {
    loading(true)
    $.ajax({
      url: state.UPDATE_COA_DTO+'?tokenid='+state.tokenid,
      method: 'POST',
      data:JSON.stringify(data),
      async: false,
      contentType: 'application/json',
      success: function(data) {
        loading(false)
        if(data.errorCode === -1 ) {
          if(data.errorMessage.replace('级联保存+', '')==''){
            cascadeUpdate(state.coaData);
          }else{
            $('#cascadeModal').modal({backdrop: false});
            vm.cascadeText(data.errorMessage);
          }

        } else {
          ip.ipInfoJump('保存成功', 'success')
        }
      },
      error: function() {
        loading(false)
        revertLevel()
      },
    })
  }

  // 删除coa
  function delCoa(coaId) {
    // ip.loading(true)
    loading(true)
    $.ajax({
      url: state.DEL_COA_DO_URL +'?tokenid='+state.tokenid+'&coa_id='+coaId,
      success: function(res) {
        loading(false)
        if(res.errorCode === 0) {
          getAllCoa({async: true})
          ip.ipInfoJump('删除成功', 'success')
          vm.selectCoa.coaId(-1)
        } else {
          // ip.ipInfoJump('删除失败')
          ip.ipInfoJump(res.errorMessage, 'error')
        }
      },
      error: function() {
        loading(false)
      }
    })
  }

  // 获取所有coa的信息
  function getAllCoa(opt) {
    ip.loading(true)
    opt = opt || {}
    opt.async = opt.async === undefined ? true : opt.async
    // 获取coas
    $.ajax({
      url: state.LIST_APP_DO_URL + '?tokenid='+state.tokenid,
      async: opt.async,
      success: function(data) {
        if(data.errorCode == 0) {
          // vm.coas(data.coaLists)
          state.coas = data.coaLists
          if(++state.loadCount === 2) {
            state.loadCount = 1
            initPage()
          }
        } else {
          ip.ipInfoJump('错误', 'error')
        }
      },
      error: function() {
        ip.ipInfoJump('网络错误', 'error')
        // vm.coas(coas)
      }
    })
  }

  function initPage() {
    vm.coas(state.coas)
    vm.els(state.els)
    ip.loading(false)
    $('#main').removeClass('hide')
  }

  // 获取单个coa的信息
  function getCoa(coaid,cell,changeCoa) {
    $.ajax({
      url: state.GET_COA + '?tokenid='+state.tokenid,
      data:{'coaid':coaid},
      dataType:'JSON',
      async: false,
      success: function(data) {
        if(data.errorCode == 0) {
          data = data.coaDto
          var item = {
            "coaDetailCode":"",
            "coaDetailId":"",
            "coaDto":"",
            "coaId":cell.coaId,
            "defaultValue":"",
            "dispOrder":0,
            "eleCode":cell.eleCode,
            "eleName":cell.eleName,
            "isMustInput":0,
            "levelName":cell.levelName(),
            "levelNum":cell.levelNum(),
            "rgCode":"",
            "setYear":0
          }
          if(cell.empty) {
            data.coaDetail.push(item)
          } else {
            data.coaDetail.some(function(coa, i, arr) {
              if(coa.eleCode === cell.eleCode) {
                if(cell.levelNum() !== '') {
                  coa.levelNum = cell.levelNum()
                  coa.levelName = cell.levelName()
                } else {
                  arr.splice(i, 1)
                }
                return true
              }
            })
          }
          data.coaDetailList = data.coaDetail
          delete data.key
          state.coaData = data
          changeCoa(data)
          // vm.coas(data.coaLists)
          // callback(data)
        } else {
          ip.ipInfoJump('错误', 'error')
        }
      },
      error: function() {
        ip.ipInfoJump('网络错误', 'error')
        revertLevel()
      }
    })
  }

  function getAllElement() {
    ip.loading(true)
    // 获取elements
    $.ajax({
      url: state.LIST_Element_APP_DO_URL + '?tokenid='+state.tokenid,
      // async: false,
      success: function(data) {
        if(data.errorCode == 0) {
          // vm.els(data.data)
          state.els = data.data
          if(++state.loadCount === 2) {
            state.loadCount = 1
            initPage()
          }
          window.els = data.data
        } else {
          ip.ipInfoJump('错误', 'error')
        }
      },
      error: function() {
        // vm.els(els)
        ip.ipInfoJump('网络错误', 'error')
      },
    })
  }

  // dropdown处理
  $(document).on('show.bs.dropdown', '.js-dropdown',function(e) {
    if($(e.target).offset().top+380 > $(window).height()) {
      $(e.target).addClass('dropup')
    }
  })
  $(document).on('hidden.bs.dropdown', '.js-dropdown',function(e) {
    $(e.target).removeClass('dropup')
  })

  // 编辑页面关闭时的回调事件，save:保存关闭 cancel：取消关闭
  var onEditAppClose = {
    save : function(data) {
      getAllCoa({async: false});
      $('#f1-edit-container').modal('hide')
    },
    cancel : function() {
      $('#f1-edit-container').modal('hide')
    }
  };

  // 编辑页面加载完成后的回调函数，需要等待编辑页面加载完才能执行的代码放在这里
  function onEditAppLoaded(code) {
    state.editApp.show(code, onEditAppClose);
  };

  /**
   * 加载编辑模块方法
   * @param onLoaded = onEditAppLoaded
   */
  function loadEditApp(onLoaded,code) {
    if($('#f1-edit-container').length) {
      $('#f1-edit-container').modal({backdrop: false})
      onLoaded(code)
      return
    }
    // var container = $('#fl-edit-container');
    var url = "fap/system/config/coa/edit";
    requirejs.undef(url);
    require([url], function (module) {
      // ko.cleanNode(container[0]);
      // container.html('');
      // container.html(module.template);
      $(document.body).append(module.template)
      $('#f1-edit-container').modal({backdrop: false})
      module.init();
      state.editApp = module;
      if(onLoaded){
        onLoaded(code);
      }
    });
  };

  //深度克隆
  function deepClone(obj){
    var result,oClass=isClass(obj);
    //确定result的类型
    if(oClass==="Object"){
      result={};
    }else if(oClass==="Array"){
      result=[];
    }else{
      return ko.observable(obj)
    }
    for(key in obj){
      var copy=obj[key];
      if(isClass(copy)=="Object"){
        result[key]= arguments.callee(copy);//递归调用
      }else if(isClass(copy)=="Array"){
        result[key]= ko.observableArray(arguments.callee(copy));
      }else{
        result[key]=ko.observable(obj[key]);
      }
    }
    return result;
  }
  //返回传递给他的任意对象的类
  function isClass(o){
    if(o===null) return "Null";
    if(o===undefined) return "Undefined";
    return Object.prototype.toString.call(o).slice(8,-1);
  }

  // 固定首列和表头
  $('#mainContent').scroll(function(e) {
    var target = e.target
    if(target.scrollLeft !== $('#fixRow').scrollLeft())
      $('#fixRow').scrollLeft(target.scrollLeft)
    if(target.scrollTop !== $('#fixCol').scrollTop()) {
      $('#fixCol').scrollTop(target.scrollTop)
    }
  })

  function loading(flag) {
    ip.loading(flag)
    if(flag) $('#myLoading').removeClass('hide')
    else $('#myLoading').addClass('hide')
  }

  function init() {
    ko.applyBindings(vm, $('#main')[0])
    getAllCoa({async: true})
    getAllElement()
  }

  init()

});
