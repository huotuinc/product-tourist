$(function(){
    var states=getUrlParam("states");
    if(states==null){
        states="all";
    }
    $("#"+states).trigger("click");
    loadOrderList();
    jScrollEdit();

});
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}


var loadOrderList=function(){
    $(".weui_navbar_item.orange").click(function(){
        var states=$(this).attr("id");
        location.href=showOrderList+"?states="+states+"&buyerId="+buyerId;

    });
};

var jScrollEdit=function(){
    $('.touristsRegion').jscroll({
        nextSelector: 'a.nextJScroll:last',
        loadingHtml: '<div class="weui-infinite-scroll"><div class="infinite-preloader"></div>滑动加载更多</div>'
    });

};

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
var  refundOrder=function(obj,orderId){
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
        dataType: 'text',
        data: {id:orderId,orderState:stateCode},
        success:function(result){
            $.alert("状态修改成功!");
            modifyOrderHtml(obj,stateCode);
        },
        error:function(e){
            $.alert("修改失败!");
        }
    });

};

var modifyOrderHtml=function(obj,stateCode){
    switch (stateCode){
        case 1:
            $(obj).text("申请退款");
            break;
        case 4:
            $(obj).text("取消退款");
            break;
        default:
            $(obj).hide();
    }
};

