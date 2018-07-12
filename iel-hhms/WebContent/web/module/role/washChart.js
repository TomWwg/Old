var chart; 
$(function() {

	//左侧树列表
	var setting = {
			async: {
				enable: true,
				url: top.pt.ctx+"/web/module/getDepartmentTree.action?isTreeOpen=false",
				autoParam: ["id", "label"],
				dataType :"json"
			},
			callback : {
				//树加载成功的之后
				onAsyncSuccess: function(event, treeId, treeNode, msg){
					if(node != null){
						tree.selectNode(node);
					}
				},
				onClick: function(event, treeId, treeNode){
					node = treeNode;
					$("#treeId").val(treeNode.id);//将treeId放入过滤的form表单，用户过滤该组织下的设备
					$.post(top.pt.ctx+"/web/module/getStaff.action", {
						groupTreeId : treeNode.id
					   }, function(data) {
							$("#staffId").empty();
							$('#staffId').html(data.msg).show();  
								
					}, 'json');
				   /* gridReload();*/
				}

			},
			view : {
				fontCss : function(treeId, treeNode) {
					return (!!treeNode.highlight) ? {
						color : "#A60000",
						"font-weight" : "bold"
					} : {
						color : "#25c4eb",
						"font-weight" : "normal",
						"font-size" : "16px"
					};
				}
			}
	};
	tree = $("#zTreea").tree(setting);
	Highcharts.setOptions({colors:["#00FF00","#FFB90F","#FF0000","#8A2BE2"]});
//	setData();
//	setChart();
	setInterval("setData()", 60000);
	
});
function setData() {
	var departStr = $("#departmentId").val();
	var staffNameStr = $("#staffId").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	//alert()
	var url = "washChart.action";
	$.ajax({
		type : "POST",
		url : url,
		dataType:"json",
		data : {
			departStr : departStr,
			staffNameStr : staffNameStr,
			startTime : startTime,
			endTime : endTime
		},
		success : function(data) {
			var fullScreenData = data.fullScreenData;
			setChart(fullScreenData);
		}
	});
}
function setChart(fullScreenData) {
	var effectWash = fullScreenData.effectWash;
	var outArea = fullScreenData.outArea;
	var inBed = fullScreenData.inBed;
	var beforeInBed = fullScreenData.beforeInBed;
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo' //关联页面元素div#id 
        }, 
        title: {  //图表标题 
            text: '手卫生实时状态' 
        }, 
        xAxis: { //x轴 
        	categories: ['', '', '', '', ''],
            labels:{y:18}  //x轴标签位置：距X轴下方18像素 
        }, 
        yAxis: {  //y轴 
        	allowDecimals:false,     //不显示小数
            title: {text: '次数'}, //y轴标题 
            lineWidth: 2 //基线宽度 
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
            enabled: false  //设置导出按钮不可用 
        }, 
        credits: {  
            text: 'helloweba.com', 
            href: 'http://www.helloweba.com' 
        }, 
        series: [{ //数据列 
            type: 'column', 
            name: '执行手卫生', 
            data: [effectWash] 
        }, 
        { 
            type: 'column', 
            name: '离开病区未手卫生', 
            data: [outArea] 
        }, 
        { 
            type: 'column', 
            name: '长时接近患者未手卫生', 
            data: [inBed] 
        }, 
        { 
            type: 'column', 
            name: '接触患者前未手卫生', 
            data: [beforeInBed] 
        }, 
        { 
            type: 'pie', //饼状图 
            name: '', 
            data: [{ 
                name: '执行手卫生', 
                y: effectWash, 
            }, 
            { 
                name: '离开病区未手卫生', 
                y: outArea, 
            }, 
            { 
                name: '长时接近患者未手卫生', 
                y: inBed, 
            }, 
            { 
                name: '接触患者前未手卫生', 
                y: beforeInBed, 
            }], 
            center: [120, 180],  //饼状图坐标 
            size: 120,  //饼状图直径大小 
            dataLabels: { 
                enabled: true  //不显示饼状图数据标签 
            } 
        }],
     /*  legend: {
        	layout: 'vertical',
        	backgroundColor: '#FFFFFF',
        	floating: true,
        	align: 'left',
        	verticalAlign: 'top',
        	x: 90,
        	y: 45
        },*/
        plotOptions:
        {  
            column: //柱形图
            {
            	dataLabels:{
                    enabled:true, // dataLabels设为true
                    style:{
                       // color:'#D7DEE9',
                    }
            	},
            	pointPadding: 1,
                borderWidth: 0,
            	pointWidth: 100,                //柱子的大小(会影响到柱子的大小)
            },
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    format: '{point.percentage:.1f} %'
                }
            }
        }
        
    }); 
}
/**
 * 
 * @param {Object} aValue
 */
function changeDepartmentId(){
	var departmentId = $("#departmentId").val();
/*	if(departmentId=='-1'){
		return false;
	}*/
	$.post("deviceAjax_getStaff.do", {
		departmentId : departmentId
		}, function(data) {
			$("#staffId").empty();
			$('#staffId').html(data.staffList).show();  
		}, 'json');
}

