/**
 * Created by slt  2016/12/26.
 */
$(function () {
    exportEdit();
});

/**
 * 导出表格配置
 */
var exportEdit = function () {
    $('#toolbar').find('select').change(function () {
        var $table = $("table");
        var pageSize = $table.bootstrapTable("getOptions").totalRows;
        if ($(this).val() === 'all') {
            pageSize = $table.bootstrapTable("getOptions").totalRows;
        }
        var a = {
            exportDataType: $(this).val(), pageSize: pageSize
        }
        $table.bootstrapTable('destroy').bootstrapTable(a);
    });
};


var modifySettlementState=function(){
    $.ajax({
        type:'POST',
        url: auditUrl,
        dataType: 'text',
        data: {id:settlementId,state:1},
        success:function(result){
            layer.msg("保存成功！");
            window.setTimeout("window.location=location.href",1000);
        },
        error:function(e){
            layer.msg("保存出错！");
            //window.setTimeout("window.location=location.href",1000);
        }
    });
};

/**
 * 搜素参数添加
 * @param params
 */
var getParams= function(params) {
    var sort=params.sort!=undefined?params.sort+","+params.order:undefined;
    var temp = {
        settlementId:settlementId,
        pageSize: params.limit, //页面大小
        pageNo: params.offset/params.limit, //页码
        sort: sort
    };
    return temp;
};

