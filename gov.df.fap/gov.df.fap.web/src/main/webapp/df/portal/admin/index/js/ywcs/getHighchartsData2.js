$(function() {
	getZbyeData();
	getZbtjData();
	getKkycData();
	getyjqsData();

});

function getZbyeData() {
	$('#zbye').highcharts({
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false
		},
		title: {
			text: ''
		},
		tooltip: {
			headerFormat: '{series.name}<br>',
			pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
		},
		exporting: {
			enabled: false
		},
		credits: {
			enabled: false
		},
		legend: {
			reversed: true,
			layout: 'vertical',
			align: 'center',
			verticalAlign: 'center',
			x: 120,
			y: 10,
			floating: false,
			borderWidth: 0,
			backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
			shadow: false
		},
//		plotOptions: {
//			pie: {
//				allowPointSelect: true,
//				cursor: 'pointer',
//				dataLabels: {
//					enabled: true,
//					format: '<b>{point.name}</b>: {point.y}',
//
//				},
//				showInLgend:true
//			}
//		},
		 plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
					format: '{point.y}',
                },
                showInLegend: true
            }
        },
		
		colors: ['#7ED321', '#108EE9', '#F56A00'
		],
		series: [{
			type: 'pie',
			name: '指标余额',
			data: [
				['本年', 16000.00],
				['1~2年', 3000.00],
				['2年以上', 1000.00],
			]
		}]
	});
}

function getZbtjData() {
	$('#zbtj').highcharts({
		chart: {
			type: 'column'
		},
		exporting: {
			enabled: false
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
			//		    title: {
			//				text: '月份'
			//			}
		},
		yAxis: {
		    title: {
            align: 'high',
            offset: 0,
            text: '单位：万元',
            rotation: 0,
            y: -25
           }
//		    visible:false
		},
		
		legend: {
			reversed: false,
			layout: 'horizontal',
			align: 'center',
			verticalAlign: 'top',
			x: 200,
			y: 10,
			floating: false,
			borderWidth: 0,
			backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
			shadow: false
		},
		colors: ['#FFBF00', '#EF564B', '#108EE9'],
		credits: {
			enabled: false
		},
		series: [{
				name: '净增指标',
				data: [50, 30, 40, 70, 20, 60, 50, 30, 40, 70, 20, 60]
			},
			{
				name: '追减指标',
				data: [-20, -20, -20, -20, -10, -10, -20, -15, -20, -20, -10, -20]
			},
			{
				name: '追加指标',
				data: [20, 40, 30, 60, 30, 50, 40, 60, 30, 60, 30, 50]
			}
		]
	});
};

function getKkycData() {
	$('#kkyc').highcharts({
		chart: {
			type: 'line'
		},
		exporting: {
			enabled: false
		},
		title: {
			text: ''
		},
		xAxis: {
			categories: ['201701', '201702', '201703', '201704', '201705', '201706', '201707', '201708', '201709', '201710', '201711', '201712']
		},
		yAxis: {
			title: {
				text: '单位：亿元'
			}
		},
		credits: {
			enabled: false
		},
		colors: ['#B5B5B5', '#7EB29E', '#FFBF00', '#FFBF00'],
		plotOptions: {
			line: {
				dataLabels: {
					enabled: true // 开启数据标签
				},
				enableMouseTracking: false // 关闭鼠标跟踪，对应的提示框、点击事件会失效
			}
		},
		series: [{
				name: '实际支出',
				data: [47.11, 17.49, 40.25, 65.68, 51.42, 33.84, '', '', '', '', '', '']
			}, {
				name: '预测支出',
				data: ['', '', '', '', '', 33.84, 48.61, 34.70, 75.70, 61.18, 78.72, 143.67]
			}, {
				name: '上限',
				data: ['', '', '', '', '', '', 80.77, 66.85, 107.86, 93.3, 110.88, 175.83]
			}, {
				name: '下限',
				data: ['', '', '', '', '', '', 20.77, 6.85, 40.86, 33.3, 40.88, 115.83]
			}

		]
	});
}

function getyjqsData() {
	$('#yjqs').highcharts({
		chart: {
			type: 'line'
		},
		exporting: {
			enabled: false
		},
		title: {
			text: '预警趋势图'
		},
		xAxis: {
			categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
		},
		yAxis: {
			title: {
				text: '次数'
			}
		},
		credits: {
			enabled: false
		},
		legend: {
			reversed: false,
			layout: 'horizontal',
			align: 'center',
			verticalAlign: 'top',
			x: 200,
			y: 30,
			floating: false,
			borderWidth: 0,
			backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
			shadow: false
		},
		colors: ['#91CCF1', '#7EB29E', '#FFBF00'],
		plotOptions: {
			line: {
				dataLabels: {
					enabled: true // 开启数据标签
				},
				enableMouseTracking: false // 关闭鼠标跟踪，对应的提示框、点击事件会失效
			}
		},
		series: [{
				name: '用款计划',
				data: [125, 120, 250, 240, 260, 375, 125, '', '', '', '', '']
			}, {
				name: '直接支付',
				data: [25, 20, 50, 40, 60, 75, 25, '', '', '', '', '']
			}, {
				name: '授权支付',
				data: ['', '', '', '', '', '', '', '', '', '', '']
			}

		]
	});
}