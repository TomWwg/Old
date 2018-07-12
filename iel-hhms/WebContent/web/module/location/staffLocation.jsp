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
<body>
     <!-- 菜单栏  -->
	<div class="breadLine displayNone">
		<ul class="breadcrumbs auto-navigation" menu-code="${menuCode}"></ul>
	</div>
	<!-- 主页面 -->
	<div class="wrapper grid-s6m0">
		<div class="col-main">
			<div class="main-wrap"
				style="margin-left: 260px; overflow-y: inherit;">
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
							       <option value="-2">---请选择---</option>
							    </select>
								</div></li>
						</ul>
						<div class=btn-set>
							<a class="buttonM bBlue" type="button" id="data_filtering">查询</a>
						</div>
					</form>
				</div>
                <!-- 数据列表  -->
                 <!-- <div class="datatable">
			         <table id="table"> </table>
			         <div id="dataTablePage"></div>
		          </div> -->
		          <div id='jOrgChart'></div>
			</div>
			<div id="chart" class="orgChart"></div>
		</div>
		<!-- 左侧树 -->
		<div class="col-sub" style="width: 200px;">
			<div class="treeview">
				<ul id="zTreea" class="ztree" style="margin: 0px 10px"></ul>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="js/staffLocation.js"></script>
</body>
</html>