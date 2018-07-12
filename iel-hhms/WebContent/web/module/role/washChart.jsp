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
     <!-- 菜单栏  -->
	<div class="breadLine displayNone">
		<ul class="breadcrumbs auto-navigation" menu-code="${menuCode}"></ul>
	</div>
	<!-- 主页面 -->
	<div class="wrapper grid-s6m0">
		<div class="col-main">
			<div class="main-wrap"
				style="overflow-y: inherit;">
				
				<!-- 查询条件  -->
				<div class="fluid filterForm">
					<form id="filterForm" action="" method="post"
						class="form-horizontal" history="true">
						<ul class="formRow" style="padding: 0px">
							<li class="control-group auto-by-filter"><label
								class="control-label">员工姓名：</label>
								<div class="controls">
									<s:select list="staffList" id="staffId" name="queryEntity.int1" listKey="id" listValue="name" theme="simple" headerKey="-1"
		                                   headerValue="---请选择---" />
								</div></li>
							<li class="control-group auto-by-filter cols2"><label
								class="control-label">起止时间：</label>
								<div class="controls">
									<input id="startTime" type="text"  gkui="calendar" showTime="true"
								maxDate="#F{$dp.$N('logBo.createTimeStop',{d:-1});}"
								name="logBo.createTimeStart"
								class="gkui-calendar Wdate datetimepicker" readonly="readonly">
								--<input id="endTime" type="text"  gkui="calendar" showTime="true"
								minDate="#F{$dp.$N('logBo.createTimeStart',{d:1});}"
								name="logBo.createTimeStop"
								class="gkui-calendar Wdate datetimepicker" readonly="readonly">
								</div></li>
						</ul>
						<div class=btn-set>
							<a class="buttonS bBlue" type="button" onclick="setData();">查询</a>
						</div>
					</form>
				</div>
                <!-- 数据列表  -->
                 <div id="chart_combo" style="height : 500px;padding-top:10px;"></div>
			</div>
		</div>
		<!-- 左侧树 -->
		<div class="col-sub" >
			<div class="treeview">
				<ul id="zTreea" class="ztree" style="margin: 0px 10px"></ul>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="js/washChart.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts/highchartsComm.js"></script>
</body>
</html>