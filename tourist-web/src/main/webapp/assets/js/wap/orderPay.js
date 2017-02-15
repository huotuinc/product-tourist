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
        orderPay(data,1);
    });
    $("#alipay").click(function () {
        var data = {orderId: $("#orderId").val(), payType: 1};
        orderPay(data,0);
    });
});

function orderPay(data,payType) {
    var load = layer.load();
    $.ajax({
        url: $.orderPayUrl,
        method: "post",
        dataType: "json",
        data: {orderId: $("#orderId").val(), payType: payType},
        complete: function () {
            layer.close(load);
        },
        success: function (apiResult) {
            if (apiResult.code == 200) {
                //前台跳转到
                var payUrl = utils.GetPaymentUrl($("#customerId").val(), apiResult.mallOrderNo, data.payType
                    , $.touristUrl + "/wap/");
                if ($.makePaySuccess) {
                    $.ajax({
                        url: $.buyerOrderNotifyUrl,
                        method: 'post',
                        data: {
                            mallOrderNo: apiResult.mallOrderNo,
                            payType: data.payType == 1 ? 0 : 1,
                            pay: true,
                            orderType: 0
                        },
                        success: function () {
                            location.href = $.touristUrl + "/wap/";
                        }
                    });
                } else {
                    if (payUrl.indexOf("http://") == 0) {
                        window.location.href = payUrl;
                    } else {
                        window.location.href = $.mallUrl + payUrl;
                    }
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