 
// DOM加载完后执行
$(function() {
	var myDate = new Date(); //获取今天日期
	myDate.setMonth(myDate.getMonth() - 2);
	$("#startTime").val(myDate.format("yyyy-MM"));
	$("#endTime").val(new Date().format("yyyy-MM"));
	$("#depart").multiselect({
		selectedList: 1
	});
	$('#depart').val( $("#departInput").val().split(',') );
    $('#depart').multiselect("refresh");
	searchData();
});  
  
function departSelect(){
	$('#depart').multiselect("update");           
	var depart = $("#depart").multiselect("MyValues");
	$("#departInput").attr("value",depart);
}

function excelData() {
	var depart = $("#departInput").val();
	var timeType = $("#timeType").val(); //统计类型
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var url = "exportRateByTime.action";
	url += "?depart="+depart+"&timeType="+timeType+"&startTime="+startTime+"&endTime="+endTime;
	window.location.href = url;
}

function searchData() {
	var depart = $("#departInput").val();
	var timeType = $("#timeType").val(); //统计类型
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var url = "queryRateByTime.action";
	$.ajax({
		type : "POST",
		dataType:"json",
		async:false,
		url : url,
		data : {
			depart : depart,
			timeType : timeType,
			startTime : startTime,
			endTime : endTime
			
		},
		success : function(data) {
			var dataList = data.departRateList;
			setTb(dataList);
		}
	});
}
function setTb(dataList) {
	var sHtml = "<thead height='25px'> ";
	sHtml +="<tr>";
	var len=dataList[0].nameList.length+1,pLen;
	pLen=10/len;
	sHtml +="<td >"+"时间"+"</td>";
	if(dataList.length>0){
		for ( var i = 0; i < dataList[0].nameList.length; i++) {
			var str = dataList[0].nameList[i];
			sHtml +="<td >"+str+"</td>";
		}
	}else{
		sHtml ='';
		sHtml +="<td colspan='1' align='center' height='30px'>"+" 无符合要求的数据数据！！"+" </td>";
		$("#listTable").html(sHtml);
		return;
	}
	sHtml +="</tr> </thead>";//表头
	
	for ( var i = 0; i < dataList.length; i++) {
			sHtml += "<tr id='tr_"+i+"'> </tr>";
	}
	$("#listTable").html(sHtml); //动态生成表
	for ( var i = 0; i < dataList.length; i++) {
		var timeStr = dataList[i].time;
		$("#tr_"+i).append("<td style='text-align: center;'>"+timeStr+"</td>");
		for ( var j = 0; j < dataList[i].nameList.length; j++) {
			var rateStr = dataList[i].rateList[j];
			$("#tr_"+i).append("<td style='text-align: center;'>"+rateStr+"</td>");
		}
	}
}




