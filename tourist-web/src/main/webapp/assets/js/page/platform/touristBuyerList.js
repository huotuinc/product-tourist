actionFormatter = function (value, row, index) {
    if (row.checkState.code == 0) {
        return '<a class="btn btn-primary update" href="touristBuyer.html"' +
            ' th:href="@{/distributionPlatform/toBuyerList}">审核通过</a> '
            + ' <button class="btn btn-primary frozen">冻结</button>';
    } else if (row.checkState.code == 1) {
        return [
            ' <button class="btn btn-primary frozen">冻结</button>'
        ].join('');
    }
};

window.actionEvents = {
    'click .frozen': function (e, value, row, index) {
        alert('冻结操作, row: ' + JSON.stringify(row));
        console.log(value, row, index);
    }
};

businessLicencesUriFormatter = function (value, row, index) {
    return ['<img src="' + row.businessLicencesUri + '">'].join('');
};

IDCardImgFormatter = function (value, row, index) {
    return [
        '<img src="' + row.IDCardImg.IDElevationsUri + '" style="vertical-align:bottom">正面</img>',
        '<img src="' + row.IDCardImg.IDInverseUri + '" style="vertical-align:bottom">反面</img>'
    ].join('');
};

getParams = function (params) {
    var temp = {
        //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        pageSize: params.limit, //页面大小
        pageNo: params.offset, //页码
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




