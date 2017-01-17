/**
 * Created by slt  2016/12/26.
 */



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
        sort:sort,
        settlement:true
    };
    return temp;
};

