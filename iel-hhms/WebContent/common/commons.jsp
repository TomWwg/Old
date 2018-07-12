<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="meta.jsp"%>
<style type="text/css">

  /*******医院***********/
.ztree li span.button.hospital_ico_open {
/* 	background: url(${ctx}/images/icon_hospital.png);
	width: 18px;
	height: 16px; */
	background: #ffffff;
	width: 2px;
	height: 2px; 
}

.ztree li span.button.hospital_ico_close {
	background: #ffffff;
	width: 2px;
	height: 2px; 
}

.ztree li span.button.hospital_ico_docu {
	background: #ffffff;
	width: 2px;
	height: 2px; 
} 
 /*******科室***********/
.ztree li span.button.depart_ico_open {
 	/* background: url(${ctx}/images/icon_depart.png); */
 	background: #ffffff;
	width: 2px;
	height: 2px; 
}

.ztree li span.button.depart_ico_close {
/* background: url(${ctx}/images/icon_depart.png); */
	background: #ffffff;
	width: 2px;
	height: 2px; 
}

.ztree li span.button.depart_ico_docu {
/* background: url(${ctx}/images/icon_depart.png); */
	background: #ffffff;
	width: 2px;
	height: 2px; 
} 

 /*******病房***********/
.ztree li span.button.room_ico_open {
/*  background: url(${ctx}/images/icon_room.png);
	width: 18px;
	height: 16px;  */
	background: #ffffff;
	width: 2px;
	height: 2px; 
}

.ztree li span.button.room_ico_close {
 	background: #ffffff;
	width: 2px;
	height: 2px; 
}

.ztree li span.button.room_ico_docu {
	background: #ffffff;
	width: 2px;
	height: 2px; 
} 
 /*******病床***********/
.ztree li span.button.bed_ico_open {
	/* background: url(${ctx}/images/icon_bed.png);
	width: 18px;
	height: 16px; */
	background: #ffffff;
	width: 2px;
	height: 2px; 
}

.ztree li span.button.bed_ico_close {
	background: #ffffff;
	width: 2px;
	height: 2px; 
}

.ztree li span.button.bed_ico_docu {
	background: #ffffff;
	width: 2px;
	height: 2px; 
} 

</style>
<s:if test="#request.debug=='true'">
	<script src="${ctx}/baseui/js/plugins/ui/jquery.breadcrumbs.js"></script>
	<script src="${ctx}/baseui/js/plugins/ui/jquery.resize.js"></script>
	<script src="${ctx}/baseui/js/plugins/jquery-ui.js"></script>
	<script src="${ctx}/baseui/js/plugins/base.js"></script>
	<script src="${ctx}/baseui/js/plugins/sea.js"></script>
	<script src="${ctx}/baseui/js/sea-override.js"></script>
	<script src="${ctx}/baseui/js/plugins/ui/jquery.cookie.js"></script>
	<script src="${ctx}/baseui/js/plugins/forms/jquery.uniform.js"></script>
	<script src="${ctx}/baseui/js/plugins/forms/jquery.ibutton.js"></script>
	<script src="${ctx}/baseui/js/plugins/forms/jquery.form.js"></script>
	<script src="${ctx}/baseui/js/plugins/forms/jquery.validate.js"></script>
	<script src="${ctx}/baseui/js/jquery-ui-override.js"></script>
	<script src="${ctx}/baseui/js/index.js"></script>
	<script src="${ctx}/baseui/js/auto-parser.js"></script>
	<script src="${ctx}/baseui/js/page.js"></script>
	<script src="${ctx}/baseui/js/plugins/grid/grid.core.js"></script>
</s:if>
<s:else>
	<script src="${ctx}/baseui/js/compress/commons.js"></script>
</s:else>

<script type="text/javascript">
$(function() {
	if($("input[type=text]").length>0){
		$("input[type=text]")[0].focus();//解决ie下iframe关闭后input不能聚焦编辑的问题（主要是对话框）
	}
});
function handleResponse(result, callback, successMsg, failMsg){
	if(!successMsg){
		successMsg = "操作成功";
	}
	if(!failMsg){
		failMsg = "操作失败 ";
	}
	if(result.success){
		jAlert(successMsg, '提示', 'attention', callback);
	}else{
		jAlert(result.message||failMsg, '提示', 'attention', callback);
	} 	
}
/* 就是浏览器窗口的高度，兼容ie */
window.getHeight= function(){  
    if(window.innerHeight!= undefined){  
        return window.innerHeight;  
    }  
    else{  
        var B= document.body, D= document.documentElement;  
        return Math.min(D.clientHeight, B.clientHeight);  
    }  
}  
//条件查询+新建按钮
setGridHeight1= function(){  
	$(".datatable").height(getHeight()-93-36+"px");
	if(window.screen.height>1000){
		rowN=20;
	}else{
		rowN=10;
	}; //根据屏幕的分辨率来设置
}
//条件查询
setGridHeight2= function(){  
	$(".datatable").height(getHeight()-93+"px");
	if(window.screen.height>1000){
		rowN=20;
	}else{
		rowN=10;
	}; //根据屏幕的分辨率来设置
}
//条件查询
setGridHeight3= function(){  
	$(".datatable").height(getHeight()-105+"px");
	if(window.screen.height>1000){
		rowN=20;
	}else{
		rowN=10;
	}; //根据屏幕的分辨率来设置
}
</script>