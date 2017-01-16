/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 平台管理员
 *  ROLE_MA 平台管理员
 * @author CJ
 */
@Entity
@Setter
@Getter
public class PlatformManager extends Login {

    @ElementCollection
    @Column(length = 50)
    private Set<String> authorityList;

    private static Set<SimpleGrantedAuthority> String2GrantedAuthoritySet(Stream<String> input) {
        return input.map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    /**
     * 作为默认权限 ROLE_USER 都将被加入。
     *
     * @return 权限表
     */
    @Transient
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorityList == null || authorityList.isEmpty())
            return String2GrantedAuthoritySet(Stream.of("ROLE_USER"));
        if (!authorityList.contains("ROLE_USER"))
            authorityList.add("ROLE_USER");
        if (!authorityList.contains("ROLE_MA"))
            authorityList.add("ROLE_MA");
        return String2GrantedAuthoritySet(authorityList.stream());
    }

    @Override
    public boolean isSupplier() {
        return false;
    }

    @Override
    public boolean isPlatformUser() {
        return true;
    }
}
