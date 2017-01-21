var ua = window.navigator.userAgent.toLowerCase();
if (ua.match(/MicroMessenger/i) == 'micromessenger') {
    $("#weixin").show();
    $("#alipay").hide();
} else {
    $("#weixin").hide();
    $("#alipay").show();
}
$(function () {
    $("#weixin").click(function () {
        var data = {payType: 9};
        orderPay(data);
    });
    $("#alipay").click(function () {
        var data = {payType: 1};
        orderPay(data);
    });
});
function orderPay(requestData) {
    var load = layer.load();
    $.ajax({
        url: $.buyerOrderPayUrl,
        method: "post",
        dataType: "json",
        data: requestData,
        complete: function () {
            layer.close(load);
        },
        success: function (apiResult) {
            if (apiResult.code == 200) {
                //前台跳转到商城支付
                utils.GetPaymentUrl(apiResult.customerId, apiResult.orderId, requestData.payType,
                    $.touristUrl + "/wap/");
            } else {
                layer.alert(apiResult.msg);
            }
        },
        error: function (error) {
            layer.alert(error);
        }
    });
}