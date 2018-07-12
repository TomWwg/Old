<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">

	$(function() {
		
		$('.userNav').xBreadcrumbs();
		if ($.fn.bgiframe) {
			$('.userNav .dropdown-menu').bgiframe();
		};
		//显示个人基本信息弹框 
		$("#profile").click(function(){
	   		$.dialog({
			   //autoOpen: false,
	           width: 800,
	           height: 450,
	           draggable: true,
	           maybeRefresh: true,
	           id: 'profileInfo',
	           title: '个人中心：',
	           iframeSrc: '${ctx}/web/module/user/personalCenter.action?userName='+'${userName}' /* ?groupInfo.parentId= +selectedNode.id */,
	           buttons: [{
	               text: '确定',
	               'class': 'bPrimary',
	               "id": 'profile_btn_ok',
	               click: function(dialog, callback){
	       				dialog.close();
	   			   }
	          	}, {
	               text: '取消',
	               click: function(dialog, callback, el){
	               		dialog.close();
	               }
	      	    }]
			});
		});
		//修改密码的弹框
		$("#changePassword").click(function(){
	   		$.dialog({
				//autoOpen: false,
	           id: 'passwordInfo',
	           title: '修改密码：',
	           width: 580,
	           height: 360,
	           draggable: true,
	           maybeRefresh: true,
	           iframeSrc: '${ctx}/web/module/user/changePasswordUI.action?userName='+'${userName}',
	           buttons: [{
	               text: '确定',
	               'class': 'bPrimary',
	               "id": 'changePassword_btn_ok',
	               click: function(dialog, callback){
	                   	var frame = top.frames["passwordInfo"];//父页面获取
	             		$.ajax({
	                        cache: true,
	                        type: "POST",
	                        url: "module/user/changePassword.action",
	                        data: frame.$('#changePasswordForm').serialize(),// 你的formid
	                        dataType: "json",
	                        async: false,
	                        beforeSend : function(){
	       						return frame.$("#changePasswordForm").validate() && frame.flag;
	       					},
	                        success: function(data){
	                        	if(data.success == true){
	                       			dialog.close();
	                       			jAlert("密码修改成功！", '提示', 'attention');
	                        	}else{
	                       			jAlert("密码修改失败："+data.message, '提示', 'attention');
	                        	}
	                        },
	                        error: function(data){
                        		jAlert("密码修改失败！"+data.message, '提示', 'attention');
	                        }
	                    });
	   			   }
	          	}, {
	               text: '取消',
	               click: function(dialog, callback, el){
	               	dialog.close();
	               }
	      	    }]
			});
		});
		//add yxnne
		//系统版本信息
		$("#versionInfo").click(function(){
			console.log("versionInfo click");			
			 $( "#sys_version_info" ).dialog({
			      resizable: false,
			      height:240,
			      modal: true,
				  buttons: [{
					 text: '确定',
		             'class': 'bPrimary',
		             click:function() {
				        	$("#sys_version_info").dialog().dialog("close");
				     }
				  }]
			    });
			
			
			
			
		});
	});

	
	function DelCookie(name){
		var exp = new Date();
		exp.setTime (exp.getTime() - 1);
		var cval = GetCookie (name);
		document.cookie = name + "=" + cval + "; expires="+ exp.toGMTString();
	}
	
	function GetCookie(name){
		var arg = name + "=";
		var alen = arg.length;
		var clen = document.cookie.length;
		var i = 0;
		while (i < clen){
			var j = i + alen;
			if (document.cookie.substring(i, j) == arg)
				return GetCookieVal (j);
			i = document.cookie.indexOf(" ", i) + 1;
			if (i == 0) break;
		}
		return null;
	}
	
	function logout() {
		jConfirm('确认要退出当前系统？', '退出系统', function(r) {
			if (r) {
				DelCookie("GK_COOKIE");
				$.post("${ctx}/web/logout.action"); //cas会主动来调用门户退出
				window.location.href = "${ctx}/web/login.jsp";
			}
		});
	}
	function goHome() {
		window.location.href = pt.ctx+"/web/index.action" ;
	}
	function goSystem() {
		window.location.href = pt.ctx+"/web/indexHome.action" ;
	}
		
	
</script>
<!-- add by yxnne -->
<!-- 版本信息的内容 -->
<div id="sys_version_info" title="版本信息">
 <h5>版本v2.0.4,本次更新的内容有：</h5><br/>
  &nbsp;&nbsp;&nbsp;1.统计相关页面布局和功能上一些调整；<br/>
  &nbsp;&nbsp;&nbsp;2.优化导出表格功能；<br/>
  &nbsp;&nbsp;&nbsp;3.对已知问题的修复；<br/>
</div>
<!-- end add yxnne -->
<div id="top" style="background-color: #14B6F6;">
	<div class="leftNav">
			 <img src="../images/icon_topShow.png" style="float: left;height:30;width:350px;"
					alt="" 易同全感控监控系统> 
		 <%-- <h2>
			<a href="${ctx}/web/indexHome.action">易同全感控监控系统</a>
		</h2>  --%>
	</div>
	<div class="floatR" style="position: relative;">
		<ul class="userNav">
			<li class="uName"><a href="javascript:void(0);"
				class="nav_user nav_icon"><span style="width: auto;font-size:16px;]">${userName }</span></a>
				<ul>
					<li id="profile" class="i-profile"><a
						href="javascript:void(0);" onclick="">
						<img src='${ctx}/images/icon_info.png'>
						</a></li>
					<li id="changePassword" class="i-password"><a
						href="javascript:void(0);" onclick=""><img src='${ctx}/images/icon_modifyPassword.png'></a></li>
					<!-- yxnne 增加版本信息的菜单  -->
					<li id="versionInfo"  class="i_versionInfo"><a href="javascript:void(0);"
						onclick=""><img src='${ctx}/images/icon_showVersionInfo.png'></a></li>
					<!-- end yxnne -->
					<li class="i-exit"><a href="javascript:void(0);"
						onclick="logout()"><img src='${ctx}/images/icon_outLogin.png'></a></li>
				</ul></li>
		</ul>
	</div>
	<div class="floatR" id="navThirdLink">
			<ul >
			   <li >
			     <a  style="vertical-align:middle;" href="javascript:void(0);" data-position = "top" onclick="goSystem()"><img
					src='${ctx}/images/icon_goSystem.png'></a>
			   </li>
			   <li>  <!-- 24*24图标显示 -->
			     <a  style="vertical-align:middle;" href="javascript:void(0);" data-position = "top" onclick="goHome()"><img
					src='${ctx}/images/icon_goHome.png'></a>
			   </li>
            </ul>
		</div>

</div>

