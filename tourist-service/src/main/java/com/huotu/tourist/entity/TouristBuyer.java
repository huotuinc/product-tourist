/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.entity;

import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.common.BuyerPayStateEnum;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 线路采购商
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Tourist_Buyer")
@Getter
@Setter
public class TouristBuyer implements SystemUser {

    public static final List<Selection<TouristBuyer, ?>> selections = Arrays.asList(
            new SimpleSelection<TouristBuyer, String>("id", "id"),
            new SimpleSelection<TouristBuyer, String>("buyerName", "buyerName")
            , new SimpleSelection<TouristBuyer, String>("buyerDirector", "buyerDirector")
            , new SimpleSelection<TouristBuyer, String>("telPhone", "telPhone")
            , new SimpleSelection<TouristBuyer, String>("businessLicencesUri", "businessLicencesUri")
            , new SimpleSelection<TouristBuyer, String>("buyerId", "buyerId")
            , new SimpleSelection<TouristBuyer, String>("nickname", "nickname")
            , new SimpleSelection<TouristBuyer, String>("IDNo","IDNo")
            , new Selection<TouristBuyer, Map>() {
                @Override
                public Map apply(TouristBuyer touristBuyer) {
                    Map<String, String> map = new HashMap<>();
                    map.put("IDElevationsUri", touristBuyer.getIDElevationsUri());
                    map.put("IDInverseUri", touristBuyer.getIDInverseUri());
                    return map;
                }

                @Override
                public String getName() {
                    return "IDCardImg";
                }
            }, new Selection<TouristBuyer, Map>() {
                @Override
                public String getName() {
                    return "checkState";
                }

                @Override
                public Map apply(TouristBuyer touristBuyer) {
                    if (touristBuyer.getCheckState() == null) {
                        return null;
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("code", touristBuyer.checkState.getCode().toString());
                    map.put("value", touristBuyer.checkState.getValue().toString());
                    return map;
                }
            }
            , new Selection<TouristBuyer, Map>() {
                @Override
                public String getName() {
                    return "payState";
                }

                @Override
                public Map apply(TouristBuyer touristBuyer) {
                    if (touristBuyer.getPayState() == null) {
                        return null;
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("code", touristBuyer.payState.getCode().toString());
                    map.put("value", touristBuyer.payState.getValue().toString());
                    return map;
                }
            }
    );

    /**
     * 作为一个采购商，它是来自一个已登录的小伙伴；即他的id是已知的，应该属于分配值
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @Column(columnDefinition = "datetime")
    private LocalDateTime updateTime;

    /**
     * 采购商名称
     */
    @Column(length = 50)
    private String buyerName;
    /**
     * 采购商负责人
     */
    @Column(length = 50)
    private String buyerDirector;
    /**
     * 手机号
     */
    @Column(length = 15)
    private String telPhone;
    /**
     * 营业执照
     */
    @Column(length = 200)
    private String businessLicencesUri;
    /**
     * 身份证正面图
     */
    @Column(length = 200)
    private String IDElevationsUri;
    /**
     * 身份证反面图
     */
    @Column(length = 200)
    private String IDInverseUri;

    /**
     * 采购商ID
     */
    @Column(length = 100, unique = true)
    private String buyerId;

    /**
     * 昵称
     */
    @Column(length = 100)
    private String nickname;

    /**
     * 审核状态
     */
    @Column
    private BuyerCheckStateEnum checkState;

    /**
     * 支付状态
     */
    @Column
    private BuyerPayStateEnum payState;

    /**
     * 身份证号
     */
    @Column(length = 18)
    private String IDNo;

    @Override
    public boolean isSupplier() {
        return false;
    }

    @Override
    public boolean isPlatformUser() {
        return false;
    }
}
