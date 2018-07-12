<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/js/assets/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/assets/style.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/assets/prettify.css" />
<link href="${ctx}/js/assets/jquery-ui.css" rel="stylesheet" />
<script type="text/javascript" src="${ctx}/js/assets/jquery-ui.js"></script> 
<script type="text/javascript" src="${ctx}/js/assets/prettify.js"></script>
<script type="text/javascript" src="${ctx}/js/assets/jquery.multiselect.js" ></script>
<style type="text/css">
</style>
<script type="text/javascript">
/* $(function(){
	$("#depart").multiselect({
		selectedList: 5
	});
    $('#depart').val( $("#departInput").val().split(',') );
    $('#depart').multiselect("refresh"); 
}); */
function departSelect(){
	$('#depart').multiselect("update");           
	var depart = $("#depart").multiselect("MyValues");
	$("#departInput").attr("value",depart);
}

function checkUserName(){
	$("#checkUserName").css("color","");
	$("#checkUserName").html("");
	var userName=$.trim($("#name").val());
	if(userName.length>0){
		$.ajax({
			url:'checkUserName.action?entity.name='+userName,
			dataType:'text',
			success:function(data){
				if(data=="0"){
					$("#checkUserName").html("用户名可用");
				}else{
					$("#checkUserName").css("color","red");
					$("#checkUserName").html("用户名已经被使用");
					
				}
			}
		})
	} 
	
}

</script>
</head>
<body onload="prettyPrint();">
	<div class="pageContent">
		<form id="showForm" action="" method="post" class="form-horizontal form-input">
		    <input type="hidden" id="id" name="entity.id"
				value='<s:property value="entity.id"/>'>
			<input type="hidden" id=groupTreeId name="groupTreeId"
				value='<s:property value="groupTreeId"/>'>
			<input type="hidden" id=userStatus name="entity.userStatus"
				value='<s:property value="entity.userStatus"/>'>
			<input type="hidden" id=loginCounts name="entity.loginCounts"
				value='<s:property value="entity.loginCounts"/>'>
			<input type="hidden" id=lastLoginTime name="entity.lastLoginTime"
				value='<s:property value="entity.lastLoginTime"/>'>
			<input type="hidden" id=favoriteIds name="entity.favoriteIds"
				value='<s:property value="entity.favoriteIds"/>'>
			<!-- <input type="hidden" id=departInput name="entity.departIds" value="<s:property value="entity.departIds"/>"/>保持和多选框一致 -->
			<div class="pageContent">
				<fieldset class="noLegend">
					<div class="control-group input">
						<label class="control-label"><em>*</em>用户名：</label>
						<div class="controls">
							<input id="name" name="userName" class='auto-input' type='text'
								required="true" vtype="common" onblur="checkUserName()">
							&nbsp;&nbsp;
							<span id="checkUserName"></span>
						</div>
					</div>
					
					<!-- <div style="display:none" class="control-group input">
						<label class="control-label"><em>*</em>密码：</label>
						<div class="controls">
							<input id="password" name="entity.password"
								class="auto-input valid" type="password" required="true"
								vtype="required,minLength,maxLength" minlength="5"
								maxlength="13" value="88888888">
						</div>
					</div> -->
					
					<!-- <div class="control-group input">
						<label class="control-label"><em>*</em>确认密码：</label>
						<div class="controls">
							<input id="passwordCheck" name="passwordCheck"
								class="auto-input valid" type="password"  vtype="required,equalToTag"
								 required="true" equalToTag="#password" />
						</div>
					</div> -->
					
					
					<!--  改掉到期时间 
                    <div class="control-group">
                      <label class="control-label"><em>*</em>到期时间：</label>
                      <div class="controls">
                        <input id="expireTime" type="text" name="entity.expireTime" value='<s:property value="formateDateYMD(entity.expireTime)"/>'
                          gkui="calendar" class="auto-input" required="true" minDate="minTime" isShowClear="false">
                      </div>
                    </div>
     
                       -->
                    
                    <s:if test="%{typeLevel==0}">
                      <div class="control-group">
                      <label class="control-label"><em>*</em>所属科室：</label>
                      <div class="controls">
                        <select id ="depart" name="entity.departIds" <%-- multiple="multiple" onchange="departSelect()" --%>>
							  <c:forEach items="${departList}" var="dList">
                                  <option value="${dList.id}">${dList.name}</option>
                              </c:forEach>	     
	                     </select>
                      </div>
                    </div>
                    </s:if>
                    <div class="control-group input">
						<label class="control-label"><em>*</em>角色：</label>
						<div class="controls">
							<s:select id='roleId' list='roleList' listKey='id' listValue='name' 
							    name="entity.roleInfo.id" >
				            </s:select>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label">手机：</label>
						<div class="controls">
							<input id="name" name="entity.tel" class='auto-input' type='text'
								 value='<s:property value="entity.tel"/>'>
						</div>
					</div>
					<div class="control-group input">
						<label class="control-label">备注：</label>
						<div class="controls">
							<textarea vtype="common" class="att-width300" name="entity.remark" style="width:435px;height: 150px""><s:property value="entity.remark"/></textarea>
						</div>
					</div>
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>


