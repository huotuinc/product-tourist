addOrUpdateUrl = /*[[@{/distributionPlatform/toAddTouristSupplier}]]*/ "supplier.html";
frozenSupplierUrl = /*[[@{/distributionPlatform/frozenSupplier}]]*/ "../../../mock/platform/httpJson.json";

actionFormatter = function (value, row, index) {
    if (row.frozen == false) {
        return '<a class="btn btn-primary resend" href="' + addOrUpdateUrl + '?id=' + row.id + '">修改</a> '
            + ' <button class="btn btn-primary frozen">冻结</button>';
    } else {
        return [
            '<a class="btn btn-primary resend" href="' + addOrUpdateUrl + '?id=' + row.id + '">修改</a> '
        ].join('');
    }
};

window.actionEvents = {
    'click .frozen': function (e, value, row, index) {
        layer.confirm('确定冻结吗？', {
            btn: ['冻结', '取消']
        }, function (index) {
            layer.close(index);
            var load = layer.load();
            $.ajax({
                url: frozenSupplierUrl,
                method: "post",
                data: {id: row.id, frozen: true},
                dataType: "json",
                success: function () {
                    console.log("success");
                    layer.close(load);
                    var $table = $('#table');
                    $table.bootstrapTable('refresh');
                },
                error: function (error) {
                    console.log("error");
                    layer.close(load);
                }
            })

        }, function () {

        });
    }
};

$('.btnSearch').click(function () {
    var $table = $('#table');
    $table.bootstrapTable('refresh');
});

getParams = function (params) {
    params.sort
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.limit, //页面大小
        pageNo: params.offset, //页码
        sortName: params.sortName,
        sortOrder: params.sortOrder,
        sort: params.sort,
        supplierName: $("input[name='supplierName']").val()
    };
    return temp;
}
