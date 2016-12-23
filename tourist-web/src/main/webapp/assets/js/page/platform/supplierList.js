/**
 * Created by Administrator xhl 2015/12/21.
 */
define(function (require, exports, module) {

    actionFormatter = function (value, row, index) {
        if (row.frozen == false) {
            return '<button class="btn btn-primary resend">修改</button> '
                + ' <button class="btn btn-primary resend">冻结</button>';
        } else {
            return [
                '<button class="btn btn-primary frozen">修改</button>'
            ].join('');
        }
    };

    window.actionEvents = {
        'click .resend': function (e, value, row, index) {
            alert('修改操作, row: ' + JSON.stringify(row));
            console.log(value, row, index);
        }
        , 'click .frozen': function (e, value, row, index) {
            alert('冻结操作, row: ' + JSON.stringify(row));
            console.log(value, row, index);
        }
    };

    function getParams(params) {
        var temp = {
            //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit, //页面大小
            pageNo: params.offset, //页码
            supplierName: $("input[name='supplierName']").val()
        };
        return temp;
    }


});