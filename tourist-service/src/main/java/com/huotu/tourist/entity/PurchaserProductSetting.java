/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 采购商支付开通
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Purchaser_Product_Setting")
@Getter
@Setter
public class PurchaserProductSetting extends BaseModel {

    /**
     * 名称
     */
    @Column(length = 100)
    private String name;

    /**
     * banner图
     */
    @Column(length = 200)
    private String bannerUri;

    /**
     * 价格
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 说明文字
     */
    @Column(length = 200)
    private String explainStr;

    /**
     * 协议
     */
    @Column
    @Lob
    private String agreement;


}
