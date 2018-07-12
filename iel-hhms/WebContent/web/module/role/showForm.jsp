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
			<input type="hidden" id="menuIds" name="entity.menuIds"
				value='<s:property value="entity.menuIds"/>'>
			<input id="expireTime" type="hidden" name="entity.expireTime" value='2015-05-01 12:00:00'>
			<div class="pageContent">
				<fieldset class="noLegend">
					<div class="control-group input">
						<label class="control-label"><em>*</em>角色名：</label>
						<div class="controls">
							<input id="name" name="entity.name" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.name"/>'>
						</div>
					</div>
<%-- 					<div class="control-group">
                      <label class="control-label"><em>*</em>到期时间：</label>
                      <div class="controls">
                        <input id="expireTime" type="text" name="entity.expireTime" value='<s:property value="formateDateYMD(entity.expireTime)"/>'
                          gkui="calendar" class="auto-input" required="true" minDate="minTime" isShowClear="false">
                      </div>
                    </div> --%>
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


