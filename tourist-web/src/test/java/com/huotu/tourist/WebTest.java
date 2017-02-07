/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist;

import com.huotu.tourist.config.MVCConfig;
import com.huotu.tourist.page.LoginPage;
import me.jiangcai.dating.ServiceBaseTest;
import me.jiangcai.lib.test.page.AbstractPage;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 常用web测试基类
 */
@SuppressWarnings("ALL")
@WebAppConfiguration
@ContextConfiguration(classes = {MVCConfig.class})
public abstract class WebTest extends ServiceBaseTest {

    /**
     * 页面级别的登录,在此之前需要执行 {@link org.openqa.selenium.WebDriver#get(String)}
     * 如果发现需要登录，则会完成其余步骤，并且自动跳转回原地址
     *
     * @param clazz    返回页面的类型
     * @param username
     * @param password
     * @param <T>
     * @return 新的页面实例
     */
    protected <T extends AbstractPage> T pageOrLogin(Class<T> clazz, String username, String password) {
        try {
            return initPage(clazz);
        } catch (Throwable ex) {
            LoginPage loginPage = initPage(LoginPage.class);
            loginPage.login(username, password);
            return initPage(clazz);
        }
    }

    /**
     * MVC登录
     *
     * @param uri      尝试浏览的uri
     * @param username 用户
     * @param password 密码
     * @return 获取session
     * @throws Exception
     */
    protected MockHttpSession mvcLogin(String uri, String username, String password) throws Exception {
        if (uri == null)
            uri = "/distributionPlatform/";
        MockHttpSession session = (MockHttpSession) mockMvc.perform(get(uri))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andReturn().getRequest().getSession();

        // 执行登录请求
        mockMvc.perform(post("/auth")
                .param("username", username)
                .param("password", password).session(session))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isFound())
                .andExpect(header().string("location", "http://localhost:80" + uri));

        return session;
    }
}
