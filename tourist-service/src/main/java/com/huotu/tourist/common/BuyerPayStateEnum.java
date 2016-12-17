package com.huotu.tourist.common;

/**
 * 结算状态
 * Created by lhx on 2016/12/16.
 */

public enum BuyerPayStateEnum implements CommonEnum  {

    NotPay(0, "未支付", "未支付"),
    PayFinish(1, "已支付", "已支付");

    private final int code;
    private final String value;
    private final String description;

    BuyerPayStateEnum(int code, String value, String getDescription) {
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
