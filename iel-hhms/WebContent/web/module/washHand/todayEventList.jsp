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
				<div class="fluid filterForm" style="height: 130px">
					<form id="filterForm" action="" method="post"
						class="form-horizontal" history="true">
						<input type="hidden" id="treeId" name="queryEntity.treeId" value="1"/>
						<input type="hidden" id=eventTypeInput name="queryEntity.str1" /><!-- 保持和多选框一致 -->
						<input type="hidden" id=rfidStatusInput name="queryEntity.str2"/><!-- 保持和多选框一致 -->
						<!-- <input type="hidden" id=rfidStatusInput name="queryEntity.str2"/> -->
						<!-- <input type="hidden" id=jobTypeInput name="jobType" /> -->
						<input type="hidden" id="staffTypeInput" name="jobType" value='<s:property value="queryEntity.str3"/>'/>
						<input type="hidden" id=washHandStatusInput name="washHandStatus"/>
						
						<ul class="formRow" style="padding: 0px">
						    <li class="control-group auto-by-filter"><label
								class="control-label">员工姓名：</label>
								<div class="controls">
		                        <%-- <select id="staffId" name="queryEntity.int1">
							       <option value="-1">---全部---</option>
							       <c:forEach items="${staffList}" var="sList">
                                      <option value="${sList.id}">${sList.name}</option>
                                   </c:forEach>	
							    </select> --%>
							    	<input type="text" name="staffName"><!-- 模糊查询 -->
								</div></li>
							<li class="control-group auto-by-filter"><label
								class="control-label">事件类型：</label>
							  <div class="controls">
							    <select id ="eventType" multiple="multiple" onchange="eventTypeSelect()">
							       <c:forEach items="${eventTypeMap}" var="eType">
                                      <option value="${eType.key}">${eType.value}</option>
                                   </c:forEach>	     
	                            </select>
							  </div></li>
							  <li class="control-group auto-by-filter"><label
								class="control-label">人员类别：</label>
							  <div class="controls">
							    <select id ="staffType" multiple="multiple" onchange="staffTypeSelect()">
							       <c:forEach items="${jobTypeMap}" var="jType">
                                      <option value="${jType.key}">${jType.value}</option>
                                   </c:forEach>	     
	                            </select>
							  </div></li>
							  <!-- 
							  <li class="control-group auto-by-filter"><label
								class="control-label">人员类别：</label>
							  <div class="controls">
							     <select id ="staffType" multiple="multiple" onchange="staffTypeSelect()">
							        <c:forEach items="${jobTypeMap}" var="jType">
                                        <option value="${jType.key}">${jType.value}</option>
                                    </c:forEach>	     
	                             </select>
							  </div></li>
							  
							  
							   -->							  
							  
							  <li class="control-group auto-by-filter"><label
								class="control-label">手卫生状态：</label>
							  <div class="controls">
							    <select id ="washHandStatus" multiple="multiple" onchange="washHandStatusSelect()">
							       <c:forEach items="${washHandStatusMap}" var="wStatus">
                                      <option value="${wStatus.key}">${wStatus.value}</option>
                                   </c:forEach>	     
	                            </select>
							  </div></li>
							<%-- <li class="control-group auto-by-filter"><label
								class="control-label">胸牌状态：</label>
							  <div class="controls">
							    <select id ="rfidStatus" multiple="multiple" onchange="rfidStatusSelect()">
							       <c:forEach items="${rfidStatusMap}" var="rStatus">
                                      <option value="${rStatus.key}">${rStatus.value}</option>
                                   </c:forEach>	     
	                            </select>
								</div></li> --%>
				<!-- 注释掉起止时间选项 因为是今日数据 将startTime 和 endTime 隐藏掉--> 
							<li class="control-group auto-by-filter cols2" ><label
								class="control-label" style="display:none">起止时间：</label>
								<div class="controls">
									<input id= "startTime" type="text" gkui="calendar" showTime="true"
										maxDate="#F{$dp.$N('queryEntity.endTime',{d:0});}"
										name="queryEntity.startTime" isShowClear="false"
										class="gkui-calendar Wdate datetimepicker" readonly="readonly" 
										style="display:none">
									<input id= "endTime" type="text" gkui="calendar" showTime="true"
										minDate="#F{$dp.$N('queryEntity.startTime',{d:0});}"
										name="queryEntity.endTime" isShowClear="false"
										class="gkui-calendar Wdate datetimepicker" readonly="readonly" 
										style="display:none">
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
	<script type="text/javascript" src="js/eventList.js"></script>
</body>
</html>