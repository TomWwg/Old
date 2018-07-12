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
	<div class="wrapper">
		<div class="pageContent">
			<form action="" class="form-horizontal form-input">
			    <input type="hidden" id="id" name="entity.id" value='<s:property value="entity.id"/>'>
			    <input type="hidden" id="ip" name="entity.ip" value='<s:property value="entity.ip"/>'>
				<fieldset>
					<legend>读数据</legend> 
					 <div class="control-group cols4" >
					    <label class="control-label"><em>*</em>编号(4位)：</label>
					    <div class="controls">
						   <input id="noD" disabled="disabled" name="entity.no"  value='<s:property value="entity.no"/>' class='auto-input'  type='text'>
					    </div>
					    <div class="controls" style="text-align:center;">
						     <a class="buttonS bBlue" type="button" id="data_get_no">获取编号</a>	
						</div>	
					</div>
					<div class="control-group cols4">
						<label class="control-label"><em>*</em>医院编号(6位)：</label>
						<div class="controls">
						  <input id="hospitalNoD" disabled="disabled" name="entity.hospitalNo"  value='<s:property value="entity.hospitalNo"/>' class='auto-input'  type='text'>
						 </div>
                        <label class="control-label"><em>*</em>医院科室编号(4位)：</label>
                        <div class="controls">
                          <input id="departNoD" disabled="disabled" name="entity.departNo"  value='<s:property value="entity.departNo"/>' class='auto-input'  type='text'>
                        </div>	
					</div>
					<div class="control-group cols4">
					 <label class="control-label"><em>*</em>邮编号(6位)：</label>
					    <div class="controls">
					      <input id="postCodeD" disabled="disabled" name="entity.postCode"  value='<s:property value="entity.postCode"/>' class='auto-input'  type='text'>
					    </div>
					    <div class="controls" style="text-align:center;">
						     <a class="buttonS bBlue" type="button" id="data_get_code">获取编码</a>	
						</div>
					</div>
				</fieldset>
				<fieldset>
					<legend>写数据</legend> 
					<div class="control-group cols4">
						<label class="control-label">AP编号：</label>
						<div class="controls">
						<input maxLength=4 minLength=4 id="no" name="entity.no" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.no"/>'>
                        </div>	
                         <div class="controls" style="text-align:center;">
						     <a class="buttonS bBlue" type="button" id="data_set_no">设置编号</a>	
						</div>	
					</div>
					<div class="control-group cols4">
						<label class="control-label">医院编号：</label>
						<div class="controls">
						 <input maxLength=6 minLength=6 id="hospitalNo" name="entity.hospitalNo" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.hospitalNo"/>'>
                        </div>	
                        <label class="control-label">医院科室编号：</label>
						<div class="controls">
						 <input maxLength=4 minLength=4 id="departNo" name="entity.departNo" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.departNo"/>'>
                        </div>		
					</div>
					<div class="control-group cols4">
						<label class="control-label">邮编(6位)：</label>
						<div class="controls">
						<input maxLength=6 minLength=6 id="postCode" name="entity.postCode" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.postCode"/>'>
                        </div>	
                        <div class="controls" style="text-align:center;">
						     <a class="buttonS bBlue" type="button" id="data_set_code">设置编码</a>	
						</div>	
					</div>

				</fieldset>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="js/apSet.js"></script>
</body>
</html>
