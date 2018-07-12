$(function() {
	searchData();
	setInterval("searchData()", 1000*60);	
});

function searchData(){
	var staffId = -1;
	$.ajax({
		url : 'getOrgTree.action',
		data : {
			staffId : staffId
     	},
		success : function(data, callback){
			var orgNode = data.orgNode;
			var locationId = data.locationId;
			 var showlist = $("<ul id='org' style='display:none'></ul>");
               showall(orgNode, showlist);
               $("div #jOrgChart").html("");
               $("#jOrgChart").append(showlist);
               $("#org").jOrgChart( {
                  chartElement : '#jOrgChart',//指定在某个dom生成jorgchart
                  dragAndDrop : false //设置是否可拖动
               });
               $("#org").html("");

               $(".node").hover(function(e){
            	   var nodeId = $(this).attr("id");
            	   $.ajax({
            		   url : 'getStaffList.action',
            		   data : {
            			   nodeId : nodeId
            		   },
            		   success : function(data,callback){
            			   var staffList = data.staffList;
            			   var staffName = "";
            			   for(var i =0;i < staffList.length;i++){
            				   if(i == staffList.length-1){
            					   staffName += staffList[i].name;
            				   }else{
            					   staffName += staffList[i].name + "，";
            				   }
            			   }
            			   var html = "<div id='staffName'>"+staffName+"</div>";
            			   $("#"+nodeId).append(html);
            			   $("#staffName").css({         				    
            				      "border": "1px solid",
            				   	  "position": "absolute",
            			          "top": (e.pageY) + "px",  
            			          "left": (e.pageX) + "px"
            			     }).show("fast"); //设置提示框的坐标，并显示
            			 
            		   },
            		   dataType : "json"
            	   });
            	  },function(){
            		  $("#staffName").remove(); 
            	  });
			},
		dataType : "json"
	});
}

//动态生成组织树
function showall(orgNode, parent){
		 var a ;
		 var b =0;
	   if(orgNode.children != null){
		   b = orgNode.children.length;
		   a = orgNode.children;
	   }	    
		//如果有子节点，则遍历该子节点
		if( b > 0){
			//创建一个子节点li
			var li = $("<li id="+orgNode.id+"></li>");
			//将li的文本设置好，并马上添加一个空白的ul子节点，并且将这个li添加到父亲节点
			$(li).append(orgNode.name).append("<ul></ul>").appendTo(parent);
			for(var i =0;i < b;i++){					
				//将空白的ul作为下一个递归遍历的父亲节点传入
				showall(orgNode.children[i],$(li).children().eq(0))
			}
				
		}else{
			//如果该节点没有子节点，则直接将该节点li以及文本创建好直接添加到父亲节点中
			 $("<li id="+orgNode.id+"></li>").append(orgNode.name).appendTo(parent);
		}
   
}