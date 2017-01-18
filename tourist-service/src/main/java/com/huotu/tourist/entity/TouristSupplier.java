/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huotu.tourist.converter.LocalDateTimeFormatter;
import com.huotu.tourist.login.Login;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 线路供应商
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Tourist_Supplier")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TouristSupplier extends Login implements SystemUser {

    public static final List<Selection<TouristSupplier, ?>> selections = Arrays.asList(
            new SimpleSelection<TouristSupplier, String>("id", "id"),
            new SimpleSelection<TouristSupplier, String>("supplierName", "supplierName")
            , new SimpleSelection<TouristSupplier, String>("loginName", "loginName")
            , new Selection<TouristSupplier, String>() {
                @Override
                public String apply(TouristSupplier touristSupplier) {
                    if (touristSupplier.getAddress() == null) {
                        return "";
                    }
                    return touristSupplier.getAddress().getProvince() + touristSupplier.getAddress().getTown()
                            + touristSupplier.getAddress().getDistrict();
                }

                @Override
                public String getName() {
                    return "address";
                }
            }
            , new SimpleSelection<TouristSupplier, String>("contacts", "contacts")
            , new SimpleSelection<TouristSupplier, String>("contactNumber", "contactNumber")
            , new SimpleSelection<TouristSupplier, String>("operatorName","operatorName")
            , new SimpleSelection<TouristSupplier, String>("operatorTel","operatorTel")
            , new SimpleSelection<TouristSupplier, String>("frozen", "frozen")
            , new Selection<TouristSupplier, String>() {
                @Override
                public String apply(TouristSupplier touristSupplier) {
                    return LocalDateTimeFormatter.toStr(touristSupplier.getCreateTime());
                }

                @Override
                public String getName() {
                    return "createTime";
                }
            }
    );

    @ManyToOne
    private TouristSupplier authSupplier;

    /**
     * 供应商名称
     */
    @Column(length = 50)
    private String supplierName;

    /**
     * 所在地址
     */
    @Embedded
    private Address address;

    /**
     * 详细地址
     */
    @Column(length = 100)
    private String detailedAddress;

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
    @Column(length = 200)
    private String businessLicenseUri;
    /**
     * 备注
     */
    @Lob
    @Column
    private String remarks;

    /**
     * 结算余额
     * 当前余额 = 结算余额+所有已确认结算单总额-所有未被拒绝的提现订单总额
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal settlementBalance = BigDecimal.ZERO;

    /**
     * 冻结
     */
    @Column(insertable = false)
    private boolean frozen;

    /**
     * 操作员姓名
     */
    @Column(length = 30)
    private String operatorName;

    /**
     * 联系手机
     */
    @Column(length = 11)
    private String operatorTel;


    /**
     * 权限列表
     */
    @ElementCollection
    @Column(length = 50)
    private Set<String> authorityList=new HashSet<>();

    /**
     * 获取操作员，如果为null,则代表自己是供应商
     * @return
     */
    public TouristSupplier getAuthSupplier(){
        return authSupplier==null?this:authSupplier;
    }

    @Override
    public boolean isSupplier() {
        return true;
    }

    @Override
    public boolean isPlatformUser() {
        return false;
    }

    private static Set<SimpleGrantedAuthority> String2GrantedAuthoritySet(Stream<String> input) {
        return input.map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    /**
     * 作为默认权限 ROLE_OPERATOR 都将被加入。
     *
     * @return 权限表
     */
    @Transient
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorityList == null || authorityList.isEmpty())
            return String2GrantedAuthoritySet(Stream.of("ROLE_SUPPLIER"));
        if (!authorityList.contains("ROLE_SUPPLIER"))
            authorityList.add("ROLE_SUPPLIER");
        return String2GrantedAuthoritySet(authorityList.stream());
    }
}
