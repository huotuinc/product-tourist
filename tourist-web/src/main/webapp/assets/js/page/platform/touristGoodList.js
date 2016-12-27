actionFormatter = function (value, row, index) {
    var arr = new Array;
    arr.push('<a class="btn btn-primary" href="touristGood.html"' +
        ' th:href="@{/distributionPlatform/touristGoodInfo(id=' + row.id + ')}">查看</a> ');
    arr.push('<a class="btn btn-primary" href="touristGoodList.html"' +
        ' th:href="@{/distributionPlatform/touristGoodInfo(id=' + row.id + ')}">链接地址</a> ');
    if (row.touristCheckState.code == 2 && row.recommend) {
        arr.push('<a class="btn btn-primary" href="touristGoodList.html"' +
            ' th:href="@{/distributionPlatform/unRecommendTouristGood(id=' + row.id + ',recommend=' + false + ')}">取消推荐</a> ');
    }
    if (row.touristCheckState.code < 2) {
        arr.push('<a class="btn btn-primary" href="touristGoodList.html"' +
            ' th:href="@{/distributionPlatform/modifyTouristGoodState(id=' + row.id + ',checkState=' + 2 + ')}">审核通过</a> ');
    }
    if (!row.recommend) {
        arr.push('<a class="btn btn-primary" href="touristGoodList.html"' +
            ' th:href="@{/distributionPlatform/recommendTouristGood(id=' + row.id + ',recommend=' + true + ')}">推荐</a> ');
    }
    return arr.join('');
};


touristImgUriFormatter = function (value, row, index) {
    return [
        '<img src="' + row.touristImgUri + '" />'
    ].join('');
};

getParams = function (params) {
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.limit, //页面大小
        pageNo: params.offset, //页码
        supplierName: $("input[name='supplierName']").val(),
        touristName: $("input[name='touristName']").val(),
        touristTypeId: $("select[name='touristTypeId']").val(),
        activityTypeId: $("select[name='activityTypeId']").val(),
        touristCheckState: $("select[name='touristCheckState']").val()
    };
    return temp;
};

$('.btnSearch').click(function () {
    var $table = $('#table');
    $table.bootstrapTable('refresh');
});




