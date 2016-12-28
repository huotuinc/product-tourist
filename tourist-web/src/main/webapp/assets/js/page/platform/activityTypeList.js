addUrl = /*[[@{/distributionPlatform/saveActivityType}]]*/ "activityTypeList.html";
updateUrl = /*[[@{/distributionPlatform/updateActivityType}]]*/ "activityTypeList.html";
delUrl = /*[[@{/distributionPlatform/delActivityType}]]*/ "activityTypeList.html";

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
            window.location.href = delUrl + "?id=" + row.id;
        }, function () {

        });
    }
};

$("#btn_add").click(function () {
    $("input[name='activityName']").val("");
    $("input[name='id']").val("");
});

$(".saveOrUpdate").click(function () {
    if ($("input[name='activityName']").val() == "") {
        layer.alert("活动名称不能为空");
        return;
    }
    if ($("input[name='id']").val() == "") {
        $("#form").attr("action", addUrl);
    } else {
        $("#form").attr("action", updateUrl);
    }
    $("#form").submit();

});

