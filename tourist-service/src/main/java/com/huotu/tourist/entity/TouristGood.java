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
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final List<Selection<TouristGood, ?>> selections = Arrays.asList(
            new SimpleSelection<TouristGood, String>("id", "id"),
            new SimpleSelection<TouristGood, String>("touristName", "touristName")
            , new SimpleSelection<TouristGood, String>("price", "price")
            , new SimpleSelection<TouristGood, String>("destination", "destination")
            , new SimpleSelection<TouristGood, String>("rebate", "rebate")
            , new SimpleSelection<TouristGood, String>("touristImgUri", "touristImgUri")
            , new SimpleSelection<TouristGood, String>("recommend", "recommend")
            , new Selection<TouristGood, String>() {
                @Override
                public String apply(TouristGood touristGood) {
                    return touristGood.getTouristSupplier().getSupplierName();
                }

                @Override
                public String getName() {
                    return "supplierName";
                }
            }
            , new Selection<TouristGood, String>() {
                @Override
                public String apply(TouristGood touristGood) {
                    return touristGood.getActivityType().getActivityName();
                }

                @Override
                public String getName() {
                    return "activityType";
                }
            }
            , new Selection<TouristGood, String>() {
                @Override
                public String apply(TouristGood touristGood) {
                    return touristGood.getTouristType().getTypeName();
                }

                @Override
                public String getName() {
                    return "touristType";
                }
            }
            , new Selection<TouristGood, Map>() {
                @Override
                public Map apply(TouristGood touristGood) {
                    Map map = new HashMap();
                    map.put("code", touristGood.getTouristCheckState().getCode().toString());
                    map.put("value", touristGood.getTouristCheckState().getValue().toString());
                    return map;
                }

                @Override
                public String getName() {
                    return "touristCheckState";
                }
            }

    );
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
    @Embedded
    @AssociationOverrides(
            {@AssociationOverride(name = "province",joinColumns = {@JoinColumn(name = "des_province")})
            ,@AssociationOverride(name = "town",joinColumns = {@JoinColumn(name = "des_town")})
            ,@AssociationOverride(name = "district",joinColumns = {@JoinColumn(name = "des_district")})
            })
    private Address destination;
    /**
     * 出发地
     */
    @Embedded
    @AssociationOverrides(
            {@AssociationOverride(name = "province",joinColumns = {@JoinColumn(name = "pla_province")})
            ,@AssociationOverride(name = "town",joinColumns = {@JoinColumn(name = "pla_town")})
            ,@AssociationOverride(name = "district",joinColumns = {@JoinColumn(name = "pla_district")})
            })
    private Address placeOfDeparture;
    /**
     * 途径地
     */
    @Embedded
    @AssociationOverrides(
            {@AssociationOverride(name = "province",joinColumns = {@JoinColumn(name = "tra_province")})
            ,@AssociationOverride(name = "town",joinColumns = {@JoinColumn(name = "tra_town")})
            ,@AssociationOverride(name = "district",joinColumns = {@JoinColumn(name = "tra_district")})
            })
    private Address travelledAddress;
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
     * 返佣比例。按路线价格的比例 0-100。不得大于100
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
     * 线路图片
     */
    @Column(length = 200)
    private String touristImgUri;
    /**
     * 推荐
     */
    @Column
    private Boolean recommend;

}
