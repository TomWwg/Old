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
<script type="text/javascript" src="${ctx}/js/highcharts/exporting.js" ></script>
<style type="text/css">
     *{
        font-size:20px;
     }
  </style>
</head>
<body >
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
				<div>
					<form id="filterForm" action="" method="post" class="form-horizontal" history="true">
					   <input type="hidden" id="treeId" name="queryEntity.treeId" value="1"/>
					</form>
				</div>
				<!-- 数据列表  -->
				<div class="datatable" style="height: 40%;">
					<table id="table">
					</table>
				</div>
				<div id="chart_combo" style="height: 59%;"></div>
			</div>
		</div>
		<!-- 左侧树 -->
		<div class="col-sub" >
			<div class="treeview">
				<ul id="zTreea" class="ztree" style="margin: 0px 10px"></ul>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="js/showScreen.js"></script>
</body>
</html>