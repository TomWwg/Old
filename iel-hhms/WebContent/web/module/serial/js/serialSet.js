
// DOM加载完后执行
$(function() {
	
});  


//查询
$("#data_filtering").click(function(){
	var staffId = $("#staffId").val();
	$.ajax({
		url : 'getOrgTree.action?isTreeOpen=false',
		data : {
			staffId : staffId
     	},
		success : function(data, callback){
			var orgNode = data.orgNode;
			 var showlist = $("<ul id='org' style='display:none'></ul>");
               showall(orgNode, showlist);
               $("#jOrgChart").append(showlist);
               $("#org").jOrgChart( {
                  chartElement : '#jOrgChart',//指定在某个dom生成jorgchart
                  dragAndDrop : false //设置是否可拖动
               });
			},
		dataType : "json"
	});
	
});




