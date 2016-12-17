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
     * 地址
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
