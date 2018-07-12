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
			<h3>基础信息</h3>
		</div>
		<div class="pageContent">
			<form action="" class="form-horizontal">
				<fieldset>
					<div class="control-group cols4">
						<label class="control-label">用户名：</label>
						<div class="controls">${userName}</div>
						<label class="control-label">用户状态：</label>
						<div class="controls" id="user-status-div">
							<s:if test='userInfo.status == 0'>
								<span class="ico i-ok"></span>正常
		                         </s:if>
							<s:else>
								<span class="ico i-useless"></span>停用
		                         </s:else>
						</div>
					</div>
					<div class="control-group cols4">
						<label class="control-label">科室：</label>
						<div class="controls">${userInfo.strRev1 }</div>
						<label class="control-label">角色：</label>
						<div class="controls">${userInfo.strRev2 }</div>			
					</div>
					<div class="control-group cols4">
						<label class="control-label">手机号码：</label>
						<div class="controls">${userInfo.tel }</div>
						<label class="control-label">备注：</label>
						<div class="controls">${userInfo.remark }</div>			
					</div>
					<div class="control-group cols4">
						<label class="control-label">最后登录时间：</label>
						<div class="controls">${userInfo.lastLoginTime }</div>
						<label class="control-label">更新时间：</label>
						<div class="controls">${userInfo.updateTime }</div>
					</div>
					 
					 <div class="control-group cols4">
		                 <!-- 
		                 <label class="control-label">过期时间：</label>
		                 <div class="controls">${userInfo.expireTime }</div>
		                  -->
		                <label class="control-label">登录次数：</label>
						 <div class="controls">${userInfo.loginCounts }</div>
		            </div> 
				</fieldset>
			</form>
		</div>
	</div>


</body>
</html>
