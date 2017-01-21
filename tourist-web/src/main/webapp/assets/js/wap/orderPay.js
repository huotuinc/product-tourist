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
        var data = {orderId: $("#orderId").val(), payType: 9};
        orderPay(data);
    });
    $("#alipay").click(function () {
        var data = {orderId: $("#orderId").val(), payType: 1};
        orderPay(data);
    });
});

function orderPay(data) {
    var load = layer.load();
    $.ajax({
        url: $.orderPayUrl,
        method: "post",
        dataType: "json",
        data: data,
        complete: function () {
            layer.close(load);
        },
        success: function (apiResult) {
            if (apiResult.code == 200) {
                //前台跳转到
                utils.GetPaymentUrl($("#customerId").val(), $("#orderId").val(), data.payType
                    , $.touristUrl + "/wap/");
                if ($.makePaySuccess) {
                    $.ajax({
                        url: $.buyerOrderNotifyUrl,
                        method: 'post',
                        data: {
                            mallOrderNo: $("#orderId").val(),
                            payType: data.payType == 1 ? 0 : 1,
                            pay: true,
                            orderType: 0
                        },
                        success: function () {
                            location.href = $.touristUrl + "/wap/";
                        }
                    });
                }
            } else {
                layer.alert(apiResult.msg);
            }
        },
        error: function (error) {
            layer.alert(error);
        }
    })
}