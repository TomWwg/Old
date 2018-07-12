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
<body class="grid">
	<DIV class="breadLine displayNone">
		<UL class="breadcrumbs auto-navigation" menu-code="${menuCode}">
		</UL>
	</DIV>
	<div class="wrapper" style="height: 100%" >
		<div class="fluid filterForm">
			<form id="filterForm" class="form-horizontal" method="post"
				history="true">
				<ul class="formRow" style="padding: 0px">
					<li class="control-group auto-by-filter"><label
						class="control-label">日志类型：</label>
						<div class="controls">
							<select name="queryEntity.int1">
								<option value="" selected="true">请选择</option>
								<option value="1">用户登录</option>
								<option value="2">用户登出</option>
								<option value="3">设备添加</option>
							</select>
						</div></li>
					<s:if test="1 == currentUserId">
						<li class="control-group auto-by-filter"><label
							class="control-label">操作用户：</label>
							<div class="controls">
								<input type="text" id=str1 name="queryEntity.str1">
							</div></li>
					</s:if>
					<li class="control-group auto-by-filter cols2"><label
								class="control-label">起止时间：</label>
								<div class="controls">
									<input id= "startTime" type="text" gkui="calendar" showTime="true"
										maxDate="#F{$dp.$N('queryEntity.endTime',{d:0});}"
										name="queryEntity.startTime" isShowClear="false"
										class="gkui-calendar Wdate datetimepicker" readonly="readonly">
									-- <input id= "endTime" type="text" gkui="calendar" showTime="true"
										minDate="#F{$dp.$N('queryEntity.startTime',{d:0});}"
										name="queryEntity.endTime" isShowClear="false"
										class="gkui-calendar Wdate datetimepicker" readonly="readonly">
								</div></li>
				</ul>
				<div class="btn-set">
					<a class="buttonM bBlue" type="button" href="javascript:void(0);"
						id="filter_btn">查询</a> 
					<a class="buttonM bBlue" type="button" onclick="excelData();" >
					   <span class="ico i-import"></span><span>导出</span></a>
				</div>
			</form>
		</div>
		<div class="datatable">
			<table id="table">
			</table>
			<div id="dataTablePage"></div>
		</div>
	</div>
	<script type="text/javascript" src="js/logInfoList.js"></script>
	<script>
        
        </script>
</body>
</html>