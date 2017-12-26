require(['jquery', 'knockout', 'bootstrap', 'uui', 'jquery.file.upload', 'grid', 'ip'], function ($, ko) {
  window.ko = ko;

  var app, tokenid;

  var viewModel = {
    sortNameValue: ko.observable(""),
    sortCodeValue: ko.observable(""),
    sortInfoValue: ko.observable(""),
    pathNameValue: ko.observable(""),
    attachInfoValue: ko.observable(""),
    attachCodeValue: ko.observable(""),
    attachNameValue: ko.observable(""),
    attachInformationValue: ko.observable(""),
    selectedSort: ko.observable(""),
    sortOptions: ko.observableArray([]),
    selectedSysId: ko.observable(""),
    sysIdOptions: ko.observableArray([]),
    attachNewCodeValue: ko.observable(""),
    attachOldCodeValue: ko.observableArray([]),
    selectedSortNumberValue: ko.observable(""),
    selectedAttachFileId: ko.observable(""),
    fileSize: ko.observable(0),
    fileDataArray: ko.observableArray([]),
    fileUploadData: ko.observable(""),

    //附件分类数据模型
    attachSort: new u.DataTable({
      meta: {
        'sortnumber': '',//分类编号
        'sortname': '',//分类名称
        'sortcode': '', //分类代号
        'sortinfo': '' //分类信息
      }
    }),
    //附件数据模型
    attachFiles: new u.DataTable({
      meta: {
        'attach_id': '',//附件ID
        'attach_code': '',//附件编号
        'attach_name': '',//附件名称
        'create_by': '',//附件上传人
        'createTime': '',//附件上传时间
        'attach_info': ''//附件说明
      },
      pageSize: 10
    }),

    //上传文件数据模型
    uploadedFiles: new u.DataTable({
      meta: {
        "attach_code": {
          type: 'string'
        },
        "name": {
          type: 'string'
        }
      }
    }, this)
  };

  /**
   * 初始化附件分类列表
   * @param sortCodeValue
   * @param sys_id
   */
  viewModel.initSortList = function (sortCodeValue, sys_id) {
    var req = $.ajax({
      type: 'GET',
      url: '/df/attach/getAttachCategroy.do?tokenid=' + tokenid,
      cache: false,
      dataType: 'json',
      data: {
        "sys_id": sys_id,
        "ajax": "noCache"
      }
    });
    req.done(function (result) {
      var errorCode = result.errorCode;
      if (errorCode === '0') {
        var sortList = result.data;
        viewModel.attachSort.setSimpleData(sortList);
        //初始化附件分类下拉列表
        viewModel.sortOptions([]);
        var AttachSort = function (sortnumber, sortname, sortcode) {
          this.sort_id = sortnumber;
          this.sort_name = '[' + sortcode + ']'+ sortname;
        };
        for (var i = 0, l = result.data.length; i < l; i++) {
          viewModel.sortOptions.push(new AttachSort(result.data[i].sortnumber, result.data[i].sortname, result.data[i].sortcode));
        }

        //初始化附件表格
        viewModel.attachFiles.setSimpleData([]);
        // 选中新增的节点
        if (sortCodeValue) {
          var rows = viewModel.attachSort.getAllRows();
          for (i = 0, l = rows.length; i < l; i++) {
            if (rows[i].getValue('sortcode') === sortCodeValue) {
              viewModel.attachSort.setRowSelect(i);
              var sortListGroup = $(".list-group");
              sortListGroup.find(".list-group-item").removeClass("sortActive");
              sortListGroup.find(".list-group-item").eq(i).addClass("sortActive");
              break;
            }
          }
        }
      } else {
        u.showMessage({
          msg: '附件分类初始化出现异常，请稍后重试',
          position: "bottom",
          msgType: "error"
        });
      }
    }).fail(function () {
      ip.ipInfoJump("附件分类初始化出现异常，请稍后重试！", "error");
    });
  };

  /**
   * 初始化附件列表
   * @param sortnumber
   * @param index
   * @param attachCode
   */
  viewModel.initAttachTable = function (sortnumber, index, attachCode) {
    if (index !== undefined) {
      var sortListGroup = $(".list-group");
      sortListGroup.find(".list-group-item").removeClass("sortActive");
      sortListGroup.find(".list-group-item").eq(index).addClass("sortActive");
      var sortname = viewModel.attachSort.getRow(index).getValue('sortnumber');
      viewModel.selectedSort(sortnumber);
      viewModel.selectedSortNumberValue(sortnumber);
      viewModel.attachFiles.pageIndex("0");
      viewModel.attachFiles.pageSize("10");
    }

    var currentPage = viewModel.attachFiles.pageIndex();
    var pageSize = viewModel.attachFiles.pageSize();
    // 传输数据
    var queryData = {
      "pageStart": currentPage,
      "pageSize": pageSize,
      "sortnumber": sortnumber,
      "ajax": "noCache"
    };

    $.ajax({
      type: 'GET',
      cache: false,
      url: '/df/attach/findAttachByCategoryId.do?tokenid=' + tokenid,
      data: queryData,
      dataType: 'JSON',
      success: function (result) {
        var errorCode = result.errorCode;
        if (errorCode === '0') {
          viewModel.attachOldCodeValue([]);
          var files = result.data.content;
          viewModel.attachFiles.setSimpleData(files);
          var rows = viewModel.attachFiles.getAllRows();
          for (var i = 0, l = rows.length; i < l; i++) {
            if (attachCode && attachCode === rows[i].getValue('attach_code')) {
              viewModel.attachFiles.setRowSelect(i);
              var sortListGroup = $("#attach-table");
              sortListGroup.find("tr").removeClass("attachActive");
              sortListGroup.find("tr").eq(i).addClass("attachActive");
            }
            viewModel.attachOldCodeValue.push(rows[i].getValue('attach_code'));
          }
          viewModel.attachFiles.totalPages(result.data.totalPages);
          viewModel.attachFiles.totalRow(result.data.size);
        } else {
          u.showMessage({
            msg: '查询附件出现异常，请稍后重试',
            position: "bottom",
            msgType: "error"
          });
        }
      },
      error: function () {
        u.showMessage({
          msg: '查询附件出现异常，请稍后重试',
          position: "bottom",
          msgType: "error"
        });
      }
    });
  };

  /**
   * 分页页码改变事件
   * @param index
   */
  viewModel.pageChangeFunc = function (index) {
    viewModel.attachFiles.pageIndex(index);
    viewModel.initAttachTable(viewModel.selectedSortNumberValue());
  };

  /**
   * 分页显示数量改变事件
   * @param size
   */
  viewModel.sizeChangeFunc = function (size) {
    viewModel.attachFiles.pageSize(size);
    viewModel.attachFiles.pageIndex("0");
    viewModel.initAttachTable(viewModel.selectedSortNumberValue());
  };

  /**
   * 新增附件分类
   */
  viewModel.addSort = function () {
    var sys_id = $('#sys-id').val();
    if (!sys_id) {
      ip.ipInfoJump("请选择一个子系统！", "info");
    } else {
      viewModel.sortCodeValue('');
      viewModel.sortNameValue('');
      viewModel.sortInfoValue('');
      document.getElementById("sort-code-error").innerHTML = "";
      document.getElementById("sort-name-error").innerHTML = "";
      $('#add-sort-box').modal({backdrop: 'static'});
    }
  };

  /**
   * 取消保存新增分类
   */
  viewModel.cancleAddSort = function () {
    $('#add-sort-box').modal('hide');
  };

  /**
   * 保存新增分类
   */
  viewModel.confirmAddSort = function () {
    var flag = true;
    var sys_id = $('#sys-id').val();
    var sortNameValue = $("#sort-name").val();
    var sortCodeValue = $("#sort-code").val();
    var sortInfoValue = $("#sort-info").val();

    if (isNull(sortNameValue)) {
      document.getElementById("sort-name-error").innerHTML = "分类名称不能为空";
      flag = false;
    }

    if (isNull(sortCodeValue)) {
      document.getElementById("sort-code-error").innerHTML = "分类代号不能为空";
      flag = false;
    }

//    if (!checkBlank(sortNameValue)) {
//      document.getElementById("sort-name-error").innerHTML = "输入的数据不能含有空格！";
//      flag = false;
//    }

    if (checkValid(sortCodeValue)) {
      document.getElementById("sort-code-error").innerHTML = "格式错误，只能由字母、数字、中横杠_、下横杠-、组成";
      flag = false;
    }

    var oldSortCodeArr = [];
    var oldSortCodeObjArr = viewModel.attachSort.getSimpleData({'fields': ['sortcode']});
    for (var i = 0, l = oldSortCodeObjArr.length; i < l; i++) {
      oldSortCodeArr.push(oldSortCodeObjArr[i].sortcode);
    }

    if (!checkValue(sortCodeValue, oldSortCodeArr)) {
      document.getElementById("sort-code-error").innerHTML = "该分类代号已经存在";
      flag = false;
    }

    if (flag) {
      var req = $.ajax({
        type: 'GET',
        url: '/df/attach/saveAttachCategory.do?tokenid=' + tokenid,
        cache: false,
        dataType: 'json',
        data: {
          'sys_id': sys_id,
          'sortNameValue': sortNameValue,
          'sortCodeValue': sortCodeValue,
          'sortInfoValue': sortInfoValue,
          "ajax": "noCache"
        }
      });
      req.done(function (result) {
        if (result.errorCode === '0') {
          var sys_id = $('#sys-id').val();
          viewModel.initSortList(sortCodeValue, sys_id);
          $('#add-sort-box').modal('hide');
          ip.ipInfoJump("保存成功！", "success");
        } else if (result.errorCode === '-1') {
          ip.ipInfoJump("保存失败，请稍后重试！", "error");
        }
      });
      req.fail(function () {
        ip.ipInfoJump("保存失败，请稍后重试！", "error");
      });
    }
  };

  /**
   * 删除附件分类按钮点击事件
   */
  viewModel.deleteSortBtn = function (index, sortnumber) {
    viewModel.attachSort.setRowSelect(index);
    var req = $.ajax({
      type: 'GET',
      url: '/df/attach/findAttachByCategoryId.do?tokenid=' + tokenid,
      cache: false,
      dataType: 'json',
      data: {
        "pageStart": '0',
        "pageSize": '10',
        'sortnumber': sortnumber,
        "ajax": "noCache"
      }
    });
    req.done(function (result) {
      if (result.errorCode === '0') {
        var files = result.data.content;
        if (files.length) {
          var delConfirmMsg1 = '该分类还有附件存在，是否继续删除？';
          viewModel.initAttachTable(sortnumber, index);
          ip.warnJumpMsg(delConfirmMsg1, 'delConfirmSureId', 'delConfirmCancelCla');
          $('#delConfirmSureId').on('click', function () {
            viewModel.deleteSort();
            $('#config-modal').remove();
          });
          $('.delConfirmCancelCla').on('click', function () {
            $('#config-modal').remove();
          });
        } else {
          var delConfirmMsg2 = '是否删除这个附件分类？';
          viewModel.initAttachTable(sortnumber, index);
          ip.warnJumpMsg(delConfirmMsg2, 'delConfirmSureId', 'delConfirmCancelCla');
          $('#delConfirmSureId').on('click', function () {
            viewModel.deleteSort();
            $('#config-modal').remove();
          });
          $('.delConfirmCancelCla').on('click', function () {
            $('#config-modal').remove();
          });
        }
      }
    });
  };

  /**
   * 删除附件分类
   */
  viewModel.deleteSort = function () {
    var data = viewModel.attachSort.getSimpleData({type: 'current', 'fields': ['sortnumber']})[0];
    $.ajax({
      type: 'GET',
      cache: false,
      url: '/df/attach/deleteAttachCategory.do?tokenid=' + tokenid,
      data: {'sortnumber': data.sortnumber},
      dataType: 'JSON',
      success: function (result) {
        var errorCode = result.errorCode;
        if (errorCode === '0') {
          var sys_id = $('#sys-id').val();
          //初始化附件分类列表
          viewModel.initSortList('', sys_id);
          //初始化附件表格
          viewModel.attachFiles.setSimpleData([]);
          ip.ipInfoJump("删除成功！", "success");
          // u.showMessage('删除成功');
        } else {
          u.showMessage({
            msg: '删除附件分类出现异常，请稍后重试！',
            position: "bottom",
            msgType: "error"
          });
        }
      },
      error: function () {
        u.showMessage({
          msg: '删除附件分类出现异常，请稍后重试',
          position: "bottom",
          msgType: "error"
        });
      }
    });
  };

  /**
   * 新增附件
   */
  viewModel.addAttach = function () {
    var sys_id = $('#sys-id').val();
    if (!sys_id) {
      ip.ipInfoJump("请选择一个子系统！", "info");
    } else {
      $('#add-attach-box').modal({backdrop: 'static'});
      $("#attach-add-confirm").click(function () {
          viewModel.confirmAddAttach();
      });
      document.getElementById("attach-sort-error").innerHTML = "";
      document.getElementById("attach-new-code-error").innerHTML = "";
      document.getElementById("fileupload-error").innerHTML = "";
      document.getElementById("sort-code-error").innerHTML = "";
      $('#progress .progress-bar').css('width', 0).html('');
    }
  };

  /**
   * 取消保存新增附件
   */
  viewModel.cancleAddAttach = function () {
    $('#add-attach-box').modal('hide');
    viewModel.attachNewCodeValue('');
    viewModel.selectedSort('');
    viewModel.pathNameValue('');
    viewModel.attachInfoValue('');
    viewModel.fileSize(0);
    viewModel.fileDataArray([]);
    $("#attach-add-confirm").off('click');
//    viewModel.selectedSortNumberValue('');
    viewModel.uploadedFiles.setSimpleData([]);
    viewModel.fileUploadData('');
  };

  /**
   * 保存新增附件
   */
  viewModel.confirmAddAttach = function () {
    var flag = true;
    var sys_id = $('#sys-id').val();
    var attachNewCodeValue = $("#attach-new-code").val();
    var selectedSort = $("#attach-sort").val();
    var pathNameValue = $("#path-name").val();
    var attachInfoValue = $("#attach-info").val();

    //数据校验
    if (isNull(selectedSort)) {
      document.getElementById("attach-sort-error").innerHTML = "请选择附件分类";
      flag = false;
    } else {
      var rows = viewModel.attachSort.getAllRows();
      for (var i = 0, l = rows.length; i < l; i++) {
        if (selectedSort === rows[i].getValue('sortnumber')) {
          viewModel.selectedSortNumberValue(rows[i].getValue('sortnumber'));
          break;
        }
      }
    }

    if (isNull(attachNewCodeValue)) {
      document.getElementById("attach-new-code-error").innerHTML = "附件编号不能为空！";
      flag = false;
    } else {
      document.getElementById("attach-new-code-error").innerHTML = "";
    }

    if (checkValid(attachNewCodeValue)) {
      document.getElementById("attach-new-code-error").innerHTML = "格式错误，只能由字母、数字、中横杠_、下横杠-、组成";
      flag = false;
    }

    var oldAttachCodeArr = [];
    var oldAttachCodeObjArr = viewModel.attachFiles.getSimpleData({'fields': ['attach_code']});
    for (var m = 0, n = oldAttachCodeObjArr.length; m < n; m++) {
      oldAttachCodeArr.push(oldAttachCodeObjArr[m].attach_code);
    }

    if (!checkValue(attachNewCodeValue, oldAttachCodeArr)) {
      document.getElementById("attach-new-code-error").innerHTML = "该附件编号已经存在";
      flag = false;
    }
    if (!viewModel.uploadedFiles.getSimpleData() || viewModel.uploadedFiles.getSimpleData().length === 0) {
      document.getElementById("fileupload-error").innerHTML = "请选择要上传的文件";
      flag = false;
    } else {
      document.getElementById("fileupload-error").innerHTML = "";
    }
    
    if (flag && viewModel.uploadedFiles.getSimpleData() && viewModel.uploadedFiles.getSimpleData().length !== 0) {
    	$.ajax({
    	      type: 'GET',
    	      url: '/df/attach/checkUploadCondition.do?tokenid=' + tokenid,
    	      data: {'attachSize': viewModel.fileSize()},
    	      dataType: 'JSON',
    	      success: function (result) {
    	        var errorCode = result.errorCode;
    	        if (errorCode === '0') {
    	            //上传文件
    	            //附加参数
    	        	  var data = viewModel.fileUploadData();
    	        		data.files = viewModel.fileDataArray();
    	        		var formData = {
    	      	              'sys_id': sys_id,
    	      	              'attachNewCodeValue': attachNewCodeValue,
    	      	              'selectedSort': selectedSort,
    	      	              'pathNameValue': pathNameValue,
    	      	              'attachInfoValue': attachInfoValue
    	      	            };
    	      	            data.formData = formData;
    	      	            data.submit();
    	        } else {
    	          u.showMessage({
    	            msg: result.errorMsg,
    	            position: "bottom",
    	            msgType: "error"
    	          });
    	        }
    	      },
    	      error: function () {
    	        u.showMessage({
    	          msg: result.errorMsg,
    	          position: "bottom",
    	          msgType: "error"
    	        });
    	      }
    	    });
    }
  };

  /**
   * 删除附件按钮点击事件
   * @param index
   */
  viewModel.deleteFileBtn = function (index) {
    viewModel.attachFiles.setRowSelect(index);
    var delConfirmMsg = '确定要删除这个附件吗？';
    ip.warnJumpMsg(delConfirmMsg, 'delConfirmSureId', 'delConfirmCancelCla');
    $('#delConfirmSureId').on('click', function () {
      viewModel.deleteFile();
      $('#config-modal').remove();
    });
    $('.delConfirmCancelCla').on('click', function () {
      $('#config-modal').remove();
    });
  };

  /**
   * 删除附件
   */
  viewModel.deleteFile = function () {
    var data = viewModel.attachFiles.getSimpleData({type: 'current', 'fields': ['attach_id']})[0];
    var index = viewModel.attachFiles.getSelectedIndex();

    var attachIdArr = [];
    attachIdArr.push(data.attach_id);
    $.ajax({
      type: 'POST',
      url: '/df/attach/deleteAttach.do?tokenid=' + tokenid,
      data: {'attach_id': attachIdArr},
      dataType: 'JSON',
      success: function (result) {
        var errorCode = result.errorCode;
        if (errorCode === '0') {
          viewModel.initAttachTable(viewModel.selectedSortNumberValue());
          ip.ipInfoJump("删除成功！", "success");
        } else {
          u.showMessage({
            msg: "删除文件出现异常，请稍后重试！",
            position: "bottom",
            msgType: "error"
          });
        }
      },
      error: function () {
        u.showMessage({
          msg: '删除文件出现异常，请稍后重试！',
          position: "bottom",
          msgType: "error"
        });
      }
    });
  };

  /**
   * 上传附件
   */
  function fileupload() {
    var options = {
      url: '/df/attach/uploadattach.do?tokenid=' + tokenid,
      dataType: 'json',
      autoUpload: false,
      maxFileSize: 50 * 1024 * 1024,
      messages: {
        maxFileSize: 'File exceeds maximum allowed size of 99MB',
        acceptFileTypes: 'File type not allowed'
      },
      dropZone: $('#dropzone'),
      disableImageResize: /Android(?!.*Chrome)|Opera/
        .test(window.navigator.userAgent),
      previewMaxWidth: 100,
      previewMaxHeight: 100,
      previewCrop: true,
      singleFileUploads: false
    };
    $('#fileupload').fileupload(options)
      .bind('fileuploadadd', fileuploadadd)
      .bind('fileuploaddone', fileuploaddone)
      .bind('fileuploadprogressall', fileuploadprogressall)
      .bind('fileuploadfail', fileuploadfail)
      .prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');
  }

  /**
   * 添加文件
   * @param e
   * @param data
   */
  function fileuploadadd(e, data) {
    var files = data.files.slice();
    var size = parseInt(viewModel.fileSize());
    var totalSize = 0;
    for(var i = 0; i < files.length; i++) {
    	totalSize += files[i].size;
    }
    viewModel.fileSize(size + totalSize);
    viewModel.uploadedFiles.addSimpleData(files);
    viewModel.fileUploadData(data);
    viewModel.fileDataArray(viewModel.fileDataArray().concat(data.files));
    document.getElementById("fileupload-error").innerHTML = "";
//    $("#attach-add-confirm").click(function () {
//      viewModel.confirmAddAttach(data);
//    });
  }

  /**
   * 上传到服务器成功
   * @param e
   * @param data
   */
  function fileuploaddone(e, data) {
    var selectedSort = $("#attach-sort").val();
    viewModel.attachFiles.pageIndex("0");
    viewModel.attachFiles.pageSize("10");
    viewModel.initAttachTable(selectedSort);
    $('#add-attach-box').modal('hide');
    // ip.ipInfoJump("保存成功！", "success");
    var rows = viewModel.attachSort.getAllRows();
    for (i = 0, l = rows.length; i < l; i++) {
      if (rows[i].getValue('sortnumber') === selectedSort) {
        viewModel.attachSort.setRowSelect(i);
        var sortListGroup = $(".list-group");
        sortListGroup.find(".list-group-item").removeClass("sortActive");
        sortListGroup.find(".list-group-item").eq(i).addClass("sortActive");
        break;
      }
    }
    viewModel.attachNewCodeValue('');
    viewModel.selectedSort('');
    viewModel.pathNameValue('');
    viewModel.attachInfoValue('');
    viewModel.fileSize(0);
    viewModel.fileDataArray([]);
    viewModel.uploadedFiles.setSimpleData([]);
    viewModel.fileUploadData('');
//    viewModel.selectedSortNumberValue('');
    $("#attach-add-confirm").off('click');
     u.messageDialog({msg: "保存成功!", title: "附件保存成功", btnText: "确定"});
  }


  /**
   * 进度条
   * @param e
   * @param data
   */
  function fileuploadprogressall(e, data) {
    var progress = parseInt(data.loaded / data.total * 100, 10);
    $('.progress-bar').css('width', progress + '%').html(progress + '%');
  }

  /**
   * 上传失败
   * @param e
   * @param data
   */
  function fileuploadfail(e, data) {
    ip.ipInfoJump("保存失败，请稍后重试！", "error");
    viewModel.attachNewCodeValue('');
    viewModel.selectedSort('');
    viewModel.pathNameValue('');
    viewModel.attachInfoValue('');
    viewModel.fileSize(0);
    viewModel.fileDataArray([]);
//    viewModel.selectedSortNumberValue('');
    viewModel.uploadedFiles.setSimpleData([]);
    viewModel.fileUploadData('');
    $("#attach-add-confirm").off('click');
    $('#progress .progress-bar').css('width', 0).html('');
  }

  //清除进度条
  $('#fileupload').click(function () {
    $('#progress .progress-bar').css('width', 0).html('');
  });

  /**
   * 下载附件
   * @param attach_id
   */
  viewModel.downloadFile = function (attach_id) {
	    $.ajax({
	        type: 'GET',
	        url: '/df/attach/checkAttachPath.do?tokenid=' + tokenid,
	        data: {'attach_id': attach_id},
	        dataType: 'JSON',
	        success: function (result) {
	          var errorCode = result.errorCode;
	          if (errorCode === '-1') {
	        	  u.showMessage({
		              msg: result.errorMsg,
		              position: "bottom",
		              msgType: "error"
		            });
	          } else {
	        	    var form = $("<form id='downloadForm'>");
	        	    form.attr('style', 'display:none');
	        	    form.attr('target', '');
	        	    form.attr('method', 'post');
	        	    var url = '/df/attach/download.do?tokenid=' + tokenid;
	        	    form.attr('action', url);
	        	    var input = $('<input>');
	        	    input.attr('type', 'hidden');
	        	    input.attr('name', 'attach_id');
	        	    input.attr('value', attach_id);
	        	    $('body').append(form);
	        	    form.append(input);
	        	    form.submit();
	        	    form.remove();
	        	    $('#downloadForm').remove();
	          }
	        },
	        error: function (result) {
	          u.showMessage({
	            msg: result.errorMsg,
	            position: "bottom",
	            msgType: "error"
	          });
	        }
	      });
  };

  /**
   * 编辑附件信息
   * @param index
   * @param row
   */
  viewModel.editFile = function (index, row) {
    viewModel.selectedAttachFileId('');
    viewModel.selectedAttachFileId(row.getValue('attach_id'));
    viewModel.attachCodeValue(row.getValue('attach_code'));
    viewModel.attachNameValue(row.getValue('attach_name'));
    viewModel.attachInformationValue(row.getValue('remark'));
    document.getElementById("attach-code-error").innerHTML = "";
    document.getElementById("attach-name-error").innerHTML = "";
    $('#edit-attach-box').modal({backdrop: 'static'});
  };

  /**
   * 关闭编辑附件界面
   */
  viewModel.cancleEditAttach = function () {
    $('#edit-attach-box').modal('hide');
    viewModel.attachCodeValue('');
    viewModel.attachNameValue('');
    viewModel.attachInfoValue('');
  };

  /**
   * 保存编辑附件信息
   */
  viewModel.confirmEditAttach = function () {
    var flag = true;
    var attachCodeValue = $("#attach-code").val();
    var attachNameValue = $("#attach-name").val();
    var attachInfoValue = $("#attach-information").val();

    if (isNull(attachCodeValue)) {
      document.getElementById("attach-code-error").innerHTML = "附件编号不能为空";
      flag = false;
    }
    if (isNull(attachNameValue)) {
      document.getElementById("attach-name-error").innerHTML = "附件名称不能为空";
      flag = false;
    }

    if (!checkBlank(attachNameValue)) {
      document.getElementById("attach-name-error").innerHTML = "输入的数据不能含有空格！";
      flag = false;
    }

    if (checkValid(attachCodeValue)) {
      document.getElementById("attach-code-error").innerHTML = "格式错误，只能由字母、数字、中横杠_、下横杠-、组成";
      flag = false;
    }

    var oldAttachCodeArr = [];
    var oldAttachCodeObjArr = viewModel.attachFiles.getSimpleData({'fields': ['attach_code']});
    for (var m = 0, n = oldAttachCodeObjArr.length; m < n; m++) {
      oldAttachCodeArr.push(oldAttachCodeObjArr[m].attach_code);
    }

    if (viewModel.attachCodeValue() !== attachCodeValue && !checkValue(attachCodeValue, oldAttachCodeArr)) {
      document.getElementById("attach-code-error").innerHTML = "该附件编号已经存在";
      flag = false;
    }

    if (flag) {
      // 刷新列表页面数据 选中新增的节点
      var req = $.ajax({
        type: 'GET',
        url: '/df/attach/updateAttachInfo.do?tokenid=' + tokenid,
        cache: false,
        dataType: 'json',
        data: {
          'attach_id': viewModel.selectedAttachFileId(),
          'attachCodeValue': attachCodeValue,
          'attachNameValue': attachNameValue,
          'attachInfoValue': attachInfoValue,
          "ajax": "noCache"
        }
      });
      req.done(function (result) {
        if (result.errorCode === '0') {
          viewModel.attachFiles.pageIndex("0");
          viewModel.attachFiles.pageSize("10");
          viewModel.initAttachTable(viewModel.selectedSortNumberValue());
          ip.ipInfoJump("保存成功！", "success");
          viewModel.attachCodeValue('');
          viewModel.attachNameValue('');
          viewModel.attachInfoValue('');
          $('#edit-attach-box').modal('hide');
        } else {
          ip.ipInfoJump("保存失败，请稍后重试！", "error");
        }
      });
      req.fail(function () {
        ip.ipInfoJump("保存失败，请稍后重试！", "error");
      });
    }
  };

  $('input[id="sort-name"]').bind('input propertychange', function () {
    document.getElementById("sort-name-error").innerHTML = "";
    if (!checkBlank($(this).val())) {
      document.getElementById("sort-name-error").innerHTML = "输入的数据不能含有空格";
    }
  });

  $('input[id="sort-code"]').bind('input propertychange', function () {
    document.getElementById("sort-code-error").innerHTML = "";
    if (checkValid($(this).val())) {
      document.getElementById("sort-code-error").innerHTML = "格式错误，只能由字母、数字、中横杠_、下横杠-、组成";
    }
  });

  //校验附件编号输入
  $('input[id="attach-new-code"]').bind('input propertychange', function () {
    document.getElementById("attach-new-code-error").innerHTML = "";
    if (checkValid($(this).val())) {
      document.getElementById("attach-new-code-error").innerHTML = "格式错误，只能由字母、数字、中横杠_、下横杠-、组成";
    }
  });

  $('input[id="attach-code"]').bind('input propertychange', function () {
    document.getElementById("attach-code-error").innerHTML = "";
    if (checkValid($(this).val())) {
      document.getElementById("attach-code-error").innerHTML = "格式错误，只能由字母、数字、中横杠_、下横杠-、组成";
    }
  });

  $('input[id="attach-sort"]').bind('input propertychange change', function () {
    document.getElementById("attach-sort-error").innerHTML = "";
  });

  /**
   * 校验编码唯一性
   * @param value 要校验的值
   * @param arr 要对比的数组
   * @returns {boolean} 返回true表示唯一
   */
  var checkValue = function (value, arr) {
    var flag = true;
    for (var i = 0, l = arr.length; i < l; i++) {
      if (value === arr[i]) {
        flag = false;
        // ip.ipInfoJump("该编码已经存在！", "error");
        break;
      }
    }
    return flag;
  };

// 输入的数据中间不能含有空格
  var checkBlank = function (value) {
    var flag = true;
    var afterValue = value.split(" ").join("");
    if (value.length !== afterValue.length) {
//      ip.ipInfoJump("输入的数据中间不能含有空格！", "error");
//      return flase;
      flag = false;
    }
    return flag;
  };

  // 判断是否为空串 未定义 null
  var isNull = function (obj) {
    return obj === '' || obj === null || obj === undefined;
  };

  //验证编码的非法字符   编码只能是字母、数字、中横杠_、下横杠-
  var checkValid = function (code) {
    var codeStr = '0123456789-_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    // var flag=true;
    for (var i = 0, l = code.length; i < l; i++) {
      var str = code.charAt(i);
      if (codeStr.indexOf(str) === -1) {
        // flag=false;
        //格式错误
        return true;
      }
    }
    return false;
  };

  //只能是数字字母和中文，不能包括特殊字符)
  function checkusername(value) {
    var regex = /^[0-9a-zA-Z\u4e00-\u9fa5]*$/;
    if (regex.test(value) === true) {
      //格式正确
      document.getElementById("error").innerHTML = "";
    } else {
      //格式错误
      document.getElementById("error").innerHTML = "格式错误，只能由数字、字母、中文组成，不能包含特殊字符";
    }
  }

  //初始化子系统下拉列表
  viewModel.initSys = function () {
    var req = $.ajax({
      type: 'GET',
      url: '/df/attach/getSysApp.do?tokenid=' + tokenid,
      cache: false,
      dataType: 'json',
      data: {"ajax": "noCache"}
    });
    req.done(function (result) {
      if (result.data) {
        viewModel.sysIdOptions([]);
        var System = function (sys_id, sys_name) {
          this.sys_id = sys_id;
          this.sys_name = sys_name;
        };
        for (var i = 0, l = result.data.length; i < l; i++) {
          viewModel.sysIdOptions.push(new System(result.data[i].sys_id, result.data[i].sys_name));
        }
      }
    }).fail(function () {
      ip.ipInfoJump("初始化子系统下拉列表失败，请稍后重试！", "error");
    });
  };

  function init() {
    'use strict';
    app = u.createApp({
      el: 'body',
      model: viewModel
    });
    tokenid = ip.getTokenId();
    //下载插件初始化
    fileupload();
    //初始化子系统下拉列表
    viewModel.initSys();

    //根据子系统的值确定附件分类列表
    $('#sys-id').on('change', function () {
      var sys_id = $('#sys-id').val();
      //初始化附件分类列表
      viewModel.initSortList('', sys_id);
      //初始化附件表格
      viewModel.attachFiles.setSimpleData([]);
    });
  }

  init();
});