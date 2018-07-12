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
						<iframe id="mainFrame" name="mainFrame" frameborder="0" class="autoIframe" src="goHomeInfo.action"></iframe>
					</div>
				</div>
			</div>
		</div>
		<%@ include file="/common/commons-footer.jsp"%>
	</div>
</body>
</html>
