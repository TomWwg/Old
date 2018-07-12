<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<style type="text/css">
</style>
<script type="text/javascript">
var tree;
var node;
// DOM加载完后执行
$(function() {
	var setting = {
			async: {
				enable: true,
				url: "getFavoriteTree.action?userId="+ $("#userId").val(),
				autoParam: ["id", "label"],
				dataType :"json"
			},
			check: {
				enable: true
			},
			callback : {
				//树加载成功的之后
				onAsyncSuccess: function(event, treeId, treeNode, msg){
					if(node != null){
						tree.selectNode(node);
					}
				} ,
				onCheck: function(event, treeId, treeNode){
				    var s="";
					var nodes = tree.getCheckedNodes(true);
					for(var i=0;i<nodes.length;i++) {
						if(nodes[i].id!=0){
							s=s+nodes[i].id+",";
						}
					}
					$("#favoriteIds").val(s); //得到所有选中的菜单id 
				}
			},
			view : {
				fontCss : function(treeId, treeNode) {
					return (!!treeNode.highlight) ? {
						color : "#A60000",
						"font-weight" : "bold"
					} : {
						color : "#333",
						"font-weight" : "normal"
					};
				}
			}
	};
	tree = $("#zTreea").tree(setting);
	
		      
});  
</script>
</head>
<body>
	<div class="pageContent">
		<form id="showForm" action="" method="post" class="form-horizontal form-input">
		    <input type="hidden" id="userId" name="userId" value='<s:property value="userId"/>' >
		    <input type="hidden" id="favoriteIds" name="favoriteIds">  
			<div class="treeview">
				<ul id="zTreea" class="ztree" style="margin: 0px 10px"></ul>
			</div>
		</form>
	</div>
</body>
</html>


