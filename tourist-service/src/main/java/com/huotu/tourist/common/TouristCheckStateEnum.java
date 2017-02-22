package com.huotu.tourist.common;

/**
 * 线路审核状态
 * Created by lhx on 2016/12/16.
 */

public enum TouristCheckStateEnum implements CommonEnum  {
    Saved(0, "已保存", "已保存"),
    NotChecking(1, "未审核", "未审核"),
    CheckFinish(2, "已审核", "已审核，审核通过"),
    Recycle(3, "已回收", "已回收"),
    NotAudited(4, "未通过审核", "审核不通过");

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
