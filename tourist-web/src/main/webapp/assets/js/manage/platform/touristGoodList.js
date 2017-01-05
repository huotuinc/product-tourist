recommendTouristGood = /*[[@{/distributionPlatform/recommendTouristGood}]]*/ "../../../mock/platform/httpJson.json"
unRecommendTouristGood = /*[[@{/distributionPlatform/unRecommendTouristGood}]]*/ "../../../mock/platform/httpJson.json"
showTouristGood = /*[[@{/distributionPlatform/showTouristGood}]]*/ "touristGood.html";
modifyTouristGoodState = /*[[@{/distributionPlatform/modifyTouristGoodState}]]*/ "../../../mock/platform/httpJson.json";
var $table = $('#table');
$(".all").click(function () {
    $table = $('#table');
});
$(".recommend").click(function () {
    $table = $('#recommendTable');
});

getParams = function (params) {
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.pageSize, //页面大小
        pageNo: params.pageNumber, //页码
        sortOrder: params.sortOrder,
        sortName: params.sortName,
        supplierName: $("input[name='supplierName']").val(),
        touristName: $("input[name='touristName']").val(),
        touristTypeId: $("select[name='touristTypeId']").val(),
        activityTypeId: $("select[name='activityTypeId']").val(),
        touristCheckState: $("select[name='touristCheckState']").val()
    };
    return temp;
};

actionFormatter = function (value, row, index) {
    var arr = new Array;
    arr.push('<a class="btn btn-primary" href="' + showTouristGood + '?id=' + row.id + '">查看</a> ');
    arr.push('<a class="btn btn-primary" href="touristGoodList.html"' +
        ' th:href="@{/distributionPlatform/touristGoodInfo(id=' + row.id + ')}">链接地址</a> ');
    if (row.touristCheckState.code == 2 && row.recommend) {
        arr.push('<button class="btn btn-primary unRecommendTouristGood">取消推荐</button> ');
    }
    if (row.touristCheckState.code < 2) {
        arr.push('<button class="btn btn-primary modifyCheckState" >审核通过</button> ');
    }
    if (!row.recommend) {
        arr.push('<button class="btn btn-primary recommendTouristGood" >推荐</button> ');
    }
    return arr.join('');
};

touristImgUriFormatter = function (value, row, index) {
    return [
        '<img src="' + row.touristImgUri + '" />'
    ].join('');
};

$('.btnSearch').click(function () {
    $table.bootstrapTable('refresh');
});

function updateRecommend(url, row, recommend) {
    var load = layer.load();
    $.ajax({
        url: url,
        method: "post",
        data: {id: row.id, recommend: recommend},
        dataType: "json",
        success: function () {
            $table.bootstrapTable('refresh');
        },
        error: function (error) {

        }
    })
    layer.close(load);
}

window.actionEvents = {
    'click .recommendTouristGood': function (e, value, row, index) {
        layer.confirm('确定推荐吗？', {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            updateRecommend(recommendTouristGood, row, true);
        }, function () {

        });
    },
    'click .unRecommendTouristGood': function (e, value, row, index) {
        layer.confirm('确定取消推荐吗？', {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            updateRecommend(unRecommendTouristGood, row, false);
        }, function () {

        });
    },
    'click .modifyCheckState': function (e, value, row, index) {
        layer.confirm('确定取审核通过吗？', {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            $.ajax({
                url: modifyTouristGoodState,
                method: "post",
                data: {id: row.id, checkState: 2},
                dataType: "json",
                success: function () {
                    $table.bootstrapTable('refresh');
                },
                error: function (error) {

                }
            })
        }, function () {

        });
    }
}



