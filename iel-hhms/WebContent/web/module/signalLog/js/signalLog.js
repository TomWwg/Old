var tree;
var node;
var rows=30; 
// DOM加载完后执行
$(function() {
	setGridHeight2();//设置列表高度自适应
	//setInterval("searchData()", 10000 );
	setDefaultQueryTime();
//	$("#startTime").val(new Date(new Date().getTime()-7 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
//	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
	$("#eventType").multiselect({
		selectedList: 1
	});
	$("#eventTypeInput").attr("value","0003,0007,0008,0102,0103,0110,0108,0109,9006,0018");
    $('#eventType').val( $("#eventTypeInput").val().split(',') );
    $('#eventType').multiselect("refresh");
    
    $("#deviceType").multiselect({
		selectedList: 1
	});
	$("#deviceTypeInput").attr("value","38,40,09,0a,10,29,18,20,2a,30,2b,41");
    $('#deviceType').val( $("#deviceTypeInput").val().split(',') );
    $('#deviceType').multiselect("refresh");
    
	$('#table').grid({
		  //colName: ['设备编号', '事件类型','发生时间','内容'],
		  colName: ['胸牌编号','设备编号', '设备类型','事件类型','发生时间','内容'],
		  colModel: [
		    {name: 'rfid', index:'rfid', style: {'textAlign': 'center'}},
		    {name: 'deviceNo', index:'deviceNo', style: {'textAlign': 'center'}},
		    {name: 'deviceTypeName', index:'deviceTypeName', sortable: false, style: {'textAlign': 'center'}},
		    {name: 'eventTypeName', index:'eventTypeName', style: {'textAlign': 'center'}},
		    {name: 'createTime', index:'createTime', style: {'textAlign': 'center'}},
		    {name: 'content', index:'content',sortable:false,width:'380px', style: {'textAlign': 'center'}}
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
		  url : 'listAjax.action',
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  ajaxDynamic : function(){ 
	  		  return $('#filterForm').formToArray();
	  	  },
		  ajaxResponseReader: {page:'pageNo', rows:'result', records: 'total', totalPage:'totalPage'}
		});
	//左侧树列表
	/*var setting = {
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
	tree = $("#zTreea").tree(setting);*/
	//查询
	$("#data_filtering").click(function(){
		$('#table').grid('reload');
	});	      
});  

function eventTypeSelect(){
	$('#eventType').multiselect("update");           
	var eventType = $("#eventType").multiselect("MyValues");
	$("#eventTypeInput").attr("value",eventType);
}
function searchData() {
	setDefaultQueryTime();
//	$("#startTime").val(new Date(new Date().getTime()-7 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
//	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
	$('#table').grid('reload');
}
//导出
function excelData() {
	var url = top.pt.ctx+'/web/module/exportFun/SignalLogSearch.action';
	 $('#filterForm').attr("action", url).submit();
}
//缺省查询时间
function setDefaultQueryTime(){
	$("#startTime").val(new Date(new Date().getTime()-7 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
}

//设备类型选择
function deviceTypeSelect(){
	$('#deviceType').multiselect("update");           
	var deviceType = $("#deviceType").multiselect("MyValues");
	console.log(deviceType);
	$("#deviceTypeInput").attr("value",deviceType);
}

