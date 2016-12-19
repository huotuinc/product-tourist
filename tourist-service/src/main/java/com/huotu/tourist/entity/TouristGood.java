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
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 线路商品
 *
 * @author CJ
 */
@Entity
@Table(name = "Tourist_Good")
@Getter
@Setter
public class TouristGood extends BaseModel {
    /**
     * 线路名称
     */
    @Column(length = 100)
    private String touristName;

    /**
     * 活动类型
     */
    @ManyToOne
    @JoinColumn
    private ActivityType activityType;

    /**
     * 线路类型
     */
    @ManyToOne
    @JoinColumn
    private TouristType touristType;

    /**
     * 线路所属供应商
     */
    @ManyToOne
    @JoinColumn
    private TouristSupplier touristSupplier;

    /**
     * 线路审核状态
     */
    @Column
    private TouristCheckStateEnum touristCheckState;

    /**
     * 线路特色
     */
    @Column
    @Lob
    private String touristFeatures;

    /**
     * 目的地
     */
    @Column(length = 100)
    private String destination;

    /**
     * 出发地
     */
    @Column(length = 100)
    private String placeOfDeparture;

    /**
     * 途径地
     */
    @Column(length = 100)
    private String travelledAddress;

    /**
     * 价格
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 成人折扣
     */
    @Column(precision = 4, scale = 2)
    private BigDecimal AdultDiscount;
    /**
     * 儿童折扣
     */
    @Column(precision = 4, scale = 2)
    private BigDecimal childrenDiscount;

    /**
     * 按路线价格的比例返佣比例。0-100。不得大于100
     */
    @Column(precision = 4, scale = 2)
    private BigDecimal rebate;

    /**
     * 地方接待人
     */
    @Column(length = 15)
    private String receptionPerson;

    /**
     * 接待人电话
     */
    @Column(length = 15)
    private String receptionTelephone;

    /**
     * 活动详情
     */
    @Lob
    @Column
    private String eventDetails;

    /**
     * 注意事项
     */
    @Lob
    @Column
    private String beCareful;

    /**
     * 推荐
     */
    @Column
    private Boolean recommend;


}
