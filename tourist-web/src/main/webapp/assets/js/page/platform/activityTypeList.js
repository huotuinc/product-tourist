addUrl = /*[[@{/distributionPlatform/saveActivityType}]]*/ "../../../mock/platform/httpJson.json";
;
updateUrl = /*[[@{/distributionPlatform/updateActivityType}]]*/ "../../../mock/platform/httpJson.json";
;
delUrl = /*[[@{/distributionPlatform/delActivityType}]]*/ "../../../mock/platform/httpJson.json";
;

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
                dataType: "json",
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
        dataType: "json",
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

