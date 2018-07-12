var tree;
var node;
var rows=30;
// DOM加载完后执行
$(function() {
	$("#userRole").multiselect({
		selectedList: 1
	});
	$("#userRole").val($("#userRoleInput").val().split(','));
	$("#userRole").multiselect("refresh");
	setGridHeight1();//设置列表高度自适应
	$('#table').grid({
		  colName: ['用户名',  '状态', '科室', '角色', '登陆次数'/*, '到期时间'*/,'修改时间', '上次登录时间', '备注'],
		  colModel: [
		    {name: 'name', index:'name',sortable:true, style: {'textAlign':'center'},
		      formart : function(dataRow, data, fn){
			  	return '<a href="javascript:void(0); " onclick="modifyDialog('+data.id+')" style="text-decoration:underline;">'+dataRow+"</a>";
		  	  }
		    },
		    {name: 'userStatus', index:'userStatus', sortable:false, style:{'textAlign': 'center'},
              	formart : function(dataRow, data, fn){
              		if(dataRow == 0){ //值为0时，状态正常
              			return '<a onclick="changeStatus('+data.id+','+dataRow+')" class="online" style=" text-decoration:underline;" title="改变用户状态"><i></i>正常 </a>';
              		} if(dataRow == 2){ //过期
              			return'<font color="red">过期</font>';
              		}else {
              			return '<a onclick="changeStatus('+data.id+','+dataRow+')" class="offline pointRed" style="text-decoration:underline;" title="改变用户状态"> <i></i>停用  </a>';
              		}
  		  		}
		    },
		    {name: 'strRev1', index:'strRev1',sortable:false,style: {'textAlign': 'center'},},
		    {name: 'strRev2', index:'strRev2',sortable:false,style: {'textAlign': 'center'},},
		    {name: 'loginCounts', index:'loginCounts',style: {'textAlign': 'center'},},
		    //{name: 'expireStr', index:'expireStr',sortable:false, style: {'textAlign': 'center'},},
		    {name: 'updateTime', index:'updateTime',style: {'textAlign': 'center'}},
		    {name: 'lastLoginTime', index:'lastLoginTime',style: {'textAlign': 'center'},},
		    {name: 'remark', index:'remark', sortable:false, style: {'textAlign':'center', 'white-space':'pre-wrap'},}
		  ] ,
		  headerFixed : true,
		  rowMark : true,
		  height : '100%',
		  rowNum: rows,
		  sortname: 'updateTime',
		  sortorder: 'desc',
		  ajaxOptions : {
		     type : 'post'
		  },
		  dataType: 'ajax',
		  url : 'pageUserAjax.action',
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  ajaxDynamic : function(){ 
	  		  return $('#filterForm').formToArray();
	  	  },
		  ajaxResponseReader: {page:'pageNo', rows:'result', records:'total', totalPage:'totalPage'},
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
					if(node != null){
						tree.selectNode(node);
					}
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
					node = treeNode;
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
	
	//添加用户信息
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
            title: '添加用户信息',
            iframeSrc: top.pt.ctx+'/web/module/user/showDialog.action?groupTreeId='+selectedNode.id+'&typeLevel='+selectedNode.level,
            buttons: [{
                text: '确定',
                'class': 'bPrimary',
                "id": 'add_user_btn_ok',
                click: function(dialog, callback){
                	$.ajax({
    					url : 'addUserSaveForm.action',
    					data : top.frames["addframe"].$("#showForm").serialize(),
    					beforeSend : function(){
							return top.frames["addframe"].$("#showForm").validate();
						},
    					success : function(data, callback){
    						if (data.success == true) {
								dialog.close();
								jAlert("添加成功,初始密码为:88888888", '提示', 'attention');
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
	
	//删除用户
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
	
	//批量停用户用状态
	$("#disabled-btn").click(
    	function(){
    		var selected = [];
    		var selTabIds =  $('#table').grid('getSelRow');
    		var datas = $('#table').grid('getDataByID', selTabIds);
			for(var i=0; i<datas.length; i++){
				if(datas[i].id == 1 || datas[i].userName == "admin" ){
					$(datas[i]).attr("checked", false);
					continue;
				}
        		selected.push(datas[i].id);
			}
    		if (datas.length == 0) {
                jAlert('当前没有选中的项！', '提示', 'attention');
                return;
            };
            $.ajax({
            	url : 'disabledUser.action',
            	type : "POST",
            	dataType : "json",
            	beforeSend : function(){
            		loader.show('正在处理，请稍后...');
				},
            	data : {
            		ids : selected.join(",")
            	},
            	success : function(data){
            		if (data.success == true) {
						$('#table').grid('reload');
					}else {
						jAlert(data.message, '错误', 'error');
					}
            	},
            	complete : function() {
					loader.hide();
				}
            })
	}); 
	//批量启用用户状态
	$("#enabled-btn").click(
        	function(){
        		//debugger;
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
                $.ajax({
                	url : 'enabledUser.action',
                	type : "POST",
                	dataType : "json",
                	data : {
                		ids : selected.join(",")
                	},
                	beforeSend : function(){
                		loader.show('正在处理，请稍后...');
    				},
                	success : function(data){
                		if (data.success == true) {
							$('#table').grid('reload');
						}else {
							jAlert(data.message, '错误', 'error');
						}
                	},
                	complete : function() {
						loader.hide();
					}
                })
    	}); 
		      
});  

//修改用户信息
function modifyDialog(dataId){
	$.dialog({
		width : 800,
		height : 600,
		draggable: true,
		maybeRefresh : true,
		id : 'editframe',
		title : '修改人员信息',
		iframeSrc : top.pt.ctx+'/web/module/user/showDialog.action?entity.id='+dataId,
		buttons : [{
			text : '确定',
			'class' : 'bPrimary',
			"id" : 'modify_user_btn_ok',
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
};
	
//改变用户状态
function changeStatus(userId, status){
	if(userId==1){
		jAlert('不能改变管理员状态！', '提示', 'attention');
		return;
	}
	jConfirm('确定要改变用户状态吗？', '提示', function(r){
		if(r){
		 	$.ajax({
        		url : 'changeStatus.action',
        		type : "post",
        		dataType : "json",
        		data : {
        			userId : userId,
        			status : status
        		},
				success : function(data){
        			handleResponse(data, function(){
        				if (data.success == true) {
        					$('#table').grid('reload')
        				} else {
        					jAlert("用户状态更改失败！", '错误', 'error');
        				}
        			});
    			}
    		})
		}
	})
};

//手动校时
$("#sysc-btn").click(function(){
	$.ajax({
			url : 'syncTime.action?startTime='+new Date().format("yyyy-MM-dd hh:mm:ss"),
			beforeSend : function(){
				loader.show('正在对服务器进行校时，请稍后...');
			},
			success : function(data, callback){
				if (data.success == true) {
					jAlert("服务器校时成功", '提示', 'attention');
				} else {
					jAlert("服务器校时失败："+data.message, '提示', 'attention');
				}
			},
			complete : function() {
				loader.hide();
			},
			dataType : "json"
		}); 
});

//导出
function excelData() {
	var url = top.pt.ctx+'/web/module/exportFun/ExportForUserList.action';
	 $('#filterForm').attr("action", url).submit();
}

function userRoleSelect(){
	$("#userRole").multiselect("update");
	var userRoles=$("#userRole").multiselect("MyValues");
	$("#userRoleInput").attr("value",userRoles);
}



