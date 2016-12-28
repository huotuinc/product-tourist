addUrl = /*[[@{/distributionPlatform/saveActivityType}]]*/ "activityTypeList.html";
updateUrl = /*[[@{/distributionPlatform/updateActivityType}]]*/ "activityTypeList.html";

actionFormatter = function (value, row, index) {
    return '<button  class="btn btn-primary update" data-toggle="modal" data-target="#myModal">修改</button> '
        + '<a href="activityTypeList.html" th:href="@{/distributionPlatform/delActivityType(id=${' + row.id + '})}"' +
        ' class="btn btn-danger">删除</a>';
};

window.actionEvents = {
    'click .update': function (e, value, row, index) {
        $("input[name='activityName']").val(row.activityName);
        $("input[name='id']").val(row.id);
    }
};

$("#btn_add").click(function () {
    $("input[name='activityName']").val("");
    $("input[name='id']").val("");
});

$(".saveOrUpdate").click(function () {
    if ($("input[name='activityName']").val() == "") {
        layer.alert("活动名称不能为空")
    }
    if ($("input[name='id']").val() == "") {
        $("#form").attr("action", addUrl);
    } else {
        $("#form").attr("action", updateUrl);
    }
    $("#form").submit();

});

$("#form").validate({
    rules: {
        activityName: {
            required: true
        }
    },
    messages: {
        activityName: {
            required: "名称不能为空"
        }
    },
    submitHandler: function (form, ev) {
        form.submit();
    },
    invalidHandler: function () {
        return true;
    }
});
