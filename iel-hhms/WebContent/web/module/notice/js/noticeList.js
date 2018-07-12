
var rows=30;
// DOM加载完后执行
$(function() {
	setGridHeight1();//设置列表高度自适应
	$('#table').grid({
		  colName: ['公告名称', '到期时间', '内容','发布时间'],
		  colModel: [
		    {name: 'name', index:'name',sortable:true, style: {'textAlign':'center'},
		      formart : function(dataRow, data, fn){
			  	return '<a href="javascript:void(0); " onclick="modifyDialog('+data.id+')" style="text-decoration:underline;">'+dataRow+"</a>";
		  	  }
		    },
		    {name: 'expireTime', index:'expireTime',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'remark', index:'remark', style: {'textAlign': 'center'}},
		    {name: 'updateTime', index:'updateTime', style: {'textAlign': 'center'}}
		  ],
		  headerFixed : true,
		  rowNum: rows,
		  rowMark : true,
		  height : '100%',
		  sortname: 'updateTime',
		  sortorder: 'desc',
		  ajaxOptions : {
		     type : 'post'
		  } ,
		  dataType: 'ajax',
		  url : 'listAjax.action',
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  ajaxDynamic : function(){ 
	  		  return $('#filterForm').formToArray();
	  	  },
		  ajaxResponseReader: {page:'pageNo', rows:'result', records: 'total', totalPage:'totalPage'}
		});
	
	//查询
	$("#data_filtering").click(function(){
		$('#table').grid('reload');
	});
	
	//添加
	$("#add_btn").click(function(){
		$.dialog({
            width: 800,
            height: 600,
            draggable: true,
            maybeRefresh: true,
            id: 'addframe',
            title: '添加公告信息',
            iframeSrc: top.pt.ctx+'/web/module/notice/showDialog.action',
            buttons: [{
                text: '确定',
                'class': 'bPrimary',
                "id": 'add_notice_btn_ok',
                click: function(dialog, callback){
                	$.ajax({
    					url : 'saveForm.action',
    					data : top.frames["addframe"].$("#showForm").serialize(),
    					beforeSend : function(){
							return top.frames["addframe"].$("#showForm").validate();
						},
    					success : function(data, callback){
    						if (data.success == true) {
								dialog.close();
								jAlert("添加成功", '提示', 'attention');
								$('#table').grid('reload');
							}else {
								jAlert("添加失败："+data.msg, '错误', 'error');
							}
    					},
    					dataType : "json"
    				});
                }
            }, {
                text: '取消',
                click: function(dialog, callback, el){
                	dialog.close();
                }
            }]
        });

	});
	
	//删除公告
	$("#del_btn").click(function(){
		var selected = [];
		var selTabIds =  $('#table').grid('getSelRow');
		var datas = $('#table').grid('getDataByID', selTabIds);
		for(var i=0; i<datas.length; i++){
    		selected.push(datas[i].id);
		}
		if (datas.length == 0) {
            jAlert('当前没有选中的项！', '提示', 'attention');
            return;
        };
        jConfirm('确定要删除当前记录吗？','提示',function(r){
            if(r){
           	  $.ajax({
                 	url : 'delData.action',
                 	type : "POST",
                 	dataType : "json",
                 	data : {
                 		ids : selected.join(",")
                 	},
                 	beforeSend : function(){
						loader.show('正在处理，请稍后...');
					},
                 	success : function(data, callback){
						if (data.success == true) {
							jAlert("删除成功！", '提示', 'attention');
        					$('#table').grid('reload');
						}else {
							jAlert("删除失败："+data.message, '错误', 'error');
						}
					},
            		complete : function() {
						loader.hide();
					}
                 })
            }
        });
	});
		      
});  

//修改公告信息
function modifyDialog(dataId){
	$.dialog({
		width : 800,
		height : 600,
		maybeRefresh : true,
		id : 'editframe',
		title : '修改公告信息',
		iframeSrc : top.pt.ctx+'/web/module/notice/showDialog.action?entity.id='+dataId,
		buttons : [{
			text : '确定',
			'class' : 'bPrimary',
			"id" : 'modify_notice_btn_ok',
			click : function(dialog, callback) {
				$.ajax({
					url : 'saveForm.action',
					data : top.frames["editframe"].$("#showForm").serialize(),
					beforeSend : function(){
						return top.frames["editframe"].$("#showForm").validate();
					},
					success : function(data, callback){
						if (data.success == true) {
							dialog.close();
							jAlert("修改成功", '提示', 'attention');
							$('#table').grid('reload');
						}else {
							jAlert("修改失败："+data.msg, '错误', 'error');
						}
					},
					dataType : "json"
				});
			}
		}, {
			text : '取消',
			click : function(dialog, callback, el) {
				dialog.close();
			}
		}]
	});
}
	




