var tree;
var node;
var chart; 
// DOM加载完后执行
$(function() {
	setDefaultQueryTime();
	Highcharts.setOptions({colors:["#1B906F","#52DDB7","#8BF25A","#5D97DD"]});
	$("#depart").multiselect({
		selectedList: 2
	});
	$('#depart').val( $("#departInput").val().split(',') );
    $('#depart').multiselect("refresh");
	setData();
	//setData2();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();	
    setTable(startTime,endTime);	

});  
//查询
$("#data_filtering").click(function(){
	setData();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();	
    setTable(startTime,endTime);
});	 
//导出
function excelData() {
	var url = top.pt.ctx+'/web/module/exportFun/outDoorExport.action';
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
/**发送请求*/
function setData() {
	var depart = $("#departInput").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var url = "outDoorTimesAsDaysList.action";
	var dayCounts = getSelectedDaysHowMany();
	$.ajax({
		type : "POST",
		url : url,
		dataType:"json",
		data : {
			depart : depart,
			startTime : startTime,
			endTime : endTime,
			dayCounts : dayCounts
		},
		success : function(data) {
			var dataList = data.dataList;
			setLineChart(dataList);
		}
	});
}
/**发送请求*/
function setData2() {
	var depart = $("#departInput").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var url = "outDoorEventList.action";
	var dayCounts = getSelectedDaysHowMany();
	$.ajax({
		type : "POST",
		url : url,
		dataType:"json",
		data : {
			depart : depart,
			startTime : startTime,
			endTime : endTime,
			dayCounts : dayCounts
		},
		success : function(data) {
			var dataList = data.dataList;
			setLineChart(dataList);
		}
	});
}
function setLineChart(dataList) {
	console.log("datalist is :"+dataList);
	//计算下用户选择了多少天
	var dataCounts = getSelectedDaysHowMany();
	//var dataNames = $("#departInput").val();
	var dataNames = "次数"
	
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo' ,//关联页面元素div#id 
            chart:'line'
        }, 
        credits: { //取消右下角的highcharts.com的
            enabled:false
        },
        title: {
            text: '外门手卫生统计'
        },
        xAxis: {
            categories: getBeforeDates(getSelectedDaysHowMany())
        },
        yAxis: {
            title: {
                text: '执行手卫生(次)'
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true          // 开启数据标签
                },
                enableMouseTracking: true // 关闭鼠标跟踪，对应的提示框、点击事件会失效
            },
            series: {
                cursor: 'pointer',
                zoneAxis: 'x',
                events: {
                    click: function (event) {
                    	
                    	var description = ""; 
                    	for(var i in this){ 
                    		var property=this[i]; 
                    		description += i+" = "+property+"\n"; 
                    	 } 

                    	var startTime = getDateByOffset(event.point.x);
                    	var endTime = getDateByOffset(event.point.x + 1);
                    }
                }
            },     
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
                                 filename: '外门手卫生统计',
                                 type:'image/jpeg',//导出的文件类型
                            });
                        }
                    }]
                }
            }
        },
        series: [{
        	name: dataNames,
            data: dataList
        }],
        legend: {                                                                  
            enabled: false                                                         
        }
    });

}
/**根据图表的点击获取对应时间点的数据*/
function checkOneDayTable(daysOffSetFromStart){
	var startStr = $("#startTime").val();
	var endStr = $("#endTime").val();
	var depart = $("#departInput").val();
	var startTimeTmp = new Date(new Date(startStr).getTime() + daysOffSetFromStart * (24*3600*1000));
	var endTimeTmp =  new Date(new Date(startStr).getTime() + (daysOffSetFromStart+1) * (24*3600*1000)) ;
	var startTime = dateToString(startTimeTmp);
	var endTime = dateToString(endTimeTmp);
	var url = "outDoorEventList.action";
	//var time2 = new Date().Format("yyyy-MM-dd HH:mm:ss"); 
/**	$.ajax({
		type : "POST",
		url : url,
		dataType:"json",
		data : {
			depart : depart,
			startTime : startTime,
			endTime : endTime,
		},
		success : function(data) {
			var dataList = data.dataList;
			//setLineChart(dataList);
			setTable();
		}
	});
*/
	$('#table').grid({
		  colName: ['设备类型','设备编号','事件类型', '发生时间'],
		  colModel: [		            
		    {name: 'deviceType', index:'deviceType',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'deviceNo', index:'deviceNo',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'eventType', index:'eventType',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'updateTimeString', index:'updateTimeString',sortable:false, style: {'textAlign': 'center'}}
		  ],
		  multiSelect : false,//是否显示复选框
		  headerFixed : true,
		  rowNum: 1000,
		  rowMark : true,
		  height : '100%',
		  postData : {
				depart : depart,
				startTime : startTime,
				endTime : endTime,
		  },
		  ajaxOptions : {
		     type : 'post'
		  } ,
		  dataType: 'ajax',
		  url : 'outDoorEventList.action',
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  ajaxDynamic : function(){ 
	  		  return $('#filterForm').formToArray();
	  	  },
	  	  ajaxResponseReader:{page:'pageNo',rows:'dataList', records: 'total', totalPage:'totalPage'}
	});
	
}

