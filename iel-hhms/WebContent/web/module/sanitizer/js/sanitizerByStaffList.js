var tree;
var node;
var rowN=10;
// DOM加载完后执行
$(function() {
	setGridHeight2();//设置列表高度自适应
	$("#startTime").val(new Date().format("yyyy-MM-dd 00:00:00"));
	$("#endTime").val(new Date().format("yyyy-MM-dd 23:59:59"));
	$("#staffType").multiselect({
		selectedList: 5
	});
	$('#staffType').val( $("#staffTypeInput").val().split(',') );
    $('#staffType').multiselect("refresh");
	$('#table').grid({
		colName: ['人员', '科室', '类别','胸牌','洗手液使用量'],
		  colModel: [
		    {name: 'docName', index:'docName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'departName', index:'departName', style: {'textAlign': 'center'}},
		    {name: 'docType', index:'docType', style: {'textAlign': 'center'}},
		    {name: 'rfid', index:'rfid',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'useNum', index:'useNum',sortable:false, style: {'textAlign': 'center'}}
		  ],
		  multiSelect : false,//是否显示复选框
		  headerFixed : true,
		  rowNum: rowN,
		  rowMark : true,
		  height : '100%',
		  sortname: 'createTime',
		  sortorder: 'desc',
		  ajaxOptions : {
		     type : 'post'
		  } ,
		  dataType: 'ajax',
		  url : 'sanitizerByStaffListAjax.action',
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