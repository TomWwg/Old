var chart; 
// DOM加载完后执行
$(function() {
	if(window.screen.height>1000){
	}else{
		$(".img").width("50px");
		$(".img").height("50px");
	} //根据屏幕的分辨率来设置
	//Highcharts.setOptions({colors:["#00FF00","#FF0000","#33a3dc","#faa755","#8A2BE2"]});
	$('#table').grid({
		  colName: ['员工姓名', '科室','胸牌编号','事件','发生时间'],
		  colModel: [
		    {name: 'docName', index:'docName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'departName', index:'departName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'rfid', index:'eventTypeName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'eventTypeName', index:'eventTypeName',sortable:false, style: {'textAlign': 'center'}},
		    {name: 'createTime', index:'createTime',sortable:false, style: {'textAlign': 'center'}}
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
		  url : 'screenListAjax.action',
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  rowMark : false,
		  headTitle : false,
		  ajaxDynamic : function(){ 
	  		  return $('#filterForm').formToArray();
	  	  },
		  ajaxResponseReader: {page:'pageNo', rows:'result', records: 'total', totalPage:'totalPage'},
		  //loadtext:'12345'
		  //loadui:"disable" // 尝试使用 jqGrid的方法，发现这个框架封装了
	});
	
	$('#tableNotice').grid({
		  colName: ['公告名称', '发布时间'],
		  colModel: [
		    {name: 'name', index:'name',sortable:true, style: {'textAlign':'center'},
		      formart : function(dataRow, data, fn){
			  	return '<a href="javascript:void(0); " onclick="showNotice('+data.id+')" style="text-decoration:underline;">'+dataRow+"</a>";
		  	  }
		    },
		    {name: 'updateTime', index:'updateTime',sortable:false, style: {'textAlign': 'center'}}
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
		  url : 'noticeListAjax.action',
		  pager: '#dataTablePage',
		  pagerFixed: true,
		  rowMark : false,
		  headTitle : false,
		  ajaxDynamic : function(){ 
	  		  return $('#filterForm').formToArray();
	  	  },
		  ajaxResponseReader: {page:'pageNo', rows:'result', records: 'total', totalPage:'totalPage'}
	});
	setData();
	setInterval("freshTable()", 2000);//毫秒
	setInterval("freshChart()", 60*60*1000);//毫秒
});  
function freshTable() {
	$('#table').grid('reload');
} 
function freshChart() {
	$('#tableNotice').grid('reload');
	setData();	
}  
function setData() {
	var url = "rateChart.action";
	$.ajax({
		type : "POST",
		url : url,
		dataType:"json",
		data : {
		},
		success : function(data) {
			var staffTypeRateList = data.staffTypeRateList;
			var departmentRateList = data.departmentRateList;
			var washHandMomentCompareList=data.washHandMomentCompareList;
			//setChartByStaff(allWorkList,docWorkList,pNurseWorkList,nurserWorkList);
			var fullScreenData = data.fullScreenData;
			setChart(fullScreenData);//当年洗手记录
			var deviceAlarmList = data.deviceAlarmList;
			setAlarmChart(deviceAlarmList);
			setStaffChart(staffTypeRateList);
			setDepartmentChart(departmentRateList);
			setMomentChart(washHandMomentCompareList);//测试洗手时机
		}
	});
}
function setChartByStaff(allWorkList,docWorkList,pNurseWorkList,nurserWorkList) {
	var rateAll =[],docAll =[],pNurseAll =[],nurserAll =[],timeName =[];
	for ( var i = 0; i < allWorkList.length; i++) {
		var o = allWorkList[i];
		var time = o.time;
		var rate = o.rate;
		timeName.push(time);
		rateAll.push(rate);
	}
	for ( var i = 0; i < docWorkList.length; i++) {
		var o = docWorkList[i];
		var rate = o.rate;
		docAll.push(rate);
	}
	for ( var i = 0; i < pNurseWorkList.length; i++) {
		var o = pNurseWorkList[i];
		var rate = o.rate;
		pNurseAll.push(rate);
	}
	for ( var i = 0; i < nurserWorkList.length; i++) {
		var o = nurserWorkList[i];
		var rate = o.rate;
		nurserAll.push(rate);
	}
	chart = new Highcharts.Chart({ 
        chart: { 
        	type: 'line',
            renderTo: 'chart_combo_staff43' //关联页面元素div#id 
        }, 
        credits: { //取消右下角的highcharts.com的
            enabled:false
        },
        title: {  //图表标题 
            text: '' 
        }, 
        xAxis: { //x轴 
        	categories: timeName,
            labels:{y:18},  //x轴标签位置：距X轴下方18像素 
            labels: {
                style: {
                    color: '#000000'//颜色
                }
            },

        }, 
        yAxis: {  //y轴 
            title: {text: '百分比'}, //y轴标题 
            min:0,
            max:100,
            lineWidth: 2, //基线宽度 
            labels: {
                style: {
                    color: '#000000'//颜色
                }
            },
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
        legend: {
            layout: 'vertical',
            align: 'left',
            verticalAlign: 'top',
            borderWidth: 0,
            x: 20,
            y: 20,
        },
        series: [{ //数据列 
            name: '全院', 
            data: rateAll 
        }, 
        { 
            name: '医生', 
            data: docAll 
        }, 
        { 
            name: '护师', 
            data: pNurseAll 
        }, 
        { 
            name: '护士', 
            data: nurserAll 
        }]
        
    }); 
}

function setChart(fullScreenData) {
	Highcharts.setOptions({colors:["#77FCA8","#3995B9","#FFBF79","#889AFB","#DF8186"]});
	var effectWash = fullScreenData.effectWash;
	var inRoom = fullScreenData.inRoom;
	var outRoom = fullScreenData.outRoom;
	var beforeInBed = fullScreenData.beforeInBed;
	var longOutBed = fullScreenData.longOutBed;
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo', //关联页面元素div#id 
        }, 
        credits: { //取消右下角的highcharts.com的
            enabled:false
        },
        title: {
            text: '近30日内手卫生执行状况'
        },
        tooltip: {
        	formatter: function() {
                return '<b>'+ this.point.name +'</b>: '+ Highcharts.numberFormat(this.percentage,1)+' %';
             }
        	/*formatter: function() {
                return '<b>'+ this.point.name +'</b>: '+ Highcharts.numberFormat(this.percentage, 2) +'% ';
             }*/
         },
        legend: {
        	enabled: false ,
            layout: 'vertical',
            align: 'left',
            verticalAlign: 'top',
            borderWidth: 0,
            x: 10,
            y: 50,
        },
        series: [
        { 
            type: 'pie', //饼状图 
            name: '', 
            innerSize:'50%',
            data: [{ 
                name: '执行手卫生', 
                y: effectWash, 
            }, 
            { 
                name: '离开病区未手卫生', 
                y: outRoom, 
            }, 
            { 
                name: '接近患者前未手卫生', 
                y: beforeInBed, 
            },
            { 
                name: '接触患者后未手卫生', 
                y: longOutBed, 
            }], 
           //size: 200 //饼状图直径大小 
        }],
        plotOptions:
        {
        	pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
            	distance:1,
            enabled: true,         
            connectorColor: '#4572A7',
            style: {
            		color: '#698B69',
            		fontSize:'10px',  //字体
            		width:'50px'
            },
            format: '<b>{point.name}</b>: {point.y} 次', 
            /* formatter: function() {
            		return '<b> point.name'+Highcharts.numberFormat(this.percentage,2) +'%</b>';
           	}*/
            	
                
            },
            showInLegend: true
          }
        }
        
    }); 
}
//修改公告信息
function showNotice(dataId){
	$.dialog({
		width : 700,
		height : 400,
		draggable: true,
		maybeRefresh : true,
		id : 'editframe',
		title : '公告信息',
		iframeSrc : top.pt.ctx+'/web/module/notice/showNotice.action?entity.id='+dataId,
		buttons : [{
			text : '确定',
			'class' : 'bPrimary',
			"id" : 'modify_notice_btn_ok',
			click : function(dialog, callback) {
				dialog.close();
			}
		}]
	});
}

