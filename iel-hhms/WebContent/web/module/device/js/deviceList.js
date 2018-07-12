var tree;
var node;
var rows=30;
const LEVEL_ALL = "全院级别";
const LEVEL_CURRENT = "当前科室";
const LEVEL_ELSE = "非科室节点与所属科室一致";
var nodeName = "医院";
var nodeParent = "无";
var nodeRemark = "";
var nodeLevel = LEVEL_ALL;

	
// DOM加载完后执行
$(function() {
	setNodeInfo(nodeName,nodeParent,nodeLevel,nodeRemark);
	setGridHeight1();//设置列表高度自适应
	$('#table').grid({
		  colName: ['设备类型','设备编号', '所属','修改时间'],
		  colModel: [
		    {name: 'typeName', index:'typeName',sortable:false, style: {'textAlign': 'center'},
		    	formart : function(dataRow, data, fn){
		    		return '<a href="javascript:void(0); " onclick="modifyDialog('+data.id+')" style="text-decoration:underline;">'+dataRow+"</a>";
		  	  	}
		    },
		    //{name: 'typeName', index:'typeName', style: {'textAlign': 'center'}},
		    {name: 'no', index:'no', style: {'textAlign':'center'}},
		    {name: 'showName', index:'showName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'updateTime', index:'updateTime', style: {'textAlign': 'center'}},
		    //{name: 'staff',index:'staff',sortable:'false',style:{'textAlign':'center'},
//		    	formart:function(dataRow,data,fn){
//		    		if(dataRow == null||dataRow==''){
//		    			if(dataRow == null||dataRow==''){ 
//	              			return '<a href="javascript:void(0); " onclick="rfidDialog('+data.id+','+data.groupTree.id+')" style="text-decoration:underline;">'+'关联员工'+"</a>";
//	              		} else {
//	              			return dataRow;
//	              		}
//		    		}
//		    	}
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
				url: top.pt.ctx+"/web/module/getAllTree.action?isTreeOpen=false",
				autoParam: ["id", "label"],
				dataType :"json"
			},
			callback : {
				//树加载成功的之后
				onAsyncSuccess: function(event, treeId, treeNode, msg){
					
					if(node != null){
						tree.selectNode(node);
					}else{
						node = tree.getNodeById($("#topGroupId").val());
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
				//点击事件
				onClick: function(event, treeId, treeNode){
					node = treeNode;
					$("#treeId").val(treeNode.id);//将treeId放入过滤的form表单，用户过滤该组织下的设备
					$('#table').grid('reload');
					getNodeInfo(treeNode.id);
				}

			},
			view : {
				fontCss : function(treeId, treeNode) {
					return (!!treeNode.highlight) ? {
						color : "#A60000",
						"font-weight" : "bold",
						"font-size" : "16px"
					} : {
						color : "#25c4eb",
						"font-weight" : "normal",
						"font-size" : "16px"
					};
				}
			}
	};
	tree = $("#zTreea").tree(setting);

	//增加树组织
	$("#addTree-btn").click(function() {
		$.ajax({url:top.pt.ctx+'/web/module/device/getUserDepartId.action',
			type:"get",
			dataType:"text",
			success:function(data){
				var showName="";
				var selectedNode = tree.getSelectedNodes()[0];
				if(selectedNode.level == 0){
					if(data!=132 && data!=1){//感控科是可以添加的
						jAlert('当前用户不可添加科室信息', '提示', 'attention');
						return;
					}
					showName= '添加科室';
				}else if(selectedNode.level == 1){
					showName= '添加病区';
				}else if(selectedNode.level == 2){
					showName= '添加';
				}else if(selectedNode.level == 3){
					jAlert('病床和走廊组织下不能添加下属节点!', '提示', 'attention');
					return;
				}
				if(!selectedNode){
					var selectedNode = {id : $("#topGroupId").val()}
				}
				if(selectedNode.level == 2){//添加病床区、水洗区、污染区、洁净区
					$.dialog({
			            width: 500,
			            height: 500,
			            draggable: true,
			            maybeRefresh: true,
			            id: 'bedTreeIframe',
			            title: showName,
			            iframeSrc: top.pt.ctx+'/web/module/device/addBedTree.action?groupTree.parentId='+selectedNode.id+'&typeLevel='+selectedNode.level,
			            buttons: [{
			                text: '确定',
			                'class': 'bPrimary',
			                "id": 'bind_bedTree_btn_ok',
			                click: function(dialog, callback){
			                	//父页面获取
			                	var getIds = top.frames["bedTreeIframe"].$("body").data('getIds');
			                	var ids = getIds && getIds();
			                	$.ajax({
			    					url : 'saveBedTree.action',
			    					data : {
			    						'groupTree.name' : ids[1],
			    						'groupTree.parentId' : ids[2],
			    						'groupTree.remark' : ids[3],
			    						'groupTree.type' : ids[4],
			    						'groupTree.addType' : ids[5],
			    						'groupTree.deviceType' : ids[6],
			    						'groupTree.count' : ids[7]
			    					},
			    					beforeSend : function(){
										return top.frames["bedTreeIframe"].$("#bedTreeForm").validate();
									},
			    					success : function(data, callback){
			    						if (data.success == true) {
		    								dialog.close();
		    								jAlert("操作成功", '提示', 'attention');
		            						tree.reAsyncChildNodes(null, "refresh");
		    							}else {
		    								jAlert(data.msg, '错误', 'error');
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
				}else{
					$.dialog({
			            width: 500,
			            height: 350,
			            draggable: true,
			            maybeRefresh: true,
			            id: 'groupTreeIframe',
			            title: showName,
			            iframeSrc: top.pt.ctx+'/web/module/device/selectGroupTree.action?groupTree.parentId='+selectedNode.id+'&typeLevel='+selectedNode.level,
			            buttons: [{
			                text: '确定',
			                'class': 'bPrimary',
			                "id": 'bind_groupTree_btn_ok',
			                click: function(dialog, callback){
			                	//父页面获取
			                	var getIds = top.frames["groupTreeIframe"].$("body").data('getIds');
			                	var ids = getIds && getIds();
			                	$.ajax({
			    					url : 'saveGroupTree.action',
//			    					data : {
//			    						'groupTree.name' : ids[1],
//			    						'groupTree.parentId' : ids[2],
//			    						'groupTree.remark' : ids[3],
//			    						'groupTree.type' : ids[4],
//			    						'groupTree.departLevel' : ids[5]
//			    					},
			    					data:top.frames["groupTreeIframe"].$("#groupTreeForm").serialize(),
			    					beforeSend : function(){
										return top.frames["groupTreeIframe"].$("#groupTreeForm").validate();
									},
			    					success : function(data, callback){
			    						dialog.close();
			    						jAlert("操作成功", '提示', 'attention');
			    						tree.reAsyncChildNodes(null, "refresh");
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
				}
			}
		});
		
		
		
	});
		
	//编辑树组织
	$("#editTree-btn").click(function() {
		$.ajax({url:top.pt.ctx+'/web/module/device/getUserDepartId.action',
			type:"get",
			dataType:"text",
			success:function(data){
				var showName="";
				var selectedNode = tree.getSelectedNodes()[0];
				if(selectedNode.level == 0){
					jAlert('医院信息不可修改', '提示', 'attention');
					return;
				}else if(selectedNode.level == 1){
					if(data!=132 && data!=1){
						jAlert('当前用户不可修改科室信息', '提示', 'attention');
						return;
					}
					showName= '修改科室信息';
				}else if(selectedNode.level == 2){
					showName= '修改病区信息';
				}else if(selectedNode.level == 3){
					showName= '修改病床信息';
				}
				if(!selectedNode){
					var selectedNode = {id : $("#topGroupId").val()}
				}
				if(selectedNode!=null){
					$.dialog({
		                width: 500,
		            	height: 350,
		                maybeRefresh: true,
		                id: 'groupTreeIframe',
		                title: showName,
		                iframeSrc: top.pt.ctx+'/web/module/device/updateSelectGroupTree.action?groupTree.id='+selectedNode.id+'&typeLevel='+selectedNode.level,
		                load: function(callback){
		                    callback.fire('load');
		                },
		                close: function(){
		                },
		                buttons: [{
		                    text: '确定',
		                    'class': 'bPrimary',
		                    "id": 'bind_groupTree_btn_ok',
		                    click: function(dialog, callback){
		                    	//debugger;
		                    	//父页面获取
		                    	var getIds = top.frames["groupTreeIframe"].$("body").data('getIds');
		                    	var ids = getIds && getIds();
		                    	$.ajax({
		        					url : 'saveGroupTree.action',
//		        					data : {
//		        						'groupTree.id' : ids[0],
//		        						'groupTree.name' : ids[1],
//		        						'groupTree.parentId' : ids[2],
//		        						'groupTree.remark' : ids[3],
//		        						'groupTree.departLevel' : ids[4]
//		        					},
		        					data:top.frames["groupTreeIframe"].$("#groupTreeForm").serialize(),
		        					beforeSend : function(){
										return top.frames["groupTreeIframe"].$("#groupTreeForm").validate();
									},
		        					success : function(data, callback){
		        						if (data.success == true) {
		    								dialog.close();
		    								jAlert("操作成功", '提示', 'attention');
		            						tree.reAsyncChildNodes(null, "refresh");
		    							}else {
		    								jAlert("操作失败："+data.msg, '错误', 'error');
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
				}
			}
		});
		
	});
		
	//删除树组织
	$("#delTree-btn").click(function() {
		$.ajax({
			url:top.pt.ctx+'/web/module/device/getUserDepartId.action',
			type:"get",
			dataType:"text",
			success:function(data){
				var selectedNode = tree.getSelectedNodes()[0];
				if(selectedNode.level == 0){
					jAlert('医院信息不能删除', '提示', 'attention');
					return;
				}
				if(selectedNode.level == 1){
					if(data!=132 && data!=1){
						jAlert('当前用户不可删除科室信息', '提示', 'attention');
						return;
					}
				}
				if(selectedNode.children!=null){
					jAlert('请先删除下属节点信息!', '提示', 'attention');
					return;
				}
				if(tree.getSelectedNodes().length>0){
					if(selectedNode!=null){
						jConfirm('确认要删除当前组织吗？', '确认信息', function(r) {
							if (r) {
								$.ajax({
			                		url : 'deleteGroupTree.action',
			                		dataType : "json",
			                		data : {
			                			ids : selectedNode.id
			                		},
			                		success : function(data){
		        						if (data.success == true) {
		    								jAlert("删除成功", '提示', 'attention');
		            						tree.reAsyncChildNodes(null, "refresh");
		    							}else {
		    								jAlert("操作失败："+data.msg, '错误', 'error');
		    							}
			                		}
			                	});
							}
						});
					}else{
						jAlert('您还没有选中组织', '提示', 'attention');
					}
				}else{
					jAlert('您还没有选中组织', '提示', 'attention');
				}
				
			}
		});
		
		
	});
		
	//刷新树组织
	$("#reload-btn").click(function() {
		tree.reAsyncChildNodes(null, "refresh");
	});
	
	//查询
	$("#data_filtering").click(function(){
		$('#table').grid('reload');
	});
	
	//添加设备信息
	$("#add_btn").click(function(){
		var showTitle="添加设备信息";
		var selectedNode = tree.getSelectedNodes()[0];
		if(selectedNode==null){
			jAlert('请选择所属科室!', '提示', 'attention');
			return;
		}
		else if(selectedNode.level == 0){
			jAlert('医院下面不能添加设备!', '提示', 'attention');
			return;
		}
		else if(selectedNode.level == 1){
			showTitle="添加胸牌信息";
		}
		$.dialog({
            width: 800,
            height: 600,
            draggable: true,
            maybeRefresh: true,
            id: 'addframe',
            title: showTitle,
            iframeSrc: top.pt.ctx+'/web/module/device/showDialog.action?groupTreeId='+selectedNode.id+'&typeLevel='+selectedNode.level,
            buttons: [{
                text: '确定',
                'class': 'bPrimary',
                "id": 'add_device_btn_ok',
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
	
	//删除设备
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

//修改设备信息
function modifyDialog(dataId){
	$.dialog({
		width : 800,
		height : 600,
		draggable: true,
		maybeRefresh : true,
		id : 'editframe',
		title : "修改设备信息",
		iframeSrc : top.pt.ctx+'/web/module/device/showDialog.action?entity.id='+dataId,
		buttons : [{
			text : '确定',
			'class' : 'bPrimary',
			"id" : 'modify_device_btn_ok',
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
//得到节点信息	
function getNodeInfo(treeId){
	console.log("tree id is ",treeId);
	if(treeId == 1){
		setNodeInfo("医院","无",LEVEL_ALL,"");
		return;
	}
	//把treeId作为参数传入
	$.ajax({url:top.pt.ctx+'/web/module/device/findGroupTreeById.action?groupTreeId='+treeId,
		type:"get",
		dataType:"text",
		success:function(data){
			console.log(data);
			var obj = JSON.parse(data);
			var name = obj.name;

			var nodeName = obj.name;;
			var nodeParent = obj.parentName;
			
			var nodeRemark = obj.remark;
			if(nodeRemark == null){
				nodeRemark = "";
			}
			var nodeLevel = "";
			if(obj.departLevel == 1){//当前
				nodeLevel = LEVEL_CURRENT;
			}else if(obj.departLevel == 0){
				nodeLevel = LEVEL_ALL;
			}else{
				nodeLevel = LEVEL_ELSE;
			}
			LEVEL_CURRENT;
			setNodeInfo(nodeName,nodeParent,nodeLevel,nodeRemark);

		
		}
	});
}

//设置节点信息值
function setNodeInfo(name,parent,level,remark){
	$("#info_name").text(name);
	$("#info_parent").text(parent);
	$("#info_level").text(level);
	$("#info_remark").text(remark);
}

