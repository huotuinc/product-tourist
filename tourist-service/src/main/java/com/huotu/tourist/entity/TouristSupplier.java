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

/**
 * 线路供应商
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Tourist_Supplier")
@Getter
@Setter
public class TouristSupplier extends BaseModel {

    /**
     * 管理员账户名
     */
    @Column(unique = true, length = 50)
    private String adminAccount;

    /**
     * 管理员密码
     */
    @Column(length = 50)
    private String adminPassword;
    /**
     * 供应商名称
     */
    @Column(length = 50)
    private String supplierName;

    /**
     * 供应商所在地址 省，市，区
     */
    @Column(length = 100)
    private String supplierPlaceAddress;

    /**
     * 联系人
     */
    @Column(length = 20)
    private String contacts;

    /**
     * 联系电话
     */
    @Column(length = 15)
    private String contactNumber;

    /**
     * 营业执照uri
     */
    @Column(length = 20)
    private String businessLicenseUri;

    /**
     * 备注
     */
    @Lob
    @Column
    private String remarks;


}
