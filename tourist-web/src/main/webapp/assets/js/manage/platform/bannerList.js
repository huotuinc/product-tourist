

getParams = function (params) {
    var sort = params.sortName != undefined ? params.sortName + "," + params.sortOrder : undefined;
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.pageSize, //页面大小
        pageNo: params.pageNumber - 1, //页码
        // sortOrder: params.sortOrder,
        // sortName: params.sortName
        sort: sort
    };
    return temp;
};

actionFormatter = function (value, row, index) {
    return '<button  class="btn btn-primary update" data-toggle="modal" data-target="#myModal">修改</button> '
        + '<button class="btn btn-danger del" >删除</button>';
};
bannerImgUriFormatter = function (value, row, index) {
    return '<img src="' + row.bannerImgUri + '"/> ';
};

window.actionEvents = {
    'click .update': function (e, value, row, index) {
        $(".oldImgDiv").show();
        $(".oldImg").attr("src", g + row.bannerImgUri);
        $("input[name='bannerName']").val(row.bannerName);
        $("input[name='bannerImgUri']").val(row.bannerImgUri);
        $("input[name='linkUrl']").val(row.linkUrl);
        $("input[name='id']").val(row.id);
        parent.uploader($('#banner-uploader'), function (path) {
            $("#bannerImgUri").val(path);
        }, {
            allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
            itemLimit: 1,
            sizeLimit: 3 * 1024 * 1024
        });
    },
    'click .del': function (e, value, row, index) {
        layer.confirm('确定删除吗？', {
            btn: ['删除', '取消']
        }, function (index) {
            layer.close(index);
            var load = layer.load();
            $.ajax({
                url: delUrl,
                method: "post",
                data: {id: row.id},
                success: function () {
                    var $table = $("#table");
                    $table.bootstrapTable('refresh');
                },
                error: function (error) {

                }
            });
            layer.close(load);
        }, function () {

        });
    }
};

$("#btn_add").click(function () {
    $(".oldImgDiv").hide();
    $("input[name='bannerName']").val("");
    $("input[name='bannerImgUri']").val("");
    $("input[name='linkUrl']").val("");
    $("input[name='id']").val("");
    // uploader
    parent.uploader($('#banner-uploader'), function (path) {
        $("#bannerImgUri").val(path);
    }, {
        allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
        itemLimit: 1,
        sizeLimit: 3 * 1024 * 1024
    });

});


function saveOrUpdate(url, id, activityName, bannerImgUri, linkUrl) {
    var load = layer.load();
    $.ajax({
        url: url,
        method: "post",
        data: {id: id, bannerName: activityName, bannerImgUri: bannerImgUri, linkUrl: linkUrl},
        success: function () {
            var $table = $("#table");
            $table.bootstrapTable('refresh');
        },
        error: function (error) {

        }
    })
    layer.close(load);
}

$(".saveOrUpdate").click(function () {
    if ($("input[name='bannerName']").val() == "") {
        layer.alert("活动名称不能为空");
        return;
    }
    if ($("input[name='bannerImgUri']").val() == "") {
        layer.alert("图片不能为空");
        return;
    }
    if ($("input[name='id']").val() == "") {
        saveOrUpdate(addUrl, null, $("input[name='bannerName']").val(), $("input[name='bannerImgUri']").val(), $("input[name='linkUrl']").val());
    } else {
        saveOrUpdate(updateUrl, $("input[name='id']").val(), $("input[name='bannerName']").val(), $("input[name='bannerImgUri']").val(), $("input[name='linkUrl']").val());
    }

});

// uploader
parent.uploader($('#banner-uploader'), function (path) {
    $("#bannerImgUri").val(path);
}, {
    allowedExtensions: ['jpeg', 'jpg', 'png', 'bmp'],
    itemLimit: 1,
    sizeLimit: 3 * 1024 * 1024
});


