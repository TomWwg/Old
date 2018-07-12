<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.sys.core.util.StringUtils"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<meta charset="utf-8" />
<title>手卫生依从性管理系统</title>
<style type="text/css">
input[type="text"] {
	width: 220px;
}

#groupInfoTree {
	display: none;
	background-color: #FCFCFC;
	border-left: 1px solid #c1c0c0;
	border-bottom: 1px solid #c1c0c0;
	border-right: 1px solid #c1c0c0;
	position: absolute;
	z-index: 2;
	width: 230px;
}
</style>
</head>
<body class="page">
	<div class="wrapper" style="padding: 0px;">
		<form id="groupTreeForm" action="saveGroupTree.action" method="post"
			class="form-horizontal form-input">
			<input type="hidden" id="iId" name="groupTree.id"
				value="${groupTree.id}" />
			<%-- <input type="hidden" id="type" name="groupTree.type"
				value="${groupTree.type}" /> --%>
			<input type="hidden" id="typeLevel" name="groupTree.type"
				value="${typeLevel}" />
			<div class="control-group" style="margin-top: 20px;">
				<label class="control-label"><em>*</em>名称：</label>
				<div class="controls">
					<input id="name" name="groupTree.name" class='auto-input'
						required="true" type='text' vtype="common"
						value='${groupTree.name}' />
				</div>
			</div>
			<div class="control-group" style="margin-top: 20px;">
				<label class="control-label"><em>*</em>上一级：</label>
				<div class="controls" style='position: relative;'>
					<input id="parentId" name="groupTree.parentId" type="hidden"
						value='${parentGroup.id}' /> <input id="parentName"
						name="parentName" value='${parentGroup.name}'
						class='auto-input' style="background-color: #FCFCFC;"
						required="true" type='text' readonly="readonly" />
				</div>
			</div>
			<s:if test="%{typeLevel==0}">
			<div class="control-group" style="margin-top: 20px;display:none;">
				<label class="control-label"><em>*</em>科室级别：</label>
				<div class="controls" style='position: relative' >
					 <select name="groupTree.departLevel" id="groupTree.departLevel" style="width: 232px; height:26px">
			        	<option value="1" selected="true">当前科室</option>
			        	<option value="0">全院科室</option>
		         	</select>
				</div>
			</div>
			</s:if>
			<div class="control-group" style="margin-top: 20px">
				<label class="control-label">备注：</label>
				<div class="controls">
					<input id="remark" name="groupTree.remark" vtype="common"
						class='auto-input' type='text' value='${groupTree.remark}'>
				</div>
			</div>
		</form>
	</div>

	<script type="text/javascript">
	$(function(){
		$("body").data('getIds', function(){
    	    var ids = [];
    	    ids.push($("#iId").val(), $("#name").val(), $("#parentId").val(), $("#remark").val(), $("#typeLevel").val(),$("#groupTree.departLevel").val());
    	    return ids;
    	});
	});
	</script>
	
</body>
</html>
