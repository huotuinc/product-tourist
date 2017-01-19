/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2015. All rights reserved.
 */

package com.huotu.tourist.common;

import com.huotu.tourist.entity.TouristBuyer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 采购商
 * @author slt
 */
public class BuyerAuthentication implements Authentication {

    private final TouristBuyer touristBuyer;

    public BuyerAuthentication(TouristBuyer touristBuyer) {
        this.touristBuyer = touristBuyer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return touristBuyer.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return touristBuyer;
    }

    @Override
    public Object getPrincipal() {
        return touristBuyer;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
    }

    @Override
    public String getName() {
        return touristBuyer.getUsername();
    }
}