//收藏配置
function favoriteDialog(){
	$.dialog({
        width: 500,
        height: 600,
        draggable: true,
        maybeRefresh: true,
        id: 'favoriteframe',
        title: '收藏配置',
        iframeSrc: top.pt.ctx+'/web/module/user/addFavoriteDialog.action',
        buttons: [{
            text: '确定',
            'class': 'bPrimary',
            "id": 'add_favorite_btn_ok',
            click: function(dialog, callback){
            	$.ajax({
					url : 'module/user/addFavorite.action',
					data : top.frames["favoriteframe"].$("#showForm").serialize(),
					success : function(data, callback){
						if (data.success == true) {
							dialog.close();
							//jAlert("收藏配置成功", '提示', 'attention');
							 jConfirm('收藏配置成功，将要重新加载界面','提示',function(r){
						        if(r){
						           history.go(0);
						        }
						   });
							
						}else {
							jAlert("收藏配置失败："+data.msg, '错误', 'error');
						}
					},
					dataType : "json"
				});
            }
        }, {
            text: '取消',
            click: function(dialog, callback, el){
            	dialog.close();
            }
        }]
    });
}



function setAlarmChart(deviceAlarmList) {
	Highcharts.setOptions({colors:["#52DDB6","#D0CDC9"]});
	var normal = [],error =[],name =[];
	for ( var i = 0; i < deviceAlarmList.length; i++) {
		var o = deviceAlarmList[i];
		var nameTemp = o.name;
		var normalNum = o.normal;
		var errorNum = o.error;
		name.push(nameTemp);
		normal.push(normalNum);
		error.push(errorNum);
	}
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo_alarm' //关联页面元素div#id 
        }, 
        credits: { //取消右下角的highcharts.com的
            enabled:false
        },
        title: {  //图表标题 
            text: '' 
        }, 
        xAxis: { //x轴 
        	categories: name,
            labels:{y:18},  //x轴标签位置：距X轴下方18像素 
            labels: {
                style: {
                    color: '#333333',//颜色
                    fontSize:'14px'  //字体
                }
            },

        }, 
        yAxis: {  //y轴 
        	allowDecimals:false,     //不显示小数
            title: {text: '个数', 
            	style: {
                color: '#333333',//颜色
                fontSize:'14px'  //字体
            }}, //y轴标题 
            lineWidth: 2, //基线宽度 
            labels: {
                style: {
                    color: '#333333',//颜色
                    fontSize:'14px'  //字体
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
        legend: {
            layout: 'vertical',
            align: 'left',
            verticalAlign: 'top',
            borderWidth: 0,
            x: 10,
            y: 60,
        },
        series: [{ //数据列 
            type: 'column', 
            name: '正常', 
            data: normal 
        }, 
        { 
            type: 'column', 
            name: '欠压', 
            data: error 
        }],
         plotOptions:
         {  
             column: //柱形图
             {
             	dataLabels:{
                     enabled:true,
                     style: {
                       	color: '#698B69',
                           fontSize:'12px'  //字体
                       }
             	},
             	//pointPadding: 1,
                 //borderWidth: 0,
             	//pointWidth: 50,                //柱子的大小(会影响到柱子的大小)
             },
         }
        
    }); 
}