/**设置表格*/
function setTable(startTime,endTime){
	var urlTable = "outDoorEventList.action?startTime= "+startTime+"&endTime="+endTime;
	$('#table').grid({
		  colName: ['设备类型','设备编号','事件类型', '发生时间'],
		  colModel: [		            
		    {name: 'deviceType', index:'deviceType',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'deviceNo', index:'deviceNo',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'eventType', index:'eventType',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'updateTimeString', index:'updateTimeString',sortable:false, style: {'textAlign': 'center'}}
		  ],

		  multiSelect : false,//是否显示复选框
		  headerFixed : true,
		  rowNum: 1000,
		  rowMark : true,
		  height : '100%',
		  ajaxOptions : {
		     type : 'post'
		  } ,
		  dataType: 'ajax',
		  url :urlTable,
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  ajaxDynamic : function(){ 
	  		  return $('#filterForm').formToArray();
	  	  },
//		  ajaxResponseReader: {page:'pageNo', rows:'result', records: 'total', totalPage:'totalPage'}
	  	  ajaxResponseReader:{page:'pageNo',rows:'dataList', records: 'total', totalPage:'totalPage'}
	});
	$('#table').grid('reload');
	//$('#table').trigger("reloadGrid");
	
}
/**设置默认选中时间，DOM加载时调用*/
function setDefaultQueryTime(){
	$("#startTime").val(new Date(new Date().getTime()-7 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
}
/**得到时间轴数组，根据具体需要的天数*/
function getBeforeDates(dayBefore){
	var earlyDate = new Date($("#startTime").val());
	//var myDate = new Date(); //获取今天日期
	earlyDate.setDate(earlyDate.getDate());
	//myDate.setDate(myDate.getDate() - dayBefore);
	var dateArray = []; 
	var dateTemp;
	var flag = 1;
	for (var i = 0; i < dayBefore + 1; i++) {
	    dateTemp = (earlyDate.getMonth()+1)+"-"+earlyDate.getDate();
	    dateArray.push(dateTemp);
	    earlyDate.setDate(earlyDate.getDate() + flag);
	}
	return dateArray;
}
/**用户选择时间选择了多少天*/
function getSelectedDaysHowMany(){
	var selectedStartDay = $("#startTime").val()
	var selectedEndDay = $("#endTime").val();

	var diffTime = new Date(selectedEndDay).getTime() - new Date(selectedStartDay).getTime();
	var diffDays=Math.floor(diffTime/(24*3600*1000));
	console.log("diffDays is :"+diffDays);
	return diffDays;
}
/**date转换String*/
function dateToString(date){
	//yyyy-MM-dd hh:mm:ss
	return date.getFullYear()+"-"+(date.getMonth() + 1)+"-"+
	date.getDate() +" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
}
/**根据偏移天数，计算日期:格式：2017-08-07 11:53:27*/
function getDateByOffset(offset){
	var start = $("#startTime").val();
	var sTime = new Date(start).getTime() + offset * (24*3600*1000);
	var sDate = new Date(sTime);
	return sDate.getFullYear()+"-"+(sDate.getMonth() + 1)+"-"+
	sDate.getDate() +" 00:00:00";
}


