<!DOCTYPE html>
<%@page import="com.sys.core.util.StringUtils"%>
<%@page import="com.gk.essh.util.CookieUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta charset="utf-8" />
<title>智控手卫生物联网管理系统</title>

<link href="/fav.ico" rel="shortcut icon" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/baseui/themes/default/css/grids.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/baseui/themes/default/css/ui.grid.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/baseui/themes/default/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/baseui/themes/default/css/base.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/baseui/themes/default/css/login.css"></link>
<link rel="stylesheet" type="text/css"
	href="${ctx}/baseui/js/plugins/popup/skin/default/pop.css" />
<%String style =StringUtils.defaultIfBlank(CookieUtil.getCookieValue(request, "GK_COOKIE_STYLE"), "default");
if(!StringUtils.equals(style, "default")){
%>
<link href="${ctx}/baseui/themes/<%=style %>/css/base.css"
	rel="stylesheet" />
<link href="${ctx}/baseui/themes/<%=style %>/css/login.css"
	rel="stylesheet" />
<%} %>
<style type="text/css">
.login-wrapper {
	margin-top: -315px;
	position: absolute;
	top: 50%;
	width: 100%;
	overflow: hidden;
}

#footer{
	height:31px;
	display:block;
	font-size:14px;
}
 body {
	   background: url(../images/log-background.png);
	   overflow: hidden;
	} 
</style>
</head>

