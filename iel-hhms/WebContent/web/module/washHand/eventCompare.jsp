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
	<div class="wrapper" style="height: 100%">
		<!-- 查询条件  -->
		<div class="fluid filterForm">
			<form id="filterForm" action="" method="post" class="form-horizontal"
				history="true">
				<input type="hidden" id="treeId" name="queryEntity.treeId" value="1"/> <input
					type="hidden" id="departInput" name="queryEntity.str1" value='<s:property value="queryEntity.str1"/>'/>
				<ul class="formRow" style="padding: 0px">
					<li class="control-group auto-by-filter"><label
						class="control-label">科室：</label>
						<div class="controls">
						    <select id ="depart" multiple="multiple" onchange="departSelect()">
							    <c:forEach items="${departList}" var="dList">
							    	<c:if test="${dList.name != '感控科'}">
							    		<option value="${dList.id}">${dList.name}</option>
									</c:if>
                                   
                                </c:forEach>	     
	                        </select>
						</div></li>
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
					<a class="buttonM bBlue" type="button" id="data_filtering">查询</a>
					<a class="buttonM bBlue" type="button" onclick="excelData();" style="display:none;" ><span class="ico i-import"></span><span>导出</span></a>
				</div>
			</form>
		</div>
		<!-- 数据列表  -->

				
		<div id="chart_combo" style="height: 48%;"></div>
		<div style="text-align:right;margin-right:20px;margin-bottom:5px;">
			<div class=btn-set>
				<a class="buttonM bBlue" style=""
				type="button" onclick="excelData();" ><span>导出</span>
				</a>
			</div>
		</div>
		<div class="datatable" style="height: 40%;">
			<table id="table">
			</table>
		</div>
	</div>
	<script type="text/javascript" src="js/eventCompare.js"></script>
</body>
</html>