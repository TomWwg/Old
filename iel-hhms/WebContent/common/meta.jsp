<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:if test="${param._debug=='true'}">
	<s:set name="debug" value="true" scope="session" />
</c:if>
<s:if test="#session.debug==true">
	<s:set name="debug" value="'true'" scope="request" />
</s:if>
<s:else>
	<s:set name="debug" value="'true'" scope="request" />
</s:else>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<script type="text/javascript">
    var pt = {
        ctx: "${ctx}",
        debug: '${debug}'=='true'?true:false,
        lang: 'zh_cn',
        history: "${param['_history']}" == "true" ? true : false
    };
</script>
<title><s:text name="title" /></title>
<link href="${ctx}/fav.ico" rel="shortcut icon" />
<link href="${ctx}/baseui/themes/default/css/reset.css" rel="stylesheet" />
<link href="${ctx}/baseui/themes/default/css/grids.css" rel="stylesheet" />
<link href="${ctx}/baseui/themes/default/css/base.css" rel="stylesheet" />
<link href="${ctx}/baseui/themes/default/css/ui.grid.css"
	rel="stylesheet" />
<link href="${ctx}/baseui/js/plugins/popup/skin/default/pop.css"
	rel="stylesheet" />
<!--[if lt IE 9]>
    <link href="${ctx}/baseui/themes/default/css/ie.css" rel="stylesheet"/>
<![endif]-->
<script type="text/javascript"
	src="${ctx}/baseui/js/language/messages_zh_cn.js"></script>
<script src="${ctx}/baseui/js/plugins/jquery.js"></script>
<script src="${ctx}/baseui/js/plugins/bootstrap.js"></script>
<script src="${ctx}/baseui/js/plugins/ui/jquery.bgiframe.js"></script>
<script src="${ctx}/baseui/js/plugins/ui/jquery.placeholder.js"></script>
<script src="${ctx}/baseui/js/plugins/ui/jquery.tipsy.js"></script>
<script src="${ctx}/baseui/js/plugins/ztree/jquery.ztree.core.js"></script>
<script src="${ctx}/baseui/js/plugins/ztree/jquery.ztree.excheck.js"></script>
<script src="${ctx}/baseui/js/plugins/underscore.js"></script>
<script src="${ctx}/baseui/js/jquery-override.js"></script>
<script src="${ctx}/baseui/js/jquery.ztree-override.js"></script>
<%String style="default";%>
