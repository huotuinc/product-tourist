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
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 线路订单
 * @author CJ
 */
@Entity
@Table(name = "Tourist_Order")
@Getter
@Setter
public class TouristOrder extends BaseModel {

    /**
     * 线路
     */
    @ManyToOne
    @JoinColumn
    private TouristGood touristGood;

    /**
     * 采购商
     */
    @ManyToOne
    @JoinColumn
    private TouristBuyer touristBuyer;

    /**
     * 订单号
     */
    @Column(unique = true, length = 50)
    private String orderNo;

    /**
     * 订单状态
     */
    @Column
    private OrderStateEnum orderState;

    /**
     * 订单总金额
     */
    @Column
    private BigDecimal orderMoney;

//    /**
//     * 返还佣金
//     */
//    @Column
//    private BigDecimal returnCommission;

//    /**
//     * 单价
//     */
//    @Column
//    private BigDecimal unitPrice;

//    /**
//     * 购买数量
//     */
//    @Column
//    private int buyCount;

    /**
     * 支付时间
     */
    @Column
    private LocalDateTime payTime;

    /**
     * 支付方式
     */
    @Column
    private PayTypeEnum payType;

//    /**
//     * 余额抵扣
//     */
//    @Column
//    private BigDecimal balanceDeduction;
//
//    /**
//     * 小金库抵扣
//     */
//    @Column
//    private BigDecimal coffersDeduction;

    /**
     * 备注
     */
    @Lob
    @Column
    private String remarks;

}
