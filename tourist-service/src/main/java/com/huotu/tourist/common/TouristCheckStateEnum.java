package com.huotu.tourist.common;

/**
 * 结算状态
 * Created by lhx on 2016/12/16.
 */

public enum TouristCheckStateEnum implements CommonEnum  {

    NotChecking(0, "未审核", "未审核"),
    CheckFinish(1, "已审核", "已审核，审核通过");

    private final int code;
    private final String value;
    private final String description;

    TouristCheckStateEnum(int code, String value, String getDescription) {
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
