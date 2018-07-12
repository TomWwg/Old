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
		<form id="bedTreeForm" action="saveGroupTree.action" method="post"
			class="form-horizontal form-input">
			<input type="hidden" id="iId" name="groupTree.id"
				value="${groupTree.id}" />
			<input type="hidden" id="type" name="groupTree.type"
				value="${groupTree.type}" />
			<input type="hidden" id="typeLevel" name="typeLevel"
				value="${typeLevel}" />
			<div class="control-group" style="margin-top: 20px;">
				<label class="control-label"><em>*</em>添加方式：</label>
				<span onclick="getRadio();">
				  <input type="radio" name="addType" value='1' checked="checked">批量 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				  <input type="radio" name="addType" value='2'>单个
				</span>
			</div>
			<div id='more1' class="control-group" style="margin-top: 20px;">
				<label class="control-label"><em>*</em>类型：</label>
				<div class="controls">
				   <select id="deviceType" name="deviceType" >
                      <option value="1" selected="selected">病床区</option>
                      <option value="2">水洗区</option>
                      <option value="3">洁净区</option>
                      <option value="4">污染区</option>
                      <option value="5">走廊区</option>
                   </select>
			   </div>	
			</div>
			<div id='more2' class="control-group" style="margin-top: 20px;">
			   <label class="control-label"><em>*</em>数量：</label>
			   <div class="controls">
			      <input id="count" name="count" vtype="number"
						class='auto-input' type='text' minValue=1 maxValue=99> 
			   </div>
			</div>
			<div id='one' class="control-group" style="margin-top: 20px; display:none" >
				<label class="control-label"><em>*</em>名称：</label>
				<div class="controls">
					<input id="name" name="groupTree.name" class='auto-input'
						 type='text' vtype="common"
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
			<div class="control-group" style="margin-top: 20px;">
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
    	    ids.push($("#iId").val(), $("#name").val(), $("#parentId").val(), $("#remark").val(), $("#typeLevel").val(),
    	    		$('input:radio[name=addType]:checked').val(), $("#deviceType").val(), $("#count").val());
    	    return ids;
    	});
	});
	function getRadio(){
	　　if($('input:radio[name=addType]:checked').val()==1){
		  $("#more1").show();
		  $("#more2").show();
		  $("#one").hide();
	　　}else{
		  $("#one").show();
		  $("#more1").hide();
		  $("#more2").hide();
	　　}　
	　}
	</script>
</body>
</html>
