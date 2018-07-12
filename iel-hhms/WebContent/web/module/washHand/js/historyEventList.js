var tree;
var node;
var rows=30;
// DOM加载完后执行
$(function() {
	//console.log(rows);
	setGridHeight4();//设置列表高度自适应
	//setInterval("searchData()", 10000 ); 注释掉自动刷新,原因是会自动覆盖掉用户查询结果
	setDefaultQueryTime();
//	$("#startTime").val(new Date(new Date().getTime()-30 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
//	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
	$("#eventType").multiselect({
		selectedList: 1
	});
	$("#jobType").multiselect({
		selectedList: 1
	});
	$("#washHandStatus").multiselect({
		selectedList: 1
	});
	/*$("#rfidStatus").multiselect({
		selectedList: 2
	});*/
	$("#eventTypeInput").attr("value","0003,0004,0007,0008,0102,0103,0110,0001,0018,0109,0045");
	/*$("#rfidStatusInput").attr("value", "1,2,3,4,5,6,7,8,9,10");*/
    $('#eventType').val( $("#eventTypeInput").val().split(',') );
    $('#eventType').multiselect("refresh");
    //$("#jobTypeInput").attr("value","1,2,3,4,5");
    $('#jobType').val( $("#jobTypeInput").val().split(',') );
    $('#jobType').multiselect("refresh");
    $("#washHandStatusInput").attr("value","1,2,3,4");
    $('#washHandStatus').val( $("#washHandStatusInput").val().split(',') );
    $('#washHandStatus').multiselect("refresh");
    /*$('#rfidStatus').val( $("#rfidStatusInput").val().split(',') );
    $('#rfidStatus').multiselect("refresh");*/
    //console.log(rows);
	$('#table').grid({
		//yxnne 需要
		//原来的顺序：'人员','科室', '胸牌', '事件','胸牌状态','识别器','发生时间'
		//改成colName: ['人员','胸牌状态', '事件', '识别器','胸牌','科室','发生时间'],
		colName: ['员工姓名','人员类别', '胸牌编号', '手卫生状态','事件','设备编号','发生时间','备注'],
		  colModel: [
          {name: 'docName', index:'docName',sortable:false, style: {'textAlign': 'center'},
        	  formart : function(dataRow, data, fn){
	          		return '<a style="text-decoration:none"; title='+data.departName+'一'+data.sex+'>'+dataRow+'</a>';
			  }
          },
          
          {name: 'staffTypeName', index: 'staffTypeName', sortable:false, style: {'textAlign': 'center'}},
          
          {name: 'rfid', index:'rfid', style: {'textAlign': 'center'}},
          
          {name: 'rfidStatusName', index:'rfidStatusName', style: {'textAlign': 'center'}},//胸牌状态
         
          {name: 'eventTypeName', index:'eventTypeName', style: {'textAlign': 'center'}},//事件
          
          {name: 'deviceInfo', index: 'deviceInfo',style: {'textAlign': 'center'},
        	  formart : function(dataRow, data, fn){
	          		return '<a style="text-decoration:none"; title='+data.location+'>'+dataRow+'</a>';
			  }
          },
          
          //{name: 'location', index: 'location', sortable:false, style: {'textAlign': 'center'}},
          
          {name: 'createTime',  index:'createTime', style: {'textAlign': 'center'}},
          
          {name: 'remark', index:'remark',sortable:false, style: {'textAlign': 'center'}}
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
		  url : 'eventListAjax.action',
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
				url: top.pt.ctx+"/web/module/getDepartmentTree.action?isTreeOpen=false",
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
					$.post(top.pt.ctx+"/web/module/getStaff.action", {
						groupTreeId : treeNode.id
					   }, function(data) {
							$("#staffId").empty();
							$('#staffId').html(data.msg).show();  
								
					}, 'json');
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
	tree = $("#zTreea").tree(setting);
	//查询
	$("#data_filtering").click(function(){
		$('#table').grid('reload');
	});	      
});  
function searchData() {
	setDefaultQueryTime();
//	$("#startTime").val(new Date(new Date().getTime()-30 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
//	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
	$('#table').grid('reload');
}
function eventTypeSelect(){
	$('#eventType').multiselect("update");           
	var eventType = $("#eventType").multiselect("MyValues");
	$("#eventTypeInput").attr("value",eventType);
}

function rfidStatusSelect(){
	$('#rfidStatus').multiselect("update");           
	 var rfidStatus = $("#rfidStatus").multiselect("MyValues");
	 $("#rfidStatusInput").attr("value",rfidStatus);
	 if(rfidStatus==""){//排错预处理，赋值一个无效的胸牌状态值
		 $("#rfidStatusInput").attr("value","15");
	 }
}
//导出
function excelData() {
	var url = top.pt.ctx+'/web/module/exportFun/exportEventByTime.action';
	 $('#filterForm').attr("action", url).submit();
}

function setDefaultQueryTime(){
	$("#startTime").val(new Date(new Date().getTime()-30 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
}

//人员类别选择
function jobTypeSelect(){
	$("#jobType").multiselect("update");
	var jobTypes=$("#jobType").multiselect("MyValues");
	$("#jobTypeInput").attr("value",jobTypes);
}

//手卫生状态选择
function washHandStatusSelect(){
	$("#washHandStatus").multiselect("update");
	var jobTypes=$("#washHandStatus").multiselect("MyValues");
	$("#washHandStatusInput").attr("value",jobTypes);
}
/* 就是浏览器窗口的高度，兼容ie */
window.getHeight= function(){  
    if(window.innerHeight!= undefined){  
        return window.innerHeight;  
    }  
    else{  
        var B= document.body, D= document.documentElement;  
        return Math.min(D.clientHeight, B.clientHeight);  
    }  
}  
//条件查询,针对本页覆盖修改下
setGridHeight4= function(){  
	//console.log("height",getHeight());
	$(".datatable").height(getHeight()-135+"px");
	if(window.screen.height>1000){
		rowN=20;
	}else{
		rowN=10;
	}; //根据屏幕的分辨率来设置
}

