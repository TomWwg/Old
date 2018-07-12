var tree;
var node;
var rows=30;
// DOM加载完后执行
$(function() {
	if(window.screen.height>1000){
		$(".datatable").height("100%");
		rowN=20;
	}else{
		$(".datatable").height("81%");
		rowN=10;
	} //根据屏幕的分辨率来设置
	//setGridHeight4();
	$('#table').grid({
		  colName: ['设备类型', '设备编号', '设备状态','所属','修改时间'],
		  colModel: [
            //{name: 'name', index:'name', style: {'textAlign': 'center'}},
            {name: 'typeName', index:'typeName',sortable:false, style: {'textAlign': 'center'}},
            {name: 'no', index:'no', style: {'textAlign':'center'}},
		    {name: 'deviceStatus', index:'deviceStatus', sortable:true, style:{'textAlign': 'center'},showTitle :false,
              	formart : function(dataRow, data, fn){
              		if(dataRow == 0){ 
              			return '<a title="" style= "cursor:default;text-decoration:none;"><span class="ico i-checked"></span><span style="color:#4cbc6e">正常</span></a>';
              		}else {
              			return '<a title="" style= "cursor:default;text-decoration:none;"><span class="ico i-disabled"></span><span style="color:#fe1e1e">欠压</span></a>';
              		}
  		  		}
		    },
		    {name: 'showName', index:'showName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'updateTime', index:'updateTime', style: {'textAlign': 'center'}}
		  ],
		  headerFixed : true,
		  rowNum: rows,
		  rowMark : true,
		  height : '100%',
		  sortname: 'updateTime',
		  sortorder: 'desc',
		  ajaxOptions : {
		     type : 'post'
		  } ,
		  dataType: 'ajax',
		  url : 'queryAlarmListAjax.action',
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
				url: top.pt.ctx+"/web/module/getAllTree.action?isTreeOpen=false",
				autoParam: ["id", "label"],
				dataType :"json"
			},
			callback : {
				//树加载成功的之后
				onAsyncSuccess: function(event, treeId, treeNode, msg){
					if(node != null){
						tree.selectNode(node);
					}else{
						node = tree.getNodeById($("#topGroupId").val());
						tree.selectNode(node);
					}
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
});
//查询
$("#data_filtering").click(function(){
	$('#table').grid('reload');
});

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