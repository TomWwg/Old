var tree;
var node;
var rows=30;
// DOM加载完后执行
$(function() {
	setGridHeight1();//设置列表高度自适应
	$('#table').grid({
		  colName: ['员工姓名', '员工编号', '人员类别','科室','性别',/*'电话','职务',*/'职称','更新时间'/*,'胸牌'*/],
		  colModel: [
		    {name: 'name', index:'name', style: {'textAlign':'center'},
		      formart : function(dataRow, data, fn){
			  	return '<a href="javascript:void(0); " onclick="modifyDialog('+data.id+')" style="text-decoration:underline;">'+dataRow+"</a>";
		  	  }
		    },
		    {name: 'no', index:'no', style: {'textAlign': 'center'},
		    	formart : function(dataRow, data, fn){
		    		
		    		if(data.no == ""||data.no == null){
		    			return "NA";
		    		}
		    		return data.no;
		  	  }
		    },
		    {name: 'categoryName', index:'categoryName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'departName', index:'departName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'sex', index:'sex',sortable:false, style: {'textAlign': 'center'}},
		    /*{name: 'tel', index:'tel', style: {'textAlign': 'center'}},
		    {name: 'duty', index:'duty', style: {'textAlign': 'center'}},*/
		    {name: 'jobTitleName', index:'jobTitleName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'updateTime', index:'updateTime', style: {'textAlign': 'center'}},
//		    {name: 'rfid', index:'rfid', sortable:false, style:{'textAlign': 'center'},
//              	formart : function(dataRow, data, fn){
//              		if(dataRow == null||dataRow==''){ 
//              			return '<a href="javascript:void(0); " onclick="rfidDialog('+data.id+','+data.groupTree.id+')" style="text-decoration:underline;">'+'关联胸牌'+"</a>";
//              		} else {
//              			return dataRow;
//              		}
//  		  		}
//		    }
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
	//左侧树列表
	var setting = {
			async: {
				enable: true,
				url: top.pt.ctx+"/web/module/getDepartmentTree.action?isTreeOpen=false",
				autoParam: ["id", "label"],
				dataType :"json"
			},
			callback : {
				//树加载成功的之后
				onAsyncSuccess: function(event, treeId, treeNode, msg){
//					if(node != null){
//						tree.selectNode(node);
//					}
					var zTree=$.fn.zTree.getZTreeObj(treeId);
					var node;
					if(msg.parentId==1){//parentId暂时用来存放部门ID,判断默认节点
						node=zTree.getNodeByParam("id", msg.id);
					}else{
						node=zTree.getNodeByParam("id", msg.children[0].id);
					}
					zTree.selectNode(node);
				},
				onClick: function(event, treeId, treeNode){
					$("#treeId").val(treeNode.id);//将treeId放入过滤的form表单，用户过滤该组织下的设备
					$('#table').grid('reload');
				}
			},
			view : {
				fontCss : function(treeId, treeNode) {
					return (!!treeNode.highlight) ? {
						color : "#A60000",
						"font-weight" : "bold"
					} : {
						color : "#25c4eb",
						"font-weight" : "normal",
						"font-size" : "16px"
					};
				}
			}
	};
	tree = $("#zTreea").tree(setting);
	
	
	//查询
	$("#data_filtering").click(function(){
		$('#table').grid('reload');
	});
	
	//添加
	$("#add_btn").click(function(){
		var selectedNode = tree.getSelectedNodes()[0];
		if(selectedNode==null){
			jAlert('请选择所属科室!', '提示', 'attention');
			return;
		}
		if(selectedNode.level == 0){
			jAlert('医院信息节点下不能添加人员信息', '提示', 'attention');
			return;
		}
		$.dialog({
            width: 800,
            height: 600,
            draggable: true,
            maybeRefresh: true,
            id: 'addframe',
            title: '添加人员信息',
            iframeSrc: top.pt.ctx+'/web/module/staff/showDialog.action?groupTreeId='+selectedNode.id,
            buttons: [{
                text: '确定',
                'class': 'bPrimary',
                "id": 'add_staff_btn_ok',
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
	
	//删除人员
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

//添加胸牌
function rfidDialog(dataId,departId){
	$.dialog({
		width : 800,
		height : 600,
		draggable: true,
		maybeRefresh : true,
		id : 'rfidDialog',
		title : "添加胸牌信息",
		iframeSrc : top.pt.ctx+'/web/module/device/rfidDialog.action?entity.staffId='+dataId+'&entity.groupTree.id='+departId,
		buttons : [{
			text : '确定',
			'class' : 'bPrimary',
			"id" : 'add_rfid_btn_ok',
			click : function(dialog, callback) {
				$.ajax({
					url : '../device/addRfid.action',
					data : top.frames["rfidDialog"].$("#showForm").serialize(),
					beforeSend : function(){
						return top.frames["rfidDialog"].$("#showForm").validate();
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
};

//修改人员信息
function modifyDialog(dataId){
	$.dialog({
		width : 800,
		height : 600,
		maybeRefresh : true,
		id : 'editframe',
		title : '修改人员信息',
		iframeSrc : top.pt.ctx+'/web/module/staff/showDialog.action?entity.id='+dataId,
		buttons : [{
			text : '确定',
			'class' : 'bPrimary',
			"id" : 'modify_staff_btn_ok',
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
	




