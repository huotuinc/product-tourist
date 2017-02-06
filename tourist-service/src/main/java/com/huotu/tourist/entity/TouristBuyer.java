/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.entity;

import com.huotu.tourist.common.BuyerCheckStateEnum;
import com.huotu.tourist.common.BuyerPayStateEnum;
import com.huotu.tourist.common.PayTypeEnum;
import com.huotu.tourist.login.SystemUser;
import com.huotu.tourist.model.Selection;
import com.huotu.tourist.model.SimpleSelection;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 线路采购商，可以是一个未录入采购商数据或者未被批准的采购商
 * Created by lhx on 2016/12/16.
 */
@Entity
@Table(name = "Tourist_Buyer")
@Getter
@Setter
public class TouristBuyer implements SystemUser, UserDetails {

    /**
     * 作为一个采购商，它是来自一个已登录的小伙伴；即他的id是已知的，应该属于分配值
     */
    @Id
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
     * 冗余字段
     */
    @Column
    private BuyerPayStateEnum payState;
    /**
     * 支付类型
     */
    @Column
    private PayTypeEnum payType;
    /**
     * 最后一个意图购买资格的商城订单号
     */
    @Column(length = 50)
    private String lastQualificationMallTradeId;
    /**
     * 身份证号
     */
    @Column(length = 18)
    private String IDNo;
    /**
     * 商城订单号
     */
    @Column(unique = true, length = 50)
    private String mallOrderNo;

    public static final List<Selection<TouristBuyer, ?>> getSelections(ResourceService resourceService) {
        return Arrays.asList(
                new SimpleSelection<TouristBuyer, String>("id", "id"),
                new SimpleSelection<TouristBuyer, String>("buyerName", "buyerName")
                , new SimpleSelection<TouristBuyer, String>("buyerDirector", "buyerDirector")
                , new SimpleSelection<TouristBuyer, String>("telPhone", "telPhone")
                , new Selection<TouristBuyer, String>() {
                    @Override
                    public String getName() {
                        return "businessLicencesUri";
                    }

                    @Override
                    @SneakyThrows(IOException.class)
                    public String apply(TouristBuyer buyer) {
                        return buyer.getBusinessLicencesUri() == null ?"" : resourceService.getResource(buyer
                                .getBusinessLicencesUri()).httpUrl().toString() ;
                    }
                }
                , new SimpleSelection<TouristBuyer, String>("nickname", "nickname")
                , new SimpleSelection<TouristBuyer, String>("IDNo", "IDNo")
                , new Selection<TouristBuyer, Map>() {
                    @Override
                    @SneakyThrows(IOException.class)
                    public Map apply(TouristBuyer touristBuyer) {
                        Map<String, String> map = new HashMap<>();
                        map.put("IDElevationsUri", touristBuyer.getIDElevationsUri() == null ? "" : resourceService
                                .getResource
                                        (touristBuyer
                                                .getIDElevationsUri())
                                .httpUrl().toString());
                        map.put("IDInverseUri", touristBuyer.getIDInverseUri() == null ? "" : resourceService.getResource(touristBuyer.getIDInverseUri())
                                .httpUrl().toString());
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
                        Map<String, String> map = new HashMap<>();
                        if (touristBuyer.getCheckState() == null) {
                            map.put("code", null);
                            map.put("value", null);
                        }else {
                            map.put("code", touristBuyer.checkState.getCode().toString());
                            map.put("value", touristBuyer.checkState.getValue().toString());
                        }
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
                        Map<String, String> map = new HashMap<>();
                        if (touristBuyer.getPayState() == null) {
                            map.put("code", null);
                            map.put("value", null);
                        }else {
                            map.put("code", touristBuyer.payState.getCode().toString());
                            map.put("value", touristBuyer.payState.getValue().toString());
                        }
                        return map;
                    }
                }
        );
    }

    /**
     * 检查一个采购商是否已得到认可
     *
     * @param buyer 采购商
     * @return XX
     */
    public static boolean isRealBuyer(TouristBuyer buyer) {
        return buyer.getCheckState() == BuyerCheckStateEnum.CheckFinish
                && buyer.getPayState() == BuyerPayStateEnum.PayFinish;
    }

    /**
     * 检查一个采购商是否已得到认可
     *
     * @param buyerPath 采购商的查询路径
     * @param builder   cb
     * @return 一个JPA谓语
     */
    public static Predicate isRealBuyer(Path<? extends TouristBuyer> buyerPath, CriteriaBuilder builder) {
        return (Predicate) builder.and(
                builder.equal(buyerPath.get("checkState"), BuyerCheckStateEnum.CheckFinish)
                , builder.equal(buyerPath.get("payState"), BuyerPayStateEnum.PayFinish)
        );
    }

    public boolean isNullResource() {
        return this.getBusinessLicencesUri() == null || getIDElevationsUri() == null || getIDInverseUri() == null;
    }

    @Override
    public boolean isSupplier() {
        return false;
    }

    @Override
    public boolean isPlatformUser() {
        return false;
    }

    public boolean isBuyer() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        if (isRealBuyer(this)) {
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_BUYER"));
//        } else {
//            return null;
//        }
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
