package com.huotu.tourist.common;

/**
 * 结算状态
 * Created by lhx on 2016/12/16.
 */

public enum TravelerTypeEnum implements CommonEnum {

    adult(0, "成人", "成人"),
    children(1, "儿童", "儿童");

    private final int code;
    private final String value;
    private final String description;

    TravelerTypeEnum(int code, String value, String getDescription) {
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
