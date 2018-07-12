var tree;
var node;
var rows=30;

var PER_PUSH_COOKIE_KEY 	= "PER_PUSH_QUALITY"
var PER_BOTTLE_COOKIE_KEY 	= "PER_BOTTLE_QUALITY"

var table = null;
// DOM加载完后执行
$(function() {
	setGridHeight2();//设置列表高度自适应
	setDefaultQueryTime();

	//先找下cookie信息
	checkInfosInCookie();
	//查询
	$("#data_filtering").click(function() {
		console.log('onclick data filtering');
		//设置cookie
		saveInfosInCookie();
		//更改表
		var treeId = $("#treeId").val();
		if (treeId == null || treeId == "") {
			jAlert('请选择组织左右的组织树', '提示', 'attention');
			return;
		}
		getData();
		$('#table').grid('reload');
	});
	//左侧树列表
	var setting = {
			async: {
				enable: true,
				//多了个参数&hideControlDepart=true 不要感控科
				url: top.pt.ctx+"/web/module/getDepartmentTree.action?isTreeOpen=false&hideControlDepart=true",
				autoParam: ["id", "label"],
				dataType :"json"
			},
			callback : {
				//树加载成功的之后
				onAsyncSuccess: function(event, treeId, treeNode, msg){
					var zTree=$.fn.zTree.getZTreeObj(treeId);
					var node;
					console.log("onAsyncSuccess -->" );
					if(msg.parentId==1){//parentId暂时用来存放部门ID,判断默认节点
						if(msg.children[0].id == null){
							node=zTree.getNodeByParam("id", msg.id);
							choosingHospitalToShow();
						}else{ // 不显示默认是医院
							notChoosingHospitalToHide();
							node=zTree.getNodeByParam("id", msg.children[0].id);
							//console.log("msg.children[0].id -->",msg.children[0].id);
							$("#treeId").val(msg.children[0].id);//将treeId放入过滤的form表单，用户过滤该组织下的设备
							$('#table').grid('reload');
							zTree.selectNode(node);
							getChartData();
							getData();
						}
					}else{
						console.log("msg.children[0].id isis-->",msg.children[0].id);
						node=zTree.getNodeByParam("id", msg.children[0].id);
						$("#treeId").val(msg.children[0].id);//将treeId放入过滤的form表单，用户过滤该组织下的设备
						$('#table').grid('reload');
						getChartData();
						getData();
						
					}

					zTree.selectNode(node);
				},
				onClick: function(event, treeId, treeNode){
					$("#treeId").val(treeNode.id);//将treeId放入过滤的form表单，用户过滤该组织下的设备
					$('#table').grid('reload');
					if(treeNode.id == '1'){//选择医院时
						choosingHospitalToShow();
					}else{
						notChoosingHospitalToHide();
						getData();
					}
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
	getData();

});
//查询数据：包括整体情况，图表和表格
function getData(){
	var startStr = $("#startTime").val();
	var endStr = $("#endTime").val();
	var depart = $("#treeId").val()

	checkDepartWashTimesStatistics(startStr,endStr,depart);
	checkStaffWashTimesTable(startStr,endStr,depart);
	getChartData();
}

//得到HighChart数据
function getChartData() {
	var treeId = $("#treeId").val();
	console.log("treeId is:",treeId);
	//var staffType = $("#staffTypeInput").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var url = "findWashTimesLineDrawing.action";
	$.ajax({
		type : "POST",
		url : url,
		dataType:"json",
		data : {
			depart : treeId,
			startTime : startTime,
			endTime : endTime
		},
		success : function(data) {
			console.log("data is :",data);
			
			setChart(data.dataMap);
		}
	});
}

//渲染HighCHart控件
function setChart(dataMap) {
	var right = [],wrong =[],partName =[];
	var times = [];
	
	for(var key in dataMap){
		var value = dataMap[key];
		partName.push(key);
		times.push(value);
	}
	
//	
//	for ( var i = 0; i < dataList.length; i++) {
//		var o = dataList[i];
//		var name = o.type;
//		var rightCount = o.rightCount;
//		var wrongCount = o.wrongCount;
//		partName.push(name);
//		right.push(rightCount);
//		wrong.push(wrongCount);
//	}
	chart = new Highcharts.Chart({
        chart: {
            renderTo: 'chart_combo' //关联页面元素div#id
        },
        credits: { //取消右下角的highcharts.com的
            enabled:false
        },
        title: {  //图表标题
            text: '人员类别手卫生次数统计'
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
            title: {text: '次数/次'}, //y轴标题
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
                                 filename: '人员类别手卫生次数统计',
                                 type:'image/jpeg',//导出的文件类型
                            });
                        }
                    }]
                }
            }
        },
        series: [{ //数据列
            type: 'column',
            name: '手卫生次数',
            data: times
        }],
        legend: {
     	   borderWidth:0,
     	   itemMarginTop:30,
     	   enabled:false,
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
             	pointWidth: 20,                //柱子的大小(会影响到柱子的大小)
             },
         }
    });
}

