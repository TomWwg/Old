<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<script src="../../../js/pdf/pdfobject.js"></script>
<script src="../../../js/swf/swfobject.js"></script>
</head>
<body style="overflow: hidden">
	
	<!-- 菜单栏  -->
	<div class="breadLine displayNone">
		<ul class="breadcrumbs auto-navigation" menu-code="${menuCode}"></ul>
	</div>
	<!-- <h1 style="text-align:center">暂无可用产品手册</h1> -->
	<!-- 主页面  -->
	<div  id ="swf" class="wrapper" style="height: 100%">
		<!-- 数据列表   -->
	</div>
<script type="text/javascript" >
 	window.onload = function (){
	 	swfobject.embedSWF("../../../swf-resource/washHand.swf", "swf", "100%", "100%", "9.0.0", "expressInstall.swf");
		//PDFObject.embed("../../../pdf-resource/washhand.pdf", "#swf");
	}
</script>
</body>
</html>