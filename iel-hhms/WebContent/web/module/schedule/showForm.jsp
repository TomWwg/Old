<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/js/assets/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/assets/style.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/assets/prettify.css" />
<link href="${ctx}/js/assets/jquery-ui.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/js/assets/jquery-ui.js"></script> 
<script type="text/javascript" src="${ctx}/js/assets/prettify.js"></script>
<script type="text/javascript" src="${ctx}/js/assets/jquery.multiselect.js" ></script>
<style type="text/css">
</style>
</head>
<body>
	<div class="pageContent">
		<form id="showForm" action="" method="post" class="form-horizontal form-input">
		    <input type="hidden" id="id" name="entity.id"
				value='<s:property value="entity.id"/>'>
			<div class="pageContent">
				<fieldset class="noLegend">
					<div class="control-group input">
						<label class="control-label"><em>*</em>排班名称：</label>
						<div class="controls">
							<input id="name" name="entity.name" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.name"/>'>
						</div>
					</div>
					<div class="control-group">
                      <label class="control-label"><em>*</em>起始时分：</label>
                      <div class="controls">
                        <input id="startHm" name="startHm" class='auto-input' type='text' required="true" minDate="minTime" isShowClear="false"
								 gkui="calendar" dateFmt="HH:mm" value='<s:property value="startHm"/>'>
                      </div>
                    </div>
					<div class="control-group">
                      <label class="control-label"><em>*</em>结束时分：</label>
                      <div class="controls">
                        <input id="endHm" name="endHm" class='auto-input' type='text' required="true" minDate="minTime" isShowClear="false"
								 gkui="calendar" dateFmt="HH:mm" value='<s:property value="endHm"/>'>
                      </div>
                    </div>
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>


