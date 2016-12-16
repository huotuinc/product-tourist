package com.huotu.tourist.common;

/**
 * 收款账户类型
 * Created by lhx on 2016/12/16.
 */

public enum CollectionAccountTypeEnum implements CommonEnum  {

    Alipay(0, "支付宝", "支付宝"),
    BankCard(1, "银行卡", "银行卡");

    private final int code;
    private final String value;
    private final String description;

    CollectionAccountTypeEnum(int code, String value, String getDescription) {
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
