/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.controller;

import com.huotu.tourist.WebTest;
import com.huotu.tourist.service.LoginService;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * 执行登录测试
 *
 * @author CJ
 */
public class LoginControllerTest extends WebTest {

    @Test
    public void go() throws Exception {
        mvcLogin(null, LoginService.DefaultRootName, LoginService.DefaultRootPassword);

    }

    @Test
    public void loginTest() throws Exception{
        String json=mockMvc.perform(get("/login"))
                .andReturn().getResponse().getContentAsString();
    }

}