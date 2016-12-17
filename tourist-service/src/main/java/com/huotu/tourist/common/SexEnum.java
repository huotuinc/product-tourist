package com.huotu.tourist.common;

/**
 * 结算状态
 * Created by lhx on 2016/12/16.
 */

public enum SexEnum implements CommonEnum {

    man(0, "男", "男"),
    woman(1, "女", "女");

    private final int code;
    private final String value;
    private final String description;

    SexEnum(int code, String value, String getDescription) {
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
