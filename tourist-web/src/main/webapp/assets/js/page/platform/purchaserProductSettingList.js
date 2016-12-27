getParams = function (params) {
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.limit, //页面大小
        pageNo: params.offset, //页码
        name: $("input[name='name']").val()

    };
    return temp;
};

$('.btnSearch').click(function () {
    var $table = $('#table');
    $table.bootstrapTable('refresh');
});

bannerUriFormatter = function (value, row, index) {
    return [
        '<img src="' + row.bannerUri + '" style="vertical-align:bottom">banner</img>'
    ].join('');
};
updateFormatter = function (value, row, index) {
    return [
        '<a class="btn btn-primary update" href="purchaserProductSetting.html"' +
        ' th:href="@{/distributionPlatform/toPurchaserProductSetting(id=' + row.id + ')}">修改</a> '
        , '<a class="btn btn-primary update" href="purchaserProductSettingList.html"' +
        ' th:href="@{/distributionPlatform/delPurchaserProductSetting(id=' + row.id + ')}">删除</a> '
    ].join('');
};





