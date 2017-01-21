
actionFormatter = function (value, row, index) {
    if (row.checkState.code == 0) {
        return '<button class="btn btn-primary update" >审核通过</button> '
            + ' <button class="btn btn-primary frozen">冻结</button>';
    } else if (row.checkState.code == 1) {
        return [
            ' <button class="btn btn-primary frozen">冻结</button>'
        ].join('');
    } else if (row.checkState.code == 2) {
        return [
            ' <button class="btn btn-primary unfrozen">取消冻结</button>'
        ].join('');
    }
};

function updateCheckState(row, checkState) {
    var load = layer.load();
    $.ajax({
        url: updateBuyerCheckState,
        method: "post",
        data: {id: row.id, checkState: checkState},
        success: function () {
            console.log("success");
            layer.close(load);
            var $table = $('#table');
            $table.bootstrapTable('refresh');
        },
        error: function (error) {
            console.log("error");
            layer.close(load);
        }
    })
}

window.actionEvents = {
    'click .frozen': function (e, value, row, index) {
        layer.confirm('确定冻结吗？', {
            btn: ['冻结', '取消']
        }, function (index) {
            layer.close(index);
            updateCheckState(row, 2);

        }, function () {

        });
    },
    'click .update': function (e, value, row, index) {
        layer.confirm('确定审核通过吗？', {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            updateCheckState(row, 1);
        }, function () {

        });
    },
    'click .unfrozen': function (e, value, row, index) {
        layer.confirm('确定取消冻结吗？', {
            btn: ['确定', '取消']
        }, function (index) {
            layer.close(index);
            updateCheckState(row, 1);
        }, function () {
        });
    },

};

businessLicencesUriFormatter = function (value, row, index) {
    return ['<img src="' + row.businessLicencesUri + '" width="50" height="50">'].join('');
};

IDCardImgFormatter = function (value, row, index) {

    return [
        '<img src="' + row.IDCardImg.IDElevationsUri + '" style="vertical-align:bottom" width="50" height="50">正面</img>',
        '<img src="' + row.IDCardImg.IDInverseUri + '" style="vertical-align:bottom" width="50" height="50">反面</img>'
    ].join('');
};

getParams = function (params) {
    var sort = params.sortName != undefined ? params.sortName + "," + params.sortOrder : undefined;
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.pageSize, //页面大小
        pageNo: params.pageNumber - 1, //页码
        sort: sort,
        // sortOrder: params.sortOrder,
        // sortName: params.sortName,
        buyerName: $("input[name='buyerName']").val(),
        buyerDirector: $("input[name='buyerDirector']").val(),
        telPhone: $("input[name='telPhone']").val(),
        buyerCheckState: $("select[name='buyerCheckState']").val()
    };
    return temp;
};

$('.btnSearch').click(function () {
    var $table = $('#table');
    $table.bootstrapTable('refresh');
});




