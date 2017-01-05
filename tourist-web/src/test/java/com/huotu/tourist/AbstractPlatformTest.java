/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist;

import com.huotu.tourist.login.PlatformManager;
import com.huotu.tourist.page.distributionPlatform.PlatformMainPage;
import com.huotu.tourist.service.LoginService;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 预约好身份的单元测试，在这个测试中已经预先建立了平台管理员；并且处于登录状态
 *
 * @author CJ
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractPlatformTest extends WebTest {

    /**
     * 当前平台管理员
     */
    protected PlatformManager manager;

    /**
     * 明文密码
     */
    protected String rawPassword;
    /**
     * 已登录的mvc session
     */
    protected MockHttpSession session;
    @Autowired
    private LoginService loginService;

    @Before
    public void beforeTest() throws Exception {
        rawPassword = UUID.randomUUID().toString();
        manager = new PlatformManager();
        manager.setEnabled(true);
        manager.setLoginName(RandomStringUtils.randomAlphanumeric(20));
        manager.setCreateTime(LocalDateTime.now());
        Set<String> authorityList = new HashSet<>();
        authorityList.add("ROLE_MA");
        manager.setAuthorityList(authorityList);
        manager = loginService.updatePassword(manager, rawPassword);
        session = mvcLogin(null, manager.getLoginName(), rawPassword);

        driver.get("http://localhost/distributionPlatform/");
        pageOrLogin(PlatformMainPage.class, manager.getLoginName(), rawPassword);
    }


}
