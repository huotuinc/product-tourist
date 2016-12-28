addUrl = /*[[@{/distributionPlatform/saveTouristType}]]*/ "touristTypeList.html";
updateUrl = /*[[@{/distributionPlatform/updateTouristType}]]*/ "touristTypeList.html";

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

$(".saveOrUpdate").click(function () {
    if ($("input[name='typeName']").val() == "") {
        layer.alert("线路名称不能为空")
        return;
    }
    if ($("input[name='id']").val() == "") {
        $("#form").attr("action", addUrl);
    } else {
        $("#form").attr("action", updateUrl);
    }
    $("#form").submit();
});



