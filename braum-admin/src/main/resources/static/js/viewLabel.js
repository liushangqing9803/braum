var pageCurr;
var form;
var startTime = timeAddMinute(-15);
var endTime = getFormatDateTime();
var totalOption = null;
var detailOption = null;
var myChart = null;
var myChartDetail = null;
$(function () {
    form = layui.form;

    // 基于准备好的dom，初始化echarts实例
    myChart = echarts.init(document.getElementById('main'));
    myChartDetail = echarts.init(document.getElementById('detail'));
    //搜索框
    layui.use(['form', 'laydate'], function () {
        var form = layui.form, layer = layui.layer
            , laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#startTime',
            format: 'yyyy-MM-dd HH:mm:ss',
            type: 'datetime',
            value: timeAddMinute(-15),
            done: function (value, date) {
                startTime = value;
                console.log(startTime)
            }
        });
        laydate.render({
            elem: '#endTime',
            format: 'yyyy-MM-dd HH:mm:ss',
            type: 'datetime',
            value: new Date(),
            done: function (value, date) {
                endTime = value;
            }
        });

        //监听搜索框
        form.on('submit(searchSubmit)', function (data) {
            search();
            return false;
        });
    });

    var limitName = GetQueryString("name");
    document.getElementById('limiterName').value = limitName;

    var dataParam = JSON.stringify({"limiterName": limitName, "startTime": startTime, "endTime": endTime});
    console.log(dataParam)

    $.ajax({
        type: "POST",
        data: $("#limiterSearch").serialize(),
        url: rootPath + "/limiterDataController/query",
        success: function (res) {
            if (res.errcode !== 0) {
                layer.msg(res.errmsg, {icon: 2, time: 1000});
                return;
            } else {
                console.log(res)
                var data = res.data;
                // 指定图表的配置项和数据
                totalOption = {
                    title: {
                        text: data.limiterDataTotalVo.name
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['LimiterTotal', 'limiterBlock']
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: data.limiterDataTotalVo.times
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            name: 'LimiterTotal',
                            type: 'line',
                            data: data.limiterDataTotalVo.limiterTotal
                        },
                        {
                            name: 'limiterBlock',
                            type: 'line',
                            data: data.limiterDataTotalVo.limiterBlock
                        }
                    ]
                };


                detailOption = {
                    title: {
                        text: data.limiterDataDetailVo.name
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: data.limiterDataDetailVo.lineName
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: data.limiterDataDetailVo.times
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: data.limiterDataDetailVo.data
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(totalOption);
                myChartDetail.setOption(detailOption);
            }
        },
        error: function (e) {
            console.log(e)
            return;
        }

    });

});


//提交表单
function search(obj) {
    var limitName = GetQueryString("name");
    var dataParam = JSON.stringify({"limiterName": limitName, "startTime": startTime, "endTime": endTime});
    console.log(dataParam)
    //echarts.dispose(document.getElementById('main'));
   // echarts.dispose(document.getElementById('detail'));

  //  myChart = echarts.init(document.getElementById('main'));
   // myChartDetail = echarts.init(document.getElementById('detail'));

    $.ajax({
        type: "POST",
        data: $("#limiterSearch").serialize(),
        url: rootPath + "/limiterDataController/query",

        success: function (res) {
            if (res.errcode !== 0) {
                layer.msg(res.errmsg, {icon: 2, time: 1000});
                return;
            } else {
                console.log(res)
                var data = res.data;
                // 指定图表的配置项和数据
                totalOption = {
                    title: {
                        text: data.limiterDataTotalVo.name
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['LimiterTotal', 'limiterBlock']
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: data.limiterDataTotalVo.times
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            name: 'LimiterTotal',
                            type: 'line',
                            data: data.limiterDataTotalVo.limiterTotal
                        },
                        {
                            name: 'limiterBlock',
                            type: 'line',
                            data: data.limiterDataTotalVo.limiterBlock
                        }
                    ]
                };


                detailOption = {
                    title: {
                        text: data.limiterDataDetailVo.name
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: data.limiterDataDetailVo.lineName
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: data.limiterDataDetailVo.times
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: data.limiterDataDetailVo.data
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(totalOption);
                myChartDetail.setOption(detailOption);
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试", function () {
                layer.closeAll();
                //加载load方法
                detailLoad(orderNo);//自定义
            });
        }
    });
}


function timeAddMinute(value) {
    var curTime = new Date();
    var date = new Date(curTime.setMinutes(curTime.getMinutes() + value));
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();
    return [year, '-', month, '-', day, ' ', hour, ':', minute, ':', second].join('');
}

function getFormatDateTime() {
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();
    return [year, '-', month, '-', day, ' ', hour, ':', minute, ':', second].join('');
}

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg); //获取url中"?"符后的字符串并正则匹配
    var context = "";
    if (r != null)
        context = r[2];
    reg = null;
    r = null;
    return context == null || context == "" || context == "undefined" ? "" : context;
}

