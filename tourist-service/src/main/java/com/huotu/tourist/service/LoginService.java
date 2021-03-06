/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service;

import com.huotu.tourist.login.Login;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author CJ
 */
public interface LoginService extends UserDetailsService {

    String DefaultRootName = "admin";
    String DefaultRootPassword = "adminIsAdmin";

    /**
     * 更新密码
     *
     * @param login       身份
     * @param rawPassword 明文密码
     * @param <T>         该主体类型
     * @return JPA实例
     */
    <T extends Login> T updatePassword(T login, String rawPassword);

    /**
     * 保存登录用户
     * @param login     登录用户
     * @param password  密码
     * @return
     */
    Login saveLogin(Login login, String password);


    /**
     * 删除的Login ID
     * @param id
     */
    @Transactional
    void delLogin(Long id);


}
