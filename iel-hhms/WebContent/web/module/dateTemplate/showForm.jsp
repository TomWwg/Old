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
$(function(){
	$("#staff").multiselect({
		selectedList: 5
	});
    $('#staff').val( $("#staffInput").val().split(',') );
    $('#staff').multiselect("refresh"); 
});
function staffSelect(){
	$('#staff').multiselect("update");           
	var staff = $("#staff").multiselect("MyValues");
	$("#staffInput").attr("value",staff);
}

</script>
</head>
<body>
	<div class="pageContent">
		<form id="showForm" action="" method="post" class="form-horizontal form-input">
		    <input type="hidden" id="id" name="entity.id"
				value='<s:property value="entity.id"/>'>
				<input type="hidden" id=staffInput name="entity.staffIds" value="<s:property value="entity.staffIds"/>"/><!-- 保持和多选框一致 -->
			<div class="pageContent">
				<fieldset class="noLegend">
					<div class="control-group">
                      <label class="control-label"><em>*</em>值班日期：</label>
                      <div class="controls">
                    	 <s:select id='day' list='daysList' theme="simple" 
							    name="entity.day">
				            </s:select>
                      </div>
                    </div>
					<div class="control-group input">
						<label class="control-label"><em>*</em>值班模板：</label>
						<div class="controls">
							<s:select id='scheduleId' list='scheduleTemplateList' listKey='id' listValue='name' 
							    name="entity.scheduleTemplate.id">
				         	   </s:select>
						</div>
					</div>         
                    <div class="control-group">
                      <label class="control-label"><em>*</em>值班人员：</label>
                      <div class="controls">
                        <select id ="staff" multiple="multiple" onchange="staffSelect()" >
							  <c:forEach items="${staffList}" var="sList">
                                  <option value="${sList.id}">${sList.name}</option>
                              </c:forEach>	     
	                     </select>
                      </div>
                    </div>
				</fieldset>
			</div>
		</form>
	</div>
</body>
</html>