function setStaffChart(staffTypeRateList) {
	var staffTypeNames=[],rates=[];
	if(staffTypeRateList.length < 3){
		for(var i=0;i<staffTypeRateList.length;i++){
			var o=staffTypeRateList[i];
			var staffTypeName=o.name;
			var rate=parseFloat(o.rate);
			staffTypeNames.push(staffTypeName);
			rates.push(rate);
		}
		staffTypeNames.push("医生");
		staffTypeNames.push("护师");
		staffTypeNames.push("护士");
		staffTypeNames = staffTypeNames.unique();
		for(var i = 0; i <= staffTypeNames.length - rates.length + 1; i++){
			rates.push(0);
		}
	} else {
		for(var i=0;i<staffTypeRateList.length;i++){
			var o=staffTypeRateList[i];
			var staffTypeName=o.name;
			var rate=parseFloat(o.rate);
			staffTypeNames.push(staffTypeName);
			rates.push(rate);
		}
	}
	var values = [];
	var count = 0;
	Highcharts.setOptions({colors:["#14B6F6"]});
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo_department', //关联页面元素div#id 
            polar: true
        }, 
        credits: { //取消右下角的highcharts.com的
            enabled:false
        },
        title: {  
        		//y: -0.5,//标题的数值偏移
            text: '近30日内人员类别依从率'
        },  
  
        pane: {  
            size: '82%'  
        },  
        yAxis: {  
        	//minorTickInterval: 'auto',
        	//tickAmount: 5, // 设置刻度的个数
        	tickPositioner: function () {
                var positions = [],
                    tick =0,
                    increment = Math.ceil((this.dataMax - this.dataMin + 4) / 4);
					console.log("this.dataMax",this.dataMax);
					console.log("this.dataMin",this.dataMin);
                for (tick; tick - increment <= this.dataMax; tick += increment) {
                    positions.push(tick);
                }
                return positions;
            },
            gridLineInterpolation: 'polygon',  
            lineWidth: 0,  
            //max:100, // 定义最大值  
            min:0 ,// 定义最小值
            labels: { //不显示刻度值
                enabled: false
            },
                 
        }, 
        xAxis: {  
            //categories: [/*'全院',*/ '医生', '护师', '保洁', '护工', '护士 ',],  
        	categories:staffTypeNames,
            tickmarkPlacement: 'on',  
            lineWidth: 0,
           
  	        labels: {
                style: {
                    color: '#14B6F6',//颜色
                    fontSize:'15px'  //字体
                },
                //控制x轴坐标，动态修改其值
                formatter: function () {
                		//有六个值 这里对7求余是有原因的 ，框架每次刷新会调用7次这里
                		var info = rates[count % (rates.length + 1)]
                		count ++ ;
                    return this.value +' '+ info+'%';
                }
            },
        },  
        legend: {
        		enabled: false 
        },
 
        tooltip: {
          	enabled:false //是否显示
    	   },
        series: [{  
        	type: 'area',
            name:'',  
            data:rates,
            pointPlacement: 'on'  ,
            	shadow: true
        }] ,
        plotOptions: {
        	area: {
                dataLabels: {
                    enabled: false,
                    formatter: function() {
                        return this.point.y+'%';  // 重新设置节点显示数据
                      },
                      style: {
                        	color: '#698B69',
                            fontSize:'12px'  //字体
                        }
                },
            }
        },
        
    }); 
}

