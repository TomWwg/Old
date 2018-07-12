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
		    <input type="hidden" id="id" name="entity.id"
				value='<s:property value="entity.id"/>'>
			<input type="hidden" id=groupTreeId name="groupTreeId"
				value='<s:property value="groupTreeId"/>'>
			<div class="pageContent">
				<fieldset class="noLegend">
					<div class="control-group input">
						<label class="control-label"><!-- <em>*</em> -->员工编号：</label>
						<div class="controls">
							<input id="no" name="entity.no" class='auto-input' type='text'
								 vtype="common" value='<s:property value="entity.no"/>'>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label"><em>*</em>员工姓名：</label>
						<div class="controls">
							<input id="name" name="entity.name" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.name"/>'>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label"><em>*</em>人员类别：</label>
						<div class="controls">
							<s:select id='category' list='jobTypeMap' listKey='key' listValue='value' 
							    name="entity.category">
				            </s:select>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label"><em>*</em>性别：</label>
						<div class="controls">
						<s:select list="#{'男':'男','女':'女'}" id="sex" name="entity.sex" />
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label">手机：</label>
						<div class="controls">
							<input id="tel" name="entity.tel" class='auto-input' type='text'
								vtype="mobile" value='<s:property value="entity.tel"/>'>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label">职务：</label>
						<div class="controls">
							<input id="duty" name="entity.duty" class='auto-input' type='text'
							    vtype="common" value='<s:property value="entity.duty"/>'>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label">职称：</label>
						<div class="controls">
							<s:select id='jobTitle' list='jobTitleMap' listKey='key' listValue='value' 
							    name="entity.jobTitle">
				            </s:select>
						</div>
					</div>
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>


