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
<%-- <script type="text/javascript" src="${ctx}/js/assets/jquery.1.7.2.min.js"></script> --%>
<script type="text/javascript" src="${ctx}/js/assets/jquery-ui.js"></script> 
<script type="text/javascript" src="${ctx}/js/assets/prettify.js"></script>
<script type="text/javascript" src="${ctx}/js/assets/jquery.multiselect.js" ></script>
<script type="text/javascript" src="${ctx}/js/highcharts/highcharts.js" ></script>
<script type="text/javascript" src="${ctx}/js/highcharts/highchartsComm.js" ></script>
</head>
<body onload="prettyPrint();" class="grid withFixed">
	<!-- 菜单栏  -->
	<div class="breadLine displayNone">
		<ul class="breadcrumbs auto-navigation" menu-code="${menuCode}"></ul>
	</div>
	<!-- 主页面 -->
	<div class="wrapper" style="height: 100%">
		<!-- 查询条件  -->
		<div class="fluid filterForm">
			<form id="filterForm" action="export.action"
				method="post" class="form-horizontal" history="true">
				<input type="hidden" id="treeId" name="queryEntity.treeId" value="1"/> <input
					type="hidden" id="departInput" name="queryEntity.str1" value='<s:property value="queryEntity.str1"/>'/>
				<ul class="formRow" style="padding: 0px">
					<li class="control-group auto-by-filter"><label
						class="control-label">科室：</label>
						<div class="controls">
						    <select id ="depart" multiple="multiple" onchange="departSelect()">
							    <c:forEach items="${departList}" var="dList">
                                   <option value="${dList.id}">${dList.name}</option>
                                </c:forEach>	     
	                        </select>
						</div></li>
					<li class="control-group auto-by-filter"><label
						class="control-label">统计类型：</label>
						<div class="controls">
							<s:select id='timeType' list="#{1:'年统计',2:'月统计'}" listKey='key' listValue="value">
							</s:select>
						</div></li>	
					<li class="control-group auto-by-filter cols2"><label
						class="control-label">起止时间：</label>
						<div class="controls">
							<input id="startTime" type="text" gkui="calendar" isShowClear="false"
								maxDate="#F{$dp.$N('queryEntity.endTime',{d:0});}"
								name="queryEntity.startTime"  dateFmt="yyyy-MM"
								class="gkui-calendar Wdate datetimepicker" readonly="readonly">
							-- <input id="endTime" type="text" gkui="calendar" isShowClear="false"
								minDate="#F{$dp.$N('queryEntity.startTime',{d:0});}"
								name="queryEntity.endTime" dateFmt="yyyy-MM"
								class="gkui-calendar Wdate datetimepicker" readonly="readonly">
						</div></li>
				</ul>

				<div class=btn-set>
					<a class="buttonM bBlue" type="button" onclick="searchData();">查询</a>
					<a class="buttonM bBlue" type="button" onclick="excelData();" ><span class="ico i-import"></span><span>导出</span></a>
				</div>
			</form>
		</div>
		
			<!-- 数据列表  -->
				<div class="datatable" style="height: 80%;">
					 <table id="listTable" cellspacing="0" cellpadding="0" width="100%" class="tLight checkAll">
	    			</table>
				</div>
    </div>
	<script type="text/javascript" src="js/timeList.js"></script>
</body>
</html>