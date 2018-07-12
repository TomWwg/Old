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
</head>
<body>
	<!-- 主页面 -->
	<div class="wrapper grid-m">
		<div class="col-main">
			<div class="main-wrap"
				style="overflow-y: inherit;">
				<!-- 查询条件  -->
				<div class="fluid filterForm">
					<form id="filterForm" action="" method="post"
						class="form-horizontal" history="true">
						<!-- <input type="hidden" id="treeId" name="queryEntity.treeId" value="1"/> -->
						<input type="hidden" id=eventTypeInput name="queryEntity.str1" /><!-- 保持和多选框一致 -->
						<input type="hidden" id=deviceTypeInput name="queryEntity.str2" /><!-- 保持和多选框一致 -->
						<ul class="formRow" style="padding: 0px">
							<li class="control-group auto-by-filter"><label
								class="control-label">设备/胸牌编号：</label>
								<div class="controls">
							    	<input type="text" name="queryEntity.str3">
								</div></li>
						   <li class="control-group auto-by-filter"><label
								class="control-label">事件类型：</label>
							  <div class="controls">
							    <select id ="eventType" multiple="multiple" onchange="eventTypeSelect()" style="width:170px">
							       <c:forEach items="${logEventTypeMap}" var="eType">
                                      <option value="${eType.key}">${eType.value}</option>
                                   </c:forEach>	     
	                            </select>
							  </div></li>
							  <li class="control-group auto-by-filter"><label
								class="control-label">设备类型：</label>
							  <div class="controls">
							    <select id ="deviceType" multiple="multiple" onchange="deviceTypeSelect()" style="width:170px">
							       <c:forEach items="${logDeviceTypeMap}" var="lType">
                                      <option value="${lType.key}">${lType.value}</option>
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
							<a class="buttonM bBlue" type="button" onclick="excelData();" ><span class="ico i-import"></span><span>导出</span></a>
						</div>
					</form>
				</div>
                <!-- 数据列表  -->
                 <div class="datatable">
			         <table id="table"> </table>
			         <div id="dataTablePage"></div>
		          </div>
			</div>
		</div>
		<!-- 左侧树 -->
<!-- 		<div class="col-sub" >
			<div class="treeview">
				<ul id="zTreea" class="ztree" style="margin: 0px 10px"></ul>
			</div>
		</div> -->
	</div>
	<script type="text/javascript" src="js/signalLog.js"></script>
</body>
</html>