<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<style type="text/css">
</style>
<script type="text/javascript" >
$(function() {
	$("input:file").uniform();
});  
</script>
</head>
<body>
	<div class="pageContent">
		<form id="showForm" action="" method="post" class="form-horizontal form-input">
		    <input type="hidden" id="id" name="entity.id"
				value='<s:property value="entity.id"/>'>
			<div class="pageContent">
				<fieldset class="noLegend">
				    <div class="control-group input">
					   <label class="control-label"><em>*</em>参数类型：</label>
					   <div class="controls">
						  <s:select id='type' list='parameterMap' listKey='key' listValue='value' 
							   name="entity.type">
				          </s:select>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label"><em>*</em>参数关键字：</label>
						<div class="controls">
							<input id="key" name="entity.key" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.key"/>'>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label"><em>*</em>参数值：</label>
						<div class="controls">
							<input id="value" name="entity.value" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.value"/>'>
						</div>
					</div>
					<!-- <div class="control-group input">
						<label class="control-label"><em>*</em>参数值：</label>
						<div class="controls">
							<input id="photo" name="photo" type="file" accept="image/*"/>
						</div>
					</div> -->
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>


