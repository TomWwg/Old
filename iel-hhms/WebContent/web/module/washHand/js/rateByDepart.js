var tree;
var node;
var chart; 
// DOM加载完后执行
$(function() {
	setDefaultQueryTime();
	Highcharts.setOptions({colors:["#52DDB6","#D0CDC9"]});
	$("#depart").multiselect({
		selectedList: 2
	});
	$('#depart').val( $("#departInput").val().split(',') );
    $('#depart').multiselect("refresh");
	setData();
	$('.filterForm>#filterForm').css("position","fixed");
});  
//查询
$("#data_filtering").click(function(){
	setData();
});	  
//导出
function excelData() {
	var url = top.pt.ctx+'/web/module/exportFun/ExportRateByDept.action';
	var depart = $("#departInput").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	window.location.href = url+"?depart="+depart+"&startTime="+startTime+"&endTime="+endTime ;
}
function departSelect(){
	$('#depart').multiselect("update");           
	var depart = $("#depart").multiselect("MyValues");
	$("#departInput").attr("value",depart);
}
function setData() {
	var depart = $("#departInput").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var url = "rateChartByDepart.action";
	$.ajax({
		type : "POST",
		url : url,
		dataType:"json",
		data : {
			depart : depart,
			startTime : startTime,
			endTime : endTime
		},
		success : function(data) {
			var dataList = makeDataListNoControllerDepart(data.dataList);
			setChart(dataList);
			setTb(dataList);
		}
	});
}
function setTb(dataList) {
	var sHtml = "<thead height='25px'> ";
	sHtml +="<tr>";
	var len=dataList[0].nameList.length+1,pLen;
	pLen=10/len;
	sHtml +="<td >"+"科室"+"</td>";
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
		var depart = dataList[i].depart;
		$("#tr_"+i).append("<td style='text-align: center;'>"+depart+"</td>");
		for ( var j = 0; j < dataList[i].nameList.length; j++) {
			var rateStr = dataList[i].rateList[j];
			$("#tr_"+i).append("<td style='text-align: center;'>"+rateStr+"</td>");
		}
	}
	//document.getElementById('chart_combo').style.position = 'fixed';
}
function setChart(dataList) {
	var right = [],wrong =[],partName =[];
	for ( var i = 0; i < dataList.length; i++) {
		var o = dataList[i];
		var name = o.rate;
		var rightCount = o.rightCount;
		var wrongCount = o.wrongCount;
		//屏蔽感控科

		partName.push(name);
		right.push(rightCount);
		wrong.push(wrongCount);

	}
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo' ,//关联页面元素div#id 
            style: {
                position: 'fixed'
            }
        }, 
        credits: { //取消右下角的highcharts.com的
            enabled:false
        },
        title: {  //图表标题 
            text: '科室依从率统计' 
        }, 
        xAxis: { //x轴 
        	categories: partName,
            labels:{y:18},  //x轴标签位置：距X轴下方18像素 
            labels: {
                style: {
                    color: '#000000'//颜色
                }
            },
        }, 
        yAxis: {  //y轴 
        	allowDecimals:false,     //不显示小数
            title: {text: '次数'}, //y轴标题 
            lineWidth: 2, //基线宽度 
            labels: {
                style: {
                    color: '#000000'//颜色
                }
            },
        }, 
        tooltip: {
        	//格式化返回串为空
        	formatter: function() {
        	return '';
        	},
        	//数据提示框显示的绝对位置，这里显示再原点位置比欺骗用户之用
        	positioner: function () {
        	return { x: 0, y: 0 };
        	},
        	//提示框的背景颜色设置和chart的背景色一样
        	backgroundColor:'#FFFFFF',
        	borderWidth: 0,//边框宽度
        	borderRadius:0, //边框圆角半径
        	shadow: false//不显示阴影
        	},
        labels: { //图表标签 
            items: [{ 
                html: '', 
                style: { 
                    left: '48px',
                    top: '8px' 
                } 
             }] 
        }, 
        exporting: { 
            buttons: {
                contextButton: {
                    menuItems: [{
                        text: '下载JPEG图片',
                        onclick: function () {
                            this.exportChart({
                            	 sourceWidth: 1500,
                                 sourceHeight: 600,
                                 filename: '科室依从率统计',
                                 type:'image/jpeg',//导出的文件类型
                            });
                        }
                    }]
                }
            }
        }, 
        series: [{ //数据列 
            type: 'column', 
            name: '已执行', 
            data: right 
        }, 
        { 
            type: 'column', 
            name: '未执行', 
            data: wrong 
        }],
        legend: {
     	   borderWidth:0,
     	   itemMarginTop:30,
     	   itemStyle:{
     		   color:'#000000',
     	   }
         },
         plotOptions:
         {  
             column: //柱形图
             {
             	dataLabels:{
                     enabled:true, // dataLabels设为true
                     style:{
                         color:'#000000',
                     }
             	},
             	pointPadding: 1,
                 borderWidth: 0,
                 pointWidth: 20,              //柱子的大小(会影响到柱子的大小)
             },
         }
        
    }); 
	//document.getElementById('chart_combo').style.position = 'fixed';
}

function setDefaultQueryTime(){
	$("#startTime").val(new Date(new Date().getTime()-7 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
}
//感控科的数据拿掉
function makeDataListNoControllerDepart(dataList){
	var newDatas = [];
	for ( var i = 0; i < dataList.length; i++) {
		//屏蔽感控科
		if(!(dataList[i].depart == "感控科"|| dataList[i].depart == "感染控制科")){
			newDatas.push(dataList[i]);
		}	
	}
	return newDatas;
}
