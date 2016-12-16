/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.entity;

import com.huotu.tourist.common.TouristCheckStateEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 线路商品
 *
 * @author CJ
 */
public class TouristGood {
    /**
     * 线路名称
     */
    private String touristName;

    /**
     * 活动类型
     */
    private ActivityType activityType;

    /**
     * 线路类型
     */
    private TouristType touristType;


    /**
     * 线路所属供应商
     */
    private TouristSupplier touristSupplier;

    /**
     * 线路审核状态
     */
    private TouristCheckStateEnum touristCheckState;


    /**
     * 线路特色
     */
    private String touristFeatures;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 出发地
     */
    private String placeOfDeparture;

    /**
     * 途径地
     */
    private String travelledAddress;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 成人折扣
     */
    private BigDecimal AdultDiscount;
    /**
     * 儿童折扣
     */
    private BigDecimal childrenDiscount;

    /**
     * 按路线价格的比例返佣比例。0-100。不得大于100
     */
    private BigDecimal rebate;

    /**
     * 地方接待人
     */
    private String receptionPerson;

    /**
     * 接待人电话
     */
    private String receptionTelephone;

    /**
     * 出行时间及人数
     * 时间，人数
     */
    private List<Map<LocalDateTime,Integer>>  departureTimeAndNumber;


    /**
     * 活动详情
     */
    private String eventDetails;

    /**
     * 注意事项
     */
    private String beCareful;




}
