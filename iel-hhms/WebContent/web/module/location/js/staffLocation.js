var tree;
var node;
var orgNode;
// DOM加载完后执行
$(function() {
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
				},
				onClick: function(event, treeId, treeNode){
					node = treeNode;
					$("#treeId").val(treeNode.id);//将treeId放入过滤的form表单，用户过滤该组织下的设备
					$.post(top.pt.ctx+"/web/module/location/getStaff.action", {
						groupTreeId : treeNode.id
					   }, function(data) {
							$("#staffId").empty();
							$('#staffId').html(data.msg).show();  
								
					}, 'json');
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
	setInterval("searchData()", 1000*60);
});  
//查询
$("#data_filtering").click(function(){
	searchData();	
});
function searchData(){
	var staffId = $("#staffId").val();
	if(staffId == -2){
		alert("请选择你要查询的人员！");
		return false;
	}
	$.ajax({
		url : 'getOrgTree.action',
		data : {
			staffId : staffId
     	},
		success : function(data, callback){
			var orgNode = data.orgNode;
			var locationId = data.locationId;
			 var showlist = $("<ul id='org' style='display:none'></ul>");
               showall(orgNode, showlist);
               $("div #jOrgChart").html("");
               $("#jOrgChart").append(showlist);
               $("#org").jOrgChart( {
                  chartElement : '#jOrgChart',//指定在某个dom生成jorgchart
                  dragAndDrop : false //设置是否可拖动
               });
               $("#org").html("");
               $("#"+locationId+"").append("<br/><img src='../../../images/test3.gif'  width='40px' height='40px'>");             
			},
		dataType : "json"
	});
}

//动态生成组织树
function showall(orgNode, parent){
		 var a ;
		 var b =0;
	   if(orgNode.children != null){
		   b = orgNode.children.length;
		   a = orgNode.children;
	   }	    
		//如果有子节点，则遍历该子节点
		if( b > 0){
			//创建一个子节点li
			var li = $("<li id="+orgNode.id+"></li>");
			//将li的文本设置好，并马上添加一个空白的ul子节点，并且将这个li添加到父亲节点
			$(li).append(orgNode.name).append("<ul></ul>").appendTo(parent);
			for(var i =0;i < b;i++){					
				//将空白的ul作为下一个递归遍历的父亲节点传入
				showall(orgNode.children[i],$(li).children().eq(0))
			}
				
		}else{
			//如果该节点没有子节点，则直接将该节点li以及文本创建好直接添加到父亲节点中
			 $("<li id="+orgNode.id+"></li>").append(orgNode.name).appendTo(parent);
		}
   
}
