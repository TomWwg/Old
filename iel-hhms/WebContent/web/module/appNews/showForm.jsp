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
			<div class="pageContent">
				<fieldset class="noLegend">
					<div class="control-group input">
						<label class="control-label"><em>*</em>新闻标题：</label>
						<div class="controls">
							<input id="name" name="entity.title" class='auto-input' type='text'
								required="true" vtype="common" value='<s:property value="entity.title"/>'>
						</div>
					</div>
					<div class="control-group">
                      <label class="control-label"><em>*</em>图片链接：</label>
                      <div class="controls">
                        <input id="name" name="entity.image" class='auto-input' type='text'
								 vtype="common" value='<s:property value="entity.image"/>'>
                      </div>
                    </div>
					<div class="control-group input">
						<label class="control-label">新闻内容：</label>
						<div class="controls">
							<textarea vtype="common" class="att-width300" name="entity.content" style="width:435px;height: 150px"><s:property value="entity.content"/></textarea>
						</div>
					</div>
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>


