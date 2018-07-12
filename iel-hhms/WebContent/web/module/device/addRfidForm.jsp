<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<style type="text/css">
</style>
</head>
<body>
	<div class="pageContent">
		<form id="showForm" action="" method="post" class="form-horizontal form-input">
		<input type="hidden" id="staffId" name="entity.staffId" value='<s:property value="entity.staffId"/>'>
		<input type="hidden" id="groupTreeId" name="entity.groupTree.id" value='<s:property value="entity.groupTree.id"/>'>
			<div class="pageContent">
				<fieldset class="noLegend">
					 <div class="control-group input">
						<label class="control-label"><em>*</em>胸牌编号(6位)：</label>
					    <div class="controls">
						   <input id="no" name="entity.no" class='auto-input' type='text' maxLength=6 minLength=6
							   required="true" vtype="common" value='<s:property value="entity.no"/>'>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label"><em>*</em>名称：</label>
						<div class="controls">
							<input id="name" name="entity.name" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.name"/>'>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label">备注：</label>
						<div class="controls">
							<textarea vtype="common" class="att-width300" name="entity.remark" style="width:435px;;height: 150px""><s:property value="entity.remark"/></textarea>
						</div>
					</div>
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>