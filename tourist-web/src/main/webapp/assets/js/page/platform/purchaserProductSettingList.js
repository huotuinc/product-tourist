deleteUrl = /*[[@{/distributionPlatform/delPurchaserProductSetting}]]*/ "../../../mock/platform/httpJson.json"
// addUrl = /*[[@{/distributionPlatform/addPurchaserProductSetting}]]*/ "purchaserProductSetting.html";
toPurchaserProductSetting = /*[[@{/distributionPlatform/toPurchaserProductSetting}]]*/ "purchaserProductSetting.html"

getParams = function (params) {
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.limit, //页面大小
        pageNo: params.offset, //页码
        name: $("input[name='name']").val()

    };
    return temp;
};

bannerUriFormatter = function (value, row, index) {
    return [
        '<img src="' + row.bannerUri + '" style="vertical-align:bottom"/>'
    ].join('');
};
updateFormatter = function (value, row, index) {
    return [
        '<a class="btn btn-primary update" href="' + toPurchaserProductSetting + '?id=' + row.id + '">修改</a> '
        , '<button class="btn btn-danger del delete">删除</button> '
    ].join('');
};

$('.btnSearch').click(function () {
    var $table = $('#table');
    $table.bootstrapTable('refresh');
});
function updateCheckState(row) {
    var load = layer.load();
    $.ajax({
        url: deleteUrl,
        method: "post",
        data: {id: row.id},
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
}

window.actionEvents = {
    'click .delete': function (e, value, row, index) {
        layer.confirm('确定删除吗？', {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            updateCheckState(row);
        }, function () {

        });
    }
}









