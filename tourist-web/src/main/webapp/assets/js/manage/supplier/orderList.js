/**
 * Created by slt  2016/12/26.
 */
$(function(){
    dateRangeEdit();
    bindSearch();
    bindOrderStateLabel();
    changesStyleTouristDate();
    datecancelEdit();
    var $table = $('#table');
    $('#toolbar').find('select').change(function () {
        var pageSize = $table.bootstrapTable("getOptions").totalRows;
        if ($(this).val() === 'all') {
            pageSize = $table.bootstrapTable("getOptions").totalRows;
        }
        var a = {
            exportDataType: $(this).val(), pageSize: pageSize
        }
        $table.bootstrapTable('destroy').bootstrapTable(a);
    });
});

/**
 * 时间控件编辑
 */
var dateRangeEdit=function(){
    //noinspection JSUnresolvedFunction
    $('input[name$="Date"]').daterangepicker(
        {
            locale: {
                format: 'YYYY-MM-DD hh:mm:ss'
            },
            startDate: '2017-01-01',
            endDate: '2017-06-01',
            linkedCalendars: false
        }
    );
};

/**
 * 将2017-01-01:12:00:00 - 2017-01-01:12:00:00这种格式的字符串转换成两个时间格式的字符串
 * @param text
 */
var dateRangeFormat=function(text){
    var s = "2017-01-01 12:00:00";
    var array=[];
    array[0]=text.substr(0, s.length);
    array[1]=text.substr(s.length+3);
    return array;
};

/**
 * 日期确定操作
 */
//var dateapplyEdit=function(){
//    $('input[name$="Date"]').on('apply.daterangepicker', function (ev, picker) {
//        var startDate=picker.startDate.format("YYYY-MM-DD hh:mm:ss");
//        var endDate=picker.endDate.format("YYYY-MM-DD hh:mm:ss");
//        $(this).val(startDate+" - "+endDate);
//    });
//};

/**
 * 日期点击取消操作
 * @constructor
 */
var datecancelEdit=function(){
    $('input[name="orderDate"]').on('cancel.daterangepicker', function (ev, picker) {
        $(this).val("");
    });
};

/**
 * 筛选按钮绑定单机事件，用于搜索
 */
var bindSearch=function(){
    $("#searchList").on("click",function(){
        search();
    });
};

/**
 * 绑定单击事件，订单状态切换
 */
var bindOrderStateLabel=function(){
    $("#shortcutSearch").on("click","li",function(){
        $("#shortcutSearch li").removeClass();
        $(this).attr("class","active");
        search();
    });
};

/**
 * 搜索
 */
var search=function(){
    $("table").bootstrapTable('refresh');
};

/**
 * 操作 格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
var actionFormatter = function (value, row, index) {
    return '<button class="btn btn-link showDetail">详情</button>';
};

/**
 * 线路名称 格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
var touristNameFormatter=function(value,row,index){
    return '<img src="'+row.touristImgUri+'" height="50px" width="80px"><span>'+row.touristName+'</span>';
};

/**
 * 出行时间 格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
var touristDateFormatter=function(value,row,index){
    var html='<a class="btn btn-link showDetail"><span>'+row.touristDate+'</span></a>';
    return html;
};


/**
 * 结算状态格式化
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
var settlementFormatter=function(value,row,index){
    if(row.settlement){
        return "已结算";
    }else {
        return "未结算";
    }
};

/**
 * 出行时间设置事件
 * @type {{[click .showDetail]: touristDateEvents.'click .showDetail'}}
 */
var touristDateEvents={
    'click .showDetail': function (e, value, row, index) {
        var id=row.id;
        var formerId=row.touristRouteId;
        var peopleNumber=parseInt(row.peopleNumber);
        getAllOrderTouristDate(row.id,row.touristRouteId);
        layer.open({
            type:1,
            area: ['700px', '400px'],
            title:"修改出行时间",
            btn:['确定修改','取消'],
            content:$("#modifyTouristDate"),
            yes:function(index){
                var div=$("div[class='panel panel-success']",this.content);
                var laterId=parseInt($(".laterId",div).text());
                var surplusNumber=parseInt($(".surplusNumber",div).text());
                if(peopleNumber>surplusNumber){
                    layer.msg("修改人数超出限制");
                }else {
                    //ajax
                    $.ajax({
                        type:'GET',
                        url: modifyTouristDateurl,
                        dataType: 'json',
                        data: {formerId:row.touristRouteId,laterId:laterId},
                        success:function(result){
                            layer.msg("修改成功！");
                            $("#table").bootstrapTable('refresh');
                        },
                        error:function(e){
                            layer.msg("修改失败！");
                        }
                    });

                }
                //layer.msg("formerId:"+formerId+",laterId:"+laterId);
                layer.close(index);
            }
        });
    }
};

/**
 * 修改该订单所有游客的出行时间
 * @param formerId  之前的线路ID
 * @param laterId   修改之后的线路ID
 */
var modifyOrderTouristDate=function(formerId,laterId){
    $.ajax({
        type:'POST',
        url: '',
        dataType: 'json',
        data: {formerId:formerId,laterId:laterId},
        success:function(result){
            layer.msg("保存成功！");
        },
        error:function(e){
            layer.msg("保存出错！");
        }
    });
};

/**
 * 获取所有该订单商品的其他的线路信息
 * @param id    订单ID
 */
