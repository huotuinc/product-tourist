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
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
@Cacheable(value = false)
public class TouristOrder extends BaseModel {

    public static List<Selection<TouristOrder, ?>> htmlSelections = Arrays.asList(
            new SimpleSelection<TouristOrder, Long>("id", "id")
            , new SimpleSelection<TouristOrder, String>("touristGood.touristName", "touristName")
            , new SimpleSelection<TouristOrder, String>("touristGood.touristImgUri", "touristImgUri")
            , new SimpleSelection<TouristOrder, BigDecimal>("orderMoney", "orderMoney")
            , new SimpleSelection<TouristOrder, String>("touristBuyer.buyerName", "buyerName")
            , new SimpleSelection<TouristOrder, String>("orderNo", "orderNo")
            , new SimpleSelection<TouristOrder, String>("payType.value", "payType")
            , new SimpleSelection<TouristOrder, String>("remarks", "remarks")
            , new SimpleSelection<TouristOrder, String>("orderState.value", "orderStateValue")
            , new SimpleSelection<TouristOrder, String>("orderState.code", "orderStateCode")
            , new SimpleSelection<TouristOrder, String>("payType.value", "payTypeValue")
            , new SimpleSelection<TouristOrder, String>("payType.code", "payTypeCode")
            , new Selection<TouristOrder, BigDecimal>() {
                @Override
                public BigDecimal apply(TouristOrder touristOrder) {
                    if (touristOrder.getTouristGood().getRebate() == null) {
                        return new BigDecimal(0);
                    }

                    return touristOrder.getOrderMoney().multiply(touristOrder.getTouristGood().getRebate()).setScale(2
                            , RoundingMode.HALF_UP);
                }

                @Override
                public String getName() {
                    return "commission";
                }
            }
            , new Selection<TouristOrder, Boolean>() {
                @Override
                public Boolean apply(TouristOrder touristOrder) {
                    return touristOrder.getSettlement() != null;
                }

                @Override
                public String getName() {
                    return "settlement";
                }
            }
//            , new SimpleSelection<TouristOrder, Boolean>("settlement","settlement")
//            , new Selection<TouristOrder, String>() {
//                @Override
//                public String getName() {
//                    return "buyerName";
//                }
//
//                @Override
//                public String apply(TouristOrder touristOrder) {
//                    return touristOrder.getTouristBuyer().getBuyerName();
//                }
//            }
            , new Selection<TouristOrder, String>() {
                @Override
                public String getName() {
                    return "payTime";
                }

                @Override
                public String apply(TouristOrder touristOrder) {
                    if (touristOrder.getPayTime() == null) {
                        return "";
                    }
                    return LocalDateTimeFormatter.toStr(touristOrder.getPayTime());
                }
            }
            , new Selection<TouristOrder, String>() {
                @Override
                public String getName() {
                    return "createTime";
                }

                @Override
                public String apply(TouristOrder touristOrder) {
                    if (touristOrder.getCreateTime() == null) {
                        return "";
                    }
                    return LocalDateTimeFormatter.toStr(touristOrder.getCreateTime());
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
     * 商城订单号
     */
    @Column(unique = true, length = 100)
    private String mallOrderNo;

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
     * 积分
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal mallIntegral;
    /**
     * 余额
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal mallBalance;
    /**
     * 小金库
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal mallCoffers;

    /**
     * 支付时间
     */
    @Column(columnDefinition = "datetime")
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

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "order")
    private List<Traveler> travelers;

    /**
     * 结算单
     */
    @ManyToOne
    private SettlementSheet settlement;



}
