<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<%@ include file="/common/commons.jsp" %>
<title>授权管理</title>
	<style type="text/css">
		.msg-weak {width: 330px;}
	</style>
</head>
<body class="page">
	<div class="popWrap popWin" style="position: absolute; margin: -142px 0px 0px -222px; top: 50%; left: 50%;">
		<div class="popInner">
			<div class="popTitle">
				<h3>授权管理</h3>
			</div>
		</div>
		<div class="popContent">
			<form name="licenseUpload" action="" enctype="multipart/form-data" method="post">
				<div>
					<img alt="" src="${ctx}/images/license.png" width="100%" height="80" complete="complete" />
				</div>
				<fieldset class="noLegend">
					<div class="control-group ml30 mr30 pd20">
						<div class="msg-weak msg-attention" style="background:#FFF;">
                            <i></i>
                            <div class="msg-cnt">
                                <span>请导入授权许可文件</span>
                            </div>
                        </div>
						<div class="input-append">
							<!-- <input class="input-xlarge" type="text"/>
							<a class="buttonS bDefault bUpload" href="">选择
								<input name="licenseFile" class="iUpload" type="file"/>
							</a> -->
							<input id="licenseFile" name="licenseFile" type="file" class="iUpload" accept=""/>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div class="popButton">
			<button class="bPrimary ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" onclick="uploadFile();" type="button">
				<span class="ui-button-text">提交</span>
			</button>
			<button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" onclick="cancel();" type="button">
				<span class="ui-button-text">取消</span>
			</button>
		</div>
	</div>
	<!-- 以下为当前页脚本 -->
	<script type="text/javascript" src="${ctx}/common/js/ajaxfileupload.js"></script>
	<script type="text/javascript">
	
		 $(function(){
	         $('input.iUpload').change(function(){
	             var file = $(this).val();
	             var index = file.lastIndexOf('\\');
	             //$(this).parents('.input-append').find('.input-xlarge').val(file.substring(index+1));
	             $(this).parents('.input-append').find('.iUpload').val(file.substring(index+1));
	         });
	     }); 
        
        //var currentUrl = top.location.href; // 当前的url
    	//if(currentUrl.lastIndexOf("/cms/uploadLicense") == -1 && currentUrl.lastIndexOf("/cms/uploadlicenseFile.do") == -1){
    	//	top.location.href = "/cms/uploadLicense";
    	//}

        function uploadFile(){
        	var value = licenseUpload.licenseFile.value;
            if (value=="") {
               alert("请选择要上传的文件！");
                return false;
            }
            var pos = value.lastIndexOf(".");
            var lastname = value.substring(pos,value.length);
            if (!(lastname.toLowerCase() == ".cyt")) {
            	alert("上传文件格式有误，请检查后重新上传！");
            	return;
            }
            //ajax提交
            $.ajaxFileUpload({
        		url : "${ctx}/web/module/license/uploadLicense.action",
        		secureuri : false,				
        		fileElementId : "licenseFile",	// file标签的id
        		dataType : "json",			// 返回值类型
        		success : function(data){
        			if(data.success == true){
        				alert("证书上传成功！");
        				window.document.location = '${ctx}/web/login.jsp';
                	}else{
                		alert("证书上传失败："+data.message);
                	}
        		},
       		 	error: function (data, status, e){	//服务器响应失败处理函数
       		 	    alert("导入文件出错，请重试！"+e);
                }
        	});
        }
        function cancel(){
            window.document.location = '${ctx}/web/login.jsp';
        }
        document.oncontextmenu=stop;	//禁用右键菜单
        function stop(){
        	return false; 
        }
       
	</script>
</body>
</html>
