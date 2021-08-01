<%--
  Created by IntelliJ IDEA.
  User: 飞翔的兰
  Date: 2021/7/28
  Time: 22:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title"><span class="glyphicon glyphicon-signal"></span>&nbsp;数据报表</div>
        <div class="container-fluid">
            <div class="row" style="padding-top: 20px;">
                <div class="col-md-12">
                    <%--  柱状图的所需容器  --%>
                    <div id="monthChart" style="height: 500px"></div>

                    <%--  百度地图的加载  --%>
                    <h3 align="center">日记地区分布图</h3>

                    <%--  百度地图的容器  --%>
                    <div id="baiduMap" style="height: 600px; width: 100%;"></div>
                </div>
                <div class="col-md-4">
                </div>
            </div>
        </div>
    </div>
</div>

<%--
关于Echarts报表的使用
    1. 下载Echarts的依赖(JS文件)
    2. 在需要的页面引入Echarts文件
    3. 为Echarts准备
    4. 通过echarts.init方法初始化一个echarts实例并通过setOption方法生成一个简单的柱状图
  --%>
<script type="text/javascript" src="../static/echarts/echarts.min.js"></script>
<script type="text/javascript"
        src="https://api.map.baidu.com/api?v=1.0&&type=webgl&ak=3YjSmtWz0tjughPWQYhojW0vTncpY24U"></script>
<script>
    // 发送Ajax请求, 通过月份查询对应的日记数量
    $.ajax({
        type: "GET",
        url: "report",
        data: {
            actionName: "month"
        },
        success: function (result) {
            console.log(result)
            if (result.code == 1) {
                // 得到月份( X轴的数据 )
                var monthArry = result.result.monthArry;
                // 得到月份对应的日记数量( Y轴的数据 )
                var dataArry = result.result.dataArry;
                // 加载柱状图
                loadMonthCharts(monthArry, dataArry);
            }
        }
    })

    /**
     * 加载柱状图
     */
    function loadMonthCharts(monthArry, dataArry) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('monthChart'));

        var dataAxis = monthArry;
        var data = dataArry;
        var yMax = 30;
        var dataShadow = [];

        for (var i = 0; i < data.length; i++) {
            dataShadow.push(yMax);
        }

        // 指定图表的配置项和数据
        var option = {
            // 标题
            title: {
                text: '按月统计', // 主标题
                subtext: '通过月份查询对应的日记数量', // 子标题
                left: 'center' // 标题对齐方式
            },
            tooltip: {},
            xAxis: {
                data: dataAxis,
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                z: 10
            },
            yAxis: {
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    textStyle: {
                        color: '#999'
                    }
                }
            },
            dataZoom: [
                {
                    type: 'inside'
                }
            ],
            series: [
                {
                    type: 'bar',
                    data: data,   // Y轴数据
                    showBackground: true,
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 0, 1,
                            [
                                {offset: 0, color: '#83bff6'},
                                {offset: 0.5, color: '#188df0'},
                                {offset: 1, color: '#188df0'}
                            ]
                        )
                    },
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#2378f7'},
                                    {offset: 0.7, color: '#2378f7'},
                                    {offset: 1, color: '#83bff6'}
                                ]
                            )
                        }
                    },
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }


    /**
     * 通过用户发布的坐标查询日记
     */
    $.ajax({
        type: "GET",
        url: "report",
        data: {
            actionName: "location"
        },
        success: function (result) {
            console.log(result);
            if (result.code == 1) {
                // 加载百度地图
                loadBaiduMap(result.result);
            }
        }
    });

    /**
     * 加载百度地图
     */
    function loadBaiduMap(markers) {
        // 加载地图实例
        var map = new BMapGL.Map("baiduMap");
        // 设置地图的中心点
        var point = new BMapGL.Point(116.404, 39.915);
        // 地图初始化
        map.centerAndZoom(point, 15);
        map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放

        // 添加控件
        var scaleCtrl = new BMapGL.ScaleControl(); // 添加比例尺控件
        map.addControl(scaleCtrl);
        var zoomCtrl = new BMapGL.ZoomControl(); // 添加比例尺控件
        map.addControl(zoomCtrl);

        var navi3DCtrl = new BMapGL.NavigationControl3D(); // 添加3D控件
        map.addControl(navi3DCtrl);

        // 判断是否有坐标
        if (markers != null && markers.length > 0) { // 集合中的第一个坐标是用户当前所在的位置,其它日记的地理位置为日记的记录
            // 将用户所在的位置设置为中心点
            map.centerAndZoom(new BMapGL.Point(markers[0].lon, markers[0].lat), 10);
            // 循环在地图上添加点标记
            for (var i = 1; i < markers.length; i++) {
                // 创建点标记
                var marker = new BMapGL.Marker(new BMapGL.Point(markers[i].lon, markers[i].lat));
                // 在地图上添加点标记
                map.addOverlay(marker);
            }
        }
    }
</script>