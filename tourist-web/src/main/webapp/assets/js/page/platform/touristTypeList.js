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
    if ($("input[name='id']").val() == "") {
        alert("添加")
    } else {
        alert("修改")
    }
});



