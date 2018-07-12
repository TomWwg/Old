<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>智控手卫生依从性管理系统</title>
<%@ include file="/common/commons-index.jsp"%>
<link rel="stylesheet" type="text/css" href="css/menu.css">
<script type="text/javascript">
	if (self != top) {
		top.document.getElementById("mainFrame").src = '${bodyUrl}';
	}
	$(function() {
		$("#leftPane .menu_body:eq(0)").show();
		var imgDefault = $("#leftPane p.menu_head:eq(0)").children(".li_down").children("img");
		imgDefault.attr("src","${ctx}/images/icon_menu_arrow_down.png");
		//$("#leftPane .menu_body:eq(0)").prev(p.menu_head).children(".li_down").children("img").attr("src","${ctx}/images/icon_menu_arrow_down.png");
		$("#leftPane p.menu_head").click(function(){
			$("#leftPane p.menu_head").each(function(i,domEle){
				console.log('i：',i); 
				console.log('domEle：',$(this)); 
				var isHidden = $(this).next("div.menu_body").is(':hidden');
				var img = $(this).children(".li_down").children("img");
				if(!isHidden){
					//console.log('Not** Hidden：',i); 
					
					img.attr("src","${ctx}/images/icon_menu_arrow_right.png");
				}
			}); 
			var img = $(this).children(".li_down").children("img");
			var isHidden = $(this).next("div.menu_body").is(':hidden');
			$(this).next("div.menu_body").slideToggle(300).siblings("div.menu_body").slideUp("slow");
			
			if(isHidden){
				img.attr("src","${ctx}/images/icon_menu_arrow_down.png");
			}else{
				img.attr("src","${ctx}/images/icon_menu_arrow_right.png");
			}
			
			
			
		});
		
		$("a[target='mainFrame']").click(function(){
			/* $(".menu_body a").each(function(){
				$(this).css("color","#ffffff");
			}); */
			$(".menu_body a").css("color","#116F93");
			$(".menu_body a").css("background","#F5F5F5");
			$("div.color_env_light").remove();
			$(".color_env_light_bg").removeClass("color_env_light_bg");
			$(this).css("color","#ffffff");
			$(this).prepend('<div class="color_env_light">&nbsp;</div>');
			$(this).addClass("color_env_light_bg");
			$(this).css("background","#0776A0");
			
			//console.log('this is ：',$(this)); 
		});
		 $("#mainFrame").attr("src",$("#menuContent_1 div.menu_body a").first().attr("href"));//默认打开页面
		 //$("#mainFrame").attr("src","module/washHand/todayEventList.action");//默认打开页面
		 $("#menuContent_1 div.menu_body a").first().click();  //默认选中
		 //$("a[href='module/washHand/todayEventList.action']").click();
		 //$("#menuContent_1 div.menu_body a[href='module/washHand/todayEventList.action']").click();//默认选中
		 
	});
	
	
</script>

</head>
<body class="hasCopyright" style="overflow: -Scroll; overflow-y: hidden; padding: 0px;">
	<div id="wrapper">
		<!-- Top begins -->
		<%@ include file="/common/commons-menu.jsp"%>
		<!-- Top ends -->
		<!-- Content begins -->
		<div id="content" class="layout">
			<div class="wrapper grid-s6m0">
				<div class="col-main">
					<div class="main-wrap">
						<iframe id="mainFrame" name="mainFrame" frameborder="0" class="autoIframe"></iframe>
					</div>
				</div>
				<div class="col-sub" style="background:#F5F5F5;">
					<div id="leftPane" class="menu_list floatL">
						<s:iterator value="superMenuList" var="item" status="sta">
						<div id="menuContent_<s:property value="#item.id"/>">
							<s:iterator value="menuList" var="it" status="st">
							<s:if test='%{#it.parentId == #item.id}'>
							<p class="menu_head current">  <!-- 24*24图标 -->
								<a class="li_menu_ent"><img src="${ctx}/<s:property value="#it.img"/>"></a>
								<s:property value="#it.name" />			
								<a class="li_down"><img src="${ctx}/images/icon_menu_arrow_right.png"></a>	
								
							</p>
							<!--  <span ><a> 12134 <s:property value="#it.id"/></a></span> -->
							<div style="display: none" class="menu_body">
								<s:iterator value="menuList" status="st" id="i">
								<s:if test='%{#i.parentId == #it.id}'>
								<a href="<s:property value="#i.href"/>" target="mainFrame"><s:property value="#i.name"/></a>
								</s:if>
								</s:iterator>
							</div>
							</s:if>
							</s:iterator>
						</div>
						</s:iterator>
					</div>
				</div>
			</div>
		</div>
		<%@ include file="/common/commons-footer.jsp"%>
	</div>
</body>
</html>
