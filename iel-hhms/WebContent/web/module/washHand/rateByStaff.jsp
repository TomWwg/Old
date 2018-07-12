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
						<input type="hidden" id="staffTypeInput" name="queryEntity.str1" value='<s:property value="queryEntity.str1"/>'/>
						<ul class="formRow" style="padding: 0px">
							<li class="control-group auto-by-filter"><label
								class="control-label">人员类别：</label>
							  <div class="controls">
							     <select id ="staffType" multiple="multiple" onchange="staffTypeSelect()">
							        <c:forEach items="${jobTypeMap}" var="jType">
                                        <option value="${jType.key}">${jType.value}</option>
                                    </c:forEach>	     
	                             </select>
							  </div></li>
							<li class="control-group auto-by-filter"><label
								class="control-label">员工姓名：</label>
							  <div class="controls">
							    <%-- <select id="staffName" name="queryEntity.int1">
							       <option value="-1">---全部---</option>
							    </select> --%>
							    <input type="text" name="staffName"><!-- 模糊查询 -->
								</div></li>
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
		<div class="col-sub" >
			<div class="treeview">
				<ul id="zTreea" class="ztree" style="margin: 0px 10px"></ul>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="js/rateByStaff.js"></script>
</body>
</html>