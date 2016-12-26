// define(function (require, exports, module) {
    actionFormatter = function (value, row, index) {
        if (row.frozen == false) {
            return '<a class="btn btn-primary resend" href="supplier.html"' +
                ' th:href="@{/distributionPlatform/toAddTouristSupplier}">修改</a> '
                + ' <button class="btn btn-primary frozen">冻结</button>';
        } else {
            return [
                '<a class="btn btn-primary resend" href="supplier.html"' +
                ' th:href="@{/distributionPlatform/toAddTouristSupplier}">修改</a> '
            ].join('');
        }
    };

    window.actionEvents = {
        'click .frozen': function (e, value, row, index) {
            alert('冻结操作, row: ' + JSON.stringify(row));
            console.log(value, row, index);
        }
    };

    $('.search').click(function () {
        var $table = $('#table');
        $table.bootstrapTable('refresh');
    });

    getParams = function (params) {
        var temp = {
            //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit, //页面大小
            pageNo: params.offset, //页码
            supplierName: $("input[name='supplierName']").val()
        };
        return temp;
    }
// });