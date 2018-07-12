<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html lang="en">
<head>
<link rel="stylesheet" type="text/css"
	href="../css/home/lib/bootstrap.css">
<link rel="stylesheet" type="text/css" href="../css/home/theme.css">
<%@ include file="/common/commons.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/home/add.css">
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
	     	<div class="block span4">
				<div class="block-body">
					<div id="chart_combo_staff" style="height: 260px;"></div>
				</div>
			</div>
			
			<div class="block span4">
				<div class="block-body">
					<div id="chart_combo_department" style="height: 260px;"></div>
				</div>
			</div>
			<div class="block span4">
				<p class="block-heading">
					感控沙龙
					<img alt="" src="../images/png_notice.png"  width="20px" height="20px" style="padding: 1px 1px 1px 3px;">
					<span class="panel-icon pull-right"> <a
						href="module/notice/list.action">更多...</a>
					</span>
				</p>
				<div class="block-body">
					<div class="datatable" style="height: 218px;">
						<table id="tableNotice" class="table">
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
		   <div class="block span8">
				<div class="block-body">
					<div id="chart_combo_alarm" style="height: 245px;"></div>
				</div>
			</div>
		   <div class="block span4">
				<div class="block-body">
					<div id="chart_combo" style="height: 245px;"></div>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="block span8">
				<div class="block-body">
					<div class="datatable" style="height: 240px;">
						<table id="table" class="table">
						</table>
					</div>
				</div>
			</div>
			<div class="block span4">
				<div class="block-body">
					<div id="chart_combo_moment" style="height: 240px;"></div>
				</div>
			</div>
			
		</div>
	</div>

	<!-- Le javascript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script type="text/javascript" src="homeInfo.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts/highchartsComm.js"></script>
	<script type="text/javascript" src="${ctx}/js/highcharts/highcharts-more.src.js"></script>
</body>
</html>