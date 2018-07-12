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
		<div class="pageTitle">
			<h3>公告信息</h3>
		</div>
		<div class="pageContent">
			<form action="" class="form-horizontal">
				<fieldset>
					<div class="control-group cols4">
						<label class="control-label">公告名称：</label>
						<div class="controls"><s:property value="entity.name"/></div>
					</div>
					<div class="control-group cols4">
						<label class="control-label">到期时间：</label>
						<div class="controls"><s:property value="formateDateYMD(entity.expireTime)"/></div>		
					</div>
					<div class="control-group cols4">
						<label class="control-label">公告内容：</label>
						<div class="controls"><s:property value="entity.remark"/></div>		
					</div>
				</fieldset>
			</form>
		</div>
	</div>

</body>
</html>



