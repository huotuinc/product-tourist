/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

package com.huotu.tourist.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 登录页面
 * login.html
 *
 * @author CJ
 */
public class LoginPage extends AbstractTouristPage {
    private WebElement username;
    private WebElement password;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void validatePage() {
        assertThat(webDriver.getTitle())
                .isEqualToIgnoringCase("后台登录");
    }

    public void login(String loginName, String rawPassword) {
        username.clear();
        username.sendKeys(loginName);
        password.clear();
        password.sendKeys(rawPassword);
        webDriver.findElement(By.tagName("form")).submit();
    }
}
