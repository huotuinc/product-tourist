$(function () {
    dateRangeEdit();
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

getParams = function (params) {
    var sort = params.sortName != undefined ? params.sortName + "," + params.sortOrder : undefined;
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.pageSize, //页面大小
        pageNo: params.pageNumber - 1, //页码
        sort: sort,
        // sortOrder: params.sortOrder,
        // sortName: params.sortName,
        buyerName: $("input[name='buyerName']").val(),
        buyerDirector: $("input[name='buyerDirector']").val(),
        telPhone: $("input[name='telPhone']").val(),
        payDate: $("input[name='payDate']").val()
    };
    return temp;
};

$('.btnSearch').click(function () {
    var $table = $('#table');
    $table.bootstrapTable('refresh');
});


/**
 * 时间控件编辑
 */
dateRangeEdit = function () {
    $('.datePicker').daterangepicker(
        {
            locale: {
                format: 'YYYY-MM-DD hh:mm:ss'
            },
            startDate: '2017-01-01',
            endDate: '2017-6-01'
        }
    );
};

/**
 * 将2017-01-01 12:00:00 - 2017-01-01 12:00:00这种格式的字符串转换成两个时间格式的字符串
 * @param text
 */
dateRangeFormat = function (text) {
    var s = "2017-01-01 12:00:00";
    var array = [];
    array[0] = text ? "" : text.substr(0, s.length);
    array[1] = text ? "" : text.substr(s.length + 3);
    return array;
};
//
// $("#btn_export").click(function () {
//     var dateArr = dateRangeFormat($("input[name='payDate']").val());
//     var buyerName = $("input[name='buyerName']").val();
//     var buyerDirector = $("input[name='buyerDirector']").val();
//     var telPhone = $("input[name='telPhone']").val();
//
//     window.location.href = exportsPurchaserPaymentRecordUrl + "?startPayDate=" + dateArr[0] + "&endPayDate=" + dateArr[1] + "&buyerName=" + buyerName
//         + "&buyerDirector=" + buyerDirector + "&telPhone=" + telPhone;//+"&pageSize="+"&pageNo=";
// })