//导出数据
function excelData() {
	var url = top.pt.ctx+'/web/module/exportFun/timesOfWashHand.action';
	$('#filterForm').attr("action", url).submit();
}

//去后台查询整体部门统计 findLiquidByTimeAndDepart
function checkDepartWashTimesStatistics(startTime,endTime,depart){
	var urlStatis = "findLiquidByTimeAndDepart.action"
	$.ajax({
		type : "POST",
		url : urlStatis,
		dataType:"json",
		data : {
			depart : depart,
			startTime : startTime,
			endTime : endTime,
		},
		success : function(data) {
			console.log('Depart data is :', data);
			
			calcResult(data);
		}
	});
}
//计算整体统计结果
function calcResult(data){
	//总共

	var times = data.dataList;
	$("#statis_result_total_times").html( times);

}

//去后台查询表格数据
function checkStaffWashTimesTable(startTime,endTime,depart){
	
	if(table == null){
		var urlTable = "findFrequencyByStaff.action";
		table = $('#table').grid({
		  colName: ['员工姓名','排名' , '手卫生次数', '不清洁时','有限清洁时','清洁时'],
		  colModel: [
			{name: 'docName', index:'docName',sortable:false, style: {'textAlign': 'center'}},
			{name: 'rank', index:'rank', style: {'textAlign': 'center'}},
			{name: 'totalWashTimes', index:'totalWashTimes', style: {'textAlign': 'center'}},
			{name: 'notcleanTimes', index:'cleanTimes', style: {'textAlign': 'center'}},
			{name: 'limitcleanTimes', index:'limitcleanTimes',sortable:false, style: {'textAlign': 'center'}},
			{name: 'cleanTimes', index:'notcleanTimes',sortable:false, style: {'textAlign': 'center'}},
			],
		  multiSelect : false,//是否显示复选框
		  headerFixed : true,
		  rowNum: rows,
		  rowMark : true,
		  height : '100%',
		  sortname: 'createTime',
		  sortorder: 'desc',
		  ajaxOptions : {
		     type : 'post'
		  } ,
		  dataType: 'ajax',
		  url : urlTable,
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  ajaxDynamic : function(){
	  		  return $('#filterForm').formToArray();
	  	  },
		  ajaxResponseReader: {page:'pageNo', rows:'dataList', records: 'total', totalPage:'totalPage'}
		});
	}
	table.grid("clearGridData");
	table.grid('reload');
}

//设置默认时间
function setDefaultQueryTime(){
	$("#startTime").val(new Date(new Date().getTime()- 30 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
}
//date转换String
function dateToString(date){
	//yyyy-MM-dd hh:mm:ss
	return date.getFullYear()+"-"+(date.getMonth() + 1)+"-"+
	date.getDate() +" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
}

// 选择医院的时候的
function choosingHospitalToShow(){
	$('#select_info').show();
	$('#error_info').hide();
	$('#statis_result_total_times').html('0');
	$('#statis_result_per_out').html('0');
	$('#statis_result_total_bottle').html('0');
	$('#table').hide();
}

// 不选择医院的时候
function notChoosingHospitalToHide(){
	$('#table').show();
	$('#select_info').hide();
}

//查询cookie
function checkInfosInCookie(){
	var perPushPref = $.cookie(PER_PUSH_COOKIE_KEY);
	var perBottlePref = $.cookie(PER_BOTTLE_COOKIE_KEY);
	if(perPushPref){
		$(".perPush").val(perPushPref)
	}
	if(perBottlePref){
		$(".perBottle").val(perBottlePref)
	}
}

//保存cookie
function saveInfosInCookie(){

	$.cookie(PER_PUSH_COOKIE_KEY,$(".perPush").val());
	$.cookie(PER_BOTTLE_COOKIE_KEY,$(".perBottle").val());
}
