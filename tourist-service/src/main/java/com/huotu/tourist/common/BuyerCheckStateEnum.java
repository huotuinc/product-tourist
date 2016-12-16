package com.huotu.tourist.common;

/**
 * 结算状态
 * Created by lhx on 2016/12/16.
 */

public enum BuyerCheckStateEnum implements CommonEnum  {

    Checking(0, "审核中", "审核中"),
    CheckFinish(1, "已审核", "已审核，审核通过"),
    Frozen(2, "已冻结", "已冻结");

    private final int code;
    private final String value;
    private final String description;

    BuyerCheckStateEnum(int code, String value, String getDescription) {
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
