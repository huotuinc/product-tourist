/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.entity;

import com.huotu.tourist.common.OrderStateEnum;
import com.huotu.tourist.common.PayTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 线路订单
 * @author CJ
 */
public class TouristOrder extends BaseModel {

    /**
     * 线路
     */
    private TouristGood touristGood;

    /**
     * 采购商
     */
    private TouristBuyer touristBuyer;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private OrderStateEnum orderState;

    /**
     * 订单总金额
     */
    private String orderMoney;

    /**
     * 返还佣金
     */
    private BigDecimal returnCommission;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 购买数量
     */
    private int buyCount;

    /**
     * 人员信息 todo 不妥
     */
    private List<Map<String,Object>> persons;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付方式
     */
    private PayTypeEnum payType;

    /**
     * 余额抵扣
     */
    private BigDecimal balanceDeduction;

    /**
     * 小金库抵扣
     */
    private BigDecimal coffersDeduction;

    /**
     * 备注
     */
    private String remarks;

}
