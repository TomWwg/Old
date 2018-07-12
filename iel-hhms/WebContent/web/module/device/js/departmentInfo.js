var tree;
var node;
var rows=30;
// DOM加载完后执行
$(function() {
	setGridHeight1();//设置列表高度自适应
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
				onClick: function(event, treeId, treeNode){
					node = treeNode;
					$("#treeId").val(treeNode.id);//将treeId放入过滤的form表单，用户过滤该组织下的设备
					$('#table').grid('reload');
				}

			},
			view : {
				fontCss : function(treeId, treeNode) {
					var style = {} ;
					if(treeNode.level == 0 ){//最顶级
						style = {
								color : "#385fee",
								"font-weight" : "bold",
								"font-size" : "20px",
								"height":"25px"
							} ;
					}else if(treeNode.level == 1 ){//次顶级
						style = {
								color : "#25c5eb",
								"font-weight" : "bold",
								"font-size" : "18px",
								"height":"25px"
							} ;
						
					}else {//其他
						style = {
								color : "#6987f7",
								"font-weight" : "bold",
								"font-size" : "18px",
								"height":"25px"
							} ;
					}
					return style;
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
					if(data!=1){
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
					if(data!=1){
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
					if(data!=1){
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
	
	//展开全部树组织
	$("#extendTree-btn").click(function() {
		
		tree.expandAll(true);
		
	});
	//收齐全部
	$("#closeTree-btn").click(function() {
		
		tree.expandAll(false);
		
		
	});
	//操作的hover事件
	$(".one_btn_section").hover(function() {
		consoel.log("hovered");
		$(".one_btn_section").css("background-color","yellow");
		
		
	},function() {
		consoel.log("hovered");
		$(".one_btn_section").css("background-color","red");
		
		
	});
	

});  

	


