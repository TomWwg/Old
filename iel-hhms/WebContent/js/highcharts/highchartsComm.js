
var SplintChart = {
	chart : {
		type : 'spline'
	},
	title : {
		text : ''
	},
	yAxis : {
		title : {
			text : '温度(℃)'
		},
		endOnTick : true,
		lineWidth : 1,
		lineColor : 'black',
		gridLineWidth : 0
	// 设置网格宽度
	},
	xAxis : {
		title : {
			text : '长度(m)'
		},
		lineWidth : 1,
		lineColor : 'black',
		gridLineWidth : 0,
		 labels: {
             align: 'right'   //刻度值显示的位置：left、center、right
         }
	// 设置网格宽度
	}, 
	legend: {
        enabled: true
    },
	tooltip : {
		enabled : true,
		formatter : function() {
			return this.x + 'm:' + this.y + '℃';
		}
	},
	plotOptions : {
		series : {
			cursor : 'pointer',
			line : {
				dataLabels : {
					enabled : false
				},
				enableMouseTracking : false
			},
			point : {
				allowPointSelect : true,// 是否允许选中点
				animation : true,// 是否在显示图表的时候使用动画
				cursor : 'pointer',// 鼠标移到图表上时鼠标的样式
				dataLabels : {
					enabled : true,// 是否在点的旁边显示数据
					rotation : 0
				},
				enableMouseTracking : true,// 鼠标移到图表上时是否显示提示框
				events : {// 监听点的鼠标事件
					click : function(e) {

					}
				}
			}
		},
	    spline: {
	        marker: {
	            enabled: false
	        }
	    }
	}
};

var ColumnChart = {
	chart : {
		type : 'column'
	},
	title : {
		text : ''
	},
	yAxis : {
		title : {
			text : '温度(℃)'
		},
		endOnTick : true,
		lineWidth : 1,
		lineColor : 'black',
		gridLineWidth : 0
	// 设置网格宽度
	},
	xAxis : {
		title : {
			text : '长度(m)'
		},
		lineWidth : 1,
		lineColor : 'black',
		gridLineWidth : 0
	// 设置网格宽度
	},
	tooltip : {
		enabled : true,
		formatter : function() {
			return this.x + 'm:' + this.y + '℃';
		}
	},
	plotOptions : {
		series : {
			cursor : 'pointer',
			line : {
				dataLabels : {
					enabled : false
				},
				enableMouseTracking : false
			},
			point : {
				allowPointSelect : true,// 是否允许选中点
				animation : true,// 是否在显示图表的时候使用动画
				cursor : 'pointer',// 鼠标移到图表上时鼠标的样式
				dataLabels : {
					enabled : true,// 是否在点的旁边显示数据
					rotation : 0
				},
				enableMouseTracking : true,// 鼠标移到图表上时是否显示提示框
				events : {// 监听点的鼠标事件
					click : function(e) {

					}
				}
			}
		}
	}
};

function hDateFormat1(sDate){
	if(sDate == null || sDate == "" || sDate == undefined){
		return "";
	}
//	var format = ('%Y-%m-%d %H:%M:%S');
//	var date1 =Highcharts.dateFormat(format,new Date(sDate));
	//////IE7对js解析不支持/////////////////
	//console.log(sDate);
	sDate = sDate.replace("T"," ");
	return sDate;
}


function hDateFormat2(sDate){
	if(sDate == null || sDate == "" || sDate == undefined){
		return "";
	}
//	var format = ('%Y年%m月%d日%H时%M分%S秒');
//	var date1 =Highcharts.dateFormat(format,new Date(sDate));
	//////IE7对js解析不支持/////////////////
	sDate = sDate.replace("-","年");
	sDate = sDate.replace("-","月");
	sDate = sDate.replace("T","日");
	sDate = sDate.replace(":","时");
	sDate = sDate.replace(":","分");
	sDate += "秒";
	return sDate;
}
