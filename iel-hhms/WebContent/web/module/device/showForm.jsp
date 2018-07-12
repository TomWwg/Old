<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<style type="text/css">
</style>
</head>
<body>
	<div class="pageContent">
		<form id="showForm" action="" method="post" class="form-horizontal form-input">
		    <input type="hidden" id="id" name="entity.id"
				value='<s:property value="entity.id"/>'>
			<input type="hidden" id=groupTreeId name="groupTreeId"
				value='<s:property value="groupTreeId"/>'>
			<input type="hidden" id="departId" name="entity.departId"
				value='<s:property value="entity.departId"/>'>
			<input type="hidden" id="roomId" name="entity.roomId"
				value='<s:property value="entity.roomId"/>'>
			<input type="hidden" id="bedId" name="entity.bedId"
				value='<s:property value="entity.bedId"/>'>
			<input type="hidden" id="staffId" name="entity.staffId"
				value='<s:property value="entity.staffId"/>'>
			<input type="hidden" id="deviceStatus" name="entity.deviceStatus"
				value='<s:property value="entity.deviceStatus"/>'>
			<div class="pageContent">
				<fieldset class="noLegend">
				     <s:if test="%{typeLevel==1}">
            		   <div class="control-group">
						  <label class="control-label"><em>*</em>人员姓名：</label>
						  <div class="controls">
							  <s:select id='staffId' name='entity.type' list='typeList'
				                 	listKey='value' listValue='name' required="true" >
				              </s:select>
						  </div>
					   </div>
					   <div class="control-group input">
						 <label class="control-label"><em>*</em>胸牌编号(6位)：</label>
						 <div class="controls">
							<input id="no" name="entity.no" class='auto-input' type='text' maxLength=6 minLength=6
								required="true" vtype="common" value='<s:property value="entity.no"/>'>
						 </div>
					  </div>
            	     </s:if><s:else>
            		   <div class="control-group">
						  <label class="control-label"><em>*</em>类型：</label>
						  <div class="controls">
							  <s:select id='type' name='entity.type' list='typeList'
				                 	listKey='value' listValue='name' >
				              </s:select>
						  </div>
					   </div>
					   <div class="control-group input">
						 <label class="control-label"><em>*</em>设备编号(4位)：</label>
						 <div class="controls">
							<input id="no" name="entity.no" class='auto-input' type='text' maxLength=4 minLength=4
								required="true" vtype="common" value='<s:property value="entity.no"/>'>
						 </div>
					  </div>
            	    </s:else>
            	    <%--设备名称基本没什么用 --%>
					<div class="control-group input" style="display:none">
						<label class="control-label"><em>*</em>名称：</label>
						<div class="controls">
							<input id="name" name="entity.name" class='auto-input' type='text'
								required="true" vtype="common" value='缺省'>
								<%-- required="true" vtype="common" value='<s:property value="entity.name"/>'> --%>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label">备注：</label>
						<div class="controls">
							<textarea vtype="common" class="att-width300" name="entity.remark" style="width:435px;;height: 150px""><s:property value="entity.remark"/></textarea>
						</div>
					</div>
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>


