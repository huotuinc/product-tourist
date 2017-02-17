

actionFormatter = function (value, row, index) {
    if (row.typeName != '长途游' && row.typeName != '短途游') {
        return '<button class="btn btn-primary update" data-toggle="modal"' +
            ' data-target="#myModal">修改</button> ';
    } else {
        return '系统数据不允许更改';
    }

};

window.actionEvents = {
    'click .update': function (e, value, row, index) {
        $("input[name='typeName']").val(row.typeName);
        $("input[name='id']").val(row.id);
    }
};

$("#btn_add").click(function () {
    $("input[name='typeName']").val("");
    $("input[name='id']").val("");
});


function saveOrUpdate(url, id, typeName) {
    var load = layer.load();
    $.ajax({
        url: url,
        method: "post",
        data: {id: id, typeName: typeName},
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
    if ($("input[name='typeName']").val() == "") {
        layer.alert("线路名称不能为空");
        return;
    }
    if ($("input[name='id']").val() == "") {
        saveOrUpdate(addUrl, null, $("input[name='typeName']").val());
    } else {
        saveOrUpdate(updateUrl, $("input[name='id']").val(), $("input[name='typeName']").val());
    }
});
getParams = function (params) {
    var sort = params.sortName != undefined ? params.sortName + "," + params.sortOrder : undefined;
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.pageSize, //页面大小
        pageNo: params.pageNumber - 1, //页码
        sort: sort
        // sortOrder: params.sortOrder,
        // sortName: params.sortName
    };
    return temp;
};



