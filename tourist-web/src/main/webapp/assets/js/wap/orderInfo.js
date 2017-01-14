var affirmOrder=function(obj,orderId){
    $.confirm("确定完成订单吗", function() {
        //点击确认后的回调函数
        modifyOrderStateAjax(obj,orderId,3);
    });
};
var cancelOrder=function(obj,orderId){
    $.confirm("确定取消订单吗", function() {
        //点击确认后的回调函数
        modifyOrderStateAjax(obj,orderId,6);
    });
};
var refundOrder=function(obj,orderId){
    var text=$(obj).text();
    $.confirm("确定"+text+"吗", function() {
        var stateCode;
        if(text=="申请退款"){
            stateCode=4;
        }else {
            stateCode=1;
        }
        modifyOrderStateAjax(obj,orderId,stateCode);
    });
};
var modifyOrderStateAjax=function(obj,orderId,stateCode){
    $.ajax({
        type:'POST',
        url: modifyOrderStateUrl,
        dataType: 'json',
        data: {id:orderId,orderState:stateCode},
        success:function(result){
            if(result.data==200){
                $.alert("状态修改成功!");
                window.setTimeout("window.location=location.href",1000);
            }else {
                $.alert("状态失败!");
            }

        },
        error:function(e){
            $.alert("修改出错!");
        }
    });

};

