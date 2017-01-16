
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
}
actionFormatter = function (value, row, index) {
    return '<button  class="btn btn-primary update" data-toggle="modal" data-target="#myModal">修改</button> '
        + '<button class="btn btn-danger del" >删除</button>';
};

window.actionEvents = {
    'click .update': function (e, value, row, index) {
        $("input[name='activityName']").val(row.activityName);
        $("input[name='id']").val(row.id);
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
    $("input[name='activityName']").val("");
    $("input[name='id']").val("");
});


function saveOrUpdate(url, id, activityName) {
    var load = layer.load();
    $.ajax({
        url: url,
        method: "post",
        data: {id: id, activityName: activityName},
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
    if ($("input[name='activityName']").val() == "") {
        layer.alert("活动名称不能为空");
        return;
    }
    if ($("input[name='id']").val() == "") {
        saveOrUpdate(addUrl, null, $("input[name='activityName']").val());
    } else {
        saveOrUpdate(updateUrl, $("input[name='id']").val(), $("input[name='activityName']").val());
    }

});

