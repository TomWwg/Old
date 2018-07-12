<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>

</head>
<body onload="prettyPrint();">
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
						<input type="hidden" id="treeId" name="queryEntity.treeId" value="1"/>
						<ul class="formRow" style="padding: 0px">
						    <li class="control-group auto-by-filter"><label
								class="control-label">员工姓名：</label>
								<div class="controls">
		                        <select id="staffId" name="queryEntity.int1">
							       <option value="-1">---全部---</option>
							    </select>
								</div></li>
							<li class="control-group auto-by-filter cols2"><label
								class="control-label">起止时间：</label>
								<div class="controls">
									<input id= "startTime" type="text" gkui="calendar" showTime="true"
										maxDate="#F{$dp.$N('queryEntity.endTime',{d:0});}"
										name="queryEntity.startTime"  isShowClear="false"
										class="gkui-calendar Wdate datetimepicker" readonly="readonly">
								--<input id= "endTime" type="text" gkui="calendar" showTime="true"
										minDate="#F{$dp.$N('queryEntity.startTime',{d:0});}"
										name="queryEntity.endTime"  isShowClear="false"
										class="gkui-calendar Wdate datetimepicker" readonly="readonly">
								</div></li>
						</ul>
						<div class=btn-set>
							<a class="buttonM bBlue" type="button" id="data_filtering">查询</a>
						</div>
					</form>
				</div>
                <!-- 数据列表  -->
                <div id="chart_combo" style="height: 700px;"></div>
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
	<script type="text/javascript" src="${ctx}/js/highcharts/exporting.js" ></script>
</body>
</html>