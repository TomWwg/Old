var tree;
var node;
var chart; 
// DOM加载完后执行
$(function() {
	setDefaultQueryTime();
//	$("#startTime").val(new Date().format("yyyy-MM-dd 00:00:00"));
//	$("#endTime").val(new Date().format("yyyy-MM-dd 23:59:59"));
	Highcharts.setOptions({colors:["#52DDB6","#D0CDC9"]});
	$("#staffType").multiselect({
		selectedList: 1
	});
	$('#staffType').val( $("#staffTypeInput").val().split(',') );
    $('#staffType').multiselect("refresh");
	$('#table').grid({
		  colName: ['人员类别', '人数','已执行(次)','未执行(次)','依从率(%)'],
		  colModel: [
		    {name: 'name', index:'name',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'perpleNum', index:'perpleNum',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'rightCount', index:'rightCount',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'wrongCount', index:'wrongCount',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'rate', index:'rate',sortable:false, style: {'textAlign': 'center'}}
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
		  url : 'rateByStaffTypeListAjax.action',
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  ajaxDynamic : function(){ 
	  		  return $('#filterForm').formToArray();
	  	  },
		  ajaxResponseReader: {page:'pageNo', rows:'result', records: 'total', totalPage:'totalPage'}
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
//					if(node != null){
//						tree.selectNode(node);
//					}
					var zTree=$.fn.zTree.getZTreeObj(treeId);
					var node;
					if(msg.parentId==1){//parentId暂时用来存放部门ID,判断默认节点
						node=zTree.getNodeByParam("id", msg.id);
					}else{
						node=zTree.getNodeByParam("id", msg.children[0].id);
					}
					zTree.selectNode(node);
				},
				onClick: function(event, treeId, treeNode){
					//node = treeNode;
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
});  

//查询
$("#data_filtering").click(function() {
	var treeId = $("#treeId").val();
	if (treeId == null || treeId == "") {
		jAlert('请选择组织左右的组织树', '提示', 'attention');
		return;
	}
	$('#table').grid('reload');
	// 生成图标
	setData();
});	   
function staffTypeSelect(){
	$('#staffType').multiselect("update");           
	var staffType = $("#staffType").multiselect("MyValues");
	$("#staffTypeInput").attr("value",staffType);
	  
}
function setData() {
	var treeId = $("#treeId").val();
	var staffType = $("#staffTypeInput").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var url = "rateChartByStaffType.action";
	$.ajax({
		type : "POST",
		url : url,
		dataType:"json",
		data : {
			treeId : treeId,
			staffType : staffType,
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
	var right = [],wrong =[],partName =[];
	for ( var i = 0; i < dataList.length; i++) {
		var o = dataList[i];
		var name = o.type;
		var rightCount = o.rightCount;
		var wrongCount = o.wrongCount;
		partName.push(name);
		right.push(rightCount);
		wrong.push(wrongCount);
	}
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo' //关联页面元素div#id 
        }, 
        credits: { //取消右下角的highcharts.com的
            enabled:false
        },
        title: {  //图表标题 
            text: '人员类别依从率统计' 
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
                                 filename: '人员类别依从率统计',
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
             	pointWidth: 20,                //柱子的大小(会影响到柱子的大小)
             },
         }
        
    }); 
}
//导出
function excelData() {
	var url = top.pt.ctx+'/web/module/exportFun/ExportRateByStaffType.action';
	 $('#filterForm').attr("action", url).submit();
}

function setDefaultQueryTime(){
	$("#startTime").val(new Date(new Date().getTime()-7 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
}