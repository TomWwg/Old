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
<style type="text/css">
.statis_result{
	font-size:30px;
	color:#5D97DD;
}
.error_info{
	font-size:18px;
	color:#5D97DD;
}

</style>
<%-- <script type="text/javascript" src="${ctx}/js/assets/jquery.1.7.2.min.js"></script> --%>
<script type="text/javascript" src="${ctx}/js/assets/jquery-ui.js"></script> 
<script type="text/javascript" src="${ctx}/js/assets/prettify.js"></script>
<script type="text/javascript" src="${ctx}/js/assets/jquery.multiselect.js" ></script>
<script type="text/javascript" src="${ctx}/js/highcharts/highcharts.js" ></script>
<script type="text/javascript" src="${ctx}/js/highcharts/highchartsComm.js" ></script>
<script type="text/javascript" src="${ctx}/js/highcharts/exporting.js" ></script>
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
				style=" overflow-y: inherit;">
				<!-- 查询条件  -->
				<div class="fluid filterForm">
					<form id="filterForm" action="" method="post"
						class="form-horizontal" history="true">
						<input type="hidden" id="treeId" name="queryEntity.treeId" value="1"/> 
						<input type="hidden" id="staffTypeInput" name="queryEntity.str1" value='<s:property value="queryEntity.str1"/>'/>
						<input type="hidden" id="departInput" name="queryEntity.str1" />
						<ul class="formRow" style="padding: 0px">
							<!--  
							<li type="hidden" class="control-group auto-by-filter">
								<label class="control-label">每次出液预估：</label>
								<div class="controls">
                                      <input type="text" class="perPush" name="perPush"  style="width:100px;text-align:right;"><label style="color:#0b7ba7"> mL</label>
								</div>
								
							</li>
							<li type="hidden" class="control-group auto-by-filter">
								<label class="control-label">每瓶预设含量：</label>
								<div class="controls" >
                                      <input type="text" class="perBottle" name="perBottle" style="width:100px;text-align:right;"><label style="color:#0b7ba7"> mL</label>
								</div>
								
							</li>
							-->
							<li class="control-group auto-by-filter cols2"><label
								class="control-label">起止时间：</label>
								<div class="controls">
									<input id= "startTime" type="text" gkui="calendar" showTime="true"
										maxDate="#F{$dp.$N('queryEntity.endTime',{d:0});}"
										name="queryEntity.startTime" isShowClear="false"
										class="gkui-calendar Wdate datetimepicker" readonly="readonly">
								--<input id= "endTime" type="text" gkui="calendar" showTime="true"
										minDate="#F{$dp.$N('queryEntity.startTime',{d:0});}"
										name="queryEntity.endTime" isShowClear="false"
										class="gkui-calendar Wdate datetimepicker" readonly="readonly">
								</div></li>
						</ul>

						<div class=btn-set>
							<a class="buttonM bBlue" type="button" id="data_filtering">统计</a>
							<a class="buttonM bBlue" type="button" onclick="excelData();"><span class="ico i-import"></span><span>导出</span></a>
						</div>
					</form>
				</div>
				<div class="statisticFrame" style="padding-left:20px;border-style: solid; border-width: 2px 0px 0px 0px; border-color: #aaaaaa;">
					<ul class="formRow" style="padding: 0px">
						<li class="control-group auto-by-filter">
							<label>本次统计的结果:<span class="error_info" id="error_info" ></span><span class="error_info" id="select_info" >&nbsp;&nbsp;</span></label>
						</li>
						<li class="control-group auto-by-filter">
						
							<label>在所选时间段内，部门内共洗手:&nbsp;&nbsp;&nbsp;<span class="statis_result" id="statis_result_total_times">100</span>&nbsp;&nbsp;次    ;</label>
				
						
						</li>
					</ul>
					
					<div id="chart_combo" style="height: 200px;"></div>
				</div>
				
				<div class="datatable" style="height: 40%;">
					<table id="table">
					
					</table>
					<!--  
					<div id="dataTablePage"></div> 
					 -->
				</div>
			</div>
		</div>
		<!-- 左侧树 -->
		<div class="col-sub">
			<div class="treeview">
				<ul id="zTreea" class="ztree" style="margin: 0px 10px"></ul>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="js/washTimesStatistics.js"></script>
</body>
</html>
