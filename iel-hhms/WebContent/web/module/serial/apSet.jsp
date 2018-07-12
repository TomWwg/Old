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
				<fieldset>
				    <legend>AP信息</legend> 
					<div class="control-group cols4">
						<label class="control-label">IP：</label>
						<div class="controls">
							<input id="name" name="entity.name" class='auto-input' type='text'
								required="true" vtype="ip" value='<s:property value="entity.name"/>'>
						</div>
					</div>
				</fieldset>
				<fieldset>
					<legend>读数据</legend> 
					<div class="control-group cols4">
						<label class="control-label"><em>*</em>邮编(6位)：</label>
						<input id="name" name="entity.name" class='auto-input' type='text'
								required="true" vtype="number" value='<s:property value="entity.name"/>'>
						<div class="controls">
                        </div>		
					</div>
					<div class="control-group cols4">
						<label class="control-label"><em>*</em>医院编号：</label>
						<div class="controls">
                        </div>	
                        <label class="control-label"><em>*</em>医院科室编号：</label>
						<div class="controls">
                        </div>		
					</div>
					 <div class="control-group cols4" >
					    <label class="control-label">读取时间：</label>
						<div class="controls">
                        </div>	
					    <div class="controls" style="text-align:center;">
						     <a class="buttonS bBlue" type="button" id="data_filtering">获取</a>	
						</div>	
					</div>
				</fieldset>
				<fieldset>
					<legend>写数据</legend> 
					<div class="control-group cols4">
						<label class="control-label">邮编(6位)：</label>
						<div class="controls">
                        </div>		
					</div>
					<div class="control-group cols4">
						<label class="control-label">医院编号：</label>
						<div class="controls">
                        </div>	
                        <label class="control-label">医院科室编号：</label>
						<div class="controls">
                        </div>		
					</div>
					<div class="control-group cols4" >
					    <label class="control-label">设置时间：</label>
						<div class="controls">
                        </div>	
					    <div class="controls" style="text-align:center;">
						     <a class="buttonS bBlue" type="button" id="data_filtering">设置</a>	
						</div>	
					</div>
				</fieldset>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="js/serialSet.js"></script>
</body>
</html>
