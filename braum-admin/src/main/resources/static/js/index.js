var pageCurr;
var form;
$(function () {
    layui.use('table', function () {
        var table = layui.table;
        form = layui.form;

        tableIns = table.render({
            elem: '#tableList',
            url: '/limiterRegisterInfoController/queryPage',
            method: 'post', //默认：get请求
            cellMinWidth: 80,
            page: true,
            request: {
                pageName: 'offset', //页码的参数名称，默认：pageNum
                limitName: 'limit' //每页数据量的参数名，默认：pageSize
            },
            response: {
                statusName: 'errcode', //数据状态的字段名称，默认：code
                statusCode: 0, //成功的状态码，默认：0
                countName: 'totalCount', //数据总数的字段名称，默认：count
                dataName: 'data' //数据列表的字段名称，默认：data
            },
            cols: [[
                {type: 'numbers'}
                , {field: 'name', title: '限流名称', align: 'center'}
                , {title: '操作', align: 'center', toolbar: '#optBar'}
            ]],
            done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                console.log(curr);
                $("[data-field='userStatus']").children().each(function () {
                    if ($(this).text() == '1') {
                        $(this).text("有效")
                    } else if ($(this).text() == '0') {
                        $(this).text("失效")
                    }
                });
                //得到数据总量
                //console.log(count);
                pageCurr = curr;
            }
        });

        //监听工具条
        table.on('tool(tableFilter)', function (obj) {
            var data = obj.data;
            if (obj.event === 'view') {
                //查看
                window.open("/viewLabel?name=" + data.name);
            }
        });
    });


    //搜索框
    layui.use(['form', 'laydate'], function () {
        var form = layui.form, layer = layui.layer
            , laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#createTimeStart',
            format: 'yyyy-MM-dd HH:mm:ss',
            type: 'datetime'
        });
        laydate.render({
            elem: '#createTimeEnd',
            format: 'yyyy-MM-dd HH:mm:ss',
            type: 'datetime'
        });

        //监听搜索框
        form.on('submit(searchSubmit)', function (data) {
            //重新加载table
            load(data);
            return false;
        });
    });
    layui.use('element', function () {
        var $ = layui.jquery
            , element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块

        //触发事件
        var active = {
            tabAdd: function () {
                //新增一个Tab项
                element.tabAdd('demo', {
                    title: '新选项' + (Math.random() * 1000 | 0) //用于演示
                    , content: '内容' + (Math.random() * 1000 | 0)
                    , id: new Date().getTime() //实际使用一般是规定好的id，这里以时间戳模拟下
                })
            }
            , tabChange: function () {
                //切换到指定Tab项
                element.tabChange('demo', '22'); //切换到：用户管理
            }
        };
    });

});


function load(obj) {
    //重新加载table
    tableIns.reload({
        where: obj.field
        , page: {
            curr: 1 //从1页码开始
        }
    });
}
