package com.huotu.tourist.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 后台订单列表model
 * Created by slt on 2016/12/19.
 */
@Getter
@Setter
@EqualsAndHashCode
public class TouristOrderModel {
    /**
     * 订单ID
     */
    private long id;

    /**
     * 线路(商品)名称
     */
    private String touristName;

    /**
     * 购买人姓名
     */
    private String buyerName;

    /**
     * 订单金额
     */
    private double orderMoney;

    /**
     * 支付状态
     */
    private String orderState;

    /**
     * 出行时间
     */
    private String touristDate;

    /**
     * 游客人数
     */
    private int peopleNumber;

    /**
     * 备注
     */
    private String remarks;



}
