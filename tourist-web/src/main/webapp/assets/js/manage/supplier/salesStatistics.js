/**
 * Created by slt  2016/12/26.
 */
$(function(){
    dateRangeEdit();

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
});

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
        content:"payeeAccountDetails.html"
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
        location.href="settlementDetailsList.html";
    },
    'click .audit': function (e, value, row, index) {
        var id=row.id;
    }
};

