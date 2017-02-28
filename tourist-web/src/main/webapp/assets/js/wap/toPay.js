$(function () {

    //总金额
    var buyerMoney = 0;

    $(".money").text('￥' + buyerMoney);
    $(".moneypay").text('应付￥' + buyerMoney);

    //成人减1
    $(".priceDecrease").click(function () {
        if (parseInt($("input[name='adult']").val()) > 0) {

            $("input[name='adult']").val(parseInt($("input[name='adult']").val()) - 1);
            buyerMoney =
                parseInt($("input[name='adult']").val()) * price + parseInt($("input[name='children']").val()) * childPrice;
            if ((parseInt($("input[name='adult']").val()) == 0 && parseInt($("input[name='children']").val()) ==
                0) || buyerMoney - moneyPay() < 0) {
                $("input[type='checkbox']").prop("checked", false);
            }
            $(".money").text('￥' + buyerMoney);
            $('.adultDiv:last').remove();
            moneyPay();

        }
    });
    //成人加1
    $(".priceIncrease").click(function () {
        if (parseInt($("input[name='adult']").val()) + parseInt($("input[name='children']").val()) < amount) {
            $("input[name='adult']").val(parseInt($("input[name='adult']").val()) + 1)
            buyerMoney =
                parseInt($("input[name='adult']").val()) * price + parseInt($("input[name='children']").val()) * childPrice;
            $(".itemTemplate").clone().removeClass("itemTemplate").addClass("adultDiv").appendTo(".items")
                .show().find(".travelerType").val(0);
            $(".money").text('￥' + buyerMoney);
            moneyPay();
        }
    });

    //儿童减1
    $(".childPriceDecrease").click(function () {
        if (parseInt($("input[name='children']").val()) > 0) {
            $("input[name='children']").val(parseInt($("input[name='children']").val()) - 1)
            buyerMoney =
                parseInt($("input[name='adult']").val()) * price + parseInt($("input[name='children']").val()) * childPrice;
            if ((parseInt($("input[name='adult']").val()) == 0 && parseInt($("input[name='children']").val()) ==
                0) || buyerMoney - moneyPay() < 0) {
                $("input[type='checkbox']").prop("checked", false);
            }
            $(".money").text('￥' + buyerMoney);
            $('.childrenDiv:last').remove();
            moneyPay();
        }
    });

    //儿童加1
    $(".childPriceIncrease").click(function () {
        if (parseInt($("input[name='adult']").val()) + parseInt($("input[name='children']").val()) < amount) {
            $("input[name='children']").val(parseInt($("input[name='children']").val()) + 1)
            $(".itemTemplate").clone().removeClass("itemTemplate").addClass("childrenDiv").appendTo(".items")
                .show().find(".travelerType").val(1);
            buyerMoney =
                parseInt($("input[name='adult']").val()) * price + parseInt($("input[name='children']").val()) * childPrice;
            $(".money").text('￥' + buyerMoney);
            moneyPay();
        }
    });

    //删除人员信息
    $(".items").on('click', '.delete', function () {
        $(this).parent().parent().remove();
        var travelerType = $(this).parent().parent().find(".travelerType").val();
        if (travelerType == 0) {
            $("input[name='adult']").val(parseInt($("input[name='adult']").val()) - 1)
        } else {
            $("input[name='children']").val(parseInt($("input[name='children']").val()) - 1)
        }
        buyerMoney =
            parseInt($("input[name='adult']").val()) * price + parseInt($("input[name='children']").val()) * childPrice;

        if ((parseInt($("input[name='adult']").val()) == 0 && parseInt($("input[name='children']").val()) ==
            0) || buyerMoney - moneyPay() < 0) {
            $("input[type='checkbox']").prop("checked", false);
        }
        amount -= 1;
        $(".money").text('￥' + buyerMoney);
        moneyPay();
    });


    $("input[type='checkbox']").change(moneyPay);

    function moneyPay() {
        // 所有计算步骤都在这里
        // 已扣除金额
        var mallNum = 0;
        $("input[type='checkbox']").each(function () {
            var name = $(this).attr("name");
            var useSpanEle = $('.twossw[data-name=' + name + ']');
            useSpanEle.text('减￥0');
            useSpanEle.attr('data-value', '0');
            if ($(this).is(':checked')) {
                // 剩余最多可扣金额
                var maxCostableMoney = buyerMoney - mallNum;
                // 获取比例
                var rate = $(this).attr('data-value') || 1;
                var maxCostable = maxCostableMoney * rate;
                var maxSpanEle = $('.twoss[data-name=' + name + ']');

                var maxValue = parseInt(maxSpanEle.attr('data-value'));

                var toCost = Math.min(maxCostable, maxValue);
                mallNum += toCost / rate;

                useSpanEle.text('减￥' + toCost / rate);
                useSpanEle.attr('data-value', toCost);
            }
        });
        $(".moneypay").text('应付￥' + (buyerMoney - mallNum));
        return mallNum;
    }


    $("#submitBtn").click(function () {
        if ($(".items .item").length < 1) {
            $.alert("请添加游客");
            return;
        }
        var travelers = new Array();
        var flag = false;
        $(".items .item").each(function () {
            var travelerType = parseInt($(this).find("input[name='travelerType']").val());
            var name = $(this).find("input[name='name']").val();
            var telPhone = $(this).find("input[name='telPhone']").val();
            var IDNo = $(this).find("input[name='IDNo']").val();
            var age = parseInt($(this).find("input[name='age']").val());
            var sex = parseInt($(this).find("select[name='sex']").val());
            if (name == '' || telPhone == '' || telPhone.length != 11 || IDNo == '' || IDNo.length != 18 || age <= 0) {
                flag = true;
                return;
            }
            var traveler = {
                "travelerType": travelerType,
                "name": name,
                "telPhone": telPhone,
                "number": IDNo,
                "age": age,
                "sex": sex
            };
            travelers.push(traveler);
        });
        if (flag) {
            $.alert("请完善游客信息，姓名、电话、身份证号，年龄必填,且数据长度要符合");
            return;
        }
        var data = {goodId: goodId, routeId: routeId, travelers: JSON.stringify(travelers)};
        data.remark = $("textarea[name='remark']").val();
        data.mallIntegral = $('.mallIntegral').attr("data-value");
        data.mallBalance = $('.mallBalance').attr("data-value");
        data.mallCoffers = $('.mallCoffers').attr("data-value");
        $.ajax({
            url: addOrderUrl,
            method: "post",
            data: data,
            dataType: "json",
            success: function (apiResult) {
                window.location.href = toOrderInfoPage + "?orderId=" + apiResult.orderId;
            },
            error: function (error) {
                $.alert(error.msg)
            }
        });
    })
});