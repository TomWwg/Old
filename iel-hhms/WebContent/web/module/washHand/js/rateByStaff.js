var tree;
var node;
var rows=30;
// DOM加载完后执行
$(function() {
	setGridHeight2();//设置列表高度自适应
	setDefaultQueryTime();
//	$("#startTime").val(new Date().format("yyyy-MM-dd 00:00:00"));
//	$("#endTime").val(new Date().format("yyyy-MM-dd 23:59:59"));
	$("#staffType").multiselect({
		selectedList: 2
	});
	$('#staffType').val( $("#staffTypeInput").val().split(',') );
    $('#staffType').multiselect("refresh");
	$('#table').grid({
		  colName: ['员工姓名', '科室', '人员类别','胸牌编号','已执行(次)','未执行(次)','依从率(%)'],
		  colModel: [
		    {name: 'docName', index:'docName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'departName', index:'departName', style: {'textAlign': 'center'}},
		    {name: 'docType', index:'docType', style: {'textAlign': 'center'}},
		    {name: 'rfid', index:'rfid',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'normalNum', index:'normalNum',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'errorNum', index:'errorNum',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'rate', index:'rate',sortable:false, style: {'textAlign': 'center'}}
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
		  url : 'rateByStaffListAjax.action',
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
	$("#data_filtering").click(function() {
		var treeId = $("#treeId").val();
		if (treeId == null || treeId == "") {
			jAlert('请选择组织左右的组织树', '提示', 'attention');
			return;
		}
		$('#table').grid('reload');
	});	      
});  

function staffTypeSelect(){
	$('#staffType').multiselect("update");           
	var staffType = $("#staffType").multiselect("MyValues");
	$("#staffTypeInput").attr("value",staffType);
	//加载人员列表
	$.post(top.pt.ctx+"/web/module/getStaffByType.action", {
		staffType : staffType,
		groupTreeId : $("#treeId").val()
	   }, function(data) {
		 /*  $("#staffName").attr("list","'1':'na'");*/
		   $("#staffName").empty();
		   $('#staffName').html(data.msg).show();
	}, 'json');
}

/*function staffNameSelect(){
	$('#staffName').multiselect("update");           
	 var staffName = $("#staffName").multiselect("MyValues");
	 $("#staffNameInput").attr("value",staffName);
}*/
//导出
function excelData() {
	var url = top.pt.ctx+'/web/module/exportFun/ExportForRateByStaff.action';
	$('#filterForm').attr("action", url).submit();
}

function setDefaultQueryTime(){
	$("#startTime").val(new Date(new Date().getTime()-7 * 24 * 3600 * 1000).format("yyyy-MM-dd hh:mm:ss"));
	$("#endTime").val(new Date().format("yyyy-MM-dd hh:mm:ss"));
}



