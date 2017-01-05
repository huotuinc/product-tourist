/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist;

import com.huotu.tourist.entity.TouristSupplier;
import com.huotu.tourist.page.supplier.SupplierMainPage;
import com.huotu.tourist.service.LoginService;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 预约好身份的单元测试，在这个测试中已经预先建立了一个供应商；并且处于登录状态
 *
 * @author lhx
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractSupplierTest extends WebTest {

    /**
     * 当前供应商
     */
    protected TouristSupplier supplier;
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
        supplier = new TouristSupplier();
        supplier.setEnabled(true);
        supplier.setLoginName(RandomStringUtils.randomAlphanumeric(20));
        supplier.setCreateTime(LocalDateTime.now());
        supplier = loginService.updatePassword(supplier, rawPassword);

        session = mvcLogin(null, supplier.getLoginName(), rawPassword);

        driver.get("http://localhost/supplier/");
        pageOrLogin(SupplierMainPage.class, supplier.getLoginName(), rawPassword);
    }


}
