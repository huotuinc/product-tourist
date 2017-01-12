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

