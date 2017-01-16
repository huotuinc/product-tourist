actionFormatter = function (value, row, index) {
    var arr = new Array;
    arr.push('<button class="btn btn-primary" >查看明细</button> ');
    if (row.platformChecking.code == 0) {
        arr.push('<button class="btn btn-primary platformChecking" >审核</button> ');
    }
    return arr.join('');
};

presentFormatter = function (value, row, index) {
    var arr = new Array;
    if (row.presentState.code == 2) {
        arr.push('已发放');
    }
    if (row.presentState.code == 0) {
        arr.push('<a class="btn btn-primary presentStateCheckFinish">审核通过</a> ');
    }
    if (row.presentState.code == 1) {
        arr.push('<a class="btn btn-primary presentStateAlreadyPaid">发放</a> ');
    }
    return arr.join('');
};

function platformChecking(url, row, platformChecking) {
    var load = layer.load();
    $.ajax({
        url: url,
        method: "post",
        data: {id: row.id, platformChecking: platformChecking},
        success: function () {
            $table = $('#table');
            $table.bootstrapTable('refresh');
        },
        error: function (error) {
        }
    })
    layer.close(load);
}
function presentRecord(url, row, platformChecking) {
    var load = layer.load();
    $.ajax({
        url: url,
        method: "post",
        data: {id: row.id, presentState: platformChecking},
        success: function () {
            $table = $('#txTable');
            $table.bootstrapTable('refresh');
        },
        error: function (error) {
        }
    })
    layer.close(load);
}

window.actionEvents = {
    'click .platformChecking': function (e, value, row, index) {
        layer.confirm('确定审核通过吗？', {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            platformChecking(modifySettlementSheet, row, 1);
        }, function () {

        });
    }
}
window.presentEvents = {
    'click .presentStateCheckFinish': function (e, value, row, index) {
        layer.confirm('确定审核通过吗？', {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            presentRecord(modifyPresentRecord, row, 1);
        }, function () {

        });
    },
    'click .presentStateAlreadyPaid': function (e, value, row, index) {
        layer.confirm('确定发放吗？', {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            presentRecord(modifyPresentRecord, row, 2);
        }, function () {

        });
    }
}

getParams = function (params) {
    var sort = params.sortName != undefined ? params.sortName + "," + params.sortOrder : undefined;
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.pageSize, //页面大小
        pageNo: params.pageNumber - 1, //页码
        sort: sort,
        supplierName: $("input[name='supplierName']").val(),
        createTime: $("input[name='createTime']").val(),
        platformChecking: $("select[name='platformChecking']").val()
    };
    return temp;
};

getTxParams = function (params) {
    var sort = params.sortName != undefined ? params.sortName + "," + params.sortOrder : undefined;
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.pageSize, //页面大小
        pageNo: params.pageNumber - 1, //页码
        sort: sort,
        supplierName: $("input[name='supplierName']").val(),
        createTime: $("input[name='createTime']").val(),
        presentState: $("select[name='presentState']").val()
    };
    return temp;
};

$('.btnSearch').click(function () {
    var $table;
    if (flag == "js") {
        $table = $('#table');
    } else {
        $table = $('#txTable')
    }
    $table.bootstrapTable('refresh');
});

$(function () {
    dateRangeEdit();
    $("select[name='presentState']").hide();
});

var flag = "js";
$(".js").click(function () {

    $("select[name='platformChecking']").show();
    $("select[name='presentState']").hide();
    flag = "js";
});

$(".tx").click(function () {
    flag = "tx";
    $("select[name='presentState']").show();
    $("select[name='platformChecking']").hide();
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