var getAllOrderTouristDate=function(orderId,routeId){

    $.ajax({
        type:'GET',
        url: getAllOrderTouristDateUrl,
        dataType: 'json',
        data: {orderId:orderId,routeId:routeId},
        success:function(result){
            console.log(result);
            bulidAllOrderTouristDate(result.data);
        },
        error:function(e){
            layer.msg("获取出行线路信息出错！");
        }
    });
};

/**
 * 根据线路信息渲染页面元素
 * @param data
 */
var bulidAllOrderTouristDate=function(data){
    $("#modifyTouristDate").empty();
    var html="";
    for(var i=0;i<data.length;i++){
        var model=data[i];
        html+= '<div class="col-sm-4"> ' +
            '<div class="panel panel-default"> ' +
            '<div class="panel-body"> ' +
            '<p class="laterId" style="display: none">'+model.id+'</p> ' +
            '<p style="text-align: center">'+model.fromDate+'</p> ' +
            '<p style="text-align: center">剩余<span class="surplusNumber">'+model.remainPeople+'</span>人</p> ' +
            '</div> </div> </div>';

    }
    $("#modifyTouristDate").append(html);
};

/**
 * 线路元素绑定事件，用来修改显示效果
 */
var changesStyleTouristDate=function(){
    $("#modifyTouristDate").on("mouseover","div[class='panel panel-default']",function(){
        if($(this).attr("class")!="panel panel-success"){
            $(this).attr("class","panel panel-primary");
        }

    });
    $("#modifyTouristDate").on("mouseout","div[class='panel panel-primary']",function(){
        if($(this).attr("class")!="panel panel-success"){
            $(this).attr("class","panel panel-default");
        }

    });
    $("#modifyTouristDate").on("click","div[class='panel panel-primary']",function(){
        $(".panel-success").each(function(){
            $(this).attr("class","panel panel-default");
        })
        $(this).attr("class","panel panel-success");
    });
};


/**
 * 备注格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
var remarkFormatter=function(value,row,index){
    //var txt="";
    //var size=row.remarks.length;
    //if(size>5){
    //    txt =row.remarks.substr(0,5)+"...";
    //}else {
    //    txt= row.remarks;
    //}
    return "<a href='#' class=''>"+row.remarks+"</a>";

};

/**
 * 备注 添加事件
 * @type {{click a: remarkEvents.'click a'}}
 */
var remarkEvents={
    'click a': function (e, value, row, index) {
        layer.open({
            type: 1,
            title: false,
            area: ['400px', '200px'],
            shade: false,
            btn:['确定'],
            shadeClose: true,
            content: "<p id='remark' contenteditable='true' class='text-area'>"+value+"</p>",
            yes: function(index){
                //ajax修改备注
                var newText=$("#remark").text();
                //ajax请求
                $.ajax({
                    url: modifyRemarkUrl,
                    method: "post",
                    data: {id: row.id,remark:newText},
                    dataType: "text",
                    success: function () {
                        layer.msg("修改成功");
                        $("#table").bootstrapTable('refresh');
                    },
                    error: function () {
                        layer.msg("修改失败");

                    }
                });


                layer.close(index);
            }
        });
    }
};

/**
 * 操作添加事件
 * @type {{[click .showDetail]: actionEvents.'click .showDetail'}}
 */
var actionEvents = {
    'click .showDetail': function (e, value, row, index) {
        var id=row.id;
        location.href="orderDetails.html";
        console.log(id);
    }
};

/**
 * 搜素参数添加
 * @param params
 * @returns {{pageSize: *, pageNo: number, orderId: *, name: *, buyer: *, tel: *, orderStateEnum: *, orderDate: undefined, endOrderDate: undefined, payDate: undefined, endPayDate: undefined, touristDate: undefined, endTouristDate: undefined}}
 */
var getParams= function(params) {
    var orderStateEnum=$(".active","#shortcutSearch").index();
    var orderDates=dateRangeFormat($("input[name='orderDate']").val());
    var payDate=dateRangeFormat($("input[name='payDate']").val());
    var touristDate=dateRangeFormat($("input[name='touristDate']").val());
    var orderId=$("input[name='orderId']").val();
    var name=$("input[name='name']").val();
    var buyer=$("input[name='buyer']").val();
    var tel=$("input[name='tel']").val();
    var settlement=$("#settlement option:checked").val();
    var sort=params.sort!=undefined?params.sort+","+params.order:undefined;
    var temp = {
        pageSize: params.limit, //页面大小
        pageNo: params.offset/params.limit, //页码
        sort: sort,   //排序
        orderNo:orderId!=""?orderId:undefined,
        name:name!=""?name:undefined,
        buyer:buyer!=""?buyer:undefined,
        tel:buyer!=""?buyer:undefined,
        orderState:orderStateEnum>0?orderStateEnum-1:undefined,
        orderDate:orderDates[0]!=""?orderDates[0]:undefined,
        endOrderDate:orderDates[1]!=""?orderDates[1]:undefined,
        payDate:payDate[0]!=""?payDate[0]:undefined,
        endPayDate:payDate[1]!=""?payDate[1]:undefined,
        touristDate:touristDate[0]!=""?touristDate[0]:undefined,
        endTouristDate:touristDate[1]!=""?touristDate[1]:undefined,
        settlement:settlement!="-1"?settlement:undefined
    };
    return temp;
};

