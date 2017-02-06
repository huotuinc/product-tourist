/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.tourist.login;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author CJ
 */
@Entity
@Setter
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "loginName")})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Login implements UserDetails, SystemUser {
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
     * 是否已删除
     */
    @Column(insertable = false)
    private boolean deleted;

    @Column(length = 20)
    private String loginName;
    private String password;
    private boolean enabled;

    @Override
    public String getUsername() {
        return getLoginName();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return Objects.equals(id, login.id) &&
                Objects.equals(createTime, login.createTime) &&
                Objects.equals(updateTime, login.updateTime) &&
                Objects.equals(deleted, login.deleted) &&
                Objects.equals(loginName, login.loginName) &&
                Objects.equals(password, login.password) &&
                Objects.equals(enabled, login.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createTime, updateTime, deleted, loginName, password, enabled);
    }
}
