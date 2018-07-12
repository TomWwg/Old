<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<style type="text/css">
/* .input { 
		padding-top:10px;
	} */
</style>
</head>
<body>
	<div class="pageContent">
		<form id="formDevice" action="" method="post"
			class="form-horizontal form-input">
			<div class="pageContent">
				<fieldset class="noLegend">
			        <div class="control-group">
						<label class="control-label">设备类型：</label>
						<div class="controls">
						   <select id="isEncrypt" name="cameraBo.isEncrypt">
							<option value="">请选择</option>
							<option value="1">加密</option>
							<option value="0">不加密</option>
						   </select>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label"><em>*</em>设备编号：</label>
						<div class="controls">
							<input id="phoneNo" name="phoneNo" class="auto-input" 
								type="text" required="true" vtype="common" >
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label"><em>*</em>设备名称：</label>
						<div class="controls">
							<input id="description" name="description" class="auto-input"
								type="text" required="true" vtype="common">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">是否使用：</label>
						<div class="controls">
						   <select id="isEncrypt" name="cameraBo.isEncrypt">
							<option value="0">是</option>
							<option value="1">否</option>
						   </select>
						</div>
					</div>
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>