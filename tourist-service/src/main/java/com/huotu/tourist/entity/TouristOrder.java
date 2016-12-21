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
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 线路订单
 *
 * @author CJ
 */
@Entity
@Table(name = "Tourist_Order")
@Getter
@Setter
public class TouristOrder extends BaseModel {

    public static List<Selection<TouristOrder, ?>> htmlSelections = Arrays.asList(
            new SimpleSelection<TouristOrder,Long>("id","id")
            ,new SimpleSelection<TouristOrder, String>("touristGood.touristName", "touristName")
            ,new SimpleSelection<TouristOrder,BigDecimal>("orderMoney","orderMoney")
            ,new SimpleSelection<TouristOrder,String>("orderNo","orderNo")
            ,new SimpleSelection<TouristOrder,String>("payType.value", "payType")
            ,new SimpleSelection<TouristOrder,String>("remarks", "remarks")
            ,new SimpleSelection<TouristOrder,String>("orderState.value", "orderState")
            ,new SimpleSelection<TouristOrder,String>("payType.value", "payType")
            ,new Selection<TouristOrder, String>() {
                @Override
                public String getName() {
                    return "buyerName";
                }

                @Override
                public String apply(TouristOrder touristOrder) {
                    return touristOrder.getTouristBuyer().getBuyerName() + touristOrder.getTouristBuyer().getTelPhone();
                }
            }
    );

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
    @Column(precision = 10, scale = 2)
    private BigDecimal orderMoney;

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

    /**
     * 备注
     */
    @Lob
    @Column
    private String remarks;
}
