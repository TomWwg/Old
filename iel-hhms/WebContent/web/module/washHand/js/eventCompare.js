var tree;
var node;
var chart; 
// DOM加载完后执行
$(function() {
	setDefaultQueryTime();
//    $("#startTime").val(new Date().format("yyyy-MM-dd 00:00:00"));
//	$("#endTime").val(new Date().format("yyyy-MM-dd 23:59:59"));
	Highcharts.setOptions({colors:["#1B906F","#52DDB7","#8BF25A","#5D97DD"]});
	$("#depart").multiselect({
		selectedList: 2
	});
	$('#depart').val( $("#departInput").val().split(',') );
    $('#depart').multiselect("refresh");
	$('#table').grid({
		  colName: ['科室','无菌操作前(%)','接触患者前(%)', '接触患者后(%)','接触患者环境后(%)'],
		  colModel: [		            
		    {name: 'name', index:'name',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'rateWashBeforeAsepticOperation', index:'rateWashBeforeAsepticOperation',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'rateWashBeforeCloseNick', index:'rateWashBeforeCloseNick',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'rateWashAfterCloseNick', index:'rateWashAfterCloseNick',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'rateWashAfterCloseNickEnvri', index:'rateWashAfterCloseNickEnvri',sortable:false, style: {'textAlign': 'center'}}
		  ],
		  multiSelect : false,//是否显示复选框
		  headerFixed : true,
		  rowNum: 20,
		  rowMark : true,
		  height : '100%',
		  ajaxOptions : {
		     type : 'post'
		  } ,
		  dataType: 'ajax',
		  url : 'eventCompareListAjax2.action?hideControllerDepart=true',
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  ajaxDynamic : function(){ 
	  		  return $('#filterForm').formToArray();
	  	  },
		  ajaxResponseReader: {page:'pageNo', rows:'result', records: 'total', totalPage:'totalPage'}
	});
	setData();
});  
//查询
$("#data_filtering").click(function(){
	$('#table').grid('reload');
   //生成图标
	setData();
});	 
//导出
function excelData() {
	var url = top.pt.ctx+'/web/module/exportFun/ExportEventCompare.action';
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
	var url = "eventCompareChart2.action?hideControllerDepart=true";
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
			var dataList = data.dataList;
			setChart(dataList);
		}
	});
}
function setChart(dataList) {
	var right = [],wrong =[];
	
	var partName =[];
	//---
	var washBeforeCloseNick = [],washBeforeAsepticOperation = [], 
		washAfterCloseNick = [],washAfterCloseNickEnvri = [];
	var rateWashBeforeCloseNick = [],
		rateWashBeforeAsepticOperation = [],
		rateWashAfterCloseNick = [],
		rateWashAfterCloseNickEnvri = [];
	//---
	for ( var i = 0; i < dataList.length; i++) {
		var o = dataList[i];
		var name = o.name;
		var beforeCloseNick = o.washBeforeCloseNickTimes;
		var beforeAsepticOperation = o.washBeforeAsepticOperationTimes;
		var afterCloseNick = o.washAfterCloseNickTimes;
		var afterCloseNickEnvri = o.washAfterCloseNickEnvriTimes;

		var rBeforeCloseNick = o.rateWashBeforeCloseNick;
		var rBeforeAsepticOperation = o.rateWashBeforeAsepticOperation;
		var rAfterCloseNick = o.rateWashAfterCloseNick;
		var rAfterCloseNickEnvri = o.rateWashAfterCloseNickEnvri;
		
		partName.push(name);
		washBeforeCloseNick.push(beforeCloseNick);
		washBeforeAsepticOperation.push(beforeAsepticOperation);
		washAfterCloseNick.push(afterCloseNick);
		washAfterCloseNickEnvri.push(afterCloseNickEnvri);
		
		rateWashBeforeCloseNick.push(rBeforeCloseNick);
		rateWashBeforeAsepticOperation.push(rBeforeAsepticOperation);
		rateWashAfterCloseNick.push(rAfterCloseNick);
		rateWashAfterCloseNickEnvri.push(rAfterCloseNickEnvri);
	}
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo' //关联页面元素div#id 
        }, 
        credits: { //取消右下角的highcharts.com的
            enabled:false
        },
        title: {  //图表标题 
            text: '执行手卫生时机统计(单位:次)' 
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
                                 filename: '执行手卫生时机统计',
                                 type:'image/jpeg',//导出的文件类型
                            });
                        }
                    }]
                }
            }
        }, 
        series: [{ 
            type: 'column', 
            name: '无菌操作前', 
            data: washBeforeAsepticOperation 
        },
        { //数据列 
            type: 'column', 
            name: '接触患者前', 
            data: washBeforeCloseNick 
        }, 
        
        { //数据列 
            type: 'column', 
            name: '接触患者后', 
            data: washAfterCloseNick 
        }, 
        { 
            type: 'column', 
            name: '接触患者环境后', 
            data: washAfterCloseNickEnvri 
        }],
        legend: {
     	   borderWidth:0,
     	   itemMarginTop:20,
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
             	pointWidth: 20, //柱子的大小(会影响到柱子的大小)
             },
         }
        
    }); 
}

function setDefaultQueryTime(){
	$("#startTime").val(new Date(new Date().getTime()-7 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
}

