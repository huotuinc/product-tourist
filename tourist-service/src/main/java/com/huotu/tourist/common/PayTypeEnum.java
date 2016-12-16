package com.huotu.tourist.common;

/**
 * 结算状态
 * Created by lhx on 2016/12/16.
 */

public enum PayTypeEnum implements CommonEnum  {

    Alipay(0, "支付宝支付", "支付宝"),
    WeixinPay(1, "微信支付", "微信支付");

    private final int code;
    private final String value;
    private final String description;

    PayTypeEnum(int code, String value, String getDescription) {
        this.code = code;
        this.value = value;
        this.description = getDescription;
    }

    @Override
    public Object getCode() {
        return this.code;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
