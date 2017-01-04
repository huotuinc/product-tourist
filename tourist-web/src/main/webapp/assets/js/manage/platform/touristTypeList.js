addUrl = /*[[@{/distributionPlatform/saveTouristType}]]*/ "../../../mock/platform/httpJson.json";
updateUrl = /*[[@{/distributionPlatform/updateTouristType}]]*/ "../../../mock/platform/httpJson.json";

actionFormatter = function (value, row, index) {
    return '<button class="btn btn-primary update" data-toggle="modal" data-target="#myModal">修改</button> ';
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