<body  style="background-repeat:no-repeat;background-position:0;">
	<div class="wrapper">
		<div class="login-wrapper">
			 <div class="logo" align="left" style="margin-left:300px;"> <!-- <h1 style="color:bule"> 易同全感控监控系统 </h1>  --> <img src="../images/icon_logo2.png" /> </div>
			<div class="login">
				<div class="">
					<div class="login-banner" ></div>
					<div class="login-panel" style="background:#FFFFF0">
						<h3>
							<span>欢迎登录</span>
						</h3>
						<div class="login-form">
							<div id="login_msg">
								<i></i><span></span>
							</div>
							<div class="login-input">
								<i class="login-icon"></i>
								<p class="placeholder">请输入用户名</p>
								<input id="username" class="input-text" maxlength="64"
									name="username" type="text">
							</div>
							<div class="login-input">
								<i class="secret-icon"></i>
								<p class="placeholder">请输入密码</p>
								<input id="password" class="input-text" maxlength="64"
									name="password" type="password" >
							</div>
							<div>
								<a onclick="login()" href="javascript:void(0);"
									class="btn login_btn" style="background:#369bfd">登录</a>
							</div>
							
							<div style="margin-top: 15px;">
							    <input type="checkbox" id="rememberUser"/><span style="color:blue">记住账号</span>
								<a style="float: right;" onclick="forgetPassword()" href="javascript:void(0);" style="cursor:pointer;font-size:12px;">
								<span style="color:red">找回密码</span></a>
						   </div>
						</div>
					</div>
				</div>
			</div>
			
			<!-- <div class="footer">
				<div class="copyright" style="font-size:16px;color:#FFFFF0">
					中电海康(<a href="http://www.cethik.com" target="_blank">http://www.cethik.com</a>)
				</div>
			</div> -->
		</div>
		<%@ include file="/common/commons-footer.jsp"%>
	</div>
	
	<script type="text/javascript" src="${ctx}/baseui/js/plugins/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/baseui/js/plugins/base.js"></script>
	<script type="text/javascript"
		src="${ctx}/baseui/js/language/messages_zh_cn.js"></script>
	<script type="text/javascript"
		src="${ctx}/baseui/js/plugins/underscore.js"></script>
	<script type="text/javascript"
		src="${ctx}/baseui/js/plugins/ui/jquery.placeholder.js"></script>
	<script type="text/javascript"
		src="${ctx}/baseui/js/plugins/ui/jquery.breadcrumbs.js"></script>
	<script type="text/javascript"
		src="${ctx}/baseui/js/jquery-override.js"></script>
	<script type="text/javascript"
		src="${ctx}/baseui/js/plugins/jquery-ui.js"></script>
	<script type="text/javascript"
		src="${ctx}/baseui/js/jquery-ui-override.js"></script>
	<script type="text/javascript"
		src="${ctx}/baseui/js/plugins/popup/pop.js"></script>
	<script type="text/javascript">

//防止页面被嵌套在Iframe里
window.onload = function() {
	if (window != top) {
		top.location.href = location.href;
	}
}

//其他页面跳转到首页
var url = document.location.href;
var tmpStr = url.substring(url.indexOf('://') + 3);
var ip = tmpStr.substring(0, tmpStr.indexOf('/'));
tmpStr = tmpStr.substring(tmpStr.indexOf('/'));
if (tmpStr != "${ctx}/web/login.jsp") {
	top.document.location.href = "${ctx}/web/login.jsp";
}

$(document).ready(function() {
	//检测license过期时间 start
	$.post('${ctx}/web/module/license/getVersion.action', function(data){
		if (data.success == false) {
			jSticky("<p>授权证书无效!<p/><a href='${ctx}/web/licenseUpload.jsp' target='_top' class='license-upload textRed' style='text-decoration:underline;'><h6>点击上传license文件</h6></a>", "标题", {position:"top-center",type:"nNote nWarning",autoclose:false});
	    	$("input").attr("disabled", true);
	    	$(".login_btn").attr("disabled", true);
		} else {
		    var expireDateDay = data.result.expireDateDay;
			if( parseInt(expireDateDay) <30 && parseInt(expireDateDay) > 0){
				if(data.msg == '2'){
					jSticky("您现在处于试用期，还剩"+expireDateDay+"天，请联系管理员恢复license", "标题", {position:"top-center",type:"nNote nWarning",autoclose:false});
				}else{
			    	jSticky("<p>您的license在"+expireDateDay+"天后过期!<p/><a href='${ctx}/web/licenseUpload.jsp' target='_top' class='license-upload' style='text-decoration:underline;'>更新License文件</a>", "标题", {position:"top-center",type:"nNote nFailure",autoclose:false});
				}
			} else if(parseInt(expireDateDay) <= 0){
		    	jSticky("<p>您的授权文件已经过期!</p><a href='${ctx}/web/licenseUpload.jsp' target='_top' class='license-upload' style='text-decoration:underline;'>更新License文件</a>", "标题", {position:"top-center",type:"nNote nWarning",autoclose:false});
		    	$("input").attr("disabled", true);
		    	$(".login_btn").attr("disabled", true);
			} else if (!expireDateDay){	//未上传证书 
				jSticky("<p>授权证书无效!<p/><a href='${ctx}/web/licenseUpload.jsp' target='_top' class='license-upload textRed' style='text-decoration:underline;'><h6>点击上传license文件</h6></a>", "标题", {position:"top-center",type:"nNote nWarning",autoclose:false});
		    	$("input").attr("disabled", true);
		    	$(".login_btn").attr("disabled", true);
			}
		}
	}, "json");
	//检测license过期时间 end

	/***************记住账号的功能******************/
	if ($.browser.msie) {//判断IE内核
	  $('input:checkbox').click(function () {
		  this.blur();  
		  this.focus();
		});  
	 };
	 $("#rememberUser").change(function() {
		 if($("#rememberUser").attr('checked')){
			 setCookie("userName",$("#username").val(),30);
		 }else{
			 delCookie("userName");
		 }
	 });
	 var userName = getCookie("userName");
	 if(userName!=null){
		 $("#username").attr("value",userName);
		 $("#rememberUser").attr("checked",'true');//非空就选中显示记住密码
	 }
	 /***************记住账号的功能******************/
	var errorCode = <%=request.getParameter("errorCode")%>;
	if (errorCode == -3) {
		$('#login_msg').css("visibility", "visible");
		$('#login_msg span').html("用户未登录或登录过期");
	}
	$('input').focus(function() {
				$(this).addClass('active').prev('.placeholder').hide();
			}).blur(function() {
				$(this).removeClass('active').val() == "" ? $(this).prev('.placeholder').show() : $(this).prev('.placeholder').hide();
			});
	$('.placeholder').hover(function() {
			$(this).next('input').addClass('hover');
		}, function() {
			$(this).next('input').removeClass('hover');
		}).click(function() {
			$(this).hide().next('input').trigger('focus');
		});
	if ($('#username').val() == '') {
		$('#username').trigger('focus');
	} else {
		$('#username').trigger('focus');
		$('#password').trigger('focus');
	}
	$("input").keydown(function(event) {
		if (event.which == 13) { // 13等于回车键(Enter)
			login();
		}
	});
});

		///设置cookie   
		function setCookie(NameOfCookie, value, expiredays) {
			var ExpireDate = new Date();
			ExpireDate.setTime(ExpireDate.getTime()
					+ (expiredays * 24 * 3600 * 1000));
			document.cookie = NameOfCookie
					+ "="
					+ escape(value)
					+ ((expiredays == null) ? "" : "; expires="
							+ ExpireDate.toGMTString());
		}
		///获取cookie值   
		function getCookie(NameOfCookie) {
			if (document.cookie.length > 0) {
				begin = document.cookie.indexOf(NameOfCookie + "=");
				if (begin != -1) {
					begin += NameOfCookie.length + 1;//cookie值的初始位置   
					end = document.cookie.indexOf(";", begin);//结束位置   
					if (end == -1)
						end = document.cookie.length;//没有;则end为字符串结束位置   
					return unescape(document.cookie.substring(begin, end));
				}
			}
			return null;
		}

		///删除cookie   
		function delCookie(NameOfCookie) {
			if (getCookie(NameOfCookie)) {
				document.cookie = NameOfCookie + "="
						+ "; expires=Thu, 01-Jan-70 00:00:01 GMT";
			}//如果设置了则将过期时间调到过去的时间;
		}

		//登陆
		function login() {
			if ($('#username').val() == '') {
				$('#username').addClass('error');
				$('#login_msg').css("visibility", "visible");
				$('#login_msg span').html("用户名不能为空");
				return;
			} else {
				$('#username').removeClass('error');
			}
			if ($('#password').val() == '') {
				$('#password').addClass('error');
				$('#login_msg').css("visibility", "visible");
				$('#login_msg span').html("密码不能为空");
				return;
			} else {
				$('#password').removeClass('error');
			}
			var url = document.location.href;
			var ip = url.substring(url.indexOf('://') + 3);
			ip = ip.substring(0, ip.indexOf('/'));
			$.post("login.action", {
				"userVo.userName" : $("#username").val(),
				"userVo.password" : $("#password").val(),
				"userVo.ip" : ip
			}, function(data) {
				if (data.errorCode != null) {
					$('#username').addClass('error');
					$('#login_msg').css("visibility", "visible");
					$('#login_msg span').html(data.actionErrors);
				} else {
					location.href = "index.action";
				}
			}, "json");
		}
		
		//忘记密码
		function forgetPassword(){
			//window.location.href='forgetPassword.jsp';
			alert("请联系系统管理员，重置密码！");
		}
	</script>
</body>
</html>

