/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.service.impl;

import com.huotu.tourist.login.Login;
import com.huotu.tourist.login.PlatformManager;
import com.huotu.tourist.repository.LoginRepository;
import com.huotu.tourist.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * @author CJ
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login login = loginRepository.findByLoginName(username);
        if (login == null)
            throw new UsernameNotFoundException(username);
        return login;
    }

    @Override
    public <T extends Login> T updatePassword(T login, String rawPassword) {
        login.setPassword(passwordEncoder.encode(rawPassword));
        return loginRepository.save(login);
    }

    @PostConstruct
    @Transactional
    public void init() {
        // 建立默认管理员 该帐号只向运维人员开放
        try {
            loadUserByUsername(DefaultRootName);
        } catch (UsernameNotFoundException ex) {
            PlatformManager manager = new PlatformManager();
            manager.setEnabled(true);
            manager.setLoginName(DefaultRootName);
            manager.setAuthorityList(Collections.singleton("ROOT"));
            updatePassword(manager, DefaultRootPassword);
        }
    }
}
