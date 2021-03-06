package com.huotu.tourist.common;

/**
 * 订单状态
 * Created by lhx on 2016/12/16.
 */

public enum OrderStateEnum implements CommonEnum {
    NotPay(0, "未支付", "未支付"),
    PayFinish(1, "已支付", "已支付"),
    NotFinish(2, "已确认", "已确认(未完成)"),
    Finish(3, "已完成", "已完成"),
    Refunds(4, "退款中", "退款中"),
    RefundsFinish(5, "已退款", "已退款"),
    Invalid(6, "已取消", "已取消(作废)");


    private final int code;
    private final String value;
    private final String description;

    OrderStateEnum(int code, String value, String getDescription) {
        this.code = code;
        this.value = value;
        this.description = getDescription;
    }

    public static OrderStateEnum getOrderStateByCode(int code) {
        switch (code) {
            case 0:
                return OrderStateEnum.NotPay;
            case 1:
                return OrderStateEnum.PayFinish;
            case 2:
                return OrderStateEnum.NotFinish;
            case 3:
                return OrderStateEnum.Finish;
            case 4:
                return OrderStateEnum.Invalid;
            case 5:
                return OrderStateEnum.Refunds;
            default:
                return OrderStateEnum.RefundsFinish;
        }
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
