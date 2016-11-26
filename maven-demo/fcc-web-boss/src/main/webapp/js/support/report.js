/**
 * 报表js- highcharts-3.0.2,FusionCharts-3.3.1
 * @author 傅泉明
 * @version v1.0
 * 需要赋值 
 *   	var dataList;
 *		var dataTitle;
 */
 
// 设置图表标题
function setChartsTitle(title) {
	$('#container').show().window({
		modal : false,
		title : title,
		closable : true,
		closed: false
	});
}
// 取得FusionCharts json 数据
function getFusionChartsData(dataList) {
	var length = dataList.length;
	var fusionChartsData = [];
	for (var i = 0; i < length; i++) {
		var groupName = dataList[i].groupName;
		var count = dataList[i].count;
		if (groupName) {
		} else {
			groupName = '';
		}
		fusionChartsData.push({"label": groupName, "value": count})
	}
	return fusionChartsData;
}
function buildFusionCharts(chartsType, title) {
	var fusionChartsData = getFusionChartsData(dataList);
	var swf = 'Line.swf';
	if (chartsType == 'line') {
		swf = 'Line.swf';
	} else if (chartsType == 'column') {
		swf = 'Column3D.swf';
	} else if (chartsType == 'pie') {
		swf = 'Pie3D.swf';
	}
	$("#container").insertFusionCharts({
        swfUrl: "js/FusionCharts-3.2/" + swf,
        //width: "400",
        height: "95%", 
        renderer: "flash",
        //id: "myChartId",
        dataFormat: "json", 
        dataSource: { 
                "chart": { 
                          "caption" : title ,
                          "xAxisName" : dataTitle.html(),
                          "yAxisName" : "总数",
                          "numberPrefix" : "",
                          "showBorder" : '0',
                          "exportEnabled" : '1',
                          "exportFileName" : 'charts',
                          "exportAtClient" : '0',
                          "exportAction" : 'download',
                          "exportHandler" : 'manage/FCExporter'
                          
                          
                },
                "data" : fusionChartsData
       	}
 	});
}
var type_3d = '3D';
// 折线图
function lineCharts(title, basePath) {
	setChartsTitle('折线图')
	if (Tool.isFlash()) {
		/*buildFusionCharts('line', title);
		return;*/
	}
	var length = dataList.length;
	var categories = [];
	var counts = [];
	for (var i = 0; i < length; i++) {
		var groupName = dataList[i].groupName;
		if (groupName) {
		} else {
			groupName = '';
		}
		categories.push(groupName)
		counts.push(dataList[i].count)
	}
	$('#container').highcharts({
           chart: {
               type: 'line',
               marginRight: 130,
               marginBottom: 25
           },
           title: {
               text: title,
               x: -20 //center
           },
           subtitle: {
               text: 'Source: ' + basePath,
               x: -20
           },
           xAxis: {
               categories: categories
           },
           yAxis: {
               title: {
                   text: '条'
               },
               plotLines: [{
                   value: 0,
                   width: 1,
                   color: '#808080'
               }]
           },
           exporting:{
               filename: dataTitle.html() + '-折线图',
               url: 'manage/sys/hightchart/export.do' //这里是一个重点哦,也可以修改exporting.js中对应的url  
           },
           credits: {
			enabled: true,// 不生成logo
			text: basePath,
			href: '',
			position: {
				align: 'right',
				x: -10,
				verticalAlign: 'bottom',
				y: -5
			},
			style: {
				cursor: 'pointer',
				color: '#909090',
				fontSize: '9px'
			}
		},
           tooltip: {
               valueSuffix: '条'
           },
           legend: {
               layout: 'vertical',
               align: 'right',
               verticalAlign: 'top',
               x: -10,
               y: 100,
               borderWidth: 0
           },// 显示数值
           plotOptions: {
               line: {
                   dataLabels: {
                       enabled: true
                   },
                   enableMouseTracking: false
               }
           },
           series: [{name: dataTitle.html(), data: counts}]
       });
}
// 柱形图
function columnCharts(title, basePath) {
	setChartsTitle('柱形图');
	if (Tool.isFlash()) {
		/*buildFusionCharts('column', title);
		return;*/
	}
	var length = dataList.length;
	var categories = [];
	var counts = [];
	for (var i = 0; i < length; i++) {
		var groupName = dataList[i].groupName;
		var count = dataList[i].count;
		if (groupName) {
		} else {
			groupName = '';
		}
		categories.push(groupName)
		counts.push(dataList[i].count)
	}
	$('#container').highcharts({
           chart: {
               type: 'column'
           },
           title: {
               text: title
           },
           subtitle: {
               text: 'Source: ' + basePath
           },
           xAxis: {
               categories : /*['192.168.1.1','127.0.0.1','']*/ 
               categories
           },
           yAxis: {
               min: 0,
               title: {
                   text: '总数 (条)'
               }
           },
           exporting:{
               filename: dataTitle.html() + '-柱形图',
               url: 'manage/sys/hightchart/export.do' //这里是一个重点哦,也可以修改exporting.js中对应的url  
           },
           credits: {
			enabled: true,// 不生成logo
			text: basePath,
			href: '',
			position: {
				align: 'right',
				x: -10,
				verticalAlign: 'bottom',
				y: -5
			},
			style: {
				cursor: 'pointer',
				color: '#909090',
				fontSize: '9px'
			}
		},
           tooltip: {// point.y:.1f
               headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
               pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                   '<td style="padding:0"><b>{point.y} 条</b></td></tr>',
               footerFormat: '</table>',
               shared: true,
               useHTML: true
           },
           plotOptions: {
           	/* 在柱状条里显示数值
               column: {
                   stacking: 'normal',
                   dataLabels: {
                       enabled: true,
                       color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                   }
               }
               */
               column: {
                   cursor: 'pointer',
                   point: {
                       events: {
                           click: function() {
                               var drilldown = this.drilldown;
                               if (drilldown) { // drill down
                                   setChart(drilldown.name, drilldown.categories, drilldown.data, drilldown.color);
                               } else { // restore
                                   setChart(name, categories, data);
                               }
                           }
                       }
                   },
                   dataLabels: {
                       enabled: true,
                       color: Highcharts.getOptions().colors[0],
                       style: {
                           fontWeight: 'bold'
                       },
                       formatter: function() {
                           return this.y;
                       }
                   }
               }
           },
           series: [{name: dataTitle.html(), data: counts}]
       });
}
// 饼图
function pieCharts(title, basePath) {
	setChartsTitle('饼图');
	if (Tool.isFlash()) {
		/*buildFusionCharts('pie', title);
		return;*/
	}
	var length = dataList.length;
	var data = [];
	for (var i = 0; i < length; i++) {
		var groupName = dataList[i].groupName;
		if (groupName) {
		} else {
			groupName = '';
		}
		var count = dataList[i].count;
		if (i == 0) {
			data.push({name:groupName, y:count, sliced:true, selected: true});			
		} else {
			data.push([groupName, count]);	
		}
	}
	$('#container').highcharts({
           chart: {
               plotBackgroundColor: null,
               plotBorderWidth: null,
               plotShadow: false
           },
           title: {
               text: title
           },
           exporting:{  
               filename: dataTitle.html() + '-饼图',
               url:'manage/sys/hightchart/export.do' //这里是一个重点哦,也可以修改exporting.js中对应的url  
           },
           credits: {
			enabled: true,// 不生成logo
			text: basePath,
			href: '',
			position: {
				align: 'right',
				x: -10,
				verticalAlign: 'bottom',
				y: -5
			},
			style: {
				cursor: 'pointer',
				color: '#909090',
				fontSize: '9px'
			}
		},
        tooltip: {
    	    pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>',
        	percentageDecimals: 1
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    formatter: function() {
                    	var result = Math.round(this.percentage*100) / 100
                        return '<b>'+ this.point.name +'</b>: '+ result +' %';
                    }
                }
            }
        },
        series: [{
            type: 'pie',
            name: dataTitle.html(),
            data: data
        }]
    });
}