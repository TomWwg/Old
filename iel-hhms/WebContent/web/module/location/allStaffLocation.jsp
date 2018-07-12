<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
 <link rel="stylesheet" href="../../../js/jOrgChart/jquery.jOrgChart.css"/>
<!--  <link rel="stylesheet" href="../../../js/jOrgChart/custom.css"/> -->
 <script src="../../../js/jOrgChart/jquery.jOrgChart.js"></script>
</head>
<body >
	<!-- 菜单栏  -->
	<div class="breadLine displayNone">
		<ul class="breadcrumbs auto-navigation" menu-code="${menuCode}"></ul>
	</div>
	<!-- 主页面 -->
	<div class="wrapper" style="height: 100%">
		<!-- 数据列表  -->
		<div id='jOrgChart'><img src='../../../images/test.gif'></div>
		<div id="chart" class="orgChart"></div>
	</div>
	<script type="text/javascript" src="js/allStaffLocation.js"></script>
</body>
</html>