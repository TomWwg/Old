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
<style type="text/css">
.pointRed {
	color: #E32F48;
}

.pointRed i {
	background: #E32F48;
}
</style>
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
				style="overflow-y: inherit;">
				<!-- 工具栏  -->
				<div class="btn-toolbar" style="height: 36px; position: relative;">
					<div class=btn-padding>
						<a class="buttonS" style="border:1px solid #369dfd;" id="add_btn" href="javascript:void(0);"><span
							class="ico i-add" ></span><span>添加</span></a> <a class="buttonS" style="border:1px solid #369dfd;"
							id="del_btn" href="javascript:void(0);"><span
							class="ico i-del"></span><span>删除</span></a> 
					     <a class="buttonS" style="border:1px solid #369dfd;" id="enabled-btn" href="javascript:void(0);"><span
					       class="ico i-open"></span><span>启用</span></a>
						 <a class="buttonS" style="border:1px solid #369dfd;" id="disabled-btn" href="javascript:void(0);"><span
					        class="ico i-closed"></span><span>停用</span></a> 
					     <a class="buttonS" style="border:1px solid #369dfd;" id="sysc-btn" href="javascript:void(0);"><span
					        class="ico i-time-compare"></span><span>服务器校时</span></a> 
					</div>
				</div>
				<!-- 查询条件  -->
				<div class="fluid filterForm">
					<form id="filterForm" action="" method="post"
						class="form-horizontal" history="true">
						<input type="hidden" id="treeId" name="queryEntity.treeId" value="1"/>
						<input type="hidden" id="userRoleInput" name="queryEntity.str2" value='<s:property value="queryEntity.str2"/>'/>
						<ul class="formRow" style="padding: 0px">
							<li class="control-group auto-by-filter"><label
								class="control-label">用户名：</label>
								<div class="controls">
									<input type="text" name="queryEntity.str1">
								</div></li>
							<li class="control-group auto-by-filter"><label
								class="control-label">角色：</label>
								<div class="controls">
									<select id="userRole" multiple="multiple" onchange="userRoleSelect()">
										<c:forEach items="${userRoleList}" var="userRole" >
											<option value="${userRole.id}">${userRole.name}</option>
										</c:forEach>
									</select>
								</div></li>
							<li class="control-group auto-by-filter"><label
				            	class="control-label">用户状态：</label>
					         <div class="controls">
						        <select name="queryEntity.int1">
						        	<option value="" selected="true">请选择</option>
						        	<option value="0">正常</option>
							        <option value="1">停用</option>
							        <option value="2">过期</option>
					         	</select>
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
		           	<table id="table"></table>
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
	<script type="text/javascript" src="js/userList.js"></script>
</body>
</html>