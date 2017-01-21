/**
 * Created by slt  2016/12/26.
 */
$(function(){
    dateRangeEdit();
    exportEdit();
    dateSearch();
    datecancelEdit();
    checkwithdrawals();
    //dateapplyEdit();

});

/**
 * 导出表格配置
 */
var exportEdit=function(){
    var $table = $('table');
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
};

/**
 * 根据日期查询
 */
var dateSearch=function(){
    $("#dateSearch").on("click",function(){
        $(".tab-pane.active").find("table").bootstrapTable('refresh');
    })
};


/**
 * 日期点击取消操作
 * @constructor
 */
var datecancelEdit=function(){
    $('input[name="orderDate"]').on('cancel.daterangepicker', function (ev, picker) {
        $(this).val("");
    });
};

var presentStateFormatter=function(value,row,index){
    return row.presentState.value;
};

//var dateapplyEdit=function(){
//    $('input[name="orderDate"]').on('apply.daterangepicker', function (ev, picker) {
//        var startDate=picker.startDate.format("YYYY-MM-DD hh:mm:ss");
//        var endDate=picker.endDate.format("YYYY-MM-DD hh:mm:ss");
//        $(this).val(startDate+" - "+endDate);
//    });
//};




/**
 * 时间控件编辑
 */
var dateRangeEdit=function(){
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

var setPaymentAccount=function(){
    layer.open({
        type:2,
        area:["60%","60%"],
        content:collectionAccountUrl
    });
};

/**
 * 操作 格式化
 * @param value
 * @param row
 * @param index
 * @returns {string}
 */
var actionFormatter = function (value, row, index) {
    return '<button class="btn btn-link showDetail">查看明细</button>' +
           '<button class="btn btn-link audit">审核</button>';
};

/**
 * 操作添加事件
 * @type {{[click .showDetail]: actionEvents.'click .showDetail'}}
 */
var actionEvents = {
    'click .showDetail': function (e, value, row, index) {
        var id=row.id;
        location.href=detailsUrl+"?id="+id;
    },
    'click .audit': function (e, value, row, index) {
        var id=row.id;
        if(row.selfCheckingCode==1){
            layer.msg("结算单已审核");
            return;
        }
        $.ajax({
            type:'POST',
            url: auditUrl,
            dataType: 'text',
            data: {id:id,state:1},
            success:function(result){
                layer.msg("保存成功！");
                $(".tab-pane.active").find("table").bootstrapTable('refresh');
            },
            error:function(e){
                layer.msg("保存出错！");
            }
        });


    }
};


/**
 * 平台审核状态格式化
 * @param value
 * @param row
 * @param index
 */
var platformCheckingFormatter=function(value, row, index){
    return row.platformChecking.value;
};


/**
 * 将2017-01-01 12:00:00-2017-01-01 12:00:00这种格式的字符串转换成两个时间格式的字符串
 * @param text
 */
var dateRangeFormat = function (text) {
    var s = "2017-01-01 12:00:00";
    var array=[];
    array[0]=text.substr(0, s.length);
    array[1]=text.substr(s.length+3);
    return array;
};

/**
 * 校验提现余额
 */
var checkwithdrawals=function(){
    $(document).on("keyup",".layui-layer-input",function(){
        var balance=parseFloat($("#balance").text());
        var withdrawalsMoney=parseFloat($(this).val());
        if(withdrawalsMoney>balance){
            $(this).val(balance);
        }
    });
};

/**
 * 提现
 */
var withdrawals=function(){
    //var balance=parseFloat($("#balance").val());

    layer.prompt({title: '请输入提现数额', formType: 3}, function(text, index){
        var money=parseFloat(text);
        if(money<=0){
            return;
        }
        //ajax

        $.ajax({
            type:'POST',
            url: withdrawalUrl,
            dataType: 'json',
            data: {money:money},
            success:function(result){
                if(result.data==200){
                    layer.msg("保存成功！");
                    window.setTimeout("window.location=location.href",1000);
                }else {
                    layer.msg(result.msg);
                }
            },
            error:function(e){
                layer.msg("提现出错！");
            }
        });
        layer.close(index);
    });
};



/**
 * 搜素参数添加
 * @param params
 */
var getParams= function(params) {
    //var isNotSettledList=$("#tab-2").hasClass("active");
    var orderDates=dateRangeFormat($("input[name='createDate']").val());
    var sort=params.sort!=undefined?params.sort+","+params.order:"id,desc";
    var temp = {
        pageSize: params.limit, //页面大小
        pageNo: params.offset/params.limit, //页码
        sort:sort,
        createDate:orderDates[0]!=""?orderDates[0]:undefined,
        endCreateDate:orderDates[1]!=""?orderDates[1]:undefined,
        orderDate:orderDates[0]!=""?orderDates[0]:undefined,
        endOrderDate:orderDates[1]!=""?orderDates[1]:undefined,
        settlement:false
    };
    return temp;
};