function setDepartmentChart(departmentRateList) {
	var departName=[],rate=[];
	if(departmentRateList.length < 3){
		for(var i=0;i<departmentRateList.length;i++){
			var o=departmentRateList[i];
			var tempDepartName=o.depart;
			var tempRate=parseFloat(o.rate);
			departName.push(tempDepartName);
			rate.push(tempRate);
		}
		departName.push("其他");
		rate.push(0);
	} else if(departmentRateList.length < 4){
		for(var i=0;i<departmentRateList.length;i++){
			var o=departmentRateList[i];
			var tempDepartName=o.depart;
			if(tempDepartName != "感控科"){
				var tempRate=parseFloat(o.rate);
				departName.push(tempDepartName);
				rate.push(tempRate);
			}
		}
		departName.push("其他");
		rate.push(0);
	}else{
		for(var i=0;i<departmentRateList.length;i++){
			var o=departmentRateList[i];
			var tempDepartName=o.depart;
			if(tempDepartName != "感控科"){
				var tempRate=parseFloat(o.rate);
				departName.push(tempDepartName);
				rate.push(tempRate);
			}
		}
	}
	
	Highcharts.setOptions({colors:["#52DDB6"]});
	var count = 0;
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo_staff', //关联页面元素div#id 
            polar: true
        }, 
        credits: { //取消右下角的highcharts.com的
            enabled:false
        },
        title: {  
            text: "近30日内部门依从率"
        },  
  
        pane: {  
            size: '82%'  
        },  
  
        xAxis: {  
            categories: departName,  
            tickmarkPlacement: 'on',  
            lineWidth: 0,
           
  	        labels: {
                style: {
                    color: '#52DDB6',//颜色
                    fontSize:'15px'  //字体
                },
                formatter: function () {
	        		//多一个
	        		var info = rate[count % (rate.length	+1 )];
	        		count ++ ;
	        		//console.log("rate[count % (rate.length	+1 )---->",info);
	        		//console.log("count is now ---->",count);
	            return this.value +' '+ info+'%';
                }
            },
        },  
  
        yAxis: {  
        		//minorTickInterval: 'auto',
        		//minorTickInterval: 20,
        		//tickAmount: 5, // 设置刻度的个数
	        	tickPositioner: function () {
	                var positions = [],
	                    tick =0,
	                    increment = Math.ceil((this.dataMax - this.dataMin + 4) / 4);
	
	                for (tick; tick - increment <= this.dataMax; tick += increment) {
	                    positions.push(tick);
	                }
	                return positions;
	            },
        		gridLineInterpolation: 'polygon',  
        		lineWidth: 0,  
        		//max:50, // 定义最大值  
        		min:0 ,// 定义最小值
        		labels: { //不显示刻度值
        			enabled: false
        		},
        },  
        legend: {
        	enabled: false 
        },
 
        tooltip: {
          	enabled:false //是否显示
    	   },
        series: [{  
        	type: 'area',
            name: '',  
            data: rate,  
            pointPlacement: 'on'  ,
            shadow: true
        }] ,
        plotOptions: {
        	area: {
                dataLabels: {
                    enabled: false,
                    formatter: function() {
                        return this.point.y+'%';  // 重新设置节点显示数据
                      },
                      style: {
                      	color: '#698B69',
                          fontSize:'12px'  //字体
                      },
                },
            }
        },
        
    }); 
}

