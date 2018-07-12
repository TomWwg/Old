var rows=30;
$(function(){
	setGridHeight2();//设置列表高度自适应
	$("#startTime").val(new Date().format("yyyy-MM-dd 00:00:00"));
	$("#endTime").val(new Date().format("yyyy-MM-dd 23:59:59"));
	$('#table').grid({
		  colName: ['日志类型', '操作用户','操作内容', '操作时间'],
		  colModel: [{name: 'type', index:'type', sortable:false, style: {'textAlign': 'center'}, width:"15",
		              formart : function(d, fn){
		            	  var arr = ["用户登录","用户退出","设备添加"];
		               	  return arr[d-1];	//这个设计的比较巧妙，根据索引返回数组中的某值；
		    }},
		    {name: 'optUser', index:'optUser', sortable:false, width:"15", style: {'textAlign': 'center'}},
		    {name: 'remark', index:'remark', sortable:false, width:"15", style: {'textAlign':'center', 'white-space':'pre-wrap'}},
		    {name: 'createTime', index:'createTime',width:"15", style: {'textAlign': 'center'}}
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
		  url : 'findPageList.action',
		  ajaxDynamic : function(){ 
	  		  //这里放置你的查询条件，调用$('#dataT').grid('reload');我们会自动拉取form里的数据 return $('..').formToArray();
	  		  return $('#filterForm').formToArray();
	  	  },
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  ajaxResponseReader: {page: 'pageNo', rows: 'result',records: 'total',totalPage: 'totalPage'}
	});
	
	$("#filter_btn").click(function(){
		$('#table').grid('reload');
	})
	
	$(":reset").click(function(){
		$('#table').grid('reload');
		return true;
	})
    	
});
//导出
function excelData() {
	var url = top.pt.ctx+'/web/module/exportFun/ExportForLogCheck.action';
	 $('#filterForm').attr("action", url).submit();
}