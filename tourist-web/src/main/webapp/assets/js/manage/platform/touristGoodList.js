var $table = $('#table');
$(".all").click(function () {
    $table = $('#table');
});
$(".recommend").click(function () {
    $table = $('#recommendTable');
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
    arr.push('<button  class="btn btn-primary updateLinkUrl" data-toggle="modal"' +
        ' data-target="#myModal">链接地址</button> ');
    if (row.touristCheckState.code == 2 && row.recommend) {
        arr.push('<button class="btn btn-primary unRecommendTouristGood">取消推荐</button> ');
    }
    if (row.touristCheckState.code < 2) {
        arr.push('<button class="btn btn-primary modifyCheckState" data-toggle="modal"' +
            ' data-target="#productModal" >审核通过</button> ');
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
        success: function () {
            $table.bootstrapTable('refresh');
        },
        error: function (error) {
            layer.alert("修改失败");
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
        if ($("#mallProductId").val() == null) {
            layer.alert("商城货品id不能为空");
            return;
        }
        $.ajax({
            url: modifyTouristGoodState,
            method: "post",
            data: {id: row.id, checkState: 2, mallProductId: $("#mallProductId").val()},
            success: function () {
                $table.bootstrapTable('refresh');
            },
            error: function (error) {
                layer.alert(error);
            }
        });

    },
    'click .updateLinkUrl': function (e, value, row, index) {
        $("#linkUrl").val(linkUrl + "?id=" + row.id);
    }
}



