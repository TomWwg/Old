var tree;
var node;
var chart; 
// DOM加载完后执行
$(function() {
	Highcharts.setOptions({colors:["#00FF00","#FF0000","#33a3dc","#faa755","#8A2BE2"]});
	$('#table').grid({
		  colName: ['姓名', '科室','事件','创建时间'],
		  colModel: [
		    {name: 'docName', index:'docName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'departName', index:'departName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'eventTypeName', index:'eventTypeName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'createTime', index:'createTime',sortable:false, style: {'textAlign': 'center'}}
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
		  url : 'screenListAjax.action',
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  rowMark : false,
		  headTitle : false,
		  ajaxDynamic : function(){ 
	  		  return $('#filterForm').formToArray();
	  	  },
		  ajaxResponseReader: {page:'pageNo', rows:'result', records: 'total', totalPage:'totalPage'}
	});
	
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
					$('#table').grid('reload');
					setData();
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
	setData();
	setInterval("freshData()", 60000);//5分钟300000
});  
function freshData() {
	$('#table').grid('reload');
	setData();
}  
function setData() {
	var treeId = $("#treeId").val();
	var url = "screenChart.action";
	$.ajax({
		type : "POST",
		url : url,
		dataType:"json",
		data : {
			treeId : treeId
		},
		success : function(data) {
			var fullScreenData = data.fullScreenData;
			setChart(fullScreenData);
		}
	});
}
function setChart(fullScreenData) {
	var effectWash = fullScreenData.effectWash;
	var inRoom = fullScreenData.inRoom;
	var outRoom = fullScreenData.outRoom;
	var beforeInBed = fullScreenData.beforeInBed;
	var longOutBed = fullScreenData.longOutBed;
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo', //关联页面元素div#id 
            backgroundColor: '#BFEFFF'
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
                        text: '下载PNG图片',
                        onclick: function () {
                            this.exportChart({
                            	 sourceWidth: 1500,
                                 sourceHeight: 600,
                                 type:'image/png',//导出的文件类型
                            });
                        }
                    },{
                        text: '下载JPEG图片',
                        onclick: function () {
                            this.exportChart({
                            	 sourceWidth: 1500,
                                 sourceHeight: 600,
                                 type:'image/jpeg',//导出的文件类型
                            });
                        }
                    }]
                }
            }
        }, 
        credits: {
            //去除默认水印
            	text: ""
            },
        series: [{ //数据列 
            type: 'column', 
            name: '执行手卫生', 
            data: [effectWash] 
        }, 
        { 
            type: 'column', 
            name: '进入病区未手卫生', 
            data: [inRoom] 
        }, 
        { 
            type: 'column', 
            name: '离开病区未手卫生', 
            data: [outRoom] 
        }, 
        { 
            type: 'column', 
            name: '接近患者前未手卫生', 
            data: [beforeInBed] 
        }, 
        { 
            type: 'column', 
            name: '长时离开患者未手卫生', 
            data: [longOutBed] 
        }, 
//        { 
//            type: 'pie', //饼状图 
//            name: '', 
//            data: [{ 
//                name: '洗手次数', 
//                y: effectWash, 
//            }, 
//            { 
//                name: '进入病区未洗手', 
//                y: inRoom, 
//            }, 
//            { 
//                name: '离开病区未洗手', 
//                y: outRoom, 
//            }, 
//            { 
//                name: '接近患者前未洗手', 
//                y: beforeInBed, 
//            },
//            { 
//                name: '长时离开患者未洗手', 
//                y: longOutBed, 
//            }], 
//            center: [160, 120],  //饼状图坐标 
//            size: 180,  //饼状图直径大小 
//            dataLabels: { 
//                enabled: true  //不显示饼状图数据标签 
//            } 
//        }
        ],
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
            	//pointPadding: 1,
                borderWidth: 0,
            	//pointWidth: 100,                //柱子的大小(会影响到柱子的大小)
            },
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                	distance:10,
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    format: '{point.percentage:.1f}%'
                }
            }
        }
        
    }); 
}