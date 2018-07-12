<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<style type="text/css">
</style>
<script type="text/javascript">
           	var flag = false;
            $(function(){
           		$("#confirmPassword").focus(function() {
            		$("#confirmPwdTip").hide();
        		}).blur(function(){
           			var newpwd = $("#newPassword").val();
           			var conpwd = $("#confirmPassword").val();
           			if(newpwd.length > 0){
	           			if(newpwd != conpwd){
	           				if(conpwd.length == 0){
	           					$('#confirmPassword').trigger('blur');
	           					flag = false;
	           					return;
	           				}
	           				$("#confirmPwdTip").css("display", "inline-block");
	           				flag = false;
	           				return ;
	        			}else{
	        				flag = true;
	        			}
           			}else{
           				flag = false;
           				$('#newPassword').trigger('blur');
           				$("#confirmPwdTip").hide();
           				return ;
           			}
           		});
            	//回传到父页面的值 
            	var infos = [];
	           	$("body").data('getInfos', function(){
        			infos = [];
	       			var oldPassword = $("#oldPassword").val();
	       			var newPassword = $("#newPassword").val();
	       			var confirmPassword = $("#confirmPassword").val();
	       			if(oldPassword==''||newPassword==''||confirmPassword==''||confirmPassword!=newPassword||newPassword.length<5||confirmPassword.length<5){
	       				flag = false;
	       			}
        			infos.push(oldPassword, newPassword, confirmPassword, flag);
            	    return infos;
            	});
            	
            });  
        </script>
</head>
<body>
	<div class="wrapper">
		<div class="pageTitle">
			<h3>修改密码</h3>
		</div>
		<div class="pageContent">
			<form id="changePasswordForm" class="form-horizontal form-input"
				method="post" action="changePassword.action">
				<input type="hidden" name="userName" value="${userName}"></input> <input
					type="hidden" name="userId" value="${userInfo.id}"></input>
				<fieldset class="noLegend">
					<div class="control-group">
						<label class="control-label"><em>*</em>旧密码：</label>
						<div class="controls">
							<input id="oldPassword" name="oldPassword"
								class="auto-input valid" required="true" type="password"
								vtype="required">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><em>*</em>新密码：</label>
						<div class="controls">
							<input id="newPassword" name="newPassword"
								class="auto-input valid" type="password" required="true"
								vtype="required,minLength,maxLength" minlength="5"
								maxlength="13">
						</div>
						<div class="control-group">
							<label class="control-label"><em>*</em>确认新密码：</label>
							<div class="controls">
								<input id="confirmPassword" name="confirmPassword"
									type="password" required="true" vtype="required"
									class="auto-input valid">
								<div id="confirmPwdTip" style="margin-left: 4px; display: none;">
									<label for="confirmPassword" class="error">两次输入的密码不一致！</label>
								</div>
							</div>
						</div>
					</div>
				<!-- 	<div class="form-actions" style="display: none;">
						<a id="btn_confirm" class="buttonM bBlue"
							href="javascript:void(0);" type="submit">确定</a> <a
							id="btn_cancel" class="buttonM bDefault"
							href="javascript:void(0);">取消</a>
					</div> -->
				</fieldset>
			</form>

		</div>
	</div>
</body>
</html>
