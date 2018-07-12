<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>手卫生依从性管理系统</title>
<%@ include file="/common/commons-index.jsp"%>
<link rel="stylesheet" type="text/css" href="css/menu.css">
<script src="js/menu.js"></script>
<script type="text/javascript">
	if (self != top) {
		top.document.getElementById("mainFrame").src = '${bodyUrl}';
	}
	$(function() {
		 $.MetroMenu({
	            backicon: "images/back.png",
	            animation: "fadeInDown",
	            position: "top",
	            color1: "#369bfd",
	            color2: "#25c4eb",
	            withtooltip: false,
	            closeonclick: false,
	            escclose: false,
	            items: [
	            <s:iterator value="superMenuList" status="st" id="item">
	            {
	                name: "<s:property value="name" />",
	                icon: "<s:property value="img" />",
	                link: "<s:property value="href" />"
	            }
		            <s:if test="#st.Last==false"> 
		            ,
		            </s:if>
	            </s:iterator>
	            ]
	        });
		 
			$("#leftPane .menu_body:eq(0)").show();
			$("#leftPane p.menu_head").click(function(){
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
				$("div.color_env_light").remove();
				$(".color_env_light_bg").removeClass("color_env_light_bg");
				$(this).prepend('<div class="color_env_light">&nbsp;</div>');
				$(this).addClass("color_env_light_bg");
			});
			/* $("#MenuOption0").click(); */
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
		    <div class="wrapper grid-m">
				<div class="col-main">
					<div class="main-wrap">
						<iframe id="mainFrame" name="mainFrame" frameborder="0" class="autoIframe"></iframe>
					</div>
				</div>
			</div>
			
			<%-- <div class="wrapper grid-s6m0">
				<div class="col-main">
					<div class="main-wrap">
						<iframe id="mainFrame" name="mainFrame" frameborder="0" class="autoIframe"></iframe>
					</div>
				</div>

				<div class="col-sub" style="background:#3e5b76;">
					<div id="leftPane" class="menu_list floatL">
						
						<s:iterator value="superMenuList" var="item" status="sta">
						<div id="menuContent_<s:property value="#item.id"/>" style="display: none">
							<s:iterator value="menuList" var="it" status="st">
							<s:if test='%{#it.parentId == #item.id}'>
							<p class="menu_head current">
								<a class="li_menu_ent"><img src="${ctx}/<s:property value="#it.img"/>"></a>
								<s:property value="#it.name"/>
								<a class="li_down"><img src="${ctx}/images/icon_menu_arrow_right.png"></a>
							</p>
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
			</div> --%>
		</div>
		<%@ include file="/common/commons-footer.jsp"%>
	</div>
</body>
</html>