function setMomentChart(washHandMomentCompareList) {// 洗手时机
	var washBeforeCloseNick=washHandMomentCompareList[0];
	var washBeforeAsepticOperation=washHandMomentCompareList[1];	
	var washAfterCloseNick=washHandMomentCompareList[2];
	var washAfterCloseNickEnvri=washHandMomentCompareList[3];
	Highcharts.setOptions({colors:["#7EDDE4"]});
	chart = new Highcharts.Chart({ 
        chart: { 
            renderTo: 'chart_combo_moment', // 关联页面元素div#id
            type: 'bar'
        }, 
        credits: { // 取消右下角的highcharts.com的
            enabled:false
        },
        title: {  // 图表标题
            text: '近30日内手卫生时机对比' 
        }, 
        xAxis: {
        	      categories: ['接触患者前', '无菌操作前', '接触患者后', '接触患者环境后'],
        	      title: {
        	         text: null
        	      },
        	      gridLineWidth: 0,
        	      labels: {
                      style: {
                          color: '#333333',//颜色
                          fontSize:'16px'  //字体
                      }
                  },
        	   },
        yAxis: {
        	     title: {
   	               text: null
   	               },
        	      labels: {
        	         enabled:false //设置刻度是否显示
        	      },
        	      gridLineWidth: 0
        	   },
        tooltip: {
              	enabled:false //是否显示
        	   },
           legend: {
        	   enabled: false 
               },
         series: [{name: '',
        	        data: [washBeforeCloseNick,washBeforeAsepticOperation,washAfterCloseNick,washAfterCloseNickEnvri]}
        	   ],
        	   plotOptions: {
                   bar: {
                       dataLabels: {
                           enabled: true
                       }
                   }
               },
          plotOptions: {
        	      bar: {
        	    	  dataLabels:{
                          enabled:true, // dataLabels设为true
                          formatter: function() {
                              return this.point.y+'%';  // 重新设置节点显示数据
                            },
                            style: {
                              	color: '#698B69',
                                  fontSize:'12px'  //字体
                              }
                  	},
                  	pointPadding: 1,
                     borderWidth: 0,
                    pointWidth: 25,                //柱子的大小(会影响到柱子的大小)
        	      }
        	   },
    }); 
}


Array.prototype.unique = function(){
	var res = [];
	var json = {};
	for(var i = 0; i < this.length; i++){
 		if(!json[this[i]]){
			res.push(this[i]);
			json[this[i]] = 1;
		}
	}
	return res;
}